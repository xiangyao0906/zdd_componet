package com.juju.core.utils

import android.annotation.SuppressLint
import android.util.Base64
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.security.*
import java.security.spec.AlgorithmParameterSpec
import java.security.spec.InvalidKeySpecException
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.*
import javax.crypto.spec.DESKeySpec
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import kotlin.experimental.and


/**
 * Created by xiangyao on 2019-06-27.
 */
object EncryptUtils {

    /**
     * Return the hex string of MD5 encryption.
     *
     * @param data The data.
     * @return the hex string of MD5 encryption
     */
    fun encryptMD5ToString(data: String?): String {
        return if (data == null || data.isEmpty()) "" else encryptMD5ToString(data.toByteArray())
    }

    /**
     * Return the hex string of MD5 encryption.
     *
     * @param data The data.
     * @param salt The salt.
     * @return the hex string of MD5 encryption
     */
    fun encryptMD5ToString(data: String?, salt: String?): String {
        if (data == null && salt == null) return ""
        if (salt == null) return bytes2HexString(
            encryptMD5(
                data!!.toByteArray()
            )
        )
        return if (data == null) bytes2HexString(
            encryptMD5(
                salt.toByteArray()
            )
        ) else bytes2HexString(encryptMD5((data + salt).toByteArray()))
    }

    /**
     * Return the hex string of MD5 encryption.
     *
     * @param data The data.
     * @return the hex string of MD5 encryption
     */
    private fun encryptMD5ToString(data: ByteArray): String {
        return bytes2HexString(encryptMD5(data))
    }

    /**
     * Return the bytes of MD5 encryption.
     *
     * @param data The data.
     * @return the bytes of MD5 encryption
     */
    private fun encryptMD5(data: ByteArray): ByteArray? {
        return hashTemplate(data, "MD5")
    }

    /**
     * Return the bytes of hash encryption.
     *
     * @param data      The data.
     * @param algorithm The name of hash encryption.
     * @return the bytes of hash encryption
     */
    private fun hashTemplate(data: ByteArray?, algorithm: String): ByteArray? {
        if (data == null || data.isEmpty()) return null
        return try {
            val md = MessageDigest.getInstance(algorithm)
            md.update(data)
            md.digest()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            null
        }

    }

    private val HEX_DIGITS = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F')

    private fun bytes2HexString(bytes: ByteArray?): String {
        if (bytes == null) return ""
        val len = bytes.size
        if (len <= 0) return ""
        val ret = CharArray(len shl 1)
        var i = 0
        var j = 0
        while (i < len) {
            ret[j++] = HEX_DIGITS[bytes[i].toInt() shr 4 and 0x0f]
            ret[j++] = HEX_DIGITS[bytes[i].toInt() and 0x0f]
            i++
        }
        return String(ret)
    }

    ///////////////////////////////////////////////////////////////////////////
    // RSA encryption
    ///////////////////////////////////////////////////////////////////////////
    /**
     * Return the Base64-encode bytes of RSA encryption.
     *
     * @param data           The data.
     * @param publicKey      The public key.
     * @param keySize        The size of key, e.g. 1024, 2048...
     * @param transformation The name of the transformation, e.g., *RSA/CBC/PKCS1Padding*.
     * @return the Base64-encode bytes of RSA encryption
     */
    fun encryptRSA2Base64(data: ByteArray?, publicKey: ByteArray?, keySize: Int, transformation: String): String? {
        return encryptRSA(data, publicKey, keySize, transformation)?.let { base64Encode(it) }
    }

    /**
     * Return the hex string of RSA encryption.
     *
     * @param data           The data.
     * @param publicKey      The public key.
     * @param keySize        The size of key, e.g. 1024, 2048...
     * @param transformation The name of the transformation, e.g., *RSA/CBC/PKCS1Padding*.
     * @return the hex string of RSA encryption
     */
    fun encryptRSA2HexString(data: ByteArray?, publicKey: ByteArray?, keySize: Int, transformation: String): String? {
        return bytes2HexString(encryptRSA(data, publicKey, keySize, transformation))
    }

