package mx.itesm.testbasicapi.controller.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import mx.itesm.testbasicapi.controller.fragment.LoginFragment
import mx.itesm.testbasicapi.controller.fragment.RecoverFragment
import mx.itesm.testbasicapi.controller.fragment.RegisterFragment

class FragmentAdapter(fragmentManager: FragmentManager?, lifecycle: Lifecycle?) :
    FragmentStateAdapter(fragmentManager!!, lifecycle!!) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        val var10000: Fragment
        var10000 = when (position) {
            1 -> RegisterFragment() as Fragment
            2 -> RecoverFragment() as Fragment
            else -> LoginFragment() as Fragment
        }
        return var10000
    }
}