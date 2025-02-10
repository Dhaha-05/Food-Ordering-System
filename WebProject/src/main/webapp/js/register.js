$('#registerForm').on('submit', function (e) {
    e.preventDefault();

    $('.error').text('');

    let isValid = true;

    const username = $('#username').val().trim();
    const fullname = $('#fullname').val().trim();
    const email = $('#email').val().trim();
    const mobileno = $('#mobileno').val().trim();
    const password = $('#password').val().trim();
    const confirmpassword = $('#confirmpassword').val().trim();

    if (email) {
        if (!/\S+@\S+\.\S+/.test(email)) {
            $('#emailError').text('Valid email is required');
            isValid = false;
            return;
        }
    }
    if (mobileno) {
        if (!/^\d{10}$/.test(mobileno)) {
            $('#mobileError').text('Valid 10-digit mobile number is required').show();
            isValid = false;
            return;
        }
    }
    if (password === "") {
        $('#passwordError').text('Password is required');
        isValid = false;
    }
    if (password!=null && confirmpassword!=null && password !== confirmpassword) {
        $('#confirmPasswordError').text('Passwords do not match').show();
        isValid = false;
    }
    else if(password===null){
        $('#confirmPasswordError').text('Enter valid password').show();
        isValid = false;
    }

    if (isValid) {
        let data = {
            username: username,
            fullname:fullname,
            email: email,
            mobileno: mobileno,
            password: password,
            role: $('#role').val()
        };
        $.ajax({
            url: 'http://localhost:8085/WebProject/register',
            method: 'GET',
            data: data,
            success: function (res) {
                console.log("Inside ajax username event");
                console.log(res);
                let response = JSON.stringify(res);
                if (response.userstatus === "success") {
                    $('#usernameError').text('Username available').removeClass('error').addClass('success').show();
                } else {
                    $('#usernameError').text("Username already exists").removeClass('success').addClass('error').show();
                }
               if (response.emailstatus === "success") {
                   $('#emailError').text('').removeClass('error').hide();
               } else {
                   $('#emailError').text("Email already exists").addClass('error').show();
               }
               if (response.mobilestatus === "success") {
                   $('#mobileError').text('').removeClass('error').show();
               } else {
                   $('#mobileError').text("Mobile number already exists").addClass('error').show();
               }
            },
            error: function (xhr, status, error) {
                console.error("Error:", status, error);
                $("#responseMessage").text("Error occurred during validation").show();
            }
        });
        //alert(JSON.stringify(data));
        console.log("inside submit event")
        $.ajax({
            url: 'http://localhost:8085/WebProject/register',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(data),
            success: function (response) {
                console.log(response);
                if (response.status === "success") {
                    $('#responseMessage').removeClass('error').addClass('success').text("Registration successful!");
                    window.location.href = "home-page.html";
                } else {
                    $('#responseMessage').text(response.message || 'Registration failed.').show();
                }
            },
            error: function () {
                $('#responseMessage').text('Error while registering. Please try again.');
            }
        });
    }
});

//$("#username").on('change', () => {
//    let username = $("#username").val();
//    if (username) {
//        console.log("Username change event");
//        $.ajax({
//            url: 'http://localhost:8080/WebProject/register',
//            method: 'GET',
//            data: { username: username },
//            success: function (res) {
//                console.log("Inside ajax username event");
//                console.log(res);
//                if (JSON.parse(res).status === "success") {
//                    $('#usernameError').text('Username available').removeClass('error').addClass('success').show();
//                } else {
//                    $('#usernameError').text("Username already exists").removeClass('success').addClass('error').show();
//                }
//            },
//            error: function (xhr, status, error) {
//                console.error("Error:", status, error);
//                $("#usernameError").text("Error occurred during username validation").show();
//            }
//        });
//    }
//});
//
//$("#email").on('change', () => {
//    let email = $("#email").val();
//    if (email) {
//        if (!/\S+@\S+\.\S+/.test(email)) {
//                $('#emailError').text('Valid email is required');
//                isValid = false;
//                return;
//            }
//        console.log("Email change event");
//        $.ajax({
//            url: 'http://localhost:8080/WebProject/register',
//            method: 'GET',
//            data: { email: email },
//            success: function (res) {
//                console.log("Inside ajax email event");
//                console.log(res);
//                if (JSON.parse(res).status === "success") {
//                    $('#emailError').text('').removeClass('error').hide();
//                } else {
//                    $('#emailError').text("Email already exists").addClass('error').show();
//                }
//            },
//            error: function (xhr, status, error) {
//                console.error("Error:", status, error);
//                $("#emailError").text("Error occurred during email validation").show();
//            }
//        });
//    }
//});
//
//$("#mobileno").on('change', () => {
//    let mobileno = $("#mobileno").val();
//
//        console.log("Mobileno change event");
//        $.ajax({
//            url: 'http://localhost:8080/WebProject/register',
//            method: 'GET',
//            data: { mobileno: mobileno },
//            success: function (res) {
//                console.log("Inside ajax mobileno event");
//                console.log(res);
//                let response = JSON.parse(res);
//                if (response.status === "success") {
//                    $('#mobileError').text('').removeClass('error').show();
//                } else {
//                    $('#mobileError').text("Mobile number already exists").addClass('error').show();
//                }
//            },
//            error: function (xhr, status, error) {
//                console.error("Error:", status, error);
//                $("#mobileError").text("Error occurred during mobile validation").show();
//            }
//        });
//    }
//});
