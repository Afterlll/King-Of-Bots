package com.kob.backend.service.impl.user.bot;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.kob.backend.bean.Bot;
import com.kob.backend.bean.Result;
import com.kob.backend.bean.User;
import com.kob.backend.mapper.BotMapper;
import com.kob.backend.service.user.bot.UpdateService;
import com.kob.backend.utils.message.TotalMessage;
import com.kob.backend.utils.user.UserDetailsImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

@Service
public class UpdateServiceImpl implements UpdateService {

    @Resource
    private BotMapper botMapper;

    @Override
    @Transactional
    public Result<Bot> update(Map<String, String> data) {
        // 获取到前端传过来的bot_id
        try {
            int botId = Integer.parseInt(data.get("bot_id"));

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

            String title = data.get("title").trim();
            String description = data.get("description").trim();
            String content = data.get("content").trim();

            if (StringUtils.isEmpty(title)) {
                return new Result<>("444", TotalMessage.BOT_TITLE_IS_NULL);
            }

            if (title.length() > 100) {
                return new Result<>("444", TotalMessage.BOT_TITLE_IS_TOO_LONG);
            }

            if (StringUtils.isEmpty(description)) {
                description = "当前用户很懒，什么都没留下~";
            }

            if (description.length() > 300) {
                return new Result<>("444", TotalMessage.BOT_DESCRIPTION_IS_TOO_LONG);
            }

            if (StringUtils.isEmpty(content)) {
                return new Result<>("444", TotalMessage.BOT_CONTENT_IS_NULL);
            }

            if (content.length() > 10000) {
                return new Result<>("444", TotalMessage.BOT_CONTENT_IS_TOO_LONG);
            }

            Bot bot1 = new Bot(
                    botId,
                    user.getId(),
                    title,
                    description,
                    content,
                    bot.getCreateTime(),
                    new Date()
            );

            int i = botMapper.updateById(bot1);

            if (i < 0) {
                return new Result<>("444", TotalMessage.BOT_UPDATE_FAILED);
            }

            return new Result<>("200", TotalMessage.BOT_UPDATE_SUCCESS);
        } catch (NumberFormatException e) {
            return new Result<>("444", TotalMessage.BOT_DELETE_FAILED);
        }
    }
}
