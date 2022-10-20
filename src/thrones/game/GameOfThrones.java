package thrones.game;

// Oh_Heaven.java

import ch.aplu.jcardgame.*;
import thrones.game.character.BaseCharacter;
import thrones.game.character.Character;
import thrones.game.gameLogic.sequence.plays.Battle;
import thrones.game.gameLogic.sequence.plays.Play;
import thrones.game.gameLogic.sequence.plays.PlayFactory;
import thrones.game.players.HumanPlayer;
import thrones.game.players.Player;
import thrones.game.players.RandomPlayer;
import thrones.game.utility.CardUI;
import thrones.game.utility.LoggingSystem;
import thrones.game.utility.RandomSingleton;

import java.util.*;

@SuppressWarnings("serial")
public class GameOfThrones extends CardGame {

    private final static String defaultSeed = "130006";
    private final static String defaultWatchingTime = "5000";
    private final static String defaultPlayerType = "human";

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
    public static Card randomCard(Hand hand) {
        assert !hand.isEmpty() : " random card from empty hand.";
        int x = random.nextInt(hand.getNumberOfCards());
        return hand.get(x);
    }

    private void dealingOut(Hand[] handsOld, int nbPlayers, int nbCardsPerPlayer) {

        Hand[] hands = {new Hand(deck),new Hand(deck),new Hand(deck),new Hand(deck) };


        Hand pack = deck.toHand(false);
        assert pack.getNumberOfCards() == 52 : " Starting pack is not 52 cards.";
        // Remove 4 Aces
        List<Card> aceCards = pack.getCardsWithRank(Rank.ACE);
        for (Card card : aceCards) {
            card.removeFromHand(false);
        }
        assert pack.getNumberOfCards() == 48 : " Pack without aces is not 48 cards.";
        // Give each player 3 heart cards
        for (int i = 0; i < nbPlayers; i++) {
            for (int j = 0; j < 3; j++) {
                List<Card> heartCards = pack.getCardsWithSuit(Suit.HEARTS);
                int x = random.nextInt(heartCards.size());
                Card randomCard = heartCards.get(x);
                randomCard.removeFromHand(false);
                hands[i].insert(randomCard, false);
            }
        }
        assert pack.getNumberOfCards() == 36 : " Pack without aces and hearts is not 36 cards.";
        // Give each player 9 of the remaining cards
        for (int i = 0; i < nbCardsPerPlayer; i++) {
            for (int j = 0; j < nbPlayers; j++) {
                assert !pack.isEmpty() : " Pack has prematurely run out of cards.";
                Card dealt = randomCard(pack);
                dealt.removeFromHand(false);
                hands[j].insert(dealt, false);
            }
        }
        for (int j = 0; j < nbPlayers; j++) {
            assert hands[j].getNumberOfCards() == 12 : " Hand does not have twelve cards.";
        }

        for(int k = 0; k < nbPlayers; k++){
            players[k].setHand(hands[k]); //rm clean hands up round 2
        }
    }

    private final String version = "1.0";
    public final int nbPlayers = 4;
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


    boolean[] humanPlayers = { true, false, false, false};
    //boolean[] humanPlayers = { false, false, false, false};




    private Optional<Card> selected;
    private final int NON_SELECTION_VALUE = -1;
    private int selectedPileIndex = NON_SELECTION_VALUE;
    private final int UNDEFINED_INDEX = -1;
    private final int ATTACK_RANK_INDEX = 0;
    private final int DEFENCE_RANK_INDEX = 1;

    private void setupGame() {


        players = new Player[nbPlayers];

        for (int i = 0; i < nbPlayers; i++) {
            if(humanPlayers[i] == true){ // PropertiesLoader.getPlayerType(i)== PlayerType.HUMAN
                players[i] = new HumanPlayer(new Hand(deck), this, i);
            } else {
                players[i] = new RandomPlayer(new Hand(deck), this, i);

            }

        }


        dealingOut(hands, nbPlayers, nbStartCards);

        for (int i = 0; i < nbPlayers; i++) {
            players[i].sortHand();

             LoggingSystem.logHand(i, players[i].getHand());
        }


        Hand[] newhands = new Hand[4];
        int i = 0; //will fix later so initLayout accepts Player
        for(Player p: players){
            p.setUpClickListener();
            newhands[i] = p.getHand();
            i++;
        }




        cardUI.initLayout(nbPlayers, newhands);
    }




    private void updatePileRanks() {
        for (int j = 0; j < characters.length; j++) { //
            int[] ranks = characters[j].calculatePileRanks();
            cardUI.updatePileRankState(j, ranks[ATTACK_RANK_INDEX], ranks[DEFENCE_RANK_INDEX]);
        }
    }

    private int getPlayerIndex(int index) {
        return index % nbPlayers;
    }

    private void executeAPlay(int playIndex) {

        Play play = playFactory.createPlay(playIndex,this);

        play.runPlay();

    }



    public GameOfThrones() {
        super(700, 700, 30);

        cardUI = new CardUI(this);

        setupGame();


        playFactory = new PlayFactory();

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
            properties.setProperty("seed",defaultSeed);
            properties.setProperty("watchingTime",defaultWatchingTime );
            properties.setProperty("players.0",defaultPlayerType);
            properties.setProperty("players.1",defaultPlayerType);
            properties.setProperty("players.2",defaultPlayerType);
            properties.setProperty("players.3",defaultPlayerType);
        } else {
            properties = PropertiesLoader.loadPropertiesFile(args[0]);
        }

        String seedProp = properties.getProperty("seed");  //Seed property
        if (seedProp != null) { // Use property seed
			  seed = Integer.parseInt(seedProp);
        } else { // and no property
			  seed = new Random().nextInt(); // so randomise
        }
        
        //set up random singleton
        RandomSingleton.getInstance().addSeed(130006);

        System.out.println("Seed = " + seed);
        System.out.println(properties);
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
