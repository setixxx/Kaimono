package software.setixx.kaimono.presentation.account.address

import software.setixx.kaimono.domain.model.Address

data class AddressViewModelState(
    val addresses: List<Address> = emptyList(),
    val selectedAddress: Address? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
