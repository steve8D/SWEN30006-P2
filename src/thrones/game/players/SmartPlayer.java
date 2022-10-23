package thrones.game.players;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import thrones.game.GameOfThrones;
import thrones.game.GameOfThrones.Rank;
import thrones.game.GameOfThrones.Suit;
import thrones.game.character.Character;
import thrones.game.character.effect.CharacterEffectFactory;
import thrones.game.character.effect.MagicEffect;
import thrones.game.gameSequence.plays.Battle;
import thrones.game.utility.CardCounter;
import thrones.game.utility.Subscriber;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SmartPlayer extends Player implements Subscriber {
    private ArrayList<Rank> diamondNumbersSeen = new ArrayList<>();
    private Card selectedCard;

    public SmartPlayer(Hand hand, int playerIndex) {
        super(hand,  playerIndex);
        CardCounter.getInstance().subscribe(this, "Diamonds");
    }

    @Override
    public void setHand(Hand hand) {
        super.setHand(hand);
        for (Card c : hand.getCardList()) {
            if (((Suit) c.getSuit()).isMagic()) {
                diamondNumbersSeen.add((Rank) c.getRank());
            }
        }
    }

    @Override
    public Optional<Card> pickCard(boolean isCharacter, Character[] characters) {
        this.isCharacter = isCharacter;
        Optional<Card> selected;
        if (isCharacter) {
            selected = Optional.of(pickHeart());
        } else {
            selected = pickEffect(characters);
        }
        return selected;
    }

    @Override
    public int pickPile(Character[] characters) {
        int selectedPile;
        if (((Suit) selectedCard.getSuit()).isMagic()) {
            selectedPile = (team + 1) % 2;
        } else {
            selectedPile = team;
        }
        if (isLegal(characters[selectedPile], selectedCard) == false) {
            return NON_SELECTION_INDEX;
        }
        return selectedPile;
    }

    private Optional<Card> pickEffect(Character[] characters) {
        ArrayList<Card> viableCards = removeDiamonds();
        Card selectedCard = simulateBattles(characters, viableCards);
        if (selectedCard == null) {
            return Optional.empty();
        } else {
            return Optional.of(selectedCard);
        }
    }

    private Card simulateBattles(Character[] characters, ArrayList<Card> viableCards) {
        ArrayList<Card> influentialCards = new ArrayList<>();
        Character friendlyCharacter = characters[team];
        Character enemyCharacter = characters[(team + 1) % 2];
        Battle battle = new Battle();
        boolean[] currentBattleOutcome = battle.simulateBattle(friendlyCharacter, enemyCharacter);
        for (Card c : viableCards) {
            Suit suit = ((Suit) c.getSuit());
            boolean[] hypotheticalBattleOutcome;
            if (suit.isMagic()) {
                hypotheticalBattleOutcome =
                        battle.simulateBattle(friendlyCharacter, new MagicEffect(c, enemyCharacter, false));
            } else {
                Character newchar = CharacterEffectFactory.getInstance().createCharacter(c, friendlyCharacter, false);
                hypotheticalBattleOutcome =
                        battle.simulateBattle(newchar, enemyCharacter);
            }
            if ((currentBattleOutcome[0] == false && hypotheticalBattleOutcome[0] == true) || (currentBattleOutcome[1] == true && hypotheticalBattleOutcome[1] == false)) {
                influentialCards.add(c);
            }
        }
        return selectInfluentialCard(influentialCards);
    }

    private Card selectInfluentialCard(ArrayList<Card> influentialCards) {
        if (influentialCards.size() > 0) {
            selectedCard = influentialCards.get(0);
        } else {
            selectedCard = null;
        }
        return selectedCard;
    }

    private ArrayList<Card> removeDiamonds() {
        ArrayList<Card> viableCards = new ArrayList<>();
        for (Card c : hand.getCardList()) {
            Rank rank = ((Rank) c.getRank());
            Suit suit = ((Suit) c.getSuit());
            if (suit.isCharacter()) {
                continue;
            }
            if (suit.isMagic()) {
                viableCards.add(c);
                continue;
            }
            if (diamondNumbersSeen.contains(rank)) {
                if (rank.getRankValue() == 10) {
                    if (playTen()) {
                        viableCards.add(c);
                    }
                } else {
                    viableCards.add(c);
                }
            }
        }
        return viableCards;
    }

    private Card pickHeart() {
        List<Card> shortListCards = new ArrayList<>();
        for (int i = 0; i < hand.getCardList().size(); i++) {
            Card card = hand.getCardList().get(i);
            GameOfThrones.Suit suit = (GameOfThrones.Suit) card.getSuit();
            if (suit.isCharacter()) {
                shortListCards.add(card);
            }
        }
        return shortListCards.get(0);
    }

    private boolean playTen() {
        if (diamondNumbersSeen.contains(Rank.TEN)
                && diamondNumbersSeen.contains(Rank.JACK)
                && diamondNumbersSeen.contains(Rank.QUEEN)
                && diamondNumbersSeen.contains(Rank.KING)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void notify(Card event) {
        if (diamondNumbersSeen.contains((Rank) event.getRank())) {
            return;
        }
        diamondNumbersSeen.add((Rank) event.getRank());
    }
}
