package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Product;
import com.example.demo.entity.userCart;

public interface ProductService {
	
	public  userCart getCartdetails(int custId);
	public List<userCart> addToCart(userCart cart);
	public List<Product> getProductDetailsByProductId(int productId);
	public  List<Product> getProductDetails();
	public userCart removeFromCart(int id);

}
