package com.j1.esutils;

import com.j1.pojo.Goods;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ESUtils {

	protected static final Logger logger = LoggerFactory.getLogger(ESUtils.class);
	/**
	 * es服务器的host
	 */
	private static final String host = "192.168.253.6";

	/**
	 * es服务器暴露给client的port
	 */
	private static final int port = 9300;

	/**
	 * jackson用于序列化操作的mapper
	 */
	private static final ObjectMapper mapper = new ObjectMapper();

	/**
	 * 获得连接
	 * 
	 * @return
	 * @throws UnknownHostException
	 */
	private static Client getClient() throws UnknownHostException {
		// 设置集群名称
		Settings settings = Settings.builder().put("cluster.name", "myelasticsearch").build();
		// 创建client
		@SuppressWarnings("resource")
		TransportClient client = new PreBuiltTransportClient(settings)
				.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host), port));
		return client;
	}

	/**
	 * 创建商品索引
	 * 
	 * @param goodsList
	 *            商品dto的列表
	 * @throws UnknownHostException
	 * @throws JsonProcessingException
	 */
	public static void createIndex(List<Goods> goodsList) throws UnknownHostException, JsonProcessingException {
		Client client = getClient();
		// 如果存在就先删除索引
		if (client.admin().indices().prepareExists("test_index1").get().isExists()) {
			client.admin().indices().prepareDelete("test_index1").get();
		}
		// 创建索引,并设置mapping.
		// String mappingStr = "{ \"goods\" :"+ " { \"properties\": "+ "{
		// \"id\": { \"type\": \"long\" }, "+ "\"name\": {\"type\": \"string\",
		// "+"\"sellPoint\": {\"type\": \"string\", "+ "\"analyzer\":
		// \"ik_max_word\"}, "+ "\"regionIds\": {\"type\": \"string\","+
		// "\"index\": \"not_analyzed\"}"+ "}"+ "}"+ "}";
		String mappingStr = "{ \"goods\" : { \"properties\": { \"id\": { \"type\": \"long\" },\"sellPoint\": { \"type\": \"string\",\"analyzer\": \"ik_max_word\" }, \"name\": {\"type\": \"string\", \"analyzer\": \"ik_max_word\"}, \"regionIds\": {\"type\": \"string\",\"index\": \"not_analyzed\"}}}}";

		client.admin().indices().prepareCreate("test_index1").addMapping("goods", mappingStr).get();

		// 批量处理request
		BulkRequestBuilder bulkRequest = client.prepareBulk();

		byte[] json;
		for (Goods goods : goodsList) {
			json = mapper.writeValueAsBytes(goods);
			bulkRequest.add(new IndexRequest("test_index1", "goods", goods.getId() + "").source(json));
		}

		// 执行批量处理request
		BulkResponse bulkResponse = bulkRequest.get();

		try {
			// 处理错误信息
			if (bulkResponse.hasFailures()) {
				System.out.println("====================批量创建索引过程中出现错误 下面是错误信息==========================");
				long count = 0L;
				for (BulkItemResponse bulkItemResponse : bulkResponse) {
					System.out.println("发生错误的 索引id为 : " + bulkItemResponse.getId() + " ，错误信息为："
							+ bulkItemResponse.getFailureMessage());
					count++;
				}
				System.out.println(
						"====================批量创建索引过程中出现错误 上面是错误信息 共有: " + count + " 条记录==========================");
				logger.info("创建索引成功");
			}
		} catch (Exception e) {
			logger.error("Cookie Decode Error.", e);
		}

		client.close();
	}

	/**
	 * 查询商品
	 * 
	 * @param filter
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static List<Goods> search(Goods good) throws Exception {
		Client client = getClient();
		QueryBuilder qb = new BoolQueryBuilder().must(QueryBuilders.matchQuery("sellPoint", good.getSellPoint()));
		// .must(QueryBuilders.termQuery("sellPoint", good.getSellPoint()));

		SearchResponse response = client.prepareSearch("test_index1").setTypes("goods").setQuery(qb).execute()
				.actionGet();

		SearchHit[] hits = response.getHits().getHits();

		List<Goods> list = new ArrayList<>();
		for (SearchHit hit : hits) {
			Goods goods = mapper.readValue(hit.getSourceAsString(), Goods.class);
			list.add(goods);
		}

		// 关闭
		client.close();
		return list;
	}

	/**
	 * 新增document
	 * 
	 * @param index
	 *            索引名称
	 * @param type
	 *            类型名称
	 * @param goods
	 *            商品dto
	 * @throws UnknownHostException
	 * @throws JsonProcessingException
	 */
	public static void addDocument(String index, String type, Goods goods) throws Exception {
		Client client = getClient();

		byte[] json = mapper.writeValueAsBytes(goods);
		logger.info("创建索引成功", json);
		client.prepareIndex(index, type, goods.getId() + "").setSource(json).get();
		System.out.println(json);
		client.close();
	}

	/**
	 * 删除document
	 * 
	 * @param index
	 *            索引名称
	 * @param type
	 *            类型名称
	 * @param goodsId
	 *            要删除的商品id
	 * @throws UnknownHostException
	 */
	public static void deleteDocument(String index, String type, Long goodsId) throws Exception {
		Client client = getClient();

		client.prepareDelete(index, type, goodsId + "").get();
		logger.info("删除成功");
		client.close();
	}

	/**
	 * 更新document
	 * 
	 * @param index
	 *            索引名称
	 * @param type
	 *            类型名称
	 * @param goods
	 *            商品dto
	 * @throws JsonProcessingException
	 * @throws UnknownHostException
	 */
	public static void updateDocument(String index, String type, Goods goods) throws Exception {
		// 如果新增的时候index存在，就是更新操作
		addDocument(index, type, goods);
		logger.info("更新成功");
	}

	/**
	 * 删除索引
	 */
	public static void deleteIndex(String s) throws UnknownHostException, JsonProcessingException {
		Client ia = getClient();
		DeleteIndexResponse deleteResponse = ia.admin().indices().prepareDelete("test").execute().actionGet();
		
		if (deleteResponse.isAcknowledged()) {
			logger.info("删除索引成功");
			System.out.println("删除索引成功");
		} else {
			logger.info("删除索引失败");
			System.out.println("删除索引失败");
		}

	}

}
