//对话窗口消息的html
function showMsgInChatWindow(n){
    var html="";
    var recordSendTime=n.receiveTime;
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
            var imgsrc=imgUrlPre+fileUrl;
            content='<div name="'+fileUrl+'" filename="'+fileName+'" class="fileShowDiv" onclick="download(this)"><img src="/img/file.png"><span class="fileShowSpan">'+fileName+'</span></div>';
        }
    }
    // console.log("加载record ： "+recordSrc+" "+recordDst+" "+recordType);
    if(recordSrc==src){//如果记录来源为自己，则显示在右边
        html='<div name="'+n.data.uuid+'" class="RightTalk chatMsg">\n' +
            '                    <div class="rightTalkImgDiv talkImgDiv">\n' +
            '                        <img src="../img/head.png">\n' +
            '                    </div>\n' +
            '                    <div class="rightTalkContent">\n' +
            '                        <div class="talkUser">'+recordSendTime+'&nbsp;&nbsp;&nbsp;我</div>\n' +
            '                        <div class="rightTalkNotify">\n' +
            '                            <div class="rightTalkBubble">'+content+'</div>\n' +
            '                            <div class="aFile" onclick="openFile(this)">打开</div><div class="read">'+readText+'</div>\n' +
            '                        </div>\n' +
            '                    </div>\n' +
            '                </div>';
    }else {//除了自己以外，则显示在左侧
        html='<div name="'+n.data.uuid+'" class="leftTalk chatMsg">\n' +
            '                    <div class="leftTalkImgDiv talkImgDiv" onclick=showUserInfo("'+recordSrc+'","single")>\n' +
            '                        <img src="../img/head.png">\n' +
            '                    </div>\n' +
            '                    <div class="leftTalkContent">\n' +
            '                        <div class="talkUser">'+n.data.src.sName+'&nbsp;&nbsp;&nbsp;'+recordSendTime+'</div>\n' +
            '                        <div class="talkBubble">'+content+'</div>\n' +
            '                    </div>\n' +
            '                </div>';
    }
    return html;
}

//好友列表的html
function showFriendList(src, n){
    var html="";
    var liClass="";
    if(src=="search"){
        liClass="searchFriendLi";
    }
    html='<li name="'+n.sId+'" class="'+liClass+'" type="single" onclick=showUserInfo("'+n.sId+'","single")>\n' +
        '                        <div class="liContentDiv scrollDiv">\n' +
        '                            <div class="liContentImgDiv">\n' +
        '                                <img src="../img/head.png">\n' +
        '                            </div>\n' +
        '                            <div class="liContentRightDiv">\n' +
        '                                <div class="liFriendUser liName">'+n.sUnitname+'_'+n.sName+'</div>\n' +
        '                            </div>\n' +
        '                        </div>\n' +
        '                        <div class="liSpace"/>\n' +
        '                    </li>';
    return html;
}

//群组列表html
function showGroupList(n){
    var html="";
    html='<li name="'+n.id+'" type="group" onclick=jumpChat("'+n.id+'","group","'+n.name+'")>\n' +
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
    return html;
}

//首页列表html
function showIndexList(n){
    var html="";
    var indexListType=n.type;
    var notice=n.notice;
    var id="";
    var headImgName="";
    var talkName="";
    var talkContent="";
    if(indexListType=="single"){
        id=n.dst.sId;
        headImgName="head";
        talkName=n.dst.sUnitname+"_"+n.dst.sName;
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
    html='<li name="'+id+'" type="'+indexListType+'" onclick=jumpChat("'+id+'","'+indexListType+'","'+talkName+'")>\n' +
        '                        <div class="liContentDiv scrollDiv">\n' +
        '                            <div class="liContentImgDiv">\n' +
        '                                <img src="../img/'+headImgName+'.png">\n' +
        '                                <div class="notice '+noticeHide+'">'+notice+'</div>\n' +
        '                            </div>\n' +
        '                            <div class="liContentRightDiv">\n' +
        '                                <div class="liContentUser liName">'+talkName+'</div>\n' +
        '                                <div class="liContentRecord">'+talkContent+'</div>\n' +
        '                            </div>\n' +
        '                        </div>\n' +
        '                        <div class="liSpace"/>\n' +
        '                    </li>';
    return html;
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
        var liHtml='<li name="'+chatSrcId+'" type="'+chatSrcType+'" onclick=jumpChat("'+chatSrcId+'","'+chatSrcType+'","'+chatSrcName+'")>\n' +
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
        var oldChatLi=$("#chatUl").html();
        $("#chatUl").html(liHtml+oldChatLi);
    }
}

//显示发送文件预览
function showFilePreview(time,data,uuid){
    // console.log(data)
    var readText="发送文件中...";
    //对话窗口显示消息
    var html='<div name="'+uuid+'" class="RightTalk chatMsg">\n' +
        '                    <div class="rightTalkImgDiv talkImgDiv">\n' +
        '                        <img src="../img/head.png">\n' +
        '                    </div>\n' +
        '                    <div class="rightTalkContent">\n' +
        '                        <div class="talkUser">'+time+'&nbsp;&nbsp;&nbsp;我</div>\n' +
        '                        <div class="rightTalkNotify">\n' +
        '                            <div class="rightTalkBubble">'+data+'</div>\n' +
        '                            <div class="read">'+readText+'</div>\n' +
        '                        </div>\n' +
        '                    </div>\n' +
        '                </div>'
    $("#chatWindow").append(html);
    scrollChatWindow();
}

//弹出好友名片
function showUserInfo(id, type){
    if(id==src){
        layer.msg("这是自己",{
            time:1000
        });
        return;
    }
    $.ajax({
        type : "POST",
        contentType: "application/json;charset=UTF-8",
        url : "/getUserByUserId/"+id,
        //请求成功
        success : function(result) {
            // console.log("getUser : "+result.data);
            var p=result.data;
            var index=layer.open({
                title:false,
                skin:"blueBackground",
                btn:false,
                content:'    <div id="userInfoImg"><img src="../img/head.png"></div>\n' +
                    '    <div id="userInfoName" class="userInfoDiv">姓名&nbsp;:&nbsp;<span>'+handleNull(p.sName)+'</span></div>\n' +
                    '    <div id="userInfoUnit" class="userInfoDiv">单位&nbsp;:&nbsp;<span>'+handleNull(p.sUnitname)+'</span></div>\n' +
                    '    <div id="userInfoDuty" class="userInfoDiv">职务&nbsp;:&nbsp;<span>'+handleNull(p.sDuty)+'</span></div>\n' +
                    '    <div id="userInfoTel" class="userInfoDiv">电话&nbsp;:&nbsp;<span>'+handleNull(p.sTel)+'</span></div>\n' +
                    '    <div id="openChatBtn" class="userInfoBtn">发送消息</div>'
            })
            $("#openChatBtn").on("click",function(){
                var name=p.sUnitname+"_"+p.sName;
                jumpChat(id,type,name);
                layer.close(index);
            })

        },
        //请求失败，包含具体的错误信息
        error : function(e){
            console.log(e.status);
            console.log(e.responseText);
        }
    })
}