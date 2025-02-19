$(document).ready(function () {
    $('#login-form').submit(function (event) {
        event.preventDefault();

        const userData = {
            "userBean.username": $('#username').val(),
            "userBean.password": btoa($('#password').val())
        };

        $.ajax({
            url: 'login.action',
            type: 'POST',
            data: userData,
            dataType:'json',
            success: function (response) {
                console.log("Response : ",response);
                if (response.status === "success") {
                    console.log("User Data:"+ response.message);
                    window.location.href = 'dashboard.html';
                } else {
                    $('#errorMessage').text("Invalid username or password").show();
                }
            },
            error: function () {
                $('#errorMessage').text("An error occurred").show();
            }
        });

    });
});
