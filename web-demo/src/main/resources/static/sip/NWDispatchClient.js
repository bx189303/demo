/////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////
// Copyright 2017-2018 CoCall.Net

//功能：实现 Dispatch 客户端 JS 操作部分

/////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////


///////////////////////////////////////////////////////////////////////////////////////////////////////
//Dispatch Client 部分
///////////////////////////////////////////////////////////////////////////////////////////////////////

//判断浏览器版本
var BrowserTypeInfo = "unknow";
function BrowserTypeInfoRead() {
    //取得浏览器的userAgent字符串
    var userAgent = navigator.userAgent;

    BrowserTypeInfo = "unknow";

    if (window.ActiveXObject || "ActiveXObject" in window)
        BrowserTypeInfo = "ie";
    else if (userAgent.indexOf("edge") > -1)
        BrowserTypeInfo = "edge";
    else if (userAgent.indexOf("Firefox") > -1)
        BrowserTypeInfo = "firefox";
    else if (userAgent.indexOf("Chrome") > -1 && userAgent.indexOf("Safari") > -1)
        BrowserTypeInfo = "chrome";
    else if (userAgent.indexOf("Opera") > -1)
        BrowserTypeInfo = "opera";
    else if (userAgent.indexOf("Safari") > -1 && userAgent.indexOf("Chrome") == -1)
        BrowserTypeInfo = "safari";

    //alert(BrowserTypeInfo);
}

/////////////////////////////////////////////////////////////////////////
//功能：初始化客函数。
//参数：
//      sip_server_addr ： Sip 服务器地址。
//
/////////////////////////////////////////////////////////////////////////
//保存客户端的COM组件实例。
var DispatchClientComInstance

//初始化客户端。
function DispatchClient_Init(
    _ClientComInstance              //客户端的COM组件实例
    , _ClientLogOutMessageFunction       //客户端日志输出接口。（Chrome）
    , _ClientLoginStatusFunction         //接收注册事件（Chrome）
    , _ClientPagerMessageFunction        //接收文本消息。
    , _ClientMessageResponseFunction      //接收文本消息发送结果。
    , _ClientCallIncomeFunction           //接收客户端呼入事件。
    , _ClientHostStatusEventHander        //接收Host状态事件。
) {
    try {
        console.log('插件测试')
        //判断浏览器版本
        BrowserTypeInfoRead();

        //卸载以前的。
        //DispatchClient_UnInit()

        //开启IE的调试模式.
        //debugger;

        //IE浏览器初始化。
        if (BrowserTypeInfo == "ie") {

            if (_ClientComInstance == null
                || typeof (_ClientComInstance) == "undefined") {
                alert("没有定义客户端COM组件");
                return;
            }

            DispatchClientComInstance = _ClientComInstance ;

            DispatchClientComInstance.Init();
            if (DispatchClientComInstance.last_error_code != 0) {
                alert("初始化组件失败");
                return;
            }
        }
        //Chrome浏览器初始化。
        else if (BrowserTypeInfo == "chrome") {
            //连接服务器并初始化。
            DispatchInit(_ClientLogOutMessageFunction
                , _ClientLoginStatusFunction
                , _ClientPagerMessageFunction        //接收文本消息。
                , _ClientMessageResponseFunction      //接收文本消息发送结果。
                , _ClientCallIncomeFunction           //接收客户端呼入事件。
                , _ClientHostStatusEventHander        //接收Host状态事件。

            );
        }
        //其他浏览器报错。
        else {
            alert("请使用IE或者Chrome浏览器");
        }

    }
    catch (ex) {
        // alert(ex);
        theState.innerText = "你的计算机不能运行此控件，请检查是否已经安装!";
    }
}


