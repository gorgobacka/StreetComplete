package de.westnordost.streetcomplete.quests.parking_type

import android.os.Bundle

import de.westnordost.streetcomplete.R
import de.westnordost.streetcomplete.data.osm.SimpleOverpassQuestType
import de.westnordost.streetcomplete.data.osm.changes.StringMapChangesBuilder
import de.westnordost.streetcomplete.data.osm.download.OverpassMapDataDao

class AddParkingType(o: OverpassMapDataDao) : SimpleOverpassQuestType(o) {

    override val tagFilters = "nodes, ways, relations with amenity=parking and !parking"
    override val commitMessage = "Add parking type"
    override val icon = R.drawable.ic_quest_parking

	override fun getTitle(tags: Map<String, String>) = R.string.quest_parkingType_title

	override fun createForm() = AddParkingTypeForm()

    override fun applyAnswerTo(answer: Bundle, changes: StringMapChangesBuilder) {
        val values = answer.getStringArrayList(AddParkingTypeForm.OSM_VALUES)
        if (values != null && values.size == 1) {
            changes.add("parking", values[0])
        }
    }
}
