$(document).ready(function () {

    var order_list = $("#order_list").DataTable({
        "language": lang,
        "paging": true,
        "lengthChange": true,
        "searching": true,
        "destroy": true,
        "info": true,
        "autoWidth": false,
        "bStateSave": true,
        "mark": {
            "exclude": [".exclude"]
        },
        "ajax": function (data, callback) {
            var param = {};
            param.pageSize = data.length;
            param.pageIndex = data.start;
            param.page = (data.start / data.length) + 1;
            $.ajax({
                url: BASE_PATH + "ordermgt/refund",
                type: "GET",
                cache: false,
                data: param,
                dataType: "json",
                success: function (result) {
                    var resultData = {};
                    resultData.draw = data.draw;
                    resultData.recordsTotal = result.length;
                    resultData.recordsFiltered = result.length;
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
            {data: "orderId"},
            {data: "type"},
            {data: "price"}
        ],
        "aoColumnDefs": [
            {"bSortable": false, "aTargets": [0]},
            {
                "sClass": "center",
                "aTargets": [4],
                "mData": "id",
                "mRender": function (a, b, c, d) {//c表示当前记录行对象
                    var buttons;
                    if (c.status != 5) {
                        buttons = '<button class="btn btn-primary btn-details" data-id="' + c.id + '" >确认退款</button>';
                    }
                    else {
                        buttons = '<div class="btn-group">';
                        buttons += '<div class="btn-group">';
                        buttons += '<button type="button" class="btn btn-default  btn-sx dropdown-toggle" data-toggle="dropdown">操作';
                        buttons += '<span class="caret"></span></button>';
                        buttons += '<ul class="dropdown-menu">';
                        buttons += '<li><a href="javascript:;" class="btn-moreinfo" data-id="' + a + '">查看详情</a></li>';
                        buttons += '<li><a href="javascript:;" class="btn-treated" data-id="' + a + '">订单已处理</a></li>';
                        buttons += '</ul></div></div>';
                    }
                    return buttons;
                }
            }
        ],
        fnRowCallback: function (nRow, aData, iDataIndex) {

            // 状态0:平台购买退款， 1 试衣购买退款，2：视频购买退款
            var type = aData.type;
            var status = aData.status;
            var html = '';
            if (type == 0) {
                html = '<span  style="color:#AF7AC5 " data-state = "' + status + '">平台购买退款</span>';
            }
            if (type == 1) {
                html = '<span  style="color:#A93226 "  data-state = "' + status + '">试衣购买退款</span>';
            }
            if (type == 2) {
                html = '<span  style="color: #5499C7"  data-state = "' + status + '">视频购买退款</span>';
            }
            if (status == 5) {
                html = '<span  style="color:#ff0000 "  data-state = "' + status + '">订单待处理</span>';
            }
            $('td:eq(2)', nRow).html(html);
        }

    });

    $("#order_list").off('click', '.btn-details').on('click', '.btn-details', function () {
        var id = $(this).data('id');
        Request.get("api/refundexchange/refund/" + id, function (e) {
            if (e.success) {
                toastr.success(e.data);
                order_list.ajax.reload();
            } else {
                toastr.error(e.data);
            }
        });
    });

    // 文件提交框选项设置
    var fileinputoption = {
        required: true,
        language: 'zh', //设置语言
        showUpload: false, //是否显示上传按钮
        showRemove: false,
        showCaption: true,//是否显示标题
        showClose: true,
        allowedPreviewTypes: ['image'],
        allowedFileTypes: ['image'],
        allowedFileExtensions: ['jpg', 'gif', 'png'],
        maxFileCount: 10,
        maxFileSize: 2000,
        autoReplace: false,
        validateInitialCount: true,
        overwriteInitial: false,
        initialPreviewAsData: true,
        uploadAsync: true //同步上传

    };

    //查看商家拒绝详情
    $("#order_list").off('click', '.btn-moreinfo').on('click', '.btn-moreinfo', function () {
        //初始化图片上传控件
        $("input#carousel_upload_info").fileinput('destroy');
        $("input#kv-explorer_info").fileinput('destroy');
        delete fileinputoption.initialPreview;
        delete fileinputoption.initialPreviewConfig;
        $("input#carousel_upload_info").fileinput(fileinputoption);
        $("input#kv-explorer_info").fileinput(fileinputoption);
        var id = $(this).data('id');
        Request.get("api/refundexchange/" + id + "/showRefundsInfo", function (e) {
            if (e.success) {
                $("#modal-readonly").modal('show');
                $("#refund_id").val(e.data.refundId);
                $("#refund_price").val(e.data.price);
                $("#application_time").val(e.data.applicationTime);
                $("#content").text(e.data.content);
                $("#refuse_content").text(e.data.reason);
                var initialPreview = [];
                var initialPreviewConfig = [];
                if(e.data.imageSrc != null) {
                    for (var i = 0; i < e.data.imageSrc.length; i++) {
                        initialPreview.push("/file/image/" + e.data.imageSrc[i].resourceId);
                        initialPreviewConfig.push({
                            width: '160px',
                            url: '/shop/img/delete',
                            key: i + 1
                        });
                    }
                }
                if (initialPreview.length == 0) {     //判断店铺图片是否为空
                    $('#img_list_box').css("display","none");
                }
                else {
                    $('#carousel_upload_info').fileinput('destroy');
                    fileinputoption.initialPreview = initialPreview;
                    fileinputoption.initialPreviewConfig = initialPreviewConfig;
                    $('#carousel_upload_info').fileinput(fileinputoption);
                    $('#carousel_upload_info').fileinput('disable');
                }

                var initialPreview2 = [];
                var initialPreviewConfig2 = [];
                if(e.data.altImageSrc != null) {
                    for (var i = 0; i < e.data.altImageSrc.length; i++) {
                        initialPreview2.push("/file/image/" + e.data.altImageSrc[i].resourceId);
                        initialPreviewConfig2.push({
                            width: '160px',
                            url: '/shop/img/delete',
                            key: i
                        });
                    }

                }
                if (initialPreview2.length != 0) {     //判断店铺图片是否为空
                    $('#kv-explorer_info').fileinput('destroy');
                    fileinputoption.initialPreview = initialPreview2;
                    fileinputoption.initialPreviewConfig = initialPreviewConfig2;
                    $('#kv-explorer_info').fileinput(fileinputoption);
                    $('#kv-explorer_info').fileinput('disable');
                }
                else{
                    $('#img_list_box2').css("display","none");
                }

            } else {
                toastr.error(e.data);
            }
        });
    });

    $("#order_list").off('click', '.btn-treated').on('click', '.btn-treated', function () {
        var id = $(this).data('id');
        Request.put("ordermgt/refundOrderTreated/" + id,{}, function (e) {
            console.log(e);
            if (e.success) {
                toastr.success(e.data);
                order_list.ajax.reload();
            } else {
                toastr.error(e.data);
            }
        });
    });

});



