import comp1110.ass2.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

// This file is organized by EdenTian u7807670, the tests are authored by different team members
public class D2FTest {
    private List<Player> players;
    private Scoreboard scoreboard;
    private Player player1;
    private Player player2;
    private GameTemplate game;
    private PlayerSheet playerSheet;

    @BeforeEach
    public void setUp() {
        players = new ArrayList<>();
        player1 = new Player(0);
        player2 = new Player(1);
        players.add(player1);
        players.add(player2);
        scoreboard = new Scoreboard(players);
        game = new GameTemplate();
        playerSheet = new PlayerSheet();
        game.players.add(player1);
        game.players.add(player2);
        facadeSheet = new FacadeSheet();
        player = new Player(0);
        playerSheet = player.getPlayerSheet();
        coatsOfArms = new CoatsOfArms(player);
    }

    @Test //Authored by Eden Tian u7807670
    //Tested updateScore method in Scoreboard
    public void TestScoreboard1() {
        // Set up a player sheet with a completed row
        boolean[][] windowGrid = player1.getPlayerSheet().getWindowGrid();
        for (int j = 0; j < 5; j++) {
            windowGrid[0][j] = true;
        }
        player1.getPlayerSheet().setWindowGrid(windowGrid);

        // Update score and check the player's score
        scoreboard.updateScore(player1, 0, "row");
        assertEquals(2, player1.getScore(), "Player score should be 2 after completing a row.");
    }

    @Test //Authored by Eden Tian u7807670
    //Tested updateScore method in Scoreboard
    public void TestScoreboard2() {
        // Set up a player sheet with a completed column
        boolean[][] windowGrid = player1.getPlayerSheet().getWindowGrid();
        for (int i = 0; i < 9; i++) {
            windowGrid[i][0] = true;
        }
        player1.getPlayerSheet().setWindowGrid(windowGrid);

        // Update score and check the player's score
        scoreboard.updateScore(player1, 0, "column");
        assertEquals(4, player1.getScore(), "Player score should be 4 after completing a column.");
    }


    @Test //Authored by Eden Tian u7807670
    //Tested addTileShape method in playersheet
    public void TestPlayerSheet1() {
        Tile.TileShape tileShape = new Tile.TileShape("S1O");
        boolean[] windows = {true};

        playerSheet.addTileShape(tileShape, 10, 0, 0, windows);
        assertFalse(playerSheet.getGrid()[8][0], "Tile should not be added out of grid bounds.");
    }


    @Test //Authored by Eden Tian u7807670
    //Tested checkRowCompletion method in playersheet
    public void TestPlayerSheet2() {
        boolean[][] grid = playerSheet.getGrid();
        for (int i = 0; i < grid[0].length; i++) {
            grid[0][i] = true;
        }
        playerSheet.setWindowGrid(grid);

        assertTrue(playerSheet.checkRowCompletion(0), "Row 0 should be completed.");
    }


    @Test //Authored by Eden Tian u7807670
    //Tested checkColumnCompletion method in playersheet
    public void TestPlayerSheet3() {
        boolean[][] grid = playerSheet.getGrid();
        for (int i = 0; i < grid.length; i++) {
            grid[i][0] = true;
        }
        playerSheet.setWindowGrid(grid);

        assertTrue(playerSheet.checkColumnCompletion(0), "Column 0 should be completed.");
    }
    Player player3 = new Player(2);
    Player player4 = new Player(3);

    @Test //Authored by Larry Wang u7807744
    //Tested determineWinner method in scoreboard
    public void TestScoreboard_SoleWinner() {
        player1.setScore(12);
        player2.setScore(12);
        player3.setScore(13);
        player4.setScore(15);
        List<Player> players = new ArrayList<>(Arrays.asList(player1, player2, player3, player4));
        Scoreboard scoreboard = new Scoreboard(players);

        assertEquals("The winner is player4", scoreboard.determineWinner(), "Winner should be player 4.");
    }

