<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Orders</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</head>
<body data-context-path="${pageContext.servletContext.contextPath}">

<tags:header cart="" isAuthenticated="${isAuthenticated}" username="${username}"/>

<main class="container my-4">
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h2>Phones</h2>
    </div>

    <span id="add-to-cart-msg" class="d-block mb-3"></span>

    <div class="table-responsive">
        <table class="table table-bordered">
            <thead class="table-light">
            <tr>
                <th class="align-middle">Order number</th>
                <th class="align-middle">Customer</th>
                <th class="align-middle">Phone</th>
                <th class="align-middle">Address</th>
                <th class="align-middle">Date</th>
                <th class="align-middle">Total price</th>
                <th class="align-middle">Status</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="order" items="${orders}">
                <tr>
                    <td class="text-center align-middle">
                        <a href="${pageContext.servletContext.contextPath}/admin/orders/${order.id}">${order.id}</a>
                    </td>
                    <td class="align-middle">
                            ${order.customerFirstName} ${order.customerLastName}
                    </td>
                    <td class="align-middle">
                            ${order.contactPhoneNo}
                    </td>
                    <td class="align-middle">
                            ${order.deliveryAddress}
                    </td>
                    <td class="align-middle">
                            ${order.dateOfRegistrationFormatted}
                    </td>
                    <td class="align-middle">
                        <fmt:formatNumber value="${order.totalPrice}" type="currency" currencySymbol="$"/>
                    </td>
                    <td class="align-middle">
                            ${order.status.value}
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</main>
</body>
</html>