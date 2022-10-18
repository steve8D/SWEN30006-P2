package thrones.game.character;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;
import thrones.game.GameOfThrones;
import thrones.game.GameOfThrones.Rank;

public class BaseCharacter extends Character{

    protected Deck deck = new Deck(GameOfThrones.Suit.values(), GameOfThrones.Rank.values(), "cover");
    protected Hand pile = new Hand(deck);

    public BaseCharacter(Card card){
        super();
        this.card = card;
        pile.insert(card,false);
    }
    public BaseCharacter(){
        card = null;

    }
    public void addBaseCard(Card card){
        this.card= card;
        pile.insert(card,false);
    }



    public Hand getPile() {
        return pile;
    }

    @Override
    public void insertToPile(Card card) {
        pile.insert(card,false);
    }

    @Override
    public int getAttack() {
        if(card==null){
            return 0;
        }
        Rank rank =  (Rank)card.getRank();
        return rank.getRankValue();
    }
    @Override
    public int getDefense() {
        if(card==null){
            return 0;
        }
        Rank rank =  (Rank)card.getRank();
        return rank.getRankValue();
    }
    @Override
    public int getBaseRank() {
        if(card==null){
            return 0;
        }
        Rank rank =  (Rank)card.getRank();
        return rank.getRankValue();
    }


}
