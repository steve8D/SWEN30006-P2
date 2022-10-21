package thrones.game.gameSequence.strategy;

public class LastStartStrategy implements  IStartingPlayerStrategy {
    int startingPlayer;


    public LastStartStrategy(int prevPlayerIndex){
        startingPlayer = prevPlayerIndex + 2;
    }
    @Override
    public int getStartingPlayer() {
        return startingPlayer;
    }
}
