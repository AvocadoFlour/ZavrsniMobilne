package hr.tomislav.stipic.chatpractice.startScreens

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.android.synthetic.main.custom_edit_text_field.view.*
import android.text.InputType
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import hr.tomislav.stipic.chatpractice.messages.MessagesActivity
import hr.tomislav.stipic.chatpractice.R
import hr.tomislav.stipic.chatpractice.objects.User
import java.util.*
import android.view.inputmethod.InputMethodManager


class RegistrationActivity : AppCompatActivity() {

    private val tag = "RADebug"
    private var scale = 0f
    var usernameCheck: Boolean = false
    var emailCheck: Boolean = false
    var passwordCheck: Boolean = false
    var passwordRepeatCheck: Boolean = false
    private lateinit var hint: String
    private lateinit var auth: FirebaseAuth
    private var email = "emptyemail"
    private var password = "emptypassword"
    private var username = "emptyusername"
    private lateinit var raul: LinearLayout
    private lateinit var rael: LinearLayout
    private lateinit var rapl: LinearLayout
    private lateinit var rarpl: LinearLayout
    private val greenColor = Color.parseColor("#228B22")
    private val lightGreenColor = Color.parseColor("#ddffd9")
    private val redColor = Color.parseColor("#ffcc0000")

    public override fun onStart() {
        super.onStart()
        scale = this.resources.displayMetrics.density
        // Check if user is signed in (non-null) and update UI accordingly.
        // val currentUser = auth.currentUser
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        // Remove focus from an input field
        registration_activity_global_container.setOnClickListener {
            hideKeyboard(this)
            window.decorView.clearFocus()
        }

        // initialise the UI
        setupView()

        // open LoginActivity if already registered
        register_already_registered_text_view.setOnClickListener { goToLogin()}

        // ON USERNAME INPUT  ON USERNAME INPUT  ON USERNAME INPUT  ON USERNAME INPUT

        // Set on editText edit listeners for animations of the editText views
        raul.customEditTextBoxMain.setOnFocusChangeListener { view, b ->
                if((view.customEditTextBoxMain.hint as String).isNotEmpty()) {
                    hint = view.customEditTextBoxMain.hint as String }
            onEditTextAnimation(
                view,
                b,
                hint,
                scale,
                usernameCheck
            )
        }

        // Check if the inputted username is valid and change the look accordingly
        raul.customEditTextBoxMain.addTextChangedListener(object:
            TextWatcher {override fun afterTextChanged(s: Editable?) {
        }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s!!.length in 1..7) {
                    raul.customEditTextBoxHint.text = getString(R.string.username_too_short)
                    raul.imageView.visibility = View.VISIBLE
                    val g: GradientDrawable = raul.customEditTextBoxMain.background as GradientDrawable
                    val r:  GradientDrawable = raul.customEditTextBoxHint.background as GradientDrawable
                    g.setStroke(
                        pxToDP(
                            2,
                            scale
                        ), redColor)
                    r.setColor(redColor)
                    usernameCheck = false
                } else {
                    raul.customEditTextBoxHint.text = getString(R.string.raul_hint)
                    raul.imageView.visibility = View.GONE

                    raul.customEditTextBoxHint.background
                    if (s.isNotEmpty()) {
                        usernameCheck = true
                    }
                }
                if(usernameCheck) {
                    val g: GradientDrawable = raul.customEditTextBoxMain.background as GradientDrawable
                    val r:  GradientDrawable = raul.customEditTextBoxHint.background as GradientDrawable
                    g.setStroke(
                        pxToDP(
                            2,
                            scale
                        ), greenColor)
                    g.setColor(lightGreenColor)
                    r.setColor(greenColor)
                } else {
                    val g: GradientDrawable = raul.customEditTextBoxMain.background as GradientDrawable
                    g.setColor(Color.WHITE)
                }
                username = s.toString()
            }
        })

        //   ON EMAIL INPUT    ON EMAIL INPUT    ON EMAIL INPUT    ON EMAIL INPUT    ON EMAIL INPUT

        rael.customEditTextBoxMain.setOnFocusChangeListener { view, b ->
            if((view.customEditTextBoxMain.hint as String).isNotEmpty()) {
                hint = view.customEditTextBoxMain.hint as String }
            onEditTextAnimation(
                view,
                b,
                hint,
                scale,
                emailCheck
            )
        }

        // Check if the email address entered is valid
        rael.customEditTextBoxMain.addTextChangedListener(object:TextWatcher{override fun afterTextChanged(s: Editable?) {
        }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // "count:" je koliko sam shvatio 0 ukoliko se maknulo slovo a 1 ukoliko se dodalo slovo, odnosno znak, u tom potezu
                // "s:" je unos u taj editText u tom potezu
                // "start:" je broj znakova trenutno u polju u tom potezu. Ovo ne radi baš kako spada odnosno nisi to točno shvatio
                // "before" je broj izbrisanih znakova u potezu
                if (s!!.isNotEmpty()) {
                    if(!s.toString().isEmailValid()) {
                        rael.customEditTextBoxHint.text = getString(R.string.invalid_email)
                        rael.imageView.visibility = View.VISIBLE
                        val g: GradientDrawable = rael.customEditTextBoxMain.background as GradientDrawable
                        val r:  GradientDrawable = rael.customEditTextBoxHint.background as GradientDrawable
                        g.setStroke(
                            pxToDP(
                                2,
                                scale
                            ), redColor)
                        r.setColor(redColor)
                        emailCheck = false
                    } else {
                        resetReal()
                        email = s.toString()
                        val g: GradientDrawable = rael.customEditTextBoxMain.background as GradientDrawable
                        g.setStroke(
                            pxToDP(
                                2,
                                scale
                            ), Color.parseColor("#32CD32"))
                        if (s.isNotEmpty()) {
                            emailCheck = true
                        }
                    }
                    if(emailCheck) {
                        val g: GradientDrawable = rael.customEditTextBoxMain.background as GradientDrawable
                        val r:  GradientDrawable = rael.customEditTextBoxHint.background as GradientDrawable
                        g.setStroke(
                            pxToDP(
                                2,
                                scale
                            ), greenColor)
                        g.setColor(lightGreenColor)
                        r.setColor(greenColor)
                    } else {
                        val g: GradientDrawable = rael.customEditTextBoxMain.background as GradientDrawable
                        g.setColor(Color.WHITE)
                    }
                } else {
                    resetReal()
                }
            }
        })

        // ON PASSWORD INPUT  ON PASSWORD INPUT  ON PASSWORD INPUT  ON PASSWORD INPUT  ON PASSWORD INPUT

        rapl.customEditTextBoxMain.setOnFocusChangeListener { view, b ->
            if((view.customEditTextBoxMain.hint as String).isNotEmpty()) {
                hint = view.customEditTextBoxMain.hint as String }
            onEditTextAnimation(
                view,
                b,
                hint,
                scale,
                passwordCheck
            )
        }

        // Check if the inputted password is valid
        rapl.customEditTextBoxMain.addTextChangedListener(object:TextWatcher{override fun afterTextChanged(s: Editable?) {
        }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s!!.length in 1..6) {
                    rapl.customEditTextBoxHint.setText(R.string.password_too_short)
                    rapl.imageView.visibility = View.VISIBLE
                    val g: GradientDrawable = rapl.customEditTextBoxMain.background as GradientDrawable
                    val r:  GradientDrawable = rapl.customEditTextBoxHint.background as GradientDrawable
                    g.setStroke(
                        pxToDP(
                            2,
                            scale
                        ), redColor)
                    r.setColor(redColor)
                    passwordCheck = false
                } else {
                    rapl.customEditTextBoxHint.text = getString(R.string.rapl_hint)
                    rapl.imageView.visibility = View.GONE
                    if (s.isNotEmpty()) {
                        passwordCheck = true
                    }
                }
                if (passwordCheck) {
                    val g: GradientDrawable = rapl.customEditTextBoxMain.background as GradientDrawable
                    val r:  GradientDrawable = rapl.customEditTextBoxHint.background as GradientDrawable
                    g.setStroke(
                        pxToDP(
                            2,
                            scale
                        ), greenColor)
                    g.setColor(lightGreenColor)
                    r.setColor(greenColor)
                } else {
                    val g: GradientDrawable = rapl.customEditTextBoxMain.background as GradientDrawable
                    g.setColor(Color.WHITE)
                }
                password = s.toString()
            }
        })

        //  ON REPEAT PASSWORD INPUT   ON REPEAT PASSWORD INPUT   ON REPEAT PASSWORD INPUT

        rarpl.customEditTextBoxMain.setOnFocusChangeListener { view, b ->
            if((view.customEditTextBoxMain.hint as String).isNotEmpty()) {
                hint = view.customEditTextBoxMain.hint as String }
            onEditTextAnimation(
                view,
                b,
                hint,
                scale,
                passwordRepeatCheck
            )
        }

        // Check if the repeated password matches the initial one
        rarpl.customEditTextBoxMain.addTextChangedListener(object:TextWatcher{override fun afterTextChanged(s: Editable?) {
        }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if ((s!!.toString() == password).not()) {
                    rarpl.customEditTextBoxHint.text = getString(R.string.password_not_matching)
                    rarpl.imageView.visibility = View.VISIBLE
                    val g: GradientDrawable = rarpl.customEditTextBoxMain.background as GradientDrawable
                    val r:  GradientDrawable = rarpl.customEditTextBoxHint.background as GradientDrawable
                    g.setStroke(
                        pxToDP(
                            2,
                            scale
                        ), redColor)
                    r.setColor(redColor)
                    passwordRepeatCheck = false
                } else {
                    rarpl.customEditTextBoxHint.text = getString(R.string.rarpl_hint)
                    rarpl.imageView.visibility = View.GONE
                    if (s.isNotEmpty()) {
                        passwordRepeatCheck = true
                    }
                }
                if (passwordRepeatCheck) {
                    val g: GradientDrawable = rarpl.customEditTextBoxMain.background as GradientDrawable
                    val r:  GradientDrawable = rarpl.customEditTextBoxHint.background as GradientDrawable
                    g.setStroke(
                        pxToDP(
                            2,
                            scale
                        ), greenColor)
                    g.setColor(lightGreenColor)
                    r.setColor(greenColor)
                } else {
                    val g: GradientDrawable = rarpl.customEditTextBoxMain.background as GradientDrawable
                    g.setColor(Color.WHITE)
                }
            }
        })

        registration_register_button.setOnClickListener {
            if (!emailCheck || !passwordCheck || !passwordRepeatCheck) {
                Toast.makeText(baseContext, "Please fill out the form as God commands!",  Toast.LENGTH_LONG).show()
            } else {
                registerButtonAction()
                registration_register_button.isEnabled = false
            }
        }

        picture_button.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

    }

    private var selectedPhotoUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            // provjera odabrane slike

            selectedPhotoUri = data.data

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)
            registration_activity_picture_view.setImageBitmap(bitmap)
        }
    }

    private fun goToLogin() {
        Log.d(tag, "Try to show login activity")
        // launch the login activity
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    // https://stackoverflow.com/questions/56214609/how-to-check-if-a-string-is-a-valid-email-in-android
    fun String.isEmailValid(): Boolean {
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }

    private fun registerButtonAction() {

        // Firebase Authentication to create a user with email and password
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(tag, "createUserWithEmail:success. UserID = ${task.result?.user?.uid}")
                    // val user = auth.currentUser

                    uploadImageToFirebaseStorage()
                    // Update UI
                    //updateUI(user)
                } else {

                    // If sign in fails, display a message to the user.
                    Log.w(tag, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed. ${task.exception?.message}",
                        Toast.LENGTH_LONG
                    ).show()
                    registration_register_button.isEnabled = true
                    //updateUI(null)
                }
            }
    }

    private fun uploadImageToFirebaseStorage() {

        // Ukoliko nije odabrana slika onda se ovo preskače da ne bi došlo do rušenja
        if (selectedPhotoUri != null) {

            // Pohranjivanje slike sa jedinstvenim imenom datoteke na server
            val filename = UUID.randomUUID().toString()
            val storageReference = FirebaseStorage.getInstance().getReference("/images/$filename")
            storageReference.putFile(selectedPhotoUri!!)
                .addOnSuccessListener {
                    Log.d(tag, "Successfully uploaded image: ${it.metadata?.path}")

                    // Dohvati url slike koja je uploadana, doslovno link do slike
                    storageReference.downloadUrl.addOnSuccessListener {
                        it.toString()
                        Log.d(tag, "File location: $it")

                        saveUserToFirebaseDatebase(it.toString())
                    }
                }
                .addOnFailureListener {
                    Log.d(tag, "Image upload unsuccessful")
                }
        } else saveUserToFirebaseDatebase("No picture")
    }

    private fun saveUserToFirebaseDatebase(profileImageUrl: String) {
        val uid = FirebaseAuth.getInstance().uid as String
        val ref = FirebaseDatabase.getInstance().getReference("/user/$uid")

        val user = User(uid, username, profileImageUrl)

        ref.setValue(user)
            .addOnSuccessListener {
                Log.d(tag, "Finally we saved the user to Firebase Databse")

                val intent = Intent(this, MessagesActivity::class.java)

                // Without using this line of code, pressing back will go back into RegistrationActivity,
                // while with the line below, it will go to the desktop of your device
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            .addOnFailureListener {
                Log.d(tag, "User-to-database push unsuccessful: ${it.message}")
                Toast.makeText(baseContext, "Registration failed: ${it.message}", Toast.LENGTH_LONG).show()
                registration_register_button.isEnabled = true
            }
    }

    //____________________________________________________________________________________________
    // HAVING FUN HAVING FUN HAVING FUN HAVING FUN HAVING FUN HAVING FUN HAVING FUN HAVING FUN
    //____________________________________________________________________________________________
    // Companion objects are a way to mimic Java static methods in Kotlin
    companion object {

        // https://stackoverflow.com/a/17789187/10299831
        @JvmStatic
        fun hideKeyboard(activity: Activity) {
            val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            //Find the currently focused view, so we can grab the correct window token from it.
            var view = activity.currentFocus
            //If no view currently has focus, create a new one, just so we can grab a window token from it
            if (view == null) {
                view = View(activity)
            }
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

        @JvmStatic
        private fun pxToDP(px: Int, scale: Float): Int {
            return (px * scale + 0.5f).toInt()
        }

        @JvmStatic
        fun onEditTextAnimation(view: View, b: Boolean, h: String, scale: Float, verification: Boolean) {
            val r = view.parent as FrameLayout
            val l = r.parent as LinearLayout
            val g: GradientDrawable = r.customEditTextBoxMain.background as GradientDrawable
            if (b) {
                r.customEditTextBoxMain.hint = ""
                if (verification)
                {
                    g.setStroke(
                        pxToDP(
                            2,
                            scale
                        ), Color.parseColor("#228B22"))
                } else {
                    g.setStroke(
                        pxToDP(
                            2,
                            scale
                        ), Color.parseColor("#ffcc0000"))
                }
                l.custom_edit_linear_layout_hint_container.visibility = View.VISIBLE
            }
            if (!b) {
                g.setStroke(
                    pxToDP(
                        2,
                        scale
                    ), Color.parseColor("#B1BCBE"))
                r.customEditTextBoxMain.hint = h
                l.custom_edit_linear_layout_hint_container.visibility = View.GONE
            }
        }

        @JvmStatic
        fun getLayoutId(l: LinearLayout): String {
            return l.context.resources.getResourceEntryName(l.id)
        }
    }



    // Initialise UI elements
    private fun setupView() {
        raul = registration_activity_username_layout as LinearLayout
        rael = registration_activity_email_layout as LinearLayout
        rapl = registration_activity_password_layout as LinearLayout
        rarpl = registration_activity_repeat_password_layout as LinearLayout
        raul.customEditTextBoxHint.text = getString(R.string.raul_hint)
        raul.customEditTextBoxMain.hint = "Username"
        rael.customEditTextBoxMain.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        rael.customEditTextBoxHint.text =  getString(R.string.rael_hint)
        rael.customEditTextBoxMain.hint = "Email"
        rapl.customEditTextBoxHint.text =  getString(R.string.rapl_hint)
        rapl.customEditTextBoxMain.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        rapl.customEditTextBoxMain.hint = "Password"
        rarpl.customEditTextBoxHint.text =  getString(R.string.rarpl_hint)
        rarpl.customEditTextBoxMain.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        rarpl.customEditTextBoxMain.hint = "Repeat password"

    }

    fun resetReal() {
        rael.customEditTextBoxHint.text = getString(R.string.rael_hint)
        rael.imageView.visibility = View.GONE
    }

}