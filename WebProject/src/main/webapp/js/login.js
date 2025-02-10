$(document).ready(() => {
    $("#submit-btn").click(function (event) {
        event.preventDefault();

        const username = $("#username").val();
        const password = $("#password").val();

        $.ajax({
            type: "POST",
            url: "http://localhost:8085/WebProject/login",
            data: JSON.stringify({ username: username, password: password }),
            contentType: "application/json",
            dataType: "json",
            success: function (response) {
                if (response.status === "success") {
                    console.log("Successful");
                    window.location.href = "home-page.html";
                } else {
                    $("#errorMessage").text("Invalid username or password").show();
                }
            },
            error: function (xhr, status, error) {
                console.error("Error:", status, error);
                $("#errorMessage").text("Error occurred during login").show();
            }
        });
    });
});
