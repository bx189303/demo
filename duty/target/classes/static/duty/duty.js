// var dutyServerUrl="http://localhost:23268";//localhost
var dutyServerUrl="http://10.2.68.70:23268";//centos
var dutyUserZtreeLevel='';

function initDutyHtml(id){
    loadUnit(id);
    windowHtmlMove('dutyHtml','duTyWindowTitleName');
}

var dutyZtreeSetting = {
    view: {
        // dblClickExpand: true,
        dblClickExpand: dblClickExpand,
        showLine: true,
        selectedMulti: false,
        showIcon:false,
        fontCss:{'color':'#4a4a4a'},//字体样式函数
    },
    data: {
        key:{
            name:"sName"
        },
        simpleData: {
            enable: true,
            idKey: "sId",
            pIdKey: "sParent",
            rootPId: "110108000000"
        }
    },
    callback: {
        beforeClick: function (treeId, treeNode,clickFlag,policeNum) {
            // console.log("树点击事件："+treeNode.sId)
            // console.log("树点击事件： "+policeNum+","+treeId+","+treeNode+","+clickFlag)
            var zTree = $.fn.zTree.getZTreeObj("zTree");
            // console.log(treeNode.sId);
            if(dutyUserZtreeLevel>treeNode.level){
                console.log("不能查看父节点");
                return true;
            }
            $("#dutyUnitName").html(treeNode.sName);
            loadDutyType(treeNode.sId,policeNum);
            return true;
            // if (treeNode.isParent) {
            //     zTree.expandNode(treeNode);
            //     return true;
            // } else {
            //     return true;
            // }
        }
    }
};

function dblClickExpand(treeId, treeNode) {
    return treeNode.level > 0;
}

//读取组织机构树
function loadUnit(userId){
    // console.log(new Date());
    $.ajax({
        type : "POST",
        contentType: "application/json;charset=UTF-8",
        url : dutyServerUrl+"/getUnitByUserId/"+userId,
        // url : "/getUnit",
        //请求成功
        success : function(result) {
            var zNodes=result.data;
            var userUnitId=result.data[0].sId;
            var t = $("#zTree");
            t = $.fn.zTree.init(t, dutyZtreeSetting, zNodes);
            var zTree = $.fn.zTree.getZTreeObj("zTree");
            var userTreeNode=zTree.getNodeByParam("sId",userUnitId);
            dutyUserZtreeLevel=userTreeNode.level;
            zTree.selectNode(userTreeNode);
            zTree.expandNode(userTreeNode);
            //树的点击事件
            dutyZtreeSetting.callback.beforeClick(userUnitId,userTreeNode);
        },
        //请求失败，包含具体的错误信息
        error : function(e){
            console.log(e.status);
            console.log(e.responseText);
        }
    })
}

//根据选中节点加载职位类型
function loadDutyType(unitId,policeNum){
    // console.log("加载战位类型 ："+policeNum)
    var data={
        "unitId":unitId
    }
    $.ajax({
        type : "POST",
        contentType: "application/json;charset=UTF-8",
        url : dutyServerUrl+"/getDutyByUnitId",
        data: JSON.stringify(data),
        //请求成功
        success : function(result) {
            // console.log(result.data)
            // if(result.data.length==0){
            //     return;
            // }
            showDutyType(result);
            if(result.data.length!=0){
                if(typeof policeNum=="undefined"){
                    policeNum=(result.data[0].policenum);
                }
                $('.dutyList[name="'+policeNum+'"]').click();
                $('.dutyList[name="'+policeNum+'"]')[0].scrollIntoView();
            }

        },
        //请求失败，包含具体的错误信息
        error : function(e){
            console.log(e.status);
            console.log(e.responseText);
        }
    })
}