    //Authored by Larry Wang u7807744
    //Tested determineWinner method in scoreboard
    @Test
    public void TestScoreboard_Winner1() {
        player1.setScore(11);
        player2.setScore(12);
        player3.setScore(13);
        player4.setScore(13);
        List<Player> players = new ArrayList<>(Arrays.asList(player1, player2, player3, player4));
        Scoreboard scoreboard = new Scoreboard(players);
        // Each player's empty space by default is 45, because no tiles are placed.

        assertEquals("Player3 and Player4 share the win!", scoreboard.determineWinner(), "Winner should be player3 and player4");
    }

    //Authored by Larry Wang u7807744
    //Tested determineWinner method in scoreboard
    @Test
    public void TestScoreboard_Winner2() {
        player1.setScore(11);
        player2.setScore(12);
        player3.setScore(13);
        player4.setScore(13);
        List<Player> players = new ArrayList<>(Arrays.asList(player1, player2, player3, player4));
        Scoreboard scoreboard = new Scoreboard(players);

        boolean[] windowsB3 = {true, false, true};
        FacadeSheet facadeSheet = new FacadeSheet();
        Tile.TileShape B3 = facadeSheet.B3();
        player4.getPlayerSheet().addTileShape(B3, 0, 0, 0, windowsB3);
        // Player4 places a tile B3.
        // Other player's empty space by default is 45, because no tiles are placed.

        assertEquals("The winner is player4", scoreboard.determineWinner(), "The winner is player4, for having less empty space");
    }


    // Tested trackCompletion method in AbilityTrack
    public void testAdvanceTrackAndCompletion() {
        AbilityTrack abilityTrack = new AbilityTrack();

        // Advancing the red track by 3 steps
        abilityTrack.advanceTrack(ColorTrack.Red, 3);

        // Ensuring that the track is not yet complete
        assertFalse(abilityTrack.trackCompletion(), "Red track should not be complete after advancing by 3 steps.");

        // Advancing the red track by 6 more steps to complete it
        abilityTrack.advanceTrack(ColorTrack.Red, 6);

        // Now the track should be complete
        assertTrue(abilityTrack.trackCompletion(), "Red track should be complete after advancing by 9 steps.");

        // Advancing the blue track by 9 steps to complete it
        abilityTrack.advanceTrack(ColorTrack.Blue, 9);

        // Ensuring that the track is now complete
        assertTrue(abilityTrack.trackCompletion(), "Blue track should be complete after advancing by 9 steps.");

        // Advancing the purple track by 9 steps to complete it
        abilityTrack.advanceTrack(ColorTrack.Purple, 9);

        // Ensuring that the track is now complete
        assertTrue(abilityTrack.trackCompletion(), "Purple track should complete after advancing by 9 steps.");

    }

    @Test
    // Tested trackCompletion method in AbilityTrack
    public void TestAbilityTrack() {
        AbilityTrack abilityTrack = new AbilityTrack();

        // Advancing the red track by 3 steps
        abilityTrack.advanceTrack(ColorTrack.Red, 3);
        assertFalse(abilityTrack.trackCompletion(), "Red track should not be complete after advancing by 3 steps.");

        // Advancing the red track by 6 more steps to complete it
        abilityTrack.advanceTrack(ColorTrack.Red, 6);
        assertTrue(abilityTrack.trackCompletion(), "Red track should be complete after advancing by 9 steps.");

        // Reset the AbilityTrack for independent testing of the blue track
        abilityTrack = new AbilityTrack();

        // Advancing the blue track by 9 steps to complete it
        abilityTrack.advanceTrack(ColorTrack.Blue, 9);
        assertTrue(abilityTrack.trackCompletion(), "Blue track should be complete after advancing by 9 steps.");

        // Reset the AbilityTrack for independent testing of the purple track
        abilityTrack = new AbilityTrack();

        // Advancing the purple track by 9 steps to complete it
        abilityTrack.advanceTrack(ColorTrack.Purple, 9);
        assertTrue(abilityTrack.trackCompletion(), "Purple track should complete after advancing by 9 steps.");

        // Reset the AbilityTrack for independent testing of the yellow track
        abilityTrack = new AbilityTrack();

        // Advancing the yellow track by 4 steps, should not complete it
        abilityTrack.advanceTrack(ColorTrack.Yellow, 4);
        assertFalse(abilityTrack.trackCompletion(), "Yellow track should not be complete after advancing by 4 steps.");
    }

