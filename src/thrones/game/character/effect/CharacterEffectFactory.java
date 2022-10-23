package thrones.game.character.effect;

import ch.aplu.jcardgame.Card;
import thrones.game.GameOfThrones;
import thrones.game.character.Character;

public class CharacterEffectFactory {
    private static CharacterEffectFactory instance = null;

    public static CharacterEffectFactory getInstance() {
        if (instance == null) {
            instance = new CharacterEffectFactory();
        }
        return instance;
    }

    public Character createCharacter(Card card, Character mostRecentCard, boolean transfer) {
        if (((GameOfThrones.Suit) card.getSuit()).isAttack()) {
            return new AttackEffect(card, mostRecentCard, transfer);
        } else if (((GameOfThrones.Suit) card.getSuit()).isDefence()) {
            return new DefenseEffect(card, mostRecentCard, transfer);
        } else if (((GameOfThrones.Suit) card.getSuit()).isMagic()) {
            return new MagicEffect(card, mostRecentCard, transfer);
        }
        return null;
    }
}
