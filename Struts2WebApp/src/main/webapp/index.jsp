<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
    <head>
        <title>Initial Page</title>
    </head>
    <body>
        <h1>Food Ordering System</h1>
        <s:form action="loginPage">
            <s:submit value="Login"/>
        </s:form>
        <s:form action="registerPage">
            <s:submit value="Register"/>
        </s:form>
<%--        <s:a action="loginPage">Login</s:a><br/>    --%>
<%--        <s:a action="registerPage">Register</s:a>   --%>
    </body>
</html>