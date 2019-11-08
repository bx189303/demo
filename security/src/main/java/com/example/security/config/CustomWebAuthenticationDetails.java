package com.example.security.config;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;

/**
 * WebAuthenticationDetails类提供了获取用户登录时携带的额外信息的功能，
 * 默认提供了 remoteAddress 与 sessionId 信息。
 */
public class CustomWebAuthenticationDetails extends WebAuthenticationDetails {
    private String verifyCode;

    public CustomWebAuthenticationDetails(HttpServletRequest request) {
        super(request);
        verifyCode=request.getParameter("verifyCode");
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        sb.append(super.toString()).append(";VerifyCode:").append(getVerifyCode());
        return sb.toString();
    }
}
