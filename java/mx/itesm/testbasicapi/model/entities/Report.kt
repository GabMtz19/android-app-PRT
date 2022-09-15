package mx.itesm.testbasicapi.model.entities

import com.google.gson.annotations.SerializedName
import java.util.*

class Report (
    @SerializedName("_id") var id: String,
    var title: String,
    var location: String,
    var description: String?,
    var category: String,
    var importance: Boolean,
    var status: Boolean,
    var photoPath: String?,
    var date: String?,
    var time: String?
  ){
}