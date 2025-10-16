package com.maybank.platform.services.util;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Range;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import org.springframework.data.redis.core.ZSetOperations;

import org.springframework.beans.factory.annotation.Autowired;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RedisUtil {
    private static final Logger log = LoggerFactory.getLogger(RedisUtil.class);
    final Range<Double> range = Range.unbounded();

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public boolean put(String key, Object object) {
        return this.put(key, object, 60L);
    }

    public boolean put(String key, Object object, long activeTime) {
        return this.redisTemplate.execute((RedisCallback<Boolean>) (connection) -> {
            try {
                byte[] keys = key.getBytes();
                byte[] value = JsonUtil.toJsonAsBytes(object);
                if (activeTime > 0L) {
                    connection.setEx(keys, activeTime, value);
                } else {
                    connection.set(keys, value);
                }
                return true;
            } catch (Exception var7) {
                log.error("put error", var7);
                return false;
            }
        });
    }

    public <T> T get(String key, Class<T> clz) {
        return this.redisTemplate.execute((RedisCallback<T>) (connection) -> {
            try {
                byte[] value = connection.get(key.getBytes());
                return value == null ? null : JsonUtil.parseJsonAsBytes(value, clz);
            } catch (Exception var4) {
                log.error("get error", var4);
                return null;
            }
        });
    }

    public <T> List<T> mGet(List<String> keys, Class<T> clz) {
        return (List) (CollectionUtils.isEmpty(keys) ? new ArrayList() : this.redisTemplate.execute((RedisCallback<List>) (connection) -> {
            try {
                int len = keys.size();
                byte[][] bkeys = new byte[len][];

                for (int i = 0; i < keys.size(); ++i) {
                    bkeys[i] = ((String) keys.get(i)).getBytes();
                }

                List<byte[]> values = connection.mGet(bkeys);
                return (List) values.stream().filter(Objects::nonNull).map((x) -> {
                    return JsonUtil.parseJsonAsBytes(x, clz);
                }).collect(Collectors.toList());
            } catch (Exception var6) {
                log.error("get error", var6);
                return null;
            }
        }));
    }

    public <T> List<T> getList(String key, Class<T> clz) {
        return (List) this.redisTemplate.execute((RedisCallback<List<T>>) (connection) -> {
            try {
                byte[] bytes = connection.get(key.getBytes());
                return bytes == null ? null : JsonUtil.parseListJsonAsBytes(bytes, clz);
            } catch (Exception var4) {
                log.error("getList error", var4);
                return null;
            }
        });
    }

    public List<byte[]> getList(String key) {
        return (List) this.redisTemplate.execute((RedisCallback<List<byte[]>>) (connection) -> {
            List<byte[]> byteList = connection.lRange(key.getBytes(), 0L, -1L);
            return byteList;
        });
    }

    public byte[] get(String key) {
        return (byte[]) this.redisTemplate.execute((RedisCallback<byte[]>) (connection) -> {
            return connection.get(key.getBytes());
        });
    }

    public boolean rPush(String key, Object... objects) {
        return this.redisTemplate.execute((RedisCallback<Boolean>) (connection) -> {
            try {
                if (objects.length == 1) {
                    connection.rPush(key.getBytes(), new byte[][]{JsonUtil.toJsonAsBytes(objects[0])});
                } else {
                    Object[] var3 = objects;
                    int var4 = objects.length;

                    for (int var5 = 0; var5 < var4; ++var5) {
                        Object object = var3[var5];
                        connection.rPush(key.getBytes(), new byte[][]{JsonUtil.toJsonAsBytes(object)});
                    }
                }

                return true;
            } catch (Exception var7) {
                log.error("rPush error ", var7);
                return false;
            }
        });
    }

    public boolean lPush(String key, Object... objects) {
        return this.redisTemplate.execute((RedisCallback<Boolean>) (connection) -> {
            try {
                if (objects.length == 1) {
                    connection.lPush(key.getBytes(), new byte[][]{JsonUtil.toJsonAsBytes(objects[0])});
                } else {
                    Object[] var3 = objects;
                    int var4 = objects.length;

                    for (int var5 = 0; var5 < var4; ++var5) {
                        Object object = var3[var5];
                        connection.lPush(key.getBytes(), new byte[][]{JsonUtil.toJsonAsBytes(object)});
                    }
                }

                return true;
            } catch (Exception var7) {
                log.error("lPush error ", var7);
                return false;
            }
        });
    }

    public byte[] lIndex(String key, long index) {
        return this.redisTemplate.execute((RedisCallback<byte[]>) (connection) -> {
            try {
                return connection.lIndex(key.getBytes(), index);
            } catch (Exception var5) {
                log.error("lIndex error ", var5);
                return null;
            }
        });
    }

    public <T> List<T> lRange(String key, Class<T> clz) {
        return this.lRange(key, 0L, -1L, clz);
    }

    public <T> List<T> lRange(String key, long begin, long end, Class<T> clz) {
        return (List) this.redisTemplate.execute((RedisCallback<List>) (connection) -> {
            try {
                List<byte[]> list = connection.lRange(key.getBytes(), begin, end);
                return CollectionUtils.isEmpty(list) ? null : (List) list.stream().map((p) -> {
                    try {
                        return JsonUtil.parseJsonAsBytes(p, clz);
                    } catch (Exception var3) {
                        log.error("new String error by UTF8 ", var3);
                        return null;
                    }
                }).filter((p) -> {
                    return p != null;
                }).collect(Collectors.toList());
            } catch (Exception var8) {
                log.error("lRange error ", var8);
                return null;
            }
        });
    }

    public boolean hmPut(String key, String innerKey, Object obj) {
        return this.redisTemplate.execute((RedisCallback<Boolean>) (connection) -> {
            try {
                Map<byte[], byte[]> maps = Maps.newHashMap();
                maps.put(innerKey.getBytes(), JsonUtil.toJson(obj).getBytes());
                connection.hMSet(key.getBytes(), maps);
                return true;
            } catch (Exception var5) {
                log.error("", var5);
                return false;
            }
        });
    }

    public boolean hmput(String key, Map<String, Object> maps) {
        return this.redisTemplate.execute((RedisCallback<Boolean>) (connection) -> {
            try {
                Map<byte[], byte[]> redisMap = (Map) maps.keySet().stream().collect(Collectors.toMap((key1) -> {
                    return key1.getBytes();
                }, (key2) -> {
                    Object obj = maps.get(key2);
                    return JsonUtil.toJson(obj).getBytes();
                }));
                connection.hMSet(key.getBytes(), redisMap);
                return true;
            } catch (Exception var4) {
                log.error("", var4);
                return false;
            }
        });
    }

    public <T> T hmget(String key, String field, Class<T> clz) {
        return this.redisTemplate.execute((RedisCallback<T>) (connection) -> {
            List<byte[]> list = connection.hMGet(key.getBytes(), new byte[][]{field.getBytes()});
            if (!CollectionUtils.isEmpty(list)) {
                try {
                    T entity = JsonUtil.parseJsonAsBytes((byte[]) list.get(0), clz);
                    return entity;
                } catch (Exception var6) {
                    log.error("new String error by UTF8 ", var6);
                    return null;
                }
            } else {
                return null;
            }
        });
    }

    public <T> List<T> hmget(String key, Class<T> clz, byte[]... fields) {
        return this.redisTemplate.execute((RedisCallback<List>) (connection) -> {
            List<byte[]> list = connection.hMGet(key.getBytes(), fields);
            return !CollectionUtils.isEmpty(list) ? (List) list.stream().filter((p) -> {
                return p != null;
            }).map((p) -> {
                try {
                    T entity = JsonUtil.parseJsonAsBytes(p, clz);
                    return entity;
                } catch (Exception var3) {
                    log.error("", var3);
                    return null;
                }
            }).filter((p) -> {
                return p != null;
            }).collect(Collectors.toList()) : null;
        });
    }

    public boolean zAdd(String key, double score, String value) {
        return this.redisTemplate.execute((RedisCallback<Boolean>) (connection) -> {
            try {
                connection.zAdd(key.getBytes(), score, value.getBytes());
                return true;
            } catch (Exception var6) {
                log.error("", var6);
                return false;
            }
        });
    }

    public String zRangeByScore(String key, double min, double max, long offset, long count) {
        return this.redisTemplate.execute((RedisCallback<String>) (connection) -> {
            Set<byte[]> resultSet = connection.zRangeByScore(key.getBytes(), min, max, offset, count);
            if (!CollectionUtils.isEmpty(resultSet)) {
                try {
                    return new String((byte[]) resultSet.iterator().next(), "UTF-8");
                } catch (UnsupportedEncodingException var12) {
                    log.error("出现异常", var12);
                }
            }

            return null;
        });
    }

    public Long incrBy(String key, long increment) {
        return this.redisTemplate.execute((RedisCallback<Long>) (connection) -> {
            return connection.incrBy(key.getBytes(), increment);
        });
    }

    public Set<String> matchKeys(String pattern) {
        return this.redisTemplate.execute((RedisCallback<Set<String>>) (connection) -> {
            RedisSerializer<String> serializer = new StringRedisSerializer();
            Set<byte[]> rawKeys = connection.keys(serializer.serialize(pattern));
            if (rawKeys == null) {
                return null;
            } else {
                Set<String> values = new HashSet(rawKeys.size());
                Iterator iterator = rawKeys.iterator();

                while (iterator.hasNext()) {
                    byte[] bs = (byte[]) ((byte[]) iterator.next());
                    values.add(serializer.deserialize(bs));
                }

                return values;
            }
        });
    }

    public <T> void lRem(byte[] key, long count, byte[] value) {
        this.redisTemplate.execute((RedisCallback<Object>) (connection) -> {
            try {
                return connection.lRem(key, count, value);
            } catch (Exception var6) {
                log.error("lRem error ", var6);
                return 0L;
            }
        });
    }

    public boolean exists(final String key) {
        return (Boolean) this.redisTemplate.execute((RedisCallback<Object>) (connection) -> {
            return connection.exists(key.getBytes());
        });
    }

    public boolean delete(String... keys) {
        return (Boolean) this.redisTemplate.execute((RedisCallback<Boolean>) (connection) -> {
            try {
                connection.openPipeline();
                String[] var2 = keys;
                int var3 = keys.length;

                for (int var4 = 0; var4 < var3; ++var4) {
                    String key = var2[var4];
                    connection.del(new byte[][]{key.getBytes()});
                }

                connection.closePipeline();
                return true;
            } catch (Exception var6) {
                log.error("delete error ", var6);
                return false;
            }
        });
    }

    public void deleteByPattern(String pattern) {
        try {
            Set<String> keys = matchKeys(pattern);
            if (!CollectionUtils.isEmpty(keys)) {
                delete(keys.toArray(new String[0]));
                log.info("Deleted {} keys matching pattern: {}", keys.size(), pattern);
            } else {
                log.info("No keys found for pattern: {}", pattern);
            }
        } catch (Exception e) {
            log.error("Error deleting keys by pattern: {}", pattern, e);
        }
    }

    public Long count(String key) {
        return (Long) this.redisTemplate.execute((RedisCallback<Long>) (connection) -> {
            try {
                Set<byte[]> sets = connection.keys(key.getBytes());
                return CollectionUtils.isEmpty(sets) ? 0L : (long) sets.size();
            } catch (Exception var3) {
                log.error("delete error ", var3);
                return 0L;
            }
        });
    }

    public long zCount(String key) {
        return redisTemplate.execute((RedisCallback<Long>) connection -> {
            ZSetOperations<String, Object> zSetOps = redisTemplate.opsForZSet();
            Long count = zSetOps.zCard(key);
            return count != null ? count : 0L;
        });
    }

    public void zRemRangeByScore(String key, double score) {
        ZSetOperations<String, Object> zSetOps = redisTemplate.opsForZSet();
        long change = zSetOps.removeRangeByScore(key, Double.MIN_VALUE, score);
        log.info("zRemRangeByScore change {}", change);
    }

    public Double zIncrBy(String key, Double increment, String value) {
        return (Double) this.redisTemplate.execute((RedisCallback<Object>) (connection) -> {
            try {
                return connection.zIncrBy(key.getBytes(), increment, value.getBytes());
            } catch (Exception var5) {
                log.error("出现异常", var5);
                return 0.0D;
            }
        });
    }

    public Double zScore(String key, String value) {
        return (Double) this.redisTemplate.execute((RedisCallback<Object>) (connection) -> {
            try {
                return connection.zScore(key.getBytes(), value.getBytes());
            } catch (Exception var4) {
                log.error("出现异常", var4);
                return 0.0D;
            }
        });
    }

    public Long zRank(String key, String value) {
        return (Long) this.redisTemplate.execute((RedisCallback<Object>) (connection) -> {
            try {
                return connection.zRank(key.getBytes(), value.getBytes());
            } catch (Exception var4) {
                log.error("出现异常", var4);
                return -1L;
            }
        });
    }

    public Boolean expire(String key, long seconds) {
        return (Boolean) this.redisTemplate.execute((RedisCallback<Object>) (connection) -> {
            try {
                return connection.expire(key.getBytes(), seconds);
            } catch (Exception var5) {
                log.error("出现异常", var5);
                return false;
            }
        });
    }

    public Boolean lTrim(String key, long begin, long end) {
        return (Boolean) this.redisTemplate.execute((RedisCallback<Boolean>) (connection) -> {
            try {
                connection.lTrim(key.getBytes(), begin, end);
                return true;
            } catch (Exception var7) {
                log.error("出现异常", var7);
                return false;
            }
        });
    }

    public Long zRemRange(String key, long begin, long end) {
        return (Long) this.redisTemplate.execute((RedisCallback<Object>) (connection) -> {
            try {
                return connection.zRemRange(key.getBytes(), begin, end);
            } catch (Exception var7) {
                log.error("出现异常", var7);
                return 0L;
            }
        });
    }

    public Long zCard(String key) {
        return (Long) this.redisTemplate.execute((RedisCallback<Object>) (connection) -> {
            try {
                return connection.zCard(key.getBytes());
            } catch (Exception var3) {
                log.error("出现异常", var3);
                return 0L;
            }
        });
    }

    public Long zRem(String key, String value) {
        return (Long) this.redisTemplate.execute((RedisCallback<Object>) (connection) -> {
            try {
                return connection.zRem(key.getBytes(), new byte[][]{value.getBytes()});
            } catch (Exception var4) {
                log.error("出现异常", var4);
                return 0L;
            }
        });
    }

    public void batchPushToSet(List<Pair<String, Object>> pairs, long activeTime) {
        this.redisTemplate.executePipelined((RedisCallback<?>) (connection) -> {
            try {
                connection.openPipeline();
                pairs.forEach((pair) -> {
                    byte[] keys = ((String) pair.getKey()).getBytes();
                    byte[] value = JsonUtil.toJsonAsBytes(pair.getValue());
                    connection.sAdd(keys, new byte[][]{value});
                    if (activeTime > 0L) {
                        connection.expire(keys, activeTime);
                    }

                });
                connection.closePipeline();
            } catch (Exception var5) {
                log.error("{}", var5);
            }

            return null;
        });
    }

    public void appendToSet(Map<String, Object> map, long activeTime) {
        this.redisTemplate.executePipelined((RedisCallback<?>) (connection) -> {
            try {
                connection.openPipeline();
                map.forEach((key, object) -> {
                    byte[] keys = key.getBytes();
                    byte[] value = JsonUtil.toJsonAsBytes(object);
                    connection.sAdd(keys, new byte[][]{value});
                    if (activeTime > 0L) {
                        connection.expire(keys, activeTime);
                    }

                });
                connection.closePipeline();
            } catch (Exception var5) {
                log.error("{}", var5);
            }

            return null;
        });
    }

    public long getCountOfSet(String key) {
        return (Long) this.redisTemplate.execute((RedisCallback<Object>) (connection) -> {
            byte[] keys = key.getBytes();

            try {
                return connection.sCard(keys);
            } catch (Exception var4) {
                log.error("{}", var4);
                return 0L;
            }
        });
    }

    public <T> T lPop(String key, Class<T> clz) {
        return this.redisTemplate.execute((RedisCallback<T>) (connection) -> {
            try {
                byte[] value = connection.lPop(key.getBytes());
                return value == null ? null : JsonUtil.parseJsonAsBytes(value, clz);
            } catch (Exception var4) {
                log.error("get error", var4);
                return null;
            }
        });
    }

    public <T> T rPop(String key, Class<T> clz) {
        return this.redisTemplate.execute((RedisCallback<T>) (connection) -> {
            try {
                byte[] value = connection.rPop(key.getBytes());
                return value == null ? null : JsonUtil.parseJsonAsBytes(value, clz);
            } catch (Exception var4) {
                log.error("get error", var4);
                return null;
            }
        });
    }

    public boolean setLock(String key, long expireTime) {
        return this.redisTemplate.execute((RedisCallback<Boolean>) (connection) -> {
            try {
                byte[] keys = key.getBytes();
                Boolean success = connection.setNX(keys, "locked".getBytes());
                if (Boolean.TRUE.equals(success)) {
                    connection.expire(keys, expireTime);
                }
                return Boolean.TRUE.equals(success);
            } catch (Exception e) {
                log.error("setLock error", e);
                return false;
            }
        });
    }

    public boolean releaseLock(String key) {
        return this.delete(key);
    }

    public boolean isLocked(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
}
