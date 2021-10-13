package com.csumut.model;

import java.math.BigDecimal;

/**
 * A sample entity that represents a home appliance tuple.
 * <br>
 * A home appliance simply contains information about its:
 * <ul>
 * 	<li>name</li>
 * 	<li>category</li>
 * 	<li>price</li>
 * </ul>
 * 
 * @author UMUT
 *
 */
public class HomeAppliance {
	
	private Long id;
	private String name;
	private String category;
	private String price;
	
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
	
	public String getCategory() {
		return category;
	}
	
	public void setCategory(String category) {
		this.category = category;
	}
	
	public String getPrice() {
		return price;
	}
	
	public void setPrice(String price) {
		this.price = price;
	}
	
	@Override
	public String toString() {
		return "HomeAppliance [name=" + name + ", category=" + category + ", price=" + price + "]";
	}	
}