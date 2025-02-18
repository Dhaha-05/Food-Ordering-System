<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
    <head>
        <title>Initial Page</title>
        <style>
            body{
                display: flex;
                flex-direction: column;
                justify-content: center;
                align-items: center;
                height: 90vh;
                background-color : black;
            }
            #container{
                display: flex;
                flex-direction: column;
                align-items: center;
                background-color : white;
                padding : 40px;
                border-radius : 10px;
            }
            .btn{
                border:none;
                border-radius:5px;
                padding : 10px;
                width : 90px;
                background-color : lightblue;
                font-size : medium;
                box-shadow: 1px 1px 1px 1px #000;
                cursor : pointer;
                transition : 0.5s;
            }
            .btn:hover{
                background-color: #0056b3;
                color:white;
                font-size: medium;
            }
        </style>
    </head>
    <body>
        <div id = "container">
            <h1>Food Ordering System</h1>
            <s:form action="loginPage">
                <s:submit id="loginId" class="btn" value="Login"/>
            </s:form>
            <s:form action="registerPage">
                <s:submit id="registerID" class="btn" value="Register"/>
            </s:form>
        </div>
<%--        <s:a action="loginPage">Login</s:a><br/>    --%>
<%--        <s:a action="registerPage">Register</s:a>   --%>
    </body>
</html>