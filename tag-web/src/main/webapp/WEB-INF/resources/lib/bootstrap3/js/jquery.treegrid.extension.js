(function ($) {
    "use strict";

    $.fn.treegridData = function (options, param) {
        //如果是调用方法
        if (typeof options == 'string') {
            return $.fn.treegridData.methods[options](this, param);
        }

        //如果是初始化组件
        options = $.extend({}, $.fn.treegridData.defaults, options || {});
        var target = $(this);
        //得到根节点
        target.getRootNodes = function (data) {
            var result = [];
            $.each(data, function (index, item) {
                if (item[options.parentColumn] == -1) {
                    result.push(item);
                }
            });
            return result;
        };
        var j = 0;
        //二级子节点
        target.getsecondChildNodes = function (data, parentNode, parentIndex, tbody) {
            $.each(data, function (i, item) {
                if (item[options.parentColumn] == parentNode[options.id]) {
                    var tr = $('<tr></tr>');
                    tr.addClass('treegrid-' + (++j));
                    tr.addClass('treegrid-parent-' + parentIndex);
                    tr.addClass('treegrid-leveltwo');
                    $.each(options.columns, function (index, column) {
                        var td = $('<td></td>');
                        if (index == 0) {
                            if (column.formatter != null && column.formatter != undefined) {
                                td.html(column.formatter(item[column.field]));
                            } else {
                                td.html(item[column.field]);
                            }
                            tr.append(td);
                            tr.append("<td><span class='labelEdit' title='编辑' style='display:none;'  data-id='" + item.id + "' data-toggle='modal' data-target='#maineditModal'><img src='../../../mengyao/res/imgs/user_edit.png'></span><span class='commondel' title='删除' id='" + item.id + "' level='" + item.level + "' style='display:none;'><img src='../../../mengyao/res/imgs/55.png'></span></td>")
                        }
                    });
                    tbody.append(tr);
                    target.getChildNodes(data, item, j, tbody)
                }

            });
        }
        //递归获取子节点并且设置子节点
        target.getChildNodes = function (data, parentNode, parentIndex, tbody) {
            $.each(data, function (i, item) {
                if (item[options.parentColumn] == parentNode[options.id]) {
                    var tr = $('<tr></tr>');
                    tr.addClass('treegrid-' + (++j));
                    tr.addClass('treegrid-parent-' + parentIndex);
                    tr.addClass('treegrid-levelthree');
                    $.each(options.columns, function (index, column) {
                        var td = $('<td></td>');
                        if (index == 0) {
                            if (column.formatter != null && column.formatter != undefined) {
                                td.html(column.formatter(item[column.field]));
                            } else {
                                td.html(item[column.field]);
                            }
                            tr.append(td);
                            tr.append("<td><span class='labelEdit' title='编辑' style='display:none;'  data-id='" + item.id + "' data-toggle='modal' data-target='#maineditModal'><img src='../../../mengyao/res/imgs/user_edit.png'></span><span class='commondel' title='删除' id='" + item.id + "' level='" + item.level + "' style='display:none;'><img src='../../../mengyao/res/imgs/55.png' id='" + item.id + "'></span></td>")
                        }
                    });
                    tbody.append(tr);
                    target.getChildNodes(data, item, j, tbody)
                }

            });

        };
        target.addClass('table');
        if (options.striped) {
            target.addClass('table-striped');
        }
        if (options.bordered) {
            target.addClass('table-bordered');
        }
        if (options.url) {
            $.ajax({
                type: options.type,
                url: options.url,
                data: options.ajaxParams,
                dataType: "JSON",
                success: function (data, textStatus, jqXHR) {
                    //构造表头
                    var thr = $('<tr></tr>');
                    $.each(options.columns, function (i, item) {
                        var th = $('<th style="padding:10px;"></th>');
                        th.text(item.title);
                        thr.append(th);
                    });
                    var thead = $('<thead></thead>');
                    thead.append(thr);
                    target.append(thead);

                    //构造表体
                    var tbody = $('<tbody></tbody>');

                    var rootNode = target.getRootNodes(data.data);
                    $.each(rootNode, function (i, item) {
                        var tr = $('<tr></tr>');
                        tr.addClass('treegrid-' + (++j));
                        tr.addClass('treegrid-levelone');
                        $.each(options.columns, function (index, column) {
                            var td = $('<td></td>');
                            if (index == 0) {
                                if (column.formatter != null && column.formatter != undefined) {
                                    td.html(column.formatter(item[column.field]));
                                } else {
                                    td.html(item[column.field]);
                                }
                                tr.append(td);
                                tr.append("<td><span class='labelEdit' title='编辑' style='display:none;'  data-id='" + item.id + "' data-toggle='modal' data-target='#maineditModal'><img src='../../../mengyao/res/imgs/user_edit.png'></span><span class='commondel' title='删除' id='" + item.id + "' level='" + item.level + "' style='display:none;'><img src='../../../mengyao/res/imgs/55.png'></span></td>")
                            }

                        });

                        tbody.append(tr);
                        target.getsecondChildNodes(data.data, item, j, tbody);
                    });
                    target.append(tbody);
                    target.treegrid({
                        expanderExpandedClass: options.expanderExpandedClass,
                        expanderCollapsedClass: options.expanderCollapsedClass
                    });
                    if (!options.expandAll) {
                        target.treegrid('collapseAll');
                    }
                    for (var i = 0; i < oneArr.length; i++) {
                        if (oneArr[i] == "edit") {
                            target.find(".treegrid-levelone .labelEdit").show();
                        } else if (oneArr[i] == "del") {
                            target.find(".treegrid-levelone .commondel").show();
                        }
                    }
                    for (var i = 0; i < twoArr.length; i++) {
                        if (twoArr[i] == "edit") {
                            target.find(".treegrid-leveltwo .labelEdit").show();
                        } else if (twoArr[i] == "del") {
                            target.find(".treegrid-leveltwo .commondel").show();
                        }
                    }
                    for (var i = 0; i < threeArr.length; i++) {
                        if (threeArr[i] == "edit") {
                            target.find(".treegrid-levelthree .labelEdit").show();
                        } else if (threeArr[i] == "del") {
                            target.find(".treegrid-levelthree .commondel").show();
                        }
                    }

                }
            });
        } else {
            //也可以通过defaults里面的data属性通过传递一个数据集合进来对组件进行初始化....有兴趣可以自己实现，思路和上述类似
        }
        return target;
    };

    $.fn.treegridData.methods = {
        getAllNodes: function (target, data) {
            return target.treegrid('getAllNodes');
        },
        //组件的其他方法也可以进行类似封装........
    };

    $.fn.treegridData.defaults = {
        id: 'Id',
        parentColumn: 'ParentId',
        data: [],    //构造table的数据集合
        type: "GET", //请求数据的ajax类型
        url: null,   //请求数据的ajax的url
        ajaxParams: {}, //请求数据的ajax的data属性
        expandColumn: null,//在哪一列上面显示展开按钮
        expandAll: true,  //是否全部展开
        striped: false,   //是否各行渐变色
        bordered: false,  //是否显示边框
        columns: [],
        expanderExpandedClass: 'glyphicon glyphicon-chevron-down',//展开的按钮的图标
        expanderCollapsedClass: 'glyphicon glyphicon-chevron-right'//缩起的按钮的图标

    };
})(jQuery);