<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="header">
    <div id="top">
        <div class="left" id="logo">
            <a href="/"><img src="${pageContext.request.contextPath}/images/logo.png" alt="Truedash SSO" /></a>
        </div>
        <div class="left navigation" id="main-nav">
            <ul class="tabbed">
                <li class="current-tab"><a href="${pageContext.request.contextPath}/">Truedash SSO Application</a></li>
            </ul>
            <div class="clearer">&nbsp;</div>
        </div>
        <div class="clearer">&nbsp;</div>
    </div>
    <div class="navigation" id="sub-nav">
        <ul class="tabbed">
            <li<c:if test="${tab != 'metadata'}"> class="current-tab"</c:if>><a href="${pageContext.request.contextPath}/saml/login">SAML Login</a></li>
            <li<c:if test="${tab == 'metadata'}"> class="current-tab"</c:if>><a href="${pageContext.request.contextPath}/saml/web/metadata">Metadata Administration</a></li>
        </ul>
        <div class="clearer">&nbsp;</div>
    </div>
</div>