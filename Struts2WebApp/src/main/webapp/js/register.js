$(document).ready(function () {
    $('#registerForm').submit(function (event) {
        event.preventDefault();

        const username = $('#username').val().trim();
        const fullname = $('#fullname').val().trim();
        const email = $('#email').val().trim();
        const mobileno = $('#mobileno').val().trim();
        const password = $('#password').val().trim();
        const confirmpassword = $('#confirmpassword').val().trim();
        const role = $('#role').val().trim();
        let isValid = true;

        const usernameRegex = /^[a-zA-Z0-9]{4,9}$/;
        if (username && username !== "" && !usernameRegex.test(username)) {
            $('#usernameError').text("Username must be 4 to 9 alphanumeric characters.").show();
            isValid = false;
        } else {
            $('#usernameError').hide();
        }

        const fullnameRegex = /^[a-zA-Z\s]+$/;
        if (fullname && fullname !== "" && !fullnameRegex.test(fullname)) {
            $('#fullnameError').text("Full name must only contain letters and spaces.").show();
            isValid = false;
        } else {
            $('#fullnameError').hide();
        }

        const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
        if (email && email !== "" && !emailRegex.test(email)) {
            $('#emailError').text("Invalid email format.").show();
            isValid = false;
        } else {
            $('#emailError').hide();
        }

        const mobilenoRegex = /^\d{10}$/;
        if (mobileno && mobileno !== "" && !mobilenoRegex.test(mobileno)) {
            $('#mobileError').text("Mobile number must be exactly 10 digits.").show();
            isValid = false;
        } else {
            $('#mobileError').hide();
        }


        if (password !== confirmpassword) {
            $('#confirmPasswordError').text("Passwords do not match.").show();
            isValid = false;
        } else {
            $('#confirmPasswordError').hide();
        }

        if(isValid){
            const userData = {
                "userBean.username": username,
                "userBean.fullname": fullname,
                "userBean.email":email,
                "userBean.mobileno":mobileno,
                "userBean.password": btoa(password),
                "userBean.role":$('#role').val()
            };

            $.ajax({
                url: 'register.action',
                type: 'POST',
                data: userData,
                dataType:'json',
                success: function (response) {
                    console.log("response : "+response);
                    if(response.status){
                        if (response.status === "success") {
                            console.log("response in success:"+ response);
                            window.location.href = 'dashboard.html';
                        } else if(response.status === "failed"){
                            if(response.usernameError)
                            {
                                $('#usernameError').text(response.usernameError).show();
                            }
                            if(response.emailError)
                            {
                                $('#emailError').text(response.emailError).show();
                            }
                            if(response.mobilenoError)
                            {
                                $('#mobileError').text(response.mobilenoError).show();
                            }
                        }
                        else if(response.status === "error")
                        {
                            $('#responseMessage').text(response.message).show();
                        }
                    }
                },
                error: function () {
                    $('#errorMessage').text("An error occurred").show();
                }
            });
        }
        else
        {
            $('#responseMessage').text("Please correct the errors before submitting.").show();
        }
    });
});
