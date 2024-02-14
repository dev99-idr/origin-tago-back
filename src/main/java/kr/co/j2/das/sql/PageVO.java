package kr.co.j2.das.sql;

import java.util.LinkedList;

public class PageVO implements java.io.Serializable
{
	public int totalcount = -1;
	public int totalpage = -1;
	public int startnum = -1;
	public LinkedList rowset = null;

	public PageVO(int totalcount, int totalpage, int startnum, LinkedList rowset)
	{
		this.totalcount = totalcount;
		this.totalpage = totalpage;
		this.startnum = startnum;
		this.rowset = rowset;
	}

	public String toString()
	{
		return "totalcount:" + totalcount +
			"\r\ntotalpage:" + totalpage +
			"\r\nstartnum:" + startnum +
			"\r\nrowset:" + rowset;
	}
}
