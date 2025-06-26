// src/pages/HomePage.jsx
import React from 'react';

const HomePage = () => {
  const user = JSON.parse(localStorage.getItem('user'));

  return (
    <div className="min-h-screen flex flex-col items-center justify-center bg-gray-900 text-white">
      <h1 className="text-3xl font-bold mb-4">Welcome, {user?.username || 'Guest'}!</h1>
      <p className="text-lg">Role: {user?.role}</p>
      <p className="text-lg">Guest: {user?.guest ? 'Yes' : 'No'}</p>
      <button
        onClick={() => {
          localStorage.removeItem('token');
          localStorage.removeItem('user');
          window.location.href = '/login';
        }}
        className="mt-6 bg-red-600 px-4 py-2 rounded hover:bg-red-700"
      >
        Logout
      </button>
      <button
        onClick={() => window.location.href = "/select-game"}
        className="mt-4 bg-green-600 px-4 py-2 rounded hover:bg-green-700"
      >
        Choose a Game
      </button>
    </div>
  );
};

export default HomePage;
