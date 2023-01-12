package database;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class SQLiteDataSource {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SQLiteDataSource.class);
	private static final HikariConfig config = new HikariConfig();
	private static final HikariDataSource ds;
	
	static {
		try {
			File dbFile = new File("database.db");
			
			if (!dbFile.exists()) {
				if (dbFile.createNewFile()) {
					LOGGER.info("Created database file");
				}
				else {
					LOGGER.info("Cannot create database file");
				}
			}
		} catch (IOException e){
			e.printStackTrace();
		}
		
		config.setJdbcUrl("jdbc:sqlite:database.db");
		config.setConnectionTestQuery("SELECT 1");
		config.addDataSourceProperty("cachePrepStmts", "true");
		config.addDataSourceProperty("prepStmtCacheSize", "250");
		config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
		ds = new HikariDataSource(config);
		String sql = "CREATE TABLE IF NOT EXISTS user_schedules (\n"
                + "	id STRING PRIMARY KEY,\n"
                + "	blk0 STRING VARCHAR(100) NOT NULL DEFAULT \"N/A\",\n"
                + "	blk1 STRING VARCHAR(100) NOT NULL DEFAULT \"N/A\" ,\n"
                + "	blk2 STRING VARCHAR(100) NOT NULL DEFAULT \"N/A\",\n"
                + "	blk3 STRING VARCHAR(100) NOT NULL DEFAULT \"N/A\",\n"
                + "	blk4 STRING VARCHAR(100) NOT NULL DEFAULT \"N/A\",\n"
                + "	blk5 STRING VARCHAR(100) NOT NULL DEFAULT \"N/A\",\n"
                + "	blk6 STRING VARCHAR(100) NOT NULL DEFAULT \"N/A\" \n"
                + ");";
		try (final Statement statement = getConnection().createStatement()) {
			statement.execute(sql);
			System.out.println("table initialized");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private SQLiteDataSource() {	
		
	}
	public static Connection getConnection() throws SQLException {
		return ds.getConnection();
	}
	
}
