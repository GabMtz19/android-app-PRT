package mx.itesm.testbasicapi.model

import com.google.gson.Gson
import mx.itesm.testbasicapi.model.entities.JwtToken
import mx.itesm.testbasicapi.model.entities.Report
import mx.itesm.testbasicapi.model.entities.User
import mx.itesm.testbasicapi.model.repository.RemoteRepository
import mx.itesm.testbasicapi.model.repository.backendinterface.ReportsApi
import mx.itesm.testbasicapi.model.repository.backendinterface.UsersApi
import mx.itesm.testbasicapi.model.repository.responseinterface.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Model(private val token:String) {
//    fun getProducts(callback: IGetProducts) {
//        val retrofit = RemoteRepository.getRetrofitInstance(token)
//        val callGetUser = retrofit.create(ProductsApi::class.java).getProducts()
//        callGetUser.enqueue(object : Callback<List<Product>?> {
//            override fun onResponse(
//                call: Call<List<Product>?>,
//                response: Response<List<Product>?>
//            ) {
//                if (response.isSuccessful) callback.onSuccess(response.body())
//                else callback.onNoSuccess(response.code(), response.message())
//            }
//
//            override fun onFailure(call: Call<List<Product>?>, t: Throwable) {
//                callback.onFailure(t)
//            }
//        })
//    }
//
//    fun addProduct(product: Product, productPhotoBytes: ByteArray, callback: IAddProduct) {
//        val bodyProductPhoto =
//            RequestBody.create(MediaType.parse("application/octet-stream"), productPhotoBytes)
//        val partProductPhoto =
//            MultipartBody.Part.createFormData("photo", "product.png", bodyProductPhoto)
//
//        val productAsJson = Gson().toJson(product)
//        val productPart = MultipartBody.Part.createFormData("product", productAsJson)
//
//        val retrofit = RemoteRepository.getRetrofitInstance(token)
//        // val callAddProduct = retrofit.create(ProductsApi::class.java).insertProduct(productPart, partProductPhoto)
//        val callAddProduct: Call<Product> = if (productPhotoBytes.isEmpty())
//            retrofit.create(ProductsApi::class.java).insertProduct(productPart, null)
//        else
//            retrofit.create(ProductsApi::class.java)
//                .insertProduct(productPart, partProductPhoto)
//
//        callAddProduct.enqueue(object : Callback<Product?> {
//            override fun onResponse(call: Call<Product?>, response: Response<Product?>) {
//                if (response.isSuccessful) {
//                    callback.onSuccess(product)
//                } else {
//                    // callback.onNoSuccess(response.code(), "something went wrong")
//                    val message: String = if (response.errorBody() != null)
//                        response.errorBody()!!.string()
//                    else
//                        response.message()
//                    callback.onNoSuccess(response.code(), message)
//                }
//            }
//
//            override fun onFailure(call: Call<Product?>, t: Throwable) {
//                callback.onFailure(t)
//            }
//        })
//    }
//
//    fun getProduct(productId: String, callback: IGetProduct) {
//        val retrofit = RemoteRepository.getRetrofitInstance(token)
//        val callGetProduct = retrofit.create(ProductsApi::class.java).getProduct(productId)
//
//        callGetProduct.enqueue(object : Callback<Product?> {
//            override fun onResponse(call: Call<Product?>, response: Response<Product?>) {
//                if (response.isSuccessful)
//                    callback.onSuccess(response.body())
//                else {
//                    val message: String = if (response.errorBody() != null)
//                        response.errorBody()!!.string()
//                    else
//                        response.message()
//                    callback.onNoSuccess(response.code(), message)
//                }
//            }
//
//            override fun onFailure(call: Call<Product?>, t: Throwable) {
//                callback.onFailure(t)
//            }
//        })
//    }
//
//    fun deleteProduct(productId: String, callback: IDeleteProduct) {
//        val retrofit = RemoteRepository.getRetrofitInstance(token)
//
//        val callDeleteProduct = retrofit.create(ProductsApi::class.java).deleteProduct(productId)
//
//        callDeleteProduct.enqueue(object : Callback<Product?> {
//            override fun onResponse(call: Call<Product?>, response: Response<Product?>) {
//                if (response.isSuccessful)
//                    callback.onSuccess(response.body())
//                else {
//                    val message: String = if (response.errorBody() != null)
//                        response.errorBody()!!.string()
//                    else
//                        response.message()
//                    callback.onNoSuccess(response.code(), message)
//                }
//            }
//
//            override fun onFailure(call: Call<Product?>, t: Throwable) {
//                callback.onFailure(t)
//            }
//        })
//    }
//
//    fun updateProduct(
//        product: Product,
//        productPhotoBytes: ByteArray,
//        callback: IUpdateProduct
//    ) {
//        val productAsJson = Gson().toJson(product)
//        val productPart = MultipartBody.Part.createFormData("product", productAsJson)
//
//        val bodyProductPhoto =
//            RequestBody.create(MediaType.parse("application/octet-stream"), productPhotoBytes)
//        val partProductPhoto =
//            MultipartBody.Part.createFormData("photo", "product.png", bodyProductPhoto)
//
//        val retrofit = RemoteRepository.getRetrofitInstance(token)
//        val callUpdateProduct: Call<Product> = if (productPhotoBytes.isEmpty())
//            retrofit.create(ProductsApi::class.java).updateProduct(product.id, productPart, null)
//        else
//            retrofit.create(ProductsApi::class.java).updateProduct(product.id, productPart, partProductPhoto)
//
//        callUpdateProduct.enqueue(object : Callback<Product?> {
//            override fun onResponse(call: Call<Product?>, response: Response<Product?>) {
//                if (response.isSuccessful) callback.onSuccess(response.body())
//                else {
//                    val message: String = if (response.errorBody() != null)
//                        response.errorBody()!!.string()
//                    else
//                        response.message()
//                    callback.onNoSuccess(response.code(), message)
//                }
//            }
//
//            override fun onFailure(call: Call<Product?>, t: Throwable) {
//                callback.onFailure(t)
//            }
//        })
//    }

    fun login(user: User, callback: ILogin){
        val retrofit = RemoteRepository.getRetrofitInstance(token)

        val callLogin = retrofit.create(UsersApi::class.java).login(user)

        callLogin.enqueue(object : Callback<JwtToken?> {
            override fun onResponse(call: Call<JwtToken?>, response: Response<JwtToken?>) {
                if (response.isSuccessful) callback.onSuccess(response.body())
                else {
                    val message: String = if (response.errorBody() != null)
                        response.errorBody()!!.string()
                    else
                        response.message()
                    callback.onNoSuccess(response.code(), message)
                }
            }

            override fun onFailure(call: Call<JwtToken?>, t: Throwable) {
                callback.onFailure(t)
            }
        })
    }

    fun createUser(user: User, callback: ICreateUser){
        val retrofit = RemoteRepository.getRetrofitInstance(token)

        val callCreateUser = retrofit.create(UsersApi::class.java).createUser(user)

        callCreateUser.enqueue(object : Callback<JwtToken?> {
            override fun onResponse(call: Call<JwtToken?>, response: Response<JwtToken?>) {
                if (response.isSuccessful) callback.onSuccess(response.body())
                else {
                    val message: String = if (response.errorBody() != null)
                        response.errorBody()!!.string()
                    else
                        response.message()
                    callback.onNoSuccess(response.code(), message)
                }
            }

            override fun onFailure(call: Call<JwtToken?>, t: Throwable) {
                callback.onFailure(t)
            }
        })
    }

    fun getReports(callback: IGetReports){
        val retrofit = RemoteRepository.getRetrofitInstance(token)
        val callGetUser = retrofit.create(ReportsApi::class.java).getReports()
        callGetUser.enqueue(object : Callback<List<Report>?> {
            override fun onResponse(
                call: Call<List<Report>?>,
                response: Response<List<Report>?>
            ) {
                if (response.isSuccessful) callback.onSuccess(response.body())
                else callback.onNoSuccess(response.code(), response.message())
            }

            override fun onFailure(call: Call<List<Report>?>, t: Throwable) {
                callback.onFailure(t)
            }
        })

    }

    fun getReport(reportId: String, callback: IGetReport) {
        val retrofit = RemoteRepository.getRetrofitInstance(token)
        val callGetReport = retrofit.create(ReportsApi::class.java).getReport(reportId)

        callGetReport.enqueue(object : Callback<Report?> {
            override fun onResponse(call: Call<Report?>, response: Response<Report?>) {
                if (response.isSuccessful)
                    callback.onSuccess(response.body())
                else {
                    val message: String = if (response.errorBody() != null)
                        response.errorBody()!!.string()
                    else
                        response.message()
                    callback.onNoSuccess(response.code(), message)
                }
            }

            override fun onFailure(call: Call<Report?>, t: Throwable) {
                callback.onFailure(t)
            }
        })
    }

    fun addReport(report: Report, reportPhotoBytes: ByteArray, callback: IAddReport) {
        val bodyReportPhoto =
            RequestBody.create(MediaType.parse("application/octet-stream"), reportPhotoBytes)
        val partReportPhoto =
            MultipartBody.Part.createFormData("photo", "report.png", bodyReportPhoto)

        val reportAsJson = Gson().toJson(report)
        val reportPart = MultipartBody.Part.createFormData("report", reportAsJson)

        val retrofit = RemoteRepository.getRetrofitInstance(token)
        // val callAddProduct = retrofit.create(ProductsApi::class.java).insertProduct(productPart, partProductPhoto)
        val callAddReport: Call<Report> = if (reportPhotoBytes.isEmpty())
            retrofit.create(ReportsApi::class.java).insertReport(reportPart, null)
        else
            retrofit.create(ReportsApi::class.java)
                .insertReport(reportPart, partReportPhoto)

        callAddReport.enqueue(object : Callback<Report?> {
            override fun onResponse(call: Call<Report?>, response: Response<Report?>) {
                if (response.isSuccessful) {
                    callback.onSuccess(report)
                } else {
                    // callback.onNoSuccess(response.code(), "something went wrong")
                    val message: String = if (response.errorBody() != null)
                        response.errorBody()!!.string()
                    else
                        response.message()
                    callback.onNoSuccess(response.code(), message)
                }
            }

            override fun onFailure(call: Call<Report?>, t: Throwable) {
                callback.onFailure(t)
            }
        })
    }
}