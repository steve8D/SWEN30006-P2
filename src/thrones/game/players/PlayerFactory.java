package thrones.game.players;

import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;
import thrones.game.GameOfThrones;
import thrones.game.players.humanplayer.HumanPlayer;
import thrones.game.utility.PropertiesLoader;

public class PlayerFactory {
    public static final int NB_CARDS_PER_PLAYER = 9;
    private Player[] players = new Player[GameOfThrones.nbPlayers];

    public Player[] getPlayers(GameOfThrones game, Deck deck) {
        for (int i = 0; i < GameOfThrones.nbPlayers; i++) {
            String keyString = "players." + i;
            String value = PropertiesLoader.getProperties().getProperty(keyString, PropertiesLoader.getDefaultPlayerType());
            players[i] = createPlayerType(value, deck, i);
        }
        Dealer dealer = new Dealer();
        dealer.dealingOut(players, GameOfThrones.nbPlayers, NB_CARDS_PER_PLAYER);
        return players;
    }

    private Player createPlayerType(String playerType, Deck deck, int index) {
        if (playerType != null) {
            Player player = null;
            if (playerType.matches("human")) {
                player = new HumanPlayer(new Hand(deck), index);
            }
            if (playerType.matches("random")) {
                player = new RandomPlayer(new Hand(deck), index);
            }
            if (playerType.matches("simple")) {
                player = new SimplePlayer(new Hand(deck), index);
            }
            if (playerType.matches("smart")) {
                player = new SmartPlayer(new Hand(deck), index);
            }
            return player;
        }
        System.out.println("Unable to create player");
        return null;
    }
}
