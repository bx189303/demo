package haidian.chatSip.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;

/**
 * mvc结果集封装工具类
 */
public class Response implements Serializable {
    // 定义jackson对象
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private String type;

    // 响应业务状态
    private Integer status;

    // 响应消息
    private String msg;

    // 响应中的数据
    private Object data;



    public static Response ok(String type,Object data) {
        return new Response(type,data);
    }

    public static Response build(String type, Integer status, String msg, Object data) {
        return new Response(type,status,msg,data);
    }

    public Response() {}

    public Response(String type, Integer status, String msg, Object data) {
        this.type = type;
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public Response(String type, Object data) {
        this.type=type;
        this.status = 200;
        this.msg = "";
        this.data = data;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * 没有object对象的转化
     *
     * @param json
     * @return
     */
    public static Response format(String json) {
        try {
            return MAPPER.readValue(json, Response.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



}
