package com.lyhoangvinh.app2369media.utils

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import com.google.gson.*
import com.lyhoangvinh.app2369media.BuildConfig

import com.lyhoangvinh.app2369media.data.entities.Entities
import com.lyhoangvinh.app2369media.data.entities.ErrorEntity
import com.lyhoangvinh.app2369media.ui.base.interfaces.PlainConsumer
import com.lyhoangvinh.app2369media.ui.base.interfaces.PlainEntitiesPagingConsumer
import com.lyhoangvinh.app2369media.ui.base.interfaces.PlainPagingConsumer
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function4
import io.reactivex.schedulers.Schedulers
import lyhoangvinh.com.myutil.network.Tls12SocketFactory
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.IOException
import java.lang.reflect.Type
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager

fun <T> makeRequest(
    request: Single<T>,
    shouldUpdateUi: Boolean,
    @NonNull responseConsumer: PlainConsumer<T>,
    @Nullable errorConsumer: PlainConsumer<ErrorEntity>?
): Disposable {

    var single = request.subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io())
    if (shouldUpdateUi) {
        single = single.observeOn(AndroidSchedulers.mainThread())
    }

    return single.subscribe(responseConsumer, Consumer {
        // handle error
        it.printStackTrace()
        errorConsumer?.accept(ErrorEntity(getPrettifiedErrorMessage(it), getErrorCode(it)))
    })
}

/**
 * Get http error code from [Throwable] if it is instance of [HttpException]
 * @param throwable input throwable
 * @return http code or -1 if throwable isn't a instance of [HttpException]
 */
fun getErrorCode(throwable: Throwable): Int {
    return (throwable as? HttpException)?.code() ?: -1
}

/**
 * Get a error message from retrofit response throwable
 * @param throwable retrofit rx throwable
 * @return error message
 */
fun getPrettifiedErrorMessage(throwable: Throwable?): String {
    if (throwable is HttpException) {
        return ErrorEntity.NETWORK_UNAVAILABLE
    } else if (throwable is IOException) {
        return ErrorEntity.NETWORK_UNAVAILABLE
    }
    return ErrorEntity.OOPS
}

fun <T> makeService(serviceClass: Class<T>, gson: Gson, okHttpClient: OkHttpClient, url: String): T {
    val retrofit = Retrofit.Builder()
        .baseUrl(url)
        .client(okHttpClient)
        .addConverterFactory(ServiceResponseConverter(gson))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .build()
    return retrofit.create(serviceClass)
}

/**
* call that supports String & Gson and always uses json as its request body
*/

class ServiceResponseConverter(
    private val gSon: Gson
) : Converter.Factory() {

    override fun responseBodyConverter(type: Type?, annotations: Array<Annotation>, retrofit: Retrofit): Converter<ResponseBody, *>? {
        return try {
            if (type === String::class.java) {
                StringResponseConverter()
            } else GsonConverterFactory.create(gSon).responseBodyConverter(type!!, annotations, retrofit)
        } catch (ignored: OutOfMemoryError) {
            null
        }
    }

    override fun requestBodyConverter(type: Type?, parameterAnnotations: Array<Annotation>,
                                      methodAnnotations: Array<Annotation>, retrofit: Retrofit): Converter<*, RequestBody>? {
        return GsonConverterFactory.create(gSon).requestBodyConverter(type!!, parameterAnnotations, methodAnnotations, retrofit)
    }

    private class StringResponseConverter : Converter<ResponseBody, String> {
        @Throws(IOException::class)
        override fun convert(value: ResponseBody): String {
            return value.string()
        }
    }
}




fun makeOkHttpClientBuilder(context: Context): OkHttpClient.Builder {
    val logging = HttpLoggingInterceptor()

    if (BuildConfig.DEBUG) {
        logging.level = HttpLoggingInterceptor.Level.BODY
    }

    // : 4/26/2017 add the UnauthorisedInterceptor to this retrofit, or 401
    val builder = OkHttpClient.Builder()
//        .addInterceptor(UnauthorisedInterceptor(context))
        .addInterceptor(ResponseInterceptor())
        .addInterceptor(logging)
        .followRedirects(true)
        .followSslRedirects(true)
        .retryOnConnectionFailure(true)
        .cache(null)
        .connectTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)

    // add cache to client
    val baseDir = context.cacheDir
    if (baseDir != null) {
        val cacheDir = File(baseDir, "HttpResponseCache")
        builder!!.cache(Cache(cacheDir, (10 * 1024 * 1024).toLong())) // 10 MB
    }

    return enableTls12OnPreLollipop(builder)
}

class ResponseInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        return response.newBuilder()
            .addHeader("Content-Type", "application/json; charset=utf-8")
            .build()
    }
}

/**
 * Enable TLS 1.2 on Pre Lollipop android versions
 * @param client OkHttpClient builder
 * @return builder with SSL Socket Factory set
 * according to [OkHttpClient.Builder.sslSocketFactory] deprecation,
 * Please add config SSL with [X509TrustManager] by using [CustomTrustManager]
 * * how to enable tls on android 4.4
 * [](https://github.com/square/okhttp/issues/2372)
 */
@Suppress("DEPRECATION")
@SuppressLint("ObsoleteSdkInt")
fun enableTls12OnPreLollipop(client: OkHttpClient.Builder): OkHttpClient.Builder {
    if (Build.VERSION.SDK_INT in 16..21) {
        try {
            val sc = SSLContext.getInstance("TLSv1.2")
            sc.init(null, null, null)
            // TODO: 9/7/17 set SSL socket factory with X509TrustManager
            client.sslSocketFactory(Tls12SocketFactory(sc.socketFactory))
            val cs = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .tlsVersions(TlsVersion.TLS_1_2)
                .build()

            val specs = ArrayList<ConnectionSpec>()
            specs.add(cs)
            specs.add(ConnectionSpec.COMPATIBLE_TLS)
            specs.add(ConnectionSpec.CLEARTEXT)

            client.connectionSpecs(specs)
        } catch (exc: Exception) {
            Log.e("OkHttpTLSCompat", "Error while setting TLS 1.2", exc)
        }

    }

    return client
}

class DateDeserializer : JsonDeserializer<Date> {
    @SuppressLint("SimpleDateFormat")
    @Throws(JsonParseException::class)
    override fun deserialize(element: JsonElement, arg1: Type, arg2: JsonDeserializationContext): Date? {
        val date = element.asString
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        return try {
            formatter.parse(date)
        } catch (e: ParseException) {
            null
        }

    }
}