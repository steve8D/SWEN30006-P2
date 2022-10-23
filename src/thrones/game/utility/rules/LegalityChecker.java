package thrones.game.utility.rules;

import ch.aplu.jcardgame.Card;
import thrones.game.character.Character;

public abstract class LegalityChecker {
    public abstract boolean isLegal(Character targetCharacter, Card attemptedCard);
}
