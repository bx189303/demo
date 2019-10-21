/////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////
// Copyright 2017-2018 CoCall.Net

//功能：实现Chrome 浏览器的 JS 交互部分

/////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////





////////////////////////////////////////////////////////////////////////////////////////
//DespatchExtension
////////////////////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////////////////////////////////////////////
//操作类型定义。
///////////////////////////////////////////////////////////////////////////////////////////////////
var COCALL_HOST_MSG_TYPE                                        =       "host_msg_type";
var COCALL_HOST_MSG_ID	                                        =	     "host_msg_id";
var COCALL_HOST_MSG_TYPE_REQUEST_FUNCTION                       =       "function";
var COCALL_HOST_MSG_TYPE_REQUEST_ATTRIBUTE_GET                  =       "attribute_get";
var COCALL_HOST_MSG_TYPE_REQUEST_ATTRIBUTE_SET                  =       "attribute_set";
var COCALL_HOST_MSG_TYPE_EVENT                                  =       "host_event";
var COCALL_HOST_MSG_TYPE_RESPONSE                               =       "msg_respons";

///////////////////////////////////////////////////////////////////////////////////////////////////
//属性相关定义。
///////////////////////////////////////////////////////////////////////////////////////////////////
var COCALL_HOST_ATTRIBUTE_NAME                                  =       "attribute_name";
var COCALL_HOST_ATTRIBUTE_VALUE                                 =       "attribute_value";

var COCALL_HOST_ATTRIBUTE_NAME_SOAP_SERVER                    =         "soap_server";
var COCALL_HOST_ATTRIBUTE_NAME_SIP_SERVER                    =          "sip_server";
var COCALL_HOST_ATTRIBUTE_NAME_COCALL_SERVER                    =       "cocall_server";
var COCALL_HOST_ATTRIBUTE_NAME_COCALL_NUMBER                    =       "cocall_number";
var COCALL_HOST_ATTRIBUTE_NAME_COCALL_PASSWORD                  =       "cocall_password";
var COCALL_HOST_ATTRIBUTE_NAME_AUDIO_CONTROL_HOST_NAME          =       "audio_control_host_name";
var COCALL_HOST_ATTRIBUTE_NAME_AUDIO_IN_CONTROL_HOST_NAME       =       "audio_in_control_host_name";
var COCALL_HOST_ATTRIBUTE_NAME_AUDIO_CODE_LIST                  =       "audio_code_list";
var COCALL_HOST_ATTRIBUTE_NAME_REMOTE_NUMBER                    =       "remote_number";
var COCALL_HOST_ATTRIBUTE_NAME_MESSAGE_TEXT_CODE_PAGE			=       "message_text_code_page";
var COCALL_HOST_ATTRIBUTE_NAME_INFO_TEXT_CODE_PAGE				=       "info_text_code_page";



///////////////////////////////////////////////////////////////////////////////////////////////////
//函数相关定义。
///////////////////////////////////////////////////////////////////////////////////////////////////
var COCALL_HOST_FUNCTION_NAME							=					"function_name";
var COCALL_HOST_FUNCTION_PARAMETERS						=					"function_parameters";



//////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////
//全局功能模块。
//////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////

var COCALL_HOST_FUNCTION_NAME_REGISTER					=					"Register";
var COCALL_HOST_FUNCTION_NAME_UNREGISTER				=					"UnRegister";

var COCALL_HOST_FUNCTION_NAME_SHUTDOWN    				=					"Shutdown";

//////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////
//点对点通信功能模块。
//////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////

var COCALL_HOST_FUNCTION_NAME_MAKECALL					=					"MakeCall";
var COCALL_HOST_FUNCTION_NAME_DROPCALL					=					"DropCall";
var COCALL_HOST_FUNCTION_NAME_ANSWERCALL				=					"AnswerCall";
var COCALL_HOST_FUNCTION_NAME_SENDDTMF					=					"SendDTMF";
var COCALL_HOST_FUNCTION_NAME_SENDPAGERMESSAGE			=					"SendPagerMessage";
var COCALL_HOST_FUNCTION_NAME_SENDINFOTEXT				=					"SendInfoText";
var COCALL_HOST_FUNCTION_NAME_SENDINFOCOMMAND			=					"SendInfoCommand";
var COCALL_HOST_FUNCTION_NAME_SENDINFOCOMMANDRSP		=					"SendInfoCommandRsp";
var COCALL_HOST_FUNCTION_NAME_SENDINFONOTIFY			=					"SendInfoNotify";


var  COCALL_HOST_FUNCTION_PARAMETER_CALLED_NUMBER		=					"called_number"  ;
var  COCALL_HOST_FUNCTION_PARAMETER_CHAN_ID				=					"chan_id"  ;
var  COCALL_HOST_FUNCTION_PARAMETER_DTMF_VALUE			=					"dtmf_value"  ;

var  COCALL_HOST_FUNCTION_PARAMETER_INFO_TYPE			=					"info_type"  ;
var  COCALL_HOST_FUNCTION_PARAMETER_INFO_TEXT			=					"info_text"  ;

var  COCALL_HOST_FUNCTION_PARAMETER_INFO_CMD_ID			=					"cmd_id"  ;
var  COCALL_HOST_FUNCTION_PARAMETER_INFO_CMD_TYPE		=					"cmd_type"  ;
var  COCALL_HOST_FUNCTION_PARAMETER_INFO_CMD_INFO		=					"cmd_info"  ;
var  COCALL_HOST_FUNCTION_PARAMETER_INFO_LPARM_INFO		=					"lparm_info"  ;
var  COCALL_HOST_FUNCTION_PARAMETER_INFO_RPARM_INFO		=					"rparm_info"  ;
var  COCALL_HOST_FUNCTION_PARAMETER_INFO_FROM_ID		=					"from_id"  ;
var  COCALL_HOST_FUNCTION_PARAMETER_INFO_TO_ID			=					"to_id"  ;

var  COCALL_HOST_FUNCTION_PARAMETER_INFO_RSP_CODE		=					"rsp_code"  ;
var  COCALL_HOST_FUNCTION_PARAMETER_INFO_RSP_INFO		=					"rsp_info"  ;

var  COCALL_HOST_FUNCTION_PARAMETER_INFO_USER_ID		=					"user_id"  ;
var  COCALL_HOST_FUNCTION_PARAMETER_INFO_USER_NAME		=					"user_name"  ;
var  COCALL_HOST_FUNCTION_PARAMETER_INFO_INFO			=					"info"  ;
var  COCALL_HOST_FUNCTION_PARAMETER_INFO_LPARM			=					"lparm"  ;
var  COCALL_HOST_FUNCTION_PARAMETER_INFO_RPARAM			=					"rparam"  ;

var  COCALL_HOST_FUNCTION_PARAMETER_INFO_MESSAGE_SUBJECT	=				"message_subject"  ;
var  COCALL_HOST_FUNCTION_PARAMETER_INFO_MESSAGE_TEXT		=				"message_text"  ;
var  COCALL_HOST_FUNCTION_PARAMETER_INFO_SEND_TO			=				"send_to"   ;



//全局功能事件。
var COCALL_HOST_EVENT_NAME_ONCOCALLMSG					=					"OnCoCallMsg";
var COCALL_HOST_EVENT_NAME_ONINFOCOMMAND				=					"OnInfoCommand";
var COCALL_HOST_EVENT_NAME_ONINFOCOMMANDRSP				=					"OnInfoCommandRsp";
var COCALL_HOST_EVENT_NAME_ONINFONOTIFY					=					"OnInfoNotify";

//Host状态值定义。
var COCALL_HOST_EVENT_NAME_ONCOCALLMSG_MSG_TYPE_HOST_STATUS				=   	100    ;
var COCALL_HOST_STATUS_CODE_INIT_BEGIN			=   	0       ;
var COCALL_HOST_STATUS_CODE_INIT_FAIL			=   	1       ;
var COCALL_HOST_STATUS_CODE_PROCESS_BEGIN		=   	2	    ;
var COCALL_HOST_STATUS_CODE_PROCESS_END			=   	3       ;


//////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////
//视频功能模块。
//////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////

//属性相关
//当前电脑输出桌面数量。
var COCALL_HOST_ATTRIBUTE_NAME_DESKTOP_COUNTGET				=				"desktop_count";
//当前电脑的每个输出桌面名字。
var  COCALL_HOST_ATTRIBUTE_NAME_DESKTOP_NAME				=				"desktop_name";
//当前电脑输出桌面次序。
var  COCALL_HOST_ATTRIBUTE_NAME_DESKTOP_INDEX				=				"desktop_index";


var COCALL_HOST_FUNCTION_PARAMETER_DESKTOP_NAME				=				"desktop_name";

//视频墙操作。
var COCALL_HOST_FUNCTION_NAME_VIDEOWALL_CREATE				=				"VideoWallCreate";
var COCALL_HOST_FUNCTION_NAME_VIDEOWALL_DESTROY				=				"VideoWallDestroy";
var COCALL_HOST_FUNCTION_NAME_VIDEOWALL_SET_SHOWINFO		=				"VideoWallSetShowInfo";
var COCALL_HOST_FUNCTION_NAME_VIDEOWALL_CLEAR				=				"VideoWallClear";

var COCALL_HOST_FUNCTION_PARAMETER_DIALOG_CAPTION			=				"dlg_caption";
var COCALL_HOST_FUNCTION_PARAMETER_DIALOG_NAME				=				"dlg_name";
var COCALL_HOST_FUNCTION_PARAMETER_FULL_SCREEN				=				"full_screen";

var COCALL_HOST_FUNCTION_PARAMETER_DIALOG_LEFT				=				"dlg_left";
var COCALL_HOST_FUNCTION_PARAMETER_DIALOG_TOP				=				"dlg_top";
var COCALL_HOST_FUNCTION_PARAMETER_DIALOG_HEIGHT			=				"dlg_height";
var COCALL_HOST_FUNCTION_PARAMETER_DIALOG_WIDTH				=				"dlg_width";

