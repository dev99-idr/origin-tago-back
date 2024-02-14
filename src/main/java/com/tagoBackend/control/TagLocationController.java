package com.tagoBackend.control;

import com.tagoBackend.custom.CustomFunction;
import com.tagoBackend.response.Message;
import com.tagoBackend.response.StatusEnum;
import com.tagoBackend.service.TagLocationService;
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

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

@RestController
@RequestMapping(value = "/tag-location")
public class TagLocationController {
    TagLocationService tagLocationService = new TagLocationService();

    CustomFunction cf = new CustomFunction();

    private final Logger log = LoggerFactory.getLogger(TagLocationController.class);

    @RequestMapping(value = "/get-location-info", method = RequestMethod.POST)
    public ResponseEntity<Message> getLocationInfo(@RequestBody HashMap<String, Object> param) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        try {
            LinkedList LocationResult = tagLocationService.getLocationInfo();

            if (LocationResult == null) {
                return new ResponseEntity<>(cf.crtMsg(StatusEnum.UNAUTHORIZED, "UNAUTHORIZED", ""), headers, HttpStatus.UNAUTHORIZED);
            }

            LinkedHashMap result = null;
            JSONArray array = new JSONArray();

            for(int i = 0; i < LocationResult.size(); i++) {
                result = (LinkedHashMap)LocationResult.get(i);

                JSONObject resJson = new JSONObject();
                resJson.put("idx", result.get("idx"));
                resJson.put("location_name", result.get("location_name"));
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

    @RequestMapping(value="/set-location-info", method = RequestMethod.POST)
    public ResponseEntity<Message> setLocationInfo(@RequestBody HashMap<String, Object> param){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        try{
            JSONObject json = new JSONObject(param);
            String key = json.get("key").toString();
            String newLocationName = json.get("newName").toString();

            int result = tagLocationService.setLocationInfo(key, newLocationName);

            return new ResponseEntity<>(cf.crtMsg(StatusEnum.OK, "OK", result), headers, HttpStatus.OK);
        }catch (Exception e){
            log.error(e.getMessage() + "\n" + e.getStackTrace());
            e.printStackTrace();
            return new ResponseEntity<>(cf.crtMsg(StatusEnum.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", ""), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value="/insert-location-info", method = RequestMethod.POST)
    public ResponseEntity<Message> insertLocationInfo(@RequestBody HashMap<String, Object> param){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        try{
            JSONObject json = new JSONObject(param);
            String parent_rawid = json.get("parent_rawid").toString();
            String location_name = json.get("location_name").toString();
            String depth = json.get("depth").toString();

            int result = tagLocationService.insertLocationInfo(parent_rawid, location_name, depth);

            return new ResponseEntity<>(cf.crtMsg(StatusEnum.OK, "OK", result), headers, HttpStatus.OK);
        }catch (Exception e){
            log.error(e.getMessage() + "\n" + e.getStackTrace());
            e.printStackTrace();
            return new ResponseEntity<>(cf.crtMsg(StatusEnum.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", ""), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value="/delete-location-info", method = RequestMethod.POST)
    public ResponseEntity<Message> deleteLocationInfo(@RequestBody HashMap<String, Object> param){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        try{
            JSONObject json = new JSONObject(param);
            String idx = json.get("idx").toString();

            LinkedList locationResult = tagLocationService.getLocationInfo();
            LinkedHashMap parentResult = null;

            ArrayList<Integer> parentArray = new ArrayList<Integer>();

            parentArray.add(Integer.parseInt(idx));

            for(int i = 0; i < locationResult.size(); i++) {
                parentResult = (LinkedHashMap) locationResult.get(i);

                for (int j = 0; j < parentArray.size(); j++) {
                    if (parentArray.get(j).toString().equals(parentResult.get("parent_rawid"))) {
                        parentArray.add(Integer.valueOf((String) parentResult.get("idx")));
                    }
                }

            }

            int result = tagLocationService.deleteLocationInfo(idx, parentArray);

            return new ResponseEntity<>(cf.crtMsg(StatusEnum.OK, "OK", result), headers, HttpStatus.OK);
        }catch (Exception e){
            log.error(e.getMessage() + "\n" + e.getStackTrace());
            e.printStackTrace();
            return new ResponseEntity<>(cf.crtMsg(StatusEnum.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", ""), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/tag-info", method = RequestMethod.POST)
    public ResponseEntity<Message> getTagInfo(@RequestBody HashMap<String, Object> param) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        try {
            JSONObject json = new JSONObject(param);
            String start = json.get("start").toString();
            String end = json.get("end").toString();
            String idx = json.get("idx").toString();
            String searchCriteria = json.get("searchCriteria").toString();
            String searchText = json.get("searchText").toString();

            LinkedList locationResult = tagLocationService.getLocationInfo();
            LinkedHashMap parentResult = null;

            ArrayList<Integer> parentArray = new ArrayList<Integer>();
            ArrayList<String> searchTextArray = new ArrayList<String>();

            parentArray.add(Integer.parseInt(idx));

            for(Object obj : locationResult){
                parentResult = (LinkedHashMap) obj;

                for (int j = 0; j < parentArray.size(); j++) {
                    if (parentArray.get(j).toString().equals(parentResult.get("parent_rawid"))) {
                        parentArray.add(Integer.valueOf((String) parentResult.get("idx")));
                    }
                }

                searchTextArray.add(searchText);
            }

            LinkedList tagResult = tagLocationService.getTagInfo(start, end, searchCriteria, searchTextArray, parentArray);

            if (tagResult == null) {
                return new ResponseEntity<>(cf.crtMsg(StatusEnum.UNAUTHORIZED, "UNAUTHORIZED", ""), headers, HttpStatus.UNAUTHORIZED);
            }
            if (locationResult == null) {
                return new ResponseEntity<>(cf.crtMsg(StatusEnum.UNAUTHORIZED, "UNAUTHORIZED", ""), headers, HttpStatus.UNAUTHORIZED);
            }

            LinkedHashMap result = null;

            JSONArray array = new JSONArray();

            for(int i = 0; i < tagResult.size(); i++) {
                result = (LinkedHashMap) tagResult.get(i);

                JSONObject resJson = new JSONObject();

                for(int j = 0; j < parentArray.size(); j++) {
                    if(parentArray.get(j).toString().equals(result.get("tag_location"))){
                        resJson.put("idx", result.get("idx"));
                        resJson.put("tag_name", result.get("tag_name"));
                        resJson.put("tag_thing_id", result.get("tag_thing_id"));
                        resJson.put("tag_location", result.get("tag_location"));
                        resJson.put("tag_wakeup_prd", result.get("tag_wakeup_prd"));
                        resJson.put("location_name", result.get("location_name"));

                        array.add(resJson);
                    }
                }
            }

            return new ResponseEntity<>(cf.crtMsg(StatusEnum.OK, "OK", array), headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage() + "\n" + e.getStackTrace());
            e.printStackTrace();
            return new ResponseEntity<>(cf.crtMsg(StatusEnum.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", ""), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value="/count-location", method = RequestMethod.POST)
    public ResponseEntity<Message> countLocation(@RequestBody HashMap<String, Object> param){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        try{
            JSONObject json = new JSONObject(param);
            String idx = json.get("idx").toString();
            String searchCriteria = json.get("searchCriteria").toString();
            String searchText = json.get("searchText").toString();

            LinkedList locationResult = tagLocationService.getLocationInfo();

            LinkedHashMap parentResult = null;

            ArrayList<Integer> parentArray = new ArrayList<Integer>();
            ArrayList<String> searchTextArray = new ArrayList<String>();

            parentArray.add(Integer.parseInt(idx));

            for(Object obj : locationResult){
                parentResult = (LinkedHashMap) obj;

                for (int j = 0; j < parentArray.size(); j++) {
                    if (parentArray.get(j).toString().equals(parentResult.get("parent_rawid"))) {
                        parentArray.add(Integer.valueOf((String) parentResult.get("idx")));
                    }
                }

                searchTextArray.add(searchText);
            }

            int count = tagLocationService.countLocation(searchCriteria, parentArray, searchTextArray);

            if(count < 1) {
                return new ResponseEntity<>(cf.crtMsg(StatusEnum.BAD_REQUEST, "BAD_REQUEST",""), headers, HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<>(cf.crtMsg(StatusEnum.OK, "OK", count), headers, HttpStatus.OK);
        }catch (Exception e){
            log.error(e.getMessage() + "\n" + e.getStackTrace());
            e.printStackTrace();
            return new ResponseEntity<>(cf.crtMsg(StatusEnum.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", ""), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
