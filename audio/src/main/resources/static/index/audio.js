var audioUserId="";
// var audioUrl="http://10.11.194.50:23333/";
var audioUrl="http://10.11.194.50/download/record/";
// var serverUrl="http://192.168.8.102:23270"; //localhost
var serverUrl="http://10.11.194.50:23270"; //sip
// var serverUrl="http://10.2.68.70:23270"; //centos

function initAudioHtml(id){
    initDate();
    audioUserId=id;
    loadSingleAudio(id,getNowFormatDate(),getNextFormatDate());
    windowHtmlMove('audioHtml','audioWindowTitleName');
}

function loadGroupAudio(userId,startDate,endDate){
    var data={
        "userId":userId,
        "startDate":startDate,
        "endDate":endDate
    }
    $.ajax({
        type : "POST",
        contentType: "application/json;charset=UTF-8",
        url : serverUrl+"/getGroupAudio",
        data: JSON.stringify(data),
        //请求成功
        success : function(result) {
            // console.log("loadGroupAudio : "+result.data);
            var res=result.data;
            var html='';
            var qn=0;
            var qid="";
            $(res).each(function(i,n){
                if(qid!=n.queueId){
                    qid=n.queueId;
                    qn++;
                }
                html+='<tr><td class="audioCheckBoxTd"><input type="checkbox" class="audioTdCheckBox" onclick=checkAllAduioBox()></td>\n' +
                    '                    <td class="audioTd1">'+(i+1)+'</td>\n' +
                    '                    <td class="audioTd2" name="'+n.queueId+'">会议'+qn+'</td>\n' +
                    '                    <td class="audioTdDate audioTdStartDate">'+n.recordStartTime+'</td>\n' +
                    '                    <td class="audioTdDate audioTdEndDate">'+n.recordEndTime+'</td>\n' +
                    '                    <td class="audioTdBtn" fileUrl="'+n.recordFile+'" onclick=addAudio(this)>播放</td>\n' +
                    '                </tr>';
            })
            $("#audioTable tbody").html(html);
        },
        //请求失败，包含具体的错误信息
        error : function(e){
            console.log(e.status);
            console.log(e.responseText);
        }
    })
}

function loadSingleAudio(userId,startDate,endDate){
    var data={
        "userId":userId,
        "startDate":startDate,
        "endDate":endDate
    }
    $.ajax({
        type : "POST",
        contentType: "application/json;charset=UTF-8",
        url : serverUrl+"/getSingleAudio",
        data: JSON.stringify(data),
        //请求成功
        success : function(result) {
            // console.log("loadSingleAudio : "+result.data);
            var res=result.data;
            var html='';
            var callName='';
            $(res).each(function(i,n){
                callName=userId==n.calledNbr?n.callingNbr:n.calledNbr;
                if(callName.length>7){
                    callName=callName.substr(3);
                }
                html+='                <tr><td class="audioCheckBoxTd"><input type="checkbox" class="audioTdCheckBox" onclick=checkAllAduioBox()></td>\n' +
                    '                    <td class="audioTd1">'+(i+1)+'</td>\n' +
                    '                    <td class="audioTd2">'+callName+'</td>\n' +
                    '                    <td class="audioTdDate audioTdStartDate">'+n.talkStartTime+'</td>\n' +
                    '                    <td class="audioTdDate audioTdEndDate">'+n.talkEndTime+'</td>\n' +
                    '                    <td class="audioTdBtn" fileUrl="'+n.fileName+'" onclick=addAudio(this)>播放</td>\n' +
                    '                </tr>';
            })
            $("#audioTable tbody").html(html);
        },
        //请求失败，包含具体的错误信息
        error : function(e){
            console.log(e.status);
            console.log(e.responseText);
        }
    })
}


//查询录音记录
function queryAudio(){
    var start=$("#audioStartDateInput").val();
    var end=$("#audioEndDateInput").val();
    var checkedTab=$("#audioTabs .audioTabChecked").attr("id");
    if("singleLi"==checkedTab){
        loadSingleAudio(audioUserId,start,end);
    }else if("groupLi"==checkedTab){
        loadGroupAudio(audioUserId,start,end);
    }
}
//时间输入框
function initDate(){
    // laydate.render({
    //     elem: '#audioStartDateInput',
    //     type: 'datetime',
    //     value: getNowFormatDate()+' 00:00:00',
    //     btns: ['confirm']
    // });
    // laydate.render({
    //     elem: '#audioEndDateInput',
    //     type: 'datetime',
    //     value: getNextFormatDate()+' 00:00:00',
    //     btns: ['confirm']
    // });
    $("#audioStartDateInput").val(getNowFormatDate());
    $("#audioEndDateInput").val(getNextFormatDate());
}

