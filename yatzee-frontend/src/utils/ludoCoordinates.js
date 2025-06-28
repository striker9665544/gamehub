// src/utils/ludoCoordinates.js

// This map defines the grid-template-areas location for each position on the Ludo board.
// It assumes a 15x15 grid layout for the entire board.
// We will define these grid areas in the LudoBoard component's CSS.

export const LUDO_PATH_COORDINATES = {
    // Red Player Path
    1: { row: 7, col: 2 },  // Start
    2: { row: 7, col: 3 },
    3: { row: 7, col: 4 },
    4: { row: 7, col: 5 },
    5: { row: 7, col: 6 },
    6: { row: 6, col: 7 },
    7: { row: 5, col: 7 },
    8: { row: 4, col: 7 },
    9: { row: 3, col: 7 },
    10: { row: 2, col: 7 },
    11: { row: 1, col: 7 }, // Turn
    12: { row: 1, col: 8 },
    13: { row: 1, col: 9 }, // Safe star
    // Green Player Path
    14: { row: 2, col: 9 }, // Start
    15: { row: 3, col: 9 },
    16: { row: 4, col: 9 },
    17: { row: 5, col: 9 },
    18: { row: 6, col: 9 },
    19: { row: 7, col: 10 },
    20: { row: 7, col: 11 },
    21: { row: 7, col: 12 },
    22: { row: 7, col: 13 },
    23: { row: 7, col: 14 },
    24: { row: 7, col: 15 }, // Turn
    25: { row: 8, col: 15 },
    26: { row: 9, col: 15 }, // Safe star
    // Yellow Player Path
    27: { row: 9, col: 14 }, // Start
    28: { row: 9, col: 13 },
    29: { row: 9, col: 12 },
    30: { row: 9, col: 11 },
    31: { row: 9, col: 10 },
    32: { row: 10, col: 9 },
    33: { row: 11, col: 9 },
    34: { row: 12, col: 9 },
    35: { row: 13, col: 9 },
    36: { row: 14, col: 9 },
    37: { row: 15, col: 9 }, // Turn
    38: { row: 15, col: 8 },
    39: { row: 15, col: 7 }, // Safe star
    // Blue Player Path
    40: { row: 14, col: 7 }, // Start
    41: { row: 13, col: 7 },
    42: { row: 12, col: 7 },
    43: { row: 11, col: 7 },
    44: { row: 10, col: 7 },
    45: { row: 9, col: 6 },
    46: { row: 9, col: 5 },
    47: { row: 9, col: 4 },
    48: { row: 9, col: 3 },
    49: { row: 9, col: 2 },
    50: { row: 9, col: 1 }, // Turn
    51: { row: 8, col: 1 },
    52: { row: 7, col: 1 }, // Safe star
    // Home Stretches
    // Red
    53: { row: 2, col: 8 },
    54: { row: 3, col: 8 },
    55: { row: 4, col: 8 },
    56: { row: 5, col: 8 },
    57: { row: 6, col: 8 },
    // Green (assuming positions 58-62)
    58: { row: 8, col: 2 },
    // etc. for Green, Yellow, Blue
};