package comp1110.ass2;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import comp1110.ass2.Scoreboard;
import comp1110.ass2.gui.GameGUI;

/**
 * Tracks the progress of player abilities, which can be advanced by selecting specific dice rolls.
 * This class is authored by Larry Wang u7807744.
 */
public class AbilityTrack {
    // Fields
    public Map<ColorTrack, Integer> colorProgress = new HashMap<>();// for record progress(mark) of each color track.
    public Map<ColorTrack, Integer> starAvail = new HashMap<>();// for record available star numbers of each color track.
    public Map<ColorTrack, Integer> crossAvail = new HashMap<>();// for record available cross numbers of each color track.
    public Map<ColorTrack, Integer> starNext = new HashMap<>();// for record Next available star of each color track.
    public Map<ColorTrack, Integer> crossNext = new HashMap<>();// for record Next available cross of each color track.
    public FacadeSheet facadeSheet;

    //constructor
    public AbilityTrack() {
        for (ColorTrack track : ColorTrack.values()) {
            colorProgress.put(track, 0);  // initialise the mark of each color track
            if (track.name().equals("Red")) {
                starAvail.put(track, 2); // red track has 2 stars at the beginning
            } else {
                starAvail.put(track, 0); // initialise other tracks with 0 stars.
            }
            crossAvail.put(track, 0); // initialise the cross of every track with 0.
            updateStarNext(track); //initialise the starNext
            updateCrossNext(track);//initialise the crossNext
        }
    }

    /**
     * Unlocks the ability associated with the color on the ability track.
     * advanceNumber is by default 1; But in the case of CoatsOfArm bonus, advanceNumber can be 2.
     * Authored by Larry Wang u7807744.
     */
    public void advanceTrack(ColorTrack track, int steps) {
        int currentMark = colorProgress.get(track);
        int newMark = currentMark + steps;// by default 1, unless use ability
        colorProgress.put(track, newMark);

        // update Available Star/Cross number
        if (track.isStarUnlockPoint(newMark-1)) {
            int currentStars = starAvail.get(track);
            if (currentStars < track.getStarUnlockPoints().length) { // should not be greater than the upper bound
                starAvail.put(track, currentStars + 1);
            }
        }

        if (track.isCrossUnlockPoint(newMark-1)) {
            int currentCrosses = crossAvail.get(track);
            if (currentCrosses < track.getCrossUnlockPoints().length) { // should not be greater than the upper bound
                crossAvail.put(track, currentCrosses + 1);
            }
        }
        updateStarNext(track); //update the starNext on track info after advancing
        updateCrossNext(track);//update the crossNext on track info after advancing
    }

    /**
     * enumerate the Next star unlock point.
     * Authored by Larry Wang u7807744.
     */
    public void updateStarNext(ColorTrack track){
        int currentMark = colorProgress.get(track);
        if (track.name().equals("Red")){
            if (currentMark==1||currentMark== 3||currentMark== 4||currentMark == 5)
                starNext.put(track,1);
            else if (currentMark==0||currentMark== 2)
                starNext.put(track,2);
            else starNext.put(track,0);
        }
        else if (track.name().equals("Blue")){
            if (currentMark==1||currentMark== 3||currentMark== 5)
                starNext.put(track,1);
            else if (currentMark==0||currentMark== 2||currentMark== 4)
                starNext.put(track,2);
            else starNext.put(track,0);
        }
        else if (track.name().equals("Purple")){
            if (currentMark==2||currentMark== 4||currentMark== 6)
                starNext.put(track,1);
            else if (currentMark==1||currentMark== 3||currentMark== 5)
                starNext.put(track,2);
            else if (currentMark==0)
                starNext.put(track,3);
            else starNext.put(track,0);
        }
        else if (track.name().equals("Green")){
            if (currentMark==3||currentMark== 6||currentMark== 7)
                starNext.put(track,1);
            else if (currentMark==2||currentMark== 5)
                starNext.put(track,2);
            else if (currentMark==1||currentMark== 4)
                starNext.put(track,3);
            else if (currentMark==0)
                starNext.put(track,4);
            else starNext.put(track,0);
        }
        else if (track.name().equals("Yellow")){
            if (currentMark==2||currentMark== 5||currentMark== 6)
                starNext.put(track,1);
            else if (currentMark==1||currentMark== 4)
                starNext.put(track,2);
            else if (currentMark==0||currentMark== 3)
                starNext.put(track,3);
            else starNext.put(track,0);
        }
    }

