package ru.descend.gamekotlin.fragments

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.viewbinding.library.fragment.viewBinding
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.descend.dellib.extensions.log
import ru.descend.dellib.extensions.safetyNavigate
import ru.descend.dellib.extensions.setVisibility
import ru.descend.dellib.extensions.toast
import ru.descend.gamekotlin.R
import ru.descend.gamekotlin.databinding.FragmentAuthorizationBinding
import ru.descend.gamekotlin.helper.*
import ru.descend.gamekotlin.main.MainViewModel
import java.util.*

class FragmentAuthorization : AbstractFragment("Авторизация") {

    private val binding: FragmentAuthorizationBinding by viewBinding()

    private val firebaseAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val mGoogleSignInClient: GoogleSignInClient by lazy {
        GoogleSignIn.getClient(
            requireActivity(), GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestProfile()
                .build()
        )
    }

    private val googleActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                handleResult(GoogleSignIn.getSignedInAccountFromIntent(result.data))
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_authorization, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initExe()

        lifecycleScope.launch(Dispatchers.Main) {
            snackView.setVisibility(false)
        }
    }

    private fun initExe() = binding.apply {
        buttonEnter.setOnClickListener {
            signInGoogle()
//            DesDialogAccept("Перейти на следующий фрагмент? Вы точно этого хотите?", "Перейти", EnumConfirmColors.RED){
//                DesSnackBar(snackView, "Перейдено на следующий фрагмент", SnackColors.ACCESS)
//                safetyNavigate(FragmentAuthorizationDirections.actionFragmentAuthorizationToFragmentMain())
//                snackView.setVisibility(true)
//            }
        }
    }

    private fun signInGoogle() {
        googleActivity.launch(mGoogleSignInClient.signInIntent)
    }

    override fun onStart() {
        super.onStart()
        if (GoogleSignIn.getLastSignedInAccount(requireContext()) != null) {
            startLaunch()
        }
    }

    private fun handleResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount? = completedTask.getResult(ApiException::class.java)
            if (account != null) {
                updateUI(account)
            }
        } catch (e: ApiException) {
            log(e.status.statusMessage ?: ("" + " code:" + e.statusCode))
        }
    }

    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                startLaunch()
            } else {
                toast("Проверьте подключение к Интернету")
            }
        }
    }

    private fun startLaunch() = binding.apply {
        firebaseAuth.currentUser?.let { user ->
            lifecycleScope.launch {
                progress.show()
                buttonEnter.isEnabled = false
                if (refUsers.exists(user.uid)) {
                    refUsers.document(user.uid).set(mapOf("first_enter" to Date().time))
                }
                val UID = refUsers.document(user.uid).id
                refUsers.document(user.uid).update(
                    mapOf(
                        "uid" to UID,
                        "name" to user.displayName,
                        "last_enter" to Date().time,
                        "email" to user.email,
                        "icon" to user.photoUrl.toString()
                    )
                ).addOnCompleteListener {
                    progress.hide()
                    if (it.isSuccessful) {
                        MainViewModel.initialize(user)
                        MainViewModel.initializeHero(requireContext())
                        binding.root.post {
                            safetyNavigate(FragmentAuthorizationDirections.actionFragmentAuthorizationToFragmentMain())
                            snackView.setVisibility(true)
                        }
                    } else {
                        buttonEnter.isEnabled = true
                    }
                }
            }
        }
    }
}