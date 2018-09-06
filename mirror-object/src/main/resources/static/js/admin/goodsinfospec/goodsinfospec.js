$(document).ready(function () {
    var inited = false;
    var area_list = [];
    var shop_list = null;
    var shop_all_goods =null;
    var goods_list = null;
    //***************************构建区域 begin**********************
    var areaTree = {
        init: function () {
            if (inited) return this;
            if (jQuery === undefined) {
                console.error("Required jQuery support is not available");
            } else {
                inited = true;
                var that = this;
                $(function () {

                });
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
                        id: item.uId,
                        cid: index,
                        // level: 0,
                        parentId: item.parentId,
                        text: item.areaName,
                        level: item.level,
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
                if (item['parentId'] == parentNode['uId']) {
                    var obj = {
                        id: item.uId,
                        cid: i,
                        // level: parentNode[level]+1,
                        parentId: item.parentId,
                        text: item.areaName,
                        level: item.level,
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
    //***************************构建区域 end************************


    //***************************初始化begin*************************
    var initAreaTree = function () {
        Request.get("area/queryallviewtree", function (e) {
            area_list = e;
            var tree = areaTree.init();

            var rootNodes = tree.getRootNodes(e);
            $('#area_tree').treeview({
                data: rootNodes,
                selectedBackColor: "#07100e",
                levels: 3
            });


            $('#area_tree').on('nodeSelected', function (event, data) {
                var str = data.id + "";
                if (data.level == 1) {
                    shop(str.substr(0, 2));
                } else if (data.level == 2) {
                    shop(str.substr(0, 4));
                } else {
                    shop(str);
                }
            });
            $('#area_tree').treeview('selectNode', [0]);
        });
    };
    //***************************初始化end***************************
    initAreaTree();

    function shop(areaId) {
        shop_list = $('#shop_list').DataTable({
            "language": lang,
            "paging": true,
            "lengthChange": true,
            "searching": true,
            "ordering": false,
            "destroy": true,
            "info": true,
            "autoWidth": false,
            "bStateSave": true,
            "mark":{
                "exclude":[".exclude"]
            },
            "ajax": function (data, callback) {
                var param = {};
                param.pageSize = data.length;
                param.pageIndex = data.start;
                param.page = (data.start / data.length) + 1;
                $.ajax({
                    url: BASE_PATH + "shop/queryshopbyareaid/" + areaId,
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
                        if (resultData.data == null) {
                            resultData.data = [];
                        }
                        callback(resultData);
                    },
                    error: function () {
                        toastr.warning("请求列表数据失败, 请重试");
                    }
                });
            },
            columns: [
                {
                    "data": "uId",
                    "searchable":false,
                    "className":"exclude",
                    // targets: 0,
                    // width: "30px"
                    render: function (data, type, row, meta) {
                        // 显示行号
                        var startIndex = meta.settings._iDisplayStart;
                        return startIndex + meta.row + 1;
                    }
                },
                {data: "name"},
                {data: "userName"},
                {data: "userPhone"},
                {data: "address"},
                {data: "status"}
            ],
            "aoColumnDefs": [
                {"bSortable": false, "aTargets": [0]},
                {
                    "sClass": "center",
                    "aTargets": [6],
                    "mData": "id",
                    "mRender": function (a, b, c, d) {//c表示当前记录行对象
                        // 修改 删除 权限判断

                        var buttons = '<div class="btn-group">';
                        buttons += '<div class="btn-group">';
                        buttons += '<button type="button" class="btn btn-default  btn-sx dropdown-toggle" data-toggle="dropdown">操作';
                        buttons += '<span class="caret"></span></button>';
                        buttons += '<ul class="dropdown-menu">';
                        if (accessCreate) {
                            buttons += '<li><a href="javascript:;" data-id="' + c.uId + '" class=" btn-add-goods">关联服装</a></li>';
                        }
                        buttons += '<li><a href="javascript:;" data-id="' + c.uId + '" class="btn-goods">服装列表</a></li>';
                        buttons += '</ul></div></div>';
                        return buttons;
                    }
                }
            ],
            fnRowCallback: function (nRow, aData) {
                var status = aData.status;
                var html = '<span class="fa fa-circle text-error" aria-hidden="true" style="color: red" data-state = "' + status + '"></span>';
                if (status == 1) {
                    html = '<span class="fa fa-circle text-success" aria-hidden="true" style="color: #00e765"  data-state = "' + status + '"></span>';
                }
                $('td:eq(5)', nRow).html(html);
                return nRow;
            }


        });
    }

    //服装列表
    function no_goods_list (shopId){
        goods_list=   $("#class_list_table").DataTable({
            "language": lang,
            "paging": true,
            "lengthChange": true,
            "searching": true,
            "ordering": false,
            "destroy": true,
            "info": true,
            "autoWidth": false,
            "bStateSave": true,
            "mark":{
                "exclude":[".exclude"]
            },
            "ajax": function (data, callback) {
                var param = {};
                param.pageSize = data.length;
                param.pageIndex = data.start;
                param.page = (data.start / data.length) + 1;
                $.ajax({
                    url: BASE_PATH + "goodsinfo/allGoods/" +shopId,
                    type: "GET",
                    cache: false,
                    data: {},
                    dataType: "json",
                    success: function (result) {
                        var resultData = {};
                        resultData.draw = data.draw;
                        resultData.recordsTotal = result.data.length;
                        resultData.recordsFiltered = result.data.length;
                        resultData.data = result.data;
                        callback(resultData);
                    },
                    error: function (jqXhr) {
                    }
                });
            },
            columns: [
                {"data": "goodsId",
                    "width": "2%",
                    "render": function(data){
                        return  '<input class="checkChild" type="checkbox" name="'+data+'">';
                    }},//商品id
                {"data": "className"},
                {"data": "goodsName"},
            ]

        });
    }



// 服装列表
    $("#shop_list").off('click', '.btn-add-goods').on('click', '.btn-add-goods', function () {
      var  shop_id = $(this).data('id');
        //查询当前店铺没有关联的所有商品
        no_goods_list(shop_id);

        //商品店铺关联数组 数组第一个值为店铺id
        var selectLoans = [];
        selectLoans.push(shop_id);
        //批量处理店铺商品关联数据
        $("#modal-goods-info").modal('show');
        $('#submitBtn').off('click').on('click',function(){
            $('.checkChild').each(function () {
                if($(this).is(':checked')){
                    selectLoans.push($(this).attr('name'));
                }
            });
            if(selectLoans.length > 1){
                Request.post("shop/addGoodsShop", JSON.stringify(selectLoans), function (e) {
                    if (e.success) {
                        toastr.success("保存完毕");
                        $("#modal-goods-info").modal('hide');
                        //数据操作接口
                        console.log(selectLoans);
                    } else {
                        toastr.error(e.message);
                        $("#modal-goods-info").modal('hide');
                    }
                });
            }else {
                $("#modal-goods-info").modal('hide');
            }
        });
        goods_list.ajax.reload();
        shop_list.ajax.reload();
    });


//加载店铺所有在卖商品
    $("#shop_list").off('click', '.btn-goods').on('click', '.btn-goods', function () {
        var  shop_id = $(this).data('id');
        shop_all_goods = $("#shop-goods-info").DataTable({
            "language": lang,
            "paging": true,
            "lengthChange": true,
            "searching": true,
            "ordering": false,
            "destroy": true,
            "info": true,
            "autoWidth": false,
            "bStateSave": true,
            "mark":{
                "exclude":[".exclude"]
            },
            "ajax": function (data, callback) {
                var param = {};
                param.pageSize = data.length;
                param.pageIndex = data.start;
                param.page = (data.start / data.length) + 1;
                $.ajax({
                    url: BASE_PATH + "shop/selectAllShopGoods/"  + shop_id,
                    type: "GET",
                    cache: false,
                    data: {},
                    dataType: "json",
                    success: function (result) {
                        var resultData = {};
                        resultData.draw = data.draw;
                        resultData.recordsTotal = result.data.length;
                        resultData.recordsFiltered = result.data.length;
                        resultData.data = result.data;
                        callback(resultData);
                    },
                    error: function (jqXhr) {
                    }
                });
            },
            columns: [
                {
                    "data": "goodsId",
                    "searchable":false,
                    "className":"exclude",
                    "mRender": function (data, type, row, meta) {
                        // 显示行号
                        var startIndex = meta.settings._iDisplayStart;
                        return startIndex + meta.row + 1;
                    }
                },
                {"data": "brandName"},
                {"data": "goodsClassName"},
                {"data": "goodsName"},
                {"data": "shopGoodsStatus"}
            ],
            "aoColumnDefs": [
                {
                    "sClass":"center",
                    "aTargets":[5],
                    "searchable":false,

                    "mData":"id",
                    "mRender":function(a,b,c) {//a表示statCleanRevampId对应的值，c表示当前记录行对象
                        var buttons = '';
                        if (accessDelete) {
                            if (c.shopGoodsStatus==0)
                            {
                                buttons += '<button type="button" data-id="'+c.goodsId+'" class="btn btn-danger btn-xs btn-close">禁用</button>';
                            }
                            else {
                                buttons += '<button type="button" data-id="'+c.goodsId+'" class="btn btn-success btn-xs btn-open">启用</button>';
                            }
                        }
                        return buttons;
                    }
                }
            ],
            fnRowCallback : function(nRow,aData){
                var status=aData.shopGoodsStatus;
                var html = '<span class="fa fa-circle text-error" aria-hidden="true" style="color: red" data-state = "'+status+'"></span>';
                if (status==0)
                {
                    html = '<span class="fa fa-circle text-success" aria-hidden="true" style="color: #00e765"  data-state = "'+status+'"></span>';
                }
                $('td:eq(4)', nRow).html(html);
                return nRow;
            }

        });
        $("#modal-shop-goods-info").modal('show');
    });


    //用户禁用
    $("#shop-goods-info").off('click', '.btn-close').on('click', '.btn-close', function () {
        var that = $(this);
        var id = that.data('id');
        $("#modal-delete").modal('show');

        $("#modal-delete").off('click', '.btn-close-sure').on('click', '.btn-close-sure', function () {
            $("#modal-delete").modal('hide');
            Request.put("shop/statusUpdate/" + id,null,function (e) {
                if (e.success) {
                    toastr.info("下架成功!");
                    shop_all_goods.ajax.reload().draw();
                } else {
                    toastr.error(e.message);
                }
            });
        });

    });

    //用户启用
    $("#shop-goods-info").off('click', '.btn-open').on('click', '.btn-open', function () {
        var that = $(this);
        var id = that.data('id');
        Request.put("shop/statusUpdate1/" + id, null,function (e) {
            if (e.success) {
                toastr.info("上架成功!");
                shop_all_goods.ajax.reload();
            } else {
                toastr.error(e.message);
            }
        });

    });
});



