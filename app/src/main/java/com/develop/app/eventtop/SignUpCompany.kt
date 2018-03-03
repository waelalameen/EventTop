package com.develop.app.eventtop

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Typeface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.Toolbar
import android.text.InputType
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.*
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView

class SignUpCompany : AppCompatActivity(), View.OnTouchListener {

    lateinit var compPassword: AutoCompleteTextView
    private var isClicked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_company)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)

        val title: TextView = findViewById(R.id.title)
        val typeface = Typeface.createFromAsset(assets, "STV_0.ttf")
        title.typeface = typeface
        title.text = "تسجيل كشركة"

        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        val viewGroup = findViewById<ViewGroup>(R.id.container)
        TextModifier.replaceTypeface(viewGroup, this)

        val compName: AutoCompleteTextView = findViewById(R.id.autoCompleteTextView)
        val compUserName: AutoCompleteTextView = findViewById(R.id.autoCompleteTextView2)
        val compEmail: AutoCompleteTextView = findViewById(R.id.autoCompleteTextView3)
        compPassword = findViewById(R.id.autoCompleteTextView7)
        compPassword.setOnTouchListener(this)
        val compPhone: AutoCompleteTextView = findViewById(R.id.autoCompleteTextView4)
        val compAddress: AutoCompleteTextView = findViewById(R.id.autoCompleteTextView5)
        val compWebsite: AutoCompleteTextView = findViewById(R.id.autoCompleteTextView6)

        val signIn: Button = findViewById(R.id.sign_up)
        signIn.setOnClickListener({ view ->
            val compNameTxt = compName.text.toString().trim()
            val compUserNameTxt = compUserName.text.toString().trim()
            val compEmailTxt = compEmail.text.toString().trim()
            val compPasswordTxt = compPassword.text.toString().trim()
            val compPhoneTxt = compPhone.text.toString().trim()
            val compAddressTxt = compAddress.text.toString().trim()
            val compWebsiteTxt = compWebsite.text.toString().trim()

            for (i in 0 until viewGroup.childCount) {
                val v = viewGroup.getChildAt(i)
                if (v is AutoCompleteTextView) {
                    val childText = v.text.trim()

                    if (childText.isEmpty()) {
                        val message = v.hint.toString()
                        val mSnack: Snackbar = Snackbar.make(view, message + " مطلوب ", Snackbar.LENGTH_SHORT)
                        TextModifier.replaceTypeface(mSnack, this)
                        return@setOnClickListener
                    }
                }
            }

            val preferences = getSharedPreferences("signup_comp_preferences", Context.MODE_PRIVATE)
            var editor: SharedPreferences.Editor = preferences.edit()
            editor.putString("compNameTxt", compNameTxt)
            editor.putString("compUserNameTxt", compUserNameTxt)
            editor.putString("compEmailTxt", compEmailTxt)
            editor.putString("compPasswordTxt", compPasswordTxt)
            editor.putString("compPhoneTxt", compPhoneTxt)
            editor.putString("compAddressTxt", compAddressTxt)
            editor.putString("compWebsiteTxt", compWebsiteTxt)
            editor.apply()

            val preferences2 = getSharedPreferences("window_preferences", Context.MODE_PRIVATE)
            editor = preferences2.edit()
            editor.putBoolean("isFromGallery", false)
            editor.apply()

            val preferences3 = getSharedPreferences("account_type_preferences", Context.MODE_PRIVATE)
            editor = preferences3.edit()
            editor.putInt("account_type", 2)
            editor.apply()

            val intent = Intent(this, ProgressSignUp::class.java)
            startActivity(intent)
        })
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            startActivity(Intent(this, SignIn::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        startActivity(Intent(this, SignIn::class.java))
        super.onBackPressed()
    }

    override fun onTouch(view: View?, event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_UP) {
            val diff = compPassword.compoundDrawables[0].bounds.width() + 40
            if (event.rawX <= diff) {
                Log.d("event", "click " + event.rawX + " " + diff)
                view?.performClick()
                isClicked = !isClicked

                val typeface = Typeface.createFromAsset(assets, "STV_0.ttf")
                compPassword.typeface = typeface

                if (isClicked)  {
                    val drawable = applicationContext.resources.getDrawable(R.drawable.ic_visibility_black_24dp)
                    compPassword.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
                    compPassword.inputType = InputType.TYPE_CLASS_TEXT
                    compPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                } else {
                    val drawable = applicationContext.resources.getDrawable(R.drawable.ic_visibility_off_black_24dp)
                    compPassword.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
                    compPassword.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
                    compPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                }

                return true
            }
        }
        return false
    }
}
