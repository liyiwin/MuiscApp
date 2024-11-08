package com.example.musicapp.data.dao.RequestResultConverter

import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.nio.charset.Charset

open class RequestResultConverter {

    protected fun ConvertResponseErrorMessage(response: Response<ResponseBody>):String{
        try{
            val errorBody = response.errorBody();
            val jsonObject = getJsonObjectFromResponseBody(errorBody);
            val errorObject = jsonObject.getJSONObject("error");
            return errorObject.getString("message")
        }catch (e:Exception ){
            return "Api 錯誤訊息轉換失敗，$e";
        }
    }


    protected fun ConvertResponseFailureMessage(call: Call<ResponseBody>, t: Throwable):String{

        when {
            call.isCanceled -> {
                return "請求已取消"
            }
            t is UnknownHostException -> {
                return "網路連線不穩定，無法獲取伺服器位置"
            }
            t is SocketTimeoutException -> {
                // "Connection Timeout";
                return "網路連線逾時"
            }
            t is IOException -> {
                // "Timeout";
                return "網路連線逾時"
            }
            else -> {
                return "網路連線不穩定，無法獲取伺服器位置"

            }
        }
    }



    protected fun getJsonObjectFromResponseBody(responseBody: ResponseBody?): JSONObject {
        val source = responseBody?.source();
        source?.request(Long.MAX_VALUE); // Buffer the entire body.
        val buffer = source?.buffer();
        val UTF8 = Charset.forName("UTF-8");
        val res = buffer?.clone()?.readString(UTF8)?:"";
        val jsonObject =  JSONObject(res);
        return jsonObject;
    }

}