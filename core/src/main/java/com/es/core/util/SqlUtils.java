package com.es.core.util;

public class SqlUtils {
    private SqlUtils() {
    }

    public static final String ID_SET = "ids";

    public static class Color {
        private Color() {}

        public static final String TABLE_NAME = "colors";
        public static final String COLOR_ID = "id";
        public static final String SELECT_BY_ID_SET_QUERY = String.format(
                "select * from %s where %s in (:%s)", TABLE_NAME, COLOR_ID, ID_SET);

        public static final String UPDATE_QUERY = String.format(
                "update %s set code = ? where %s = ?", TABLE_NAME, COLOR_ID);

        public static final String FIND_ALL_QUERY = String.format("select * from %s", TABLE_NAME);

        public static final String JOIN_WITH_PHONE_COLOR_RELATIONS_TABLE = String.format("""
                select c.%s, c.code, pc.%s from %s c inner join %s pc on c.%s = pc.%s
                where pc.%s in (:%s)""",
                COLOR_ID, Phone.PHONES_COLORS_RELATIONS_PHONE_ID, TABLE_NAME, Phone.PHONES_COLORS_RELATIONS_TABLE_NAME,
                COLOR_ID, Phone.PHONES_COLORS_RELATIONS_COLOR_ID, Phone.PHONES_COLORS_RELATIONS_PHONE_ID, ID_SET);
    }

    public static class Stock {
        private Stock() {}
        public static final String TABLE_NAME = "stocks";
        public static final String PHONE_ID = "phoneId";
        public static final String FIND_BY_PHONE_ID_QUERY = String.format("""
                select * from %s where %s = ?""", TABLE_NAME, PHONE_ID);
        public static final String UPDATE_STOCK_QUERY = String.format("""
                update %s set stock = :stock, reserved = :reserved where %s = :phoneId""",
                TABLE_NAME, PHONE_ID);
    }

    public static class Phone {
        private Phone() {}
        public static final String TABLE_NAME = "phones";
        public static final String PHONE_ID = "id";
        public static final String PHONES_COLORS_RELATIONS_PHONE_ID = "phoneId";
        public static final String PHONES_COLORS_RELATIONS_COLOR_ID = "colorId";
        public static final String PHONES_COLORS_RELATIONS_TABLE_NAME = "phone2color";

        public static final String GET_BY_ID_WITH_COLORS_QUERY = String.format("""
                select p.*, c.id as colorId, c.code as colorCode from %s p
                left join %s pc on pc.%s = p.%s
                left join %s c on c.%s = pc.%s
                where p.%s = ?""", TABLE_NAME, PHONES_COLORS_RELATIONS_TABLE_NAME, PHONES_COLORS_RELATIONS_PHONE_ID,
                PHONE_ID, Color.TABLE_NAME, Color.COLOR_ID, PHONES_COLORS_RELATIONS_COLOR_ID, PHONE_ID);

        public static final String UPDATE_QUERY = String.format("""
                update %s set
                brand = :brand, model = :model, price = :price, displaySizeInches = :displaySizeInches,
                weightGr = :weightGr, lengthMm = :lengthMm, widthMm = :widthMm, heightMm = :heightMm,
                announced = :announced, deviceType = :deviceType, os = :os, displayResolution = :displayResolution,
                pixelDensity = :pixelDensity, displayTechnology = :displayTechnology,
                backCameraMegapixels = :backCameraMegapixels, frontCameraMegapixels = :frontCameraMegapixels,
                ramGb = :ramGb, internalStorageGb = :internalStorageGb, batteryCapacityMah = :batteryCapacityMah,
                talkTimeHours = :talkTimeHours, standByTimeHours = :standByTimeHours, bluetooth = :bluetooth,
                positioning = :positioning, imageUrl = :imageUrl, description = :description
                where %s = :id""", TABLE_NAME, PHONE_ID);

        public static final String FIND_ALL_CONDITION = """
                       inner join %s s on s.%s = p.%s
                       where price is not null and s.stock - s.reserved > 0
                       and (:query is null or lower(p.brand) like lower(concat('%%', :query, '%%'))
                       or lower(p.model) like lower(concat('%%', :query, '%%')))""";

        public static final String FIND_ALL_QUERY = """
                       select p.%s, p.brand, p.model, p.price, p.displaySizeInches, p.imageUrl, s.stock, s.reserved from %s p\s
                       """ + FIND_ALL_CONDITION + """
                       order by %s %s
                       offset :offset limit :limit""";

        public static final String COUNT_ROWS_FIND_ALL_QUERY = "select count(*) from %s p " + FIND_ALL_CONDITION;

        public static final String DELETE_PHONE_COLOR_RELATIONS_QUERY = String.format(
                "delete from %s where %s = ?", PHONES_COLORS_RELATIONS_TABLE_NAME, PHONES_COLORS_RELATIONS_PHONE_ID);

        public static final String INSERT_PHONE_COLOR_RELATIONS_QUERY = String.format(
                "insert into %s (%s, %s) values (?, ?)",
                PHONES_COLORS_RELATIONS_TABLE_NAME, PHONES_COLORS_RELATIONS_PHONE_ID, PHONES_COLORS_RELATIONS_COLOR_ID);
    }

}
