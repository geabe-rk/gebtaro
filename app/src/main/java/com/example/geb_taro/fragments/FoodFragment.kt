package com.example.geb_taro.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import com.example.geb_taro.databinding.FragmentFoodBinding

class FoodFragment : Fragment() {

    private var _binding: FragmentFoodBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFoodBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        binding.edtSearchFood.doAfterTextChanged { text ->
            val query = text?.toString()?.trim().orEmpty()

            val showPizza =
                query.isEmpty() || "pizza".contains(query, true)

            val showBurger =
                query.isEmpty() || "burger".contains(query, true)

            binding.txtFoodPizza.visibility =
                if (showPizza) View.VISIBLE else View.GONE

            binding.txtFoodPizzaPrice.visibility =
                if (showPizza) View.VISIBLE else View.GONE

            binding.txtFoodBurger.visibility =
                if (showBurger) View.VISIBLE else View.GONE

            binding.txtFoodBurgerPrice.visibility =
                if (showBurger) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
