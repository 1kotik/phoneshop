<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</head>
<body>


<main class="container my-4">
    <a href="${pageContext.servletContext.contextPath}"
       class="btn btn-light border text-decoration-none mb-3">
        Continue without authentication
    </a>
   <div class="mb-3">
       <c:if test="${not empty error}">
           <span>${error}</span>
       </c:if>
   </div>
    <form:form action="${pageContext.servletContext.contextPath}/auth/login"
               method="post" modelAttribute="authenticationRequest">
        <div class="mb-3">
            <label for="username" class="form-label fw-bold">Username</label>
            <form:input path="username"
                        class="form-control form-control w-25"
                        id="username"/>
        </div>
        <div class="mb-3">
            <label for="password" class="form-label fw-bold">Password</label>
            <form:input path="password"
                        class="form-control form-control w-25"
                        id="password"/>
        </div>
        <button type="submit"
                class="btn btn-light border text-decoration-none">
            Login
        </button>
    </form:form>
</main>
</body>
</html>

