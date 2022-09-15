package mx.itesm.testbasicapi.controller.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import mx.itesm.testbasicapi.R
import mx.itesm.testbasicapi.Utils
import mx.itesm.testbasicapi.model.Model
import mx.itesm.testbasicapi.model.entities.Report
import mx.itesm.testbasicapi.model.repository.RemoteRepository
import mx.itesm.testbasicapi.model.repository.responseinterface.IGetReport

class UpdateReportFragment : Fragment() {
    companion object {
        private const val CAMERA_PERMISSION_CODE = 100;
    }

    private lateinit var imgReportPhoto: ImageView

    private lateinit var txtReportTitle: TextView
    private lateinit var txtReportDate: TextView
    private lateinit var txtReportTime: TextView
    private lateinit var txtReportCategory: TextView
    private lateinit var txtReportLocation: TextView
    private lateinit var txtReportDescription: TextView

    private lateinit var btnTrash: ImageButton
    private lateinit var btnArchive: ImageButton
    private lateinit var btnStatus: ImageButton
    private lateinit var btnCall: ImageButton

    private lateinit var imageByteArray: ByteArray

    private lateinit var reportRef: Report
    private lateinit var model: Model

    private var reportId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            reportId = it.getString(Utils.REPORT_ID_KEY)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update_report, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model = Model(Utils.getToken(requireContext()))
        imageByteArray = ByteArray(0)

        imgReportPhoto = view.findViewById(R.id.report_img_info)
        txtReportTitle = view.findViewById(R.id.report_title_info)
        txtReportDate = view.findViewById(R.id.report_date_info)
        txtReportTime = view.findViewById(R.id.report_time_info)
        txtReportCategory = view.findViewById(R.id.report_category_info)
        txtReportLocation = view.findViewById(R.id.report_location_info)
        txtReportDescription = view.findViewById(R.id.report_description_info)

        prepareGUI()
    }

    private fun prepareGUI() {
        if(reportId != null){
            model.getReport(reportId!!, object : IGetReport {
                override fun onSuccess(report: Report?) {
                    if(report != null){
                        reportRef = report
                        txtReportTitle.text = reportRef.title
                        txtReportDate.text = reportRef.date.toString()
                        txtReportTime.text = reportRef.time.toString()
                        txtReportCategory.text = reportRef.category
                        txtReportLocation.text = reportRef.location
                        txtReportDescription.text = reportRef.description
                        getPhoto()
                    }
                }

                override fun onNoSuccess(code: Int, message: String) {
                    Toast.makeText(
                        requireContext(),
                        "Problem detected $code $message",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e("getReport", "$code: $message")
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

    private fun getPhoto() {
        val photoPath = reportRef.photoPath
        if (photoPath != null && photoPath.isNotBlank()) {
            val picasso = RemoteRepository.getPicassoInstance(
                requireContext(),
                Utils.getToken(requireContext())
            )
            val urlForImage = "${Utils.BASE_URL}reports/images/$photoPath"
            picasso.load(urlForImage).into(imgReportPhoto);
            // val urlForImage = "${Utils.BASE_URL}products/images/${productRef.photoPath}"
            // Picasso.get().load(urlForImage).into(imgProductPhoto)
        }
    }


}