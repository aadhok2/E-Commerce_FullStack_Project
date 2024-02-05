// CartModal.js
import React from 'react';
import '../styling/CartModal.css';

const CartModal = ({ cartItems, onClose, onReduce, onRemove, onPlaceOrder }) => {
  return (
    <div className="cart-modal-overlay" onClick={onClose}>
      <div className="cart-modal-content" onClick={(e) => e.stopPropagation()}>
        <span className="cart-modal-close" onClick={onClose}>&times;</span>
        <h2>Your Cart</h2>
        {cartItems.length > 0 ? (
          <>
            <ul className="cart-modal-list">
              {cartItems.map((item) => (
                <li key={item.id} className="cart-modal-item">
                  <div className='cart-modal-item-entity'>
                    <img src={item.imageUrl} alt={item.title}/>
                    {item.title} - Quantity: {item.quantity}
                  </div>
                  <div>
                    <button className='reduce' onClick={() => onReduce(item)}>-</button>
                    <button className='remove' onClick={() => onRemove(item)}>R</button>
                  </div>
                </li>
              ))}
            </ul>
            <div className="cart-modal-buttons">
              <button className="cart-modal-place-order" onClick={onPlaceOrder}>Place Order</button>
            </div>
          </>
        ) : (
          <p>Your cart is empty.</p>
        )}
      </div>
    </div>
  );
};

export default CartModal;
