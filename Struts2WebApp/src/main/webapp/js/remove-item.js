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

    function displayRestaurants(restaurants) {
        const container = $('#restaurant-list');
        container.empty();
        restaurants.forEach(restaurant => {
            const button = `<button class="select-restaurant-btn" value="${restaurant.restaurantid}">${restaurant.restaurantname}</button><br>`;
            container.append(button);
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

    $(document).on('click', '.select-restaurant-btn', function () {
        const restaurantId = $(this).val();
        fetchItemsByRestaurant(restaurantId);
    });

    function fetchItemsByRestaurant(restaurantId) {
        $.ajax({
            url: 'food.action',
            method: 'GET',
            data: { 
                action: 'restaurant-items',
                'foodItem.restaurantid': restaurantId 
            },
            dataType: 'json',
            success: function (response) {
                if (response.status === 'success') {
                    const items = response.foodItems || [];
                    displayItems(items);
                } else {
                    $('#items-container').html('<p>No items found.</p>');
                }
            },
            error: function (xhr, status, error) {
                console.log('Error fetching items:', error);
            }
        });
    }

    function displayItems(items) {
        const container = $('#items-container');
        container.empty();
        items.forEach(item => {
            const card = `
                <div class="item-card">
                    <h3>${item.itemname}</h3>
                    <p>Category: ${item.category}</p>
                    <p>Price: Rs.${item.price}</p>
                    <button class="remove-item-btn" value="${item.itemid}">Remove</button>
                </div>
            `;
            container.append(card);
        });
        $('#item-list').show();
    }

    $(document).on('click', '.remove-item-btn', function () {
        const itemId = $(this).val();
        if (confirm('Are you sure you want to remove this item?')) {
            $.ajax({
                url: 'food.action',
                method: 'POST',
                data: { 'action': 'remove-item', 'foodItem.itemid': itemId },
                dataType: 'json',
                success: function (response) {
                    if (response.status === 'success') {
                        alert('Item removed successfully!');
                        window.location.reload();
                    } else {
                        alert('Failed to remove item.');
                    }
                },
                error: function (xhr, status, error) {
                    console.log('Error removing item:', error);
                }
            });
        }
    });
});

