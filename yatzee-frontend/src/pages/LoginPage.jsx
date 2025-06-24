import React, { useState } from 'react';
import { loginUser } from '../services/authService';
import { decodeToken } from '../utils/jwtUtils';
import { useNavigate } from 'react-router-dom';
import GuestLoginButton from '../components/GuestLoginButton';


const LoginPage = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
    setError('');

    try {
      const token = await loginUser(email, password);
      const decoded = decodeToken(token);
      console.log("Token from login:", token);
      localStorage.setItem('token', token);
      localStorage.setItem('user', JSON.stringify(decoded));
      console.log("Decoded JWT:", decoded);
      navigate('/home', { replace: true });
      window.location.reload();
    } catch (err) {
      setError('Invalid credentials');
    }
  };

  return (
    <div className="min-h-screen bg-gray-900 flex items-center justify-center">
      <div className="bg-white rounded-lg shadow-lg p-8 w-96">
        <h2 className="text-2xl font-bold mb-6 text-center">Login</h2>
        <form onSubmit={handleLogin} className="space-y-4">
          <div>
            <label className="block font-semibold">Email</label>
            <input
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              className="w-full px-3 py-2 border rounded"
              required
            />
          </div>
          <div>
            <label className="block font-semibold">Password</label>
            <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="w-full px-3 py-2 border rounded"
              required
            />
          </div>
          {error && <p className="text-red-500 text-sm">{error}</p>}
          <button
            type="submit"
            className="w-full bg-blue-600 text-white py-2 rounded hover:bg-blue-700"
          >
            Login
          </button>
        </form>
        <div className="mt-4 text-center">
          <p>
            Don't have an account? <a href="/register" className="text-blue-600">Register</a>
          </p>
          <p className="mt-2">
            <GuestLoginButton />
          </p>
        </div>
      </div>
    </div>
  );
};

export default LoginPage;
