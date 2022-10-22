package thrones.game.players;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.CardAdapter;
import ch.aplu.jcardgame.Hand;
import thrones.game.GameOfThrones;
import thrones.game.character.Character;
import thrones.game.utility.rules.CompositeRule;
import thrones.game.utility.rules.DiamondOnHeartRule;
import thrones.game.utility.rules.EffectRule;
import thrones.game.utility.rules.HeartRule;

import java.util.Optional;

public class HumanPlayer extends Player {
    private final int NON_SELECTION_VALUE = -1;

    public HumanPlayer(Hand hand, GameOfThrones game, int playerIndex) {
        super(hand, game, playerIndex);

    }

    @Override
    public Optional<Card> pickCard(boolean isCharacter, Character[] characters) {
        this.isCharacter=isCharacter;
        return waitForCorrectSuit(isCharacter);
    }

    @Override
    public int pickPile(Character[] characters) {
        return waitForPileSelection(characters);
    }


    public Optional<Card> waitForCorrectSuit(boolean isCharacter) {
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

    public int waitForPileSelection(Character[] characters) {
        selectedPileIndex = NON_SELECTION_VALUE;

        // more listener stuff
        Hand[] newpiles = new Hand[2];
        newpiles[0] = characters[0].getPile();
        newpiles[1] = characters[1].getPile();


        //establish listeners
        for (int i = 0; i < 2; i++) {
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
        while (selectedPileIndex == NON_SELECTION_VALUE) {
            GameOfThrones.delay(100);
        }
        for (Hand pile : newpiles) {
            pile.setTouchEnabled(false);
        }
        if(isLegal(characters[selectedPileIndex],selected.get() )==false){
            sortHand(); // unfocuses the illegal card
            return NON_SELECTION_VALUE;
        }


        return selectedPileIndex;
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

    @Override
    public void setHand(Hand hand) {
        super.setHand(hand);
        setUpClickListener();
    }


}
