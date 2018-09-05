package com.ygy.shiro.realm;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.ValidatingSession;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.SerializationUtils;

import redis.clients.jedis.Jedis;
import java.io.*;


/**
* project_name: shiro-redis-json
* package: com.ygy.shiro.realm
* describe: TODO
* @author : ygy
* creat_time: 2018年8月31日 上午9:21:20
* 
**/
public class RedisSessionDao extends CachingSessionDAO {

    private Logger logger = LoggerFactory.getLogger(RedisSessionDao.class);






    //本地redis
	public Jedis getJedis() {
		return new Jedis("localhost");
	}
   


	private byte[] sessionIdSerialize(Serializable sessionId) {
        return String.valueOf(sessionId).getBytes();
    }

    private void sessionSerialize(Session session, OutputStream outputStream) {
        try (ObjectOutput out = new ObjectOutputStream(outputStream)) {
            out.writeObject(session);
        } catch (Exception e) {
            logger.error("Shiro Session Serialize Exception", e);
        }
    }

    private byte[] serialize(Session session) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(512);
        sessionSerialize(session, baos);
        return baos.toByteArray();
    }

    private void setSession(Session session) {
        if ( session != null && session.getId() != null ) {
        	//getRedisClient().getResource()
            try ( Jedis jedis = getJedis() ) {
            	//转为byte数组
                byte[] key = sessionIdSerialize(session.getId());
                byte[] value = serialize(session);
                //session过期时间
                int seconds = Long.valueOf(session.getTimeout()/1000).intValue();
                jedis.set(key, value);
                //redis 数据保存过期时间
                jedis.expire(key, seconds+10);
            } catch (Exception e) {
                logger.error("Jedis保存SESSION异常", e);
            }
        }
    }

    private Session getSession(Serializable sessionId) {

        if ( sessionId != null ) {
        	//getRedisClient().getResource()
            try ( Jedis jedis = getJedis() ) {
                byte[] key = sessionIdSerialize(sessionId);
                byte[] value = jedis.get(key);

                if ( value != null && value.length > 0 ) {
                    Object obj = SerializationUtils.deserialize(value);
                    if ( obj instanceof Session ) {
                    	// 重置Redis中缓存过期时间??
//                        jedis.expire(key, 1800000);
                        return (Session) obj;
                    }
                }

            } catch (Exception e) {
            	System.out.println("读取redis session Exception");
            	e.printStackTrace();
                logger.error("Jedis读取SESSION异常", e);
            }
        }

        return null;

    }

    @Override
    protected void doUpdate(Session session) {

        logger.debug(String.format("doUpdate session %s", session.getId()));

        //如果会话过期/停止 没必要再更新了
        if(session instanceof ValidatingSession && !((ValidatingSession)session).isValid()) {
            return;
        }

        setSession(session);
    }

    @Override
    protected void doDelete(Session session) {

        logger.debug(String.format("doDelete session %s", session.getId()));

        getJedis().del(String.valueOf(session.getId()));

    }

    @Override
    protected Serializable doCreate(Session session) {

        logger.debug(String.format("doCreate session %s", session.getId()));

        Serializable sessionId = generateSessionId(session);

        assignSessionId(session, sessionId);

        setSession(session);

        return session.getId();
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {

        logger.debug(String.format("doReadSession session %s", sessionId));
        Session session = getSession(sessionId);
        return session;
    }
}
