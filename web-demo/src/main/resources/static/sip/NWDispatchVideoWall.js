/////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////
// Copyright 2017-2018 CoCall.Net

//功能：实现 Dispatch《视频墙》功能模块的 JS 操作部分

/////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////

//保存视频墙的组件实例（IE）
var mDispatchVideoWallInstance = null;

//保存视频墙的ID。（Chrome）
var mDispatchVideoWallID = -1 ;

//创建视频墙
function VideoWallCreate(_DispatchVideoWallInstance, dlg_catpion, desktop_name, extern_parameters) {
    try {

        //IE浏览器。
        if (BrowserTypeInfo == "ie") {

            //是否传入了COM组件实例。
            if (_DispatchVideoWallInstance != null
                && typeof (_DispatchVideoWallInstance) != "undefined") {
                mDispatchVideoWallInstance = _DispatchVideoWallInstance;
            }

            //检查组件实例是否可用。
            if (mDispatchVideoWallInstance == null
                || typeof (mDispatchVideoWallInstance) == "undefined") {
                alert("没有初始化COM组件模块");
                return;
            }

            //创建视频墙。
            mDispatchVideoWallInstance.VideoWallCreate(dlg_catpion, desktop_name);
            if (mDispatchVideoWallInstance.last_error_code != 0) {
                alert("创建一个新的视频墙失败");
                return;
            }

            //返回新建视频墙的ID。
            return ;
        }
        //Chrome浏览器。
        else if (BrowserTypeInfo == "chrome") {

            //创建一个新的ID，用于标识这个视频墙。
            mDispatchVideoWallID = generateUUID();

            //设置事件回调函数。
            DispatchOnVideoWallCreatedEventHanderSet(mDispatchVideoWallID, OnVideoWallCreated);
            DispatchOnVideoWallDestroyedEventHanderSet(mDispatchVideoWallID, OnVideoWallDestroyed);
            DispatchOnVideoChanCreatedEventHanderSet(mDispatchVideoWallID, OnVideoChanCreated );
            DispatchOnVideoChanDestroyedEventHanderSet(mDispatchVideoWallID, OnVideoChanDestroyed );

            DispatchVideoWallCreate(dlg_catpion, mDispatchVideoWallID, desktop_name, extern_parameters ,
                function (response_code, response_info, original_msg) {
                    //回调接收返回结果
                    if (response_code != 0) {
                        alert("创建一个新的视频墙失败");
                        mDispatchVideoWallID = response_info ;
                        //取消事件回调。
                        DispatchOnVideoWallCreatedEventHanderSet(mDispatchVideoWallID, null);
                        DispatchOnVideoWallDestroyedEventHanderSet(mDispatchVideoWallID, null);
                        DispatchOnVideoChanCreatedEventHanderSet(mDispatchVideoWallID, null);
                        DispatchOnVideoChanDestroyedEventHanderSet(mDispatchVideoWallID, null);
                    }
                }
            ) ;


        }

    }
    catch (e) {
    }
}

//关闭视频墙
function VideoWallDestroy() {
    try {

        //IE浏览器。
        if (BrowserTypeInfo == "ie") {
            //检查组件实例是否可用。
            if (mDispatchVideoWallInstance == null
                || typeof (mDispatchVideoWallInstance) == "undefined") {
                alert("没有初始化COM组件模块");
                return;
            }

            //关闭视频墙。
            mDispatchVideoWallInstance.VideoWallDestroy();
            if (mDispatchVideoWallInstance.last_error_code != 0) {
                alert("关闭视频墙失败");
                return;
            }
        }
        //Chrome浏览器。
        else if (BrowserTypeInfo == "chrome") {

            if (mDispatchVideoWallID == null
                || typeof (mDispatchVideoWallID) == "undefined") {
                return;
            }

            DispatchVideoWallDestroy( mDispatchVideoWallID,
                function (response_code, response_info, original_msg) {
                    //回调接收返回结果
                    if (response_code != 0) {
                        alert("关闭一个视频墙失败");
                    }
                    else
                    {
                        //取消事件回调。
                        DispatchOnVideoWallCreatedEventHanderSet(mDispatchVideoWallID, null );
                        DispatchOnVideoWallDestroyedEventHanderSet(mDispatchVideoWallID, null);
                        DispatchOnVideoChanCreatedEventHanderSet(mDispatchVideoWallID, null);
                        DispatchOnVideoChanDestroyedEventHanderSet(mDispatchVideoWallID, null);

                        mDispatchVideoWallID = null;
                    }
                }
            ) ;
        }
    }
    catch (e) {
    }
}


