package ru.descend.gamekotlin.helper

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import ru.descend.dellib.elements.DesSnackBar
import ru.descend.dellib.elements.SnackColors
import ru.descend.dellib.extensions.isNotNull
import ru.descend.dellib.extensions.log
import ru.descend.dellib.extensions.logToInfo
import ru.descend.gamekotlin.main.MainViewModel.Companion.currentUser
import java.util.*

val localDB: FirebaseFirestore
    get() = Firebase.firestore

val myRef: DocumentReference
    get() = refUsers.document(currentUser.uid)

val refHeroes: CollectionReference
    get() = myRef.collection("Heroes")

val refUsers: CollectionReference
    get() = localDB.collection("Users")

/**
 * Запуск потока, выполнение которого необходимо ожидать, не блокируя главный поток
 */
fun Fragment.runAwaiting(body: suspend (() -> Any)) = lifecycleScope.launch {
    body.invoke()
}

/**
 * Запуск потока, выполнение которого необходимо ожидать, не блокируя главный поток
 */
suspend fun runAwaiting(body: suspend (() -> Any)) {
    body.invoke()
}

/**
 * Запуск потока, выполнение которого необходимо убедиться, не блокируя главный поток
 */
suspend fun runCompletable(body: suspend ((CompletableDeferred<Boolean>) -> Any)): Boolean {
    val result = CompletableDeferred<Boolean>()
    body.invoke(result)
    return result.await()
}

suspend fun checkCollectionExists(reference: CollectionReference, findValue: String): Boolean {
    val result = CompletableDeferred<Boolean>()
    reference.get().addOnCompleteListener {
        if (it.isSuccessful) {
            result.complete(it.result.documents.firstOrNull { doc -> doc.id == findValue }.isNotNull)
        } else {
            result.complete(false)
        }
    }
    return result.await()
}

//suspend fun addImageToStorage(peopleUid: String, uri: Uri?): Boolean {
//    val result = CompletableDeferred<Boolean>()
//    if (uri == null) {
//        result.complete(true)
//    } else {
//        val path = storage.child(peopleUid)
//        path.putFile(uri).addOnCompleteListener { putFile ->
//            if (putFile.isSuccessful) {
//                path.downloadUrl.addOnCompleteListener {
//                    if (it.isSuccessful) {
//                        refPeople.document(peopleUid)
//                            .update(mapOf("image" to it.result.toString()))
//                            .addOnCompleteListener { last ->
//                                if (last.isSuccessful) result.complete(true)
//                                else result.complete(false)
//                            }
//                    } else result.complete(false)
//                }
//            } else result.complete(false)
//        }
//    }
//    return result.await()
//}

suspend fun <TResult> Task<TResult>.await(): TResult? {
    val result = CompletableDeferred<TResult?>()
    addOnCompleteListener {
        if (it.isSuccessful) result.complete(it.result)
        else result.complete(null)
    }
    return result.await()
}

suspend fun DocumentReference.await(): DocumentSnapshot? {
    val result = CompletableDeferred<DocumentSnapshot?>()
    get().addOnCompleteListener {
        if (it.isSuccessful) result.complete(it.result)
        else result.complete(null)
    }
    return result.await()
}

data class ReturnedField(
    var isDone: Boolean,
    var uid: String?
)

suspend fun CollectionReference.exists(item: String): Boolean {
    val result = CompletableDeferred<Boolean>()
    get().addOnCompleteListener {
        if (it.isSuccessful) {
            result.complete(it.result.documents.firstOrNull { doc -> doc.id == item }.isNotNull)
        } else {
            result.complete(false)
        }
    }
    return result.await()
}

suspend fun CollectionReference.exists(field: String, value: String, body: ((String) -> Any?)? = null): Boolean {
    val result = CompletableDeferred<Boolean>()
    get().addOnCompleteListener {
        if (it.isSuccessful) {
            it.result.documents.forEach { snapshot ->
                if (snapshot[field] == value) {
                    body?.invoke(snapshot.id)
                    result.complete(true)
                }
            }
            result.complete(false)
        } else {
            result.complete(false)
        }
    }
    return result.await()
}

suspend fun ViewBinding.createField(
    reference: CollectionReference?,
    data: Any,
    body: ((String) -> Any?)? = null
) {
    val result = createFieldWithID(reference, data)
    if (result.isDone) {
        if (!result.uid.isNullOrEmpty())
            body?.invoke(result.uid!!)
        else {
            body?.invoke(result.uid!!)
            DesSnackBar(
                this.root,
                "Создание объекта завершилось с ошибкой",
                snackMode = SnackColors.ERROR
            )
        }
    } else {
        body?.invoke(result.uid!!)
        DesSnackBar(this.root, result.uid!!, snackMode = SnackColors.ERROR)
    }
}

suspend fun createFieldWithID(reference: CollectionReference?, data: Any): ReturnedField {
    val result = CompletableDeferred<ReturnedField>()
    try {
        if (reference == null) {
            result.complete(ReturnedField(false, "CollectionReference is null value (not found)"))
        } else {
            reference.add(data).addOnCompleteListener {
                if (it.isSuccessful) {
                    logToInfo(
                        "Создание объекта в [${reference.path.substringAfterLast('/')}] с uid [${it.result.id}]",
                        1
                    )
                    reference.document(it.result.id).update(
                        mapOf(
                            "uid" to it.result.id,
                            "createDate" to Date().time,
                            "reference" to it.result.path
                        )
                    ).addOnCompleteListener { _ ->
                        result.complete(ReturnedField(false, it.result.id))
                    }
                } else {
                    result.complete(ReturnedField(false, it.exception?.localizedMessage))
                }
            }
        }
    } catch (e: RuntimeException) {
        result.complete(ReturnedField(false, e.localizedMessage))
    }
    return result.await()
}