package com.tagoBackend.service;

import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tagoBackend.control.TagMonitoringController;

import kr.co.j2.das.sql.DAS3;

public class TagMonitoringService {
    DAS3 db = new DAS3("tago");
    private final Logger log = LoggerFactory.getLogger(TagMonitoringController.class);

    /**
     * tag list
     * @return
     */
    public LinkedList tagList() {
        try{

        	String sql = "select list.*, loc.location_name, node.tag_size, node.node_type";
        			sql += " from tbl_smart_tag_info as list";
        			sql += " inner join tbl_location_info as loc on list.tag_location = loc.idx and list.del_yn = 'N'";
        			sql += " inner join tbl_node as node on list.tag_thing_id = node.node_name and node.del_yn = 'N'";
        			sql += " ORDER BY idx DESC";


            LinkedList list = new LinkedList();

            LinkedList resultList = db.select(null, sql, list);
            if(resultList.size() < 1) throw new Exception();

            return  resultList;
        }catch (Exception e){
            return new LinkedList();
        }
    }
    
    
    public LinkedList tagList(int pageNumber, int pageSize,int totalCount) {
        try{
        	String sql = "select list.*, loc.location_name, node.tag_size, node.node_type";
			sql += " from tbl_smart_tag_info as list";
			sql += " inner join tbl_location_info as loc on list.tag_location = loc.idx and list.del_yn = 'N'";
			sql += " inner join tbl_node as node on list.tag_thing_id = node.node_name and node.del_yn = 'N'";
			sql += " ORDER BY idx DESC";
            sql += " limit ?,?";

            int startOffset = pageNumber*pageSize;
            
            if ( startOffset > totalCount) {
            	startOffset = 0;
            }

            LinkedList list = new LinkedList();            
            list.add(startOffset);
            list.add(pageSize);
            LinkedList resultList = db.select(null, sql, list);
            if(resultList.size() < 1) throw new Exception();
//            JSONArray jarr = (JSONArray) resultList.;

            return  resultList;
        }catch (Exception e){
            return new LinkedList();
        }
    }
    
    public LinkedList tagListCount() {
        try{
            String sql = "select count(*) as tagListCount from tbl_smart_tag_info where del_yn = 'N' ;";
            
            
            LinkedList resultList = db.select(null, sql, null);
            if(resultList.size() < 1) throw new Exception();
//            JSONArray jarr = (JSONArray) resultList.;
            return  resultList;
        }catch (Exception e){
            return new LinkedList();
        }
    }

    public LinkedList tagCollectionList(int idx) {
        try{

            String sql = "select * from tbl_collection_info where p_idx = ? ";




            LinkedList list = new LinkedList();
            list.add(idx);

            LinkedList resultList = db.select(null, sql, list);
            if(resultList.size() < 1) throw new Exception();
//            JSONArray jarr = (JSONArray) resultList.;


            return  resultList;
        }catch (Exception e){
            return new LinkedList();
        }

    }
    public boolean tagDelete(int idx) {
        Connection conn = null;
        try {
            conn = db.beginTrans();
            String sql = "where idx = ?;";

            LinkedList list = new LinkedList();
            list.add(idx);

            LinkedHashMap hashMap = new LinkedHashMap();
            hashMap.put("del_yn", 'Y');

            String sql2 = "where node_name = (select tag_thing_id from tbl_smart_tag_info where idx = "+idx+")";
            String sql3 = "delete from tbl_variable where node_idx  = (SELECT a.idx as idx from tbl_node a "
            		+ "inner join tbl_smart_tag_info b on  b.idx = ? and a.node_name = tag_thing_id)";
            String sql4 = "delete from tbl_collection_info where p_idx = ?";

            int result = db.update(conn, "tbl_smart_tag_info", hashMap, sql, list);
            // update tbl_smart_tag_info set del_yn ='Y' where idx = ?

            if (result < 1)
            {
                throw new Exception("tagDelete error: update tbl_smart_tag_info");
            }

            int result2 = db.update(conn, "tbl_node", hashMap, sql2, null);
         // update tbl_node set del_yn ='Y' where idx = ?

            if (result2 < 1)
            {
                throw new Exception("tagDelete error: update tbl_node");
            }

            int result3 = db.execute(conn, sql3, list);

            if (result3 < 1)
            {
                throw new Exception("tagDelete error:" + sql3 + " " + list);
            }

            int result4 = db.execute(conn, sql4, list);

            if (result4 < 1)
            {
                throw new Exception("tagDelete error" + sql4 + " " + list);
            }

            db.commit(conn);
            return true;
        } catch (Exception e) {
            db.rollback(conn);
            e.printStackTrace();
            return false;
        }
    }

