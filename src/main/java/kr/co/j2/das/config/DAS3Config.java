package kr.co.j2.das.config;

import java.io.File;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

public class DAS3Config
{
	private static String CONFIG_PATH = "";
	public static Hashtable hConfig = new Hashtable();
	public static Properties SYS_PROPS = new Properties();

	static Logger logger = Logger.getLogger(DAS3Config.class);

	static
	{
		SYS_PROPS = System.getProperties();

		CONFIG_PATH = SYS_PROPS.getProperty("DAS3.Config");

		if (CONFIG_PATH == null)
		{
			CONFIG_PATH = DAS3Config.class.getResource("/").getPath() + "conf" + File.separator + "das3.xml";
		}

		logger.debug("CONFIG_PATH:" + CONFIG_PATH);

		try
		{
			init();
		} catch(Exception e)
		{
			logger.warn("CONFIG_PATH:" + CONFIG_PATH, e);
		}
	}

	public static void init() throws Exception
	{
		hConfig.clear();

		Document doc = new SAXBuilder().build(CONFIG_PATH);
		Element root = doc.getRootElement();

		List lDb = root.getChildren("db");
		Iterator iter = lDb.iterator();

		while (iter.hasNext())
		{
			logger.debug("================================");
			DBCP dbcp = new DBCP();

			Element eDb = (Element)iter.next();

			Attribute attrName = eDb.getAttribute("name");
			String dbName = attrName.getValue();
			logger.debug("dbName:" + dbName);

			Attribute attrType = eDb.getAttribute("type");
			int dbType = Integer.parseInt(attrType.getValue().trim());
			logger.debug("dbType:" + dbType);
			dbcp.dbType = dbType;

			Element eDriverClassName = eDb.getChild("driverClassName");
			String driverClassName = eDriverClassName.getText().trim();
			logger.debug("driverClassName:" + driverClassName);
			dbcp.driverClassName = driverClassName;

			Element eUrl = eDb.getChild("url");
			String url = eUrl.getText().trim();
			logger.debug("url:" + url);
			dbcp.url = url;

			Element eUsername = eDb.getChild("username");
			String username = eUsername.getText().trim();
			logger.debug("username:" + username);
			dbcp.username = username;

			Element ePassword = eDb.getChild("password");
			String password = ePassword.getText().trim();
			logger.debug("password:" + password);
			dbcp.password = password;

			Element eMinIdle = eDb.getChild("minIdle");
			int minIdle = Integer.parseInt(eMinIdle.getText().trim());
			logger.debug("minIdle:" + minIdle);
			dbcp.minIdle = minIdle;

			Element eMaxActive = eDb.getChild("maxActive");
			int maxActive = Integer.parseInt(eMaxActive.getText().trim());
			logger.debug("maxActive:" + maxActive);
			dbcp.maxActive = maxActive;

			Element eMaxWait = eDb.getChild("maxWait");
			int maxWait = Integer.parseInt(eMaxWait.getText().trim());
			logger.debug("maxWait:" + maxWait);
			dbcp.maxWait = maxWait;

			Element ePoolPreparedStatements = eDb.getChild("poolPreparedStatements");
			boolean poolPreparedStatements = Boolean.parseBoolean(ePoolPreparedStatements.getText().trim());
			logger.debug("poolPreparedStatements:" + poolPreparedStatements);
			dbcp.poolPreparedStatements = poolPreparedStatements;

			Element eMaxOpenPreparedStatements = eDb.getChild("maxOpenPreparedStatements");
			int maxOpenPreparedStatements = Integer.parseInt(eMaxOpenPreparedStatements.getText().trim());
			logger.debug("maxOpenPreparedStatements:" + maxOpenPreparedStatements);
			dbcp.maxOpenPreparedStatements = maxOpenPreparedStatements;

			Element eRemoveAbandoned = eDb.getChild("removeAbandoned");
			boolean removeAbandoned = Boolean.parseBoolean(eRemoveAbandoned.getText().trim());
			logger.debug("removeAbandoned:" + removeAbandoned);
			dbcp.removeAbandoned = removeAbandoned;

			Element eRemoveAbandonedTimeout = eDb.getChild("removeAbandonedTimeout");
			int removeAbandonedTimeout = Integer.parseInt(eRemoveAbandonedTimeout.getText().trim());
			logger.debug("removeAbandonedTimeout:" + removeAbandonedTimeout);
			dbcp.removeAbandonedTimeout = removeAbandonedTimeout;

			Element eLogAbandoned = eDb.getChild("logAbandoned");
			boolean logAbandoned = Boolean.parseBoolean(eLogAbandoned.getText().trim());
			logger.debug("logAbandoned:" + logAbandoned);
			dbcp.logAbandoned = logAbandoned;

			Element eValidationQuery = eDb.getChild("validationQuery");
			String validationQuery = eValidationQuery.getText().trim();
			logger.debug("validationQuery:" + validationQuery);
			dbcp.validationQuery = validationQuery;

			Element eTestWhileIdle = eDb.getChild("testWhileIdle");
			boolean testWhileIdle = Boolean.parseBoolean(eTestWhileIdle.getText().trim());
			logger.debug("testWhileIdle:" + testWhileIdle);
			dbcp.testWhileIdle = testWhileIdle;

			Element eMinEvictableIdleTimeMillis = eDb.getChild("minEvictableIdleTimeMillis");
			int minEvictableIdleTimeMillis = Integer.parseInt(eMinEvictableIdleTimeMillis.getText().trim());
			logger.debug("minEvictableIdleTimeMillis:" + minEvictableIdleTimeMillis);
			dbcp.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;

			Element eTimeBetweenEvictionRunsMillis = eDb.getChild("timeBetweenEvictionRunsMillis");
			int timeBetweenEvictionRunsMillis = Integer.parseInt(eTimeBetweenEvictionRunsMillis.getText().trim());
			logger.debug("timeBetweenEvictionRunsMillis:" + timeBetweenEvictionRunsMillis);
			dbcp.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;

			hConfig.put(dbName, dbcp);
		}
	}

	public String toString()
	{
		return CONFIG_PATH + "\n" + hConfig.toString();
	}
}
