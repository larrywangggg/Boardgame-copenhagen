package comp1110.ass2;

import comp1110.ass2.gui.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameTemplate extends Application {
    //Declare GUI instance
    private GameGUI gui;
    public List<Player> players = new ArrayList<>();
    private int controlPlayerIndex = 0; //initialise the current player index
    private Player controlPlayer;
    private Scoreboard scoreboard;
    private List<Integer> selectedDice;
    private List<Integer> wishReroll;
    private final List<String> allTiles = List.of("S1X","S1O","R2", "R3", "R4", "R5", "B2", "B3", "B4L", "B4R", "B5","Y2","Y3","Y4L","Y4R","Y5","P2","P3","P4","P5","G2","G3","G4L","G4R","G5");
    int currentSelectingPlayerIndex;
    List<Integer> unselectedDiceIndexes;
    List<String> unselectedDiceColor;
    boolean tilePlacedThisTurn = false;// initialization: player can put the first legal tile
    List<String> availableTiles;
    private boolean finalRoundStarted = false; //initialize final round condition
    private int winnerIndex = -1; //initialize winner index
    ColorTrack colorForCOA;



    @Override
    public void start(Stage stage) throws Exception {
        gui = new GameGUI();
        Scene scene = new Scene(gui, GameGUI.WINDOW_WIDTH, GameGUI.WINDOW_HEIGHT);
        stage.setScene(scene);
        stage.setTitle("Copenhagen Roll & Write");
        stage.show();

        /**
         * after hit "start" button, create n players and initialise their Dice,Tile,Track
         * Authored by Larry Wang u7807744.
         * */

        gui.setOnStartGame((np, isAI) -> {
            gui.setMessage("start new game with " + np + " players");

            //create a player list according to np
            for (int i = 0; i < np; i++) {
                Player player = new Player(i);
                players.add(player);//add player to List<Player> players.
                player.rollDice();
                initialTrackInfo(i);
                setMainActions();
                //System.out.println("the diceColor at first roll is "+ Arrays.toString(player.getDice()));
            }


            // Initialize the scoreboard
            scoreboard = new Scoreboard(players);

            //set control player and display his/her interface.
            controlPlayer = players.get(controlPlayerIndex);
            setDiceAndTile();
        });


        /**
         * To properly allow player place the TileShape, if the placement is valid, the TileShape will be displayed
         * Authored by EdenTian u7807670
         * */
        gui.setOnTilePlaced(placement -> {
            if (tilePlacedThisTurn) {
                gui.setMessage("You can only put 1 tile per turn.");
            }
            else {
                gui.setMessage(" ");
                Player controlPlayer = players.get(controlPlayerIndex);// get the current player index
                //use drawTile function for player to draw tile
                Tile.TileShape tileShape = controlPlayer.getFacadeSheet().drawTile(placement.getTileName());

                int x = placement.getX();
                int y = placement.getY();
                int rotation = placement.getRotation();
                boolean[] windows = placement.getWindows();

                // boolean to see if the tile is valid to be placed
                boolean isValidPlacement = controlPlayer.getPlayerSheet().isValidPlacement(tileShape, x, y, rotation);
                gui.setMessage("");


                if (isValidPlacement) {
                    controlPlayer.getPlayerSheet().addTileShape(tileShape, x, y, rotation, windows); //add TileShape
                    //save the rotated tileShape
                    List<Tile.ShapeofTile> placedTiles = tileShape.rotate(rotation);

                    //go through each tile to confirm their coordinate, color and window, add interface
                    for (int i = 0; i < placedTiles.size(); i++) {
                        Tile.ShapeofTile tile = placedTiles.get(i);

                        int tileX = x + tile.getX();
                        int tileY = y + tile.getY();

                        String tileColor = tile.getTile().getColor().toString();  // check the color

                        boolean hasWindow = windows[i];  // check the window place

                        // use gui to update the interface after placed
                        gui.setFacadeSquare(controlPlayerIndex, tileX, tileY, tileColor, hasWindow);
                    }
                    gui.setMessage("Tile " + tileShape.getName() + " placed by Player " + controlPlayerIndex);
                    // check CoA each time the tile placed
                    updateCoA(controlPlayerIndex);
                    tilePlacedThisTurn = true;

                } else {
                    gui.setMessage("Invalid placement for " + tileShape.getName());
                }
            }
        });


        gui.setOnDiceSelectionChanged((i) -> {
            gui.setMessage("dice selection: " + gui.getSelectedDice());
            selectedDice = gui.getSelectedDice();
        });

        gui.setOnTrackSelectionChanged((i) -> {
            gui.setMessage("track selection: " + gui.getSelectedTracks());
        });



        gui.setOnConfirm((s) -> {
            gui.setMessage("confirm: " + s);
            selectedDice = gui.getSelectedDice();
        });

        /**
         * When Control player click "Pass", allows other player to click on one of the abilityTrack checkbox,
         * according to the colors of unselected Dices. The Avail&Next Stars/Cross will be updated on their own interface.
         * After all players have chosen the ability track, switch the control player to the Next player, i.e. a new round of game starts.
         * Authored by Larry Wang u7807744.
         */
        gui.setOnPass((s) -> {
            gui.setMessage("pass: " + s);

            // Step1: Get the lists of index and color of unselected(by control player) Dices.
            unselectedDiceIndexes = new ArrayList<>();
            unselectedDiceColor = new ArrayList<>();
            selectedDice = gui.getSelectedDice();
            for (int i = 0; i < 5; i++) {
                if (!selectedDice.contains(i)) {
                    unselectedDiceIndexes.add(i);
                    unselectedDiceColor.add(controlPlayer.getDice()[i]);
                }
            }

            // Step2: call a method and allow the next player to select ability track.
            currentSelectingPlayerIndex = (controlPlayerIndex + 1) % players.size();
            promptNextPlayerTrackSelection(unselectedDiceColor);
        });


        // Start the application:
        gui.setControlPlayer(controlPlayerIndex);
        stage.setScene(scene);
        stage.setTitle("Copenhagen Roll & Write");
        stage.show();
    }

    //method for checking CoA
    //Authored by Eden Tian u7807670
    public void updateCoA(int player) {

        Player currentPlayer = players.get(controlPlayerIndex);
        //scoreboard.updateScore(currentPlayer);  // Update the score in the Scoreboard
        gui.setScore(controlPlayerIndex, currentPlayer.getScore());
        CoatsOfArms coa = new CoatsOfArms(currentPlayer);

        PlayerSheet playerSheet = players.get(player).getPlayerSheet();

        // check CoA condition with checkForBonus method
        boolean rowBonus3 = coa.checkForBonus(playerSheet, "row", 3);  // check row 1 as GUI point of view, but our grid's index is different
        //System.out.println("Row 5 bonus: " + rowBonus3);
        boolean rowBonus5 = coa.checkForBonus(playerSheet, "row", 5);  // check row 3 as GUI point of view, but our grid's index is different
        //System.out.println("Row 3 bonus: " + rowBonus5);
        boolean rowBonus7 = coa.checkForBonus(playerSheet, "row", 7);  // check row 5 as GUI point of view, but our grid's index is different
        //System.out.println("Row 1 bonus: " + rowBonus7);

        boolean colBonus1 = coa.checkForBonus(playerSheet, "column", 1);  // check column 1
        //System.out.println("Column 1 bonus: " + colBonus1);
        boolean colBonus3 = coa.checkForBonus(playerSheet, "column", 3);  // check column 3
        //System.out.println("Column 3 bonus: " + colBonus3);

        // highlight the logo accordingly
        gui.setRowCoA(player, 1, rowBonus7);
        gui.setRowCoA(player, 3, rowBonus5);
        gui.setRowCoA(player, 5, rowBonus3);

        gui.setColumnCoA(player, 1, colBonus1);
        gui.setColumnCoA(player, 3, colBonus3);

        boolean bonusAchieved = rowBonus3 || rowBonus5 || rowBonus7 || colBonus1 || colBonus3;
        if (bonusAchieved) {
            gui.setMessage("Player " + controlPlayerIndex + " has earned a Coat of Arms! Choose either to advance a color track by 2 steps or use 'AddWindow(B)' in action menu.");
            gui.setAvailableActions(List.of("AdvanceColorTrack", "AddWindow(CoA)"));  // make the action menu into two options for player to choose from
        }


        // Check row completions and update score accordingly
        //scoreboard.score=0;
        //cap of score not exceeding 15
        if(currentPlayer.getScore()<15) {

            currentPlayer.setScore(0);
            for (int i = 0; i < 9; i++) {
                boolean rowCompleted = coa.checkForBonus(playerSheet, "row", i);
                if (rowCompleted) {
                    scoreboard.updateScore(currentPlayer, i, "row");// Update score using scoreboard
                    gui.setScore(controlPlayerIndex, currentPlayer.getScore());  // Update GUI with new score
                    gui.setMessage("Player " + controlPlayerIndex + " completed row " + i);

                }
            }


            // Check column completions and update score accordingly
            for (int i = 0; i < 5; i++) {
                boolean colCompleted = coa.checkForBonus(playerSheet, "column", i);
                if (colCompleted) {
                    scoreboard.updateScore(currentPlayer, i, "column");  // Update score using scoreboard
                    gui.setScore(controlPlayerIndex, currentPlayer.getScore());  // Update GUI with new scoreﬁ
                    gui.setMessage("Player " + controlPlayerIndex + " completed column " + i);
                }
            }
        }


//		 Check if ability track is completed and update score accordingly
        if (currentPlayer.getAbilityTrack().trackCompletion()) {
            gui.setMessage("Player " + controlPlayerIndex + " completed an ability track!");
            scoreboard.updateScore(currentPlayer,-1,"abilityTrack");  // Reward points for ability track completion
            gui.setScore(controlPlayerIndex, currentPlayer.getScore()); // Update GUI with new score
        }
    }


    /**
     *set and display(by calling a method updatePlayerView(int playerIndex)) the next player as the control player.
     * Authored by Larry Wang u7807744.
     */
    private void switchToNextPlayer() {
        int currentPlayerIndex = gui.getSelectedPlayer(); // display the current player
        int nextPlayerIndex = (currentPlayerIndex + 1) % players.size(); // get next player's index

        if (finalRoundStarted && nextPlayerIndex == winnerIndex) {
            endGame();
            return;
        } // to check if it's the final round, and trigger endGame if the final round finished

        //set and display(by calling a method) the next player as the control player.
        gui.setSelectedPlayer(nextPlayerIndex);
        gui.setControlPlayer(nextPlayerIndex);
        updatePlayerView(nextPlayerIndex);

        this.controlPlayerIndex = nextPlayerIndex;//make sure the current player index is updated
        this.controlPlayer = players.get(controlPlayerIndex);//make sure the control player is updated
        tilePlacedThisTurn = false;
        gui.clearTrackSelection();
        setDiceAndTile();// update the dice and tiles for the new control player
        setMainActions();// update the action list for the new control player
    }

    /**
     * set the available Dice&Tiles ui for the input player.
     * Authored by Larry Wang u7807744.
     */
    private void updatePlayerView(int playerIndex) {
        List<String> diceColors = Arrays.asList(players.get(playerIndex).getDice());
        gui.setAvailableDice(diceColors); // show dice
        gui.setAvailableTiles(getAvailableTiles(players.get(playerIndex),false,""));
        gui.setMessage("Player " + playerIndex + " is now the control player.");
    }

    /**
     * Based on the dice, set and display the available Tiles ui for the input player.
     * Authored by Larry Wang u7807744.
     */
    public List<String> getAvailableTiles(Player player, Boolean uBonus, String whichBonusColor){
        List<String> availableTiles = new ArrayList<>();//initialise the availableTile list
        for (int j = 0 ; j < allTiles.size(); j++){
            String tsName = allTiles.get(j);
            Tile.TileShape ts = new Tile.TileShape(tsName);
            if (!uBonus){
                if (player.chooseTile(ts,false,false,""))
                    availableTiles.add(tsName); //add tiles to available list based on chooseTile rules
            }
            else  {
                if(player.chooseTile(ts,false,true,whichBonusColor))
                    availableTiles.add(tsName);}
        }
        return availableTiles;
    }


    /**
     *Initialise the TrackInfo values and ui for the input player.
     * Authored by Larry Wang u7807744.
     */
    public void initialTrackInfo(int playerIndex) {
        AbilityTrack abilityTrack = players.get(playerIndex).getAbilityTrack();

        // for loop in each color track and display on interface
        for (ColorTrack track : ColorTrack.values()) {
            // get mark & avail/next star、cross info of current track, avoid null.
            int currentMark = abilityTrack.colorProgress.getOrDefault(track,-1);
            int starAvailable = abilityTrack.starAvail.getOrDefault(track,-1);
            int crossAvailable = abilityTrack.crossAvail.getOrDefault(track,-1);
            int nextStarUnlock = abilityTrack.starNext.getOrDefault(track,-1);
            int nextCrossUnlock = abilityTrack.crossNext.getOrDefault(track,-1);

            // set each track info and display
            gui.setTrackInfo(playerIndex, track.name(), currentMark,
                    crossAvailable, starAvailable, nextCrossUnlock, nextStarUnlock);
        }
    }

    /**
     * Update the TrackInfo values and ui for the input player.
     * Authored by Larry Wang u7807744.
     */
    public void updatePlayerTrackInfo(int playerIndex,ColorTrack color) {
        AbilityTrack abilityTrack = players.get(playerIndex).getAbilityTrack();
        // for loop in each color track and display on interface

        // get mark & avail/next star、cross info of current track
        int mark = abilityTrack.colorProgress.getOrDefault(color,-1);
        int markAfterAdvance = mark +1 ;//advance by 1, for gui update usage.

        abilityTrack.advanceTrack(color,1);// update the value of Mark, avail stars and avail cross of the input color track.

        int starAvailable = abilityTrack.starAvail.getOrDefault(color,-1);
        int crossAvailable = abilityTrack.crossAvail.getOrDefault(color,-1);
        int nextStarUnlock = abilityTrack.starNext.getOrDefault(color,-1);
        int nextCrossUnlock = abilityTrack.crossNext.getOrDefault(color,-1);
        abilityTrack.updateStarNext(color);// update the starNext of the input color track.
        abilityTrack.updateCrossNext(color);// update the crossNext of the input color track.
        //System.out.println("available cross of player "+playerIndex+ " is"+crossAvailable);


        // set each track info and display
        gui.setTrackInfo(currentSelectingPlayerIndex, color.name(), markAfterAdvance,
                crossAvailable, starAvailable, nextCrossUnlock, nextStarUnlock);
    }

    /**
     * Make sure player select dices of same color, before allowing them to use green ability(change dices to wish color).
     * Authored by Larry Wang u7807744.
     */
    public boolean validForGreenAbility(){
        selectedDice = gui.getSelectedDice();
        String selectedColor = controlPlayer.getDice()[selectedDice.get(0)];//track the first selected dice color.
        for (int i=0; i < selectedDice.size();i++){
            if (!selectedColor.equals(controlPlayer.getDice()[selectedDice.get(i)]))
                return false;}
        return true;
    }

    /**
     * Check method to see if the player's score meet the final round condition
     * Authored by Eden Tian u7807670.
     * */
    public void checkEndGameCondition() {
        if (!finalRoundStarted && controlPlayer.getScore() >= 12) {
            finalRoundStarted = true;
            winnerIndex = controlPlayerIndex;
            gui.setMessage("Player " + controlPlayerIndex + " has reached 12 points! Final round starts.");
        }
    }

    /**
     * Endgame triggering, combined with gui endGame
     * Authored by Eden Tian u7807670.
     * */
    public void endGame() {
        int[] finalScores = new int[players.size()];
        for (int i = 0; i < players.size(); i++) {
            finalScores[i] = players.get(i).getScore();
        }

        gui.endGame(finalScores);
    }




    /**
     * Use a prompt to remind other players to select a color track to advance, after the control player hit "pass".
     * Players need to hit "confirm" to confirm their choice.
     * And after that the prompt changes to remind next player to choose.
     * Authored by Larry Wang u7807744.
     */
    private void promptNextPlayerTrackSelection(List<String> colors) {
        Player selectingPlayer = players.get(currentSelectingPlayerIndex);
        gui.setMessage("Player " + currentSelectingPlayerIndex + " is choosing the track based on available colors: " + colors);
        gui.clearTrackSelection();

        gui.setOnTrackSelectionChanged((trackIndex) -> {
            //switch checkbox index to String of colors；
            String selectedColor = "";
            switch (trackIndex){
                case 0 -> selectedColor = "Red";
                case 1 -> selectedColor = "Blue";
                case 2 -> selectedColor = "Purple";
                case 3 -> selectedColor = "Green";
                case 4 -> selectedColor = "Yellow";
            }
            if (!colors.contains(selectedColor))//check if player is selecting from unselected dices.
                gui.setMessage("Please choose from the unselected dice colors!");
            else
                gui.setMessage("Player " + currentSelectingPlayerIndex + " has selected " + selectedColor + " track. Please confirm.");
            // Store the selected track index temporarily in Player.java.
            selectingPlayer.setTempTrackSelection(trackIndex);
        });

        gui.setOnConfirm((s) -> {
            // check if any track is selected
            List<Integer> selectedTracks = gui.getSelectedTracks();
            if (selectedTracks.isEmpty()) {
                gui.setMessage("Please select a track before confirming.");
            }
            int trackIndex = selectingPlayer.getTempTrackSelection();
            // call a method to update the corresponding track info.
            switch (trackIndex) {
                case 0 -> updatePlayerTrackInfo(currentSelectingPlayerIndex,ColorTrack.Red);
                case 1 -> updatePlayerTrackInfo(currentSelectingPlayerIndex,ColorTrack.Blue);
                case 2 -> updatePlayerTrackInfo(currentSelectingPlayerIndex,ColorTrack.Purple);
                case 3 -> updatePlayerTrackInfo(currentSelectingPlayerIndex,ColorTrack.Green);
                case 4 -> updatePlayerTrackInfo(currentSelectingPlayerIndex,ColorTrack.Yellow);
            }
            //System.out.println("Player "+currentSelectingPlayerIndex+" is selecting track");

            //set Next player as the current selecting player, and prompt to remind players.
            currentSelectingPlayerIndex = (currentSelectingPlayerIndex + 1) % players.size();

            //System.out.println("The current selecting player after update is Player "+currentSelectingPlayerIndex);
            // if the player index == control player index, means all players have chosen track, then start next round of the game.
            if (currentSelectingPlayerIndex != controlPlayerIndex) {
                promptNextPlayerTrackSelection(colors);
            } else {
                checkEndGameCondition();
                switchToNextPlayer();
            }
        });
    }


    /**
     * Get a player's one specific colorTrack info.
     * The number order in int[] trackInfo is matching the no.3-no.7 number in the gui.setTrackInfo() input value.
     * It will be used only in the control players turn, to tell the abilities(i.e. reRoll) are available or not.
     * Authored by Larry Wang u7807744.
     */
    public int[] getTrackInfo(ColorTrack color){
        int[] trackInfo = new int[5];
        trackInfo[0] = controlPlayer.getAbilityTrack().colorProgress.get(color);
        trackInfo[1] = controlPlayer.getAbilityTrack().crossAvail.get(color);
        trackInfo[2] = controlPlayer.getAbilityTrack().starAvail.get(color);
        trackInfo[3] = controlPlayer.getAbilityTrack().crossNext.get(color);
        trackInfo[4] = controlPlayer.getAbilityTrack().starNext.get(color);
        return trackInfo;
    }

    /**
     * Encapsulate the logic of setting dice and tile for control player.
     * Used at Initialization of the game start, and every new round of the game.
     * Authored by Larry Wang u7807744.
     */
    public void setDiceAndTile(){
        controlPlayer.rollDice();
        List<String> diceColors = Arrays.asList(controlPlayer.getDice());
        gui.setAvailableDice(diceColors);//display dice
        availableTiles =getAvailableTiles(controlPlayer,false,"");//get available tiles based on dice.
        gui.setAvailableTiles(availableTiles);//display available tiles
        gui.setAvailableActions(List.of("ReRoll(R)", "AddWindow(B)","AddX(P)","ChangeDiceToRed(G)","ChangeDiceToBlue(G)","ChangeDiceToPurple(G)","ChangeDiceToGreen(G)","ChangeDiceToYellow(G)","DrawAnyTile(Y)","UseBonusToAddDice"));
        gui.clearDiceSelection();
    }


    /**
     * Encapsulate the logic for handling sub-actions.
     * This method is specifically used in scenarios such as switching to the `UseBonusToAddDice` sub-action.
     * It ensures that when different actions are selected (e.g., when the control player chooses to use the bonus to add a die),
     * the corresponding sub-actions are set and handled appropriately, without redundant code.
     * Authored by Larry Wang u7807744.
     */
    private void handleBonusAction(String action) {
        switch (action){
            case "Red" -> {
                if (controlPlayer.getAbilityTrack().crossAvail.get(ColorTrack.Red) >0){
                    String[] nowDice = controlPlayer.getDice();
                    String[] updatedDice = new String[6];
                    System.arraycopy(nowDice, 0, updatedDice, 0, 5);
                    updatedDice[5] = "Red";
                    gui.setAvailableTiles(getAvailableTiles(controlPlayer,true,"Red"));
                    gui.setMessage("Red+1 already");
                    int redAvailCross = controlPlayer.getAbilityTrack().crossAvail.get(ColorTrack.Red);
                    controlPlayer.getAbilityTrack().crossAvail.put(ColorTrack.Red,redAvailCross-1);//update the red avail cross number
                    //update the control player's trackInfo ui.
                    gui.setTrackInfo(controlPlayerIndex,"Red",getTrackInfo(ColorTrack.Red)[0],getTrackInfo(ColorTrack.Red)[1]
                            ,getTrackInfo(ColorTrack.Red)[2],getTrackInfo(ColorTrack.Red)[3],getTrackInfo(ColorTrack.Red)[4]);
                }
                else gui.setMessage("You don't have enough Red cross, please choose another color.");
            }
            case "Blue"-> {
                if (controlPlayer.getAbilityTrack().crossAvail.get(ColorTrack.Blue) >0) {
                    String[] nowDice = controlPlayer.getDice();
                    String[] updatedDice = new String[6];
                    System.arraycopy(nowDice, 0, updatedDice, 0, 5);
                    updatedDice[5] = "Blue";
                    gui.setAvailableTiles(getAvailableTiles(controlPlayer, true, "Blue"));
                    gui.setMessage("Blue+1 already");
                    int blueAvailCross = controlPlayer.getAbilityTrack().crossAvail.get(ColorTrack.Blue);
                    controlPlayer.getAbilityTrack().crossAvail.put(ColorTrack.Blue,blueAvailCross-1);//update the red avail cross number
                    //update the control player's trackInfo ui.
                    gui.setTrackInfo(controlPlayerIndex,"Blue",getTrackInfo(ColorTrack.Blue)[0],getTrackInfo(ColorTrack.Blue)[1]
                            ,getTrackInfo(ColorTrack.Blue)[2],getTrackInfo(ColorTrack.Blue)[3],getTrackInfo(ColorTrack.Blue)[4]);
                }
                else gui.setMessage("You don't have enough Blue cross, please choose another color.");
            }
            case "Purple"-> {
                if (controlPlayer.getAbilityTrack().crossAvail.get(ColorTrack.Purple) >0) {
                    String[] nowDice = controlPlayer.getDice();
                    String[] updatedDice = new String[6];
                    System.arraycopy(nowDice, 0, updatedDice, 0, 5);
                    updatedDice[5] = "Purple";
                    gui.setAvailableTiles(getAvailableTiles(controlPlayer, true, "Purple"));
                    gui.setMessage("Purple+1 already");
                    int purpleAvailCross = controlPlayer.getAbilityTrack().crossAvail.get(ColorTrack.Purple);
                    controlPlayer.getAbilityTrack().crossAvail.put(ColorTrack.Purple,purpleAvailCross-1);//update the red avail cross number
                    //update the control player's trackInfo ui.
                    gui.setTrackInfo(controlPlayerIndex,"Purple",getTrackInfo(ColorTrack.Purple)[0],getTrackInfo(ColorTrack.Purple)[1]
                            ,getTrackInfo(ColorTrack.Purple)[2],getTrackInfo(ColorTrack.Purple)[3],getTrackInfo(ColorTrack.Purple)[4]);
                }
                else gui.setMessage("You don't have enough purple cross, please choose another color.");
            }
            case "Green"-> {
                if (controlPlayer.getAbilityTrack().crossAvail.get(ColorTrack.Green) >0) {
                    String[] nowDice = controlPlayer.getDice();
                    String[] updatedDice = new String[6];
                    System.arraycopy(nowDice, 0, updatedDice, 0, 5);
                    updatedDice[5] = "Green";
                    gui.setAvailableTiles(getAvailableTiles(controlPlayer, true, "Green"));
                    gui.setMessage("Green+1 already");
                    int greenAvailCross = controlPlayer.getAbilityTrack().crossAvail.get(ColorTrack.Green);
                    controlPlayer.getAbilityTrack().crossAvail.put(ColorTrack.Green,greenAvailCross-1);//update the red avail cross number
                    //update the control player's trackInfo ui.
                    gui.setTrackInfo(controlPlayerIndex,"Green",getTrackInfo(ColorTrack.Green)[0],getTrackInfo(ColorTrack.Green)[1]
                            ,getTrackInfo(ColorTrack.Green)[2],getTrackInfo(ColorTrack.Green)[3],getTrackInfo(ColorTrack.Green)[4]);
                }
                else gui.setMessage("You don't have enough green cross, please choose another color.");
            }
            case "Yellow"-> {
                if (controlPlayer.getAbilityTrack().crossAvail.get(ColorTrack.Yellow) >0) {
                    String[] nowDice = controlPlayer.getDice();
                    String[] updatedDice = new String[6];
                    System.arraycopy(nowDice, 0, updatedDice, 0, 5);
                    updatedDice[5] = "Yellow";
                    gui.setAvailableTiles(getAvailableTiles(controlPlayer, true, "Yellow"));
                    gui.setMessage("Yellow+1 already");
                    int yellowAvailCross = controlPlayer.getAbilityTrack().crossAvail.get(ColorTrack.Yellow);
                    controlPlayer.getAbilityTrack().crossAvail.put(ColorTrack.Yellow,yellowAvailCross-1);//update the red avail cross number
                    //update the control player's trackInfo ui.
                    gui.setTrackInfo(controlPlayerIndex,"Yellow",getTrackInfo(ColorTrack.Yellow)[0],getTrackInfo(ColorTrack.Yellow)[1]
                            ,getTrackInfo(ColorTrack.Yellow)[2],getTrackInfo(ColorTrack.Yellow)[3],getTrackInfo(ColorTrack.Yellow)[4]);
                }
                else gui.setMessage("You don't have enough yellow cross, please choose another color.");
            }
            default -> gui.setMessage("");
        }
    }


    private void setMainActions() {
        gui.setAvailableActions(List.of("ReRoll(R)", "AddWindow(B)","AddX(P)","ChangeDiceToRed(G)","ChangeDiceToBlue(G)","ChangeDiceToPurple(G)","ChangeDiceToGreen(G)","ChangeDiceToYellow(G)","DrawAnyTile(Y)","UseBonusToAddDice"));
        gui.setOnGameAction(this::handleMainAction);
    }

    /**
     * Encapsulate the logic for handling main-actions.
     * This method is specifically used at the start of game and every turn(switch to new control player)
     * to set and handle action lists without redundant code.
     * Authored by Larry Wang u7807744.
     */
    private void handleMainAction(String s) {

        gui.setMessage("action: " + s);
        controlPlayer = players.get(controlPlayerIndex);// use the currentPlayerIndex to get controlledPlayer
        wishReroll = new ArrayList<>();
        selectedDice = gui.getSelectedDice();//seems redundant.

        //add the unchosen dice index into the wishReRoll list.
        for (int i = 0; i < 5; i++){
            if (! selectedDice.contains(i))
                wishReroll.add(i);
        }

        // transfer ArrayList<Integer> to int[]
        int[] wish_ReRoll = wishReroll.stream().mapToInt(Integer::intValue).toArray();


        // When click "ReRoll" button in "Action",keep the chosen dice and ReRoll the others.
        // Allow reRoll only when red star avail number > 0 , and redAvailStar -1 every time after reRoll.
        if (s.equals("ReRoll(R)")){
            int redAvailStar = controlPlayer.getAbilityTrack().starAvail.get(ColorTrack.Red);//get the red avail star number
            if (redAvailStar > 0){
                controlPlayer.reRollDice(wish_ReRoll);
                controlPlayer.getAbilityTrack().starAvail.put(ColorTrack.Red,redAvailStar-1);//update the red avail star number
                //update the control player's trackInfo ui.
                gui.setTrackInfo(controlPlayerIndex,"Red",getTrackInfo(ColorTrack.Red)[0],getTrackInfo(ColorTrack.Red)[1]
                        ,getTrackInfo(ColorTrack.Red)[2],getTrackInfo(ColorTrack.Red)[3],getTrackInfo(ColorTrack.Red)[4]);

            }
            else gui.setMessage("You don't have enough red star to reRoll!");
        }

        /**
         * Displaying Tile S1O only, allow controlPlayer to place this window for ability and CoA purpose
         * Authored by Eden Tian u7807670.
         * */
        if (s.equals("AddWindow(B)")) {
            int blueAvailStar = controlPlayer.getAbilityTrack().starAvail.get(ColorTrack.Blue);//get the blue avail star number
            if (blueAvailStar > 0) {
                List<String> availableTile = List.of("S1O");
                gui.setAvailableTiles(availableTile);
                controlPlayer.getAbilityTrack().useBlueAbility(controlPlayer);
                //System.out.println("Player is using Blue Ability (S1O available)");

                controlPlayer.getAbilityTrack().starAvail.put(ColorTrack.Blue,blueAvailStar-1);//update the blue avail star number
                //update the control player's trackInfo ui.
                gui.setTrackInfo(controlPlayerIndex,"Blue",getTrackInfo(ColorTrack.Blue)[0],getTrackInfo(ColorTrack.Blue)[1]
                        ,getTrackInfo(ColorTrack.Blue)[2],getTrackInfo(ColorTrack.Blue)[3],getTrackInfo(ColorTrack.Blue)[4]);

            }
            else gui.setMessage("You don't have enough blue star to add window!");
        }

        /**
         * Displaying Tile S1X only, allow controlPlayer to place this X for ability purpose
         * Authored by Eden Tian u7807670.
         * */
        if (s.equals("AddX(P)")){
            int purpleAvailStar = controlPlayer.getAbilityTrack().starAvail.get(ColorTrack.Purple);//get the purple avail star number
            if (purpleAvailStar > 0) {
                List<String> availableTile = List.of("S1X");
                gui.setAvailableTiles(availableTile);
                controlPlayer.getAbilityTrack().usePurpleAbility(controlPlayer);
                //System.out.println("Player is using Blue Ability (S1X available)");

                controlPlayer.getAbilityTrack().starAvail.put(ColorTrack.Purple,purpleAvailStar-1);//update the purple avail star number
                //update the control player's trackInfo ui.
                gui.setTrackInfo(controlPlayerIndex,"Purple",getTrackInfo(ColorTrack.Purple)[0],getTrackInfo(ColorTrack.Purple)[1]
                        ,getTrackInfo(ColorTrack.Purple)[2],getTrackInfo(ColorTrack.Purple)[3],getTrackInfo(ColorTrack.Purple)[4]);

            }
            else gui.setMessage("You don't have enough purple star to add window!");
        }

        /**
         * Logic of green ability: set dies(of same colors) to a wish color.
         * Authored by Larry Wang u7807744.
         * */
        if (s.startsWith("ChangeDice")){
            int greenAvailStar = controlPlayer.getAbilityTrack().starAvail.get(ColorTrack.Green);//get the green avail star number
            if (greenAvailStar>0){
                String changeToColor = s.substring(10);// track the color Change part for switch usage
                selectedDice = gui.getSelectedDice();
                if (!validForGreenAbility())
                    gui.setMessage("If you want to use Green ability, please choose dice of same color");
                else {
                    //switch the selected dice to corresponding wished color
                    switch (changeToColor){
                        case "ToRed(G)" -> {
                            for (Integer i : selectedDice)
                                controlPlayer.getDiceObject().setOneDice(i,"Red");
                        }
                        case "ToBlue(G)" -> {
                            for (Integer i : selectedDice)
                                controlPlayer.getDiceObject().setOneDice(i,"Blue");
                        }
                        case "ToPurple(G)" -> {
                            for (Integer i : selectedDice)
                                controlPlayer.getDiceObject().setOneDice(i,"Purple");
                        }
                        case "ToGreen(G)" -> {
                            for (Integer i : selectedDice)
                                controlPlayer.getDiceObject().setOneDice(i,"Green");
                        }
                        case "ToYellow(G)" -> {
                            for (Integer i : selectedDice)
                                controlPlayer.getDiceObject().setOneDice(i,"Yellow");
                        }

                    }
                    gui.setAvailableDice(Arrays.asList(controlPlayer.getDice()));//display dice
                    controlPlayer.getAbilityTrack().starAvail.put(ColorTrack.Green,greenAvailStar-1);//update the green avail star number
                    //update the control player's trackInfo ui.
                    gui.setTrackInfo(controlPlayerIndex,"Green",getTrackInfo(ColorTrack.Green)[0],getTrackInfo(ColorTrack.Green)[1]
                            ,getTrackInfo(ColorTrack.Green)[2],getTrackInfo(ColorTrack.Green)[3],getTrackInfo(ColorTrack.Green)[4]);

                }
            }
            else gui.setMessage("You don't have enough green star to change dices!");
        }

        /**
         * Draw any tiles even it's limited tiles that have been used up.
         * Authored by Eden Tian u7807670.
         * */
        if (s.equals("DrawAnyTile(Y)")){
            int yellowAvailStar = controlPlayer.getAbilityTrack().starAvail.get(ColorTrack.Yellow);//get the green avail star number
            if (yellowAvailStar>0){
                List<String> availableTile = new ArrayList<>(allTiles);
                gui.setAvailableTiles(availableTile);
                controlPlayer.getAbilityTrack().starAvail.put(ColorTrack.Yellow,yellowAvailStar-1);//update the yellow avail star number
                //update the control player's trackInfo ui.
                gui.setTrackInfo(controlPlayerIndex,"Yellow",getTrackInfo(ColorTrack.Yellow)[0],getTrackInfo(ColorTrack.Yellow)[1]
                        ,getTrackInfo(ColorTrack.Yellow)[2],getTrackInfo(ColorTrack.Yellow)[3],getTrackInfo(ColorTrack.Yellow)[4]);

            }
            else gui.setMessage("You don't have enough yellow star to draw any tile!");
        }

        /**When choose "UseBonusToAddDice", call the encapsulated method so set sub-list
         * When choose a color, the dice number +1 (behind the scene, won't be display), so that the bigger tiles can be chosen.
         * Authored by Larry Wang u7807744.
         */
        if (s.equals("UseBonusToAddDice")){
            gui.setAvailableActions(List.of("Red", "Blue","Purple","Green","Yellow"));
            gui.setOnGameAction(this::handleBonusAction);
        }


        /**When coa complete, calls the sublist of "AdvanceColorTrack" and "AddWindow(B)" as bonus mentioned in game rules.
         * "AdvanceColorTrack" allow player to advance 2 steps in a color track (+2mark).
         * Authored by Larry Wang u7807744.
         */
        if (s.equals("AdvanceColorTrack")) {
            gui.setMessage("Player " + controlPlayerIndex + " advances a color track by 2 steps.");
            //Need to select a color track, then hit "confirm" to advance 2 steps.
            gui.setOnConfirm((s1) -> {
                List<Integer> advanceColor = gui.getSelectedTracks();
                //System.out.println("Selected Tracks: " + Arrays.asList(advanceColor));
                if (advanceColor.size() != 1)
                    gui.setMessage("Please select 1 color track to advance 2 steps");
                else{
                    switch (advanceColor.get(0)){
                        case 0 -> colorForCOA = ColorTrack.Red;
                        case 1 -> colorForCOA = ColorTrack.Blue;
                        case 2 -> colorForCOA = ColorTrack.Purple;
                        case 3 -> colorForCOA = ColorTrack.Green;
                        case 4 -> colorForCOA = ColorTrack.Yellow;
                    }
                    System.out.println("The control player is player: "+ controlPlayerIndex);
                    //advance 2 steps in chosen ability track, and update the ui.
                    controlPlayer.getAbilityTrack().advanceTrack(colorForCOA,2);
                    gui.setTrackInfo(controlPlayerIndex,colorForCOA.name(),getTrackInfo(colorForCOA)[0],getTrackInfo(colorForCOA)[1]
                            ,getTrackInfo(colorForCOA)[2],getTrackInfo(colorForCOA)[3],getTrackInfo(colorForCOA)[4]);
                }
            });
        }

        if (s.equals("AddWindow(CoA)")) {
            tilePlacedThisTurn = false;
            List<String> availableTile = List.of("S1O");
            gui.setAvailableTiles(availableTile);
            controlPlayer.getAbilityTrack().useBlueAbility(controlPlayer);
            tilePlacedThisTurn = true;
        }
        //Get and display the dices after reRoll
        List<String> diceColors = Arrays.asList(controlPlayer.getDice());
        gui.setAvailableDice(diceColors);
        gui.setAvailableTiles(getAvailableTiles(controlPlayer,false,""));
    }

}

