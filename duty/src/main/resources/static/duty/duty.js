$(function(){
    // var userId=GetUrlParam("id");
    // if(userId==null){
    //     userId="6DFEB2D364DA4CD3A4A1EB12A1F462B1";
    // }
    // loadUnit(userId);
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
            $("#unitName").html(treeNode.sName);
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

function closeDuty(){
    $("#dutyHtml").remove();
}

//读取组织机构树
function loadUnit(userId){
    // console.log(new Date());
    $.ajax({
        type : "POST",
        contentType: "application/json;charset=UTF-8",
        url : "/getUnitByUserId/"+userId,
        //请求成功
        success : function(result) {
            var zNodes=result.data;
            var userUnitId=result.data[0].sId;
            var t = $("#zTree");
            t = $.fn.zTree.init(t, setting, zNodes);
            var zTree = $.fn.zTree.getZTreeObj("zTree");
            zTree.selectNode(zTree.getNodeByParam("sId",userUnitId));
            zTree.expandNode(zTree.getNodeByParam("sId", userUnitId));
            setting.callback.beforeClick(userUnitId,zTree.getNodeByParam("sId", userUnitId));
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
        url : "/getDutyByUnitId",
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
        url : "/getDutyByUserId",
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
        $("#mainDutyDiv").removeClass("fullHeight");
        $(list).each(function(i,n){
            html+='            <div class="dutyList" onclick=loadDutyDetail("'+n.policeid+'")>\n' +
                '                <div class="dutyType">'+n.duty+'</div><div class="dutyName">'+n.policename+'</div>\n' +
                '            </div>'
        })
        $("#mainDutyTypeDiv").html(html);
        loadDutyDetail(list[0].policeid);
    }else {
        $("#mainDutyTypeDiv").html('<div class="notips">暂无战位信息</div>');
        $("#mainDutyDiv").addClass("fullHeight");
        $("#dutyDetailDiv").hide();
    }
}
function showDutyDetail(res){
    $("#dutyDetailDiv").show();
    // console.log(res);
    var n=res.data;
    var html='<div class="dutyInfo"><div class="infoName">职位</div><div class="infoValue">'+n.duty+'</div></div>\n' +
        '            <div class="dutyInfo"><div class="infoName">单位</div><div class="infoValue" title="'+n.unitname+'">'+n.unitname+'</div></div>\n' +
        '            <div class="dutyInfo"><div class="infoName">姓名</div><div class="infoValue" title="'+n.policename+'">'+n.policename+'</div></div>\n' +
        '            <div class="dutyInfo"><div class="infoName">警号</div><div class="infoValue" title="'+n.policenum+'">'+n.policenum+'</div></div>\n' +
        '            <div class="dutyInfo"><div class="infoName">电话</div><div class="infoValue" title="'+handleNull(n.tel)+'">'+handleNull(n.tel)+'</div></div>\n' +
        '            <div class="dutyInfo"><div class="infoName">移动警务</div><div class="infoValue" title="'+handleNull(n.ydjw)+'">'+handleNull(n.ydjw)+'</div></div>\n' +
        '            <div class="dutyInfo"><div class="infoName">内线</div><div class="infoValue" title="'+handleNull(n.nx)+'">'+handleNull(n.nx)+'</div></div>\n' +
        '            <div class="dutyInfo"><div class="infoName">外线</div><div class="infoValue" title="'+handleNull(n.wx)+'">'+handleNull(n.wx)+'</div></div>\n' +
        '            <div class="dutyInfo"><div class="infoName">800M</div><div class="infoValue" title="'+handleNull(n.tel800m)+'">'+handleNull(n.tel800m)+'</div></div>\n' +
        '            <div class="dutyInfo"><div class="infoName">身份证</div><div class="infoValue" title="'+handleNull(n.idcard)+'">'+handleNull(n.idcard)+'</div></div>'+
        '            <div class="dutyInfo"><div class="infoName">开始时间</div><div class="infoValue" title="'+DateToYMDH(n.starttime)+'">'+DateToYMDH(n.starttime)+'</div></div>\n' +
        '            <div class="dutyInfo"><div class="infoName">结束时间</div><div class="infoValue" title="'+DateToYMDH(n.endtime)+'">'+DateToYMDH(n.endtime)+'</div></div>\n' ;
    $("#dutyDetailDiv").html(html);
}

function DateToYMDH(str){
    var y=str.substr(0,4);
    var m=str.substr(5,2);
    var d=str.substr(8,2);
    var h=str.substr(11,2);
    return y+"年"+m+"月"+d+"日"+h+"时";

}