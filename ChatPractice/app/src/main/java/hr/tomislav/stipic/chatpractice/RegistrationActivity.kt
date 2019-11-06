package hr.tomislav.stipic.chatpractice

import android.content.Context
import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.ViewParent
import android.webkit.WebView
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.WithHint
import androidx.core.text.set
import androidx.core.view.updateLayoutParams
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.view.*
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.android.synthetic.main.custom_edit_text_field.view.*

class RegistrationActivity : AppCompatActivity() {

    var scale = 0f
    private lateinit var hint: String
    private lateinit var auth: FirebaseAuth
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var username: String
    private lateinit var repeatPassword: String
    private lateinit var r_a_u_l: LinearLayout
    private lateinit var r_a_e_l: LinearLayout
    private lateinit var r_a_p_l: LinearLayout
    private lateinit var r_a_r_p_l: LinearLayout

    public override fun onStart() {
        super.onStart()
        scale = this.resources.displayMetrics.density
        // Check if user is signed in (non-null) and update UI accordingly.
        // val currentUser = auth.currentUser
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        // initialise the UI
        setupView()

        // open LoginActivity if already registered
        register_already_registered_text_view.setOnClickListener { goToLogin()}

        // Set on editText edit listeners for animations of the editText views
        r_a_u_l.customEditTextBoxMain.setOnFocusChangeListener { view, b ->
                if((view.customEditTextBoxMain.hint as String).isNotEmpty()) {
                    hint = view.customEditTextBoxMain.hint as String }
                onEditTextAnimation(view, b, hint, scale) }

        r_a_e_l.customEditTextBoxMain.setOnFocusChangeListener { view, b ->
            if((view.customEditTextBoxMain.hint as String).isNotEmpty()) {
                hint = view.customEditTextBoxMain.hint as String }
            onEditTextAnimation(view, b, hint, scale) }

        r_a_p_l.customEditTextBoxMain.setOnFocusChangeListener { view, b ->
            if((view.customEditTextBoxMain.hint as String).isNotEmpty()) {
                hint = view.customEditTextBoxMain.hint as String }
            onEditTextAnimation(view, b, hint, scale) }

        r_a_r_p_l.customEditTextBoxMain.setOnFocusChangeListener { view, b ->
            if((view.customEditTextBoxMain.hint as String).isNotEmpty()) {
                hint = view.customEditTextBoxMain.hint as String }
            onEditTextAnimation(view, b, hint, scale) }

        // jiken1.customEditTextBoxMain.setText("WTF")

        registration_register_button.setOnClickListener {
            //notEmptyInput()
        }



    }

    /*fun notEmptyInput() {
        email = emailJiken.text.toString()
        password = passwordJiken.text.toString()
        username = usernameJiken.text.toString()
        repeatPassword = passwordRJiken.text.toString()

        if (username.isEmpty() || email.isEmpty() || password.isEmpty())
        {
            Toast.makeText(baseContext, "None of the fields may be left empty",  Toast.LENGTH_LONG).show()
            return
        }
        if (!password.equals(repeatPassword)) {
            Toast.makeText(baseContext, "Password fields aren't matching", Toast.LENGTH_LONG).show()
            return
        }
        if (!email.isEmailValid()) {
            Toast.makeText(baseContext, "Invalid email address", Toast.LENGTH_LONG).show()
            return
        }
        registerButtonAction()
    }*/

    fun goToLogin() {
        Log.d("Main activity", "Try to show login activity")
        // launch the login activity
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    // https://stackoverflow.com/questions/56214609/how-to-check-if-a-string-is-a-valid-email-in-android
    fun String.isEmailValid(): Boolean {
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }

    fun registerButtonAction() {

        // Firebase Authentication to create a user with email and password
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("Registration activity", "createUserWithEmail:success. UserID = ${task.result?.user?.uid}")
                    val user = auth.currentUser
                    goToLogin()
                    finish()
                    // Update UI
                    //updateUI(user)
                } else {

                    // If sign in fails, display a message to the user.
                    Log.w("Registration activity", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed. ${task.exception?.message}",
                        Toast.LENGTH_LONG
                    ).show()

                    //updateUI(null)
                }
            }
    }

    // Companion objects are a way to mimic Java static methods in Kotlin
    companion object {

        private fun pxToDP(px: Int, scale: Float): Int {
            return (px * scale + 0.5f).toInt()
        }
        fun onEditTextAnimation(view: View, b: Boolean, h: String, scale: Float) {
            val r = view.parent as LinearLayout
            if (b) {
                r.customEditTextBoxMain.hint = ""
                r.customEditTextBoxMain.layoutParams.height = pxToDP(37, scale)
                r.customEditTextBoxHint.layoutParams.height = pxToDP(13, scale)
                r.customEditTextBoxHint.visibility = View.VISIBLE
                r.redLine.visibility = View.VISIBLE
            }
            if (!b) {
                r.customEditTextBoxMain.hint = h
                r.customEditTextBoxMain.layoutParams.height = pxToDP(50, scale)
                r.customEditTextBoxHint.visibility = View.GONE
                r.redLine.visibility = View.GONE
            }
        }
    }



    fun setupView() {
        r_a_u_l = registration_activity_username_layout as LinearLayout
        r_a_e_l = registration_activity_email_layout as LinearLayout
        r_a_p_l = registration_activity_password_layout as LinearLayout
        r_a_r_p_l = registration_activity_repeat_password_layout as LinearLayout
        r_a_u_l.customEditTextBoxHint.text = "Make up a username. Try to be unique? :)"
        r_a_u_l.customEditTextBoxMain.hint = "Username"
        r_a_e_l.customEditTextBoxHint.text = "Input a valid email not associated with another account"
        r_a_e_l.customEditTextBoxMain.hint = "Email"
        r_a_p_l.customEditTextBoxHint.text = "Your password needs to be longer than 6 characters"
        r_a_p_l.customEditTextBoxMain.hint = "Password"
        r_a_r_p_l.customEditTextBoxHint.text = "It needs to match."
        r_a_r_p_l.customEditTextBoxMain.hint = "Repeat password"
    }

}