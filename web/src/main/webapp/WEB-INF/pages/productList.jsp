<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Phones</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="${pageContext.servletContext.contextPath}/scripts/addToCart.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</head>
<body>

<header>
    <nav class="navbar navbar-expand-lg navbar-light bg-white border-bottom">
        <div class="container">
            <a class="navbar-brand fs-2 fw-bold" href="${pageContext.servletContext.contextPath}">
                <i class="bi bi-phone"></i> Phonify
            </a>
            <div class="d-flex align-items-center">
                <div class="ms-3">
                    <button class="btn btn-light border">
                        My cart:
                        <span id="total-items">${not empty cart ? cart.totalQuantity : 0} items</span>
                        <span id="total-price">$${not empty cart ? cart.totalPrice : 0}</span>
                    </button>
                </div>
            </div>
        </div>
    </nav>
</header>

<main class="container my-4">
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h2>Phones</h2>
        <form id="searchForm" class="w-25">
            <div class="input-group">
                <span class="input-group-text bg-white border-end-0"><i class="bi bi-search"></i></span>
                <input class="form-control border-start-0" type="search" name="q" value="${param.q}" placeholder="Search">
            </div>
        </form>
    </div>

    <span id="add-to-cart-msg" class="d-block mb-3"></span>

    <div class="table-responsive">
        <table class="table table-bordered">
            <thead class="table-light">
            <tr>
                <th class="align-middle">Image</th>
                <th class="align-middle">Brand
                    <tags:sortQuery criteria="brand" order="asc"/>
                    <tags:sortQuery criteria="brand" order="desc"/>
                </th>
                <th class="align-middle">Model
                    <tags:sortQuery criteria="model" order="asc"/>
                    <tags:sortQuery criteria="model" order="desc"/>
                </th>
                <th class="align-middle">Color</th>
                <th class="align-middle">Display size
                    <tags:sortQuery criteria="displaySizeInches" order="asc"/>
                    <tags:sortQuery criteria="displaySizeInches" order="desc"/>
                </th>
                <th class="align-middle">Price
                    <tags:sortQuery criteria="price" order="asc"/>
                    <tags:sortQuery criteria="price" order="desc"/>
                </th>
                <th class="align-middle">Quantity</th>
                <th class="align-middle">Action</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="phone" items="${response.phones}">
                <tr>
                    <td class="text-center align-middle">
                        <img src="https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/${phone.imageUrl}" alt="${phone.model}" style="width: 60px;">
                    </td>
                    <td class="align-middle">${phone.brand}</td>
                    <td class="align-middle">${phone.model}</td>
                    <td class="align-middle">
                        <c:forEach var="color" items="${phone.colors}" varStatus="status">
                            ${color.code}${not status.last ? ', ' : ''}
                        </c:forEach>
                    </td>
                    <td class="align-middle">${phone.displaySizeInches}"</td>
                    <td class="align-middle">${phone.price}$</td>
                    <td class="align-middle">
                        <input type="text" class="form-control quantity-input" value="1" style="width: 70px;"/>
                        <p class="error-message text-danger small"></p>
                    </td>
                    <td class="text-center align-middle">
                        <button type="button" class="btn btn-light border add-to-cart-btn" data-phone-id="${phone.id}">
                            Add
                        </button>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
    <tags:pageQuery page="prev"/>
    <c:forEach var="pageNumber" begin="1" end="${response.totalPages}">
        <tags:pageQuery page="${pageNumber}"/>
    </c:forEach>
    <tags:pageQuery page="next"/>
</main>
</body>
</html>