package com.example.demo.util;

import org.springframework.stereotype.Component;
import sun.misc.BASE64Encoder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class HtmlUtil {
    /**
     *
     * @Title: imgInHtmlToBase
     * @Description:将html中img标签的src的url对应的图片替换成base64的图片
     * @param strHtml
     * @return
     *
     */
    public static String imgInHtmlToBase(String strHtml) {
        StringBuffer sb = new StringBuffer();
        // 目前img标签标示有3种表达式
        // <img alt="" src="1.jpg"/> <img alt="" src="1.jpg"></img> <img alt=""
        // src="1.jpg">
        // 开始匹配content中的<img />标签
        Pattern p_img = Pattern.compile("<(img|IMG)(.*?)(/>|></img>|>)");
        Matcher m_img = p_img.matcher(strHtml);
        boolean result_img = m_img.find();
        if (result_img) {
            while (result_img) {
                StringBuffer sbSrc = new StringBuffer();
                // 获取到匹配的<img />标签中的内容
                String str_img = m_img.group(2);
                // 开始匹配<img />标签中的src
                Pattern p_src = Pattern
                        .compile("(src|SRC)=(\"|\')(.*?)(\"|\')");
                Matcher m_src = p_src.matcher(str_img);
                if (m_src.find()) {
                    // 取出img的src的url路径
                    String str_src = m_src.group(3);
                    // 将图片转成base64码
                    String base = imgToBase64(str_src);
                    m_src.appendReplacement(sbSrc, " src=\"" + str_src + "\" ");
                    m_img.appendReplacement(sb,
                            "<img class=\"img-responsive\" src=\"data:image/png;base64,"
                                    + base + "\" />");
                }
                // 结束匹配<img />标签中的src
                // 匹配strHtml中是否存在下一个<img />标签，有则继续以上步骤匹配<img />标签中的src
                result_img = m_img.find();
            }
            m_img.appendTail(sb);
            return sb.toString();
        } else {
            return strHtml;
        }

    }

    /**
     *
     * @Title: imgToBase64
     * @Description:根据图片的路径转成base64码
     * @param imgURL
     * @return
     *
     */
    public static String imgToBase64(String imgURL) {
        boolean flag = false;
        ByteArrayOutputStream outPut = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        try {
            // 创建URL
            URL url = new URL(imgURL);
            // 创建链接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(10 * 1000);

            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                flag = false;// 连接失败/链接失效/图片不存在
            }
            InputStream inStream = conn.getInputStream();
            int len = -1;
            while ((len = inStream.read(data)) != -1) {
                outPut.write(data, 0, len);
            }
            inStream.close();
            flag = true;
        } catch (IOException e) {
            flag = false;
        }
        // 异常情况下 返回一个图片丢失的base64的图片
        String failImg = "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";
        // 对字节数组Base64编码
        if (flag == true) {
            BASE64Encoder encoder = new BASE64Encoder();
            return encoder.encode(outPut.toByteArray());
        } else {
            return failImg;
        }
    }
}

