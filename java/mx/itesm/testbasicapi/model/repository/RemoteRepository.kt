package mx.itesm.testbasicapi.model.repository

import android.content.Context
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import mx.itesm.testbasicapi.Utils.Companion.BASE_URL
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RemoteRepository {
    companion object {
        private lateinit var client: OkHttpClient
        private lateinit var retrofitInstance: Retrofit
        private lateinit var picasso: Picasso

        fun updateRemoteReferences(token: String, context: Context){
            client = OkHttpClient.Builder()
                .addInterceptor(AuthInterceptor(token))
                .build()

            retrofitInstance = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            picasso =
                Picasso.Builder(context).downloader(OkHttp3Downloader(getClient(token))).build()
        }

        private fun getClient(token: String): OkHttpClient {
            if (!::client.isInitialized) {
                client = OkHttpClient.Builder()
                    .addInterceptor(AuthInterceptor(token))
                    .build()
            }
            return client
        }

        fun getRetrofitInstance(token: String): Retrofit {
            getClient(token)
            if (!::retrofitInstance.isInitialized) {
                retrofitInstance = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofitInstance
        }

        fun getPicassoInstance(context: Context, token: String): Picasso {
            // TODO Investigate, take out getClient call
            getClient(token)
            if (!::picasso.isInitialized) {
                picasso =
                    Picasso.Builder(context).downloader(OkHttp3Downloader(client)).build()
            }
            return picasso
        }
    }

    class AuthInterceptor(val token: String) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val requestBuilder = chain.request().newBuilder()
            requestBuilder.addHeader("Authorization", "Bearer $token")
            return chain.proceed(requestBuilder.build())
        }
    }
}