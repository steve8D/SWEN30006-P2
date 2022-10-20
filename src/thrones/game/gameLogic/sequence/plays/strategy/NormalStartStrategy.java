package thrones.game.gameLogic.sequence.plays.strategy;

public class NormalStartStrategy implements  IStartingPlayerStrategy {
    @Override
    public int getStartingPlayer(int prevPlayerIndex) {
        return prevPlayerIndex + 1;
    }
}
