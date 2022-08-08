package com.ab.autoBill.service.impl;

import com.ab.autoBill.domain.Bill;
import com.ab.autoBill.domain.User;
import com.ab.autoBill.domain.request.EditUserRequest;
import com.ab.autoBill.exception.BadCredentialsException;
import com.ab.autoBill.exception.UsernameNotFoundException;
import com.ab.autoBill.repository.UserRepository;
import com.ab.autoBill.security.jwt.JwtConfigProperties;
import com.ab.autoBill.security.jwt.JwtTokenUtil;
import com.ab.autoBill.service.IAuthService;
import com.ab.autoBill.service.IUserService;
import com.ab.autoBill.utils.FileUtil;
import com.ab.autoBill.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Keyan
 */
@Service
public class UserService implements IUserService {
    private UserRepository userRepository;

    private JwtTokenUtil jwtTokenUtil = new JwtTokenUtil(new JwtConfigProperties());

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public Map<String, Object> getUserInfo(int userId){
        Map<String, Object> result = new HashMap<>();
        User user =  userRepository.findUserById(userId);
        result.put("data",user);
        return result;
    }

    /**
     * 修改密码
     * @param request
     * @param file
     * @param parentDir
     * @return
     */
    public Map<String, Object> editUserInfo(EditUserRequest request, MultipartFile file, String parentDir){
        Map<String, Object> result = new HashMap<>();
        String message = "编辑用户信息成功";
        User user = userRepository.findUserById(request.getId());
        user.setPassword(StringUtil.getValidValue(user.getPassword(),request.getPassword()));
        if(file != null ){
            if(FileUtil.isImgFile(file)){
                user.setUserImg(FileUtil.saveFile(file, parentDir,"userImg"));
            }else{
                message = "图像文件不是有效的文件";
            }

        }
        userRepository.save(user);
        result.put("msg",message);
        result.put("data",user);
        return result;
    }
    public Map<String, Object> getUserList(){
        Map<String, Object> result = new HashMap<>();
       // List<User> users = userRepository.findUsersByValid(true);
        List<User> users = new LinkedList<>();
        result.put("data",users);
        return result;
    }
}

