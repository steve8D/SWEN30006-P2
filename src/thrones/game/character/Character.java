package thrones.game.character;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;
import thrones.game.GameOfThrones;

public abstract class Character {


    protected Card card;


    abstract public int getAttack();
    abstract public int getDefense();

    abstract public int getBaseRank();


    abstract public Hand getPile(); //protected later
    abstract public void insertToPile(Card card);
}
