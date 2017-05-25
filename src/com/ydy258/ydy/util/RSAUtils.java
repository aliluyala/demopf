package com.ydy258.ydy.util;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.SecureRandom;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.util.Assert;

public class RSAUtils {
	/** ��ȫ�����ṩ�� */
	private static final Provider PROVIDER = new BouncyCastleProvider();

	/** ��Կ��С */
	private static final int KEY_SIZE = 1024;

	/**
	 * ����ʵ��
	 */
	private RSAUtils() {
	}

	/**
	 * �����Կ��
	 * 
	 * @return ��Կ��
	 */
	public static KeyPair generateKeyPair() {
		try {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA", PROVIDER);
			keyPairGenerator.initialize(KEY_SIZE, new SecureRandom());
			return keyPairGenerator.generateKeyPair();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * ����
	 * 
	 * @param publicKey
	 *            ��Կ
	 * @param data
	 *            ���
	 * @return ���ܺ�����
	 */
	public static byte[] encrypt(PublicKey publicKey, byte[] data) {
		Assert.notNull(publicKey);
		Assert.notNull(data);
		try {
			Cipher cipher = Cipher.getInstance("RSA", PROVIDER);
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			return cipher.doFinal(data);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * ����
	 * 
	 * @param publicKey
	 *            ��Կ
	 * @param text
	 *            �ַ�
	 * 
	 * @return Base64�����ַ�
	 */
	public static String encrypt(PublicKey publicKey, String text) {
		Assert.notNull(publicKey);
		Assert.notNull(text);
		byte[] data = encrypt(publicKey, text.getBytes());
		return data != null ? Base64.encodeBase64String(data) : null;
	}

	/**
	 * ����
	 * 
	 * @param privateKey
	 *            ˽Կ
	 * @param data
	 *            ���
	 * @return ���ܺ�����
	 */
	public static byte[] decrypt(PrivateKey privateKey, byte[] data) {
		Assert.notNull(privateKey);
		Assert.notNull(data);
		try {
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", PROVIDER);
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			return cipher.doFinal(data);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * ����
	 * 
	 * @param privateKey
	 *            ˽Կ
	 * @param text
	 *            Base64�����ַ�
	 * @return ���ܺ�����
	 */
	public static String decrypt(PrivateKey privateKey, String text) {
		Assert.notNull(privateKey);
		Assert.notNull(text);
		byte[] data = decrypt(privateKey, Base64.decodeBase64(text));
		return data != null ? new String(data) : null;
	}
}