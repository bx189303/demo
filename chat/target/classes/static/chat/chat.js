var size=20;
var page;
var noMoreRecord=false;

//websocket
function openSocket() {
    if(typeof(WebSocket) == "undefined") {
        console.log("您的浏览器不支持WebSocket");
    }else{
        // var socketUrl="ws://localhost:8080/ws/"+userId;
        var socketUrl=serverUrl.replace("http","ws")+"/ws/"+userId;
        socket = new WebSocket(socketUrl);
        //打开事件
        socket.onopen = function() {
            console.log(userId+" : 首页websocket已打开");
        };
        //关闭事件
        socket.onclose = function() {
            console.log("websocket已关闭");
            layer.alert('连接已断开',{
                skin:"redText",
                title:false
            })
        };
        //发生了错误事件
        socket.onerror = function() {
            console.log("websocket发生了错误");
            layer.alert('连接已断开',{
                skin:"redText",
                title:false
            })
        }
        //获得消息事件
        socket.onmessage = function(msg) {
            // console.log("index页面收到msg ： "+msg.data);
            msg=JSON.parse(msg.data);
            msgType=msg.type;
            if(msgType=="msg"){
                var dataSrc=msg.data.src.sId;
                var dataType=msg.data.type;
                var talkTime=msg.receiveTime;
                var dataGroup="";
                if(dataType=="group"){
                    dataGroup=msg.data.group.id;
                    dataSrc=dataGroup;
                }
                //消息列表更新
                var talkName="";
                if(dataType=="single"){
                    talkName=msg.data.src.sUnitname+"_"+msg.data.src.sName;
                }else if(dataType=="group"){
                    talkName=msg.data.group.name;
                }
                var talkContent="";
                if(msg.data.content.type=="text"){
                    talkContent=msg.data.content.content;
                }else if(msg.data.content.type=="file"){
                    if(msg.data.content.content.type=="file"){
                        talkContent="[文件]";
                    }else if(msg.data.content.content.type=="img"){
                        talkContent="[图片]";
                    }
                }
                indexMsgListUpdate(dataSrc,talkName,dataType,talkContent,talkTime,"receive");
                //聊天窗口显示消息
                if((dst==dataSrc&&dataType=="single")||(dst==dataGroup&&dataType=="group")){//如果当前窗口打开则显示消息并发送已读
                    var html=showMsgInChatWindow(msg);
                    $("#chatWindow").append(html);
                    scrollChatWindow();//下滑滚动条
                    //发送已读
                    var param={
                        "type":"notify",
                        "terminal": "3nPc",
                        "sendTime":getMyDate(Date.now()),
                    };
                    var paramData={
                        "type": type,
                        "uuid":msg.data.uuid,
                        "src": src,
                        "dst": msg.data.src.sId
                    }
                    if(type=="group"){
                        paramData.groupId=dataGroup;
                    }
                    param.data=paramData;
                    $.ajax({
                        type : "POST",
                        contentType: "application/json;charset=UTF-8",
                        url : "/send",
                        data : JSON.stringify(param),
                        //请求成功
                        success : function(result) {

                        },
                        //请求失败，包含具体的错误信息
                        error : function(e){
                            console.log(e.status);
                            console.log(e.responseText);
                        }
                    })

                }
            }else if(msgType=="notify"){
                var notifyUuid=msg.data.uuid;
                var notifySrc=msg.data.src;
                var notifyDst=msg.data.dst;
                var notifyType=msg.data.type;
                var notifyGroup="";
                if(notifyType=="group"){
                    notifyGroup=msg.data.groupId;
                }
                //如果当前窗口打开则显示已读
                if(notifyType=="single"&&dst==notifySrc){
                    $('.chatMsg[name="'+notifyUuid+'"] .read').text("已读");
                }else if(notifyType=="group"&&dst==notifyGroup){
                    var readOldText=$('.chatMsg[name="'+notifyUuid+'"] .read').text();
                    var readOldCount=readOldText.substr(0,readOldText.indexOf("人"));
                    var readNewCount=parseInt(readOldCount)+1;
                    $('.chatMsg[name="'+notifyUuid+'"] .read').text(readNewCount+"人已读");
                }
            }else if(msgType=="groupNotify"){
                var groupNotifyType=msg.data.type;
                if(groupNotifyType=="updateGroup"){
                    var groupId=msg.data.groupId;
                    //如果在对话窗口，更新标题名
                    if(type=="group"&&dst==groupId){
                        loadGroups(groupId);
                    }else{
                        loadGroups();
                    }
                    //如果是群更新，更新首页列表的名字
                    setTimeout(function(){
                        var groupName=$('#groupUl li[name="'+groupId+'"] .liName').text();
                        $('#chatUl li[name="'+groupId+'"] .liName').text(groupName);
                    },500)
                }else if(groupNotifyType=="addUser"){
                    loadGroups();
                }
            }

        };

    }
}



