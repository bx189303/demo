var dutyUrl="http://192.168.8.102:23268/duty/index.html";

window.onload=function () {
    // console.log("引入getDuty.js");
    // var html='<iframe id="dutyIframe" src='+dutyUrl+' style="width:700px;height:470px;position:fixed;top:0;left:0;right:0;bottom:0;margin:auto;z-index: 1111;"></iframe>';
    // $('body').append(html);
    // document.getElementById("dutyIframe").style.display="none";
};

//执行方法
function func(funcName,param){
    try{
        if(typeof(eval(funcName)) == "function"){
            if(typeof(param)=="undefined"){
                eval(funcName+"();");
            }else{
                // console.log("子页面传来的方法参数 ： "+param)
                var params=param.split(",");
                if(params.length==1){
                    eval(funcName+"(params[0]);");
                }else if(params.length==2){
                    eval(funcName+"(params[0],params[1]);");
                }else if(params.length==3){
                    eval(funcName+"(params[0],params[1],params[2]);");
                }else if(params.length==4){
                    eval(funcName+"(params[0],params[1],params[2],params[3]);");
                }
            }
        } else {
            postToChild("请输入正确的方法名");
        }
    }catch (e) {
        console.error(e);
        postToChild("请输入正确的方法名");
    }
}

//执行子页面的方法
function runChildFunc(){
    if(!document.getElementById("dutyIframe")){
        console.log("请先调用getDuty(userId)方法打开战位页面");
        return;
    }
    var funcName=arguments[0];
    var param='';
    var res={
        src:"parentWindow",
        type:"func",
        data: {
            func:funcName
        }
    }
    for(var i=0; i<arguments.length; i++){
        if(i!=0){
            param+=arguments[i]+",";
        }
    }
    if(param.length!=0){
        param=param.substr(0,param.length-1);
        res.data.param=param;
    }
    // console.log("子页面调用的参数："+param)
    document.getElementById("dutyIframe").contentWindow.postMessage(res,"*");
}

//给战位页面发消息
function postToChild(data){
    if(!document.getElementById("dutyIframe")){
        console.log("请先调用getDuty(userId)方法打开战位页面");
        return;
    }
    var res={
        src:"parentWindow",
        type:"data",
        data:data
    }
    document.getElementById("dutyIframe").contentWindow.postMessage(res,"*");
}

window.addEventListener('message', function (e) {
    if(e.data.src=="dutyWindow"){
        // console.log("接收到战位窗口的postmessage ： "+e.data.data);
        if(e.data.type=="func"){
            func(e.data.data.func,e.data.data.param);
        }else if(e.data.type=="data"){
            console.log("接收到战位窗口的消息 ： "+e.data.data);
        }
    }
})

//关闭战位窗口
function closeDutyWindow(){
    document.getElementById('dutyIframe').remove();
    // document.getElementById("dutyIframe").style.display="none";
}

//弹出战位页面
function getDuty_iframe(id) {
    if(document.getElementById("dutyIframe")){
        return;
    }
    if(typeof(id)=="undefined"){
        console.log("请传入id");
        return;
    }
    var html='<iframe id="dutyIframe" src="http://192.168.8.102:23268/duty/index.html?id='+id+'" style="width:700px;height:470px;position:fixed;top:0;left:0;right:0;bottom:0;margin:auto;z-index: 1111;"></iframe>';
    $('body').append(html);
    // document.getElementById("dutyIframe").setAttribute("src",dutyUrl+"?id="+id);
    // document.getElementById("dutyIframe").style.display="block";
}

