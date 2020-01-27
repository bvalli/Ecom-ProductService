package com.example.demo.entity;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;



@Document
public class userCart implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String id;
	private int custId;
	private int quantity;
	private float cartPrice;
	private List<Product> productList;

	public List<Product> getProductList() {
		return productList;
	}

	public userCart(int custId, int quantity, float cartPrice, List<Product> productList) {
		super();
		this.custId = custId;
		this.quantity = quantity;
		this.cartPrice = cartPrice;
		this.productList = productList;
	}

	public void setProductList(List<Product> productList) {
		this.productList = productList;
	}

	public int getCustId() {
		return custId;
	}

	public void setCustId(int custId) {
		this.custId = custId;
	}

	public userCart() {

	}

	public float getCartPrice() {
		return cartPrice;
	}

	public void setCartPrice(float cartPrice) {
		this.cartPrice = cartPrice;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public float getTotalOrderPrice() {
		float sum = 0F;
		List<Product> cartProducts = getProductList();

		for (Product product : cartProducts) {

			sum += product.getProductPrice() * product.getQuantity();
		}

		// cartProducts.stream().forEach((product) ->{ product.getProductPrice() *
		// product.getQuantity();});
		return sum;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
