import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import HomePage from './pages/HomePage';
import RequireAuth from './components/RequireAuth';
import GameSelectionPage from './pages/GameSelectionPage';

const App = () => {
  const token = localStorage.getItem('token'); // ✅ Fix added here

  return (
    <Router>
      <Routes>
        <Route path="/" element={<Navigate to="/login" />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />

        <Route path="/select-game" element={token ? <GameSelectionPage /> : <Navigate to="/login" />} />

        <Route
          path="/home"
          element={
            <RequireAuth>
              <HomePage />
            </RequireAuth>
          }
        />
      </Routes>
    </Router>
  );
};

export default App;