    /**
     * Return the bytes of RSA encryption.
     *
     * @param data           The data.
     * @param publicKey      The public key.
     * @param keySize        The size of key, e.g. 1024, 2048...
     * @param transformation The name of the transformation, e.g., *RSA/CBC/PKCS1Padding*.
     * @return the bytes of RSA encryption
     */
    fun encryptRSA(data: ByteArray?, publicKey: ByteArray?, keySize: Int, transformation: String): ByteArray? {
        return rsaTemplate(data, publicKey, keySize, transformation, true)
    }

    /**
     * Return the bytes of RSA decryption for Base64-encode bytes.
     *
     * @param data           The data.
     * @param privateKey     The private key.
     * @param keySize        The size of key, e.g. 1024, 2048...
     * @param transformation The name of the transformation, e.g., *RSA/CBC/PKCS1Padding*.
     * @return the bytes of RSA decryption for Base64-encode bytes
     */
    fun decryptBase64RSA(data: ByteArray?, privateKey: ByteArray?, keySize: Int, transformation: String): ByteArray? {
        return data?.let { decryptRSA(base64Decode(it), privateKey, keySize, transformation) }
    }

    /**
     * Return the bytes of RSA decryption for hex string.
     *
     * @param data           The data.
     * @param privateKey     The private key.
     * @param keySize        The size of key, e.g. 1024, 2048...
     * @param transformation The name of the transformation, e.g., *RSA/CBC/PKCS1Padding*.
     * @return the bytes of RSA decryption for hex string
     */
    fun decryptHexStringRSA(data: String?, privateKey: ByteArray?, keySize: Int, transformation: String): ByteArray? {
        return data?.let { decryptRSA(hexString2Bytes(it), privateKey, keySize, transformation) }
    }

    /**
     * Return the bytes of RSA decryption.
     *
     * @param data           The data.
     * @param privateKey     The private key.
     * @param keySize        The size of key, e.g. 1024, 2048...
     * @param transformation The name of the transformation, e.g., *RSA/CBC/PKCS1Padding*.
     * @return the bytes of RSA decryption
     */
    fun decryptRSA(data: ByteArray?, privateKey: ByteArray?, keySize: Int, transformation: String): ByteArray? {
        return rsaTemplate(data, privateKey, keySize, transformation, false)
    }

    /**
     * Return the bytes of RSA encryption or decryption.
     *
     * @param data           The data.
     * @param key            The key.
     * @param keySize        The size of key, e.g. 1024, 2048...
     * @param transformation The name of the transformation, e.g., *DES/CBC/PKCS1Padding*.
     * @param isEncrypt      True to encrypt, false otherwise.
     * @return the bytes of RSA encryption or decryption
     */
    @SuppressLint("DefaultLocale")
    private fun rsaTemplate(data: ByteArray?, key: ByteArray?, keySize: Int, transformation: String, isEncrypt: Boolean): ByteArray? {
        if (data == null || data.isEmpty() || key == null || key.isEmpty()) {
            return null
        }
        try {
            val rsaKey: Key
            rsaKey = if (isEncrypt) {
                val keySpec = X509EncodedKeySpec(key)
                KeyFactory.getInstance("RSA").generatePublic(keySpec)
            } else {
                val keySpec = PKCS8EncodedKeySpec(key)
                KeyFactory.getInstance("RSA").generatePrivate(keySpec)
            }
            if (rsaKey == null) return null
            val cipher: Cipher = Cipher.getInstance(transformation)
            cipher.init(if (isEncrypt) Cipher.ENCRYPT_MODE else Cipher.DECRYPT_MODE, rsaKey)
            val len = data.size
            var maxLen = keySize / 8
            if (isEncrypt) {
                val lowerTrans = transformation.toLowerCase()
                if (lowerTrans.endsWith("pkcs1padding")) {
                    maxLen -= 11
                }
            }
            val count = len / maxLen
            return if (count > 0) {
                var ret: ByteArray? = ByteArray(0)
                var buff = ByteArray(maxLen)
                var index = 0
                for (i in 0 until count) {
                    System.arraycopy(data, index, buff, 0, maxLen)
                    ret = joins(ret!!, cipher.doFinal(buff))
                    index += maxLen
                }
                if (index != len) {
                    val restLen = len - index
                    buff = ByteArray(restLen)
                    System.arraycopy(data, index, buff, 0, restLen)
                    ret = joins(ret!!, cipher.doFinal(buff))
                }
                ret
            } else {
                cipher.doFinal(data)
            }
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: NoSuchPaddingException) {
            e.printStackTrace()
        } catch (e: InvalidKeyException) {
            e.printStackTrace()
        } catch (e: BadPaddingException) {
            e.printStackTrace()
        } catch (e: IllegalBlockSizeException) {
            e.printStackTrace()
        } catch (e: InvalidKeySpecException) {
            e.printStackTrace()
        }
        return null
    }

