/////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////
// Copyright 2017-2018 CoCall.Net

//功能：实现 Dispatch 会议事件操作部分

//应用可根据需要修改内部事件实现

//注意：这里可能需要供VBS调用，VBScript 函数调用javascrip函数时  javascrip函数中的 ；必须全部去掉 不然调用不通

/////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////

//接收本地与会议中心连接事件。
function OnRoomConfConnected(queue_id)
{
    try {
    }
    catch (e) {
    }
}

//接收本地与会议中心断开事件。
function OnRoomConfDisconnected(queue_id)
{
    try {
    }
    catch (e) {
    }
}

//接收会议中心解散事件。
function OnRoomConfDissolve(queue_id)
{
    try {
    }
    catch (e) {
    }
}

//接收会议中心发过来的文本消息事件。
function OnRoomConfTextInfo(queue_id,info_text)
{
    try {
    }
    catch (e) {
    }
}

        
//接收会议成员类型改变事件。
function OnRoomMemberTypeChanged(queue_id, member_id, member_name,  member_type)
{
    try {
    }
    catch (e) {
    }
}


//接收会议成员状态改变事件。
function OnRoomMemberStatusChanged(queue_id, member_id,  member_name,  member_status,  lparam,  rparam)
{
    try {
    }
    catch (e) {
    }
}

/*
//本地查看某个会议成员的视频已经打开事件。
function OnRoomMemberVideoViewOpened(queue_id,member_id, member_name, member_view_hwnd)
{
    try {
    }
    catch (e) {
    }
}

//本地查看某个会议成员的视频已经关闭。
function OnRoomMemberVideoViewCloseed(queue_id,member_id, member_name, member_view_hwnd)
{
    try {
    }
    catch (e) {
    }
}

//某个会议成员查看本地的视频已经打开。
function OnRoomLocalVideoViewerAdd(queue_id,member_id, member_name, viewer_count)
{
    try {
    }
    catch (e) {
    }
}


//某个会议成员查看本地的视频已经关闭。
function OnRoomLocalVideoViewerRemove(queue_id,member_id, member_name, viewer_count) {
    try {
    }
    catch (e) {
    }
}
//*/