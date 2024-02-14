package com.tagoBackend.service;

import kr.co.j2.das.sql.DAS3;

import java.sql.Connection;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class LoginService {
    DAS3 db = new DAS3("tago");

    public LinkedHashMap signin(String id, String pw) {
        try {
            String sql = "select * from tbl_user_info where user_id = ? and user_pw = ? and del_yn = 'N'";

            LinkedList list = new LinkedList();
            list.add(id);
            list.add(pw);

            LinkedList resultList = db.select(null, sql, list);

            if (resultList.size() < 1) throw new Exception();

            return (LinkedHashMap) resultList.getFirst();
        } catch (Exception e) {
            return null;
        }
    }

    public int signup(String user_id, String user_pw, String user_authority){
        Connection conn = null;

        try{
            conn = db.beginTrans();

            LinkedHashMap map = new LinkedHashMap();
            map.put("user_id", user_id);
            map.put("user_pw", user_pw);
            map.put("user_authority", user_authority);

            int count = db.insert(conn, "tbl_user_info", map);

            if(count < 1) throw new Exception();

            db.commit(conn);

            return 1;
        }catch (Exception e){
            db.rollback(conn);
            e.printStackTrace();
            return -1;
        }
    }

    public LinkedHashMap getUser(String user_id) {
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

    public LinkedList getLoginLog(String user_id, String user_authority, String sortByTime) {
        try {
            String sql = "";

            LinkedList list = new LinkedList();

            if(Integer.parseInt(user_authority) == 2) {
                sql = "select * from tbl_login_log_info";
            }
            else {
                sql = "select * from tbl_login_log_info where user_id = ?";
                list.add(user_id);
            }

            if(sortByTime.equals("DESC")) {
                sql += " order by login_time DESC";
            }
            else {
                sql += " order by login_time ASC";
            }

            LinkedList resultList = db.select(null, sql, list);

            if (resultList.size() < 1) throw new Exception();

            return resultList;
        } catch (Exception e) {
            return null;
        }
    }

    public int loginLog(String user_id, String user_name, String user_email, String user_authority, String login_time, String login_yn) {
        Connection conn = null;

        try{
            conn = db.beginTrans();

            LinkedHashMap map = new LinkedHashMap();
            map.put("user_id", user_id);
            map.put("user_name", user_name);
            map.put("user_email", user_email);
            map.put("user_authority", user_authority);
            map.put("login_time", login_time);
            map.put("login_yn", login_yn);

            int count = db.insert(conn, "tbl_login_log_info", map);

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

//
//    public LinkedHashMap autoSignIn(String id){
//        try{
//
//            String sql = "SELECT tuser.idx, tuser.user_type, tuser.user_id, tuser.user_name, tuser.user_tel, tuser.user_mail, tuser.start_hour, tuser.start_min, tuser.end_hour, tuser.end_min, tuser.user_auth, dept.dept_name FROM tbl_user tuser INNER JOIN tbl_dept dept ON tuser.dept_idx = dept.idx WHERE tuser.del_yn = 'N' AND tuser.user_id = ?;";
//
//            LinkedList list = new LinkedList();
//            list.add(id);
//
//            LinkedList resultList = db.select(null, sql, list);
//
//            if(resultList.size() < 1) throw new Exception();
//
//            return (LinkedHashMap)resultList.getFirst();
//        }catch (Exception e){
//            return null;
//        }
//    }
//
//    public int userInsert(String id, String pw, String name, String tel, String mail, String dept, String start_hour, String start_min, String end_hour, String end_min){
//        Connection conn = null;
//
//        try{
//            conn = db.beginTrans();
//
//            Long unixTime = System.currentTimeMillis();
//
//            LinkedHashMap map = new LinkedHashMap();
//            // map.put("del_yn", "N"); Default Value
//            map.put("user_type", 'U');
//            map.put("user_id", id);
//            map.put("user_passwd", pw);
//            map.put("user_name", name);
//            map.put("user_tel", tel);
//            map.put("user_mail", mail);
//            map.put("dept_idx", dept);
//            map.put("start_hour", start_hour);
//            map.put("start_min", start_min);
//            map.put("end_hour", end_hour);
//            map.put("end_min", end_min);
//            map.put("create_dt", unixTime);
//            map.put("create_by", "user");
//            map.put("update_dt", unixTime);
//            map.put("update_by", "user");
//
//            int count = db.insert(conn, "tbl_user", map);
//
//            if(count < 1) throw new Exception();
//
//            db.commit(conn);
//            return 1;
//        }catch (Exception e){
//            db.rollback(conn);
//            e.printStackTrace();
//            return -1;
//        }
//    }
//
//    public LinkedHashMap findDataById(String id){
//        try{
//            String sql = "select * from tbl_user where user_id = ?";
//
//            LinkedList list = new LinkedList();
//            list.add(id);
//
//            LinkedList resultList = db.select(null, sql, list);
//
//            if(resultList.size() < 1) throw new Exception();
//
//            return (LinkedHashMap) resultList.getFirst();
//        }catch (Exception e){
//            return null;
//        }
//    }
//
//    public int idIsExist(String id){
//        try{
//            String sql = "select * from tbl_user where user_id = ?";
//
//            LinkedList list = new LinkedList();
//            list.add(id);
//
//            LinkedList resultList = db.select(null, sql, list);
//
//            if(resultList.size() > 0){
//                return 1;
//            }else{
//                return 0;
//            }
//        }catch (Exception e){
//            return -1;
//        }
//    }
//
//    public String findIdByNameAndMail(String name, String mail){
//        try{
//            String sql = "select user_id from tbl_user where user_name = ? and user_mail = ?";
//
//            LinkedList list = new LinkedList();
//            list.add(name);
//            list.add(mail);
//
//            LinkedList resultList = db.select(null, sql, list);
//
//            if(resultList.size() < 1) throw new Exception();
//
//            LinkedHashMap result = (LinkedHashMap) resultList.getFirst();
//
//            return result.get("user_id").toString();
//        }catch (Exception e){
//            return "";
//        }
//    }
//
//    public int authByIdAndNameAndMail(String id, String name, String mail){
//        try{
//            String sql = "select * from tbl_user where user_id = ? and user_name = ? and user_mail = ?";
//
//            LinkedList list = new LinkedList();
//            list.add(id);
//            list.add(name);
//            list.add(mail);
//
//            LinkedList resultList = db.select(null, sql, list);
//
//            if(resultList.size() < 1) return 0;
//
//            return 1;
//        }catch (Exception e){
//            return -1;
//        }
//    }
//
//    public int updatePwById(String id, String pw){
//        try{
//            LinkedHashMap columnMap = new LinkedHashMap();
//            columnMap.put("user_passwd", pw);
//
//            String sql = "where user_id = ?";
//
//            LinkedList list = new LinkedList();
//            list.add(id);
//
//            int result = db.update(null, "tbl_user", columnMap, sql, list);
//
//            if(result < 1) return 0;
//
//            return 1;
//        }catch (Exception e){
//            e.printStackTrace();
//            return -1;
//        }
//    }