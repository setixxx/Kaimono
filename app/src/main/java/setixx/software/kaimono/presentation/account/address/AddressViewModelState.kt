package setixx.software.kaimono.presentation.account.address

import setixx.software.kaimono.domain.model.Address

data class AddressViewModelState(
    val addresses: List<Address> = emptyList(),
    val selectedAddress: Address? = null,
    val isLoading: Boolean = false,
    val isDefaultAddressChanged: Boolean = false,
    val isAddressDeleted: Boolean = false,
    val errorMessage: String? = null
)