//根据选中人加载人员信息
function loadDutyDetail(userId){
    //选中效果
    $('.dutyList').removeClass("checkedDutyList");
    $('.dutyList[name="'+userId+'"]').addClass("checkedDutyList");
    //请求
    var data={
        "userId":userId
    }
    $.ajax({
        type : "POST",
        contentType: "application/json;charset=UTF-8",
        url : dutyServerUrl+"/getDutyByUserId",
        data: JSON.stringify(data),
        //请求成功
        success : function(result) {
            // console.log('loadDutyDetail : '+result.data);
            showDutyDetail(result);
        },
        //请求失败，包含具体的错误信息
        error : function(e){
            console.log(e.status);
            console.log(e.responseText);
        }
    })
}
//根据搜索框查询人员信息
function loadDutyDetailByName(name){
    var data={
        "name":name
    }
    $.ajax({
        type : "POST",
        contentType: "application/json;charset=UTF-8",
        url : dutyServerUrl+"/getDutyByUserNameOrNum",
        data: JSON.stringify(data),
        //请求成功
        success : function(result) {
            // console.log('searchDutyDetail : '+result.data);
            if(typeof (result.data)=="undefined"){
                return;
            }
            var userUnitId=result.data.unitid;
            var userPoliceNum=result.data.policenum;
            var zTree = $.fn.zTree.getZTreeObj("zTree");
            var userTreeNode=zTree.getNodeByParam("sId",userUnitId);
            zTree.selectNode(userTreeNode);
            zTree.expandNode(userTreeNode,true);
            //树的点击事件
            dutyZtreeSetting.callback.beforeClick(userUnitId,userTreeNode,null,userPoliceNum);
        },
        //请求失败，包含具体的错误信息
        error : function(e){
            console.log(e.status);
            console.log(e.responseText);
        }
    })
}

//***************************************************************************************************************************
//***************************************************************************************************************************
function showDutyType(res){
    var list=res.data;
    var html='';
    if(list.length!=0){
        $("#mainDutyDiv").removeClass("dutyFullHeight");
        $(list).each(function(i,n){
            var duty="";
            if(n.duty==n.dutytype){
                duty=n.duty;
            }else{
                duty=n.duty+handleNull(n.dutytype);
            }
            html+='            <div class="dutyList" name="'+n.policenum+'" onclick=loadDutyDetail("'+n.policenum+'")>\n' +
                '                <div class="dutyType" title="'+duty+'">'+duty+'</div><div class="dutyName" title="'+n.policename+'">'+n.policename+'</div>\n' +
                '            </div>'
        })
        $("#mainDutyTypeDiv").html(html);
    }else {
        $("#mainDutyTypeDiv").html('<div class="dutyNotips">暂无战位信息</div>');
        $("#mainDutyDiv").addClass("dutyFullHeight");
        $("#dutyDetailDiv").hide();
    }
}
function showDutyDetail(res){
    $("#dutyDetailDiv").show();
    // console.log(res);
    var n=res.data;
    var duty="";
    if(n.duty==n.dutytype){
        duty=n.duty;
    }else{
        duty=n.duty+handleNull(n.dutytype);
    }
    var html='<div class="dutyInfo"><div class="dutyInfoName">职位</div><div class="dutyInfoValue" title="'+duty+'">'+duty+'</div></div>\n' +
        '            <div class="dutyInfo"><div class="dutyInfoName">单位</div><div class="dutyInfoValue" title="'+handleNull(n.unitname)+'">'+handleNull(n.unitname)+'</div></div>\n' +
        '            <div class="dutyInfo"><div class="dutyInfoName">姓名</div><div class="dutyInfoValue dutyInfoOfUserName" title="'+handleNull(n.policename)+'">'+handleNull(n.policename)+'</div></div>\n' +
        '            <div class="dutyInfo"><div class="dutyInfoName">警号</div><div class="dutyInfoValue" title="'+handleNull(n.policenum)+'"><u onclick=dutyCallbackZh(this)>'+handleNull(n.policenum)+'</u></div></div>\n' +
        '            <div class="dutyInfo"><div class="dutyInfoName">电话</div><div class="dutyInfoValue" title="'+handleNull(n.tel)+'"><u onclick=dutyCallbackSj(this)>'+handleNull(n.tel)+'</u></div></div>\n' +
        '            <div class="dutyInfo"><div class="dutyInfoName">移动警务</div><div class="dutyInfoValue" title="'+handleNull(n.ydjw)+'">'+handleNull(n.ydjw)+'</div></div>\n' +
        '            <div class="dutyInfo"><div class="dutyInfoName">内线</div><div class="dutyInfoValue" title="'+handleNull(n.nx)+'"><u onclick=dutyCallbackNx(this)>'+handleNull(n.nx)+'</u></div></div>\n' +
        '            <div class="dutyInfo"><div class="dutyInfoName">外线</div><div class="dutyInfoValue" title="'+handleNull(n.wx)+'"><u onclick=dutyCallbackWx(this)>'+handleNull(n.wx)+'</u></div></div>\n' +
        '            <div class="dutyInfo"><div class="dutyInfoName">800M</div><div class="dutyInfoValue" title="'+handleNull(n.tel800m)+'"><u onclick=dutyCallbackSt(this)>'+handleNull(n.tel800m)+'</u></div></div>\n' +
        '            <div class="dutyInfo"><div class="dutyInfoName">身份证</div><div class="dutyInfoValue" title="'+handleNull(n.idcard)+'">'+handleNull(n.idcard)+'</div></div>'+
        '            <div class="dutyInfo"><div class="dutyInfoName">开始时间</div><div class="dutyInfoValue" title="'+DateToYMDH(n.starttime)+'">'+DateToYMDH(n.starttime)+'</div></div>\n' +
        '            <div class="dutyInfo"><div class="dutyInfoName">结束时间</div><div class="dutyInfoValue" title="'+DateToYMDH(n.endtime)+'">'+DateToYMDH(n.endtime)+'</div></div>\n' ;
    $("#dutyDetailDiv").html(html);
}

