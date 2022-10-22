package thrones.game.gameSequence.plays;

import thrones.game.GameOfThrones;
import thrones.game.gameSequence.strategy.IStartingPlayerStrategy;
import thrones.game.gameSequence.strategy.LastStartStrategy;
import thrones.game.gameSequence.strategy.NormalStartStrategy;
import thrones.game.gameSequence.strategy.RandomStartStrategy;

public class PlayFactory {

    private int prevPlayerIndex = -1;
    private int playIndex = 0;
    private IStartingPlayerStrategy startingPlayerStrategy;
    private static PlayFactory instance = null;


    public static PlayFactory getInstance() {
        if (instance == null) {
            instance = new PlayFactory();
        }
        return instance;
    }



    public Play createPlay(int playIndex, GameOfThrones game) {
//        this.playIndex = playIndex;
//        this.game = game;
//        startingPlayerStrategy = selectStartingStrategy();
//        prevPlayerIndex = startingPlayerStrategy.getStartingPlayer(prevPlayerIndex);
//        return new Play(game.getHands(), game.piles, game.scores, prevPlayerIndex);

        this.playIndex = playIndex;
        Play play = new Play(game,game.getCardUI(),game.getPlayers(),selectStartingStrategy());


        return play;
    }

    private IStartingPlayerStrategy selectStartingStrategy() {
        if (playIndex == 0) {
            startingPlayerStrategy = new RandomStartStrategy(prevPlayerIndex);
        } else if (playIndex == 6-1) {// magic for now
            startingPlayerStrategy = new LastStartStrategy(prevPlayerIndex);
        } else {
            startingPlayerStrategy = new NormalStartStrategy(prevPlayerIndex);
        }
        prevPlayerIndex = startingPlayerStrategy.getStartingPlayer();

        return startingPlayerStrategy;
    }
}
