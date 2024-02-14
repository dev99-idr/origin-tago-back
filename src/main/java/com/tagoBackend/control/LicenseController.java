package com.tagoBackend.control;

import com.tagoBackend.custom.CustomFunction;
import com.tagoBackend.response.Message;
import com.tagoBackend.response.StatusEnum;
import com.tagoBackend.service.LicenseService;
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

@RestController
@RequestMapping(value = "/license")
public class LicenseController {
    LicenseService licenseService = new LicenseService();

    CustomFunction cf = new CustomFunction();

    private final Logger log = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping(value = "/get-license", method = RequestMethod.POST)
    public ResponseEntity<Message> getLicense(@RequestBody HashMap<String, Object> param) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        try {
            JSONObject json = new JSONObject(param);
            String mac_address = json.get("mac_address").toString();

            LinkedHashMap result = licenseService.getLicense(mac_address);

            if (result == null) {
                return new ResponseEntity<>(cf.crtMsg(StatusEnum.UNAUTHORIZED, "UNAUTHORIZED", ""), headers, HttpStatus.UNAUTHORIZED);
            }

            JSONObject resJson = new JSONObject();
            resJson.put("license_key", result.get("license_key"));
            resJson.put("mac_address", result.get("mac_address"));
            resJson.put("active_yn", result.get("active_yn"));
            resJson.put("active_time", result.get("active_time"));

            return new ResponseEntity<>(cf.crtMsg(StatusEnum.OK, "OK", resJson), headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage() + "\n" + e.getStackTrace());
            e.printStackTrace();
            return new ResponseEntity<>(cf.crtMsg(StatusEnum.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", ""), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/license-list", method = RequestMethod.POST)
    public ResponseEntity<Message> licenseList(@RequestBody HashMap<String, Object> param) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        try {
            LinkedHashMap result = licenseService.licenseList();

            if (result == null) {
                return new ResponseEntity<>(cf.crtMsg(StatusEnum.UNAUTHORIZED, "UNAUTHORIZED", ""), headers, HttpStatus.UNAUTHORIZED);
            }

            JSONObject resJson = new JSONObject();
            resJson.put("license_key", result.get("license_key"));
            resJson.put("mac_address", result.get("mac_address"));
            resJson.put("active_yn", result.get("active_yn"));
            resJson.put("active_time", result.get("active_time"));

            return new ResponseEntity<>(cf.crtMsg(StatusEnum.OK, "OK", resJson), headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage() + "\n" + e.getStackTrace());
            e.printStackTrace();
            return new ResponseEntity<>(cf.crtMsg(StatusEnum.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", ""), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/set-license", method = RequestMethod.POST)
    public ResponseEntity<Message> setLicense(@RequestBody HashMap<String, Object> param){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        try{
            JSONObject json = new JSONObject(param);
            String mac_address = json.get("mac_address").toString();
            String license_key = json.get("license_key").toString();
            String active_time = json.get("active_time").toString();

            int result = licenseService.setLicense(mac_address, license_key, active_time);

            return new ResponseEntity<>(cf.crtMsg(StatusEnum.OK, "OK", result), headers, HttpStatus.OK);
        }catch (Exception e){
            log.error(e.getMessage() + "\n" + e.getStackTrace());
            e.printStackTrace();
            return new ResponseEntity<>(cf.crtMsg(StatusEnum.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", ""), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @RequestMapping(value = "/insert-license", method = RequestMethod.POST)
    public ResponseEntity<Message> insertLicense(@RequestBody HashMap<String, Object> param){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        try{
            JSONObject json = new JSONObject(param);

            String license_key = json.get("license_key").toString();
            String mac_address = json.get("mac_address").toString();
            String active_yn = json.get("active_yn").toString();

            int result = licenseService.insertLicense(license_key, mac_address, active_yn);

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
