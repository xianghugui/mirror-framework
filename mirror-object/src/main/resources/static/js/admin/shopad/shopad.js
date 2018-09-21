$(document).ready(function () {
    var inited = false;
    var adId = '';

    $('#tagsinput').tagsinput({
        itemValue: 'id',
        itemText: 'text'
    });


    //富文本编辑器初始化
    var editor = KindEditor.create('#content', {
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


    var device_shop_ad_list = $("#device_shop_ad_list").DataTable({
        "language": lang,
        "paging": true,
        "lengthChange": true,
        "lengthMenu": [6],
        "searching": false,
        "ordering": false,
        "destroy": true,
        "info": true,
        "autoWidth": false,
        "bStateSave": true,
        "serverSide": true,
        "ajax": function (data, callback) {
            var param = {};
            param.pageSize = data.length;
            param.pageIndex = data.start;
            param.page = (data.start / data.length) + 1;
            param.pushType = $('#pushType').val();
            $.ajax({
                url: BASE_PATH + "UserPush/queryAllAd",
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
        columns: [{
            "data": "id",
            bSortable: false,
            targets: 0,
            render: function (a, b, c, d) {
                var str = '<div class="panel"><div class="panel-heading"><h4 style="text-align: center">' + c.title + '</h4></div>' +
                    '<div class="panel-heading"><div><span>发布人&nbsp;:&nbsp;' + c.user_name + '</span><span style="float: right">' + c.create_time + '</span></div></div>' +
                    '<div class="panel-body" style="overflow-y: scroll;height: 230px;width: 100%;">' + c.content + '</div>';
                var button = '';
                if(accessUpdate) {
                    if (c.status == 0) {
                        button += '<button type="button" class="btn btn-primary  btn-sx btn-push" style="width: 50%;height: 30px" data-id="' + c.u_id + '">发布广告</button>' +
                            '<button type="button" class="btn btn-primary  btn-sx btn-update" style="width: 50%;height: 30px" data-id="' + c.u_id + '" >编辑广告</button></div>';
                    }
                    else {
                        button += '<button type="button" class="btn btn-primary  btn-sx btn-update" style="width: 50%;height: 30px" data-id="' + c.u_id + '" disabled>编辑广告</button>' +
                            '<button type="button" class="btn btn-default  btn-sx btn-cancel" style="width: 50%;height: 30px" data-id="' + c.u_id + '">取消发布</button></div>';
                    }
                }
                return str + button;
            }
        }]
    });

    //新增视频广告
    $('.box-tools').off('click', '.btn-add').on('click', '.btn-add', function () {
        setEmptyModalData();
        $('.modal-title').html("添加广告");
        $('#modal-add').modal('show');
        adId = '';
    });

    //添加或修改广告
    $('form#shop_form').validate({
        rules: {
            ad_title: {required: true},
            ad_user: {required: true}
        },
        messages: {
            ad_title: {required: "请输入广告标题"},
            ad_user: {required: "请输入发布人姓名"}
        },
        submitHandler: function (form) {
            var btn = $('button[type="submit"]');
            var reqType = adId == '';
            var req = reqType ? Request.post : Request.put;
            btn.attr('disabled', "true").html("保存中..请稍后");

            //同步富文本编辑器，才能取得富文本编辑器内地内容
            editor.sync();

            if (editor.html() == null || editor.html() == '<br />') {
                toastr.warning('广告内容未填写！请填写后再提交', opts);
                btn.html("保存");
                btn.removeAttr('disabled');
            }
            else {
                var params = {
                    title: $('#ad_title').val(),
                    userName: $('#ad_user').val(),
                    content: editor.html(),
                    pushType: $('#pushType').val()
                };

                req('UserPush/' + (reqType ? '' : adId), JSON.stringify(params), function (e) {
                    if (e.success) {
                        toastr.success('操作成功！', opts);
                        $("#modal-add").modal('hide');
                    } else {
                        toastr.warning('操作失败', opts);
                    }
                    btn.removeAttr('disabled').html('保存');
                    device_shop_ad_list.draw();
                });
            }
        }
    });

    //编辑视频广告
    $("#device_shop_ad_list").off('click', '.btn-update').on('click', '.btn-update', function () {
        $(".modal-title").html("编辑广告");
        $("#modal-add").modal('show');
        var id = $(this).data('id');
        adId = id;
        Request.get('UserPush/queryAd/' + id, {}, function (e) {
            if (e.success) {
                var data = e.data;
                $('#ad_title').val(data.title);
                $('#ad_user').val(data.userName);
                editor.html(data.content);
                $("#modal-basebox").modal('show');
            }
            else {
                toastr.warning('操作失败', opts);
            }
        });
    });

    //清空修改数据
    var setEmptyModalData = function () {
        $('#ad_title').val('');
        $('#ad_user').val('');
        editor.html('');
    };

    //加载设备区域信息
    var initAreaTree = function () {
        Request.get("area/queryall", function (e) {
            area_list = e;
            var tree = areaTree.init();
            var rootNodes = tree.getRootNodes(e);
            $('#area_tree').treeview({
                multiSelect: true,
                data: rootNodes,
                selectedBackColor: "#07100e",
                levels: 3
            });
        });
    };

    initAreaTree();

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
                        level: item.level,
                        parentId: item.parentId,
                        text: item.areaName,
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
                        level: item.level,
                        parentId: item.parentId,
                        text: item.areaName,
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

    //取消发布广告
    $("#device_shop_ad_list").off('click', '.btn-cancel').on('click', '.btn-cancel', function () {
        var id = $(this).data('id');
        Request.put('ShopAd/cancelPush/' + id, {}, function (e) {
            if (e.success) {
                toastr.success('操作成功！', opts);
                device_shop_ad_list.draw();
            }
            else {
                toastr.warning('操作失败', opts);
            }
        });
    });

    //发布广告
    $("#device_shop_ad_list").off('click', '.btn-push').on('click', '.btn-push', function () {

        $('#area_tree').treeview('collapseAll', {silent: true});

        $('#tagsinput').tagsinput("removeAll");

        $(".modal-title").html("发布设备广告");

        $("#modal-push").modal('show');

        adId = $(this).data('id');

        $('#area_tree').on('nodeSelected', function (event, data) {
            $('#tagsinput').tagsinput('add', {id: data.id, text: data.text});
            if (data.nodes != null) {
                toastr.info("你选中了父节点，将不能选择该子节点", opts);
                for (var i = 0; i < data.nodes.length; i++) {
                    $('#area_tree').treeview('disableNode', [data.nodes[i].nodeId, {silent: true}]);
                    $('#tagsinput').tagsinput('remove', data.nodes[i].id);
                }
            }
        });

        $('#area_tree').on('nodeUnselected ', function (event, data) {
            $('#tagsinput').tagsinput('remove', data.id);
            if (data.nodes != null) {
                for (var i = 0; i < data.nodes.length; i++) {
                    $('#area_tree').treeview('enableNode', [data.nodes[i].nodeId, {silent: true}]);
                    var nodelist = data.nodes[i];
                    if (nodelist.nodes != null) {
                        for (var h = 0; h < nodelist.nodes.length; h++) {
                            $('#area_tree').treeview('enableNode', [nodelist.nodes[h].nodeId, {silent: true}]);
                        }
                    }
                }
            }
        });
    });

    $('#area_form').validate({
        submitHandler: function (form) {
            var selected = $('#area_tree').treeview('getSelected')
            var selectValue = $('#tagsinput').val();

            if (selected.length == 0) {
                toastr.info("请选择需要发布的节点", opts);
            }
            else {
                var btn = $('button[type="submit"]');
                btn.attr('disabled', "true").html("保存中..请稍后");
                var params = {
                    adId: adId,
                    areaList: selectValue
                };
                Request.post('ShopAd/add', JSON.stringify(params), function (e) {
                    if (e.success) {
                        toastr.success('操作成功！', opts);
                        $("#modal-push").modal('hide');
                    }
                    else {
                        toastr.warning('操作失败', opts);
                    }
                    btn.removeAttr('disabled').html('保存');
                    device_shop_ad_list.draw();
                })
            }
        }
    });

});

