$(document).ready(function () {
    var dataProduct={};

    function readURL(input) {
        if(input.files && input.files[0]) {
            var reader =new FileReader();
            reader.onload= function (e) {
                $('#preview-product-img').attr('src', e.target.result);
            }
            reader.readAsDataURL(input.files[0]);
        }

    }


    $("#change-image").change(function () {
        readURL(this);
        var formData = new FormData();
        NProgress.start();
        formData.append('file', $("#change-image")[0].files[0]);
        axios.post("/api/upload/upload-image", formData).then(function (res) {
            NProgress.done();
            if (res.data.success) {
                $('.product-image').attr('src', res.data.link);

            }
        }, function(err) {
            NProgress.done();
        });
    });

    $("#new-image").on("click", function () {
        dataProduct = {};
        dataProduct.productId = $(this).data("product");
        $('.product-image').attr('src', 'https://www.vietnamprintpack.com/images/default.jpg');

    });


    $(".edit-image").on("click", function () {
        var pdInfo = $(this).data("image");
        console.log(pdInfo);
        NProgress.start();
        axios.get("/api/product-image/detail/" + pdInfo).then(function(res){
            NProgress.done();
            if(res.data.success) {
                dataProduct.id = res.data.data.id;
                dataProduct.productId = res.data.data.productId;
                console.log(dataProduct);
                if(res.data.data.link != null) {
                    $('.product-image').attr('src', res.data.data.link);
                }
            }else {
                console.log("Lỗi");
            }
        }, function(err){
            NProgress.done();
        })
    });

    $(".btn-save-image").on("click", function () {

        dataProduct.productId = $(this).data("product");
        dataProduct.link = $('.product-image').attr('src');
        NProgress.start();
        console.log(dataProduct);
        var linkPost = "/api/product-image/create";
        if(dataProduct.id) {
            linkPost = "/api/product-image/update/" + dataProduct.id;
        }

        axios.post(linkPost, dataProduct).then(function(res){
            NProgress.done();
            if(res.data.success) {
                swal(
                    'Thành Công!',
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
        })
    });


});