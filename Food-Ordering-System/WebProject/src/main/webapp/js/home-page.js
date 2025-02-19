$(document).ready(function () {
    let userid = null;
    let username = null;
    let role = null;

    function parseCookie(cookie) {
        const cookies = cookie.split(';');
        const result = {};

        for (let i = 0; i < cookies.length; i++) {
            const [key, value] = cookies[i].trim().split('=');
            result[key] = value;
        }
        return result;
    }

    function fetchUserDetails() {
        let cookie = parseCookie(document.cookie)
        if(cookie.userid && cookie.userid!=="")
        {
            userid = atob(cookie.userid);
        }else
        {
            userid = null;
        }
        if(cookie.name && cookie.name!== "")
        {
            username = atob(cookie.name);
        }
        else
        {
            username = null;
        }
        if(cookie.role && cookie.role!=="")
        {
            role = atob(cookie.role);
        }
        else
        {
            role = null;
        }
        updateUserUI(username);
    }

    function updateUserUI(username) {
        if(role==="manager")
        {
            $("#currentUser").html(`Welcome, ${username} <button id='logout-btn' class='btn'>Logout</button>`);
            $("#btn-grp").empty();
            $("#btn-grp").append(`
                <button id="viewRestaurantBtn" class="btn">View Restaurant</button>
                <button id="foodItemsBtn" class="btn">Food Items</button>
                <button id='viewCartBtn' class='btn' value='${userid}'>View Cart</button>
                <button id='viewOrderBtn' class='btn' value='${userid}'>View Order</button>
                <button id='addRestaurantBtn' class='btn' >Add Restaurant</button>
            `);
        }
        else if (username) {
            $("#currentUser").html(`Welcome, ${username} <button id='logout-btn' class='btn'>Logout</button>`);
            $("#btn-grp").empty();
            $("#btn-grp").append(`
                <button id="viewRestaurantBtn" class="btn">View Restaurant</button>
                <button id="foodItemsBtn" class="btn">Food Items</button>
                <button id='viewCartBtn' class='btn' value='${userid}'>View Cart</button>
                <button id='viewOrderBtn' class='btn' value='${userid}'>View Order</button>
            `);
        } else {
            $("#currentUser").html(`
                <button id='register-btn' class='btn'>Register</button>
                <button id='login-btn' class='btn'>Login</button>
            `);
            $("#btn-grp").empty();
            $("#btn-grp").append(`
                <button id="viewRestaurantBtn" class="btn">View Restaurant</button>
                <button id="foodItemsBtn" class="btn">Food Items</button>
            `);
        }
    }

    function logout() {
        $.ajax({
            url: 'http://localhost:8085/WebProject/logout',
            method: 'POST',
            success: function () {
                console.log("Logged out successfully");
                userid = null;
                username = null;
//                document.cookie = "userid=; expires=Sat, 16 Nov 2003 00:00:00 UTC; path=/;";
//                document.cookie = "fullname=; expires=Sat, 16 Nov 2003 00:00:00 UTC; path=/;";
                fetchUserDetails();
            },
            error: function (xhr, status, error) {
                console.log("Error logging out:", error);
            }
        });
    }

    $(document).on('click', '#logout-btn', function () {
        logout();
    });

    $(document).on('click','#addRestaurantBtn',()=>{
        if(role==="manager")
        {
            window.location.href="add-restaurant.html";
        }
    });

    $(document).on('click', '#login-btn', () => {
        window.location.href = "login.html";
    });

    $(document).on('click', '#register-btn', () => {
        window.location.href = "register.html";
    });

    $(document).on('click', '#viewRestaurantBtn', () => {
        fetchRestaurant('restaurants');
    });

    $(document).on('click', '#foodItemsBtn', () => {
        fetchFoodItems('food-items');
    });

    $(document).on('click', '#viewCartBtn', () => {
        if (!userid) {
            alert("Please log in to view your cart.");
            return;
        }
        $.ajax({
            url: `http://localhost:8085/WebProject/foodapp/purchase/${userid}/cart-items`,
            method: 'GET',
            dataType: 'json',
            success: function (response) {
                if (response.status === "success") {
                    let cartItems = response?.items || [];
                    displayCartItems(cartItems, !!username);
                } else {
                    const container = $('#foodContainer');
                    container.empty();
                    const message = '<p>No cart items Found</p>';
                    container.append(message);
                    console.log("Something went wrong");
                }
            },
            error: function (xhr, status, error) {
                console.log('Error fetching the data : ' + error);
            }
        });
    });

    $(document).on('click', '#viewOrderBtn', () => {
        if (!userid) {
            alert("Please log in to view your orders.");
            return;
        }
        $.ajax({
            url: `http://localhost:8085/WebProject/foodapp/purchase/${userid}/orders`,
            method: 'GET',
            dataType: 'json',
            success: function (response) {
                if (response.status === "success") {
                    let orderItems = response?.items || [];
                    displayOrderItems(orderItems, !!username);
                } else {
                    const container = $('#foodContainer');
                    container.empty();
                    const message = '<p>No Order History Available</p>';
                    container.append(message);
                    return;
                    console.log("Something went wrong");
                }
            },
            error: function (xhr, status, error) {
                console.log('Error fetching the data : ' + error);
            }
        });
    });

    $(document).on('click', '.view-items-btn', function () {
        const restaurantId = $(this).val();
        $.ajax({
            url: `http://localhost:8085/WebProject/foodapp/restaurants/${restaurantId}/food-items`,
            method: 'GET',
            dataType: 'json',
            success: function (response) {
                if (response.status === 'success') {
                    let foodItems = response.foodItems || [];
                    displayFoodItems(foodItems, !!username);
                } else {
                    displayFoodItems([],!!username);
                }
            },
            error: function (xhr, status, error) {
                console.log("Error fetching data : " + error + " : " + status);
            }
        });
    });

    $(document).on('click', '.order-btn', function () {
        const item = JSON.parse($(this).val());

        const data = {
            cartid: item.id,
            userid: userid,
            itemid: item.itemId,
            quantity: item.quantity,
            totalPrice: item.totalPrice
        };

        $.ajax({
            url: `http://localhost:8085/WebProject/foodapp/purchase/${userid}/orders`,
            method: 'POST',
            contentType: 'application/json',
            dataType: 'json',
            data: JSON.stringify(data),
            success: function (response) {
                if (response.status === 'success') {
                    console.log('Order placed');
                    alert('Order placed successfully');
                } else {
                    console.log('Order failed!!!!!');
                }
            },
            error: function (xhr, status, error) {
                console.log("Error placing order : ", error);
            }
        });
    });

    $(document).on('click', '.cart-btn', function () {
        if (!userid) {
            alert("Please log in to add items to your cart.");
            return;
        }
        const item = JSON.parse($(this).val());
        const quantity = $(`#quan${item.id}`).val();

        if (!quantity || quantity <= 0) {
            alert("Please enter a valid quantity.");
            return;
        }
        const cartData = {
            itemid: item.id,
            quantity: parseInt(quantity),
            totalprice: item.price * quantity,
            billamount: item.price * quantity
        };
        console.log(userid);
        $.ajax({
            url: `http://localhost:8085/WebProject/foodapp/purchase/${userid}/cart-items`,
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(cartData),
            success: function (response) {
                if (response.status === "success") {
                    console.log("Item added to cart:", response);
                    alert("Item added to cart successfully!");
                } else {
                    console.log("Items not added");
                }
            },
            error: function (xhr, status, error) {
                console.log("Error adding item to cart:", error);
                alert("Failed to add item to cart. Please try again.");
            }
        });
    });

    function fetchFoodItems(endpoint) {
        $.ajax({
            url: `http://localhost:8085/WebProject/foodapp/${endpoint}`,
            type: 'GET',
            dataType: 'json',
            success: function (response) {
                const foodItems = response.foodItems || [];
                displayFoodItems(foodItems, !!username);
            },
            error: function (xhr, status, error) {
                console.log("Error fetching data: " + error);
            }
        });
    }

    function displayCartItems(items, isLoggedIn) {
        const container = $('#foodContainer');
        container.empty();
        if (items.length == 0) {
            const message = '<p>No cart items available</p>';
            container.append(message);
            return;
        }
        items.forEach((item) => {
            const card = `
            <div class="cart-card">
                <h2>${item.name}</h2>
                <p>Quantity: ${item.quantity}</p>
                <p>Price per unit : Rs.${item.price}</p>
                <p>Total Price: Rs.${item.totalPrice}</p>
                ${isLoggedIn ? `<button class="order-btn btn" value='${JSON.stringify(item)}'>Buy now</button>` : ""}
            </div>
            `;
            container.append(card);
        });
    }

    function displayOrderItems(items, isLoggedIn) {
        const container = $('#foodContainer');
        container.empty();
        if (items.length == 0) {
            const message = '<p>No Order History Available</p>';
            container.append(message);
            return;
        }
        items.forEach(item => {
            const card = `
            <div class="order-card">
                <h2>${item.name}</h2>
                <p>Quantity: ${item.quantity}</p>
                <p>Price per unit : Rs.${item.price}</p>
                <p>Total Price: Rs.${item.totalPrice}</p>
                <p>Status : ${item.status}</p>
            </div>
            `;
            container.append(card);
        });
    }

    function displayFoodItems(items, isLoggedIn) {
        const container = $("#foodContainer");
        container.empty();
        if (items.length === 0) {
            const message = "<p>No items Available now</p>";
            container.append(message);
            return;
        }
        items.forEach(item => {
            const card = `
                <div class="food-card">
                    <h2>${item.name}</h2>
                    <h3>${item.item}</h3>
                    <p>Category: ${item.category}</p>
                    <p>Price: Rs.${item.price}</p>
                    <p>Rating: ${item.rating}</p>
                    <p>Offer: ${item.offer}%</p>
                    ${isLoggedIn ? `<p>Quantity <input type='number' class='quantity-field' id='quan${item.id}' min='1' value='1'></p>` : ""}
                    ${isLoggedIn ? `<button class="cart-btn btn" value='${JSON.stringify(item)}'>Add to Cart</button>` : ""}
                </div>
            `;
            container.append(card);
        });
    }

    function fetchRestaurant(endpoint) {
        $.ajax({
            url: 'http://localhost:8085/WebProject/foodapp/restaurants',
            type: 'GET',
            dataType: 'json',
            success: function (res) {
                console.log("response : ", res);
                if (res.status === "success") {
                    const restaurants = res.restaurants || [];

                    if (Array.isArray(restaurants) && restaurants.length > 0) {
                        displayRestaurants(restaurants);
                    } else {
                        console.log("No valid restaurants found.");
                        $("#foodContainer").html("<p>No Restaurants Found.</p>");
                    }
                } else {
                    $("#foodContainer").html("<p>No Restaurants Found.</p>");
                }
            },
            error: function () {
                console.log("Error fetching restaurants.");
            }
        });
    }

    function displayRestaurants(restaurants) {
        if (!Array.isArray(restaurants)) {
            console.log("Invalid data format for restaurants");
            return;
        }
        const container = $("#foodContainer");
        container.empty();
        restaurants.forEach(restaurant => {
            const card = `
                <div class="restaurant-card">
                    <h2>${restaurant.name}</h2>
                    <p>Location: ${restaurant.location}</p>
                    <p>Rating: ${restaurant.rating}</p>
                    <button class="view-items-btn view-items-btn${restaurant.id} btn" value="${restaurant.id}">View Items</button>
                </div>
            `;

            container.append(card);
        });

    }

    fetchUserDetails();
    fetchFoodItems('food-items');
});