<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>B2B Cart - Add by Model</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
</head>
<body data-context-path="${pageContext.servletContext.contextPath}">

<tags:header cart="${cartTotals}"/>
<tags:csrfMetaInfo/>

<main class="container my-4">
    <a href="${pageContext.servletContext.contextPath}" class="btn btn-light border text-decoration-none mb-3">
        Back to product list
    </a>
    <a href="${pageContext.servletContext.contextPath}/order" class="btn btn-light border text-decoration-none mb-3">
        Order
    </a>

    <c:forEach var="successMessage" items="${successMessages}">
        <div class="alert alert-success">${successMessage}</div>
    </c:forEach>
    <c:if test="${not empty b2bCartFormErrors}">
        <div class="alert alert-danger">There were errors</div>
    </c:if>

    <h2 class="h4 mb-4">Add products by model (B2B)</h2>

    <div class="table-responsive">
        <form:form action="${pageContext.servletContext.contextPath}/cart/b2b" method="post"
                   modelAttribute="b2bCartForm">

            <tags:csrfHiddenInput/>
            <input type="hidden" name="rowNum" value="9"/>

            <table class="table table-bordered align-middle">
                <thead class="table-light">
                <tr>
                    <th style="width: 45%">Product Model</th>
                    <th style="width: 25%">Quantity</th>
                    <th style="width: 30%">Error</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="pos" begin="0" end="9">
                    <c:set var="prefix" value="model${pos}"/>
                    <c:set var="hasError" value="${not empty b2bCartFormErrors[prefix]}"/>
                    <c:set var="savedModel" value="${b2bCartFormErrors[prefix].enteredModel}"/>
                    <c:set var="savedQty" value="${b2bCartFormErrors[prefix].enteredQuantity}"/>
                    <c:set var="errorMessage" value="${b2bCartFormErrors[prefix].message}"/>

                    <tr>
                        <td>
                            <input type="text"
                                   name="${prefix}"
                                   class="form-control ${hasError ? 'is-invalid' : ''}"
                                   value="${hasError ? savedModel : ''}"
                            />
                        </td>
                        <td>
                            <input type="text"
                                   name="items[${prefix}]"
                                   class="form-control ${hasError ? 'is-invalid' : ''}"
                                   style="width: 100px;"
                                   value="${hasError ? savedQty : ''}"/>
                        </td>
                        <td>
                            <c:if test="${hasError}">
                                <small class="text-danger d-block">
                                        ${errorMessage}
                                </small>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>

            <div class="d-flex justify-content-end mt-4">
                <button type="submit" class="btn btn-primary px-4">
                    Add to Cart
                </button>
            </div>
        </form:form>
    </div>
</main>
</body>
</html>