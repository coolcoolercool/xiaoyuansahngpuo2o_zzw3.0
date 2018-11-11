package com.zzw.o2o.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

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

    /**
     * 生成二维码的图片流
     * @param content
     * @param resp
     * @return
     */
    public static BitMatrix generateQRCCodeStream(String content, HttpServletResponse resp){
        //给相应添加头部信息,主要是告诉浏览器返回的是图片流
        resp.setHeader("Cache-Control","no-store");
        resp.setHeader("Pragma","no-cache");
        resp.setDateHeader("Expires", 0);
        resp.setContentType("image/png");
        //设置图片文字编码以及内边边框
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, 0);
        BitMatrix bitMatrix;
        try {
            //参数顺序分别为: 编码内容,编码类型,生成图片宽度,申城图片高度,设置参数
            bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, 300, 300, hints);
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
        return bitMatrix;
    }
}
