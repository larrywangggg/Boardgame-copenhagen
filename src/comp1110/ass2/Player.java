package comp1110.ass2;
import comp1110.ass2.Tile.TileShape;
import java.util.*;

/**
 * Represents each player, tracking their personal sheet, abilities, and score.
 * line 27 this.facadeSheet = new FacadeSheet();  need revision.
 */
public class Player {
    // Fields
    private int playerIndex; // The index of the player, from 0-3
    private PlayerSheet playerSheet; // The player's board where TileShapes are placed to form the facade.
    private AbilityTrack abilityTrack; // Tracks the progress of the player's abilities based on dice rolls.
    private int score; // The current score of the player.
    private Dice Dice;// managing the dice rolls during the game
    private FacadeSheet facadeSheet; //object representing the tiles available for the player to choose from

    private List<TileShape> availableTiles;// List to store the available tiles for the player
    private int tempTrackSelection = -1; // -1 means no track is chosen.
    private CoatsOfArms coatsOfArms;

    // Constructor
    public Player(int playerIndex) {
        this.playerIndex = playerIndex;
        this.playerSheet = new PlayerSheet();
        this.abilityTrack = new AbilityTrack();
        this.score = 0;// Initialize player score
        this.Dice = new Dice();
        this.facadeSheet = new FacadeSheet();
    }

    // Getters
    public PlayerSheet getPlayerSheet() {
        return playerSheet;
    }

    public AbilityTrack getAbilityTrack() {
        return abilityTrack;
    }

    public int getScore() {
        return score;
    }

    public FacadeSheet getFacadeSheet(){
        return FacadeSheet.SharedResources.getFacadeSheet();
    } //to make sure all the players are using only one FacadeSheet

    public void setScore(int score){
        this.score = score ;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    // Rolls and reRolls the dice, allowing the player to select tiles or advance abilities.
    public void rollDice() {
        Dice.roll();
        System.out.println("Player " + playerIndex + " is rolling the dice...");
    }

    //Authored by Larry Wang u7807744
    public void reRollDice(int[] indexes) {
        Dice.reRollDice(indexes);
        // avail redStar -= 1;
        System.out.println("Player " + playerIndex + " is reRolling the dice...");
    }

    //Authored by Larry Wang u7807744
    public String[] getDice() {
        return Dice.getCurrentRolls();
    }

    //Authored by Larry Wang u7807744
    public Dice getDiceObject() {
        return Dice;
    }

    // Method to add an available tile to the player's list of tiles
    public void addAvailableTile(TileShape tile) {
        availableTiles.add(tile);
        System.out.println("Tile " + tile.getName() + " added to Player " + playerIndex + "'s available tiles.");
    }

    // Method to get the available tiles for the player
    public List<TileShape> getAvailableTiles() {
        return availableTiles;
    }


    /**
     * Allows the player to choose a TileShape from the FacadeSheet based on the dice roll.
     * The player can only select a tile whose color matches the dice roll.
     * If the player has enough dice of the corresponding color, they can select larger tiles (up to size 5).
     * White dice can be used as wildcards (any color). For special abilities, they can draw any tile.
     * the ability track not yet been used, need to revise it in the future. But for D2C it is okay.
     */
    public boolean chooseTile(TileShape targetTileShape,boolean useSpecialAbility,boolean useBonus,String whichBonus) {
        String[] currentRolls;
        if (!useBonus){
            currentRolls = getDice();  // Get the dice results
        } else {
            currentRolls = new String[6];
            System.arraycopy(getDice(),0,currentRolls,0,5);
            currentRolls[5] = whichBonus;
        }
        int[] colorCount = new int[5];      // Array to count red, blue, purple, green, yellow dice
        int wildcardCount = 0;              // Count the number of white dice (wildcards)

        // Count the number of dice for each color and wildcards
        for (String roll : currentRolls) {
            switch (roll) {
                case "Red":
                    colorCount[0]++;
                    break;
                case "Blue":
                    colorCount[1]++;
                    break;
                case "Purple":
                    colorCount[2]++;
                    break;
                case "Green":
                    colorCount[3]++;
                    break;
                case "Yellow":
                    colorCount[4]++;
                    break;
                case "White":  // White is a wildcard
                    wildcardCount++;
                    break;
                default:
                    break;
            }
        }

        // Display the available tiles for selection from facadesheet
        List<TileShape> availableTiles = facadeSheet.getAvailableTiles();

        //Check tile eligibility
        String tileName = targetTileShape.getName(); //represent the name of the TileShape that the player is attempting to select, getName() is taken from Tile class
        char tileColor = tileName.charAt(0);  // The first letter represents the color of the tile
        int tileSize = Character.getNumericValue(tileName.charAt(1)); // The second character is the size of the tile
        boolean canChoose = false;

        // If special ability is activated, allow player to choose any tile using drawAnyTile from FacadeSheet
        if (useSpecialAbility) {
            System.out.println("Special ability activated! Player " + playerIndex + " can choose any tile.");
            TileShape chosenTile = facadeSheet.drawAnyTile(tileName); //from FacadeSheet
            System.out.println("Player " + playerIndex + " chose tile: " + chosenTile.getName());
        }

        // Otherwise, determine if the player can choose the tile based on dice roll
        //checks the tile’s color (tileColor) and compares the number of dice of that color
        // (plus any wildcard dice) to the size of the tile (tileSize)
        switch (tileColor) {
            case 'R':  // Red
                canChoose = (colorCount[0] + wildcardCount) >= tileSize;
                //should add 1 if the abilityTrack available cross number is greater than 1.
                break;
            case 'B':  // Blue
                canChoose = (colorCount[1] + wildcardCount) >= tileSize;
                break;
            case 'P':  // Purple
                canChoose = (colorCount[2] + wildcardCount) >= tileSize;
                break;
            case 'G':  // Green
                canChoose = (colorCount[3] + wildcardCount) >= tileSize;
                break;
            case 'Y':  // Yellow
                canChoose = (colorCount[4] + wildcardCount) >= tileSize;
                break;
            default:
                break;
        }

        return canChoose; //the method returns true if the player can pick the tile (based on their dice or the special ability), or
        // false if they can’t.
    }


    /**
     * Store the track selection index temporarily.
     * and the other one is to get the track selection index.
     * Authored by Larry Wang u7807744.
     */
    public void setTempTrackSelection(int trackIndex) {
        this.tempTrackSelection = trackIndex;
    }

    public int getTempTrackSelection() {
        return this.tempTrackSelection;
    }

}

