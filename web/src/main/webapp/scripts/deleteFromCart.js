$(document).ready(function () {
    $('.delete-from-cart-btn').click(function () {
        let button = $(this)
        let phoneId = button.data('phone-id')
        let contextPath = $('body').data('context-path')
        let rowToDelete = button.closest('tr')
        let csrfToken = $("meta[name='_csrf_token']").attr("content");
        let csrfHeader = $("meta[name='_csrf_header']").attr("content");

        button.prop('disabled', true).text('Wait...')

        $.ajax({
            url: contextPath + `/api/cart/${phoneId}`,
            method: 'DELETE',
            headers: {
                [csrfHeader]: csrfToken
            },
            success: function(response) {
                rowToDelete.remove()
                button.prop('disabled', false).text('Delete')
                $('#total-items').text(response.totalQuantity + ' items for ')
                $('#total-price').text(response.totalPrice.toFixed(2) + ' $')
                $('#delete-from-cart-msg').text('Successfully removed item from cart!')
            },
            error: function () {
                button.prop('disabled', false).text('Delete')
                $('#delete-from-cart-msg').text('Error occurred while removing from cart')
            }
        })
    })
})