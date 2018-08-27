$(document).ready(function () {
    var id = "";
    var getmoney_list = $("#getmoney_list").DataTable({
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
        // "serverSide": true,
        "ajax": function (data, callback,setting) {
            var param = {};
            param.pageSize = data.length;
            param.pageIndex = data.start;
            param.page = (data.start / data.length) + 1;
            $.ajax({
                url: BASE_PATH + "GetMoney/show",
                type: "GET",
                cache: false,
                data: param,
                dataType: "json",
                success: function (result) {
                    var resultData = {};
                    resultData.draw = data.draw;
                    resultData.recordsTotal = result.total;//当前页显示数量
                    resultData.recordsFiltered = result.total;//数据总数
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
            {data: "userId"},
            {data: "money"},
            {data: "fees"},
            {data: "bank"},
            {data: "createTime"},
            {data: "expectTime"},
            {data: "name"},
            {data: "dealTime"},
            {data: "status", orderable: false,"searchable":false},
        ],
        "aoColumnDefs": [
            {"bSortable": false, "aTargets": [0]},
            {
                "sClass": "center",
                "aTargets": [10],
                "mData": "id",
                "mRender": function (a, b, c, d) {//c表示当前记录行对象
                    var buttons = ""
                    if(c.status == 0){
                        buttons = '<button  class="btn btn-primary  btn-sx btn-extract" data-id="' + c.id + '">提现</button>';
                    }else {
                        buttons = '已处理';
                    }
                    return buttons;
                }
            }
        ],
        fnRowCallback: function (nRow, aData, iDataIndex) {
            var status = aData.status;
            var html = '申请提现';
            if (status == 2) {
                html = '提现成功';
            }else if(status == 1){
                html = '提现中';
            }
            $('td:eq(9)', nRow).html(html);
            return nRow;
        }

    });
    //设备检修
    $("#getmoney_list").off('click', '.btn-extract').on('click', '.btn-extract', function () {
        id = $(this).data('id');
        $('#modal-extract').modal('show');

    });
    $("#modal-extract").off('click', '.btn-extract-sure').on('click', '.btn-extract-sure', function () {
        var data = {
            id: id
        }
        Request.get("GetMoney/transfers",data, function (e) {
            if (e.success) {
                toastr.info("同意提现");
                getmoney_list.draw();
                getmoney_list.ajax.reload();
            } else {
                toastr.error("请求失败:" + e.message);
            }
        });
    });

});
