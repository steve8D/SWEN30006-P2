package thrones.game.character.effect;

import ch.aplu.jcardgame.Card;
import thrones.game.GameOfThrones;
import thrones.game.character.Character;
import thrones.game.character.effect.CharacterEffect;

public class DefenseEffect extends CharacterEffect {
    public DefenseEffect(Card card, Character character) {
        super(card, character);
    }

    @Override
    public int getDefense() {
        int defense =  character.getDefense();
        int extraDefense = ((GameOfThrones.Rank)card.getRank()).getRankValue();
        if(isDouble()){
            extraDefense=extraDefense*2;
        }
        return defense+extraDefense;
    }

    @Override
    public boolean isDouble() {
        int thisCardRank = ((GameOfThrones.Rank)card.getRank()).getRankValue();
        int rankOfCardBelow = ((GameOfThrones.Rank)character.getCard().getRank()).getRankValue();
        return (thisCardRank==rankOfCardBelow);
    }
}
