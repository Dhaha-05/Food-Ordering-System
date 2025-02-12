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
        if((role==="admin" || role==="manager") && !!username)
        {
            $("#currentUser").html(`Welcome, ${username} <button id='logout-btn' class='btn'>Logout</button>`);
            $("#btn-grp").empty();
            $("#btn-grp").append(`
                <button id="viewRestaurantBtn" class="btn">View Restaurant</button>
                <button id="foodItemsBtn" class="btn">Food Items</button>
                <button id='viewCartBtn' class='btn' value='${userid}'>View Cart</button>
                <button id='viewOrderBtn' class='btn' value='${userid}'>View Order</button>
                <button id='addRestaurantBtn' class='btn' >Control Center</button>
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
            url: 'http://localhost:8085/Struts2WebApp/logout',
            method: 'POST',
            success: function (response) {
                console.log("Logout Successfully");
                window.location.reload();
            },
            error: function (xhr, status, error) {
                console.log("Error logging out:", error);
                alert("Logout failed!!!");
            }
        });
    }

    $(document).on('click', '#logout-btn', function () {
        logout();
    });

    $(document).on('click','#addRestaurantBtn',()=>{
        if(role==="manager" || role === "admin")
        {
            window.location.href="manage-restaurant.html";
        }
    });

    $(document).on('click', '#login-btn', () => {
        window.location.href = "login.html";
    });

    $(document).on('click', '#register-btn', () => {
        window.location.href = "register.html";
    });

    $(document).on('click', '#viewRestaurantBtn', () => {
        fetchRestaurant();
    });

    $(document).on('click', '#foodItemsBtn', () => {
        fetchFoodItems();
    });


    $(document).on('click', '#viewCartBtn', () => {
        if (!userid && !username) {
            alert("Please log in to view your cart.");
            return;
        }
        $.ajax({
            url: 'purchase.action',
            method: 'GET',
            data : {
                userId : userid,
                action : 'cart-items'
            },
            dataType: 'json',
            success: function (response) {
                if(response.status){
                    if (response.status === "success") {
                        let cartItems = response.items || [];
                        displayCartItems(cartItems, !!username);
                    } else {
                        const container = $('#foodContainer');
                        container.empty();
                        const message = '<p>No cart items Found</p>';
                        container.append(message);
                        console.log("Something went wrong");
                    }
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
            url: 'purchase.action',
            method: 'GET',
            data:{
                action : 'orders',
                userId : userid
            },
            dataType: 'json',
            success: function (response) {
                if (response.status === "success") {
                    let orderItems = response.items || [];
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
        console.log(restaurantId);
        $.ajax({
            url: 'restaurantFood.action',
            method: 'GET',
            data : {
                restaurantId : restaurantId
            },
            dataType: 'json',
            success: function (response) {
                console.log('response : ',response);
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
            'order.cartid': item.cartid,
            'order.userid': item.userid,
            'order.itemid': item.itemid,
            'order.quantity': item.quantity,
            'order.totalprice': item.totalprice,
            'action': 'orders'
        };

        $.ajax({
            url: 'purchase.action',
            method: 'POST',
            dataType: 'json',
            data: data,
            success: function (response) {
                console.log('response : ',response);
                if (response.status === 'success') {
                    console.log('Order placed');
                    alert('Order placed successfully');
                    window.location.reload();
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
        if (!userid && !username) {
            alert("Please log in to add items to your cart.");
            return;
        }
        const item = JSON.parse($(this).val());
        const quantity = $(`#quan${item.itemid}`).val();

        if (!quantity || quantity <= 0) {
            alert("Please enter a valid quantity.");
            return;
        }

        const cartData = {
            'cartItem.userid': userid,
            'cartItem.itemid': item.itemid,
            'cartItem.quantity': parseInt(quantity),
            'cartItem.totalprice': item.price * quantity,
            'action' : 'cart-items'
        };
        console.log(userid);
        $.ajax({
            url: 'purchase.action',
            type: 'POST',
            data: cartData,
            dataType:'json',
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

    function fetchFoodItems() {
        $.ajax({
            url: 'food.action',
            type: 'GET',
            data:{
                'action':'all-items'
            },
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
                <h2>${item.itemname}</h2>
                <p>Quantity: ${item.quantity}</p>
                <p>Price per unit : Rs.${item.price}</p>
                <p>Total Price: Rs.${item.totalprice}</p>
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
                <h2>${item.itemname}</h2>
                <p>Quantity: ${item.quantity}</p>
                <p>Price per unit : Rs.${item.price}</p>
                <p>Total Price: Rs.${item.totalprice}</p>
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
                    <h2>${item.restaurantname}</h2>
                    <h3>${item.itemname}</h3>
                    <p>Category: ${item.category}</p>
                    <p>Price: Rs.${item.price}</p>
                    <p>Rating: ${item.rating}</p>
                    <p>Offer: ${item.offer}%</p>
                    ${isLoggedIn ? `<p>Quantity <input type='number' class='quantity-field' id='quan${item.itemid}' min='1' value='1'></p>` : ""}
                    ${isLoggedIn ? `<button class="cart-btn btn" value='${JSON.stringify(item)}'>Add to Cart</button>` : ""}
                </div>
            `;
            container.append(card);
        });
    }

    function fetchRestaurant() {
        $.ajax({
            url: 'restaurants.action',
            type: 'GET',
            data: {
                action:'all-restaurant'
            },
            dataType: 'json',
            success: function (res) {
                console.log("response : ", res);
                if (res.status === "success") {
                    const restaurants = res.restaurants || [];

                    if (Array.isArray(restaurants) && restaurants.length > 0) {
                        displayRestaurants(restaurants);
                    } else {
                        console.log("No valid restaurants found.");
                        $("#foodContainer").html("<p>No Restaurants Available.</p>");
                    }
                } else {
                    $("#foodContainer").html("<p>No Restaurants Available.</p>");
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
                    <h2>${restaurant.restaurantname}</h2>
                    <p>Location: ${restaurant.location}</p>
                    <p>Rating: ${restaurant.rating}</p>
                    <button class="view-items-btn view-items-btn${restaurant.restaurantid} btn" value="${restaurant.restaurantid}">View Items</button>
                </div>
            `;
            container.append(card);
        });

    }

    fetchUserDetails();
    fetchFoodItems();
});