    public LinkedList tagLayoutList(int idx) {
        try{
            String sql = "select *, 0 as num from ( select * from tbl_smart_tag_info tsti where tsti.idx = ? ) as a join tbl_tag_layout_list ttll on a.image_name_1 = ttll.tag_image_file_name \n" +
                    "union\n" +
                    "select *, 1 as num from ( select * from tbl_smart_tag_info tsti where tsti.idx = ? ) as a join tbl_tag_layout_list ttll on a.image_name_2 = ttll.tag_image_file_name \n" +
                    "union\n" +
                    "select *, 2 as num from ( select * from tbl_smart_tag_info tsti where tsti.idx = ? ) as a join tbl_tag_layout_list ttll on a.image_name_3 = ttll.tag_image_file_name \n";

            LinkedList list = new LinkedList();
            list.add(idx);
            list.add(idx);
            list.add(idx);

            LinkedList resultList = db.select(null, sql, list);
            if(resultList.size() < 1) throw new Exception();

            return  resultList;
        }catch (Exception e){
            return new LinkedList();
        }
    }
    public LinkedList tagLatestDataList(String thingid) {
        try{
            String sql = "SELECT * from tbl_variable tv where node_idx  = (SELECT idx  from tbl_node tn where node_name = ? );";

            LinkedList list = new LinkedList();
            list.add(thingid);

            LinkedList resultList = db.select(null, sql, list);
            if(resultList.size() < 1) throw new Exception();

            return  resultList;
        }catch (Exception e){
            return new LinkedList();
        }
    }
    public LinkedList tagHistoryDataList(JSONObject json) {
        try{
            String dataCategory = json.get("dataCategory").toString();
            LinkedList data_result = new LinkedList();
            String fromDateUnix = json.get("fromDateUnix").toString()+"000";
            String toDateUnix = json.get("toDateUnix").toString()+"000";
            String fromTableName = json.get("fromTableName").toString();
            String toTableName = json.get("toTableName").toString();

            String thingID = json.get("thingID").toString();

            DateFormat df = new SimpleDateFormat("yyMMddHH");
            
            Calendar calSearchStart = Calendar.getInstance();            
            calSearchStart.set(2000 +  Integer.parseInt(fromTableName.substring(0,2)) ,Integer.parseInt(fromTableName.substring(2,4)) - 1,Integer.parseInt(fromTableName.substring(4,6)), Integer.parseInt(fromTableName.substring(6,8)),0  );	//검색 시작년도

            Calendar calSearchEnd = Calendar.getInstance();
            calSearchEnd.set(2000 +  Integer.parseInt(toTableName.substring(0,2)) ,Integer.parseInt(toTableName.substring(2,4)) - 1,Integer.parseInt(toTableName.substring(4,6)), Integer.parseInt(toTableName.substring(6,8)),0  );	//검색 시작년도


            if(dataCategory.equals("control")){
                String controlSql = "select * from tbl_value "+
                 " where node_name like '%"+thingID+"%'"+
                        "and variable_time > ? and variable_time < ?";
                LinkedList list2 = new LinkedList();
                list2.add(fromDateUnix);
                list2.add(toDateUnix);

                data_result = db.select(null, controlSql, list2);
            }
            else if (dataCategory.equals("collection")){
                String sql = "";

                String searchTableNameStart = df.format(calSearchStart.getTime());
                String searchTableNameEnd = df.format(calSearchEnd.getTime());
                
                while (!searchTableNameStart.equals(searchTableNameEnd)){
             	
                	String tableName = df.format(calSearchStart.getTime());
                	
                	
                    LinkedList list = new LinkedList();

                    String checkSql = "show tables like ?";

                    list.add("tbl_value_"+ tableName);
                    LinkedList checkList = db.select(null, checkSql, list);
                    if(checkList.size() == 0){
                        log.debug("table does not exiset tbl_value_"+ tableName);
                        //return new LinkedList();
                    }
                    else{
                        list.clear();
                        sql = "select * from tbl_value_"+ tableName +
                                " where node_name like '%"+thingID+"%'"+
                                " and variable_time > "+fromDateUnix+ " and variable_time < "+toDateUnix ;

                        LinkedList resultList = db.select(null, sql, list);
                        data_result.addAll(resultList);
                    }

                    calSearchStart.add(Calendar.MINUTE, 60);
                    searchTableNameStart = df.format(calSearchStart.getTime());
                    
                }
            }
            return  data_result;

        }catch (Exception e){
            e.printStackTrace();
            return new LinkedList();
        }
    }

