//KindEditor变量
var editor;
var uploader;
var logoUrl;
var goodsUploader;
var goodsPicUrl = [];
$(function () {
    var $ = jQuery,
        $list = $('#fileList'),
        // 优化retina, 在retina下这个值是2
        ratio = 1 || 1,

        // 缩略图大小
        thumbnailWidth = 100 * ratio,
        thumbnailHeight = 100 * ratio;



    var productInfo = getProductInfo();

    debugger;
    if (productInfo != null){
        picDisplay(productInfo);
    }


    //详情编辑器
    editor = KindEditor.create('textarea[id="editor"]', {
        items: ['undo', 'redo', '|', 'preview',   'cut', 'copy', 'paste',
            'plainpaste', 'wordpaste', '|', 'justifyleft', 'justifycenter', 'justifyright',
            'justifyfull', 'insertorderedlist', 'insertunorderedlist', 'indent', 'outdent', 'subscript',
            'superscript', 'quickformat', 'selectall', '|', 'fullscreen', '/',
            'formatblock', 'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold',
            'italic', 'underline', 'strikethrough', 'lineheight', 'removeformat', '|', 'multiimage',
            'table', 'hr', 'emoticons', 'baidumap', 'pagebreak',
            'anchor', 'link', 'unlink'],
        uploadJson: '/admin/upload/file',
        filePostName: 'file'
    });

    /*new AjaxUpload('#uploadGoodsCoverImg', {
        action: '/admin/upload/file',
        name: 'file',
        autoSubmit: true,
        responseType: "json",
        onSubmit: function (file, extension) {
            if (!(extension && /^(jpg|jpeg|png|gif)$/.test(extension.toLowerCase()))) {
                alert('只支持jpg、png、gif格式的文件！');
                return false;
            }
        },
        onComplete: function (file, r) {
            if (r != null && r.success == true) {
                $("#logoImgUrl").attr("src", r.data);
                $("#logoImgUrl").attr("style", "width: 128px;height: 128px;display:block;");
                return false;
            } else {
                alert("error");
            }
        }
    });*/


    // 初始化Web Uploader
    uploader = WebUploader.create({

        // 选完文件后，是否自动上传。
        auto: true,

        // swf文件路径
        swf:'/admin/plugins/webuploader/Uploader.swf',

        // 文件接收服务端。
        server: '/admin/upload/file',

        // 选择文件的按钮。可选。
        // 内部根据当前运行是创建，可能是input元素，也可能是flash.
        pick: '#filePicker',
        threads:3,
        fileNumLimit:1,

        formData: {
            destDir: "user"
        },
        // 不压缩image
        resize: false,

        // 只允许选择图片文件。
        accept: {
            title: 'Images',
            extensions: 'gif,jpg,jpeg,bmp,png',
            mimeTypes: 'image/*'
        }
    });



    // 当有文件添加进来的时候
    uploader.on('fileQueued', function (file) {
        debugger;
        var $list = $('#fileList');
        var $li = $(
            '<div id="' + file.id + '" class="file-item thumbnail">' +
            '<p class="imgWrap"><img></p>' +
            '<div class="info">' + file.name + '</div>' +
            '</div>'
            ),
            $img = $li.find('img');
        var $btns = $('<div class="file-panel">' +
            '<span class="cancel" >删除</span>').appendTo($li);
        $li.on('mouseenter', function () {
            $btns.stop().animate({height: 30});
        });
        $li.on('mouseleave', function () {
            $btns.stop().animate({height: 0});
        });
        // $list为容器jQuery实例
        $list.append($li);
        $btns.on('click', 'span', function () {
            var index = $(this).index();
            switch (index) {
                case 0:
                    uploader.removeFile(file);
                    removeFile(file);
                    return;
            }
        });
        // 创建缩略图
        // 如果为非图片文件，可以不用调用此方法。
        // thumbnailWidth x thumbnailHeight 为 100 x 100
        uploader.makeThumb(file, function (error, src) {
            if (error) {
                $img.replaceWith('<span>不能预览</span>');
                return;
            }
            $img.attr('src', src);
        }, thumbnailWidth, thumbnailHeight);
    });
    uploader.on('beforeFileQueued',function (file) {
        //限制个数
        var fileNumLimit =uploader.options.fileNumLimit;
        var completeNum = uploader.getFiles("complete").length;
        if (fileNumLimit == completeNum){
            $.fn.modalAlert("最大上传"+fileNumLimit+"个文件，不能超过","error");
        }
    });
    // 文件上传过程中创建进度条实时显示。
    /*uploader.on('uploadProgress', function (file, percentage) {
        var $li = $('#' + file.id),
            $percent = $li.find('.progress span');
        // 避免重复创建
        if (!$percent.length) {
            $percent = $('<p class="progress"><span></span></p>')
                .appendTo($li)
                .find('span');
        }
        $percent.css('width', percentage * 100 + '%');
    });*/
    // 文件上传成功，给item添加成功class, 用样式标记上传成功。
    uploader.on('uploadSuccess', function (file, response) {
        //photoList.push(JSON.parse(response._raw));
        //$("#photo").val(JSON.stringify(photoList));
        //$("#logoImgUrl").val(response._raw);
        debugger;

        var result   = JSON.parse(response._raw);
        logoUrl = result.data;
        var fileStatusnum = uploader.getStats();
        // $.fn.modalMsg("上传成功"+fileStatusnum.successNum+"个文件","success");
        //$.fn.modalMsg("上传成功", "success");
        $('#' + file.id).addClass('upload-state-done');

    });

    // 文件上传失败，显示上传出错。
    uploader.on('uploadError', function (file) {
        //alert("失败");
        var $li = $('#' + file.id),
            $error = $li.find('div.error');
        // 避免重复创建
        if (!$error.length) {
            $error = $('<div class="error"></div>').appendTo($li);
        }
        $error.text('上传失败');
        var fileStatusnum = uploader.getStats();
        layer.msg("上传失败" + fileStatusnum.uploadFailNum + "个文件", {icon: 1, time: 1000});
    });

    // 完成上传完了，成功或者失败，先删除进度条。
    uploader.on('uploadComplete', function (file) {
        $('#' + file.id).find('.progress').remove();
    });

    // 负责view的销毁
    function removeFile( file ) {

        var $li = $('#'+file.id);
        $li.off().find('.file-panel').off().end().remove();
    }




    // 初始化Web Uploader
    goodsUploader = WebUploader.create({

        // 选完文件后，是否自动上传。
        auto: true,

        // swf文件路径
        swf:'/admin/plugins/webuploader/Uploader.swf',

        // 文件接收服务端。
        server: '/admin/upload/file',

        // 选择文件的按钮。可选。
        // 内部根据当前运行是创建，可能是input元素，也可能是flash.
        pick: '#filePicPicker',
        threads:3,

        formData: {
            destDir: "user"
        },
        // 不压缩image
        resize: false,

        // 只允许选择图片文件。
        accept: {
            title: 'Images',
            extensions: 'gif,jpg,jpeg,bmp,png',
            mimeTypes: 'image/*'
        }
    });

    // 当有文件添加进来的时候
    goodsUploader.on('fileQueued', function (file) {
        var $list = $('#filePicList');
        var $li = $(
            '<div id="' + file.id + '" class="file-item thumbnail">' +
            '<p class="imgWrap"><img></p>' +
            '<div class="info">' + file.name + '</div>' +
            '</div>'
            ),
            $img = $li.find('img');
        var $btns = $('<div class="file-panel">' +
            '<span class="cancel" >删除</span>').appendTo($li);
        $li.on('mouseenter', function () {
            $btns.stop().animate({height: 30});
        });
        $li.on('mouseleave', function () {
            $btns.stop().animate({height: 0});
        });
        // $list为容器jQuery实例
        $list.append($li);
        $btns.on('click', 'span', function () {
            var index = $(this).index();
            switch (index) {
                case 0:
                    goodsUploader.removeFile(file);
                    removeGoodsFile(file);
                    return;
            }
        });
        // 创建缩略图
        // 如果为非图片文件，可以不用调用此方法。
        // thumbnailWidth x thumbnailHeight 为 100 x 100
        goodsUploader.makeThumb(file, function (error, src) {
            if (error) {
                $img.replaceWith('<span>不能预览</span>');
                return;
            }
            $img.attr('src', src);
        }, thumbnailWidth, thumbnailHeight);
    });
    /*uploader.on('beforeFileQueued',function (file) {
        //限制个数
        var fileNumLimit =uploader.options.fileNumLimit;
        var completeNum = uploader.getFiles("complete").length;
        if (fileNumLimit == completeNum){
            $.fn.modalAlert("最大上传"+fileNumLimit+"个文件，不能超过","error");
        }
        /!*console.log("上传限制"+);
        console.log("上传成功"+);*!/
    });*/
    // 文件上传过程中创建进度条实时显示。
    /*uploader.on('uploadProgress', function (file, percentage) {
        var $li = $('#' + file.id),
            $percent = $li.find('.progress span');
        // 避免重复创建
        if (!$percent.length) {
            $percent = $('<p class="progress"><span></span></p>')
                .appendTo($li)
                .find('span');
        }
        $percent.css('width', percentage * 100 + '%');
    });*/
    // 文件上传成功，给item添加成功class, 用样式标记上传成功。
    goodsUploader.on('uploadSuccess', function (file, response) {
        //photoList.push(JSON.parse(response._raw));
        //$("#photo").val(JSON.stringify(photoList));
        //$("#logoImgUrl").val(response._raw);

        var result  = JSON.parse(response._raw);
        goodsPicUrl.push(result.data);
        debugger;
        console.log(response);
        var fileStatusnum = goodsUploader.getStats();
        // $.fn.modalMsg("上传成功"+fileStatusnum.successNum+"个文件","success");
        //$.fn.modalMsg("上传成功", "success");
        $('#' + file.id).addClass('upload-state-done');

    });

    // 文件上传失败，显示上传出错。
    goodsUploader.on('uploadError', function (file) {
        //alert("失败");
        var $li = $('#' + file.id),
            $error = $li.find('div.error');
        // 避免重复创建
        if (!$error.length) {
            $error = $('<div class="error"></div>').appendTo($li);
        }
        $error.text('上传失败');
        var fileStatusnum = uploader.getStats();
        layer.msg("上传失败" + fileStatusnum.uploadFailNum + "个文件", {icon: 1, time: 1000});
    });

    // 完成上传完了，成功或者失败，先删除进度条。
    goodsUploader.on('uploadComplete', function (file) {
        $('#' + file.id).find('.progress').remove();
    });

    // 负责view的销毁
    function removeGoodsFile( file ) {
        debugger;
        var $li = $('#'+file.id);
        goodsPicUrl.splice($li.index(),1);
        $li.off().find('.file-panel').off().end().remove();
    }


});

