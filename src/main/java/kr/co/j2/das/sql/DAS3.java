package kr.co.j2.das.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import kr.co.j2.das.util.DAS3Util;
import org.apache.log4j.Logger;

public class DAS3
{
	private ConnectionFactoryInterface connectionFactory = null;
	private int dbType = -1;

	static Logger logger = Logger.getLogger(DAS3.class);

	public DAS3(String dbName)
	{
		this.connectionFactory = DBCPConnectionFactory.getInstance(dbName);
		this.dbType = connectionFactory.getDbType();
	}

	protected Connection getConnection() throws Exception
	{
		return connectionFactory.getConnection();
	}

	protected void freeConnection(Connection conn)
	{
		connectionFactory.freeConnection(conn);
	}

	public Connection beginTrans() throws Exception
	{
		Connection conn = getConnection();
		conn.setAutoCommit(false);
		return conn;
	}

	public void commit(Connection conn) throws Exception
	{
		conn.commit();
		conn.setAutoCommit(true);
		freeConnection(conn);
	}

	public void rollback(Connection conn)
	{
		try { conn.rollback(); } catch(Exception ex) {}
		try { conn.setAutoCommit(true); } catch(Exception ex) {}
		freeConnection(conn);
	}

	public int execute(Connection conn, String sql, LinkedList dataList) throws Exception
	{
		int count = -1;

		PreparedStatement pstmt = null;
		//LinkedList clobList = new LinkedList();

		boolean isCreated = false;

		try
		{
			if (conn == null)
			{
				isCreated = true;
				conn = getConnection();
			}

			pstmt = conn.prepareStatement(sql);
			setPstmt(pstmt, dataList);

			count = pstmt.executeUpdate();
		} catch(Exception e)
		{
			if (dataList != null) { 
				logger.warn("==========dataList==========:\r\n" + DAS3Util.getDataList(dataList)); 
			}
			
			logger.warn("==========sql==========:\r\n" + sql + "\r\n" + DAS3Util.getSql(sql, dataList));
			logger.warn("", e);
				throw e;
		} finally
		{
			//freeClobList(clobList);
			if (pstmt != null) try { pstmt.close(); } catch(Exception ex) {}
			if (isCreated)	freeConnection(conn);
		}

		return count;
	}

	public int insert(Connection conn, String tableName, LinkedHashMap columnMap) throws Exception
	{
		StringBuffer sb = new StringBuffer();
		StringBuffer sb2 = new StringBuffer();
		LinkedList dataList = new LinkedList();

		sb.append("insert into ");
		sb.append(tableName);
		sb.append(" (");

		Iterator iterator = columnMap.keySet().iterator();

		while (iterator.hasNext())
		{
			Object _obj = iterator.next();

			if (_obj instanceof kr.co.j2.das.sql.Function)
			{
				kr.co.j2.das.sql.Function func = (kr.co.j2.das.sql.Function)_obj;
				String columnName = func.columnName;
				String sentence = func.sentence;
				LinkedList funcList = func.funcList;

				sb.append(columnName);
				sb.append(", ");
				sb2.append(sentence);
				sb2.append(", ");

				if (funcList != null && funcList.size() > 0)
				{
					dataList.addAll(funcList);
				}
			} else
			{
				String _key = _obj.toString();
				int pos = _key.indexOf("+");

				if (pos > 0)
				{
					String columnName = _key.substring(0, pos);
					String columnValue = _key.substring(pos + 1, _key.length());

					sb.append(columnName);
					sb.append(", ");
					sb2.append(columnValue);
					sb2.append(", ");
				} else
				{
					sb.append(_key);
					sb.append(", ");
					sb2.append("?, ");

					Object _value = columnMap.get(_key);
					dataList.add(_value);
				}
			}
		}

		String sql = sb.toString();
		sql = sql.substring(0, sql.length() - 2);

		String sql2 = sb2.toString();
		sql2 = sql2.substring(0, sql2.length() - 2);

		sql = sql + ") values(" + sql2 + ")";
		//logger.debug(sql);

		return execute(conn, sql, dataList);
	}

