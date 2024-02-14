package com.tagoBackend.service;

import kr.co.j2.das.sql.DAS3;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class TagLocationService {
    DAS3 db = new DAS3("tago");

    public LinkedList getLocationInfo() {
        try {
            String sql = "select * from tbl_location_info where del_yn = 'N'";

            LinkedList resultList = db.select(null, sql, null);

            if (resultList.size() < 1) throw new Exception();

            return resultList;
        } catch (Exception e) {
            return null;
        }
    }

    public int setLocationInfo(String key, String newLocationName){
        try{
            LinkedHashMap columnMap = new LinkedHashMap();
            columnMap.put("location_name", newLocationName);

            String sql = "where idx = ?";

            LinkedList list = new LinkedList();
            list.add(key);

            int result = db.update(null, "tbl_location_info", columnMap, sql, list);

            if(result < 1) return 0;

            return 1;
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }

    public int insertLocationInfo(String parent_rawid, String location_name, String depth) {
        Connection conn = null;

        try{
            conn = db.beginTrans();

            LinkedHashMap map = new LinkedHashMap();
            map.put("parent_rawid", parent_rawid);
            map.put("location_name", location_name);
            map.put("depth", depth);

            int count = db.insert(conn, "tbl_location_info", map);

            if(count < 1) throw new Exception();

            db.commit(conn);

            return 1;
        }catch (Exception e){
            db.rollback(conn);
            e.printStackTrace();
            return -1;
        }
    }

    public int deleteLocationInfo(String idx, ArrayList<Integer> parentArray){
        try{
            LinkedHashMap columnMap = new LinkedHashMap();
            columnMap.put("del_yn", "Y");

            String sql = "where idx IN(";

            for(int i = 0; i < parentArray.size(); i++) {
                if(i != parentArray.size() - 1) {
                    sql += parentArray.get(i) + ",";
                }
                else{
                    sql += parentArray.get(i);
                }
            }

            sql += ");";

            int result = db.update(null, "tbl_location_info", columnMap, sql, null);

            if(result < 1) return 0;

            return 1;
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }

    public LinkedList getTagInfo(String start, String end, String searchCriteria, ArrayList searchTextArray, ArrayList parentArray) {
        try {
            int startInt = Integer.valueOf(start);
            int endInt = Integer.valueOf(end);

            String sql = "SELECT cl.location_name, c.* from tbl_smart_tag_info c INNER JOIN tbl_location_info cl ON c.tag_location = cl.idx WHERE c.del_yn ='N' ";
            LinkedList list = new LinkedList();
            list.add(startInt);
            list.add(endInt);

            if(searchCriteria.equals("tag_location")) {
                sql += "and c.tag_location = (select idx from tbl_location_info where del_yn = 'N' and location_name like '%" + searchTextArray.get(0) + "%') ";
            }
            else if(searchCriteria.equals("tag_name")) {
                sql += "and c." + searchCriteria + " Like '%"+searchTextArray.get(0) + "%' ";
            }

            sql += "and c.tag_location IN(";

            for(int i = 0; i < parentArray.size(); i++) {
                if(i < parentArray.size() - 1) {
                    sql += parentArray.get(i) + ",";
                }
                else {
                    sql += parentArray.get(i) + ") ";
                }
            }

            sql += "limit ?, ? ";

            LinkedList resultList = db.select(null, sql, list);

            if (resultList.size() < 1) throw new Exception();

            return resultList;
        } catch (Exception e) {
            return null;
        }
    }

    public int countLocation(String searchCriteria, ArrayList<Integer> parentArray, ArrayList<String> searchTextArray){
        try{
            LinkedList list = new LinkedList();

            String sql = "";
            sql += "tbl_smart_tag_info";
            sql += " WHERE del_yn = 'N' ";

            if(searchCriteria.equals("tag_location")) {
                sql += "and tag_location = (select idx from tbl_location_info where del_yn = 'N' and location_name like '%" + searchTextArray.get(0) + "%') ";
            }
            else if(searchCriteria.equals("tag_name")) {
                sql += "and " + searchCriteria + " Like '%"+searchTextArray.get(0) + "%' ";
            }

            sql += "and tag_location IN(";

            for(int i = 0; i < parentArray.size(); i++) {
                if(i < parentArray.size() - 1) {
                    sql += parentArray.get(i) + ",";
                }
                else {
                    sql += parentArray.get(i) + ") ";
                }
            }

            int resultList = db.getCount(null, sql, "", list);

            if(resultList < 1) throw new Exception();

            return resultList;
        } catch (Exception e){
            return -1;
        }
    }

}
