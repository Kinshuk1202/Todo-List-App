package com.example.todolist.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.todolist.R
import com.example.todolist.databinding.ActivityMainBinding
import com.example.todolist.databinding.FragmentSignupBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase


class SignupFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var navControl :NavController
    private lateinit var binding: FragmentSignupBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignupBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        registerEvents()
    }
    private fun init(view: View)
    {
        navControl = Navigation.findNavController(view)
        auth = FirebaseAuth.getInstance()
    }
    private fun registerEvents()
    {
        binding.textViewSignIn.setOnClickListener{
            navControl.navigate(R.id.action_signupFragment_to_signinFragment)
        }
        binding.nextBtn.setOnClickListener{
            val email = binding.emailEt.text.toString().trim()
            val pass = binding.passEt.text.toString().trim()
            val verifyPass = binding.verifyPassEt.text.toString().trim()
            if(email.isNotEmpty() && pass.isNotEmpty() && verifyPass.isNotEmpty())
            {
                binding.progressBar.visibility = View.VISIBLE
                if(pass == verifyPass)
                    auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(
                        OnCompleteListener {
                            if(it.isSuccessful){
                                Toast.makeText(context,"Registerred Successfully!!" , Toast.LENGTH_SHORT).show()
                                navControl.navigate(R.id.action_signupFragment_to_homeFragment)
                            }
                            else
                            {
                                Toast.makeText(context,it.exception?.message , Toast.LENGTH_SHORT).show()
                            }
                            binding.progressBar.visibility = View.GONE
                        }
                    )
            }
            else{
                Toast.makeText(context,"Empty Field Not Awllowed!!" , Toast.LENGTH_SHORT).show()
            }
        }
    }
}