package thrones.game;

// Oh_Heaven.java

import ch.aplu.jcardgame.*;
import thrones.game.character.BaseCharacter;
import thrones.game.character.Character;
import thrones.game.gameLogic.sequence.*;
import thrones.game.players.Player;
import thrones.game.players.PlayerType;
import thrones.game.utility.CardUI;
import thrones.game.utility.LoggingSystem;

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
    private int nextStartingPlayer = random.nextInt(nbPlayers);

    private int[] scores = new int[nbPlayers];


    //boolean[] humanPlayers = { true, false, false, false};
    boolean[] humanPlayers = { false, false, false, false};


//    private void initScore() {
//        for (int i = 0; i < nbPlayers; i++) {
//            scores[i] = 0;
//            String text = "P" + i + "-0";
//            scoreActors[i] = new TextActor(text, Color.WHITE, bgColor, bigFont);
//            addActor(scoreActors[i], scoreLocations[i]);
//        }
//
//        String text = "Attack: 0 - Defence: 0";
//        for (int i = 0; i < pileTextActors.length; i++) {
//            pileTextActors[i] = new TextActor(text, Color.WHITE, bgColor, smallFont);
//            addActor(pileTextActors[i], pileStatusLocations[i]);
//        }
//    }

//    private void updateScore(int player) {
//        removeActor(scoreActors[player]);
//        String text = "P" + player + "-" + scores[player];
//        scoreActors[player] = new TextActor(text, Color.WHITE, bgColor, bigFont);
//        addActor(scoreActors[player], scoreLocations[player]);
//    }

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
            players[i] = new Player(new Hand(deck), this, i); //will remove this later
        }


        dealingOut(hands, nbPlayers, nbStartCards);

        for (int i = 0; i < nbPlayers; i++) {
            //hands[i].sort(Hand.SortType.SUITPRIORITY, true);
            players[i].sortHand();

            //LoggingSystem.logHand(i,hands[i]);
             LoggingSystem.logHand(i, players[i].getHand());
        }

//        for (final Hand currentHand : hands) {
//            // Set up human player for interaction
//            currentHand.addCardListener(new CardAdapter() {
//                public void leftDoubleClicked(Card card) {
//                    selected = Optional.of(card);
//                    currentHand.setTouchEnabled(false);
//                }
//                public void rightClicked(Card card) {
//                    selected = Optional.empty(); // Don't care which card we right-clicked for player to pass
//                    currentHand.setTouchEnabled(false);
//                }
//            });
//        }

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

//    private void pickACorrectSuit(int playerIndex, boolean isCharacter) {
//        Hand currentHand = hands[playerIndex];
//        List<Card> shortListCards = new ArrayList<>();
//        for (int i = 0; i < currentHand.getCardList().size(); i++) {
//            Card card = currentHand.getCardList().get(i);
//            Suit suit = (Suit) card.getSuit();
//            if (suit.isCharacter() == isCharacter) {
//                shortListCards.add(card);
//            }
//        }
//        if (shortListCards.isEmpty() || !isCharacter && random.nextInt(3) == 0) {
//            selected = Optional.empty();
//        } else {
//            selected = Optional.of(shortListCards.get(random.nextInt(shortListCards.size())));
//        }
//    }
//
//    private void selectRandomPile() {
//        selectedPileIndex = random.nextInt(2);
//    }
//
//    private void waitForCorrectSuit(int playerIndex, boolean isCharacter) {
//        if (hands[playerIndex].isEmpty()) {
//            selected = Optional.empty();
//        } else {
//            selected = null;
//            hands[playerIndex].setTouchEnabled(true);
//            do {
//                if (selected == null) {
//                    delay(100);
//                    continue;
//                }
//                Suit suit = selected.isPresent() ? (Suit) selected.get().getSuit() : null;
//                if (isCharacter && suit != null && suit.isCharacter() ||         // If we want character, can't pass and suit must be right
//                        !isCharacter && (suit == null || !suit.isCharacter())) { // If we don't want character, can pass or suit must not be character
//                    // if (suit != null && suit.isCharacter() == isCharacter) {
//                    break;
//                } else {
//                    selected = null;
//                    hands[playerIndex].setTouchEnabled(true);
//                }
//                delay(100);
//            } while (true);
//        }
//    }
//
//    private void waitForPileSelection() {
//        selectedPileIndex = NON_SELECTION_VALUE;
//
//        // more listener stuff
//        Hand[] newpiles = new Hand[2];
//        newpiles[0] = characters[0].getPile();
//        newpiles[1] = characters[1].getPile();
//
//
//
//        for (Hand pile : newpiles) {
//            pile.setTouchEnabled(true);
//        }
//        while(selectedPileIndex == NON_SELECTION_VALUE) {
//            delay(100);
//        }
//        for (Hand pile : newpiles) {
//            pile.setTouchEnabled(false);
//        }
//    }

    private int[] calculatePileRanks(int pileIndex) {
        //Hand currentPile = piles[pileIndex];
        //int i = currentPile.isEmpty() ? 0 : ((Rank) currentPile.get(0).getRank()).getRankValue(); //remove

        Character currentCharacter = characters[pileIndex];
        int attack = currentCharacter.getAttack();
        int def = currentCharacter.getDefense();

        return new int[] { attack, def };
    }

