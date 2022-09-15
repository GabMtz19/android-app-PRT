package mx.itesm.testbasicapi.controller.activity

import android.R.attr
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import android.os.CountDownTimer;
import com.google.android.material.textfield.TextInputLayout
import mx.itesm.testbasicapi.R
import mx.itesm.testbasicapi.Utils
import mx.itesm.testbasicapi.controller.adapter.FragmentAdapter
import mx.itesm.testbasicapi.model.Model
import mx.itesm.testbasicapi.model.entities.JwtToken
import mx.itesm.testbasicapi.model.entities.User
import mx.itesm.testbasicapi.model.repository.RemoteRepository
import mx.itesm.testbasicapi.model.repository.responseinterface.ICreateUser
import mx.itesm.testbasicapi.model.repository.responseinterface.ILogin
import java.util.regex.Pattern
import android.R.attr.button
import android.annotation.SuppressLint
import android.content.Context
import android.widget.CheckBox


class Forms : AppCompatActivity() {
    lateinit var viewPager: ViewPager2
    lateinit var fragmentAdapter: FragmentAdapter
    var cont: Int = 0
    val EMAIL_ADDRESS_PATTERN: Pattern = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_forms)
        viewPager = findViewById(R.id.view_pager)
        val supportFragmentManager = supportFragmentManager
        val lifecycle = lifecycle
        fragmentAdapter = FragmentAdapter(supportFragmentManager, lifecycle)
        viewPager.setAdapter(fragmentAdapter)

        if (Utils.isUserLoggedIn(this)) advanceToMainActivity()

        val loginEmailInput = findViewById<TextInputLayout>(R.id.login_email_input)
        val loginPasswordInput = findViewById<TextInputLayout>(R.id.login_password_input)

        val sharedPreference =  getSharedPreferences("LOGIN_INFO", Context.MODE_PRIVATE)
        var shareCheck = sharedPreference.getBoolean("check", false)
        var shareEmail = sharedPreference.getString("email", null)
        var sharePass = sharedPreference.getString("pass", null)
        if(shareCheck){
            loginEmailInput.editText?.setText(shareEmail)
            loginPasswordInput.editText?.setText(sharePass)
        }
        else sharedPreference.edit().clear().commit()

    }



    fun redirectLogin(view: View?) {
        viewPager!!.currentItem = 0
    }

    fun redirectRegister(view: View?) {
        viewPager!!.currentItem = 1
    }

    fun redirectRecover(view: View?) {
        viewPager!!.currentItem = 2
    }
    fun redirectTermsConditions(view: View?) {
        Toast.makeText(
            this@Forms,
            "Redirected to Terms and Conditions",
            Toast.LENGTH_LONG
        ).show()
    }
    private fun checkEmail(email: String): Boolean {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches()
    }


    @SuppressLint("CommitPrefEdits")
    fun login(view: View?) {
        // Check valid inputs(email, password)
        val loginEmailInput = findViewById<TextInputLayout>(R.id.login_email_input)
        val loginPasswordInput = findViewById<TextInputLayout>(R.id.login_password_input)
        val checkBoxInput = findViewById<CheckBox>(R.id.check_box)

        val email = "${loginEmailInput.editText?.text}"
        val password = "${loginPasswordInput.editText?.text}"

        loginEmailInput.error = null
        loginPasswordInput.error = null

        if(email.isEmpty()){
            loginEmailInput.error = "Email Empty"
            return
        }
        if(!checkEmail(email)){
            loginEmailInput.error = "Invalid Email"
            return
        }
        if(password.isEmpty()){
            loginPasswordInput.error = "Password Empty"
            return
        }
        if(cont == 5){
            Toast.makeText(
            this@Forms,
            "cinco intentos fallidos, intenta mas tarde",
            Toast.LENGTH_LONG
            ).show()

//            val timer = object: CountDownTimer(20000, 1000) {
//                override fun onTick(millisUntilFinished: Long) {
//                }
//                override fun onFinish() {
//                    Toast.makeText(
//                        this@Forms,
//                        "Puedes volver a intentar",
//                        Toast.LENGTH_LONG
//                    ).show()
//                }
//            }
//            timer.start()

            cont = 0
            return
        }

        if(checkBoxInput.isChecked){
            val sharedPreference =  getSharedPreferences("LOGIN_INFO", Context.MODE_PRIVATE)
            sharedPreference.edit().putBoolean("check", true)
            sharedPreference.edit().putString("email", email)
            sharedPreference.edit().putString("password", password)
            sharedPreference.edit().commit()

        }



//        Toast.makeText(
//            this@Forms,
//            "email: $email \n pass: $password \n ",
//            Toast.LENGTH_LONG
//        ).show()


        val user = User("anyname", email, password)
            Model(Utils.getToken(this)).login(user, object : ILogin {

                override fun onSuccess(token: JwtToken?) {
                    Toast.makeText(this@Forms, "Welcome", Toast.LENGTH_LONG).show()

                    if (token != null) {
                        Utils.saveToken(token, this@Forms.applicationContext)
                        // This updates the HttpClient that at this moment might not have a valid token!
                        RemoteRepository.updateRemoteReferences(token.token, this@Forms);
                        cont = 0;
                        advanceToMainActivity()
                    } else {
                        // do not advance, an error occurred
                        Toast.makeText(
                            this@Forms,
                            "Something weird happened, login was ok but token was not given...",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onNoSuccess(code: Int, message: String) {
                    Toast.makeText(
                        this@Forms,
                        message,
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e("addProduct", "$code: $message")
                    cont++
                }

                override fun onFailure(t: Throwable) {
                    Toast.makeText(
                        this@Forms,
                        "Network or server error occurred",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e("addProduct", t.message.toString())
                }
            })

    }


    fun register(view: View?) {
        val registerNameInput = findViewById<TextInputLayout>(R.id.register_name_input)
        val name = "${registerNameInput.editText?.text}"

        val registerEmailInput = findViewById<TextInputLayout>(R.id.register_email_input)
        val email = "${registerEmailInput.editText?.text}"

        val registerPasswordInput = findViewById<TextInputLayout>(R.id.register_password_input)
        val password = "${registerPasswordInput.editText?.text}"

        val registerConfirmInput = findViewById<TextInputLayout>(R.id.register_password_confirm_input)
        val confirmPassword = "${registerConfirmInput.editText?.text}"

        registerNameInput.error = null
        registerEmailInput.error = null
        registerPasswordInput.error = null
        registerConfirmInput.error = null

        if(name.isEmpty()){
            registerNameInput.error = "Name Empty"
            return
        }
        if(email.isEmpty()) {
            registerEmailInput.error = "Email Empty"
            return
        }
        if(!checkEmail(email)){
            registerEmailInput.error = "Invalid Email"
            return
        }
        if(password.isEmpty()){
            registerPasswordInput.error = "Password Empty"
            return
        }
        if(confirmPassword.isEmpty()){
            registerConfirmInput.error = "Confirm Password Empty"
            return
        }
        if(password.length < 7){
            registerPasswordInput.error = "Password must be more than 7 characters"
            return
        }
        if(password != confirmPassword){
            registerConfirmInput.error = "Password does not match"
            return
        }

        val user = User(name, email, password)
        Model(Utils.getToken(this)).createUser(user, object : ICreateUser {
            override fun onSuccess(token: JwtToken?) {
                Toast.makeText(this@Forms, "Welcome", Toast.LENGTH_LONG).show()

                if (token != null) {
                    Utils.saveToken(token, this@Forms.applicationContext)
                    // This updates the HttpClient that at this moment might not have a valid token!
                    RemoteRepository.updateRemoteReferences(token.token, this@Forms);
                    advanceToMainActivity()
                } else {
                    // do not advance, an error occurred
                    Toast.makeText(
                        this@Forms,
                        "Something weird happened, login was ok but token was not given...",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onNoSuccess(code: Int, message: String) {
                Toast.makeText(
                    this@Forms,
                    "Problem detected $code $message",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("addProduct", "$code: $message")
            }

            override fun onFailure(t: Throwable) {
                Toast.makeText(
                    this@Forms,
                    "Network or server error occurred",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("addProduct", t.message.toString())
            }


        })

    }

    fun recover(view: View?) {
        val recoverEmailInput = findViewById<TextInputLayout>(R.id.recover_email_input)
        val email = "${recoverEmailInput.editText?.text}"

        if(email.isEmpty()){
            recoverEmailInput.error = "Email Empty"
            return
        }
        if(!checkEmail(email)){
            recoverEmailInput.error = "Invalid Email"
            return
        }


        // Check valid inputs (email in database already)

        // Check Valid user with data base

    }

    private fun advanceToMainActivity() {
        val mainActivityIntent =
            Intent(applicationContext, MainActivity::class.java)
        mainActivityIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(mainActivityIntent)
        finish()
    }
}