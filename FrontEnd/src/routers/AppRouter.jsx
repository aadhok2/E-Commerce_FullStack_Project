// AppRouter.js
import React, { useState } from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import { useAuthState, useAuthDispatch } from '../context/AuthContext';
import NavBar from '../components/Navbar';
import Home from '../Pages/Home';
import About from '../Pages/About';
import Products from '../Pages/Products';
import Orders from '../Pages/Orders';
import Sidebar from '../components/Sidebar';
import CartModal from '../components/CartModal';
import Login from '../Pages/Login';
import '../styling/AppRouter.css';

const ProtectedRoute = ({ element }) => {
  const { isAuthenticated } = useAuthState();

  return isAuthenticated ? element : <Navigate to="/login" />;
};

const AppRouter = () => {
  const [isSidebarOpen, setIsSidebarOpen] = useState(false);
  const [isCartModalOpen, setIsCartModalOpen] = useState(false);
  const [cartItems, setCartItems] = useState([]);
  const [showSuccessMessage, setShowSuccessMessage] = useState(false);
 

  const toggleSidebar = () => {
    setIsSidebarOpen(!isSidebarOpen);
  };

  const openCartModal = () => {
    setIsCartModalOpen(true);
  };

  const closeCartModal = () => {
    setIsCartModalOpen(false);
  };

  const updateCart = (newCartItems) => {
    const consolidatedCart = newCartItems.reduce((cart, item) => {
      const existingItem = cart.find(
        (existing) => existing.productId === item.productId
      );

      if (existingItem) {
        existingItem.quantity += item.quantity;
      } else {
        cart.push(item);
      }

      return cart;
    }, []);

    setCartItems(consolidatedCart);
  };

  const onReduce = (product) => {
    const productIndex = cartItems.findIndex(
      (item) => item.id === product.id
    );

    if (productIndex !== -1) {
      const updatedCart = [...cartItems];

      updatedCart[productIndex] = {
        ...updatedCart[productIndex],
        quantity: updatedCart[productIndex].quantity - 1,
      };

      const filteredCart = updatedCart.filter((item) => item.quantity > 0);

      updateCart(filteredCart);
    }
  };

  const onRemove = (product) => {
    const updatedCart = cartItems.filter(
      (item) => item.productId !== product.productId
    );
    updateCart(updatedCart);
  };

  const handlePlaceOrder = async () => {
    if (cartItems.length > 0) {
      cartItems.forEach(async (item) => {
        const requestData = {
          productId: item.productId,
          quantity: item.quantity,
          addressId: 'afbb5881-a872-4d13-993c-faeb8350eea5',
        };

        try {
          const response = await fetch(
            'http://localhost:8083/orders/create',
            {
              method: 'POST',
              headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
              },
              body: JSON.stringify(requestData),
              mode: 'cors',
              credentials: 'include',
            }
          );
          if (response.ok) {
            updateCart([]);
            setShowSuccessMessage(true);
          } else {
            console.error(`Error placing orders: ${response.statusText}`);
          }
        } catch (error) {
          console.error(`Error placing orders: ${error.message}`);
        }
      });
    }
  };

  const authDispatch = useAuthDispatch();

  return (
    <div className={`app-container ${isSidebarOpen ? 'sidebar-open' : ''}`}>
      <Sidebar isOpen={isSidebarOpen} onToggle={toggleSidebar} />
      <div className="content">
        <NavBar
          onSidebarToggle={toggleSidebar}
          isSidebarOpen={isSidebarOpen}
          openCartModal={openCartModal}
          cartItems={cartItems}
          onReduce={onReduce}
        />
        <div className="page-content">
          <Routes>
            <Route path="/login" element={<Login />} />
            <Route
              path="/"
              element={
                <ProtectedRoute
                  element={
                    <>
                      <Home />
                      {/* ... (other components) */}
                    </>
                  }
                />
              }
            />
            <Route
              path="/products"
              element={
                <ProtectedRoute
                  element={
                    <Products
                      cartItems={cartItems}
                      updateCart={updateCart}
                      onReduce={onReduce}
                      onRemove={onRemove}
                    />
                  }
                />
              }
            />
            <Route
              path="/orders"
              element={<ProtectedRoute element={<Orders />} />}
            />
            <Route path="/about" element={<About />} />
          </Routes>
        </div>
      </div>
      {isCartModalOpen && (
        <CartModal
          cartItems={cartItems}
          onClose={closeCartModal}
          onReduce={onReduce}
          onRemove={onRemove}
          onPlaceOrder={handlePlaceOrder}
        />
      )}
      {showSuccessMessage && (
        <div className="success-message">
          <p>Order placed successfully!</p>
        </div>
      )}
    </div>
  );
};

export default AppRouter;
