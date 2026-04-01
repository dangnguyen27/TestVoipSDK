package com.interits.voipsdktest
import java.util.UUID

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tc.voipsdk.VOIPSDK
import com.interits.voipsdktest.databinding.FragmentFirstBinding
import com.tc.voipsdk.UserInfo
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {
    val TAG = "voipsdk"
    class MyAccountListener : VOIPSDK.AccountListener {
        override fun onLoginSuccess(gsim: String, contact: String) {
            Log.d("ID CALL", "Login success: gsim=$gsim contact=$contact")
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

        var token: String? = null
        val uuid: UUID = UUID.randomUUID()
        lifecycleScope.launch {
            val res = DataService().loginSip(
                "0973385522",
                "android",
                "Pixel 4",
                uuid.toString(),
                "127.0.0.1",
                "AndroidBrowser"
            )
            if (res != null) {
                val data = res.getJSONArray("data")
                val sipItem = data.getJSONObject(0)
                token = sipItem.getString("token")
                Log.d(TAG, token.toString())
            }
            if (token != null) {
                UserInfo.saveSipAccount(
                    "0973385522", "0973385522",
                    token,
                    token, context
                )
                VOIPSDK.getInstance(context).initCore(context)
                VOIPSDK.getInstance(context)
                    .loadAccountToCore(context)

            }
        }

        binding.background.setOnClickListener{
            context?.let { it1 ->
//                com.tc.voipsdk.Theme.getInstance(context).primaryColor = resources.getColor(com.tc.voipsdk.R.color.g99_colorPrimary)

                VOIPSDK.getInstance(context).call(context,"0975803515",true)
//                VOIPSDK.getInstance(context).init(it1,"1081",
//                    User.load(context).uid, "0973385522"," congbt@interits.com", MyAccountListener())

            }
        }
        Log.d("SDKTEST","USERINFO: "+User.load(context))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}