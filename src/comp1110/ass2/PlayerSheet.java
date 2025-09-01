package comp1110.ass2;


import comp1110.ass2.Tile.TileShape;

import java.util.List;

/**
 * The grid-based board where players place TileShapes to build their facades.
 */
public class PlayerSheet {
    private boolean[][] grid = new boolean[9][5];  // 5*9 grid, true if it has a tile, false if not
    private boolean[][] windowGrid = new boolean[9][5]; // True if there is a window in that position

    private Player player;

    /**
     * Adds a TileShape to the specified position on the grid.
     * The TileShape object defines its shape and the positions it covers on the grid.
     * The shape will be rotated based on the provided rotation value.
     * The window array specifies where windows should be placed.
     *
     * @param tileShape the TileShape to be placed, which contains multiple Tile blocks
     * @param x         the x-coordinate where the TileShape is placed
     * @param y         the y-coordinate where the TileShape is placed
     * @param rotation  the rotation of the TileShape (0 = no rotation, 1 = 90°, 2 = 180°, 3 = 270°)
     * @param windows   boolean array representing window configuration for each tile block
     */
    public void addTileShape(TileShape tileShape, int x, int y, int rotation, boolean[] windows) {
        List<Tile.ShapeofTile> rotatedTiles = tileShape.rotate(rotation);
        int tileIndex = 0; // Used to track the index of each tile block in the shape

        // Add the TileShape to the grid and apply the window configuration
        for (Tile.ShapeofTile tile : rotatedTiles) {
            int newX = x + tile.getX();
            int newY = y + tile.getY();

            // Adjust the y-coordinate to fit the grid with (0, 0) being at the bottom-left
            int adjustedY = 8 - newY; // Since the grid's 0th index is at the top, subtracting from 8 flips the y-axis

            // Ensure the TileShape can fit within the grid bounds
            if (newX >= grid[0].length || adjustedY >= grid.length || newX < 0 || adjustedY < 0) {
                System.out.println("TileShape '" + tileShape.getName() + "' doesn't fit within the grid boundaries at (" + newX + ", " + adjustedY + ")!");
                return; // Early exit if out of bounds
            }

            // Check for overlap with existing tiles
            if (grid[adjustedY][newX]) {
                System.out.println("TileShape '" + tileShape.getName() + "' overlaps with an existing tile at (" + newX + ", " + adjustedY + ")!");
                return; // Early exit if there is an overlap
            }

            // Mark the grid position as filled with a tile
            grid[adjustedY][newX] = true;

            // Apply the window configuration to the grid
            if (windows != null && tileIndex < windows.length) {
                windowGrid[adjustedY][newX] = windows[tileIndex];
            }

            tileIndex++;
        }

        // Success case: All tiles were successfully added to the grid
        System.out.println("TileShape '" + tileShape.getName() + "' successfully added at starting position (" + x + ", " + y + ") with rotation " + (rotation * 90) + "° and windows " + java.util.Arrays.toString(windows));
    }


    /**
     * Checks if the TileShape can be placed at the specified coordinates. (possibility)
     * It ensures the TileShape does not go out of bounds or overlap with existing tiles, and is supported.
     *
     * @param tileShape the TileShape to be placed, which contains multiple Tile blocks
     * @param x         the x-coordinate where the TileShape is placed
     * @param y         the y-coordinate where the TileShape is placed
     * @param rotation  the rotation of the TileShape (0, 1, 2, 3 representing 0°, 90°, 180°, 270°)
     * @return true if the placement is valid, false otherwise
     */

