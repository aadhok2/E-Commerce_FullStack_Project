import React, { useState, useEffect } from 'react';
import { useAuthDispatch } from '../context/AuthContext';
import { useNavigate } from 'react-router-dom';
import '../styling/Login.css';

const Login = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [userName, setUserName] = useState('');
  const [age, setAge] = useState('');
  const [address, setAddress] = useState('');
  const [phoneNumber, setPhoneNumber] = useState('');

  const [isRegistering, setIsRegistering] = useState(false);
  const [error, setError] = useState(null);
  const authDispatch = useAuthDispatch();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const endpoint = isRegistering
        ? 'http://localhost:8085/users/register'
        : 'http://localhost:8085/users/login';

      const requestBody = isRegistering
        ? { email, password, userName, age, address, phoneNumber }
        : { email, password };

      const response = await fetch(endpoint, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(requestBody),
        credentials: 'include',
      });

      if (response.ok) {
        const user = await response.json();
        authDispatch({ type: 'LOGIN', payload: user });
        navigate('/');
      } else {
        setError('Login credentials are wrong');
      }
    } catch (error) {
      setError('An error occurred during login');
    }
  };

  // Check if the user is already authenticated on component mount
  useEffect(() => {
    const token = document.cookie.split(';').find(cookie => cookie.trim().startsWith('token='));
    if (token) {
      navigate('/');
    }
  }, [navigate]);

  return (
    <div className={`login-container ${isRegistering ? 'register' : 'login'}`}>
      <div className="login-card">
        <h2 className="login-heading">{isRegistering ? 'Register' : 'Welcome to DailyOrders'}</h2>
        <form onSubmit={handleSubmit} className="login-form">
        <label className='login-label'>Email:</label>
          <input
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            className="login-input"
          />

          <label className='login-label'>Password:</label>
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            className="login-input"
          />

          {isRegistering && (
            <>
              <label>Username:</label>
              <input
                type="text"
                value={userName}
                onChange={(e) => setUserName(e.target.value)}
                className="login-input"
              />

              <label>Age:</label>
              <input
                type="number"
                value={age}
                onChange={(e) => setAge(e.target.value)}
                className="login-input"
              />

              <label>Address:</label>
              <input
                type="text"
                value={address}
                onChange={(e) => setAddress(e.target.value)}
                className="login-input"
              />

              <label>Phone Number:</label>
              <input
                type="tel"
                value={phoneNumber}
                onChange={(e) => setPhoneNumber(e.target.value)}
                className="login-input"
              />
            </>
          )}
          {error && <p className="error-message">{error}</p>}

          <button type="submit" className="login-button">
            {isRegistering ? 'Register' : 'Login'}
          </button>

          <p onClick={() => setIsRegistering(!isRegistering)} className="login-register-link">
            {isRegistering
              ? 'Already have an account? Login here.'
              : 'Don\'t have an account? Register here.'}
          </p>
        </form>
      </div>
    </div>
  );
};

export default Login;
