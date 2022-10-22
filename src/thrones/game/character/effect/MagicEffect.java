package thrones.game.character.effect;

import ch.aplu.jcardgame.Card;
import thrones.game.GameOfThrones;
import thrones.game.character.Character;

import java.util.ArrayList;

public class MagicEffect extends CharacterEffect {
    public MagicEffect(Card card, Character character) {
        super(card, character);
    }

    public MagicEffect(Card card, Character character, boolean transfer) {
        super(card, character, transfer);
    }

    @Override
    public int getAttack() {
        if (applyEffect(GameOfThrones.Suit.CLUBS)) {
            int thisCardRank = ((GameOfThrones.Rank) card.getRank()).getRankValue();
            int rankOfCardBelow = 0;
            rankOfCardBelow += character.getAttack();
            if (isDouble()) {
                thisCardRank = thisCardRank * 2;
            }
            if (thisCardRank > rankOfCardBelow) {
                return 0;
            }
            return rankOfCardBelow - thisCardRank;
        }
        return character.getAttack();
    }

    @Override
    public int getDefense() {
        if (applyEffect(GameOfThrones.Suit.SPADES)) {
            int thisCardRank = ((GameOfThrones.Rank) card.getRank()).getRankValue();
            int rankOfCardBelow = 0;
            rankOfCardBelow += character.getDefense();
            if (isDouble()) {
                thisCardRank = thisCardRank * 2;
            }
            if (thisCardRank > rankOfCardBelow) {
                return 0;
            }
            return rankOfCardBelow - thisCardRank;
        }
        return character.getDefense();
    }

    @Override
    public boolean isDouble() {
        int thisCardRank = ((GameOfThrones.Rank) card.getRank()).getRankValue();
        int rankOfCardBelow = ((GameOfThrones.Rank) character.getCard().getRank()).getRankValue();
        return (thisCardRank == rankOfCardBelow);
    }

    private boolean applyEffect(GameOfThrones.Suit suit) {
        GameOfThrones.Suit suitOfCardBelow = (GameOfThrones.Suit) character.getCard().getSuit();
        if (suitOfCardBelow == GameOfThrones.Suit.DIAMONDS) {
            suitOfCardBelow = getSuitOfCardBelow();
        }
        return (suit == suitOfCardBelow);
    }

    private GameOfThrones.Suit getSuitOfCardBelow() {
        GameOfThrones.Suit suitOfCardBelow;
        ArrayList<Card> piles = character.getPile().getCardList();
        int curCard = piles.indexOf(character.getCard());
        int i = 1;
        while (true) {
            int prevIndex = curCard - i;
            Card prevCard = piles.get(prevIndex);
            if (prevCard.getSuit() != GameOfThrones.Suit.DIAMONDS) {
                suitOfCardBelow = (GameOfThrones.Suit) prevCard.getSuit();
                break;
            }
            i++;
        }
        return suitOfCardBelow;
    }
}
