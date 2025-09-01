package comp1110.ass2;
import comp1110.ass2.Player;

/**
 * Handles the bonus system, awarding extra points or abilities when players complete rows or columns.
 */
public class CoatsOfArms {
    private Player player;
    public CoatsOfArms(Player player){
        this.player=player;
    }
    // Methods
    /**
     * Evaluates the player's sheet for any completed bonus conditions.
     * Authored by EdenTian u7807670
     */

    public boolean checkForBonus(PlayerSheet playerSheet, String type, int index) {
        if (type.equals("row")) {
            return playerSheet.checkRowCompletion(index);
        } else if (type.equals("column")) {
            return playerSheet.checkColumnCompletion(index);
        }
        return false;
    }


    /**
     * Applies the bonus effects to the player.
     * Authored by EdenTian u7807670
     */
    public void applyBonus(Player player, int CoABonusChoice, ColorTrack color, int rowIndex, int columnIndex) {
        PlayerSheet playerSheet = player.getPlayerSheet();


        if (checkForBonus(playerSheet, "row", rowIndex)) {
            System.out.println("Row " + rowIndex + " bonus triggered!");
            if (CoABonusChoice == 1) {
                addWindow(player, rowIndex, columnIndex);
            }
        }

        if (checkForBonus(playerSheet, "column", columnIndex)) {
            System.out.println("Column " + columnIndex + " bonus triggered!");
            if (CoABonusChoice == 2) {
                advanceAbilityTrack(player, color);
            }
        }
    }


    /**
     * Allow player to add a window when a CoatOfArm is completed.
     * Authored by EdenTian u7807670
     */
    private void addWindow(Player player,int rowIndex, int columnIndex) {
        player.getPlayerSheet().addWindow(rowIndex,columnIndex);
    }

    /**
     * Allow player to advance 2 steps in an ability track when a CoatOfArm is completed.
     * Authored by EdenTian u7807670
     */
    private void advanceAbilityTrack(Player player,ColorTrack color) {
        player.getAbilityTrack().advanceTrack(color,2);
    }
}

