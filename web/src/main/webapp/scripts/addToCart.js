$(document).ready(function () {
    $('.add-to-cart-btn').click(function () {
        let button = $(this)
        let phoneId = button.data('phone-id')
        let quantity = button.closest('tr').find('.quantity-input').val()
        let errorMessage = button.closest('tr').find('.error-message')
        let contextPath = $('body').data('context-path')

        if(quantity === undefined) {
            quantity = $('#quantity-input').val()
        }

        if(quantity === '') {
            quantity = 0
        }

        if(errorMessage.length === 0) {
            errorMessage = $('#error-message')
        }

        button.prop('disabled', true).text('Wait...')

        $.ajax({
            url: contextPath + `/ajaxCart`,
            method: 'POST',
            data: {
                phoneId: phoneId,
                quantity: quantity
            },
            success: function(response) {
                button.prop('disabled', false).text('Add')
                $('#total-items').text(response.totalQuantity + ' items for ')
                $('#total-price').text(response.totalPrice.toFixed(2) + ' $')
                $('#add-to-cart-msg').text('Successfully added phone to cart!')
                errorMessage.text('')
            },
            error: function (xhr) {
                button.prop('disabled', false).text('Add')
                $('#add-to-cart-msg').text('Error occurred while adding to cart')
                errorMessage.text(xhr.responseJSON.message)
            }
        })
    })
})