$(document).ready(function () {
    var order_id = "";
    var info = ["待付款","待派单","待发货","退回","完成订单"];
    var order_list = $("#order_list").DataTable({
        "language": lang,
        "paging": true,
        "lengthChange": true,
        "searching": true,
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
                url: BASE_PATH + "ordermgt/showorder",
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
                data: "id",
                searchable: false,
                orderable: false,
                className: "exclude",
                targets: 0,
                width: "30px",
                render: function (data, type, row, meta) {
                    // 显示行号
                    var startIndex = meta.settings._iDisplayStart;
                    return startIndex + meta.row + 1;
                }
            },
            {data: "orderId","orderable":false},
            {data: "childOrderId","orderable":false},
            {data: "userName","orderable":false},
            {data: "phone","orderable":false},
            {data: "address","orderable":false},
            {data: "createTime", "className": "exclude", "searchable": false},
            {data: "shopName","orderable":false},
            {data: "status", "className": "exclude", "searchable": false,"orderable":false},
        ],
        "aoColumnDefs": [
            {"bSortable": false, "aTargets": [0]},
            {
                "sClass": "center",
                "aTargets": [9],
                "mData": "id",
                "mRender": function (a, b, c, d) {//c表示当前记录行对象

                    var buttons = '<button class="btn btn-primary btn-details" data-id="' + c.childOrderId +
                        '" data-longtitude = "' + c.longtitude +'" data-laltitude = "' + c.laltitude +'"';
                    if (c.status <= 2) {

                    } else {
                        buttons+='disabled="disabled"';
                    }
                    buttons+='>选择发货商家</button>';
                    return buttons;
                }
            }
        ],
        fnRowCallback : function(nRow,aData,iDataIndex){

            // 状态0:待付款， 1 待派单，2：待发货，3：待收货，4：待评价，5：已评价，6：退货，7：订单关闭
            var status=aData.status;
            var html ='';
            if (status==0){
                html = '<span  style="color:#AF7AC5 " data-state = "'+status+'">待付款</span>';
            }
            if (status==1){
                html = '<span  style="color:#A93226 "  data-state = "'+status+'">待派单</span>';
            }
            if (status==2){
                html = '<span  style="color: #5499C7"  data-state = "'+status+'">待发货</span>';
            }
            if (status==3){
                html = '<span  style="color: #148F77"  data-state = "'+status+'">待收货</span>';
            }
            if (status==4){
                html = '<span  style="color: #00e765"  data-state = "'+status+'">待评价</span>';
            }
            if (status==5){
                html = '<span  style="color: #F7DC6F"  data-state = "'+status+'">已评价</span>';
            }
            if (status==6){
                html = '<span  style="color: #7B7D7D"  data-state = "'+status+'">退回</span>';
            }
            if (status==7){
                html = '<span  style="color: #B2BABB"  data-state = "'+status+'">订单关闭</span>';
            }
            $('td:eq(8)', nRow).html(html);
        }

    });
    //订单详情
    $("#order_list").off('click', '.btn-details').on('click', '.btn-details', function () {
        order_id = $(this).data('id');
        var longtitude = $(this).data('longtitude');
        var laltitude = $(this).data('laltitude');
        var str = "";
        $.ajax({
            url: "ordermgt/" + order_id + "/showorderinfo?longtitude=" + longtitude + "&laltitude=" + laltitude,
            type:"GET",
            async: false,
            success: function(data) {
                    var shops = data.data.shops;
                    var opt = '<option value="-1">请选择店铺</option>';
                    for(var j = 0; j < shops.length; j++){
                        if(shops[j].shopId == data.shopId){
                            opt += '<option selected="selected" value="' + shops[j].shopId + '">' + shops[j].shopName + '</option>';
                        }else{
                            opt += '<option value="' + shops[j].shopId + '">' + shops[j].shopName + '</option>';
                        }
                    }
                    str += '<div class="goodsInfo col-md-5"> ' +
                        '<h3 class="text-center">' + data.data.goodsName + '</h3>' +
                        '<p><img src="' + data.data.imageUrl + '"></p> ' +
                        '<p> 颜色&nbsp;:&nbsp;&nbsp;<span class="table label label-success">' + data.data.color + '</span> <span class="right">尺寸&nbsp;:&nbsp;&nbsp;<span class="table label label-success">' + data.data.size + '</span></span> </p>' +
                        '<p> 数量&nbsp;:&nbsp;&nbsp;<span class="table label label-success">' + data.data.num + '</span> <span class="right">价格&nbsp;:&nbsp;&nbsp;<span class="table label label-success">' + data.data.price + '</span></span> </p> ' +
                        '<p>' + data.data.createTime + '<span class="right">' + info[data.data.status] + '</span></p>' +
                        '<p class="center">订单ID&nbsp;:&nbsp;&nbsp;' + order_id + '</p>' +
                        '<p class="center">详情ID&nbsp;:&nbsp;&nbsp;' + data.data.orderDetailId + '</p>' +
                        '<select class="selectshop">' + opt +
                        '</select><button class="btn btn-primary btn-save" data-orderid = "' + order_id + '" data-orderdetailid = "' + data.data.orderDetailId + '">保存</button></div>';
                $('#demo').html(str);
                if(data.data.shopId != null){
                    $('.selectshop').multiselect({
                        enableFiltering: false,
                        buttonWidth: '80%'
                    });
                    $('.selectshop').multiselect('select', data.data.shopId);
                    $('.selectshop').multiselect('refresh');
                }
            },
            error: function () {
                toastr.warning("请求列表数据失败, 请重试");
            }
        });
        $('.selectshop').multiselect({
            buttonWidth: '80%',
        });
        $('#modal-orderinfo').modal('show');
    });

    $('#demo').off('click', '.btn-save').on('click', '.btn-save', function () {
        if($('#selectshop :selected').val() == -1){
            toastr.warning("未选择店铺");
            return false;
        }
        var order_id = $(this).data('orderid');
        var order_detail_id = $(this).data('orderdetailid');
        var param = {
            orderId : order_id,
            orderDetailId: order_detail_id,
            shopId: $(this).prev().find('.selectshop').val()
        };
        Request.post("ordermgt/selectshop", param, function(e){
            if(e.success){
                toastr.info("保存成功");
                order_list.draw();
                order_list.ajax.reload();
            }else{
                toastr.warning("保存失败, 请重试");
            }
        });
    });
});

