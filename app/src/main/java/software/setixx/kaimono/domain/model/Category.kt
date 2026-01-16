package software.setixx.kaimono.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(
    val id: Long,
    val name: String,
    val description: String?,
    val parentId: Long?
) : Parcelable