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
            data: {
                "userBean.username": $('#username').val(),
                "userBean.password": btoa($('#password').val())
            },
            dataType:'json',
            success: function (response) {
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
