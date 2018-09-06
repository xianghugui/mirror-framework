$(document).ready(function () {
    var order_id = "";

    //订单状态（0,待报价.1待付款，2待发货，3待收货，4退货，5订单完成，6用户取消，7缺货）
    var info = ["待报价","待付款","待发货","待收货","退回","订单完成","用户取消","缺货"];
    var searchPlaceholder ="用户名/地址";
    lang.searchPlaceholder  = searchPlaceholder;
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
                url: BASE_PATH + "videoorder/showvideoorder",
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
            {data: "userName"},
            {data: "num", "className": "exclude", "searchable": false},
            {data: "price" , "className": "exclude", "searchable": false},
            {data: "address", orderable: false},
            {data: "createTime", "className": "exclude", "searchable": false},
            {data: "updateUser" , "className": "exclude", "searchable": false},
            {data: "updateTime", "className": "exclude", "searchable": false},
            {data: "status", orderable: false, "className": "exclude", "searchable": false},
            {data: "videoSrc", orderable: false, "className": "exclude", "searchable": false},
        ],
        "aoColumnDefs": [
            {"bSortable": false, "aTargets": [0]},
            {
                "sClass": "center",
                "aTargets": [9],
                "mRender": function (a, b, c, d) {//c表示当前记录行对象
                    var buttons = '<button class="byn btn-primary btn-details" data-image="' + c.videoImageUrl +'" data-id="' + c.videoUrl +'">视频</button>';
                    return buttons;
                }
            }
        ],
        fnRowCallback: function (nRow, aData, iDataIndex) {
            $('td:eq(8)', nRow).html(info[aData.status]);
            return nRow;
        }

    });
    //试衣订单详情
    $("#order_list").off('click', '.btn-details').on('click', '.btn-details', function () {
        var videosrc = $(this).data("id");
        var videoImageUrl = $(this).data("image");
        $('#modal-video .modal-content').html('<video id="my-video2" class="video-js" controls = "" preload="none" width="100%" height="100%"' +
            'poster="'+videoImageUrl+'" data-setup="{}">'+
           '<source src="' + videosrc + '" type="video/mp4"/>'+
           '<source src="' + videosrc + '" type="video/webm"></video>');
        $('#modal-video').modal('show');


    });

});

