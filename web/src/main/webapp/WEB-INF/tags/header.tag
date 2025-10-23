<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@tag trimDirectiveWhitespaces="true" %>
<%@attribute name="cart" type="com.es.core.model.CartTotals" required="true" %>

<header>
    <nav class="navbar navbar-expand-lg navbar-light bg-white border-bottom">
        <div class="container">
            <a class="navbar-brand fs-2 fw-bold" href="${pageContext.servletContext.contextPath}">
                <i class="bi bi-phone"></i> Phonify
            </a>
            <div class="d-flex align-items-center">
                <div class="ms-3">
                    <a href="${pageContext.servletContext.contextPath}/cart"
                       class="btn btn-light border text-decoration-none">
                        My cart:
                        <span id="total-items">${not empty cart ? cart.totalQuantity : 0} items</span>
                        <span id="total-price">
                            <fmt:formatNumber value="${not empty cart ? cart.totalPrice : 0}"
                                              type="currency" currencySymbol="$"/>
                        </span>
                    </a>
                </div>
            </div>
        </div>
    </nav>
</header>
