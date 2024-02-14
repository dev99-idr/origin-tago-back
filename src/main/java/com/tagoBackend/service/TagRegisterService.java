package com.tagoBackend.service;

import kr.co.j2.das.sql.DAS3;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.sql.Connection;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class TagRegisterService {
    DAS3 db = new DAS3("tago");
    public int tagInsert(JSONObject commonData, JSONArray collectionData ) {
        Connection conn = null;
        try {
            conn = db.beginTrans();
            int count = -1;
            int count2 = -1;
            int count3 = -1;
            int count4 = -1;

            String tagName = commonData.get("tagName").toString();
            String tagSerialNumber = commonData.get("tagSerialNumber").toString();
            String tagLocation = commonData.get("tagLocation").toString();
            String tagThingId = commonData.get("tagThingId").toString();
            String tagMfr = commonData.get("tagMfr").toString();
            String tagSize = commonData.get("tagSize").toString();
            String tagType = commonData.get("tagType").toString();
            String tag_info_json_1 = commonData.get("tag_info_json_1").toString();
            String tag_info_json_2 = commonData.get("tag_info_json_2").toString();
            String tag_info_json_3 = commonData.get("tag_info_json_3").toString();
            String image_path = "D:" + File.separator + "Tago" + File.separator + "tago-front" + File.separator + "resource" + File.separator + "tagBackground"  + File.separator;

            String image_name_1 = commonData.get("image_name_1").toString();
            String image_name_2 = commonData.get("image_name_2").toString();
            String image_name_3 = commonData.get("image_name_3").toString();
            String image_type_1 = commonData.get("image_type_1").toString();
            String image_type_2 = commonData.get("image_type_2").toString();
            String image_type_3 = commonData.get("image_type_3").toString();
            String thirdPartyType = commonData.get("thirdPartyType").toString();
            String tag_mfr = commonData.get("tagMfr").toString();

            int tagWakeupPrd = Integer.parseInt(commonData.get("tagWakeupPrd").toString());
            String unixTime = new Long(System.currentTimeMillis()).toString();

            LinkedHashMap map = new LinkedHashMap();

            String insertSql = "INSERT INTO tbl_smart_tag_info (tag_name, tag_serial_number, tag_location, tag_wakeup_prd, tag_thing_id, create_dt" +
                    ",update_dt, tag_info_json_1, tag_info_json_2, tag_info_json_3, image_path, image_name_1, image_name_2,image_name_3, " +
                    "image_type_1, image_type_2, image_type_3, third_party_type, tag_mfr) VALUES " +
                    "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE del_yn='N', update_dt = ?";

            LinkedList insertSqlList = new LinkedList();
            insertSqlList.add(tagName);
            insertSqlList.add(tagSerialNumber);
            insertSqlList.add(tagLocation);
            insertSqlList.add(tagWakeupPrd);
            insertSqlList.add(tagThingId);
            insertSqlList.add(unixTime);
            insertSqlList.add(unixTime);
            insertSqlList.add(tag_info_json_1);
            insertSqlList.add(tag_info_json_2);
            insertSqlList.add(tag_info_json_3);
            insertSqlList.add(image_path);
            insertSqlList.add(image_name_1);
            insertSqlList.add(image_name_2);
            insertSqlList.add(image_name_3);
            insertSqlList.add(image_type_1);
            insertSqlList.add(image_type_2);
            insertSqlList.add(image_type_3);
            insertSqlList.add(thirdPartyType);
            insertSqlList.add(tag_mfr);
            insertSqlList.add(unixTime);

            count = db.execute(conn, insertSql, insertSqlList);
            map.clear();

            LinkedList list = new LinkedList();
            list.add(tagThingId.split("_")[0]);
            LinkedList parent_idx = db.select(null,"select idx from tbl_node where node_name = ?",list);

            String parent_idx_str = (String) ((LinkedHashMap<Object, Object>) parent_idx.get(0)).get("idx");

            map.put("parent_idx", parent_idx_str);
            map.put("node_name", tagThingId);
            map.put("node_type", tagThingId.split("_")[0]);
            map.put("del_yn", "N");
            map.put("ins_time", System.currentTimeMillis());
            map.put("upd_time", System.currentTimeMillis());
//            map.put("ins_time", System.currentTimeMillis() / 1000L + "000");
//            map.put("upd_time", System.currentTimeMillis() / 1000L + "000");

            insertSql = "INSERT INTO tbl_node (parent_idx, node_name, node_type, tag_mfr, tag_size, del_yn, ins_time, upd_time) VALUES " +
                    "(?,?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE del_yn='N'";

            list.clear();
            list.add(parent_idx_str);
            list.add(tagThingId);
            list.add(tagType);
            list.add(tagMfr);
            list.add(tagSize);
            list.add("N");
            list.add(System.currentTimeMillis());
            list.add(System.currentTimeMillis());
//            list.add(System.currentTimeMillis() / 1000L + "000");
//            list.add(System.currentTimeMillis() / 1000L + "000");

            count2 = db.execute(null, insertSql, list);

            count3 = -1;

            LinkedList<Object> rowset = db.select(conn,"select max(idx) as maxrawid from tbl_smart_tag_info", null);
            int tagIdx = -1;
            if(rowset.size() > 0)
            {
                String rawid = (String) ((LinkedHashMap<Object, Object>) rowset.get(0)).get("maxrawid");
                tagIdx = Integer.parseInt(rawid);
            }

            for (int i = 0; i < collectionData.size(); i++) {
                JSONObject tempJSON = new JSONObject((LinkedHashMap)  collectionData.get(i));
                String collectionDataName = tempJSON.get("collectionDataName").toString();
                String collectionDataKey = tempJSON.get("collectionDataKey").toString();
                String collectionDataUnit = tempJSON.get("collectionDataUnit").toString();
                String collectionDataType = tempJSON.get("collectionDataType").toString();
                String collectionDataPeriodType = tempJSON.get("collectionDataPeriodType").toString();
                String collectionDataCategory = tempJSON.get("collectionDataCategory").toString();

                map.clear();
                map.put("collection_data_name", collectionDataName);
                map.put("collection_data_key", collectionDataKey);
                map.put("collection_data_unit", collectionDataUnit);
                map.put("collection_data_type", collectionDataType);
                map.put("collection_data_category", collectionDataCategory);
                map.put("collection_dataperiod_type", collectionDataPeriodType);
                map.put("p_idx", tagIdx);
                count3 = db.insert(conn, "tbl_collection_info", map);

                map.clear();
                list.clear();
                list.add(tagThingId);

                LinkedList node_idx = db.select(null,"select idx from tbl_node where node_name = ?",list);
                String node_idx_str = (String) ((LinkedHashMap<Object, Object>) node_idx.get(0)).get("idx");

                map.put("node_idx",node_idx_str);
                map.put("variable_name",collectionDataKey);
                map.put("variable_value","");
                map.put("del_yn","N");
                map.put("ins_time", System.currentTimeMillis());
                map.put("upd_time", System.currentTimeMillis());
//                map.put("ins_time", System.currentTimeMillis() / 1000L + "000");
//                map.put("upd_time", System.currentTimeMillis() / 1000L + "000");

                count4 = db.insert(conn,"tbl_variable",map);

                if(count3 < 1 || count4 < 1){
                    throw new Exception();
                }
            }

            if(count < 1 || count2 < 1 || count3 < 1 || count4 < 1) throw new Exception();

            db.commit(conn);
            return 1;
        } catch (Exception e) {
            db.rollback(conn);
            e.printStackTrace();
            return -1;
        }
    }
    public int tagEdit(JSONObject commonData, JSONArray collectionData ) {
        Connection conn = null;
        try {
            conn = db.beginTrans();
            int count = -1;
            int count2 = -1;

            String tagName = commonData.get("tagName").toString();
            String tagLocation = commonData.get("tagLocation").toString();
            String tagThingId = commonData.get("tagThingId").toString();
            String p_idx = commonData.get("idx").toString();
            String tagWakeupPrd = commonData.get("tagWakeupPrd").toString();
            int tagWakeupPrdInt = -1;

            if(tagWakeupPrd.equals("")){
                tagWakeupPrdInt = -1;
            }
            else{
                tagWakeupPrdInt = Integer.parseInt(commonData.get("tagWakeupPrd").toString());
            }

//            long unixTime = System.currentTimeMillis() / 1000L;
//            String unixTime = System.currentTimeMillis() / 1000L + "000";
            String unixTime = new Long(System.currentTimeMillis()).toString();

            LinkedHashMap map = new LinkedHashMap();
//            String image_path_1 = commonData.get("image_path_1").toString();
//            String image_path_2 = commonData.get("image_path_2").toString();
//            String image_path_3 = commonData.get("image_path_3").toString();

            String tag_info_json_1 = commonData.get("tag_info_json_1").toString();
            String tag_info_json_2 = commonData.get("tag_info_json_2").toString();
            String tag_info_json_3 = commonData.get("tag_info_json_3").toString();
//            String image_path = commonData.get("image_path").toString();
            String image_path = "D:" + File.separator + "Tago" + File.separator + "tago-front" + File.separator + "resource" + File.separator + "tagBackground"  + File.separator;

            String image_name_1 = commonData.get("image_name_1").toString();
            String image_name_2 = commonData.get("image_name_2").toString();
            String image_name_3 = commonData.get("image_name_3").toString();

            String image_type_1 = commonData.get("image_type_1").toString();
            String image_type_2 = commonData.get("image_type_2").toString();
            String image_type_3 = commonData.get("image_type_3").toString();
            String thirdPartyType = commonData.get("thirdPartyType").toString();

            map.put("tag_name", tagName);
            map.put("tag_location", tagLocation);
            map.put("tag_wakeup_prd", tagWakeupPrdInt);
            map.put("update_dt", unixTime);
//            map.put("image_path_1", image_path_1);
//            map.put("image_path_2", image_path_2);
//            map.put("image_path_3", image_path_3);
            map.put("tag_info_json_1", tag_info_json_1);
            map.put("tag_info_json_2", tag_info_json_2);
            map.put("tag_info_json_3", tag_info_json_3);
            map.put("image_path", image_path);
            map.put("image_name_1", image_name_1);
            map.put("image_name_2", image_name_2);
            map.put("image_name_3", image_name_3);
            map.put("image_type_1", image_type_1);
            map.put("image_type_2", image_type_2);
            map.put("image_type_3", image_type_3);
            map.put("third_party_type", thirdPartyType);

            //del_yn 체크를 안하여 tag_thing_id가 동일한 tag의 경우 같이 업데이트 방지
            String sql = "where tag_thing_id = ? and del_yn='N'";

            LinkedList list = new LinkedList();
            list.add(tagThingId);

            count = db.update(conn, "tbl_smart_tag_info", map, sql, list);

            for (int i = 0; i < collectionData.size(); i++) {
                JSONObject tempJSON = new JSONObject((LinkedHashMap)  collectionData.get(i));
                String collectionDataName = tempJSON.get("collectionDataName").toString();
                String collectionDataKey = tempJSON.get("collectionDataKey").toString();
                String collectionDataValue = tempJSON.get("collectionDataValue").toString();
                String collectionDataUnit = tempJSON.get("collectionDataUnit").toString();
                String collectionDataType = tempJSON.get("collectionDataType").toString();
                String collectionDataPeriodType = tempJSON.get("collectionDataPeriodType").toString();
                String collectionDataCategory = tempJSON.get("collectionDataCategory").toString();
                String idx = tempJSON.get("idx").toString();

                map.clear();
                map.put("collection_data_name", collectionDataName);
                map.put("collection_data_key", collectionDataKey);

                map.put("collection_data_value", collectionDataValue);

                map.put("collection_data_unit", collectionDataUnit);
                map.put("collection_data_type", collectionDataType);
                map.put("collection_dataperiod_type", collectionDataPeriodType);

                map.put("collection_data_category", collectionDataCategory);
                sql = "where p_idx = ? and idx = ?";

                list.clear();
                list.add(p_idx);
                list.add(idx);

                count2 = db.update(conn, "tbl_collection_info", map, sql, list);

                if(count2 < 0){
                    throw new Exception();
                }
            }

            if(count < 1 || count2 < 1 ) throw new Exception();

            db.commit(conn);
            return 1;
        } catch (Exception e) {
            db.rollback(conn);
            e.printStackTrace();
            return -1;
        }
    }
    
    /**
     * 시리얼 번호 존재 여부 체크
     * @param strSerialNo
     * @return
     */
    public int chkSerialNumber(String strSerialNo ) {
    	
    	LinkedList resultList = null;
    	
    	
    	try {
            String sql = "SELECT * FROM tago.tbl_smart_tag_info where del_yn= 'N' and tag_serial_number = ?";

            LinkedList list = new LinkedList();
            list.add(strSerialNo);
            
            resultList = db.select(null, sql, list);
         
        } catch (Exception e) {
        	e.printStackTrace();
        }
    	return resultList.size();
    }
}
