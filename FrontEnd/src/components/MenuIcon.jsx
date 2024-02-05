// MenuIcon.js
import React from 'react';
import '../styling/MenuIcon.css';

const MenuIcon = ({ onClick, isSidebarOpen }) => {
  return (
    <div className={`container ${isSidebarOpen ? 'change' : ''}`} onClick={onClick}>
      <div className="bar1"></div>
      <div className="bar2"></div>
      <div className="bar3"></div>
    </div>
  );
}

export default MenuIcon;
