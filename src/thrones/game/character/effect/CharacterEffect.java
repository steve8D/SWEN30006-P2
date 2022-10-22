package thrones.game.character.effect;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import thrones.game.GameOfThrones.Rank;
import thrones.game.character.Character;


public abstract class CharacterEffect extends Character {
    protected Character character;

    public CharacterEffect(Card card, Character character){
        this.card = card;
        this.character = character;
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

    protected CharacterEffect getCharacter(){ // for recursion in magic piles
        return (CharacterEffect) this.character;
    }
}