    public int createTrigger() throws Exception {
        try {
            Date today = new Date();
            Date tomorrow = new Date(today.getTime() + (1000 * 60 * 60 * 24));

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd");

            int day = 0;

            for(int i = 0 ; i < 24; i++){
                String dayString = Integer.toString(day);
                if( i < 10){
                    dayString = "0"+dayString;
                }
                String sql =
                    "\tCREATE trigger tbl_value_" + dateFormat.format(tomorrow)+dayString+
                    "\tafter insert  ON tbl_value_" + dateFormat.format(tomorrow)+dayString+
                    "\tFOR EACH ROW \n" +
                    "\tBEGIN\n" +
                    "\t\tDECLARE variable_time_u bigint;\n" +
                    "\t\tDECLARE variable_name_u varchar(500);\n" +
                    "\t\tDECLARE node_name_u varchar(500);\n" +
                    "\t\tDECLARE variable_value_u varchar(100);\n" +
                    "\n" +
                    "\t\tSET variable_time_u = NEW.variable_time;\n" +
                    "\t\tSET variable_name_u = NEW.variable_name;\n" +
                    "\t\tSET node_name_u = NEW.node_name;\n" +
                    "\t\tSET variable_value_u = NEW.variable_value;\n" +
                    "\t\t\nUPDATE tbl_variable SET variable_value = variable_value_u, upd_time = variable_time_u WHERE variable_name = variable_name_u\n" +
                    "\t\t\tAND node_idx = (SELECT idx FROM tbl_node WHERE node_name = node_name_u);\n" +
                    "\tEND\n";

                db.execute(null, sql,null);

                day++;
            }

            log.info("success create trigger");
            return 1;
        }
        catch (Exception e){
            e.printStackTrace();
            return -1;
        }


    }

    public LinkedList wakeUpPeriodList(String thingid) {
        try{

            String sql = "select a.*, tn.node_name  from ( select * from tbl_variable where node_idx = (select idx from tbl_node where node_name = ? and del_yn = 'N')  ) as a left join tbl_node tn on tn.idx  = a.node_idx;";

            LinkedList list = new LinkedList();
            list.add(thingid);

            LinkedList resultList = db.select(null, sql, list);
            if(resultList.size() < 1) throw new Exception();

            return  resultList;
        }catch (Exception e){
            return new LinkedList();
        }
    }

    public int insertControlData(JSONObject json) {
        try {
            String variable_time = json.get("variable_time").toString();
            String node_name = json.get("node_name").toString();
            String variable_name = json.get("variable_name").toString();
            String variable_value = json.get("variable_value").toString();

            int count = -1;
            LinkedHashMap map = new LinkedHashMap();
            map.put("variable_time", variable_time);
            map.put("node_name", node_name);
            map.put("variable_name",variable_name);
            map.put("variable_value",variable_value);

            count = db.insert(null, "tbl_value", map);
            if (count < 1) throw new Exception();
//            JSONArray jarr = (JSONArray) resultList.;
            return count;
        } catch (Exception e) {
            return -1;
        }
    }

