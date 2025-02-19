$(document).ready(function () {
    const managerId = getManagerIdFromCookie();
    
    $(document).on('click','#control-btn',(event)=>{
        event.preventDefault();
        window.location.href = "manage-restaurant.html";
    });

    $.ajax({
        url: 'restaurants.action',
        method: 'GET',
        data: { action: 'all-restaurant', 'restaurant.managerid': managerId },
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
            const card = `
                <div class="restaurant-card">
                    <h2>${restaurant.restaurantname}</h2>
                    <p>Location: ${restaurant.location}</p>
                    <p>Rating: ${restaurant.rating}</p>
                    <button class="remove-restaurant-btn" value="${restaurant.restaurantid}">Remove</button>
                </div>
            `;
            container.append(card);
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

    $(document).on('click', '.remove-restaurant-btn', function () {
        const restaurantId = $(this).val();
        if (confirm('Are you sure you want to remove this restaurant?')) {
            $.ajax({
                url: 'restaurants.action',
                method: 'POST',
                data: { 
                    action: 'remove-restaurant',
                     'restaurant.restaurantid': restaurantId 
                    },
                dataType: 'json',
                success: function (response) {
                    if (response.status === 'success') {
                        alert('Restaurant removed successfully!');
                        window.location.reload();
                    } else {
                        console.log('failed to remove');
                        // alert('Failed to remove restaurant.');
                    }
                },
                error: function (xhr, status, error) {
                    console.log('Error removing restaurant:', error);
                }
            });
        }
    });
});