    ///////////////////////////////////////////////////////////////////////////
    // other utils methods
    ///////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////
// other utils methods
///////////////////////////////////////////////////////////////////////////
    private fun joins(prefix: ByteArray, suffix: ByteArray): ByteArray {
        val ret = ByteArray(prefix.size + suffix.size)
        System.arraycopy(prefix, 0, ret, 0, prefix.size)
        System.arraycopy(suffix, 0, ret, prefix.size, suffix.size)
        return ret
    }

    private fun base64Encode(input: ByteArray): String {
        return Base64.encodeToString(input,Base64.NO_WRAP)
    }

    private fun base64Decode(input: ByteArray): ByteArray {
        return Base64.decode(input, Base64.NO_WRAP)
    }

    @SuppressLint("DefaultLocale")
    private fun hexString2Bytes(hexString: String): ByteArray? {
        var hexString = hexString
        if (isSpace(hexString)) return null
        var len = hexString.length
        if (len % 2 != 0) {
            hexString = "0$hexString"
            len += 1
        }
        val hexBytes = hexString.toUpperCase().toCharArray()
        val ret = ByteArray(len shr 1)
        var i = 0
        while (i < len) {
            ret[i shr 1] =
                (hex2Dec(hexBytes[i]) shl 4 or hex2Dec(hexBytes[i + 1])).toByte()
            i += 2
        }
        return ret
    }

    private fun hex2Dec(hexChar: Char): Int {
        return when (hexChar) {
            in '0'..'9' -> {
                hexChar - '0'
            }
            in 'A'..'F' -> {
                hexChar - 'A' + 10
            }
            else -> {
                throw IllegalArgumentException()
            }
        }
    }

    private fun isSpace(s: String?): Boolean {
        if (s == null) return true
        var i = 0
        val len = s.length
        while (i < len) {
            if (!Character.isWhitespace(s[i])) {
                return false
            }
            ++i
        }
        return true
    }
    ///////////////////////////////////////////////////////////////////////////
    // AES encryption
    ///////////////////////////////////////////////////////////////////////////
    /**
     * Return the Base64-encode bytes of AES encryption.
     *
     * @param data           The data.
     * @param key            The key.
     * @param transformation The name of the transformation, e.g., *DES/CBC/PKCS5Padding*.
     * @param iv             The buffer with the IV. The contents of the
     * buffer are copied to protect against subsequent modification.
     * @return the Base64-encode bytes of AES encryption
     */
    fun encryptAES2Base64(data: ByteArray?, key: ByteArray?, transformation: String?, iv: ByteArray?): String {
        return base64Encode(encryptAES(data, key, transformation, iv)!!)
    }

    /**
     * Return the hex string of AES encryption.
     *
     * @param data           The data.
     * @param key            The key.
     * @param transformation The name of the transformation, e.g., *DES/CBC/PKCS5Padding*.
     * @param iv             The buffer with the IV. The contents of the
     * buffer are copied to protect against subsequent modification.
     * @return the hex string of AES encryption
     */
    fun encryptAES2HexString(
        data: ByteArray?,
        key: ByteArray?,
        transformation: String?,
        iv: ByteArray?
    ): String? {
        return bytes2HexString(encryptAES(data, key, transformation, iv))
    }

    /**
     * Return the bytes of AES encryption.
     *
     * @param data           The data.
     * @param key            The key.
     * @param transformation The name of the transformation, e.g., *DES/CBC/PKCS5Padding*.
     * @param iv             The buffer with the IV. The contents of the
     * buffer are copied to protect against subsequent modification.
     * @return the bytes of AES encryption
     */
    fun encryptAES(data: ByteArray?, key: ByteArray?, transformation: String?, iv: ByteArray?): ByteArray? {
        return symmetricTemplate(data, key, "AES", transformation, iv, true)
    }

