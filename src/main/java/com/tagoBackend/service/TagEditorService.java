package com.tagoBackend.service;

import kr.co.j2.das.sql.DAS3;
import org.json.simple.JSONObject;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;


public class TagEditorService {
    DAS3 db = new DAS3("tago");


    public int getTagMapLayout(JSONObject json) {
        try {

            String tag_info_json = json.get("canvasJson").toString();
            String tag_layout_name = json.get("name").toString();
            String tag_image_file_name = json.get("fileName").toString();
            String tag_size = json.get("tagSize").toString();
            String image_type = json.get("imageType").toString();
            String tag_type = json.get("tagType").toString();

            int count = -1;
            LinkedHashMap map = new LinkedHashMap();
            map.put("tag_info_json", tag_info_json);
            map.put("tag_layout_name", tag_layout_name);
            map.put("tag_image_file_name",tag_image_file_name);
            map.put("tag_size",tag_size);
            map.put("image_type",image_type);
            map.put("tag_mfr",tag_type);

            count = db.insert(null, "tbl_tag_layout_list", map);
            if (count < 1) throw new Exception();
//            JSONArray jarr = (JSONArray) resultList.;
            return count;
        } catch (Exception e) {
            return -1;
        }
    }
    public int registerFont(String url, String fontFamilyName) {
        try {
            int count = -1;
            LinkedHashMap map = new LinkedHashMap();
            map.put("font_url", url);
            map.put("font_name", fontFamilyName);

            count = db.insert(null, "tbl_web_fonts_list", map);
            if (count < 1) throw new Exception();
//            JSONArray jarr = (JSONArray) resultList.;
            return count;
        } catch (Exception e) {
            return -1;
        }
    }
    public LinkedList fontList() {
        try{
            String sql = "select * from tbl_web_fonts_list ;";
            LinkedList resultList = db.select(null, sql, null);
            if(resultList.size() < 1) throw new Exception();
//            JSONArray jarr = (JSONArray) resultList.;
            return  resultList;
        }catch (Exception e){
            return new LinkedList();
        }
    }
    public LinkedList tagLayoutList(String tagSize) {
        try{
        	
        	 LinkedList list = new LinkedList();            
            
            String sql = "select * from tbl_tag_layout_list where del_yn = 'N'";
            if ( !"".equals(tagSize)) {
            	sql += "and tag_size =?";
            	list.add(tagSize);
            }
            
            LinkedList resultList = db.select(null, sql, list);
            if(resultList.size() < 1) throw new Exception();
//            JSONArray jarr = (JSONArray) resultList.;
            return  resultList;
        }catch (Exception e){
            return new LinkedList();
        }
    }
    
    
    public LinkedList tagLayoutList(int pageNumber, int pageSize) {
        try{
            String sql = "select * from tbl_tag_layout_list where del_yn = 'N' limit ? , ?";
            
           
            
            LinkedList list = new LinkedList();            
            list.add(pageNumber*pageSize);
            list.add(pageSize);
            
            LinkedList resultList = db.select(null, sql, list);
            if(resultList.size() < 1) throw new Exception();
//            JSONArray jarr = (JSONArray) resultList.;
            return  resultList;
        }catch (Exception e){
            return new LinkedList();
        }
    }

    public int deleteTagLayout(JSONObject json) {
        try {
            int count = -1;
            LinkedHashMap map = new LinkedHashMap();

            String idx = json.get("idx").toString();

            map.put("del_yn", "Y");

            String sql = "where idx = ?";

            LinkedList list = new LinkedList();
            list.add(idx);

            count = db.update(null, "tbl_tag_layout_list", map, sql, list);
            if (count < 1) throw new Exception();
            return count;
        } catch (Exception e) {
            return -1;
        }
    }

