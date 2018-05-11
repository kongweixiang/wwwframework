package com.smart.gateway.util;

import com.smart.gateway.constant.SmartConstant;
import com.smart.gateway.constant.SysConfig;
import org.apache.log4j.Logger;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.*;
import java.security.spec.KeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class AppUtil {

	private static Logger logger = Logger.getLogger(AppUtil.class);

	public static byte[] readBytes(InputStream in) throws IOException {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		for (int i = in.read(); i != -1; i = in.read()) {
			bout.write(i);
		}
		return bout.toByteArray();
	}

	public static PrivateKey getPrivateKey(String keyNo) {
		if (keyMap == null) {
			keyMap = new ConcurrentHashMap<String, PrivateKey>();
		}
		if (keyMap.get(keyNo) == null) {
			logger.info("getPrivateKey:" + SysConfig.get(keyNo));
			synchronized ("keyMap".intern()) {
				if (keyMap.get(keyNo) == null) {
					//todo
				}
			}
		}
		return keyMap.get(keyNo);
	}

	private static ConcurrentHashMap<String, PrivateKey> keyMap;

	public static PublicKey getPublicKey() {
		if (pk == null) {
			synchronized ("pk".intern()) {
				if (pk == null) {
					try {
						byte[] encodedKey = BytesUtil.hexToBytes(SysConfig.get(SysConfig.KEY_TOP_PK));
						KeySpec keySpec = new X509EncodedKeySpec(encodedKey);
						KeyFactory keyFactory = KeyFactory.getInstance("RSA");
						pk = keyFactory.generatePublic(keySpec);
					} catch (Exception ex) {
						ex.printStackTrace();
						logger.info(ex.getMessage(), ex);
					}
				}
			}
		}
		return pk;
	}

	private static PublicKey pk;

	public static String getMaskDataKey() {
		if (maskDataKey == null) {
			synchronized ("maskDataKey".intern()) {
				//todo
			}
		}
		return maskDataKey;
	}

	private static String maskDataKey;

	public static String decryptData(String value, String ck) throws UnsupportedEncodingException {
		return new String(decrypt(BytesUtil.hexToBytes(value), ck), SmartConstant.ENCODING).trim();
	}
	
	public static String genH5Ck() {
		String noncestr = createNonceStr(16);
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("DESede");
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			random.setSeed(noncestr.getBytes());
			kgen.init(112, random);
			SecretKey secretKey = kgen.generateKey();
			byte[] key = new byte[24];
			System.arraycopy(secretKey.getEncoded(), 0, key, 0, 24);
			String ck = BytesUtil.bytesToHex(key);
			return ck;
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}

	}

	public static String getCk(String encryptedCk, String tk) {
		// decrypt by tk;
		try {
			byte[] ck = ecbDecrypt(BytesUtil.hexToBytes(tk), BytesUtil.hexToBytes(encryptedCk), 0);
			return new String(ck);
		} catch (Exception ex) {
			logger.info(ex.getMessage(), ex);
			return null;
		}
	}

	private static byte[] ecbDecrypt(byte[] key, byte[] data, int padding) throws GeneralSecurityException {
		return internalCrypt(0, 1, key, data, null, padding);
	}

	private static byte[] internalCrypt(int enc, int ecb, byte[] key, byte[] data, byte[] iv, int padding) throws GeneralSecurityException {
		if (key == null || (key.length != 8 && key.length != 16 && key.length != 24)) {
			throw new IllegalArgumentException();
		}

		if (data == null) {
			throw new IllegalArgumentException();
		}

		Cipher c = Cipher.getInstance(ecb != 0 ? "DESede/ECB/NoPadding" : "DESede/CBC/NoPadding");

		byte[] deskey = new byte[24];
		if (key.length == 8) {
			System.arraycopy(key, 0, deskey, 0, 8);
			System.arraycopy(key, 0, deskey, 8, 8);
			System.arraycopy(key, 0, deskey, 16, 8);
		} else if (key.length == 16) {
			System.arraycopy(key, 0, deskey, 0, 16);
			System.arraycopy(key, 0, deskey, 16, 8);
		} else {
			System.arraycopy(key, 0, deskey, 0, 24);
		}

		byte[] desdata = data;
		if (padding == 1) {
			desdata = new byte[(data.length / 8 + 1) * 8];
			System.arraycopy(data, 0, desdata, 0, data.length);
			desdata[data.length] = (byte) 0x80;
			Arrays.fill(desdata, data.length + 1, desdata.length, (byte) 0x00);
		} else if (padding == 2) {
			if (data.length % 8 != 0) {
				desdata = new byte[(data.length / 8 + 1) * 8];
				System.arraycopy(data, 0, desdata, 0, data.length);
				Arrays.fill(desdata, data.length, desdata.length, (byte) 0x00);
			}
		}

		if (ecb != 0) {
			c.init(enc != 0 ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, new SecretKeySpec(deskey, "DESede"));
		} else {
			byte[] zero = { (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 };
			if (iv == null) {
				iv = zero;
			}
			IvParameterSpec ivps = new IvParameterSpec(iv);
			c.init(enc != 0 ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, new SecretKeySpec(deskey, "DESede"), ivps);
		}
		return c.doFinal(desdata);
	}

	private static byte[] ecbEncrypt(byte[] key, byte[] data, int padding) throws GeneralSecurityException {
		return internalCrypt(1, 1, key, data, null, padding);
	}

	public static byte[] decrypt(byte[] input, String ck) {
		try {
			return ecbDecrypt(BytesUtil.hexToBytes(ck), input, 2);
		} catch (Exception ex) {
			logger.info(ex.getMessage(), ex);
			return null;
		}
	}

	public static byte[] encrypt(byte[] input, String ck) {
		try {
			return ecbEncrypt(BytesUtil.hexToBytes(ck), input, 2);
		} catch (Exception ex) {
			logger.info(ex.getMessage(), ex);
			return null;
		}
	}

	public static String fetchFirst(String input, String regex) {
		if (input == null) {
			return null;
		}
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(input);
		if (matcher.find()) {
			return matcher.group(0);
		}
		return null;
	}


	public static String toEncodedQueryString(Map<String, String> map, String enconding) {
		try {
			StringBuilder qs = new StringBuilder();
			for (Map.Entry<String, String> entry : map.entrySet()) {
				qs.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), enconding));
				qs.append("&");
			}
			qs.setLength(qs.length() - 1);
			return qs.toString();
		} catch (UnsupportedEncodingException ex) {
			logger.info(ex.getMessage(), ex);
			return "";
		}
	}

	public static String encrypt3DES(String value, String key) throws Exception {
		if (null == value || "".equals(value)) {
			return "";
		}
		byte[] valueByte = value.getBytes();
		byte[] sl = encrypt3DES(valueByte, BytesUtil.hexToBytes(key));
		String result = BytesUtil.bytesToHex(sl);
		return result;
	}

	public static String decrypt3DES(String value, String key) throws Exception {
		if (null == value || "".equals(value)) {
			return "";
		}
		byte[] valueByte = BytesUtil.hexToBytes(value);
		byte[] sl = decrypt3DES(valueByte, BytesUtil.hexToBytes(key));
		String result = new String(sl);
		return result;
	}

	public static byte[] encrypt3DES(byte[] input, byte[] key) throws Exception {
		Cipher c = Cipher.getInstance("DESede/ECB/PKCS5Padding");
		c.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "DESede"));
		return c.doFinal(input);
	}

	public static byte[] decrypt3DES(byte[] input, byte[] key) throws Exception {
		Cipher c = Cipher.getInstance("DESede/ECB/PKCS5Padding");
		c.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "DESede"));
		return c.doFinal(input);
	}

	public static byte[] encryptRSA(PublicKey pk, byte[] input) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.ENCRYPT_MODE, pk);
		byte[] result = cipher.doFinal(input);
		return result;
	}

	public static byte[] decryptRSA(PrivateKey sk, byte[] input) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, sk);
		byte[] result = cipher.doFinal(input);
		return result;
	}

	public static PublicKey getPublicKey(byte[] encodedKey) throws Exception {
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		return keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
	}

	public static String createNonceStr(int length) {
		String sl = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		StringBuilder bf = new StringBuilder();
		for (int i = 0; i < length; i++) {
			bf.append(sl.charAt(new Random().nextInt(sl.length() - 1)));
		}
		return bf.toString();
	}

	public static String encrypt3DESH5(String value, String key) throws Exception {
		if (null == value || "".equals(value)) {
			return "";
		}
		byte[] valueByte = value.getBytes();
		byte[] keyByte = BytesUtil.hexToBytes(key);

		byte[] b = new byte[(valueByte.length / 8 + (valueByte.length % 8 == 0 ? 0 : 1)) * 8];
		System.arraycopy(valueByte, 0, b, 0, valueByte.length);
		Cipher c = Cipher.getInstance("DESede/ECB/NoPadding");
		c.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(keyByte, "DESede"));
		byte[] sl = c.doFinal(b);
		String result = BytesUtil.bytesToHex(sl);
		return result;
	}

	public static String decrypt3DESH5(String value, String key) throws Exception {
		if (null == value || "".equals(value)) {
			return "";
		}
		byte[] valueByte = BytesUtil.hexToBytes(value);
		byte[] keyByte = BytesUtil.hexToBytes(key);
		Cipher c = Cipher.getInstance("DESede/ECB/NoPadding");
		c.init(Cipher.DECRYPT_MODE, new SecretKeySpec(keyByte, "DESede"));
		byte[] sl = c.doFinal(valueByte);
		String result = new String(sl);
		return result.trim();
	}

	public static int id = 0;

	public static synchronized int getSerialId() {
		id++;
		if (id >= 100000) {
			id = 0;
		}
		return id;
	}

	public static String replaceJsonFields(String json, char maskChar) {
		String[] fields = new String[] { "pin", "cvn2", "expire", "expired", "password", "newPassword", "oldPassword" };
		for (String field : fields) {
			json = replaceJsonField(json, maskChar, field);
		}
		return json;
	}

	public static String replaceJsonFields(String json, char maskChar, String... fields) {
		for (String field : fields) {
			json = replaceJsonField(json, maskChar, field);
		}
		return json;
	}

	private static String replaceJsonField(String json, char maskChar, String fieldName) {
		String patternText = "(\"" + fieldName + "\"\\s*:\\s*\")(.*?)(\")";
		Pattern pattern = Pattern.compile(patternText);
		Matcher matcher = pattern.matcher(json);

		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
			String orignalFieldValue = matcher.group(2);
			char[] chars = new char[orignalFieldValue.length()];
			Arrays.fill(chars, maskChar);
			String replacement = new String(chars);
			matcher.appendReplacement(sb, "$1" + replacement + "$3");
		}
		matcher.appendTail(sb);

		return sb.toString();
	}
}