    public int saveTagMap(JSONObject json) {
        try {
            String canvasJson = json.get("canvasJson").toString();

            int count = -1;
            LinkedHashMap map = new LinkedHashMap();
            map.put("canvasJson", canvasJson);

            count = db.insert(null, "tbl_tag_map_info", map);
            if (count < 1) throw new Exception();

            return count;
        } catch (Exception e) {
            return -1;
        }
    }

    public LinkedList getTagMapMap(JSONObject json) {
        try{
            String sql = "select * from tbl_tag_map_info order by idx desc limit 1;";

            LinkedList resultList = db.select(null, sql, null);
            if(resultList.size() < 1) throw new Exception();

            return  resultList;
        }catch (Exception e){
            return new LinkedList();
        }
    }

    public LinkedList getTagZigbeeBle(JSONObject json) {
        try{

            String zigbeeThingId = json.get("thingid").toString();

        	String sql = "select * from tbl_zigbee_ble where 1=1 and del_yn = 'N' and zigbee_thing_id = ?;";

            LinkedList list = new LinkedList();
            list.add(zigbeeThingId);

            LinkedList resultList = db.select(null, sql, list);

            return  resultList;
        }catch (Exception e){
            return new LinkedList();
        }
    }

    public LinkedList getNotMappingBle() {
        try{

        	String sql = " select a.idx, a.tag_thing_id " +
        	             " from tbl_smart_tag_info a " +
        	             " left outer join tbl_zigbee_ble b " +
        	             " on a.tag_thing_id = b.ble_thing_id " +
        	             " and b.del_yn = 'N' " +
        	             " where 1=1 " +
        	             " and a.tag_thing_id like ('%BTAG%') " +
        	             " and b.ble_thing_id is null; ";

            LinkedList resultList = db.select(null, sql, null);

            return  resultList;
        }catch (Exception e){
            return new LinkedList();
        }
    }

    public void saveZigbeeBleTag(JSONObject json) {
        try {
            String zigbeeThingId = json.get("zigbeeThingId").toString();
            String bleThingId = json.get("bleThingId").toString();

            int count = -1;

            LinkedList list = new LinkedList();
            list.add(zigbeeThingId);

            String countSql = " select count(*) as count from tbl_zigbee_ble where 1=1 and zigbee_thing_id = ? ";
            
            LinkedList countList = db.select(null, countSql, list);
            
            Integer count1 = Integer.parseInt(countList.get(0).toString().replace("{count=","").replace("}",""));
            
            if(count1 > 0){

                // update
                LinkedHashMap updateMap = new LinkedHashMap();
                updateMap.put("del_yn", "N");
                updateMap.put("ble_thing_id", bleThingId);

                LinkedList updateList = new LinkedList();
                updateList.add(zigbeeThingId);

                String sql2 = " where 1=1 and zigbee_thing_id = ? ";
                count = db.update(null, "tbl_zigbee_ble", updateMap, sql2, updateList);
            }else{
                // insert
                LinkedHashMap insertMap = new LinkedHashMap();
                insertMap.put("zigbee_thing_id", zigbeeThingId);
                insertMap.put("ble_thing_id", bleThingId);
                insertMap.put("create_dt", System.currentTimeMillis());
                insertMap.put("update_dt", System.currentTimeMillis());

                count = db.insert(null, "tbl_zigbee_ble", insertMap);
            }

            // count = db.insert(null, "tbl_tag_map_info", map);
            if (count < 1) throw new Exception();

        } catch (Exception e) {
            new Exception();
        }
    }

    public void removeZigbeeBleTag(JSONObject json) {
        try {
            String zigbeeThingId = json.get("zigbeeThingId").toString();

            int count = -1;

            // update
            LinkedHashMap updateMap = new LinkedHashMap();
            updateMap.put("del_yn", "Y");
            updateMap.put("ble_thing_id", "");

            LinkedList updateList = new LinkedList();
            updateList.add(zigbeeThingId);

            String sql2 = " where 1=1 and zigbee_thing_id = ? ";
            count = db.update(null, "tbl_zigbee_ble", updateMap, sql2, updateList);
           
            // count = db.insert(null, "tbl_tag_map_info", map);
            if (count < 1) throw new Exception();

        } catch (Exception e) {
            new Exception();
        }
    }
}
