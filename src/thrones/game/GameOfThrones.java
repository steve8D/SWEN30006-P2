package thrones.game;

// Oh_Heaven.java

import ch.aplu.jcardgame.*;
import thrones.game.character.Character;
import thrones.game.players.*;
import thrones.game.gameSequence.plays.Play;
import thrones.game.gameSequence.plays.PlayFactory;
import thrones.game.players.HumanPlayer;
import thrones.game.players.Player;
import thrones.game.players.RandomPlayer;
import thrones.game.utility.CardUI;
import thrones.game.utility.LoggingSystem;
import thrones.game.utility.PropertiesLoader;
import thrones.game.utility.RandomSingleton;

import java.util.*;

@SuppressWarnings("serial")
public class GameOfThrones extends CardGame {


    enum GoTSuit { CHARACTER, DEFENCE, ATTACK, MAGIC }
    public enum Suit {
        SPADES(GoTSuit.DEFENCE),
        HEARTS(GoTSuit.CHARACTER),
        DIAMONDS(GoTSuit.MAGIC),
        CLUBS(GoTSuit.ATTACK);
        Suit(GoTSuit gotsuit) {
            this.gotsuit = gotsuit;
        }
        private final GoTSuit gotsuit;

        public boolean isDefence(){ return gotsuit == GoTSuit.DEFENCE; }

        public boolean isAttack(){ return gotsuit == GoTSuit.ATTACK; }

        public boolean isCharacter(){ return gotsuit == GoTSuit.CHARACTER; }

        public boolean isMagic(){ return gotsuit == GoTSuit.MAGIC; }
    }

    public enum Rank {
        // Reverse order of rank importance (see rankGreater() below)
        // Order of cards is tied to card images
        ACE(1), KING(10), QUEEN(10), JACK(10), TEN(10), NINE(9), EIGHT(8), SEVEN(7), SIX(6), FIVE(5), FOUR(4), THREE(3), TWO(2);
        Rank(int rankValue) {
            this.rankValue = rankValue;
        }
        private final int rankValue;
        public int getRankValue() {
            return rankValue;
        }
    }

    static public int seed;
    static public Random random; //public for now

    // return random Card from Hand
//    public static Card randomCard(Hand hand) {
//        assert !hand.isEmpty() : " random card from empty hand.";
//        int x = random.nextInt(hand.getNumberOfCards());
//        return hand.get(x);
//    }

//    private void dealingOut(Hand[] handsOld, int nbPlayers, int nbCardsPerPlayer) {
//
//        Hand[] hands = {new Hand(deck),new Hand(deck),new Hand(deck),new Hand(deck) };
//
//        Hand pack = deck.toHand(false);
//        assert pack.getNumberOfCards() == 52 : " Starting pack is not 52 cards.";
//        // Remove 4 Aces
//        List<Card> aceCards = pack.getCardsWithRank(Rank.ACE);
//        for (Card card : aceCards) {
//            card.removeFromHand(false);
//        }
//        assert pack.getNumberOfCards() == 48 : " Pack without aces is not 48 cards.";
//        // Give each player 3 heart cards
//        for (int i = 0; i < nbPlayers; i++) {
//            for (int j = 0; j < 3; j++) {
//                List<Card> heartCards = pack.getCardsWithSuit(Suit.HEARTS);
//                int x = random.nextInt(heartCards.size());
//                Card randomCard = heartCards.get(x);
//                randomCard.removeFromHand(false);
//                hands[i].insert(randomCard, false);
//            }
//        }
//        assert pack.getNumberOfCards() == 36 : " Pack without aces and hearts is not 36 cards.";
//        // Give each player 9 of the remaining cards
//        for (int i = 0; i < nbCardsPerPlayer; i++) {
//            for (int j = 0; j < nbPlayers; j++) {
//                assert !pack.isEmpty() : " Pack has prematurely run out of cards.";
//                Card dealt = randomCard(pack);
//                dealt.removeFromHand(false);
//                hands[j].insert(dealt, false);
//            }
//        }
//        for (int j = 0; j < nbPlayers; j++) {
//            assert hands[j].getNumberOfCards() == 12 : " Hand does not have twelve cards.";
//        }
//
//        for(int k = 0; k < nbPlayers; k++){
//            players[k].setHand(hands[k]); //rm clean hands up round 2
//        }
//    }

    private final String version = "1.0";
    public static final int nbPlayers = 4;
    public final int nbStartCards = 9;
    public final int nbPlays = 6;
    public final int nbRounds = 3;

    private Deck deck = new Deck(Suit.values(), Rank.values(), "cover");

    private Hand[] hands;//rm
    private Player[] players;
    public Hand[] getHands() {
        return hands;
    }


    private Hand[] piles; //remove
    private Character[] characters;


    private CardUI cardUI;

    //necessary to keep random consistent
    private int nextStartingPlayer = random.nextInt(nbPlayers);

    private PlayFactory playFactory;

    private int[] scores = new int[nbPlayers];


//    boolean[] humanPlayers = { true, false, false, false};
    boolean[] humanPlayers = { false, false, false, false};




    private Optional<Card> selected;
    private final int NON_SELECTION_VALUE = -1;
    private int selectedPileIndex = NON_SELECTION_VALUE;
    private final int UNDEFINED_INDEX = -1;
    private final int ATTACK_RANK_INDEX = 0;
    private final int DEFENCE_RANK_INDEX = 1;

    private void setupGame() {

        PlayerFactory playerFactory = new PlayerFactory();
        players = playerFactory.getPlayers(this,deck);

        cardUI.initLayout(players);
    }

    private void executeAPlay(int playIndex) {
        Play play = playFactory.getInstance().createPlay(playIndex,this);

        play.runPlay();
    }

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

        String seedProp = properties.getProperty("seed",PropertiesLoader.getDefaultSeed());
        if (seedProp != null) { // Use property seed
			  seed = Integer.parseInt(seedProp);
        }
        random = new Random(seed);
        
        //set up random singleton
        RandomSingleton.getInstance().addSeed(seed);
        LoggingSystem.logSeed(seed);
        GameOfThrones.random = new Random(seed);
        new GameOfThrones();
    }


    public Character[] getCharacters() {
        return characters;
    }

    public CardUI getCardUI() {
        return cardUI;
    }

    public Player[] getPlayers() {
        return players;
    }
}
