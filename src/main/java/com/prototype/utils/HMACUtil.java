package com.prototype.utils;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.prototype.config.AppConfig.ApplicationConfiguration;

@Component
public class HMACUtil {

	private static final String HMAC_ALGORITHM = "HmacSHA1";
	
	private static String key;

	@Autowired
	public HMACUtil(ApplicationConfiguration config) {
		HMACUtil.key = config.getKey();
	}

	public static String encode(String messageToEncode) throws Exception {
		SecretKey secretKey = new SecretKeySpec(key.getBytes("ASCII"), HMAC_ALGORITHM);
		Mac mac = Mac.getInstance(secretKey.getAlgorithm());
		mac.init(secretKey);
		byte[] encrypt = mac.doFinal(messageToEncode.getBytes("UTF8"));
		return new String(Base64.encodeBase64(encrypt), "ASCII");
	}

	public static boolean validate(String messageEncoded, String messageToEncode) throws Exception {
		if(key == null || messageEncoded == null || messageToEncode == null) {
			return false;
		}

		String encodeMessageToValidate = encode(messageToEncode);
		return messageEncoded.equals(encodeMessageToValidate);
	}
}
