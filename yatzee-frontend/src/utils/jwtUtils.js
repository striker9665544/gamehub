import { jwtDecode } from 'jwt-decode';

export const decodeToken = (token) => {
  try {
    return jwtDecode(token); // use jwtDecode (not jwt_decode)
  } catch (error) {
    return null;
  }
};
