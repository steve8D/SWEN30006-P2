package thrones.game.players;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;
import thrones.game.GameOfThrones;
import thrones.game.utility.LoggingSystem;
import thrones.game.utility.PropertiesLoader;
import thrones.game.utility.RandomSingleton;

import java.util.List;

public class PlayerFactory {
    public static final int NB_CARDS_PER_PLAYER = 9;
    private Player[] players = new Player[GameOfThrones.nbPlayers];


    public Player[] getPlayers(GameOfThrones game, Deck deck) {
        for (int i = 0; i < GameOfThrones.nbPlayers; i++) {
            String keyString = "players." + i;
            String value = PropertiesLoader.getProperties().getProperty(keyString, PropertiesLoader.getDefaultPlayerType());
            players[i] = createPlayerType(value, deck, game, i);
        }
        //dealingOut(players, GameOfThrones.nbPlayers, NB_CARDS_PER_PLAYER);
        Dealer dealer = new Dealer();
        dealer.dealingOut(players,GameOfThrones.nbPlayers,NB_CARDS_PER_PLAYER);
        return players;
    }

    private Player createPlayerType(String playerType, Deck deck, GameOfThrones game, int index) {
        if (playerType != null) {
            Player player = null;
            if (playerType.matches("human")) {
                player = new HumanPlayer(new Hand(deck), game, index);
            }
            if (playerType.matches("random")) {
                player = new RandomPlayer(new Hand(deck), game, index);
            }
            if (playerType.matches("simple")) {
                player = new SimplePlayer(new Hand(deck), game, index);
            }
            if (playerType.matches("smart")) {
                player = new SmartPlayer(new Hand(deck), game, index);
            }
            return player;
        }
        System.out.println("Unable to create player");
        return null;
    }

//    private void dealingOut(Player[] players, int nbPlayers, int nbCardsPerPlayer) {
//        Hand[] hands = {new Hand(deck), new Hand(deck), new Hand(deck), new Hand(deck)};
//        Hand pack = deck.toHand(false);
//        assert pack.getNumberOfCards() == 52 : " Starting pack is not 52 cards.";
//        // Remove 4 Aces
//        List<Card> aceCards = pack.getCardsWithRank(GameOfThrones.Rank.ACE);
//        for (Card card : aceCards) {
//            card.removeFromHand(false);
//        }
//        assert pack.getNumberOfCards() == 48 : " Pack without aces is not 48 cards.";
//        // Give each player 3 heart cards
//        for (int i = 0; i < nbPlayers; i++) {
//            for (int j = 0; j < 3; j++) {
//                List<Card> heartCards = pack.getCardsWithSuit(GameOfThrones.Suit.HEARTS);
//                int x = RandomSingleton.getInstance().generateRandomInt(heartCards.size());
//                Card randomCard = heartCards.get(x);
//                randomCard.removeFromHand(false);
//                hands[i].insert(randomCard, false);
//            }
//        }
//        assert pack.getNumberOfCards() == 36 : " Pack without aces and hearts is not 36 cards.";
//        // Give each player 9 of the remaining cards
//        for (int i = 0; i < nbCardsPerPlayer; i++) {
//            for (int j = 0; j < nbPlayers; j++) {
//                assert !pack.isEmpty() : " Pack has prematurely run out of cards.";
//                Card dealt = randomCard(pack);
//                dealt.removeFromHand(false);
//                hands[j].insert(dealt, false);
//            }
//        }
//        for (int j = 0; j < nbPlayers; j++) {
//            assert hands[j].getNumberOfCards() == 12 : " Hand does not have twelve cards.";
//        }
//        for (int k = 0; k < nbPlayers; k++) {
//            players[k].setHand(hands[k]);
//            players[k].sortHand();
//            LoggingSystem.logHand(players[k]);
//        }
//    }
}
