$(document).ready(function () {
    $(".add-comment").on("click", function () {
        var dataCmt = {};
        // dataCmt.star = $("#selectStar").val();
        dataCmt.content = document.getElementById("key-word").value;
        dataCmt.productId = $(this).data("product");
        // dataCmt.userId = $(this).data("userId");
        console.log(dataCmt);

        NProgress.start();

        var linkPost = "/api/comment/add";

        axios.post(linkPost, dataCmt).then(function(res){
            NProgress.done();
            if(res.data.success) {
                swal(
                    'Xin cảm ơn!',
                    res.data.message,
                    'success'
                ).then(function() {
                    location.reload();
                });
            } else {
                swal(
                    'Lỗi Rồi',
                    res.data.message,
                    'error'
                );
            }
        }, function(err){
            NProgress.done();
            swal(
                'Lỗi Rồi',
                'Không được rồi!',
                'error'
            );
        });
    });
});