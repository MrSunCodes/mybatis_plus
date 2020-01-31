package com.atguigu.mybatisplus;

import com.atguigu.mybatisplus.entity.User;
import com.atguigu.mybatisplus.mapper.UserMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CRUDTests {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testInsert() {
        User user = new User();
        user.setAge(15);
        user.setEmail("790567648@qq.com");
        user.setName("Chris");

        int result = userMapper.insert(user);
        System.out.println(result); //影响的行数
        System.out.println(user); //id自动回填
        //新在数据库中生成的一行数据的id为19位，可以判断是分布式id生成器所生成
    }

    @Test
    public void testUpdate() {
        User user = new User();
        user.setId(1222451509303422980L);//设置要修改的id

        user.setEmail("text@mail.com");//设置要修改的内容
        int result = userMapper.updateById(user);//result为影响的行数，传entity类型参数即user
        System.out.println(result);

    }

    /**
     * 测试 乐观锁插件
     */
    @Test
    public void testOptimisticLocker() {

        //必须要先查询，用于获取version的值
        User user = userMapper.selectById(1222451509303422980L);
        //修改数据
        user.setName("Helen Yao");
        user.setEmail("helen@qq.com");
        //执行更新
        userMapper.updateById(user);
    }

    @Test
    public void testSelectById() {//单个id查询

        User user = userMapper.selectById(1L);
        System.out.println(user);
    }

    @Test
    public void testSelectBatchIds(){//多个id查询

        List<User> users = userMapper.selectBatchIds(Arrays.asList(1, 2, 3));
        users.forEach(System.out::println);
    }

    @Test
    public void testSelectByMap(){//使用map封装查询条件

        HashMap<String, Object> map = new HashMap<>();
        map.put("name", "Tom");
        map.put("age", 28);
        List<User> users = userMapper.selectByMap(map);

        users.forEach(System.out::println);
    }

    @Test
    public void testSelectPage() {

        Page<User> page = new Page<>(1,5);//第一个参数:页码，第二个参数:每页的数量
        userMapper.selectPage(page, null);

        page.getRecords().forEach(System.out::println);
        System.out.println(page.getCurrent());//当前页码
        System.out.println(page.getPages());//总页数
        System.out.println(page.getSize());//每页记录数
        System.out.println(page.getTotal());//总记录数
        System.out.println(page.hasNext());//是否有下一页内容
        System.out.println(page.hasPrevious());//是否有上页内容
    }

    @Test
    public void testSelectMapsPage() {//结果集是Map

        Page<User> page = new Page<>(1, 5);

        IPage<Map<String, Object>> mapIPage = userMapper.selectMapsPage(page, null);

        //注意：此行必须使用 mapIPage 获取记录列表，否则会有数据类型转换错误
        mapIPage.getRecords().forEach(System.out::println);
        System.out.println(page.getCurrent());
        System.out.println(page.getPages());
        System.out.println(page.getSize());
        System.out.println(page.getTotal());
        System.out.println(page.hasNext());
        System.out.println(page.hasPrevious());
    }

    @Test
    public void testDeleteById(){

        int result = userMapper.deleteById(1L);
        System.out.println(result);
    }

    @Test
    public void testDeleteBatchIds() {

        int result = userMapper.deleteBatchIds(Arrays.asList(2, 3));
        System.out.println(result);
    }

    /*
    简单条件删除：先组装hashmap，再使用deleteByMap方法进行删除
     */
    @Test
    public void testDeleteByMap() {

        HashMap<String, Object> map = new HashMap<>();
        map.put("name", "Sandy");
        map.put("age", 21);

        int result = userMapper.deleteByMap(map);
        System.out.println(result);
    }

    /**
     * 测试 逻辑删除
     */
    @Test
    public void testLogicDelete() {

        int result = userMapper.deleteById(5L);
        System.out.println(result);
    }

    /**
     * 测试 逻辑删除后的查询：
     * 不包括被逻辑删除的记录
     */
    @Test
    public void testLogicDeleteSelect() {
        User user = new User();
        List<User> users = userMapper.selectList(null);
        users.forEach(System.out::println);
    }

}
