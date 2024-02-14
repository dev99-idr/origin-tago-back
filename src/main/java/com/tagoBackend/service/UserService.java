package com.tagoBackend.service;

import kr.co.j2.das.sql.DAS3;

import java.sql.Connection;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class UserService {
    DAS3 db = new DAS3("tago");

    public LinkedHashMap getUserInfo(String user_id) {
        try {
            String sql = "select * from tbl_user_info where user_id = ?";

            LinkedList list = new LinkedList();
            list.add(user_id);

            LinkedList resultList = db.select(null, sql, list);

            if (resultList.size() < 1) throw new Exception();

            return (LinkedHashMap) resultList.getFirst();
        } catch (Exception e) {
            return null;
        }
    }

    public LinkedList getAllUserInfo(String start, String end) {
        try {
            String sql = "select * from tbl_user_info limit ?, ?";

            int startInt = Integer.valueOf(start);
            int endInt = Integer.valueOf(end);

            LinkedList list = new LinkedList();
            list.add(startInt);
            list.add(endInt);

            LinkedList resultList = db.select(null, sql, list);

            if (resultList.size() < 1) throw new Exception();

            return resultList;
        } catch (Exception e) {
            return null;
        }
    }

    public int setUserInfo(String user_id, String user_name, String user_pw, String user_email){
        try{
            LinkedHashMap columnMap = new LinkedHashMap();
            columnMap.put("user_name", user_name);
            columnMap.put("user_pw", user_pw);
            columnMap.put("user_email", user_email);

            String sql = "where user_id = ?";

            LinkedList list = new LinkedList();
            list.add(user_id);

            int result = db.update(null, "tbl_user_info", columnMap, sql, list);

            if(result < 1) return 0;

            return 1;
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }

    public int deleteUserInfo(String user_id){
        try{
            LinkedHashMap columnMap = new LinkedHashMap();
            columnMap.put("del_yn", "Y");

            String sql = "where user_id = ?";

            LinkedList list = new LinkedList();
            list.add(user_id);

            int result = db.update(null, "tbl_user_info", columnMap, sql, list);

            if(result < 1) return 0;

            return 1;
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }

    public int getCountUser() {
        try{
            LinkedList list = new LinkedList();

            String sql = "";
            sql += "tbl_user_info";

            int resultList = db.getCount(null, sql, "", list);

            if(resultList < 1) throw new Exception();

            return resultList;
        }catch (Exception e){
            return -1;
        }
    }

//    public int setUserAuth(String idx, String auth){
//        try{
//            LinkedHashMap columnMap = new LinkedHashMap();
//
//            int authority = 0;
//            switch (auth){
//                case "최고 관리자" : authority = 4;
//                    break;
//                case "IT 관리자" : authority =3;
//                    break;
//                case "일반 관리자" : authority =2;
//                    break;
//                default: authority = 1;
//                    break;
//            }
//
//            columnMap.put("user_authority", authority);
//
//            String sql = "where idx in ("+idx+")";
//
//            LinkedList list = new LinkedList();
//
//            int result = db.update(null, "tbl_user_info", columnMap, sql, list);
//
//            if(result < 1) return 0;
//
//            return 1;
//        }catch (Exception e){
//            e.printStackTrace();
//            return -1;
//        }
//    }

}
