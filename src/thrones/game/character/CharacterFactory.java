package thrones.game.character;

import ch.aplu.jcardgame.Card;
import thrones.game.GameOfThrones;
import thrones.game.character.effect.AttackEffect;
import thrones.game.character.effect.DefenseEffect;
import thrones.game.character.effect.MagicEffect;

public class CharacterFactory {
    private static CharacterFactory instance = null;

    public static CharacterFactory getInstance() {
        if (instance == null) {
            instance = new CharacterFactory();
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

    public Character createCharacter() {
        return new BaseCharacter();
    }
}
