package thrones.game.utility.rules;

import ch.aplu.jcardgame.Card;
import thrones.game.character.Character;


public abstract class LegalityChecker {

    protected static final  String HEARTTURN = "HEART";
    protected static final  String EFFECTTURN = "EFFECT";

    abstract boolean isLegal(Character targetCharacter, Card attemptedCard);

}
