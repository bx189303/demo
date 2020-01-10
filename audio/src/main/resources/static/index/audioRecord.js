// var serverUrl="http://192.168.8.102:23270"; //localhost
var serverUrl="http://10.11.194.50:23270"; //sip
// var serverUrl="http://10.2.68.70:23270"; //centos


// window.onload=function () {
//     console.log("开始加载audioRecord.js");
//     addAudioCssJs();
// };
(function () {
    // console.log("开始加载audioRecord.js");
    addAudioCssJs();
})()

function getAudioRecord(id){
    if(typeof(id)=="undefined"){
        console.log("请传入id");
        return;
    }
    if($("#audioHtml").length>0){
        return;
    }
    var html='<div id="audioHtml">\n' +
        '    <div id="audioWindowTitle">\n' +
        '        <div id="audioWindowTitleName" class="audioTitleName"><h2>录音查询</h2></div>\n' +
        '        <div class="audioRightTopBtn" onclick="closeAudioWindow()">X</div>\n' +
        '    </div>\n' +
        '    <div id="audioCheckDiv">\n' +
        '        <div id="audioTabs" class="tabs is-toggle">\n' +
        '            <div id="singleLi" class="audioTab audioTabChecked" onclick=showSingleHis(this)>单呼</div>\n' +
        '            <div id="groupLi" class="audioTab" onclick=showGroupHis(this)>会议</div>\n' +
        '        </div>\n' +
        '        <div id="audioDateDiv">\n' +
        '            <span>时间范围:</span>\n' +
        '            <input id="audioStartDateInput" type="date" class="btn">\n' +
        '            <span>至</span>\n' +
        '            <input id="audioEndDateInput" type="date" class="btn">\n' +
        '        </div>\n' +
        '        <div id="downloadAudioBtn" class="audioCheckBtn" onclick=downloadAudios()>下载</div>\n' +
        '        <div id="searchAudioBtn" class="audioCheckBtn" onclick=queryAudio()>查询</div>\n' +
        '    </div>\n' +
        '    <div id="audioTable">\n' +
        '        <table class="table is-hoverable is-bordered is-narrow">\n' +
        '            <thead>\n' +
        '                <tr>\n' +
        '                    <th class="audioCheckBoxTd"><input type="checkbox" class="audioTdCheckBox" onchange=audioTdSelectAll(this)></th>\n' +
        '                    <th class="audioTd1">序号</th>\n' +
        '                    <th class="audioTd2">通话人</th>\n' +
        '                    <th class="audioTdDate audioTdStartDate">开始时间</th>\n' +
        '                    <th class="audioTdDate audioTdEndDate">结束时间</th>\n' +
        '                    <th class="audioTdBtn">操作</th>\n' +
        '                </tr>\n' +
        '            </thead>\n' +
        '            <tbody class="scrollDiv">\n' +
        '            </tbody>\n' +
        '        </table>\n' +
        '    </div>\n' +
        '</div>';
    $('body').append(html);
    // initDate();
    // audioUserId=id;
    // loadSingleAudio(id,getNowFormatDate(),getNextFormatDate());
    initAudioHtml(id);
}


function addAudioCssJs(){
    var cssjs='    <link rel="stylesheet" type="text/css" href="'+serverUrl+'/css/audio-base.css">\n' +
        '    <link rel="stylesheet" type="text/css" href="'+serverUrl+'/css/audioList.css">'+
        '<script type="text/javascript" src="'+serverUrl+'/js/util.js"></script>\n' +
        // '<script type="text/javascript" src="'+serverUrl+'/laydate/laydate.js"></script>\n' +
        '<script type="text/javascript" src="'+serverUrl+'/index/audio.js"></script>';
    $('head').append(cssjs);
    // $('head').prepend(cssjs);
}