package hr.tomislav.stipic.chatpractice

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.custom_edit_text_field.view.*

class LoginActivity : AppCompatActivity() {

    var scale = 0f
    private lateinit var hint: String
    private lateinit var l_a_u_l: LinearLayout
    private lateinit var l_a_p_l: LinearLayout

    override fun onStart() {
        super.onStart()
        scale = this.resources.displayMetrics.density
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login);

        setupView()
        l_a_u_l.customEditTextBoxMain.setOnFocusChangeListener { view, b ->
            if((view.customEditTextBoxMain.hint as String).isNotEmpty()) {
                hint = view.customEditTextBoxMain.hint as String }
            RegistrationActivity.onEditTextAnimation(view, b, hint, scale)
        }

        l_a_p_l.customEditTextBoxMain.setOnFocusChangeListener { view, b ->
            if((view.customEditTextBoxMain.hint as String).isNotEmpty()) {
                hint = view.customEditTextBoxMain.hint as String }
            RegistrationActivity.onEditTextAnimation(view, b, hint, scale)
        }

        login_register_redirect_text_view.setOnClickListener {
            Log.d("Login activity", "Try to show register activity");

            // go to register activity
            goToRegistration()
            finish()
        }

    }

    fun login() {

    }

    fun goToRegistration() {
        Log.d("Login activity", "Try to show registration activity");
        // launch the login activity
        val intent = Intent(this, RegistrationActivity::class.java)
        startActivity(intent)
    }

    fun setupView() {
        l_a_u_l = login_activity_username_layout as LinearLayout
        l_a_p_l = login_activity_password_layout as LinearLayout
        l_a_u_l.customEditTextBoxMain.hint = "Username"
        l_a_u_l.customEditTextBoxHint.setText("Your unique username")
        l_a_p_l.customEditTextBoxMain.hint = "Password"
        l_a_p_l.customEditTextBoxHint.setText("Your password")
    }

}