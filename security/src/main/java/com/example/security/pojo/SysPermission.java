package com.example.security.pojo;

import java.util.Arrays;
import java.util.List;

public class SysPermission {
    private int id;
    private String url;
    private int roleId;
    private String permission;
    private List permissions;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public void setPermissions(List permissions) {
        this.permissions = permissions;
    }

    public List getPermissions() {
        return Arrays.asList(permission.trim().split(","));
    }
}
