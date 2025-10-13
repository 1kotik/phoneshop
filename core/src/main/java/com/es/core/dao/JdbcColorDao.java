package com.es.core.dao;

import com.es.core.model.Color;
import com.es.core.model.ColorWithPhoneId;
import com.es.core.util.CustomStringUtils;
import com.es.core.util.SqlUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Component
public class JdbcColorDao implements ColorDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsertColor;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public JdbcColorDao(JdbcTemplate jdbcTemplate, SimpleJdbcInsert jdbcInsertColor,
                        NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsertColor = jdbcInsertColor;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Transactional
    public List<Long> saveAll(List<Color> colors) {
        if (colors == null || colors.isEmpty()) {
            return new ArrayList<>();
        }

        List<Long> colorIds = colors.stream()
                .map(Color::getId)
                .filter(Objects::nonNull)
                .toList();
        SqlParameterSource params = new MapSqlParameterSource(SqlUtils.ID_SET, colorIds);
        List<Color> existentColors = colorIds.isEmpty() ? new ArrayList<>()
                : namedParameterJdbcTemplate.query(SqlUtils.Color.SELECT_BY_ID_SET_QUERY,
                params, new BeanPropertyRowMapper(Color.class));
        List<Color> colorsToInsert = colors.stream()
                .filter(color -> existentColors.stream()
                        .noneMatch(existentColor -> existentColor.getId().equals(color.getId())))
                .toList();
        List<Color> colorsToUpdate = colors.stream()
                .filter(color -> existentColors.stream()
                        .anyMatch(existentColor -> existentColor.getId().equals(color.getId())))
                .toList();

        List<Long> insertedColorIds = insertColors(colorsToInsert);
        List<Long> updatedColorIds = updateColors(colorsToUpdate);
        insertedColorIds.addAll(updatedColorIds);

        return insertedColorIds;
    }

    public List<Color> findAll() {
        String sql = SqlUtils.Color.FIND_ALL_QUERY;
        List<Color> colors = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Color.class));
        return colors;
    }

    public Map<Long, Set<Color>> findColorsByPhoneIds(Set<Long> phoneIds) {
        if (phoneIds == null || phoneIds.isEmpty()) {
            return Collections.emptyMap();
        }

        SqlParameterSource params = new MapSqlParameterSource(SqlUtils.ID_SET, phoneIds);
        List<ColorWithPhoneId> colors = namedParameterJdbcTemplate
                .query(SqlUtils.Color.JOIN_WITH_PHONE_COLOR_RELATIONS_TABLE,
                params, new BeanPropertyRowMapper<>(ColorWithPhoneId.class));

        Map<Long, Set<Color>> colorMap = new HashMap<>();
        colors.forEach(color -> colorMap.computeIfAbsent(color.getPhoneId(), key -> new HashSet<>())
                .add(new Color(color.getId(), color.getCode())));

        return colorMap;
    }

    private List<Long> insertColors(List<Color> colors) {
        if (colors == null || colors.isEmpty()) {
            return new ArrayList<>();
        }

        List<Long> generatedColorIds = new ArrayList<>();
        for (Color color : colors) {
            SqlParameterSource parameters = new BeanPropertySqlParameterSource(color);
            Long id = jdbcInsertColor.executeAndReturnKey(parameters).longValue();
            generatedColorIds.add(id);
        }

        return generatedColorIds;
    }

    private List<Long> updateColors(List<Color> colors) {
        if (colors == null || colors.isEmpty()) {
            return new ArrayList<>();
        }

        String updateSql = SqlUtils.Color.UPDATE_QUERY;

        jdbcTemplate.batchUpdate(updateSql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, colors.get(i).getCode());
                ps.setLong(2, colors.get(i).getId());
            }

            @Override
            public int getBatchSize() {
                return colors.size();
            }
        });

        return colors.stream()
                .map(Color::getId)
                .toList();
    }
}
