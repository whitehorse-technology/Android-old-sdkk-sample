package com.dengage.android.kotlin.nawaz.ui.fragment

import com.dengage.android.kotlin.nawaz.R
import com.dengage.android.kotlin.nawaz.databinding.FragmentCountryBinding
import com.dengage.android.kotlin.nawaz.extensions.getDengageManager
import com.dengage.android.kotlin.nawaz.ui.base.BaseDataBindingFragment

/**
 * Created by Batuhan Coskun on 21 January 2021
 */
class CountryFragment : BaseDataBindingFragment<FragmentCountryBinding>() {

    override fun getLayoutRes(): Int {
        return R.layout.fragment_country
    }

    override fun init() {
        sendPageView("country")

        binding.etCountry.setText(activity?.getDengageManager()?.subscription?.country)

        binding.btnSave.setOnClickListener {
            val country = binding.etCountry.text.toString().trim()
            if (country.isNotEmpty()) {
                activity?.getDengageManager()?.setCountry(country)
            }
        }
    }
}