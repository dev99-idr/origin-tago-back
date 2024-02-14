package kr.co.j2.das.config;

public class DBCP
{
	public int dbType = -1;

	public String driverClassName = "";
	public String url = "";
	public String username = "";
	public String password = "";

	public int minIdle = -1;
	public int maxActive = -1;
	public int maxWait = -1;

	public boolean poolPreparedStatements = false;
	public int maxOpenPreparedStatements = -1;

	public boolean removeAbandoned = false;
	public int removeAbandonedTimeout = -1;
	public boolean logAbandoned = false;

	public String validationQuery = "select 1";
	public boolean testWhileIdle = false;
	public int minEvictableIdleTimeMillis = 1800000;
	public int timeBetweenEvictionRunsMillis = 60000;
}
