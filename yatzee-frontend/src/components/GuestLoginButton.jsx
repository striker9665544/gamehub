import { useNavigate } from 'react-router-dom';
import { guestLogin } from '../services/authService';
import { jwtDecode } from 'jwt-decode'; // ✅ Named import

const GuestLoginButton = () => {
  const navigate = useNavigate();

  const handleGuest = async () => {
    try {
      const token = await guestLogin();
      console.log("Guest Token:", token);
      const decoded = jwtDecode(token); // ✅ Updated usage
      localStorage.setItem('token', token);
      localStorage.setItem('user', JSON.stringify(decoded));
      navigate('/home', { replace: true });
      window.location.reload();
    } catch (error) {
      console.error("Guest login failed", error);
      alert("Guest login failed");
    }
  };

  return (
    <button onClick={handleGuest} className="bg-blue-500 text-white p-2 rounded">
      Continue as Guest
    </button>
  );
};

export default GuestLoginButton;
