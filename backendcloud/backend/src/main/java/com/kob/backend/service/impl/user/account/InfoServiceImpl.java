package com.kob.backend.service.impl.user.account;

import com.kob.backend.bean.Result;
import com.kob.backend.bean.User;
import com.kob.backend.service.user.account.InfoService;
import com.kob.backend.utils.message.TotalMessage;
import com.kob.backend.utils.user.UserDetailsImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class InfoServiceImpl implements InfoService {
    
    @Override
    public Result<User> getInfo() {

        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authentication.getPrincipal();
        User user = loginUser.getUser();

        Map map = new HashMap();
        map.put("username", user.getUsername());
        map.put("photo", user.getPhoto());
        map.put("id", user.getId());

        return new Result<>("200", map, TotalMessage.GET_MESSAGE_BY_TOKEN);
    }
}
