package com.atguigu.mybatisplus.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {


    @Override
    public void insertFill(MetaObject metaObject) {//插入时自动填充
        System.out.println("插入时自动填充：-------");
        this.setFieldValByName("createTime", new Date(), metaObject);//第一个字段必须是entity中的fieldName，并且第二个字段必须与第一个字段的类型一样
        this.setFieldValByName("updateTime", new Date(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {//更新时自动填充
        System.out.println("更新时自动填充：-------");
        this.setFieldValByName("updateTime", new Date(), metaObject);

    }
}
