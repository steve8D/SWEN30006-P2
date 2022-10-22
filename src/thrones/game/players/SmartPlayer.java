package thrones.game.players;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import thrones.game.GameOfThrones;
import thrones.game.GameOfThrones.Suit;
import thrones.game.character.Character;

import java.util.*;

import thrones.game.GameOfThrones.Rank;
import thrones.game.character.effect.AttackEffect;
import thrones.game.character.effect.DefenseEffect;
import thrones.game.character.effect.MagicEffect;
import thrones.game.gameSequence.Battle;
import thrones.game.utility.CardCounter;
import thrones.game.utility.Subscriber;

public class SmartPlayer extends Player implements Subscriber {

    private Map<Card,Integer> shortlist = new HashMap<>();
    private ArrayList<Rank> diamondNumbersSeen = new ArrayList<>();
    private Card selectedCard ;




    public SmartPlayer(Hand hand, GameOfThrones game, int playerIndex) {
        super(hand, game, playerIndex);
        CardCounter.getInstance().subscribe(this,"Diamonds");
    }

    @Override
    public void setHand(Hand hand) {
        super.setHand(hand);
        for(Card c: hand.getCardList()){
            if( ((Suit)c.getSuit()).isMagic()){
                diamondNumbersSeen.add((Rank) c.getRank());
            }
        }
    }

    @Override
    public Optional<Card> pickCard(boolean isCharacter, Character[] characters) {
        this.isCharacter=isCharacter;


        Optional<Card>  selected;
        if(isCharacter){
            selected = Optional.of(pickHeart());
        }
        else{
            selected = pickEffect(characters);
        }
        return selected;
    }

    @Override
    public int pickPile(Character[] characters) {

        int selectedPile;

        if(((Suit)selectedCard.getSuit()).isMagic()){
            selectedPile= (team+1)%2; //magic on enemy
        }
        else{
            selectedPile= team;
        }

        if(isLegal(characters[selectedPile],selectedCard )==false){
            // should not be neccessary for smart player
            return NON_SELECTION_INDEX;
        }

        return selectedPile;
    }

    private Optional<Card> pickEffect(Character[] characters){



        ArrayList<Card> viableCards = removeDiamonds();


        Card selectedCard = simulateBattles(characters,viableCards);
        // cards which will change the battle outcome

        if(selectedCard==null){
            return Optional.empty();
        }
        else{
            return Optional.of(selectedCard);
        }

    }

    private Card simulateBattles(Character[] characters, ArrayList<Card> viableCards){
        //find a card which changes the battle outcome
        ArrayList<Card> influentialCards = new ArrayList<>();

        Character friendlyCharacter = characters[team];
        Character enemyCharacter=characters[(team+1)%2];

        Battle battle = new Battle();
        boolean[] currentBattleOutcome = battle.simulateBattle(friendlyCharacter,enemyCharacter );

        for(Card c: viableCards){
            Suit suit = ((Suit) c.getSuit());

            boolean[] hypotheticalBattleOutcome;
            if(suit.isMagic()){
                // apply magic to enemy
                hypotheticalBattleOutcome =
                        battle.simulateBattle(friendlyCharacter,new MagicEffect(c, enemyCharacter,false));

            }else {
                Character newchar;
                //this will become a factory's problem later
                if (suit.isAttack()) {
                    newchar = new AttackEffect(c, friendlyCharacter,false);
                } else if (suit.isDefence()) {
                    newchar = new DefenseEffect(c, friendlyCharacter,false);
                } else {
                    newchar = null;
                }
                //buff our character
                hypotheticalBattleOutcome =
                        battle.simulateBattle(newchar, enemyCharacter);
            }
            if((currentBattleOutcome[0]==false&& hypotheticalBattleOutcome[0] ==true)|| (currentBattleOutcome[1] ==true&&hypotheticalBattleOutcome[1]==false)){
                // if we win the battle, or the enemy loses the battle
                influentialCards.add(c);

            }


        }

        return selectInfluentialCard(influentialCards);


    }

    private Card selectInfluentialCard(ArrayList<Card> influentialCards){
        //just picks the first one. can modify in future
        if(influentialCards.size()>0){
            selectedCard = influentialCards.get(0);

        }
        else{
            selectedCard= null;
        }
        return selectedCard;
    }



    private ArrayList<Card> removeDiamonds(){
        // remve cards which there is still a diamond
        ArrayList<Card> viableCards = new ArrayList<>();

        for(Card c: hand.getCardList()){
            Rank rank = ((Rank)c.getRank());
            Suit suit = ((Suit) c.getSuit());

            if(suit.isCharacter()){
                continue;
            }

            if(suit.isMagic()){
                // if it is magic, then no opponent can double magic it
                viableCards.add(c);
                continue;
            }

           if(diamondNumbersSeen.contains(rank)){
                // if the smart player has seen the diamond, card can be played
                if(rank.getRankValue()==10){ // if it is one of the tens
                    if(playTen()){ //make sure you have seen all tens
                        viableCards.add(c);
                    }
                } else{
                    viableCards.add(c);

                }
                continue;
            }
        }

        return viableCards;

    }


    private Card pickHeart(){
        List<Card> shortListCards = new ArrayList<>();
        for (int i = 0; i < hand.getCardList().size(); i++) {
            Card card = hand.getCardList().get(i);
            GameOfThrones.Suit suit = (GameOfThrones.Suit) card.getSuit();
            if (suit.isCharacter() ) {
                shortListCards.add(card);
            }
        }
        // selects first heart. open to extension
        return shortListCards.get(0);
    }



    private boolean playTen(){
        if(diamondNumbersSeen.contains(Rank.TEN)&&diamondNumbersSeen.contains(Rank.JACK)&&diamondNumbersSeen.contains(Rank.QUEEN)&&diamondNumbersSeen.contains(Rank.KING)){
            //if you have seen all possible magics for 10
            return true;
        }else{
            return false;
        }
    }


    @Override
    public void notify(Card event) {
        if(diamondNumbersSeen.contains((Rank)event.getRank())){
            return; //if we played that card
        }
        diamondNumbersSeen.add((Rank)event.getRank());

    }
}
