package thrones.game.character;

import ch.aplu.jcardgame.Card;
import thrones.game.GameOfThrones;

public class MagicEffect extends CharacterEffect{
    public MagicEffect(Card card, Character character) {
        super(card, character);
    }

    @Override
    public int getAttack() {
        if (applyEffect(GameOfThrones.Suit.CLUBS)) {
            int thisCardRank = ((GameOfThrones.Rank)card.getRank()).getRankValue();
            int rankOfCardBelow = 0;
            rankOfCardBelow += character.getAttack();
            if(isDouble()){
                thisCardRank=thisCardRank*2;
            }
            if (thisCardRank > rankOfCardBelow) {
                return 0;
            }
            return rankOfCardBelow-thisCardRank;
        }
        return character.getAttack();
    }

    @Override
    public int getDefense() {
        if (applyEffect(GameOfThrones.Suit.SPADES)) {
            int thisCardRank = ((GameOfThrones.Rank)card.getRank()).getRankValue();
            int rankOfCardBelow = 0;
            rankOfCardBelow += character.getDefense();
            if(isDouble()){
                thisCardRank=thisCardRank*2;
            }
            if (thisCardRank > rankOfCardBelow) {
                return 0;
            }
            return rankOfCardBelow-thisCardRank;
        }
        return character.getDefense();
    }

    @Override
    public boolean isDouble() {
        int thisCardRank = ((GameOfThrones.Rank)card.getRank()).getRankValue();
        int rankOfCardBelow = ((GameOfThrones.Rank)character.getCard().getRank()).getRankValue();
        return (thisCardRank==rankOfCardBelow);
    }

    private boolean applyEffect(GameOfThrones.Suit suit) {
        GameOfThrones.Suit suitOfCardBelow = (GameOfThrones.Suit)character.getCard().getSuit();
//        while (suitOfCardBelow == GameOfThrones.Suit.DIAMONDS) {
//            Card temp = )character.getCard();
//            suitOfCardBelow = (GameOfThrones.Suit) card.getSuit();
//        }
        return (suit == suitOfCardBelow);
    }
}
