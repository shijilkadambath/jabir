package com.bigtime.mla.data.model
/**
 * Created by Shijil Kadambath on 03/08/2018
 * for NewAgeSMB
 * Email : shijil@newagesmb.com
 */
import android.arch.persistence.room.Entity
import android.os.Parcel
import android.os.Parcelable
import android.support.annotation.NonNull
import com.google.gson.annotations.SerializedName

@Entity(primaryKeys = ["id"])
data class Program (
        @field:SerializedName("title")
        var title:String,

        @NonNull
        @field:SerializedName("id")
        var id:Integer,

        @NonNull
        @field:SerializedName("description")
        var description:String,

        @NonNull
        @field:SerializedName("location")
        var location:String,



        @NonNull
        @field:SerializedName("event_type")
        var type:String,

        @field:SerializedName("event_timestamp")
        var event_timestamp:Long,


        @NonNull
        @field:SerializedName("status")
        var status:String,

        @NonNull
        @field:SerializedName("co_name")
        var contact_name:String,

        @NonNull
        @field:SerializedName("co_phone")
        var contact_phone:String,

        @NonNull
        @field:SerializedName("event_date_string")
        var dateString: String,

        @NonNull
        @field:SerializedName("event_time_string")
        var timeString:String

        /*"
created_at : "2018-10-20T09:00:04.000Z"
updated_at : "2018-10-20T09:00:04.000Z"*/

): Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readString(),
                TODO("id"),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readLong(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString()) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeString(title)
                parcel.writeString(description)
                parcel.writeString(location)
                parcel.writeString(type)
                parcel.writeString(status)
                parcel.writeString(contact_name)
                parcel.writeString(contact_phone)
                parcel.writeString(dateString)
                parcel.writeString(timeString)
                parcel.writeLong(event_timestamp)
        }

        override fun describeContents(): Int {
                return 0
        }

        companion object CREATOR : Parcelable.Creator<Program> {
                override fun createFromParcel(parcel: Parcel): Program {
                        return Program(parcel)
                }

                override fun newArray(size: Int): Array<Program?> {
                        return arrayOfNulls(size)
                }
        }
}

