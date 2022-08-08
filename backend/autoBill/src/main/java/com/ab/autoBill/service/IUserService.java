package com.ab.autoBill.service;

import com.ab.autoBill.domain.request.EditUserRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface IUserService {
    /**
     * 获取用户信息
     * @param userId
     * @return
     */
    Map<String, Object> getUserInfo(int userId);

    /**
     * 编辑用户信息
     * @param request
     * @return
     */
    Map<String, Object> editUserInfo(EditUserRequest request , MultipartFile file, String parentDir);

    /**
     * 获取所有用户列表
     * @return
     */
    Map<String, Object> getUserList();
}
