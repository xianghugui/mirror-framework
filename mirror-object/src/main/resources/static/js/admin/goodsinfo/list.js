$(document).ready(function () {

    var goods_list = [];
    var inited = false;
    var is_add = true;

    //富文本编辑器初始化

    var editor = KindEditor.create('#goods_describe', {
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


    function endLength(str) {
        var n = str.getAttribute('src').search(/\./i);
        if (n == -1) {
            return str.length;
        }
        return n;
    }

    // $('#base_tree').treeview({
    //
    //     selectedColor: "#193a32b8"
    // })
    // >>构建服装树
    var initGoodsClassTree = function () {
        Request.get("goodsclass/currentNodeTree", function (e) {
            goods_list = e;
            var tree = goodsTree.init();
            var rootNode = tree.getRootNodes(e);
            $('#base_tree').treeview({
                data: rootNode,
                selectedBackColor: "#07100e",
                onNodeSelected: function (event, data) {
                    baseTable.ajax.reload().draw();
                }
            });
            $('#base_goods_tree').treeview({
                data: rootNode,
                selectedBackColor: "#07100e",
                onNodeSelected: function (event, data) {
                    baseTable.ajax.reload().draw();
                }
            });
            $('#base_tree').treeview('selectNode', [0]);
        });
    }
    var goodsTree = {
        init: function () {
            if (inited) return this;
            if (jQuery === undefined) {
                console.error("Required jQuery support is not available");
            } else {
                inited = true;
                var that = this;
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
                        id: item.id,
                        level: item.level,
                        parentId: item.parentId,
                        text: item.className,
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
                if (item['parentId'] == parentNode['id']) {
                    var obj = {
                        id: item.id,
                        level: item.level,
                        parentId: item.parentId,
                        text: item.className,
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
    initGoodsClassTree();
    window.goodsTree = goodsTree.init();
    // 编辑服装树
    $('form#edit_tree_form').validate({
        rules: {
            edit_class_name: {required: true}
        },
        messages: {
            edit_class_name: {required: "类别名称不可为空"}
        },
        submitHandler: function (form) {
            var btn = $('button[type="submit"]');
            btn.attr('disabled', "true").html("保存中..请稍后");
            //服装类别图片资源id
            var imageId = null;
            var carouselSrc = $('#edit_goods_class_image .kv-preview-thumb .file-preview-image');
            if (carouselSrc && carouselSrc.length > 0) {
                for (var i = 0; i < carouselSrc.length; i++) {
                    if (carouselSrc[i].getAttribute('src').startsWith('/file/image/')) {
                        imageId = carouselSrc[i].getAttribute('src').slice(12, endLength(carouselSrc[i]));
                    }
                }
            }
            var id = $('#base_tree').treeview('getSelected')[0].id;
            var params = {
                id: id,
                className: $(form).find("#edit_class_name").val(),
                resourceId: imageId

            };
            Request.put("goodsclass/updateGoodsClass/" + id, JSON.stringify(params), function (e) {
                if (e.success) {
                    toastr.info("修改完毕", opts);
                } else {
                    toastr.error("修改失败", opts)
                }
                btn.html("保存").removeAttr('disabled');
                // 重载树
                initGoodsClassTree();
                $("#modal-tree_edit").modal('hide');
            });
        }
    });

    // 服装类别树编辑
    $('.btn-tree-edit').off('click').on('click', function () {
        var selected = $('#base_tree').treeview('getSelected');
        if (selected == null || selected.length == 0) {
            toastr.warning('请选择类型后在编辑', opts);
            return;
        } else {

            $("#edit_tree_form #edit_class_name").val(selected[0].text);
            $("input#goods_class_image_upload").fileinput('destroy');
            $("input#goods_class_image_upload").fileinput(fileinputoption);
            Request.get("goodsclass/queryGoodsClassById/" + selected[0].id, function (e) {
                if (e.success) {
                    if (e.data.resourceId != null && e.data.resourceId != '') {
                        fileinputoption.initialPreview = ["/file/image/" + e.data.resourceId];
                        fileinputoption.initialPreviewConfig = {
                            width: '160px',
                            url: '/shop/img/delete',
                            key: 1
                        }
                    }
                } else {
                    toastr.error(e.message);
                }
            });
            $("#modal-tree_edit").modal('show');
        }
    });
    // 添加顶级类别
    $('.btn-tree-add-parent').off('click').on('click', function () {
        $("input#add_parent_goods_class_image_upload").fileinput('destroy');
        delete fileinputoption.initialPreview;
        delete fileinputoption.initialPreviewConfig;
        $("input#add_parent_goods_class_image_upload").fileinput(fileinputoption);
        $("#modal_tree_add_parent").modal('show');
    });

    $("#add_parent_tree_form").validate({
        rules: {
            className: {required: true}
        },
        messages: {
            className: {required: "类别名称不能为空"}
        },
        submitHandler: function () {
            var btn = $('#submit-parent');
            btn.attr('disabled', "true");
            btn.html("保存中..请稍后");
            var imageId = null;
            var carouselSrc = $('#add_parent_goods_class_image .kv-preview-thumb .file-preview-image');
            if (carouselSrc && carouselSrc.length > 0) {
                for (var i = 0; i < carouselSrc.length; i++) {
                    if (carouselSrc[i].getAttribute('src').startsWith('/file/image/')) {
                        imageId = carouselSrc[i].getAttribute('src').slice(12, endLength(carouselSrc[i]));
                    }
                }
            }
            var params = {
                className: $("#add_parent_class_name").val(),
                resourceId: imageId
            };
            Request.post("goodsclass/goodsClassAdd", JSON.stringify(params), function (e) {
                if (e.success) {
                    toastr.info("保存完毕");
                    $("#modal_tree_add_parent").modal('hide');
                    initGoodsClassTree();
                } else {
                    toastr.error(e.message);
                }
                btn.html("保存");
                btn.removeAttr('disabled');
            });
        }
    });

    // 添加子节点
    $('.btn-tree-add').off('click').on('click', function () {
        var selected = $('#base_tree').treeview('getSelected');
        if (selected == null || selected.length == 0) {
            toastr.warning('请选择类型后再添加', opts);
            return;
        } else if ($('#base_tree').treeview('getSelected')[0].level >= 3) {
            toastr.warning('当前类别树只支持三级', opts);
        } else {
            $("input#add_child_goods_class_image_upload").fileinput('destroy');
            delete fileinputoption.initialPreview;
            delete fileinputoption.initialPreviewConfig;
            $("input#add_child_goods_class_image_upload").fileinput(fileinputoption);
            $("#modal_tree_add_child").modal('show');

            $("#add_child_tree_form").validate({
                rules: {
                    className: {required: true}
                },
                messages: {
                    className: {required: "类别名称不能为空"}
                },
                submitHandler: function () {
                    var btn = $('#submit-child');
                    btn.attr('disabled', "true");
                    btn.html("保存中..请稍后");
                    var imageId = null;
                    var carouselSrc = $('#add_child_goods_class_image .kv-preview-thumb .file-preview-image');
                    if (carouselSrc && carouselSrc.length > 0) {
                        for (var i = 0; i < carouselSrc.length; i++) {
                            if (carouselSrc[i].getAttribute('src').startsWith('/file/image/')) {
                                imageId = carouselSrc[i].getAttribute('src').slice(12, endLength(carouselSrc[i]));
                            }
                        }
                    }
                    var params = {
                        className: $("#add_child_class_name").val(),
                        id: $('#base_tree').treeview('getSelected')[0].id,
                        level: $('#base_tree').treeview('getSelected')[0].level,
                        resourceId: imageId

                    };
                    Request.post("goodsclass/goodsClassAddChild", JSON.stringify(params), function (e) {
                        if (e.success) {
                            toastr.info("保存完毕");
                            $("#modal_tree_add_child").modal('hide');
                            initGoodsClassTree();
                        } else {
                            toastr.error(e.message);
                        }
                        btn.html("保存");
                        btn.removeAttr('disabled');
                    });
                }
            });
        }
    });

    //删除服装类别
    $(".btn-tree-del").off('click').on('click', function () {
        var selected = $('#base_tree').treeview('getSelected');
        if (selected.length == 0) {
            toastr.info("请选择需要删除的节点", opts);
        }
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
            var selectedNode = $("#base_tree").treeview('getSelected', null);
            $.ajax({
                url: BASE_PATH + "goodsinfo/queryGoodsByClassId/" + selected[0].id + "/" + selected[0].level,
                type: "GET",
                cache: false,
                dataType: "json",
                success: function (result) {
                    if (result.data.length > 0) {
                        confirm('提示', '当前节点下绑定有商品数据，需要先删除相应的数据再删除当前节点', function () {
                        });
                    } else {
                        var area_name = selected[0].text;
                        confirm('警告', '真的要删除 ' + area_name + ' 吗', function () {

                            $('.btn-delete').attr('disabled', 'disabled');
                            Request.put("goodsclass/deleteGoodsClass/" + selected[0].id, {}, function (e) {
                                if (e.success) {
                                    toastr.success("删除成功");
                                    initGoodsClassTree();
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


    //  服装基本信息列表
    var baseTable = $("#base_data_table").DataTable({
        "language": lang,
        "paging": true,
        "lengthChange": true,
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
            var selected = $('#base_tree').treeview('getSelected');
            if (selected == null || selected.length == 0) {
                return;
            }
            if (selected[0].id != "base_tree" && selected[0].level !== undefined) {
                $.ajax({
                    url: BASE_PATH + "goodsinfo/queryGoodsByClassId/" + selected[0].id + "/" + selected[0].level,
                    type: "GET",
                    cache: false,
                    data: param,
                    dataType: "json",
                    success: function (result) {
                        var resultData = {};
                        resultData.draw = data.draw;
                        resultData.recordsTotal = result.data.length;
                        resultData.recordsFiltered = result.data.length;
                        resultData.data = result.data;
                        callback(resultData);
                    },
                    error: function () {
                        toastr.warning("请求列表数据失败, 请重试");
                    }
                });
            }
        },

        // goodsID
        columns: [
            {
                "data": "id",
                bSortable: false,
                targets: 0,
                width: "30px",
                render: function (data, type, row, meta) {
                    // 显示行号
                    var startIndex = meta.settings._iDisplayStart;
                    return startIndex + meta.row + 1;
                }
            },
            {"data": "goodsName"},
            {"data": "brandName"},
            {"data": "commission"},
            {"data": "cashBach"},
            {"data": "price", "searchable": false},
            {"data": "num"},
            {"data": "status"}
        ],
        "aoColumnDefs": [
            {
                "sClass": "center",
                "aTargets": ["id"],
                "bSearchable": false,
                "mData": "id",
                "mRender": function (a, b, c, d) {
                    return d.row;
                }
            },
            {
                "sClass": "center",
                "aTargets": [8],
                "mData": "id",
                "searchable": false,
                "mRender": function (a, b, c, d) {//a表示statCleanRevampId对应的值，c表示当前记录行对象
                    // 修改 删除 权限判断
                    var buttons = '<div class="btn-group">';
                    buttons += '<div class="btn-group">';
                    buttons += '<button type="button" class="btn btn-default  btn-sx dropdown-toggle" data-toggle="dropdown">操作';
                    buttons += '<span class="caret"></span></button>';
                    buttons += '<ul class="dropdown-menu">';
                    buttons += '<li><a href="javascript:;" class="btn-moreinfo" data-id="' + a + '">商品详情</a></li>';
                    buttons += '<li><a href="javascript:;" class="btn-class-list" data-id="' + a + '">规格列表</a></li>';
                    buttons += '<li><a href="javascript:;" class="btn-comment-list" data-id="' + a + '">评论列表</a></li>';
                    if (accessUpdate) {
                        buttons += '<li><a href="javascript:;" class="btn-edit" data-id="' + a + '">编辑</a></li>';
                        if (c.recommendStatus == 1) {
                            buttons += '<li><a href="javascript:;" class="btn-recommend" data-id="' + a + '">取消推荐商品</a></li>';
                        }
                        else {
                            buttons += '<li><a href="javascript:;" class="btn-cancel-recommend" data-id="' + a + '">推荐商品</a></li>';
                        }
                    }
                    if (accessDelete) {
                        if (c.status == 1) {
                            buttons += '<li><a href="javascript:;" class="btn-close" data-id="' + a + '">商品下架</a></li>';
                        }
                        else {
                            buttons += '<li><a href="javascript:;" class="btn-open" data-id="' + a + '">商品上架</a></li>';
                        }
                    }
                    buttons += '</ul></div></div>';

                    return buttons;

                }
            }
        ],
        fnRowCallback: function (nRow, aData, iDataIndex) {
            var status = aData.status;
            var html = '';
            if (status == 0) {
                html = '已下架';
            }
            else if (status == 1) {
                html = '在售';
            }
            else {
                html = '已售罄';
            }
            $('td:eq(7)', nRow).html(html);
            return nRow;
        }
    });

    // >>商品推荐
    $("#base_data_table").off('click', '.btn-recommend').on('click', '.btn-recommend', function () {
        var that = $(this);
        var id = that.data('id');
        user_id = id;
        Request.put("goodsinfo/" + id + "/recommend", {}, function (e) {
            if (e.success) {
                toastr.info("商品取消推荐成功!");
                baseTable.draw();
                baseTable.ajax.reload();
            } else {
                toastr.error(e.message);
            }
        });

    });

    // >>取消商品推荐
    $("#base_data_table").off('click', '.btn-cancel-recommend').on('click', '.btn-cancel-recommend', function () {
        var that = $(this);
        var id = that.data('id');
        Request.put("goodsinfo/" + id + "/recommend", {}, function (e) {
            if (e.success) {
                toastr.info("商品推荐成功!");
                baseTable.draw();
                baseTable.ajax.reload();
            } else {
                toastr.error(e.message);
            }
        });
    });

    // >>商品下架
    $("#base_data_table").off('click', '.btn-close').on('click', '.btn-close', function () {
        var that = $(this);
        var id = that.data('id');
        user_id = id;
        Request.put("goodsinfo/" + id + "/disable", {}, function (e) {
            if (e.success) {
                toastr.info("商品下架成功!");
                baseTable.draw();
                baseTable.ajax.reload();
            } else {
                toastr.error(e.message);
            }
        });

    });

    // >>商品上架
    $("#base_data_table").off('click', '.btn-open').on('click', '.btn-open', function () {
        var that = $(this);
        var id = that.data('id');
        Request.put("goodsinfo/" + id + "/enable", {}, function (e) {
            if (e.success) {
                toastr.info("商品上架成功!");
                baseTable.draw();
                baseTable.ajax.reload();
            } else {
                toastr.error(e.message);
            }
        });
    });

    // 添加商品
    $('#btn_add_new').off('click').on('click', function () {
        var addType = $('#base_list').attr('id');
        // 这里做判断
        switch (addType) {
            // 添加服装
            case 'base_list':
                // 基本信息分页新增
                setEmptyModalData();
                var selected = $('#base_tree').treeview('getSelected');
                var goodClassName = selected[0].text;
                var goodsClassId = selected[0].id;
                if (selected[0].level == 1) {
                    toastr.info("当前节点为类别根节点，请选择子节点添加", opts);
                } else {
                    $("#kv-explorer").fileinput(fileinputoption);
                    $("#carousel_upload").fileinput(fileinputoption);
                    $("#add_image_Compress").fileinput(fileCompressInputOption);
                    $("#goods_class_id").val(goodClassName);
                    $("#goods_class_id").data("id", goodsClassId);
                    $("#modal-basebox").modal('show');
                }
                break;
            case 'class_list':
                // 规格列表分页新增
                break;
            case 'comment_list':
                // 评价列表分页新增
                break;
        }

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
        allowedPreviewTypes: ['image'],
        allowedFileTypes: ['image'],
        allowedFileExtensions: ['jpg', 'gif', 'png'],
        maxFileCount: 10,
        maxFileSize: 2000,
        autoReplace: false,
        validateInitialCount: true,
        overwriteInitial: false,
        initialPreviewAsData: true,
        deleteUrl: '/shop/img/delete',
        uploadAsync: true //同步上传

    };
    // 文件压缩提交框选项设置
    var fileCompressInputOption = {
        required: true,
        uploadUrl: Request.BASH_PATH + 'file/imgCompressUpload',
        dropZoneTitle: "拖拽文件到这里...",
        language: 'zh', //设置语言
        showUpload: true, //是否显示上传按钮
        showRemove: true,
        showCaption: true,//是否显示标题
        showClose: true,
        allowedPreviewTypes: ['image'],
        allowedFileTypes: ['image'],
        allowedFileExtensions: ['jpg', 'gif', 'png'],
        maxFileCount: 1,
        maxFileSize: 2000,
        autoReplace: false,
        validateInitialCount: true,
        overwriteInitial: false,
        initialPreviewAsData: true,
        deleteUrl: '/shop/img/delete',
        uploadAsync: true //同步上传

    };

    // 添加或修改数据校验
    $('form#goods_form').validate({
        rules: {
            goodsTitle: {required: true},
            goodsPrice: {required: true}
        },
        messages: {
            goodsTitle: {required: "请填写商品标题"},
            goodsPrice: {required: "价格不能为空"}
        },
        submitHandler: function (form) {
            //同步富文本编辑器，才能取得富文本编辑器内地内容
            editor.sync();
            var ele = $(form);
            var btn = $('button[type="submit"]');
            btn.attr('disabled', "true").html("保存中..请稍后");
            var reqType = $(form).data('type') == '0';
            var goodsId = $(form).find('#goods_id').val();
            var req = reqType ? Request.post : Request.put;
            var imgs = new Array(), carouselImgUrls = new Array(), brand = '';
            var CompressUrl = 0;
            var carouselSrc = $('#add_carousel_img_list .kv-preview-thumb .file-preview-image');
            if (carouselSrc && carouselSrc.length > 0) {
                for (var i = 0; i < carouselSrc.length; i++) {
                    if (carouselSrc[i].getAttribute('src').startsWith('/file/image/')) {
                        carouselImgUrls[i] = carouselSrc[i].getAttribute('src').slice(12, endLength(carouselSrc[i]));
                    }
                }
            }

            var imageSrc = $('#add_img_list .kv-preview-thumb .file-preview-image');
            if (imageSrc && imageSrc.length > 0) {
                for (var i = 0; i < imageSrc.length; i++) {
                    if (imageSrc[i].getAttribute('src').startsWith('/file/image/')) {
                        imgs[i] = imageSrc[i].getAttribute('src').slice(12, endLength(imageSrc[i]));
                    }
                }
            }
            //获取商品压缩资源 id
            var goodsCompressSrc = $('#add_image_Compress_list .kv-preview-thumb .file-preview-image');
            if (goodsCompressSrc && goodsCompressSrc.length > 0) {
                for (var i = 0; i < goodsCompressSrc.length; i++) {
                    if (goodsCompressSrc[i].getAttribute('src').startsWith('/file/image/')) {
                        var index = goodsCompressSrc[i].getAttribute('src').lastIndexOf(".");
                        if (index == null || index == -1) {
                            CompressUrl = goodsCompressSrc[i].getAttribute('src').slice(12);
                        } else {
                            CompressUrl = goodsCompressSrc[i].getAttribute('src').slice(12, index);
                        }
                        break;
                    }
                }
            }
            var brandId = $('.multiselect-container .active input').val();
            if (brandId == "0") {
                toastr.warning('品牌未选择！请勾选要品牌服装信息的品牌后再提交', opts);
                btn.html("保存");
                btn.removeAttr('disabled');
            }
            else if (carouselImgUrls == "") {
                toastr.warning('商品轮播图片未上传！请上传商品轮播图片后再提交', opts);
                btn.html("保存");
                btn.removeAttr('disabled');
            } else if (imgs == "") {
                toastr.warning('商品图片未上传！请上传商品图片后再提交', opts);
                btn.html("保存");
                btn.removeAttr('disabled');
            }
            else if (CompressUrl == "" || CompressUrl == 0 || CompressUrl == undefined || CompressUrl == null) {
                toastr.warning('商品压缩图片未上传！请上传缩图片后再提交', opts);
                btn.html("保存");
                btn.removeAttr('disabled');
            }
            else {
                var params = {
                    brandId: brandId,
                    goodsClassId: ele.find('#goods_class_id').data("id"),
                    cashBach: ele.find('#cashBach').val(),
                    commission: ele.find('#commission').val(),
                    goodsName: ele.find('#goods_title').val(),
                    imgIds: imgs,
                    compressId: CompressUrl,
                    CarouselImgUrls: carouselImgUrls,
                    price: ele.find('#goods_price').val(),
                    describe: editor.html()
                };
                req('goodsinfo/' + (reqType ? 'insertGoods/' : 'updateGoods/' + goodsId), JSON.stringify(params), function (e) {
                    if (e.success) {
                        toastr.success('操作成功！', opts);
                        baseTable.ajax.reload().draw();
                        $("#modal-basebox").modal('hide');
                    } else {
                        toastr.warning('操作失败', opts);
                    }
                    btn.removeAttr('disabled').html('保存');
                    window.location.reload();
                });
            }
        }
    });
    // 商品编辑---------------------------------------------------------------------

    var setModalData = function (obj) {
        $('#input_goods_name').val(obj.goodsName);
        $('#input_goods_price').val(obj.price);
        $('#input_cashBach').val(obj.cashBach);
        $('#input_commission').val(obj.commission);
        $('#input_goods_id').val(obj.className);
        $('#input_goods_describe').append(obj.describe);
        $('#input_goods_brand').val(obj.brandName);
    };

    var setEmptyModalData = function () {
        $('#input_goods_name').val('');
        $('#input_goods_price').val('');
        editor.html('');
        //初始化图片上传控件
        $("input#kv-explorer").fileinput('destroy');
        $("input#carousel_upload").fileinput('destroy');
        delete fileinputoption.initialPreview;
        delete fileinputoption.initialPreviewConfig;
        $("input#kv-explorer").fileinput(fileinputoption);
        $("input#carousel_upload").fileinput(fileinputoption);
        //商品图片封面图压缩初始化 begin
        $("input#add_image_Compress").fileinput('destroy');
        delete fileCompressInputOption.initialPreview;
        delete fileCompressInputOption.initialPreviewConfig;
        $("input#add_image_Compress").fileinput(fileCompressInputOption);
        //商品图片封面图压缩初始化 end
        $('#goods_title').val('');
        $('#cashBach').val('');
        $('#commission').val('');
        $('#goods_price').val('');
        $('#goods_class_id').val('');
        $('#goods_describe').val('');
        $('input[name="imgIds"]').remove();
        $('input[name="carouselImgUrl"]').remove();


        //初始化品牌下拉列表
        $('#brand-enableFiltering_brand').multiselect('deselectAll', false);
        $('#brand-enableFiltering_brand').multiselect('select', 0);
        $('#brand-enableFiltering_brand').multiselect('refresh');

    };

    //品牌列表查询
    var loadBrandList = function () {

        Request.get('brand/querybrand', {}, function (e) {
            if (e.data != null) {
                var brandlist = $("#brand-enableFiltering_brand");
                var str = '';
                var data = e.data;
                if (data.length > 0) {
                    for (var i in data) {
                        str += '<option value="' + data[i].u_id + '" >' + data[i].name + '</option>'
                    }
                }
                brandlist.append('<option value="0">请选择品牌</option>\n' + str);
                $('#brand-enableFiltering_brand').multiselect({
                    enableFiltering: true,
                    buttonWidth: '100%'
                });
            }
            else {
                toastr.warning('没有品牌信息', opts);

            }
        });
    };
    loadBrandList();

    //编辑商品信息-------------------------------------------------------------------------------
    $('#base_data_table').off('click', '.btn-edit').on('click', '.btn-edit', function () {
        var id = $(this).data('id');
        $('#goods_form').data('type', "1");

        Request.get('goodsinfo/queryGoodsById/' + id, {}, function (e) {
            if (e.success) {
                var data = e.data;
                $('#goods_id').val(data.id);
                $('#cashBach').val(data.cashBach);
                $('#commission').val(data.commission);
                $('#goods_title').val(data.goodsName);
                $('#brand-enableFiltering_brand').multiselect('select', data.brandId);
                $('#brand-enableFiltering_brand').multiselect('refresh');
                $('#goods_class_id').val(data.className);
                $('#goods_class_id').data("id", data.goodsClassId);
                $('#goods_price').val(data.price);
                editor.html(data.describe);
                if (data.imageId != null && data.imageId != '') {
                    var param = {
                        refId: data.imageId,
                        dataType: 2
                    };
                    $("input#kv-explorer").fileinput('clear');
                    Request.post('goodsinfo/pic/', JSON.stringify(param), function (r) {
                        if (r.success) {
                            var initialPreview = [];
                            var initialPreviewConfig = [];
                            for (var i = 0; i < r.data.length; i++) {
                                initialPreview.push("/file/image/" + r.data[i].resourceId);
                                initialPreviewConfig.push({
                                    width: '160px',
                                    url: '/shop/img/delete',
                                    key: i + 1
                                });
                            }
                            if (initialPreview.length != 0) {     //判断店铺图片是否为空
                                $('#kv-explorer').fileinput('destroy');
                                fileinputoption.initialPreview = initialPreview;
                                fileinputoption.initialPreviewConfig = initialPreviewConfig;
                                $('#kv-explorer').fileinput(fileinputoption);
                            }
                        }
                    });
                }

                if (data.carouselId != null && data.carouselId != '') {
                    $("input#carousel_upload").fileinput('clear');
                    var param = {
                        refId: data.carouselId,
                        dataType: 3
                    };
                    Request.post('goodsinfo/pic/', JSON.stringify(param), function (r) {
                        if (r.success) {
                            var initialPreview = [];
                            var initialPreviewConfig = [];
                            for (var i = 0; i < r.data.length; i++) {

                                initialPreview.push("/file/image/" + r.data[i].resourceId);
                                initialPreviewConfig.push({
                                    width: '160px',
                                    url: '/shop/img/delete',
                                    key: i
                                });
                            }
                            if (initialPreview.length != 0) {     //判断店铺图片是否为空
                                $('#carousel_upload').fileinput('destroy');
                                fileinputoption.initialPreview = initialPreview;
                                fileinputoption.initialPreviewConfig = initialPreviewConfig;
                                $('#carousel_upload').fileinput(fileinputoption);
                            }
                        }
                    });
                }

                if (data.compressId != null && data.compressId != '') {
                    $("input#add_image_Compress").fileinput('clear');
                    var initialPreview = [];
                    var initialPreviewConfig = [];
                    initialPreview.push("/file/image/" + data.compressId);
                    initialPreviewConfig.push({
                        width: '160px',
                        url: '/shop/img/delete',
                        key: data.compressId
                    });
                    if (initialPreview.length != 0) {     //判断商品压缩图片是否为空
                        $('#add_image_Compress').fileinput('destroy');
                        fileCompressInputOption.initialPreview = initialPreview;
                        fileCompressInputOption.initialPreviewConfig = initialPreviewConfig;
                        $('#add_image_Compress').fileinput(fileCompressInputOption);
                    }
                } else {
                    $("input#add_image_Compress").fileinput('clear');
                    var initialPreview = [];
                    var initialPreviewConfig = [];
                    //没封面图默认显示图
                    initialPreview.push("/file/image/" + data.compressId);
                    initialPreviewConfig.push({
                        width: '160px',
                        url: '/shop/img/delete',
                        key: data.compressId
                    });
                    if (initialPreview.length != 0) {     //判断商品压缩图片是否为空
                        $('#add_image_Compress').fileinput('destroy');
                        fileCompressInputOption.initialPreview = initialPreview;
                        fileCompressInputOption.initialPreviewConfig = initialPreviewConfig;
                        $('#add_image_Compress').fileinput(fileCompressInputOption);
                    }
                }
                $("#modal-basebox").modal('show');
            }
        });
    });
    // 查看详细信息
    $("#base_list").off('click', '.btn-moreinfo').on('click', '.btn-moreinfo', function () {
        var that = $(this);
        that.attr('disabled', 'disabled');
        var goods_id = $(this).data('id');

        Request.get('goodsinfo/queryGoodsById/' + goods_id, {}, function (e) {
            if (e.success) {
                setModalData(e.data);
                $("#modal-readonly").modal('show');
                that.removeAttr('disabled');

                if (e.data.imageId != null && e.data.imageId != '') {

                    var param = {
                        refId: e.data.imageId,
                        dataType: 2
                    };

                    $("input#kv-explorer_info").fileinput('clear');
                    Request.post('goodsinfo/pic', JSON.stringify(param), function (r) {
                        if (r.success) {
                            var initialPreview = [];
                            var initialPreviewConfig = [];
                            for (var i = 0; i < r.data.length; i++) {
                                initialPreview.push("/file/image/" + r.data[i].resourceId);
                                initialPreviewConfig.push({
                                    width: '160px',
                                    url: '/shop/img/delete',
                                    key: i + 1
                                });
                            }
                            if (initialPreview.length != 0) {     //判断店铺图片是否为空
                                $('#kv-explorer_info').fileinput('destroy');
                                fileinputoption.initialPreview = initialPreview;
                                fileinputoption.initialPreviewConfig = initialPreviewConfig;
                                $('#kv-explorer_info').fileinput(fileinputoption);
                                $('#kv-explorer_info').fileinput('disable');

                            }
                        }
                    });
                }

                if (e.data.carouselId != null && e.data.carouselId != '') {

                    var param = {
                        refId: e.data.carouselId,
                        dataType: 3
                    };

                    $("input#carousel_upload_info").fileinput('clear');
                    Request.post('goodsinfo/pic', JSON.stringify(param), function (r) {
                        if (r.success) {
                            var initialPreview = [];
                            var initialPreviewConfig = [];
                            for (var i = 0; i < r.data.length; i++) {

                                initialPreview.push("/file/image/" + r.data[i].resourceId);
                                initialPreviewConfig.push({
                                    width: '160px',
                                    url: '/shop/img/delete',
                                    key: i
                                });
                            }
                            if (initialPreview.length != 0) {     //判断店铺图片是否为空
                                $('#carousel_upload_info').fileinput('destroy');
                                fileinputoption.initialPreview = initialPreview;
                                fileinputoption.initialPreviewConfig = initialPreviewConfig;
                                $('#carousel_upload_info').fileinput(fileinputoption);
                                $('#carousel_upload_info').fileinput('disable');
                            }
                        }
                    });
                }
            } else if (e.code == 401) {
                window.location.reload();
            }
        });
    });

    // 规格列表点击事件
    $("#base_list").off('click', '.btn-class-list').on('click', '.btn-class-list', function () {
        var that = $(this);
        that.attr('disabled', 'disabled');
        var goods_id = $(this).data('id');
        initGoodsSpecTable(goods_id);
        $('#modal-classinfo').modal('show');

    });

    var goodsSpecTable = undefined;
    // 规格列表信息加载
    var initGoodsSpecTable = function (goodsId) {
        $('#btn-class-new').data('gid', goodsId);
        goodsSpecTable = $("#class_list_table").DataTable({
            "language": lang,
            "paging": true,
            "lengthChange": true,
            "searching": false,
            "ordering": false,
            "info": true,
            "autoWidth": false,
            "bStateSave": true,
            "bFilter": true, //搜索栏
            "destroy": true,
            "bSort": false,
            // "serverSide": true,
            "sPaginationType": "full_numbers",
            "ajax": function (data, callback) {
                var param = {};
                param.pageSize = data.length;
                param.pageIndex = data.start;
                param.page = (data.start / data.length) + 1;

                $.ajax({
                    url: BASE_PATH + "GoodsSpec/queryGoodsSpecByGoodsId/" + goodsId,
                    type: "GET",
                    cache: false,
                    data: {},
                    dataType: "json",
                    success: function (result) {
                        var resultData = {};
                        resultData.draw = data.draw;
                        resultData.recordsTotal = result.data.length;
                        resultData.recordsFiltered = result.data.length;
                        resultData.data = result.data;
                        callback(resultData);
                    },
                    error: function (jqXhr) {
                    }
                });
            },
            columns: [
                {
                    "data": "id",
                    bSortable: false,
                    targets: 0,
                    width: "30px",
                    render: function (data, type, row, meta) {
                        // 显示行号
                        var startIndex = meta.settings._iDisplayStart;
                        return startIndex + meta.row + 1;
                    }
                },
                {"data": "size"},
                {"data": "color"},
                {"data": "quality"},
            ],
            "aoColumnDefs": [
                {
                    "sClass": "center",
                    "aTargets": ["id"],
                    "mData": "id",
                    "mRender": function (a, b, c, d) {
                        return d.row;
                    }
                },
                {
                    "sClass": "center",
                    "aTargets": [4],
                    "mData": "id",
                    "mRender": function (a, b, c, d) {//a表示statCleanRevampId对应的值，c表示当前记录行对象
                        // 修改 删除 权限判断
                        var buttons = '';
                        if (accessUpdate || accessDelete) {
                            buttons = '<div class="btn-group">';
                            buttons += '<div class="btn-group">';
                            buttons += '<button type="button" class="btn btn-default  btn-sx dropdown-toggle" data-toggle="dropdown">操作';
                            buttons += '<span class="caret"></span></button>';
                            buttons += '<ul class="dropdown-menu">';
                            if (accessUpdate) {
                                buttons += '<li><a href="javascript:;" class="btn-class-edit" data-id="' + c.id + '" data-gid="' + goodsId + '">编辑</a></li>';
                            }
                            buttons += '</ul></div></div>';
                        }
                        return buttons;

                    }
                }
            ]
        });
    };

    // 绑定规格操作事件
    // 新增规格
    $('#modal-classinfo').off('click', '#btn-class-new').on('click', '#btn-class-new', function () {
        var color_tag = $('#color').find('.tag');
        var size_tag = $('#size').find('.tag');
        color_tag.css('background-color', '#209e91');
        size_tag.css('background-color', '#209e91');
        $('#tagsinput_color').data('data-color', '');
        $('#tagsinput_size').data('data-size', '');
        $('#input_class_quality').val('');
        $('#class-form').data('type', '0').data('id', '').data('gid', $(this).data('gid')); // 追加表单状态为 1 ，编辑 。 0 ， 新增
        $('#modal-class-modify').modal('show');
    });

    //标签修改
    function update_tags(clickId, showId, getId, setId) {
        var tag = $(clickId).find('.tag');
        tag.css('background-color', '#209e91');
        for (var i = 0; i < tag.length; i++) {
            if (tag[i].innerHTML == setId) {
                $(tag[i]).css('background-color', '#ff700d');
                $(showId).data(getId, tag[i].innerHTML);
            }
        }
    }

    // 修改规格
    $('#class_list_table').off('click', '.btn-class-edit').on('click', '.btn-class-edit', function () {
        var that = $(this);
        var goodsId = that.data('goodsId');
        var specId = that.data('id');
        var i = 0;
        var updateSize = null;
        var updateColor = null;
        if (goodsId != '' && specId != '') {
            Request.get('GoodsSpec/' + specId, {}, function (e) {
                if (e.success) {
                    updateSize = (update_tags('#size', '#tagsinput_size', 'data-size', e.data.size));
                    updateColor = (update_tags('#color', '#tagsinput_color', 'data-color', e.data.color));
                    $('#input_class_quality').val(e.data.quality);
                    $('#class-form').data('type', '1').data('id', specId); // 追加表单状态为 1 ，编辑 。 0 ， 新增
                    $('#modal-class-modify').modal('show');
                }
            });
        }
    });

    $('form#class-form').validate({
        rules: {
            input_class_quality: {required: true}
        },
        messages: {
            input_class_quality: {required: "请输入该规格商品数量"}
        },
        submitHandler: function (form) {

            var btn = $('button[type="submit"]');
            btn.attr('disabled', "true").html("保存中..请稍后");
            var reqType = $(form).data('type') == '0';
            var id = $(form).data('id');
            var gid = $(form).data('gid');
            var req = reqType ? Request.post : Request.put;
            if ($('#tagsinput_color').data('data-color') == '') {
                toastr.warning('颜色未选择！请勾选服装信息的颜色后再提交', opts);
                btn.html("保存");
                btn.removeAttr('disabled');
            }
            else if ($('#tagsinput_size').data('data-size') == '') {
                toastr.warning('尺寸未选择！请勾选服装信息的尺寸后再提交', opts);
                btn.html("保存");
                btn.removeAttr('disabled');
            }
            else {

                var data = {
                    color: $('#tagsinput_color').data('data-color'),
                    size: $('#tagsinput_size').data('data-size'),
                    quality: $(form).find('#input_class_quality').val()
                };
                if (gid != '' && gid != undefined && gid != null) {
                    data.goodsId = gid;
                }

                req('GoodsSpec/' + (reqType ? '' : id), data, function (e) {
                    btn.html("保存").removeAttr('disabled');

                    if (e.success) {
                        toastr.success('保存成功', opts);
                        goodsSpecTable.ajax.reload().draw();
                        $('#modal-class-modify').modal('hide');
                    } else {
                        toastr.error('保存失败', opts);
                    }
                });
                return false;
            }
        }
    });

    // 标签的点击函数
    function tags_click(clickId, showId, dataId) {
        var click_list = $(clickId).find('.tag');
        click_list.off('click').on('click', function () {
            click_list.css('background-color', '#209e91');
            $(showId).data(dataId, this.innerHTML);
            $(this).css('background-color', '#ff700d');
        })
    }

    var loadSpecList = function () {
        Request.get('Specification/queryAllSpec', {}, function (e) {
            if (e.data != null) {
                var data = e.data;
                if (data.length > 0) {
                    for (var i in data) {
                        if (data[i].type == '0') {
                            $('#tagsinput_size').tagsinput('add', data[i].name);
                        }
                        else {
                            $('#tagsinput_color').tagsinput('add', data[i].name);
                        }
                    }
                }
                // 标签的点击事件
                var color_tag = (tags_click('#color', '#tagsinput_color', 'data-color'));
                var size_tag = (tags_click('#size', '#tagsinput_size', 'data-size'));
            }
            else {
                toastr.warning('没有品牌信息', opts);
            }
        });
    };
    loadSpecList();

//    商品评论点击事件
    $("#base_list").off('click', '.btn-comment-list').on('click', '.btn-comment-list', function () {
        var that = $(this);
        that.attr('disabled', 'disabled');
        var goodsId = $(this).data('id');
        initGoodsCommentTable(goodsId);
        $('#modal-comment-box').modal('show');

    });

    //   商品评论信息加载
    var initGoodsCommentTable = function (goodsId) {
        goodsCommentTable = $("#comment_list_table").DataTable({
            "language": lang,
            "paging": true,
            "lengthChange": true,
            "searching": false,
            "ordering": false,
            "info": true,
            "autoWidth": false,
            "bStateSave": true,
            "bFilter": true, //搜索栏
            "destroy": true,
            "bSort": false,
            // "serverSide": true,
            "sPaginationType": "full_numbers",
            "ajax": function (data, callback, settings) {
                var param = {};
                param.pageSize = data.length;
                param.pageIndex = data.start;
                param.page = (data.start / data.length) + 1;

                $.ajax({
                    url: BASE_PATH + "goodscomment/queryGoodsComment/" + goodsId,
                    type: "GET",
                    cache: false,
                    data: {},
                    dataType: "json",
                    success: function (result) {
                        var resultData = {};
                        resultData.draw = data.draw;
                        resultData.recordsTotal = result.data.length;
                        resultData.recordsFiltered = result.data.length;
                        resultData.data = result.data;
                        callback(resultData);
                    },
                    error: function (jqXhr) {
                        // toastr.warning("请求列表数据失败, 请重试");
                    }
                });
            },
            columns: [
                {
                    "data": "id",
                    bSortable: false,
                    targets: 0,
                    width: "30px",
                    render: function (data, type, row, meta) {
                        // 显示行号
                        var startIndex = meta.settings._iDisplayStart;
                        return startIndex + meta.row + 1;
                    }
                },
                {"data": "userId"},
                {"data": "star"},
                {"data": "content"},
                {"data": "createTime"}
            ]
        });
    };

    //服装编辑--点击服装类别
    $('#goods_class_id').on('click', function () {
        $(".goods-modal-title").html("修改服装类别");
        $("#modal-push").modal('show');
    });

    $('#brand_tree').validate({
        submitHandler: function (form) {
            var selected = $('#base_goods_tree').treeview('getSelected')
            var btn = $('button[type="submit"]');
            btn.attr('disabled', "true").html("保存中..请稍后");
            if (selected[0].level != 3) {
                toastr.info("请选择子节点服装类别", opts);
            }
            else {
                $('#goods_class_id').data("id", selected[0].id);
                $('#goods_class_id').val(selected[0].text);

                $("#modal-push").modal('hide');
            }
            btn.removeAttr('disabled').html('保存');
            setTimeout(function () {
                $('body').addClass('modal-open')
            }, 1000)
        }
    });

    //二级弹出框关闭后，让一级弹出框可以滚动
    $('.data_no_change,#push_close').on('click', function () {
        setTimeout(function () {
            $('body').addClass('modal-open')
        }, 500)
    });

    $('#btn_add_brand').on('click', function () {
        $("#modal-brand-info").modal('show');
    });

    // 服装品牌列表信息加载
    var initGoodsBrandTable = ($("#brand_list_table").DataTable({
        "language": lang,
        "paging": true,
        "lengthChange": true,
        "searching": false,
        "ordering": false,
        "info": true,
        "autoWidth": false,
        "bStateSave": true,
        "bFilter": true, //搜索栏
        "destroy": true,
        "bSort": false,
        // "serverSide": true,
        "sPaginationType": "full_numbers",
        "ajax": function (data, callback) {
            var param = {};
            param.pageSize = data.length;
            param.pageIndex = data.start;
            param.page = (data.start / data.length) + 1;

            $.ajax({
                url: BASE_PATH + "brand/querybrand",
                type: "GET",
                cache: false,
                data: {},
                dataType: "json",
                success: function (result) {
                    var resultData = {};
                    resultData.draw = data.draw;
                    resultData.recordsTotal = result.data.length;
                    resultData.recordsFiltered = result.data.length;
                    resultData.data = result.data;
                    callback(resultData);
                },
                error: function (jqXhr) {
                }
            });
        },
        columns: [
            {
                "data": "id",
                bSortable: false,
                targets: 0,
                width: "30px",
                render: function (data, type, row, meta) {
                    // 显示行号
                    var startIndex = meta.settings._iDisplayStart;
                    return startIndex + meta.row + 1;
                }
            },
            {"data": "name"},
            {"data": "userName"}
        ],
        "aoColumnDefs": [
            {
                "sClass": "center",
                "aTargets": ["id"],
                "mData": "id",
                "mRender": function (a, b, c, d) {
                    return d.row;
                }
            },
            {
                "sClass": "center",
                "aTargets": [3],
                "mData": "id",
                "mRender": function (a, b, c, d) {//a表示statCleanRevampId对应的值，c表示当前记录行对象
                    // 修改 删除 权限判断
                    var buttons = '';
                    if (accessUpdate || accessDelete) {
                        buttons = '<div class="btn-group">';
                        buttons += '<div class="btn-group">';
                        buttons += '<button type="button" class="btn btn-default  btn-sx dropdown-toggle" data-toggle="dropdown">操作';
                        buttons += '<span class="caret"></span></button>';
                        buttons += '<ul class="dropdown-menu">';
                        if (accessUpdate) {
                            buttons += '<li><a href="javascript:;" class="btn-class-edit" data-userId="' + c.userId + '" data-name="' + c.name + '" data-id="' + a + '">编辑</a></li>';
                        }
                        if (accessDelete) {
                            buttons += '<li><a href="javascript:;" class="btn-class-delete" data-id="' + c.id + '">删除</a></li>';
                        }
                        buttons += '</ul></div></div>';
                    }
                    return buttons;

                }
            }
        ]
    }));

    //添加品牌
    $('#btn-add-brand').off('click').on('click', function () {
        $.ajax({
            url: BASE_PATH + "user/allNoBelongBrandUser/",
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

                organization_user.append('<option value="1">请选择品牌负责人</option>\n' + str);
                $('#example-enableFiltering').multiselect('destroy');
                $('#example-enableFiltering').multiselect({
                    enableFiltering: true,
                    buttonWidth: '479px',
                });
            },
            error: function () {
                toastr.warning("数据加载失败，请刷新列表重新加载！");
                return false;
            }
        });
        $(".modal-title").html("添加品牌");
        $('#brand-form').data('type', '0').data('id', '');
        $('#brand_name').val("");
        $('#modal-brand-modify').modal('show');
    });


    //修改品牌信息
    $('#brand_list_table').off('click', '.btn-class-edit').on('click', '.btn-class-edit', function () {
        $.ajax({
            url: BASE_PATH + "user/addBrandUser/" + $(this).data('userid'),
            type: "GET",
            cache: false,
            async: false,
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
                organization_user.append('<option value="1">请选择品牌负责人</option>\n' + str);
                $('#example-enableFiltering').multiselect({
                    enableFiltering: true,
                    buttonWidth: '479px',
                });

            },
            error: function () {
                toastr.warning("数据加载失败，请刷新列表重新加载！");
                return false;
            }
        });
        $(".modal-title").html("修改品牌");
        $('#brand-form').data('type', '0').data('id', $(this).data('id'));
        $('#brand_name').val($(this).data('name'));
        console.log($(this).data('userid'))
        $('#example-enableFiltering').multiselect('select', $(this).data('userid'));
        $('#example-enableFiltering').multiselect('refresh');
        $('#modal-brand-modify').modal('show');
    });

    //删除品牌
    $('#brand_list_table').off('click', '.btn-class-delete').on('click', '.btn-class-delete', function () {
        Request.put('brand/' + $(this).data('id') + '/delete', {}, function (e) {
            if (e.success) {
                toastr.success('删除成功', opts);
                initGoodsBrandTable.ajax.reload().draw();
            } else {
                toastr.warning(e.message);
            }
        });
    });

    $('form#brand-form').validate({
        rules: {
            brand_name: {required: true}
        },
        messages: {
            brand_name: {required: "请输入品牌名"}
        },
        submitHandler: function (form) {

            var btn = $('button[type="submit"]');
            btn.attr('disabled', "true").html("保存中..请稍后");
            var reqType = $(form).data('id') == "";
            var id = $(form).data('id');
            var userId = $("#example-enableFiltering :selected").val();
            var req = reqType ? Request.post : Request.put;
            var data = {
                name: $('#brand_name').val(),
                status: "1",
                userId: userId
            };
            req('brand/' + (reqType ? '' : id), data, function (e) {
                btn.html("保存").removeAttr('disabled');

                if (e.success) {
                    toastr.success('保存成功', opts);
                    initGoodsBrandTable.ajax.reload().draw();
                    $('#modal-brand-modify').modal('hide');
                } else {
                    toastr.error('保存失败', opts);
                }
            });
            return false;
        }
    });


    $('#btn-close').off('click').on('click', function () {
        $("#modal-brand-info").modal('hide');
        window.location.reload();
    });
});