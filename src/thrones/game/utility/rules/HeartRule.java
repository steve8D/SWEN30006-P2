package thrones.game.utility.rules;

import ch.aplu.jcardgame.Card;
import thrones.game.GameOfThrones.Suit;
import thrones.game.character.Character;

public class HeartRule extends LegalityChecker{

    @Override
    boolean isLegal(Character targetCharacter, Card attemptedCard) {

        Suit suit = (Suit)attemptedCard.getSuit();


            if(suit.isCharacter()){
                return true;
            }else{return false;}




    }
}
