$(document).ready(function () {
    var inited = false;
    var adDataId='';

    var device_shop_ad_list = $("#device_shop_ad_list").DataTable({
        "language": lang,
        "paging": true,
        "lengthChange": true,
        "searching": false,
        "ordering":false,
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
            $.ajax({
                url: BASE_PATH + "DevicePush/queryAllAd/",
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
                    i = Request.BASH_PATH;
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
                "data": "uId",
                bSortable: false,
                targets: 0,
                render: function (a, b, c, d) {
                    var str='<div class="panel-heading"><div><span>发布人&nbsp;:&nbsp;'+c.userName+'</span><span style="float: right">创建时间:'+c.createDate+'</span></div>' +
                        '<div><span>播放时间:'+c.startTime+'</span> -- <span>'+c.endTime+'</span></div>';
                    var button='';
                    if(c.resourcePath != null){
                        str += "<div class='item'><video id='my_video' class='video-js' controls = '' preload='none'" +
                            "poster='' style='margin: auto;height: 230px;'>" +
                            "<source src='"+c.resourcePath[0].resourceUrl+"' type='video/mp4'>" +
                            "</video></div>";
                    }
                    if(c.status==0){
                        button+='<button type="button" class="btn btn-primary  btn-sx btn-push col-md-6" data-id="'+a+'">发布广告</button>' +
                            '<button type="button" class="btn btn-primary  btn-sx btn-update col-md-6" data-id="'+a+'" >编辑广告</button></div>';
                    }
                    else {
                        button+= '<button type="button" class="btn btn-primary  btn-sx btn-update col-md-6" data-id="'+a+'" disabled="disabled">编辑广告</button>' +
                            '<button type="button" class="btn btn-default  btn-sx btn-cancel col-md-6" data-id="'+a+'">取消发布</button></div>';
                    }
                    return str+button;
                }
            }
        ]

    });

    // 文件提交框选项设置
    var fileinputoption = {
        required: true,
        uploadUrl: Request.BASH_PATH + 'file/shopImgUpload',
        dropZoneTitle: "拖拽文件到这里...",
        language: 'zh', //设置语言
        showUpload: true, //是否显示上传按钮
        showRemove: true,
        showCaption: true,//是否显示标题
        showClose: true,
        allowedPreviewTypes: ['video'],
        allowedFileTypes: ['video'],
        allowedFileExtensions: ['mp4'],
        maxFileCount: 1,
        maxFileSize: 80000,
        autoReplace: true,
        validateInitialCount: true,
        overwriteInitial: false,
        initialPreviewAsData: true,
        deleteUrl: '/shop/img/delete',
        uploadAsync: true, //同步上传
        msgFilesTooMany: "选择上传的文件数量({1}) 超过允许的最大数值{1}！"
    };

    //新增视频广告
    $(".box-tools").off('click', '.btn-add').on('click', '.btn-add', function () {
        $(".modal-title").html("创建设备广告");
        $("#modal-add").modal('show');
        setEmptyModalData();
        $('#img1').fileinput(fileinputoption);
        adDataId = '';
    });

    //编辑视频广告
    $("#device_shop_ad_list").off('click', '.btn-update').on('click', '.btn-update', function () {
        $(".modal-title").html("编辑设备广告");
        $("#modal-add").modal('show');
        var id = $(this).data('id');
        adDataId = id;
        Request.get('DevicePush/queryAdById/' + id, {}, function (e) {
            if (e.success) {
                var data = e.data;
                $('#user_name').val(data.userName);
                $('#startTime').val(data.startTime);
                $('#endTime').val(data.endTime);
                if (data.adDataId != null && data.adDataId != '') {
                    $("#img1").fileinput('clear');
                            var initialPreview = [];
                            var initialPreviewConfig = [];

                            for (var i = 0; i < data.resources.length; i++) {
                                initialPreview.push(data.resources[i]);
                                if(data.resourcesType[i] == 2){
                                    initialPreviewConfig.push({
                                        height: '160px',
                                        url: '/shop/img/delete',
                                        type:'video',
                                        filetype:'video/mp4',
                                        key: i + 1
                                    });
                                }
                                else{
                                    initialPreviewConfig.push({
                                        width: '160px',
                                        url: '/shop/img/delete',
                                        key: i + 1
                                    });
                                }
                            }
                            if (initialPreview.length != 0) {     //判断店铺图片是否为空
                                $('#img1').fileinput('destroy');

                                fileinputoption.initialPreview = initialPreview;
                                fileinputoption.initialPreviewConfig = initialPreviewConfig;
                                $('#img1').fileinput(fileinputoption);
                            }
                }
                $("#modal-basebox").modal('show');
            }
        });
    });

    //数据清空
    var setEmptyModalData = function () {
        $('#user_name').val('');
        //初始化图片上传控件
        $("#img1").fileinput('destroy');
        delete fileinputoption.initialPreview;
        delete fileinputoption.initialPreviewConfig;
        $("#img1").fileinput(fileinputoption);
        $('#startTime').val("");
        $('#endTime').val("");
    };


    //添加或修改广告
    $('form#shop_form').validate({
        rules: {
            user_name: {required: true}
        },
        messages: {
            user_name: {required: "请填写用户名"}
        },
        submitHandler: function (form) {
            var adList=new Array();
            // var adListSrc = $('#shop-img-div .kv-preview-thumb .file-preview-image');
            var adListSrc = $('#shop-img-div .kv-preview-thumb .kv-preview-data');
            var btn = $('button[type="submit"]');
            var reqType = adDataId == '';
            var req = reqType ? Request.post : Request.put;
            var startTime = $('#startTime').val();
            var endTime = $('#endTime').val();

            btn.attr('disabled', "true").html("保存中..请稍后");
            if (adListSrc && adListSrc.length > 0) {
                var index;
                var index2;
                for (var i = 0; i < adListSrc.length; i++) {
                    index = adListSrc[i].currentSrc.split(adListSrc[i].baseURI+'file/download/');
                    index2 = index[1].split(".");
                    adList[i] = index2[0];
                }
            }
            if (startTime == "") {
                toastr.warning('请选择开始时间', opts);
                btn.html("保存");
                btn.removeAttr('disabled');
            }
            if (endTime == "") {
                toastr.warning('请选择结束时间', opts);
                btn.html("保存");
                btn.removeAttr('disabled');
            }

            startTime = new Date(startTime);
            endTime = new Date(endTime);
            if(startTime.getTime() >= endTime.getTime()){
                toastr.warning('结束时间必须大于开始时间', opts);
                btn.html("保存");
                btn.removeAttr('disabled');
            }
            if (adListSrc.length == 0) {
                toastr.warning('商品广告未上传！请上传商品广告后再提交', opts);
                btn.html("保存");
                btn.removeAttr('disabled');
            }
            if(adListSrc.length > 1){
                toastr.warning('商品广告视频只能上传一个！请删除多余商品广告后再提交', opts);
                btn.html("保存");
                btn.removeAttr('disabled');
            }
            else{
                var params = {
                    userName:$('#user_name').val(),
                    adList:adList,
                    startTime:startTime,
                    endTime:endTime
                };
                req('DevicePush/'+(reqType?'':adDataId), JSON.stringify(params), function (e) {
                    if (e.success) {
                        toastr.success('操作成功！', opts);
                        $("#modal-add").modal('hide');
                    } else {
                        toastr.warning('操作失败', opts);
                    }
                    btn.removeAttr('disabled').html('保存');
                    window.location.reload();
                });
            }
        }
    });

    //加载设备区域信息
    var initAreaTree = function () {
        Request.get("area/queryall", function (e) {
            area_list = e;
            var tree = areaTree.init();
            var rootNodes = tree.getRootNodes(e);
            $('#area_tree').treeview({
                data: rootNodes,
                selectedBackColor: "#07100e",
                levels: 3,
                multiSelect: true,
                onNodeSelected: function (event, data) {
                    var currentAreaID = data.id;
                }
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
        Request.put('DeviceShopAd/cancelPush/'+id,{},function (e) {
            if(e.success){
                toastr.success('操作成功！', opts);
                device_shop_ad_list.draw();
            }
            else{
                toastr.warning('操作失败', opts);
            }
        });
    });

    //发布广告
    $("#device_shop_ad_list").off('click', '.btn-push').on('click', '.btn-push', function () {
        $(".modal-title").html("发布设备广告");
        $("#modal-push").modal('show');
        adDataId = $(this).data('id');

        $('#area_tree').treeview('collapseAll',{ silent: true });

        $('#tagsinput').tagsinput("removeAll");

        $(".modal-title").html("发布设备广告");

        $("#modal-push").modal('show');

        $('#area_tree').on('nodeSelected',function (event,data) {
            $('#tagsinput').tagsinput('add',{id:data.id,text:data.text});
            if(data.nodes!=null){
                toastr.info("你选中了父节点，将不能选择该子节点", opts);
                for(var i=0;i<data.nodes.length;i++){
                    $('#area_tree').treeview('disableNode',[ data.nodes[i].nodeId, { silent: true }]);
                    $('#tagsinput').tagsinput('remove',data.nodes[i].id);
                }
            }
        });

        $('#area_tree').on('nodeUnselected ',function (event,data) {
            $('#tagsinput').tagsinput('remove',data.id);
            if(data.nodes!=null){
                for(var i=0;i<data.nodes.length;i++){
                    $('#area_tree').treeview('enableNode',[ data.nodes[i].nodeId, { silent: true }]);
                    var nodelist = data.nodes[i];
                    if(nodelist.nodes !=null){
                        for(var h=0;h<nodelist.nodes.length;h++){
                            $('#area_tree').treeview('enableNode',[ nodelist.nodes[h].nodeId, { silent: true }]);
                        }
                    }
                }
            }
        });
    });

    $('#area_form').validate({
        submitHandler: function (form) {
            var selected = $('#area_tree').treeview('getSelected');
            var selectValue = $('#tagsinput').val();
            var btn = $('button[type="submit"]');
            btn.attr('disabled', "true").html("保存中..请稍后");
            if (selected.length == 0) {
                toastr.info("请选择需要发布的节点", opts);
            }
            else {
                var deviceIdList = new Array();
                for(var i = 0; i < selected.length; i++){
                    deviceIdList.push(selected[i].id)
                }
                var params={
                    deviceAdId:adDataId,
                    deviceIdList:deviceIdList
                };
                Request.post('DeviceShopAd/insertDeviceShopAdList',JSON.stringify(params),function (e) {
                    if(e.success){
                        toastr.success('操作成功！', opts);
                        $("#modal-push").modal('hide');
                    }
                    else{
                        toastr.warning('操作失败', opts);
                    }
                    btn.removeAttr('disabled').html('保存');
                    window.location.reload();
                })
            }
        }
    });

    //时间插件
    $(".form_datetime").datetimepicker({format: 'yyyy-mm-dd hh:ii'});

});

