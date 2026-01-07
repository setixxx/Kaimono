package setixx.software.kaimono.data.local

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import dagger.hilt.android.qualifiers.ApplicationContext
import setixx.software.kaimono.domain.model.AuthTokens
import java.nio.charset.StandardCharsets
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    companion object {
        private const val PREFS_NAME = "encrypted_auth_prefs"
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
        private const val ANDROID_KEYSTORE = "AndroidKeyStore"
        private const val ALIAS = "kaimono_auth_key"
        private const val TRANSFORMATION = "AES/GCM/NoPadding"
    }

    private val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    init {
        createKeyIfNotExists()
    }

    fun saveTokens(tokens: AuthTokens) {
        val encryptedAccessToken = encrypt(tokens.accessToken)
        val encryptedRefreshToken = encrypt(tokens.refreshToken)

        sharedPreferences.edit()
            .putString(KEY_ACCESS_TOKEN, encryptedAccessToken)
            .putString(KEY_REFRESH_TOKEN, encryptedRefreshToken)
            .apply()
    }

    fun getAccessToken(): String? {
        val encryptedToken = sharedPreferences.getString(KEY_ACCESS_TOKEN, null) ?: return null
        return decrypt(encryptedToken)
    }

    fun getRefreshToken(): String? {
        val encryptedToken = sharedPreferences.getString(KEY_REFRESH_TOKEN, null) ?: return null
        return decrypt(encryptedToken)
    }

    fun clearTokens() {
        sharedPreferences.edit().clear().apply()
    }

    private fun createKeyIfNotExists() {
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE)
        keyStore.load(null)

        if (!keyStore.containsAlias(ALIAS)) {
            val keyGenerator = KeyGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_AES,
                ANDROID_KEYSTORE
            )
            val keyGenParameterSpec = KeyGenParameterSpec.Builder(
                ALIAS,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .build()

            keyGenerator.init(keyGenParameterSpec)
            keyGenerator.generateKey()
        }
    }

    private fun getSecretKey(): SecretKey {
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE)
        keyStore.load(null)
        return keyStore.getKey(ALIAS, null) as SecretKey
    }

    private fun encrypt(data: String): String {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey())
        
        val iv = cipher.iv
        val encryption = cipher.doFinal(data.toByteArray(StandardCharsets.UTF_8))

        val combined = ByteArray(1 + iv.size + encryption.size)
        combined[0] = iv.size.toByte()
        System.arraycopy(iv, 0, combined, 1, iv.size)
        System.arraycopy(encryption, 0, combined, 1 + iv.size, encryption.size)
        
        return Base64.encodeToString(combined, Base64.DEFAULT)
    }

    private fun decrypt(encryptedData: String): String? {
        return try {
            val decoded = Base64.decode(encryptedData, Base64.DEFAULT)
            
            val ivSize = decoded[0].toInt()
            val iv = ByteArray(ivSize)
            System.arraycopy(decoded, 1, iv, 0, ivSize)
            
            val encryptedContentSize = decoded.size - 1 - ivSize
            val encryptedContent = ByteArray(encryptedContentSize)
            System.arraycopy(decoded, 1 + ivSize, encryptedContent, 0, encryptedContentSize)
            
            val cipher = Cipher.getInstance(TRANSFORMATION)
            val spec = GCMParameterSpec(128, iv)
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(), spec)
            
            val decryptedBytes = cipher.doFinal(encryptedContent)
            String(decryptedBytes, StandardCharsets.UTF_8)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