//视频源操作。
var COCALL_HOST_FUNCTION_NAME_VIDEOCHAN_ADD					=				"VideoChanAdd";
var COCALL_HOST_FUNCTION_NAME_VIDEOCHAN_REMOVE				=				"VideoChanRemove";
var COCALL_HOST_FUNCTION_NAME_VIDEOCHAN_SET_MAX				=				"VideoChanSetMax";
var COCALL_HOST_FUNCTION_NAME_VIDEOCHAN_SET_FULL_SCREEN		=				"VideoChanSetFullScreen";
var COCALL_HOST_FUNCTION_NAME_VIDEOCHAN_CLEAN				=				"VideoChanClean";
var COCALL_HOST_FUNCTION_NAME_VIDEOCHAN_REARRANGE			=				"VideoChansRearrange";

var COCALL_HOST_FUNCTION_PARAMETER_VIDEO_SOURCE_TYPE		=				 "video_source_type";
var COCALL_HOST_FUNCTION_PARAMETER_VIDEO_SOURCE_ID			=				 "video_source_id";
var COCALL_HOST_FUNCTION_PARAMETER_VIDEO_SOURCE_NAME		=				 "video_source_name";
var COCALL_HOST_FUNCTION_PARAMETER_PARAMETER_EXTERN			=				 "parameter_extern";
var COCALL_HOST_FUNCTION_PARAMETER_SHOW_MAX					=				 "show_max";
var COCALL_HOST_FUNCTION_PARAMETER_SPLIT_SCREEN_COUNT		=				 "split_screen_count";



var COCALL_HOST_FUNCTION_EXTERN_PARAMETERS					=				 "extern_parameters";

var COCALL_HOST_FUNCTION_PARAMETER_SHOW_VIDEO_WALL_RMENU	=				 "show_video_wall_rmenu";
var COCALL_HOST_FUNCTION_PARAMETER_SHOW_VIDEO_CHAN_RMENU	=				 "show_video_chan_rmenu";
var COCALL_HOST_FUNCTION_PARAMETER_VIDEO_CHAN_OVER_TYPE		=				 "video_chan_over_type";


//视频源事件。
var COCALL_HOST_EVENT_NAME_ONVIDEOWALLCREATED				=				"OnVideoWallCreated";
var COCALL_HOST_EVENT_NAME_ONVIDEOWALLDESTROYED				=				"OnVideoWallDestroyed";
var COCALL_HOST_EVENT_NAME_ONVIDEOCHANCREATED				=				"OnVideoChanCreated";
var COCALL_HOST_EVENT_NAME_ONVIDEOCHANDESTROYED				=				"OnVideoChanDestroyed";


//////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////
//会议室功能模块。
//////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////

var COCALL_HOST_FUNCTION_NAME_ROOMCREATE					=				"RoomCreate";
var COCALL_HOST_FUNCTION_NAME_ROOMDISSOLVE                  =               "RoomDissolve";
var COCALL_HOST_FUNCTION_NAME_ROOMATTRIBUTEGET              =               "RoomAttributeGet";

var COCALL_HOST_FUNCTION_PARAMETER_QUEUE_ID					=				"queue_id";
var COCALL_HOST_FUNCTION_PARAMETER_MEMBER_ID				=				"member_id";
var COCALL_HOST_FUNCTION_PARAMETER_MEMBER_NAME				=				"member_name";
var COCALL_HOST_FUNCTION_PARAMETER_MEMBER_TYPE				=				"member_type";
var COCALL_HOST_FUNCTION_PARAMETER_MEMBER_STATUS			=				"member_status";
var COCALL_HOST_FUNCTION_PARAMETER_KICK_INFO				=				"kick_info";
var COCALL_HOST_FUNCTION_PARAMETER_VIEW_WND					=				"view_wnd";
var COCALL_HOST_FUNCTION_PARAMETER_INFO_TEXT				=				"info_text";


//会议成员操作
var COCALL_HOST_FUNCTION_NAME_MEMBER_INVITE					=				"MemberInvite";
var COCALL_HOST_FUNCTION_NAME_MEMBER_KICKOUT				=				"MemberKickOut";
var COCALL_HOST_FUNCTION_NAME_MEMBER_TYPESET				=				"MemberTypeSet";
var COCALL_HOST_FUNCTION_NAME_MEMBER_VIDEOVIEW_START		=				"MemberVideoViewStart";
var COCALL_HOST_FUNCTION_NAME_MEMBER_VIDEOVIEW_STOP			=				"MemberVideoViewStop";


//会议事件。
var COCALL_HOST_EVENT_NAME_ONCONFCONNECTED					=				"OnConfConnected";
var COCALL_HOST_EVENT_NAME_ONCONFDISCONNECTED				=				"OnConfDisconnected";
var COCALL_HOST_EVENT_NAME_ONCONFDISSOLVE					=				"OnConfDissolve";
var COCALL_HOST_EVENT_NAME_ONCONFTEXTINFO					=				"OnConfTextInfo";
var COCALL_HOST_EVENT_NAME_ONCONFMEMBERTYPECHANGED			=				"OnConfMemberTypeChanged";
var COCALL_HOST_EVENT_NAME_ONCONFMEMBERSTATUSCHANGED		=				"OnConfMemberStatusChanged";

var COCALL_HOST_ATTRIBUTE_NAME_ROOM_QUEUE_ID                =               "room_queue_id";
var COCALL_HOST_ATTRIBUTE_NAME_ROOM_SIP_NUMBER              =               "room_sip_number";
var COCALL_HOST_ATTRIBUTE_NAME_ROOM_STATUS                  =               "room_status";

///////////////////////////////////////////////////////////////////////////////
//组件变量定义。
///////////////////////////////////////////////////////////////////////////////

// The ID of the extension we want to talk to.
var DispatchExtensionId = "jkcgjdomcealhggacbokgihgljbknpgf";
var nCoCallRequestID = 0 ;


//保存需要对发出的请求结果，进行返回调用的回调函数。
var DispatchCmdMsgResponseCallbackMap = new NWMap();


///////////////////////////////////////////////////////
//全局回调函数。
//保存需要对客户端日志输出回调接口
var DispatchLogOutMessageHander = null;
//保存需要注册状态通知回调函数。
var DispatchRegisterEventHander = null ;
//保存需要对收到的文本消息返回调用的回调函数。
var DispatchPagerMessageEventHander = null ;
//保存需要对收到的文本消息发送结果，进行返回调用的回调函数。
var DispatchPagerMessageResponseEventHander = null ;
//保存客户端呼入事件的回调函数。
var DispatchCallIncomeEvenHander = null ;

///////////////////////////////////////////////////////
//点对点回调函数。
var DispatchPersonChanMsgEventHanderMap = null;
function DispatchPersonChanMsgEventHanderMapGet( )
{
    if ((typeof (DispatchPersonChanMsgEventHanderMap) == "undefined")
        || (DispatchPersonChanMsgEventHanderMap == null)
    ) {
        DispatchPersonChanMsgEventHanderMap = new NWMap();
    }
    return DispatchPersonChanMsgEventHanderMap;
};


var DispatchPersonSipChanInfoMsgEventHanderMap = null;
function DispatchPersonSipChanInfoMsgEventHanderMapGet( )
{
    if ((typeof (DispatchPersonSipChanInfoMsgEventHanderMap) == "undefined")
        || (DispatchPersonSipChanInfoMsgEventHanderMap == null)
    ) {
        DispatchPersonSipChanInfoMsgEventHanderMap = new NWMap();
    }
    return DispatchPersonSipChanInfoMsgEventHanderMap;
};


var DispatchPersonSipInfoCmdEventHanderMap = null;
function DispatchPersonSipInfoCmdEventHanderMapGet( )
{
    if ((typeof (DispatchPersonSipInfoCmdEventHanderMap) == "undefined")
        || (DispatchPersonSipInfoCmdEventHanderMap == null)
    ) {
        DispatchPersonSipInfoCmdEventHanderMap = new NWMap();
    }
    return DispatchPersonSipInfoCmdEventHanderMap;
};

var DispatchPersonSipInfoCmdRspEventHanderMap = null;
function DispatchPersonSipInfoCmdRspEventHanderMapGet( )
{
    if ((typeof (DispatchPersonSipInfoCmdRspEventHanderMap) == "undefined")
        || (DispatchPersonSipInfoCmdRspEventHanderMap == null)
    ) {
        DispatchPersonSipInfoCmdRspEventHanderMap = new NWMap();
    }
    return DispatchPersonSipInfoCmdRspEventHanderMap;
};

var DispatchPersonSipInfoNotifyEventHanderMap = null ;
function DispatchPersonSipInfoNotifyEventHanderMapGet( )
{
    if ((typeof (DispatchPersonSipInfoNotifyEventHanderMap) == "undefined")
        || (DispatchPersonSipInfoNotifyEventHanderMap == null)
    ) {
        DispatchPersonSipInfoNotifyEventHanderMap = new NWMap();
    }
    return DispatchPersonSipInfoNotifyEventHanderMap;
};




///////////////////////////////////////////////////////////////////////////////
//组件输出日志。
///////////////////////////////////////////////////////////////////////////////
function DispatchLogOutMessage(text) {

    console.log(text);

    //是否传入回调。
    if (!(typeof (DispatchLogOutMessageHander) == "undefined")
        && (DispatchLogOutMessageHander != null
            && typeof (DispatchLogOutMessageHander) === "function")
    ) {
        DispatchLogOutMessageHander(text);
    }
}



//////////////////////////////////////////////////////////////////////////////
//JS内部函数
//////////////////////////////////////////////////////////////////////////////

//发送命令给组件。
function DispatchCmdMsgSend(extension_id , cmd_msg)
{
    //chrome.runtime.sendMessage(extension_id , cmd_msg );
    var evt = document.createEvent("CustomEvent");
    evt.initCustomEvent('DispatchCommandEvent', true, false, cmd_msg );
    // fire the event
    document.dispatchEvent(evt);
}


//////////////////////////////////////////////////////////////////////////////
//组件事件相关。
//////////////////////////////////////////////////////////////////////////////

