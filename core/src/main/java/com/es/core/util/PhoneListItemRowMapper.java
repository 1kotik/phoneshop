package com.es.core.util;

import com.es.core.model.Color;
import com.es.core.model.Phone;
import com.es.core.model.PhoneListItem;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

public class PhoneListItemRowMapper implements RowMapper<PhoneListItem> {
    @Override
    public PhoneListItem mapRow(ResultSet rs, int rowNum) throws SQLException {
        PhoneListItem phone = new PhoneListItem();
        phone.setId(rs.getLong("id"));
        phone.setBrand(rs.getString("brand"));
        phone.setModel(rs.getString("model"));
        phone.setPrice(rs.getBigDecimal("price"));
        phone.setDisplaySizeInches(rs.getBigDecimal("displaySizeInches"));
        phone.setImageUrl(rs.getString("imageUrl"));
        phone.setColors(Set.of(getColor(rs)));
        return phone;
    }

    private Color getColor(ResultSet rs) throws SQLException {
        Color color = new Color();
        color.setId(rs.getLong("colorId"));
        color.setCode(rs.getString("colorCode"));
        return color;
    }
}
