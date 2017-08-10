package com.j1.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.j1.esutils.ESUtils;
import com.j1.pojo.Goods;


public class MyTest {
	   
    /** 
     * 生成索引 
     * @throws UnknownHostException 
     * @throws JsonProcessingException 
     */  
    @Test  
    public void testCreatIndex() throws Exception{  
        List<Goods> goodsList = new ArrayList<>();  
          
      
        goodsList.add(new Goods(1L, "雀巢咖啡", 1L,"欢迎品尝雀巢咖啡1,好喝"));  
        goodsList.add(new Goods(2L, "雅哈咖啡", 2L,"欢迎品尝雅哈2,好喝"));  
          
        goodsList.add(new Goods(3L, "星巴克咖啡", 3L,"欢迎品尝星巴克咖啡3,好喝"));  
        goodsList.add(new Goods(4L, "可口可乐", 4L,"欢迎品尝可口可乐4,好喝"));  
          
        ESUtils.createIndex(goodsList);  
    }  
      
    /** 
     * 测试search 
     * @throws JsonParseException 
     * @throws JsonMappingException 
     * @throws IOException 
     */  
    @Test  
    public void testSearch() throws Exception{  
//        GoodsFilter2ES filter = new GoodsFilter2ES();  
//        filter.setQueryStr("咖啡");  
       // filter.setRegionId("r2");
        Goods good=new Goods();
      //  good.setName("咖啡");
  good.setSellPoint("雀巢咖啡");
    //good.setId(1L);
        List<Goods> result = ESUtils.search(good);  
        for (Goods goods : result) {  
            System.out.println(goods);  
        }  
    }  
      
    /** 
     * 测试新增doc 
     * @throws UnknownHostException 
     * @throws JsonProcessingException 
     */  
    @Test  
    public void testAddDoc() throws Exception{  
        //test_index 和 goods 在创建索引的时候写死了 所以这样 就传这两个值  
        //String[] r = {"r2","r3"};  
        
        Goods goods = new Goods(5L, "新增的咖啡", "欢迎品尝");  
        ESUtils.addDocument("test_index", "goods", goods);  
    }  
      
    /** 
     * 测试修改doc 
     * @throws UnknownHostException 
     * @throws JsonProcessingException 
     */  
    @Test  
    public void testUpdateDoc() throws Exception{  
     //   String[] r = {"r2","r3"};  
        Goods goods = new Goods(5L, "修改啦的咖啡", "欢迎下次品尝");  
        ESUtils.updateDocument("test_index", "goods", goods);  
    }  
      
    /** 
     * 测试删除doc 
     * @throws UnknownHostException 
     * @throws JsonProcessingException 
     */  
    @Test  
    public void testDelDoc() throws Exception{  
        ESUtils.deleteDocument("test_index", "goods", 5L);  
    }  
}
