package comp1110.ass2;

import java.util.*;

import comp1110.ass2.Tile.TileShape;


/**
 * Represents the set of available tiles of limited and unlimited that players can draw from during their turn.
 * This Class was authored by EdenTian u7807670
 */
public class FacadeSheet {
    // Fields
    public List<TileShape> unlimitedShapes;// to project different shapes with its name
    public Map<TileShape,Integer>limitedShapes;//to record the number of limited shapes left
    private boolean ignoreLimit = false;//for purpose of CoA bonus, initiate it 'false' first


    //Create different TileShape according to 'tile-names' file with the method in class Tile
    //Authored by EdenTian u7807670
    public TileShape S1X(){
        TileShape shape=new TileShape("S1X");
        shape.addTile(new Tile(Color.Gray),0,0);
        return shape;
    }

    public TileShape S1O(){
        TileShape shape=new TileShape("S1O");
        shape.addTile(new Tile(Color.Gray),0,0);
        return shape;
    }
    public TileShape R2(){
        TileShape shape=new TileShape("R2");
        shape.addTile(new Tile(Color.Red),0,0);
        shape.addTile(new Tile(Color.Red),0,1);
        return shape;
    }

    public TileShape R3(){
        TileShape shape=new TileShape("R3");
        shape.addTile(new Tile(Color.Red),0,0);
        shape.addTile(new Tile(Color.Red),1,0);
        shape.addTile(new Tile(Color.Red),1,1);
        return shape;
    }

    public TileShape R4(){
        TileShape shape=new TileShape("R4");
        shape.addTile(new Tile(Color.Red),0,0);
        shape.addTile(new Tile(Color.Red),0,1);
        shape.addTile(new Tile(Color.Red),1,0);
        shape.addTile(new Tile(Color.Red),1,1);
        return shape;
    }
    public TileShape R5(){
        TileShape shape=new TileShape("R5");
        shape.addTile(new Tile(Color.Red),0,0);
        shape.addTile(new Tile(Color.Red),1,1);
        shape.addTile(new Tile(Color.Red),1,0);
        shape.addTile(new Tile(Color.Red),2,0);
        shape.addTile(new Tile(Color.Red),2,1);
        return shape;
    }

    public TileShape B2(){
        TileShape shape=new TileShape("B2");
        shape.addTile(new Tile(Color.Blue),0,0);
        shape.addTile(new Tile(Color.Blue),0,1);
        return shape;
    }

    public TileShape B3(){
        TileShape shape=new TileShape("B3");
        shape.addTile(new Tile(Color.Blue),0,0);
        shape.addTile(new Tile(Color.Blue),1,0);
        shape.addTile(new Tile(Color.Blue),1,1);
        return shape;
    }

    public TileShape B4L(){
        TileShape shape=new TileShape("B4L");
        shape.addTile(new Tile(Color.Blue),0,0);
        shape.addTile(new Tile(Color.Blue),1,0);
        shape.addTile(new Tile(Color.Blue),1,1);
        shape.addTile(new Tile(Color.Blue),1,2);
        return shape;
    }

    public TileShape B4R(){
        TileShape shape=new TileShape("B4R");
        shape.addTile(new Tile(Color.Blue),0,0);
        shape.addTile(new Tile(Color.Blue),0,1);
        shape.addTile(new Tile(Color.Blue),0,2);
        shape.addTile(new Tile(Color.Blue),1,0);
        return shape;
    }

    public TileShape B5(){
        TileShape shape=new TileShape("B5");
        shape.addTile(new Tile(Color.Blue),0,0);
        shape.addTile(new Tile(Color.Blue),1,0);
        shape.addTile(new Tile(Color.Blue),1,1);
        shape.addTile(new Tile(Color.Blue),1,2);
        shape.addTile(new Tile(Color.Blue),2,0);
        return shape;
    }
    public TileShape P2(){
        TileShape shape=new TileShape("P2");
        shape.addTile(new Tile(Color.Purple),0,0);
        shape.addTile(new Tile(Color.Purple),0,1);
        return shape;
    }

    public TileShape P3(){
        TileShape shape=new TileShape("P3");
        shape.addTile(new Tile(Color.Purple),0,0);
        shape.addTile(new Tile(Color.Purple),0,1);
        shape.addTile(new Tile(Color.Purple),0,2);
        return shape;
    }

