package thrones.game.character;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import thrones.game.GameOfThrones.Rank;

public abstract class Character {
    protected Card card;
    abstract public int getAttack();
    abstract public int getDefense();
    abstract public Rank getBaseRank();

    abstract public Hand getPile(); //protected later
    abstract public void insertToPile(Card card);

    public int[] calculatePileRanks() {
        int attack = this.getAttack();
        int def = this.getDefense();

        return new int[] { attack, def };
    }

    public Card getCard() {
        return card;
    }
}
