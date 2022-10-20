package thrones.game.gameLogic.sequence.plays.strategy;

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
