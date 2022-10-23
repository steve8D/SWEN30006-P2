package thrones.game.gameSequence.strategy;

public class LastStartStrategy implements IStartingPlayerStrategy {
    private int startingPlayer;

    public LastStartStrategy(int prevPlayerIndex) {
        startingPlayer = prevPlayerIndex + 2;
    }

    @Override
    public int getStartingPlayer() {
        return startingPlayer;
    }
}
