package com.j1.pojo;

import java.util.Arrays;

public class Goods {
	    private Long id;  
	    private String name;  
	    private String[] regionIds;  
	    private Long price;
	      
	    private String sellPoint;
	    
	    public String getSellPoint() {
			return sellPoint;
		}

		public void setSellPoint(String sellPoint) {
			this.sellPoint = sellPoint;
		}

		public Long getPrice() {
			return price;
		}

		public void setPrice(Long price) {
			this.price = price;
		}

		public Goods() {  
	        super();  
	    }  
	    
		   public Goods(Long id, String name, Long price) {  
		        super();  
		        this.id = id;  
		        this.name = name;  
		        this.price = price;  
		    }  
		   
		   
		   public Goods(Long id, String name, String sellPoint) {  
		        super();  
		        this.id = id;  
		        this.name = name;  
		        this.sellPoint = sellPoint;  
		    }
		   /**
		    * 
		    * @param id
		    * @param name
		    * @param regionIds
		    * 
		    * 
		    *  
		    *   */
		   public Goods(Long id, String name, String[] regionIds) {  
	        super();  
	        this.id = id;  
	        this.name = name;  
	        this.regionIds = regionIds;  
	    }  
		    
	  
	  
	    public Goods(Long id, String name,long price, String sellPoint) {
	         	 super();  
		        this.id = id;  
		        this.name = name;  
		        this.price = price;  
		        this.sellPoint = sellPoint;  
		}

		public Long getId() {  
	        return id;  
	    }  
	    public void setId(Long id) {  
	        this.id = id;  
	    }  
	    public String getName() {  
	        return name;  
	    }  
	    public void setName(String name) {  
	        this.name = name;  
	    }  
	  
	    /**
	     * 
	     * @return
	     * 
	     *  
	     *     */
	    public String[] getRegionIds() {  
	        return regionIds;  
	    }  
	  
	    public void setRegionIds(String[] regionIds) {  
	        this.regionIds = regionIds;  
	    }  
	    
	 
	  
	  @Override
	public String toString() {
		return "Goods [id=" + id + ", name=" + name + ", regionIds=" + Arrays.toString(regionIds) + ", price=" + price
				+ ", sellPoint=" + sellPoint + "]";
	}  
	     
}
