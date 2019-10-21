/**初始化客户端**/
function client_init(){
    console.log("开始初始化")

    setTimeout(function(){
        DispatchClient_Init(
            mDispatchClient              //客户端的COM组件实例
            , OnClientLogOutMessage       //客户端日志输出接口(Chrome)。
            , OnClientLoginStatus         //接收注册事件(Chrome)。
            , OnClientPagerMessage        //接收文本消息。
            , OnPagerMessageResponse      //接收文本消息发送结果。
            , OnClientCallIncome          //接收客户端呼入事件。
            , OnClientHostStatusEvent      //接收Host状态事件。
        );
    },500)

    //Host状态事件
    function OnClientHostStatusEvent(statue_code, statue_info) {
        if ( statue_code == COCALL_HOST_STATUS_CODE_INIT_BEGIN ){
            // DispatchLogOutMessage( "收到HOST初始化启动消息" );
        }else if ( statue_code == COCALL_HOST_STATUS_CODE_INIT_FAIL ){
            // DispatchLogOutMessage( "收到HOST初始化失败消息" );
        }else if ( statue_code == COCALL_HOST_STATUS_CODE_PROCESS_BEGIN ){
            console.log("收到HOST处理启动消息");
            //注册客户端。
            /**
             DispatchClient_Login(
             user.serverIp              //Sip 服务器地址。
             ,user.serverIp              //Soap 服务器地址。
             ,user.userName                //用户名。
             ,user.password                                      //用户密码
             , OnClientLoginStatus                       //接收注册事件（Chrome）
             );
             **/
            DispatchClient_Login(
                '122.115.36.163'             //Sip 服务器地址。
                ,'122.115.36.163'            //Soap 服务器地址。
                ,'admin'               //用户名。
                ,'admin'               //用户密码
                , OnClientLoginStatus                       //接收注册事件（Chrome）
            );
            //绑定通道事件。
            DispatchPerson_BindEvent(
                OnPersonChanMsg
                , OnPersonSipChanInfoMsg
                , OnPersonSipInfoCmd
                , OnPersonSipInfoCmdRsp
                , OnPersonSipInfoNotify );
        }else if ( statue_code == COCALL_HOST_STATUS_CODE_PROCESS_END ){
            // DispatchLogOutMessage( "收到HOST处理结束消息" );
        }
    }
}

/***************** 视频 *************/
/**回调函数**/
function response_callback1(e,e1,e2){
    console.log("*****回调函数开始*****")
    console.log(e);
    console.log(e1);
    console.log(e2);
    console.log("*****回调函数结束*****")
}
/** 创建一个视频墙 **/
function jltsVideoWall(){
    /**
     * 		show_video_wall_rmenu    是否支持视频墙右键菜单
     * 		show_video_video_rmenu	 是否支持视频通道右键菜单
     * 		show_video_over_rmenu    视频通道视频墙分屏数后的操作方式
     * 			0 替换最早shipin
     * 			1 替换最晚视频
     * 			2 添加到最后并且自动扩展分屏
     */
//	 sessionStorage.setItem('qh_jlts','1');
    try {
        var extern_parameters = {
            [COCALL_HOST_FUNCTION_PARAMETER_SHOW_VIDEO_WALL_RMENU] : 1
            , [COCALL_HOST_FUNCTION_PARAMETER_SHOW_VIDEO_CHAN_RMENU] : 1
            , [COCALL_HOST_FUNCTION_PARAMETER_VIDEO_CHAN_OVER_TYPE] : 2
        };
        VideoWallCreate(mDispatchVideoWallID, "视频" , "" , extern_parameters );
    }
    catch (e) {
        console.log("OnBtnCreateVideoWall错误!");
    }

    jltsVideoWall_show('',false,'-600','-500','600','500');
}

/** 视频墙  显示位置 **/
function jltsVideoWall_show(name,orAll,x,y,width,height){
    //jltsVideoWall_show('',false,'-600','-500','600','500');
    /**
     *  dlg_name：视频墙的ID。
     desktop_name：视频墙需要显示的所在桌面。
     full_screen：是否全屏显示视频墙。
     dlg_left：指定视频墙的x坐标，如果不需要指定，传入-1。
     dlg_top：指定视频墙的Y坐标，如果不需要指定，传入-1。
     dlg_width：指定视频墙的宽度，如果不需要指定，传入-1。
     dlg_height：指定视频墙的高度，如果不需要指定，传入-1
     response_callback：回调函数，可以为null或者不传，接收本函数执行的异步执行结果，
     */
    DispatchVideoWallSetShowInfo( mDispatchVideoWallID,name,orAll,x,y,width,height,response_callback1)
}

/** 播放视频 **/
function jltsVideoWall_newVideo(num,type){
    /**
     * 	dlg_name：视频墙的ID。
     video_source_type ：视频成员的视频源类型。
     video_source_id：视频成员的唯一ID。
     video_source_name：视频成员的名字，可以为空。
     parameter_json_string：扩展参数，备用。
     response_callback：回调函数，可以为null或者不传，接收本函数执行的异步执行结果，
     */
    var text='KdmMcu';
    if(type !=undefined && type=='hx'){
        text='IVMS8200';
    }
    DispatchVideoChanAdd( mDispatchVideoWallID, text,  num , '' ,''  ,response_callback1)
};