//收到一个请求的回复。
//参数：
//host_msg_id ： 请求的消息ID。
//response_code ： 回复值。
//response_info ： 回复的描述。
//original_msg : 请求的原始消息。

function DispatchOnMessageResponse( host_msg_id , response_code , response_info , original_msg ) {
    DispatchLogOutMessage("received one response msg ");
    DispatchLogOutMessage("response msg's msg_id is : " + JSON.stringify(host_msg_id)
        +"; response_code is : " + JSON.stringify(response_code)
        +"; response_info is : " + JSON.stringify(response_info)
    );

    if ( original_msg == null )
    {
        return ;
    }

    var request_type = original_msg[COCALL_HOST_MSG_TYPE] ;
    if ( request_type == null )
    {
        return ;
    }

    if ( request_type == COCALL_HOST_MSG_TYPE_REQUEST_FUNCTION )
    {
        DispatchOnFunctionResponse( host_msg_id , response_code , response_info , original_msg );
    }
    else if ( request_type == COCALL_HOST_MSG_TYPE_REQUEST_ATTRIBUTE_GET  )
    {
        DispatchOnAttrGetResponse(  host_msg_id , response_code , response_info , original_msg );
    }
    else if ( request_type == COCALL_HOST_MSG_TYPE_REQUEST_ATTRIBUTE_SET  )
    {
        DispatchOnAttrSetResponse(  host_msg_id , response_code , response_info , original_msg );
    }
}

//OnCoCallMsg事件。
//原型：[id(1), helpstring("method OnCoCallMsg")] HRESULT OnCoCallMsg(short msg_type ,long msg_id, BSTR msg_string, long lParam ,BSTR lParam_string , long rParam, BSTR rParam_string);
function DispatchOnCoCallMsg(event_parameters) {
    //必有参数。
    var msg_type = event_parameters.msg_type ;
    var msg_id = event_parameters.msg_id ;
    if ( msg_type == null
        || msg_id  == null  )
    {
        DispatchLogOutMessage("parameters msg_type or msg_id is null ");
        return ;
    }
    //可能有参数
    var msg_string = event_parameters.msg_string ;
    var lParam = event_parameters.lParam ;
    var lParam_string = event_parameters.lParam_string ;
    var rParam = event_parameters.rParam ;
    var rParam_string = event_parameters.rParam_string ;

    DispatchLogOutMessage("received one host event msg type is " + JSON.stringify(msg_type) );

    //注册事件。
    if( msg_type == 0 )
    {
        var register_info ;
        DispatchLogOutMessage("收到一个注册消息");
        if(msg_id == 20000 )
        {
            register_info = "客户端 "+ "正在连接服务器。" ;
        }
        else if (msg_id == 21000)
        {
            register_info = "客户端 "+ "已经连接服务器。" ;
        }
        else if (msg_id == 22000)
        {
            register_info = "客户端 "+ "正在从服务器断开。" ;
        }
        else if (msg_id == 23000)
        {
            register_info = "客户端 "+ "已经从服务器断开。" ;
        }
        else if (msg_id == 24000)
        {
            register_info = "客户端 "+ "连接服务器失败。" ;
        }
        else if (msg_id == 25000)
        {
            register_info = "客户端 "+ "注销失败。" ;
        }
        else if (msg_id == 26000)
        {
            register_info = "客户端 "+ "没有连接服务器。" ;
        }
        else
        {
            register_info = "客户端 "+ "没有连接服务器。" ;
        }

        DispatchLogOutMessage( register_info );

        //是否有输出注册事件接口。
        if( ! ( typeof(DispatchRegisterEventHander)=="undefined" )
            && ( DispatchRegisterEventHander != null
                && typeof(DispatchRegisterEventHander) === "function" )
        )
        {
            DispatchRegisterEventHander( msg_id  , register_info  );
        }
    }
    //通道事件。
    else if( msg_type == 1 )
    {
        var chan_id = lParam ;
        //是不是呼入事件，
        if(msg_id == 100 )
        {
            var call_from_id = msg_string ;
            var call_subject = lParam_string ;

            DispatchLogOutMessage( "有电话呼入，号码为：" + msg_string );

            //是否有输出注册事件接口。
            if( ! ( typeof(DispatchCallIncomeEvenHander)=="undefined" )
                && ( DispatchCallIncomeEvenHander != null
                    && typeof(DispatchCallIncomeEvenHander) === "function" )
            )
            {
                DispatchCallIncomeEvenHander(  chan_id , call_from_id  , call_subject  );
            }
        }
        else if(msg_id == 200 )
        {
            DispatchLogOutMessage( "通话中。" );
        }
        else if(msg_id == 300 )
        {
            DispatchLogOutMessage( "通话已经断开。" );
        }
        else if(msg_id == 800 )
        {
            DispatchLogOutMessage( "已呼通，等待对方摘机。" );
        }

        //检查是否有回调
        var __PersonChanMsgEventHander = DispatchPersonChanMsgEventHanderMapGet().get( chan_id );
        if( ! ( typeof(__PersonChanMsgEventHander)=="undefined" )
            && ( __PersonChanMsgEventHander != null
                && typeof(__PersonChanMsgEventHander) === "function" )
        )
        {
            __PersonChanMsgEventHander( msg_id, msg_string, lParam, lParam_string, rParam, rParam_string  );
        }
    }
    //短消息事件。
    else if( msg_type == 3 )
    {
        var recv_from = lParam_string;
        var msg_subject = rParam_string ;
        var msg_text_info  = msg_string ;

        DispatchLogOutMessage( "收到短消息，来自（" + recv_from + ")，主题（" + msg_subject + "),内容（" + msg_text_info + ")" );
        //是否有输出注册事件接口。
        if( ! ( typeof(DispatchPagerMessageEventHander)=="undefined" )
            && ( DispatchPagerMessageEventHander != null
                && typeof(DispatchPagerMessageEventHander) === "function" )
        )
        {
            DispatchPagerMessageEventHander(  recv_from , msg_subject  , msg_text_info  );
        }
    }
    //Sip 通道的 Info 消息。
    else if( msg_type == 4 )
    {
        var chan_id = msg_id ;
        var info_type = lParam_string ;
        var Info_content = rParam_string ;

        DispatchLogOutMessage( "收到Info，info_type（" + info_type + ")，Info_content（" + Info_content + ")" );

        //检查是否有回调
        var __PersonSipChanInfoMsgEventHander = DispatchPersonSipChanInfoMsgEventHanderMapGet().get( chan_id );
        if( ! ( typeof(__PersonSipChanInfoMsgEventHander)=="undefined" )
            && ( __PersonSipChanInfoMsgEventHander != null
                && typeof(__PersonSipChanInfoMsgEventHander) === "function" )
        )
        {
            __PersonSipChanInfoMsgEventHander( info_type, Info_content  );
        }

    }
    //短消息发送结果通知事件。
    else if( msg_type == 5 )
    {
        var request_send_to = rParam_string;
        var message_id = msg_string;
        var response_code = msg_id ;
        var response_info = lParam_string;

        DispatchLogOutMessage( "收到短消息发送结果，request_send_to（" + request_send_to + ")，message_id（" + message_id + "),response_info（" + response_info + ")" );

        //是否有输出注册事件接口。
        if( ! ( typeof(DispatchPagerMessageResponseEventHander)=="undefined" )
            && ( DispatchPagerMessageResponseEventHander != null
                && typeof(DispatchPagerMessageResponseEventHander) === "function" )
        )
        {
            DispatchPagerMessageResponseEventHander(  request_send_to , message_id  , response_code , response_info );
        }
    }
    //HOST 状态事件。
    else if( msg_type == COCALL_HOST_EVENT_NAME_ONCOCALLMSG_MSG_TYPE_HOST_STATUS )
    {
        var statue_code = msg_id ;
        var statue_info = msg_string;

        if ( statue_code == COCALL_HOST_STATUS_CODE_INIT_BEGIN )
        {
            DispatchLogOutMessage( "收到HOST初始化启动消息" );
        }
        else if ( statue_code == COCALL_HOST_STATUS_CODE_INIT_FAIL )
        {
            DispatchLogOutMessage( "收到HOST初始化失败消息" );
        }
        else if ( statue_code == COCALL_HOST_STATUS_CODE_PROCESS_BEGIN )
        {
            DispatchLogOutMessage( "收到HOST处理启动消息" );
        }
        else if ( statue_code == COCALL_HOST_STATUS_CODE_PROCESS_END )
        {
            DispatchLogOutMessage( "收到HOST处理结束消息" );
        }


        //是否有输出注册事件接口。
        if( ! ( typeof(DispatchHostStatusEventHander)=="undefined" )
            && ( DispatchHostStatusEventHander != null
                && typeof(DispatchHostStatusEventHander) === "function" )
        )
        {
            DispatchHostStatusEventHander(  statue_code , statue_info );
        }
    }
}


//OnInfoCommand事件。
//[id(2), helpstring("method OnInfoCommand")] HRESULT OnInfoCommand(BSTR cmd_id , BSTR cmd_type,BSTR cmd_info,BSTR lparm_info ,BSTR rparm_info ,BSTR from_id , BSTR to_id);
function DispatchOnInfoCommand(event_parameters) {

    var chan_id = event_parameters.chan_id ;
    var sip_info_type = event_parameters.sip_info_type ;

    var cmd_id = event_parameters.cmd_id ;
    var cmd_type = event_parameters.cmd_type ;
    var cmd_info = event_parameters.cmd_info ;
    var lparm_info = event_parameters.lparm_info ;
    var rparm_info = event_parameters.rparm_info ;
    var from_id = event_parameters.from_id ;
    var to_id = event_parameters.to_id ;


    //检查是否有回调
    var __PersonSipInfoCmdEventHander = DispatchPersonSipInfoCmdEventHanderMapGet().get( chan_id );
    if( ! ( typeof(__PersonSipInfoCmdEventHander)=="undefined" )
        && ( __PersonSipInfoCmdEventHander != null
            && typeof(__PersonSipInfoCmdEventHander) === "function" )
    )
    {
        __PersonSipInfoCmdEventHander( sip_info_type , cmd_id, cmd_type, cmd_info, lparm_info, rparm_info, from_id, to_id  );
    }


}

