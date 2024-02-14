package com.tagoBackend.control;

import java.nio.charset.Charset;
import java.util.*;

import com.tagoBackend.custom.CustomFunction;
import com.tagoBackend.response.Message;
import com.tagoBackend.response.StatusEnum;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tagoBackend.service.*;

// For Login API
@RestController
@RequestMapping(value = "/tag-register")
public class TagRegisterController {
    TagRegisterService TagRegisterService = new TagRegisterService();

    CustomFunction cf = new CustomFunction();

    private final Logger log = LoggerFactory.getLogger(TagRegisterController.class);

    @RequestMapping(value="/register", method = RequestMethod.POST)
    public ResponseEntity<Message> register(@RequestBody JSONObject param) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        try {
            JSONObject json = new JSONObject(param);
            JSONObject insertData = new JSONObject((LinkedHashMap) json.get("insertData"));
            JSONObject commonData = new JSONObject((LinkedHashMap) insertData.get("commonData"));

            ArrayList collectionData = (ArrayList) insertData.get("collectionData");
            JSONArray collectionDataArray = new JSONArray();

            ArrayList commandData = (ArrayList) insertData.get("commandData");
            JSONArray commandDataArray = new JSONArray();

            for(Object obj : collectionData){
                collectionDataArray.add(obj);
            }
//            for(Object obj : commandData){
//                commandDataArray.add(obj);
//            }
            int result = TagRegisterService.tagInsert(commonData,collectionDataArray);

            if (result < 1)
                return new ResponseEntity<>(cf.crtMsg(StatusEnum.BAD_REQUEST, "BAD_REQUEST", ""), headers, HttpStatus.BAD_REQUEST);

            return new ResponseEntity<>(cf.crtMsg(StatusEnum.OK, "OK", ""), headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage() + "\n" + e.getStackTrace());
            e.printStackTrace();
            return new ResponseEntity<>(cf.crtMsg(StatusEnum.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", ""), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @RequestMapping(value="/edit", method = RequestMethod.POST)
    public ResponseEntity<Message> edit(@RequestBody JSONObject param) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        try {
            JSONObject json = new JSONObject(param);
            JSONObject insertData = new JSONObject((LinkedHashMap) json.get("insertData"));
            JSONObject commonData = new JSONObject((LinkedHashMap) insertData.get("commonData"));

            ArrayList collectionData = (ArrayList) insertData.get("collectionData");
            JSONArray collectionDataArray = new JSONArray();

            for(Object obj : collectionData){
                collectionDataArray.add(obj);
            }

            int result = TagRegisterService.tagEdit(commonData,collectionDataArray);

            if (result < 1)
                return new ResponseEntity<>(cf.crtMsg(StatusEnum.BAD_REQUEST, "BAD_REQUEST", ""), headers, HttpStatus.BAD_REQUEST);

            return new ResponseEntity<>(cf.crtMsg(StatusEnum.OK, "OK", ""), headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage() + "\n" + e.getStackTrace());
            e.printStackTrace();
            return new ResponseEntity<>(cf.crtMsg(StatusEnum.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", ""), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @RequestMapping(value="/check-serialno", method = RequestMethod.POST)
    public ResponseEntity<Message> chkSerialNumber(@RequestBody JSONObject param) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        try {
            JSONObject json = new JSONObject(param);
            String serialNumber = (String) json.get("serialNumber");

            
            int result = TagRegisterService.chkSerialNumber(serialNumber);

            /*
            if (result > 1)
                return new ResponseEntity<>(cf.crtMsg(StatusEnum.BAD_REQUEST, "BAD_REQUEST", ""), headers, HttpStatus.BAD_REQUEST);
                */

            return new ResponseEntity<>(cf.crtMsg(StatusEnum.OK, "OK", result), headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage() + "\n" + e.getStackTrace());
            e.printStackTrace();
            return new ResponseEntity<>(cf.crtMsg(StatusEnum.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", ""), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
