package thrones.game.gameSequence.strategy;

import thrones.game.utility.RandomSingleton;

public class RandomStartStrategy implements IStartingPlayerStrategy {
    private int startingPlayer;

    public RandomStartStrategy(int prevPlayerIndex) {
        startingPlayer = RandomSingleton.getInstance().generateRandomInt(4);
    }

    @Override
    public int getStartingPlayer() {
        return startingPlayer;
    }
}
