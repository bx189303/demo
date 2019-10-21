/////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////
// Copyright 2017-2018 CoCall.Net

//功能：实现 Dispatch 点对点通信的 JS 操作部分

/////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////

//通道状态定义。
var en_status_error                 =   -1;
var en_status_standby               =   0 ;		//空闲。
var en_status_pickuped              =   1;		//“摘机”状态
var en_status_ringing               =   2;		//“振铃”状态
var en_status_talking               =   3;		//“通话”状态
var en_status_pending               =   7;		// “挂起”状态
var en_status_wait_remote_pickup    =   9;		// “等待被叫摘机”状态
var en_status_unavailable           =   11;		// “通道不可用”状态
var en_status_locked                =   12;		// “呼出锁定”状态
var en_status_remoteblock	        =   19;		// “对端闭塞”状态
var en_status_localblock            =   20; 	// “本端闭塞”状态 


///////////////////////////////////////////////////////////////////////////////////////////////////////
//变量定义部分
///////////////////////////////////////////////////////////////////////////////////////////////////////

//通道状态。
var DispatchPersonStatus = en_status_error;

//当前使用的通道(Chrome）。
var DispatchPersonChanID = -1;

//通道的COM组件实例(IE)
var DispatchPersonInstance = null;

//外部的事件接口
var PersonChanMsgExternHander = null ;
var PersonSipChanInfoMsgExternHander = null ;
var PersonSipInfoCmdExternHander = null ;
var PersonSipInfoCmdRspExternHander = null ;
var PersonSipInfoNotifyExternHander = null ;

///////////////////////////////////////////////////////////////////////////////////////////////////////
//内部事件接口
///////////////////////////////////////////////////////////////////////////////////////////////////////
function PersonChanMsgInsideHander(msg_id, msg_string, lParam, lParam_string, rParam, rParam_string) {

    //首先做内部处理。
    if (msg_id == 100) {
        //txtCallState.innerText = "有电话呼入"
        DispatchPersonStatus = en_status_ringing ;
    } else if (msg_id == 200) {
        //txtCallState.innerText = "通话建立"
        DispatchPersonStatus = en_status_talking ;
    } else if (msg_id == 300) {
        //txtCallState.innerText = "通话断开"
        DispatchPersonStatus = en_status_standby ;
    } else if (msg_id == 800) {
        //txtCallState.innerText = "等待对端接听"
        DispatchPersonStatus = en_status_wait_remote_pickup ;
    }

    //是否有输出注册事件接口。
    if( ! ( typeof(PersonChanMsgExternHander)=="undefined" )
        && ( PersonChanMsgExternHander != null
            && typeof(PersonChanMsgExternHander) === "function" )
    )
    {
        PersonChanMsgExternHander(  msg_id, msg_string, lParam, lParam_string, rParam, rParam_string  );
    }
}

function PersonSipChanInfoMsgInsideHander(info_type, Info_content) {
    //是否有输出注册事件接口。
    if( ! ( typeof(PersonSipChanInfoMsgExternHander)=="undefined" )
        && ( PersonSipChanInfoMsgExternHander != null
            && typeof(PersonSipChanInfoMsgExternHander) === "function" )
    )
    {
        PersonSipChanInfoMsgExternHander(  info_type, Info_content  );
    }
}

function PersonSipInfoCmdInsideHander(sip_info_type, command_id, command_type, command_info, lparm_info, rparm_info, from_id, to_id) {
    //是否有输出注册事件接口。
    if( ! ( typeof(PersonSipInfoCmdExternHander)=="undefined" )
        && ( PersonSipInfoCmdExternHander != null
            && typeof(PersonSipInfoCmdExternHander) === "function" )
    )
    {
        PersonSipInfoCmdExternHander(  sip_info_type, command_id, command_type, command_info, lparm_info, rparm_info, from_id, to_id  );
    }
}

function PersonSipInfoCmdRspInsideHander(sip_info_type, command_id, rsp_code, rsp_info) {
    //是否有输出注册事件接口。
    if( ! ( typeof(PersonSipInfoCmdRspExternHander)=="undefined" )
        && ( PersonSipInfoCmdRspExternHander != null
            && typeof(PersonSipInfoCmdRspExternHander) === "function" )
    )
    {
        PersonSipInfoCmdRspExternHander(  sip_info_type, command_id, rsp_code, rsp_info  );
    }
}

