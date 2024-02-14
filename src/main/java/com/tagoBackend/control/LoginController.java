package com.tagoBackend.control;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tagoBackend.custom.CustomFunction;
import com.tagoBackend.response.Message;
import com.tagoBackend.response.StatusEnum;
import com.tagoBackend.service.LoginService;


// For Login API
@RestController
@RequestMapping(value = "/login")
public class LoginController {
    LoginService loginService = new LoginService();

    CustomFunction cf = new CustomFunction();

    private final Logger log = LoggerFactory.getLogger(LoginController.class);

    @PostMapping("/signin")
//    @RequestMapping(value = "/signin", method = RequestMethod.POST)
    public ResponseEntity<Message> signIn(@RequestBody HashMap<String, Object> param) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        try {
            JSONObject json = new JSONObject(param);
            String id = json.get("id").toString();
            String pw = json.get("pw").toString();

            LinkedHashMap result = loginService.signin(id, pw);

            if (result == null) {
                return new ResponseEntity<>(cf.crtMsg(StatusEnum.UNAUTHORIZED, "UNAUTHORIZED", ""), headers, HttpStatus.UNAUTHORIZED);
            }
            if (result.get("del_yn") == "Y") {
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

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public ResponseEntity<Message> register(@RequestBody HashMap<String, Object> param){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        try{
            JSONObject json = new JSONObject(param);
            String user_id = json.get("id").toString();
            String user_pw = json.get("pw").toString();
            String user_authority = json.get("authority").toString();

            int result = loginService.signup(user_id, user_pw, user_authority);

            if(result < 1) {
                return new ResponseEntity<>(cf.crtMsg(StatusEnum.BAD_REQUEST, "BAD_REQUEST",""), headers, HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<>(cf.crtMsg(StatusEnum.OK, "OK", ""), headers, HttpStatus.OK);
        }catch (Exception e){
            log.error(e.getMessage() + "\n" + e.getStackTrace());
            e.printStackTrace();
            return new ResponseEntity<>(cf.crtMsg(StatusEnum.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", ""), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/get-user", method = RequestMethod.POST)
    public ResponseEntity<Message> getUser(@RequestBody HashMap<String, Object> param) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        try {
            JSONObject json = new JSONObject(param);
            String user_id = json.get("id").toString();

            LinkedHashMap result = loginService.getUser(user_id);

            if (result == null) {
                return new ResponseEntity<>(cf.crtMsg(StatusEnum.UNAUTHORIZED, "UNAUTHORIZED", ""), headers, HttpStatus.UNAUTHORIZED);
            }
            if (result.get("del_yn") == "Y") {
                return new ResponseEntity<>(cf.crtMsg(StatusEnum.UNAUTHORIZED, "UNAUTHORIZED", ""), headers, HttpStatus.UNAUTHORIZED);
            }

            JSONObject resJson = new JSONObject();
            resJson.put("user_id", result.get("user_id"));
            resJson.put("user_name", result.get("user_name"));
            resJson.put("user_email", result.get("user_email"));
            resJson.put("user_authority", result.get("user_authority"));

            return new ResponseEntity<>(cf.crtMsg(StatusEnum.OK, "OK", resJson), headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage() + "\n" + e.getStackTrace());
            e.printStackTrace();
            return new ResponseEntity<>(cf.crtMsg(StatusEnum.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", ""), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/get-login-log", method = RequestMethod.POST)
    public ResponseEntity<Message> getLoginLog(@RequestBody HashMap<String, Object> param) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        try {
            JSONObject json = new JSONObject(param);
            String user_id = json.get("id").toString();
            String user_authority = json.get("authority").toString();
            String sortByTime = json.get("sortByTime").toString();

            LinkedList loginInfoResult = loginService.getLoginLog(user_id, user_authority, sortByTime);

            LinkedHashMap result = null;

            JSONArray array = new JSONArray();

            for(int i = 0; i < loginInfoResult.size(); i++) {
                result = (LinkedHashMap)loginInfoResult.get(i);

                JSONObject resJson = new JSONObject();
                resJson.put("user_id", result.get("user_id"));
                resJson.put("user_name", result.get("user_name"));
                resJson.put("user_email", result.get("user_email"));
                resJson.put("user_authority", result.get("user_authority"));
                resJson.put("login_time", result.get("login_time"));
                resJson.put("login_yn", result.get("login_yn"));

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

    @RequestMapping(value = "/set-login-log", method = RequestMethod.POST)
    public ResponseEntity<Message> loginLog(@RequestBody HashMap<String, Object> param){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        try{
            JSONObject json = new JSONObject(param);
            String user_id = json.get("id").toString();
            String user_name = json.get("name").toString();
            String user_email = json.get("email").toString();
            String user_authority = json.get("authority").toString();
            String login_time = json.get("time").toString();
            String login_yn = json.get("login_yn").toString();

            int result = loginService.loginLog(user_id, user_name, user_email, user_authority, login_time, login_yn);

            if(result < 1) {
                return new ResponseEntity<>(cf.crtMsg(StatusEnum.BAD_REQUEST, "BAD_REQUEST",""), headers, HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<>(cf.crtMsg(StatusEnum.OK, "OK", ""), headers, HttpStatus.OK);
        }catch (Exception e){
            log.error(e.getMessage() + "\n" + e.getStackTrace());
            e.printStackTrace();
            return new ResponseEntity<>(cf.crtMsg(StatusEnum.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", ""), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

//    @RequestMapping(value="/autosignin", method = RequestMethod.POST)
//    public ResponseEntity<Message> autoSignIn(@RequestBody HashMap<String, Object> param){
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
//
//        try{
//            JSONObject json = new JSONObject(param);
//            String id = json.get("id").toString();
//
//            LinkedHashMap result = loginService.autoSignIn(id);
//
//            if(result == null) return new ResponseEntity<>(cf.crtMsg(StatusEnum.UNAUTHORIZED, "UNAUTHORIZED",""), headers, HttpStatus.UNAUTHORIZED);
//
//            if(result.get("del_yn") == "Y") return new ResponseEntity<>(cf.crtMsg(StatusEnum.UNAUTHORIZED, "UNAUTHORIZED",""), headers, HttpStatus.UNAUTHORIZED);
//
//            JSONObject resJson = new JSONObject();
//            resJson.put("type", result.get("user_type"));
//            resJson.put("name", result.get("user_name"));
//            resJson.put("tel", result.get("user_tel"));
//            resJson.put("mail", result.get("user_mail"));
//            resJson.put("dept", result.get("dept_name"));
//            resJson.put("start_hour", result.get("start_hour"));
//            resJson.put("start_min", result.get("start_min"));
//            resJson.put("end_hour", result.get("end_hour"));
//            resJson.put("end_min", result.get("end_min"));
//
//            return new ResponseEntity<>(cf.crtMsg(StatusEnum.OK, "OK", resJson), headers, HttpStatus.OK);
//        }catch (Exception e){
//            log.error(e.getMessage() + "\n" + e.getStackTrace());
//            e.printStackTrace();
//            return new ResponseEntity<>(cf.crtMsg(StatusEnum.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", ""), headers, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    @RequestMapping(value="/findid", method = RequestMethod.POST)
//    public ResponseEntity<Message> findId(@RequestBody HashMap<String, Object> param){
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
//
//        try{
//            JSONObject json = new JSONObject(param);
//            String name = json.get("name").toString();
//            String mail = json.get("mail").toString();
//
//            String result = loginService.findIdByNameAndMail(name, mail);
//
//            if(result == null){
//                return new ResponseEntity<>(cf.crtMsg(StatusEnum.NOT_FOUND, "NOT FOUND",""), headers, HttpStatus.NOT_FOUND);
//            }
//
//            return new ResponseEntity<>(cf.crtMsg(StatusEnum.OK, "OK", result), headers, HttpStatus.OK);
//        }catch (Exception e){
//            log.error(e.getMessage() + "\n" + e.getStackTrace());
//            e.printStackTrace();
//            return new ResponseEntity<>(cf.crtMsg(StatusEnum.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", ""), headers, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    @RequestMapping(value="/authid", method = RequestMethod.POST)
//    public ResponseEntity<Message> authId(@RequestBody HashMap<String, Object> param){
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
//
//        try{
//            JSONObject json = new JSONObject(param);
//            String id = json.get("id").toString();
//            String name = json.get("name").toString();
//            String mail = json.get("mail").toString();
//
//            int result = loginService.authByIdAndNameAndMail(id, name, mail);
//
//            if(result < 1){
//                return new ResponseEntity<>(cf.crtMsg(StatusEnum.UNAUTHORIZED, "UNAUTHORIZED",""), headers, HttpStatus.UNAUTHORIZED);
//            }else if( result < 0){
//                throw new Exception();
//            }
//
//            return new ResponseEntity<>(cf.crtMsg(StatusEnum.OK, "OK", ""), headers, HttpStatus.OK);
//        }catch (Exception e){
//            log.error(e.getMessage() + "\n" + e.getStackTrace());
//            e.printStackTrace();
//            return new ResponseEntity<>(cf.crtMsg(StatusEnum.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", ""), headers, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    @RequestMapping(value="/updatepw", method = RequestMethod.POST)
//    public ResponseEntity<Message> updatePw(@RequestBody HashMap<String, Object> param){
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
//
//        try{
//            JSONObject json = new JSONObject(param);
//            String id = json.get("id").toString();
//            String pw = json.get("pw").toString();
//
//            int result = loginService.updatePwById(id, pw);
//
//            if(result < 1) return new ResponseEntity<>(cf.crtMsg(StatusEnum.UNAUTHORIZED, "UNAUTHORIZED",""), headers, HttpStatus.UNAUTHORIZED);
//
//            return new ResponseEntity<>(cf.crtMsg(StatusEnum.OK, "OK", ""), headers, HttpStatus.OK);
//        }catch (Exception e){
//            log.error(e.getMessage() + "\n" + e.getStackTrace());
//            e.printStackTrace();
//            return new ResponseEntity<>(cf.crtMsg(StatusEnum.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", ""), headers, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    // ** Sign Up **
//    @RequestMapping(value="/signup", method = RequestMethod.POST)
//    public ResponseEntity<Message> signup(@RequestBody HashMap<String, Object> param) throws Exception{
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
//
//        try{
//            JSONObject json = new JSONObject(param);
//            String id = json.get("id").toString();
//            String pw = json.get("pw").toString();
//            String name = json.get("name").toString();
//            String tel = json.get("tel").toString();
//            String mail = json.get("mail").toString();
//            String dept = json.get("dept").toString();
//            String startHour = json.get("starthour").toString();
//            String startMin = json.get("startmin").toString();
//            String endHour = json.get("endhour").toString();
//            String endMin = json.get("endmin").toString();
//
//            int result = loginService.userInsert(id, pw, name, tel, mail, dept, startHour, startMin, endHour, endMin);
//
//            if(result < 1) return new ResponseEntity<>(cf.crtMsg(StatusEnum.BAD_REQUEST, "BAD_REQUEST",""), headers, HttpStatus.BAD_REQUEST);
//
//            return new ResponseEntity<>(cf.crtMsg(StatusEnum.OK, "OK", ""), headers, HttpStatus.OK);
//        }catch (Exception e){
//            log.error(e.getMessage() + "\n" + e.getStackTrace());
//            e.printStackTrace();
//            return new ResponseEntity<>(cf.crtMsg(StatusEnum.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", ""), headers, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
