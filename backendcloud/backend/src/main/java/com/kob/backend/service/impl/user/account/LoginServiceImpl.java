package com.kob.backend.service.impl.user.account;

import com.kob.backend.bean.Result;
import com.kob.backend.bean.User;
import com.kob.backend.service.user.account.LoginService;
import com.kob.backend.utils.jwt.JwtUtil;
import com.kob.backend.utils.message.TotalMessage;
import com.kob.backend.utils.user.UserDetailsImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
public class LoginServiceImpl implements LoginService {

    @Resource
    private AuthenticationManager authenticationManager;

    @Override
    public Result<User> getToken(String username, String password) {

        // 这里验证失败会自动处理，抛出定义好的异常
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticate.getPrincipal();
        User user = loginUser.getUser();
        String jwt = JwtUtil.createJWT(user.getId().toString());

        Map map = new HashMap();
        map.put("token", jwt);

        return new Result<>("200", map, TotalMessage.USER_LOGIN_SUCCESS);
    }
}
