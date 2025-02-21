package cat.institutmarianao.dao.connection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DbConnectionTest {
	private DbConnection dbConnection;
	private Connection connection;

	@Before
	void setUp() {
		dbConnection = new DbConnection();
	}

	@After
	void cleanUp() throws SQLException {
		if (connection != null) {
			connection.close();
		}
	}

	@Test
	void testConnectionOk() {
		try {
			connection = dbConnection.getConnection();
			assertNotNull(connection);
			assertEquals("H2", connection.getMetaData().getDatabaseProductName());
			assertEquals("socioc_db", connection.getCatalog().toLowerCase());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
}