	public int update(Connection conn, String tableName, LinkedHashMap columnMap, String where, LinkedList whereList) throws Exception
	{
		StringBuffer sb = new StringBuffer();
		LinkedList dataList = new LinkedList();

		sb.append("update ");
		sb.append(tableName);
		sb.append(" set ");

		Iterator iterator = columnMap.keySet().iterator();

		while (iterator.hasNext())
		{
			Object _obj = iterator.next();

			if (_obj instanceof kr.co.j2.das.sql.Function)
			{
				kr.co.j2.das.sql.Function func = (kr.co.j2.das.sql.Function)_obj;
				String columnName = func.columnName;
				String sentence = func.sentence;
				LinkedList funcList = func.funcList;

				sb.append(columnName);
				sb.append(" = ");
				sb.append(sentence);
				sb.append(", ");

				if (funcList != null && funcList.size() > 0)
				{
					dataList.addAll(funcList);
				}
			} else
			{
				String _key = _obj.toString();
				int pos = _key.indexOf("+");

				if (pos > 0)
				{
					String columnName = _key.substring(0, pos);
					String columnValue = _key.substring(pos + 1, _key.length());

					sb.append(columnName);
					sb.append(" = ");
					sb.append(columnValue);
					sb.append(", ");
				} else
				{
					sb.append(_key);
					sb.append(" = ?, ");

					Object _value = columnMap.get(_key);
					dataList.add(_value);
				}
			}
		}

		String sql = sb.toString();
		sql = sql.substring(0, sql.length() - 2);

		if (!DAS3Util.checkString(where).equals(""))
		{
			sql = sql + " " + where;
		}

		//logger.debug(sql);

		if (whereList != null && whereList.size() > 0)
		{
			dataList.addAll(whereList);
		}

		return execute(conn, sql, dataList);
	}

	public int delete(Connection conn, String tableName, String where, LinkedList dataList) throws Exception
	{
		StringBuffer sb = new StringBuffer();

		sb.append("delete from ");
		sb.append(tableName);
		sb.append(" ");

		if (!DAS3Util.checkString(where).equals(""))
		{
			sb.append(where);
		}

		String sql = sb.toString();
		//logger.debug(sql);

		return execute(conn, sql, dataList);
	}

	public long getSequence(Connection conn, String sequenceName) throws Exception
	{
		String sql = "select last_number from user_sequences where sequence_name = ?";
		//logger.debug(sql);

		long value = -1;

		PreparedStatement pstmt = null;
		ResultSet rset = null;

		boolean isCreated = false;

		try
		{
			if (conn == null)
			{
				isCreated = true;
				conn = getConnection();
			}

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, sequenceName.toUpperCase());
			rset = pstmt.executeQuery();

			if (rset.next())
			{
				value = rset.getLong(1);
			}
		} catch(Exception e)
		{
			logger.warn("==========sql==========:\r\n" + sql);
			logger.warn("", e);
			throw e;
		} finally
		{
			if (rset != null) try { rset.close(); } catch(Exception ex) {}
			if (pstmt != null) try { pstmt.close(); } catch(Exception ex) {}
			if (isCreated)	freeConnection(conn);
		}

