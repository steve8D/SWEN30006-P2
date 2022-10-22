package thrones.game.character.effect;

import ch.aplu.jcardgame.Card;
import thrones.game.GameOfThrones.Rank;
import thrones.game.character.Character;

public class AttackEffect extends CharacterEffect {
    public AttackEffect(Card card, Character character) {
        super(card, character);
    }

    public AttackEffect(Card card, Character character, boolean transfer) {
        super(card, character, transfer);
    }

    @Override
    public int getAttack() {
        int attack = character.getAttack();
        int extraAttack = ((Rank) card.getRank()).getRankValue();
        if (isDouble()) {
            extraAttack = extraAttack * 2;
        }
        return attack + extraAttack;
    }

    @Override
    public boolean isDouble() {
        int thisCardRank = ((Rank) card.getRank()).getRankValue();
        int rankOfCardBelow = ((Rank) character.getCard().getRank()).getRankValue();
        return (thisCardRank == rankOfCardBelow);
    }
}
