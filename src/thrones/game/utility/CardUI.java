package thrones.game.utility;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import ch.aplu.jcardgame.RowLayout;
import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.Location;
import ch.aplu.jgamegrid.TextActor;
import thrones.game.GameOfThrones;
import thrones.game.character.Character;
import thrones.game.players.Player;

import java.awt.*;

public class CardUI {
    private final String version = "1.0";
    private final int pileWidth = 40;
    private final int handWidth = 400;
    private final Location[] handLocations = {new Location(350, 625), new Location(75, 350), new Location(350, 75), new Location(625, 350)};
    private final Location[] scoreLocations = {new Location(575, 675), new Location(25, 575), new Location(25, 25), new Location(575, 125)};
    private final Location[] pileLocations = {new Location(350, 280), new Location(350, 430)};
    private final Location[] pileStatusLocations = {new Location(250, 200), new Location(250, 520)};
    private final String[] playerTeams = {"[Players 0 & 2]", "[Players 1 & 3]"};
    public Actor[] pileTextActors = {null, null};
    Font bigFont = new Font("Arial", Font.BOLD, 36);
    Font smallFont = new Font("Arial", Font.PLAIN, 10);
    private GameOfThrones gameOfThrones;
    private Actor[] scoreActors = {null, null, null, null};
    private final int ATTACK_RANK_INDEX = 0;
    private final int DEFENCE_RANK_INDEX = 1;

    public CardUI(GameOfThrones game) {
        this.gameOfThrones = game;
        gameOfThrones.setTitle("Game of Thrones (V" + version + ") Constructed for UofM SWEN30006 with JGameGrid (www.aplu.ch)");
        setStatusText("Initializing...");
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

    public void initLayout(Player[] players) {
        RowLayout[] layouts = new RowLayout[players.length];
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
        if (piles != null) {
            for (Hand pile : piles) {
                pile.removeAll(true);
            }
        }
    }

    public void drawPile(Hand pile, int location) {
        pile.setView(gameOfThrones, new RowLayout(pileLocations[location], 8 * pileWidth));
        pile.draw();
    }

    public void updatePileRankState(int pileIndex, int attackRank, int defenceRank) {
        TextActor currentPile = (TextActor) pileTextActors[pileIndex];
        gameOfThrones.removeActor(currentPile);
        String text = playerTeams[pileIndex] + " Attack: " + attackRank + " - Defence: " + defenceRank;
        pileTextActors[pileIndex] = new TextActor(text, Color.WHITE, gameOfThrones.bgColor, smallFont);
        gameOfThrones.addActor(pileTextActors[pileIndex], pileStatusLocations[pileIndex]);
    }

    public void displayResult(Player[] players) {

        int score0 = players[0].getScore();
        int score1 = players[1].getScore();

        String text;
        if (score0 > score1) {
            text = "Players 0 and 2 won.";
        } else if (score0 == score1) {
            text = "All players drew.";
        } else {
            text = "Players 1 and 3 won.";
        }
        setStatusText(text);
    }

    public void moveToPile(Card card, Hand pile) {
        card.setVerso(false);
        card.transfer(pile, true);
    }

    public void setStatusText(String text) {

        gameOfThrones.setStatusText(text);
    }

    public void cardSelectedMessage(int playerIndex, boolean heart) {
        String text;
        if (heart) {
            text = " select a Heart card to play";
        } else {
            text = " select a non-Heart card to play.";
        }
        setStatusText("Player " + playerIndex + text);

    }

    public void cardSelectedMessage(Card card, int playerIndex) {
        setStatusText("Selected: " + LoggingSystem.canonical(card) + ". Player" + playerIndex + " select a pile to play the card.");

    }

    public void updatePileRanks(Character[] characters) {
        for (int j = 0; j < characters.length; j++) { //
            int[] ranks = characters[j].calculatePileRanks();
            updatePileRankState(j, ranks[ATTACK_RANK_INDEX], ranks[DEFENCE_RANK_INDEX]);
        }
    }
}
