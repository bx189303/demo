window.addEventListener('message', function (e) {
    // console.log("接收到父窗口的postmessage ： "+e.data.data);
    if(e.data.src=="parentWindow"){
        if(e.data.type=="func"){
            func(e.data.data.func,e.data.data.param);
        }else if(e.data.type=="data"){
            console.log("接收到父窗口的消息 ： "+e.data.data);
        }
    }
})

//关闭父页面的iframe
function closeDutyWindow(){
    // $("#dutyHtml").remove();
    runParentFunc("closeDutyWindow");
}

//执行父页面方法
function runParentFunc(){
    var funcName=arguments[0];
    var param='';
    var res={
        src:"dutyWindow",
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
    window.parent.postMessage(res,"*");
}

//给父页面发送消息
function callBackToParent(data){
    var res={
        src:"dutyWindow",
        type:"data",
        data:data
    }
    window.parent.postMessage(res,"*");
}

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
function parentTest(json){
    callBackToParent(json);
    runParentFunc("f1");
    runParentFunc("f2","子调父f2");
    runParentFunc("f3","子调父f3参数1,子调父f3参数2");
}
function childtest(s){
    console.log("这是战位页面的方法，接受到参数 ："+s);
}