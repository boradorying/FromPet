package com.example.frompet.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.frompet.R
import com.example.frompet.databinding.ActivityLoginBinding
import com.example.frompet.databinding.ActivityMainBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var mBinding:ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
    }
}