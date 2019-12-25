$(function(){

});


$.ajax({
    type : "POST",
    contentType: "application/json;charset=UTF-8",
    url : "/getPersonByGroupId/"+id,
    //请求成功
    success : function(result) {
        // console.log("getUserByGroup : "+result.data);
        var res=result.data;
        var html='';
        $(res).each(function(i,n){


        })
        $("#div").html(html);
    },
    //请求失败，包含具体的错误信息
    error : function(e){
        console.log(e.status);
        console.log(e.responseText);
    }
})