//客户端登录。
function DispatchClient_Login(
    sip_server_addr                      //Sip 服务器地址。
    , soap_server_addr                   //Soap 服务器地址。
    , user_name                         //用户名。
    , user_pwd                          //用户密码
)
{
    try {

        //IE浏览器初始化。
        if (BrowserTypeInfo == "ie") {

            if (DispatchClientComInstance == null
                || typeof (DispatchClientComInstance) == "undefined")
            {
                alert("没有初始化客户端模块");
                return;
            }

            DispatchClientComInstance.sip_server_addr = sip_server_addr ;
            DispatchClientComInstance.soap_server_addr = soap_server_addr ;
            DispatchClientComInstance.user_name = user_name ;
            DispatchClientComInstance.user_pwd = user_pwd ;
            DispatchClientComInstance.Login();
            if (DispatchClientComInstance.last_error_code != 0) {
                alert("客户端注册失败");
                return;
            }
        }
        //Chrome浏览器初始化。
        else if (BrowserTypeInfo == "chrome") {

            //设置属性
            DispatchAttributeSet(COCALL_HOST_ATTRIBUTE_NAME_SOAP_SERVER, soap_server_addr
                , function (response_code, response_info, original_msg) {
                    //回调接收返回结果
                    if (response_code != 0) {
                        alert("设置Soap服务器地址出错!");
                    }
                }
            );

            DispatchAttributeSet(COCALL_HOST_ATTRIBUTE_NAME_SIP_SERVER, sip_server_addr
                , function (response_code, response_info, original_msg) {
                    //回调接收返回结果
                    if (response_code != 0) {
                        alert("设置Sip服务器地址出错!");
                    }
                }
            );
            DispatchAttributeSet(COCALL_HOST_ATTRIBUTE_NAME_COCALL_NUMBER, user_name
                , function (response_code, response_info, original_msg) {
                    //回调接收返回结果
                    if (response_code != 0) {
                        alert("设置用户名出错!");
                    }
                }
            );
            DispatchAttributeSet(COCALL_HOST_ATTRIBUTE_NAME_COCALL_PASSWORD, user_pwd
                , function (response_code, response_info, original_msg) {
                    //回调接收返回结果
                    if (response_code != 0) {
                        alert("设置密码出错!");
                    }
                }
            );

            /*
            //测试获取属性。
            DispatchAttributeGet(COCALL_HOST_ATTRIBUTE_NAME_SOAP_SERVER
                , function (response_code, response_info, original_msg) {
                    //回调接收返回结果
                    if (response_code != 0) {
                        alert("获取Soap服务器地址出错!");
                    }
                    else {
                        //alert("获取Soap服务器地址为：" + response_info );
                    }
                }
            );
            DispatchAttributeGet(COCALL_HOST_ATTRIBUTE_NAME_SIP_SERVER
                , function (response_code, response_info, original_msg) {
                    //回调接收返回结果
                    if (response_code != 0) {
                        alert("获取Sip服务器地址出错!");
                    }
                    else {
                        //alert("获取Sip服务器地址为：" + response_info );
                    }
                }
            );
            DispatchAttributeGet(COCALL_HOST_ATTRIBUTE_NAME_COCALL_NUMBER
                , function (response_code, response_info, original_msg) {
                    //回调接收返回结果
                    if (response_code != 0) {
                        alert("获取用户名出错!");
                    }
                    else {
                        //alert("获取用户名为：" + response_info );
                    }
                }
            );
            DispatchAttributeGet(COCALL_HOST_ATTRIBUTE_NAME_COCALL_PASSWORD
                , function (response_code, response_info, original_msg) {
                    //回调接收返回结果
                    if (response_code != 0) {
                        alert("获取用户密码出错!");
                    }
                    else {
                        //alert("获取用户密码为：" + response_info );
                    }
                }
            );
            //*/
            //注册登录。
            DispatchRegister(  function (response_code, response_info, original_msg) {
                //回调接收返回结果
                if (response_code != 0) {
//                        alert("注册出错：" + response_info);
                    console.log("注册出错：" + response_info);
                }
            });

        }
        //其他浏览器报错。
        else {
            alert("请使用IE或者Chrome浏览器");
        }

    }
    catch (ex) {
        // alert(ex);
    }
}


//客户端注销登录
function DispatchClient_Logout() {
    try {
        //开启IE的调试模式.
        //debugger;

        //IE浏览器清理。
        if (BrowserTypeInfo == "ie") {
            if (DispatchClientComInstance == null
                || typeof (DispatchClientComInstance) == "undefined") {
                alert("没有初始化客户端模块");
                return;
            }

            DispatchClientComInstance.Logout();
        }
        //Chrome浏览器清理。
        else if (BrowserTypeInfo == "chrome") {
            DispatchUnRegister(
                function (response_code, response_info, original_msg) {
                    //回调接收返回结果
                    if (response_code != 0) {
                        alert("注销登录出错：" + response_info);
                    }
                });
        }
        //其他浏览器报错。
        else {
            alert("请使用IE或者Chrome浏览器");
        }
    }
    catch (e) {
    }
}


// 网页结束
function DispatchClient_UnInit() {
    try {
        //开启IE的调试模式.
        //debugger;

        //IE浏览器清理。
        if (BrowserTypeInfo == "ie") {
            if (DispatchClientComInstance == null
                || typeof (DispatchClientComInstance) == "undefined") {
                alert("没有初始化客户端模块");
                return;
            }

            DispatchClientComInstance.Logout();
            DispatchClientComInstance.UnInit();
        }
        //Chrome浏览器清理。
        else if (BrowserTypeInfo == "chrome") {
            DispatchRegisterEventHanderSet(null);
            DispatchUnRegister();
            DispatchUnInit();
        }
        //其他浏览器报错。
        else {
            alert("请使用IE或者Chrome浏览器");
        }
    }
    catch (e) {
        //theState.innerText="你的计算机不能运行此控件，请检查是否已经安装!";
    }
}