//设置视频墙显示位置
function VideoWallSetShowInfo(desktop_name, full_screen , dlg_left, dlg_top, dlg_width, dlg_height ) {
    try {

        //IE浏览器。
        if (BrowserTypeInfo == "ie") {
            //检查组件实例是否可用。
            if (mDispatchVideoWallInstance == null
                || typeof (mDispatchVideoWallInstance) == "undefined") {
                alert("没有初始化COM组件模块");
                return;
            }

            //关闭视频墙。
            mDispatchVideoWallInstance.VideoWallSetShowInfo(desktop_name, full_screen);
            if (mDispatchVideoWallInstance.last_error_code != 0) {
                alert("设置视频墙显示属性失败");
                return;
            }
        }
        //Chrome浏览器。
        else if (BrowserTypeInfo == "chrome") {

            if (mDispatchVideoWallID == null
                || typeof (mDispatchVideoWallID) == "undefined") {
                return;
            }

            DispatchVideoWallSetShowInfo(mDispatchVideoWallID, desktop_name, full_screen
                , dlg_left, dlg_top, dlg_width, dlg_height
                ,  function (response_code, response_info, original_msg) {
                    //回调接收返回结果
                    if (response_code != 0) {
                        alert("设置视频墙显示属性失败");
                    }
                }
            );
        }
    }
    catch (e) {
    }
}

//增加一个视频通道
function VideoChanAdd( video_source_type,  video_source_id,  video_source_name,  parameter_json_string) {
    try {

        //IE浏览器。
        if (BrowserTypeInfo == "ie") {
            //检查组件实例是否可用。
            if (mDispatchVideoWallInstance == null
                || typeof (mDispatchVideoWallInstance) == "undefined") {
                alert("没有初始化COM组件模块");
                return;
            }

            //关闭视频墙。
            mDispatchVideoWallInstance.VideoChanAdd(video_source_type, video_source_id, video_source_name, parameter_json_string);
            if (mDispatchVideoWallInstance.last_error_code != 0) {
                alert("增加一个视频通道失败");
                return;
            }
        }
        //Chrome浏览器。
        else if (BrowserTypeInfo == "chrome") {

            if (mDispatchVideoWallID == null
                || typeof (mDispatchVideoWallID) == "undefined") {
                return;
            }

            DispatchVideoChanAdd(mDispatchVideoWallID , video_source_type, video_source_id, video_source_name, parameter_json_string
                , function (response_code, response_info, original_msg) {
                    //回调接收返回结果
                    if (response_code != 0) {
                        alert("增加一个视频通道失败");
                    }
                }
            );
        }
    }
    catch (e) {
    }
}

//移除一个视频通道
function VideoChanRemove( video_source_id,  paramter_json_string) {
    try {

        //IE浏览器。
        if (BrowserTypeInfo == "ie") {
            //检查组件实例是否可用。
            if (mDispatchVideoWallInstance == null
                || typeof (mDispatchVideoWallInstance) == "undefined") {
                alert("没有初始化COM组件模块");
                return;
            }

            //关闭视频墙。
            mDispatchVideoWallInstance.VideoChanRemove(video_source_id, paramter_json_string);
            if (mDispatchVideoWallInstance.last_error_code != 0) {
                alert("移除一个视频通道 失败");
                return;
            }
        }
        //Chrome浏览器。
        else if (BrowserTypeInfo == "chrome") {

            if (mDispatchVideoWallID == null
                || typeof (mDispatchVideoWallID) == "undefined") {
                return;
            }

            DispatchVideoChanRemove(mDispatchVideoWallID, video_source_id, paramter_json_string
                , function (response_code, response_info, original_msg) {
                    //回调接收返回结果
                    if (response_code != 0) {
                        alert("移除一个视频通道 失败");
                    }
                }
            );
        }
    }
    catch (e) {
    }
}


