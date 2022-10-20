package thrones.game.utility;

public class Random {
    static public int seed;
    static java.util.Random random;
    private static Random instance = null;
    public static Random getInstance() {
        if (instance == null) {
            instance = new Random();
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
