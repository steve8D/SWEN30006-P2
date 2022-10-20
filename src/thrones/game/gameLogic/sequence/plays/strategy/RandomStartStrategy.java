package thrones.game.gameLogic.sequence.plays.strategy;
import thrones.game.utility.RandomSingleton;

public class RandomStartStrategy implements  IStartingPlayerStrategy {
    static RandomSingleton random;

    int startingPlayer;


    public RandomStartStrategy(int prevPlayerIndex){
        startingPlayer = random.getInstance().generateRandomInt(4);;
    }
    @Override
    public int getStartingPlayer() {
        return startingPlayer;
    }



}
