<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@tag trimDirectiveWhitespaces="true" %>
<%@attribute name="criteria" required="true" %>
<%@attribute name="order" required="true" %>
<c:choose>
    <c:when test="${criteria eq param.criteria and order eq param.order}">
        <b>${order}</b>
    </c:when>
    <c:otherwise>
        <c:url value="" var="pageUrl">
            <c:if test="${not empty param.q}">
                <c:param name="q" value="${param.q}"/>
            </c:if>
            <c:param name="criteria" value="${criteria}"/>
            <c:param name="order" value="${order}"/>
        </c:url>
        <a href="${pageUrl}">${order}</a>
    </c:otherwise>
</c:choose>

