<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@tag trimDirectiveWhitespaces="true" %>

<%@attribute name="error" type="java.lang.String" required="true" %>
<%@attribute name="parameterName" type="java.lang.String" required="true" %>
<%@attribute name="value" type="java.lang.String" required="true" %>
<%@attribute name="label" type="java.lang.String" required="true" %>

<div class="mb-3">
    <label for="${parameterName}" class="form-label fw-bold">${label}</label>
    <form:input path="${parameterName}"
                class="form-control form-control-lg"
                value="${value}"
                id="${parameterName}"/>
    <c:if test="${not empty error}">
        <div class="text-danger small mt-1">${error}</div>
    </c:if>
</div>