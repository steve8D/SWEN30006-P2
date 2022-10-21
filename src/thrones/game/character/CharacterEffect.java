package thrones.game.character;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import thrones.game.GameOfThrones.Rank;


public class CharacterEffect extends Character{
    //will be abstract in the future

    Character character;

    public CharacterEffect(Card card, Character character){
        this.card = card;
        this.character = character;

        insertToPile(card);
    }

    public CharacterEffect(Card card, Character character, boolean transfer){
        this.card = card;
        this.character = character;
        if(transfer){
            insertToPile(card); //pile is only neccessary for rendering.
            // transfer is false for smartPlayer's calculations.
        }
    }

    @Override
    public Hand getPile() {
        return character.getPile();
    }

    @Override
    public void insertToPile(Card card) {
        character.insertToPile(card);
    }

    @Override
    public int getAttack() {
        // this will become more complicated once we actually add characters
        return character.getAttack();
    }
    @Override
    public int getDefense() {
        return character.getDefense();
    }
    @Override
    public Rank getBaseRank() {
        return character.getBaseRank();
    }

    public boolean isDouble(){
        return false;// abstract for later
    }
}
