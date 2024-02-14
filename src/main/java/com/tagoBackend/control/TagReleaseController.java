package com.tagoBackend.control;

import com.tagoBackend.custom.CustomFunction;
import com.tagoBackend.response.Message;
import com.tagoBackend.response.StatusEnum;
import com.tagoBackend.service.TagReleaseService;
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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

@RestController
@RequestMapping(value = "/tag-release")
public class TagReleaseController {
    TagReleaseService tagReleaseService = new TagReleaseService();

    CustomFunction cf = new CustomFunction();

    private final Logger log = LoggerFactory.getLogger(TagLocationController.class);

    @RequestMapping(value = "/get-tag-version", method = RequestMethod.POST)
    public ResponseEntity<Message> getTagVersion(@RequestBody HashMap<String, Object> param){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        try {
            JSONObject json = new JSONObject(param);
            LinkedList serviceResult = tagReleaseService.getTagVersion();

            LinkedHashMap result = null;
            JSONArray array = new JSONArray();

            for(int i = 0; i < serviceResult.size(); i++){
                result = (LinkedHashMap)serviceResult.get(i);

                JSONObject resJson = new JSONObject();
                resJson.put("version", result.get("version"));
                resJson.put("release_time", result.get("release_time"));
                array.add(resJson);
            }

            if(result == null) return new ResponseEntity<>(cf.crtMsg(StatusEnum.UNAUTHORIZED, "UNAUTHORIZED",""), headers, HttpStatus.UNAUTHORIZED);
            if(result.get("del_yn") == "Y") return new ResponseEntity<>(cf.crtMsg(StatusEnum.UNAUTHORIZED, "UNAUTHORIZED",""), headers, HttpStatus.UNAUTHORIZED);

            return new ResponseEntity<>(cf.crtMsg(StatusEnum.OK, "OK", array), headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage() + "\n" + e.getStackTrace());
            e.printStackTrace();
            return new ResponseEntity<>(cf.crtMsg(StatusEnum.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", ""), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @RequestMapping(value = "/get-tag-release", method = RequestMethod.POST)
    public ResponseEntity<Message> getTagRelease(@RequestBody HashMap<String, Object> param){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        try {
            JSONObject json = new JSONObject(param);
            LinkedList serviceResult = tagReleaseService.getTagRelease();

            LinkedHashMap result = null;
            JSONArray array = new JSONArray();

            for(int i = 0; i < serviceResult.size(); i++){
                result = (LinkedHashMap)serviceResult.get(i);

                JSONObject resJson = new JSONObject();
                resJson.put("version", result.get("version"));
                resJson.put("release_time", result.get("release_time"));

                array.add(resJson);
            }

            if(result == null) return new ResponseEntity<>(cf.crtMsg(StatusEnum.UNAUTHORIZED, "UNAUTHORIZED",""), headers, HttpStatus.UNAUTHORIZED);
            if(result.get("del_yn") == "Y") return new ResponseEntity<>(cf.crtMsg(StatusEnum.UNAUTHORIZED, "UNAUTHORIZED",""), headers, HttpStatus.UNAUTHORIZED);

            return new ResponseEntity<>(cf.crtMsg(StatusEnum.OK, "OK", array), headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage() + "\n" + e.getStackTrace());
            e.printStackTrace();
            return new ResponseEntity<>(cf.crtMsg(StatusEnum.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", ""), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
