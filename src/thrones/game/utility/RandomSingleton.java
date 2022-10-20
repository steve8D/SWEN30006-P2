package thrones.game.utility;

public class RandomSingleton {
    static public int seed;
    static java.util.Random random;
    private static RandomSingleton instance = null;


    public static RandomSingleton getInstance() {
        if (instance == null) {
            instance = new RandomSingleton();
        }
        return instance;
    }

    public void addSeed(int seed) {
        this.seed = seed;
        random = new java.util.Random(seed);
    }

    public int generateRandomInt(int bound) {
        return random.nextInt(bound);
    }

}
