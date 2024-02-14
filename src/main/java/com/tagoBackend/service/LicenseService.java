package com.tagoBackend.service;

import kr.co.j2.das.sql.DAS3;

import java.sql.Connection;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class LicenseService {
    DAS3 db = new DAS3("tago");

    public LinkedHashMap getLicense(String mac_address) {
        try {
            String sql = "select * from tbl_license where mac_address = ?";

            LinkedList list = new LinkedList();
            list.add(mac_address);
            LinkedList resultList = db.select(null, sql, list);

            if (resultList.size() < 1) throw new Exception();

            return (LinkedHashMap) resultList.getFirst();
        } catch (Exception e) {
            return null;
        }
    }

    public LinkedHashMap licenseList() {
        try {
            String sql = "select * from tbl_license";

            LinkedList list = new LinkedList();
            LinkedList resultList = db.select(null, sql, list);

            if (resultList.size() < 1) throw new Exception();

            return (LinkedHashMap) resultList.getFirst();
        } catch (Exception e) {
            return null;
        }
    }

    public int setLicense(String mac_address, String license_key, String active_time){
        try{
            LinkedHashMap columnMap = new LinkedHashMap();
            columnMap.put("active_yn", "Y");
            columnMap.put("active_time", active_time);

            String sql = "where license_key = ? and mac_address = ?";

            LinkedList list = new LinkedList();
            list.add(license_key);
            list.add(mac_address);

            int result = db.update(null, "tbl_license", columnMap, sql, list);

            if(result < 1) return 0;

            return 1;
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }


    public int insertLicense(String license_key, String mac_address, String active_yn){
        Connection conn = null;

        try{
            conn = db.beginTrans();

            LinkedHashMap map = new LinkedHashMap();
            map.put("license_key", license_key);
            map.put("mac_address", mac_address);
            map.put("active_yn", active_yn);
            int count = db.insert(conn, "tbl_license", map);

            if(count < 1) throw new Exception();

            db.commit(conn);
            return 1;
        }catch (Exception e){
            db.rollback(conn);
            e.printStackTrace();
            return -1;
        }
    }

}
