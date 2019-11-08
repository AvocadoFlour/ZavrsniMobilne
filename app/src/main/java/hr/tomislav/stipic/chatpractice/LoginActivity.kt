package hr.tomislav.stipic.chatpractice

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.custom_edit_text_field.view.*

class LoginActivity : AppCompatActivity() {

    var scale = 0f
    private val tag = "LADebug"
    private lateinit var auth: FirebaseAuth
    private lateinit var hint: String
    private lateinit var laul: LinearLayout
    private lateinit var lapl: LinearLayout

    override fun onStart() {
        super.onStart()
        scale = this.resources.displayMetrics.density
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setupView()
        laul.customEditTextBoxMain.setOnFocusChangeListener { view, b ->
            if((view.customEditTextBoxMain.hint as String).isNotEmpty()) {
                hint = view.customEditTextBoxMain.hint as String }
            RegistrationActivity.onEditTextAnimation(view, b, hint, scale)
        }

        lapl.customEditTextBoxMain.setOnFocusChangeListener { view, b ->
            if((view.customEditTextBoxMain.hint as String).isNotEmpty()) {
                hint = view.customEditTextBoxMain.hint as String }
            RegistrationActivity.onEditTextAnimation(view, b, hint, scale)
        }

        login_register_redirect_text_view.setOnClickListener {
            Log.d(tag, "Try to show register activity")

            // go to register activity
            goToRegistration()
            finish()
        }

    }

    fun login(email: String, password: String) {
        auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(baseContext, "signInWithEmail:success", Toast.LENGTH_LONG)
                    val user = auth.currentUser
                } else {
                    // If sign in fails, display a message to the user.
                    // Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }

                // ...
            }

    }

    fun goToRegistration() {
        Log.d(tag, "Try to show registration activity")
        // launch the login activity
        val intent = Intent(this, RegistrationActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    fun setupView() {
        laul = login_activity_username_layout as LinearLayout
        lapl = login_activity_password_layout as LinearLayout
        laul.customEditTextBoxMain.hint = "Username"
        laul.customEditTextBoxHint.text = "Your unique username"
        lapl.customEditTextBoxMain.hint = "Password"
        lapl.customEditTextBoxHint.text = "Your password"
    }

}