package connectionpool;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Connection pool implementation.
 * <p/>
 * This implementation should:
 *
 * <ul>
 *     <li>Use the provided ConnectionFactory implementation to build new Connection objects.</li>
 *     <li>Allow up to {@code maxConnections} simultaneous connections (both in-use and idle)</li>
 *     <li>Call Connection.testConnection() before returning a Connection to a caller; if testConnection() returns false,
 *     this Connection instance should be discarded and a different Connection obtained.</li>
 *     <li>Be safe to use by multiple callers simultaneously from different threads</li>
 * </ul>
 *
 * You may find the locking and queuing objects provided by {@code java.util.concurrent} useful.
 * <p/>
 * Some possible extensions:
 * <ul>
 *     <li>Check that connections returned via releaseConnection() were actually allocated via getConnection() (and
 *     haven't already been returned)</li>
 *     <li>Test idle connections periodically, and discard those which fail a testConnection() check.</li>
 *     <li>Detect Connections that have been handed out to a caller, but where the caller has discarded the Connection
 *     object, and don't count them as "in use". (hint: have the pool store WeakReferences to in-use connections, and
 *     use that to detect when they become only weakly reachable)</li>
 * </ul>
 */
public class ConnectionPoolImpl implements ConnectionPool {

    private ConnectionFactory factory;
    private int maxConnections;
    private int currentConnections;

    private ReentrantLock reentrantLock = new ReentrantLock(true);

    /**
     * Construct a new pool that uses a provided factory to construct connections, and allows a given maximum number of
     * connections simultaneously.
     *
     * @param factory the factory to use to construct connections
     * @param maxConnections the number of simultaneous connections to allow
     */
    public ConnectionPoolImpl(ConnectionFactory factory, int maxConnections) {
        this.factory = factory;
        this.maxConnections = maxConnections;
        this.currentConnections = 0;
    }

    public Connection getConnection(long delay, TimeUnit units) throws ConnectionException, InterruptedException {
        // Check first if maxConnections is reached before begin
        if (this.maxConnections == currentConnections) {
            return null;
        }

        // Trying to acquire lock
        boolean acquiredLock = reentrantLock.tryLock(delay, units);
        if (acquiredLock) {
            // Update the currentConnections and return a new connection
            try {
                this.currentConnections = this.currentConnections + 1;
                return factory.newConnection();
            } catch (Exception e) {
                throw new ConnectionException("Error encountered while getting connection ", e);
            } finally {
                reentrantLock.unlock();
            }
        }
        return null;
    }
        
    public void releaseConnection(Connection connection) throws ConnectionException {
        // Check if connection is valid
        if (connection == null) {
            throw new ConnectionException("You cannot release a null connection");
        }

        // Check to prevent from overflow
        if (this.currentConnections == 0) {
            throw new ConnectionException("You cannot release connection");
        }

        // Try to acquire lock - this should be fairly quick operation so no delay is required
        boolean acquiredLock = reentrantLock.tryLock();
        if (acquiredLock) {
            try {
                // Update currentConnections
                this.currentConnections = this.currentConnections - 1;
            } catch (Exception e) {
                throw new ConnectionException("Error encountered while releasing connection ", e);
            } finally {
                reentrantLock.unlock();
            }
        }

    }
}