$('#confirmButton').click(function () {
    var productName = $('#productName').val();
    var presentPrice = $('#presentPrice').val();
    var classifyId = $('#classifyId option:selected').val();
    var status = $("input[name='status']:checked").val();
    var detail = editor.html();
    if (isNull(classifyId)) {
        swal("请选择分类", {
            icon: "error",
        });
        return;
    }
    if (isNull(productName)) {
        swal("请输入商品名称", {
            icon: "error",
        });
        return;
    }
    if (!validLength(productName, 100)) {
        swal("商品名称过长", {
            icon: "error",
        });
        return;
    }
    if (isNull(presentPrice) || presentPrice < 0) {
        swal("请输入商品价格", {
            icon: "error",
        });
        return;
    }

    if (isNull(status)) {
        swal("请选择上架状态", {
            icon: "error",
        });
        return;
    }
    if (isNull(detail)) {
        swal("请输入商品介绍", {
            icon: "error",
        });
        return;
    }
    if (!validLength(detail, 50000)) {
        swal("商品介绍内容过长", {
            icon: "error",
        });
        return;
    }
    $('#goodsModal').modal('show');
});

$('#saveButton').click(function () {
    var productId = $("#id").val();
    var productName = $('#productName').val();
    var presentPrice = $('#presentPrice').val();
    var classifyId = $('#classifyId option:selected').val();
    var status = $("input[name='status']:checked").val();
    var detail = editor.html();
    if (isNull(logoUrl)) {
        swal("封面图片不能为空", {
            icon: "error",
        });
        return;
    }

    if (goodsPicUrl.length <= 0) {
        swal("封面图片不能为空", {
            icon: "error",
        });
        return;
    }
    var url = '/product/saveProduct';
    var swlMessage = '保存成功';
    var data = {
        "productName": productName,
        "classifyId": classifyId,
        "presentPrice": presentPrice,
        "detail": detail,
        "logoImgUrl": logoUrl,
        "status": status,
        "bannerList": goodsPicUrl
    };
    if (productId > 0) {
        url = '/product/updateProduct';
        swlMessage = '修改成功';
        data = {
            "id":productId,
            "productName": productName,
            "classifyId": classifyId,
            "presentPrice": presentPrice,
            "detail": detail,
            "logoImgUrl": logoUrl,
            "status": status,
            "bannerList": goodsPicUrl
        };
    }
    console.log(data);
    $.ajax({
        type: 'POST',//方法类型
        url: url,
        contentType: 'application/json',
        data: JSON.stringify(data),
        success: function (result) {
            if (result.success == true) {
                $('#goodsModal').modal('hide');
                swal({
                    title: swlMessage,
                    type: 'success',
                    showCancelButton: false,
                    confirmButtonColor: '#1baeae',
                    confirmButtonText: '返回商品列表',
                    confirmButtonClass: 'btn btn-success',
                    buttonsStyling: false
                }).then(function () {
                    window.location.href = "/index";
                })
            } else {
                $('#goodsModal').modal('hide');
                swal(result.message, {
                    icon: "error",
                });
            }
            ;
        },
        error: function () {
            swal("操作失败", {
                icon: "error",
            });
        }
    });
});

