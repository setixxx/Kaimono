package setixx.software.kaimono.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CategoryResponse(
    val id: Long,
    val name: String,
    val description: String?,

    @SerialName("parent_id")
    val parentId: Long?
)