package com.es.core.util;

import java.time.format.DateTimeFormatter;

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
        public static final String ORDER = "order";
        public static final String REDIRECT_ORDER_OVERVIEW = "redirect:/orderOverview";
        public static final String ORDER_OVERVIEW = "orderOverview";
        public static final String REDIRECT_ORDER = "redirect:/order";
        public static final String REDIRECT_ERROR = "redirect:/error";
        public static final String ADMIN_ORDERS = "adminOrders";
        public static final String ADMIN_ORDER_OVERVIEW = "adminOrderOverview";
    }

    public static class ErrorMessages {
        private ErrorMessages() {
        }
        public static final String ORDER_HAS_ALREADY_BEEN_PLACED = "Your order has already been placed. Return to main page";
        public static final String INVALID_QUANTITY = "Invalid quantity";
        public static final String INVALID_FORMAT = "Invalid format";
        public static final String INTERNAL_ERROR = "Something went wrong";
        public static final String CART_ITEMS_OUT_OF_STOCK = "Some items are out of stock and removed from the cart";
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
        public static final String ORDER_CUSTOMER_INFO = "orderCustomerInfo";
        public static final String ORDER = "order";
        public static final String VALIDATION_ERRORS = "validationErrors";
        public static final String ERROR = "error";
        public static final String ADMIN_ORDERS = "orders";
    }

    public static class DefaultConstants {
        private DefaultConstants() {}
        public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    }
}