    public int editTagLayout(JSONObject json) {
        try {
            int count = -1;
            LinkedHashMap map = new LinkedHashMap();
            LinkedHashMap map1 = new LinkedHashMap();
            LinkedHashMap map2 = new LinkedHashMap();
            LinkedHashMap map3 = new LinkedHashMap();

            String tag_info_json = json.get("canvasJson").toString();
            String tag_layout_name = json.get("name").toString();
            String tag_size = json.get("tagSize").toString();
            String idx = json.get("idx").toString();
            String image_type = json.get("imageType").toString();
            String tag_image_file_name = json.get("fileName").toString();
            String tag_type = json.get("tagType").toString();

            String convertedJson = json.get("convertedJson").toString();

            map1.put("tag_info_json_1", convertedJson);
            map2.put("tag_info_json_2", convertedJson);
            map3.put("tag_info_json_3", convertedJson);

            String sql1 = "where image_name_1 = ?";
            String sql2 = "where image_name_2 = ?";
            String sql3 = "where image_name_3 = ?";

            LinkedList list1 = new LinkedList();
            LinkedList list2 = new LinkedList();
            LinkedList list3 = new LinkedList();

            list1.add(tag_image_file_name);
            list2.add(tag_image_file_name);
            list3.add(tag_image_file_name);

            int count1 = db.update(null, "tbl_smart_tag_info", map1, sql1, list1);
            int count2 = db.update(null, "tbl_smart_tag_info", map2, sql2, list2);
            int count3 = db.update(null, "tbl_smart_tag_info", map3, sql3, list3);

            map.put("tag_info_json", tag_info_json);
            map.put("tag_layout_name", tag_layout_name);
            map.put("tag_size",tag_size);
            map.put("image_type",image_type);
            map.put("tag_mfr",tag_type);

            String sql = "where idx = ?";

            LinkedList list = new LinkedList();
            list.add(idx);

            count = db.update(null, "tbl_tag_layout_list", map, sql, list);
            if (count < 1) throw new Exception();
            return count;
        } catch (Exception e) {
            return -1;
        }
    }

    public LinkedList getDataGroupInfo() {
        try {
            String sql = "select * from tbl_data_group where del_yn = 'N'";

            LinkedList list = new LinkedList();
            LinkedList resultList = db.select(null, sql, list);

            if (resultList.size() < 1) throw new Exception();

            return resultList;
        } catch (Exception e) {
            return null;
        }
    }

    public int insertDataGroupInfo(String parent_rawid, String location_name, String depth) {
        Connection conn = null;

        try{
            conn = db.beginTrans();


            LinkedHashMap map = new LinkedHashMap();
            map.put("parent_rawid", parent_rawid);
            map.put("data_group_name", location_name);
            map.put("depth", depth);

            int count = db.insert(conn, "tbl_data_group", map);

            if(count < 1) throw new Exception();

            db.commit(conn);
            return 1;
        }catch (Exception e){
            db.rollback(conn);
            e.printStackTrace();
            return -1;
        }
    }

    public int deleteLocationInfo(String idx, ArrayList<Integer> parentArray) {
        try{
            LinkedHashMap columnMap = new LinkedHashMap();
            columnMap.put("del_yn", "Y");
            String sql = "where idx IN(";
            for(int i =0; i<parentArray.size(); i++){
                if(i != parentArray.size()-1){
                    sql += parentArray.get(i)+",";
                }
                else{
                    sql += parentArray.get(i);
                }
            }
            sql += ");";

            LinkedList list = new LinkedList();

            int result = db.update(null, "tbl_data_group", columnMap, sql, list);

            if(result < 1) return 0;

            return 1;
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }

    public int setDataGroupInfo(String key, String newLocationName) {
        try{
            LinkedHashMap columnMap = new LinkedHashMap();
            columnMap.put("data_group_name", newLocationName);

            String sql = "where idx = ?";

            LinkedList list = new LinkedList();
            list.add(key);

            int result = db.update(null, "tbl_data_group", columnMap, sql, list);

            if(result < 1) return 0;

            return 1;
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }

    public LinkedList dataGroupList(JSONObject json) {
        try{
            String selectedData = json.get("selectedData").toString();
            LinkedList list = new LinkedList();
            list.add(selectedData);

            String sql = "select * from (select * from tbl_data_group where parent_rawid = (select parent_rawid  from tbl_data_group where data_group_name = ?)) as a \n" +
                    "left join (select data_group_name as parnet_group_name,idx as parent_idx from tbl_data_group) as tdg  on a.parent_rawid = tdg.parent_idx;";

            LinkedList resultList = db.select(null, sql, list);

            if(resultList.size() < 1) throw new Exception();
//            JSONArray jarr = (JSONArray) resultList.;
            return  resultList;
        }catch (Exception e){
            return new LinkedList();
        }

    }
}