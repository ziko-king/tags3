var objectObj = document.getElementById("User_centerIN");
objectObj.style.marginBottom = "20px";

//格式化日期
function FormatDate(strTime) {
    var date = new Date(strTime);
    var y = date.getFullYear();
    var m = date.getMonth() + 1;
    m = m < 10 ? ('0' + m) : m;
    var d = date.getDate();
    d = d < 10 ? ('0' + d) : d;
    var h = date.getHours();
    h = h < 10 ? ('0' + h) : h;
    var minute = date.getMinutes();
    minute = minute < 10 ? ('0' + minute) : minute;
    var second = date.getSeconds();
    second = second < 10 ? ('0' + second) : second;
    return y + '-' + m + '-' + d + ' ' + h + ':' + minute + ':' + second;
}

var usergroup = {
    addTableRecord: function (records) {
        var tablebody = $('.cover_style .table').find('tbody');
        tablebody.empty();
        // 加载table数据
        for (var i = 0; i < records.length; i++) {
            var current = records[i];
            var record = $('<tr></tr>').attr('value', current.id);
            var name = $('<td></td>').text(current.name);
            var personNum = $('<td></td>').text(formatNumber(current.userNum, 0, 1)); // 含盖用户数
            var tagNum = $('<td></td>').text(formatNumber(current.tagNum, 0, 1)); // 包含标签数
            var status = $('<td></td>').text(usergroup.userGroupState(current.state));
            var ctime = $('<td></td>').text(current.ctime == null || current.ctime == undefined ? '1970-01-01 00:00:00' : FormatDate(current.ctime));   //时间格式修改
            var operation = $('<td class="td-manage" style="text-align: center;">'
                //TODO 画像列表部分入口
                /*+'<span class="openStop" style="display:none;"><img src="../res/imgs/user_open.png" alt="" style="width: 20px;height: 11px;margin-right: 5px;margin-left: 10px;"></span>'*/
                + '<span class="combinEdit" data-id="' + current.id + '"  style="display:none;color:#409eff;" title="编辑" >编辑</span>'
                + '<span class="btn btn-xs del delete_ug" data-id="' + current.id + '" basicTagId="' + current.basicTagId + '" style="display:none;color:#f56c6c;" title="删除">删除</span>'
                + '</td>');
            record.append(name, personNum, tagNum, status, ctime, operation);
            tablebody.append(record);
        }
        adddeledit();
    },

    userGroupState: function (state) {
        // 状态：1申请中、2审核通过、3运行中、4未运行、5已禁用
        if (state == 1) {
            return '申请中';
        } else if (state == 2) {
            return '开发中';
        } else if (state == 3) {
            return '开发完成';
        } else if (state == 4) {
            return '已上线';
        } else if (state == 5) {
            return '已下线';
        } else if (state == 6) {
            return '已禁用';
        } else {
            return '未定义';
        }
    }
}