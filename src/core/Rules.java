package core;

public class Rules {
	
	/**
	 * Calculate score for each player.
	 * @param board Board completely covered with stones (a terminal state).
	 * @return Information which color won the game: "w" for white, "b" for 
	 * black, "0" for a draw.
	 */
	public static String calculateScore(Board board) {
		String[][] b = board.getState();
		int scoreWhite = 0, scoreBlack = 0;

		// Check horizontally.
		for(int x = 0; x < 7; ++x) {
			for(int y = 0; y < 7; ++y) {
				int whiteLine = 1, blackLine = 1;
				try {
					if(b[x][y].equals("w")) {
						try {
							while(b[x][y+1].equals("w")) {
								whiteLine++;
								y++;
							}
						}catch(Exception e) { }
					} else if(b[x][y].equals("b")){
						try {
							while(b[x][y+1].equals("b")) {
								blackLine++;
								y++;
							}
						} catch(Exception e) { } 
					}
				} catch(Exception e) { }
				switch(whiteLine) {
				case 3:
					scoreWhite += 3;
					break;
				case 4:
					scoreWhite += 10;
					break;
				case 5:
					scoreWhite += 25;
					break;
				case 6:
					scoreWhite += 56;
					break;
				case 7:
					scoreWhite += 119;
					break;
				default:
					break;
				}
				switch(blackLine) {
				case 3:
					scoreBlack += 3;
					break;
				case 4:
					scoreBlack += 10;
					break;
				case 5:
					scoreBlack += 25;
					break;
				case 6:
					scoreBlack += 56;
					break;
				case 7:
					scoreBlack += 119;
					break;
				default:
					break;
				}				
			}
			
		}
		
		// Reverse matrix.
		String tmp;
		for(int x = 0; x < 7; ++x) {
			for(int y = x; y < 7; ++y) {
				tmp = b[x][y];
				b[x][y] = b[y][x];
				b[y][x] = tmp; 
			}
		}
		
		// Check horizontally on reversed matrix (=> vertically).
		for(int x = 0; x < 7; ++x) {
			for(int y = 0; y < 7; ++y) {
				int whiteLine = 1, blackLine = 1;
				try {
					if(b[x][y].equals("w")) {
						try {
							while(b[x][y+1].equals("w")) {
								whiteLine++;
								y++;
							}
						}catch(Exception e) { }
					} else if(b[x][y].equals("b")){
						try {
							while(b[x][y+1].equals("b")) {
								blackLine++;
								y++;
							}
						} catch(Exception e) { } 
					}
				} catch(Exception e) { }
				switch(whiteLine) {
				case 3:
					scoreWhite += 3;
					break;
				case 4:
					scoreWhite += 10;
					break;
				case 5:
					scoreWhite += 25;
					break;
				case 6:
					scoreWhite += 56;
					break;
				case 7:
					scoreWhite += 119;
					break;
				default:
					break;
				}
				switch(blackLine) {
				case 3:
					scoreBlack += 3;
					break;
				case 4:
					scoreBlack += 10;
					break;
				case 5:
					scoreBlack += 25;
					break;
				case 6:
					scoreBlack += 56;
					break;
				case 7:
					scoreBlack += 119;
					break;
				default:
					break;
				}				
			}
			
		}
		
		if(scoreWhite == scoreBlack) {
			return "0";
		} else if(scoreWhite > scoreBlack) {
			return "w";
		} else {
			return "b";
		}
	}

}
