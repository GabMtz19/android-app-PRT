package mx.itesm.testbasicapi.controller.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import mx.itesm.testbasicapi.R
import mx.itesm.testbasicapi.Utils
import mx.itesm.testbasicapi.model.entities.Report
import mx.itesm.testbasicapi.model.repository.RemoteRepository

class ReportsAdapter(
    private val reports: List<Report>,
    private val clickListener: OnItemClickListener,
    private val token: String,
    private val context: Context
) :
    RecyclerView.Adapter<ReportsAdapter.ViewHolder>() {

    // 2nd step to handle clicks
    // Define listener member variable
    interface OnItemClickListener {
        fun onItemClick(item: Report)
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    // commented line is first step, second line is for handling the clicks
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Your holder should contain and initialize a member variable
        // for any view that will be set as you render a row
        val txtReportTitle: TextView = itemView.findViewById(R.id.txtReportTitle)
        val txtReportLocation: TextView = itemView.findViewById(R.id.txtReportLocation)
        val txtReportTime: TextView = itemView.findViewById(R.id.txtReportTime)
        val imgReportPhoto: ImageView = itemView.findViewById(R.id.imgReportPhoto)
        val imgReportBackground: LinearLayout = itemView.findViewById(R.id.report_background)
    }

    // ... constructor and member variables
    // Usually involves inflating a layout from XML and returning the holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        // Inflate the custom layout
        val reportView = inflater.inflate(R.layout.item_report, parent, false)
        // Return a new holder instance
        return ViewHolder(reportView)
    }

    // Involves populating data into the item through holder
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        // https://stackoverflow.com/questions/49969278/recyclerview-item-click-listener-the-right-way
        // Get the data model based on position
        val report: Report = reports[position]
        viewHolder.txtReportTitle.text = report.title
        viewHolder.txtReportLocation.text = report.location
        viewHolder.txtReportTime.text = report.time.toString()
        if(report.importance){
            viewHolder.imgReportBackground.background.setTint(Color.rgb(255,105,97))
        } else{
            viewHolder.imgReportBackground.background.setTint(Color.rgb(251,187,98))
        }
//        val textForPrice = String.format("At $%.2f", report.price)
//        viewHolder.txtReportPrice.text = textForPrice

        if (report.photoPath != null && report.photoPath!!.isNotBlank()) {
            // val urlForImage = "${BASE_URL}products/images/${product.photoPath}"
            // Picasso.get().load(urlForImage).into(viewHolder.imgProductPhoto);
            askForImage(report.photoPath!!, viewHolder)
        }

        // Second step: handling the clicks
        viewHolder.itemView.setOnClickListener {
            clickListener.onItemClick(report)
        }
    }

    private fun askForImage(photoPath: String, viewHolder: ViewHolder) {
        val picasso = RemoteRepository.getPicassoInstance(context, token)
        val urlForImage = "${Utils.BASE_URL}reports/images/$photoPath"
        picasso.load(urlForImage).into(viewHolder.imgReportPhoto);
    }

    // Returns the total count of items in the list
    override fun getItemCount(): Int {
        return reports.size   
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        // return super.getItemViewType(position)
        return position
    }
}