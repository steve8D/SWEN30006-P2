package thrones.game.utility;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoader {

    private final static String defaultSeed = "130006";
    private final static String defaultWatchingTime = "5000";
    private final static String defaultPlayerType = "human";

    public static Properties loadPropertiesFile(String propertiesFile) {
        try (InputStream input = new FileInputStream(propertiesFile)) {

            Properties prop = new Properties();

            // load a properties file
            prop.load(input);

            return prop;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static Properties defaultProperties(){
        Properties properties = new Properties();
        properties.setProperty("seed",defaultSeed);
        properties.setProperty("watchingTime",defaultWatchingTime );
        properties.setProperty("players.0",defaultPlayerType);
        properties.setProperty("players.1",defaultPlayerType);
        properties.setProperty("players.2",defaultPlayerType);
        properties.setProperty("players.3",defaultPlayerType);
        return properties;
    }

    public static String getDefaultSeed(){
        return defaultSeed;
    }
    public static String getDefaultWatchingTime(){
        return defaultWatchingTime;
    }
    public static String getDefaultPlayerType(){
        return defaultPlayerType;
    }
}
