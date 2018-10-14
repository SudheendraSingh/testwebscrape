package com.sainsbury.scrape.model;

public class RipeFruit {
	private String title;
	private String size;
	private Double unitPrice;
	private String description;

	public RipeFruit() {
	}

	public RipeFruit(String title, String size, Double unitPrice, String description) {
		this.title = title;
		this.size = size;
		this.unitPrice = unitPrice;
		this.description = description;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