function PersonSipInfoNotifyInsideHander(sip_info_type, info_id, info_name, info_type, info, lparm_info, rparm_info) {
    //是否有输出注册事件接口。
    if( ! ( typeof(PersonSipInfoNotifyExternHander)=="undefined" )
        && ( PersonSipInfoNotifyExternHander != null
            && typeof(PersonSipInfoNotifyExternHander) === "function" )
    )
    {
        PersonSipInfoNotifyExternHander(  sip_info_type, info_id, info_name, info_type, info, lparm_info, rparm_info  );
    }
}


//初始化通道事件。
function DispatchPerson_BindEvent(
    _OnChanMsgFunction
    , _OnSipChanInfoMsgFunction
    , _OnSipInfoCmdFunction
    , _OnSipInfoCmdRspFunction
    , _OnSipInfoNotifyFunction )
{
    try {

        //回调事件只支持Chrome
        if (BrowserTypeInfo != "chrome") {
            return ;
        }

        PersonChanMsgExternHander = _OnChanMsgFunction ;
        PersonSipChanInfoMsgExternHander = _OnSipChanInfoMsgFunction ;
        PersonSipInfoCmdExternHander = _OnSipInfoCmdFunction ;
        PersonSipInfoCmdRspExternHander = _OnSipInfoCmdRspFunction ;
        PersonSipInfoNotifyExternHander = _OnSipInfoNotifyFunction ;
    }
    catch (e) {
    }
}


//获取通道状态。
function DispatchPerson_GetChanStatus(
    _DispatchPersonInstance )
{
    try {
        //开启IE的调试模式.
        //debugger;
        //检查状态。

        //IE浏览器。
        if (BrowserTypeInfo == "ie") {

            //是否传入了COM组件实例。
            if (_DispatchPersonInstance != null
                && typeof (_DispatchPersonInstance) != "undefined") {
                DispatchPersonInstance = _DispatchPersonInstance;
            }

            //检查组件实例是否可用。
            if (DispatchPersonInstance == null
                || typeof (DispatchPersonInstance) == "undefined") {
                alert("没有初始化COM组件模块");
                return;
            }

            //检查组件是否绑定有通道
            if (DispatchPersonInstance.chan_id < 0) {
                DispatchPersonStatus = en_status_error;
            }
            else{
                DispatchPersonStatus = DispatchPersonInstance.chan_status;
            }
            return DispatchPersonStatus ;
        }
        //Chrome浏览器。
        else if (BrowserTypeInfo == "chrome") {
            return DispatchPersonStatus ;
        }

    }
    catch (e) {
    }
}