    @Test //Authored by Eden Tian u7807670
    //Tested drawTile method in FacadeSheet
    public void TestFacadeSheet1(){
        Tile.TileShape player1Tile = player1.getFacadeSheet().drawTile("B3");
        assertTrue(player1Tile != null && "B3".equals(player1Tile.getName()),
                "Player1 should be able to draw B3 from unlimited shapes");
        Tile.TileShape player2Tile = player2.getFacadeSheet().drawTile("B3");
        assertTrue(player2Tile != null && "B3".equals(player1Tile.getName()),
                "Player2 should be able to draw B3 from unlimited shapes");
    }

    @Test //Authored by Eden Tian u7807670
    //Tested drawTile method in FacadeSheet
    public void TestFacadeSheet2(){
        Tile.TileShape player1Tile = player1.getFacadeSheet().drawTile("P5");
        assertTrue(player1Tile != null && "P5".equals(player1Tile.getName()),
                "Player1 should be able to draw P5 from limited shapes");
        System.out.println(player1.getFacadeSheet().getLimitedShapes().toString());
        System.out.println(player2.getFacadeSheet().getLimitedShapes().toString());
        Tile.TileShape player2Tile = player2.getFacadeSheet().drawTile("P5");
        System.out.println(player2.getFacadeSheet().getLimitedShapes().toString());
        assertTrue(player2Tile == null, "Player2 should not be able to draw P5 because it's limited and has been used by Player1");
    }


    private Player player;
    private CoatsOfArms coatsOfArms;
    private Tile redTile;
    private Tile blueTile;
    private FacadeSheet facadeSheet;


    /**
     * Tests if a Tile is created with the correct color.
     */
    @Test
    public void TestTile1() {
        redTile = new Tile(Color.Red);
        assertEquals(Color.Red, redTile.getColor(), "The tile should have the color Red.");
    }

    /**
     * Tests if a TileShape is created and drawn correctly from the FacadeSheet.
     */
    @Test
    public void TestTile2() {
        // Draw a specific tile (R2) from the facade sheet
        Tile.TileShape r2Tile = facadeSheet.R2();

        // Verify that the tile is constructed correctly with the right color and positions
        assertEquals("R2", r2Tile.getName(), "The tile shape should be named 'R2'.");
        assertEquals(2, r2Tile.tiles.size(), "The R2 tile should consist of 2 tiles.");
        assertEquals(Color.Red, r2Tile.tiles.get(0).getTile().getColor(), "The R2 tile should have red tiles.");
    }


    /**
     * Test for detecting and applying a row bonus.
     * The bonus will add a window in a specific location when a row is completed.
     */
    @Test
    public void TestCoatOfArms() {
        // Simulate completing a row (index 0)
        boolean[][] grid = playerSheet.getGrid();
        for (int i = 0; i < 5; i++) {
            grid[0][i] = true;  // Mark all cells in row 0 as filled (row is completed)
        }

        // Check if row 0 triggers a bonus
        boolean rowBonusTriggered = coatsOfArms.checkForBonus(playerSheet, "row", 0);
        assertTrue(rowBonusTriggered, "Row 0 should trigger a bonus.");

        // Apply bonus to add a window at row 1, column 1 (Bonus choice 1 adds a window)
        coatsOfArms.applyBonus(player, 1, null, 1, 1);
    }

}




