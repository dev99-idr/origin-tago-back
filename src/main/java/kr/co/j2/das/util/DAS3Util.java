package kr.co.j2.das.util;

import java.util.LinkedList;
import java.util.StringTokenizer;
import org.apache.log4j.Logger;

public class DAS3Util
{
	static Logger logger = Logger.getLogger(DAS3Util.class);

	public static String checkString(String str,String tmp)
	{
		if (!(str == null || str.trim().equals("")|| str.trim().equals("null")))
			return (String)str.trim().toString();
		else	return tmp;
	}

	public static String checkString(String str)
	{
		return checkString(str, "");
	}

	public static String getDataList(LinkedList dataList)
	{
		StringBuffer sb = new StringBuffer();

		for (int i = 0, j = dataList.size(); i < j; i++)
		{
			Object columnData = dataList.get(i);
			sb.append(i);
			sb.append(":");
			sb.append(columnData.toString());
			sb.append("(");
			sb.append(columnData.getClass().toString());
			sb.append(")\r\n");
		}

		String strData = sb.toString();
		int str_length = strData.length();

		if (str_length > 2)
		{
			strData = strData.substring(0, strData.length() - 2);
		}

		return strData;
	}

	public static String getSql(String sql, LinkedList dataList)
	{
		sql = checkString(sql);

		if (sql.equals(""))
		{
			return "";
		}

		StringBuffer sb = new StringBuffer();

		StringTokenizer tok = new StringTokenizer(sql, "?");

		if (!tok.hasMoreElements() || dataList == null)
		{
			return sql;
		}

		try
		{
			for(int i = 0; tok.hasMoreElements() && i < dataList.size(); i++)
			{
				Object columnData = dataList.get(i);

				try
				{
					if (columnData instanceof Integer
						|| columnData instanceof Long
						|| columnData instanceof Double)
					{
						sb.append(tok.nextToken());
						sb.append(columnData.toString());
					} else
					{
						sb.append(tok.nextToken());
						sb.append("'");
						sb.append(columnData.toString());
						sb.append("'");
					}
				} catch(Exception ex) {}
			}

			if (tok.hasMoreElements())
			{
				sb.append(tok.nextToken());
			}
		} catch(Exception e)
		{
			logger.warn("", e);
		}

		return sb.toString();
	}

	public static int getTotalpage(int pagemax, int totalcount)
	{
		int totalpage;

		if (totalcount >= pagemax)
		{
			int div = totalcount / pagemax;
			int mod = totalcount % pagemax;

			if (mod != 0)
			{
				totalpage = div + 1;
			} else
			{
				totalpage = div;
			}
		} else
		{
			totalpage = 1;
		}

		return totalpage;
	}

	public static int getStartnum(int pagemax, int nowpage)
	{
		return (nowpage - 1) * pagemax + 1;
	}

	public static int getEndnum(int pagemax, int totalcount, int totalpage, int nowpage)
	{
		int endnum = pagemax * nowpage;

		if (totalpage == nowpage)
		{
			endnum = totalcount;
		}

		return endnum;
	}
}
