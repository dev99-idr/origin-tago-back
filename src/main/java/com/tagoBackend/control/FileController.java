package com.tagoBackend.control;

import com.tagoBackend.custom.CustomFunction;
import com.tagoBackend.response.Message;
import com.tagoBackend.response.StatusEnum;
import com.tagoBackend.service.FileService;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.Charset;
import java.util.HashMap;

@RestController
@RequestMapping(value = "/file")
public class FileController {
    CustomFunction cf = new CustomFunction();
    FileService fileService = new FileService();
    private final Logger log = LoggerFactory.getLogger(FileController.class);

    @RequestMapping(value = "/get-profile-img", method = RequestMethod.POST)
    public ResponseEntity<Message> getUserInfo(@RequestBody HashMap<String, Object> param) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        try {
            JSONObject json = new JSONObject(param);
            String id = json.get("id").toString();

            String result = fileService.getprofileImg(id);

            return new ResponseEntity<>(cf.crtMsg(StatusEnum.OK, "OK", result), headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage() + "\n" + e.getStackTrace());
            e.printStackTrace();
            return new ResponseEntity<>(cf.crtMsg(StatusEnum.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", ""), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/set-profile-img", method = RequestMethod.POST)
    public ResponseEntity<Message> setUserInfo(@RequestBody HashMap<String, Object> param) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        try {
            JSONObject json = new JSONObject(param);
            String id = json.get("id").toString();
            String newImg = json.get("newImg").toString();

            int result = fileService.setprofileImg(id, newImg);

            if(result < 0) {
                return new ResponseEntity<>(cf.crtMsg(StatusEnum.UNAUTHORIZED, "UNAUTHORIZED", ""), headers, HttpStatus.UNAUTHORIZED);
            }

            return new ResponseEntity<>(cf.crtMsg(StatusEnum.OK, "OK", ""), headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage() + "\n" + e.getStackTrace());
            e.printStackTrace();
            return new ResponseEntity<>(cf.crtMsg(StatusEnum.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", ""), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    @PostMapping("/excel-data")
//    public ResponseEntity<Message> ExcelData(@RequestBody HashMap<String, Object> param) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
//
//        try {
//            int result = fileService.excelData(param);
//
//            if(result < 0) {
//                return new ResponseEntity<>(cf.crtMsg(StatusEnum.UNAUTHORIZED, "UNAUTHORIZED", ""), headers, HttpStatus.UNAUTHORIZED);
//            }
//
//            return new ResponseEntity<>(cf.crtMsg(StatusEnum.OK, "OK", ""), headers, HttpStatus.OK);
//        } catch (Exception e) {
//            log.error(e.getMessage() + "\n" + e.getStackTrace());
//            e.printStackTrace();
//            return new ResponseEntity<>(cf.crtMsg(StatusEnum.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", ""), headers, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }


}
