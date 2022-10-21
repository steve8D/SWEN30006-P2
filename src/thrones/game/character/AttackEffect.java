package thrones.game.character;

import ch.aplu.jcardgame.Card;
import thrones.game.GameOfThrones.Rank;

public class AttackEffect extends CharacterEffect{
    public AttackEffect(Card card, Character character) {
        super(card, character);
    }


    @Override
    public int getAttack() {
        int attack =  character.getAttack();
        int extraAttack = ((Rank)card.getRank()).getRankValue();
        if(isDouble()){
            extraAttack=extraAttack*2;
        }
        return attack+extraAttack;

    }

    @Override
    public boolean isDouble() {
        int thisCardRank = ((Rank)card.getRank()).getRankValue();
        int rankOfCardBelow = ((Rank)character.getCard().getRank()).getRankValue();
        return (thisCardRank==rankOfCardBelow);
    }
}
