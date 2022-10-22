package thrones.game.utility;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoader {
    private final static String defaultSeed = "130006";
    private final static String defaultWatchingTime = "5000";
    private final static String defaultPlayerType = "random";
    private static Properties properties;

    public static Properties loadPropertiesFile(String propertiesFile) {
        try (InputStream input = new FileInputStream(propertiesFile)) {
            Properties prop = new Properties();
            prop.load(input);
            properties = prop;
            return prop;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static Properties defaultProperties() {
        Properties prop = new Properties();
        prop.setProperty("seed", defaultSeed);
        prop.setProperty("watchingTime", defaultWatchingTime);
        prop.setProperty("players.0", defaultPlayerType);
        prop.setProperty("players.1", defaultPlayerType);
        prop.setProperty("players.2", defaultPlayerType);
        prop.setProperty("players.3", defaultPlayerType);
        properties = prop;
        return prop;
    }

    public static String getDefaultSeed() {
        return defaultSeed;
    }

    public static String getDefaultWatchingTime() {
        return defaultWatchingTime;
    }

    public static String getDefaultPlayerType() {
        return defaultPlayerType;
    }

    public static Properties getProperties() {
        return properties;
    }
}
