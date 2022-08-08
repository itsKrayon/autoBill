package com.ab.autoBill.service;

import java.util.Map;

/**
 * @author Keyan
 * @Date 2022/8/4 0:02
 */
public interface IAuthService {
    /**
     * 登录
     * @param username
     * @param password
     * @return
     */
    Map<String, Object> login(String username, String password);

    /**
     * 注册
     * @param username
     * @param password
     * @return
     */
    Map<String, Object> register(String username, String password);

    /**
     * 权限管理
     * @param userId
     * @param authority
     * @return
     */
    Map<String, Object> manageUserAuth(int userId, String authority);
}