//OnInfoCommandRsp事件。
// [id(3), helpstring("method OnInfoCommandRsp")] HRESULT OnInfoCommandRsp(BSTR cmd_id , LONG rsp_code, BSTR rsp_info);
function DispatchOnInfoCommandRsp(event_parameters) {
    var chan_id = event_parameters.chan_id ;
    var sip_info_type = event_parameters.sip_info_type ;

    var cmd_id = event_parameters.cmd_id ;
    var rsp_code = event_parameters.rsp_code ;
    var rsp_info = event_parameters.rsp_info ;

    //检查是否有回调
    var __PersonSipInfoCmdRspEventHander = DispatchPersonSipInfoCmdRspEventHanderMapGet().get( chan_id );
    if( ! ( typeof(__PersonSipInfoCmdRspEventHander)=="undefined" )
        && ( __PersonSipInfoCmdRspEventHander != null
            && typeof(__PersonSipInfoCmdRspEventHander) === "function" )
    )
    {
        __PersonSipInfoCmdRspEventHander( sip_info_type, cmd_id, rsp_code, rsp_info  );
    }


}

//OnInfoNotify事件。
//[id(4), helpstring("method OnInfoNotify")] HRESULT OnInfoNotify(BSTR user_id , BSTR user_name , BSTR info_type, BSTR  info , BSTR lparm , BSTR rparam);
function DispatchOnInfoNotify(event_parameters) {
    var chan_id = event_parameters.chan_id ;
    var sip_info_type = event_parameters.sip_info_type ;

    var user_id = event_parameters.user_id ;
    var user_name = event_parameters.user_name ;
    var info_type = event_parameters.info_type ;
    var info = event_parameters.info ;
    var lparm = event_parameters.lparm ;
    var rparam = event_parameters.rparam ;

    //检查是否有回调
    var __PersonSipInfoNotifyEventHander = DispatchPersonSipInfoNotifyEventHanderMapGet().get( chan_id );
    if( ! ( typeof(__PersonSipInfoNotifyEventHander)=="undefined" )
        && ( __PersonSipInfoNotifyEventHander != null
            && typeof(__PersonSipInfoNotifyEventHander) === "function" )
    )
    {
        __PersonSipInfoNotifyEventHander( sip_info_type, user_id, user_name, info_type, info, lparm, rparam  );
    }

}


///////////////////////////////////////////////////////
//视频墙回调函数。
///////////////////////////////////////////////////////

//视频墙已经创建
var DispatchOnVideoWallCreatedEventHanderMap = null;
function DispatchOnVideoWallCreatedEventHanderSet( dlg_name , _handler  )
{
    if ((typeof (dlg_name) == "undefined")
        || (dlg_name == null)
    ) {
        return ;
    }

    if ((typeof (DispatchOnVideoWallCreatedEventHanderMap) == "undefined")
        || (DispatchOnVideoWallCreatedEventHanderMap == null)
    ) {
        DispatchOnVideoWallCreatedEventHanderMap = new NWMap();
    }

    if ((typeof (_handler) == "undefined")
        || (_handler == null)
        || ! ( typeof(__EventHander) === "function")
    ) {
        DispatchOnVideoWallCreatedEventHanderMap.remove(dlg_name );
    }
    else{
        DispatchOnVideoWallCreatedEventHanderMap.put(dlg_name , _handler );
    }

};

function DispatchOnVideoWallCreatedEventHanderGet( dlg_name  )
{
    if ((typeof (dlg_name) == "undefined")
        || (dlg_name == null)
    ) {
        return null;
    }

    if ((typeof (DispatchOnVideoWallCreatedEventHanderMap) == "undefined")
        || (DispatchOnVideoWallCreatedEventHanderMap == null)
    ) {
        return null;
    }

    DispatchOnVideoWallCreatedEventHanderMap.get(dlg_name );
};

function DispatchOnVideoWallCreated(event_parameters) {
    var dlg_name = event_parameters.dlg_name ;
    var desktop_name = event_parameters.desktop_name ;

    //检查是否有回调
    var __EventHander = DispatchOnVideoWallCreatedEventHanderGet( dlg_name );
    if( ! ( typeof(__EventHander)=="undefined" )
        && ( __EventHander != null
            && typeof(__EventHander) === "function" )
    )
    {
        __EventHander( dlg_name, desktop_name  );
    }
}

//视频墙已经关闭
var DispatchOnVideoWallDestroyedEventHanderMap = null;
function DispatchOnVideoWallDestroyedEventHanderSet( dlg_name , _handler  )
{
    if ((typeof (dlg_name) == "undefined")
        || (dlg_name == null)
    ) {
        return ;
    }

    if ((typeof (DispatchOnVideoWallDestroyedEventHanderMap) == "undefined")
        || (DispatchOnVideoWallDestroyedEventHanderMap == null)
    ) {
        DispatchOnVideoWallDestroyedEventHanderMap = new NWMap();
    }

    if ((typeof (_handler) == "undefined")
        || (_handler == null)
        || ! ( typeof(__EventHander) === "function")
    ) {
        DispatchOnVideoWallDestroyedEventHanderMap.remove(dlg_name );
    }
    else{
        DispatchOnVideoWallDestroyedEventHanderMap.put(dlg_name , _handler );
    }
};

function DispatchOnVideoWallDestroyedEventHanderGet( dlg_name  )
{
    if ((typeof (dlg_name) == "undefined")
        || (dlg_name == null)
    ) {
        return null;
    }

    if ((typeof (DispatchOnVideoWallDestroyedEventHanderMap) == "undefined")
        || (DispatchOnVideoWallDestroyedEventHanderMap == null)
    ) {
        return null;
    }

    DispatchOnVideoWallDestroyedEventHanderMap.get(dlg_name );
};

function DispatchOnVideoWallDestroyed(event_parameters) {
    var dlg_name = event_parameters.dlg_name ;
    var desktop_name = event_parameters.desktop_name ;

    //检查是否有回调
    var __EventHander = DispatchOnVideoWallDestroyedEventHanderGet( dlg_name );
    if( ! ( typeof(__EventHander)=="undefined" )
        && ( __EventHander != null
            && typeof(__EventHander) === "function" )
    )
    {
        __EventHander( dlg_name );
    }
}

//视频源已经打开
var DispatchOnVideoChanCreatedEventHanderMap = null;
function DispatchOnVideoChanCreatedEventHanderSet( dlg_name , _handler  )
{
    if ((typeof (dlg_name) == "undefined")
        || (dlg_name == null)
    ) {
        return ;
    }

    if ((typeof (DispatchOnVideoChanCreatedEventHanderMap) == "undefined")
        || (DispatchOnVideoChanCreatedEventHanderMap == null)
    ) {
        DispatchOnVideoChanCreatedEventHanderMap = new NWMap();
    }

    if ((typeof (_handler) == "undefined")
        || (_handler == null)
        || ! ( typeof(__EventHander) === "function")
    ) {
        DispatchOnVideoChanCreatedEventHanderMap.remove(dlg_name );
    }
    else{
        DispatchOnVideoChanCreatedEventHanderMap.put(dlg_name , _handler );
    }

};

function DispatchOnVideoChanCreatedEventHanderGet( dlg_name  )
{
    if ((typeof (dlg_name) == "undefined")
        || (dlg_name == null)
    ) {
        return null;
    }

    if ((typeof (DispatchOnVideoChanCreatedEventHanderMap) == "undefined")
        || (DispatchOnVideoChanCreatedEventHanderMap == null)
    ) {
        return null;
    }

    DispatchOnVideoChanCreatedEventHanderMap.get(dlg_name );
};

function DispatchOnVideoChanCreated(event_parameters) {
    var dlg_name = event_parameters.dlg_name ;
    var video_source_id = event_parameters.video_source_id ;

    //检查是否有回调
    var __EventHander = DispatchOnVideoChanCreatedEventHanderGet( dlg_name );
    if( ! ( typeof(__EventHander)=="undefined" )
        && ( __EventHander != null
            && typeof(__EventHander) === "function" )
    )
    {
        __EventHander( dlg_name , video_source_id );
    }
}


//视频源已经关闭
var DispatchOnVideoChanDestroyedEventHanderMap = null;
function DispatchOnVideoChanDestroyedEventHanderSet( dlg_name , _handler  )
{
    if ((typeof (dlg_name) == "undefined")
        || (dlg_name == null)
    ) {
        return ;
    }

    if ((typeof (DispatchOnVideoChanDestroyedEventHanderMap) == "undefined")
        || (DispatchOnVideoChanDestroyedEventHanderMap == null)
    ) {
        DispatchOnVideoChanDestroyedEventHanderMap = new NWMap();
    }

    if ((typeof (_handler) == "undefined")
        || (_handler == null)
        || ! ( typeof(__EventHander) === "function")
    ) {
        DispatchOnVideoChanDestroyedEventHanderMap.remove(dlg_name );
    }
    else{
        DispatchOnVideoChanDestroyedEventHanderMap.put(dlg_name , _handler );
    }

};

function DispatchOnVideoChanDestroyedEventHanderGet( dlg_name  )
{
    if ((typeof (dlg_name) == "undefined")
        || (dlg_name == null)
    ) {
        return null;
    }

    if ((typeof (DispatchOnVideoChanDestroyedEventHanderMap) == "undefined")
        || (DispatchOnVideoChanDestroyedEventHanderMap == null)
    ) {
        return null;
    }

    DispatchOnVideoChanDestroyedEventHanderMap.get(dlg_name );
};
function DispatchOnVideoChanDestroyed(event_parameters) {
    var dlg_name = event_parameters.dlg_name ;
    var video_source_id = event_parameters.video_source_id ;

    //检查是否有回调
    var __EventHander = DispatchOnVideoChanDestroyedEventHanderGet( dlg_name );
    if( ! ( typeof(__EventHander)=="undefined" )
        && ( __EventHander != null
            && typeof(__EventHander) === "function" )
    )
    {
        __EventHander( dlg_name , video_source_id );
    }
}


