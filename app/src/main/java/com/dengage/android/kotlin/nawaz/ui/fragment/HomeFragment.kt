package com.dengage.android.kotlin.nawaz.ui.fragment

import android.content.Intent
import androidx.navigation.fragment.findNavController
import com.dengage.android.kotlin.nawaz.R
import com.dengage.android.kotlin.nawaz.databinding.FragmentHomeBinding
import com.dengage.android.kotlin.nawaz.ui.activity.InAppTestActivity1
import com.dengage.android.kotlin.nawaz.ui.base.BaseDataBindingFragment
import com.dengage.android.kotlin.nawaz.views.activity.MainActivityEcm
import com.dengage.sdk.DengageManager

class HomeFragment : BaseDataBindingFragment<FragmentHomeBinding>() {

    override fun getLayoutRes(): Int {
        return R.layout.fragment_home
    }

    override fun init() {
        sendPageView("home")

        binding.btnDeviceInfo.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeToDeviceInfo())
        }

        binding.btnContactKey.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeToContactKey())
        }

        binding.btnCountry.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeToCountry())
        }

        binding.btnInboxMessages.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeToInboxMessages())
        }

        binding.btnCustomEvents.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeToCustomEvent())
        }

        binding.btnInAppMessage.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeToInAppMessage())
        }

        binding.btnTags.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeToTags())
        }
        binding.btnEcomm.setOnClickListener {
            startActivity(Intent(activity, MainActivityEcm::class.java))
        }

        binding.btnInAppNav.setOnClickListener {
            startActivity(Intent(activity, InAppTestActivity1::class.java))
        }
        /* binding.btnRating.setOnClickListener {
             DengageManager.showInAppRating(this.activity)
         }*/
    }

}