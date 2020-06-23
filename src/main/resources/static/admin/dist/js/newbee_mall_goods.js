$(function () {
    $("#jqGrid").jqGrid({
        url: '/product/goods/list',
        datatype: "json",
        colModel: [
            {label: '商品编号', name: 'id', index: 'id', width: 60, key: true, align: 'center',sortable:false},
            {label: '商品名程', name: 'productName', index: 'productName', width: 120, align: 'center',sortable:false},
            {label: '商品图片', name: 'logoImgUrl', index: 'logoImgUrl', width: 120, align: 'center' ,sortable:false,formatter: coverImageFormatter},
            {label: '商品售价', name: 'presentPrice', index: 'presentPrice', align: 'center', width: 60,sortable:false},
            {
                label: '上架状态',
                name: 'status',
                index: 'status',
                width: 80,
                align: 'center',
                sortable:false,
                formatter: goodsSellStatusFormatter
            }
        ],
        height: 760,
        rowNum: 20,
        rowList: [20, 50, 80],
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
            $("thead th").css("text-align", "center");
        }
    });
    $(window).resize(function () {
        $("#jqGrid").setGridWidth($(".card-body").width());
    });

    function goodsSellStatusFormatter(cellvalue) {
        //商品上架状态 0-上架 1-下架
        if (cellvalue == 2) {
            return "<div style=\"position: relative; width: 80%;\"><button type=\"button\" class=\"btn btn-block btn-success btn-sm\" style=\"position: absolute;top: 50%;left: 50%;height: 30px;width: 50%;margin: 1% 0 0 -12%;\">销售中</button></div>";
        }
        if (cellvalue == 1) {
            return "<div style=\"position: relative; width: 80%;\"><button type=\"button\" class=\"btn btn-block btn-secondary btn-sm\" style=\"position: absolute;top: 50%;left: 50%;height: 30px;width: 50%;margin: 1% 0 0 -12%;\">已下架</button></div>";
        }
    }

    function coverImageFormatter(cellvalue) {
        return "<img src='" + cellvalue + "' height=\"80\" width=\"80\" alt='商品主图'/>";
    }

});

/**
 * jqGrid重新加载
 */
function reload() {
    initFlatPickr();
    var page = $("#jqGrid").jqGrid('getGridParam', 'page');
    $("#jqGrid").jqGrid('setGridParam', {
        page: page
    }).trigger("reloadGrid");
}

/**
 * 添加商品
 */
function addGoods() {
    window.location.href = "/product/goods/edit";
}

/**
 * 修改商品
 */
function editGoods() {
    var id = getSelectedRow();
    if (id == null) {
        return;
    }
    debugger;
    window.location.href = "/product/goods/edit/" + id;
}

function deleteGoods() {

    var ids= getSelectedRows();
    if (ids == null) {
        return;
    }

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
                    url: "/product/deleteProduct",
                    contentType: "application/json",
                    data: {"ids":JSON.stringify(ids)},
                    success: function (r) {
                        debugger;
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
    );
}

/**
 * 上架
 */
function putUpGoods() {
    var ids = getSelectedRows();
    if (ids == null) {
        return;
    }
    swal({
        title: "确认弹框",
        text: "确认要执行上架操作吗?",
        icon: "warning",
        buttons: {
            cancel: "取消",
            confirm: "确定"
        },
    }).then((flag) => {
            if (flag) {
                $.ajax({
                    type: "GET",
                    url: "/product/online",
                    contentType: "application/json",
                    data: {"ids":JSON.stringify(ids)},
                    success: function (r) {
                        if (r.success) {
                            swal("上架成功", {
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

/**
 * 下架
 */
function putDownGoods() {
    var ids = getSelectedRows();
    if (ids == null) {
        return;
    }
    swal({
        title: "确认弹框",
        text: "确认要执行下架操作吗?",
        icon: "warning",
        buttons: {
            cancel: "取消",
            confirm: "确定"
        },
    }).then((flag) => {
            if (flag) {
                $.ajax({
                    type: "GET",
                    url: "/product/offline",
                    contentType: "application/json",
                    data: {"ids":JSON.stringify(ids)},
                    success: function (r) {
                        if (r.success) {
                            swal("下架成功", {
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



/**
 * 获取jqGrid选中的多条记录
 * @returns {*}
 */
function getSelectedRows() {
    var grid = $("#jqGrid");
    var rowKey = grid.getGridParam("selrow");
    if (!rowKey) {
        swal("请选择一条记录", {
            icon: "warning",
        });
        return;
    }
    return grid.getGridParam("selarrrow");
}