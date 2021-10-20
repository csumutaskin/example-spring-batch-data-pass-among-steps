package com.csumut.homeappliances.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

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
@Entity
@Table(name = "HOME_APPLIANCE")
public class HomeAppliance {
	
	@Id
	@SequenceGenerator(name="HA_SEQ",sequenceName="HOME_APPLIANCE_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="HA_SEQ")	
	private Long id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "category")
	private String category;
	
	@Column(name = "price")
	private BigDecimal price;
	
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

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "HomeAppliance [name=" + name + ", category=" + category + ", price=" + price + "]";
	}	
}