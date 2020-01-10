// var dutyServerUrl="http://192.168.8.102:23268";//localhost
var dutyServerUrl="http://10.2.68.70:23268";//centos
var getDutyCallbackFunc=null;

// window.onload=function () {
//     console.log("开始加载getDuty.js");
//     addDutyWindowCssJs();
// };
(function () {
    // console.log("开始加载getDuty.js");
    addDutyWindowCssJs();
})()

function getDutyInfo(id,callback) {
    if(typeof(id)=="undefined"){
        return;
    }
    if($("#dutyHtml").length>0){
        return;
    }
    getDutyCallbackFunc=callback;
    var html='<div id="dutyHtml">\n' +
        '    <div id="duTyWindowTitle">\n' +
        '        <div id="duTyWindowTitleName" class="dutyTitleName"><h2>战位信息</h2></div>\n' +
        '        <div class="dutyRightTopBtn" onclick="closeDutyWindow()">X</div>\n' +
        '    </div>\n' +
        '    <div id="dutyWindowBody">\n' +
        '        <div id="dutyLeftDiv">\n' +
        '            <div id="dutyUnitTree" class="dutyDivShadow treeScroll">\n' +
        '                <ul id="zTree" class="ztree"></ul>\n' +
        '            </div>\n' +
        '        </div>\n' +
        '        <div id="dutyRightDiv">\n' +
        '            <div id="dutyInputDiv" class="dutyDivShadow">\n' +
        '                <div class="dutyInputTitleDiv">姓名</div>\n' +
        '                <input id="dutyNameInput" type="text">\n' +
        '                <div id="dutyInfoSearchBtn" onclick=searchDutyDetail()>搜索</div>\n' +
        '            </div>\n' +
        '            <div id="mainDutyDiv" class="dutyDivShadow">\n' +
        '                <div id="dutyTitle" class="unitName">\n' +
        '                    <div id="dutyUnitName"></div>\n' +
        '                </div>\n' +
        '                <div id="mainDutyTypeDiv" class="scrollDiv">\n' +
        '                </div>\n' +
        '            </div>\n' +
        '            <div id="dutyDetailDiv" class="dutyDivShadow">\n' +
        '            </div>\n' +
        '        </div>\n' +
        '\n' +
        '    </div>\n' +
        '</div>';
    $('body').append(html);
    initDutyHtml(id);

}

function addDutyWindowCssJs(){
    var cssjs='<link rel="stylesheet" type="text/css" href="'+dutyServerUrl+'/duty/dutybase.css">\n' +
        '    <link rel="stylesheet" type="text/css" href="'+dutyServerUrl+'/css/duty.css">\n' +
        '    <link rel="stylesheet" type="text/css" href="'+dutyServerUrl+'/zTree/css/zTreeStyle/zTreeStyle.css">'+
        '<script type="text/javascript" src="'+dutyServerUrl+'/js/util.js"></script>\n' +
        '<script type="text/javascript" src="'+dutyServerUrl+'/zTree/js/jquery.ztree.core.js"></script>\n' +
        '<script type="text/javascript" src="'+dutyServerUrl+'/duty/duty.js"></script>';
    $('head').append(cssjs);
}
function sdada() {
    var list = [{"name":"zhanf"}];
    if(callbackFunc != null)
    callbackFunc(list);
}