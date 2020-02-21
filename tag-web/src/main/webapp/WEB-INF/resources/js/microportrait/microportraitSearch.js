/**
 * Created by jxx on 2018/7/26.
 */
$(function () {
    var key = sessionStorage.getItem("key");
    jQuery.ajaxPost(true, "../micro/queryByNum", {'num': key}, function (data) {
        if (data.code == 10005) {
            var result = data.data.result;
            var email = result.email;
            $(".item_detils_email").attr("title", email);
            var phone = result.phoneNum;
            $(".item_detils_phone").attr("title", phone);
            var qq = result.qq;
            $(".item_detils_qq").attr("title", qq);
            $("#email").text((email == null || email == '') ? '暂无' : email);
            $("#phone").text((phone == null || phone == '') ? '暂无' : phone);
            $("#qq").text((qq == null || phone == '') ? '暂无' : qq);

            var type = result.type;
            $(".centent_gid").text(type == 0 ? "用户标示GID" : "物品标示GID");

            var allData = data.data.data;

            if (null == allData || '' == allData) {
                return;
            }
            var bottomData = allData.bottomData;
            var categories = allData.categories;
            var links = allData.links;
            var datas = allData.data;
            var data = [];
            for (var p in datas) {
                data.push({
                    //name:p,
                    showName: datas[p].name,
                    symbolSize: datas[p].symbolSize,
                    symbol: datas[p].symbol,
                    tooltip: {//此处为tootips转换
                        shor: true,
                        formatter: function (params) {
                            return params.data.showName
                        }
                    }
                })
            }
            console.log(data)
            var dom = document.getElementById("tag_show");
            var myChart = echarts.init(dom, 'macarons');
            option = {
                backgroundColor: new echarts.graphic.RadialGradient(0.3, 0.3, 0.8, [{
                    offset: 0,
                    color: '#f7f8fa'
                }, {
                    offset: 1,
                    color: '#f7f8fa'
                }]),
                title: {
                    text: "微观画像",
                    subtext: "",
                    top: "top",
                    left: "center"
                },
                tooltip: {},
                legend: [{
                    formatter: function (name) {
                        return echarts.format.truncateText(name, 40, '14px Microsoft Yahei', '…');
                    },
                    tooltip: {
                        show: true
                    },
                    selectedMode: 'false',
                    bottom: 20,
                    data: bottomData
                }],
                toolbox: {
                    show: false,
                    feature: {
                        dataView: {show: true, readOnly: true},
                        restore: {show: true},
                        saveAsImage: {show: true}
                    }
                },
                animationDuration: 3000,
                animationEasingUpdate: 'quinticInOut',
                series: [{
                    name: '标签',
                    type: 'graph',
                    layout: 'force',
                    force: {
                        repulsion: 200,
                        gravity: 0.1,
                        edgeLength: 50,
                        initLayout: 'circular'
                    },
                    data: data,
                    links: links,
                    categories: categories,
                    focusNodeAdjacency: true,
                    roam: true,
                    label: {
                        normal: {
                            show: true,
                            position: ['50%', '100%'],
                            formatter: function (params) {
                                return params.data.showName //此处为label转换
                            }
                        }
                    },
                    lineStyle: {
                        normal: {
                            color: 'source',
                            curveness: 0,
                            type: "solid"
                        }
                    }
                }]
            };
            myChart.setOption(option, true);
        } else {
            layer.msg(data.msg)
        }
    }, function (xhr) {
        layer.msg("服务器错误！")
    })


    if (baseMethod.whatBrowser() == "FF") {
        var label_item1_text1 = $(".label_item1_text1");
        //label_item1_text1.css('marginLeft',"-75px");
    }
    if (baseMethod.isIE() == true) {
        var label_item1 = $(".label_item1");
        label_item1.css('marginLeft', "-55px");
    }
    if (baseMethod.isIE()) {
        $(".label_item1_div_right").map(function () {
            var ul = $(this).find("ul").length;
            if (ul > 1) {
                var label_item1_div_right = $(".label_item1_div_right");
                label_item1_div_right.css('width', '180px');
            } else {
                var label_item1_div_right = $(".label_item1_div_right");
                label_item1_div_right.css('width', '91px');
            }
        });
    }
    var obj = {
        sroclltop: function () {
            $(window).scroll(function () {
                var s = $(window).scrollTop();
                if (s > 0) {
                    $(".left_spnal").css('z-index', '-1');
                    $(".left_spnal").css('top', '50px');
                    $(".centent_wrap_middle").css('boxShadow', '0px 1px 10px rgba(0,0,0,0.5)');
                }
                if (s == 0) {
                    $(".left_spnal").css('z-index', '0');
                    $(".left_spnal").css('top', '126px');
                    $(".centent_wrap_middle").css('boxShadow', '0px 0px 0px #fff');
                }
            });
            $(window).scroll(function () {
                var s = $(window).scrollTop();
            });
        }
    }
    /**导航切换**/
    $(document).ready(function () {
        obj.sroclltop();
    });


    /*function getNames(data){
        var names = [];
        if(null == data) {
            return names;
        }
        for(var i = 0; i < data.length; i++){
             var tag = data[i];
             var subs = [];
             subs = tag.microSubTags;
             var children = getNames(subs);
             var map = {
                     "name": tag.name,
                     "children":children
             };
             names.push(map);
         }
        return names;
    }
    //思维导图
    function first(){
        var result = ${result};
        var email = result.email;
        $(".item_detils_email").attr("title",email);
        var phone = result.phoneNum;
        $(".item_detils_phone").attr("title",phone);
        var qq = result.qq;
        $(".item_detils_qq").attr("title",qq);
        $("#email").text(email==null?'暂无':email);
        $("#phone").text(phone==null?'暂无':phone);
        $("#qq").text(qq==null?'暂无':qq);
        var tags =result.tags;
        var names = getNames(tags);
        var type = "";
        var imgsName = "";
        if(result.type=="0"){
            type = "用户";
            imgsName = "people.png";
        } else if(result.type=="1"){
            type = "物品";
            imgsName = "thing.png";
        } else {
            type = "暂无";
        }
        if(null != tags){
            var data = [{
                "name":type,
                "children": names
            }];
            var last=JSON.stringify(data);
            var dom = document.getElementById("tag_show");
            var myChart = echarts.init(dom,'macarons');
            var app = {};
            var arrData = data[0].children;
            //myChart.showLoading();
            var option = {
                    series : [
                        {
                            name:'树图',
                            type:'tree',
                            orient: 'horizontal',  // vertical horizontal
                            rootLocation: {x: 100, y: '60%'}, // 根节点位置  {x: 'center',y: 10}
                            nodePadding: 20,
                            symbol: 'emptyCircle',
                            symbolSize: 5,
                            initialTreeDepth: 6,
                            itemStyle: {
                                normal: {
                                    borderWidth: 1,
                                    borderColor: '#3393ed',
                                    label: {
                                        show: true,
                                        position: 'left',
                                        textStyle: {
                                            color: '#000',
                                            fontSize: 14
                                        }
                                    },
                                    lineStyle: {
                                        color: '#ccc',
                                        width: 1,
                                        type: 'solid'
                                    }
                                },
                                emphasis: {
                                    label: {
                                        show: true
                                    }
                                }
                            },
                            data: [
                                {
                                    name: '',
                                    value: 6,
                                    symbolSize: [113, 45],
                                    symbol: 'image://${basePath}/res/imgs/'+imgsName,
                                    itemStyle: {
                                        normal: {
                                            label: {
                                                show: false
                                            }
                                        }
                                    },
                                    children: arrData
                                }
                            ]
                        }
                    ]
                };
            if (option && typeof option === "object") {
                myChart.setOption(option, true);
            }
        }
    }*/


})