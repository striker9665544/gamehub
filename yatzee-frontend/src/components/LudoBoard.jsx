// src/components/LudoBoard.jsx
import React from 'react';
import { LUDO_PATH_COORDINATES } from '../utils/ludoCoordinates';

// A single clickable token piece
const Token = ({ color, onClick, isMyTurn }) => (
  <button 
    onClick={onClick} 
    disabled={!isMyTurn}
    className={`w-7 h-7 rounded-full border-2 border-white/80 shadow-lg absolute z-20 
               ${isMyTurn ? 'cursor-pointer transform hover:scale-110' : 'cursor-not-allowed'}`} 
    style={{ backgroundColor: color.toLowerCase() }} 
  />
);

// The home area for a player, showing tokens at home
const HomeArea = ({ color, tokens, onTokenClick, isMyTurn }) => (
  <div className={`w-full h-full flex items-center justify-center rounded-lg bg-${color.toLowerCase()}-500`}>
    <div className="w-3/4 h-3/4 bg-white/90 rounded-lg grid grid-cols-2 grid-rows-2 gap-2 p-2">
      {tokens.map((token, i) => (
        <div key={i} className="flex items-center justify-center">
          {token.state === 'HOME' && <Token color={color} onClick={() => onTokenClick(token.id)} isMyTurn={isMyTurn} />}
        </div>
      ))}
    </div>
  </div>
);

