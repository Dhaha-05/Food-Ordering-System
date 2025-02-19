<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <title>Login Page</title>
    <style>
        body {
            background-color: black;
            font-family: 'Roboto', sans-serif;
            margin: 0;
            padding: 0;
            display: flex;
            flex-direction : column;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }

        form {
            display: flex;
            flex-direction: column;
            justify-content: space-evenly;
            align-items: center;
            width: 400px;
            background: #fff;
            padding: 20px;
            border-radius: 10px;
            height: 250px;
        }

        h1 {
            text-align: center;
            color: rgb(29, 28, 28);
        }

        .formButton {
            display: flex;
            justify-content: center;
        }

        .text-input {
            font-size: 18px;
            padding: 10px;
            border-radius: 8px;
            border: 1px solid;
            margin: 10px 0;
        }

        .button-submit {
            background-color: lightblue;
            padding: 10px 25px;
            border-radius: 10px;
            border: none;
        }

        .register-link {
            color: white;
            text-align: center;
            margin-top: 20px;
        }
    </style>
</head>
<body>
    <s:form action="login" method="POST">
        <h1>LogIn Page</h1>

        <s:textfield name="userBean.username" label="Enter Username" cssClass="text-input" />
        <s:password name="userBean.password" label="Enter Password" cssClass="text-input" />

        <div class="formButton">
            <s:submit value="Login" cssClass="button-submit"/>
        </div>
    </s:form>

    <div class="register-link">
        <p>Don't have an account? <s:a href="registerPage">Register now</s:a></p>
    </div>
</body>
</html>
