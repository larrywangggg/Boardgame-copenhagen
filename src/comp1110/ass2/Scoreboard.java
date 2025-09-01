package comp1110.ass2;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages the overall scoring of the game, tracking each player's score and determining the winner.
 * Authored by Larry Wang u7807744
 */
public class Scoreboard {
    // Fields
    int score;
    private List<Player> players; // need to create the list in Game engine, and pass it here.
    private boolean finalRound = false; // Once a player get 12, trigger finalRound to true.

    //Constructor- pass player list from gameEngine.
    public Scoreboard(List<Player> players) {
        this.players = players;
    }

    // Constructor - create a new player list by default.
    public Scoreboard() {
        this.players = new ArrayList<>();
    }


    // Methods
    /**
     * @pram player is an int from 0-3.
     * Updates a player's score based on completed rows or columns.
     * Determine later: keep this method in Player.java or Scoreboard.java?
     */
    public void updateScore(Player player, int index, String type) {
        // Start by getting the current score of the player
        this.score = player.getScore();
        if(this.score>15)
            return;
        int points = 0;  // Points to be added to the current score

        boolean[][] windowGrid = player.getPlayerSheet().getWindowGrid();
        if(type.equalsIgnoreCase("row")){
            boolean hasAllWindows = true;  // Assume all windows are present
            for (int j = 0; j < 5; j += 1) {  // Check each column in the row
                if (!windowGrid[index][j]) {
                    hasAllWindows = false;  // If any window is missing, it's false
                    //break;
                }

            }
            // Score according to the presence of windows
            if (hasAllWindows) {
                points += 2;  // Full windows row

            } else {
                points += 1;  // Incomplete windows row

            }
        }
        else if(type.equalsIgnoreCase("column")){
            boolean hasAllWindows = true;  // Assume all windows are present
            for (int i = 0; i < 9; i += 1) {  // Check each row in the column
                if (!windowGrid[i][index]) {
                    hasAllWindows = false;  // If any window is missing, it's false
                    //break;
                }
            }
            // Score according to the presence of windows
            if (hasAllWindows) {
                points += 4;  // Full windows column
            } else {
                points += 2;  // Incomplete windows column
            }
        }


        // Ability Track Completion Scoring
        if (player.getAbilityTrack().trackCompletion()) {
            points += 2;  // 2 points for completing any ability track
        }

        // Add points to the player's score
        score += points;
        if(score<=15){ //cap of score not exceeding 15
            player.setScore(score);  // Update the player's score
            System.out.println("Player " + player.getPlayerIndex() + "'s new score: " + score);  // Debugging
        }

    }


//    This will interact with GUI.
//    public void endGame(int[] finalScores)
//    End the current game. This will bring up the end of game screen
//            (in the lower right corner), which shows the final scores and offers
//    the choice to quit or play again.


    /**
     * Determines the winner based on the final scores.
     * Authored by Larry Wang u7807744
     */
    public String determineWinner() {
        Player winner = null;  // winner initialization
        int highestScore = -1;

        List<Player> potentialWinners = new ArrayList<>();

        String Winner_s = "Initial value";

        //first step, find out potential tie winners.
        for (Player player : players) {
            int score = player.getScore();

            if (score > highestScore) {
                highestScore = score;
                potentialWinners.clear(); // clear the potential winner list.
                potentialWinners.add(player); //recreate the potential winner list.
            } else if (score == highestScore) {
                potentialWinners.add(player);
            }
        }

        //second step, find out final winners from potential winners.
        if (potentialWinners.size() == 1) { // if no ties.
            winner = potentialWinners.get(0);
            Winner_s = "The winner is player"+ (winner.getPlayerIndex()+1);
        }
        else {  //if there's a tie
            int fewestEmptySpaces = 45;// Initially a big and impossible value 5*9
            List<Player> finalWinners = new ArrayList<>();
            for (Player player : potentialWinners) {
                int emptySpaces = player.getPlayerSheet().getEmptySpace();
                if (emptySpaces < fewestEmptySpaces) {
                    fewestEmptySpaces = emptySpaces;
                    finalWinners.clear();
                    finalWinners.add(player);
                }
                else if (emptySpaces == fewestEmptySpaces)
                    finalWinners.add(player);
            }


            if (finalWinners.size() == 1) {
                winner = finalWinners.get(0); //if there's one exact winner.
                Winner_s = "The winner is player"+ (winner.getPlayerIndex()+1);
            }
            else if(finalWinners.size() > 1){
                StringBuilder winner_s = new StringBuilder();

                for (int i = 0; i < finalWinners.size(); i++) {

                    if (i > 0 && i == finalWinners.size() - 1) {
                        // use "and" to link winners
                        winner_s.append(" and ");
                    } else if (i > 0) {
                        // use "," if there's 3 winners.
                        winner_s.append(", ");
                    }

                    winner_s.append("Player").append((finalWinners.get(i).getPlayerIndex()+1));
                }

                winner_s.append(" share the win!");
                // will be something like "Play 1, Player 2 and Player 3 share the win!"

                Winner_s = String.valueOf(winner_s);
            }
        }


        return Winner_s;
    }
}

