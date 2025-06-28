// src/components/YahtzeeSheet.jsx
import React from 'react';

const ScoreRow = ({ category, score, onScoreClick }) => {
    const hasBeenScored = score !== null;
    return (
        <div className={`flex justify-between items-center p-2 rounded ${hasBeenScored ? 'bg-gray-800' : 'bg-gray-600 hover:bg-gray-500'}`}>
            <span className="capitalize font-semibold">{category.replace(/([A-Z])/g, ' $1').trim()}</span>
            {hasBeenScored ? (
                <span className="font-bold text-xl text-cyan-400">{score}</span>
            ) : (
                <button 
                    onClick={() => onScoreClick(category)}
                    className="text-xs bg-green-600 px-2 py-1 rounded hover:bg-green-500"
                >
                    Score
                </button>
            )}
        </div>
    );
};


const YahtzeeSheet = ({ scoreCard, onScoreClick }) => {
    if (!scoreCard) {
        return <div className="text-white">Loading Scorecard...</div>;
    }

    const upperSection = ['aces', 'twos', 'threes', 'fours', 'fives', 'sixes'];
    const lowerSection = ['threeOfAKind', 'fourOfAKind', 'fullHouse', 'smallStraight', 'largeStraight', 'yahtzee', 'chance'];

    const calculateTotal = (scores) => {
        return Object.values(scores).filter(s => s !== null).reduce((sum, current) => sum + current, 0);
    };

    return (
        <div className="bg-gray-800 p-6 rounded-lg text-white w-full max-w-sm">
            <h2 className="text-3xl font-bold mb-4 text-center">Yahtzee</h2>
            
            <div className="space-y-2 mb-4">
                <h3 className="text-lg font-bold border-b border-gray-600 pb-1">Upper Section</h3>
                {upperSection.map(category => (
                    <ScoreRow 
                        key={category} 
                        category={category}
                        score={scoreCard[category]} 
                        onScoreClick={onScoreClick}
                    />
                ))}
            </div>
            
            <div className="space-y-2">
                 <h3 className="text-lg font-bold border-b border-gray-600 pb-1">Lower Section</h3>
                {lowerSection.map(category => (
                    <ScoreRow 
                        key={category} 
                        category={category} 
                        score={scoreCard[category]}
                        onScoreClick={onScoreClick}
                    />
                ))}
            </div>
            
            <div className="mt-6 border-t-2 border-cyan-500 pt-3 text-right">
                <span className="text-xl font-bold">Total Score: </span>
                <span className="text-2xl font-extrabold text-cyan-300">{calculateTotal(scoreCard)}</span>
            </div>
        </div>
    );
};

export default YahtzeeSheet;