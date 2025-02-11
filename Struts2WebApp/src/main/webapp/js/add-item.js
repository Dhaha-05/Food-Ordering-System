$(document).ready(function () {
    const managerId = getManagerIdFromCookie();

    $(document).on('click','#control-btn',(event)=>{
        event.preventDefault();
        window.location.href = "manage-restaurant.html";
    });

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
    });

    $('#add-item-form').on('submit', function (e) {
        e.preventDefault();
        const restaurantId = $('#restaurant-id').val();
        const itemName = $('#item-name').val();
        const category = $('#item-category').val();
        const price = $('#item-price').val();
        const rating = $('#item-rating').val();

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
    });
});