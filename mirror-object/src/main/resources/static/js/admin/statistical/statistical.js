$(document).ready(function () {
    // 基于准备好的dom，初始化echarts实例
    var myChart = echarts.init(document.getElementById('statistical'));

    function initOption(selectType) {
        var now = new Date();

        loadBrandList();
        loadSelect();

        $('#select_year').multiselect('select', now.getFullYear());
        $('#select_month').multiselect('select', now.getMonth() + 1);
        $('#statistical_type').multiselect('select', 0);
        $('#select_year').multiselect('refresh');
        $('#select_month').multiselect('refresh');
        $('#statistical_type').multiselect('refresh');
        loadStatistical();
    }

    //创建图表
    function createStatistical() {
        //清除图表数据
        myChart.clear();
        //图表设置
        myChart.setOption({
            title: {
                text: '品牌销量统计'
            },
            tooltip: {
                trigger: 'axis'
            },
            legend: {
                data: []
            },
            grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
            },
            toolbox: {
                feature: {
                    saveAsImage: {}
                }
            },
            xAxis: {
                type: 'category',
                boundaryGap: false,
                data: []
            },
            yAxis: {
                type: 'value',
                name: '销量'
            },
            series: []
        });
    }

    function loadStatistical() {
        var brandName = [],
            service = [],
            sales,
            createTime,
            selectTime = "",
            statisticalType = $('#statistical_type').val(),
            shopId = "";
        $("#shopBox").css("display","none");
        createStatistical();
        console.log($("#select_brand" ).val())
        if($("#select_brand" ).val() != null && $("#select_brand" ).val() != ""){
            $("#shopBox").css("display","block");
            shopId = $("#select_shop").val();
        }

        //显示每月的全部周
        if (statisticalType === "0") {
            $("#yearBox").css("display","block");
            $("#monthBox").css("display","block");
            var selectMonth = $('#select_month').val();
            if(selectMonth < 10){
                selectMonth = "0"+selectMonth;
            }
            selectTime = $('#select_year').val() + "-" + selectMonth;
            //显示一年全部周
            if(selectMonth == 0){
                statisticalType = 4;
                selectTime = $('#select_year').val();
            }
        }
        else if (statisticalType === "1" || statisticalType === "2") {
            $("#yearBox").css("display","block");
            $("#monthBox").css("display","none");
            selectTime = 0;
        }
        else if (statisticalType === "3") {
            $("#yearBox").css("display","none");
            $("#monthBox").css("display","none");
        }


        var params = {
            brandId: $('#select_brand').val(),
            selectType: statisticalType,
            selectTime: selectTime,
            sort: $('#select_sort').val(),
            shopId: shopId
        };

        Request.get('StatisticalMain/queryWeek', params, function (e) {
            if (e.data != null && e.data.length > 0) {
                createTime = e.data[0].createTime.split(",");
                createTime.pop();
                for (var i = 0; i < e.data.length; i++) {
                    brandName.push(e.data[i].name);
                    sales = e.data[i].sales.split(",");
                    service.push({
                        name: e.data[i].name,
                        type: 'line',
                        smooth: true,
                        data: sales
                    })
                }
                // 加载数据
                myChart.setOption({
                    legend: {
                        data: brandName
                    },
                    xAxis: {
                        type: 'category',
                        boundaryGap: false,
                        data: createTime
                    },
                    series: service
                });
            } else {
                toastr.success("暂无相关统计数据");
            }
        });
    }

    //下拉数据加载
    var loadSelect = function showMonth() {

        //统计类型
        $('#statistical_type').append("" +
            "<option value='0'>按周</option>"+
            "<option value='1'>按月</option>" +
            "<option value='2'>按季</option>" +
            "<option value='3'>按年</option>");


        //销量排序
        $('#select_sort').append("<option value='0'>最高销量</option><option value='1'>最低销量</option>");


        //年份
        var nowYear = new Date().getFullYear(),
            yearStr = "",
            monthStr = "";

        for (var yearIndex = 0; yearIndex < 5; yearIndex++) {
            yearStr += "<option>" + (nowYear - yearIndex) + "</option>";
        }
        $('#select_year').append(yearStr);

        //月份
        for (var monthIndex = 1; monthIndex <= 12; monthIndex++) {
            monthStr += "<option value='" + monthIndex + "'>" + monthIndex + "月</option>";
        }
        $('#select_month').append("<option value='0'>全部</option>" + monthStr);

        $('#statistical_type,#select_sort,#select_year,#select_month').multiselect({
            enableFiltering: false,
            buttonWidth: '80%'
        });
    };

    //加载品牌信息
    var loadBrandList = function () {
        Request.get('brand/queryShopBrand', {}, function (e) {
            if (e.data != null) {
                var brandlist = $("#select_brand"),
                    str = '',
                    data = e.data;

                if (data.length > 0) {
                    for (var i in data) {
                        str += '<option value="' + data[i].id + '" >' + data[i].name + '</option>'
                    }
                }
                brandlist.append('<option value="">全部</option>' + str);
                $('#select_brand').multiselect({
                    enableFiltering: true,
                    buttonWidth: '80%'
                });
            }
        });
    };

    //加载某品牌店铺信息
    var loadShop = function () {
        var brandId = $("#select_brand").val();
        Request.get('StatisticalMain/queryAllShop', {brandId:brandId}, function (e) {
            if (e.data != null) {
                var shoplist = $("#select_shop"),
                    str = '',
                    data = e.data;

                if (data.length > 0) {
                    for (var i in data) {
                        str += "<option value='" + data[i].id + "' >" + data[i].shopName + "</option>";
                    }
                }
                shoplist.multiselect({
                    enableFiltering: true,
                    buttonWidth: '80%'
                });
                shoplist.empty();
                shoplist.append('<option value="">全部</option>'+str);
                shoplist.multiselect('rebuild');
            }
        });
    };

    initOption();

    //更改统计条件
    $("#select_month,#select_year,#select_brand,#select_sort,#statistical_type,#select_shop").on("change", function () {
        loadStatistical();
    });

    $('#select_brand').on("change",function () {
        loadShop();
    });
});