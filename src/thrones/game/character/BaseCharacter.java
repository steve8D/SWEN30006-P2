package thrones.game.character;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import thrones.game.GameOfThrones.Rank;

public class BaseCharacter extends Character{



    public BaseCharacter(Card card){
        super();
        this.card = card;
        this.pile = new Hand(deck);
        pile.insert(card,false);
    }



    @Override
    public int getAttack() {
        Rank rank =  (Rank)card.getRank();
        return rank.getRankValue();
    }
    @Override
    public int getDefense() {
        Rank rank =  (Rank)card.getRank();
        return rank.getRankValue();
    }
    @Override
    public int getBaseRank() {
        Rank rank =  (Rank)card.getRank();
        return rank.getRankValue();
    }


}
