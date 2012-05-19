package com.br.morecerto.controller.utility;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;

public class Util {

	public static String md5(String text) {
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.update(text.getBytes());
			byte messageDigest[] = digest.digest();
			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < messageDigest.length; i++) {
				String hexStr = Integer.toHexString(0xFF & messageDigest[i]);
				while (hexStr.length() < 2) {
					hexStr = "0" + hexStr;
				}
				hexString.append(hexStr);
			}
			return hexString.toString().toLowerCase();

		} catch (NoSuchAlgorithmException e) {
			return "";
		}
	}

	public static String signGoogleUrl(String googleApiGet, String apiKey) {
		String keyString = apiKey.replace('-', '+');
		keyString = keyString.replace('_', '/');
		try {
			byte[] key = Base64.decode(keyString);
			final SecretKeySpec sha1Key = new SecretKeySpec(key, "HmacSHA1");
			final Mac mac = Mac.getInstance("HmacSHA1");
			mac.init(sha1Key);
			byte[] sigBytes = mac.doFinal(googleApiGet.getBytes());
			String signature = Base64.encodeBytes(sigBytes);
			signature = signature.replace('+', '-');
			signature = signature.replace('/', '_');
			return signature;
		} catch (IOException e) {
		} catch (NoSuchAlgorithmException e) {
		} catch (InvalidKeyException e) {
		}
		return "";
	}

	public static String toSafeXml(String text) {
		String safeText = text.replaceAll("&", "&amp;");
		safeText = safeText.replaceAll("<", "&lt;");
		return safeText;
	}

	public static String streamToString(InputStream stream) throws IOException {
		final char[] buffer = new char[0x10000];
		final StringBuilder stringBuilder = new StringBuilder();
		final InputStreamReader isr = new InputStreamReader(stream, "UTF-8");
		int read;
		while ((read = isr.read(buffer, 0, buffer.length)) != -1) {
			stringBuilder.append(buffer, 0, read);
		}
		return stringBuilder.toString();
	}

	public static int toPixels(float density, int dips) {
		return (int) (dips * density + 0.5f);
	}

	public static String getTimestamp(String format) {
		final SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		final Date date = new Date();
		return dateFormat.format(date);
	}

	public static String getUTCTimestamp(String format) {
		final SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		final Date date = new Date();
		return dateFormat.format(date);
	}

	public static ArrayList<String> restoreArray(SharedPreferences prefs, String key) {
		final ArrayList<String> array = new ArrayList<String>();
		if (prefs != null) {
			final String encodedString = prefs.getString(key, null);
			if (encodedString != null) {
				final String[] stringArray = encodedString.split(";");
				for (int i = 0; i < stringArray.length; ++i) {
					array.add(stringArray[i]);
				}
			}
		}
		return array;
	}

	public static void saveArray(SharedPreferences prefs, String key, ArrayList<String> array) {
		if (prefs != null) {
			String encodedString = "";
			final int count = array.size();
			for (int i = 0; i < count; ++i) {
				encodedString += array.get(i);
				if (i != count - 1) {
					encodedString += ";";
				}
			}
			final Editor editor = prefs.edit();
			editor.putString(key, encodedString);
			editor.commit();
		}
	}

	public static byte[] concat(byte[] A, byte[] B, byte[] C) {
		byte[] array = new byte[A.length + B.length + C.length];
		System.arraycopy(A, 0, array, 0, A.length);
		System.arraycopy(B, 0, array, A.length, B.length);
		System.arraycopy(C, 0, array, A.length + B.length, C.length);
		return array;
	}

	public static String stackTraceStringValue(Exception exception) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		exception.printStackTrace(pw);
		return sw.toString();
	}

	public static SpannableString getUnderlinedString(String string) {
		SpannableString spannableString = new SpannableString(string);
		spannableString.setSpan(new UnderlineSpan(), 0, spannableString.length(), 0);
		return spannableString;
	}
}