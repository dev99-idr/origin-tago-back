package com.tagoBackend.control;

import com.tagoBackend.custom.CustomFunction;
import com.tagoBackend.response.Message;
import com.tagoBackend.response.StatusEnum;
import com.tagoBackend.service.UserService;
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
@RequestMapping(value = "/user")
public class UserController {
    UserService userService = new UserService();

    CustomFunction cf = new CustomFunction();

    private final Logger log = LoggerFactory.getLogger(UserController.class);

    @RequestMapping(value = "/get-user-info", method = RequestMethod.POST)
    public ResponseEntity<Message> getUserInfo(@RequestBody HashMap<String, Object> param) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        try {
            JSONObject json = new JSONObject(param);
            String user_id = json.get("id").toString();

            LinkedHashMap result = userService.getUserInfo(user_id);

            if (result == null){
                return new ResponseEntity<>(cf.crtMsg(StatusEnum.UNAUTHORIZED, "UNAUTHORIZED", ""), headers, HttpStatus.UNAUTHORIZED);
            }
            if (result.get("del_yn") == "Y"){
                return new ResponseEntity<>(cf.crtMsg(StatusEnum.UNAUTHORIZED, "UNAUTHORIZED", ""), headers, HttpStatus.UNAUTHORIZED);
            }

            JSONObject resJson = new JSONObject();
            resJson.put("user_id", result.get("user_id"));
            resJson.put("user_name", result.get("user_name"));
            resJson.put("user_email", result.get("user_email"));
            resJson.put("user_pw", result.get("user_pw"));
            resJson.put("user_authority", result.get("user_authority"));

            return new ResponseEntity<>(cf.crtMsg(StatusEnum.OK, "OK", resJson), headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage() + "\n" + e.getStackTrace());
            e.printStackTrace();
            return new ResponseEntity<>(cf.crtMsg(StatusEnum.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", ""), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @RequestMapping(value = "/get-all-user-info", method = RequestMethod.POST)
    public ResponseEntity<Message> getAllUserInfo(@RequestBody HashMap<String, Object> param) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        try {
            JSONObject json = new JSONObject(param);
            String start = json.get("start").toString();
            String end = json.get("end").toString();

            LinkedList serviceResult = userService.getAllUserInfo(start, end);

            LinkedHashMap result = null;
            JSONArray array = new JSONArray();

            for(int i = 0; i < serviceResult.size(); i++) {
                result = (LinkedHashMap)serviceResult.get(i);

                int userAuth = Integer.valueOf((String)result.get("user_authority"));
                String strUserAuth = "";

                if(userAuth == 2 ) {
                    strUserAuth ="최고 관리자";
                }
                else if(userAuth == 1) {
                    strUserAuth = "일반 관리자";
                }

                JSONObject resJson = new JSONObject();
                resJson.put("idx", result.get("idx"));
                resJson.put("user_name", result.get("user_name"));
                resJson.put("user_authority", strUserAuth);
                resJson.put("user_id", result.get("user_id"));
                resJson.put("user_email", result.get("user_email"));

                array.add(resJson);
            }

            if(result == null) {
                return new ResponseEntity<>(cf.crtMsg(StatusEnum.UNAUTHORIZED, "UNAUTHORIZED",""), headers, HttpStatus.UNAUTHORIZED);
            }
            if(result.get("del_yn") == "Y") {
                return new ResponseEntity<>(cf.crtMsg(StatusEnum.UNAUTHORIZED, "UNAUTHORIZED",""), headers, HttpStatus.UNAUTHORIZED);
            }

            return new ResponseEntity<>(cf.crtMsg(StatusEnum.OK, "OK", array), headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage() + "\n" + e.getStackTrace());
            e.printStackTrace();
            return new ResponseEntity<>(cf.crtMsg(StatusEnum.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", ""), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value="/set-user-info", method = RequestMethod.POST)
    public ResponseEntity<Message> setUserInfo(@RequestBody HashMap<String, Object> param){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        try{
            JSONObject json = new JSONObject(param);
            String user_id = json.get("id").toString();
            String user_name = json.get("newName").toString();
            String user_pw = json.get("newPw").toString();
            String user_email = json.get("newEmail").toString();

            int result = userService.setUserInfo(user_id, user_name, user_pw, user_email);

            return new ResponseEntity<>(cf.crtMsg(StatusEnum.OK, "OK", result), headers, HttpStatus.OK);
        }catch (Exception e){
            log.error(e.getMessage() + "\n" + e.getStackTrace());
            e.printStackTrace();
            return new ResponseEntity<>(cf.crtMsg(StatusEnum.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", ""), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/delete-user-info", method = RequestMethod.POST)
    public ResponseEntity<Message> deleteUserInfo(@RequestBody HashMap<String, Object> param){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        try{
            JSONObject json = new JSONObject(param);
            String user_id = json.get("id").toString();;

            int result = userService.deleteUserInfo(user_id);

            return new ResponseEntity<>(cf.crtMsg(StatusEnum.OK, "OK", result), headers, HttpStatus.OK);
        }catch (Exception e){
            log.error(e.getMessage() + "\n" + e.getStackTrace());
            e.printStackTrace();
            return new ResponseEntity<>(cf.crtMsg(StatusEnum.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", ""), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/count-user", method = RequestMethod.POST)
    public ResponseEntity<Message> getCountUser() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        try {
            int count = userService.getCountUser();

            if(count < 1) {
                return new ResponseEntity<>(cf.crtMsg(StatusEnum.BAD_REQUEST, "BAD_REQUEST",""), headers, HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<>(cf.crtMsg(StatusEnum.OK, "OK", count), headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage() + "\n" + e.getStackTrace());
            e.printStackTrace();
            return new ResponseEntity<>(cf.crtMsg(StatusEnum.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", ""), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


//    @RequestMapping(value="/setUserAuth", method = RequestMethod.POST)
//    public ResponseEntity<Message> setUserAuth(@RequestBody HashMap<String, Object> param){
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
//
//        try{
//            JSONObject json = new JSONObject(param);
//            String idx = json.get("idx").toString().replace("[", "").replace("]","");
//            String auth = json.get("auth").toString();
//
//            int result = userService.setUserAuth(idx, auth);
//
//            return new ResponseEntity<>(cf.crtMsg(StatusEnum.OK, "OK", result), headers, HttpStatus.OK);
//        }catch (Exception e){
//            log.error(e.getMessage() + "\n" + e.getStackTrace());
//            e.printStackTrace();
//            return new ResponseEntity<>(cf.crtMsg(StatusEnum.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", ""), headers, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

}