///////////////////////////////////////////////////////
//会议回调函数。
///////////////////////////////////////////////////////
//会议已经连接。
var DispatchOnConfConnectedEventHanderMap = null;
function DispatchOnConfConnectedEventHanderSet( dlg_name , _handler  )
{
    if ((typeof (dlg_name) == "undefined")
        || (dlg_name == null)
    ) {
        return ;
    }

    if ((typeof (DispatchOnConfConnectedEventHanderMap) == "undefined")
        || (DispatchOnConfConnectedEventHanderMap == null)
    ) {
        DispatchOnConfConnectedEventHanderMap = new NWMap();
    }

    if ((typeof (_handler) == "undefined")
        || (_handler == null)
        || ! ( typeof(__EventHander) === "function")
    ) {
        DispatchOnConfConnectedEventHanderMap.remove(dlg_name );
    }
    else{
        DispatchOnConfConnectedEventHanderMap.put(dlg_name , _handler );
    }

};

function DispatchOnConfConnectedEventHanderGet( dlg_name  )
{
    if ((typeof (dlg_name) == "undefined")
        || (dlg_name == null)
    ) {
        return null;
    }

    if ((typeof (DispatchOnConfConnectedEventHanderMap) == "undefined")
        || (DispatchOnConfConnectedEventHanderMap == null)
    ) {
        return null;
    }

    DispatchOnConfConnectedEventHanderMap.get(dlg_name );
};
function DispatchOnConfConnected(event_parameters) {
    var dlg_name = event_parameters.dlg_name ;
    var video_source_id = event_parameters.video_source_id ;

    //检查是否有回调
    var __EventHander = DispatchOnConfConnectedEventHanderGet( dlg_name );
    if( ! ( typeof(__EventHander)=="undefined" )
        && ( __EventHander != null
            && typeof(__EventHander) === "function" )
    )
    {
        __EventHander( dlg_name , video_source_id );
    }
}

//会议已经断开。

var DispatchOnConfDisconnectedEventHanderMap = null;
function DispatchOnConfDisconnectedEventHanderSet( dlg_name , _handler  )
{
    if ((typeof (dlg_name) == "undefined")
        || (dlg_name == null)
    ) {
        return ;
    }

    if ((typeof (DispatchOnConfDisconnectedEventHanderMap) == "undefined")
        || (DispatchOnConfDisconnectedEventHanderMap == null)
    ) {
        DispatchOnConfDisconnectedEventHanderMap = new NWMap();
    }

    if ((typeof (_handler) == "undefined")
        || (_handler == null)
        || ! ( typeof(__EventHander) === "function")
    ) {
        DispatchOnConfDisconnectedEventHanderMap.remove(dlg_name );
    }
    else{
        DispatchOnConfDisconnectedEventHanderMap.put(dlg_name , _handler );
    }

};

function DispatchOnConfDisconnectedEventHanderGet( dlg_name  )
{
    if ((typeof (dlg_name) == "undefined")
        || (dlg_name == null)
    ) {
        return null;
    }

    if ((typeof (DispatchOnConfDisconnectedEventHanderMap) == "undefined")
        || (DispatchOnConfDisconnectedEventHanderMap == null)
    ) {
        return null;
    }

    DispatchOnConfDisconnectedEventHanderMap.get(dlg_name );
};

function DispatchOnConfDisconnected(event_parameters) {
    var queue_id = event_parameters.queue_id ;

    //检查是否有回调
    var __EventHander = DispatchOnConfDisconnectedEventHanderGet( queue_id );
    if( ! ( typeof(__EventHander)=="undefined" )
        && ( __EventHander != null
            && typeof(__EventHander) === "function" )
    )
    {
        __EventHander( queue_id );
    }
}


//会议已经解散。

var DispatchOnConfDissolveEventHanderMap = null;
function DispatchOnConfDissolveEventHanderSet( dlg_name , _handler  )
{
    if ((typeof (dlg_name) == "undefined")
        || (dlg_name == null)
    ) {
        return ;
    }

    if ((typeof (DispatchOnConfDissolveEventHanderMap) == "undefined")
        || (DispatchOnConfDissolveEventHanderMap == null)
    ) {
        DispatchOnConfDissolveEventHanderMap = new NWMap();
    }

    if ((typeof (_handler) == "undefined")
        || (_handler == null)
        || ! ( typeof(__EventHander) === "function")
    ) {
        DispatchOnConfDissolveEventHanderMap.remove(dlg_name );
    }
    else{
        DispatchOnConfDissolveEventHanderMap.put(dlg_name , _handler );
    }

};

function DispatchOnConfDissolveEventHanderGet( dlg_name  )
{
    if ((typeof (dlg_name) == "undefined")
        || (dlg_name == null)
    ) {
        return null;
    }

    if ((typeof (DispatchOnConfDissolveEventHanderMap) == "undefined")
        || (DispatchOnConfDissolveEventHanderMap == null)
    ) {
        return null;
    }

    DispatchOnConfDissolveEventHanderMap.get(dlg_name );
};

function DispatchOnConfDissolve(event_parameters) {
    var queue_id = event_parameters.queue_id ;

    //检查是否有回调
    var __EventHander = DispatchOnConfDissolveEventHanderGet( queue_id );
    if( ! ( typeof(__EventHander)=="undefined" )
        && ( __EventHander != null
            && typeof(__EventHander) === "function" )
    )
    {
        __EventHander( queue_id );
    }
}



//收到会议文本消息。

var DispatchOnConfTextInfoEventHanderMap = null;
function DispatchOnConfTextInfoEventHanderSet( dlg_name , _handler  )
{
    if ((typeof (dlg_name) == "undefined")
        || (dlg_name == null)
    ) {
        return ;
    }

    if ((typeof (DispatchOnConfTextInfoEventHanderMap) == "undefined")
        || (DispatchOnConfTextInfoEventHanderMap == null)
    ) {
        DispatchOnConfTextInfoEventHanderMap = new NWMap();
    }

    if ((typeof (_handler) == "undefined")
        || (_handler == null)
        || ! ( typeof(__EventHander) === "function")
    ) {
        DispatchOnConfTextInfoEventHanderMap.remove(dlg_name );
    }
    else{
        DispatchOnConfTextInfoEventHanderMap.put(dlg_name , _handler );
    }

};

function DispatchOnConfTextInfoEventHanderGet( dlg_name  )
{
    if ((typeof (dlg_name) == "undefined")
        || (dlg_name == null)
    ) {
        return null;
    }

    if ((typeof (DispatchOnConfTextInfoEventHanderMap) == "undefined")
        || (DispatchOnConfTextInfoEventHanderMap == null)
    ) {
        return null;
    }

    DispatchOnConfTextInfoEventHanderMap.get(dlg_name );
};

function DispatchOnConfTextInfo(event_parameters) {
    var queue_id = event_parameters.queue_id ;
    var info_text = event_parameters.info_text ;

    //检查是否有回调
    var __EventHander = DispatchOnConfTextInfoEventHanderGet( queue_id );
    if( ! ( typeof(__EventHander)=="undefined" )
        && ( __EventHander != null
            && typeof(__EventHander) === "function" )
    )
    {
        __EventHander( queue_id , info_text );
    }
}


//收到会议成员类型改变消息。

var DispatchOnConfMemberTypeChangedEventHanderMap = null;
function DispatchOnConfMemberTypeChangedEventHanderSet( dlg_name , _handler  )
{
    if ((typeof (dlg_name) == "undefined")
        || (dlg_name == null)
    ) {
        return ;
    }

    if ((typeof (DispatchOnConfMemberTypeChangedEventHanderMap) == "undefined")
        || (DispatchOnConfMemberTypeChangedEventHanderMap == null)
    ) {
        DispatchOnConfMemberTypeChangedEventHanderMap = new NWMap();
    }

    if ((typeof (_handler) == "undefined")
        || (_handler == null)
        || ! ( typeof(__EventHander) === "function")
    ) {
        DispatchOnConfMemberTypeChangedEventHanderMap.remove(dlg_name );
    }
    else{
        DispatchOnConfMemberTypeChangedEventHanderMap.put(dlg_name , _handler );
    }

};

function DispatchOnConfMemberTypeChangedEventHanderGet( dlg_name  )
{
    if ((typeof (dlg_name) == "undefined")
        || (dlg_name == null)
    ) {
        return null;
    }

    if ((typeof (DispatchOnConfMemberTypeChangedEventHanderMap) == "undefined")
        || (DispatchOnConfMemberTypeChangedEventHanderMap == null)
    ) {
        return null;
    }

    DispatchOnConfMemberTypeChangedEventHanderMap.get(dlg_name );
};

function DispatchOnConfMemberTypeChanged(event_parameters) {
    var queue_id = event_parameters.queue_id ;
    var member_id = event_parameters.member_id ;
    var member_name = event_parameters.member_name ;
    var member_type = event_parameters.member_type ;

    //检查是否有回调
    var __EventHander = DispatchOnConfMemberTypeChangedEventHanderGet( queue_id );
    if( ! ( typeof(__EventHander)=="undefined" )
        && ( __EventHander != null
            && typeof(__EventHander) === "function" )
    )
    {
        __EventHander( queue_id , member_id ,member_name,member_type );
    }
}


//收到会议成员状态改变消息。

var DispatchOnConfMemberStatusChangedEventHanderMap = null;
function DispatchOnConfMemberStatusChangedEventHanderSet( dlg_name , _handler  )
{
    if ((typeof (dlg_name) == "undefined")
        || (dlg_name == null)
    ) {
        return ;
    }

    if ((typeof (DispatchOnConfMemberStatusChangedEventHanderMap) == "undefined")
        || (DispatchOnConfMemberStatusChangedEventHanderMap == null)
    ) {
        DispatchOnConfMemberStatusChangedEventHanderMap = new NWMap();
    }

    if ((typeof (_handler) == "undefined")
        || (_handler == null)
        || ! ( typeof(__EventHander) === "function")
    ) {
        DispatchOnConfMemberStatusChangedEventHanderMap.remove(dlg_name );
    }
    else{
        DispatchOnConfMemberStatusChangedEventHanderMap.put(dlg_name , _handler );
    }

};

