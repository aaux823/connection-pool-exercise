package connectionpool;

import java.util.concurrent.TimeUnit;

/**
 * Models a pool of equivalent connections.
 * <p/>
 * It provides two operations for callers:
 * <ul>
 *     <li>retrieve a connection, blocking until a connection is available if necessary</li>
 *     <li>return a previously retrieved connection to the pool</li>
 * </ul>
 */

public interface ConnectionPool {
    /**
     * Retrieve a connection from the pool.
     * <p/>
     * If no connection is immediately available, block for up to the timeout specified by delay / units waiting for
     * a new connection.
     * <p/>
     * If no connection is available within the specified timeout, return null.
     * <p/>
     * Callers should return the connection to the pool via {@link #releaseConnection} when they have finished using it.
     *
     * @param delay the timeout; if <=0, do not wait for a connection
     *              if none are immediately available.
     * @param units the time unit of the timeout delay
     * @return a new Connection, or {@code null} if no connection was available
     */
    Connection getConnection(long delay, TimeUnit units) throws ConnectionException, InterruptedException;

    /**
     * Return a previously retrieved connection to the pool.
     *
     * @param connection the connection to return
     */
    void releaseConnection(Connection connection) throws ConnectionException;
}
