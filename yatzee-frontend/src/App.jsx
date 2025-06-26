import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import HomePage from './pages/HomePage';
import RequireAuth from './components/RequireAuth';
import GameSelectionPage from './pages/GameSelectionPage';
// We don't need GamePage anymore, so you can delete this line and the file itself
// import GamePage from './pages/GamePage'; 
import GameLobbyPage from './pages/GameLobbyPage';
import GamePage from './pages/GamePage';

const App = () => {
  // We will rely on RequireAuth for protection, so checking the token here is optional
  // const token = localStorage.getItem('token'); 

  return (
    <Router>
      <Routes>
        {/* Public Routes */}
        <Route path="/" element={<Navigate to="/login" />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />

        {/* Protected Routes - Use RequireAuth wrapper */}
        <Route path="/home" element={ <RequireAuth> <HomePage /> </RequireAuth> } />
        <Route path="/select-game" element={ <RequireAuth> <GameSelectionPage /> </RequireAuth> } />
        <Route path="/game/:gameId" element={ <RequireAuth> <GameLobbyPage /> </RequireAuth> }/>

        {/* The problematic route is now removed */}
        {/* <Route path="/game" element={<GamePage />} /> */}
        <Route path="/play/game/:gameId" element={ <RequireAuth> <GamePage/> </RequireAuth>} />

      </Routes>
    </Router>
  );
};

export default App;