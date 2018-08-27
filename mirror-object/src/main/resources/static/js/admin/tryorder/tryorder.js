$(document).ready(function () {
    var info = ["待发货","待收货","确认收货","试衣购买","试衣退回","完成试衣"];
    var tryorder_id = "";
    var tryorder_list = $("#tryorder_list").DataTable({
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
                url: BASE_PATH + "tryorder/showtryorder/",
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
                targets: 0,
                width: "30px",
                render: function (data, type, row, meta) {
                    // 显示行号
                    var startIndex = meta.settings._iDisplayStart;
                    return startIndex + meta.row + 1;
                }
            },
            {data: "tryorderId"},
            {data: "childTryOrderId"},
            {data: "userName"},
            {data: "phone"},
            {data: "address"},
            {data: "createTime", "className": "exclude", "searchable": false},
            {data: "status", orderable: true, "searchable": true}
        ],
        "aoColumnDefs": [
            {"bSortable": false, "aTargets": [0]},
            {
                "sClass": "center",
                "aTargets": [8],
                "mData": "id",
                "mRender": function (a, b, c, d) {//c表示当前记录行对象
                    var buttons = '<button class="btn btn-primary btn-details" data-id="' + c.tryorderId +
                        '" data-longtitude = "' + c.longtitude +'" data-laltitude = "' + c.laltitude +'">选择发货商家</button>';
                    return buttons;
                }
            }
        ],
          fnRowCallback : function(nRow,aData,iDataIndex){

              // 商品状态（0待派单，1待发货，2试衣中，3购买，4退回，5商家确认退回，6待评价,7完成订单，8取消订单））
              var status=aData.status;
              var html ='';
              if (status==0){
                  html = '<span  style="color: #A93226" data-state = "'+status+'">待派单</span>';
              }
              if (status==1){
                  html = '<span  style="color: #AF7AC5"  data-state = "'+status+'">待发货</span>';
              }
              if (status==2){
                  html = '<span  style="color: #5499C7"  data-state = "'+status+'">试衣中</span>';
              }
              if (status==3){
                  html = '<span  style="color: #148F77"  data-state = "'+status+'">试衣购买</span>';
              }
              if (status==4){
                  html = '<span  style="color: #00e765"  data-state = "'+status+'">退回</span>';
              }
              if (status==5){
                  html = '<span  style="color: #239dce"  data-state = "'+status+'">商家确认退回</span>';
              }
              if (status==6){
                  html = '<span  style="color: #F7DC6F"  data-state = "'+status+'">6待评价</span>';
              }
              if (status==7){
                  html = '<span  style="color: #7B7D7D"  data-state = "'+status+'">完成试衣</span>';
              }
              if (status==8){
                  html = '<span  style="color: #B2BABB"  data-state = "'+status+'">取消订单</span>';
              }
              $('td:eq(7)', nRow).html(html);
          }
    });
    //试衣订单详情
    $("#tryorder_list").off('click', '.btn-details').on('click', '.btn-details', function () {
        tryorder_id = $(this).data('id');
        var longtitude = $(this).data('longtitude');
        var laltitude = $(this).data('laltitude');
        var str = "";
        $.ajax({
            url: "tryorder/" + tryorder_id + "/showtryinfo?longtitude=" + longtitude + "&laltitude=" + laltitude,
            type:"GET",
            async: false,
            success: function(data) {
                for(var i = 0; i < data.data.length; i++){
                    var shops = data.data[i].shops;
                    var opt = '<option value="-1">请选择店铺</option>';
                    for(var j = 0; j < shops.length; j++){
                        if(shops[j].shopId == data.data[i].shopId){
                            opt += '<option selected="selected" value="' + shops[j].shopId + '">' + shops[j].shopName + '</option>';
                        }else{
                            opt += '<option value="' + shops[j].shopId + '">' + shops[j].shopName + '</option>';
                        }
                    }
                    str += '<div class="goodsInfo col-md-5 "> ' +
                        '<h3 class="center">' + data.data[i].goodsName + '</h3>' +

                        '<p><img src="' + data.data[i].imageSrc[0].resourceUrl + '"></p> ' +
                        '<p> 颜色:<span class="table label label-success">' + data.data[i].color + '</span> <span class="right">尺寸:<span class="table label label-success">' + data.data[i].size + '</span></span> </p>' +
                        '<p> 数量:<span class="table label label-success">' + data.data[i].num + '</span> <span class="right">价格:<span class="table label label-success">' + data.data[i].price + '</span></span> </p> ' +
                        '<p>' + data.data[i].createTime + '<span class="right">' + info[data.data[i].status] + '</span></p>' +
                        '<p class="center">订单ID:' + tryorder_id + '</p>' +
                        '<p class="center">详情ID:' + data.data[i].tryInfoId + '</p>' +
                        '<select class="selectshop" >' + opt +
                        '</select><button class="btn btn-primary btn-xs btn-save"  data-tryorderid = "' + tryorder_id + '" data-tryinfoid = "' + data.data[i].tryInfoId + '">保存</button></div>';
                }
                $('#demo').html(str);
            },
            error: function () {
                toastr.warning("请求列表数据失败, 请重试");
            }
        });
        $('.selectshop').multiselect({
            buttonWidth: '80%',
        });
        $('#modal-tryinfo').modal('show');
    });
    $('#demo').off('click', '.btn-save').on('click', '.btn-save', function () {
        if($('#selectshop :selected').val() == -1){
            toastr.warning("未选择店铺");
            return false;
        }
        var tryorder_id = $(this).data('tryorderid');
        var tryinfo_id = $(this).data('tryinfoid');
        var param = {
            tryorderId : tryorder_id,
            tryinfoId: tryinfo_id,
            shopId: $(this).prev().find('.selectshop').val()
        };
        Request.post("tryorder/selectshop", param, function(e){
            if(e.success){
                toastr.info("保存成功");
                tryorder_list.draw();
                tryorder_list.ajax.reload();
            }else{
                toastr.warning("保存失败, 请重试");
            }
        });
    });
});

