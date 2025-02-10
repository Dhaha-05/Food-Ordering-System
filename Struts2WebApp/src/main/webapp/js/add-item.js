$(document).ready(()=>{
    $('form').submit((event)=>{
        event.preventDefault();

        let isValid = true;
        $(".error").text("");

        const itemName = $('#itemName').val().trim();
        const nameRegex = /^[a-zA-Z\s]+$/;
        if (itemName && itemName !== "" && !nameRegex.test(itemName)) {
            $('#itemNameError').text("Enter valid item name").show();
            isValid = false;
        } else {
            $('#itemNameError').hide();
        }


        const rating = $("#rating").val().trim();
        const ratingValue = parseFloat(rating);
        if (rating === "" || isNaN(ratingValue) || ratingValue < 1.0 || ratingValue > 5.0) {
            $("#ratingError").text("Rating must be a decimal number between 1.0 and 5.0.").show();
            isValid = false;
        }
        else{
            $("#ratingError").hide();
        }

        const price = $('#price').val().trim();
        const priceValue = parseFloat(price);
        if(priceValue==="" || isNaN(priceValue) || priceValue<1.0){
            $('#priceError').text("Price must greater than value 0").show();
            isValid = false;
        }
        else{
            $('#priceError').hide();
        }

        if(isValid)
        {
            const formData={

            }
            $.ajax({
                url:'',
                method : 'POST',
                dataType : 'json',
                data:formData,
                success: function(response){

                },
                error: function(xhr,status,error){
                    console.log('Error happened : ',error);
                }
            });
        }
    });
});