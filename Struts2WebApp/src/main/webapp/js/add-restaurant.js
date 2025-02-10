$(document).ready(function () {
    $("form").submit(function (event) {
        event.preventDefault();

        let isValid = true;

        $(".error").text("");

        const restaurantName = $("#restaurantName").val().trim();
        if (restaurantName === "") {
            $("#restaurantNameError").text("Restaurant name is required.");
            isValid = false;
        }

        const location = $("#location").val().trim();
        if (location === "") {
            $("#locationError").text("Location is required.");
            isValid = false;
        }

        const rating = $("#rating").val().trim();
        const ratingValue = parseFloat(rating);
        if (rating === "" || isNaN(ratingValue) || ratingValue < 1.0 || ratingValue > 5.0) {
            $("#ratingError").text("Rating must be a decimal number between 1.0 and 5.0.");
            isValid = false;
        }

        const panCard = $("#panCard").val().trim();
        if (panCard === "") {
            $("#panCardError").text("PAN Card is required.");
            isValid = false;
        }

        const gstNo = $("#gstNo").val().trim();
        if (gstNo === "") {
            $("#gstNoError").text("GST Number is required.");
            isValid = false;
        }

        const bankAccount = $("#bankAccount").val().trim();
        if (bankAccount === "" || !/^\d{9,18}$/.test(bankAccount)) {
            $("#bankAccountError").text("Invalid Bank Account number.");
            isValid = false;
        }

        const fssaiLicense = $("#fssaiLicense").val().trim();
        if (fssaiLicense === "") {
            $("#fssaiLicenseError").text("FSSAI License is required.");
            isValid = false;
        }

        if (isValid) {
            const formData = {
                'restaurant.restaurantname': restaurantName,
                'restaurant.location': location,
                'restaurant.rating': ratingValue,
                'restaurant.pancard': panCard,
                'restaurant.gstno': gstNo,
                'restaurant.bankaccount': bankAccount,
                'restaurant.fssailicense': fssaiLicense,
                'restaurant.userid':getUserIdFromCookie()
            };

            $.ajax({
                url: "newRestaurant.action",
                method: "POST",
                dataType: "json",
                data: formData,
                success: function (response) {
                    console.log(response);
                    if (response.status === "success") {
                        console.log(response.message);
                        window.location.href = "dashboard.html";
                    } else {
                        console.log(response.message);
                        //window.location.href = "dashboard.html";
                    }
                },
                error: function (xhr, status, error) {
                    console.error("Error:", error);
                    alert("An error occurred while adding the restaurant.");
                }
            });
        }
    });

    function getUserIdFromCookie() {
        const cookies = document.cookie.split(';');
        for (let cookie of cookies) {
            const [key, value] = cookie.trim().split('=');
            if (key === "userid") {
                return atob(value);
            }
        }
        return null;
    }
});
