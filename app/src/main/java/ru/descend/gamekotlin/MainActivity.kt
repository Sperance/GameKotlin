package ru.descend.gamekotlin

import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.descend.gamekotlin.Crypto.iv
import java.io.*
import java.math.BigInteger
import java.security.MessageDigest
import javax.crypto.*
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

class MainActivity : AppCompatActivity() {

    private val file by lazy { File(applicationInfo.dataDir + "/currentHero.dsc") }
    private lateinit var hero: Warrior
    private var counter = 0

    fun loadCharacter() {
        repeat(20) {
            hero = cryptoRead()
            counter++
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val circ: ProgressBar = findViewById(R.id.circularItem)
        val btn: Button = findViewById(R.id.buttn)

//        circ.visibility = View.VISIBLE
//        lifecycleScope.launch(Dispatchers.IO) {
//            log("START")
//            loadCharacter()
//            log("CLOSED")
//            runOnUiThread {
//                circ.visibility = View.GONE
//            }
//        }

        btn.setOnClickListener {
            circ.visibility = View.VISIBLE
            repeat(10) {
                lifecycleScope.launch(Dispatchers.IO) {
                    log("START: ${Thread.currentThread().name}")
                    loadCharacter()
                    log("CLOSED: ${Thread.currentThread().name}")
                    runOnUiThread {
                        circ.visibility = View.GONE
                    }
                }

            }
        }

//        btn.setOnClickListener {
//            circ.visibility = View.VISIBLE
//            lifecycleScope.launch {
//                log("START")
//                loadCharacter()
//                log("CLOSED")
//                runOnUiThread {
//                    log("G")
//                    circ.visibility = View.GONE
//                }
//            }
//        }

//        log("Персонаж загружен: ${hero.characterName}")
//
//        val enemyOne = Enemy("Годзилла", 1000)
//
//        val helmet = Helmet("Шлем силы")
//        helmet.changeStats {
//            it.add(Defence(14))
//            it.add(Health(52))
//        }
//
//        enemyOne.addItem(helmet)
//        enemyOne.addItem(helmet)
//
//        log("ENEMY STAT NEW: ${enemyOne.stats.printStat()}")
//
//        log("HELMET STATS: ${helmet.stats.printStat()}")
//
//        val weapon = Weapon("Меч смерти")
//        weapon.changeStats {
//            it.add(Damage(31, 10))
//            it.add(Speed(0, 5))
//        }
//
//        helmet.nameItem = "Шлем классности 151"
//        hero.equipItem(helmet)
//        hero.equipItem(weapon)
//
//        log(" ОРУЖИЕ ЭКИПИРОВАНО: ${hero.weapon?.nameItem}")
//
//        hero.characterHealth = 23
//        hero.setMaxHealth(25)
//
//        log("HLEM REF: ${hero.helmet?.nameItem}")
//        log("HASH: ${hashSave()}")
    }

    override fun onDestroy() {
        cryptoWrite(hero)
        log("Персонаж сохранен: ${hero.characterName}")
        super.onDestroy()
    }

    fun hashSave() = file.readText().toMD5()

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
        oos.close()
    }

    fun <T> cryptoRead(): T {
        val ivParameterSpec = IvParameterSpec(Base64.decode(iv, Base64.DEFAULT))
        val cipher: Cipher = Cipher.getInstance("AES/CBC/PKCS7Padding")
        cipher.init(Cipher.DECRYPT_MODE, getCryptoKey(), ivParameterSpec)
        val cipherInputStream =
            CipherInputStream(BufferedInputStream(FileInputStream(file)), cipher)
        val inputStream = ObjectInputStream(cipherInputStream)
        val sealedObject = inputStream.readObject() as SealedObject
        return sealedObject.getObject(cipher) as T
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
}

fun String.toMD5() =
    BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16)
        .padStart(32, '0')