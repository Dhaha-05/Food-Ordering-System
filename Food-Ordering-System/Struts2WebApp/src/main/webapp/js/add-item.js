$(document).ready(function () {

    function parseCookie(cookie) {
        const cookies = cookie.split(';');
        const result = {};

        for (let i = 0; i < cookies.length; i++) {
            const [key, value] = cookies[i].trim().split('=');
            result[key] = value;
        }
        return result;
    }
    const cookie = parseCookie(document.cookie);
    const managerId = atob(cookie['userid']);
    const role = atob(cookie['role']).trim();
    console.log(managerId+" : "+role);
    $(document).on('click','#control-btn',(event)=>{
        event.preventDefault();
        window.location.href = "manage-restaurant.html";
    });
    if(role === "admin")
    {
        $.ajax({
            url: 'restaurants.action',
            method: 'GET',
            data: {
             'action': 'all-restaurant',
             },
            dataType: 'json',
            success: function (response) {
                if (response.status === 'success') {
                    const restaurants = response.restaurants || [];
                    displayRestaurants(restaurants);
                } else {
                    $('#restaurant-list').html('<p>No restaurants found.</p>');
                }
            },
            error: function (xhr, status, error) {
                console.log('Error fetching restaurants:', error);
            }
        });
    }
    else if (role==="manager"){
        $.ajax({
            url: 'restaurants.action',
            method: 'GET',
            data: {
             'action': 'restaurant',
             'restaurant.managerid': managerId
             },
            dataType: 'json',
            success: function (response) {
                if (response.status === 'success') {
                    const restaurants = response.restaurants || [];
                    displayRestaurants(restaurants);
                } else {
                    $('#restaurant-list').html('<p>No restaurants found.</p>');
                }
            },
            error: function (xhr, status, error) {
                console.log('Error fetching restaurants:', error);
            }
        });
    }

    function getManagerIdFromCookie()
    {
        const cookies = document.cookie.split(';');
        for (let cookie of cookies) {
            const [key, value] = cookie.trim().split('=');
            if (key === "userid") {
                return atob(value);
            }
        }
        return null;
    }

    function displayRestaurants(restaurants) {
        const container = $('#restaurant-list');
        container.empty();
        restaurants.forEach(restaurant => {
            const button = `<button class="select-restaurant-btn" value="${restaurant.restaurantid}">${restaurant.restaurantname}</button><br>`;
            container.append(button);
        });
    }

    $(document).on('click', '.select-restaurant-btn', function () {
        const restaurantId = $(this).val();
        $('#restaurant-id').val(restaurantId);
        $('#add-item-form').show();
        $('.input-field').val('');
        $('.error').text('');
    });

    $('#add-item-form').on('submit', function (e) {
        e.preventDefault();
        const restaurantId = $('#restaurant-id').val().trim();
        const itemName = $('#item-name').val().trim();
        const category = $('#item-category').val().trim();
        const price = $('#item-price').val().trim();
        const rating = $('#item-rating').val().trim();
        let isValid=true;
        if(itemName==="")
        {
            $("#itemnameError").text("Enter Valid item name");
            isValid = false;
        }
        const ratingValue = parseFloat(rating);
        if (rating === "" || isNaN(ratingValue) || ratingValue < 1.0 || ratingValue > 5.0) {
            $("#ratingError").text("Rating must be a decimal number between 1.0 and 5.0.");
            isValid = false;
        }
        const priceValue = parseFloat(rating);
        if (price === "" || isNaN(priceValue) || priceValue<=0) {
            $("#priceError").text("Price value must be number and greater than 0");
            isValid = false;
        }
        if(isValid)
        {
            $.ajax({
                url: 'fooditem.action',
                method: 'POST',
                data: {
                    'item.restaurantid': restaurantId,
                    'item.itemname': itemName,
                    'item.category': category,
                    'item.price': price,
                    'item.rating': rating
                },
                dataType: 'json',
                success: function (response) {
                    if (response.status === 'success') {
                        alert('Item added successfully!');
                        window.location.href = 'manage-restaurant.html';
                    } else {
                        console.log('Failed to add item');
                    }
                },
                error: function (xhr, status, error) {
                    console.log('Error adding item:', error);
                }
            });
        }
    });
});