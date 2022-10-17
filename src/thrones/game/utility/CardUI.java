package thrones.game.utility;
import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.Location;
import ch.aplu.jgamegrid.TextActor;
import thrones.game.GameOfThrones;

import java.awt.*;
import java.util.Optional;

public class CardUI extends CardGame {
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
    private Hand[] hands;
    private Hand[] piles;
    private final String[] playerTeams = { "[Players 0 & 2]", "[Players 1 & 3]"};

    Font bigFont = new Font("Arial", Font.BOLD, 36);
    Font smallFont = new Font("Arial", Font.PLAIN, 10);

    private Optional<Card> selected;

    public CardUI(GameOfThrones game) {
        super(700, 700, 30);

        setTitle("Game of Thrones (V" + version + ") Constructed for UofM SWEN30006 with JGameGrid (www.aplu.ch)");
        setStatusText("Initializing...");

        this.gameOfThrones = game;
        this.hands = gameOfThrones.getHands();
        initScore(game.nbPlayers);
        initPileTextActors();
        initLayout(game.nbPlayers);
    }

    private void initScore(int players) {
        for (int i = 0; i < players; i++) {
            String text = "P" + i + "-0";
            scoreActors[i] = new TextActor(text, Color.WHITE, bgColor, bigFont);
            addActor(scoreActors[i], scoreLocations[i]);
        }
    }

    private void initPileTextActors() {
        String text = "Attack: 0 - Defence: 0";
        for (int i = 0; i < pileTextActors.length; i++) {
            pileTextActors[i] = new TextActor(text, Color.WHITE, bgColor, smallFont);
            addActor(pileTextActors[i], pileStatusLocations[i]);
        }
    }

    private void initLayout(int players) {
        for (int i = 0; i < players; i++) {
            hands[i].sort(Hand.SortType.SUITPRIORITY, true);

            LoggingSystem.logHand(i,hands[i]);
        }

        for (final Hand currentHand : hands) {
            // Set up human player for interaction
            currentHand.addCardListener(new CardAdapter() {
                public void leftDoubleClicked(Card card) {
                    selected = Optional.of(card);
                    currentHand.setTouchEnabled(false);
                }
                public void rightClicked(Card card) {
                    selected = Optional.empty(); // Don't care which card we right-clicked for player to pass
                    currentHand.setTouchEnabled(false);
                }
            });
        }

        RowLayout[] layouts = new RowLayout[players];
        Hand[] playerHands = gameOfThrones.getHands();
        for (int i = 0; i < players; i++) {
            layouts[i] = new RowLayout(handLocations[i], handWidth);
            layouts[i].setRotationAngle(90 * i);
            playerHands[i].setView(this, layouts[i]);
            hands[i].draw();
        }
    }

    public void updateScore(int player, int score) {
        removeActor(scoreActors[player]);
        String text = "P" + player + "-" + score;
        scoreActors[player] = new TextActor(text, Color.WHITE, bgColor, bigFont);
        addActor(scoreActors[player], scoreLocations[player]);
    }

    public void removeAll() {
        piles = gameOfThrones.getPiles();
        if (piles != null) {
            for (Hand pile : piles) {
                pile.removeAll(true);
            }
        }
    }

    private void drawPile(Hand pile) {
//        pile.setView(this, new RowLayout(pileLocations[i], 8 * pileWidth));
        pile.draw();
        final Hand currentPile = pile;
//        final int pileIndex = i;
        pile.addCardListener(new CardAdapter() {
            public void leftClicked(Card card) {
//                selectedPileIndex = pileIndex;
                currentPile.setTouchEnabled(false);
            }
        });
    }

    public void updatePileRankState(int pileIndex, int attackRank, int defenceRank) {
        // piles is null here, should update from GoT
        piles = gameOfThrones.getPiles();
        TextActor currentPile = (TextActor) pileTextActors[pileIndex];
        removeActor(currentPile);
        String text = playerTeams[pileIndex] + " Attack: " + attackRank + " - Defence: " + defenceRank;
        pileTextActors[pileIndex] = new TextActor(text, Color.WHITE, bgColor, smallFont);
        addActor(pileTextActors[pileIndex], pileStatusLocations[pileIndex]);
    }

    private void removeCardFromPlayer(Card card, int pileIndex) {
        // Empty
        /*
        selected.get().setVerso(false);
        selected.get().transfer(piles[pileIndex], true); // transfer to pile (includes graphic effect)
        */
    }
}
