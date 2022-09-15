package mx.itesm.testbasicapi.controller.fragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputLayout
import mx.itesm.testbasicapi.R
import mx.itesm.testbasicapi.Utils
import mx.itesm.testbasicapi.model.Model
import mx.itesm.testbasicapi.model.entities.Report
import mx.itesm.testbasicapi.model.repository.responseinterface.IAddReport
import java.io.ByteArrayOutputStream

class CreateReportFragment : Fragment() {

    companion object {
    private const val CAMERA_PERMISSION_CODE = 100
    }
    private lateinit var reportCategoryInput: TextInputLayout
    private lateinit var reportImportanceInput: TextInputLayout
    private lateinit var autoCompleteCategory: AutoCompleteTextView
    private lateinit var autoCompleteImportance: AutoCompleteTextView


    private lateinit var reportTitleInput: TextInputLayout
    private lateinit var reportLocationInput: TextInputLayout
    private lateinit var reportDescriptionInput: TextInputLayout

//    private lateinit var btnTakePhoto: Button
    private lateinit var btnCreateReport: Button
    private lateinit var reportPhoto: ImageView

    private lateinit var model: Model
    private lateinit var imageByteArray: ByteArray


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_report, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model = Model(Utils.getToken(requireContext()))
        imageByteArray = ByteArray(0)
        reportCategoryInput = view.findViewById(R.id.report_category_input)
        reportImportanceInput = view.findViewById(R.id.report_importance_input)

        autoCompleteCategory = view.findViewById(R.id.auto_complete_category)
        autoCompleteImportance = view.findViewById(R.id.auto_complete_importance)

        var categories = arrayOf(
            "Security",
            "Cleaning",
            "Administration",
            "Other"
        )
        val arrayAdaptorCategory: ArrayAdapter<String> =
            ArrayAdapter(requireContext(), R.layout.item_dropdown, categories)
        autoCompleteCategory.setAdapter(arrayAdaptorCategory);

        var importance = arrayOf(
            "Important",
            "Urgent!!!"
        )
        val arrayAdaptorImportance: ArrayAdapter<String> =
            ArrayAdapter(requireContext(), R.layout.item_dropdown, importance)
        autoCompleteImportance.setAdapter(arrayAdaptorImportance);


        reportTitleInput = view.findViewById(R.id.report_title_input)
        reportLocationInput = view.findViewById(R.id.report_location_input)
        reportDescriptionInput = view.findViewById(R.id.report_description_input)
        btnCreateReport = view.findViewById(R.id.make_report_btn)
        reportPhoto = view.findViewById(R.id.report_add_img_input)

        reportPhoto.setOnClickListener(clickListenerForTakePhoto())
        btnCreateReport.setOnClickListener(onCreateReport())
    }

    private fun onCreateReport() : View.OnClickListener? {
        return View.OnClickListener {

            val title = "${reportTitleInput.editText?.text}"
            val location = "${reportLocationInput.editText?.text}"
            val description = "${reportDescriptionInput.editText?.text}"
            val category = "${reportCategoryInput.editText?.text}"
            val importance = "${reportImportanceInput.editText?.text}"
            var bool: Boolean = importance === "Urgent!!!"

            val report = Report(
                "",
                title,
                location,
                description,
                category,
                bool,
                false,
                null,
                null,
                null

            )
            model.addReport(report, imageByteArray, object : IAddReport {
                override fun onSuccess(report: Report?) {
                    Toast.makeText(requireContext(), "Report added ok", Toast.LENGTH_SHORT).show()
                    parentFragmentManager.popBackStackImmediate();
                }

                override fun onNoSuccess(code: Int, message: String) {
                    Toast.makeText(requireContext(), "Problem detected $code $message", Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(t: Throwable) {
                    Toast.makeText(requireContext(), "Network or server error occurred", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun clickListenerForTakePhoto(): View.OnClickListener? {
        return View.OnClickListener {
            val permissionStatus = ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            )
            if (permissionStatus == PackageManager.PERMISSION_DENIED) {
                Log.i("tag", "we dont have the permission, wil ask for it")
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.CAMERA),
                    CAMERA_PERMISSION_CODE
                )
            } else {
                Log.i("tag", "we have the permission, thanks")
                takePhoto()
            }
        }
    }

    private fun takePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        resultLauncher.launch(intent)
    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val stream = ByteArrayOutputStream()
                val data = result.data
                val bmp = data?.extras?.get("data") as Bitmap
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream)
                imageByteArray = stream.toByteArray()

                val bitmap = BitmapFactory.decodeByteArray(
                    imageByteArray, 0,
                    imageByteArray.size
                )
                reportPhoto.setPadding(0)
                reportPhoto.setImageBitmap(bitmap)

//                productPhoto.setImageBitmap(bitmap)
            } else {
                Toast.makeText(requireContext(), "Picture was not taken", Toast.LENGTH_SHORT).show()
            }
        }



}