package com.example.demo.controller;

import com.example.demo.util.HtmlUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;


import java.io.*;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.DocumentEntry;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Encoder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;

@RestController
@Api(value = "测试导出", tags = "{测试API}")
public class WordController {
    @PostMapping(value = "import")
    @ApiOperation(value = "导出测试")
    public void exportWord(HttpServletRequest request, HttpServletResponse response) {

        String title = "李云龙测试";
        String text = "测试项目1";
        String imgPath = "src/main/resources/img/wechat.jpg";

        try {
            //word内容
            String content = "<html><body>" +
                    "<p style=\"text-align: center;\"><span style=\"font-family: 黑体, SimHei; font-size: 200px;\">"
                    + title + "</span></p>" + "<font color=\"red\">" + text + "</font>" +
                    "<img src= " + imgPath + " />" + "</body></html>";

            /*String strHtml = "<p>测试</p><img src=\"https://avatar.csdn.net/9/9/B/3_asuyunlong.jpg\" class=\"avatar_pic\">"
                    + "<span class=\"read-count\">阅读数：2448</span>"
                    + "<img data-v-0d738edb=\"\" src=\"https://profile.csdnimg.cn/7/2/7/1_qq_24484085\" alt=\"\" class=\"head\">";

            String content = HtmlUtil.imgInHtmlToBase(strHtml);*/

            //HtmlUtil.imgInHtmlToBase(content);
            //getImageStr(imgPath);
            byte b[] = content.getBytes("GBK");  //这里是必须要设置编码的，不然导出中文就会乱码。
            ByteArrayInputStream bais = new ByteArrayInputStream(b);//将字节数组包装到流中

            /*
             * 关键地方
             * 生成word格式 */
            POIFSFileSystem poifs = new POIFSFileSystem();
            DirectoryEntry directory = poifs.getRoot();
            DocumentEntry documentEntry = directory.createDocument("WordDocument", bais);
            //输出文件
            request.setCharacterEncoding("utf-8");
            response.setContentType("application/msword");//导出word格式
            response.addHeader("Content-Disposition", "attachment;filename=" +
                    new String(title.getBytes("GB2312"), "iso8859-1") + ".doc");
            ServletOutputStream ostream = response.getOutputStream();
            poifs.writeFilesystem(ostream);

            //输出文件的话，new一个文件流
            //FileOutputStream fostream = new FileOutputStream("D:/cccc.doc");
            //poifs.writeFilesystem(fostream);
            bais.close();
            ostream.close();
            poifs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
        public String getImageStr(String imgFile){
            InputStream in=null;
            byte[] data=null;
            try {
                in=new FileInputStream(imgFile);
                data=new byte[in.available()];
                in.read(data);
                in.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            BASE64Encoder encoder=new BASE64Encoder();
            return encoder.encode(data);
        }


        /*public static String getBodyString() throws IOException {
            File file = new File("D:\\cz\\壮\\桌面资料\\工作资料\\流程复盘节点确定.html");
            InputStream in = new FileInputStream(file);
            InputStreamReader reader = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(reader);
            String s = null;
            StringBuffer buff = new StringBuffer();
            while ((s = br.readLine()) != null) {
                buff.append(s);
            }
            br.close();
            reader.close();
            in.close();
            System.out.println(buff.toString());
            buff.delete(0,buff.indexOf("<head>"));
            buff.deleteCharAt(buff.indexOf("</html>"));
        *//*StringBuffer buffStyle = new StringBuffer();
        //截取样式代码
        buffStyle.append(buff.substring(buff.indexOf("<style type=\"text/css\">") + 23, buff.indexOf("style", buff.indexOf("<style type=\"text/css\">") + 23) - 2));
        System.out.println(buffStyle);
        //截取body代码
        String body = buff.substring(buff.indexOf("<body"), buff.indexOf("</body") + 7);
        body = body.replaceAll("body", "div");
        StringBuffer bodyBuffer = new StringBuffer(body);
        System.out.println(bodyBuffer);
        String[] split = buffStyle.toString().split("}");
        Map<String, String> styleMap = new HashMap<>();
        for (String s1 : split) {
            System.out.println(s1);
            String[] split1 = s1.split("\\{");
            styleMap.put(split1[0].substring(1), split1[1]);
        }
        Set<String> strings = styleMap.keySet();
        for (String key : strings) {
            System.out.print("key : " + key);
            System.out.println("   value : " + styleMap.get(key));
            //将嵌入样式转换为行内样式
            if (bodyBuffer.toString().contains(key)) {
                int length = bodyBuffer.toString().split(key).length - 1;
                int temp = 0;
                for (int i = 0; i < length; i++) {
                    temp = bodyBuffer.indexOf(key, temp);
                    //这个是每次查询到的位置，判断此标签中是否添加了style标签
                    String isContaionStyle = bodyBuffer.substring(temp, bodyBuffer.indexOf(">", temp));
                    if (isContaionStyle.contains("style")) {
                        //代表已经存在此style，那么直接加进去就好了
                        //首先找到style的位置
                        int styleTemp = bodyBuffer.indexOf("style", temp);
                        bodyBuffer.insert(styleTemp + 7, styleMap.get(key));
                    } else {
                        //代表没有style，那么直接插入style
                        int styleIndex = bodyBuffer.indexOf("\"", temp);
                        bodyBuffer.insert(styleIndex + 1, " style=\"" + styleMap.get(key) + "\"");
                    }
                    temp++;
                }
            }
            System.out.println(bodyBuffer.toString());
        }*//*
            System.out.println(buff.toString());
            //return buff.toString();
        }
    }*/

}
