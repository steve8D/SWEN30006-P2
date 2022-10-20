package thrones.game.gameLogic.sequence.plays.strategy;

public class NormalStartStrategy implements  IStartingPlayerStrategy {


    int startingPlayer;


    public NormalStartStrategy(int prevPlayerIndex) {
        startingPlayer = prevPlayerIndex + 1;
    }

    @Override
    public int getStartingPlayer() {
        return startingPlayer;
    }

}
