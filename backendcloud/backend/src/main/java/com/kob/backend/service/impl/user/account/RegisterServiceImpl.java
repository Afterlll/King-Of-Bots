package com.kob.backend.service.impl.user.account;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.kob.backend.bean.Result;
import com.kob.backend.bean.User;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.service.user.account.RegisterService;
import com.kob.backend.utils.common.CommonUtil;
import com.kob.backend.utils.message.TotalMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class RegisterServiceImpl implements RegisterService {

    // 注册时默认头像地址
    private final String INIT_PHOTO = "https://cdn.acwing.com/media/user/profile/photo/196333_lg_6521fc6ad3.jpg";

    @Resource
    private UserMapper userMapper;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Result<User> register(String username, String password, String confirmedPassword) {

        // 用户名为空
        if (StringUtils.isEmpty(username.trim())) {
            return new Result<>("444", TotalMessage.USERNAME_NOT_NULL);
        }

        // 密码为空
        if (StringUtils.isEmpty(password.trim()) || StringUtils.isEmpty(confirmedPassword.trim())) {
            return new Result<>("444", TotalMessage.PASSWORD_NOT_NULL);
        }

        // 用户名长度过大
        if (username.length() > 100) {
            return new Result<>("444", TotalMessage.USERNAME_LENGTH_TOO_LONG);
        }

        // 密码长度过大
//        if (password.length() > 100 && CommonUtil.isValidPassword(password)) {
        if (password.length() > 100) {
            return new Result<>("444", TotalMessage.PASSWORD_LENGTH_TOO_LONG);
        }

        // 密码不匹配
        if (!password.trim().equals(confirmedPassword.trim())) {
            return new Result<>("444", TotalMessage.PASSWORD_NOT_MATCH_CONFIRMED_PASSWORD);
        }

        // 查询用户名是否已注册
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User user = userMapper.selectOne(queryWrapper);

        if (user != null) {
            return new Result<>("444", TotalMessage.USERNAME_IS_EXISTS);
        }

        // 用户名未注册进行用户注册
        User user1 = new User();
        user1.setPassword(passwordEncoder.encode(password));
        user1.setUsername(username);
        user1.setPhoto(INIT_PHOTO);
        user1.setRating(1500);

        userMapper.insert(user1);

        return new Result<>("200", TotalMessage.USER_REGISTER_SUCCESS);
    }
}
