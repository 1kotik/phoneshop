package com.es.core.util;

public class AppConstants {
    private AppConstants() {
    }

    public static class Pages {
        private Pages() {
        }

        public static final String CART = "cart";
        public static final String REDIRECT_CART = "redirect:/cart";
        public static final String PRODUCT_LIST = "productList";
        public static final String PRODUCT_DETAILS = "productDetails";
        public static final String ERROR = "error";
    }

    public static class ErrorMessages {
        private ErrorMessages() {
        }

        public static final String INVALID_QUANTITY = "Invalid quantity";
        public static final String INVALID_FORMAT = "Invalid format";
        public static final String INTERNAL_ERROR = "Something went wrong";
    }

    public static class PageAttributes {
        private PageAttributes() {
        }

        public static final String PHONE = "phone";
        public static final String CART = "cart";
        public static final String CART_TOTALS = "cartTotals";
        public static final String PHONE_LIST_RESPONSE = "response";
        public static final String CART_UPDATE_FORM = "cartUpdateForm";
        public static final String CART_UPDATE_ERRORS = "updateErrors";
    }
}
