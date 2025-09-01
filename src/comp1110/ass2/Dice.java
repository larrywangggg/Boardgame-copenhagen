package comp1110.ass2;

import comp1110.ass2.gui.Colour;

import java.util.Arrays;
import java.util.Random;

/**
 * Represents the dice used by players to determine actions, such as tile selection or advancing abilities.
 * Authored by Larry Wang u7807744.
 */
public class Dice {
    // Fields
    private static final String[] Colors = {"Red", "Blue", "Purple", "Green", "Yellow", "White"};
    private static final String wildColor = "White";

    private Random random;
    private String[] currentRolls;

    public Dice() {
        random = new Random();
        currentRolls = new String[5];
    }

    // Methods

    /**
     * Roll 5 dice at a time ,and each num from 0-5 returns a corresponding color.
     * Authored by Larry Wang u7807744.
     */
    public String[] roll() {
        String[] rolledColors = new String[5];  // A string[] of 5 dice.
        for (int i = 0; i < 5; i++) {
            int randomNum = random.nextInt(6);
            rolledColors[i] = Colors[randomNum];  // update the string[].
        }
        currentRolls = rolledColors;
        return currentRolls;
    }

    /**
     * Allows the player to reRoll specific dice(s).
     * Authored by Larry Wang u7807744.
     */
    public void reRollDice(int[] indexes) {
        for (int index : indexes) {
            if (index >= 0 && index < currentRolls.length) {
                // Re-roll only the dice at the specified index
                int randomNum = random.nextInt(6); // Generate a new random number for this dice
                currentRolls[index] = Colors[randomNum]; // Assign the new color
            } else {
                System.out.println("Illegal choice for dice index: " + index);
            }
        }
    }


    /**
     * Allows players to change the white die to whatever colors.
     * Authored by Larry Wang u7807744.
     */
    public void setWildColor(int index, String desiredColor) {
        if (index >= 0 && index < currentRolls.length) {
            if (currentRolls[index].equals(wildColor)) {
                currentRolls[index] = desiredColor;  // Change white to whatever desired.
            } else {
                System.out.println("Not a White die, no change made.");
            }
        } else {
            System.out.println("Illegal choice!"); //  when out of bound.
        }
    }

    /**
     * Authored by Larry Wang u7807744.
     */
    public String[] getCurrentRolls() {
        return currentRolls;
    }

    /**
     * Allows players to use greenAbility to change dice colors.
     * Authored by Larry Wang u7807744.
     */
    public void setCurrentRolls(String[] newRolls) {
        if (newRolls.length == currentRolls.length) {
            System.arraycopy(newRolls, 0, currentRolls, 0, newRolls.length);  // update currentRolls
        } else {
            throw new IllegalArgumentException("New rolls must be of length 5.");
        }
    }



    public static void main(String[] args) {
        Player player = new Player(1);
        player.rollDice();
        System.out.println("Dice:"+ Arrays.asList(player.getDice()));
    }

    public void setOneDice(int i, String color){
        currentRolls[i] = color;
        System.out.println("Dice at index " + i + " changed to color " + color);
    }
}
