/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bigtime.mla.data.api

import android.arch.lifecycle.LiveData
import com.bigtime.mla.data.model.Program
import retrofit2.http.*


/**
 * Created by Shijil Kadambath on 03/08/2018
 * for NewAgeSMB
 * Email : shijil@newagesmb.com
 */


/**
 * REST API access points
 */
interface WebService {


   /* @GET("search/repositories")
    fun searchRepos(@Query("q") query: String): LiveData<ApiResponse<Program>>*/


    @GET("admin/programs/all")

    fun loadUsers(@Query("start_date") start: String, @Query("end_date") end: String):
            LiveData<ApiResponse<BaseResponse<PaginationResponse<List<Program>>>>>


    @DELETE("admin/programs/{path}")

    fun delete(@Path("path") id:String):LiveData<ApiResponse<BaseResponse<Program>>>


    @FormUrlEncoded
    @POST("admin/programs")
    abstract fun postEvent( @Field("title") name: String,@Field("description") description: String,
                            @Field("co_name") co_name: String,@Field("co_phone") co_phone: String,
                            @Field("location") location: String,@Field("event_type") event_type: String,
                            @Field("event_timestamp") event_date: Long,
                            @Field("status") status: String): LiveData<ApiResponse<BaseResponse<Program>>>


    @FormUrlEncoded
    @PUT("admin/programs/{path}")
    abstract fun update(@Path("path") id:String, @Field("title") name: String,@Field("description") description: String,
                            @Field("co_name") co_name: String,@Field("co_phone") co_phone: String,
                            @Field("location") location: String,@Field("event_type") event_type: String,
                            @Field("event_timestamp") event_date: Long,
                            @Field("status") status: String): LiveData<ApiResponse<BaseResponse<Program>>>

}
