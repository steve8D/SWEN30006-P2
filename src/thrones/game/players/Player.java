package thrones.game.players;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.CardAdapter;
import ch.aplu.jcardgame.Hand;
import thrones.game.GameOfThrones;
import thrones.game.character.Character;
import thrones.game.utility.rules.CompositeRule;
import thrones.game.utility.rules.DiamondOnHeartRule;
import thrones.game.utility.rules.EffectRule;
import thrones.game.utility.rules.HeartRule;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class Player {

    protected Hand hand;
    protected Optional<Card> selected;
    protected int score=0;
    private GameOfThrones game;
    protected int selectedPileIndex;
    protected int playerIndex;
    protected int team ;
    protected boolean isCharacter;

    protected static final int NON_SELECTION_INDEX = -1;


    public Player(Hand hand, int playerIndex) {
        this.hand = hand;
        this.playerIndex=playerIndex;
        team = playerIndex%2;
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

    protected boolean isLegal(Character character, Card card){
        CompositeRule legalityChecker = new CompositeRule();


        if(isCharacter){
            legalityChecker.addRule(new HeartRule());
        } else {
            legalityChecker.addRule(new EffectRule());
            legalityChecker.addRule(new DiamondOnHeartRule());
        }
        return legalityChecker.isLegal(character,card);

    }
}
