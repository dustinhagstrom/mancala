/**
 * @author Dustin Hagstrom
 * @version 1.0
 */
package P5;

import P5.Side.Player;

/**
 * The {@code Mancala} class represents the game Mancala.
 * 
 */
public class Mancala {
	private static final int NUM_PLAYERS = 2; // number of Players.
	private final Side[] board; // consists of two Sides.
	private Side sideInPlay; // the Side whose turn is now.
	private Side sideWaiting; // the Side whose turn is next.
	private int seedsToSow; // the number of seeds to sow.
	private int pitIndex; // the pit number converted to index number.
	private boolean gameOverConditionMet = false; // flag to indicate game over.

	/**
	 * Constructs a new instance of {@code Mancala}.
	 */
	public Mancala() {
		this.board = new Side[NUM_PLAYERS];
		board[0] = new Side(Player.One);
		board[1] = new Side(Player.Two);
		sideInPlay = board[0];
		sideWaiting = board[1];
	}

	/**
	 * This method performs the necessary function of performing a move of game
	 * play. It makes the desired move, and computes whether the game is over or
	 * not. If not, it determines if the current player gets another turn or if
	 * the game should switch turns to the waiting player. 
	 * 
	 * @param pitNumber Must be between 1 and 6 inclusive. The pitNumber that
	 *                  represents the pit the current player wants to select to
	 *                  distribute seeds on the board. This number is converted
	 *                  to it's index value for accessing data in
	 *                  currentPlayerSide.
	 * 
	 */

	public void performTurn(int pitNumber) {

		pitIndex = pitNumber - 1;

		if (gameOverConditionMet) {
			// prompt for new game goes here
			return;
		}

		if (!sideInPlay.isValidPit(pitIndex)) {
			return;
		}

		removeSeedsFromPit();

		Player playerWhoRcvdLastSeed = sowSeeds();

		if (playerWhoRcvdLastSeed == getCurrentPlayer()) {
			performLastSeedCurrentPlayerSide();
		} else {
			performLastSeedWaitingPlayerSide();
		}
	}

	/*
	 * Helper method: to collect seeds from current player's chosen pit on their
	 * side of the board. It places the seeds into the instance variable
	 * seedsToSow for distribution on the game board.
	 */

	private void removeSeedsFromPit() {
		seedsToSow = sideInPlay.collectSeedsFromPit(pitIndex);
	}

	/*
	 * Helper method: to sow seeds onto the current player's board and then onto
	 * the waiting player's side of the board, it returns the Player enumeration
	 * value where the last seed landed.
	 */

	private Player sowSeeds() {
		int indexOffset = Side.STORE_INDEX - pitIndex;
		Player playerWhoRcvdLastSeed = getCurrentPlayer();
		boolean ourMove = true;

		if (indexOffset >= seedsToSow) {
			sideInPlay.sowSeedsIntoPits(ourMove, seedsToSow);
			seedsToSow = 0;
			return playerWhoRcvdLastSeed;
		}

		sideInPlay.sowSeedsIntoPits(ourMove, indexOffset);
		seedsToSow -= indexOffset;

		playerWhoRcvdLastSeed = sowSeedsAroundTheBoardStartingWithOpponentSide();
		return playerWhoRcvdLastSeed;
	}

	/*
	 * Helper method: to alternate the sowing of the seeds onto the waiting
	 * players side and then onto current player's side. It tracks which player
	 * received the last seed and returns the Player enumeration value where the
	 * last seed landed.
	 */

	private Player sowSeedsAroundTheBoardStartingWithOpponentSide() {

		Player playerWhoRcvdLastSeed = getCurrentPlayer();
		boolean isOurTurn = true;

		for (int i = 0; seedsToSow > 0; i++) {
			if (i % NUM_PLAYERS == 0) { // waiting player side
				int batchOfSeeds = Math.min(seedsToSow, Side.STORE_INDEX);
				sideWaiting.sowSeedsIntoPits(!isOurTurn, batchOfSeeds);
				playerWhoRcvdLastSeed = getWaitingPlayer();
				seedsToSow -= batchOfSeeds;
			} else { // current player side
				int batchOfSeeds = Math.min(seedsToSow, Side.ROW_SIZE);
				sideInPlay.sowSeedsIntoPits(isOurTurn, batchOfSeeds);
				playerWhoRcvdLastSeed = getCurrentPlayer();
				seedsToSow -= batchOfSeeds;
			}
		}

		return playerWhoRcvdLastSeed;
	}

	/*
	 * Helper method: to capture seeds in that are in waiting player's pit when
	 * the current player's last seed placed during a turn of game play lands on
	 * current player's side in an empty pit.
	 */

	private void captureOpponentsSeeds(int lastPitIndex) {
		int capturedSeeds = sideInPlay.collectSeedsFromPit(lastPitIndex);

		switch (lastPitIndex) {
		case 0:
			capturedSeeds += sideWaiting.collectSeedsFromPit(5);
			break;
		case 1:
			capturedSeeds += sideWaiting.collectSeedsFromPit(4);
			break;
		case 2:
			capturedSeeds += sideWaiting.collectSeedsFromPit(3);
			break;
		case 3:
			capturedSeeds += sideWaiting.collectSeedsFromPit(2);
			break;
		case 4:
			capturedSeeds += sideWaiting.collectSeedsFromPit(1);
			break;
		case 5:
			capturedSeeds += sideWaiting.collectSeedsFromPit(0);
			break;
		}

		sideInPlay.addSeedsToStore(capturedSeeds);
	}

