package thrones.game.players;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import thrones.game.GameOfThrones;
import thrones.game.character.Character;

import java.util.Optional;

public class SimplePlayer extends RandomPlayer {

    public static final int NON_SELECTION_INDEX = -1;
    private Optional<Card> selectedCard;

    public SimplePlayer(Hand hand, GameOfThrones game, int playerIndex) {
        super(hand, game, playerIndex);
    }

    @Override
    public Optional<Card> pickCard(boolean isCharacter, Character[] characters) {
        selectedCard = pickACorrectSuit(isCharacter);
        return selectedCard;
    }

    @Override
    public int pickPile(Character[] characters) {
        int selectedPile = selectRandomPile();

        Card card = selectedCard.get();
        GameOfThrones.Suit suit = ((GameOfThrones.Suit) card.getSuit());

        if (suit.isMagic()) {
            if (selectedPile == team) {
                return NON_SELECTION_INDEX; // if apply magic to own team
            }
        } else {
            if (selectedPile != team) {
                return NON_SELECTION_INDEX; //if apply buff to enemy
            }
        }
        return selectedPile;
    }

}
