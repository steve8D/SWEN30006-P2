package thrones.game.utility.rules;

import ch.aplu.jcardgame.Card;
import thrones.game.GameOfThrones;
import thrones.game.character.Character;

public class EffectRule extends LegalityChecker{


    @Override
    boolean isLegal(Character targetCharacter, Card attemptedCard) {
        GameOfThrones.Suit suit = (GameOfThrones.Suit)attemptedCard.getSuit();

        if(suit.isCharacter()==false){
            return true;
        }else {return false;}

    }
}
