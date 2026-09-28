
  $(document).ready(function() {
   

    // Increment and decrement functionality
    function incrementValue(e) {
        e.preventDefault();
        var fieldName = $(e.target).data('field');
        var parent = $(e.target).closest('.product-quantity');
        var input = parent.find('input[name="' + fieldName + '"]');

        var currentVal = parseInt(input.val(), 10);

        if (!isNaN(currentVal)) {
            input.val(currentVal + 1);
        } else {
            input.val(0);
        }
    }

    function decrementValue(e) {
        e.preventDefault();
        var fieldName = $(e.target).data('field');
        var parent = $(e.target).closest('.product-quantity');
        var input = parent.find('input[name="' + fieldName + '"]');

        var currentVal = parseInt(input.val(), 10);

        if (!isNaN(currentVal) && currentVal > 0) {
            input.val(currentVal - 1);
        } else {
            input.val(0);
        }
    }

    $('.product-quantity').on('click', '.button-plus', function (e) {
        incrementValue(e);   
    });

    $('.product-quantity').on('click', '.button-minus', function (e) {
        decrementValue(e);
    });
});