    public boolean isValidPlacement(Tile.TileShape tileShape, int x, int y, int rotation) {
        java.util.List<Tile.ShapeofTile> rotatedTiles = tileShape.rotate(rotation);

        for (Tile.ShapeofTile tile : rotatedTiles) {
            int newX = x + tile.getX();
            int newY = y + tile.getY();
            int adjustedY = 8 - newY;

            // check if it's within the grid
            if (newX < 0 || newX >= grid[0].length || adjustedY < 0 || adjustedY >= grid.length) {
                System.out.println("TileShape placement is out of grid bounds at (" + newX + ", " + newY + ")");
                return false;
            }

            // check overlapping
            if (grid[adjustedY][newX]) {
                System.out.println("TileShape placement overlaps at (" + newX + ", " + adjustedY + ")");
                return false;
            }
        }
        //check if it has the support
        boolean hasSupport = false;
        for (Tile.ShapeofTile tile : rotatedTiles) {
            int newX = x + tile.getX();
            int newY = y + tile.getY();
            int adjustedY = 8 - newY;

            if (adjustedY < grid.length - 1 && grid[adjustedY + 1][newX]) {
                hasSupport = true;
            }
        }

        if (y > 0 && !hasSupport) {
            System.out.println("TileShape is not supported at (" + x + ", " + y + ")");
            return false;
        }

        return true;
    }


    /**
     * Prints the current grid state for debugging purposes.
     */
    public void printGrid() {
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length; col++) {
                if (grid[row][col]) {
                    System.out.print(windowGrid[row][col] ? "[W]" : "[T]"); // W for window, T for tile without window
                } else {
                    System.out.print("[ ]");
                }
            }
            System.out.println();
        }
    }


    /**
     * Checks if a row is fully completed and scores it.
     */
    public boolean checkRowCompletion(int rowIndex) {
        for (int i = 0; i < grid[rowIndex].length; i++) {
            if (!grid[rowIndex][i]) {
                return false;
            }
        }
        return true;
    }


    /**
     * Checks if a column is fully completed and scores it.
     */
    public boolean checkColumnCompletion(int columnIndex) {
        for (int i = 0; i < grid[columnIndex].length; i++) {
            if (!grid[i][columnIndex]) {
                return false;
            }
        }
        return true;
    }


    /**
     * Allow player to add one specific window for CoA bonus and Color blue ability
     * Authored by Larry Wang u7807744
     */
    public void addWindow(int rowIndex, int columnIndex) {
        Tile.TileShape S1O = player.getFacadeSheet().S1O();
        int rotation = 0;
        boolean[] windows10 = new boolean[]{true};
        if (isValidPlacement(S1O, rowIndex, columnIndex, rotation)) {
            addTileShape(S1O, rowIndex, columnIndex, rotation, windows10);
            System.out.println("S1O Tile placed at (" + rowIndex + ", " + columnIndex + ")");
        } else {
            System.out.println("S1O Tile placement is invalid at (" + rowIndex + ", " + columnIndex + ")");
        }
    }


    /**
     * Allow player to add one specific X(tile without window) tile for Color purple ability
     * Authored by Larry Wang u7807744
     */
    public void addOneX(int rowIndex, int columnIndex) {
        Tile.TileShape S1X = player.getFacadeSheet().S1X();
        int rotation = 0;
        boolean[] windows10 = new boolean[]{false};
        if (isValidPlacement(S1X, rowIndex, columnIndex, rotation)) {
            addTileShape(S1X, rowIndex, columnIndex, rotation, windows10);
            System.out.println("S1X Tile placed at (" + rowIndex + ", " + columnIndex + ")");
        } else {
            System.out.println("S1X Tile placement is invalid at (" + rowIndex + ", " + columnIndex + ")");
        }
    }

    //getters:
    //Authored by Larry Wang u7807744
    public boolean[][] getWindowGrid() {

        return windowGrid;
    }

    public boolean[][] getGrid() {
        return grid;
    }


    //Authored by Larry Wang u7807744
    public int getEmptySpace() {
        int countEmptySpace = 0;
        for (int i = 0; i < 9; i += 1) {
            for (int j = 0; j < 5; j += 1) {
                if (!grid[i][j])
                    countEmptySpace += 1;
            }
        }
        System.out.println("the empty space is" + countEmptySpace);
        return countEmptySpace;
    }

    // Setters:
    // Authored by Eden Tian u7807670
    public void setWindowGrid(boolean[][] windowGrid) {
        this.windowGrid = windowGrid;
    }

    // Authored by Eden Tian u7807670
    public void setEmptySpace(int emptySpace) {
        // For debug purpose
        int count = 0;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 5; j++) {
                if (!grid[i][j]) {
                    count++;
                }
            }
        }
        if (count != emptySpace) {
            System.out.println("Warning: The provided empty space value does not match the actual count.");
        }
    }
}


