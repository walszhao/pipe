$(function () {

    $("#jqGrid").jqGrid({
        url: '/classify/getClassifyListPage',
        datatype: "json",
        colModel: [
            {label: 'id', name: 'id', index: 'id', width: 50, key: true, hidden: true},
            {label: '分类名称', name: 'classifyName', index: 'classifyName', width: 240},
            {label: '排序值', name: 'classifyIndex', index: 'classifyIndex', width: 120}
        ],
        height: 560,
        rowNum: 10,
        rowList: [10, 20, 50],
        styleUI: 'Bootstrap',
        loadtext: '信息读取中...',
        rownumbers: false,
        rownumWidth: 20,
        autowidth: true,
        multiselect: true,
        pager: "#jqGridPager",
        jsonReader: {
            root: "data.list",
            page: "data.currPage",
            total: "data.totalPage",
            records: "data.totalCount"
        },
        prmNames: {
            page: "page",
            rows: "limit",
            order: "order",
        },
        gridComplete: function () {
            //隐藏grid底部滚动条
            $("#jqGrid").closest(".ui-jqgrid-bdiv").css({"overflow-x": "hidden"});
        }
    });

    $(window).resize(function () {
        $("#jqGrid").setGridWidth($(".card-body").width());
    });
});

/**
 * jqGrid重新加载
 */
function reload() {
    var page = $("#jqGrid").jqGrid('getGridParam', 'page');
    $("#jqGrid").jqGrid('setGridParam', {
        page: page
    }).trigger("reloadGrid");
}

function categoryAdd() {
    reset();
    $('.modal-title').html('分类添加');
    $('#categoryModal').modal('show');
}


//绑定modal上的保存按钮
$('#saveButton').click(function () {
    var classifyName = $("#classifyName").val();
    var classifyIndex = $("#classifyIndex").val();
    if (!validCN_ENString2_18(classifyName)) {
        $('#edit-error-msg').css("display", "block");
        $('#edit-error-msg').html("请输入符合规范的分类名称！");
    } else {
        var data = {
            "classifyName": classifyName,
            "classifyIndex": classifyIndex
        };
        var url = '/classify/insertCategories';
        var id = getSelectedRowWithoutAlert();
        if (id != null) {
            url = '/classify/updateClassify';
            data = {
                "id": id,
                "classifyName": classifyName,
                "classifyIndex": classifyIndex
            };
        }
        $.ajax({
            type: 'POST',//方法类型
            url: url,
            contentType: 'application/json',
            data: JSON.stringify(data),
            success: function (result) {
                if (result.success) {
                    $('#categoryModal').modal('hide');
                    swal("保存成功", {
                        icon: "success",
                    });
                    reload();
                } else {
                    $('#categoryModal').modal('hide');
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
    }
});

function categoryEdit() {
    reset();
    var id = getSelectedRow();
    if (id == null) {
        return;
    }
    var rowData = $("#jqGrid").jqGrid("getRowData", id);
    $('.modal-title').html('分类编辑');
    $('#categoryModal').modal('show');
    $("#id").val(id);
    $("#classifyName").val(rowData.classifyName);
    $("#classifyIndex").val(rowData.classifyIndex);
}

/**
 * 分类的删除会牵涉到多级分类的修改和商品数据的修改，因此暂时就不开放删除功能了，
 * 如果在商城页面不想显示相关分类可以通过调整rank值来调整显示顺序，
 * 不过代码我也写了一部分，如果想保留删除功能的话可以在此代码的基础上进行修改。
 */
function deleteCagegory() {

    var ids = getSelectedRows();
    if (ids == null) {
        return;
    }
    debugger;
    swal({
        title: "确认弹框",
        text: "确认要删除数据吗?",
        icon: "warning",
        buttons: {
            cancel: "取消",
            confirm: "确定"
        },

    }).then((flag) => {
        debugger
            if (flag) {
                $.ajax({
                    type: "GET",
                    url: "/classify/deleteClassify",
                    contentType: "application/json",
                    data: {"ids":JSON.stringify(ids)},
                    success: function (r) {
                        if (r.success) {
                            swal("删除成功", {
                                icon: "success",
                            });
                            $("#jqGrid").trigger("reloadGrid");
                        } else {
                            swal(r.message, {
                                icon: "error",
                            });
                        }
                    }
                });
            }
        }
    )
    ;
}


function reset() {
    $("#classifyName").val('');
    $("#classifyIndex").val(0);
    $('#edit-error-msg').css("display", "none");
}