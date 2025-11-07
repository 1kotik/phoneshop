$(document).ready(function () {
    $('.update-order-status-btn').click(function () {
        let button = $(this)
        let orderId = button.data('order-id')
        let newStatus = button.data('order-status')
        let contextPath = $('body').data('context-path')

        button.prop('disabled', true).text('Wait...')

        $.ajax({
            url: contextPath + `/api/admin/orders/${orderId}?newStatus=${newStatus}`,
            method: 'PATCH',
            success: function() {
                button.prop('disabled', false).text(newStatus)
                $('#order-status').text(newStatus)
                $('#update-order-status-msg').text('Order status updated successfully')
            },
            error: function () {
                button.prop('disabled', false).text(newStatus)
                $('#update-order-status-msg').text('Error occurred while updating order status')
            }
        })
    })
})