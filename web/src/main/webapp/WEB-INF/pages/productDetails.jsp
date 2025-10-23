<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${phone.brand} ${phone.model}</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="${pageContext.servletContext.contextPath}/scripts/addToCart.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</head>
<body data-context-path="${pageContext.servletContext.contextPath}">

<tags:header cart="${cartTotals}"/>

<main class="container my-4">
    <a href="${pageContext.servletContext.contextPath}"
       class="btn btn-light border text-decoration-none">
        Back to product list
    </a>
    <span id="add-to-cart-msg" class="d-block mb-3"></span>

    <div class="row mb-4">
        <div class="col-12">
            <h1 class="display-5 fw-bold">${phone.brand} ${phone.model}</h1>
            <c:if test="${not empty phone.description}">
                <p class="lead text-muted">${phone.description}</p>
            </c:if>
            <hr class="my-3">
        </div>
    </div>

    <div class="row">
        <div class="col-md-4 mb-4">
            <div class="card border-0 bg-light">
                <div class="card-body text-center">
                    <img src="https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/${phone.imageUrl}"
                         alt="${phone.model}" class="img-fluid mb-3" style="max-height: 300px;">

                    <h3 class="text-primary mb-3">
                        <fmt:formatNumber value="${phone.price}" type="currency" currencySymbol="$"/>
                    </h3>

                    <div class="d-flex align-items-center justify-content-center gap-2 mb-3">
                        <input id="quantity-input" type="text" class="form-control" value="1" style="width: 80px;">
                        <button type="button" class="btn btn-primary add-to-cart-btn" data-phone-id="${phone.id}">
                            Add
                        </button>
                    </div>
                    <p id="error-message" class="text-danger small mb-0"></p>
                </div>
            </div>
        </div>

        <div class="col-md-8">
            <div class="card mb-4">
                <div class="card-header bg-light">
                    <h5 class="card-title mb-0">Display</h5>
                </div>
                <div class="card-body">
                    <div class="table-responsive">
                        <table class="table table-bordered mb-0">
                            <tbody>
                            <tr>
                                <td class="fw-bold" style="width: 30%;">Size</td>
                                <td>${phone.displaySizeInches}"</td>
                            </tr>
                            <c:if test="${not empty phone.displayResolution}">
                                <tr>
                                    <td class="fw-bold">Resolution</td>
                                    <td>${phone.displayResolution}</td>
                                </tr>
                            </c:if>
                            <c:if test="${not empty phone.displayTechnology}">
                                <tr>
                                    <td class="fw-bold">Technology</td>
                                    <td>${phone.displayTechnology}</td>
                                </tr>
                            </c:if>
                            <c:if test="${not empty phone.pixelDensity}">
                                <tr>
                                    <td class="fw-bold">Pixel density</td>
                                    <td>${phone.pixelDensity}</td>
                                </tr>
                            </c:if>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>

            <div class="card mb-4">
                <div class="card-header bg-light">
                    <h5 class="card-title mb-0">Dimensions & Weight</h5>
                </div>
                <div class="card-body">
                    <div class="table-responsive">
                        <table class="table table-bordered mb-0">
                            <tbody>
                            <c:if test="${not empty phone.lengthMm}">
                                <tr>
                                    <td class="fw-bold" style="width: 30%;">Length</td>
                                    <td>${phone.lengthMm}mm</td>
                                </tr>
                            </c:if>
                            <c:if test="${not empty phone.widthMm}">
                                <tr>
                                    <td class="fw-bold">Width</td>
                                    <td>${phone.widthMm}mm</td>
                                </tr>
                            </c:if>
                            <c:if test="${not empty phone.heightMm}">
                                <tr>
                                    <td class="fw-bold">Height</td>
                                    <td>${phone.heightMm}mm</td>
                                </tr>
                            </c:if>
                            <c:if test="${not empty phone.weightGr}">
                                <tr>
                                    <td class="fw-bold">Weight</td>
                                    <td>${phone.weightGr}g</td>
                                </tr>
                            </c:if>
                            <c:if test="${not empty phone.colors}">
                                <tr>
                                    <td class="fw-bold">Colors</td>
                                    <td>
                                        <c:forEach var="color" items="${phone.colors}" varStatus="status">
                                            ${color.code}${not status.last ? ', ' : ''}
                                        </c:forEach>
                                    </td>
                                </tr>
                            </c:if>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>

            <div class="card mb-4">
                <div class="card-header bg-light">
                    <h5 class="card-title mb-0">Camera</h5>
                </div>
                <div class="card-body">
                    <div class="table-responsive">
                        <table class="table table-bordered mb-0">
                            <tbody>
                            <c:if test="${not empty phone.backCameraMegapixels}">
                                <tr>
                                    <td class="fw-bold" style="width: 30%;">Back</td>
                                    <td>${phone.backCameraMegapixels} megapixels</td>
                                </tr>
                            </c:if>
                            <c:if test="${not empty phone.frontCameraMegapixels}">
                                <tr>
                                    <td class="fw-bold">Front</td>
                                    <td>${phone.frontCameraMegapixels} megapixels</td>
                                </tr>
                            </c:if>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>

            <div class="card mb-4">
                <div class="card-header bg-light">
                    <h5 class="card-title mb-0">Battery</h5>
                </div>
                <div class="card-body">
                    <div class="table-responsive">
                        <table class="table table-bordered mb-0">
                            <tbody>
                            <c:if test="${not empty phone.batteryCapacityMah}">
                                <tr>
                                    <td class="fw-bold" style="width: 30%;">Battery capacity</td>
                                    <td>${phone.batteryCapacityMah}mAh</td>
                                </tr>
                            </c:if>
                            <c:if test="${not empty phone.talkTimeHours}">
                                <tr>
                                    <td class="fw-bold">Talk time</td>
                                    <td>${phone.talkTimeHours} hours</td>
                                </tr>
                            </c:if>
                            <c:if test="${not empty phone.standByTimeHours}">
                                <tr>
                                    <td class="fw-bold">Stand by time</td>
                                    <td>${phone.standByTimeHours} hours</td>
                                </tr>
                            </c:if>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>

            <div class="card mb-4">
                <div class="card-header bg-light">
                    <h5 class="card-title mb-0">Other</h5>
                </div>
                <div class="card-body">
                    <div class="table-responsive">
                        <table class="table table-bordered mb-0">
                            <tbody>
                            <c:if test="${not empty phone.deviceType}">
                                <tr>
                                    <td class="fw-bold" style="width: 30%;">Device type</td>
                                    <td>${phone.deviceType}</td>
                                </tr>
                            </c:if>
                            <c:if test="${not empty phone.bluetooth}">
                                <tr>
                                    <td class="fw-bold">Bluetooth</td>
                                    <td>${phone.bluetooth}</td>
                                </tr>
                            </c:if>
                            <c:if test="${not empty phone.os}">
                                <tr>
                                    <td class="fw-bold">OS</td>
                                    <td>${phone.os}</td>
                                </tr>
                            </c:if>
                            <c:if test="${not empty phone.ramGb}">
                                <tr>
                                    <td class="fw-bold">RAM</td>
                                    <td>${phone.ramGb}GB</td>
                                </tr>
                            </c:if>
                            <c:if test="${not empty phone.internalStorageGb}">
                                <tr>
                                    <td class="fw-bold">Internal storage</td>
                                    <td>${phone.internalStorageGb}GB</td>
                                </tr>
                            </c:if>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>
</body>
</html>