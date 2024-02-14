package com.tagoBackend.control;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.tagoBackend.service.TagEditorService;

@RestController
@RequestMapping(value = "/tag-editor")
public class TagEditorController {
    com.tagoBackend.service.TagEditorService TagEditorService = new TagEditorService();

    CustomFunction cf = new CustomFunction();

    private final Logger log = LoggerFactory.getLogger(TagEditorController.class);

    @RequestMapping(value = "/save-tag-layout", method = RequestMethod.POST)
    public ResponseEntity<Message> getTagMapLayout(@RequestBody JSONObject param, HttpServletResponse response) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        try {
            JSONObject json = new JSONObject(param);



            int registerFontResult = TagEditorService.getTagMapLayout(json);

            if (registerFontResult < 0)
                return new ResponseEntity<>(cf.crtMsg(StatusEnum.NO_CONTENT, "UNAUTHORIZED", ""), headers, HttpStatus.UNAUTHORIZED);
            return new ResponseEntity<>(cf.crtMsg(StatusEnum.OK, "OK", ""), headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage() + "\n" + e.getStackTrace());
            e.printStackTrace();
            return new ResponseEntity<>(cf.crtMsg(StatusEnum.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", ""), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @RequestMapping(value = "/get-data-group-info", method = RequestMethod.POST)
    public ResponseEntity<Message> getDataGroupInfo(@RequestBody HashMap<String, Object> param) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        try {
            JSONObject json = new JSONObject(param);

            LinkedList LocationResult = TagEditorService.getDataGroupInfo();

            if (LocationResult == null){
                return new ResponseEntity<>(cf.crtMsg(StatusEnum.UNAUTHORIZED, "UNAUTHORIZED", ""), headers, HttpStatus.UNAUTHORIZED);
            }

            LinkedHashMap result = null;
            JSONArray array = new JSONArray();

            for(int i =0; i<LocationResult.size(); i++){
                result = (LinkedHashMap)LocationResult.get(i);
                JSONObject resJson = new JSONObject();
                resJson.put("idx", result.get("idx"));
                resJson.put("name", result.get("data_group_name"));
                resJson.put("parent_rawid", result.get("parent_rawid"));
                resJson.put("depth", result.get("depth"));
                array.add(resJson);
            }

            return new ResponseEntity<>(cf.crtMsg(StatusEnum.OK, "OK", array), headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage() + "\n" + e.getStackTrace());
            e.printStackTrace();
            return new ResponseEntity<>(cf.crtMsg(StatusEnum.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", ""), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @RequestMapping(value="/insert-data-group-info", method = RequestMethod.POST)
    public ResponseEntity<Message> insertLocationInfo(@RequestBody HashMap<String, Object> param){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        try{
            JSONObject json = new JSONObject(param);
            String parent_rawid = json.get("parent_rawid").toString();
            String location_name = json.get("location_name").toString();
            String depth = json.get("depth").toString();
            int result = TagEditorService.insertDataGroupInfo(parent_rawid, location_name, depth);

            return new ResponseEntity<>(cf.crtMsg(StatusEnum.OK, "OK", result), headers, HttpStatus.OK);
        }catch (Exception e){
            log.error(e.getMessage() + "\n" + e.getStackTrace());
            e.printStackTrace();
            return new ResponseEntity<>(cf.crtMsg(StatusEnum.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", ""), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @RequestMapping(value="/delete-data-group-info", method = RequestMethod.POST)
    public ResponseEntity<Message> deleteLocationInfo(@RequestBody HashMap<String, Object> param){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        try{
            JSONObject json = new JSONObject(param);
            String idx = json.get("idx").toString();

            LinkedList locationResult = TagEditorService.getDataGroupInfo();
            LinkedHashMap parentResult = null;

            ArrayList<Integer> parentArray = new ArrayList<Integer>();
            parentArray.add(Integer.parseInt(idx));
            for(int i =0; i < locationResult.size(); i++) {
                parentResult = (LinkedHashMap) locationResult.get(i);
                for (int j = 0; j < parentArray.size(); j++) {
                    if (parentArray.get(j).toString().equals(parentResult.get("parent_rawid"))) {
                        parentArray.add(Integer.valueOf((String) parentResult.get("idx")));
                    }
                }
            }

            int result = TagEditorService.deleteLocationInfo(idx, parentArray);

            return new ResponseEntity<>(cf.crtMsg(StatusEnum.OK, "OK", result), headers, HttpStatus.OK);
        }catch (Exception e){
            log.error(e.getMessage() + "\n" + e.getStackTrace());
            e.printStackTrace();
            return new ResponseEntity<>(cf.crtMsg(StatusEnum.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", ""), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @RequestMapping(value="/set-data-group-info", method = RequestMethod.POST)
    public ResponseEntity<Message> setLocationInfo(@RequestBody HashMap<String, Object> param){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        try{
            JSONObject json = new JSONObject(param);
            String key = json.get("key").toString();
            String newLocationName = json.get("newName").toString();

            int result = TagEditorService.setDataGroupInfo(key, newLocationName);

            return new ResponseEntity<>(cf.crtMsg(StatusEnum.OK, "OK", result), headers, HttpStatus.OK);
        }catch (Exception e){
            log.error(e.getMessage() + "\n" + e.getStackTrace());
            e.printStackTrace();
            return new ResponseEntity<>(cf.crtMsg(StatusEnum.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", ""), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @RequestMapping(value = "/edit-tag-layout", method = RequestMethod.POST)
    public ResponseEntity<Message> editTagLayout(@RequestBody JSONObject param, HttpServletResponse response) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        try {
            JSONObject json = new JSONObject(param);

            int editTagLayoutResult = TagEditorService.editTagLayout(json);

            if (editTagLayoutResult < 0)
                return new ResponseEntity<>(cf.crtMsg(StatusEnum.NO_CONTENT, "UNAUTHORIZED", ""), headers, HttpStatus.UNAUTHORIZED);
            return new ResponseEntity<>(cf.crtMsg(StatusEnum.OK, "OK", ""), headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage() + "\n" + e.getStackTrace());
            e.printStackTrace();
            return new ResponseEntity<>(cf.crtMsg(StatusEnum.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", ""), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/delete-tag-layout", method = RequestMethod.POST)
    public ResponseEntity<Message> deleteTagLayout(@RequestBody JSONObject param, HttpServletResponse response) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        try {
            JSONObject json = new JSONObject(param);



            int deleteTagLayoutResult = TagEditorService.deleteTagLayout(json);

            if (deleteTagLayoutResult < 0)
                return new ResponseEntity<>(cf.crtMsg(StatusEnum.NO_CONTENT, "UNAUTHORIZED", ""), headers, HttpStatus.UNAUTHORIZED);
            return new ResponseEntity<>(cf.crtMsg(StatusEnum.OK, "OK", ""), headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage() + "\n" + e.getStackTrace());
            e.printStackTrace();
            return new ResponseEntity<>(cf.crtMsg(StatusEnum.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", ""), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    @RequestMapping(value = "/register-font", method = RequestMethod.POST)
//    public ResponseEntity<Message> registerFont(@RequestBody JSONObject param, HttpServletResponse response) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
//        try {
//            JSONObject json = new JSONObject(param);
//            String url = json.get("fontUrl").toString();
//            String fontFamilyName = json.get("fontFamilyName").toString();
//
//            int registerFontResult = TagEditorService.registerFont(url,fontFamilyName);
//
//            if (registerFontResult < 0)
//                return new ResponseEntity<>(cf.crtMsg(StatusEnum.NO_CONTENT, "UNAUTHORIZED", ""), headers, HttpStatus.UNAUTHORIZED);
//            return new ResponseEntity<>(cf.crtMsg(StatusEnum.OK, "OK", ""), headers, HttpStatus.OK);
//        } catch (Exception e) {
//            log.error(e.getMessage() + "\n" + e.getStackTrace());
//            e.printStackTrace();
//            return new ResponseEntity<>(cf.crtMsg(StatusEnum.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", ""), headers, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
    @RequestMapping(value = "/font-list", method = RequestMethod.POST)
    public ResponseEntity<Message> getFontList(@RequestBody JSONObject param, HttpServletResponse response) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        try {


            LinkedList fontListResult = TagEditorService.fontList();
            if(fontListResult == null) return new ResponseEntity<>(cf.crtMsg(StatusEnum.NO_CONTENT, "UNAUTHORIZED",""), headers, HttpStatus.UNAUTHORIZED);

            JSONObject resJson = new JSONObject();
            resJson.put("fontList", fontListResult);

            return new ResponseEntity<>(cf.crtMsg(StatusEnum.OK, "OK", resJson), headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage() + "\n" + e.getStackTrace());
            e.printStackTrace();
            return new ResponseEntity<>(cf.crtMsg(StatusEnum.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", ""), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @RequestMapping(value = "/layout-list", method = RequestMethod.POST)
    public ResponseEntity<Message> getTagLayoutList(@RequestBody JSONObject param, HttpServletResponse response) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        try {

        	/*
        	int pageNumber = Integer.parseInt(StringUtils.defaultString(String.valueOf((Integer)param.get("pNumber")),"0"));
        	int pageSize = Integer.parseInt(StringUtils.defaultString(String.valueOf((Integer)param.get("pageSize")),"10"));
        	 */
        	
        	
        	String tagSize = StringUtils.defaultString((String)param.get("tagSize"));
        	        	
            LinkedList fontListResult = TagEditorService.tagLayoutList(tagSize); 
            if(fontListResult == null) return new ResponseEntity<>(cf.crtMsg(StatusEnum.NO_CONTENT, "UNAUTHORIZED",""), headers, HttpStatus.UNAUTHORIZED);

            JSONObject resJson = new JSONObject();
            resJson.put("tagLayoutList", fontListResult);

            return new ResponseEntity<>(cf.crtMsg(StatusEnum.OK, "OK", resJson), headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage() + "\n" + e.getStackTrace());
            e.printStackTrace();
            return new ResponseEntity<>(cf.crtMsg(StatusEnum.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", ""), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @RequestMapping(value = "/data-group", method = RequestMethod.POST)
    public ResponseEntity<Message> dataGroupList(@RequestBody JSONObject param, HttpServletResponse response) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        try {

            JSONObject json = new JSONObject(param);

            LinkedList fontListResult = TagEditorService.dataGroupList(json);
            if(fontListResult == null) return new ResponseEntity<>(cf.crtMsg(StatusEnum.NO_CONTENT, "UNAUTHORIZED",""), headers, HttpStatus.UNAUTHORIZED);

            JSONObject resJson = new JSONObject();
            resJson.put("dataGroupList", fontListResult);

            return new ResponseEntity<>(cf.crtMsg(StatusEnum.OK, "OK", resJson), headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage() + "\n" + e.getStackTrace());
            e.printStackTrace();
            return new ResponseEntity<>(cf.crtMsg(StatusEnum.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", ""), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}

