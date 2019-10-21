/////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////
// Copyright 2017-2018 CoCall.Net

//功能：实现 Dispatch 会议功能的 JS 操作部分

/////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////

//保存视频会议室的组件实例（IE）
var mDispatchRoomInstance = null;

//保存视频会议室的ID。（Chrome + IE ）
var mDispatchRoomID = null;


//创建视频会议室
function RoomCreate( _DispatchRoomInstance ,room_caption ) {
    try {

        //创建一个新的ID，用于标识这个视频会议室。
        mDispatchRoomID = generateUUID();

        //IE浏览器。
        if (BrowserTypeInfo == "ie") {

            //是否传入了COM组件实例。
            if (_DispatchRoomInstance != null
                && typeof (_DispatchRoomInstance) != "undefined") {
                mDispatchRoomInstance = _DispatchRoomInstance;
            }

            //检查组件实例是否可用。
            if (mDispatchRoomInstance == null
                || typeof (mDispatchRoomInstance) == "undefined") {
                alert("没有初始化COM组件模块");
                return;
            }

            //创建视频墙。
            mDispatchRoomInstance.RoomCreate(mDispatchRoomID);
            if (mDispatchRoomInstance.last_error_code != 0) {
                alert("创建视频会议室 失败");
                return;
            }

            return ;
        }
        //Chrome浏览器。
        else if (BrowserTypeInfo == "chrome") {

            //设置回调。
            DispatchOnConfConnectedEventHanderSet(mDispatchRoomID,OnRoomConfConnected );
            DispatchOnConfDisconnectedEventHanderSet(mDispatchRoomID, OnRoomConfDisconnected );
            DispatchOnConfDissolveEventHanderSet(mDispatchRoomID, OnRoomConfDissolve);
            DispatchOnConfTextInfoEventHanderSet(mDispatchRoomID, OnRoomConfTextInfo );
            DispatchOnConfMemberTypeChangedEventHanderSet(mDispatchRoomID, OnRoomMemberTypeChanged);
            DispatchOnConfMemberStatusChangedEventHanderSet(mDispatchRoomID, OnRoomMemberStatusChanged );

            DispatchRoomCreate(mDispatchRoomID
                , room_caption
                , function (response_code, response_info, original_msg) {
                    //回调接收返回结果
                    if (response_code != 0) {
                        alert("创建视频会议室 失败");
                        mDispatchRoomID = null;

                        //取消设置回调。
                        DispatchOnConfConnectedEventHanderSet(mDispatchRoomID, null);
                        DispatchOnConfDisconnectedEventHanderSet(mDispatchRoomID, null);
                        DispatchOnConfDissolveEventHanderSet(mDispatchRoomID, null);
                        DispatchOnConfTextInfoEventHanderSet(mDispatchRoomID, null);
                        DispatchOnConfMemberTypeChangedEventHanderSet(mDispatchRoomID, null);
                        DispatchOnConfMemberStatusChangedEventHanderSet(mDispatchRoomID, null);
                    }
                    else {

                    }
                }
            ) ;


        }

    }
    catch (e) {
    }
}

//解散调度室
function RoomDissolve(  ) {
    try {

        //IE浏览器。
        if (BrowserTypeInfo == "ie") {

            //检查组件实例是否可用。
            if (mDispatchRoomInstance == null
                || typeof (mDispatchRoomInstance) == "undefined") {
                alert("没有初始化COM组件模块");
                return;
            }

            //解散调度室。
            mDispatchRoomInstance.RoomDissolve();
            if (mDispatchRoomInstance.last_error_code != 0) {
                alert("解散调度室 失败");
                return;
            }

            return ;
        }
        //Chrome浏览器。
        else if (BrowserTypeInfo == "chrome") {

            if (mDispatchRoomID == null
                || typeof (mDispatchRoomID) == "undefined") {
                return;
            }

            DispatchRoomDissolve(mDispatchRoomID
                , function (response_code, response_info, original_msg) {
                    //回调接收返回结果
                    if (response_code != 0) {
                        alert("解散调度室 失败");
                        mDispatchRoomID = null ;
                    }
                    else {
                        //取消设置回调。
                        DispatchOnConfConnectedEventHanderSet(mDispatchRoomID, null);
                        DispatchOnConfDisconnectedEventHanderSet(mDispatchRoomID, null);
                        DispatchOnConfDissolveEventHanderSet(mDispatchRoomID, null);
                        DispatchOnConfTextInfoEventHanderSet(mDispatchRoomID, null);
                        DispatchOnConfMemberTypeChangedEventHanderSet(mDispatchRoomID, null);
                        DispatchOnConfMemberStatusChangedEventHanderSet(mDispatchRoomID, null);
                    }
                }
            ) ;


        }

    }
    catch (e) {
    }
}

var COCALL_HOST_ATTRIBUTE_NAME_ROOM_QUEUE_ID = "room_queue_id";
var COCALL_HOST_ATTRIBUTE_NAME_ROOM_SIP_NUMBER = "room_sip_number";
var COCALL_HOST_ATTRIBUTE_NAME_ROOM_STATUS = "room_status";

