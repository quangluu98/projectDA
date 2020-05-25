$(document).ready(function () {
    var dataNews={};


    function readURL(input) {
        if(input.files && input.files[0]) {
            var reader =new FileReader();
            reader.onload= function (e) {
                $('#preview-product-img').attr('src', e.target.result);
            }
            reader.readAsDataURL(input.files[0]);
        }

    }

    $("#change-news-mainImage").change(function () {
        readURL(this);
        var formData = new FormData();
        NProgress.start();
        formData.append('file', $("#change-news-mainImage")[0].files[0]);
        axios.post("/api/upload/upload-image", formData).then(function (res) {
            NProgress.done();
            if (res.data.success) {
                $('.news-main-image').attr('src', res.data.link);

            }
        }, function(err) {
            NProgress.done();
        });
    });

    $("#new-news").on("click", function () {
        dataNews = {};
        $('#input-news-title').val("");
        $('#input-news-desc').val("");
        $("#input-news-content").val("");
        $("#input-news-isHot").val("");
        $('.news-main-image').attr('src', 'https://dapp.dblog.org/img/default.jpg');

    });

    $(".edit-news").on("click", function () {
        var pdInfo = $(this).data("news");
        console.log(pdInfo);
        NProgress.start();
        axios.get("/api/news/detail/" + pdInfo).then(function(res){
            NProgress.done();
            if(res.data.success) {
                dataNews.id = res.data.data.id;
                $("#input-news-title").val(res.data.data.title);
                $("#input-news-desc").val(res.data.data.shortDesc);
                $("#input-news-content").val(res.data.data.content);
                $("#input-news-isHot").val(res.data.data.isHot);
                if(res.data.data.mainImage != null) {
                    $('.news-main-image').attr('src', res.data.data.mainImage);
                }
            }else {
                console.log("Lỗi");
            }
        }, function(err){
            NProgress.done();
        })
    });

    $(".btn-save-news").on("click", function () {
        if($("#input-news-title").val() === "" ||
            $("#input-news-desc").val() === "" ||
            $("#input-news-isHot").val() === "" ||
            $("#input-news-content").val()==="") {
            swal(
                'Lỗi',
                'Bạn phải điền đầy đủ',
                'error'
            );
            return;
        }


        dataNews.title = $('#input-news-title').val();
        dataNews.shortDesc = $('#input-news-desc').val();
        dataNews.mainImage = $('.news-main-image').attr('src');
        dataNews.content = $("#input-news-content").val();
        dataNews.isHot = $("#input-news-isHot").val();
        NProgress.start();
        console.log(dataNews.id);
        var linkPost = "/api/news/create";
        if(dataNews.id) {
            linkPost = "/api/news/update/" + dataNews.id;
        }

        axios.post(linkPost, dataNews).then(function(res){
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