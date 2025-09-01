package comp1110.ass2;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The building block used to construct the player's facade, comes in different colors and sizes.
 * All the size of the tile will be 1*1, therefore tile only has one field which is color.
 * Inner static method TileShape for creating different shapes of tiles used in FacadeSheet,
 * also with the instance method of rotating shapes with int Rotation.
 * This Class was authored by EdenTian u7807670
 */
public class Tile {
    // Fields
    private Color color; // The color of the tile, influencing placement and abilities.

    public Tile(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }


    // Inner class as Tile builder
    //Authored by EdenTian u7807670
    static class ShapeofTile {
        private Tile tile;
        private int x;
        private int y;

        public ShapeofTile(Tile tile, int x, int y) {
            this.tile = tile;
            this.x = x;
            this.y = y;
        }

        public Tile getTile() {
            return tile;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }

    // Static class for creating different shapes of tiles with rotations
    // Authored by EdenTian u7807670
    public static class TileShape {
        public List<ShapeofTile> tiles;
        private String name;

        // Constructor
        public TileShape(String name) {
            this.tiles = new ArrayList<>();
            this.name = name;
        }

        // Adds a tile to the shape with its relative position
        // Authored by EdenTian u7807670
        public void addTile(Tile tile, int x, int y) {
            tiles.add(new ShapeofTile(tile, x, y));
        }

        public String getName() {
            return name;
        }

        // Override toString() to return a meaningful representation of TileShape
        @Override
        public String toString() {
            return "TileShape{" + "name='" + name + '\'' + '}';
        }

        // Method for shape to be rotated
        // Authored by EdenTian u7807670
        public List<ShapeofTile> rotate(int rotation) {
            List<ShapeofTile> rotatedTiles = new ArrayList<>();
            // Iterate through each tile in the shape
            for (ShapeofTile shapeTile : tiles) {
                int newX = shapeTile.getX();
                int newY = shapeTile.getY();

                // Apply rotation
                for (int i = 0; i < rotation; i++) {
                    int tempX = newX;
                    newX = newY;
                    newY = -tempX;
                }

                rotatedTiles.add(new ShapeofTile(shapeTile.getTile(), newX, newY));
            }


            // use a random big number to initialise the minX and minY
            int minX = 20;
            int minY = 20;

            // Find the minimum X and Y values
            for (ShapeofTile rotatedTile : rotatedTiles) {
                if (rotatedTile.getX() < minX) {
                    minX = rotatedTile.getX();
                }
                if (rotatedTile.getY() < minY) {
                    minY = rotatedTile.getY();
                }
            }

            // Move the axis so that rotated coordinates could still be represented in relative coordinate format (to get rid of
            // the negative coordinates)
            List<ShapeofTile> normalizedTiles = new ArrayList<>();
            for (ShapeofTile rotatedTile : rotatedTiles) {
                int normalizedX = rotatedTile.getX() - minX;
                int normalizedY = rotatedTile.getY() - minY;
                normalizedTiles.add(new ShapeofTile(rotatedTile.getTile(), normalizedX, normalizedY));
            }

            return normalizedTiles;

        }





    }
}