    /**
     * Return the bytes of AES decryption for Base64-encode bytes.
     *
     * @param data           The data.
     * @param key            The key.
     * @param transformation The name of the transformation, e.g., *DES/CBC/PKCS5Padding*.
     * @param iv             The buffer with the IV. The contents of the
     * buffer are copied to protect against subsequent modification.
     * @return the bytes of AES decryption for Base64-encode bytes
     */
    fun decryptBase64AES(
        data: ByteArray?,
        key: ByteArray?,
        transformation: String?,
        iv: ByteArray?
    ): ByteArray? {
        return decryptAES(base64Decode(data!!), key, transformation, iv)
    }

    /**
     * Return the bytes of AES decryption for hex string.
     *
     * @param data           The data.
     * @param key            The key.
     * @param transformation The name of the transformation, e.g., *DES/CBC/PKCS5Padding*.
     * @param iv             The buffer with the IV. The contents of the
     * buffer are copied to protect against subsequent modification.
     * @return the bytes of AES decryption for hex string
     */
    fun decryptHexStringAES(
        data: String?,
        key: ByteArray?,
        transformation: String?,
        iv: ByteArray?
    ): ByteArray? {
        return decryptAES(hexString2Bytes(data!!), key, transformation, iv)
    }

    /**
     * Return the bytes of AES decryption.
     *
     * @param data           The data.
     * @param key            The key.
     * @param transformation The name of the transformation, e.g., *DES/CBC/PKCS5Padding*.
     * @param iv             The buffer with the IV. The contents of the
     * buffer are copied to protect against subsequent modification.
     * @return the bytes of AES decryption
     */
    fun decryptAES(
        data: ByteArray?,
        key: ByteArray?,
        transformation: String?,
        iv: ByteArray?
    ): ByteArray? {
        return symmetricTemplate(data, key, "AES", transformation, iv, false)
    }

    /**
     * Return the bytes of symmetric encryption or decryption.
     *
     * @param data           The data.
     * @param key            The key.
     * @param algorithm      The name of algorithm.
     * @param transformation The name of the transformation, e.g., *DES/CBC/PKCS5Padding*.
     * @param isEncrypt      True to encrypt, false otherwise.
     * @return the bytes of symmetric encryption or decryption
     */
    private fun symmetricTemplate(
        data: ByteArray?,
        key: ByteArray?,
        algorithm: String,
        transformation: String?,
        iv: ByteArray?,
        isEncrypt: Boolean
    ): ByteArray? {
        return if (data == null || data.size == 0 || key == null || key.size == 0) null else try {
            val secretKey: SecretKey
            if ("DES" == algorithm) {
                val desKey = DESKeySpec(key)
                val keyFactory: SecretKeyFactory = SecretKeyFactory.getInstance(algorithm)
                secretKey = keyFactory.generateSecret(desKey)
            } else {
                secretKey = SecretKeySpec(key, algorithm)
            }
            val cipher = Cipher.getInstance(transformation)
            if (iv == null || iv.isEmpty()) {
                cipher.init(if (isEncrypt) Cipher.ENCRYPT_MODE else Cipher.DECRYPT_MODE, secretKey)
            } else {
                val params: AlgorithmParameterSpec = IvParameterSpec(iv)
                cipher.init(if (isEncrypt) Cipher.ENCRYPT_MODE else Cipher.DECRYPT_MODE, secretKey, params)
            }
            cipher.doFinal(data)
        } catch (e: Throwable) {
            e.printStackTrace()
            null
        }
    }
    fun getMD5(filePath: String?): String? {
        var inputStream: InputStream? = null
        try {
            inputStream = FileInputStream(File(filePath))
            val md5 = StringBuffer()
            val md = MessageDigest.getInstance("MD5")
            val dataBytes = ByteArray(1024)
            var nread = 0
            while (inputStream.read(dataBytes).also { nread = it } != -1) {
                md.update(dataBytes, 0, nread)
            }
            val mdbytes = md.digest()
            for (i in mdbytes.indices) {
                md5.append(((mdbytes[i] and 0xff.toByte()) + 0x100).toString(16).substring(1))
            }
            return md5.toString().lowercase()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

}