//    private void updatePileRankState(int pileIndex, int attackRank, int defenceRank) {
//        TextActor currentPile = (TextActor) pileTextActors[pileIndex];
//        removeActor(currentPile);
//        String text = playerTeams[pileIndex] + " Attack: " + attackRank + " - Defence: " + defenceRank;
//        pileTextActors[pileIndex] = new TextActor(text, Color.WHITE, bgColor, smallFont);
//        addActor(pileTextActors[pileIndex], pileStatusLocations[pileIndex]);
//    }

    private void updatePileRanks() {


        for (int j = 0; j < characters.length; j++) { //characters.length remove
            int[] ranks = characters[j].calculatePileRanks();
            cardUI.updatePileRankState(j, ranks[ATTACK_RANK_INDEX], ranks[DEFENCE_RANK_INDEX]);
        }
    }

    private int getPlayerIndex(int index) {
        return index % nbPlayers;
    }

    private void executeAPlay() {
        createNewPiles();

        nextStartingPlayer = getPlayerIndex(nextStartingPlayer);


        if (players[nextStartingPlayer].getHand().getNumberOfCardsWithSuit(Suit.HEARTS) == 0)
            nextStartingPlayer = getPlayerIndex(nextStartingPlayer + 1);
        assert players[nextStartingPlayer].getHand().getNumberOfCardsWithSuit(Suit.HEARTS) != 0 : " Starting player has no hearts.";




//        if (hands[nextStartingPlayer].getNumberOfCardsWithSuit(Suit.HEARTS) == 0)
//            nextStartingPlayer = getPlayerIndex(nextStartingPlayer + 1);
//        assert hands[nextStartingPlayer].getNumberOfCardsWithSuit(Suit.HEARTS) != 0 : " Starting player has no hearts.";

        // 1: play the first 2 hearts


        


        Play play = new Play(this,cardUI,characters,players,nextStartingPlayer,humanPlayers);
        play.runPlay();

//
//        for (int i = 0; i < 2; i++) {
//
//
//            int playerIndex = getPlayerIndex(nextStartingPlayer + i);
//            setStatusText("Player " + playerIndex + " select a Heart card to play");
//            PlayerType playerType;
//            if (humanPlayers[playerIndex]) {
//                playerType=PlayerType.HUMAN;
//            } else {
//                playerType=PlayerType.RANDOM;
//            }
//
//
//
//            Turn turn = new HeartTurn(this,cardUI,characters);
//            turn.runTurn(players[playerIndex], playerType);
//
//
////            if (humanPlayers[playerIndex]) {
////                //waitForCorrectSuit(playerIndex, true);
////                selected = players[playerIndex].waitForCorrectSuit(true);
////            } else {
////                //pickACorrectSuit(playerIndex, true);
////                selected = players[playerIndex].pickACorrectSuit(true);
////            }
////
////            int pileIndex = playerIndex % 2;
////            assert selected.isPresent() : " Pass returned on selection of character.";
////
////            LoggingSystem.logMove(playerIndex,selected.get(),pileIndex);
//////            selected.get().setVerso(false); //show card face
//////            selected.get().transfer(piles[pileIndex], true); // transfer to pile (includes graphic effect)
////
////            cardUI.moveToPile(selected.get(),characters[pileIndex].getPile());
////
////            // i am forced to initialise a character before a heart card is chosen because i need the pile for clicking purposes
////            // will fix later
////            BaseCharacter base = (BaseCharacter) characters[pileIndex];
////            base.addBaseCard(selected.get());
////
////
////            updatePileRanks();
//        }
//
//        int playerIndex3and4 = nextStartingPlayer+2;
//        for  (int i = 0; i < 2; i++) {
//
//            playerIndex3and4 = getPlayerIndex(playerIndex3and4);
//            setStatusText("Player" + playerIndex3and4 + " select a non-Heart card to play.");
//
//
//            PlayerType playerType;
//            if (humanPlayers[playerIndex3and4]) {
//                playerType=PlayerType.HUMAN;
//            } else {
//                playerType=PlayerType.RANDOM;
//            }
//            Turn turn = new EffectTurn(this,cardUI,characters);
//            turn.runTurn(players[playerIndex3and4], playerType);
//
//            playerIndex3and4++;
//
//        }
//
//        // 2: play the remaining nbPlayers * nbRounds - 2
//        int remainingTurns = nbPlayers * nbRounds - 4;
//        int nextPlayer = nextStartingPlayer ;
//
//        while(remainingTurns > 0) {
//            nextPlayer = getPlayerIndex(nextPlayer);
//            setStatusText("Player" + nextPlayer + " select a non-Heart card to play.");
//
//
//            PlayerType playerType;
//            if (humanPlayers[nextPlayer]) {
//                playerType=PlayerType.HUMAN;
//            } else {
//                playerType=PlayerType.RANDOM;
//            }
//            Turn turn = new EffectTurn(this,cardUI,characters);
//            turn.runTurn(players[nextPlayer], playerType);
//
////            if (humanPlayers[nextPlayer]) {
////                //waitForCorrectSuit(nextPlayer, false);
////                selected = players[nextPlayer].waitForCorrectSuit(false);
////
////            } else {
////                //pickACorrectSuit(nextPlayer, false);
////                selected = players[nextPlayer].pickACorrectSuit(false);
////
////            }
////
////            if (selected.isPresent()) {
////                // fix this later
////                setStatusText("Selected: " + LoggingSystem.canonical(selected.get()) + ". Player" + nextPlayer + " select a pile to play the card.");
////                if (humanPlayers[nextPlayer]) {
////                    //waitForPileSelection();
////                    selectedPileIndex = players[nextPlayer].waitForPileSelection(characters);
////                } else {
////                    //selectRandomPile();
////                    selectedPileIndex = players[nextPlayer].selectRandomPile();
////                }
////                LoggingSystem.logMove(nextPlayer,selected.get(),selectedPileIndex);
////
////                cardUI.moveToPile(selected.get(), characters[selectedPileIndex].getPile());
////                characters[selectedPileIndex] = new CharacterEffect(selected.get(), characters[selectedPileIndex]);
////
//////                selected.get().setVerso(false);
//////                selected.get().transfer(piles[selectedPileIndex], true); // transfer to pile (includes graphic effect)
////                updatePileRanks();
////            } else {
////                setStatusText("Pass.");
////                //for debugging
////                System.out.println("passed "+nextPlayer);
////            }
//
//
//
//            nextPlayer++;
//            remainingTurns--;
//        }

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

        nextStartingPlayer += 1;
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



        for (int i = 0; i < nbPlays; i++) {
            executeAPlay();
            updateScores();
        }


        LoggingSystem.logResult(scores[0], scores[1]);
        cardUI.displayResult(scores[0], scores[1]);
        refresh();
    }

    public static void main(String[] args) {
        Properties properties = new Properties();

        if (args == null || args.length == 0) {
            //set default
            properties = PropertiesLoader.defaultProperties();
        } else {
            properties = PropertiesLoader.loadPropertiesFile(args[0]);
        }

        String seedProp = properties.getProperty("seed");
        if (seedProp != null) { // Use property seed
			  seed = Integer.parseInt(seedProp);
        } else { // and no property
			  seed = new Random().nextInt(); // so randomise
        }

        System.out.println("get properties: "+properties);

        LoggingSystem.logSeed(seed);
        GameOfThrones.random = new Random(seed);
        new GameOfThrones();
    }

}
