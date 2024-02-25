/**
 * @author Dustin Hagstrom
 * @version 1.0
 */

package P5;

import java.util.Arrays;

/**
 * {@code Side} defines a {@code Player}'s {@code Side} of the {@code Mancala}
 * game board, consisting of six pits and one store.
 * 
 */

public class Side {

	public static final int SEED_START_VALUE = 4; // the starting value of pits.
	public static final int ROW_SIZE = 7; // Six pits and one store make a
											// Player's row.
	public static final int STORE_INDEX = 6;// The index of the store.
											// row[STORE_INDEX] is the store.
	private final Player player; // The Player on this Side
	private final int[] row; // This Side's pits and store
	private int actionIndex; // Index of row where next game action occurs.

	/**
	 * Represents the players in {@code Mancala}, a two-player game. The
	 * {@code Player} enumeration defines two constants: {@code One} and
	 * {@code Two}. This enumeration is used to distinguish between two players
	 * and their actions during the game play of {@code Mancala}.
	 *
	 */
	public enum Player {
		One, Two;
	}

	/**
	 * Constructs a Side object with the {@code Player} value provided as
	 * {@code player} argument.
	 * 
	 * @param player Either {@code Player.One} or {@code Player.Two}.
	 */
	public Side(Player player) {
		this.player = player;
		this.row = new int[ROW_SIZE];
		Arrays.fill(row, 0, STORE_INDEX, SEED_START_VALUE);
	}

	/**
	 * This method calculates whether the chosen pit number is valid or not.
	 * 
	 * @param index The index of {@code row} equating to chosen pit
	 * 
	 * @return True if it is a valid choice, False if not valid.
	 */
	public boolean isValidPit(int index) {
		if (index >= 0 && index <= 5) {
			if (row[index] != 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * This method removes the seeds from the current player's chosen pit and
	 * returns these seeds to the calling method.
	 * 
	 * @param index The index of {@code row} equating to chosen pit
	 * @return The number of seeds in the chosen pit.
	 */
	public int collectSeedsFromPit(int index) {
		actionIndex = index;

		int temp = row[actionIndex];
		row[actionIndex] = 0;
		return temp;
	}

	/**
	 * @param ourMove    True if the sowing of seeds is during our turn, false
	 *                   if it is the opposing player's move.
	 * @param seedsToSow The number of seeds that are to be sown.
	 */
	public void sowSeedsIntoPits(boolean ourMove, int seedsToSow) {
		if (ourMove) {
			for (int i = 0; i < seedsToSow; i++) { // loop through entire row
				actionIndex++;
				actionIndex = actionIndex % ROW_SIZE;
				row[actionIndex] += 1;
			}
		} else {
			actionIndex = 0;
			for (int j = 0; j < seedsToSow; j++) { // loop up to excluding seed
													// store
				actionIndex = actionIndex % STORE_INDEX;
				row[actionIndex++] += 1;
			}
		}

	}

	/**
	 * This collects all of the seeds in pits on our {@code Side} and puts them
	 * into our store.
	 */

	public void emptyPitsToStore() {
		int total = (int) Arrays.stream(row, 0, STORE_INDEX).sum();
		addSeedsToStore(total);
		Arrays.fill(row, 0, STORE_INDEX, 0);
	}

	/**
	 * This adds the {@code seeds} value to our store.
	 * 
	 * @param seeds The number of seeds to add to our store.
	 */
	public void addSeedsToStore(int seeds) {
		row[STORE_INDEX] += seeds;
	}

	/**
	 * This is an accessor method for the player instance field.
	 * 
	 * @return player The {@code Player} enumeration value for this
	 *         {@code Side}.
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * This is an accessor method for the row instance field.
	 * 
	 * @return row the {@code row} for this {@code Side}.
	 */
	public int[] getRow() {
		return row;
	}

	/**
	 * This is an accessor method for the actionIndex instance field.
	 * 
	 * @return actionIndex the {@code actionIndex} for this {@code Side}.
	 */
	public int getActionIndex() {
		return actionIndex;
	}

	/**
	 * This is an accessor method to get the seed count from the chosen pit.
	 * 
	 * @param index The index of the {@code row} representing the chosen pit.
	 * @return the number of the seeds in the chosen pit or -1 if the chosen pit
	 *         is invalid.
	 */
	public int getPitCount(int index) {
		return isValidPit(index) ? row[index] : -1;
	}

	/**
	 * This is an accessor method to get the store seed count.
	 * 
	 * @return the number of seeds in the store.
	 */
	public int getStoreCount() {
		return row[STORE_INDEX];
	}

	/**
	 * This method calculates the total number of seeds that are in pits,
	 * excluding the number of seeds in the store.
	 * 
	 * @return the total number of seeds in pits.
	 */
	public int getNumSeedsInPits() {
		return Arrays.stream(row, 0, STORE_INDEX).sum();
	}
}
