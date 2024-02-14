package com.tagoBackend.das;


import kr.co.j2.das.sql.DAS3;

import java.util.Hashtable;

public class DAS3Factory {
    private static Hashtable hDAS3 = new Hashtable();
    private static Object lock = new Object();

    public static DAS3 getInstance(String dbName){
        DAS3 instance = null;
        synchronized (lock){
            instance = (DAS3) hDAS3.get(dbName);
            if(instance == null){
                instance = new DAS3(dbName);
                hDAS3.put(dbName, instance);
            }
        }
        return instance;
    }
}
