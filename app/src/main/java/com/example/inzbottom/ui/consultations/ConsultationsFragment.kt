package com.example.inzbottom.ui.consultations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.inzbottom.R
import java.text.SimpleDateFormat
import java.util.*

class ConsultationsFragment : Fragment() {

    private lateinit var calendarView: CalendarView
    private lateinit var selectedDateTextView: TextView
    private lateinit var appointmentTitleEditText: EditText
    private lateinit var appointmentDescriptionEditText: EditText
    private lateinit var saveAppointmentButton: Button

    private val appointments: MutableMap<Long, MutableList<String>> = mutableMapOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_consultations, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        calendarView = view.findViewById(R.id.calendarView)
        selectedDateTextView = view.findViewById(R.id.selectedDate)
        appointmentTitleEditText = view.findViewById(R.id.appointmentTitle)
        appointmentDescriptionEditText = view.findViewById(R.id.appointmentDescription)
        saveAppointmentButton = view.findViewById(R.id.saveAppointmentButton)

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = Calendar.getInstance().apply {
                set(year, month, dayOfMonth)
            }.timeInMillis

            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            selectedDateTextView.text = "Selected Date: ${dateFormat.format(Date(selectedDate))}"
        }

        saveAppointmentButton.setOnClickListener {
            saveAppointment()
        }
    }

    private fun saveAppointment() {
        val title = appointmentTitleEditText.text.toString()
        val description = appointmentDescriptionEditText.text.toString()

        if (title.isNotEmpty() && description.isNotEmpty()) {
            val selectedDate = calendarView.date
            val appointmentDetails = "$title: $description"

            if (appointments.containsKey(selectedDate)) {
                appointments[selectedDate]?.add(appointmentDetails)
            } else {
                appointments[selectedDate] = mutableListOf(appointmentDetails)
            }

            Toast.makeText(requireContext(), "Appointment saved!", Toast.LENGTH_SHORT).show()

            // Clear fields after saving
            appointmentTitleEditText.text.clear()
            appointmentDescriptionEditText.text.clear()
        } else {
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
        }
    }
}
