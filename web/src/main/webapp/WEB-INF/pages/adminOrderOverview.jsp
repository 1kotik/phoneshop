<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Order Confirmation - Phonify</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="${pageContext.servletContext.contextPath}/scripts/updateOrderStatus.js"></script>
</head>
<body data-context-path="${pageContext.servletContext.contextPath}">

<tags:header cart=""/>


<main class="container my-4">
    <span id="update-order-status-msg" class="d-block mb-3"></span>
    <div class="d-flex align-items-center justify-content-between mb-2">
        <span>Order number: ${order.id}</span>
        <div>
            <span>Order status: </span>
            <span id="order-status">${order.status.value}</span>
        </div>
    </div>

    <c:if test="${not empty order.orderItems}">
        <div class="card-body">
            <div class="table-responsive">
                <table class="table table-bordered table-hover">
                    <thead class="table-light">
                    <tr>
                        <th>Brand</th>
                        <th>Model</th>
                        <th>Color</th>
                        <th>Display Size</th>
                        <th>Quantity</th>
                        <th>Price</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="item" items="${order.orderItems}" varStatus="status">
                        <tr>
                            <td class="align-middle">${item.phone.brand}</td>
                            <td class="align-middle">
                                <a href="${pageContext.servletContext.contextPath}/productDetails/${item.phone.id}"
                                   class="text-decoration-none">
                                        ${item.phone.model}
                                </a>
                            </td>
                            <td class="align-middle">
                                <c:forEach var="color" items="${item.phone.colors}" varStatus="status">
                                    <span>${color.code}${not status.last ? ', ' : ''}</span>
                                </c:forEach>
                            </td>
                            <td class="align-middle">${item.phone.displaySizeInches}"</td>
                            <td class="align-middle">${item.quantity}</td>
                            <td class="align-middle">
                                <fmt:formatNumber value="${item.phone.price}" type="currency" currencySymbol="$"/>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                    <tfoot class="table-light">
                    <tr>
                        <td colspan="3"></td>
                        <td class="fw-bold">Subtotal</td>
                        <td class="fw-bold">Delivery</td>
                        <td class="fw-bold">Total</td>
                    </tr>
                    <tr>
                        <td colspan="3"></td>
                        <td class="fw-bold">
                            <fmt:formatNumber value="${order.subtotal}" type="currency" currencySymbol="$"/>
                        </td>
                        <td class="fw-bold">
                            <fmt:formatNumber value="${order.deliveryPrice}" type="currency" currencySymbol="$"/>
                        </td>
                        <td class="fw-bold">
                            <fmt:formatNumber value="${order.totalPrice}" type="currency" currencySymbol="$"/>
                        </td>
                    </tr>
                    </tfoot>
                </table>
            </div>
        </div>

        <div class="card">
            <div class="card-body">
                <div class="row">
                    <div class="col-md-6 mb-3">
                        <strong>First name:</strong>
                        <p class="mb-0">${order.customerInfo.firstName}</p>
                    </div>
                    <div class="col-md-6 mb-3">
                        <strong>Last name:</strong>
                        <p class="mb-0">${order.customerInfo.lastName}</p>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-6 mb-3">
                        <strong>Phone:</strong>
                        <p class="mb-0">${order.customerInfo.contactPhoneNo}</p>
                    </div>
                    <div class="col-md-6 mb-3">
                        <strong>Address:</strong>
                        <p class="mb-0">${order.customerInfo.deliveryAddress}</p>
                    </div>
                </div>

                <c:if test="${not empty order.customerInfo.additionalInformation}">
                    <div class="mb-3">
                        <strong>Additional information:</strong>
                        <p class="mb-0">${order.customerInfo.additionalInformation}</p>
                    </div>
                </c:if>
            </div>
        </div>
        <div class="d-flex gap-2 mt-3">
            <a href="${pageContext.servletContext.contextPath}/admin/orders"
               class="btn btn-light border text-decoration-none">
                Back
            </a>
            <button type="button" class="btn btn-light border update-order-status-btn"
                    data-order-id="${order.id}"
                    data-order-status="Delivered">
                Delivered
            </button>
            <button type="button" class="btn btn-light border update-order-status-btn"
                    data-order-id="${order.id}"
                    data-order-status="Rejected">
                Rejected
            </button>
        </div>
    </c:if>
</main>
</body>
</html>