//单呼按钮
function showSingleHis(obj){
    $("#singleLi").addClass("audioTabChecked");
    $("#groupLi").removeClass("audioTabChecked");
    $("thead .audioTd2").html("通话人");
    //加载单呼记录
    queryAudio();
}
//会议按钮
function showGroupHis(obj){
    $("#groupLi").addClass("audioTabChecked");
    $("#singleLi").removeClass("audioTabChecked");
    $("thead .audioTd2").html("会议");
    //加载会议记录
    queryAudio();
}

//关闭录音
function closeAudio(obj){
    var audioTr=$(obj).parent();
    var audioDownTr=$(obj).parent().next();
    audioTr.remove();
    audioDownTr.remove();
}

//关闭窗口
function closeAudioWindow(){
    $("#audioHtml").remove();
}

//单个播放的方法
function addAudio(obj){
    $("table .audioTr").remove();
    var audioSrc=audioUrl+$(obj).attr("fileurl");
    // audioSrc="../wav/20191212114251.wav";
    var audioTr='<tr class="audioTr">\n' +
        '                    <td colspan="5" rowspan="2">\n' +
        '                        <audio id="recordAudio" controlsList="nodownload" autoplay="autoplay" controls="controls" loop="loop" src="'+audioSrc+'"></audio>\n' +
        '                    </td><td rowspan="1" onclick=closeAudio(this)>关闭</td><tr class="audioTr"><td rowspan="1" onclick=downloadAudio(this)>下载</td></tr>\n' +
        '                </tr>';
    $(obj).parent().after(audioTr);
}

//录音下载方法
function downloadAudio(obj){
    var dataTr=$(obj).parent().prev().prev();
    var called=dataTr.find(".audioTd2").html();
    var callDate=dataTr.find(".audioTdStartDate").html();
    callDate=callDate.replace(" ","").replace("-","").replace(":","");
    var fileAddr=$(obj).parent().prev().prev().find("td:last").attr("fileurl");
    var fileSrc=audioUrl+fileAddr;
    var fileName=audioUserId+"_"+called+"_"+callDate+".wav";
    // fileSrc="../wav/20191212114251.wav";
    // var a='<a href="'+fileSrc+'" download="'+audioUserId+'_'+called+'_'+callDate+'">';
    // $(a)[0].click();
    // fileSrc="http://192.168.8.102:23333/test.wav";
    // window.location.href=serverUrl+"/audioDownload?fileUrlAndName="+fileSrc+","+fileName;
    window.location.href=serverUrl+"/audioFileDownload?fileUrlAndName="+fileSrc+","+fileName;
}

//表头的复选框方法
function audioTdSelectAll(obj){
    $("tbody .audioTdCheckBox").prop("checked",obj.checked);
}
//表格中复选框
function checkAllAduioBox(){
    var checkboxNum=$("tbody .audioTdCheckBox").length;
    var checkedboxNum=$("tbody .audioTdCheckBox:checked").length;
    // console.log(checkboxNum+","+checkedboxNum);
    if(checkedboxNum==checkboxNum){
        $("thead .audioTdCheckBox").prop("checked",true);
    }else{
        $("thead .audioTdCheckBox").prop("checked",false);
    }
}

//批量下载文件
function downloadAudios(){
    var fileUrlAndName="";
    $("tbody .audioTdCheckBox:checked").each(function(i,n){
        var tr=$(n).parent().parent();
        var fileAddr=tr.find(".audioTdBtn").attr("fileurl");
        var called=tr.find(".audioTd2").html();
        var callDate=tr.find(".audioTdStartDate").html();
        callDate=callDate.replaceAll(" ","").replaceAll("-","").replaceAll(":","");
        var fileSrc=audioUrl+fileAddr;
        // fileSrc="http://192.168.8.102:23333/test.wav";
        var fileName=audioUserId+"_"+called+"_"+callDate+".wav";
        var fileUrlName=fileSrc+","+fileName;
        // console.log(fileUrlName);
        fileUrlAndName+=fileUrlName+";";
    })
    if(fileUrlAndName.length==0){
        return;
    }
    fileUrlAndName=fileUrlAndName.substr(0,fileUrlAndName.length-1);
    // console.log(fileUrlAndName);
    // window.location.href=serverUrl+"/zipFiles?fileUrlAndName="+fileUrlAndName;
    window.location.href=serverUrl+"/audioFileDownload?fileUrlAndName="+fileUrlAndName;
}