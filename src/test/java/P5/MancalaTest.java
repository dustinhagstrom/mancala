package P5;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import P5.Side.Player;

class MancalaTest {
	
	private Mancala game;
	
	@BeforeEach
	void init() {
		game = new Mancala();
	}

	/*
	 * Test board initialization (setup correctly)
	 */
	@Test
	@DisplayName("Test board initialized with correct number of seeds")
	void testBoardInit() {
		Side[] board = game.getBoard();
		assertTrue(board[0].getNumSeedsInPits() == 24);
		assertTrue(board[0].getStoreCount() == 0);
		assertTrue(board[1].getNumSeedsInPits() == 24);
		assertTrue(board[1].getStoreCount() == 0);
	}
	
	@Test
	@DisplayName("Test Illegal Arg and valid args")
	void testValidAndInvalidPitNumbers() {
			//	Starting board
			//	P1:		[4, 4, 4, 4, 4, 4, 0]
			//	P2:		[4, 4, 4, 4, 4, 4, 0]
		
		// Illegal move doesn't throw exception or change current player
		assertDoesNotThrow(() -> game.performTurn(7));
		assertEquals(Player.One, game.getCurrentPlayer());

		// Legal move changes current player
		assertDoesNotThrow(() -> game.performTurn(1));
		assertEquals(Player.Two, game.getCurrentPlayer());
		
		// Legal move changes current player
		assertDoesNotThrow(() -> game.performTurn(1));
		assertEquals(Player.One, game.getCurrentPlayer());
		
		// Illegal move doesn't throw exception or change current player
		assertDoesNotThrow(() -> game.performTurn(1));
		assertEquals(Player.One, game.getCurrentPlayer());
		
		// Illegal move doesn't throw exception or change current player
		assertDoesNotThrow(() -> game.performTurn(-1));
		assertEquals(Player.One, game.getCurrentPlayer());
		
		// Illegal move doesn't throw exception or change current player
		assertDoesNotThrow(() -> game.performTurn(0));
		assertEquals(Player.One, game.getCurrentPlayer());
		
		// Illegal move doesn't throw exception or change current player
		assertDoesNotThrow(() -> game.performTurn(100));
		assertEquals(Player.One, game.getCurrentPlayer());
		
		// Illegal move doesn't throw exception or change current player
		assertDoesNotThrow(() -> game.performTurn(-100));
		assertEquals(Player.One, game.getCurrentPlayer());
		
	}
	
	@DisplayName("Test first move, lands on same side, not in store")
	@Test
	void testFirstMoveV0() {
		
		// final board representations
		int[] P1 = {0, 5, 5, 5, 5, 4, 0};
		int[] P2 = {4, 4, 4, 4, 4, 4, 0};
		Player playerMakingMoves = game.getCurrentPlayer();
		Player playerwaitingToMove = game.getWaitingPlayer();
		assertTrue(playerMakingMoves == Player.One);
		assertTrue(playerwaitingToMove == Player.Two);
		
		game.performTurn(1);
		
		Side[] board = game.getBoard();
		
		playerMakingMoves = game.getCurrentPlayer();
		playerwaitingToMove = game.getWaitingPlayer();
		assertTrue(playerwaitingToMove == Player.One);
		assertTrue(playerMakingMoves == Player.Two);
		assertArrayEquals(P1, board[0].getRow());
		assertArrayEquals(P2, board[1].getRow());
		
	}
	
	@DisplayName("Test first move, lands in store on same side")
	@Test
	void testFirstMoveV1() {
		
		// final board representations
		int[] P1 = {4, 4, 0, 5, 5, 5, 1};
		int[] P2 = {4, 4, 4, 4, 4, 4, 0};
		Player playerMakingMoves = game.getCurrentPlayer();
		Player playerwaitingToMove = game.getWaitingPlayer();
		assertTrue(playerMakingMoves == Player.One);
		assertTrue(playerwaitingToMove == Player.Two);
		
		game.performTurn(3);
		
		Side[] board = game.getBoard();
		
		
		assertTrue(playerMakingMoves == Player.One);
		assertTrue(playerwaitingToMove == Player.Two);
		assertArrayEquals(P1, board[0].getRow());
		assertArrayEquals(P2, board[1].getRow());
		
	}
	
