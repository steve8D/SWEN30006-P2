package thrones.game.players;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import thrones.game.GameOfThrones;
import thrones.game.character.Character;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RandomPlayer extends Player{


    public RandomPlayer(Hand hand, GameOfThrones game, int playerIndex) {
        super(hand, game, playerIndex);
    }

    @Override
    public Optional<Card> pickCard(boolean isCharacter, Character[] characters) {
        return pickACorrectSuit(isCharacter);
    }

    @Override
    public int pickPile(Character[] characters) {
        return selectRandomPile();
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
}
