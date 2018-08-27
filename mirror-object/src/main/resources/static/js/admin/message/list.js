$(document).ready(function () {

    //富文本编辑器初始化
    KindEditor.ready(function (K) {
        window.editor = K.create('#message_content', {
            uploadJson: 'file/imageUpload',
            formatUploadUrl: false,
            allowFileManager: false,
            items: [
                'source', '|', 'undo', 'redo', '|', 'preview', 'template', 'code', 'cut', 'copy', 'paste',
                'plainpaste', 'wordpaste', '|', 'justifyleft', 'justifycenter', 'justifyright',
                'justifyfull', 'insertorderedlist', 'insertunorderedlist', 'indent', 'outdent', 'subscript',
                'superscript', 'clearhtml', 'quickformat', 'selectall', '|', 'fullscreen', '/',
                'formatblock', 'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold',
                'italic', 'underline', 'strikethrough', 'lineheight', 'removeformat', '|', 'image',
                'table', 'hr', 'emoticons', 'pagebreak', 'anchor', 'link', 'unlink', '|', 'about'
            ]
        });
    });

    //  服装基本信息列表
    var baseTable = $("#message_data_table").DataTable({
        "language": lang,
        "paging": true,
        "lengthChange": true,
        "lengthMenu":[6,12,18],
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
            $.ajax({
                url: BASE_PATH + "message/queryMessage/",
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

        },
        columns:[{
            "data": "id",
            bSortable: false,
            targets: 0,
            render: function (a, b, c, d) {
                var str='<div class="panel"><div class="panel-heading"><h4 style="text-align: center">'+c.title+'</h4></div>' +
                    '<div class="panel-heading">创建时间：'+c.createTime+'</div>' +
                    '<div class="panel-body">'+c.content+'</div>';
                var button='';
                if(accessUpdate){
                    button+='<button type="button" class="btn btn-primary  btn-sx btn-class-edit" style="width: 50%;height: 30px" data-id="'+a+'">编辑消息</button>';
                }
                if(accessDelete){
                    button+='<button type="button" class="btn btn-primary  btn-sx btn-class-delete" style="width: 50%;height: 30px" data-id="'+a+'">删除消息</button>';
                }
                return str+button;
            }
        }]
    });

    // 添加消息
    $('#btn_add_new').off('click').on('click', function () {
        $('#message_title').val("");
        window.editor.html('');
        $("#modal-basebox").modal('show');
    });

    // 修改消息
    $('#message_data_table').off('click', '.btn-class-edit').on('click', '.btn-class-edit', function () {
        var that = $(this);
        Request.get('message/' + $(this).data('id') + '/queryMessageById', {}, function (e) {
            if (e.success) {
                $('#message_form').data("id", that.data('id'));
                $('#message_title').val(e.data.title);
                window.editor.html(e.data.content);
                $("#modal-basebox").modal('show');
            } else {
                toastr.warning(e.message);
            }
        });
    });

    // 删除消息
    $('#message_data_table').off('click', '.btn-class-delete').on('click', '.btn-class-delete', function () {
        var that = $(this);
        Request.delete('message/' + $(this).data('id') + '/deleteMessage', {}, function (e) {
            if (e.success) {
                toastr.success('删除成功', opts);
                baseTable.ajax.reload().draw();
            } else {
                toastr.warning(e.message);
            }
        });
    });

    // 添加或修改数据校验
    $('form#message_form').validate({
        rules: {
            messageTitle: {required: true}
        },
        messages: {
            messageTitle: {required: "请填写标题"}
        },
        submitHandler: function (form) {
            //同步富文本编辑器，才能取得富文本编辑器内地内容
            editor.sync();
            var ele = $(form);
            var btn = $('button[type="submit"]');
            var id = ele.data("id");
            var req = id == "" ? Request.post : Request.put;
            console.log("id:" + id);
            btn.attr('disabled', "true").html("保存中..请稍后");

            var params = {
                title: ele.find('#message_title').val(),
                content: window.editor.html()
            };

            if (id != "") {
                params.id = id;
            }

            req('message/' + (id == "" ? 'addMessage' : 'updateMessage'), params, function (e) {
                if (e.success) {
                    toastr.success('操作成功！', opts);
                    baseTable.ajax.reload().draw();
                    $("#modal-basebox").modal('hide');
                } else {
                    toastr.warning('操作失败', opts);
                }
                btn.removeAttr('disabled').html('保存');
            });

        }
    });

});