$('#cancelButton').click(function () {
    window.location.href = "/index";
});

$('#goodsModal').on('shown.bs.modal', function () {
    uploader.refresh();
});

$("#ctlBtn").click(function(){
    uploader.upload();
});


$('#nextButton').click(function () {

    if (isNull(logoUrl)) {
        swal("商品主图不可为空", {
            icon: "error",
        });
        return;
    }
    $('#goodsModal').modal('hide');
    $('#goodsPicModal').modal('show');
});

$('#goodsPicModal').on('shown.bs.modal', function () {
    goodsUploader.refresh();
});


function picDisplay (productInfo) {
    var logoUrl = productInfo.logoImgUrl;
    var logoPathArr = [];
    logoPathArr.push(logoUrl);

    $.each(logoPathArr, function(index,item){

        getFileObject(item, function (fileObject) {

            var wuFile = new WebUploader.Lib.File(WebUploader.guid('rt_'),fileObject);

            var file = new WebUploader.File(wuFile);

            uploader.addFiles(file)

        })

    });


    var bannerArr = productInfo.productBannerList;

    var bannerUrlArr = [];

    for (let i = 0; i < bannerArr.length; i++) {
        bannerUrlArr.push(bannerArr[i].imgUrl);
    }
    $.each(bannerUrlArr, function(index,item){

        getFileObject(item, function (fileObject) {

            var wuFile = new WebUploader.Lib.File(WebUploader.guid('rt_'),fileObject);

            var file = new WebUploader.File(wuFile);

            goodsUploader.addFiles(file)

        })

    });



}


