package com.kob.backend.utils.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtil {

    /**
     * 验证密码合法性
     * @param password
     * @return
     */
    public static boolean isValidPassword(String password) {
        // 至少8位，包含大写字母、小写字母、数字和特殊字符
        String pattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()-_+={}|:;'<>,.?~`]).{8,}$";
        Pattern regexPattern = Pattern.compile(pattern);
        Matcher matcher = regexPattern.matcher(password);

        return matcher.matches();
    }

}
