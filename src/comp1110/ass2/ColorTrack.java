package comp1110.ass2;

/**
 * This class is authored by Larry Wang u7807744.
 */
public enum ColorTrack {
    //save the index of bonus and ability spots on different colors' track
    //and the completion initialization is false.
    Red(new int[]{1, 3, 4, 5}, new int[]{0, 2}, false),   // Red Track
    Blue(new int[]{1, 3, 5}, new int[]{0, 4}, false),  // Blue Track
    Purple(new int[]{2, 4, 6}, new int[]{0, 3}, false),// Purple Track
    Green(new int[]{3, 6, 7}, new int[]{0, 4}, false), // Green Track
    Yellow(new int[]{2, 5, 6}, new int[]{0, 3},false);// Yellow Track

    private final int[] starUnlockPoints;  // starUnlock
    private final int[] crossUnlockPoints; // crossUnlock
    private boolean completion;     // completion

    //constructor
    ColorTrack(int[] starUnlockPoints, int[] crossUnlockPoints, boolean completion) {
        this.starUnlockPoints = starUnlockPoints;
        this.crossUnlockPoints = crossUnlockPoints;
        this.completion = completion;
    }

    //getters
    public int[] getStarUnlockPoints() {
        return starUnlockPoints;
    }

    public int[] getCrossUnlockPoints() {
        return crossUnlockPoints;
    }

    public boolean getCompletion() {
        return completion;
    }

    //set the boolean as true if the track is completed
    public void setCompletion(){
        completion = true;
    }

    // Check if it's an unlock point for star
    public boolean isStarUnlockPoint(int position) {
        for (int point : starUnlockPoints) {
            if (point == position) {
                return true;
            }
        }
        return false;
    }

    // Check if it's an unlock point for cross
    public boolean isCrossUnlockPoint(int position) {
        for (int point : crossUnlockPoints) {
            if (point == position) {
                return true;
            }
        }
        return false;
    }

}