//获取会议属性。
function RoomAttributeGet(attribute_name, response_callback) {
    try {

        //开启IE的调试模式.
        // debugger;
        //IE浏览器。
        if (BrowserTypeInfo == "ie") {


            //检查组件实例是否可用。
            if (mDispatchRoomInstance == null
                || typeof (mDispatchRoomInstance) == "undefined") {
                alert("没有初始化COM组件模块");
                return;
            }

            //
            if (response_callback == null
                || typeof (response_callback) == "undefined") {
                alert("回调函数为空");
                return;
            }

            //获取属性。
            if (attribute_name == COCALL_HOST_ATTRIBUTE_NAME_ROOM_QUEUE_ID) {
                response_callback(0, mDispatchRoomInstance.room_queue_id, null);
            }
            else if (attribute_name == COCALL_HOST_ATTRIBUTE_NAME_ROOM_SIP_NUMBER) {
                response_callback(0, mDispatchRoomInstance.room_sip_number, null);
            }
            else if (attribute_name == COCALL_HOST_ATTRIBUTE_NAME_ROOM_STATUS) {
                response_callback(0, mDispatchRoomInstance.room_status , null);
            }
            else {
                response_callback(-1, "不支持的参数", null);
            }

            return;
        }
        //Chrome浏览器。
        else if (BrowserTypeInfo == "chrome") {

            if (mDispatchRoomID == null
                || typeof (mDispatchRoomID) == "undefined") {
                alert("没有创建会议");
                return;
            }

            if (response_callback == null
                || typeof (response_callback) == "undefined") {
                alert("回调函数为空");
                return;
            }

            DispatchRoomAttributeGet(mDispatchRoomID, attribute_name, response_callback);
        }

    }
    catch (e) {
    }
}


//邀请一个调度成员
function MemberInvite( member_id,  member_type ) {
    try {

        //IE浏览器。
        if (BrowserTypeInfo == "ie") {

            //检查组件实例是否可用。
            if (mDispatchRoomInstance == null
                || typeof (mDispatchRoomInstance) == "undefined") {
                alert("没有初始化COM组件模块");
                return;
            }

            //解散调度室。
            mDispatchRoomInstance.MemberInvite(member_id,  member_type);
            if (mDispatchRoomInstance.last_error_code != 0) {
                alert("邀请一个调度成员 失败");
                return;
            }

            return ;
        }
        //Chrome浏览器。
        else if (BrowserTypeInfo == "chrome") {

            if (mDispatchRoomID == null
                || typeof (mDispatchRoomID) == "undefined") {
                return;
            }

            DispatchMemberInvite(mDispatchRoomID, member_id,  member_type
                , function (response_code, response_info, original_msg) {
                    //回调接收返回结果
                    if (response_code != 0) {
                        alert("邀请一个调度成员 失败");
                        mDispatchRoomID = null ;
                    }
                }
            ) ;


        }

    }
    catch (e) {
    }
}

//强拆一个调度成员
function MemberKickOut( member_id,  kick_info ) {
    try {

        //IE浏览器。
        if (BrowserTypeInfo == "ie") {

            //检查组件实例是否可用。
            if (mDispatchRoomInstance == null
                || typeof (mDispatchRoomInstance) == "undefined") {
                alert("没有初始化COM组件模块");
                return;
            }

            //解散调度室。
            mDispatchRoomInstance.MemberKickOut(member_id,  kick_info);
            if (mDispatchRoomInstance.last_error_code != 0) {
                alert("强拆一个调度成员 失败");
                return;
            }

            return ;
        }
        //Chrome浏览器。
        else if (BrowserTypeInfo == "chrome") {

            if (mDispatchRoomID == null
                || typeof (mDispatchRoomID) == "undefined") {
                return;
            }

            DispatchMemberKickOut(mDispatchRoomID, member_id,  kick_info
                , function (response_code, response_info, original_msg) {
                    //回调接收返回结果
                    if (response_code != 0) {
                        alert("强拆一个调度成员 失败");
                        mDispatchRoomID = null ;
                    }
                }
            ) ;


        }

    }
    catch (e) {
    }
}

//设置一个成员的类型
function MemberTypeSet( member_id,  member_type ) {
    try {

        //IE浏览器。
        if (BrowserTypeInfo == "ie") {

            //检查组件实例是否可用。
            if (mDispatchRoomInstance == null
                || typeof (mDispatchRoomInstance) == "undefined") {
                alert("没有初始化COM组件模块");
                return;
            }

            //解散调度室。
            mDispatchRoomInstance.MemberTypeSet( member_id,  member_type);
            if (mDispatchRoomInstance.last_error_code != 0) {
                alert("设置一个成员的类型 失败");
                return;
            }

            return ;
        }
        //Chrome浏览器。
        else if (BrowserTypeInfo == "chrome") {

            if (mDispatchRoomID == null
                || typeof (mDispatchRoomID) == "undefined") {
                return;
            }

            DispatchMemberTypeSet( mDispatchRoomID ,member_id, member_type
                , function (response_code, response_info, original_msg) {
                    //回调接收返回结果
                    if (response_code != 0) {
                        alert("设置一个成员的类型 失败");
                        mDispatchRoomID = null ;
                    }
                }
            ) ;


        }

    }
    catch (e) {
    }
}
