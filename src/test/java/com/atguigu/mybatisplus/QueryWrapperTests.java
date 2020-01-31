package com.atguigu.mybatisplus;

import com.atguigu.mybatisplus.entity.User;
import com.atguigu.mybatisplus.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class QueryWrapperTests {
    @Autowired
    private UserMapper userMapper;

    /*
    ge:greater and equals 大于等于
    gt:greater than 大于
    le:less and equals 小于等于
    lt:less than 小于
    isNull: 为空
    isNotNull：不为空
     */
    @Test
    public void testDelete() {

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .isNull("name")
                .ge("age", 12)
                .isNotNull("email");
        int result = userMapper.delete(queryWrapper);
        System.out.println("delete return count = " + result);
    }

    /*
    eq:equals 等于
    ne:not equals 不等于
     */
    @Test
    public void testSelectOne() {//只能查询一条记录，多于一条便报错

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", "Tom");

        User user = userMapper.selectOne(queryWrapper);
        System.out.println(user);
    }
    /*
    between: 包括
    notBetween ：不包括
     */
    @Test
    public void testSelectCount() {//包括大小边界，即可以取到20和30

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("age", 20, 30);

        Integer count = userMapper.selectCount(queryWrapper);
        System.out.println(count);
    }

    //allEq: 所有条件都是等于的
    @Test
    public void testSelectList() {

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        Map<String, Object> map = new HashMap<>();
        map.put("id", 2);
        map.put("name", "Jack");
        map.put("age", 20);

        queryWrapper.allEq(map);
        List<User> users = userMapper.selectList(queryWrapper);

        users.forEach(System.out::println);
    }

    /*
    like: 包含
    notLike: 不包含
    likeLeft: 这个val左边模糊匹配(%val)
    likeRight: 这个val右面模糊匹配(val%)
     */
    @Test
    public void testSelectMaps() {

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .notLike("name", "e")
                .likeRight("email", "t");

        List<Map<String, Object>> maps = userMapper.selectMaps(queryWrapper);//返回值是Map列表
        maps.forEach(System.out::println);
    }

    /*
    in: notIn("age",{1,2,3})--->age not in (1,2,3)
    notIn: notIn("age", 1, 2, 3)--->age not in (1,2,3)
    inSql:子查询中包括 ：inSql("age", "1,2,3,4,5,6")--->age in (1,2,3,4,5,6)；inSql("id", "select id from table where id < 3")--->id in (select id from table where id < 3)
    notinSql: 子查询中不包括：
    exists:
    notExists:
     */
    @Test
    public void testSelectObjs() {

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        //queryWrapper.in("id", 1, 2, 3);
        queryWrapper.inSql("id", "select id from user where id < 3");

        List<Object> objects = userMapper.selectObjs(queryWrapper);//返回值是Object列表
        objects.forEach(System.out::println);
    }


    /*
    不调用or则默认为使用 and ，
    or表示或
    and表示和
     */
    @Test
    public void testUpdate1() {

        //修改值
        User user = new User();
        user.setAge(99);
        user.setName("Andy");

        //修改条件
        UpdateWrapper<User> userUpdateWrapper = new UpdateWrapper<>();
        userUpdateWrapper
                .like("name", "h")
                .or()
                .between("age", 20, 30);

        int result = userMapper.update(user, userUpdateWrapper);

        System.out.println(result);
    }

    /*
    使用lambda表达式进行嵌套or、嵌套and操作，跟testUpdate1类似，
    当需要组装较为复杂的sql查询条件时，可以使用lambda表达式提升语句优先级，即可做到嵌套
     */
    @Test
    public void testUpdate2() {


        //修改值
        User user = new User();
        user.setAge(99);
        user.setName("Andy");

        //修改条件
        UpdateWrapper<User> userUpdateWrapper = new UpdateWrapper<>();
        userUpdateWrapper
                .like("name", "h")
                .or(i -> i.eq("name", "李白").ne("age", 20));//or里面有and

        int result = userMapper.update(user, userUpdateWrapper);

        System.out.println(result);
    }


    /*
    orderBy:
    orderByDesc:查询结果倒序排列
    orderByAsc:查询结果正序排列
     */
    @Test
    public void testSelectListOrderBy() {

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");

        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    //直接拼接到 sql 的最后，只能调用一次,多次调用以最后一次为准，谨慎使用，有sql注入风险
    //在sql最后添加自定义代码片段
    @Test
    public void testSelectListLast() {

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.last("limit 1");

        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    //指定要查询的列,不需要展示表中所有的列
    @Test
    public void testSelectListColumn() {

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "name", "age");

        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    //为更新操作指定查询条件
    @Test
    public void testUpdateSet() {

        //修改值
        User user = new User();
        user.setAge(99);

        //修改条件
        UpdateWrapper<User> userUpdateWrapper = new UpdateWrapper<>();
        userUpdateWrapper
                .like("name", "h")
                .set("name", "老李头")//除了可以查询还可以使用set设置修改的字段
                .setSql(" email = '123@qq.com'");//可以有子查询

        int result = userMapper.update(user, userUpdateWrapper);
    }
}
