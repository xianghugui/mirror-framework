$(document).ready(function () {
    var is_add = "";
    var device_id = "";
    lang.searchPlaceholder = "设备标识码/用户名";
    var device_list = $("#device_list").DataTable({
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
        "ajax": function (data, callback,setting) {
            var param = {};
            param.pageSize = data.length;
            param.pageIndex = data.start;
            param.page = (data.start / data.length) + 1;
            $.ajax({
                url: BASE_PATH + "device/querydevice/",
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
            {data: "deviceCode"},
            {data: "username"},
            {data: "password", "className":"exclude","searchable":false},
            {data: "status", orderable: false,"searchable":false},
            {data: "createTime", "className":"exclude","searchable":false}
        ],
        "aoColumnDefs": [
            {"bSortable": false, "aTargets": [0]},
            {
                "sClass": "center",
                "aTargets": [6],
                "mData": "id",
                "mRender": function (a, b, c, d) {//c表示当前记录行对象

                    var buttons = '<div class="btn-group">';
                    buttons += '<button type="button" class="btn btn-default btn-xs dropdown-toggle" data-toggle="dropdown">操作';
                    buttons += '<i class="caret"></i> </button>';
                    buttons += '<ul class="dropdown-menu" style="min-width:100%;">';
                    if (accessUpdate) {
                        buttons += '<li><a href="javascript:;"' +
                            ' class="btn-edit" ' +
                            'data-id="' + c.id + '" ' +
                            'data-devicecode = "'+ c.deviceCode + '" ' +
                            'data-username = "'+ c.username + '" ' +
                            'data-password = "'+ c.password + '">编辑</a></li>';
                    }
                    if (accessDelete) {
                        if (c.status == 0) {
                            buttons += '<li><a href="javascript:;" class="btn-abnormal" data-id="' + c.id + '">异常</a></li>';
                            buttons += '<li><a href="javascript:;" class="btn-repair" data-id="' + c.id + '">检修</a></li>';
                        } else {
                            buttons += '<li><a href="javascript:;" class="btn-open" data-id="' + c.id + '">正常</a></li>';
                        }
                    }
                    buttons += '</ul></div>';

                    return buttons;
                }
            }
        ],
        fnRowCallback: function (nRow, aData, iDataIndex) {
            var status = aData.status;
            var html = '<span class="fa fa-circle text-error" aria-hidden="true" style="color: red" data-state = "' + status + '"></span>';
            if (status == 0) {
                html = '<span class="fa fa-circle text-success" aria-hidden="true" style="color: #00e765"  data-state = "' + status + '"></span>';
            }else if(status == 1){
                html = '<span class="fa fa-circle text-success" aria-hidden="true" style="color: #f0ad4e"  data-state = "' + status + '"></span>';
            }
            $('td:eq(4)', nRow).html(html);
            return nRow;
        }

    });
    //******************添加店铺按钮事件begin*************************
    $(".box-tools").off('click', '.btn-add').on('click', '.btn-add', function () {
        $("#device_code").val("");
        $("#user_name").val("");
        $("#pass_word").val("");
        $('#footer').show();
        $(".modal-title").html("新增设备");
        $("#modal-add").modal('show');
        is_add = true;
    });
    //******************添加店铺按钮事件end********************************


    $("#device_form").validate({
        rules: {
            device_code: {required: true},
            user_name: {required: true},
            pass_word: {required: true}

        },
        messages: {
            device_code: {required: "设备标识码不能为空."},
            user_name: {required: "用户名不能为空."},
           pass_word : {required: "密码不能为空."}
        },
        submitHandler: function (form) {
            var btn = $('#submitBtn');
            btn.attr('disabled', "true");
            btn.html("保存中..请稍后");
            var req = is_add ? Request.post : Request.put;
            var params = {
                deviceCode: $("#device_code").val(),
                username: $("#user_name").val(),
                password: $("#pass_word").val()
            };
            req("device/"  + (is_add ? "/add"  : $("#device_code").data("id")) , JSON.stringify(params), function (e) {
                console.log(e);
                if(e.code ==201){
                    toastr.info(e.message);
                }else if(e.code ==202){
                    toastr.info(e.message);
                }else if (e.success) {
                    toastr.info("保存完毕");
                    $("#modal-add").modal('hide');
                    device_list.draw();
                }else {
                    toastr.error(e.message);
                }
                btn.html("保存");
                btn.removeAttr('disabled');
                device_list.ajax.reload();
                // window.location.reload();
            });
        }
    });
    //编辑设备
    $("#device_list").off('click', '.btn-edit').on('click', '.btn-edit', function () {
        $("#device_code").val($(this).data("devicecode"));
        $("#user_name").val($(this).data("username"));
        $("#pass_word").val($(this).data("password"));
        $("#device_code").attr("data-id", $(this).data("id"));
        is_add = false;
        $("#modal-add").modal('show');
    });

    //设备异常
    $("#device_list").off('click', '.btn-abnormal').on('click', '.btn-abnormal', function () {
        device_id  = $(this).data('id');
        Request.put("device/" + device_id + "/disable", {}, function (e) {
            if (e.success) {
                toastr.info("已设为异常!");
                device_list.draw();
                device_list.ajax.reload();
            } else {
                toastr.error("请求失败:" + e.message);
            }
        });

    });
    //设备正常
    $("#device_list").off('click', '.btn-open').on('click', '.btn-open', function () {
        var id = $(this).data('id');
        Request.put("device/" + id + "/enable", {}, function (e) {
            if (e.success) {
                toastr.info("已设为正常!");
                device_list.draw();
                device_list.ajax.reload();
            } else {
                toastr.error("请求失败:" + e.message);
            }
        });
    });
    //设备检修
    $("#device_list").off('click', '.btn-repair').on('click', '.btn-repair', function () {
        var that = $(this);
        var id = that.data('id');
        Request.put("device/" + id + "/repair", {}, function (e) {
            if (e.success) {
                toastr.info("已设为检修");
                device_list.draw();
                device_list.ajax.reload();
            } else {
                toastr.error("请求失败:" + e.message);
            }
        });
    });

});
