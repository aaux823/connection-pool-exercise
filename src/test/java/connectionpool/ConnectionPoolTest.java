package connectionpool;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.*;


class ConnectionPoolTest {

    @Test
    @DisplayName("given connection pool available when getConnection with 0 delays then should return connection")
    void getConnectionReturnsAConnection() throws ConnectionException, InterruptedException {
        ConnectionFactory connectionFactory = mock(ConnectionFactory.class);
        when(connectionFactory.newConnection()).thenReturn(mock(Connection.class));
        ConnectionPool connectionPool = new ConnectionPoolImpl(connectionFactory, 10);
        assertNotNull(connectionPool.getConnection(0, TimeUnit.MILLISECONDS));
    }

    @Test
    @DisplayName("given connection pool available when getConnection with delays then should return connection")
    void getConnectionWithDelayReturnsAConnection() throws ConnectionException, InterruptedException {
        ConnectionFactory connectionFactory = mock(ConnectionFactory.class);
        when(connectionFactory.newConnection()).thenReturn(mock(Connection.class));
        ConnectionPool connectionPool = new ConnectionPoolImpl(connectionFactory, 10);
        assertNotNull(connectionPool.getConnection(2, TimeUnit.MILLISECONDS));
    }

    @Test
    @DisplayName("given maximum connection is reached when getConnection then should return null")
    void getConnectionMaxConnectionReturnsNull() throws ConnectionException, InterruptedException {
        ConnectionFactory connectionFactory = mock(ConnectionFactory.class);
        when(connectionFactory.newConnection()).thenReturn(mock(Connection.class));
        ConnectionPool connectionPool = new ConnectionPoolImpl(connectionFactory, 1);

        // First connection - max connection reached
        assertNotNull(connectionPool.getConnection(0, TimeUnit.MILLISECONDS));

        // Assert that null is returned by getConnection
        assertNull(connectionPool.getConnection(0, TimeUnit.MILLISECONDS));
    }

    @Test
    @DisplayName("given no new connection was created when release connection then should throw exception")
    void releaseConnectionWithNullParamThrowsException() throws ConnectionException {
        ConnectionFactory connectionFactory = mock(ConnectionFactory.class);
        ConnectionPool connectionPool = new ConnectionPoolImpl(connectionFactory, 1);
        Exception exception = assertThrows(ConnectionException.class, () -> {
            connectionPool.releaseConnection(null);
        });
        assertEquals("You cannot release a null connection", exception.getMessage());
    }

    @Test
    @DisplayName("given null connection when release connection then should throw exception")
    void releaseConnectionWithoutGettingConnectionFirstThrowsException() throws ConnectionException {
        ConnectionFactory connectionFactory = mock(ConnectionFactory.class);
        ConnectionPool connectionPool = new ConnectionPoolImpl(connectionFactory, 1);
        Exception exception = assertThrows(ConnectionException.class, () -> {
            connectionPool.releaseConnection(mock(Connection.class));
        });
        assertEquals("You cannot release connection", exception.getMessage());
    }

    @Test
    @DisplayName("given all connections were released when release connection then should throw exception")
    void releaseConnectionWhenAllConnectionsHaveBeenReleasedFirstThrowsException() throws ConnectionException, InterruptedException {
        ConnectionFactory connectionFactory = mock(ConnectionFactory.class);
        ConnectionPool connectionPool = new ConnectionPoolImpl(connectionFactory, 1);
        connectionPool.getConnection(0, TimeUnit.MILLISECONDS);
        // First release - all connections released
        connectionPool.releaseConnection(mock(Connection.class));
        // Attempt to release connection again
        Exception exception = assertThrows(ConnectionException.class, () -> {
            connectionPool.releaseConnection(mock(Connection.class));
        });
        assertEquals("You cannot release connection", exception.getMessage());
    }
}
