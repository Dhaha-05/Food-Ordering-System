<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
    <title>Login Page</title>
    <style>
            body
            {
                display:flex;
                flex-direction:column;
                justify-content:center;
                align-items:center;
                padding:0px;
                background-color:black;
                height:90vh;
            }
            .form-content
            {
                display:flex;
                flex-direction:column;
                align-items:center;
                background-color:white;
                border-radius:5px;
                width:400px;
            }
            .formButton{
                display:flex;
                justify-content:center;
            }
            .button-submit {
                background-color: lightblue;
                padding: 10px 25px;
                border-radius: 10px;
                border: none;
            }
    </style>
</head>
<body>
    <div class="form-content">
        <h1>Register Page</h1>
        <s:form action="register" method="POST" >
            <s:textfield name="userBean.username" label="Enter Username" cssClass="text-input"/>
            <s:textfield name="userBean.fullname" label="Enter Fullname" cssClass="text-input"/>
            <s:textfield name="userBean.email" label="Enter Email" cssClass="text-input"/>
            <s:textfield name="userBean.mobileno" label="Enter mobileno" cssClass="text-input"/>
            <s:password name="userBean.password" label="Enter Password" cssClass="text-input"/>
            <s:password label="Re-enter Password" cssClass="text-input"/>
            <s:select name="userBean.role" id="role" list="{'customer', 'manager'}" value="customer" label="Select Role" cssClass="text-input" />
            <s:submit value="Register" cssClass="button-submit"/>
        </s:form>
        <div class="login-link">
            <p>Already have an account? <s:a href="loginPage">Login</s:a></p>
        </div>
    </div>
</body>
</html>
