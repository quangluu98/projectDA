$(document).ready(function() {

    var dataOrder = {};

    $(".edit-order").on("click", function () {
        var odInfo= $(this).data("order");
        console.log(dataOrder.id);
        NProgress.start();
        axios.get("/api/order/detail/" + odInfo).then(function(res){
            NProgress.done();
            if(res.data.success) {
                dataOrder.id = res.data.data.id;
                $("#input-order-name").val(res.data.data.customerName);
                $("#input-order-address").val(res.data.data.address);
                $("#input-order-email").val(res.data.data.email);
                $("#input-order-phone").val(res.data.data.phoneNumber);
                $("#input-order-status").val(res.data.data.statusId);
            }else {
                console.log("Lỗi");
            }
        }, function(err){
            NProgress.done();
        })
    });

    $(".btn-save-order").on("click", function () {
        if($("#input-order-name").val() === "" || $("#input-order-address").val() === "" || $("#input-order-email").val()==="" || $("#input-order-phone").val()==="") {
            swal(
                'Lỗi',
                'Bạn phải điền đầy đủ',
                'error'
            );
            return;
        }

        dataOrder.customerName = $("#input-order-name").val();
        dataOrder.address = $("#input-order-address").val();
        dataOrder.email = $("#input-order-email").val();
        dataOrder.phoneNumber = $("#input-order-phone").val();
        dataOrder.statusId = $("#input-order-status").val();
        NProgress.start();
        linkPost = "/api/order/update/" + dataOrder.id;
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

    $(".remove-order").on("click", function () {
        dataOrder.id = $(this).data("remove");
        console.log(dataOrder);
        NProgress.start();
        linkPost = "/api/order/remove/";
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

    $(".accept-order").on("click", function () {
        dataOrder.id = $(this).data("accept");
        console.log(dataOrder);
        NProgress.start();
        linkPost = "/api/order/accept/";
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

    $(".delete-order").on("click",function(){
        var pdInfo = $(this).data("order");
        dataOrder.id = pdInfo;

        NProgress.start();
        var linkGet = "/api/order/delete";
        axios.post(linkGet, dataOrder).then(function(res){
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
    });
});