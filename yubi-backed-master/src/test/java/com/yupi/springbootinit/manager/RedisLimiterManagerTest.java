//package com.yupi.springbootinit.manager;
//
//import cn.hutool.http.HttpRequest;
//import cn.hutool.json.JSONArray;
//import cn.hutool.json.JSONObject;
//import cn.hutool.json.JSONUtil;
//import com.yupi.springbootinit.model.entity.Message;
//import com.yupi.springbootinit.service.MessageService;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import javax.annotation.Resource;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.*;
//@SpringBootTest
//class RedisLimiterManagerTest {
//
//    @Resource
//    private RedisLimiterManager redisLimiterManager;
//
//    @Resource
//    private MessageService messageService;
//
//    @Test
//    void doRateLimit() throws  InterruptedException {
//        String userId="1";
//        for (int i = 0; i < 2; i++) {
//            redisLimiterManager.doRateLimit(userId);
//            System.out.println("成功");
//        }
//        Thread.sleep(1000);
//        for (int i = 0; i < 5; i++) {
//            redisLimiterManager.doRateLimit(userId);
//            System.out.println("成功");
//        }
//    }
//
//
//
//    @Test
//    void testFetchPassage() {
//        // 1. 获取数据
//        String json = "{\"current\":1,\"pageSize\":8,\"sortField\":\"createTime\",\"sortOrder\":\"descend\",\"category\":\"文章\",\"reviewStatus\":1}";
//        String url = "https://www.code-nav.cn/api/post/search/page/vo";
//        String result = HttpRequest
//                .post(url)
//                .body(json)
//                .execute()
//                .body();
////        System.out.println(result);
//        // 2. json 转对象
//        Map<String, Object> map = JSONUtil.toBean(result, Map.class);
//        JSONObject data = (JSONObject) map.get("data");
//        JSONArray records = (JSONArray) data.get("records");
//        List<Message> messageList = new ArrayList<>();
//        for (Object record : records) {
//            JSONObject tempRecord = (JSONObject) record;
//            Message message = new Message();
//            message.setTitle(tempRecord.getStr("title"));
//            message.setContent(tempRecord.getStr("content"));
//            JSONArray tags = (JSONArray) tempRecord.get("tags");
//            List<String> tagList = tags.toList(String.class);
//            message.setTags(JSONUtil.toJsonStr(tagList));
//            message.setUserId(1L);
//            messageList.add(message);
//        }
////        System.out.println(postList);
//        // 3. 数据入库
//        boolean b = messageService.saveBatch(messageList);
//        Assertions.assertTrue(b);
//    }
//}