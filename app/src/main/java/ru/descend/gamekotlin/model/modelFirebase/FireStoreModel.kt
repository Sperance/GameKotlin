package ru.descend.gamekotlin.model.modelFirebase

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Exclude
import kotlinx.coroutines.CompletableDeferred
import ru.descend.dellib.extensions.logToInfo
import ru.descend.gamekotlin.helper.await
import ru.descend.gamekotlin.helper.localDB

abstract class FireStoreModel constructor(
    val uid: String? = null,
    val reference: String? = null,
    val createDate: Long? = null,

    var sorting: Int = 0,
    var systemField: Boolean = false
) {

    @Exclude
    suspend fun delete() {
        if (reference != null) {
            logToInfo("Удаление объекта в [${path()}] с uid [$uid]")
            localDB.document(reference).delete().await()
        }
    }

    @Exclude
    private fun path() = reference?.substringBeforeLast("/")?.substringAfterLast("/")

    @Exclude
    suspend fun update(map: Map<String, Any?>, body: (() -> Any?)? = null) {
        val result = CompletableDeferred<Boolean>()
        var stringLog = ""
        map.forEach { (s, any) ->
            stringLog += "$s -> $any\n"
        }
        logToInfo("Обновление элемента ${path()} с uid $uid\n" + stringLog)
        toReference().update(map).addOnCompleteListener {
            body?.invoke()
            result.complete(it.isSuccessful)
        }
        result.await()
    }

    @Exclude
    fun toReference(): DocumentReference {
        return reference?.let { localDB.document(it) }!!
    }

    @Exclude
    override fun toString(): String {
        return "FirestoreModel(uid=$uid, reference=$reference, createDate=$createDate)"
    }
}