//加载好友列表
function loadFriends(){
    $.ajax({
        type : "POST",
        contentType: "application/json;charset=UTF-8",
        url : "/getFriend/"+src,
        //请求成功
        success : function(result) {
            var friends=result.data;
            var html='';
            $(friends).each(function(i,n){
                if(n.sId==src){
                    return true;//遍历到自己则不显示
                }
                html+=showFriendList("load",n);
            })
            $("#friendUl").html(html);
        },
        //请求失败，包含具体的错误信息
        error : function(e){
            console.log(e.status);
            console.log(e.responseText);
        }
    })
}
//加载群组列表
function loadGroups(id){
    $.ajax({
        type : "POST",
        contentType: "application/json;charset=UTF-8",
        url : "/getGroup/"+userId,
        //请求成功
        success : function(result) {
            // console.log("loadGroups : "+result.data);
            var groups=result.data;
            var html='';
            $(groups).each(function(i,n){
                html+=showGroupList(n);
                // groups.push(n.id);
            })
            $("#groupUl").html(html);
            if(typeof(id)!="undefined"){
                $('#groupUl li[name="'+id+'"]').click();
            }
        },
        //请求失败，包含具体的错误信息
        error : function(e){
            console.log(e.status);
            console.log(e.responseText);
        }
    })
}

//跳转到对话窗口-加载对话记录
function jumpChat(id, idType, name){
    dst=id;
    type=idType;
    console.log("跳转窗口 ： "+src+"  "+dst+"  "+type);
    //清空对话窗口
    $("#chatWindow").html("");
    //加载对话记录
    loadChatMsg();
    //加载对话窗口信息
    $("#chatWindowCount").text("");
    // $("#titleName").text(name);//改对话窗口标题
    $("#chatWindowName").text(name);//改对话窗口标题
    //加载对话窗口详情
    loadChatDetail();
    //窗口切换
    $("#indexDiv").hide();//首页隐藏
    $("#chatDiv").show();//对话窗口显示
    chatDetailback();//关闭详情页
    //消除首页未读提醒
    $('#chatUl li[name="'+id+'"] .notice').text(0);
    $('#chatUl li[name="'+id+'"] .notice').addClass("hide");
}

function loadChatMsg(){
    if(dst==""||type==""){
        return;
    }
    var data={
        "src":src,
        "dst":dst,
        "type":type,
        "size":size,
        "page":page
    }
    $.ajax({
        type : "POST",
        contentType: "application/json;charset=UTF-8",
        // url : "/getRecord",
        url : "/getRecordByPage",
        data: JSON.stringify(data),
        //请求成功
        success : function(result) {
            var record=result.data;
            var html='';
            if(typeof(record)=="undefined"){
                noMoreRecord=true;
                if(page>1){
                    layer.msg("无更多记录",{
                        time:1000
                    });
                }
                return;
            }
            $(record).each(function(i,n){
                html+=showMsgInChatWindow(n);
            })
            var oldChatHtml=$("#chatWindow").html();
            var oldHeight=$("#chatWindow")[0].scrollHeight;
            $("#chatWindow").html(html+oldChatHtml);
            var newHeight=$("#chatWindow")[0].scrollHeight;
            // scrollChatWindow();
            if(page==1){
                $('#chatWindow').scrollTop(newHeight);
            }else{
                $('#chatWindow').scrollTop(newHeight-oldHeight);
            }
        },
        //请求失败，包含具体的错误信息
        error : function(e){
            console.log(e.status);
            console.log(e.responseText);
        }
    })
}

//加载首页列表
function loadIndex(){
    $.ajax({
        type : "POST",
        contentType: "application/json;charset=UTF-8",
        url : "/getIndex/"+userId,
        //请求成功
        success : function(result) {
            // console.log("loadIndex : "+result.data);
            var indexList=result.data;
            var html='';
            $(indexList).each(function(i,n){
                html+=showIndexList(n);
            })
            $("#chatUl").html(html);
        },
        //请求失败，包含具体的错误信息
        error : function(e){
            console.log(e.status);
            console.log(e.responseText);
        }
    })
}

//添加好友
function addFriend(twoUserId){
    $.ajax({
        type : "POST",
        contentType: "application/json;charset=UTF-8",
        url : "/addFriend/"+twoUserId,
        //请求成功
        success : function(result) {
            console.log("添加好友 : "+result.msg);
        },
        //请求失败，包含具体的错误信息
        error : function(e){
            console.log(e.status);
            console.log(e.responseText);
        }
    })
}

//下载文件
function download(obj){
    var fileUrl=$(obj).attr("name");
    var fileName=$(obj).attr("filename");
    console.log(fileUrl+","+fileName);
    window.location.href="/download?fileUrl="+fileUrl+"&fileName="+fileName;
}

function openFile(obj){
    var p=$(obj).parent();
    var fileName=$(".fileShowDiv",p).attr("name");
    var fileUrl=imgUrlPre+fileName;
    var a='<a href="'+fileUrl+'" target="view_frame"></a>';
    $(a)[0].click();
}

//对话窗口滚动条默认最下方
function scrollChatWindow(){
    $('#chatWindow').scrollTop( $('#chatWindow')[0].scrollHeight);
}

//重新加载人员信息
function reloadUserInfo(){
    $.ajax({
        type : "POST",
        contentType: "application/json;charset=UTF-8",
        url : "/loadUserInfo",
        //请求成功
        success : function(result) {
            console.log("重新加载人员信息！")
        },
        //请求失败，包含具体的错误信息
        error : function(e){
            console.log(e.status);
            console.log(e.responseText);
        }
    })
}