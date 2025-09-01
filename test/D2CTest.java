import comp1110.ass2.Player;
import comp1110.ass2.Tile;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class D2CTest {
    /**
     * Create an instance of the game for two players, set the available dice to three red, one blue and one white.
     * Test your code for checking if the current player can select the red size 3 tile (should be allowed),
     * the red size 4 tile (should be allowed) and the blue size 3 tile (should not be allowed).
     */

    Player player1 = new Player(0);


    /**
     * Create an instance of the game for two players, and add two tiles to the first player's building.
     * The two tile placements are:
     * tile = "B3", x = 0, y = 0, rotation = 1, windows = [true, false, true];
     * tile = "G4L", x = 3, y = 0, rotation = 0, windows = [true, false, true, true].
     * Then test the valid and invalid tile placements as described.
     */
    Tile.TileShape B3 = player1.getFacadeSheet().B3();
    Tile.TileShape G4L = player1.getFacadeSheet().G4L();
    Tile.TileShape Y3 = player1.getFacadeSheet().Y3();

    boolean[] windowsB3 = {true, false, true};
    boolean[] windowsG4L = {true, false, true, true};
    //boolean[] windowY3 = {true, false, true};


    @Test
    public void testTilePlacement() {
        player1.getPlayerSheet().addTileShape(B3, 0, 0, 1, windowsB3);
        player1.getPlayerSheet().addTileShape(G4L, 3, 0, 0, windowsG4L);
        //player1.getPlayerSheet().addTileShape(Y3, 0, 2, 0, windowsG4L);
        player1.getPlayerSheet().printGrid();

        assertFalse(player1.getPlayerSheet().isValidPlacement(B3, 0, 0, 0), "B3 should be placed at (0, 0) with rotation 1");
//        player1.getPlayerSheet().addTileShape(B3, 0, 0, 1, windowsB3);

        assertFalse(player1.getPlayerSheet().isValidPlacement(G4L, 3, 0, 0), "G4L should be placed at (3, 0) with rotation 0");
//        player1.getPlayerSheet().addTileShape(G4L, 3, 0, 0, windowsG4L);

        //Test Y3 at (1, 0), rotation 0 -> should be invalid
        assertFalse(player1.getPlayerSheet().isValidPlacement(Y3, 1, 0, 0), "Y3 at (1, 0) with rotation 0 should be invalid");

        // Test Y3 at (1, 0), rotation 3 -> should be valid
        assertTrue(player1.getPlayerSheet().isValidPlacement(Y3, 1, 0, 3), "Y3 at (1, 0) with rotation 3 should be valid");

        // Test Y3 at (2, 0), rotation 3 -> should be invalid
        assertFalse(player1.getPlayerSheet().isValidPlacement(Y3, 2, 0, 3), "Y3 at (2, 0) with rotation 3 should be invalid");

        // Test Y3 at (2, 0), rotation 1 -> should be valid
        assertTrue(player1.getPlayerSheet().isValidPlacement(Y3, 2, 0, 1), "Y3 at (2, 0) with rotation 1 should be valid");

        // Test Y3 at (1, 1), rotation 3 -> should be invalid
        assertFalse(player1.getPlayerSheet().isValidPlacement(Y3, 1, 1, 3), "Y3 at (1, 1) with rotation 3 should be invalid");

        // Test Y3 at (0, 2), rotation 0 -> should be valid
        assertTrue(player1.getPlayerSheet().isValidPlacement(Y3, 0, 2, 0), "Y3 at (0,2) with rotation 0 should be valid");
    }
}
