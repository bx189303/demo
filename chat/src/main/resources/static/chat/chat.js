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
        };
        //发生了错误事件
        socket.onerror = function() {
            console.log("websocket发生了错误");
        }
        //获得消息事件
        socket.onmessage = function(msg) {
            console.log("index页面 ： "+msg.data);
            msg=JSON.parse(msg.data);
            msgType=msg.type;
            if(msgType=="msg"){
                var dataSrc=msg.data.src.sId;
                var dataType=msg.data.type;
                var dataGroup="";
                if(dataType=="group"){
                    dataGroup=msg.data.group.id;
                    dataSrc=dataGroup;
                }
                //消息列表更新
                var talkName="";
                if(dataType=="single"){
                    talkName=msg.data.src.sUnitname+" "+msg.data.src.sName;
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
                indexMsgListUpdate(dataSrc,talkName,dataType,talkContent,"receive");
                //聊天窗口显示消息
                if((dst==dataSrc&&dataType=="single")||(dst==dataGroup&&dataType=="group")){//如果当前窗口打开则显示消息并发送已读
                    var content="";
                    var contentType=msg.data.content.type;
                    var html="";
                    if(contentType=="text"){
                        content=msg.data.content.content;
                    }else if(contentType=="file"){
                        var fileUrl=msg.data.content.content.content;
                        var fileName=msg.data.content.content.name;
                        var fileType=msg.data.content.content.type;
                        if(fileType=="img"){
                            var imgsrc=imgUrlPre+fileUrl;
                            content='<div name="'+fileUrl+'" filename="'+fileName+'" class="imgShowDiv" onclick="download(this)"><img src="'+imgsrc+'"></div>';
                        }else if(fileType=="file"){
                            content='<div name="'+fileUrl+'" filename="'+fileName+'" class="fileShowDiv" onclick="download(this)"><img src="/img/file.png"><span class="fileShowSpan">'+fileName+'</span></div>';
                        }
                    }
                    html='<div name="'+msg.data.uuid+'" class="leftTalk chatMsg">\n' +
                        '                    <div class="leftTalkImgDiv talkImgDiv">\n' +
                        '                        <img src="../img/head.png">\n' +
                        '                    </div>\n' +
                        '                    <div class="leftTalkContent">\n' +
                        '                        <div class="talkUser">'+msg.data.src.sName+'</div>\n' +
                        '                        <div class="talkBubble">'+content+'</div>\n' +
                        '                    </div>\n' +
                        '                </div>';
                    $("#chatWindow").append(html);
                    scrollChatWindow();//下滑滚动条
                    //发送已读
                    var param={
                        "type":"notify",
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
            }

        };

    }
}

//首页列表更新
function indexMsgListUpdate(chatSrcId,chatSrcName,chatSrcType,chatContent,updateType){
    //消息列表更新
    var indexNoExistence=true;//变量,默认首页不存在对话框
    //遍历后如果首页存在则更新对话框
    $("#msgListDiv li").each(function(i,n){
        var liId=$(this).attr("name");
        var liType=$(this).attr("type");
        if(liId==chatSrcId&&liType==chatSrcType){
            //列表显示最后一条消息
            $(this.getElementsByClassName("liContentRecord")[0]).text(chatContent);
            //如果不在聊天窗口，则notice显示并+1
            if(updateType=="receive"){
                if(chatSrcId!=dst||chatSrcType!=type){
                    var n=$(this.getElementsByClassName("notice")[0]).text();
                    $(this.getElementsByClassName("notice")[0]).text(parseInt(n)+1);
                    $(this.getElementsByClassName("notice")[0]).removeClass("hide");
                }
            }
            //放在第一个
            $(this).insertBefore($("#msgListDiv li")[0]);
            //变量改为首页存在
            indexNoExistence=false;
            //更新后跳出循环
            return true;
        }
    })
    //遍历后如果首页不存在则新建对话框
    if(indexNoExistence){
        var headImgName="";
        if(chatSrcType=="single"){
            headImgName="head";
        }else if(chatSrcType=="group"){
            headImgName="grouphead";
        }
        var noticeHtml='';
        if(updateType=="receive"){
            noticeHtml='<div class="notice">1</div>';
        }else if(updateType="send"){
            noticeHtml='<div class="notice hide">0</div>';
        }
        var liHtml='<li name="'+chatSrcId+'" type="'+chatSrcType+'" onclick="jumpChat(this)">\n' +
            '                        <div class="liContentDiv scrollDiv">\n' +
            '                            <div class="liContentImgDiv">\n' +
            '                                <img src="../img/'+headImgName+'.png">\n' +noticeHtml+
            '                            </div>\n' +
            '                            <div class="liContentRightDiv">\n' +
            '                                <div class="liContentUser liName">'+chatSrcName+'</div>\n' +
            '                                <div class="liContentRecord">'+chatContent+'</div>\n' +
            '                            </div>\n' +
            '                        </div>\n' +
            '                        <div class="liSpace"/>\n' +
            '                    </li>';
        $(liHtml).insertBefore($("#msgListDiv li")[0]);
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
                html+='<li name="'+n.sId+'" class="" type="single" onclick="jumpChat(this)">\n' +
                    '                        <div class="liContentDiv scrollDiv">\n' +
                    '                            <div class="liContentImgDiv">\n' +
                    '                                <img src="../img/head.png">\n' +
                    '                            </div>\n' +
                    '                            <div class="liContentRightDiv">\n' +
                    '                                <div class="liFriendUser liName">'+n.sUnitname+' '+n.sName+'</div>\n' +
                    '                            </div>\n' +
                    '                        </div>\n' +
                    '                        <div class="liSpace"/>\n' +
                    '                    </li>';
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
function loadGroups(){
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
                html+='<li name="'+n.id+'" type="group" onclick="jumpChat(this)">\n' +
                    '                        <div class="liContentDiv scrollDiv">\n' +
                    '                            <div class="liContentImgDiv">\n' +
                    '                                <img src="../img/grouphead.png">\n' +
                    '                            </div>\n' +
                    '                            <div class="liContentRightDiv">\n' +
                    '                                <div class="liFriendUser liName">'+n.name+'</div>\n' +
                    '                            </div>\n' +
                    '                        </div>\n' +
                    '                        <div class="liSpace"/>\n' +
                    '                    </li>';
                groups.push(n.id);
            })
            $("#groupUl").html(html);
        },
        //请求失败，包含具体的错误信息
        error : function(e){
            console.log(e.status);
            console.log(e.responseText);
        }
    })
}

