package de.westnordost.streetcomplete.quests.bike_parking_capacity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import de.westnordost.streetcomplete.R
import de.westnordost.streetcomplete.quests.TextInputQuestAnswerFragment
import kotlinx.android.synthetic.main.quest_bike_parking_capacity.*

class AddBikeParkingCapacityForm : TextInputQuestAnswerFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        setContentView(R.layout.quest_bike_parking_capacity)
        return view
    }

    override fun getEditText() = inputCapacity
}