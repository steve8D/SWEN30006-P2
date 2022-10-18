package thrones.game.character;

import ch.aplu.jcardgame.Card;
import thrones.game.GameOfThrones;

public class CharacterEffect extends Character{
    //will be abstract in the future

    Character character;

    public CharacterEffect(Card card, Character character){
        this.card = card;
        this.character = character;
        this.pile = character.pile;
        this.pile.insert(card,false);

    }

    @Override
    public int getAttack() {
        return character.getAttack();
    }
    @Override
    public int getDefense() {
        return character.getDefense();
    }
    @Override
    public int getBaseRank() {
        return character.getBaseRank();
    }
}
