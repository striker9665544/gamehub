import React from 'react';

// A single square on the board's path
const PathSquare = ({ isSafe, children }) => (
  <div className={`w-8 h-8 md:w-10 md:h-10 border border-gray-500 flex items-center justify-center ${isSafe ? 'bg-gray-400' : 'bg-gray-200'}`}>
    {children}
  </div>
);

// A token piece
const Token = ({ color, onClick }) => (
  <button onClick={onClick} className={`w-6 h-6 md:w-7 md:h-7 rounded-full border-2 border-white shadow-md transform hover:scale-110 transition-transform`} style={{ backgroundColor: color.toLowerCase() }} />
);

// The home area for a player
const HomeArea = ({ color, tokens }) => (
  <div className={`w-40 h-40 md:w-48 md:h-48 flex items-center justify-center rounded-lg`} style={{ backgroundColor: `${color.toLowerCase()}` }}>
    <div className="w-32 h-32 md:w-40 md:h-40 bg-white rounded-lg grid grid-cols-2 grid-rows-2 gap-2 p-2">
      {tokens.map((token, i) => (
        <div key={i} className="flex items-center justify-center">
          {token.state === 'HOME' && <Token color={color} />}
        </div>
      ))}
    </div>
  </div>
);

// The main Ludo Board component
const LudoBoard = ({ gameState, onTokenClick, humanPlayerColor }) => {
  if (!gameState || !gameState.playerStates) {
    return <div className="text-white">Waiting for game state...</div>;
  }

  const { playerStates } = gameState;
  const redTokens = playerStates['RED']?.tokens || [];
  const greenTokens = playerStates['GREEN']?.tokens || [];
  const blueTokens = playerStates['BLUE']?.tokens || [];
  const yellowTokens = playerStates['YELLOW']?.tokens || [];

  return (
    <div className="bg-gray-900 p-4 rounded-xl shadow-2xl flex flex-col items-center">
        {/* Main Board Grid */}
        <div className="grid grid-cols-3 gap-1">
            {/* Top-left: Green Home */}
            <HomeArea color="GREEN" tokens={greenTokens} />
            {/* Top-middle: Path */}
            <div className="flex flex-col items-center">
                <PathSquare /> <PathSquare isSafe /> <PathSquare />
            </div>
            {/* Top-right: Red Home */}
            <HomeArea color="RED" tokens={redTokens} />

            {/* Middle-left: Path */}
            <div className="flex justify-center">
                <PathSquare /> <PathSquare isSafe /> <PathSquare /> <PathSquare /> <PathSquare /> <PathSquare />
            </div>

            {/* Center Area */}
            <div className="w-40 h-40 md:w-48 md:h-48 bg-gray-700 flex items-center justify-center">
                <div className="w-0 h-0
                  border-l-[60px] border-l-green-500
                  border-t-[60px] border-t-red-500
                  border-r-[60px] border-r-yellow-500
                  border-b-[60px] border-b-blue-500 rounded-full">
                </div>
            </div>

            {/* Middle-right: Path */}
            <div className="flex justify-center">
                <PathSquare /> <PathSquare /> <PathSquare /> <PathSquare isSafe /> <PathSquare /> <PathSquare />
            </div>

            {/* Bottom-left: Yellow Home */}
            <HomeArea color="YELLOW" tokens={yellowTokens} />
            {/* Bottom-middle: Path */}
             <div className="flex flex-col items-center">
                <PathSquare /> <PathSquare /> <PathSquare isSafe />
            </div>
            {/* Bottom-right: Blue Home */}
            <HomeArea color="BLUE" tokens={blueTokens} />
        </div>

        {/* Info Panel */}
        <div className="w-full mt-4 bg-gray-800 p-4 rounded-lg text-white">
            <h3 className="text-xl font-bold">Game Info</h3>
            <p>Your Color: <span className="font-bold" style={{color: humanPlayerColor?.toLowerCase()}}>{humanPlayerColor}</span></p>
            <p>Current Turn: <span className="font-bold" style={{color: gameState.currentTurn?.toLowerCase()}}>{gameState.currentTurn}</span></p>
            {gameState.gameCompleted && <p className="text-green-400 font-bold text-xl">Game Over!</p>}
        </div>
    </div>
  );
};

export default LudoBoard;