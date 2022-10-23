package thrones.game.players.humanplayer;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.CardAdapter;
import ch.aplu.jcardgame.Hand;
import thrones.game.GameOfThrones;
import thrones.game.character.Character;

import java.util.Optional;

public class HumanAdapter implements IHumanInputAdapter {

    private Optional<Card> selectedCard;
    private int selectedPileIndex;
    private final int NON_SELECTION_VALUE = -1;


    public int selectPile(Character[] characters) {
        selectedPileIndex = NON_SELECTION_VALUE;

        Hand[] newpiles = new Hand[2];
        newpiles[0] = characters[0].getPile();
        newpiles[1] = characters[1].getPile();

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
        return selectedPileIndex;
    }

    public void setUpClickListener(Hand hand) {
        final Hand currentHand = hand;
        currentHand.addCardListener(new CardAdapter() {
            public void leftDoubleClicked(Card card) {
                selectedCard = Optional.of(card);
                currentHand.setTouchEnabled(false);
            }

            public void rightClicked(Card card) {
                selectedCard = Optional.empty(); // Don't care which card we right-clicked for player to pass
                currentHand.setTouchEnabled(false);
            }
        });
    }

    public Optional<Card> getSelectedCard() {
        return selectedCard;
    }

    public void setSelectedCard(Optional<Card> selectedCard) {
        this.selectedCard = selectedCard;
    }
}
