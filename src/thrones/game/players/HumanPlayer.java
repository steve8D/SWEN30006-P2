package thrones.game.players;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import thrones.game.GameOfThrones.Suit;
import thrones.game.GameOfThrones;
import thrones.game.character.Character;

import java.util.Optional;

public class HumanPlayer extends Player {
    private final int NON_SELECTION_VALUE = -1;
    private HumanAdapter humanAdapter = new HumanAdapter();

    public HumanPlayer(Hand hand, GameOfThrones game, int playerIndex) {
        super(hand, game, playerIndex);
    }

    @Override
    public Optional<Card> pickCard(boolean isCharacter, Character[] characters) {
        this.isCharacter = isCharacter;
        return waitForCorrectSuit(isCharacter);
    }

    @Override
    public int pickPile(Character[] characters) {
        return waitForPileSelection(characters);
    }

    public Optional<Card> waitForCorrectSuit(boolean isCharacter) {
        if (hand.isEmpty()) {
            //selected = Optional.empty();
            humanAdapter.setSelectedCard(Optional.empty());
        } else {
            //selected = null;
            humanAdapter.setSelectedCard(null);
            hand.setTouchEnabled(true);
            do {
                if (humanAdapter.getSelectedCard() == null) {
                    GameOfThrones.delay(100);
                    continue;
                }
                GameOfThrones.Suit suit = null;
                if(humanAdapter.getSelectedCard().isPresent()){
                    suit = (Suit) humanAdapter.getSelectedCard().get().getSuit();
                }
                if (isCharacter && suit != null && suit.isCharacter() ||
                        !isCharacter && (suit == null || !suit.isCharacter())) {
                    break;
                } else {
                    humanAdapter.setSelectedCard(null);
                    hand.setTouchEnabled(true);
                }
                GameOfThrones.delay(100);
            } while (true);
        }
        return humanAdapter.getSelectedCard();
    }

    public int waitForPileSelection(Character[] characters) {

        selectedPileIndex = humanAdapter.selectPile(characters);

        if (isLegal(characters[selectedPileIndex], humanAdapter.getSelectedCard().get()) == false) {
            sortHand(); // unfocuses the illegal card
            return NON_SELECTION_VALUE;
        }
        return selectedPileIndex;
    }

    @Override
    public void setHand(Hand hand) {
        super.setHand(hand);
        humanAdapter.setUpClickListener(hand);
    }
}