	@DisplayName("Test first move, lands in pit on opposite side")
	@Test
	void testFirstMoveV2() {
		
		// final board representations
		int[] P1 = {4, 4, 4, 4, 4, 0, 1};
		int[] P2 = {5, 5, 5, 4, 4, 4, 0};
		
		Player playerMakingMoves = game.getCurrentPlayer();
		Player playerwaitingToMove = game.getWaitingPlayer();
		assertTrue(playerMakingMoves == Player.One);
		assertTrue(playerwaitingToMove == Player.Two);
		
		game.performTurn(6);
		
		Side[] board = game.getBoard();
		
		
		playerMakingMoves = game.getCurrentPlayer();
		playerwaitingToMove = game.getWaitingPlayer();
		assertTrue(playerwaitingToMove == Player.One);
		assertTrue(playerMakingMoves == Player.Two);
		assertArrayEquals(P1, board[0].getRow());
		assertArrayEquals(P2, board[1].getRow());
		
	}
	
	@DisplayName("Test capture opponent's seeds")
	@Test
	void testCaptureOpponentsSeeds() {
		
		game.performTurn(3);
		game.performTurn(4);
		game.performTurn(4);
		game.performTurn(2);
		game.performTurn(3);
		game.performTurn(5);
		game.performTurn(3);
		game.performTurn(1);
		game.performTurn(4);
		game.performTurn(1);
		game.performTurn(2);
		game.performTurn(3);
		game.performTurn(5);
		
		// final board representations
		int[] P1 = {1, 0, 2, 2, 0, 10, 8};
		int[] P2 = {1, 7, 1, 4, 0, 9, 3};
		Player playerMakingMoves = game.getCurrentPlayer();
		Player playerwaitingToMove = game.getWaitingPlayer();
		assertTrue(playerMakingMoves == Player.Two);
		assertTrue(playerwaitingToMove == Player.One);
		
		Side[] board = game.getBoard();
		
		
		assertArrayEquals(P1, board[0].getRow());
		assertArrayEquals(P2, board[1].getRow());
		
	}
	
	@DisplayName("Test game over, player one wins")
	@Test
	void testGameOverV0() {
		game.performTurn(3);
		game.performTurn(4);
		game.performTurn(4);
		game.performTurn(2);
		game.performTurn(3);
		game.performTurn(5);
		game.performTurn(3);
		game.performTurn(1);
		game.performTurn(4);
		game.performTurn(1);
		game.performTurn(2);
		game.performTurn(3);
		game.performTurn(5);
		game.performTurn(1);
		game.performTurn(4);
		game.performTurn(3);
		game.performTurn(3);
		game.performTurn(4);
		game.performTurn(5);
		game.performTurn(4);
		game.performTurn(6);
		game.performTurn(6);
		game.performTurn(5);
		game.performTurn(4);
		game.performTurn(5);
		game.performTurn(4);
		game.performTurn(3);
		game.performTurn(1);
		game.performTurn(6);
		
		// final board representations
		int[] P1 = {0, 0, 0, 0, 0, 0, 32};
		int[] P2 = {0, 0, 0, 0, 0, 0, 16};
		
		Side[] board = game.getBoard();
		
		
		assertArrayEquals(P1, board[0].getRow());
		assertArrayEquals(P2, board[1].getRow());
		
	}
	
	@DisplayName("Test game over, player one wins, players continue picking pits")
	@Test
	void testGameOverV1() {
		game.performTurn(3);
		game.performTurn(4);
		game.performTurn(4);
		game.performTurn(2);
		game.performTurn(3);
		game.performTurn(5);
		game.performTurn(3);
		game.performTurn(1);
		game.performTurn(4);
		game.performTurn(1);
		game.performTurn(2);
		game.performTurn(3);
		game.performTurn(5);
		game.performTurn(1);
		game.performTurn(4);
		game.performTurn(3);
		game.performTurn(3);
		game.performTurn(4);
		game.performTurn(5);
		game.performTurn(4);
		game.performTurn(6);
		game.performTurn(6);
		game.performTurn(5);
		game.performTurn(4);
		game.performTurn(5);
		game.performTurn(4);
		game.performTurn(3);
		game.performTurn(1);
		game.performTurn(6);
		/////////////////////////////////////////GAMEOVER HAS BEEN REACHED/////////////////////////////
		
		//ADDITIONAL MOVES CALLED - NO EFFECT EXPECTED
		game.performTurn(1);
		game.performTurn(2);
		game.performTurn(3);
		game.performTurn(4);
		game.performTurn(5);
		game.performTurn(6);
		
		// final board representations
		int[] P1 = {0, 0, 0, 0, 0, 0, 32};
		int[] P2 = {0, 0, 0, 0, 0, 0, 16};
		
		Side[] board = game.getBoard();
		
		
		assertArrayEquals(P1, board[0].getRow());
		assertArrayEquals(P2, board[1].getRow());
		
	}
	
