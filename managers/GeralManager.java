package notzStorage.utils.managers;

import notzStorage.utils.sub.DM;

import java.io.IOException;
import java.sql.SQLException;

public class GeralManager extends DM {
    public static void startPlugin() {
        try {
            initialize();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void startBackup() {
        try {
            makeBackup();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void resetPlugin() {
        try {
            resetDatabase();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