//关闭所有的视频通道
function VideoChanClean( ) {
    try {

        //IE浏览器。
        if (BrowserTypeInfo == "ie") {
            //检查组件实例是否可用。
            if (mDispatchVideoWallInstance == null
                || typeof (mDispatchVideoWallInstance) == "undefined") {
                alert("没有初始化COM组件模块");
                return;
            }

            //关闭视频墙。
            mDispatchVideoWallInstance.VideoChanClean();
            if (mDispatchVideoWallInstance.last_error_code != 0) {
                alert("关闭所有的视频通道 失败");
                return;
            }
        }
        //Chrome浏览器。
        else if (BrowserTypeInfo == "chrome") {

            if (mDispatchVideoWallID == null
                || typeof (mDispatchVideoWallID) == "undefined") {
                return;
            }

            DispatchVideoChanClean(mDispatchVideoWallID
                , function (response_code, response_info, original_msg) {
                    //回调接收返回结果
                    if (response_code != 0) {
                        alert("关闭所有的视频通道 失败");
                    }
                }
            );
        }
    }
    catch (e) {
    }
}

//重新排布所有的视频通道
function VideoChansRearrange( split_screen_count ) {
    try {

        //IE浏览器。
        if (BrowserTypeInfo == "ie") {
            //检查组件实例是否可用。
            if (mDispatchVideoWallInstance == null
                || typeof (mDispatchVideoWallInstance) == "undefined") {
                alert("没有初始化COM组件模块");
                return;
            }

            //关闭视频墙。
            mDispatchVideoWallInstance.VideoChansRearrange(split_screen_count);
            if (mDispatchVideoWallInstance.last_error_code != 0) {
                alert("重新排布所有的视频通道 失败");
                return;
            }
        }
        //Chrome浏览器。
        else if (BrowserTypeInfo == "chrome") {

            if (mDispatchVideoWallID == null
                || typeof (mDispatchVideoWallID) == "undefined") {
                return;
            }

            //关闭视频墙。
            DispatchVideoChansRearrange(mDispatchVideoWallID, split_screen_count
                , function (response_code, response_info, original_msg) {
                    //回调接收返回结果
                    if (response_code != 0) {
                        alert("重新排布所有的视频通道 失败");
                    }
                }
            );
        }
    }
    catch (e) {
    }
}

//设置某个视频通道的最大化状态
function VideoChanSetMax( video_source_id,  show_max ) {
    try {

        //IE浏览器。
        if (BrowserTypeInfo == "ie") {
            //检查组件实例是否可用。
            if (mDispatchVideoWallInstance == null
                || typeof (mDispatchVideoWallInstance) == "undefined") {
                alert("没有初始化COM组件模块");
                return;
            }

            //设置某个视频通道的最大化状态。
            mDispatchVideoWallInstance.VideoChanSetMax(video_source_id, show_max);
            if (mDispatchVideoWallInstance.last_error_code != 0) {
                alert("设置某个视频通道的最大化状态 失败");
                return;
            }
        }
        //Chrome浏览器。
        else if (BrowserTypeInfo == "chrome") {

            if (mDispatchVideoWallID == null
                || typeof (mDispatchVideoWallID) == "undefined") {
                return;
            }

            //设置某个视频通道的最大化状态。
            DispatchVideoChanSetMax(mDispatchVideoWallID, video_source_id, show_max
                , function (response_code, response_info, original_msg) {
                    //回调接收返回结果
                    if (response_code != 0) {
                        alert("设置某个视频通道的最大化状态 失败");
                    }
                }
            );
        }
    }
    catch (e) {
    }
}

//设置某个视频通道的全屏状态
function VideoChanSetFullScreen( video_source_id,  full_screen ) {
    try {

        //IE浏览器。
        if (BrowserTypeInfo == "ie") {
            //检查组件实例是否可用。
            if (mDispatchVideoWallInstance == null
                || typeof (mDispatchVideoWallInstance) == "undefined") {
                alert("没有初始化COM组件模块");
                return;
            }

            //设置某个视频通道的全屏状态。
            mDispatchVideoWallInstance.VideoChanSetFullScreen(video_source_id, full_screen);
            if (mDispatchVideoWallInstance.last_error_code != 0) {
                alert("设置某个视频通道的全屏状态 失败");
                return;
            }
        }
        //Chrome浏览器。
        else if (BrowserTypeInfo == "chrome") {

            if (mDispatchVideoWallID == null
                || typeof (mDispatchVideoWallID) == "undefined") {
                return;
            }

            //设置某个视频通道的全屏状态。
            DispatchVideoChanSetFullScreen(mDispatchVideoWallID, video_source_id, full_screen
                , function (response_code, response_info, original_msg) {
                    //回调接收返回结果
                    if (response_code != 0) {
                        alert("设置某个视频通道的全屏状态 失败");
                    }
                }
            );
        }
    }
    catch (e) {
    }
}
