package com.zzw.o2o.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * author: zzw5005
 * date: 2018/10/27 21:36
 */

/*
* 用于验证码校验
* */
public class CodeUtil {
    private static final Logger logger = LoggerFactory.getLogger(CodeUtil.class);
    /**
     * 验证码校验
     * @param request
     * @return
     */
    public static boolean checkVerifyCode(HttpServletRequest request){
        // 图片中的验证码
        String verifyCodeExpected = (String) request.getSession().getAttribute(
                com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
        logger.debug("verifyCodeExpected:{}", verifyCodeExpected);
        // 用户输入的验证码
        String verifyCodeActual = HttPServletRequestUtil.getString(request,
                "verifyCodeActual");
        logger.debug("verifyCodeActual:{}", verifyCodeActual);
        if (verifyCodeActual == null
                || !verifyCodeActual.equalsIgnoreCase(verifyCodeExpected)) {
            return false;
        }
        return true;
    }
}
