package com.example.inzbottom.ui.consultations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.inzbottom.databinding.FragmentConsultationsBinding
import com.example.inzbottom.ui.consultations.ConsultationsViewModel

class ConsultationsFragment : Fragment() {

    private var _binding: FragmentConsultationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val consultationsViewModel =
            ViewModelProvider(this).get(ConsultationsViewModel::class.java)

        _binding = FragmentConsultationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textConsultations
        consultationsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}