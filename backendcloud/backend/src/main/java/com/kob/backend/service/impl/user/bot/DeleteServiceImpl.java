package com.kob.backend.service.impl.user.bot;

import com.kob.backend.bean.Bot;
import com.kob.backend.bean.Result;
import com.kob.backend.bean.User;
import com.kob.backend.mapper.BotMapper;
import com.kob.backend.service.user.bot.DeleteService;
import com.kob.backend.utils.message.TotalMessage;
import com.kob.backend.utils.user.UserDetailsImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class DeleteServiceImpl implements DeleteService {

    @Resource
    private BotMapper botMapper;

    @Override
    @Transactional
    public Result<Bot> delete(Map<String, String> map) {
        // 获取到前端传过来的bot_id
        try {
            int botId = Integer.parseInt(map.get("bot_id"));

            // 获取当前登录的用户
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            UserDetailsImpl loginUser = (UserDetailsImpl) usernamePasswordAuthenticationToken.getPrincipal();
            User user = loginUser.getUser();

            // 查询出当前bot_id对应的bot信息
            Bot bot = botMapper.selectById(botId);

            if (bot == null) {
                return new Result<>("444", TotalMessage.BOT_NOT_EXISTS);
            }

            if (!bot.getUserId().equals(user.getId())) {
                return new Result<>("444", TotalMessage.BOT_YOU_CANNOT_DELETE);
            }

            int i = botMapper.deleteById(botId);

            if (i < 0) {
                return new Result<>("444", TotalMessage.BOT_DELETE_FAILED);
            }

            return new Result<>("200", TotalMessage.BOT_DELETE_SUCCESS);
        } catch (NumberFormatException e) {
            return new Result<>("444", TotalMessage.BOT_DELETE_FAILED);
        }
    }
}
