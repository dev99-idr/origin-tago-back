// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DAS3Config.java

package kr.co.j2.das.config;

import java.io.File;
import java.net.URL;
import java.util.*;
import org.apache.log4j.Logger;
import org.jdom.*;
import org.jdom.input.SAXBuilder;

// Referenced classes of package kr.co.j2.das.config:
//            DBCP

public class DAS3Config
{

    public DAS3Config()
    {
    }

    public static void init()
        throws Exception
    {
        hConfig.clear();
        Document document = (new SAXBuilder()).build(CONFIG_PATH);
        Element element = document.getRootElement();
        List list = element.getChildren("db");
        DBCP dbcp;
        String s;
        for(Iterator iterator = list.iterator(); iterator.hasNext(); hConfig.put(s, dbcp))
        {
            logger.debug("================================");
            dbcp = new DBCP();
            Element element1 = (Element)iterator.next();
            Attribute attribute = element1.getAttribute("name");
            s = attribute.getValue();
            logger.debug((new StringBuilder()).append("dbName:").append(s).toString());
            Attribute attribute1 = element1.getAttribute("type");
            int i = Integer.parseInt(attribute1.getValue().trim());
            logger.debug((new StringBuilder()).append("dbType:").append(i).toString());
            dbcp.dbType = i;
            Element element2 = element1.getChild("driverClassName");
            String s1 = element2.getText().trim();
            logger.debug((new StringBuilder()).append("driverClassName:").append(s1).toString());
            dbcp.driverClassName = s1;
            Element element3 = element1.getChild("url");
            String s2 = element3.getText().trim();
            logger.debug((new StringBuilder()).append("url:").append(s2).toString());
            dbcp.url = s2;
            Element element4 = element1.getChild("username");
            String s3 = element4.getText().trim();
            logger.debug((new StringBuilder()).append("username:").append(s3).toString());
            dbcp.username = s3;
            Element element5 = element1.getChild("password");
            String s4 = element5.getText().trim();
            logger.debug((new StringBuilder()).append("password:").append(s4).toString());
            dbcp.password = s4;
            Element element6 = element1.getChild("minIdle");
            int j = Integer.parseInt(element6.getText().trim());
            logger.debug((new StringBuilder()).append("minIdle:").append(j).toString());
            dbcp.minIdle = j;
            Element element7 = element1.getChild("maxActive");
            int k = Integer.parseInt(element7.getText().trim());
            logger.debug((new StringBuilder()).append("maxActive:").append(k).toString());
            dbcp.maxActive = k;
            Element element8 = element1.getChild("maxWait");
            int l = Integer.parseInt(element8.getText().trim());
            logger.debug((new StringBuilder()).append("maxWait:").append(l).toString());
            dbcp.maxWait = l;
            Element element9 = element1.getChild("poolPreparedStatements");
            boolean flag = Boolean.parseBoolean(element9.getText().trim());
            logger.debug((new StringBuilder()).append("poolPreparedStatements:").append(flag).toString());
            dbcp.poolPreparedStatements = flag;
            Element element10 = element1.getChild("maxOpenPreparedStatements");
            int i1 = Integer.parseInt(element10.getText().trim());
            logger.debug((new StringBuilder()).append("maxOpenPreparedStatements:").append(i1).toString());
            dbcp.maxOpenPreparedStatements = i1;
            Element element11 = element1.getChild("removeAbandoned");
            boolean flag1 = Boolean.parseBoolean(element11.getText().trim());
            logger.debug((new StringBuilder()).append("removeAbandoned:").append(flag1).toString());
            dbcp.removeAbandoned = flag1;
            Element element12 = element1.getChild("removeAbandonedTimeout");
            int j1 = Integer.parseInt(element12.getText().trim());
            logger.debug((new StringBuilder()).append("removeAbandonedTimeout:").append(j1).toString());
            dbcp.removeAbandonedTimeout = j1;
            Element element13 = element1.getChild("logAbandoned");
            boolean flag2 = Boolean.parseBoolean(element13.getText().trim());
            logger.debug((new StringBuilder()).append("logAbandoned:").append(flag2).toString());
            dbcp.logAbandoned = flag2;
            Element element14 = element1.getChild("validationQuery");
            String s5 = element14.getText().trim();
            logger.debug((new StringBuilder()).append("validationQuery:").append(s5).toString());
            dbcp.validationQuery = s5;
            Element element15 = element1.getChild("testWhileIdle");
            boolean flag3 = Boolean.parseBoolean(element15.getText().trim());
            logger.debug((new StringBuilder()).append("testWhileIdle:").append(flag3).toString());
            dbcp.testWhileIdle = flag3;
            Element element16 = element1.getChild("minEvictableIdleTimeMillis");
            int k1 = Integer.parseInt(element16.getText().trim());
            logger.debug((new StringBuilder()).append("minEvictableIdleTimeMillis:").append(k1).toString());
            dbcp.minEvictableIdleTimeMillis = k1;
            Element element17 = element1.getChild("timeBetweenEvictionRunsMillis");
            int l1 = Integer.parseInt(element17.getText().trim());
            logger.debug((new StringBuilder()).append("timeBetweenEvictionRunsMillis:").append(l1).toString());
            dbcp.timeBetweenEvictionRunsMillis = l1;
        }

    }

    public String toString()
    {
        return (new StringBuilder()).append(CONFIG_PATH).append("\n").append(hConfig.toString()).toString();
    }

    private static String CONFIG_PATH;
    public static Hashtable hConfig = new Hashtable();
    public static Properties SYS_PROPS;
    static Logger logger;

    static 
    {
        CONFIG_PATH = "";
        SYS_PROPS = new Properties();
        logger = Logger.getLogger("kr/co/j2/das/config/DAS3Config");
        SYS_PROPS = System.getProperties();
        CONFIG_PATH = SYS_PROPS.getProperty("DAS3.Config");
        if(CONFIG_PATH == null)
            CONFIG_PATH = (new StringBuilder()).append(("kr/co/j2/das/config/DAS3Config").getClass().getResource("/").getPath()).append("src").append(File.separator).append("main").append(File.separator).append("java").append(File.separator).append("conf").append(File.separator).append("das3.xml").toString();
        logger.debug((new StringBuilder()).append("CONFIG_PATH:").append(CONFIG_PATH).toString());
        try
        {
            init();
        }
        catch(Exception exception)
        {
            logger.warn((new StringBuilder()).append("CONFIG_PATH:").append(CONFIG_PATH).toString(), exception);
        }
    }
}
