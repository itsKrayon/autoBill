package com.ab.autoBill.service.impl;

import com.ab.autoBill.domain.Authority;
import com.ab.autoBill.exception.BadCredentialsException;
import com.ab.autoBill.exception.UsernameNotFoundException;
import com.ab.autoBill.domain.User;
import com.ab.autoBill.repository.AuthorityRepository;
import com.ab.autoBill.repository.UserRepository;
import com.ab.autoBill.security.jwt.JwtConfigProperties;
import com.ab.autoBill.security.jwt.JwtTokenUtil;
import com.ab.autoBill.service.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


@Service
public class AuthService implements IAuthService {
    private AuthenticationManager authenticationManager = new AuthenticationManager() {
        @Override
        public Authentication authenticate(Authentication authentication) throws AuthenticationException {
            return null;
        }
    };
    private UserRepository userRepository;
    private AuthorityRepository authorityRepository;
    private JwtTokenUtil jwtTokenUtil = new JwtTokenUtil(new JwtConfigProperties());

    @Autowired
    public AuthService(UserRepository userRepository,AuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
    }

    /**
     * 用户登录实现
     * @param username 用户名
     * @param password 密码
     * @return token和用户信息
     */
    public Map<String, Object> login(String username, String password) {
        User user = userRepository.findByUsername(username);
        //用户不存在
        if (user == null) {
            throw new UsernameNotFoundException(username);
            //密码不正确
        } else if (!password.equals(user.getPassword()) ) {
            throw new BadCredentialsException();
        } else {
            UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(username, password);
            authenticationManager.authenticate(upToken);
            //生成token
            String token = jwtTokenUtil.generateToken(user);
            Map<String, Object> map = new HashMap<>();
            map.put("token", token);
            map.put("userDetails", user);
            return map;
        }
    }

    public Map<String, Object> register(String username, String password){
        Map<String, Object> result = new HashMap<>();
        //控制username的唯一性
        if(userRepository.findByUsername(username) != null){
            result.put("msg","用户名已存在");
            return result;
        }
        User user = new User(username,password);
        Authority authority = new Authority();
        authority.setAuthority("Role_User");
        authority.setUser(user);
        Set<Authority> authoritySet = new HashSet<>();
        authoritySet.add(authority);
        user.setAuthorities(authoritySet);
        user.setValid(true);
        userRepository.save(user);
        authorityRepository.save(authority);
        result.put("msg","注册成功");
        result.put("user",user);
        return  result;
    }

    public Map<String, Object> manageUserAuth(int userId, String authority){
        Authority auth = authorityRepository.findByAuthority(authority);
        User user = userRepository.findUserById(userId);
        auth.setUser(user);
        Map<String, Object> map = new HashMap<>();
        map.put("msg","改变用户权限成功");
        return map;
    }
}

