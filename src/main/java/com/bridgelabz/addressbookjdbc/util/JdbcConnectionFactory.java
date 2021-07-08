/**
 * Purpose:JDBC connection
 * @author Ananya K
 * @version 1.0
 * @since 07/07/2021
 * 
 */
package com.bridgelabz.addressbookjdbc.util;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bridgelabz.addressbookjdbc.exception.JdbcConnectorException;

public class JdbcConnectionFactory {
	private static final Logger LOG = LogManager.getLogger(JdbcConnectionFactory.class);

	/**
	 * Attempts to establish a connection to the given database URL. The
	 * DriverManager attempts to select an appropriate driver from the set of
	 * registered JDBC drivers.
	 * 
	 * @return
	 * @throws JdbcConnectorException
	 */
	public static Connection getJdbcConnection() throws JdbcConnectorException {
		String jdbcUrl = "jdbc:mysql://localhost:3306/address_book_service?useSSL=false";
		String username = "root";
		String password = "Ananyagowda123!";
		Connection connection;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			LOG.debug("Driver loaded");
		} catch (ClassNotFoundException e) {
			throw new JdbcConnectorException("Cannot find the driver in the class path" + e);
		}
		listDrivers();
		try {
			LOG.debug("Connecting to database" + jdbcUrl);
			connection = DriverManager.getConnection(jdbcUrl, username, password);
			LOG.debug("Connection is successful" + connection);
		} catch (Exception e) {
			throw new JdbcConnectorException(e.getMessage());
		}
		return connection;
	}

	/**
	 * Retrieves an Enumeration with all of the currently loaded JDBC drivers to
	 * which the current caller has access.
	 */
	private static void listDrivers() {
		Enumeration<Driver> driverList = DriverManager.getDrivers();
		while (driverList.hasMoreElements()) {
			Driver driverClass = driverList.nextElement();
			LOG.debug(" " + driverClass.getClass().getName());
		}
	}
}