// The Main Ludo Board Component
const LudoBoard = ({ gameState, onTokenClick, humanPlayerColor }) => {
  if (!gameState || !gameState.playerStates) {
    return (
      <div className="w-[600px] h-[600px] flex items-center justify-center bg-gray-800 rounded-xl">
        <p className="text-white text-2xl">Waiting for game state...</p>
      </div>
    );
  }

  const { playerStates, currentTurn } = gameState;
  const isMyTurn = currentTurn === humanPlayerColor;
  
  const allTokens = Object.entries(playerStates).flatMap(([color, state]) => 
      state.tokens.map(token => ({ ...token, color }))
  );

  // Helper function to create a styled path cell
  const PathCell = ({ row, col, color = 'white', isSafe = false, isStart = false }) => {
    const finalBgColor = isStart ? `bg-${color.toLowerCase()}-500` : (color === 'white' ? 'bg-white/90' : `bg-${color.toLowerCase()}-500`);
    return (
        <div 
            className={`flex items-center justify-center text-2xl font-bold text-white ${finalBgColor}`}
            style={{ gridRow: row, gridColumn: col }}
        >
            {isSafe && '★'}
        </div>
    );
  };
  
  return (
    <div className="relative w-[600px] h-[600px] bg-gray-900 p-2 rounded-xl shadow-2xl">
        <div className="w-full h-full grid grid-cols-15 grid-rows-15 gap-px bg-gray-700">
            {/* --- HOME AREAS --- */}
            <div className="col-start-1 col-span-6 row-start-1 row-span-6"><HomeArea color="GREEN" tokens={playerStates['GREEN']?.tokens || []} onTokenClick={onTokenClick} isMyTurn={isMyTurn && currentTurn === 'GREEN'}/></div>
            <div className="col-start-10 col-span-6 row-start-1 row-span-6"><HomeArea color="RED" tokens={playerStates['RED']?.tokens || []} onTokenClick={onTokenClick} isMyTurn={isMyTurn && currentTurn === 'RED'} /></div>
            <div className="col-start-1 col-span-6 row-start-10 row-span-6"><HomeArea color="YELLOW" tokens={playerStates['YELLOW']?.tokens || []} onTokenClick={onTokenClick} isMyTurn={isMyTurn && currentTurn === 'YELLOW'}/></div>
            <div className="col-start-10 col-span-6 row-start-10 row-span-6"><HomeArea color="BLUE" tokens={playerStates['BLUE']?.tokens || []} onTokenClick={onTokenClick} isMyTurn={isMyTurn && currentTurn === 'BLUE'} /></div>

            {/* ✅ --- CORRECTED CENTER AREA --- ✅ */}
            <div className="col-start-7 col-span-3 row-start-7 row-span-3 grid grid-cols-3 grid-rows-3">
                <div className="col-start-2 row-start-1 bg-red-500" style={{clipPath: 'polygon(0 100%, 50% 0, 100% 100%)'}}></div>
                <div className="col-start-1 row-start-2 bg-green-500" style={{clipPath: 'polygon(100% 0, 0 50%, 100% 100%)'}}></div>
                <div className="col-start-3 row-start-2 bg-yellow-500" style={{clipPath: 'polygon(0 0, 100% 50%, 0 100%)'}}></div>
                <div className="col-start-2 row-start-3 bg-blue-500" style={{clipPath: 'polygon(0 0, 50% 100%, 100% 0)'}}></div>
            </div>

            {/* --- PATHS --- */}
            {/* Top Vertical Arm */}
            {[...Array(6)].map((_, i) => <PathCell key={`T${i}`} row={i + 1} col={7} />)}
            <PathCell row={1} col={8} color="red" isStart={true} />
            {[...Array(5)].map((_, i) => <PathCell key={`TRH${i}`} row={i + 2} col={8} color="red" />)}
            {[...Array(6)].map((_, i) => <PathCell key={`T${i+6}`} row={i + 1} col={9} />)}
            
            {/* Left Horizontal Arm */}
            {[...Array(6)].map((_, i) => <PathCell key={`L${i}`} row={7} col={i + 1} />)}
            <PathCell row={8} col={1} color="green" isStart={true} />
            {[...Array(5)].map((_, i) => <PathCell key={`LGH${i}`} row={8} col={i + 2} color="green" />)}
            {[...Array(6)].map((_, i) => <PathCell key={`L${i+6}`} row={9} col={i + 1} />)}
            
            {/* Right Horizontal Arm */}
            {[...Array(6)].map((_, i) => <PathCell key={`R${i}`} row={7} col={i + 10} />)}
            <PathCell row={8} col={15} color="yellow" isStart={true}/>
            {[...Array(5)].map((_, i) => <PathCell key={`RYH${i}`} row={8} col={14 - i} color="yellow" />)}
            {[...Array(6)].map((_, i) => <PathCell key={`R${i+6}`} row={9} col={i + 10} />)}

            {/* Bottom Vertical Arm */}
            {[...Array(6)].map((_, i) => <PathCell key={`B${i}`} row={i + 10} col={7} />)}
            <PathCell row={15} col={8} color="blue" isStart={true}/>
            {[...Array(5)].map((_, i) => <PathCell key={`BBH${i}`} row={14 - i} col={8} color="blue" />)}
            {[...Array(6)].map((_, i) => <PathCell key={`B${i+6}`} row={i + 10} col={9} />)}

            {/* Safe Stars */}
            <PathCell row={7} col={2} isSafe={true}/>
            <PathCell row={2} col={9} isSafe={true}/>
            <PathCell row={9} col={14} isSafe={true}/>
            <PathCell row={14} col={7} isSafe={true}/>
        </div>

        {/* Render all ACTIVE tokens on top of the grid structure */}
        <div className="absolute top-0 left-0 w-full h-full p-2 pointer-events-none">
            {allTokens.filter(token => token.state === 'ACTIVE').map(token => {
                const coords = LUDO_PATH_COORDINATES[token.position];
                if (!coords) return null;

                // Adjust positioning to center the token within a cell
                const top = `calc(${(coords.row - 1) * (100 / 15)}% + 2px)`;
                const left = `calc(${(coords.col - 1) * (100 / 15)}% + 2px)`;

                return (
                    <div key={`${token.color}-${token.id}`} style={{ position: 'absolute', top, left }} className="pointer-events-auto">
                        <Token color={token.color} onClick={() => onTokenClick(token.id)} isMyTurn={isMyTurn && currentTurn === token.color} />
                    </div>
                );
            })}
        </div>
    </div>
  );
};

export default LudoBoard;