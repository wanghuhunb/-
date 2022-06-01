package com.itheima;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@SpringBootTest(classes = ReggieApplication.class)
public class Test01 {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void test01() {
        stringRedisTemplate.opsForValue().set("17376596763", "1234");
        String s = stringRedisTemplate.opsForValue().get("17376596763");
        //设置生存时间
        stringRedisTemplate.expire("17376596763", 20, TimeUnit.MINUTES);
        System.out.println(s);
    }

    @Test
    public void test02() {
        stringRedisTemplate.opsForHash().put("卞正伟", "1", "芭比娃娃");
        stringRedisTemplate.opsForHash().put("卞正伟", "2", "变形金刚");
        stringRedisTemplate.opsForHash().put("卞正伟", "3", "金刚芭比");
        Object o = stringRedisTemplate.opsForHash().get("卞正伟", "1");
        System.out.println(o);
        Set<Object> keys = stringRedisTemplate.opsForHash().keys("卞正伟");
        System.out.println(keys);

        List<Object> 卞正伟 = stringRedisTemplate.opsForHash().values("卞正伟");
        System.out.println(卞正伟);
    }

    @Test
    public void test03() {
      /*  stringRedisTemplate.opsForList().leftPush("哇哈哈","第一");
        stringRedisTemplate.opsForList().leftPush("哇哈哈","第二");
        stringRedisTemplate.opsForList().leftPush("哇哈哈","第三");
        stringRedisTemplate.opsForList().leftPush("哇哈哈","第四");*/

        //stringRedisTemplate.opsForList().leftPop("")
        String s = stringRedisTemplate.opsForList().rightPop("哇哈哈");
        System.out.println(s);
        Long 哇哈哈 = stringRedisTemplate.opsForList().size("哇哈哈");
        System.out.println(哇哈哈);
        List<String> 哇哈哈1 = stringRedisTemplate.opsForList().range("哇哈哈", 0, -1);
        System.out.println(哇哈哈1);
    }

    @Test
    public void test04() {
        //有序对列表
        stringRedisTemplate.opsForZSet().add("好人榜", "1", 200);
        stringRedisTemplate.opsForZSet().add("好人榜", "2", 2000);
        stringRedisTemplate.opsForZSet().add("好人榜", "3", 20000);
        stringRedisTemplate.opsForZSet().add("好人榜", "4", 200000);
        Set<String> 好人榜 = stringRedisTemplate.opsForZSet().range("好人榜", 0, 2);

        System.out.println(好人榜);
        Long 好人榜1 = stringRedisTemplate.opsForZSet().remove("好人榜", "1");
        System.out.println(好人榜1);
        stringRedisTemplate.opsForZSet().incrementScore("好人榜", "4", 20000);

        Set<String> 好人榜2 = stringRedisTemplate.opsForZSet().range("好人榜", 0, -1);
        System.out.println(好人榜2);
    }
    @Test
    public void test06(){
        //通用方法
        Set<String> keys = stringRedisTemplate.keys("*");
        System.out.println(keys);
        stringRedisTemplate.delete("土豪帮");
        stringRedisTemplate.hasKey("土豪帮");
        DataType 好人榜 = stringRedisTemplate.type("好人榜");
        System.out.println(好人榜);
        stringRedisTemplate.expire("好人榜",5,TimeUnit.DAYS);
    }
}
