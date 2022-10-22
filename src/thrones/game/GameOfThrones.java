package thrones.game;
// Oh_Heaven.java

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.CardGame;
import ch.aplu.jcardgame.Deck;
import thrones.game.gameSequence.plays.Play;
import thrones.game.gameSequence.plays.PlayFactory;
import thrones.game.players.Player;
import thrones.game.players.PlayerFactory;
import thrones.game.utility.CardUI;
import thrones.game.utility.LoggingSystem;
import thrones.game.utility.PropertiesLoader;
import thrones.game.utility.RandomSingleton;

import java.util.Optional;
import java.util.Properties;
import java.util.Random;

@SuppressWarnings("serial")
public class GameOfThrones extends CardGame {
    public static final int nbPlayers = 4;
    static public int seed;
    static public Random random;
    public final int nbPlays = 6;
    private Deck deck = new Deck(Suit.values(), Rank.values(), "cover");
    private Player[] players;
    private CardUI cardUI;
    private PlayFactory playFactory;

    public GameOfThrones() {
        super(700, 700, 30);
        cardUI = new CardUI(this);
        setupGame();
        for (int i = 0; i < nbPlays; i++) {
            executeAPlay(i);
        }
        LoggingSystem.logResult(players[0].getScore(), players[1].getScore());
        cardUI.displayResult(players[0].getScore(), players[1].getScore());
        refresh();
    }

    public static void main(String[] args) {
        Properties properties = new Properties();
        if (args == null || args.length == 0) {
            //set default
//            properties = PropertiesLoader.defaultProperties();
            properties = PropertiesLoader.loadPropertiesFile("properties/got.properties");
        } else {
            properties = PropertiesLoader.loadPropertiesFile(args[0]);
        }
        String seedProp = properties.getProperty("seed", PropertiesLoader.getDefaultSeed());
        if (seedProp != null) {
            seed = Integer.parseInt(seedProp);
        }
        random = new Random(seed);
        RandomSingleton.getInstance().addSeed(seed);
        LoggingSystem.logSeed(seed);
        GameOfThrones.random = new Random(seed);
        new GameOfThrones();
    }

    private void setupGame() {
        PlayerFactory playerFactory = new PlayerFactory();
        players = playerFactory.getPlayers(this, deck);
        cardUI.initLayout(players);
    }

    private void executeAPlay(int playIndex) {
        Play play = playFactory.getInstance().createPlay(playIndex, this);
        play.runPlay();
    }

    public CardUI getCardUI() {
        return cardUI;
    }

    public Player[] getPlayers() {
        return players;
    }

    enum GoTSuit {CHARACTER, DEFENCE, ATTACK, MAGIC}

    public enum Suit {
        SPADES(GoTSuit.DEFENCE),
        HEARTS(GoTSuit.CHARACTER),
        DIAMONDS(GoTSuit.MAGIC),
        CLUBS(GoTSuit.ATTACK);
        private final GoTSuit gotsuit;

        Suit(GoTSuit gotsuit) {
            this.gotsuit = gotsuit;
        }

        public boolean isDefence() {
            return gotsuit == GoTSuit.DEFENCE;
        }

        public boolean isAttack() {
            return gotsuit == GoTSuit.ATTACK;
        }

        public boolean isCharacter() {
            return gotsuit == GoTSuit.CHARACTER;
        }

        public boolean isMagic() {
            return gotsuit == GoTSuit.MAGIC;
        }
    }

    public enum Rank {
        // Reverse order of rank importance (see rankGreater() below)
        // Order of cards is tied to card images
        ACE(1), KING(10), QUEEN(10), JACK(10), TEN(10), NINE(9), EIGHT(8), SEVEN(7), SIX(6), FIVE(5), FOUR(4), THREE(3), TWO(2);
        private final int rankValue;

        Rank(int rankValue) {
            this.rankValue = rankValue;
        }

        public int getRankValue() {
            return rankValue;
        }
    }
}
