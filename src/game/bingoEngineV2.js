const fs = require('fs');
const path = require('path');

class BingoEngineV2 {
  constructor(configPath = path.join(__dirname, '../config/app_config.json')) {
    this.config = JSON.parse(fs.readFileSync(configPath, 'utf8'));
    this.roundDurationSec = this.config.roundLifecycle.roundIntervalSeconds || 120;
    this.maxBalls = this.config.roundLifecycle.maxCalledNumbers || 75;
  }

  generateCard(cardNumber) {
    const min = this.config.cardPoolRange.min;
    const max = this.config.cardPoolRange.max;
    if (cardNumber < min || cardNumber > max) {
      throw new Error(`Card number must be between ${min} and ${max}`);
    }
    const ranges = [[1, 15], [16, 30], [31, 45], [46, 60], [61, 75]];
    let seed = cardNumber * 2654435761;
    const nextRand = () => {
      seed = (seed * 1664525 + 1013904223) % 4294967296;
      return seed / 4294967296;
    };
    const grid = Array.from({ length: 5 }, () => Array(5).fill(null));
    for (let col = 0; col < 5; col++) {
      const [low, high] = ranges[col];
      const pool = [];
      for (let n = low; n <= high; n++) pool.push(n);
      for (let i = pool.length - 1; i > 0; i--) {
        const j = Math.floor(nextRand() * (i + 1));
        [pool[i], pool[j]] = [pool[j], pool[i]];
      }
      for (let row = 0; row < 5; row++) {
        grid[row][col] = row === 2 && col === 2 ? 'FREE' : pool[row];
      }
    }
    return grid;
  }

  drawNextNumber(alreadyCalled = [], roundSeed = 1) {
    const calledSet = new Set(alreadyCalled);
    const remaining = [];
    for (let n = 1; n <= this.maxBalls; n++) {
      if (!calledSet.has(n)) remaining.push(n);
    }
    if (remaining.length === 0) return null;
    const idxSeed = (roundSeed * 1103515245 + alreadyCalled.length * 12345 + 1) % 2147483647;
    return remaining[idxSeed % remaining.length];
  }

  getRemainingSeconds(roundStartEpochMs, nowEpochMs = Date.now()) {
    const elapsedSec = Math.floor((nowEpochMs - roundStartEpochMs) / 1000);
    return Math.max(0, this.roundDurationSec - (elapsedSec % this.roundDurationSec));
  }

  checkWin(grid, calledNumbers, mode = 'classic_75_line') {
    const calledSet = new Set(calledNumbers);
    const marked = grid.map((row, r) =>
      row.map((val, c) => (r === 2 && c === 2) || calledSet.has(val))
    );
    if (mode === 'four_corners') return marked[0][0] && marked[0][4] && marked[4][0] && marked[4][4];
    if (mode === 'x_pattern') return [0, 1, 2, 3, 4].every((i) => marked[i][i]) && [0, 1, 2, 3, 4].every((i) => marked[i][4 - i]);
    if (mode === 'full_house_blackout') return marked.every((row) => row.every(Boolean));
    for (let i = 0; i < 5; i++) {
      if (marked[i].every(Boolean)) return true;
      if ([0, 1, 2, 3, 4].every((r) => marked[r][i])) return true;
    }
    return [0, 1, 2, 3, 4].every((i) => marked[i][i]) || [0, 1, 2, 3, 4].every((i) => marked[i][4 - i]);
  }
}

module.exports = BingoEngineV2;
