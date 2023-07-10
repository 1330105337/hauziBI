package com.yupi.springbootinit;

import cn.hutool.http.HttpUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

/**
 * 主类测试
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
//@SpringBootTest
class MainApplicationTests {


    @Test
    void test(){
//        HashMap<String, Object> paramMap = new HashMap<>();
//        paramMap.put("city", "北京");
        int id=1330105337;
        String result= HttpUtil.get("https://api.btstu.cn/qqxt/api.php?qq="+id);
        System.out.println(result);
    }

    @Test
    void test1(){
//        HashMap<String, Object> paramMap = new HashMap<>();
//        paramMap.put("city", "北京");
        int id=1330105337;
        for (int i=0;i<10;i++) {
            String result = HttpUtil.get("https://api.btstu.cn/yan/api.php?charset=utf-8&encode=json");
            System.out.println(result);
        }
    }



//    @Test
//    String contextLoads() {
//        String url="https://api.map.baidu.com/telematics/v3/weather?location=\"+city+\"&output=xml&ak=AnoXuNe4ciKZIGWGeVpqklXIeUyWqLDX";
//        StringBuilder json = new StringBuilder();
//        try {
//            URL urlObject = new URL(url);
//            URLConnection uc = urlObject.openConnection();
//            BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream(),"utf-8"));
//            String inputLine = null;
//            while ( (inputLine = in.readLine()) != null) {
//                json.append(inputLine);
//            }
//            in.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        //System.out.println(json.toString());
//        return json.toString();
//
//    }

}
