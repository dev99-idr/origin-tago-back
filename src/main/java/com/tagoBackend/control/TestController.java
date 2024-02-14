package com.tagoBackend.control;

//// For Login API
//@RestController
//@RequestMapping(value = "/test")
//public class TestController {
//    LoginService loginService = new LoginService();
//
//    CustomFunction cf = new CustomFunction();
//
//    private final Logger log = LoggerFactory.getLogger(LoginController.class);
//
//    @RequestMapping(value="/sign-in", method = RequestMethod.POST)
//    public ResponseEntity<Message> signIn(@RequestBody HashMap<String, Object> param){
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
//
//        try{
//
//            JSONObject json = new JSONObject(param);
//            String id = json.get("id").toString();
//            String pw = json.get("pw").toString();
//
//            LinkedHashMap result = loginService.signin(id, pw);
//
//            if(result == null) return new ResponseEntity<>(cf.crtMsg(StatusEnum.UNAUTHORIZED, "UNAUTHORIZED",""), headers, HttpStatus.UNAUTHORIZED);
//            if(result.get("del_yn") == "Y") return new ResponseEntity<>(cf.crtMsg(StatusEnum.UNAUTHORIZED, "UNAUTHORIZED",""), headers, HttpStatus.UNAUTHORIZED);
//
//            JSONObject resJson = new JSONObject();
//            resJson.put("id", result.get("user_id"));
//            resJson.put("name", result.get("user_name"));
//            resJson.put("email", result.get("user_email"));
//
//
//            return new ResponseEntity<>(cf.crtMsg(StatusEnum.OK, "OK", resJson), headers, HttpStatus.OK);
//        }catch (Exception e){
//            log.error(e.getMessage() + "\n" + e.getStackTrace());
//            e.printStackTrace();
//            return new ResponseEntity<>(cf.crtMsg(StatusEnum.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", ""), headers, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
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
//}
