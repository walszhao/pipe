$(function () {
    //修改个人信息
    $('#updateUserNameButton').click(function () {
        $("#updateUserNameButton").attr("disabled",true);

        var id = $('#id').val();
        var userName = $('#username').val();
        var nickName = $('#nickName').val();
        debugger;
        if (validUserNameForUpdate(userName, nickName)) {
            //ajax提交数据
            var params = {
                "id":id,
                "username":userName,
                "nickName": nickName
            };
            $.ajax({
                type: "POST",
                url: "/updateAdminUserInfo",
                data: JSON.stringify(params),
                contentType: "application/json",
                success: function (r) {
                    console.log(r);
                    if (r.success) {

                        $("#updateUserNameButton").attr("disabled",false);
                        swal("修改成功！", {
                            icon: "success",
                        });

                    } else {
                        swal("修改失败！", {
                            icon: "error",
                        });
                    }
                }
            });
        }
    });
    //修改密码
    $('#updatePasswordButton').click(function () {
        $("#updatePasswordButton").attr("disabled",true);

        var id = $('#id01').val();
        var originalPassword = $('#originalPassword').val();
        var newPassword = $('#newPassword').val();
        if (validPasswordForUpdate(originalPassword, newPassword)) {
            var params = {
                "id":id,
                "originalPassword":originalPassword,
                "newPassword": newPassword
            };
            $.ajax({
                type: "POST",
                url: "/updateAdminUserInfo",
                data: JSON.stringify(params),
                contentType: "application/json",
                success: function (r) {
                    console.log(r);
                    if (r.success) {
                        /*swal("修改成功！", {
                            icon: "success",
                        });*/


                        swal("修改成功！",{
                            icon: "success",
                            buttons: {
                                confirm: "确定"
                            },
                        }).then((flag) => {
                            window.location.href = '/login';
                        });
                    } else {
                        swal("修改失败！", {
                            icon: "error",
                        });
                    }
                }
            });
        }
    });
})

/**
 * 名称验证
 */
function validUserNameForUpdate(userName, nickName) {
    if (isNull(userName) || userName.trim().length < 1) {
        $('#updateUserName-info').css("display", "block");
        $('#updateUserName-info').html("请输入登陆名称！");
        return false;
    }
    if (isNull(nickName) || nickName.trim().length < 1) {
        $('#updateUserName-info').css("display", "block");
        $('#updateUserName-info').html("昵称不能为空！");
        return false;
    }
    if (!validUserName(userName)) {
        $('#updateUserName-info').css("display", "block");
        $('#updateUserName-info').html("请输入符合规范的登录名！");
        return false;
    }
    if (!validCN_ENString2_18(nickName)) {
        $('#updateUserName-info').css("display", "block");
        $('#updateUserName-info').html("请输入符合规范的昵称！");
        return false;
    }
    return true;
}

/**
 * 密码验证
 */
function validPasswordForUpdate(originalPassword, newPassword) {
    if (isNull(originalPassword) || originalPassword.trim().length < 1) {
        $('#updatePassword-info').css("display", "block");
        $('#updatePassword-info').html("请输入原密码！");
        return false;
    }
    if (isNull(newPassword) || newPassword.trim().length < 1) {
        $('#updatePassword-info').css("display", "block");
        $('#updatePassword-info').html("新密码不能为空！");
        return false;
    }
    if (!validPassword(newPassword)) {
        $('#updatePassword-info').css("display", "block");
        $('#updatePassword-info').html("请输入符合规范的密码！");
        return false;
    }
    return true;
}
