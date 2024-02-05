// Navbar.js
import React from 'react';
import { NavLink } from 'react-router-dom';
import '../styling/Navbar.css';
import MenuIcon from './MenuIcon';

const NavBar = ({ onSidebarToggle, isSidebarOpen, cartItems, openCartModal, onReduce }) => {
  const totalCartItems = cartItems.reduce((total, item) => total + item.quantity, 0);

  return (
    <nav className="navbar">
      <div className="nav-container">
        <div className='menuIcon'>
          <MenuIcon onClick={onSidebarToggle} isSidebarOpen={isSidebarOpen}/>
        </div>
        <div className="nav-logo">
          <NavLink to="/" className="nav-link-logo">
            DailyOrders
          </NavLink>
        </div>
        <div className="nav-links">
          <NavLink to="/" className="nav-link">
            Home
          </NavLink>
          <NavLink to="/products" className="nav-link">
            Products
          </NavLink>
          <NavLink to="/orders" className="nav-link">
            Orders
          </NavLink>
          <NavLink to="/about" className="nav-link">
            About
          </NavLink>
          <div className="cart-icon" onClick={openCartModal}>
            🛒 {totalCartItems > 0 && <span className="cart-counter">{totalCartItems}</span>}
          </div>
        </div>
      </div>
    </nav>
  );
}

export default NavBar;
