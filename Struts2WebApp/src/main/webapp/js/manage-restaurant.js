$(document).ready(()=>{
    $(document).on('click','#add-btn1',(event)=>{
        event.preventDefault();
        window.location.href = "add-restaurant.html";
    });

    $(document).on('click','#add-btn2',(event)=>{
        event.preventDefault();
        window.location.href="add-item.html";
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