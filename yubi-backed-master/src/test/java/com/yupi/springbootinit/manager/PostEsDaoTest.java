//package com.yupi.springbootinit.manager;
//
//import com.yupi.springbootinit.esdao.PostEsDao;
//import com.yupi.springbootinit.model.dto.message.MessageQueryRequest;
//import com.yupi.springbootinit.model.dto.message.PostEsDTO;
//import com.yupi.springbootinit.model.entity.Message;
//import com.yupi.springbootinit.service.MessageService;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Sort;
//
//import javax.annotation.Resource;
//import java.util.Arrays;
//import java.util.Date;
//import java.util.List;
//import java.util.Optional;
//
///**
// * 帖子 ES 操作测试
// *
// * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
// * @from <a href="https://yupi.icu">编程导航知识星球</a>
// */
//@SpringBootTest
//public class PostEsDaoTest {
//
//    @Resource
//    private PostEsDao postEsDao;
//
//    @Resource
//    private MessageService postService;
//
//    @Test
//    void test() {
//        MessageQueryRequest postQueryRequest = new MessageQueryRequest();
//        com.baomidou.mybatisplus.extension.plugins.pagination.Page<Message> page =
//                postService.searchFromEs(postQueryRequest);
//        System.out.println(page);
//    }
//
//    @Test
//    void testSelect() {
//        System.out.println(postEsDao.count());
//        Page<PostEsDTO> PostPage = postEsDao.findAll(
//                PageRequest.of(0, 5, Sort.by("createTime")));
//        List<PostEsDTO> postList = PostPage.getContent();
//        Optional<PostEsDTO> byId = postEsDao.findById(1L);
//        System.out.println(byId);
//        System.out.println(postList);
//    }
//
//    @Test
//    void testAdd() {
//        PostEsDTO postEsDTO = new PostEsDTO();
//        postEsDTO.setId(2L);
//        postEsDTO.setTitle("鱼皮是小黑子");
//        postEsDTO.setContent("鱼皮的知识星球：https://yupi.icu，直播带大家做项目");
//        postEsDTO.setTags(Arrays.asList("java", "python"));
//        postEsDTO.setUserId(1L);
//        postEsDTO.setCreateTime(new Date());
//        postEsDTO.setUpdateTime(new Date());
//        postEsDTO.setIsDelete(0);
//        postEsDao.save(postEsDTO);
//        System.out.println(postEsDTO.getId());
//    }
//
//    @Test
//    void testFindById() {
//        Optional<PostEsDTO> postEsDTO = postEsDao.findById(1L);
//        System.out.println(postEsDTO);
//    }
//
//    @Test
//    void testCount() {
//        System.out.println(postEsDao.count());
//    }
//
//    @Test
//    void testFindByCategory() {
//        List<PostEsDTO> postEsDaoTestList = postEsDao.findByUserId(1L);
//        System.out.println(postEsDaoTestList);
//    }
//
//    @Test
//    void testFindByTitle() {
//        List<PostEsDTO> postEsDTOS = postEsDao.findByTitle("鱼皮是小黑子");
//        System.out.println(postEsDTOS);
//    }
//
//}
