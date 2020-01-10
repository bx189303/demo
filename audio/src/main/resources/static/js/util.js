//获取url参数
function GetUrlParam(name)
{
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null)return  unescape(r[2]); return null;
}
//获取日期 yyyy-MM-dd HH:mm:ss
function getMyDate(str) {
    if (str == null || str == "") {
        return '';
    }
    var oDate = new Date(str),
        oYear = oDate.getFullYear(),
        oMonth = oDate.getMonth() + 1,
        oDay = oDate.getDate(),
        oHour = oDate.getHours(),
        oMin = oDate.getMinutes(),
        oSen = oDate.getSeconds(),
        oTime = oYear + '-' + getzf(oMonth) + '-' + getzf(oDay) + ' ' + getzf(oHour) + ':' + getzf(oMin) + ':' + getzf(oSen);//最后拼接时间
    return oTime;
};
function getzf(num) {
    if (parseInt(num) < 10) {
        num = '0' + num;
    }
    return num;
}
//判断文件类型
function getFileType(fileName){
    var fileType="file";
    var suffixIndex=fileName.lastIndexOf(".");
    var suffix=fileName.substring(suffixIndex+1).toUpperCase();
    if(suffix=="BMP"||suffix=="JPG"||suffix=="JPEG"||suffix=="PNG"||suffix=="GIF"){ //如果是图片
        fileType="img";
    }
    return fileType;
}

//处理undenfind
function handleNull(str){
    str=typeof(str)=="undefined"?"":str;
    return str;
}

//获取当前时间年月日
function getNowFormatDate() {
    var date = new Date();
    var seperator1 = "-";
    var year = date.getFullYear();
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    var currentdate = year + seperator1 + month + seperator1 + strDate;
    return currentdate;
}
//获取当前日期下一天
function getNextFormatDate() {
    var date = new Date();
    date=date.setDate(date.getDate()+1);
    date=new Date(date);
    var seperator1 = "-";
    var year = date.getFullYear();
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    var currentdate = year + seperator1 + month + seperator1 + strDate;
    return currentdate;
}
//js没有 replaceAll 方法，需要手写
String.prototype.replaceAll = function(s1, s2) {
    return this.replace(new RegExp(s1, "gm"), s2);
}

//处理undenfind
function handleNull(str){
    str=typeof(str)=="undefined"?"":str;
    return str;
}

//鼠标拖动div
function windowHtmlMove(body,title){
    var div= document.getElementById(body);
    var divTitle=document.getElementById(title);
    //1 div.onmousedown
    //2 document.onmousemove
    //3 document.onmouseup
    // var isDrag= false;
    var distanceX,distanceY;
    //按下鼠标时，定义事件
    divTitle.onmousedown =function(evt){
        evt= evt||window.event;
        isDrag= true;
        //console.log("isDrag:"+isDrag);
        //保存鼠标相对于div的偏移量
        distanceX= evt.offsetX;
        distanceY= evt.offsetY;
        // 按下鼠标时，定义document的移动事件
        document.onmousemove=function(evt){
            evt= evt||window.event;
            console.log(isDrag+" onmousemove");
            // if(isDrag){
            // div.style.left= evt.clientX- distanceX+"px";
            // div.style.top = evt.clientY- distanceY+"px";
            var divLeft= evt.clientX- distanceX;
            var divTop = evt.clientY- distanceY;
            var maxLeft= document.documentElement.clientWidth- div.offsetWidth;
            var maxTop = document.documentElement.clientHeight- div.offsetHeight;
            //console.log(document.documentElement.clientHeight);
            // if(divLeft<0){
            //     divLeft=0;
            // }
            // if(divTop<0){
            //     divTop=0;
            // }
            // if(divLeft> maxLeft){
            //     divLeft= maxLeft;
            // }
            // if(divTop >maxTop){
            //     divTop= maxTop;
            // }

            div.style.left= divLeft+"px";
            div.style.top = divTop+"px";
            // }
            return false;
        }
        //按下鼠标时，定义document的鼠标弹起事件
        document.onmouseup= function(){
            // isDrag =false;
            // console.log(isDrag + " onmouseup");
            document.onmousemove = null;
            document.onmouseup= null;
        }
    }
}