function dutyCallbackZh(obj){
    var username=$("#dutyDetailDiv .dutyInfoOfUserName").text();
    var res=[{
        "name":username,
        "tel":$(obj).text(),
        "type":"zh"
    }]
    // console.log(res)
    if(typeof(getDutyCallbackFunc)!="undefined"&&getDutyCallbackFunc != null){
        getDutyCallbackFunc(res);
    }
}
function dutyCallbackSj(obj){
    var username=$("#dutyDetailDiv .dutyInfoOfUserName").text();
    var res=[{
        "name":username,
        "tel":$(obj).text(),
        "type":"sj"
    }]
    // console.log(res)
    if(typeof(getDutyCallbackFunc)!="undefined"&&getDutyCallbackFunc != null){
        getDutyCallbackFunc(res);
    }
}
function dutyCallbackNx(obj){
    var username=$("#dutyDetailDiv .dutyInfoOfUserName").text();
    var res=[{
        "name":username,
        "tel":$(obj).text(),
        "type":"nx"
    }]
    // console.log(res)
    if(typeof(getDutyCallbackFunc)!="undefined"&&getDutyCallbackFunc != null){
        getDutyCallbackFunc(res);
    }
}
function dutyCallbackWx(obj){
    var username=$("#dutyDetailDiv .dutyInfoOfUserName").text();
    var res=[{
        "name":username,
        "tel":$(obj).text(),
        "type":"wx"
    }]
    // console.log(res)
    if(typeof(getDutyCallbackFunc)!="undefined"&&getDutyCallbackFunc != null){
        getDutyCallbackFunc(res);
    }
}
function dutyCallbackSt(obj){
    var username=$("#dutyDetailDiv .dutyInfoOfUserName").text();
    var res=[{
        "name":username,
        "tel":$(obj).text(),
        "type":"st"
    }]
    // console.log(res)
    if(typeof(getDutyCallbackFunc)!="undefined"&&getDutyCallbackFunc != null){
        getDutyCallbackFunc(res);
    }
}

//***************************************************************************************************************************

function searchDutyDetail(){
    var name=$("#dutyNameInput").val();
    if(typeof name=="undefined"){
        return;
    }
    loadDutyDetailByName(name);
}

//***************************************************************************************************************************
function DateToYMDH(str){
    var y=str.substr(0,4);
    var m=str.substr(5,2);
    var d=str.substr(8,2);
    var h=str.substr(11,2);
    // return y+"年"+m+"月"+d+"日"+h+"时";
    return y+"-"+m+"-"+d+" "+h+"时";
}
function closeDutyWindow(){
    $("#dutyHtml").remove();
}
