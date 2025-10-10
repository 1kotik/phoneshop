<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@tag trimDirectiveWhitespaces="true" %>
<%@attribute name="page" required="true" %>
<%@attribute name="itemsPerPage" required="false" %>

<c:choose>
    <c:when test="${empty param.page and page eq 'next'}">
        <c:set var="pageNumber" value="2"/>
    </c:when>
    <c:when test="${page eq 'prev'}">
        <c:set var="pageNumber" value="${param.page - 1}"/>
    </c:when>
    <c:when test="${page eq 'next'}">
        <c:set var="pageNumber" value="${param.page + 1}"/>
    </c:when>
    <c:otherwise>
        <c:set var="pageNumber" value="${page}"/>
    </c:otherwise>
</c:choose>

<c:choose>
    <c:when test="${param.page eq pageNumber or (empty param.page and pageNumber eq 1)}">
        <b>${pageNumber}</b>
    </c:when>
    <c:when test="${pageNumber > response.totalPages or pageNumber < 1}"/>
    <c:otherwise>
        <c:url value="" var="pageUrl">
            <c:if test="${not empty param.q}">
                <c:param name="q" value="${param.q}"/>
            </c:if>
            <c:if test="${not empty param.criteria}">
                <c:param name="criteria" value="${param.criteria}"/>
            </c:if>
            <c:if test="${not empty param.order}">
                <c:param name="order" value="${param.order}"/>
            </c:if>
            <c:param name="page" value="${pageNumber}"/>
            <c:if test="${not empty param.itemsPerPage}">
                <c:param name="itemsPerPage" value="${itemsPerPage}"/>
            </c:if>
        </c:url>
        <a href="${pageUrl}">${page}</a>
    </c:otherwise>
</c:choose>