		return value;
	}

	public java.util.Date getDBTime(Connection conn) throws Exception
	{
		String sql = "select sysdate from dual";

		java.util.Date value = null;

		PreparedStatement pstmt = null;
		ResultSet rset = null;

		boolean isCreated = false;

		try
		{
			if (conn == null)
			{
				isCreated = true;
				conn = getConnection();
			}

			pstmt = conn.prepareStatement(sql);
			rset = pstmt.executeQuery();

			if (rset.next())
			{
				value = rset.getDate(1);
			}
		} catch(Exception e)
		{
			logger.warn("==========sql==========:\r\n" + sql);
			logger.warn("", e);
			throw e;
		} finally
		{
			if (rset != null) try { rset.close(); } catch(Exception ex) {}
			if (pstmt != null) try { pstmt.close(); } catch(Exception ex) {}
			if (isCreated)	freeConnection(conn);
		}

		return value;
	}

	public long getSequence_Nextval(Connection conn, String sequenceName) throws Exception
	{
		String sql = "select " + sequenceName + ".nextval from dual";
		//logger.debug(sql);

		long value = -1;

		PreparedStatement pstmt = null;
		ResultSet rset = null;

		boolean isCreated = false;

		try
		{
			if (conn == null)
			{
				isCreated = true;
				conn = getConnection();
			}

			pstmt = conn.prepareStatement(sql);
			rset = pstmt.executeQuery();

			if (rset.next())
			{
				value = rset.getLong(1);
			}
		} catch(Exception e)
		{
			logger.warn("==========sql==========:\r\n" + sql);
			logger.warn("", e);
			throw e;
		} finally
		{
			if (rset != null) try { rset.close(); } catch(Exception ex) {}
			if (pstmt != null) try { pstmt.close(); } catch(Exception ex) {}
			if (isCreated)	freeConnection(conn);
		}

		return value;
	}

	public int[] batchExecute(Connection conn, String sql, LinkedList dataset) throws Exception
	{
		int count[] = null;

		PreparedStatement pstmt = null;
		//LinkedList clobList = new LinkedList();

		LinkedList dataList = null;

		boolean isCreated = false;

		try
		{
			if (conn == null)
			{
				isCreated = true;
				conn = getConnection();
			}

			pstmt = conn.prepareStatement(sql);

			for (int i = 0, j = dataset.size(); i < j; i++)
			{
				dataList = (LinkedList)dataset.get(i);
				setPstmt(pstmt, dataList);
				pstmt.addBatch();
			}

			count = pstmt.executeBatch();
		} catch(Exception e)
		{
			if (dataList != null) { logger.warn("==========dataList==========:\r\n" + DAS3Util.getDataList(dataList)); }
			logger.warn("==========sql==========:\r\n" + sql + "\r\n" + DAS3Util.getSql(sql, dataList));
			logger.warn("", e);
			throw e;
		} finally
		{
			//freeClobList(clobList);
			if (pstmt != null) try { pstmt.close(); } catch(Exception ex) {}
			if (isCreated)	freeConnection(conn);
		}

		return count;
	}

	public int[] batchInsert(Connection conn, String tableName, LinkedList columnset) throws Exception
	{
		if (columnset == null || columnset.size() == 0)
		{
			return null;
		}

		String sql = "";
		LinkedList dataset = new LinkedList();

		for (int i = 0, j = columnset.size(); i < j; i++)
		{
			LinkedHashMap columnMap = (LinkedHashMap)columnset.get(i);
			LinkedList dataList = new LinkedList();

			if (i == 0)
			{
				StringBuffer sb = new StringBuffer();
				StringBuffer sb2 = new StringBuffer();

				sb.append("insert into ");
				sb.append(tableName);
				sb.append(" (");

				Iterator iterator = columnMap.keySet().iterator();

				while (iterator.hasNext())
				{
					Object _obj = iterator.next();

					if (_obj instanceof kr.co.j2.das.sql.Function)
					{
						kr.co.j2.das.sql.Function func = (kr.co.j2.das.sql.Function)_obj;
						String columnName = func.columnName;
						String sentence = func.sentence;
						LinkedList funcList = func.funcList;

						sb.append(columnName);
						sb.append(", ");
						sb2.append(sentence);
						sb2.append(", ");

						if (funcList != null && funcList.size() > 0)
						{
							dataList.addAll(funcList);
						}
					} else
					{
						String _key = _obj.toString();
						int pos = _key.indexOf("+");

						if (pos > 0)
						{
							String columnName = _key.substring(0, pos);
							String columnValue = _key.substring(pos + 1, _key.length());

							sb.append(columnName);
							sb.append(", ");
							sb2.append(columnValue);
							sb2.append(", ");
						} else
						{
							sb.append(_key);
							sb.append(", ");
							sb2.append("?, ");

							Object _value = columnMap.get(_key);
							dataList.add(_value);
						}
					}
				}

				sql = sb.toString();
				sql = sql.substring(0, sql.length() - 2);

				String sql2 = sb2.toString();
				sql2 = sql2.substring(0, sql2.length() - 2);

				sql = sql + ") values(" + sql2 + ")";
				//logger.debug(sql);
			} else
			{
				Iterator iterator = columnMap.keySet().iterator();

				while (iterator.hasNext())
				{
					Object _obj = iterator.next();

					if (_obj instanceof kr.co.j2.das.sql.Function)
					{
						kr.co.j2.das.sql.Function func = (kr.co.j2.das.sql.Function)_obj;
						String columnName = func.columnName;
						String sentence = func.sentence;
						LinkedList funcList = func.funcList;

						if (funcList != null && funcList.size() > 0)
						{
							dataList.addAll(funcList);
						}
					} else
					{
						String _key = _obj.toString();
						int pos = _key.indexOf("+");

						if (pos == -1)
						{
							Object _value = columnMap.get(_key);
							dataList.add(_value);
						}
					}
				}
			}

			dataset.add(dataList);
		}

		return batchExecute(conn, sql, dataset);
	}

	public int[] batchUpdate(Connection conn, String tableName, LinkedList columnset, String where, LinkedList whereset) throws Exception
	{
		if (columnset == null || columnset.size() == 0)
		{
			return null;
		}

		String sql = "";
		LinkedList dataset = new LinkedList();

		boolean isNull_whereset = true;

		if (whereset != null && whereset.size() > 0)
		{
			isNull_whereset = false;
		}

		for (int i = 0, j = columnset.size(); i < j; i++)
		{
			LinkedHashMap columnMap = (LinkedHashMap)columnset.get(i);
			LinkedList dataList = new LinkedList();

			if (i == 0)
			{
				StringBuffer sb = new StringBuffer();

				sb.append("update ");
				sb.append(tableName);
				sb.append(" set ");

				Iterator iterator = columnMap.keySet().iterator();

				while (iterator.hasNext())
				{
					Object _obj = iterator.next();

					if (_obj instanceof kr.co.j2.das.sql.Function)
					{
						kr.co.j2.das.sql.Function func = (kr.co.j2.das.sql.Function)_obj;
						String columnName = func.columnName;
						String sentence = func.sentence;
						LinkedList funcList = func.funcList;

						sb.append(columnName);
						sb.append(" = ");
						sb.append(sentence);
						sb.append(", ");

						if (funcList != null && funcList.size() > 0)
						{
							dataList.addAll(funcList);
						}
					} else
					{
						String _key = _obj.toString();
						int pos = _key.indexOf("+");

						if (pos > 0)
						{
							String columnName = _key.substring(0, pos);
							String columnValue = _key.substring(pos + 1, _key.length());

							sb.append(columnName);
							sb.append(" = ");
							sb.append(columnValue);
							sb.append(", ");
						} else
						{
							sb.append(_key);
							sb.append(" = ?, ");

							Object _value = columnMap.get(_key);
							dataList.add(_value);
						}
					}
				}

				sql = sb.toString();
				sql = sql.substring(0, sql.length() - 2);

				if (!DAS3Util.checkString(where).equals(""))
				{
					sql = sql + " " + where;
				}

				//logger.debug(sql);
			} else
			{
				Iterator iterator = columnMap.keySet().iterator();

				while (iterator.hasNext())
				{
					Object _obj = iterator.next();

					if (_obj instanceof kr.co.j2.das.sql.Function)
					{
						kr.co.j2.das.sql.Function func = (kr.co.j2.das.sql.Function)_obj;
						String columnName = func.columnName;
						String sentence = func.sentence;
						LinkedList funcList = func.funcList;

						if (funcList != null && funcList.size() > 0)
						{
							dataList.addAll(funcList);
						}
					} else
					{
						String _key = _obj.toString();
						int pos = _key.indexOf("+");

						if (pos == -1)
						{
							Object _value = columnMap.get(_key);
							dataList.add(_value);
						}
					}
				}
			}

			if (!isNull_whereset)
			{
				LinkedList whereList = (LinkedList)whereset.get(i);

				if (whereList != null && whereList.size() > 0)
				{
					dataList.addAll(whereList);
				}
			}

			dataset.add(dataList);
		}

		return batchExecute(conn, sql, dataset);
	}

	public int[] batchDelete(Connection conn, String tableName, String where, LinkedList dataset) throws Exception
	{
		StringBuffer sb = new StringBuffer();

		sb.append("delete from ");
		sb.append(tableName);
		sb.append(" ");

		if (!DAS3Util.checkString(where).equals(""))
		{
			sb.append(where);
		}

		String sql = sb.toString();
		//logger.debug(sql);

		return batchExecute(conn, sql, dataset);
	}

	public LinkedList select(Connection conn, String sql, LinkedList dataList) throws Exception
	{
		LinkedList rowset = null;

		PreparedStatement pstmt = null;
		ResultSet rset = null;
		//LinkedList clobList = new LinkedList();

		boolean isCreated = false;

		try
		{
			if (conn == null)
			{
				isCreated = true;
				conn = getConnection();
			}

			pstmt = conn.prepareStatement(sql);
			setPstmt(pstmt, dataList);

			rset = pstmt.executeQuery();
			rowset = getRowset(rset);
		} catch(Exception e)
		{
			if (dataList != null) { logger.warn("==========dataList==========:\r\n" + DAS3Util.getDataList(dataList)); }
			logger.warn("==========sql==========:\r\n" + sql + "\r\n" + DAS3Util.getSql(sql, dataList));
			logger.warn("", e);
			throw e;
		} finally
		{
			if (rset != null) try { rset.close(); } catch(Exception ex) {}
			//freeClobList(clobList);
			if (pstmt != null) try { pstmt.close(); } catch(Exception ex) {}
			if (isCreated)	freeConnection(conn);
		}

		return rowset;
	}

	public int getCount(Connection conn,
		String table_name,
		String sql2,
		LinkedList dataList) throws Exception
	{
		int totalcount = 0;

		PreparedStatement pstmt = null;
		ResultSet rset = null;
		//LinkedList clobList = new LinkedList();

		String sql = "";

		boolean isCreated = false;

		try
		{
			if (conn == null)
			{
				isCreated = true;
				conn = getConnection();
			}

			sql = "select count(*) from " + table_name;

			if (!DAS3Util.checkString(sql2).equals(""))	sql += " " + sql2;

			pstmt = conn.prepareStatement(sql);
			setPstmt(pstmt, dataList);

			rset = pstmt.executeQuery();

			if (sql2.toLowerCase().contains("group by"))
			{
				while (rset.next())
				{
					totalcount++;
				}
			} else if (rset.next())
			{
				totalcount = rset.getInt(1);
			}
		} catch(Exception e)
		{
			if (dataList != null) { logger.warn("==========dataList==========:\r\n" + DAS3Util.getDataList(dataList)); }
			logger.warn("==========sql==========:\r\n" + sql + "\r\n" + DAS3Util.getSql(sql, dataList));
			logger.warn("", e);
			throw e;
		} finally
		{
			if (rset != null) try { rset.close(); } catch(Exception ex) {}
			//freeClobList(clobList);
			if (pstmt != null) try { pstmt.close(); } catch(Exception ex) {}
			if (isCreated)	freeConnection(conn);
		}

		return totalcount;
	}

	public PageVO pageSelect(Connection conn,
		String table_name,
		String table_fields,
		String table_fields2,
		String sql2,
		String orderBy,
		String reverse,
		int pagemax,
		int nowpage,
		LinkedList dataList) throws Exception
	{
		PageVO pageVO = null;

		PreparedStatement pstmt = null;
		ResultSet rset = null;
		//LinkedList clobList = new LinkedList();

		String sql = "";
		int i = 0;

		boolean isCreated = false;

		try
		{
			if (conn == null)
			{
				isCreated = true;
				conn = getConnection();
			}

			if (table_fields2 == null || table_fields2.equals(""))
			{
				table_fields = table_fields2;
			}

			int totalcount = getCount(conn, table_name, sql2, dataList);

			int totalpage	= DAS3Util.getTotalpage(pagemax, totalcount);
			int startnum	= DAS3Util.getStartnum(pagemax, nowpage);
			int endnum	= DAS3Util.getEndnum(pagemax, totalcount, totalpage, nowpage);

			if (dbType == 1)
			{
				sql = "SELECT " + table_fields + " FROM(SELECT " + table_fields + ", ROWNUM AS RN FROM("
				+ "SELECT " + table_fields2 + " FROM " + table_name;

				if (!DAS3Util.checkString(sql2).equals(""))	sql += " " + sql2;
				if (!DAS3Util.checkString(orderBy).equals(""))	sql += " " + orderBy;

				sql += ") WHERE ROWNUM <= ? ) WHERE RN >= ?";

				pstmt = conn.prepareStatement(sql);
				i = setPstmt(pstmt, dataList);

				pstmt.setInt(++i, endnum);
				pstmt.setInt(++i, startnum);
			} else if (dbType == 0 || dbType == 2)
			{
				if (nowpage == totalpage)
				{
					int rest = totalcount % pagemax;

					if (rest != 0)
					{
						pagemax = rest;
					}
				}

				sql = "select " + table_fields + " from ("
				+ "select top " + pagemax + " " + table_fields + " from ("
				+ "select top " + endnum + " " + table_fields2 + " from " + table_name;

				if (!DAS3Util.checkString(sql2).equals(""))	sql += " " + sql2;
				if (!DAS3Util.checkString(orderBy).equals(""))	sql += " " + orderBy;

				sql += ") a " + reverse;
				sql += ") a " + orderBy;

				pstmt = conn.prepareStatement(sql);
				setPstmt(pstmt, dataList);
			} else if (dbType == 3)
			{
				sql = "SELECT " + table_fields2 + " FROM " + table_name;

				if (!DAS3Util.checkString(sql2).equals(""))	sql += " " + sql2;
				if (!DAS3Util.checkString(orderBy).equals(""))	sql += " " + orderBy;

				sql += " LIMIT ?, ?";

				pstmt = conn.prepareStatement(sql);
				i = setPstmt(pstmt, dataList);

				pstmt.setInt(++i, startnum - 1);
				pstmt.setInt(++i, pagemax);
			}

			rset = pstmt.executeQuery();
			pageVO = new PageVO(totalcount, totalpage, startnum, getRowset(rset));
		} catch(Exception e)
		{
			if (dataList != null) { logger.warn("==========dataList==========:\r\n" + DAS3Util.getDataList(dataList)); }
			logger.warn("==========sql==========:\r\n" + sql + "\r\n" + DAS3Util.getSql(sql, dataList));
			logger.warn("", e);
			throw e;
		} finally
		{
			if (rset != null) try { rset.close(); } catch(Exception ex) {}
			//freeClobList(clobList);
			if (pstmt != null) try { pstmt.close(); } catch(Exception ex) {}
			if (isCreated)	freeConnection(conn);
		}

		return pageVO;
	}

	protected int setPstmt(PreparedStatement pstmt, LinkedList dataList) throws Exception
	{
		int size = 0;

		if (dataList == null || dataList.size() == 0)
		{
			return 0;
		}

		size = dataList.size();
		int index = 0;

		for(int i = 1, j = size; i <= j;  i++)
		{
			index = i - 1;
			Object columnData = dataList.get(index);

			if (columnData == null)
			{
				pstmt.setNull(i, java.sql.Types.NULL);
			} else if (columnData instanceof Integer)
			{
				pstmt.setInt(i, ((Integer)columnData).intValue());
			} else if (columnData instanceof Long)
			{
				pstmt.setLong(i, ((Long)columnData).longValue());
			} else if (columnData instanceof Float)
			{
				pstmt.setFloat(i, ((Float)columnData).floatValue());
			} else if (columnData instanceof Double)
			{
				pstmt.setDouble(i, ((Double)columnData).doubleValue());
			} else if (columnData instanceof java.util.Date)
			{
				pstmt.setTimestamp(i, new Timestamp(((java.util.Date)columnData).getTime()));
			} else
			{
				String strTemp = columnData.toString();

				/*if (strTemp.length() > 2048)
				{
					CLOB tmpClob = CLOB.createTemporary(pstmt.getConnection(), false, oracle.sql.CLOB.DURATION_SESSION);
					clobList.add(tmpClob);

					Writer writer = null;

					try
					{
						writer = tmpClob.getCharacterOutputStream();
						writer.write(strTemp);
						writer.flush();
					} finally
					{
						writer.close();
					}

					pstmt.setClob(i, tmpClob);
				} else*/
				{
					pstmt.setString(i, strTemp);
				}
			}
		}

		return size;
	}

	/*private void freeClobList(LinkedList clobList)
	{
		if (clobList == null || clobList.size() == 0)
		{
			return;
		}

		for (int i = 0, j = clobList.size(); i < j; i++)
		{
			try
			{
				CLOB tmpClob = (CLOB)clobList.get(i);
				tmpClob.freeTemporary();
			} catch(Exception e)
			{
			}
		}
	}*/

	private LinkedList getRowset(ResultSet rset) throws Exception
	{
		LinkedList rowset = new LinkedList();
		ResultSetMetaData metaData = rset.getMetaData();
		int count = metaData.getColumnCount();

		while (rset.next())
		{
			LinkedHashMap row = new LinkedHashMap();

			for (int i = 1; i <= count; i++)
			{
				row.put(((String)metaData.getColumnName(i)).toLowerCase(), DAS3Util.checkString(rset.getString(i)));
			}

			rowset.add(row);
		}

		return rowset;
	}
}
