package com.tagoBackend.control;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.LinkedList;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tagoBackend.custom.CustomFunction;
import com.tagoBackend.response.Message;
import com.tagoBackend.response.StatusEnum;
import com.tagoBackend.service.TagMonitoringService;


@RestController
@RequestMapping(value = "/tag-monitoring")
public class TagMonitoringController {
    //com.tagoBackend.service.TagMonitoringService TagMonitoringService = new TagMonitoringService();

    @Autowired
    private TagMonitoringService TagMonitoringService;

    CustomFunction cf = new CustomFunction();

    private final Logger log = LoggerFactory.getLogger(TagMonitoringController.class);

    @RequestMapping(value="/map-list", method = RequestMethod.POST)
    public ResponseEntity<Message> getTagMap(@RequestBody JSONObject param) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        try {
            JSONObject json = new JSONObject(param);

            LinkedList getTagMapMapResult = TagMonitoringService.getTagMapMap(json);

            JSONObject resJson = new JSONObject();
            resJson.put("getTagMapMap", getTagMapMapResult);

            if(getTagMapMapResult == null) return new ResponseEntity<>(cf.crtMsg(StatusEnum.NO_CONTENT, "UNAUTHORIZED",""), headers, HttpStatus.UNAUTHORIZED);

            return new ResponseEntity<>(cf.crtMsg(StatusEnum.OK, "OK", resJson), headers, HttpStatus.OK);
        }
        catch (Exception e) {
            log.error(e.getMessage() + "\n" + e.getStackTrace());
            e.printStackTrace();
            return new ResponseEntity<>(cf.crtMsg(StatusEnum.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", ""), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value="/tag-map", method = RequestMethod.POST)
    public ResponseEntity<Message> saveTagMap(@RequestBody JSONObject param, HttpServletResponse response) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        try {
            JSONObject json = new JSONObject(param);

            int saveTagMapResult = TagMonitoringService.saveTagMap(json);

            JSONObject resJson = new JSONObject();
            resJson.put("saveTagMap", saveTagMapResult);

            if(saveTagMapResult == -1) return new ResponseEntity<>(cf.crtMsg(StatusEnum.NO_CONTENT, "UNAUTHORIZED",""), headers, HttpStatus.UNAUTHORIZED);

            return new ResponseEntity<>(cf.crtMsg(StatusEnum.OK, "OK", resJson), headers, HttpStatus.OK);
        }
        catch (Exception e) {
            log.error(e.getMessage() + "\n" + e.getStackTrace());
            e.printStackTrace();
            return new ResponseEntity<>(cf.crtMsg(StatusEnum.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", ""), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value="/tag-control-data", method = RequestMethod.POST)
    public ResponseEntity<Message> insertControlData(@RequestBody JSONObject param, HttpServletResponse response) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        try {
            JSONObject json = new JSONObject(param);

            int insertControlDataResult = TagMonitoringService.insertControlData(json);

            JSONObject resJson = new JSONObject();
            resJson.put("insertControlData", insertControlDataResult);

            if(insertControlDataResult == -1) return new ResponseEntity<>(cf.crtMsg(StatusEnum.NO_CONTENT, "UNAUTHORIZED",""), headers, HttpStatus.UNAUTHORIZED);

            return new ResponseEntity<>(cf.crtMsg(StatusEnum.OK, "OK", resJson), headers, HttpStatus.OK);
        }
        catch (Exception e) {
            log.error(e.getMessage() + "\n" + e.getStackTrace());
            e.printStackTrace();
            return new ResponseEntity<>(cf.crtMsg(StatusEnum.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", ""), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @RequestMapping(value="/create-trigger", method = RequestMethod.POST)
    public ResponseEntity<Message> createTrigger() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        try {
            int createTriggerResult = TagMonitoringService.createTrigger();

            JSONObject resJson = new JSONObject();
            resJson.put("createTriggerResult", createTriggerResult);

            if(createTriggerResult == -1) return new ResponseEntity<>(cf.crtMsg(StatusEnum.NO_CONTENT, "UNAUTHORIZED",""), headers, HttpStatus.UNAUTHORIZED);

            return new ResponseEntity<>(cf.crtMsg(StatusEnum.OK, "OK", resJson), headers, HttpStatus.OK);
        }
        catch (Exception e) {
            log.error(e.getMessage() + "\n" + e.getStackTrace());
            e.printStackTrace();
            return new ResponseEntity<>(cf.crtMsg(StatusEnum.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", ""), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value="/wakeup-period-list", method = RequestMethod.POST)
    public ResponseEntity<Message> wakeupPeriodList(@RequestBody JSONObject param, HttpServletResponse response) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        try {
            JSONObject json = new JSONObject(param);
            String thingid = json.get("thingid").toString();

            LinkedList tagListResult = TagMonitoringService.wakeUpPeriodList(thingid);

            if(tagListResult == null) return new ResponseEntity<>(cf.crtMsg(StatusEnum.NO_CONTENT, "UNAUTHORIZED",""), headers, HttpStatus.UNAUTHORIZED);

            JSONObject resJson = new JSONObject();
            resJson.put("tagCollectionList", tagListResult);

            return new ResponseEntity<>(cf.crtMsg(StatusEnum.OK, "OK", resJson), headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage() + "\n" + e.getStackTrace());
            e.printStackTrace();
            return new ResponseEntity<>(cf.crtMsg(StatusEnum.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", ""), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @RequestMapping(value="/tag-collection-list", method = RequestMethod.POST)
    public ResponseEntity<Message> tagCollectionList(@RequestBody JSONObject param, HttpServletResponse response) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        try {
            JSONObject json = new JSONObject(param);
            int idx = Integer.parseInt(json.get("idx").toString());
            String thingid = json.get("thingid").toString();

            LinkedList tagListResult = TagMonitoringService.tagCollectionList(idx);
            LinkedList tagLayoutResult = TagMonitoringService.tagLayoutList(idx);
            LinkedList tagLatestData = TagMonitoringService.tagLatestDataList(thingid);

            if(tagListResult == null) return new ResponseEntity<>(cf.crtMsg(StatusEnum.NO_CONTENT, "UNAUTHORIZED",""), headers, HttpStatus.UNAUTHORIZED);

            JSONObject resJson = new JSONObject();
            resJson.put("tagCollectionList", tagListResult);
            resJson.put("tagLayoutList", tagLayoutResult);
            resJson.put("tagLatestData", tagLatestData);

            return new ResponseEntity<>(cf.crtMsg(StatusEnum.OK, "OK", resJson), headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage() + "\n" + e.getStackTrace());
            e.printStackTrace();
            return new ResponseEntity<>(cf.crtMsg(StatusEnum.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", ""), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @RequestMapping(value="/tag-history-data-list", method = RequestMethod.POST)
    public ResponseEntity<Message> tagHistoryDataList(@RequestBody JSONObject param, HttpServletResponse response) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        try {
            JSONObject json = new JSONObject(param);

            LinkedList tagHistoryDataListResult = TagMonitoringService.tagHistoryDataList(json);

            if(tagHistoryDataListResult == null) return new ResponseEntity<>(cf.crtMsg(StatusEnum.NO_CONTENT, "UNAUTHORIZED",""), headers, HttpStatus.UNAUTHORIZED);

            JSONObject resJson = new JSONObject();
            resJson.put("tagHistoryDataList", tagHistoryDataListResult);

            return new ResponseEntity<>(cf.crtMsg(StatusEnum.OK, "OK", resJson), headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage() + "\n" + e.getStackTrace());
            e.printStackTrace();
            return new ResponseEntity<>(cf.crtMsg(StatusEnum.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", ""), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value="/tag-list", method = RequestMethod.POST)
    public ResponseEntity<Message> tagList(@RequestBody JSONObject param, HttpServletResponse response) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        try {
        	
        	/*
        	int re = (Integer)param.get("pNumber");
        	
        	int pageNumber = Integer.parseInt(StringUtils.defaultString(String.valueOf((Integer)param.get("pNumber")),"0"));
        	int pageSize = Integer.parseInt(StringUtils.defaultString(String.valueOf((Integer)param.get("pageSize")),"10"));
        	
        	LinkedList tagListCountResult = TagMonitoringService.tagListCount();
        	
        	HashMap li = (HashMap)tagListCountResult.get(0);
        	
        	int totalCount = Integer.parseInt(StringUtils.defaultString((String)li.get("taglistcount"),"0"));
       	
        	   
        	LinkedList tagListResult = TagMonitoringService.tagList(pageNumber,pageSize,totalCount);
        	*/
        	
            LinkedList tagListResult = TagMonitoringService.tagList();
            LinkedList tagListCountResult = TagMonitoringService.tagListCount();

            if(tagListResult == null) return new ResponseEntity<>(cf.crtMsg(StatusEnum.NO_CONTENT, "UNAUTHORIZED",""), headers, HttpStatus.UNAUTHORIZED);
            if(tagListCountResult == null) return new ResponseEntity<>(cf.crtMsg(StatusEnum.NO_CONTENT, "UNAUTHORIZED",""), headers, HttpStatus.UNAUTHORIZED);

            JSONObject resJson = new JSONObject();
            resJson.put("tagList", tagListResult);
            resJson.put("tagListCount", tagListCountResult);

            return new ResponseEntity<>(cf.crtMsg(StatusEnum.OK, "OK", resJson), headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage() + "\n" + e.getStackTrace());
            e.printStackTrace();
            return new ResponseEntity<>(cf.crtMsg(StatusEnum.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", ""), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @RequestMapping(value="/tag-delete", method = RequestMethod.POST)
    public ResponseEntity<Message> tagDelete(@RequestBody JSONObject param, HttpServletResponse response) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        try {

            JSONObject json = new JSONObject(param);
            int idx = Integer.parseInt(json.get("idx").toString());
            boolean tagListResult = TagMonitoringService.tagDelete(idx);

            if(!tagListResult) return new ResponseEntity<>(cf.crtMsg(StatusEnum.NO_CONTENT, "UNAUTHORIZED",""), headers, HttpStatus.UNAUTHORIZED);

            return new ResponseEntity<>(cf.crtMsg(StatusEnum.OK, "OK", ""), headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage() + "\n" + e.getStackTrace());
            e.printStackTrace();
            return new ResponseEntity<>(cf.crtMsg(StatusEnum.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", ""), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    /**
     * 2024.02.14
     * 박상문
     * zigbee ble tag mapping 리스트 조회
     * @param param
     * @return
     */
    @RequestMapping(value="/tag-zigbee-ble", method = RequestMethod.POST)
    public ResponseEntity<Message> getTagZigbeeBle(@RequestBody JSONObject param) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        try {
            JSONObject json = new JSONObject(param);

            LinkedList getTagZigbeeBleResult = TagMonitoringService.getTagZigbeeBle(json);

            JSONObject resJson = new JSONObject();
            resJson.put("getTagZigbeeBleResult", getTagZigbeeBleResult);

            if(getTagZigbeeBleResult == null) return new ResponseEntity<>(cf.crtMsg(StatusEnum.NO_CONTENT, "UNAUTHORIZED",""), headers, HttpStatus.UNAUTHORIZED);

            return new ResponseEntity<>(cf.crtMsg(StatusEnum.OK, "OK", resJson), headers, HttpStatus.OK);
        }
        catch (Exception e) {
            log.error(e.getMessage() + "\n" + e.getStackTrace());
            e.printStackTrace();
            return new ResponseEntity<>(cf.crtMsg(StatusEnum.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", ""), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 2024.02.14
     * 박상문
     * ble tag mapping 안된 리스트 조회
     * @param param
     * @return
     */
    @RequestMapping(value="/not-mapping-ble", method = RequestMethod.POST)
    public ResponseEntity<Message> getNotMappingBle() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        try {

            LinkedList getNotMappingBleResult = TagMonitoringService.getNotMappingBle();

            JSONObject resJson = new JSONObject();
            resJson.put("getNotMappingBleResult", getNotMappingBleResult);

            if(getNotMappingBleResult == null) return new ResponseEntity<>(cf.crtMsg(StatusEnum.NO_CONTENT, "UNAUTHORIZED",""), headers, HttpStatus.UNAUTHORIZED);

            return new ResponseEntity<>(cf.crtMsg(StatusEnum.OK, "OK", resJson), headers, HttpStatus.OK);
        }
        catch (Exception e) {
            log.error(e.getMessage() + "\n" + e.getStackTrace());
            e.printStackTrace();
            return new ResponseEntity<>(cf.crtMsg(StatusEnum.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", ""), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 2024.02.14
     * 박상문
     * ble tag mapping 등록/수정
     * @param param
     * @return
     */
    @RequestMapping(value="/save-zigbee-ble", method = RequestMethod.POST)
    public ResponseEntity<Message> saveZigbeeBleTag(@RequestBody JSONObject param) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        try {
            JSONObject json = new JSONObject(param);
            TagMonitoringService.saveZigbeeBleTag(json);
            return new ResponseEntity<>(cf.crtMsg(StatusEnum.OK, "OK", ""), headers, HttpStatus.OK);
        }
        catch (Exception e) {
            log.error(e.getMessage() + "\n" + e.getStackTrace());
            e.printStackTrace();
            return new ResponseEntity<>(cf.crtMsg(StatusEnum.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", ""), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 2024.02.14
     * 박상문
     * ble tag mapping 삭제
     * @param param
     * @return
     */
    @RequestMapping(value="/remove-zigbee-ble", method = RequestMethod.POST)
    public ResponseEntity<Message> removeZigbeeBleTag(@RequestBody JSONObject param) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        try {
            JSONObject json = new JSONObject(param);
            TagMonitoringService.removeZigbeeBleTag(json);
            return new ResponseEntity<>(cf.crtMsg(StatusEnum.OK, "OK", ""), headers, HttpStatus.OK);
        }
        catch (Exception e) {
            log.error(e.getMessage() + "\n" + e.getStackTrace());
            e.printStackTrace();
            return new ResponseEntity<>(cf.crtMsg(StatusEnum.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", ""), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}