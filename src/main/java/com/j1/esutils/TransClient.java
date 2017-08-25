package com.j1.esutils;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
public class TransClient {
	  public static String clusterName = "myelasticsearch";// 集群名称
	    public static String serverIP = "http://192.168.253.6";// 服务器IP
	    public static void main(String[] args) {
	        System.out.println(getMapping("news", "sportnews"));
	    }

	    public static String getMapping(String indexname, String typename) {
	      //  Settings settings = Settings.settingsBuilder().put("cluster.name", clusterName).build();
	        String mapping="";
	        try {
//	            TransportClient client = TransportClient.builder().settings(settings).build()
//	                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(serverIP), 9300));

	            //设置集群名称
	            Settings settings = Settings.builder().put("cluster.name", "myelasticsearch").build();
	            //创建client
	            @SuppressWarnings("resource")
	    		TransportClient client = new PreBuiltTransportClient(settings)
	                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.253.6"), 9200));
	            
	            
	            
	            ImmutableOpenMap<String, MappingMetaData> mappings = client.admin().cluster().prepareState().execute()
	                    .actionGet().getState().getMetaData().getIndices().get(indexname).getMappings();
	            mapping = mappings.get(typename).source().toString();


	            client.close();
	        } catch (UnknownHostException e) {
	            e.printStackTrace();
	        }

	        return mapping;
	    }
}
