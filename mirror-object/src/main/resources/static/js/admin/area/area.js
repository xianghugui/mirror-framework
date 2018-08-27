$(document).ready(function () {

    var inited = false;
    var area_list = [];
    var is_add = true;
    var userID;

    //获取省市区方法
    function getCity(name, cn, selectedId) {
        var str = '<option value="1">请选择' + cn + '</option>\n';
        var url;
        if (!selectedId || selectedId == "") {
            url = 'area/' + name;
        } else {
            url = 'area/' + name + '/' + selectedId;
        }
        $.ajax({
            url: url,
            type: "GET",
            async: false,
            success: function (data) {
                if (data.success && data.data != null) {
                    var d = data.data;
                    for (var i = 0; i < d.length; i++) {
                        str += '<option value="' + d[i].uId + '">' + d[i].areaName + '</option>\n';
                    }

                } else {
                    toastr.warning("数据加载失败，请重试");
                }
            }
        });
        return str;
    }

    var initAreaTree = function () {
        Request.get("area/queryallviewtree", function (e) {
            area_list = e;
            var tree = areaTree.init();
            var rootNodes = tree.getRootNodes(e);
            $('#area_tree').treeview({
                data: rootNodes,
                selectedBackColor: "#07100e",
                levels: 3,
                onNodeSelected: function (event, data) {
                    var currentAreaID = data.id;
                }
            });
            $('#area_tree').treeview('selectNode', [0]);
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
                        // level: 0,
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
                        // level: parentNode[level]+1,
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


    $("form#area_form").validate({
        submitHandler: function (form) {
            var selected = $('#areaSelect option:selected').val();
            var btn = $('button[type="submit"]');
            btn.attr('disabled', "true");
            btn.html("保存中..请稍后");
            if (selected == 1){
                selected = $('#citySelect option:selected').val();
                if (selected == 1) {
                    selected = $('#provinceSelect option:selected').val();
                }
            }
            Request.put("area/" + selected + "/addarea", {}, function (e) {
                if (e.success) {
                    toastr.info("保存完毕", opts);
                    $("#modal-add").modal('hide');
                    initAreaTree();
                    window.location.reload();
                } else {
                    toastr.error("保存失败", opts)
                }

                btn.html("保存");
                btn.removeAttr('disabled');
            });
            // }
        }
    });
    $('#provinceSelect').multiselect({
        buttonWidth: '100%',
        maxHeight: 500,
        onChange: function (element, checked) {
            $('#citySelect').empty();
            $('#citySelect').append(getCity("querycity", "市", $('#provinceSelect option:selected').val()));
            $('#citySelect').multiselect('rebuild');
            $('#areaSelect').empty();
            $('#areaSelect').append('<option value="1">请选择区</option>\n');
            $('#areaSelect').multiselect('rebuild');
        }

    });
    $('#citySelect').multiselect({
        buttonWidth: '100%',
        maxHeight: 500,
        onChange: function (element, checked) {
            $('#areaSelect').empty();
            $('#areaSelect').append(getCity($('#citySelect option:selected').val() + '/queryarea/true', "区"));
            $('#areaSelect').multiselect('rebuild');
        }
    });
    $('#citySelect').append('<option value="1">请选择市</option>\n');
    $('#citySelect').multiselect('rebuild');
    $('#areaSelect').multiselect({
        buttonWidth: '100%',
        maxHeight: 500,
    });
    $('#areaSelect').append('<option value="1">请选择区</option>\n');
    $('#areaSelect').multiselect('rebuild');
    //添加区域按钮事件
    $(".box-body").off('click', '.btn-add').on('click', '.btn-add', function () {
        // var selected = $('#area_tree').treeview('getSelected');
        //获取区域列表数组
        $('#provinceSelect').empty();
        $('#provinceSelect').append(getCity("queryprovince", "省"));
        $('#provinceSelect').multiselect('rebuild');

        $(".modal-title").html("添加区域");
        $("#modal-add").modal('show');
        $("input#area_name").val("");
        $("#organization_user").val("");
        is_add = true;
    });

    //删除区域按钮事件
    $(".box-body").off('click', '.btn-delete').on('click', '.btn-delete', function () {
        var selected = $('#area_tree').treeview('getSelected');
        if (selected.length == 0) {
            toastr.info("请选择需要删除的节点", opts);
        }
        // if (selected[0].parentId == null) {
        //     toastr.info("根节点不允许删除，请选择非根节点进行操作", opts);
        // }
        var leng;
        if (selected[0].nodes != null) {
            leng = selected[0].nodes.length;
        } else {
            leng = 0;
        }
        if (leng > 0) {
            toastr.info("当前节点存在子节点，请先删除子节点在删除当前节点", opts);
        } else {
            //判断当前节点下是否有数据，如果有则需要先删除数据再删除当前节点
            var selectedNode = $("#area_tree").treeview('getSelected', null);
            $.ajax({
                url: BASE_PATH + "shop/queryshopbyareaid/" + selectedNode[0]['id'],
                type: "GET",
                cache: false,
                dataType: "json",
                success: function (result) {
                    if (result.data.length > 0) {
                        confirm('提示', '当前节点下绑定有店铺数据，需要先删除相应的店铺数据再删除当前节点', function () {
                        });
                    } else {
                        var area_id = selected[0].id;
                        var area_name = selected[0].text;
                        confirm('警告', '真的要删除 ' + area_name + ' 吗', function () {
                            var id = area_id;
                            $('.btn-delete').attr('disabled', 'disabled');
                            Request.put("area/" + id + "/deletearea", {}, function (e) {
                                if (e.success) {
                                    toastr.success("删除成功");
                                    initAreaTree();
                                } else {
                                    toastr.error(e.message);
                                }
                                $('.btn-delete').removeAttr('disabled');
                            });
                        });
                    }
                },
                error: function () {
                    toastr.warning("请求列表数据失败, 请重试");
                }
            });
        }
    });
});

