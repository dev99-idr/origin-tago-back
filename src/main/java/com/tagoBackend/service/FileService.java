package com.tagoBackend.service;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;

public class FileService {

    @Autowired
    private HttpServletRequest request;

    public String getprofileImg(String id) {
        String realPath = this.getClass().getResource("/").getPath();
        String filePath = realPath.substring(1,realPath.indexOf("build")) + "src/main/resources/webapp/img/";
        File filePathAndName = new File(filePath + id + "_img.png");
        String fileName = "";

        if(filePathAndName.exists() == true){
            fileName = id + "_img.png";
        }
        else{
            fileName = "user.png";
        }

        try {
            File file = new File(filePath + fileName);
            byte[] bt = new byte[(int)file.length()];
            FileInputStream inputStream = new FileInputStream(file);
            inputStream.read(bt);
            String imgBase64 = new String(Base64.encodeBase64(bt));
            inputStream.close();

            return imgBase64;
        } catch (Exception e) {
            return "";
        }
    }

    public int setprofileImg(String id, String newImg) {
        String realPath = this.getClass().getResource("/").getPath();
        String filePath = realPath.substring(1,realPath.indexOf("build")) + "src/main/resources/webapp/img/";
        String fileName = id + "_img";
        newImg = newImg.replace("data:image/png;base64", "");
        fileName += ".png";

        try {
            File file = new File(filePath + fileName);
            byte[] decode = Base64.decodeBase64(newImg);
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(decode);
            outputStream.close();

            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

//    public int excelData(HashMap param) {
//        try {
//            System.out.println(param.values());
//            for(int i = 0; i < param.size(); i++){
//
//            }
//            return 1;
//        } catch (Exception e) {
//            return 0;
//        }
//    }

}
