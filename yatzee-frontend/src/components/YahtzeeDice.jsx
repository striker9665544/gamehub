// src/components/YahtzeeDice.jsx
import React from 'react';

// A single die face
const Die = ({ value, isHeld, onClick }) => {
    return (
        <button 
            onClick={onClick}
            className={`w-16 h-16 md:w-20 md:h-20 border-2 rounded-lg flex items-center justify-center text-4xl font-bold
                       transition-all duration-200
                       ${isHeld ? 'bg-cyan-500 border-cyan-300 transform scale-95' : 'bg-white border-gray-400'}`}
        >
            <span className={isHeld ? 'text-white' : 'text-black'}>{value}</span>
        </button>
    );
};


const YahtzeeDice = ({ dice, held, onDieClick, onRollClick, rollsLeft }) => {
    return (
        <div className="bg-gray-700 p-6 rounded-lg shadow-lg flex flex-col items-center">
            <h2 className="text-2xl text-white font-bold mb-4">Dice</h2>
            <div className="flex gap-4 mb-6">
                {dice.map((value, index) => (
                    <Die 
                        key={index} 
                        value={value} 
                        isHeld={held[index]} 
                        onClick={() => onDieClick(index)} 
                    />
                ))}
            </div>
            <button
                onClick={onRollClick}
                disabled={rollsLeft === 0}
                className="bg-blue-600 text-white px-8 py-3 rounded-lg font-bold text-xl disabled:bg-gray-500 disabled:cursor-not-allowed"
            >
                Roll ({rollsLeft} left)
            </button>
        </div>
    );
};

export default YahtzeeDice;