    public TileShape P4(){
        TileShape shape=new TileShape("P4");
        shape.addTile(new Tile(Color.Purple),0,0);
        shape.addTile(new Tile(Color.Purple),0,1);
        shape.addTile(new Tile(Color.Purple),0,2);
        shape.addTile(new Tile(Color.Purple),0,3);
        return shape;
    }

    public TileShape P5(){
        TileShape shape=new TileShape("P5");
        shape.addTile(new Tile(Color.Purple),0,0);
        shape.addTile(new Tile(Color.Purple),1,0);
        shape.addTile(new Tile(Color.Purple),2,0);
        shape.addTile(new Tile(Color.Purple),3,0);
        shape.addTile(new Tile(Color.Purple),4,0);
        return shape;
    }
    public TileShape G2(){
        TileShape shape=new TileShape("G2");
        shape.addTile(new Tile(Color.Green),0,0);
        shape.addTile(new Tile(Color.Green),0,1);
        return shape;
    }

    public TileShape G3(){
        TileShape shape=new TileShape("G3");
        shape.addTile(new Tile(Color.Green),0,0);
        shape.addTile(new Tile(Color.Green),0,1);
        shape.addTile(new Tile(Color.Green),0,2);
        return shape;
    }
    public TileShape G4L(){
        TileShape shape=new TileShape("G4L");
        shape.addTile(new Tile(Color.Green),0,1);
        shape.addTile(new Tile(Color.Green),1,0);
        shape.addTile(new Tile(Color.Green),1,1);
        shape.addTile(new Tile(Color.Green),1,2);
        return shape;
    }

    public TileShape G4R(){
        TileShape shape=new TileShape("G4R");
        shape.addTile(new Tile(Color.Green),0,0);
        shape.addTile(new Tile(Color.Green),0,1);
        shape.addTile(new Tile(Color.Green),0,2);
        shape.addTile(new Tile(Color.Green),1,1);
        return shape;
    }

    public TileShape G5(){
        TileShape shape=new TileShape("G4R");
        shape.addTile(new Tile(Color.Green),0,1);
        shape.addTile(new Tile(Color.Green),1,0);
        shape.addTile(new Tile(Color.Green),1,1);
        shape.addTile(new Tile(Color.Green),1,2);
        shape.addTile(new Tile(Color.Green),2,1);
        return shape;
    }

    public TileShape Y2(){
        TileShape shape=new TileShape("Y2");
        shape.addTile(new Tile(Color.Yellow),0,0);
        shape.addTile(new Tile(Color.Yellow),0,1);
        return shape;
    }

    public TileShape Y3(){
        TileShape shape=new TileShape("Y3");
        shape.addTile(new Tile(Color.Yellow),0,0);
        shape.addTile(new Tile(Color.Yellow),1,0);
        shape.addTile(new Tile(Color.Yellow),1,1);
        return shape;
    }

    public TileShape Y4L(){
        TileShape shape=new TileShape("Y4L");
        shape.addTile(new Tile(Color.Yellow),0,0);
        shape.addTile(new Tile(Color.Yellow),0,1);
        shape.addTile(new Tile(Color.Yellow),1,1);
        shape.addTile(new Tile(Color.Yellow),1,2);
        return shape;
    }

    public TileShape Y4R(){
        TileShape shape=new TileShape("Y4R");
        shape.addTile(new Tile(Color.Yellow),0,1);
        shape.addTile(new Tile(Color.Yellow),0,2);
        shape.addTile(new Tile(Color.Yellow),1,1);
        shape.addTile(new Tile(Color.Yellow),1,0);
        return shape;
    }
    public TileShape Y5(){
        TileShape shape=new TileShape("Y5");
        shape.addTile(new Tile(Color.Yellow),0,1);
        shape.addTile(new Tile(Color.Yellow),0,2);
        shape.addTile(new Tile(Color.Yellow),1,1);
        shape.addTile(new Tile(Color.Yellow),2,1);
        shape.addTile(new Tile(Color.Yellow),2,2);
        return shape;
    }

