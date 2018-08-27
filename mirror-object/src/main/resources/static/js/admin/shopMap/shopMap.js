$(document).ready(function () {
    //腾讯地图

    var map;

    //初始化地图函数  自定义函数名init
    function init(brandId) {
        //定义map变量 调用 qq.maps.Map() 构造函数   获取地图显示容器
         map = new qq.maps.Map(document.getElementById("allmap"), {
            center: new qq.maps.LatLng(39.916527, 116.397128),      // 地图的中心地理坐标。
            zoom: 8                                                 // 地图的中心地理坐标。
        });

        $.ajax({
            url: "shop/queryAllShopInfo",
            type: "GET",
            async: false,
            data: {brandId: brandId},
            success: function (e) {
                var deviceNum = '';
                if (e.data != null && e.data.length > 0) {
                    for (var i = 0; i < e.data.length; i++) {
                        var shopCenter = new qq.maps.LatLng(e.data[i].latitude, e.data[i].longtitude);
                        var marker = new qq.maps.Marker({
                            position: shopCenter,
                            map: map
                        });
                        marker.setTitle(e.data[i].name);
                        //设置Marker的动画属性为从落下
                        marker.setAnimation(qq.maps.MarkerAnimation.DOWN);
                        deviceNum = '';
                        deviceNum += e.data[i].deviceNum;
                        var label = new qq.maps.Label({
                            position: shopCenter,
                            map: map,
                            content: deviceNum,
                            style:{color:"red",fontSize:"14px",fontWeight:"bold",border:"none",background:"none"},
                            offset: new qq.maps.Size(-7, -60)

                        });
                    }
                }
            }
        });
    }



    loadBrandList();

    //调用初始化函数地图
    init();

    //加载品牌信息
    function loadBrandList() {

        Request.get('brand/querybrand', {}, function (e) {
            if (e.data != null) {
                var brandlist = $("#shopBrand");
                var str = '';
                var data = e.data;
                if (data.length > 0) {
                    for (var i in data) {
                        str += '<option value="' + data[i].u_id+ '" >' + data[i].name + '</option>'
                    }
                }
                brandlist.append('<option value="">全部</option>\n' + str);
                $('#shopBrand').multiselect({
                    enableFiltering: true,
                    buttonWidth: '100%'
                });
            }
            else {
                toastr.warning('没有品牌信息', opts);

            }
        });
    };

    $('#shopBrand').on('change', function () {
        var brandId = $('#shopBrand').val();
        init(brandId);
    });

});



