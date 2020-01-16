package com.kiki.skill.redis;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author  kiKi
 */
@SuppressWarnings({"ALL", "AliControlFlowStatementWithoutBraces"})
@Service
public class RedisService {

    @Autowired
    JedisPool jedisPool;

    /**
     * 有前缀的set，设置单个，或者多个对象
     * @param prefix  // 多态
     * @param key
     * @param value
     * @param <T>
     * @return
     */
    public <T>boolean set(KeyPrefix prefix, String key, T value){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            String realKey = prefix.getPrefix()+key;
            String str = beanToString(value);
            if(str == null || str.length() <= 0){
                return false;
            }
            //判断过期时间
            int seconds = prefix.expireSeconds();
            if(seconds <= 0){
                jedis.set(realKey,str);
            }else {//自己设置过期时间
                jedis.setex(realKey,seconds,str);
            }
            return true;
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * 无前缀，set
     * @param key
     * @param value
     * @param <T>
     * @return
     */
    public <T>boolean set(String key, T value){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            String str = beanToString(value);
            if(str == null || str.length() <= 0){
                return false;
            }
            jedis.set(key,str);
            return true;
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * 又前缀get，获取单个对象
     * @param prefix
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T>T get(KeyPrefix prefix, String key, Class<T> clazz){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            String realKey = prefix.getPrefix()+key;
            String str = jedis.get(realKey);
            T t = stringToBean(str,clazz);
            return t;
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * 无前缀get
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T>T get(String key, Class<T> clazz){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            String str = jedis.get(key);
            T t = stringToBean(str,clazz);
            return t;
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * 增加值
     * @param prefix
     * @param key
     * @param <T>
     * @return
     */
    public <T> Long incr(KeyPrefix prefix, String key){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            String realKey = prefix.getPrefix()+key;
            return jedis.incr(realKey);
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * 减少值
     * @param prefix
     * @param key
     * @param <T>
     * @return
     */
    public <T> Long decr(KeyPrefix prefix, String key){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            String realKey = prefix.getPrefix()+key;
            return jedis.decr(realKey);
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * 判断可以是否存在
     * @param prefix
     * @param key
     * @param <T>
     * @return
     */
    public <T> Boolean existsKey(KeyPrefix prefix, String key){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            String realKey = prefix.getPrefix()+key;
            return jedis.exists(realKey);
        }finally {
            returnToPool(jedis);
        }
    }

//    public <T> Long  del(KeyPrefix prefix, String key){
//        Jedis jedis = null;
//        try{
//            jedis = jedisPool.getResource();
//            String realKey = prefix.getPrefix() + key;
//            return jedis.del(realKey);
//        }finally {
//            returnToPool(jedis);
//        }
//    }
    /**
     * 将字符串转换成Bean对象
     * @param str
     * @param <T>
     * @return
     */
    public <T>T stringToBean(String str,Class<T> clazz) {
        if(str == null || str.length() <= 0 || clazz == null){
            return null;
        }else if(clazz == int.class || clazz == Integer.class){
            return (T) Integer.valueOf(str);
        }else if(clazz == long.class || clazz == Long.class){
            return (T) Long.valueOf(str);
        }else if(clazz == double.class || clazz == Double.class){
            return (T) Double.valueOf(str);
        }else if(clazz == String.class){
            return (T) str;
        }else{
            return JSON.toJavaObject(JSON.parseObject(str),clazz);
        }
    }

    /**
     * 将Bean对象转换成 String
     * @param value
     * @param <T>
     * @return
     */
    public <T>String  beanToString(T value) {
        if(value == null){
            return null;
        }else if(value == long.class || value == Long.class){
            return ""+value;
        }else if(value == String.class){
            return (String) value;
        }else if(value == int.class || value == Integer.class){
            return ""+value;
        }else if(value == double.class || value == Double.class){
            return ""+value;
        }else{
            return JSON.toJSONString(value);
        }
    }

    /**
     * 删除
     * @param prefix
     * @param key
     * @return
     */
    public boolean del(KeyPrefix prefix, String key){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            String realKey = prefix.getPrefix()+key;
            long result = jedis.del(realKey);
            return result > 0;
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * 关闭jedis的连接
     * @param jedis
     */
    @SuppressWarnings("AliControlFlowStatementWithoutBraces")
    private void returnToPool(Jedis jedis) {
        //noinspection AliControlFlowStatementWithoutBraces
        if(jedis != null) {
            jedis.close();
        }
    }
}
