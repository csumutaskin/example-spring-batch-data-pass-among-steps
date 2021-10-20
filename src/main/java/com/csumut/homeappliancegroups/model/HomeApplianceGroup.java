package com.csumut.homeappliancegroups.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * A sample entity that represents a home appliance group tuple.
 * <br>
 * A home appliance group simply contains information about its:
 * <ul> 	
 * 	<li>category</li>
 *  <li>count</li>
 * 	<li>total price</li>
 *  <li>percentage</li>
 * </ul>
 * 
 * @author UMUT
 *
 */
@Entity
@Table(name = "HOME_APPLIANCE_GROUP")
public class HomeApplianceGroup {
	
	@Id
	@SequenceGenerator(name="HAG_SEQ",sequenceName="HOME_APPLIANCE_GROUP_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="HAG_SEQ")	
	private Long id;
	
	@Column(name = "count", nullable = false)
	private Long count;
	
	@Column(name = "category", nullable = false)
	private String category;
	
	@Column(name = "total_price", nullable = false)
	private BigDecimal totalPrice;
	
	@Column(name = "PERCENTAGE", nullable = false)
	private String percentage;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public String getCategory() {
		return category;
	}
	
	public void setCategory(String category) {
		this.category = category;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getPercentage() {
		return percentage;
	}

	public void setPercentage(String percentage) {
		this.percentage = percentage;
	}

	@Override
	public String toString() {
		return "HomeApplianceGroup [id=" + id + ", count=" + count + ", category=" + category + ", totalPrice="
				+ totalPrice + ", percentage=" + percentage + "]";
	}	
}