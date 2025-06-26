// src/services/authservice.js
import axios from 'axios';

const API_URL = 'http://localhost:8080/api/auth';

export const loginUser = async (email, password) => {
  const response = await axios.post(`${API_URL}/login`, { email, password });
  return response.data.token; // backend only returns token
};

export const registerUser = async (username, email, password) => {
  const response = await axios.post(`${API_URL}/register`, {
    username,
    email,
    password,
  });
  return response.data.token;
};

export const guestLogin = async () => {
  const response = await axios.post(`${API_URL}/guest`);
  return response.data.token;
};
