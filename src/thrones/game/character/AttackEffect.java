package thrones.game.character;

import ch.aplu.jcardgame.Card;

public class AttackEffect extends CharacterEffect{
    public AttackEffect(Card card, Character character) {
        super(card, character);
    }
    public AttackEffect(Card card, Character character, boolean transfer) {
        super(card, character, transfer);
    }
}
