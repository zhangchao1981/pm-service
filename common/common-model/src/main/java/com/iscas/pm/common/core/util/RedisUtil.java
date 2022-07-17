package com.iscas.pm.common.core.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.iscas.pm.common.core.web.exception.SimpleBaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
public class RedisUtil {

    private String errorMsgFormat="redis操作失败，msg={}";

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisUtil(RedisTemplate<String, Object> redisTemplate){
        this.redisTemplate = redisTemplate;
    }
    // =============================common============================

    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     * @return
     */
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }

            return true;
        } catch (Exception e) {
            log.error(errorMsgFormat,e.getMessage(),e);
            return false;
        }
    }

    /**
     * 根据key 获取过期时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }


    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            log.error(errorMsgFormat,e.getMessage(),e);
            return false;
        }
    }

    /**
     * 删除缓存
     *
     * @param key 可以传一个值 或多个
     */
    @SuppressWarnings("unchecked")
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }

    /**
     * 删除缓存
     *
     * @param keys key数组
     */
    public void del(List<String> keys) {
        redisTemplate.delete(keys);
    }


    // ============================String=============================

    /**
     * 获取单个值
     *
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 批量获取值
     *
     * @param keys key数组
     * @return
     */
    public Object multGet(List<String> keys) {
        return keys.isEmpty() ? null : redisTemplate.opsForValue().multiGet(keys);
    }

    /**
     * 更新单个键值
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            log.error(errorMsgFormat,e.getMessage(),e);
            return false;
        }
    }

    /**
     * 更新单个键值 并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            log.error(errorMsgFormat,e.getMessage(),e);
            return false;
        }
    }




    /**
     * 递减
     *
     * @param key   键
     * @param delta 要减少几(小于0)
     * @return
     */
    public long decr(String key, long delta) {
        if (delta < 0) {
            throw new SimpleBaseException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "递减因子必须大于0");
        }

        return redisTemplate.opsForValue().increment(key, -delta);
    }


    // ================================hashMap=================================

    /**
     * 获取指定map键对应的值,如果存在该键则获取值，没有则返回null
     *
     * @param key     redis键
     * @param hashKey hashMap键
     * @return hashMap键对应的值
     */
    public Object hget(String key, String hashKey) {
        return redisTemplate.opsForHash().get(key, hashKey);
    }

    /**
     * 批量获取指定map键对应的值
     *
     * @param key      redis键
     * @param hashKeys hashMap键集合
     * @return hashMap键集合对应的值集合
     */
    public List<Object> hmGet(String key, Collection<Object> hashKeys) {
        return redisTemplate.opsForHash().multiGet(key, hashKeys);
    }


    /**
     * 获取hash表中对应的所有键值
     *
     * @param key redis键
     * @return 对应的多个键值
     */
    public Map<Object, Object> hmGet(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 获取hash表中所有值
     *
     * @param key redis键
     * @return 对应的多个键值
     */
    public List<Object> hmGetValues(String key) {
        return redisTemplate.opsForHash().values(key);
    }

    /**
     * 更新单个hashKey的值
     *
     * @param key     redis键
     * @param hashKey hash键
     * @param value   hash值
     * @return true 成功 false失败
     */
    public boolean hset(String key, String hashKey, Object value) {
        try {
            redisTemplate.opsForHash().put(key, hashKey, value);
            return true;
        } catch (Exception e) {
            log.error(errorMsgFormat,e.getMessage(),e);
            return false;
        }
    }

    /**
     * 更新单个hashKey的值 并设置过期时间
     *
     * @param key     redis键
     * @param hashKey hash键
     * @param value   hash值
     * @param time    过期时间
     * @return
     */
    public boolean hset(String key, String hashKey, Object value, long time) {
        try {
            redisTemplate.opsForHash().put(key, hashKey, value);

            if (time > 0) {
                expire(key, time);
            }

            return true;

        } catch (Exception e) {
            log.error(errorMsgFormat,e.getMessage(),e);
            return false;
        }
    }




    /**
     * 批量设置map键值 并设置过期时间
     *
     * @param key  redis键
     * @param map  多个键值
     * @param time 时间(秒)
     * @return true成功 false失败
     */
    public boolean hmset(String key, Map<Object, Object> map, long time) {
        try {
            redisTemplate.opsForHash().putAll(key, map);

            if (time > 0) {
                expire(key, time);
            }
            return true;

        } catch (Exception e) {
            log.error(errorMsgFormat,e.getMessage(),e);
            return false;
        }
    }

    /**
     * 删除指定键的值
     *
     * @param key     redis键 不能为null
     * @param hashKey hash键 可以使多个 不能为null
     */
    public void hdel(String key, Object... hashKey) {
        redisTemplate.opsForHash().delete(key, hashKey);
    }

    /**
     * 批量删除hash中的键field
     *
     * @param key     redis键 不能为null
     * @param hashKeys hash键 集合
     */
    public void hmdel(String key, List<String> hashKeys) {
        RedisSerializer keySerializer = redisTemplate.getKeySerializer();
        RedisSerializer hashKeySerializer = redisTemplate.getHashKeySerializer();
        byte[] keyByte = keySerializer.serialize(key);
        redisTemplate.executePipelined(
                (RedisCallback<Object>) connection -> {
                    hashKeys.forEach(k-> {
                        byte[] hashKey = hashKeySerializer.serialize(k);
                        assert keyByte != null;
                        connection.hDel(keyByte,hashKey);
                    });
                    return null;
                });
    }

    /**
     * 判断hash表中是否有指定键的值
     *
     * @param key     键 不能为null
     * @param hashKey 项 不能为null
     * @return true 存在 false不存在
     */
    public boolean hHasKey(String key, String hashKey) {
        return redisTemplate.opsForHash().hasKey(key, hashKey);
    }

    /**
     * 获取hash表的长度
     *
     * @param key redis键
     * @return
     */
    public long hsize(String key) {
        return redisTemplate.opsForHash().size(key);
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key  键
     * @param item 项
     * @param by   要增加几(大于0)
     * @return
     */
    public double hincr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, by);
    }

    /**
     * hash递减
     *
     * @param key  键
     * @param item 项
     * @param by   要减少记(小于0)
     * @return
     */
    public double hdecr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, -by);
    }



    // ============================set=============================

    /**
     * 根据key获取Set中的所有值
     *
     * @param key 键
     * @return
     */
    public Set<Object> sGet(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            log.error(errorMsgFormat,e.getMessage(),e);
            return Sets.newHashSet();
        }
    }

    /**
     * 根据value从一个set中查询,是否存在
     *
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */
    public boolean sHasKey(String key, Object value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            log.error(errorMsgFormat,e.getMessage(),e);
            return false;
        }
    }

    /**
     * 将数据放入set缓存
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sSet(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            log.error(errorMsgFormat,e.getMessage(),e);
            return 0;
        }
    }

    /**
     * 将set数据放入缓存
     *
     * @param key    键
     * @param time   时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sSetAndTime(String key, long time, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().add(key, values);

            if (time > 0) {
                expire(key, time);
            }
            return count;
        } catch (Exception e) {
            log.error(errorMsgFormat,e.getMessage(),e);
            return 0;
        }
    }

    /**
     * 获取set缓存的长度
     *
     * @param key 键
     * @return
     */
    public long sGetSetSize(String key) {
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            log.error(errorMsgFormat,e.getMessage(),e);
            return 0;
        }
    }

    /**
     * 移除值为value的
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public long setRemove(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().remove(key, values);
        } catch (Exception e) {
            log.error(errorMsgFormat,e.getMessage(),e);
            return 0;
        }
    }


    // ===============================list=================================

    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束 0 到 -1代表所有值
     * @return
     */
    public List<Object> lGet(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            log.error(errorMsgFormat,e.getMessage(),e);
            return Lists.newArrayList();
        }
    }

    /**
     * 获取list缓存的长度
     *
     * @param key 键
     * @return
     */
    public long lGetListSize(String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            log.error(errorMsgFormat,e.getMessage(),e);
            return 0;
        }
    }

    /**
     * 通过索引 获取list中的值
     *
     * @param key   键
     * @param index 索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     */
    public Object lGetIndex(String key, long index) {
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            log.error(errorMsgFormat,e.getMessage(),e);
            return null;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public boolean lSet(String key, Object value) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            log.error(errorMsgFormat,e.getMessage(),e);
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    public boolean lSet(String key, Object value, long time) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error(errorMsgFormat,e.getMessage(),e);
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public boolean lSet(String key, List<Object> value) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception e) {
            log.error(errorMsgFormat,e.getMessage(),e);
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    public boolean lSet(String key, List<Object> value, long time) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error(errorMsgFormat,e.getMessage(),e);
            return false;
        }
    }

    /**
     * 根据索引修改list中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return
     */
    public boolean lUpdateIndex(String key, long index, Object value) {
        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            log.error(errorMsgFormat,e.getMessage(),e);
            return false;
        }
    }

    /**
     * 移除N个值为value
     *
     * @param key   键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    public long lRemove(String key, long count, Object value) {
        try {
            return redisTemplate.opsForList().remove(key, count, value);
        } catch (Exception e) {
            log.error(errorMsgFormat,e.getMessage(),e);
            return 0;
        }
    }
}
