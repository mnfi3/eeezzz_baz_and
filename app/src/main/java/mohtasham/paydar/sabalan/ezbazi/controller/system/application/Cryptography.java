package mohtasham.paydar.sabalan.ezbazi.controller.system.application;

import android.annotation.SuppressLint;
import android.util.Base64;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import mohtasham.paydar.sabalan.ezbazi.controller.system.hardware.Hardware;

public class Cryptography {
  private static final String SALT="7c3d596ed03ab9116c547b0eb678b247";
  private static final String SALT_COPY="7c3d596ed03ab9116c547b0eb678b247";

  private static final String AES = "AES";




  public static String encrypt(String strClearText){
    try {
      SecretKey secretKey = generateDefaultKey();
//      for (int i = 0; i< Character.getNumericValue(getHashedString(SALT).charAt(10))-1; i++) {
      @SuppressLint("GetInstance") Cipher cipher = Cipher.getInstance(AES);
      cipher.init(Cipher.ENCRYPT_MODE, secretKey);
      byte[] encrypted = cipher.doFinal(strClearText.getBytes("UTF-8"));
      strClearText= Base64.encodeToString(encrypted, Base64.DEFAULT);
//      }
      return strClearText;

    } catch (Exception e) {
      e.printStackTrace();
      return "not encrypted";
    }
  }

  public static String decrypt(String strEncrypted){
    try {
      SecretKey secretKey = generateDefaultKey();
//      for (int i = 0; i< Character.getNumericValue(getHashedString(SALT).charAt(10))-1; i++) {
      @SuppressLint("GetInstance") Cipher cipher = Cipher.getInstance(AES);
      cipher.init(Cipher.DECRYPT_MODE, secretKey);
      byte[] decrypted = cipher.doFinal(Base64.decode(strEncrypted, Base64.DEFAULT));
      strEncrypted= new String(decrypted, "UTF-8");
//      }
      return strEncrypted;
    } catch (Exception e) {
      e.printStackTrace();
      return "not decrypted";
    }
  }


  private static String generateClientKey(){
    return getHashedString(
      Cryptography.SALT +
        Hardware.getWifiMac() +
//        username +
        Hardware.getDeviceModel()
      // G.getSubscriberId(G.context)
      // G.getAndroidId(G.context)
    );
  }


  private static String getHashedString(String text){
    MessageDigest m = null;
    try {
      m = MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    if (m != null) {
      m.reset();
    }
    if (m != null) {
      m.update(text.getBytes());
    }
    byte[] digest = new byte[0];
    if (m != null) {
      digest = m.digest();
    }
    BigInteger bigInt = new BigInteger(1,digest);
    String hashed_text = bigInt.toString(16);
    while(hashed_text.length() < 32 ){
      hashed_text = "0"+ hashed_text;
    }
    return hashed_text;
  }


  private static SecretKeySpec generateDefaultKey() {
//    byte[] ENCRYPTION_KEY = new byte[]{
//        '0',(byte)getHashedString(SALT).charAt(20),
//        '4',(byte)getHashedString(SALT).charAt(16),
//        '3',(byte)getHashedString(SALT).charAt(14),
//        'i',(byte)getHashedString(SALT).charAt(4) ,
//        'm',(byte)getHashedString(SALT).charAt(0) ,
//        'a',(byte)getHashedString(SALT).charAt(6) ,
//        'j',(byte)getHashedString(SALT).charAt(13),
//        'r',(byte)getHashedString(SALT).charAt(15),
//        'a',(byte)getHashedString(SALT).charAt(26),
//        'F',(byte)getHashedString(SALT).charAt(30),
//        'n',(byte)getHashedString(SALT).charAt(18),
//        'e',(byte)getHashedString(SALT).charAt(7) ,
//        's',(byte)getHashedString(SALT).charAt(2) ,
//        'h',(byte)getHashedString(SALT).charAt(25),
//        'o',(byte)getHashedString(SALT).charAt(30),
//        'M',(byte)getHashedString(SALT).charAt(29),
//      };
//
    byte[] ENCRYPTION_KEY = getHashedString(generateClientKey()).getBytes();
    return new SecretKeySpec(ENCRYPTION_KEY, AES);
  }
}
