package kr.co.j2.das.sql;

import java.util.LinkedList;

public class Function
{
	public String columnName = "";
	public String sentence = "";
	public LinkedList funcList = null;

	public Function()
	{
	}

	public Function(String columnName, String sentence, LinkedList funcList)
	{
		this.columnName = columnName;
		this.sentence = sentence;
		this.funcList = funcList;
	}
}
