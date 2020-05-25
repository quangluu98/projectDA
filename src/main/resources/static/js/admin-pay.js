$(document).ready(function() {

    var dataOrder = {};

    $(".accept-order").on("click", function () {
        dataOrder.id = $(this).data("accept");
        console.log(dataOrder);
        NProgress.start();
        linkPost = "/api/order/pay/";
        axios.post(linkPost, dataOrder).then(function(res){
            NProgress.done();
            if(res.data.success) {
                swal(
                    'Thành công!',
                    res.data.message,
                    'success'
                ).then(function() {
                    location.reload();
                });
            } else {
                swal(
                    'Lỗi rồi!!',
                    res.data.message,
                    'error'
                );
            }
        }, function(err){
            NProgress.done();
            swal(
                'Lỗi rồi !!',
                'Không được rồi!!',
                'error'
            );
        })
    });
});