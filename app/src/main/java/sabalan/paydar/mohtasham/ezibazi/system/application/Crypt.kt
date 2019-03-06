package sabalan.paydar.mohtasham.ezibazi.system.application

import android.annotation.SuppressLint
import android.util.Base64

import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

import sabalan.paydar.mohtasham.ezibazi.system.hardware.Hardware
import java.nio.charset.Charset

object Crypt {
    private val SALT = "7c3d596ed03ab9116c547b0eb678b247"
    private val SALT_COPY = "7c3d596ed03ab9116c547b0eb678b247"

    private val AES = "AES"



    fun encrypt(strClearText: String): String {
        var strClearText = strClearText
        try {
            val secretKey = generateDefaultKey()
            @SuppressLint("GetInstance") val cipher = Cipher.getInstance(AES)
            cipher.init(Cipher.ENCRYPT_MODE, secretKey)
            val encrypted = cipher.doFinal(strClearText.toByteArray(charset("UTF-8")))
            strClearText = Base64.encodeToString(encrypted, Base64.DEFAULT)
            return strClearText

        } catch (e: Exception) {
            e.printStackTrace()
            return "not encrypted"
        }

    }

    fun encrypt(strClearText: String, key: String): String {
        var strClearText = strClearText
        try {

            val secretKey = SecretKeySpec(key.toByteArray(), AES)
            @SuppressLint("GetInstance") val cipher = Cipher.getInstance(AES)
            cipher.init(Cipher.ENCRYPT_MODE, secretKey)
            val encrypted = cipher.doFinal(strClearText.toByteArray(charset("UTF-8")))
            strClearText = Base64.encodeToString(encrypted, Base64.DEFAULT)
            return strClearText

        } catch (e: Exception) {
            e.printStackTrace()
            return "not encrypted"
        }

    }

    fun decrypt(strEncrypted: String): String {
        var strEncrypted = strEncrypted
        try {
            val secretKey = generateDefaultKey()
            @SuppressLint("GetInstance") val cipher = Cipher.getInstance(AES)
            cipher.init(Cipher.DECRYPT_MODE, secretKey)
            val decrypted = cipher.doFinal(Base64.decode(strEncrypted, Base64.DEFAULT))
            strEncrypted = String(decrypted,  charset("UTF-8"))
            return strEncrypted
        } catch (e: Exception) {
            e.printStackTrace()
            return "not decrypted"
        }

    }



    fun decrypt(strEncrypted: String, key: String): String {
        var strEncrypted = strEncrypted
        try {
            val secretKey = SecretKeySpec(key.toByteArray(), AES)
            @SuppressLint("GetInstance") val cipher = Cipher.getInstance(AES)
            cipher.init(Cipher.DECRYPT_MODE, secretKey)
            val decrypted = cipher.doFinal(Base64.decode(strEncrypted, Base64.DEFAULT))
            strEncrypted = String(decrypted, charset("UTF-8"))
            return strEncrypted
        } catch (e: Exception) {
            e.printStackTrace()
            return "not decrypted"
        }

    }


    fun generateClientKey(): String {
        return getHashedString(
                Hardware.wifiMac
                        + Hardware.deviceModel
                        + Hardware.device
                        + Hardware.deviceBrand
                        + Hardware.deviceId
                        + Hardware.subscriberId
        )
    }

    fun generateFcmClientKey(): String {
        return encrypt(
                Hardware.wifiMac
                        + Hardware.deviceModel
                        + Hardware.device
                        + Hardware.deviceBrand
                        + Hardware.deviceId
                        + Hardware.subscriberId
                        + Hardware.androidId, generateDefaultKey().toString())
    }


    private fun getHashedString(text: String): String {
        var m: MessageDigest? = null
        try {
            m = MessageDigest.getInstance("MD5")
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }

        m?.reset()
        m?.update(text.toByteArray())
        var digest = ByteArray(0)
        if (m != null) {
            digest = m.digest()
        }
        val bigInt = BigInteger(1, digest)
        var hashed_text = bigInt.toString(16)
        while (hashed_text.length < 32) {
            hashed_text = "0$hashed_text"
        }
        return hashed_text
    }


    private fun generateDefaultKey(): SecretKeySpec {
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
        val ENCRYPTION_KEY = getHashedString(generateClientKey()).toByteArray()
        return SecretKeySpec(ENCRYPTION_KEY, AES)
    }
}
