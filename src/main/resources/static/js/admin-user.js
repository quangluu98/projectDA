$(document).ready(function () {
    var dataUser={};

    $("#new-user").on("click", function () {
        dataUser = {};
        $('#input-user-name').val("");
        $('#input-email').val("");
        $("#input-password").val("");
        $("#input-role").val("");

    });

    $(".btn-save-user").on("click", function () {
        if($("#input-user-name").val() === "" ||
            $("#input-email").val() === "" ||
            $("#input-password").val() === "" ||
            $("#input-role").val()==="") {
            swal(
                'Lỗi',
                'Bạn phải điền đầy đủ',
                'error'
            );
            return;
        }

        dataUser.userName = $('#input-user-name').val();
        dataUser.email = $('#input-email').val();
        dataUser.password = $('#input-password').val();
        dataUser.role = $('#input-role').val();

        NProgress.start();
        var linkPost = "/api/user/create";

        axios.post(linkPost, dataUser).then(function(res){
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

    $(".remove-user").on("click", function () {
        dataUser.id = $(this).data("remove");
        console.log(dataUser);
        NProgress.start();
        linkPost = "/api/user/delete/";
        axios.post(linkPost, dataUser).then(function(res){
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