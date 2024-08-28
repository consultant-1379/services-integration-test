package com.ericsson.eniq.events.server.integritytests.stubs;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;

import com.ericsson.eniq.events.server.datasource.DBConnectionManager;
import com.ericsson.eniq.events.server.datasource.DataSourceConfigurationException;
import com.ericsson.eniq.events.server.datasource.loadbalancing.LoadBalancingPolicy;

/**
 * Intercept calls to fetch connection from DBConnectionManager, and return the
 * provided connection instead
 * 
 * Temporary tables are only visible from the same connection, so need to ensure
 * that the same connection is used from both the unit test creating and
 * populating the tables, and from the services code executing the query
 * 
 * @author EEMECOY
 * 
 */
public class InterceptedDBConnectionManager extends DBConnectionManager {

    private final LinkedList<Connection> connectionsList;

    public InterceptedDBConnectionManager(final Connection connection) {
        connectionsList = new LinkedList<Connection>();
        connectionsList.add(connection);
    }

    /**
     * This method is used to add a subsequent to the interceptedDbConnectionManager. Note, if only one connection
     * is needed it should be added using the constructor. This is necessary if the code will require a second db request 
     * because DataServiceBean will close the connection after each query.  
     * 
     * @param connection - the new connection to add.
     */
    public void addConnection(final Connection connection) {
        connectionsList.addFirst(connection);
    }

    @Override
    public Connection getConnection(final LoadBalancingPolicy loadBalancingPolicy) throws SQLException,
            DataSourceConfigurationException {
        return connectionsList.poll();
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            for (final Connection conn : connectionsList) {
                conn.close();
            }
        } finally {
            super.finalize();
        }
    }
}
