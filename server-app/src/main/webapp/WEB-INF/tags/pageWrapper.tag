<%@ tag body-content="scriptless" isELIgnored="false" %>
<%@ attribute name="sectionName" required="true" type="java.lang.String" %>
<%@ attribute name="isLoggedIn" required="false" type="java.lang.Boolean" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>server-app &raquo; ${sectionName}</title>
    <link rel="stylesheet" type="text/css" href="<c:url value="/static/css/style.css"/>" />
    <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.5.2/jquery.min.js"></script>
    <script type="text/javascript" src="<c:url value="/static/js/ns.js"/>"></script>
</head>
<body>

<div id="header">
    <ul class="hor-list user">
        <li><a href="<c:url value="#"/>">Login</a></li>
    </ul>
</div>

<div id="wrapper">
    <h2>${sectionName}</h2>
    <jsp:doBody/>
</div>
</body>
</html>
