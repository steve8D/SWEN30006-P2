package thrones.game.gameSequence.strategy;

public class NormalStartStrategy implements IStartingPlayerStrategy {
    private int startingPlayer;

    public NormalStartStrategy(int prevPlayerIndex) {
        startingPlayer = prevPlayerIndex + 1;
    }

    @Override
    public int getStartingPlayer() {
        return startingPlayer;
    }
}
