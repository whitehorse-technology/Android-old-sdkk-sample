package com.dengage.android.kotlin.nawaz.ui.fragment

import com.dengage.android.kotlin.nawaz.R
import com.dengage.android.kotlin.nawaz.databinding.FragmentContactKeyBinding
import com.dengage.android.kotlin.nawaz.extensions.getDengageManager
import com.dengage.android.kotlin.nawaz.ui.base.BaseDataBindingFragment

/**
 * Created by Batuhan Coskun on 02 December 2020
 */
class ContactKeyFragment : BaseDataBindingFragment<FragmentContactKeyBinding>() {

    override fun getLayoutRes(): Int {
        return R.layout.fragment_contact_key
    }

    override fun init() {
        sendPageView("contact-key")

        binding.etContactKey.setText(activity?.getDengageManager()?.subscription?.contactKey)

        binding.btnSave.setOnClickListener {
            val contactKey = binding.etContactKey.text.toString().trim()
            activity?.getDengageManager()?.setContactKey(contactKey)
        }
    }

}