function DispatchOnConfMemberStatusChangedEventHanderGet( dlg_name  )
{
    if ((typeof (dlg_name) == "undefined")
        || (dlg_name == null)
    ) {
        return null;
    }

    if ((typeof (DispatchOnConfMemberStatusChangedEventHanderMap) == "undefined")
        || (DispatchOnConfMemberStatusChangedEventHanderMap == null)
    ) {
        return null;
    }

    DispatchOnConfMemberStatusChangedEventHanderMap.get(dlg_name );
};

function DispatchOnConfMemberStatusChanged(event_parameters) {
    var queue_id = event_parameters.queue_id ;
    var member_id = event_parameters.member_id ;
    var member_name = event_parameters.member_name ;
    var member_status = event_parameters.member_status ;


    //检查是否有回调
    var __EventHander = DispatchOnConfMemberStatusChangedEventHanderGet( queue_id );
    if( ! ( typeof(__EventHander)=="undefined" )
        && ( __EventHander != null
            && typeof(__EventHander) === "function" )
    )
    {
        __EventHander( queue_id , member_id ,member_name,member_status );
    }
}


//////////////////////////////////////////////////////////////////////////////
//初始化组件。
//////////////////////////////////////////////////////////////////////////////

//功能：初始化组件
function DispatchInit(
    _OnLogOutMessageFunction
    , _OnLoginStatusFunction
    ,_OnPagerMessageFunction        //接收文本消息。
    , _OnPagerMessageResponseFunction      //接收文本消息发送结果。
    , _OnCallIncomeFunction           //接收客户端呼入事件。
    , _OnHostStatusEventHander        //接收Host状态事件。
)
{
    DispatchLogOutMessageHander = _OnLogOutMessageFunction ;
    DispatchRegisterEventHander = _OnLoginStatusFunction ;
    DispatchPagerMessageEventHander = _OnPagerMessageFunction ;
    DispatchPagerMessageResponseEventHander = _OnPagerMessageResponseFunction ;
    DispatchCallIncomeEvenHander = _OnCallIncomeFunction
    DispatchHostStatusEventHander = _OnHostStatusEventHander ;

    DispatchLogOutMessage("DispatchInit...");
    DispatchCmdMsgSend(DispatchExtensionId , { operator: "connect" });
    DispatchLogOutMessage("DispatchInit End...");
}

//功能：卸载组件
function DispatchUnInit() {
    DispatchClientShutdown();
    DispatchRegisterEventHander = null ;
    DispatchLogOutMessageHander = null ;
    DispatchLogOutMessage("DispatchUnInit...");
    DispatchCmdMsgSend(DispatchExtensionId , { operator: "disconnect" });
    DispatchLogOutMessage("DispatchUnInit End...");
}


//////////////////////////////////////////////////////////////////////////////
//组件属性相关操作。
//////////////////////////////////////////////////////////////////////////////

//设置组件属性
function DispatchAttributeSet(attribute_name, attribute_value, response_callback )
{
    nCoCallRequestID = nCoCallRequestID + 1 ;
    var opera_object = {
        [COCALL_HOST_MSG_ID] : nCoCallRequestID
        , [COCALL_HOST_MSG_TYPE]: COCALL_HOST_MSG_TYPE_REQUEST_ATTRIBUTE_SET
        , [COCALL_HOST_ATTRIBUTE_NAME] : attribute_name
        , [COCALL_HOST_ATTRIBUTE_VALUE]:attribute_value};

    //是否传入回调。
    if( ! ( typeof(response_callback)=="undefined" )
        && ( response_callback != null
            && typeof(response_callback) === "function" )
    )
    {
        DispatchCmdMsgResponseCallbackMap.put( nCoCallRequestID , response_callback );
    }

    DispatchCmdMsgSend(DispatchExtensionId , opera_object);

    DispatchLogOutMessage("DispatchAttributeSet : " + JSON.stringify(opera_object)  );
}

//设置组件属性的返回接口。
function  DispatchOnAttrSetResponse(  host_msg_id , response_code , response_info , original_msg ) {

    if ( response_code == 0  )
    {
        DispatchLogOutMessage("Run attribute set " + JSON.stringify( original_msg[COCALL_HOST_ATTRIBUTE_NAME] ) + " success !"
            +  "response info Is :" + JSON.stringify(response_info)  );
    }
    else
    {
        DispatchLogOutMessage("Run attribute set " + JSON.stringify( original_msg[COCALL_HOST_ATTRIBUTE_NAME] )
            + " error , Code Is :" + JSON.stringify(response_code)
            + "response info Is :" + JSON.stringify(response_info)  );
    }
};

//获取组件属性
function DispatchAttributeGet(attribute_name , response_callback)
{
    nCoCallRequestID = nCoCallRequestID + 1 ;
    var opera_object = {
        [COCALL_HOST_MSG_ID] : nCoCallRequestID
        , [COCALL_HOST_MSG_TYPE]: COCALL_HOST_MSG_TYPE_REQUEST_ATTRIBUTE_GET
        , [COCALL_HOST_ATTRIBUTE_NAME] : attribute_name
    };

    //是否传入回调。
    if( ! ( typeof(response_callback)=="undefined" )
        && ( response_callback != null
            && typeof(response_callback) === "function" )
    )
    {
        DispatchCmdMsgResponseCallbackMap.put( nCoCallRequestID , response_callback );
    }


    DispatchCmdMsgSend(DispatchExtensionId , opera_object);

    DispatchLogOutMessage("DispatchAttributeGet : " + JSON.stringify(opera_object)  );
}

//获取组件属性的返回接口。
function  DispatchOnAttrGetResponse( host_msg_id , response_code , response_info , original_msg ){
    if ( response_code == 0  )
    {
        DispatchLogOutMessage("Run attribute get " + JSON.stringify( original_msg[COCALL_HOST_ATTRIBUTE_NAME] ) + " success !"
            +  "attribute value Is :" + JSON.stringify(response_info)  );
    }
    else
    {
        DispatchLogOutMessage("Run attribute get " + JSON.stringify( original_msg[COCALL_HOST_ATTRIBUTE_NAME] )
            + " error , Code Is :" + JSON.stringify(response_code)
            + "attribute value Is :" + JSON.stringify(response_info)  );
    }
};


//////////////////////////////////////////////////////////////////////
//组件功能调用
/////////////////////////////////////////////////////////////////////////

//发送一个功能调用请求函数
function DispatchFunctionRun(function_name, function_parameters , response_callback ) {

    nCoCallRequestID = nCoCallRequestID + 1 ;

    if ( function_parameters != null )
    {
        var opera_object = {
            [COCALL_HOST_MSG_ID] : nCoCallRequestID
            , [COCALL_HOST_MSG_TYPE]: COCALL_HOST_MSG_TYPE_REQUEST_FUNCTION
            , [COCALL_HOST_FUNCTION_NAME]: function_name
            , [COCALL_HOST_FUNCTION_PARAMETERS]: function_parameters
        };
        DispatchCmdMsgSend(DispatchExtensionId , opera_object);
    }
    else
    {
        var opera_object = {
            [COCALL_HOST_MSG_ID] : nCoCallRequestID
            , [COCALL_HOST_MSG_TYPE]: COCALL_HOST_MSG_TYPE_REQUEST_FUNCTION
            , [COCALL_HOST_FUNCTION_NAME]: function_name
        };
        DispatchCmdMsgSend(DispatchExtensionId , opera_object);
    }

    //是否传入回调。
    if( ! ( typeof(response_callback)=="undefined" )
        && ( response_callback != null
            && typeof(response_callback) === "function" )
    )
    {
        DispatchCmdMsgResponseCallbackMap.put( nCoCallRequestID , response_callback );
    }

    DispatchLogOutMessage("DispatchFunctionRun : " + JSON.stringify(opera_object)  );
}

//功能请求结果回调函数。
function  DispatchOnFunctionResponse(  host_msg_id , response_code , response_info , original_msg ) {

    if ( response_code == 0  )
    {
        DispatchLogOutMessage("Run funciton " + JSON.stringify( original_msg[COCALL_HOST_FUNCTION_NAME] ) + " success !"
            +  "response info Is :" + JSON.stringify(response_info)  );
    }
    else
    {
        DispatchLogOutMessage("Run funciton " + JSON.stringify( original_msg[COCALL_HOST_FUNCTION_NAME] )
            + " error , Code Is :" + JSON.stringify(response_code)
            + "response info Is :" + JSON.stringify(response_info)  );
    }
};


//设置注册状态通知回调函数。
function DispatchRegisterEventHanderSet(  _OnLoginStatusFunction ) {
    DispatchRegisterEventHander = _OnLoginStatusFunction ;
}

//注册到服务器。
function DispatchRegister(response_callback) {
    DispatchFunctionRun( COCALL_HOST_FUNCTION_NAME_REGISTER , null , response_callback );
}

//从服务器注销。
function DispatchUnRegister(response_callback) {
    DispatchFunctionRun( COCALL_HOST_FUNCTION_NAME_UNREGISTER  , null , response_callback );
}

//关闭组件。
function DispatchClientShutdown() {
    DispatchFunctionRun( COCALL_HOST_FUNCTION_NAME_SHUTDOWN  , null , null  );
}

//////////////////////////////////////////////////////////////////////
//点对点通信功能
/////////////////////////////////////////////////////////////////////////

//发起呼叫
function DispatchMakeCall( chan_id  , called_number,response_callback) {
    var function_parameters = {
        [COCALL_HOST_FUNCTION_PARAMETER_CHAN_ID]: chan_id
        , [COCALL_HOST_FUNCTION_PARAMETER_CALLED_NUMBER] : called_number
    };
    DispatchFunctionRun( COCALL_HOST_FUNCTION_NAME_MAKECALL  , function_parameters ,response_callback);
}

