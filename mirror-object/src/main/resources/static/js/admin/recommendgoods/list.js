$(document).ready(function () {

    var goods_list=[];
    var inited = false;

    // >>构建服装树
    var initGoodsClassTree = function () {
        Request.get("goodsclass/currentNodeTree",function(e){
            goods_list = e;
            var tree = goodsTree.init();
            var rootNode = tree.getRootNodes(e);
            $('#base_tree').treeview({
                data:rootNode,
                selectedBackColor: "#07100e",
                onNodeSelected:function (event,data) {
                    baseTable.ajax.reload().draw();
                }
            });
            $('#base_tree').treeview('selectNode', [0]);
        });
    }
    var goodsTree={
        init: function () {
            if (inited) return this;
            if (jQuery === undefined) {
                console.error("Required jQuery support is not available");
            } else {
                inited = true;
                var that = this;
            }
            return this;
        },
        load: function () {

        },
        reload: function () {

        },
        getRootNodes: function (data) {
            var that = this;
            var result = [];
            $.each(data.data, function (index, item) {
                if (item['parentId'] == '0') {
                    var obj = {
                        id: item.id,
                        level: item.level,
                        parentId: item.parentId,
                        text: item.className,
                        nodes: []
                    };
                    obj.nodes = that.getChildNodes(data.data, item);
                    result.push(obj);
                }
            });
            return result;
        },
        getChildNodes: function (data, parentNode) {
            var that = this;
            var result = [];
            $.each(data, function (i, item) {
                if (item['parentId'] == parentNode['id']) {
                    var obj = {
                        id: item.id,
                        level:item.level,
                        parentId: item.parentId,
                        text: item.className,
                        nodes: null
                    };
                    result.push(obj);
                    var childNodes = that.getChildNodes(data, item);
                    if (childNodes != null && childNodes.length > 0) {
                        obj.nodes = childNodes;
                    }
                }
            });
            return result;
        }
    };
    initGoodsClassTree();
    window.goodsTree = goodsTree.init();


    //  服装基本信息列表
    var baseTable = $("#base_data_table").DataTable({
        "language": lang,
        "paging": true,
        "lengthChange": true,
        "searching": true,
        "ordering": false,
        "info": true,
        "autoWidth": false,
        "bStateSave": true,
        "bFilter": true, //搜索栏
        "bSort": false,
        "sPaginationType": "full_numbers",
        "ajax": function (data, callback) {
            var param = {};
            param.pageSize = data.length;
            param.pageIndex = data.start;
            param.page = (data.start / data.length) + 1;
            var selected = $('#base_tree').treeview('getSelected');
            if (selected == null || selected.length == 0) {
                return;
            }
            if(selected[0].id!="base_tree" && selected[0].level!==undefined) {
                $.ajax({
                    url: BASE_PATH + "recommendGoods/queryGoodsByClassId/" + selected[0].id + "/" + selected[0].level,
                    type: "GET",
                    cache: false,
                    data: param,
                    dataType: "json",
                    success: function (result) {
                        var resultData = {};
                        resultData.draw = data.draw;
                        resultData.recordsTotal = result.total;
                        resultData.recordsFiltered = result.total;
                        resultData.data = result.data;
                        callback(resultData);
                    },
                    error: function () {
                        toastr.warning("请求列表数据失败, 请重试");
                    }
                });
            }
        },
        columns: [
            {
                "data": "id",
                bSortable: false,
                targets: 0,
                width: "30px",
                render: function (data, type, row, meta) {
                    // 显示行号
                    var startIndex = meta.settings._iDisplayStart;
                    return startIndex + meta.row + 1;
            }},
            {"data": "goodsName"},
            {"data": "status"},
            {"data": "recommendTime"}
        ],
        "aoColumnDefs": [
            {
                "sClass": "center",
                "aTargets": [4],
                "mData": "id",
                "searchable": false,
                "mRender": function (a, b, c, d) {//a表示statCleanRevampId对应的值，c表示当前记录行对象
                    // 修改 删除 权限判断
                    var buttons = '';
                    if(c.status == 0){
                         buttons+= '<button type="button" data-id="'+a+'" class="btn btn-success btn-xs btn-recommend">商品推荐</button>';
                    }
                    else {
                         buttons+= '<button type="button" data-id="'+a+'" class="btn btn-success btn-xs btn-cancel-recommend">取消商品推荐</button>';
                    }
                    return buttons;

                }
            }
        ],
        fnRowCallback: function (nRow, aData, iDataIndex) {
            var status = aData.status;
            var html='';
            if (status == 0) {
                html = '未推荐';
            }
            else if(status == 1){
                html = '已推荐';
            }
            $('td:eq(2)', nRow).html(html);
            return nRow;
        }
    });

    // >>商品推荐
    $("#base_data_table").off('click', '.btn-cancel-recommend').on('click', '.btn-cancel-recommend', function () {
        var that = $(this);
        var id = that.data('id');
        user_id = id;
        toastr.info("商品取消推荐中...");
        Request.put("recommendGoods/" + id + "/recommend", {}, function (e) {
            if (e.success) {
                toastr.info("商品取消推荐成功!");
                baseTable.draw();
                baseTable.ajax.reload();
            } else {
                toastr.error(e.message);
            }
        });

    });

    // >>取消商品推荐
    $("#base_data_table").off('click', '.btn-recommend').on('click', '.btn-recommend', function () {
        var that = $(this);
        var id = that.data('id');
        toastr.info("商品推荐中...");
        Request.put("recommendGoods/" + id + "/recommend", {}, function (e) {
            if (e.success) {
                toastr.info("商品上架成功!");
                baseTable.draw();
                baseTable.ajax.reload();
            } else {
                toastr.error(e.message);
            }
        });
    });

});