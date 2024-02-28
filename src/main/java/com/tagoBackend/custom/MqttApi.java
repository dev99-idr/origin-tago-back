package com.tagoBackend.custom;

import java.util.Properties;
import java.util.UUID;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MqttApi {
    
    public static void publish(String broker, String zigbeeTag, String bleTag, String opCode) {
		
		String topic = "/Ultra/"+zigbeeTag+"/zigbee_ble";

		String clientId = UUID.randomUUID().toString().replace("-", "").substring(0,11);
		
		JSONObject sendData = new JSONObject();
		JSONObject tagData = new JSONObject();
		
		tagData.put("ble_thingid", bleTag);
		tagData.put("op_code", opCode);
		sendData.put("thingid", zigbeeTag);
		sendData.put("tid", UUID.randomUUID().toString().replace("-", "").substring(0,11));
		sendData.put("msg_type", "ReportData");
		sendData.put("thing_type", zigbeeTag.split("_")[0]);
		sendData.put("tag_data", tagData);
		
		String content = sendData.toString();
		
		try {
   
            if(broker == null){
                new Exception();
            }
            
			MqttClient client = new MqttClient(broker, clientId, new MemoryPersistence());
			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setCleanSession(true);
			System.out.println("Connecting to broker: " + broker);
			
			client.connect(connOpts);
			System.out.println("Connected");
			
			MqttMessage message = new MqttMessage(content.getBytes());
			message.setQos(2);
			client.publish(topic, message);
			System.out.println("Message published");
			
			client.disconnect();
            System.out.println("Disconnected");
		} catch (Exception e) {
			// TODO: handle exception
            new Exception();
		}
	}
}