//结束呼叫
function DispatchDropCall( chan_id ,response_callback) {
    var function_parameters = {
        [COCALL_HOST_FUNCTION_PARAMETER_CHAN_ID]: chan_id
    };
    DispatchFunctionRun( COCALL_HOST_FUNCTION_NAME_DROPCALL  , function_parameters  ,response_callback );
}

//应答呼入。
function DispatchAnswerCall( chan_id  ,response_callback) {
    var function_parameters = {
        [COCALL_HOST_FUNCTION_PARAMETER_CHAN_ID]: chan_id
    };
    DispatchFunctionRun( COCALL_HOST_FUNCTION_NAME_ANSWERCALL  , function_parameters  ,response_callback);
}

//发送一个DTMF按键。
function DispatchSendDTMF(chan_id  , dtmf_value  ,response_callback) {
    var function_parameters = {
        [COCALL_HOST_FUNCTION_PARAMETER_CHAN_ID]: chan_id
        , COCALL_HOST_FUNCTION_PARAMETER_DTMF_VALUE : dtmf_value
    };
    DispatchFunctionRun( COCALL_HOST_FUNCTION_NAME_SENDDTMF  , function_parameters  ,response_callback);
}

//发送一个INFO TEXT
function DispatchSendInfoText(chan_id  , info_type ,info_text  ,response_callback) {
    var function_parameters = {
        [COCALL_HOST_FUNCTION_PARAMETER_CHAN_ID]: chan_id
        , [COCALL_HOST_FUNCTION_PARAMETER_INFO_TYPE] : info_type
        , [COCALL_HOST_FUNCTION_PARAMETER_INFO_TEXT] : info_text
    };
    DispatchFunctionRun( COCALL_HOST_FUNCTION_NAME_SENDINFOTEXT  , function_parameters  ,response_callback);
}

//发送一个INFO COMMAND
function DispatchSendInfoCommand( chan_id  ,cmd_id , cmd_type, cmd_info, lparm_info , rparm_info ,from_id , to_id  ,response_callback) {
    var function_parameters = {
        [COCALL_HOST_FUNCTION_PARAMETER_CHAN_ID]: chan_id
        , [COCALL_HOST_FUNCTION_PARAMETER_INFO_CMD_ID] : cmd_id
        , [COCALL_HOST_FUNCTION_PARAMETER_INFO_CMD_TYPE] : cmd_type
        , [COCALL_HOST_FUNCTION_PARAMETER_INFO_CMD_INFO] : cmd_info
        , [COCALL_HOST_FUNCTION_PARAMETER_INFO_LPARM_INFO] : lparm_info
        , [COCALL_HOST_FUNCTION_PARAMETER_INFO_RPARM_INFO] : rparm_info
        , [COCALL_HOST_FUNCTION_PARAMETER_INFO_FROM_ID] : from_id
        , [COCALL_HOST_FUNCTION_PARAMETER_INFO_TO_ID] : to_id
    };
    DispatchFunctionRun( COCALL_HOST_FUNCTION_NAME_SENDINFOCOMMAND  , function_parameters  ,response_callback);
}

function DispatchSendInfoCommandRsp( chan_id  ,info_type ,info_text  ,response_callback) {
    var function_parameters = {
        [COCALL_HOST_FUNCTION_PARAMETER_CHAN_ID]: chan_id
        , [COCALL_HOST_FUNCTION_PARAMETER_INFO_CMD_ID] : cmd_id
        , [COCALL_HOST_FUNCTION_PARAMETER_INFO_RSP_CODE] : rsp_code
        , [COCALL_HOST_FUNCTION_PARAMETER_INFO_RSP_INFO] : rsp_info
    };
    DispatchFunctionRun( COCALL_HOST_FUNCTION_NAME_SENDINFOCOMMANDRSP  , function_parameters  ,response_callback);
}

function DispatchSendInfoNotify( chan_id  ,user_id , user_name , info_type, info , lparm , rparam ,response_callback ) {
    var function_parameters = {
        [COCALL_HOST_FUNCTION_PARAMETER_CHAN_ID]: chan_id
        , [COCALL_HOST_FUNCTION_PARAMETER_INFO_USER_ID] : user_id
        , [COCALL_HOST_FUNCTION_PARAMETER_INFO_USER_NAME] : user_name
        , [COCALL_HOST_FUNCTION_PARAMETER_INFO_TYPE] : info_type
        , [COCALL_HOST_FUNCTION_PARAMETER_INFO_INFO] : info
        , [COCALL_HOST_FUNCTION_PARAMETER_INFO_LPARM] : lparm
        , [COCALL_HOST_FUNCTION_PARAMETER_INFO_RPARAM] : rparam
    };
    DispatchFunctionRun( COCALL_HOST_FUNCTION_NAME_SENDINFONOTIFY  , function_parameters  ,response_callback);
}

function DispatchSendPagerMessage( message_subject, message_text , send_to  ,response_callback) {
    var function_parameters = {
        [COCALL_HOST_FUNCTION_PARAMETER_INFO_MESSAGE_SUBJECT] : message_subject
        , [COCALL_HOST_FUNCTION_PARAMETER_INFO_MESSAGE_TEXT] : message_text
        , [COCALL_HOST_FUNCTION_PARAMETER_INFO_SEND_TO] : send_to
    };
    DispatchFunctionRun( COCALL_HOST_FUNCTION_NAME_SENDPAGERMESSAGE  , function_parameters  ,response_callback);
}


//////////////////////////////////////////////////////////////////////
//视频功能
/////////////////////////////////////////////////////////////////////////

//创建一个新的视频墙。
function DispatchVideoWallCreate( dlg_caption, dlg_name , desktop_name  ,extern_parameters ,response_callback ) {
    var function_parameters = {
        [COCALL_HOST_FUNCTION_PARAMETER_DIALOG_CAPTION] : dlg_caption
        , [COCALL_HOST_FUNCTION_PARAMETER_DIALOG_NAME] : dlg_name
        , [COCALL_HOST_FUNCTION_PARAMETER_DESKTOP_NAME] : desktop_name
        , [COCALL_HOST_FUNCTION_EXTERN_PARAMETERS] : extern_parameters
    };
    DispatchFunctionRun( COCALL_HOST_FUNCTION_NAME_VIDEOWALL_CREATE  , function_parameters  ,response_callback);
}

//释放一个视频墙。
function DispatchVideoWallDestroy( dlg_name  ,response_callback) {
    var function_parameters = {
        [COCALL_HOST_FUNCTION_PARAMETER_DIALOG_NAME] : dlg_name
    };
    DispatchFunctionRun( COCALL_HOST_FUNCTION_NAME_VIDEOWALL_DESTROY  , function_parameters  ,response_callback);
}

//设置一个视频墙的显示方式
function DispatchVideoWallSetShowInfo( dlg_name, desktop_name ,  full_screen
    ,dlg_left,dlg_top,dlg_width, dlg_height
    ,response_callback ) {
    var function_parameters = {
        [COCALL_HOST_FUNCTION_PARAMETER_DIALOG_NAME] : dlg_name
        , [COCALL_HOST_FUNCTION_PARAMETER_DESKTOP_NAME] : desktop_name
        , [COCALL_HOST_FUNCTION_PARAMETER_FULL_SCREEN] : full_screen
        , [COCALL_HOST_FUNCTION_PARAMETER_DIALOG_LEFT] : dlg_left
        , [COCALL_HOST_FUNCTION_PARAMETER_DIALOG_TOP] : dlg_top
        , [COCALL_HOST_FUNCTION_PARAMETER_DIALOG_WIDTH] : dlg_width
        , [COCALL_HOST_FUNCTION_PARAMETER_DIALOG_HEIGHT] : dlg_height
    };
    DispatchFunctionRun( COCALL_HOST_FUNCTION_NAME_VIDEOWALL_SET_SHOWINFO  , function_parameters  ,response_callback);
}



//关闭所有视频墙
function DispatchVideoWallClear( response_callback) {
    DispatchFunctionRun( COCALL_HOST_FUNCTION_NAME_VIDEOWALL_CLEAR  , null  ,response_callback);
}


//新加一个视频窗口
function DispatchVideoChanAdd( dlg_name, video_source_type ,  video_source_id , video_source_name ,parameter_extern  ,response_callback ) {
    var function_parameters = {
        [COCALL_HOST_FUNCTION_PARAMETER_DIALOG_NAME]            : dlg_name
        , [COCALL_HOST_FUNCTION_PARAMETER_VIDEO_SOURCE_TYPE]    : video_source_type
        , [COCALL_HOST_FUNCTION_PARAMETER_VIDEO_SOURCE_ID]      : video_source_id
        , [COCALL_HOST_FUNCTION_PARAMETER_VIDEO_SOURCE_NAME]    : video_source_name
        , [COCALL_HOST_FUNCTION_PARAMETER_PARAMETER_EXTERN]     : parameter_extern
    };
    DispatchFunctionRun( COCALL_HOST_FUNCTION_NAME_VIDEOCHAN_ADD  , function_parameters  ,response_callback);
}

//移除一个视频窗口
function DispatchVideoChanRemove( dlg_name,  video_source_id , parameter_extern  ,response_callback ) {
    var function_parameters = {
        [COCALL_HOST_FUNCTION_PARAMETER_DIALOG_NAME]            : dlg_name
        , [COCALL_HOST_FUNCTION_PARAMETER_VIDEO_SOURCE_ID]      : video_source_id
        , [COCALL_HOST_FUNCTION_PARAMETER_PARAMETER_EXTERN]     : parameter_extern
    };
    DispatchFunctionRun( COCALL_HOST_FUNCTION_NAME_VIDEOCHAN_REMOVE  , function_parameters  ,response_callback );
}


//设置一个视频窗口最大化
function DispatchVideoChanSetMax( dlg_name,  video_source_id , show_max  ,response_callback ) {
    var function_parameters = {
        [COCALL_HOST_FUNCTION_PARAMETER_DIALOG_NAME]            : dlg_name
        , [COCALL_HOST_FUNCTION_PARAMETER_VIDEO_SOURCE_ID]      : video_source_id
        , [COCALL_HOST_FUNCTION_PARAMETER_SHOW_MAX]             : show_max
    };
    DispatchFunctionRun( COCALL_HOST_FUNCTION_NAME_VIDEOCHAN_SET_MAX  , function_parameters  ,response_callback);
}


