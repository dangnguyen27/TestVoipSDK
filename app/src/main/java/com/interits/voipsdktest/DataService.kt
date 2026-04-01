package com.interits.voipsdktest

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import javax.net.ssl.HttpsURLConnection

class DataService {

    private val APIURL = "https://api-ghtk.interits.com/example-sdk/api/"
    val TAG = "voipsdk"

    @Throws(IOException::class)
    private fun encodeParams(params: JSONObject): String? {
        val result = StringBuilder()
        var first = true
        val itr = params.keys()
        while (itr.hasNext()) {
            val key = itr.next()
            val value = params[key]
            if (first) first = false else result.append("&")
            result.append(URLEncoder.encode(key, "UTF-8"))
            result.append("=")
            result.append(URLEncoder.encode(value.toString(), "UTF-8"))
        }
        return result.toString()
    }
    fun requestPOST(r_url: String?, postDataParams: JSONObject): JSONObject? {
//        try {
            val url = URL(APIURL + r_url)
            Log.d(TAG, url.toString())
            val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
            conn.readTimeout = 3000
            conn.connectTimeout = 3000
            conn.requestMethod = "POST"
            conn.doInput = true
            conn.doOutput = true

            val os: OutputStream = conn.outputStream
            val writer = BufferedWriter(OutputStreamWriter(os, "UTF-8"))
            writer.write(encodeParams(postDataParams))
            writer.flush()
            writer.close()
            os.close()
            val responseCode: Int = conn.responseCode // To Check for 200
            Log.d(TAG, "responseCode $responseCode")
            if (responseCode == HttpsURLConnection.HTTP_OK || responseCode == HttpsURLConnection.HTTP_CREATED) {
                val `in` = BufferedReader(InputStreamReader(conn.inputStream))
                val sb = StringBuffer("")
                var line: String? = ""
                while (`in`.readLine().also { line = it } != null) {
                    sb.append(line)
                    break
                }
                `in`.close()
                Log.d(TAG, sb.toString())
                return JSONObject(sb.toString())
            } else {
                val `in` = BufferedReader(InputStreamReader(conn.errorStream))
                val sb = StringBuffer("")
                var line: String? = ""
                while (`in`.readLine().also { line = it } != null) {
                    sb.append(line)
                    break
                }
                `in`.close()
                Log.d(TAG, sb.toString())
                return JSONObject(sb.toString())
            }
            return null
//        }catch (e: Exception){
//            Log.d(TAG, e.toString())
//            return null
//        }
    }

    fun saveRegToken(regToken:String, uid: String) : JSONObject?{
        val map = HashMap<String,String>()
        map["token"] = regToken
        map["uid"] = uid
        map["os"] = "android"
        val json = JSONObject(map as Map<*, *>?)
        return requestPOST("save-token",json)
    }

    suspend fun loginSip(msisdn: String, os: String, model: String, uiid: String, ip: String, user_agent: String) : JSONObject?{
        val map = HashMap<String,String>()
        map["msisdn"] = msisdn
        map["uiid"] = uiid
        map["model"] = model
        map["ip"] = ip
        map["user_agent"] = user_agent
        map["os"] = "android"
        val json = JSONObject(map as Map<*, *>?)
        Log.d(TAG, "loginSip")
        return callApi("login-sip",json)
    }

    suspend fun callApi(r_url: String, postDataParams: JSONObject): JSONObject? {
        // Chuyển tác vụ sang luồng IO
        return withContext(Dispatchers.IO) {
            try {
                val url = URL(APIURL + r_url)
                val conn = url.openConnection() as HttpsURLConnection
                conn.readTimeout = 3000
                conn.connectTimeout = 3000
                conn.requestMethod = "POST"
                conn.doInput = true
                conn.doOutput = true

                conn.outputStream.use { os ->
                    BufferedWriter(OutputStreamWriter(os, "UTF-8")).use { writer ->
                        writer.write(encodeParams(postDataParams))
                        writer.flush()
                    }
                }

                val responseCode = conn.responseCode
                Log.d(TAG, "responseCode $responseCode")

                // Cách đọc stream rút gọn và an toàn hơn của Kotlin
                val responseString = if (responseCode == HttpsURLConnection.HTTP_OK || responseCode == HttpsURLConnection.HTTP_CREATED) {
                    conn.inputStream.bufferedReader().use { it.readText() }
                } else {
                    conn.errorStream.bufferedReader().use { it.readText() }
                }

                Log.d(TAG, responseString)
                return@withContext JSONObject(responseString)

            } catch (e: Exception) {
                Log.e(TAG, "API Error: ${e.message}")
                return@withContext null
            }
        }
    }

}