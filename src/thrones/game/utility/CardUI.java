package thrones.game.utility;
import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.Location;
import ch.aplu.jgamegrid.TextActor;
import thrones.game.GameOfThrones;
import thrones.game.players.Player;

import java.awt.*;
import java.util.Optional;

public class CardUI {
    private GameOfThrones gameOfThrones;
    private final String version = "1.0";
    private final int pileWidth = 40;
    private final int handWidth = 400;
    private final Location[] handLocations = {
            new Location(350, 625),
            new Location(75, 350),
            new Location(350, 75),
            new Location(625, 350)
    };

    private final Location[] scoreLocations = {
            new Location(575, 675),
            new Location(25, 575),
            new Location(25, 25),
            new Location(575, 125)
    };
    private final Location[] pileLocations = {
            new Location(350, 280),
            new Location(350, 430)
    };
    private final Location[] pileStatusLocations = {
            new Location(250, 200),
            new Location(250, 520)
    };
    public Actor[] pileTextActors = { null, null };
    private Actor[] scoreActors = {null, null, null, null};
//    private Hand[] hands;
//    private Hand[] piles;
    private final String[] playerTeams = { "[Players 0 & 2]", "[Players 1 & 3]"};

    Font bigFont = new Font("Arial", Font.BOLD, 36);
    Font smallFont = new Font("Arial", Font.PLAIN, 10);

    private Optional<Card> selected;

    public CardUI(GameOfThrones game) {

        this.gameOfThrones = game;


        gameOfThrones.setTitle("Game of Thrones (V" + version + ") Constructed for UofM SWEN30006 with JGameGrid (www.aplu.ch)");
        gameOfThrones.setStatusText("Initializing...");


//        this.hands = gameOfThrones.getHands();
        initScore(game.nbPlayers);
        initPileTextActors();

    }

    private void initScore(int players) {
        for (int i = 0; i < players; i++) {
            String text = "P" + i + "-0";
            scoreActors[i] = new TextActor(text, Color.WHITE, gameOfThrones.bgColor, bigFont);
            gameOfThrones.addActor(scoreActors[i], scoreLocations[i]);
        }
    }

    private void initPileTextActors() {
        String text = "Attack: 0 - Defence: 0";
        for (int i = 0; i < pileTextActors.length; i++) {
            pileTextActors[i] = new TextActor(text, Color.WHITE, gameOfThrones.bgColor, smallFont);
            gameOfThrones.addActor(pileTextActors[i], pileStatusLocations[i]);
        }
    }

    public void initLayout(int players, Hand[] hands) {

        RowLayout[] layouts = new RowLayout[players];
        Hand[] playerHands = hands;
        for (int i = 0; i < players; i++) {
            layouts[i] = new RowLayout(handLocations[i], handWidth);
            layouts[i].setRotationAngle(90 * i);
            playerHands[i].setView(gameOfThrones, layouts[i]);
            playerHands[i].draw();
        }
    }

    public void initLayout(Player[] players){

        RowLayout[] layouts = new RowLayout[players.length];
        //Hand[] playerHands = hands;
        for (int i = 0; i < players.length; i++) {
            layouts[i] = new RowLayout(handLocations[i], handWidth);
            layouts[i].setRotationAngle(90 * i);
            players[i].getHand().setView(gameOfThrones, layouts[i]);
            players[i].getHand().draw();
        }

    }

    public void updateScore(int player, int score) {
        gameOfThrones.removeActor(scoreActors[player]);
        String text = "P" + player + "-" + score;
        scoreActors[player] = new TextActor(text, Color.WHITE, gameOfThrones.bgColor, bigFont);
        gameOfThrones.addActor(scoreActors[player], scoreLocations[player]);
    }

    public void removeAll(Hand[] piles) {
        //piles = gameOfThrones.getPiles();

        if (piles != null) {
            for (Hand pile : piles) {
                pile.removeAll(true);
            }
        }
    }

    public void drawPile(Hand pile, int location) {

        pile.setView(gameOfThrones, new RowLayout(pileLocations[location], 8 * pileWidth));
        pile.draw();
////        pile.setView(this, new RowLayout(pileLocations[i], 8 * pileWidth));
//        pile.draw();
//        final Hand currentPile = pile;
////        final int pileIndex = i;
//        pile.addCardListener(new CardAdapter() {
//            public void leftClicked(Card card) {
////                selectedPileIndex = pileIndex;
//                currentPile.setTouchEnabled(false);
//            }
//        });
    }





    public void updatePileRankState(int pileIndex, int attackRank, int defenceRank) {
        // piles is null here, should update from GoT
        //piles = gameOfThrones.getPiles();
        TextActor currentPile = (TextActor) pileTextActors[pileIndex];
        gameOfThrones.removeActor(currentPile);
        String text = playerTeams[pileIndex] + " Attack: " + attackRank + " - Defence: " + defenceRank;
        pileTextActors[pileIndex] = new TextActor(text, Color.WHITE, gameOfThrones.bgColor, smallFont);
        gameOfThrones.addActor(pileTextActors[pileIndex], pileStatusLocations[pileIndex]);
    }

    private void removeCardFromPlayer(Card card, int pileIndex) {
        // Empty
        /*
        selected.get().setVerso(false);
        selected.get().transfer(piles[pileIndex], true); // transfer to pile (includes graphic effect)
        */
    }


    public void displayResult(int score0, int score1){
        String text;
        if (score0 > score1) {
            text = "Players 0 and 2 won.";
        } else if (score0 == score1) {
            text = "All players drew.";
        } else {
            text = "Players 1 and 3 won.";
        }
        gameOfThrones.setStatusText(text);
    }


    public void moveToPile(Card card, Hand pile){
        card.setVerso(false); //show card face
        card.transfer(pile, true); // transfer to pile (includes graphic effect)


    }
}
