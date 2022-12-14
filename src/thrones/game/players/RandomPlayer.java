package thrones.game.players;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import thrones.game.GameOfThrones;
import thrones.game.character.Character;
import thrones.game.utility.RandomSingleton;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RandomPlayer extends Player {
    public RandomPlayer(Hand hand, int playerIndex) {
        super(hand, playerIndex);
    }

    @Override
    public Optional<Card> pickCard(boolean isCharacter, Character[] characters) {
        this.isCharacter = isCharacter;
        return pickACorrectSuit(isCharacter);
    }

    @Override
    public int pickPile(Character[] characters) {
        int selectedPile = selectRandomPile();
        if (isLegal(characters[selectedPile], selected.get()) == false) {
            return NON_SELECTION_INDEX;
        }
        return selectedPile;
    }

    public Optional<Card> pickACorrectSuit(boolean isCharacter) {
        Hand currentHand = hand;
        List<Card> shortListCards = new ArrayList<>();
        for (int i = 0; i < currentHand.getCardList().size(); i++) {
            Card card = currentHand.getCardList().get(i);
            GameOfThrones.Suit suit = (GameOfThrones.Suit) card.getSuit();
            if (suit.isCharacter() == isCharacter) {
                shortListCards.add(card);
            }
        }
        if (shortListCards.isEmpty() || !isCharacter && RandomSingleton.getInstance().generateRandomInt(3) == 0) {
            selected = Optional.empty();
        } else {
            selected = Optional.of(shortListCards.get(RandomSingleton.getInstance().generateRandomInt(shortListCards.size())));
        }
        return selected;
    }

    public int selectRandomPile() {
        int selectedPileIndex = RandomSingleton.getInstance().generateRandomInt(2);
        return selectedPileIndex;
    }
}
