<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <title>Error Page</title>
</head>
<body>
    <h1 style="color:red">Error Occurred!!!</h1>
    <h2>Status Code: <s:property value="#request.errorStatus" /></h2>
    <h3>Message: <s:property value="#request.errorMessage" /></h3>
</body>
</html>