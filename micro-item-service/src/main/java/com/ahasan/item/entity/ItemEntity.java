package com.ahasan.item.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name = "item")
public class ItemEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "item_id")
	private Long itemId;

	@Size(max = 10, min = 1, message = "product name field must be equal or less than {max}")
	@Column(name = "product_name")
	private String itemName;

	@Size(max = 5, min = 1, message = "product size field must be equal or less than {max}")
	@Column(name = "product_size")
	private String itemtSize;

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemtSize() {
		return itemtSize;
	}

	public void setItemtSize(String itemtSize) {
		this.itemtSize = itemtSize;
	}

}
