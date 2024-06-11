package com.example.marmitonai.models
import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ingredients")
data class Ingredient(
    @PrimaryKey val barcode: String,
    val name: String,
    val servingSize: Int,
    val servingUnit: String,
    val kcal: Int,
    val fat: Int,
    val carbs: Int,
    val sugars: Int,
    val protein: Int,
    val salt: Int,
    val imageId: String?,
    @ColumnInfo(defaultValue = "0") val quantity: Int
    ): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(barcode)
        parcel.writeString(name)
        parcel.writeInt(servingSize)
        parcel.writeString(servingUnit)
        parcel.writeInt(kcal)
        parcel.writeInt(fat)
        parcel.writeInt(carbs)
        parcel.writeInt(sugars)
        parcel.writeInt(protein)
        parcel.writeInt(salt)
        parcel.writeString(imageId)
        parcel.writeInt(quantity)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Ingredient> {
        override fun createFromParcel(parcel: Parcel): Ingredient {
            return Ingredient(parcel)
        }

        override fun newArray(size: Int): Array<Ingredient?> {
            return arrayOfNulls(size)
        }
    }
}