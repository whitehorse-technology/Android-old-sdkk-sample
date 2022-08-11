package com.dengage.android.kotlin.nawaz.ui.fragment

import com.dengage.android.kotlin.nawaz.R
import com.dengage.android.kotlin.nawaz.databinding.FragmentDeviceInfoBinding
import com.dengage.android.kotlin.nawaz.extensions.getDengageManager
import com.dengage.android.kotlin.nawaz.ui.base.BaseDataBindingFragment

/**
 * Created by Batuhan Coskun on 02 December 2020
 */
class DeviceInfoFragment : BaseDataBindingFragment<FragmentDeviceInfoBinding>() {

    override fun getLayoutRes(): Int {
        return R.layout.fragment_device_info
    }

    override fun init() {
        sendPageView("device-info")

        binding.tvIntegrationKey.text = getString(
            R.string.dengage_integration_key,
            activity?.getDengageManager()?.subscription?.getIntegrationKey()
        )
        binding.tvDeviceId.text = getString(
            R.string.dengage_device_id,
            activity?.getDengageManager()?.subscription?.deviceId
        )
        binding.tvAdvertisingId.text = getString(
            R.string.dengage_advertising_id,
            activity?.getDengageManager()?.subscription?.advertisingId
        )
        binding.tvToken.text = getString(
            R.string.dengage_token,
            activity?.getDengageManager()?.token
        )
        binding.tvContactKey.text = getString(
            R.string.dengage_contact_key,
            activity?.getDengageManager()?.subscription?.contactKey
        )
        binding.tvTimeZone.text = getString(
            R.string.dengage_timezone,
            activity?.getDengageManager()?.subscription?.timezone
        )
        binding.tvLanguage.text = getString(
            R.string.dengage_language,
            activity?.getDengageManager()?.subscription?.language
        )
        binding.tvUserPermission.text = getString(
            R.string.dengage_user_permission,
            activity?.getDengageManager()?.userPermission.toString()
        )
    }

}