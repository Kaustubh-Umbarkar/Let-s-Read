package com.example.blogapp.Data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize

class User(val uid:String, val username: String,val fullname:String,val phone:String): Parcelable
{
    constructor():this("","","","")

}