//发起呼叫可以不需要通道ID，底层会自动获取一个空闲的通道ID。
function DispatchPerson_MakeCall(
    _DispatchPersonInstance
    , called )
{
    try {
        //开启IE的调试模式.
        //debugger;
        //检查状态。

        //IE浏览器。
        if (BrowserTypeInfo == "ie") {

            //是否传入了COM组件实例。
            if (_DispatchPersonInstance != null
                && typeof (_DispatchPersonInstance) != "undefined") {
                DispatchPersonInstance = _DispatchPersonInstance;
            }

            //检查组件实例是否可用。
            if (DispatchPersonInstance == null
                || typeof (DispatchPersonInstance) == "undefined") {
                alert("没有初始化COM组件模块");
                return;
            }

            //检查被叫号码是否可用。
            if (called == null
                || typeof (called) == "undefined") {
                alert("没有传入被叫号码");
                return;
            }


            //检查组件是否绑定有通道
            if (DispatchPersonInstance.chan_id < 0) {
                //初始化一个默认通道
                DispatchPersonInstance.Init(-1);
                if (DispatchPersonInstance.last_error_code != 0
                    || DispatchPersonInstance.chan_id < 0 ) {
                    alert("初始化默认通道失败");
                    return;
                }
            }

            DispatchPersonChanID = DispatchPersonInstance.chan_id;
            DispatchPersonStatus = DispatchPersonInstance.chan_status;
            if( DispatchPersonStatus != en_status_standby) {
                //检查组件状态。
                alert("通道状态没有空闲，不能外呼");
                return;
            }

            DispatchPersonInstance.MakeCall(called);
            if (DispatchPersonInstance.last_error_code != 0) {
                alert("MakeCall失败");
                return;
            }

            DispatchPersonStatus = en_status_wait_remote_pickup;
        }
        //Chrome浏览器。
        else if (BrowserTypeInfo == "chrome") {
            //启动外呼的时候，传-1作为通道号，让底层自动选择一个空闲通道。
            DispatchMakeCall( -1 , called,
                function (response_code, response_info, original_msg) {
                    //回调接收返回结果
                    if (response_code != 0) {
                        alert("启动外呼出错：" + response_info);
                        DispatchPersonChanID = -1;
                        DispatchPersonStatus = en_status_error;
                    }
                    else {
                        //外呼成功，将会返回通道号。
                        DispatchPersonChanID = response_info;
                        DispatchPersonStatus = en_status_wait_remote_pickup;

                        //绑定事件。
                        DispatchPersonChanMsgEventHanderMapGet().put(DispatchPersonChanID, PersonChanMsgInsideHander);

                        DispatchPersonSipChanInfoMsgEventHanderMapGet().put(DispatchPersonChanID, PersonSipChanInfoMsgInsideHander);

                        DispatchPersonSipInfoCmdEventHanderMapGet().put(DispatchPersonChanID, PersonSipInfoCmdInsideHander);

                        DispatchPersonSipInfoCmdRspEventHanderMapGet().put(DispatchPersonChanID, PersonSipInfoCmdRspInsideHander);

                        DispatchPersonSipInfoNotifyEventHanderMapGet().put(DispatchPersonChanID, PersonSipInfoNotifyInsideHander);

                    }
                });
        }

    }
    catch (e) {
    }
}


//应答呼叫需要传入一个通道ID，这个通道ID会附带在振铃事件传到上层。
function DispatchPerson_AnswerCall(
    _DispatchPersonInstance
    , _DispatchPersonChanID ) {
    try {
        //开启IE的调试模式.
        //debugger;

        //是否传入了通道ID。
        if (_DispatchPersonChanID != null
            && typeof (_DispatchPersonChanID) != "undefined") {
            DispatchPersonChanID = _DispatchPersonChanID;
        }

        //检查通道ID是否可用。
        if (DispatchPersonChanID == null
            || typeof (DispatchPersonChanID) == "undefined") {
            alert("没有传入通道ID");
            return;
        }

        if (DispatchPersonChanID < 0) {
            alert("没有正确的通道ID");
            return;
        }


        //IE浏览器。
        if (BrowserTypeInfo == "ie") {

            //是否传入了COM组件实例。
            if (_DispatchPersonInstance != null
                && typeof (_DispatchPersonInstance) != "undefined") {
                DispatchPersonInstance = _DispatchPersonInstance;
            }

            //检查组件实例是否可用。
            if (DispatchPersonInstance == null
                || typeof (DispatchPersonInstance) == "undefined") {
                alert("没有初始化COM组件模块");
                return;
            }

            //检查组件是否绑定有通道
            if (DispatchPersonInstance.chan_id < 0) {
                //绑定需要应答的默认通道
                DispatchPersonInstance.Init(DispatchPersonChanID );
                if (DispatchPersonInstance.last_error_code != 0
                    || DispatchPersonInstance.chan_id < 0) {
                    alert("初始化默认通道失败");
                    return;
                }
            }

            DispatchPersonStatus = DispatchPersonInstance.chan_status;
            if (DispatchPersonStatus != en_status_ringing) {
                //检查组件状态。
                alert("通道没有呼入，不能应答");
                return;
            }


            DispatchPersonInstance.AnswerCall();
            if (DispatchPersonInstance.last_error_code != 0) {
                alert("AnswerCall 失败");
                return;
            }

        }
        //Chrome浏览器。
        else if (BrowserTypeInfo == "chrome") {

            DispatchAnswerCall(DispatchPersonChanID,
                function (response_code, response_info, original_msg) {
                    //回调接收返回结果
                    if (response_code != 0) {
                        alert("应答呼入出错：" + response_info);
                    }
                });

            //绑定事件。
            DispatchPersonChanMsgEventHanderMapGet().put(DispatchPersonChanID, PersonChanMsgInsideHander);
            DispatchPersonSipChanInfoMsgEventHanderMapGet().put(DispatchPersonChanID, PersonSipChanInfoMsgInsideHander);
            DispatchPersonSipInfoCmdEventHanderMapGet().put(DispatchPersonChanID, PersonSipInfoCmdInsideHander);
            DispatchPersonSipInfoCmdRspEventHanderMapGet().put(DispatchPersonChanID, PersonSipInfoCmdRspInsideHander);
            DispatchPersonSipInfoNotifyEventHanderMapGet().put(DispatchPersonChanID, PersonSipInfoNotifyInsideHander);
        }
    }
    catch (e) {
    }
}



