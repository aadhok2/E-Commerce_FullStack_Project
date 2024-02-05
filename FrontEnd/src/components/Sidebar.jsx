// Sidebar.js
import React from 'react';
import { useAuthState, useAuthDispatch } from '../context/AuthContext';
import { useNavigate } from 'react-router-dom';
import '../styling/Sidebar.css';

const Sidebar = ({ isOpen, onToggle }) => {
  const { user, isAuthenticated } = useAuthState();
  const authDispatch = useAuthDispatch();
  const navigate = useNavigate();

  const handleLogout = () => {
    authDispatch({ type: 'LOGOUT' });
    navigate('/login');
  };

  return (
    <div className={`sidebar ${isOpen ? 'open' : ''}`}>
      <div className='user-container'>
        {isAuthenticated ? (
          <h2>Hello {user?.userName}</h2>
        ) : (
          <h2>Hello Guest</h2>
        )}
      </div>
      <div className='sidebar-content'>
        <ul>
          {/* Add your sidebar links here */}
          <li><a href="/">Home</a></li>
          <li><a href="/products">Products</a></li>
          <li><a href="/orders">Orders</a></li>
          <li><a href="/about">About</a></li>
        </ul>
        <div className='logout-button'>
          {isAuthenticated && (
            <button onClick={handleLogout}>Logout</button>
          )}
        </div>
      </div>
    </div>
  );
};

export default Sidebar;
