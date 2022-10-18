package thrones.game.character;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;
import thrones.game.GameOfThrones;

public abstract class Character {

    protected Hand pile;
    protected Card card;
    protected Deck deck = new Deck(GameOfThrones.Suit.values(), GameOfThrones.Rank.values(), "cover");

    abstract public int getAttack();
    abstract public int getDefense();

    abstract public int getBaseRank();


}
