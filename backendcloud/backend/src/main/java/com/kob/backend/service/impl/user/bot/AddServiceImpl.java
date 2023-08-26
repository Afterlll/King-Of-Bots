package com.kob.backend.service.impl.user.bot;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.kob.backend.bean.Bot;
import com.kob.backend.bean.Result;
import com.kob.backend.bean.User;
import com.kob.backend.mapper.BotMapper;
import com.kob.backend.service.user.bot.AddService;
import com.kob.backend.utils.message.TotalMessage;
import com.kob.backend.utils.user.UserDetailsImpl;
import lombok.experimental.Tolerate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

@Service
public class AddServiceImpl implements AddService{


    @Resource
    private BotMapper botMapper;

    @Override
    public Result<Bot> insert(Map<String, String> data) {

        // 获取bot的所有者id
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) usernamePasswordAuthenticationToken.getPrincipal();
        User user = loginUser.getUser();

        if (user == null) {
            return new Result<>("444", TotalMessage.USER_NOT_LOGIN);
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

        QueryWrapper<Bot> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", user.getId());
        if (botMapper.selectCount(queryWrapper) >= 10) {
            return new Result<>("444", TotalMessage.MAX_BOT_NUM);
        }

        Date now = new Date();
        Bot kob = new Bot(null, user.getId(), title, description, content, now, now);

        int insert = botMapper.insert(kob);

        if (insert < 0) {
            return new Result<>("444", TotalMessage.BOT_ADD_FAILED);
        }

        return new Result<>("200", TotalMessage.BOT_ADD_SUCCESS);
    }
}
