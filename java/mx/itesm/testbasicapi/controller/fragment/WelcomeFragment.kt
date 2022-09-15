package mx.itesm.testbasicapi.controller.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import mx.itesm.testbasicapi.R


class WelcomeFragment : Fragment() {
    private val PARAM_NAME_KEY = "name"
    private var name: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            name = it.getString(PARAM_NAME_KEY)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_welcome, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val message = "Welcome ".plus(arguments?.getString(PARAM_NAME_KEY, "unknown guy"))
        view.findViewById<TextView>(R.id.txtHelloMessage).text = message
    }

    companion object {
        @JvmStatic
        fun newInstance(param_name: String) =
            WelcomeFragment().apply {
                arguments = Bundle().apply {
                    putString(PARAM_NAME_KEY, param_name)
                }
            }
    }
}