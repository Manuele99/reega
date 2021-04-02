package reega.data;

import reega.data.remote.RemoteConnection;
import reega.data.remote.RemoteDatabaseAPI;

/**
 * This factory returns an implementation of DataController based on the needs.
 */
public final class DataControllerFactory {
    public static DataController getDefaultDataController(RemoteConnection connection) {
        return RemoteDatabaseAPI.getInstance(connection);
    }

    public static DataController getRemoteDatabaseController(RemoteConnection connection) {
        return RemoteDatabaseAPI.getInstance(connection);
    }
}