// DTMF发送
function DispatchPerson_SendDTMF(num) {
    try {
        //开启IE的调试模式.
        //debugger;

        //IE浏览器。
        if (BrowserTypeInfo == "ie") {

            //检查组件实例是否可用。
            if (DispatchPersonInstance == null
                || typeof (DispatchPersonInstance) == "undefined") {
                //alert("没有初始化COM组件模块");
                return;
            }

            //检查组件是否绑定有通道
            DispatchPersonChanID = DispatchPersonInstance.chan_id;
            if (DispatchPersonChanID < 0) {
                //alert("没有传入通道ID");
                return;
            }

            //检查通道状态。
            DispatchPersonStatus = DispatchPersonInstance.chan_status;
            if (DispatchPersonStatus != en_status_talking) {
                // alert("通话没有建立，不能发送DTMF");
                return;
            }


            DispatchPersonInstance.SendDTMF(num);
            if (DispatchPersonInstance.last_error_code != 0) {
                //alert("发送DTMF失败");
                return;
            }
        }
        //Chrome浏览器。
        else if (BrowserTypeInfo == "chrome") {

            //检查通道ID是否可用。
            if (DispatchPersonChanID == null
                || typeof (DispatchPersonChanID) == "undefined") {
                // alert("没有传入通道ID");
                return;
            }

            if (DispatchPersonChanID < 0) {
                // alert("没有正确的通道ID");
                return;
            }

            if (DispatchPersonStatus != en_status_talking) {
                // alert("通话没有建立，不能发送DTMF");
                return;
            }

            DispatchSendDTMF(DispatchPersonChanID, num,
                function (response_code, response_info, original_msg) {
                    //回调接收返回结果
                    if (response_code != 0) {
                        alert("发送按键出错：" + response_info);
                    }
                });
        }
    }
    catch (e) {
    }
}


function DispatchPerson_DropCall() {
    try {
        //开启IE的调试模式.
        //debugger;
        //IE浏览器。
        if (BrowserTypeInfo == "ie") {

            //检查组件实例是否可用。
            if (DispatchPersonInstance == null
                || typeof (DispatchPersonInstance) == "undefined") {
                alert("没有初始化COM组件模块");
                return;
            }

            //检查组件是否绑定有通道
            DispatchPersonChanID = DispatchPersonInstance.chan_id;
            if (DispatchPersonChanID < 0) {
                alert("没有传入通道ID");
                return;
            }

            DispatchPersonInstance.DropCall();
            if (DispatchPersonInstance.last_error_code != 0) {
                alert("DropCall 失败");
                return;
            }
        }
        //Chrome浏览器。
        else if (BrowserTypeInfo == "chrome") {

            //检查通道ID是否可用。
            if (DispatchPersonChanID == null
                || typeof (DispatchPersonChanID) == "undefined") {
                alert("没有传入通道ID");
                return;
            }

            if (DispatchPersonChanID < 0) {
                alert("没有正确的通道ID");
                return;
            }

            DispatchDropCall(DispatchPersonChanID,
                function (response_code, response_info, original_msg) {
                    //回调接收返回结果
                    if (response_code != 0) {
                        alert("挂机出错：" + response_info);
                    }
                });
        }
    }
    catch (e) {
    }
}




