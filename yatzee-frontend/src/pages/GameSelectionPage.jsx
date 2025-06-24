import React from 'react';
import { useNavigate } from 'react-router-dom';

const GameSelectionPage = () => {
  const navigate = useNavigate();

  const handleSelection = (game, mode) => {
    console.log(`Selected: ${game}, Mode: ${mode}`);
    // navigate(`/game/${game}?mode=${mode}`); // implement route later
    alert(`Starting ${game} in ${mode} mode (navigate coming soon)`);
  };

  //Images
  const gameOptions = [
    {
      name: 'Ludo',
      image: '/images/ludo.PNG', // add in public/images/
      modes: ['2 Players', '3 Players', '4 Players', 'Computer'],
    },
    {
      name: 'Yatzee',
      image: '/images/yatzee.PNG', // add in public/images/
      modes: ['Player', 'Computer'],
    },
  ];

  return (
    <div className="min-h-screen bg-gray-900 text-white flex flex-col items-center px-4 py-8">
      <h1 className="text-3xl font-bold mb-8">🎮 Choose a Game to Play</h1>
      <div className="grid grid-cols-1 md:grid-cols-2 gap-8 w-full max-w-4xl">
        {gameOptions.map((game) => (
          <div
            key={game.name}
            className="bg-gray-800 rounded-xl p-6 shadow-md hover:shadow-xl transition"
          >
            <img
              src={game.image}
              alt={game.name}
              className="rounded-lg w-full h-40 object-cover mb-4"
            />
            <h2 className="text-xl font-bold mb-4 text-center">{game.name}</h2>
            <div className="flex flex-wrap justify-center gap-3">
              {game.modes.map((mode) => (
                <button
                  key={mode}
                  onClick={() => handleSelection(game.name.toLowerCase(), mode.toLowerCase())}
                  className="bg-blue-600 hover:bg-blue-700 px-4 py-2 rounded text-sm"
                >
                  {mode}
                </button>
              ))}
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default GameSelectionPage;
