<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@tag trimDirectiveWhitespaces="true" %>
<%@attribute name="cart" type="com.es.core.model.CartTotals" required="true" %>
<%@attribute name="isAuthenticated" type="java.lang.Boolean" required="true" %>
<%@attribute name="username" type="java.lang.String" required="true" %>

<header>
    <nav class="navbar navbar-expand-lg navbar-light bg-white border-bottom">
        <div class="container">
            <a class="navbar-brand fs-2 fw-bold" href="${pageContext.servletContext.contextPath}">
                <i class="bi bi-phone"></i> Phonify
            </a>
            <div class="d-flex align-items-center gap-2">
                <c:if test="${isAuthenticated}">
                </c:if>
                <c:if test="${not empty cart}">
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
                </c:if>
                <c:choose>
                    <c:when test="${isAuthenticated}">
                        <form method="post" action="${pageContext.servletContext.contextPath}/auth/logout">
                            <button type="submit" class="btn btn-light border text-decoration-none">
                                Logout
                            </button>
                        </form>
                        <a href="${pageContext.servletContext.contextPath}/admin/orders"
                           class="btn btn-light border text-decoration-none">
                            Admin
                        </a>
                        <span class="fw-bold">${username}</span>
                    </c:when>
                    <c:otherwise>
                        <a href="${pageContext.servletContext.contextPath}/auth/login"
                           class="btn btn-light border text-decoration-none">
                            Login
                        </a>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </nav>
</header>
