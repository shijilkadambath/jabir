package com.bigtime.mla.data.api
/**
 * Created by Shijil Kadambath on 03/08/2018
 * for NewAgeSMB
 * Email : shijil@newagesmb.com
 */

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose



data class PaginationResponse<T>(

        //@field:SerializedName(value = "group_icon", alternate = arrayOf("value", "username", "count", "jabber_id"))
        @field:SerializedName("programs")
        val data: T?
){

   @field:SerializedName("pagination")
   val pagination: Pagination? = null


inner class Pagination {

    @SerializedName("page")
    @Expose
    var page: Int? = null
    @SerializedName("pageSize")
    @Expose
    var pageSize: Int? = null
    @SerializedName("rowCount")
    @Expose
    var rowCount: Int? = null
    @SerializedName("pageCount")
    @Expose
    var pageCount: Int? = null
    @SerializedName("firstPage")
    @Expose
    var firstPage: String? = null
    @SerializedName("prevPage")
    @Expose
    var prevPage: Boolean? = null
    @SerializedName("nextPage")
    @Expose
    var nextPage: Boolean? = null
    @SerializedName("lastPage")
    @Expose
    var lastPage: String? = null

}

}