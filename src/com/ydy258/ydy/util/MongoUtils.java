package com.ydy258.ydy.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.ydy258.ydy.Setting;

/**
 * MongoDB
 * @author Administrator
 *
 */
public class MongoUtils {
	private final static MongoUtils mongoUtils = new MongoUtils();
	private MongoClient mongoClient = null;	
	
	private MongoUtils() {
		if(mongoClient == null) {
			MongoClientOptions.Builder build = new MongoClientOptions.Builder();
			build.connectionsPerHost(100);
			build.threadsAllowedToBlockForConnectionMultiplier(50);
			/* 
             * һ���̷߳�����ݿ��ʱ���ڳɹ���ȡ��һ��������ݿ�����֮ǰ����ȴ�ʱ��Ϊ2���� 
             * ����Ƚ�Σ�գ�����maxWaitTime��û�л�ȡ��������ӵĻ������߳̾ͻ��׳�Exception 
             * ���������õ�maxWaitTimeӦ���㹻�����������Ŷ��̹߳����ɵ���ݿ����ʧ�� 
             */  
            build.maxWaitTime(1000*60*2);  
            build.connectTimeout(1000*60*1);    //����ݿ⽨�����ӵ�timeout����Ϊ1����
            
            Setting setting = SettingUtils.get();
            
            MongoClientOptions myOptions = build.build();   
            String userName = setting.getMongoUserName();
            String dbName = setting.getMongoDBName();
            String pass = setting.getMongoPassword();
            List<MongoCredential> credentials = Arrays.asList(MongoCredential.createScramSha1Credential(
            		userName, dbName, pass.toCharArray()));			
            
            String[] hosts = setting.getMongoHosts();
            ArrayList<ServerAddress> address = new ArrayList<ServerAddress>();
            for(String h: hosts) {
            	ServerAddress sa = new ServerAddress(h);
            	address.add(sa);
            }			
			mongoClient = new MongoClient(address, credentials, myOptions);
		}
	}
	
	public static MongoUtils getInstance() {
		return mongoUtils;
	}
	
	public MongoDatabase getMongoDB(String dbName) {
		return mongoClient.getDatabase(dbName);
	}
	//����Ӧ��ȱʡ��ݿ�
	public MongoDatabase getMongoDB() {
		Setting setting = SettingUtils.get();		
		return mongoClient.getDatabase(setting.getMongoDBName());
	}
 	
	public MongoCollection<?> getCollection(String dbName, String collectionName) {
		return mongoClient.getDatabase(dbName).getCollection(collectionName);
	}
}
