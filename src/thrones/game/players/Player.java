package thrones.game.players;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.CardAdapter;
import ch.aplu.jcardgame.Hand;
import thrones.game.GameOfThrones;
import thrones.game.character.Character;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public  abstract class Player {

    protected Hand hand;
    protected Optional<Card> selected;
    protected int score=0;
    private GameOfThrones game;


    int selectedPileIndex;

    int playerIndex;



    public Player(Hand hand, GameOfThrones game , int playerIndex) {
        this.hand = hand;
        this.game=game;
        this.playerIndex=playerIndex;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    public Hand getHand() {
        return hand;
    }

    public void setHand(Hand hand) {
        this.hand = hand;


    }

    public void sortHand(){
        hand.sort(Hand.SortType.SUITPRIORITY, true);
    }

    public void setUpClickListener(){
        final Hand currentHand = hand;

        currentHand.addCardListener(new CardAdapter() {
            public void leftDoubleClicked(Card card) {
                selected = Optional.of(card);
                currentHand.setTouchEnabled(false);
            }
            public void rightClicked(Card card) {
                selected = Optional.empty(); // Don't care which card we right-clicked for player to pass
                currentHand.setTouchEnabled(false);
            }
        });
    }


    public abstract Optional<Card> pickCard(boolean isCharacter, Character[] characters) ;
    public  abstract int  pickPile (Character[] characters) ;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void addScore(int score){
        this.score += score;
    }
}
