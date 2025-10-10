package com.es.core.dao;

import com.es.core.enums.SortCriteria;
import com.es.core.enums.SortOrder;
import com.es.core.model.Color;
import com.es.core.model.Phone;
import com.es.core.model.PhoneListItem;
import com.es.core.util.PhoneRowMapper;
import com.es.core.util.SqlUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class JdbcPhoneDao implements PhoneDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsertPhone;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final PhoneRowMapper phoneRowMapper;

    public JdbcPhoneDao(JdbcTemplate jdbcTemplate, PhoneRowMapper phoneRowMapper,
                        SimpleJdbcInsert jdbcInsertPhone,
                        NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsertPhone = jdbcInsertPhone;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.phoneRowMapper = phoneRowMapper;
    }

    public Optional<Phone> get(final Long key) {
        String sql = SqlUtils.Phone.GET_BY_ID_WITH_COLORS_QUERY;

        List<Phone> phones = jdbcTemplate.query(sql, phoneRowMapper, key);
        Optional<Phone> result = phones.stream().findFirst();

        if (result.isPresent()) {
            Set<Color> colors = new HashSet<>();
            phones.forEach(phone -> colors.addAll(phone.getColors()));
            result.get().setColors(colors);
        }

        return result;
    }

    public Long save(final Phone phone) {
        return phone.getId() == null ? insertPhone(phone) : updatePhone(phone);
    }

    public List<PhoneListItem> findAll(SortCriteria sortCriteria, SortOrder sortOrder) {
        String sql = SqlUtils.Phone.FIND_ALL_QUERY;

        if (!SortCriteria.QUERY.equals(sortCriteria)) {
            sql = String.format("%s order by %s %s", SqlUtils.Phone.FIND_ALL_QUERY,
                    sortCriteria.getDbColumnName(), sortOrder);
        }

        return jdbcTemplate.query(sql, new BeanPropertyRowMapper(PhoneListItem.class));
    }

    private Long updatePhone(Phone phone) {
        if (get(phone.getId()).isEmpty()) {
            return insertPhone(phone);
        }

        String updateSql = SqlUtils.Phone.UPDATE_QUERY;

        SqlParameterSource params = new BeanPropertySqlParameterSource(phone);
        namedParameterJdbcTemplate.update(updateSql, params);

        return phone.getId();
    }

    private Long insertPhone(Phone phone) {
        SqlParameterSource parameters = new BeanPropertySqlParameterSource(phone);
        return jdbcInsertPhone.executeAndReturnKey(parameters).longValue();
    }

    @Transactional
    public void savePhoneColorRelations(List<Long> colors, Long phoneId) {
        if (colors == null || colors.isEmpty()) {
            return;
        }
        deletePhoneColorRelations(phoneId);

        String insertSql = SqlUtils.Phone.INSERT_PHONE_COLOR_RELATIONS_QUERY;
        jdbcTemplate.batchUpdate(insertSql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, phoneId);
                ps.setLong(2, colors.get(i));
            }

            @Override
            public int getBatchSize() {
                return colors.size();
            }
        });
    }

    private void deletePhoneColorRelations(Long phoneId) {
        String deleteSql = SqlUtils.Phone.DELETE_PHONE_COLOR_RELATIONS_QUERY;
        jdbcTemplate.update(deleteSql, phoneId);
    }

}
