package com.kob.backend;

import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordTest {
    @Test
    void name() {
        String password = "Abc123@1{"; // 替换为你的密码

        if (isValidPassword(password)) {
            System.out.println("密码符合要求");
        } else {
            System.out.println("密码不符合要求");
        }
    }

    public static boolean isValidPassword(String password) {
        // 至少8位，包含大写字母、小写字母、数字和特殊字符
        String pattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()-_+={}|:;'<>,.?~`]).{8,}$";
        Pattern regexPattern = Pattern.compile(pattern);
        Matcher matcher = regexPattern.matcher(password);

        return matcher.matches();
    }


}
