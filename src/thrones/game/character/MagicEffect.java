package thrones.game.character;

import ch.aplu.jcardgame.Card;

public class MagicEffect extends CharacterEffect{
    public MagicEffect(Card card, Character character) {
        super(card, character);
    }


    private boolean lessThanZero(int effect){
        if(effect<0){
            return true;
        }else{
            return false;
        }
    }
}
