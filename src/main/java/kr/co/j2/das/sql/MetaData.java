package kr.co.j2.das.sql;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.Hashtable;
import java.util.LinkedList;
import kr.co.j2.das.config.DAS3Config;
import kr.co.j2.das.config.DBCP;
import org.apache.log4j.Logger;

public class MetaData
{
	private static Hashtable hMetaData = new Hashtable();
	private static Object lock = new Object();

	private String schemaPattern = "";
	private ConnectionFactoryInterface connectionFactory = null;
	private Hashtable hDateTable = new Hashtable();

	static Logger logger = Logger.getLogger(MetaData.class);

	public static MetaData getInstance(String dbName) throws Exception
	{
		MetaData metaData = null;

		synchronized (lock)
		{
			metaData = (MetaData)hMetaData.get(dbName);

			if (metaData == null)
			{
				metaData = new MetaData(dbName);
				hMetaData.put(dbName, metaData);
			}
		}

		return metaData;
	}

	private MetaData(String dbName) throws Exception
	{
		DBCP dbcp = (DBCP)DAS3Config.hConfig.get(dbName);
		schemaPattern = dbcp.username.toUpperCase();

		connectionFactory = DBCPConnectionFactory.getInstance(dbName);

		reload();
	}

	public LinkedList getDateColumn(String tableName)
	{
		return (LinkedList)hDateTable.get(tableName);
	}

	public boolean isDateColumn(String tableName, String columnName)
	{
		tableName = tableName.toUpperCase();
		columnName = columnName.toUpperCase();

		LinkedList vDateColumn = getDateColumn(tableName);
		return vDateColumn.contains(columnName);
	}

	public void reload() throws Exception
	{
		hDateTable.clear();

		Connection conn = null;
		DatabaseMetaData dbmd = null;
		ResultSet rsTable = null;
		ResultSet rsColumn = null;

		try
		{
			conn = connectionFactory.getConnection();
			dbmd = conn.getMetaData();

			String catalog = null;
			//String schemaPattern = username.toUpperCase();
			String tableNamePattern = "%";
			String[] types = {"TABLE", "VIEW"};

			rsTable = dbmd.getTables(catalog, schemaPattern, tableNamePattern, types);

			while (rsTable.next())
			{
				String tableName = rsTable.getString(3);

				if (tableName.indexOf("$") > -1)
				{
					continue;
				}

				logger.debug(tableName + "................");

				LinkedList vDateColumn = new LinkedList();
				hDateTable.put(tableName, vDateColumn);

				rsColumn = dbmd.getColumns(catalog, schemaPattern, tableName, "%");

				while (rsColumn.next())
				{
					String columnName = rsColumn.getString(4);
					String columnType = rsColumn.getString(6);
					String columnSize = rsColumn.getString(7);

					//logger.debug("columnName:" + columnName);
					//logger.debug("columnType:" + columnType);
					//logger.debug("columnSize:" + columnSize);

					vDateColumn.add(columnName);

					if (columnType.equals("DATE"))
					{
						logger.debug("columnName:" + columnName);
						logger.debug("columnType:" + columnType);
					}
				}

				rsColumn.close();
			}
		} catch(Exception e)
		{
			throw e;
		} finally
		{
			connectionFactory.freeConnection(conn);
			try { rsColumn.close(); } catch(Exception e) {}
			try { rsTable.close(); } catch(Exception e) {}
		}
	}

	public static void main(String[] args) throws Exception
	{
		MetaData metaData = MetaData.getInstance("EN");
		System.out.println(metaData.isDateColumn("WRITING_PAY_TRX", "DEL_DT"));
	}
}