//跳转方法-加载对话记录
function jumpChat(obj){
    //获取参数
    var id=obj.getAttribute("name");
    var toType=obj.getAttribute("type");
    src=userId;
    dst=id;
    type=toType;
    console.log("跳转窗口 ： "+src+"  "+dst+"  "+type);
    //如果是从搜索好友列表跳转来，则添加好友
    var objClass=$(obj).attr("class");
    if(typeof(objClass)!="undefined"&&objClass.indexOf("searchFriendLi")!=-1){
        var twoUserId=src+","+dst;
        // addFriend(twoUserId);
    }
    //加载对话记录
    loadChatMsg();
    //加载对话窗口信息
    var name=$(obj.getElementsByClassName("liName")[0]).text();//获取对话人姓名
    $("#titleName").text(name);//改对话窗口标题
    loadChatDetail();

    //窗口切换
    $("#indexDiv").hide();//首页隐藏
    $("#chatDiv").show();//对话窗口显示
    //消除首页未读提醒
    $(obj.getElementsByClassName("notice")).text(0);
    $(obj.getElementsByClassName("notice")).addClass("hide");

}
function loadChatMsg(){
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
                return;
            }
            $(record).each(function(i,n){
                var recordSendTime=n.sendTime;
                var recordType=n.data.type;
                var recordSrc=n.data.src.sId;
                var readId=n.data.readId;
                var readText="";
                var recordDst="";
                if(recordType=="single"){
                    recordDst=n.data.dst.sId;
                    if(readId.length==0){
                        readText="未读";
                    }else if(readId.length>0){
                        readText="已读";
                    }
                }else if(recordType=="group"){
                    recordDst=n.data.dst.id;
                    if(readId.length==0){
                        readText="0人已读";
                    }else if(readId.length>0){
                        var readCount=readId.split(",").length;
                        readText=readCount+"人已读";
                    }
                }
                var content="";
                if(n.data.content.type=="text"){
                    content=n.data.content.content;
                }else if(n.data.content.type=="file"){
                    var fileUrl=n.data.content.content.content;
                    var fileName=n.data.content.content.name;
                    var fileType=n.data.content.content.type;
                    if(fileType=="img"){
                        var imgsrc=imgUrlPre+fileUrl;
                        content='<div name="'+fileUrl+'" filename="'+fileName+'" class="imgShowDiv" onclick="download(this)"><img src="'+imgsrc+'"></div>';
                    }else if(fileType=="file"){
                        content='<div name="'+fileUrl+'" filename="'+fileName+'" class="fileShowDiv" onclick="download(this)"><img src="/img/file.png"><span class="fileShowSpan">'+fileName+'</span></div>';
                    }
                }
                // console.log("加载record ： "+recordSrc+" "+recordDst+" "+recordType);
                if(recordSrc==src){//如果记录来源为自己，则显示在右边
                    html+='<div name="'+n.data.uuid+'" class="RightTalk chatMsg">\n' +
                        '                    <div class="rightTalkImgDiv talkImgDiv">\n' +
                        '                        <img src="../img/head.png">\n' +
                        '                    </div>\n' +
                        '                    <div class="rightTalkContent">\n' +
                        '                        <div class="talkUser">'+recordSendTime+'&nbsp;&nbsp;&nbsp;我</div>\n' +
                        '                        <div class="rightTalkNotify">\n' +
                        '                            <div class="rightTalkBubble">'+content+'</div>\n' +
                        '                            <div class="read">'+readText+'</div>\n' +
                        '                        </div>\n' +
                        '                    </div>\n' +
                        '                </div>';
                }else {//除了自己以外，则显示在左侧
                    html+='<div name="'+n.data.uuid+'" class="leftTalk chatMsg">\n' +
                        '                    <div class="leftTalkImgDiv talkImgDiv">\n' +
                        '                        <img src="../img/head.png">\n' +
                        '                    </div>\n' +
                        '                    <div class="leftTalkContent">\n' +
                        '                        <div class="talkUser">'+n.data.src.sName+'&nbsp;&nbsp;&nbsp;'+recordSendTime+'</div>\n' +
                        '                        <div class="talkBubble">'+content+'</div>\n' +
                        '                    </div>\n' +
                        '                </div>';
                }
            })
            var oldChatHtml=$("#chatWindow").html();
            var oldHeight=$("#chatWindow")[0].scrollHeight;
            $("#chatWindow").html(html+oldChatHtml);
            var newHeight=$("#chatWindow")[0].scrollHeight;
            // scrollChatWindow();
            $('#chatWindow').scrollTop(newHeight-oldHeight);
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
                var indexListType=n.type;
                var notice=n.notice;
                var id="";
                var headImgName="";
                var talkName="";
                var talkContent="";
                var talkPersonUnit="";
                if(indexListType=="single"){
                    id=n.dst.sId;
                    headImgName="head";
                    talkName=n.dst.sName;
                    talkPersonUnit=n.dst.sUnitname+" ";
                }else if(indexListType=="group"){
                    id=n.dst.id;
                    talkName=n.dst.name;
                    headImgName="grouphead";
                }
                if(n.content.type=="text"){
                    talkContent=n.content.content;
                }else if(n.content.type=="file"){
                    if(n.content.content.type=="img"){
                        talkContent="[图片]";
                    }else if(n.content.content.type=="file"){
                        talkContent="[文件]";
                    }
                }
                var noticeHide="";
                if(notice=="0"){
                    noticeHide="hide";
                }
                html+='<li name="'+id+'" type="'+indexListType+'" onclick="jumpChat(this)">\n' +
                    '                        <div class="liContentDiv scrollDiv">\n' +
                    '                            <div class="liContentImgDiv">\n' +
                    '                                <img src="../img/'+headImgName+'.png">\n' +
                    '                                <div class="notice '+noticeHide+'">'+notice+'</div>\n' +
                    '                            </div>\n' +
                    '                            <div class="liContentRightDiv">\n' +
                    '                                <div class="liContentUser liName">'+talkPersonUnit+talkName+'</div>\n' +
                    '                                <div class="liContentRecord">'+talkContent+'</div>\n' +
                    '                            </div>\n' +
                    '                        </div>\n' +
                    '                        <div class="liSpace"/>\n' +
                    '                    </li>';
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
    window.location.href="/download/"+fileUrl+"/"+fileName;
}

//对话窗口滚动条默认最下方
function scrollChatWindow(){
    $('#chatWindow').scrollTop( $('#chatWindow')[0].scrollHeight);
}