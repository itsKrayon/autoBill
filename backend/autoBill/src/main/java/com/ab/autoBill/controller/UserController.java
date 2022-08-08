package com.ab.autoBill.controller;

import com.ab.autoBill.domain.Bill;
import com.ab.autoBill.domain.User;
import com.ab.autoBill.domain.request.EditUserRequest;
import com.ab.autoBill.service.IUserService;
import com.ab.autoBill.service.impl.AuthService;
import com.ab.autoBill.service.IAuthService;
import com.ab.autoBill.domain.request.LoginRequest;
import com.ab.autoBill.service.impl.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.Map;


/**
 * @author Keyan
 * @date 2022/8/4 23:08
 */
@RestController
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private IAuthService authService;

    private IUserService userService;

    @Autowired
    public UserController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    /**
     * 登录
     *
     * @param request
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request.getUsername(), request.getPassword()));
    }

    /**
     * 注册
     *
     * @param request
     * @return
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.register(request.getUsername(), request.getPassword()));
    }

    /**
     * 获取用户信息
     *
     * @param userId
     * @return
     */
    @PostMapping("/user")
    public ResponseEntity<Map<String, Object>> getUserInfo(@RequestParam(value = "userId") int userId) {
        return ResponseEntity.ok(userService.getUserInfo(userId));
    }

    /**
     * 编辑用户信息,主要是包括头像，不过优先级低
     *
     * @param request
     * @return
     */
//    @PostMapping("/user/edit")
//    public ResponseEntity<Map<String, Object>> editUserInfo(@RequestBody EditUserRequest request) {
//        //有文件传输的请求
//        return ResponseEntity.ok(userService.editUserInfo(request));
//    }

    /**
     * 管理员获取用户列表
     *
     * @return
     */
    @PostMapping("/admin/userList")
    @PreAuthorize("hasAuthority('Role_Admin')")
    public ResponseEntity<Map<String, Object>> getUserList() {
        return ResponseEntity.ok(userService.getUserList());
    }

    /**
     * 管理员管理用户权限
     *
     * @return
     */
    @PostMapping("/admin/manage")
    @PreAuthorize("hasAuthority('Role_Admin')")
    public ResponseEntity<Map<String, Object>> manageUserAuth(@RequestParam(value = "userId") int userId,
                                                           @RequestParam(value = "authority") String authority) {
        return ResponseEntity.ok(authService.manageUserAuth(userId, authority));
    }

}
