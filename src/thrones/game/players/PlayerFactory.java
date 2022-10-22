package thrones.game.players;

import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;
import thrones.game.GameOfThrones;
import thrones.game.PropertiesLoader;

import java.util.Properties;

public class PlayerFactory {
    private Player[] players = new Player[GameOfThrones.nbPlayers];

    public Player[] getPlayers(GameOfThrones game, Deck deck) {
        for (int i = 0; i < GameOfThrones.nbPlayers; i++) {
            String keyString = "players."+i;
            String value = PropertiesLoader.getProperties().getProperty(keyString, PropertiesLoader.getDefaultPlayerType());
            players[i] = createPlayerType(value, deck, game, i);
        }
        return players;
    }

    private Player createPlayerType(String playerType, Deck deck, GameOfThrones game, int index){
        if(playerType!=null){
            Player player = null;
            if(playerType.matches("human")){
                player = new HumanPlayer(new Hand(deck), game , index);
            }
            if(playerType.matches("random")){
                player = new RandomPlayer(new Hand(deck), game , index);
            }
            if(playerType.matches("simple")){
                //player = new SimplePlayer(new Hand(deck), game , index);
            }
            if(playerType.matches("smart")){
                //player = new SmartPlayerr(new Hand(deck), game , index);
            }
            return player;
        }
        System.out.println("Unable to create player");
        return null;
    }
}
