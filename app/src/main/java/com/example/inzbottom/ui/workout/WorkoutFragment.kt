package com.example.inzbottom.ui.workout

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.example.inzbottom.R

class WorkoutFragment : Fragment() {

    private lateinit var cardsContainer: LinearLayout
    private lateinit var addCardButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_workout, container, false)
        cardsContainer = view.findViewById(R.id.cardsContainer)
        addCardButton = view.findViewById(R.id.addCardButton)

        addCardButton.setOnClickListener {
            addNewCard()
        }

        return view
    }

    private fun addNewCard() {
        // Create a new CardView programmatically
        val cardView = LayoutInflater.from(requireContext()).inflate(R.layout.card_item, cardsContainer, false) as CardView

        // Set up drag-and-drop functionality for the new CardView
        setCardDragListener(cardView)

        // Add the new CardView to the container
        cardsContainer.addView(cardView)
    }

    private fun setCardDragListener(cardView: CardView) {
        var dX = 0f
        var dY = 0f
        var lastAction: Int = 0

        cardView.setOnTouchListener { v, event ->
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    dX = v.x - event.rawX
                    dY = v.y - event.rawY
                    lastAction = MotionEvent.ACTION_DOWN
                }
                MotionEvent.ACTION_MOVE -> {
                    v.animate()
                        .x(event.rawX + dX)
                        .y(event.rawY + dY)
                        .setDuration(0)
                        .start()
                    lastAction = MotionEvent.ACTION_MOVE
                }
                MotionEvent.ACTION_UP -> {
                    if (lastAction == MotionEvent.ACTION_MOVE) {
                        // Save position if needed
                        val sharedPreferences = requireContext().getSharedPreferences("CardPosition", Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putFloat("x_${v.id}", v.x)
                        editor.putFloat("y_${v.id}", v.y)
                        editor.apply()
                    }
                }
                else -> return@setOnTouchListener false
            }
            true
        }
    }
}
