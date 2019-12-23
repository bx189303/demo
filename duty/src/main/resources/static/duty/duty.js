var serverUrl="http://192.168.8.102:23268";
var userLevel='';

$(function(){

});

var setting = {
    view: {
        // dblClickExpand: true,
        dblClickExpand: dblClickExpand,
        showLine: true,
        selectedMulti: false,
        showIcon:false,
        fontCss:{'color':'#fff'},//字体样式函数
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
        beforeClick: function (treeId, treeNode) {
            var zTree = $.fn.zTree.getZTreeObj("zTree");
            // console.log(treeNode.sId);
            if(userLevel>treeNode.level){
                console.log("不能查看父节点");
                return true;
            }
            $("#dutyUnitName").html(treeNode.sName);
            loadDutyType(treeNode.sId);
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
        url : serverUrl+"/getUnitByUserId/"+userId,
        //请求成功
        success : function(result) {
            var zNodes=result.data;
            var userUnitId=result.data[0].sId;
            var t = $("#zTree");
            t = $.fn.zTree.init(t, setting, zNodes);
            var zTree = $.fn.zTree.getZTreeObj("zTree");
            var userTreeNode=zTree.getNodeByParam("sId",userUnitId);
            userLevel=userTreeNode.level;
            zTree.selectNode(userTreeNode);
            zTree.expandNode(userTreeNode);
            setting.callback.beforeClick(userUnitId,userTreeNode);
        },
        //请求失败，包含具体的错误信息
        error : function(e){
            console.log(e.status);
            console.log(e.responseText);
        }
    })
}

//根据选中节点加载职位类型
function loadDutyType(unitId){
    var data={
        "unitId":unitId
    }
    $.ajax({
        type : "POST",
        contentType: "application/json;charset=UTF-8",
        url : serverUrl+"/getDutyByUnitId",
        data: JSON.stringify(data),
        //请求成功
        success : function(result) {
            // console.log(result.data)
            showDutyType(result);
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
    var data={
        "userId":userId,
        "date":"2019-12-17"
    }
    $.ajax({
        type : "POST",
        contentType: "application/json;charset=UTF-8",
        url : serverUrl+"/getDutyByUserId",
        data: JSON.stringify(data),
        //请求成功
        success : function(result) {
            // console.log(result.data)

            showDutyDetail(result);
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
            html+='            <div class="dutyList" onclick=loadDutyDetail("'+n.policeid+'")>\n' +
                '                <div class="dutyType">'+n.duty+'</div><div class="dutyName">'+n.policename+'</div>\n' +
                '            </div>'
        })
        $("#mainDutyTypeDiv").html(html);
        loadDutyDetail(list[0].policeid);
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
    var html='<div class="dutyInfo"><div class="dutyInfoName">职位</div><div class="dutyInfoValue">'+n.duty+'</div></div>\n' +
        '            <div class="dutyInfo"><div class="dutyInfoName">单位</div><div class="dutyInfoValue" title="'+n.unitname+'">'+n.unitname+'</div></div>\n' +
        '            <div class="dutyInfo"><div class="dutyInfoName">姓名</div><div class="dutyInfoValue" title="'+n.policename+'">'+n.policename+'</div></div>\n' +
        '            <div class="dutyInfo"><div class="dutyInfoName">警号</div><div class="dutyInfoValue" title="'+n.policenum+'">'+n.policenum+'</div></div>\n' +
        '            <div class="dutyInfo"><div class="dutyInfoName">电话</div><div class="dutyInfoValue" title="'+handleNull(n.tel)+'">'+handleNull(n.tel)+'</div></div>\n' +
        '            <div class="dutyInfo"><div class="dutyInfoName">移动警务</div><div class="dutyInfoValue" title="'+handleNull(n.ydjw)+'">'+handleNull(n.ydjw)+'</div></div>\n' +
        '            <div class="dutyInfo"><div class="dutyInfoName">内线</div><div class="dutyInfoValue" title="'+handleNull(n.nx)+'">'+handleNull(n.nx)+'</div></div>\n' +
        '            <div class="dutyInfo"><div class="dutyInfoName">外线</div><div class="dutyInfoValue" title="'+handleNull(n.wx)+'">'+handleNull(n.wx)+'</div></div>\n' +
        '            <div class="dutyInfo"><div class="dutyInfoName">800M</div><div class="dutyInfoValue" title="'+handleNull(n.tel800m)+'">'+handleNull(n.tel800m)+'</div></div>\n' +
        '            <div class="dutyInfo"><div class="dutyInfoName">身份证</div><div class="dutyInfoValue" title="'+handleNull(n.idcard)+'">'+handleNull(n.idcard)+'</div></div>'+
        '            <div class="dutyInfo"><div class="dutyInfoName">开始时间</div><div class="dutyInfoValue" title="'+DateToYMDH(n.starttime)+'">'+DateToYMDH(n.starttime)+'</div></div>\n' +
        '            <div class="dutyInfo"><div class="dutyInfoName">结束时间</div><div class="dutyInfoValue" title="'+DateToYMDH(n.endtime)+'">'+DateToYMDH(n.endtime)+'</div></div>\n' ;
    $("#dutyDetailDiv").html(html);
}

//***************************************************************************************************************************
//***************************************************************************************************************************
function DateToYMDH(str){
    var y=str.substr(0,4);
    var m=str.substr(5,2);
    var d=str.substr(8,2);
    var h=str.substr(11,2);
    return y+"年"+m+"月"+d+"日"+h+"时";

}
function closeDutyWindow(){
    $("#dutyHtml").remove();
}