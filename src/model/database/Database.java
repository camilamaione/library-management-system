/**
 * This class implements operations related to connectivity to the database in operation.
 * @author Camila Maione
 */
package model.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class Database {
	
	private static Connection conn = null;
	
	/**
	 * Gets a connection to the database.
	 * @return A connection to the implemented database.
	 */
	public static Connection getConnection() {
		if (conn == null) {			
			try {
				// Load connection properties specified in db.properties file.
				Properties props = LoadProperties(); 
				String url = props.getProperty("dburl");
				conn = DriverManager.getConnection(url, props);
			} catch (SQLException e) {
				throw new DatabaseException(e.getMessage());
			}
		}
		return conn;
	}
	
	/**
	 * Closes the connection currently open to the database.
	 */
	public static void closeConnection() {
		try {
			conn.close();
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}
	
	/**
	 * Closes the given statement.
	 * @param st The statement to be closed.
	 */
	public static void closeStatement(Statement st) {
		try {
			st.close();
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}
	
	/**
	 * Closes the given result set.
	 * @param rs The result set to be closed.
	 */
	public static void closeResultSet(ResultSet rs) {
		try {
			rs.close();
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	/**
	 * Loads the set of properties described in "db.properties" file, which
	 * are necessary to open a connection to the database.
	 * @return The properties loaded.
	 */
	private static Properties LoadProperties() {
		try (FileInputStream fs = new FileInputStream(new File("db.properties"))) {
			Properties props = new Properties();
			props.load(fs);
			return props;
		} catch (FileNotFoundException e) {
			throw new DatabaseException(e.getMessage());
		} catch (IOException e) {
			throw new DatabaseException(e.getMessage());
		}
	}
	

}
