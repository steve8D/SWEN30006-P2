package thrones.game.players;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.CardAdapter;
import ch.aplu.jcardgame.Hand;
import thrones.game.GameOfThrones;
import thrones.game.character.Character;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Player {

    private Hand hand;
    private Optional<Card> selected;
    private GameOfThrones game;

    private final int NON_SELECTION_VALUE = -1;

    int selectedPileIndex;

    int playerIndex;



    public Player(Hand hand, GameOfThrones game , int playerIndex) {
        this.hand = hand;
        this.game=game;
        this.playerIndex=playerIndex;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    public Hand getHand() {
        return hand;
    }

    public void setHand(Hand hand) {
        this.hand = hand;


    }

    public void sortHand(){
        hand.sort(Hand.SortType.SUITPRIORITY, true);
    }

    public void setUpClickListener(){
        final Hand currentHand = hand;

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



    public Optional<Card> pickACorrectSuit( boolean isCharacter) {
        Hand currentHand = hand;
        List<Card> shortListCards = new ArrayList<>();
        for (int i = 0; i < currentHand.getCardList().size(); i++) {
            Card card = currentHand.getCardList().get(i);
            GameOfThrones.Suit suit = (GameOfThrones.Suit) card.getSuit();
            if (suit.isCharacter() == isCharacter) {
                shortListCards.add(card);
            }
        }
        if (shortListCards.isEmpty() || !isCharacter && GameOfThrones.random.nextInt(3) == 0) {
            selected = Optional.empty();
        } else {
            selected = Optional.of(shortListCards.get(GameOfThrones.random.nextInt(shortListCards.size())));
        }

        return selected;
    }

    public int selectRandomPile() {

        int selectedPileIndex = GameOfThrones.random.nextInt(2);
        return selectedPileIndex;
    }


    public Optional<Card> waitForCorrectSuit( boolean isCharacter) {
        if (hand.isEmpty()) {
            selected = Optional.empty();
        } else {
            selected = null;
            hand.setTouchEnabled(true);
            do {
                if (selected == null) {
                    GameOfThrones.delay(100);
                    continue;
                }
                GameOfThrones.Suit suit = selected.isPresent() ? (GameOfThrones.Suit) selected.get().getSuit() : null;
                if (isCharacter && suit != null && suit.isCharacter() ||         // If we want character, can't pass and suit must be right
                        !isCharacter && (suit == null || !suit.isCharacter())) { // If we don't want character, can pass or suit must not be character
                    // if (suit != null && suit.isCharacter() == isCharacter) {
                    break;
                } else {
                    selected = null;
                    hand.setTouchEnabled(true);
                }
                GameOfThrones.delay(100);
            } while (true);
        }

        return selected;
    }

    public  int  waitForPileSelection(Character[] characters) {
        selectedPileIndex = NON_SELECTION_VALUE;

        // more listener stuff
        Hand[] newpiles = new Hand[2];
        newpiles[0] = characters[0].getPile();
        newpiles[1] = characters[1].getPile();


        //establish listeners
        for(int i = 0; i < 2; i++){
            Hand characterPile = newpiles[i];

            final int pileIndex = i;
            final Hand currentPile = characterPile;

            characterPile.addCardListener(new CardAdapter() {
                public void leftClicked(Card card) {
                    selectedPileIndex = pileIndex;
                    currentPile.setTouchEnabled(false);
                }
            });

        }





        for (Hand pile : newpiles) {
            pile.setTouchEnabled(true);
        }
        while(selectedPileIndex == NON_SELECTION_VALUE) {
            GameOfThrones.delay(100);
        }
        for (Hand pile : newpiles) {
            pile.setTouchEnabled(false);
        }

        return selectedPileIndex;
    }
}
