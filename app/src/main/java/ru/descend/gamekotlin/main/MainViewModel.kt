package ru.descend.gamekotlin.main

import android.app.Application
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import ru.descend.dellib.HashFile
import ru.descend.dellib.elements.DesSnackBar
import ru.descend.dellib.extensions.log
import ru.descend.gamekotlin.model.modelValues.Hero
import ru.descend.gamekotlin.helper.*
import ru.descend.gamekotlin.model.modelFirebase.HeroModel
import java.io.File

class MainViewModel(application: Application) : AndroidViewModel(application) {

    companion object {

        lateinit var file: File
        lateinit var hero: Hero
        lateinit var currentUser: FirebaseUser

        fun Fragment.saveOffline(){
            cryptoWrite(hero)
            DesSnackBar(snackView, "Персонаж сохранен: ${hero.characterName}")
        }

        fun Fragment.saveOnline(){
            runAwaiting {
                val byteFile = file.readBytes()
                val hashFile = HashFile.hashFromFile(file)
                val allHeroes = getHeroList()
                log("LEN: ${allHeroes?.size}")
                refHeroes.exists("md5hash", hashFile) {
                    DesSnackBar(snackView, "Изменений не обнаружено. Сохранение не требуется")
                }.run {
                    if (!this) {
                        if (allHeroes.isNullOrEmpty()) {
                            val res = createFieldWithID(refHeroes, HeroModel(String(byteFile), hashFile))
                            DesSnackBar(snackView, "Создание нового персонажа: ${res.uid}")
                        } else {
                            if (allHeroes.size == 1){
                                allHeroes.first().update(mapOf("sourceFile" to String(byteFile)))
                                DesSnackBar(snackView, "Сохранение прошло успешно")
                            } else {
                                DesSnackBar(snackView, "Нельзя создавать больше 1 персонажа")
                            }
                        }
                    }
                }
            }
        }

        fun initializeHero(context: Context) {
            file = File(context.applicationInfo.dataDir + "/currentHero.dsc")
            if (!file.exists()) file.createNewFile()
            loadCharacter()
        }

        private fun loadCharacter() {
            if (file.length() == 0L) {
                hero = Hero("Новый герой", 100)
                log("Создан новый герой: ${hero.UID}")
            } else {
                val readFile = cryptoRead<Hero>(file)
                if (readFile != null){
                    hero = readFile
                    log("Загружен герой: ${hero.characterName} ${hero.UID} ${hero.inventory.itemsMoney.money}")
                } else {
                    hero = Hero("Новый герой", 100)
                    log("Создан новый герой: ${hero.UID}")
                }
            }
        }

        var currentUserSnapshot: DocumentSnapshot? = null

        val mutableHero: MutableLiveData<ArrayList<HeroModel>> = MutableLiveData()

        fun getHeroList() = mutableHero.value

        fun <T> MutableLiveData<T>.notifyUpdate() {
            value = value
        }

        fun initialize(user: FirebaseUser?) {
            if (user != null) {
                currentUser = user
                startListening()
            }
        }

        private fun startListening() {
            refHeroes.addSnapshotListener { value, _ ->
                    mutableHero.value =
                        value!!.documents.map { it.toObject(HeroModel::class.java)!! } as ArrayList<HeroModel>
                }
        }
    }
}
