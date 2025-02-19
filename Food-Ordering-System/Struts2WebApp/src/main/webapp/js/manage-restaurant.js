$(document).ready(() => {
    function getRoleFromCookie() {
        const cookies = document.cookie.split(';');
        for (let i=0;i<cookies.length;i++) {
            const [key, value] = cookies[i].trim().split('=');
            if (key === "role") {
                return atob(value);
            }
        }
        return null;
    }

    const userRole = getRoleFromCookie();

    let form = $("form");

    if (userRole === "admin") {
        form.append('<div class="btn"><button type="submit" id="add-btn1">Add Restaurant</button></div>');
        form.append('<div class="btn"><button type="submit" id="remove-btn1">Remove Restaurant</button></div>');
        form.append('<div class="btn"><button type="submit" id="add-btn2">Add Items</button></div>');
        form.append('<div class="btn"><button type="submit" id="remove-btn2">Remove Item</button></div>');
    } else if (userRole === "manager") {
        form.append('<div class="btn"><button type="submit" id="add-btn2">Add Items</button></div>');
        form.append('<div class="btn"><button type="submit" id="remove-btn2">Remove Item</button></div>');
    }

    form.append('<div class="btn"><button type="submit" id="home-btn">Back</button></div>');

    $(document).on('click', '#add-btn1', (event) => {
        event.preventDefault();
        window.location.href = "add-restaurant.html";
    });

    $(document).on('click', '#add-btn2', (event) => {
        event.preventDefault();
        window.location.href = "add-item.html";
    });

    $(document).on('click', '#remove-btn1', (event) => {
        event.preventDefault();
        window.location.href = "remove-restaurant.html";
    });

    $(document).on('click', '#remove-btn2', (event) => {
        event.preventDefault();
        window.location.href = "remove-item.html";
    });

    $(document).on('click', '#home-btn', (event) => {
        event.preventDefault();
        window.location.href = "dashboard.html";
    });
});
