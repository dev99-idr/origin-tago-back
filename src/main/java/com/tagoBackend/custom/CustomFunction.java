package com.tagoBackend.custom;

import com.tagoBackend.response.*;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

public class CustomFunction {
    // ** Response Message **
    // ** senum = HTTP Status, msg = Message(ex: success, failed..), data = data (ex: {id:abc, pw:abc}
    public Message crtMsg(StatusEnum senum, String msg, Object data){
        Message message = new Message();
        message.setStatus(senum);
        message.setMessage(msg);
        message.setData(data);

        return message;
    }

    // ** Create Error Message **
    public String crtErrMsg(Exception e){
        return "Error Message : " + e.getMessage() + "\nError StackTrace : " + e.getStackTrace();
    }

    // ** Create Current UNIX Timestamp **
    public Long unixTimeStamp(){
        Long unixTime = System.currentTimeMillis();
        return unixTime;
    }

    public boolean makeFileWithString(String path, String base64){
        byte decode[] = Base64.decodeBase64(base64);
        FileOutputStream fos;

        try{
            File target = new File(path);
            target.createNewFile();

            fos = new FileOutputStream(target);
            fos.write(decode);
            fos.close();

            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public String fileToBase64(String path){
        String result = "";
        FileInputStream inputStream;

        try{
            File file = new File(path);

            byte[] byteArray = new byte[(int) file.length()];

            inputStream = new FileInputStream(file);
            inputStream.read(byteArray, 0, byteArray.length - 1);
            inputStream.close();

            result = Base64.encodeBase64String(byteArray);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public boolean saveVideo(String path, MultipartFile file){
        try{
            File video = new File(path);
            file.transferTo(video);

            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }


}
