package thrones.game.gameLogic.sequence.plays.strategy;

public class LastStartStrategy implements  IStartingPlayerStrategy {
    @Override
    public int getStartingPlayer(int prevPlayerIndex) {
        return prevPlayerIndex + 2;
    }
}
