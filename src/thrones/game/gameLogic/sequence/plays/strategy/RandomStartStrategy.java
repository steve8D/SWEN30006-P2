package thrones.game.gameLogic.sequence.plays.strategy;
import thrones.game.utility.Random;

public class RandomStartStrategy implements  IStartingPlayerStrategy {
    static Random random;
    @Override
    public int getStartingPlayer(int prevPlayerIndex) {
        return random.getInstance().generateRandomInt(4);
    }
}
