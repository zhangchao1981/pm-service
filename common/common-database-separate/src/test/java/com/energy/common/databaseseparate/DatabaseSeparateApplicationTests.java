//package com.energy.common.databaseseparate;
//
//import com.energy.common.databaseseparate.holder.DataSourceHolder;
//import com.energy.common.databaseseparate.model.Event;
//import com.energy.common.databaseseparate.model.Tttttt;
//import org.junit.Assert;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.query.Criteria;
//import org.springframework.data.mongodb.core.query.Query;
//import org.springframework.test.context.junit4.SpringRunner;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class DatabaseSeparateApplicationTests {
//
//    @Autowired
//    private MongoTemplate mongoTemplate;
//
//    @Autowired
//    private DynamicMongoClient dynamicMongoClient;
//
//    public Event findEventById(String id){
//        Query query = new Query(Criteria.where("equipId").is(id));
//        Event mgt = mongoTemplate.findOne(query, Event.class);
//        return mgt;
//    }
//
//    @Test
//    public void contextLoads() {
//        dynamicMongoClient.createNewHashMapItemByDatabaseName("test2","test2");
//        DataSourceHolder.setDB("test2");
//        Event mgt = findEventById("5cc2bbf2000decd8758bdb73");
//        Assert.assertNotNull(mgt);
//        System.out.println(mgt);
//        dynamicMongoClient.createNewHashMapItemByDatabaseName("test","test");
//        DataSourceHolder.setDB("test");
//        mgt = findEventById("5cc2bbf2000decd8758bdb73");
//        System.out.println(mgt);
//        dynamicMongoClient.createNewHashMapItemByDatabaseName("test3","test3");
//        DataSourceHolder.setDB("test3");
//        mgt.setId("123456");
//        mongoTemplate.save(mgt);
//        Tttttt tttttt = new Tttttt();
//        tttttt.setId(1234);
//        tttttt.setName("tyi it");
//        mongoTemplate.save(tttttt);
//        mgt = findEventById("5cc2bbf2000decd8758bdb73");
//        System.out.println(mgt);
//        mongoTemplate.getDb().drop();
//        dynamicMongoClient.deteleHashMapItemByDatabaseNameKey("test3");
//    }
//
//}
