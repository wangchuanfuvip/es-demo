package com.j1.test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.j1.esutils.BooksESUtils;
import com.j1.esutils.ESUtils;
import com.j1.pojo.Books;
import com.j1.pojo.Goods;


public class EsTest {
	   
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
      
    @Test  
    public void testBooksCreatIndex() throws Exception{  
        List<Books> booksList = new ArrayList<>();  
          /**
           * 添加数据
           */

		SimpleDateFormat df = new SimpleDateFormat("yyyy");//设置日期格式
		String format = df.format(new Date());
		
        booksList.add(new Books(1L, "Java编程思想", "java","Bruce Eckel",70L,"Java学习必读经典,殿堂级著作！赢得了全球程序员的"));  
        

        booksList.add(new Books(2L, "Java程序性能优化", "java","葛一鸣",60L,"让你的Java程序更快、更稳定。深入剖析软件设计层面、代码层面、JVM虚拟机层面的优化方法"));  

        booksList.add(new Books(3L, "Python科学计算", "python","张若愚",50L,"零基础学python,光盘中作者独家整合开发winPython运行环境，涵盖了Python各个扩展库"));  
        

        booksList.add(new Books(4L, "Python基础教程", "python","张若愚",40L,"经典的Python入门教程，层次鲜明，结构严谨，内容翔实"));  
        
        booksList.add(new Books(5L, "JavaScript高级程序设计", "javascript","Nicholas C.Zakas",30L,"JavaScript技术经典名著"));  
          
        BooksESUtils.createIndex(booksList);  
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
    
    @Test  
    public void testBooksSearch() throws Exception{
    	
    	Books books=new Books();
    	books.setLanguage("java");
    	 List<Books> result= ESUtils.searchBooks(books); 
    	 for (Books books2 : result) {
			System.out.println(books2);
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
        
        Goods goods = new Goods(6L, "新增的咖啡", "欢迎品尝");  
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
        Books books =    	new Books(1L, "Java编程思想", "java","Bruce Eckel",70L,"Java学习必读经典,殿堂级著作！赢得了全球程序员的");

        BooksESUtils.updateDocument("test_index2", "goods", books);  
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
    
    
    /**
     * 删除索引测试
     */
    
    
    @Test  
    public void testDeletIndex() throws Exception{  
        ESUtils.deleteIndex("test");  
    } 
}
