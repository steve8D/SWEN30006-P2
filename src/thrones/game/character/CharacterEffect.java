package thrones.game.character;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import thrones.game.GameOfThrones.Rank;

import java.util.ArrayList;


public class CharacterEffect extends Character {
    //will be abstract in the future
    Character character;

    public CharacterEffect(Card card, Character character){
        this.card = card;
        this.character = character;

//        ArrayList<Card> temp = character.getPile().getCardList();
//        BaseCharacter tempCard = new BaseCharacter();
//        this.character = tempCard;
        insertToPile(card);
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
        int score = character.getAttack();
        return score;
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
