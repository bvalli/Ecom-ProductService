package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Product;
import com.example.demo.entity.userCart;
import com.example.demo.exception.ProductNotFoundException;
import com.example.demo.repository.ProductInterface;
import com.example.demo.repository.cartInterface;
import com.example.utils.ProductUtils;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductInterface productInterface;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private cartInterface cartInterface;
	
	

	@Override
	public List<userCart> addToCart(userCart cart) {
		int custId = cart.getCustId();
		Query query = new Query();
		query.addCriteria(Criteria.where("custId").is(custId));
		userCart cartDetails1 = mongoTemplate.findOne(query, userCart.class);
		List<userCart> cartDetails = new ArrayList<>();
		if (cartDetails1 != null) {
			cartDetails.add(cartDetails1);
		}
		if (cartDetails != null && !cartDetails.isEmpty()) {
			List<userCart> currentCart =cartDetails
													.stream()
													.filter(cartdet -> cartdet.getCustId() == cart.getCustId())
													.collect(Collectors.toList());
					cart.setCartPrice(cart.getTotalOrderPrice());
					Query query1 = new Query();
					query1.addCriteria(Criteria.where("custId").is(cart.getCustId()));
					Update update = new Update();
					cart.getProductList().addAll(currentCart.get(0).getProductList());
					update.set("productList", cart.getProductList());
					update.set("cartPrice", cart.getTotalOrderPrice());
					update.set("quantity", cart.getProductList().size());
					mongoTemplate.updateFirst(query1, update, userCart.class);
				} else {
					cart.setCartPrice(cart.getTotalOrderPrice());
					cartInterface.save(cart);
				}
					
		return cartInterface.findAll();
	}

	
	@Override
	public List<Product> getProductDetailsByProductId(int productId) throws ProductNotFoundException {

		List<Product> prodDetails = productInterface.findAll();
		List<Product> prodById = new ArrayList<>();
		if (prodDetails != null 
				&& !prodDetails.isEmpty()) {
			prodById = prodDetails
					            .stream()
								.filter(product -> product.getProductId() == productId)
					            .collect(Collectors.toList());
			if (prodById.isEmpty()) {
				throw new ProductNotFoundException("Requested Product is not found");
			}

		} else {
			throw new ProductNotFoundException("No details is present for the requested product ");
		}
	
		return prodById;

	}

	@Override
	public List<Product> getProductDetails() throws ProductNotFoundException {
		List<Product> productData = (List<Product>) productInterface.findAll();
		if (productData != null && !productData.isEmpty()) {
			return productData
					     .stream()
					     .distinct()
					     .collect(Collectors.toList());

		} else {
			throw new ProductNotFoundException("No Products to show up ");
		}

	}

	
	@Override
	public userCart getCartdetails(int custId) {

		Query query = new Query();
		query.addCriteria(Criteria.where("custId").is(custId));
		userCart cartDetails = mongoTemplate.findOne(query, userCart.class);
		
		if (cartDetails != null) {
			cartDetails.setCartPrice(cartDetails.getTotalOrderPrice());
			return cartDetails;

		} else {
			throw new ProductNotFoundException("No Products in the cart ");
		}
	}

	
	@Override
	public userCart removeFromCart(int id) {

		int custId = 122;
		Query query = new Query();
		Update update = new Update();
		query.addCriteria(Criteria.where("custId").is(custId));
		userCart cartProducts = mongoTemplate.findOne(query, userCart.class);
		List<Product> cartprod = cartProducts.getProductList();
		List<Product> cart = cartprod
										.stream()
				                        .filter(product -> product.getProductId() != id)
				                        .collect(Collectors.toList());
		update.set("productList", cart);
		update.set("quantity", cart.size());
		mongoTemplate.updateFirst(query, update, userCart.class);

		userCart cartdet = mongoTemplate.findOne(query, userCart.class);
		update.set("cartPrice", cartdet.getTotalOrderPrice());

		return cartdet;
	}
	
}
