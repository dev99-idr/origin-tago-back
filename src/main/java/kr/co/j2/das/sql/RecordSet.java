package kr.co.j2.das.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;
import kr.co.j2.das.util.DAS3Util;
import org.apache.log4j.Logger;

public class RecordSet
{
	PreparedStatement pstmt = null;
	ResultSet rset = null;

	static Logger logger = Logger.getLogger(RecordSet.class);

	public RecordSet(PreparedStatement pstmt, ResultSet rset) throws Exception
	{
		this.pstmt = pstmt;
		this.rset = rset;
	}

	public void close()
	{
		if (rset != null) try { rset.close(); } catch(Exception ex) {}
		if (pstmt != null) try { pstmt.close(); } catch(Exception ex) {}
	}

	public boolean next() throws Exception
	{
		try
		{
			return rset.next();
		} catch(Exception e)
		{
			logger.warn("", e);
			throw e;
		}
	}

	public int getInt(String name) throws Exception
	{
		try
		{
			return rset.getInt(name);
		} catch(Exception e)
		{
			logger.warn("name:" + name, e);
			throw e;
		}
	}

	public long getLong(String name) throws Exception
	{
		try
		{
			return rset.getLong(name);
		} catch(Exception e)
		{
			logger.warn("name:" + name, e);
			throw e;
		}
	}

	public float getFloat(String name) throws Exception
	{
		try
		{
			return rset.getFloat(name);
		} catch(Exception e)
		{
			logger.warn("name:" + name, e);
			throw e;
		}
	}

	public double getDouble(String name) throws Exception
	{
		try
		{
			return rset.getDouble(name);
		} catch(Exception e)
		{
			logger.warn("name:" + name, e);
			throw e;
		}
	}

	public java.sql.Timestamp getTimeStamp(String name) throws Exception
	{
		try
		{
			return rset.getTimestamp(name);
		} catch(Exception e)
		{
			logger.warn("name:" + name, e);
			throw e;
		}
	}

	public java.util.Date getDate(String name) throws Exception
	{
		try
		{
			return rset.getDate(name);
		} catch(Exception e)
		{
			logger.warn("name:" + name, e);
			throw e;
		}
	}

	public String getString(String name) throws Exception
	{
		try
		{
			return DAS3Util.checkString(rset.getString(name));
		} catch(Exception e)
		{
			logger.warn("name:" + name, e);
			throw e;
		}
	}

	/*public String getClob(String name) throws Exception
	{
		String clobString = "";

		try
		{
			Clob clobValue = (Clob)rset.getClob(name);
			clobString = clobValue.getSubString(1L, (int)clobValue.length());
		} catch(Exception e)
		{
			logger.warn("name:" + name, e);
			throw e;
		}

		return clobString;
	}*/
}
