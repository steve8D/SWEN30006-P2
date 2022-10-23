package thrones.game.players.humanplayer;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import thrones.game.GameOfThrones.Suit;
import thrones.game.GameOfThrones;
import thrones.game.character.Character;
import thrones.game.players.Player;

import java.util.Optional;

public class HumanPlayer extends Player {
    private final int NON_SELECTION_VALUE = -1;
    private HumanInputListener humanInputListener = new HumanInputListener();

    public HumanPlayer(Hand hand,  int playerIndex) {
        super(hand,  playerIndex);
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
            humanInputListener.setSelectedCard(Optional.empty());
        } else {
            //selected = null;
            humanInputListener.setSelectedCard(null);
            hand.setTouchEnabled(true);
            do {
                if (humanInputListener.getSelectedCard() == null) {
                    GameOfThrones.delay(100);
                    continue;
                }
                GameOfThrones.Suit suit = null;
                if(humanInputListener.getSelectedCard().isPresent()){
                    suit = (Suit) humanInputListener.getSelectedCard().get().getSuit();
                }
                if (isCharacter && suit != null && suit.isCharacter() ||
                        !isCharacter && (suit == null || !suit.isCharacter())) {
                    break;
                } else {
                    humanInputListener.setSelectedCard(null);
                    hand.setTouchEnabled(true);
                }
                GameOfThrones.delay(100);
            } while (true);
        }
        return humanInputListener.getSelectedCard();
    }

    public int waitForPileSelection(Character[] characters) {

        selectedPileIndex = humanInputListener.selectPile(characters);

        if (isLegal(characters[selectedPileIndex], humanInputListener.getSelectedCard().get()) == false) {
            sortHand(); // unfocuses the illegal card
            return NON_SELECTION_VALUE;
        }
        return selectedPileIndex;
    }

    @Override
    public void setHand(Hand hand) {
        super.setHand(hand);
        humanInputListener.setUpClickListener(hand);
    }
}