//设置一个视频窗口全屏化
function DispatchVideoChanSetFullScreen( dlg_name,  video_source_id , full_screen  ,response_callback ) {
    var function_parameters = {
        [COCALL_HOST_FUNCTION_PARAMETER_DIALOG_NAME]            : dlg_name
        , [COCALL_HOST_FUNCTION_PARAMETER_VIDEO_SOURCE_ID]      : video_source_id
        , [COCALL_HOST_FUNCTION_PARAMETER_FULL_SCREEN]          : full_screen
    };
    DispatchFunctionRun( COCALL_HOST_FUNCTION_NAME_VIDEOCHAN_SET_FULL_SCREEN  , function_parameters ,response_callback );
}


//关闭视频墙上的全部视频窗口
function DispatchVideoChanClean( dlg_name ,response_callback ) {
    var function_parameters = {
        [COCALL_HOST_FUNCTION_PARAMETER_DIALOG_NAME]            : dlg_name
    };
    DispatchFunctionRun( COCALL_HOST_FUNCTION_NAME_VIDEOCHAN_CLEAN  , function_parameters ,response_callback);
}

//重新排列视频墙上的全部视频窗口
function DispatchVideoChansRearrange( dlg_name , split_screen_count ,response_callback ) {
    var function_parameters = {
        [COCALL_HOST_FUNCTION_PARAMETER_DIALOG_NAME]            : dlg_name
        , [COCALL_HOST_FUNCTION_PARAMETER_SPLIT_SCREEN_COUNT]            : split_screen_count
    };
    DispatchFunctionRun( COCALL_HOST_FUNCTION_NAME_VIDEOCHAN_REARRANGE  , function_parameters  ,response_callback);
};


//////////////////////////////////////////////////////////////////////
//会议功能
/////////////////////////////////////////////////////////////////////////

//创建一个会议室
function DispatchRoomCreate(queue_id, room_caption,response_callback) {
    var function_parameters = {
        [COCALL_HOST_FUNCTION_PARAMETER_QUEUE_ID]            : queue_id
        , [COCALL_HOST_FUNCTION_PARAMETER_DIALOG_CAPTION]    : room_caption
    };
    DispatchFunctionRun( COCALL_HOST_FUNCTION_NAME_ROOMCREATE  , function_parameters  ,response_callback);
};

//释放一个会议室
function  DispatchRoomDissolve( queue_id  ,response_callback) {
    var function_parameters = {
        [COCALL_HOST_FUNCTION_PARAMETER_QUEUE_ID]            : queue_id
    };
    DispatchFunctionRun( COCALL_HOST_FUNCTION_NAME_ROOMDISSOLVE  , function_parameters ,response_callback );
};

//获取一个会议室的属性
function DispatchRoomAttributeGet(queue_id, attribute_name, response_callback) {
    var function_parameters = {
        [COCALL_HOST_FUNCTION_PARAMETER_QUEUE_ID]: queue_id
        , [COCALL_HOST_ATTRIBUTE_NAME]: attribute_name
    };
    DispatchFunctionRun(COCALL_HOST_FUNCTION_NAME_ROOMATTRIBUTEGET, function_parameters, response_callback);
};

//邀请一个会议成员
function  DispatchMemberInvite( queue_id ,member_id, member_type ,response_callback) {
    var function_parameters = {
        [COCALL_HOST_FUNCTION_PARAMETER_QUEUE_ID]            : queue_id
        ,[COCALL_HOST_FUNCTION_PARAMETER_MEMBER_ID]          : member_id
        ,[COCALL_HOST_FUNCTION_PARAMETER_MEMBER_TYPE]        : member_type
    };
    DispatchFunctionRun( COCALL_HOST_FUNCTION_NAME_MEMBER_INVITE  , function_parameters ,response_callback );
};


//剔除一个会议成员
function  DispatchMemberKickOut( queue_id ,member_id, kick_info ,response_callback) {
    var function_parameters = {
        [COCALL_HOST_FUNCTION_PARAMETER_QUEUE_ID]            : queue_id
        ,[COCALL_HOST_FUNCTION_PARAMETER_MEMBER_ID]          : member_id
        ,[COCALL_HOST_FUNCTION_PARAMETER_KICK_INFO]        : kick_info
    };
    DispatchFunctionRun( COCALL_HOST_FUNCTION_NAME_MEMBER_KICKOUT  , function_parameters  ,response_callback);
};

//设置一个会议成员类型
function  DispatchMemberTypeSet( queue_id ,member_id, member_type ,response_callback) {
    var function_parameters = {
        [COCALL_HOST_FUNCTION_PARAMETER_QUEUE_ID]            : queue_id
        ,[COCALL_HOST_FUNCTION_PARAMETER_MEMBER_ID]          : member_id
        ,[COCALL_HOST_FUNCTION_PARAMETER_MEMBER_TYPE]        : member_type
    };
    DispatchFunctionRun( COCALL_HOST_FUNCTION_NAME_MEMBER_TYPESET  , function_parameters ,response_callback );
};
//*/


////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////
//web 页面操作
////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////

//注册接收组件事件。
window.addEventListener("message", function(event) {
    // We only accept messages from ourselves
    if (event.source != window)
        return;

    if (event.data.type && (event.data.type == "host_msg")) {
        var host_msg = event.data.msg ;
        if ( host_msg == null )
        {
            DispatchLogOutMessage("received host msg is null "  );
            return ;
        }

        DispatchLogOutMessage("received host msg : " + JSON.stringify(host_msg)  );

        var host_msg_type = host_msg.host_msg_type;
        if (host_msg_type  == null )
        {
            DispatchLogOutMessage("received host msg is unknow type! " );
            return ;
        }

        if ( host_msg_type == COCALL_HOST_MSG_TYPE_RESPONSE )
        {
            //检查是否有回调
            var callback = DispatchCmdMsgResponseCallbackMap.get( host_msg.host_msg_id );
            if( ! ( typeof(callback)=="undefined" )
                && ( callback != null
                    && typeof(callback) === "function" )
            )
            {
                callback( host_msg.response_code , host_msg.response_info , host_msg.original_msg  );
                DispatchCmdMsgResponseCallbackMap.remove( host_msg.host_msg_id );
            }

            DispatchOnMessageResponse( host_msg.host_msg_id , host_msg.response_code , host_msg.response_info , host_msg.original_msg  );
            return ;
        }
        else if ( host_msg_type == COCALL_HOST_MSG_TYPE_EVENT  )
        {
            DispatchLogOutMessage("received one host event msg ");
            var event_name = host_msg.event_name ;
            if ( event_name == null )
            {
                DispatchLogOutMessage("received one host event msg's name is null ");
                return ;
            }

            DispatchLogOutMessage("received one host event msg name is " + JSON.stringify(event_name) );

            var event_parameters = host_msg.event_parameters ;
            if( event_parameters == null )
            {
                DispatchLogOutMessage("host event msg's parameters is null ");
                return ;
            }

            DispatchLogOutMessage("received one host event msg parameters is " + JSON.stringify(event_parameters) );

            if ( event_name == COCALL_HOST_EVENT_NAME_ONCOCALLMSG )
            {
                DispatchOnCoCallMsg( event_parameters ) ;
            }
            else if ( event_name == COCALL_HOST_EVENT_NAME_ONINFOCOMMAND )
            {
                DispatchOnInfoCommand( event_parameters );
            }
            else if ( event_name == COCALL_HOST_EVENT_NAME_ONINFOCOMMANDRSP )
            {
                DispatchOnInfoCommandRsp( event_parameters );
            }
            else if ( event_name == COCALL_HOST_EVENT_NAME_ONINFONOTIFY )
            {
                DispatchOnInfoNotify( event_parameters );
            }
            //视频墙事件。
            else if ( event_name == COCALL_HOST_EVENT_NAME_ONVIDEOWALLCREATED )
            {
                DispatchOnVideoWallCreated( event_parameters );
            }
            else if ( event_name == COCALL_HOST_EVENT_NAME_ONVIDEOWALLDESTROYED )
            {
                DispatchOnVideoWallDestroyed( event_parameters );
            }
            else if ( event_name == COCALL_HOST_EVENT_NAME_ONVIDEOCHANCREATED )
            {
                DispatchOnVideoChanCreated( event_parameters );
            }
            else if ( event_name == COCALL_HOST_EVENT_NAME_ONVIDEOCHANDESTROYED )
            {
                DispatchOnVideoChanDestroyed( event_parameters );
            }
            //会议事件。
            else if ( event_name == COCALL_HOST_EVENT_NAME_ONCONFCONNECTED )
            {
                DispatchOnConfConnected( event_parameters );
            }
            else if ( event_name == COCALL_HOST_EVENT_NAME_ONCONFDISCONNECTED )
            {
                DispatchOnConfDisconnected( event_parameters );
            }
            else if ( event_name == COCALL_HOST_EVENT_NAME_ONCONFDISSOLVE )
            {
                DispatchOnConfDissolve( event_parameters );
            }
            else if ( event_name == COCALL_HOST_EVENT_NAME_ONCONFTEXTINFO )
            {
                DispatchOnConfTextInfo( event_parameters );
            }
            else if ( event_name == COCALL_HOST_EVENT_NAME_ONCONFMEMBERTYPECHANGED )
            {
                DispatchOnConfMemberTypeChanged( event_parameters );
            }
            else if ( event_name == COCALL_HOST_EVENT_NAME_ONCONFMEMBERSTATUSCHANGED )
            {
                DispatchOnConfMemberStatusChanged( event_parameters );
            }

            return ;
        }
    }
}, false );

//接收组件的事件。
document.addEventListener('DispatchResponseEvent', function(response)
{
    if ( response== null ) return ;
    if ( response.detail == null ) return ;
    DispatchLogOutMessage("Recv DispatchResponseEvent:" + JSON.stringify(response.detail) );
}, false);
