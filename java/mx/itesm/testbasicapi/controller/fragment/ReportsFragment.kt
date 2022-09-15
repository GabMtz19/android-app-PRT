package mx.itesm.testbasicapi.controller.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import mx.itesm.testbasicapi.R
import mx.itesm.testbasicapi.Utils
import mx.itesm.testbasicapi.controller.adapter.ReportsAdapter
import mx.itesm.testbasicapi.model.Model
import mx.itesm.testbasicapi.model.entities.Report
import mx.itesm.testbasicapi.model.repository.responseinterface.IGetReports

class ReportsFragment : Fragment() {
    private lateinit var model: Model
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reports, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model = Model(Utils.getToken(requireContext()))
        showReports()
        assignAddButtonListener()
    }
    private fun assignAddButtonListener() {
        requireView().findViewById<FloatingActionButton>(R.id.floatBtnAddProduct)
            .setOnClickListener {
//                val bottomSheetDialog = BottomSheetDialog(this.requireContext())
////                bottomSheetDialog.setContentView(R.layout.fragment_create_report)
//                bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog_layout)
//                bottomSheetDialog.show()

                val createReportFragment = CreateReportFragment()
                val fragmentTransaction = parentFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.home_content, createReportFragment)
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            }

    }

    private fun showReports() {
        model.getReports(object : IGetReports {
            override fun onSuccess(reports: List<Report>?) {
                if (reports != null) {
                    val rvReports = requireView().findViewById<RecyclerView>(R.id.rvReports)
                    val adapter =
                        ReportsAdapter(reports, object : ReportsAdapter.OnItemClickListener {
                            override fun onItemClick(item: Report) {
                                 Toast.makeText(requireContext(), "Clicked on an Item", Toast.LENGTH_SHORT).show()

                                val updateReportFragment = UpdateReportFragment()
                                val bundle = Bundle()
                                bundle.putString(Utils.REPORT_ID_KEY, item.id)
                                updateReportFragment.arguments = bundle

                                val fragmentTransaction = parentFragmentManager.beginTransaction()
                                fragmentTransaction.replace(
                                    R.id.home_content,
                                    updateReportFragment
                                )
                                fragmentTransaction.addToBackStack(null)
                                fragmentTransaction.commit()
                            }
                        }, Utils.getToken(requireContext()), requireContext())
                    rvReports.adapter = adapter
                    rvReports.layoutManager = LinearLayoutManager(requireContext())
                }
            }

            override fun onNoSuccess(code: Int, message: String) {
                Toast.makeText(
                    requireContext(),
                    "Problem detected $code $message",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("getProducts", "$code: $message")
            }

            override fun onFailure(t: Throwable) {
                Toast.makeText(
                    context,
                    "Network or server error occurred",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("addUser", t.message.toString())
            }
        })
    }


}