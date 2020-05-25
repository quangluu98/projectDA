$(document).ready(function () {
    var dataCartProduct = {};

    $(".change-product-amount").change(function () {
        dataCartProduct = {};
        dataCartProduct.id = $(this).data("id");
        dataCartProduct.amount = $(this).val();
        dataCartProduct.productId = $(this).data("product");


        NProgress.start();

        var linkPost = "/api/cart-product/update";

        axios.post(linkPost, dataCartProduct).then(function(res){
            NProgress.done();
            if(res.data.success) {
                location.reload();
            } else {
                swal(
                    'Lỗi',
                    res.data.message,
                    'error'
                ).then(function() {
                    location.reload();
                });
            }
        }, function(err){
            NProgress.done();
            swal(
                'Lỗi',
                'Lỗi',
                'error'
            );
        });
    });

    $(".delete-cart-product").on("click",function(){
        var pdInfo = $(this).data("id");
        dataCartProduct.id = pdInfo;
        dataCartProduct.productId = $(this).data("product");

        NProgress.start();
        var linkGet = "/api/cart-product/delete";
        axios.post(linkGet, dataCartProduct).then(function(res){
            NProgress.done();
            if(res.data.success) {
                swal(
                    'Thành Công',
                    res.data.message,
                    'success'
                ).then(function() {
                    location.reload();
                });
            } else {
                swal(
                    'Lỗi',
                    res.data.message,
                    'error'
                );
            }
        }, function(err){
            NProgress.done();
            swal(
                'Lỗi',
                'Lỗi',
                'error'
            );
        });
    })
});