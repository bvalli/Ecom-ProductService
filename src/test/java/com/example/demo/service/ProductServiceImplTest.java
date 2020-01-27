package com.example.demo.service;

import static org.hamcrest.CoreMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.example.demo.entity.Product;
import com.example.demo.entity.userCart;
import com.example.demo.exception.ProductNotFoundException;
import com.example.demo.repository.ProductInterface;
import com.example.demo.repository.cartInterface;
import com.mongodb.client.result.UpdateResult;

@SpringBootTest
class ProductServiceImplTest {

	@InjectMocks
	private ProductServiceImpl mockProductServiceImpl;

	@Mock
	private ProductInterface mockProductInterface;

	@Mock
	private MongoTemplate mockMongoTemplate;

	@Mock
	private cartInterface cartInterface;

	@Mock
	private ProductNotFoundException exception;

	List<Product> productData = new ArrayList<>();
	List<Product> prodDetails = new ArrayList<Product>();
	Product prod = new Product(124, "abc", "abc", 12345, 1);
	Product prod1 = new Product(121, "dsf", "dsf", 1542, 2);
	Query query = new Query();
	Update update = new Update();
	
	userCart newdata = new userCart(120, 2, 100, productData);


	@Test
	void testAddToCart_existingCustomer() {
		productData.add(prod1);
		productData.add(prod);
		int custId=122;
		userCart data = new userCart(122, 3, 1235, productData);
		Query query = new Query();
		query.addCriteria(Criteria.where("custId").is(custId));

		when(mockMongoTemplate.findOne(query, userCart.class)).thenReturn(data);
		Update update = new Update();
		when(mockMongoTemplate.updateFirst(query, update, userCart.class)).thenReturn(null);
		List<userCart> cartProd = new ArrayList<>();
		cartProd.add(data);
		when(cartInterface.save(Mockito.any(userCart.class))).thenReturn(null);
		when(cartInterface.findAll()).thenReturn(cartProd);
		List<userCart> output = mockProductServiceImpl.addToCart(data);
		assertEquals(cartProd, output);

	}
	
	@Test
	void testAddToCart_newCustomer() {
		productData.add(prod1);
		productData.add(prod);
		int custId=122;
		userCart data = new userCart(122, 3, 1235, productData);
		Query query = new Query();
		query.addCriteria(Criteria.where("custId").is(custId));

		when(mockMongoTemplate.findOne(query, userCart.class)).thenReturn(data);
		//Update update = new Update();
		//when(mockMongoTemplate.updateFirst(query, update, userCart.class)).thenReturn(null);
		List<userCart> cartProd = new ArrayList<>();
		cartProd.add(data);
		when(cartInterface.save(Mockito.any(userCart.class))).thenReturn(null);
		when(cartInterface.findAll()).thenReturn(cartProd);
		List<userCart> output = mockProductServiceImpl.addToCart(data);
		assertEquals(cartProd, output);

	}

	@Test
	void testGetProductDetailsByProductId() {
		int productId = 121;
		prod1.setId(productId);
		productData.add(prod1);
		productData.add(prod);
		when(mockProductInterface.findAll()).thenReturn(productData);
		List<Product> mockProdDetails = mockProductServiceImpl.getProductDetailsByProductId(productId);

		assertEquals(1, mockProdDetails.size());
	}

	@Test
	void testGetProductDetailsByProductId_WithProductDetailsEmpty() {
		int productId = 121;
		when(mockProductInterface.findAll()).thenReturn(productData);
		Exception exception = assertThrows(ProductNotFoundException.class, () -> {
			mockProductServiceImpl.getProductDetailsByProductId(productId);
		});
		String expectedMessage = "No details is present for the requested product ";
		String actualMessage = exception.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));

	}

	@Test
	void testGetProductDetailsByProductId_WithProductDetailsNull() {
		int productId = 120;
		when(mockProductInterface.findAll()).thenReturn(null);
		Exception exception = assertThrows(ProductNotFoundException.class, () -> {
			mockProductServiceImpl.getProductDetailsByProductId(productId);
		});
		String expectedMessage = "No details is present for the requested product ";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}
	
	@Test
	void testGetProductDetailsByProductId_WithException() {
		int productId = 122;
		productData.add(prod1);
		productData.add(prod);
		when(mockProductInterface.findAll()).thenReturn(productData);
		Exception exception = assertThrows(ProductNotFoundException.class, () -> {
			 mockProductServiceImpl.getProductDetailsByProductId(productId);
		});
		String expectedMessage = "Requested Product is not found";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
		
	}

	
	@Test
	void testGetProductDetails() {
		productData.add(prod1);
		productData.add(prod);

		when(mockProductInterface.findAll()).thenReturn(productData);
		List<Product> mockProductDetails = mockProductServiceImpl.getProductDetails();
		assertEquals(productData, mockProductDetails);
	}
	
	@Test
	void testGetProductDetails_WithException() {
		when(mockProductInterface.findAll()).thenReturn(null);
		Exception exception = assertThrows(ProductNotFoundException.class, () -> {
			 mockProductServiceImpl.getProductDetails();
		});
		String expectedMessage = "No Products to show up ";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
		
	}

	@Test
	void testGetCartdetails() {
		productData.add(prod1);
		productData.add(prod);
		userCart data = new userCart(122, 3, 1235, productData);
		int custId = data.getCustId();
		Query query = new Query();
		query.addCriteria(Criteria.where("custId").is(custId));
		when(mockMongoTemplate.findOne(query, userCart.class)).thenReturn(data);
		userCart cartDetails = mockProductServiceImpl.getCartdetails(custId);
		assertEquals(data, cartDetails);
	}
	
	@Test
	void testGetCartdetails_WithException() {
		userCart data = new userCart(122, 3, 1235, productData);
		int custId = data.getCustId();
		when(mockMongoTemplate.findOne(query, userCart.class)).thenReturn(null);
		Exception exception = assertThrows(ProductNotFoundException.class, () -> {
			mockProductServiceImpl.getCartdetails(custId);
		});
		String expectedMessage = "No Products in the cart ";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
		
	}

	@Test
	void testRemoveFromCart() {
		productData.add(prod);
		productData.add(prod1);
		int custId = 122;
		userCart data = new userCart(122, 3, 1235, productData);
		when(mockMongoTemplate.findOne(query, userCart.class)).thenReturn(data);
		// List<Product> cartprod = data.getProductList();
		int productId = 121;

		Query query1 = new Query();
		Update update = new Update();
		query.addCriteria(Criteria.where("custId").is(custId));
		userCart removedata = new userCart(122, 2, 1235, productData);


		when(mockMongoTemplate.updateFirst(query1, update, userCart.class)).thenReturn(null);
		productData.remove(prod1);

		
		when(mockMongoTemplate.findOne(query, userCart.class)).thenReturn(removedata);
		userCart mockedRemoveProductFromCart = mockProductServiceImpl.removeFromCart(productId);

		assertEquals(removedata, mockedRemoveProductFromCart);
	}
}
