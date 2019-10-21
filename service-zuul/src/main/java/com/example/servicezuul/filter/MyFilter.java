package com.example.servicezuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.condition.RequestCondition;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class MyFilter extends ZuulFilter {

    /*
     * filterType：返回一个字符串代表过滤器的类型，在zuul中定义了四种不同生命周期的过滤器类型，具体如下：
     * pre：路由之前
     * routing：路由之时
     * post： 路由之后
     * error：发送错误调用
     */
    @Override
    public String filterType() {
        return "pre";
    }

    /*
     * filterOrder：过滤的顺序
     */
    @Override
    public int filterOrder() {
        return 0;
    }

    /*
     * shouldFilter：这里可以写逻辑判断，是否要过滤，本文true,永远过滤。
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /*
     * run：过滤器的具体逻辑。可以很复杂，包括查sql，nosql去判断该请求到底有没有权限访问，
     * 这里是判断请求有没有 token 参数
     */
    @Override
    public Object run() throws ZuulException {
        RequestContext ctx=RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
//        Object accessToken = request.getParameter("token");
        Object accessToken = null;
        try {
            accessToken = GetRequestJsonUtils.getRequestJsonObject(request).getString("token");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(accessToken == null) {
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(401);
            try {
                ctx.getResponse().getWriter().write("token is empty");
            }catch (Exception e){

            }

            return null;
        }
        return null;
    }
}
