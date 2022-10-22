package thrones.game.character;

import ch.aplu.jcardgame.Card;

public class MagicEffect extends CharacterEffect{
    public MagicEffect(Card card, Character character) {
        super(card, character);
    }

    public MagicEffect(Card card, Character character, boolean transfer) {
        super(card, character, transfer);
    }
}