var getFileBlob = function (url, cb) {

    var xhr = new XMLHttpRequest();

    xhr.open("GET", url);

    xhr.responseType = "blob";

    xhr.addEventListener('load', function() {

        cb(xhr.response);

    });

    xhr.send();

};



var blobToFile = function (blob, name) {

    blob.lastModifiedDate = new Date();

    blob.name = name;

    return blob;

};



var getFileObject = function(filePathOrUrl, cb) {

    getFileBlob(filePathOrUrl, function (blob) {

        cb(blobToFile(blob, 'test.jpg'));

    });

};



//需要编辑的图片列表

/*var picList = ['图片url','图片url','图片url','图片url' ]

$.each(picList, function(index,item){

    getFileObject(item, function (fileObject) {

        var wuFile = new WebUploader.Lib.File(WebUploader.guid('rt_'),fileObject);

        var file = new WebUploader.File(wuFile);

        uploader.addFiles(file)

    })

});*/

function getProductInfo() {
    var result;
    var productId = $("#id").val();
    $.ajax({
        type: "GET",
        url: "/product/getProductById",
        contentType: "application/json",
        data: {"id":productId},
        async: false,
        success: function (r) {
            if (r.success) {
                result =  r.data
            } else {
                console.log("err");
            }
        }
    });
    return result;
}

