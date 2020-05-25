$(document).ready(function () {
    var dataProduct={};


    $("#new-category").on("click", function () {
        dataProduct = {};
        $('#input-category-name').val("");
        $('#input-category-desc').val("");

    });

    $(".edit-category").on("click", function () {
        var pdInfo = $(this).data("category");
        console.log(pdInfo);
        NProgress.start();
        axios.get("/api/category/detail/" + pdInfo).then(function(res){
            NProgress.done();
            if(res.data.success) {
                dataProduct.id = res.data.data.id;
                $("#input-category-name").val(res.data.data.name);
                $("#input-category-desc").val(res.data.data.shortDesc);
            }else {
                console.log("Lỗi");
            }
        }, function(err){
            NProgress.done();
        })
    });

    $(".btn-save-category").on("click", function () {
        if($("#input-category-name").val() === "" || $("#input-category-desc").val() === "") {
            swal(
                'Lỗi',
                'Bạn phải điền đầy đủ',
                'error'
            );
            return;
        }


        dataProduct.name = $('#input-category-name').val();
        dataProduct.shortDesc = $('#input-category-desc').val();
        NProgress.start();
        console.log(dataProduct.id);
        var linkPost = "/api/category/create";
        if(dataProduct.id) {
            linkPost = "/api/category/update/" + dataProduct.id;
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