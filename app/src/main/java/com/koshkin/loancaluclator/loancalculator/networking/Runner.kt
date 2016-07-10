package com.koshkin.loancaluclator.loancalculator.networking

import android.os.AsyncTask
import android.util.Log
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody

/**
 * Created by koshkin on 7/4/16.
 *
 * Runner
 */
var userToken: String ? = null;

class Runner : AsyncTask<Request, Void, Response>() {

    var defaultMediaType = MediaType.parse("application/json; charset=utf-8")

    override fun onPostExecute(result: Response?) {
        if (request != null)
            request!!.networkResponse.onResponse(result!!, request!!)
    }

    var request: Request? = null

    override fun doInBackground(vararg request: Request?): Response {
        val req = request[0]
        val response = Response(req!!.parsingObject)
        this.request = req

        try {
            when (req.requestType) {
                Request.Type.GET -> response.httpRequest(req) { get() }
                Request.Type.POST -> response.httpRequest(req) { post(RequestBody.create(defaultMediaType, req.requestData)) }
                Request.Type.DELETE -> response.httpRequest(req) { delete() }
                Request.Type.PUT -> response.httpRequest(req) { put(RequestBody.create(defaultMediaType, req.requestData)) }
            }
        } catch (e: Exception) {
            Log.v(this.javaClass.simpleName, "Exception", e)
            response.status = Response.ResponseStatus.FAILURE
        }

        return response
    }

    private fun Response.httpRequest(request: Request, func: okhttp3.Request.Builder.() -> okhttp3.Request.Builder) {
        val client = getClient()

        Log.d(TAG, "Request url - ${request.url}")

        val builder = defaultBuilder(request)!!.func()
        val okResponse = client.newCall(builder.build()).execute()

        parseResponse(this, okResponse)
    }

    private fun parseResponse(response: Response, okResponse: okhttp3.Response) {
        if (okResponse.isSuccessful) response.status = Response.ResponseStatus.SUCCESS
        else response.status = Response.ResponseStatus.FAILURE

        Log.d(TAG, "Response STATUS - " + response.status)

        if (response.status == Response.ResponseStatus.SUCCESS)
            response.parsingObject.parse(okResponse.body().string())
    }

    val AuthorizationHeader = "Authorization"
    val TAG = "Runner"

    private fun defaultBuilder(request: Request): okhttp3.Request.Builder? {
        Log.d(TAG, "userToken $userToken")
        return okhttp3.Request.Builder().addHeader(AuthorizationHeader, userToken)
                .url(request.url)
    }

    private fun getClient(): OkHttpClient {
        if (client == null) {
            client = OkHttpClient()
        }
        return client as OkHttpClient
    }

}

var client: OkHttpClient? = null