	@DisplayName("Test game over, player two wins")
	@Test
	void testGameOverV2() {
		game.performTurn(6);
		game.performTurn(2);
		game.performTurn(6);
		game.performTurn(5);
		game.performTurn(4);
		game.performTurn(4);
		game.performTurn(2);
		game.performTurn(5);
		game.performTurn(6);
		game.performTurn(5);
		game.performTurn(2);
		game.performTurn(6);
		game.performTurn(1);
		game.performTurn(4);
		game.performTurn(6);
		game.performTurn(5);
		game.performTurn(5);
		game.performTurn(3);
		game.performTurn(3);
		game.performTurn(6);
		game.performTurn(6);
		game.performTurn(4);
		game.performTurn(6);
		game.performTurn(3);
		game.performTurn(4);
		game.performTurn(5);
		game.performTurn(2);
		game.performTurn(6);
		game.performTurn(2);
		
		// final board representations
		int[] P1 = {0, 0, 0, 0, 0, 0, 16};
		int[] P2 = {0, 0, 0, 0, 0, 0, 32};
		
		Side[] board = game.getBoard();
		
		
		assertArrayEquals(P1, board[0].getRow());
		assertArrayEquals(P2, board[1].getRow());
	}
	
	@DisplayName("Test game over, player one wins, pit capture ends game")
	@Test
	void testGameOverV3() {
		game.performTurn(3);
		game.performTurn(2);
		game.performTurn(3);
		game.performTurn(6);
		game.performTurn(1);
		game.performTurn(4);
		game.performTurn(3);
		game.performTurn(6);
		game.performTurn(5);
		game.performTurn(6);
		game.performTurn(3);
		game.performTurn(2);
		game.performTurn(4);
		game.performTurn(5);
		game.performTurn(1);
		game.performTurn(1);
		game.performTurn(6);
		game.performTurn(3);
		game.performTurn(2);
		game.performTurn(4);
		game.performTurn(3);
		game.performTurn(3);
		game.performTurn(1);
		
		// final board representations
		int[] P1 = {0, 0, 0, 0, 0, 0, 41};
		int[] P2 = {0, 0, 0, 0, 0, 0, 7};
		
		Side[] board = game.getBoard();
		
		
		assertArrayEquals(P1, board[0].getRow());
		assertArrayEquals(P2, board[1].getRow());
	}
	
	@DisplayName("Test game over, player one wins, last move is P2 moving last seed into store.")
	@Test
	void testGameOverV4() {
		game.performTurn(3);
		game.performTurn(2);
		game.performTurn(3);
		game.performTurn(6);
		game.performTurn(1);
		game.performTurn(4);
		game.performTurn(3);
		game.performTurn(6);
		game.performTurn(5);
		game.performTurn(6);
		game.performTurn(3);
		game.performTurn(2);
		game.performTurn(4);
		game.performTurn(5);
		game.performTurn(1);
		game.performTurn(1);
		game.performTurn(6);
		game.performTurn(3);
		game.performTurn(2);
		game.performTurn(4);
		game.performTurn(3);
		game.performTurn(5);
		game.performTurn(2);
		game.performTurn(3);
		game.performTurn(5);
		game.performTurn(6);
		
		// final board representations
		int[] P1 = {0, 0, 0, 0, 0, 0, 39};
		int[] P2 = {0, 0, 0, 0, 0, 0, 9};
		
		Side[] board = game.getBoard();
		
		
		assertArrayEquals(P1, board[0].getRow());
		assertArrayEquals(P2, board[1].getRow());
	}

}
