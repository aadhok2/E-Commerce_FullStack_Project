// index.js or your main file
import React from 'react';
import ReactDOM from 'react-dom';
import { BrowserRouter as Router } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext'; // Update the path accordingly
import AppRouter from './routers/AppRouter.jsx';

ReactDOM.render(
  <React.StrictMode>
    <Router>
      <AuthProvider>
        <AppRouter />
      </AuthProvider>
    </Router>
  </React.StrictMode>,
  document.getElementById('root')
);