    //Constructor, containing limited shapes and unlimited
    //Authored by EdenTian u7807670
    public FacadeSheet(){
        unlimitedShapes=new ArrayList<>();
        limitedShapes=new HashMap<>();

        unlimitedShapes.add(R2());
        unlimitedShapes.add(R3());
        unlimitedShapes.add(P2());
        unlimitedShapes.add(P3());
        unlimitedShapes.add(B2());
        unlimitedShapes.add(B3());
        unlimitedShapes.add(G2());
        unlimitedShapes.add(G3());
        unlimitedShapes.add(Y2());
        unlimitedShapes.add(Y3());
        unlimitedShapes.add(S1X());
        unlimitedShapes.add(S1O());

        limitedShapes.put(R4(),2);
        limitedShapes.put(R5(),1);
        limitedShapes.put(B4L(),1);
        limitedShapes.put(B4R(),1);
        limitedShapes.put(B5(),1);
        limitedShapes.put(P4(),2);
        limitedShapes.put(P5(),1);
        limitedShapes.put(G4L(),1);
        limitedShapes.put(G4R(),1);
        limitedShapes.put(G5(),1);
        limitedShapes.put(Y4L(),1);
        limitedShapes.put(Y4R(),1);
        limitedShapes.put(Y5(),1);

    }

    public void setIgnoreLimit(boolean ignoreLimit) {
        this.ignoreLimit = ignoreLimit;
    }


    // Methods
    /**
     * Allows a player to draw a tile of a specific color from the sheet, if the tile is from the limited, then deduct 1
     * Authored by EdenTian u7807670
     */
    public TileShape drawTile(String name) {
        // First, check if the tile is in the unlimitedShapes
        for (TileShape shape : unlimitedShapes) {
            if (shape.getName().equals(name)) {
                // Return the unlimited tile directly
                return shape;
            }
        }

        // If not found in unlimited, check in the limitedShapes
        for (Map.Entry<TileShape, Integer> entry : limitedShapes.entrySet()) {
            TileShape shape = entry.getKey();
            if (shape.getName().equals(name)) {
                int limitedRemained = entry.getValue();

                // Ignore the limit if ignoreLimit is true
                if (ignoreLimit && limitedRemained <= 0) {
                    limitedRemained = 1; // Reset to 1 to allow reuse of this tile
                    limitedShapes.put(shape, limitedRemained); // Update the count
                }

                if (limitedRemained > 0) {
                    limitedShapes.put(shape, limitedRemained - 1); // Decrease the count
                    return shape;
                } else {
                    System.out.println(name + " is used up");
                    return null;
                }
            }
        }

        // If tile is neither in unlimited nor available in limited
        return null;
    }


    // Methods
    /**
     * Allows a player to draw a tile of a specific color from the sheet regardless of the tiles' limits
     * For Ability Yellow specially
     * Authored by EdenTian u7807670
     */
    public TileShape drawAnyTile(String name){
        List<TileShape> availableShapes = getAvailableTiles();
        for (TileShape shape : availableShapes) {
            if (shape.getName().equals(name)) {
                return shape;
            }
        }
        return null;
    }

    /**
     * Returns the list of tiles currently available for selection.Getters.
     * Authored by EdenTian u7807670
     */

    public List<TileShape>getUnlimitedShapes(){
        return unlimitedShapes;
    }

    public Map<TileShape, Integer> getLimitedShapes() {
        return limitedShapes;
    }


    /**
     * Returns a list with all the limited and unlimited tiles
     * Authored by EdenTian u7807670
     */
    public List<TileShape> getAvailableTiles() {
        List<TileShape> unlimitedTileList=unlimitedShapes;
        List<TileShape> limitedTileList= new ArrayList<>(limitedShapes.keySet());
        ArrayList<TileShape> availableTiles=new ArrayList<>();
        availableTiles.addAll(limitedTileList);
        availableTiles.addAll(unlimitedTileList);
        return availableTiles;
    }


    //for debugging
    public void printLimitedShapes() {
        System.out.println("Limited Shapes:");
        for (Map.Entry<TileShape, Integer> entry : limitedShapes.entrySet()) {
            System.out.println("Tile: " + entry.getKey() + ", Remaining: " + entry.getValue());
        }
    }


    //Static inner class FacadeSheet to make sure all the players are sharing this one FacadeSheet
    //Authored by EdenTian u7807670
    public class SharedResources {
        private static FacadeSheet facadeSheet = new FacadeSheet();

        public static FacadeSheet getFacadeSheet() {
            return facadeSheet;
        }
    }



}