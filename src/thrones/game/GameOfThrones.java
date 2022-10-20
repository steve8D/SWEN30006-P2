package thrones.game;

// Oh_Heaven.java

import ch.aplu.jcardgame.*;
import thrones.game.character.BaseCharacter;
import thrones.game.character.Character;
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

    /*
    Canonical String representations of Suit, Rank, Card, and Hand
    */
//    String canonical(Suit s) { return s.toString().substring(0, 1); }
//
//    String canonical(Rank r) {
//        switch (r) {
//            case ACE: case KING: case QUEEN: case JACK: case TEN:
//                return r.toString().substring(0, 1);
//            default:
//                return String.valueOf(r.getRankValue());
//        }
//    }
//
//    String canonical(Card c) { return canonical((Rank) c.getRank()) + canonical((Suit) c.getSuit()); }
//
//    String canonical(Hand h) {
//        return "[" + h.getCardList().stream().map(this::canonical).collect(Collectors.joining(",")) + "]";
//    }
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

    private final int watchingTime = 5000;
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


    //boolean[] humanPlayers = { true, false, false, false};
    boolean[] humanPlayers = { false, false, false, false};


    private void updateScores() {
        for (int i = 0; i < nbPlayers; i++) {
            cardUI.updateScore(i, scores[i]);
        }
        LoggingSystem.logScores(scores);
    }

    private Optional<Card> selected;
    private final int NON_SELECTION_VALUE = -1;
    private int selectedPileIndex = NON_SELECTION_VALUE;
    private final int UNDEFINED_INDEX = -1;
    private final int ATTACK_RANK_INDEX = 0;
    private final int DEFENCE_RANK_INDEX = 1;

    private void setupGame() {


        //hands = new Hand[nbPlayers]; //rm
        players = new Player[nbPlayers];

        for (int i = 0; i < nbPlayers; i++) {
            //hands[i] = new Hand(deck);
            if(humanPlayers[i] == true){
                players[i] = new HumanPlayer(new Hand(deck), this, i);
            } else {
                players[i] = new RandomPlayer(new Hand(deck), this, i);

            }

            //players[i] = new Player(new Hand(deck), this, i); //will remove this later
        }


        dealingOut(hands, nbPlayers, nbStartCards);

        for (int i = 0; i < nbPlayers; i++) {
            //hands[i].sort(Hand.SortType.SUITPRIORITY, true);
            players[i].sortHand();

            //LoggingSystem.logHand(i,hands[i]);
             LoggingSystem.logHand(i, players[i].getHand());
        }


        Hand[] newhands = new Hand[4];
        int i = 0; //will fix later so initLayout accepts Player
        for(Player p: players){
            p.setUpClickListener();
            newhands[i] = p.getHand();
            i++;
        }




        cardUI.initLayout(nbPlayers, newhands); //newhands
    }

    private Hand[] getPilesFromCharacters(Character[] characters){
        Hand[] piles =null;
        if(characters != null){ //if there are characters, get their piles
            piles = new Hand[2];
            piles[0] = characters[0].getPile();
            piles[1] = characters[1].getPile();
        }
        return piles;

    }

    private void removeOldPiles(){
        Hand[] piles =getPilesFromCharacters(characters);
        cardUI.removeAll(piles);
    }

    private void createNewPiles() {

        characters = new Character[2];

        for (int i = 0; i < 2; i++) {


            characters[i] = new BaseCharacter();

            //get the pile for setting up the listener (this will happen when the heart card is played later)
            // for now the code requires i set up the listener now
            Hand characterPile = characters[i].getPile();

            cardUI.drawPile(characterPile, i);

            final Hand currentPile = characterPile;
            final int pileIndex = i;


            //this will go to input adapter \//can remove maybe
            characterPile.addCardListener(new CardAdapter() {
                public void leftClicked(Card card) {
                    selectedPileIndex = pileIndex;
                    currentPile.setTouchEnabled(false);
                }
            });
        }

        updatePileRanks();
    }

    private int[] calculatePileRanks(int pileIndex) {
        //Hand currentPile = piles[pileIndex];
        //int i = currentPile.isEmpty() ? 0 : ((Rank) currentPile.get(0).getRank()).getRankValue(); //remove

        Character currentCharacter = characters[pileIndex];
        int attack = currentCharacter.getAttack();
        int def = currentCharacter.getDefense();

        return new int[] { attack, def };
    }


    private void updatePileRanks() {
        for (int j = 0; j < characters.length; j++) { //characters.length remove
            int[] ranks = characters[j].calculatePileRanks();
            cardUI.updatePileRankState(j, ranks[ATTACK_RANK_INDEX], ranks[DEFENCE_RANK_INDEX]);
        }
    }

    private int getPlayerIndex(int index) {
        return index % nbPlayers;
    }

    private void executeAPlay(int playIndex) {
        createNewPiles();


        Play play = playFactory.createPlay(playIndex,this);

        play.runPlay();


        // 3: calculate winning & update scores for players
        updatePileRanks();
        int[] character0stats = calculatePileRanks(0);
        int[] character1stats = calculatePileRanks(1);


        LoggingSystem.logPiles(getPilesFromCharacters(characters), character0stats,character1stats );

        Rank pile0CharacterRank = characters[0].getBaseRank(); //(Rank) piles[0].getCardList().get(0).getRank();
        Rank pile1CharacterRank = characters[1].getBaseRank(); //(Rank) piles[1].getCardList().get(0).getRank();


        String character0Result;
        String character1Result;

        if (character0stats[ATTACK_RANK_INDEX] > character1stats[DEFENCE_RANK_INDEX]) {
            scores[0] += pile1CharacterRank.getRankValue();
            scores[2] += pile1CharacterRank.getRankValue();
            character0Result = "Character 0 attack on character 1 succeeded.";

        } else {
            scores[1] += pile1CharacterRank.getRankValue();
            scores[3] += pile1CharacterRank.getRankValue();
            character0Result = "Character 0 attack on character 1 failed.";


        }

        if (character1stats[ATTACK_RANK_INDEX] > character0stats[DEFENCE_RANK_INDEX]) {
            scores[1] += pile0CharacterRank.getRankValue();
            scores[3] += pile0CharacterRank.getRankValue();
            character1Result = "Character 1 attack on character 0 succeeded.";


        } else {
            scores[0] += pile0CharacterRank.getRankValue();
            scores[2] += pile0CharacterRank.getRankValue();
            character1Result = "Character 1 attack character 0 failed.";


        }
        updateScores();


        LoggingSystem.logBattle(character0Result);
        LoggingSystem.logBattle(character1Result);

        setStatusText(character0Result + " " + character1Result);

        // 5: discarded all cards on the piles

//        nextStartingPlayer += 1;
        delay(watchingTime);
        removeOldPiles();

    }

    public GameOfThrones() {
        super(700, 700, 30);

        cardUI = new CardUI(this);

        setupGame();



        //testing
//        Card QH =  new Card(deck, Suit.HEARTS, Rank.QUEEN);
//        Card card2C =  new Card(deck, Suit.CLUBS, Rank.TWO);
//        Character c = new BaseCharacter(QH );
//
//        Character c2 = new CharacterEffect(card2C,c);
//        cardUI.drawPile(c2.getPile(), 0);

        playFactory = new PlayFactory();

        for (int i = 0; i < nbPlays; i++) {
            executeAPlay(i);
            updateScores();
        }


        LoggingSystem.logResult(scores[0], scores[1]);
        cardUI.displayResult(scores[0], scores[1]);
        refresh();
    }

    public static void main(String[] args) {
        // System.out.println("Working Directory = " + System.getProperty("user.dir"));
        // final Properties properties = new Properties();
        // properties.setProperty("watchingTime", "5000");
        /*
        if (args == null || args.length == 0) {
            //  properties = PropertiesLoader.loadPropertiesFile("cribbage.properties");
        } else {
            //  properties = PropertiesLoader.loadPropertiesFile(args[0]);
        }

        String seedProp = properties.getProperty("seed");  //Seed property
        if (seedProp != null) { // Use property seed
			  seed = Integer.parseInt(seedProp);
        } else { // and no property
			  seed = new Random().nextInt(); // so randomise
        }
        */

        //set up random singleton
        RandomSingleton.getInstance().addSeed(130006);

        GameOfThrones.seed = 130006;
        //System.out.println("Seed = " + seed);
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
