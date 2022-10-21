package thrones.game.character;

import ch.aplu.jcardgame.Card;
import thrones.game.GameOfThrones;

public class DefenseEffect extends CharacterEffect{
    public DefenseEffect(Card card, Character character) {
        super(card, character);
    }

    @Override
    public int getDefense() {
        int dense =  character.getDefense();
        int extraDefense = ((GameOfThrones.Rank)card.getRank()).getRankValue();
        if(isDouble()){
            extraDefense=extraDefense*2;
        }
        return dense+extraDefense;
    }

    @Override
    public boolean isDouble() {
        int thisCardRank = ((GameOfThrones.Rank)card.getRank()).getRankValue();
        int rankOfCardBelow = ((GameOfThrones.Rank)character.getCard().getRank()).getRankValue();
        return (thisCardRank==rankOfCardBelow);
    }
}
