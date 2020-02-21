var tagman = {
    addThree: function (tagThree) {
        var addlabel = $('.drop_downRight .add_tab_n2a');
        $('.drop_downRight').children('.year').remove();
        // 加载四级标签
        for (var i = 0; i < tagThree.length; i++) {
            var moban = $('#threeLabel').clone();
            moban.children().attr("style", "display:inline-block");
            var current = tagThree[i];
            moban.find('.shu_name').text(current.name)
                .attr('value', current.id).attr("title", current.name);
            moban.find('.shangxian').text(tagman.tagState(current.state));
            moban.find('.gender3_2').children().last().text(
                current.ruleDesc != null ? (current.ruleDesc).substring(0,
                    5) + '....' : '暂无标签规则');
            addlabel.before(moban.html());
        }
    },

    addFour: function (tagFours, tagType, threeId) {
        var three = $('.clearfix .year0').find('[value="' + threeId + '"]')
            .parent().siblings(".clearfix");
        for (var i = 0; i < tagFours.length; i++) {
            var target = tagFours[i];
            var moban = $('#fourLabel').clone();
            moban.children().attr("style", "display:block");
            moban.find(".shu_name").text(target.name).attr('value', target.id).attr("title", target.name);
            moban.find('.gender4_on2_2').children().last().text(
                target.ruleDesc != null ? (target.ruleDesc).substring(0, 5)
                    + '....' : '暂无标签规则');
            if (tagType == 1) {
                moban.find(".shu_name_img").attr('style', "visibility:hidden;");
            }
            three.after(moban.html());
        }
    },
    tagState: function (state) {
        if (state == 1) {
            return '已上线';
        } else if (state == 2) {
            return '申请中';
        } else if (state == 3) {
            return '已暂停';
        } else if (state == 4) {
            return '已失败';
        } else {
            return '未定义';
        }
    }
}