//src/components/PlayerJoinForm.jsx
import { useState } from "react";
import { joinGameAsPlayer } from "../services/playerApi";

export default function PlayerJoinForm({ gameId }) {
  const [isBot, setIsBot] = useState(false);
  const [color, setColor] = useState("red");
  const [message, setMessage] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await joinGameAsPlayer({ gameId, isBot, color });
      setMessage("Joined successfully: " + JSON.stringify(response.data));
    } catch (error) {
      setMessage("Error: " + error.response?.data?.message || "Failed");
    }
  };

  return (
    <form onSubmit={handleSubmit} className="p-4 bg-gray-800 rounded-md text-white">
      <label className="block mb-2">
        Join as:
        <select value={isBot} onChange={(e) => setIsBot(e.target.value === "true")} className="ml-2 text-black">
          <option value="false">User</option>
          <option value="true">Bot</option>
        </select>
      </label>
      <label className="block mb-2">
        Choose Color:
        <input
          type="text"
          value={color}
          onChange={(e) => setColor(e.target.value)}
          className="ml-2 text-black"
        />
      </label>
      <button type="submit" className="bg-blue-500 px-4 py-2 rounded">
        Join Game
      </button>
      {message && <p className="mt-2 text-sm">{message}</p>}
    </form>
  );
}

