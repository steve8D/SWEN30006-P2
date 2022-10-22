package thrones.game.utility.rules;

import ch.aplu.jcardgame.Card;
import thrones.game.character.Character;

import java.util.ArrayList;

public class CompositeRule extends LegalityChecker{
    ArrayList<LegalityChecker> rules = new ArrayList<>();

    @Override
    public boolean isLegal(Character targetCharacter, Card attemptedCard){
        for(LegalityChecker rule: rules){
            if(rule.isLegal(targetCharacter,attemptedCard)==false){
                return false;
            }
        }
        return true;
    }

    public void addRule(LegalityChecker rule){
        rules.add(rule);
    }

}
