package com.interits.voipsdktest

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.tc.voipsdk.GSIMSDK
import com.tc.voipsdk.Theme
import com.interits.voipsdktest.databinding.FragmentFirstBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    class MyAccountListener : GSIMSDK.AccountListener {
        override fun onLoginSuccess(gsim: String, contact: String) {
            Log.d("GSIM", "Login success: gsim=$gsim contact=$contact")
        }

    }

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        binding.buttonFirst.setOnClickListener {
//            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
//        }
        binding.background.setOnClickListener{
            context?.let { it1 ->
//                Theme.getInstance(context).primaryColor = resources.getColor(com.gmobile.gsim.R.color.g99_red)
                GSIMSDK.init(it1,"1081",User.load(context).uid, "0996000092","vinh.nd@gmobile.vn", MyAccountListener()) }
                GSIMSDK.call("",false)
        }
        Log.d("SDKTEST","USERINFO: "+User.load(context))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}