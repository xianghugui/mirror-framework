$(document).ready(function () {
    var inited = false;
    var area_list = [];
    var shop_list = null;
    var device_list = null;
    var is_add = true;  //记录是否添加图片，如果为false则为编辑图片
    var shop_id = null; //记录店铺id
    var area_id = null;
    var device_id = null;
    if (typeof String.prototype.startsWith != 'function') {
        String.prototype.startsWith = function (prefix) {
            return this.slice(0, prefix.length) === prefix;
        }
    }
    ;

    if (typeof String.prototype.endsWith != 'function') {
        String.prototype.endsWith = function (suffix) {
            return this.indexOf(suffix, this.length - suffix.length) !== -1;
        }
    }
    ;


    //文件提交框选项设置
    var fileinputoption = {
        required: true,
        uploadUrl: Request.BASH_PATH + 'file/shopImgUpload',
        dropZoneTitle: "拖拽文件到这里...",
        language: 'zh', //设置语言
        showUpload: true, //是否显示上传按钮
        showRemove: true,
        showCaption: true,//是否显示标题
        showClose: true,
        showUploadedThumbs: true,
        allowedPreviewTypes: ['image'],
        allowedFileTypes: ['image'],
        allowedFileExtensions: ['jpg'],
        maxFileCount: 1,
        maxFileSize: 2000,
        autoReplace: false,
        overwriteInitial: false,
        validateInitialCount: true,
        initialPreviewAsData: true,
        deleteUrl: '/shop/img/delete',
        uploadAsync: true //异步上传
    };

    $("#business_url").fileinput(fileinputoption);

    $("#logo").fileinput(fileinputoption);

    fileinputoption["maxFileCount"] = 10;
    $("#img1").fileinput(fileinputoption);

    $('input#img1').on('filebatchselected', function () {
        //如果选择的图片大于3张，则清空图片预览区
        if ($('input#img1').fileinput('getFilesCount') > 10) {
            $('input#img1').fileinput('clear');
            toastr.info("店铺图片不能多于10张", opts);
        }
    });
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
        lang.searchPlaceholder = "店铺名称/负责人";
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
            "mark": {
                "exclude": [".exclude"]
            },
            // "serverSide": true,
            "ajax": function (data, callback, settings) {
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
                    data: "uId",
                    // bSortable: false,
                    searchable: false,
                    // orderable: false,
                    targets: 0,
                    width: "30px",
                    className: "exclude",
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
                {data: "status", orderable: false, "className": "exclude"},
            ],
            "aoColumnDefs": [
                {"bSortable": false, "aTargets": [0]},
                {
                    "sClass": "center",
                    "aTargets": [6],
                    "mData": "id",
                    "mRender": function (a, b, c, d) {//c表示当前记录行对象

                        var buttons = '<div class="btn-group">';
                        buttons += '<button type="button" class="btn btn-default  btn-sx dropdown-toggle" data-toggle="dropdown">操作';
                        buttons += '<span class="caret"></span></button>';
                        buttons += '<ul class="dropdown-menu" style="min-width:100%;">';
                        buttons += '<li><a href="javascript:;" class="btn-message" data-id="' + c.uId + '">店铺详情</a></li>';
                        buttons += '<li><a href="javascript:;" class="btn-device" data-id="' + c.uId + '"data-areaid = "' + c.areaId + '">设备信息</a></li>';
                        buttons += '<li><a href="javascript:;" class="btn-brand" data-id="' + c.uId + '">品牌信息</a></li>';
                        if (accessUpdate) {
                            buttons += '<li><a href="javascript:;" class="btn-edit" data-id="' + c.uId + '">编辑</a></li>';
                        }
                        if (accessDelete) {
                            if (c.status == 1) {
                                buttons += '<li><a href="javascript:;" class="btn-close" data-id="' + c.uId + '">禁用</a></li>';
                            } else {
                                buttons += '<li><a href="javascript:;" class="btn-open" data-id="' + c.uId + '">启用</a></li>';
                            }
                        }
                        buttons += '<li><a href="javascript:;" class="btn-delete" data-id="' + c.uId + '">删除店铺</a></li>';
                        buttons += '</ul></div>';

                        return buttons;
                    }
                }
            ],
            fnRowCallback: function (nRow, aData, iDataIndex) {
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

    //******************添加店铺按钮事件begin*************************
    $(".box-tools").off('click', '.btn-add').on('click', '.btn-add', function () {

        $.ajax({
            url: BASE_PATH + "tuser/addshopquerytuser/" +null,
            type: "GET",
            cache: false,
            data: null,
            dataType: "json",
            success: function (text) {
                var organization_user = $("#example-enableFiltering");
                   organization_user.empty();
                var str = '';
                var data = text.data;
                if (data.length > 0) {
                    for (var i = 0; i < data.length; i++) {
                        str += '<option value="' + data[i].uId + '" data-phone="' + data[i].phone + '">' + data[i].name + '</option>'
                    }
                }

                organization_user.append('<option value="1">请选择负责人</option>\n' + str);
                $('#example-enableFiltering').multiselect('destroy');
                $('#example-enableFiltering').multiselect({
                    enableFiltering: true,
                    buttonWidth: '150px',
                });
            },
            error: function () {
                toastr.warning("数据加载失败，请刷新列表重新加载！");
                return false;
            }
        });
        var selected = $('#area_tree').treeview('getSelected');
        if (selected.length == 0 || selected[0].parentId == undefined) {
            toastr.info("请选择新增店铺所处区域", opts);
        }
        if (selected.length != 0 && selected[0].nodes != null) {
            toastr.info("当前节点存在后续节点，请选择末节点添加店铺", opts);
        }
        else {
            $('#modal-add input.form-control').val('');


            //初始化图片上传控件
            $("input#logo").fileinput('destroy');
            $("input#img1").fileinput('destroy');
            $("input#business_url").fileinput('destroy');
            delete fileinputoption.initialPreview;
            delete fileinputoption.initialPreviewConfig;
            $("input#logo").fileinput(fileinputoption);
            $("input#img1").fileinput(fileinputoption);
            $("input#business_url").fileinput(fileinputoption);
            //负责人选项重置
            $('#example-enableFiltering').multiselect('select', '1');
            $('#example-enableFiltering').multiselect('refresh');
            $('#province').val("1");

            //地图定位
            searchService.search($('#area_tree').treeview('getParent', selected[0]).text + selected[0].text);
            map.zoomTo(12);

            $("#modal-add input").removeAttr("disabled");
            $('#longitude,#latitude').attr('disabled', 'disabled');

            window.editor.readonly(false);

            //初始化图文信息框
            window.editor.html('');

            $("#longitude").val(marker.getPosition().getLng());
            $("#latitude").val(marker.getPosition().getLat());

            $('#footer').show();
            $(".modal-title").html("新增店铺");
            $("#modal-add").modal('show');
            is_add = true;
        }
    });
    //******************添加店铺按钮事件end********************************


    $("#shop_form").validate({
        rules: {
            shop_name: {required: true},
            principal: {required: true},
            principal_tel: {required: true, minlength: 11, maxlength: 11},
            address: {required: true}
        },
        messages: {
            shop_name: {required: "店铺名不能为空."},
            principal: {required: "负责人不能为空."},
            principal_tel: {required: "负责人电话不能为空.", maxlength: "请输入11位正确的电话号码", minlength: "请输入11位正确的电话号码"},
            address: {required: "详细地址不能为空."}
        },
        submitHandler: function (form) {

            var logoUrl = null; //记录店铺logo 资源id
            var businessUrl = null; //记录店铺营业执照 资源id
            var imgUrl = [];    //记录店铺图片 资源id

            var btn = $('#submitBtn');

            btn.attr('disabled', "true");
            btn.html("保存中..请稍后");
            var selected = $('#area_tree').treeview('getSelected');
            //同步富文本编辑器，才能取得富文本编辑器内地内容
            editor.sync();

            //获取logo资源id
            var logoSrc = $('#logo-div .kv-preview-thumb .file-preview-image');
            if (logoSrc && logoSrc.length > 0) {
                for (var i = 0; i < logoSrc.length; i++) {
                    if (logoSrc[i].getAttribute('src').startsWith('/file/image/')) {
                        var index = logoSrc[i].getAttribute('src').lastIndexOf(".");
                        if (index == null || index == -1) {
                            logoUrl = logoSrc[i].getAttribute('src').slice(12);
                        } else {
                            logoUrl = logoSrc[i].getAttribute('src').slice(12, index);
                        }
                        break;
                    }
                }
            }

            var businessSrc = $('#business-div .kv-preview-thumb .file-preview-image');
            if (businessSrc && businessSrc.length > 0) {
                for (var i = 0; i < businessSrc.length; i++) {
                    if (businessSrc[i].getAttribute('src').startsWith('/file/image/')) {
                        var index = businessSrc[i].getAttribute('src').lastIndexOf(".");
                        if (index == null || index == -1) {
                            businessUrl = businessSrc[i].getAttribute('src').slice(12);
                        } else {
                            businessUrl = businessSrc[i].getAttribute('src').slice(12, index);
                        }
                        break;
                    }
                }
            }

            var imgSrc = $('#shop-img-div .kv-preview-thumb .file-preview-image');
            if (imgSrc && imgSrc.length > 0) {
                for (var i = 0; i < imgSrc.length; i++) {
                    if (imgSrc[i].getAttribute('src').startsWith('/file/image/')) {
                        var index = imgSrc[i].getAttribute('src').lastIndexOf(".");
                        if (index == null || index == -1) {
                            imgUrl += imgSrc[i].getAttribute('src').slice(12) + " ";
                        } else {
                            imgUrl += imgSrc[i].getAttribute('src').slice(12, index) + " ";
                        }
                    }
                }
            }
            var shop_leader_id = $("#example-enableFiltering :selected").text();
            if (!logoUrl) {
                toastr.warning('店铺logo图片未上传！请上传logo图片后再提交', opts);
                btn.html("保存");
                btn.removeAttr('disabled');
            }
            else if (!businessUrl) {
                toastr.warning('店铺营业执照图片未上传！请上传营业执照图片后再提交', opts);
                btn.html("保存");
                btn.removeAttr('disabled');
            }
            else if (imgUrl.length == 0) {
                toastr.warning('店铺图片未上传！请上传图片后再提交', opts);
                btn.html("保存");
                btn.removeAttr('disabled');
            }else  if(shop_leader_id == "请选择负责人" || shop_leader_id == 1){

                    toastr.warning('店铺负责人为必选项，请选择店铺负责人之后再提交', opts);
                    btn.html("保存");
                    btn.removeAttr('disabled');
            }
            else  {
                if (!$("#address").data("address") || $("#address").data("address") == "") {
                    var selectedParent = $('#area_tree').treeview('getParent', selected[0]);
                    var address = $('#area_tree').treeview('getParent', selectedParent).text + "," +
                        $('#area_tree').treeview('getParent', selected[0]).text + "," +
                        selected[0].text + "," + $("#address").val();
                } else {
                    var address = $("#address").data("address") + $("#address").val();
                }
                var longitude = $("#longitude").val();
                var latitude = $("#latitude").val();
                if (longitude == '' || longitude == undefined || latitude == '' || latitude == undefined) {
                    toastr.warning('店铺坐标为必填项，请先选择店铺位置再上传', opts);
                    btn.html("保存");
                    btn.removeAttr('disabled');
                } else {
                    shop_leader_id = $('#example-enableFiltering option:selected').val();
                    var params = {
                        shopName: $("#shop_name").val(),
                        logo: logoUrl,
                        userId: shop_leader_id,
                        businessUrl: businessUrl,
                        address: address,
                        longitude: longitude,
                        latitude: latitude,
                        img: imgUrl.trim(),
                        areaId: selected[0].id,
                        refId: $("#logo").data("refid"),
                        shopId: shop_id,
                        //logoid
                        logoId: $("#logo").data("filerefid"),
                        //营业执照id
                        businessId: $("#business_url").data("filerefid"),
                        //店铺图片id数组
                        imgsId: $("#img1").data("filerefid"),
                        content: window.editor.html()
                    };

                    var req = is_add ? Request.post : Request.put;
                    req("shop" + (is_add ? "/add" : "/update"), JSON.stringify(params), function (e) {
                        if (e.success) {
                            toastr.success("保存完毕");
                            $("#modal-add").modal('hide');
                            shop_list.draw();
                            shop_list.ajax.reload();
                            window.editor.html('');
                        } else {
                            toastr.error(e.message);
                        }
                    });
                    btn.html("保存");
                    btn.removeAttr('disabled');
                }
            }
        }
    });

    //删除店铺操作
    $("#shop_list").off('click', '.btn-delete').on('click', '.btn-delete', function () {
        var that = $(this);
        shop_id = that.data('id');
        if (shop_id == null) {
            toastr.error("请选择需要删除的店铺");
        }
        else {
            confirm('警告', '真的要删除该店铺 吗?', function () {
                // 请求 module_id 删除
                Request.delete("shop/delete/" + shop_id, {}, function (e) {
                    if (e.success) {
                        toastr.success("删除成功");
                        shop_list.draw();
                        initAreaTree();
                        shop_list.ajax.reload();
                    } else {
                        toastr.error(e.message);
                    }
                });
            });

        }
    });

    //编辑或查看店铺详情，初始化页面信息
    var initEditorPage = function (that, title) {
        shop_id = that.data('id');
        $.ajax({
            url: BASE_PATH + "tuser/addshopquerytuser/" +that.data('id'),
            type: "GET",
            cache: false,
            data: null,
            dataType: "json",
            success: function (text) {
                var organization_user = $("#example-enableFiltering");
                organization_user.empty();
                var str = '';
                var data = text.data;
                if (data.length > 0) {
                    for (var i = 0; i < data.length; i++) {
                        str += '<option value="' + data[i].uId + '" data-phone="' + data[i].phone + '">' + data[i].name + '</option>'
                    }
                }
                $('#example-enableFiltering').multiselect('destroy');
                organization_user.append('<option value="1">请选择负责人</option>\n' + str);
                $('#example-enableFiltering').multiselect({
                    enableFiltering: true,
                    buttonWidth: '150px',
                });
            },
            error: function () {
                toastr.warning("数据加载失败，请刷新列表重新加载！");
                return false;
            }
        });

        var url = 'shop/shopInfo/' + shop_id;
        $.get(url, function (data) {            //根据店铺id，获得店铺详细信息
                if (data.success && data.data != null) {
                    var arr = data.data["address"].split(",");

                    //地图定位
                    var center = new qq.maps.LatLng(data.data["latitude"], data.data["longtitude"]);
                    latlngBounds.extend(center);
                    marker.setPosition(center);
                    map.panTo(center);
                    map.zoomTo(12);
                    //初始化各输入框
                    $("input#shop_name").val(data.data["name"]);
                    $('#example-enableFiltering').multiselect('select', data.data["userId"]);
                    $('#example-enableFiltering').multiselect('refresh');
                    $("input#principal_tel").val(data.data["userPhone"]);
                    $("input#longitude").val(data.data["longtitude"]);
                    $("input#latitude").val(data.data["latitude"]);
                    $("input#address").val(arr[3]);
                    $("input#address").attr("data-address", arr[0] + "," + arr[1] + "," + arr[2] + ",");
                    $("span#shop_state").data.state = data.data["status"];
                    //初始化上传图片预览区域
                    //初始化图片上传控件
                    $("input#logo").fileinput('destroy');
                    $("input#img1").fileinput('destroy');
                    $("input#business_url").fileinput('destroy');
                    delete fileinputoption.initialPreview;
                    delete fileinputoption.initialPreviewConfig;

                    if (data.data["Logoimgs"] != null && data.data["Logoimgs"] != '') {
                        $("input#logo").attr("data-filerefid", data.data["Logoimgs"][0].id);
                        $("input#logo").attr("data-refid", data.data["businessId"]);
                        fileinputoption.initialPreview = ["/file/image/" + data.data.Logoimgs[0].resourceId];
                        fileinputoption.initialPreviewConfig = {
                            width: '160px',
                            url: '/shop/img/delete',
                            key: 1
                        }
                        $("input#logo").fileinput(fileinputoption);
                    }

                    if (data.data["Businessimgs"] != null && data.data["Businessimgs"] != '') {
                        $("input#business_url").attr("data-filerefid", data.data["Businessimgs"][0].id);
                        fileinputoption.initialPreview = ["/file/image/" + data.data["Businessimgs"][0].resourceId];
                        fileinputoption.initialPreviewConfig = {
                            width: '160px',
                            url: '/shop/img/delete',
                            key: 11
                        };
                        $("input#business_url").fileinput(fileinputoption);
                    }
                    var imgs = data.data["imgs"];
                    var initialPreview = [];
                    var initialPreviewConfig = [];
                    var fileredid = "";
                    for (var i = 0; i < imgs.length; i++) {
                        if (imgs[i] != null && imgs[i] != '') {
                            fileredid += imgs[i].id + " ";
                            initialPreview.push("/file/image/" + imgs[i].resourceId);
                            initialPreviewConfig.push({
                                width: '160px',
                                url: '/shop/img/delete',
                                key: 100
                            });
                        }
                    }
                    $('#img1').attr("data-filerefid", fileredid.trim());
                    if (initialPreview.length != 0) {     //判断店铺图片是否为空
                        $('#img1').fileinput('destroy');
                        fileinputoption.initialPreview = initialPreview;
                        fileinputoption.initialPreviewConfig = initialPreviewConfig;
                        $('#img1').fileinput(fileinputoption);
                    }

                    window.editor.html(data.data['content']);
                    is_add = false;

                    if (title == '店铺详情') {
                        marker.setDraggable(false);
                        $("#modal-add input").attr("disabled", "disabled");
                        $("#submitBtn").attr("disabled", "disabled");
                        $("input#logo").fileinput("disable");
                        $("input#business_url").fileinput("disable");
                        $("input#img1").fileinput("disable");
                        //点击地图改变标注位置
                        qq.maps.event.addListener(map, 'click', function (event) {
                        });
                        window.editor.readonly(true);
                        // $('#footer').hide();
                    } else {
                        marker.setDraggable(true);
                        $("#modal-add input").removeAttr("disabled");
                        $("#principal_tel").attr("disabled", "disabled");
                        $("#submitBtn").removeAttr("disabled");
                        $('#longitude,#latitude').attr('disabled', 'disabled');
                        $("input#logo").fileinput("enable");
                        $("input#business_url").fileinput("enable");
                        $("input#img1").fileinput("enable");
                        //点击地图改变标注位置
                        qq.maps.event.addListener(map, 'click', function (event) {
                            marker.setPosition(event.latLng);
                        });
                        window.editor.readonly(false);
                        $('#footer').show();
                    }

                    $(".modal-title").html(title);
                    $("#modal-add").modal('show');

                }
                else {       //根据店铺id请求店铺数据失败
                    toastr.warning("数据加载失败，请重试");
                }
            }
        );
    };

    //编辑店铺
    $("#shop_list").off('click', '.btn-edit').on('click', '.btn-edit', function () {
        initEditorPage($(this), '编辑店铺');
    });

    //店铺详情
    $("#shop_list").off('click', '.btn-message').on('click', '.btn-message', function () {
        initEditorPage($(this), '店铺详情');
        $('.distpicker').remove();
    });

    //click callbak value to input frame


    //腾讯地图

    var center = new qq.maps.LatLng(39.916527, 116.397128);
    var map = new qq.maps.Map(document.getElementById("allmap"), {
        center: center,
        zoom: 12
    });
    //创建一个Marker
    var marker = new qq.maps.Marker({
        //设置Marker的位置坐标
        position: center,
        //设置显示Marker的地图
        map: map
    });

    //设置Marker的可见性，为true时可见,false时不可见，默认属性为true
    marker.setVisible(true);
    //设置Marker是否可以被拖拽，为true时可拖拽，false时不可拖拽，默认属性为false
    marker.setDraggable(true);

    //Marker拖动事件
    qq.maps.event.addListener(marker, 'position_changed', function () {
        $("#longitude").val(marker.getPosition().getLng());
        $("#latitude").val(marker.getPosition().getLat());
    });


    //根据地址信息定位
    var latlngBounds = new qq.maps.LatLngBounds();
    searchService = new qq.maps.SearchService({
        complete: function (results) {
            if (results.detail.pois && results.detail.pois.length > 0) {
                var poi = results.detail.pois[0];
                latlngBounds.extend(poi.latLng);
                marker.setPosition(poi.latLng);
                map.panTo(poi.latLng);
            }
        }
    });


    //添加->负责人信息加载

    $("#example-enableFiltering").change(function () {
        $('#principal_tel').val($('#example-enableFiltering option:selected').data("phone"));
    });

    $('#address').blur(function () {
        var selected = $('#area_tree').treeview('getSelected');
        var address = "";
        if ($(".modal-title").text == "编辑店铺") {
            address = $("#address").data("address").split(",");
            address = address[0] + address[1] + address[2] + $("#address").val();
        } else {
            address = $('#area_tree').treeview('getParent', selected[0]).text + selected[0].text + $("#address").val();
        }
        searchService.search(address);
        map.zoomTo(14);
    });


    //店铺禁用
    $("#shop_list").off('click', '.btn-close').on('click', '.btn-close', function () {
        var that = $(this);
        shop_id = that.data('id');
        $("#modal-delete .title").html('提示信息');
        $("#modal-delete .modal-body").html('您确认要禁用该店铺吗？');
        $("#modal-delete .btn-success").removeClass('btn-deldevice-sure');
        $("#modal-delete .btn-success").addClass('btn-close-sure');
        $("#modal-delete").modal('show');

    });
    $("#modal-delete").off('click', '.btn-success').on('click', '.btn-success', function () {
        if ($(this).hasClass('btn-close-sure')) {
            //禁用店铺
            Request.put("shop/" + shop_id + "/disable", {}, function (e) {
                if (e.success) {
                    toastr.info("注销成功!");
                    shop_list.draw();
                    shop_list.ajax.reload();
                } else {
                    toastr.error(e.message);
                }
            });
        } else if ($(this).hasClass('btn-deldevice-sure')) {
            //删除设备
            Request.put("shop/" + device_id + "/deldevice", {}, function (e) {
                if (e.success) {
                    toastr.info("删除成功!");
                    device_list.draw();
                    device_list.ajax.reload();
                } else {
                    toastr.error(e.message);
                }
            });
        }

    });
    //店铺启用
    $("#shop_list").off('click', '.btn-open').on('click', '.btn-open', function () {
        var that = $(this);
        var id = that.data('id');
        Request.put("shop/" + id + "/enable", {}, function (e) {
            if (e.success) {
                toastr.info("启用成功!");
                shop_list.draw();
                shop_list.ajax.reload();
            } else {
                toastr.error(e.message);
            }
        });
    });
    //店铺设备信息
    $("#shop_list").off('click', '.btn-device').on('click', '.btn-device', function () {
        shop_id = $(this).data("id");
        area_id = $(this).data("areaid");
        device(shop_id);
        $("#modal-shop-device").modal('show');
    });
    //新增设备
    $("#modal-shop-device").off('click', '#btn-device-new').on('click', '#btn-device-new', function () {
        //加载未关联的设备，且状态为正常
        var device_str = "<option>选择设备标识码</option>";
        $.ajax({
            url: "device/queryUndistributedDevice",
            type: "GET",
            async: false,
            success: function (e) {
                if (e.success && e.data != null) {
                    for (var i = 0; i < e.data.length; i++) {
                        device_str += '<option value="' + e.data[i].deviceId + '">' + e.data[i].deviceCode + '</option>';
                    }
                } else {
                    toastr.warning("数据加载失败，请重试");
                }
            }
        });
        $("#add_devicecode").multiselect({
            buttonWidth: '100%',
            maxHeight: 500,
            enableFiltering: true,
        });
        $("#add_devicecode").empty();
        $("#add_devicecode").append(device_str);
        $("#add_devicecode").multiselect('rebuild');
        $("#modal-adddevice").modal('show');
    });
    //添加设备
    $("#modal-adddevice").off('click', '.btn-primary').on('click', '.btn-primary', function () {
        var params = {
            shopId: shop_id,
            areaId: area_id,
            deviceId: $('#add_devicecode').treeview('getSelected')[0].value
        };
        Request.post("shop/adddevice", JSON.stringify(params), function (e) {
            if (e.success) {
                toastr.info("保存完毕");
                $("#modal-adddevice").modal('hide');
                device_list.draw();
                device_list.ajax.reload();
            } else {
                toastr.error(e.message);
            }
        });

    });


    function device(shopId) {
        device_list = $('#device_list').DataTable({
            "destroy": true,
            "language": lang,
            "paging": true,
            "lengthChange": true,
            "searching": true,
            "info": true,
            "autoWidth": false,
            "bStateSave": true,
            "mark": {
                "exclude": [".exclude"]
            },
            // "serverSide": true,
            "ajax": function (data, callback, settings) {
                var param = {};
                param.pageSize = data.length;
                param.pageIndex = data.start;
                param.page = (data.start / data.length) + 1;
                $.ajax({
                    url: BASE_PATH + "device/querydevicebyshopid/" + shopId,
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
                    "className": "exclude",
                    render: function (data, type, row, meta) {
                        // 显示行号
                        var startIndex = meta.settings._iDisplayStart;
                        return startIndex + meta.row + 1;
                    }
                },
                {data: "deviceCode"},
                {data: "areaName"},
                {data: "createTime", "className": "exclude", "searchable": false},
                {data: "status", orderable: false, searchable: false},
            ],
            // "order": [[1, 'asc']],
            "aoColumnDefs": [
                {"bSortable": false, "aTargets": [0]},
                {
                    "sClass": "center",
                    "aTargets": [5],
                    "mData": "id",
                    "mRender": function (a, b, c, d) {//c表示当前记录行对象

                        var buttons = '<button class="btn btn-danger btn-deldevice" data-id="' + c.u_id + '">删除</button>';
                        return buttons;
                    }
                }
            ],
            fnRowCallback: function (nRow, aData, iDataIndex) {
                var status = aData.status;
                var html = '<span class="fa fa-circle text-error" aria-hidden="true" style="color: red" data-state = "' + status + '"></span>';
                if (status == 0) {
                    html = '<span class="fa fa-circle text-success" aria-hidden="true" style="color: #00e765"  data-state = "' + status + '"></span>';
                }
                $('td:eq(4)', nRow).html(html);
                return nRow;
            }
        });
        //设备删除
        $("#device_list").off('click', '.btn-deldevice').on('click', '.btn-deldevice', function () {
            device_id = $(this).data("id");
            $("#modal-delete .title").html('提示信息');
            $("#modal-delete .modal-body").html('您确定要删除该设备么？');
            $("#modal-delete .btn-success").removeClass('btn-close-sure');
            $("#modal-delete .btn-success").addClass('btn-deldevice-sure');
            $("#modal-delete").modal('show');
        });
    }

    //品牌信息
    $("#shop_list").off('click', '.btn-brand').on('click', '.btn-brand', function () {
        shop_id = $(this).data("id");
        var str = "";
        $.ajax({
            url: "shop/" + shop_id + "/querybrand",
            type: "GET",
            async: false,
            success: function (e) {
                if (e.data != null && e.data.length > 0) {
                    for (var i = 0; i < e.data.length; i++) {
                        str += '<span class="label label-primary">' + e.data[i].brandName + '</span>';
                    }
                    str += "";
                } else {
                    str += '<p class=" text-center">暂无数据。</p>';
                }
            }
        });
        $('#modal-delete .modal-body').html(str);
        $("#modal-delete .title").html('品牌信息');
        $("#modal-delete .btn-success").removeClass('btn-close-sure');
        $("#modal-delete .btn-success").removeClass('btn-deldevice-sure');
        $("#modal-delete").modal('show');
    });

});