    /**
     * enumerate the Next cross unlock point.
     * Authored by Larry Wang u7807744.
     */
    public void updateCrossNext(ColorTrack track){
        int currentMark = colorProgress.get(track);
        if (track.name().equals("Red")){
            if (currentMark==0||currentMark== 2)
                crossNext.put(track,1);
            else if (currentMark==1)
                crossNext.put(track,2);
            else crossNext.put(track,0);
        }
        else if (track.name().equals("Blue")||track.name().equals("Green")){
            if (currentMark==0||currentMark== 4)
                crossNext.put(track,1);
            else if (currentMark==3)
                crossNext.put(track,2);
            else if (currentMark==2)
                crossNext.put(track,3);
            else if (currentMark==1)
                crossNext.put(track,4);
            else crossNext.put(track,0);
        }
        else if (track.name().equals("Purple")||track.name().equals("Yellow")){
            if (currentMark==0||currentMark==3)
                crossNext.put(track,1);
            else if (currentMark==2)
                crossNext.put(track,2);
            else if (currentMark==1)
                crossNext.put(track,3);
            else crossNext.put(track,0);
        }
    }

    /**
     * Check each track's completion, and avoid repeatedly update.
     * Authored by Larry Wang u7807744.
     */
    public boolean trackCompletion(){
        for( ColorTrack track : ColorTrack.values())
            //if the track is complete AND its completion is not mark as true. return true and set it to true.
            if (colorProgress.get(track) == 9 && !track.getCompletion()){
                track.setCompletion();// update its completion
                return true;
            }
        return false;
    }

    //Below are ability methods

    public void useRedAbility(Player player, int[] diceIndexes){
        player.reRollDice(diceIndexes);
    }

    /**
     * Allows the player to use Blue ability : add an extra window when placing a tile.
     * Might need to revise.
     * Authored by Larry Wang u7807744.
     */
    public void useBlueAbility(Player player) {
        // Add S1O to their availableTile List
        Tile.TileShape s1oTile = new Tile.TileShape("S1O");
        player.addAvailableTile(s1oTile);
    }


    /**
     * Allows the player to use Purple ability : place a tile without window.
     * Authored by Larry Wang u7807744.
     */
    public void usePurpleAbility(Player player){
        Tile.TileShape s1oTile = new Tile.TileShape("S1X");
        player.addAvailableTile(s1oTile);
    }


    /**
     * Allows the player to use Green ability : change any dices in same color to an expected color.
     * Authored by Larry Wang u7807744.
     */
    public void useGreenAbility(Player player,int[] diceToChange, String newColor){
        String[] currentDice = player.getDice();
        for (int i = 0; i < diceToChange.length; i+= 1) {
            int indexToChange = diceToChange[i];
            currentDice[indexToChange]= newColor;
        }
        player.getDiceObject().setCurrentRolls(currentDice); //update current dice
    }

    /**
     * Allows the player to use Yellow ability : choose any tiles(even if it's limited and has been used.)
     * Authored by Larry Wang u7807744.
     */
    public void useYellowAbility(Player player) {
        FacadeSheet facadeSheet = FacadeSheet.SharedResources.getFacadeSheet();

        facadeSheet.setIgnoreLimit(true);

        List<String> tileNames = List.of("S1O", "S1X", "R2", "R3", "R4", "R5", "B2", "B3", "B4L", "B4R", "B5",
                "Y2", "Y3", "Y4L", "Y4R", "Y5", "P2", "P3", "P4", "P5", "G2", "G3", "G4L", "G4R", "G5");

        for (String tileName : tileNames) {
            Tile.TileShape tileShape = facadeSheet.drawTile(tileName);
            if (tileShape != null) {
                player.addAvailableTile(tileShape);
            }
        }

        facadeSheet.setIgnoreLimit(false);

        System.out.println("Yellow ability activated! Player " + player.getPlayerIndex() + " can choose any tile.");
    }




}

