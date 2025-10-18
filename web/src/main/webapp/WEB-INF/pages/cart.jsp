<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Phones</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="${pageContext.servletContext.contextPath}/scripts/deleteFromCart.js"></script>
</head>
<body data-context-path="${pageContext.servletContext.contextPath}">

<tags:header cart="${cartTotals}"/>

<main class="container my-4">
    <a href="${pageContext.servletContext.contextPath}"
       class="btn btn-light border text-decoration-none">
        Back to product list
    </a>
    <span id="delete-from-cart-msg" class="d-block mb-3"></span>

    <div class="table-responsive">
        <form:form action="${pageContext.servletContext.contextPath}/cart" method="post"
                   style="display: inline;" modelAttribute="cartUpdateForm">
            <table class="table table-bordered">
                <thead class="table-light">
                <tr>
                    <th class="align-middle">Image</th>
                    <th class="align-middle">Brand</th>
                    <th class="align-middle">Model</th>
                    <th class="align-middle">Color</th>
                    <th class="align-middle">Display size</th>
                    <th class="align-middle">Price</th>
                    <th class="align-middle">Quantity</th>
                    <th class="align-middle">Action</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="item" items="${cart.cartItems}">
                    <tr>
                        <td class="text-center align-middle">
                            <img src="https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/${item.phone.imageUrl}"
                                 alt="${item.phone.model}" style="width: 60px;">
                        </td>
                        <td class="align-middle">
                                ${item.phone.brand}
                        </td>
                        <td class="align-middle">
                            <a href="productDetails/${item.phone.id}">${item.phone.model}</a>
                        </td>
                        <td class="align-middle">
                            <c:forEach var="color" items="${item.phone.colors}" varStatus="status">
                                ${color.code}${not status.last ? ', ' : ''}
                            </c:forEach>
                        </td>
                        <td class="align-middle">${item.phone.displaySizeInches}"</td>
                        <td class="align-middle">
                            <fmt:formatNumber value="${item.phone.price}" type="currency" currencySymbol="$"/>
                        </td>
                        <td class="align-middle">
                            <form:input path="items[${item.phone.id}]" class="form-control quantity-input"
                                        value="${not empty updateErrors[item.phone.id]
                                        ? updateErrors[item.phone.id].enteredValue : item.quantity}"
                                        style="width: 70px;"/>
                            <c:if test="${not empty updateErrors[item.phone.id]}">
                                <p class="error-message text-danger small">${updateErrors[item.phone.id].message}</p>
                            </c:if>
                        </td>
                        <td class="text-center align-middle">
                            <button type="button" class="btn btn-light border delete-from-cart-btn"
                                    data-phone-id="${item.phone.id}">
                                Delete
                            </button>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <input type="hidden" name="_method" value="PUT"/>
            <div class="d-flex justify-content-end mt-3">
                <button type="submit" class="btn btn-light border" style="width: 120px;">UPDATE</button>
            </div>
        </form:form>
    </div>
</main>
</body>
</html>