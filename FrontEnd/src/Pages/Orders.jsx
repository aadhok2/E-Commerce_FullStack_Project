// OrderPage.js
import React, { useState, useEffect } from 'react';
import { useAuthState } from '../context/AuthContext';
import '../styling/Orders.css';

const OrderPage = () => {
  const [orders, setOrders] = useState([]);
  const { user, isAuthenticated } = useAuthState();
  const [orderDetails, setOrderDetails] = useState([]);

  useEffect(() => {
    const fetchOrders = async () => {
      try {
        const response = await fetch('http://localhost:8083/orders/get');
        if (response.ok) {
          const data = await response.json();
          setOrders(data);
        } else {
          console.error('Error fetching orders:', response.statusText);
        }
      } catch (error) {
        console.error('Error fetching orders:', error.message);
      }
    };

    fetchOrders();
  }, []);

  useEffect(() => {
    const fetchOrderDetails = async () => {
      const orderDetailsPromises = orders.map(async (order) => {
        const productDetails = await fetchProductDetails(order.productId);
        const productImage = productDetails ? await fetchProductImage(productDetails.imageId) : null;

        return {
          orderId: order.orderId,
          productId: order.productId,
          quantity: order.quantity,
          addressId: order.addressId,
          productName: productDetails ? productDetails.title : 'N/A',
          orderStatus:order.orderStatus,
          productDetails,
          productImage,
        };
      });

      const resolvedOrderDetails = await Promise.all(orderDetailsPromises);
      setOrderDetails(resolvedOrderDetails);
    };

    fetchOrderDetails();
  }, [orders]);

  const fetchProductDetails = async (productId) => {
    try {
      const response = await fetch(`http://localhost:8084/products/product/${productId}`);
      if (response.ok) {
        return await response.json();
      } else {
        console.error('Error fetching product details:', response.statusText);
        return null;
      }
    } catch (error) {
      console.error('Error fetching product details:', error.message);
      return null;
    }
  };

  const fetchProductImage = async (imageId) => {
    try {
      const response = await fetch(`http://localhost:8084/products/images/${imageId}`);
      if (response.ok) {
        return URL.createObjectURL(await response.blob());
      } else {
        console.error('Error fetching product image:', response.statusText);
        return null;
      }
    } catch (error) {
      console.error('Error fetching product image:', error.message);
      return null;
    }
  };

  return (
    <div className="orders-container">
      <h1>Your Orders</h1>
      <div className='content-container'>
        {orderDetails.map((order) => (
          <div key={order.orderId} className="order-item"
          style={{
            backgroundColor:
              order.orderStatus === 'REJECTED'
                ? 'rgb(255, 232, 231)'
                : order.orderStatus === 'APPROVED'
                ? 'rgb(206, 249, 203)'
                : 'inherit',
          }}
          >
            {order.productImage && (
              <img
                src={order.productImage}
                alt={`Product ${order.productId}`}
                className="product-image"
              />
            )}
            <div className="order-details">
              <h3>Product Name: {order.productName}</h3>
              <p>Order ID: {order.orderId}</p>
              <p>Product ID: {order.productId}</p>
              <p>Quantity: {order.quantity}</p>
              <p>Address ID: {order.addressId}</p>
              <p>Status:{order.orderStatus}</p>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default OrderPage;