	/*
	 * Helper method: to handle the condition where the last seed placed during
	 * a turn of game play lands on the waiting player's side.
	 */

	private void performLastSeedWaitingPlayerSide() {

		if (isGameOver()) {
			performGameOver();
		} else {
			switchTurns();
		}

	}

	/*
	 * Helper method: to handle the condition where the last seed placed during
	 * a turn of game play lands on the current player's side.
	 */

	private void performLastSeedCurrentPlayerSide() {

		int lastPitIndex = sideInPlay.getActionIndex();

		// lands in store
		if (lastPitIndex == Side.STORE_INDEX) {
			if (isGameOver()) {
				performGameOver();
			}
			return; // current player gets another turn
		}

		// lands in empty pit
		else if (sideInPlay.getPitCount(lastPitIndex) == 1) {
			captureOpponentsSeeds(lastPitIndex);
			if (isGameOver()) {
				performGameOver();
			} else {
				switchTurns();
			}
		}

		// lands in non-empty pit
		else {
			switchTurns();
		}

	}

	/*
	 * Helper method: to sweep all of the seeds within pits to their respective
	 * stores. Sets game over condition variable to true
	 */

	private void performGameOver() {
		sideInPlay.emptyPitsToStore();
		sideWaiting.emptyPitsToStore();
		gameOverConditionMet = true;
	}

	/*
	 * Helper method: to switch the current player's side of the board to the
	 * opposite player's side of the board, so that they may have a turn at game
	 * play.
	 */

	private void switchTurns() {
		Side temp = sideInPlay;
		sideInPlay = sideWaiting;
		sideWaiting = temp;

	}

	/*
	 * Helper method: checks the game over condition and returns boolean to
	 * indicate game over condition met.
	 */

	private boolean isGameOver() {
		return sideInPlay.getNumSeedsInPits() == 0
				|| sideWaiting.getNumSeedsInPits() == 0;
	}

	/**
	 * Accessor method to get the current {@code Player} whose turn it is.
	 * 
	 * @return {@code Player}, the current player.
	 */

	public Player getCurrentPlayer() {
		return sideInPlay.getPlayer();
	}

	/**
	 * Accessor method to get the waiting {@code Player} whose turn is next.
	 * 
	 * @return {@code Player}, the waiting player.
	 */

	public Player getWaitingPlayer() {
		return sideWaiting.getPlayer();
	}

	/**
	 * This method returns a copy of the current {@code Side} whose turn it is.
	 * 
	 * @return {@code Side}, the current {@code Side} of the {@code board}.
	 */

	public Side[] getBoard() {
		return board.clone();
	}

	/**
	 * Print method that prints the current {@code Mancala} {@code board}. If
	 * the game is over, it also announces the {@code Player} that has won. If
	 * the game is not over, it announces the {@code Player} whose turn it is.
	 */

	public void printCurrentGameStatus() {
		Player playerToPrint = sideInPlay.getPlayer();
		String msg = String.format("Player %s's turn.%n Current board:%n",
				playerToPrint);
		if (isGameOver()) {
			playerToPrint = sideInPlay.getStoreCount() > sideWaiting
					.getStoreCount() ? sideInPlay.getPlayer()
							: sideWaiting.getPlayer();
			msg = String.format("Player %s has won!!%n Final board: ",
					playerToPrint);
		}

		System.out.println(String.format("%28s%n" + this.toString(), msg));
	}

	/**
	 * This method formats and prints a representation of the {@code Mancala}
	 * {@code board} with {@code Player.One}'s {@code Side} of the board on the
	 * bottom with its store on the Right; {@code Player.Two}'s {@code Side} of
	 * the board on the top with its store on the Left.
	 * 
	 * @return String, the board as a String.
	 */

	@Override
	public String toString() {
		Player currentPlayer = sideInPlay.getPlayer();
		int[] playerOneRow = currentPlayer == Player.One ? sideInPlay.getRow()
				: sideWaiting.getRow();
		int[] playerTwoRow = currentPlayer == Player.Two ? sideInPlay.getRow()
				: sideWaiting.getRow();
		String filler = "---------------------------";
		String middleFiller = "|=================|";
		return String.format(
				"%s%n" + "|   |%2d|%2d|%2d|%2d|%2d|%2d|   |%n" + "|%3d%s%3d|%n"
						+ "|   |%2d|%2d|%2d|%2d|%2d|%2d|   |%n" + "%s%n",
				filler, playerTwoRow[5], playerTwoRow[4], playerTwoRow[3],
				playerTwoRow[2], playerTwoRow[1], playerTwoRow[0],
				playerTwoRow[6], middleFiller, playerOneRow[6], playerOneRow[0],
				playerOneRow[1], playerOneRow[2], playerOneRow[3],
				playerOneRow[4], playerOneRow[5], filler);
	}

}