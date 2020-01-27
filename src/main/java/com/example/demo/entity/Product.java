package com.example.demo.entity;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document
public class Product implements Serializable {

	private int productId;
	private String productName;
	private String productDescription;
	private float productPrice;
	private int quantity;

	public Product() {

	}

	public int getProductId() {
		return productId;
	}

	public void setId(int productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public float getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(float productPrice) {
		this.productPrice = productPrice;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "Product [productName=" + productName + ", productDescription=" + productDescription + ", productPrice="
				+ productPrice + ", quantity=" + quantity + "]";
	}

	public Product(int productId, String productName, String productDescription, float productPrice, int quantity) {
		super();
		this.productId = productId;
		this.productName = productName;
		this.productDescription = productDescription;
		this.productPrice = productPrice;
		this.quantity = quantity;
	}

	public Product(String productName, String productDescription, float productPrice, int quantity) {

		this.productName = productName;
		this.productDescription = productDescription;
		this.productPrice = productPrice;
		this.quantity = quantity;
	}

	/*
	 * public static Product newProduct(String id) { Product prod = new
	 * Product("Wallet" + id, "Ladies Wallet" + id, 450, 1); return prod; }
	 */

}
