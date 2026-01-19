package software.setixx.kaimono.domain.validation

import software.setixx.kaimono.domain.model.Cart
import javax.inject.Inject

class CartValidator @Inject constructor() {
    fun validateCart(cart: Cart): ValidationResult {
        if (cart.items.isEmpty()) {
            return ValidationResult.Error(ValidationError.CartEmpty)
        }
        return ValidationResult.Success
    }
}