window.onload=function () {
    console.log("开始加载");
    addCssJs();

};

function getDuty(id) {
    if($("#dutyHtml").length>0){
        return;
    }
    var html='<div id="dutyHtml">\n' +
        '    <div id="duTyWindowTitle">\n' +
        '        <div class="titleName"><h2>战位信息</h2></div>\n' +
        '        <div class="rightTopBtn" onclick="closeDuty()">X</div>\n' +
        '    </div>\n' +
        '    <div id="dutyWindowBody">\n' +
        '        <div id="leftDiv">\n' +
        '            <div id="unitTree" class="divShadow treeScroll">\n' +
        '                <ul id="zTree" class="ztree"></ul>\n' +
        '            </div>\n' +
        '        </div>\n' +
        '        <div id="rightDiv">\n' +
        '            <div id="mainDutyDiv" class="divShadow">\n' +
        '                <div id="dutyTitle" class="unitName">\n' +
        '                    <div id="unitName"></div>\n' +
        '                </div>\n' +
        '                <div id="mainDutyTypeDiv" class="scrollDiv">\n' +
        '                </div>\n' +
        '            </div>\n' +
        '            <div id="dutyDetailDiv" class="divShadow">\n' +
        '            </div>\n' +
        '        </div>\n' +
        '\n' +
        '    </div>\n' +
        '</div>';
    $('body').append(html);
    loadUnit(id);
}

function addCssJs(){
    var cssjs='    <link rel="stylesheet" type="text/css" href="../css/base.css">\n' +
        '    <link rel="stylesheet" type="text/css" href="../css/duty.css">\n' +
        '    <link rel="stylesheet" type="text/css" href="../zTree/css/zTreeStyle/zTreeStyle.css">'+
        '<script type="text/javascript" src="../js/util.js"></script>\n' +
        // '<script type="text/javascript" src="../layer/layer.js"></script>'+
        '<script type="text/javascript" src="../zTree/js/jquery.ztree.core.js"></script>\n' +
        '<script type="text/javascript" src="./duty.js"></script>';
    $('head').append(cssjs);
}
