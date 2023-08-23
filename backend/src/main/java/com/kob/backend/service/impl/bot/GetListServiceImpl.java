package com.kob.backend.service.impl.bot;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.bean.Bot;
import com.kob.backend.bean.Result;
import com.kob.backend.bean.User;
import com.kob.backend.mapper.BotMapper;
import com.kob.backend.service.user.bot.GetListService;
import com.kob.backend.utils.message.TotalMessage;
import com.kob.backend.utils.user.UserDetailsImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class GetListServiceImpl implements GetListService {

    @Resource
    private BotMapper botMapper;

    @Override
    public Result<Bot> getList() {

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) usernamePasswordAuthenticationToken.getPrincipal();
        User user = loginUser.getUser();

        List<Bot> bots = botMapper.selectList(new QueryWrapper<Bot>().eq("user_id", user.getId()));

        if (bots == null) {
            return new Result<>("444", TotalMessage.BOT_LIST_GET_FAILED);
        }

        return new Result<>("200", bots, TotalMessage.BOT_LIST_GET_SUCCESS);
    }
}
