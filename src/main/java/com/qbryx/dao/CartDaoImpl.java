package com.qbryx.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.qbryx.domain.CartProduct;
import com.qbryx.domain.Product;
import com.qbryx.domain.User;
import com.qbryx.manager.ConnectionManager;
import com.qbryx.util.DAOQuery;

@Repository("cartDao")
public class CartDaoImpl implements CartDao {

	public void addProductInCart(CartProduct product) {
		
		Connection connection = ConnectionManager.getConnection();

		if (connection != null) {
			PreparedStatement stmt;

			try {
				stmt = connection.prepareStatement(DAOQuery.SQL_ADD_PRODUCT_IN_CART);
				stmt.setLong(1, product.getUserId());
				stmt.setLong(2, product.getProduct().getId());
				stmt.setInt(3, product.getQuantity());
				stmt.setInt(4, product.getIsPurchased());

				stmt.executeUpdate();

				connection.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
		
		
	}

	public List<CartProduct> getProductsInCart(User user) {

		List<CartProduct> cartProducts = new ArrayList<CartProduct>();
		
		Connection connection = ConnectionManager.getConnection();

		if (connection != null) {
			PreparedStatement stmt;

			try {
				stmt = connection.prepareStatement(DAOQuery.SQL_GET_PRODUCTS_IN_CART);
				stmt.setLong(1, user.getUserId());

				ResultSet rs = stmt.executeQuery();

				while (rs.next()) {
					Product product = new Product(rs.getString("upc"), rs.getString("name"), rs.getBigDecimal("price"));
					
					CartProduct cartProduct = new CartProduct();
					cartProduct.setProduct(product);
					cartProduct.setQuantity(rs.getInt("quantity"));

					cartProducts.add(cartProduct);

					connection.close();
				}
			} catch (SQLException e) {
				throw new RuntimeException();
			}
		}

		return cartProducts;
	}

	public void removeProductInCart(CartProduct cartProduct) {
		
		Connection connection = ConnectionManager.getConnection();
		
		if (connection != null) {
			PreparedStatement stmt;

			try {
				stmt = connection.prepareStatement(DAOQuery.SQL_REMOVE_PRODUCT_IN_CART);
				stmt.setLong(1, cartProduct.getUserId());
				stmt.setLong(2, cartProduct.getProduct().getId());

				stmt.executeUpdate();

				connection.close();
			} catch (SQLException e) {
				throw new RuntimeException();
			}
		}
	}

	public void checkout(CartProduct cartProduct) {
		
		Connection connection = ConnectionManager.getConnection();
		
		if (connection != null) {
			PreparedStatement stmt;

			try {
				stmt = connection.prepareStatement(DAOQuery.SQL_UPDATE_PRODUCT_IN_CART);
				stmt.setLong(1, cartProduct.getUserId());
				stmt.setLong(2, cartProduct.getProduct().getId());

				stmt.executeUpdate();

				connection.close();
			} catch (SQLException e) {
				throw new RuntimeException();
			}
		}
	}

	public CartProduct findProductInCart(User user, long productId) {

		CartProduct cartProduct = null;
		
		Connection connection = ConnectionManager.getConnection();

		if (connection != null) {
			PreparedStatement stmt;

			try {
				stmt = connection.prepareStatement(DAOQuery.SQL_CHECK_PRODUCT_IN_CART);
				stmt.setLong(1, user.getUserId());
				stmt.setLong(2, productId);

				ResultSet rs = stmt.executeQuery();

				if (rs.next()) {
					
					cartProduct = new CartProduct();
					cartProduct.setUserId(rs.getLong("user_id"));
					cartProduct.setProduct(new Product(rs.getLong("id")));
					cartProduct.setQuantity(rs.getInt("quantity"));
					
					connection.close();
				}
			} catch (SQLException e) {
				throw new RuntimeException();
			}

		}

		return cartProduct;
	}

	public void updateProductQuantityInCart(CartProduct cartProduct) {
		
		Connection connection = ConnectionManager.getConnection();

		if (connection != null) {
			PreparedStatement stmt;

			try {
				stmt = connection.prepareStatement(DAOQuery.SQL_UPDATE_PRODUCT_QUANTITY_IN_CART);
				stmt.setInt(1, cartProduct.getQuantity());
				stmt.setLong(2, cartProduct.getUserId());
				stmt.setLong(3, cartProduct.getProduct().getId());

				stmt.executeUpdate();

				connection.close();
			} catch (SQLException e) {
				throw new RuntimeException();
			}
		}
	}
}
