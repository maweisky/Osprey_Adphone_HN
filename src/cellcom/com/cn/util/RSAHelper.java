package cellcom.com.cn.util;

import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

public class RSAHelper {
	private static PublicKey publicKey;
	private static PrivateKey privateKey;
	private static String publicKeyStr;
	private static String privateKeyStr;
	static {
		try {
			
			 KeyPairGenerator keygen = KeyPairGenerator.getInstance("RSA");
			  SecureRandom random = new SecureRandom();
			  random.setSeed("cellcom".getBytes());
			  // 初始加密，长度为512，必须是大于512才可以的
			  keygen.initialize(1024, random);
			  // 取得密钥对
			  KeyPair kp = keygen.generateKeyPair();
			  privateKey=kp.getPrivate();
			  publicKey=kp.getPublic();
//			// 密钥位数
//			keyPairGen.initialize(1024);
//			// 密钥对
//			KeyPair keyPair = keyPairGen.generateKeyPair();
//			// 公钥
//			publicKey = (RSAPublicKey) keyPair.getPublic();
//			publicKeyStr= getKeyString(publicKey);
//			// 私钥
//			privateKey = (RSAPrivateKey) keyPair.getPrivate();
//			privateKeyStr= getKeyString(privateKey);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 得到公钥
	 * 
	 * @param key
	 *            密钥字符串（经过base64编码）
	 * @throws Exception
	 */
	public static String getPublicKey() throws Exception {
		String publicKeyStr = getKeyString(publicKey);
		return publicKeyStr;
	}

	/**
	 * 得到私钥
	 * 
	 * @param key
	 *            密钥字符串（经过base64编码）
	 * @throws Exception
	 */
	public static PrivateKey getPrivateKey() throws Exception {
		return privateKey;
	}

	/**
	 * 利用公钥加密数据
	 * 
	 * @param publicKeyString
	 *            公钥经过base64加密字符串
	 * @param plainText
	 *            要加密的字符串
	 * @return 加密后的字符串
	 * @throws Exception
	 */
	public static String encode(String publicKeyString, byte[] plainText)
			throws Exception {
		publicKey = getPublicKey(publicKeyString);
		// 加解密类
		Cipher cipher =  Cipher.getInstance("RSA/ECB/PKCS1Padding");
		// 加密
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		byte[] enBytes = cipher.doFinal(plainText);
		return bytesToString(enBytes);
	}

	/**
	 * 利用私钥解密数据
	 * 
	 * @param privateKeyString
	 *            私钥经过base64加密字符串
	 * @param enBytes
	 *            要解密的字符串
	 * @return 解密后的字符串
	 * @throws Exception
	 */
	public static String decode(PrivateKey privateKey, String enStr)
			throws Exception {
		byte[] enBytes = stringToBytes(enStr);
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		// 解密
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		byte[] deBytes = cipher.doFinal(enBytes);
		return new String(deBytes);
	}

	private static PublicKey getPublicKey(String key) throws Exception {
		byte[] keyBytes;
		keyBytes = /* (new BASE64Decoder()).decodeBuffer(key) */Base64Util
				.decode(key);

		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PublicKey publicKey = keyFactory.generatePublic(keySpec);
		return publicKey;
	}

	private static PrivateKey getPrivateKey(String key) throws Exception {
		byte[] keyBytes;
		keyBytes = /* (new BASE64Decoder()).decodeBuffer(key) */Base64Util
				.decode(key);

		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
		return privateKey;
	}

	
	// 得到密钥字符串（经过base64编码）
	private static String getKeyString(Key key) throws Exception {
		byte[] keyBytes = key.getEncoded();
		String s = /* (new BASE64Encoder()).encode(keyBytes) */Base64Util
				.encodeToString(keyBytes);
		return s;
	}

	//字节转换为字符串
	private static String bytesToString(byte[] encrytpByte) {
		String result = "";
		for (Byte bytes : encrytpByte) {
			result += bytes.toString() + " ";
		}
		return result;
	}

	//字符串转换为字节
	private static byte[] stringToBytes(String encrytpStr) {
		String[] strArr = encrytpStr.split(" ");
		int len = strArr.length;
		byte[] clone = new byte[len];
		for (int i = 0; i < len; i++) {
			clone[i] = Byte.parseByte(strArr[i]);
		}
		return clone;
	}

	public static void main(String[] args) throws Exception {
		// 明文
		byte[] plainText = "50 48 49 50 80 105 110 103 97 110 86 105 116 97 108 105 116 121 48 55 53 53 50 50 ".getBytes();
		String enstr = encode(getPublicKey(), plainText);
		// System.out.println("enstr==>"+enstr);

		
		String destr = decode(getPrivateKey(), enstr);
		System.out.println("destr==>" + destr);
	}
}
