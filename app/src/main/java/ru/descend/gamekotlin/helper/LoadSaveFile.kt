package ru.descend.gamekotlin.helper

import android.util.Base64
import ru.descend.dellib.extensions.toMD5
import ru.descend.dellib.log
import ru.descend.gamekotlin.Crypto
import ru.descend.gamekotlin.Crypto.iv
import ru.descend.gamekotlin.main.MainViewModel.Companion.file
import java.io.*
import javax.crypto.*
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

fun File.hashSave() = readText().toMD5()

fun <T> cryptoRead(file: File): T? {
    var cipherInputStream: CipherInputStream? = null
    var inputStream: ObjectInputStream? = null
    log("FILE HASH: ${file.hashSave()}")
    return try {
        val ivParameterSpec = IvParameterSpec(Base64.decode(iv, Base64.DEFAULT))
        val cipher: Cipher = Cipher.getInstance("AES/CBC/PKCS7Padding")
        cipher.init(Cipher.DECRYPT_MODE, getCryptoKey(), ivParameterSpec)
        cipherInputStream =
            CipherInputStream(BufferedInputStream(FileInputStream(file)), cipher)
        inputStream = ObjectInputStream(cipherInputStream)
        val sealedObject = inputStream.readObject() as SealedObject
        sealedObject.getObject(cipher) as T
    } catch (e: Exception){
        null
    } finally {
        cipherInputStream?.close()
        inputStream?.close()
    }
}

fun cryptoWrite(obj: Any) {
    val ivParameterSpec = IvParameterSpec(Base64.decode(iv, Base64.DEFAULT))
    val cipher: Cipher = Cipher.getInstance("AES/CBC/PKCS7Padding")
    cipher.init(Cipher.ENCRYPT_MODE, getCryptoKey(), ivParameterSpec)
    val sealedEm1 = SealedObject(obj as Serializable?, cipher)
    val fos = FileOutputStream(file)
    val bos = BufferedOutputStream(fos)
    val cos = CipherOutputStream(bos, cipher)
    val oos = ObjectOutputStream(cos)
    oos.writeObject(sealedEm1)
    oos.flush()
    oos.close()
}

private fun getCryptoKey(): SecretKeySpec {
    val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
    val spec = PBEKeySpec(
        Crypto.secretKey.toCharArray(),
        Base64.decode(Crypto.salt, Base64.DEFAULT),
        10000,
        256
    )
    val tmp = factory.generateSecret(spec)
    return SecretKeySpec(tmp.encoded, "AES")
}