package de.westnordost.streetcomplete.quests.wheelchair_access

import android.os.Bundle

import de.westnordost.streetcomplete.R
import de.westnordost.streetcomplete.data.osm.SimpleOverpassQuestType
import de.westnordost.streetcomplete.data.osm.changes.StringMapChangesBuilder
import de.westnordost.streetcomplete.data.osm.download.OverpassMapDataDao

class AddWheelChairAccessToilets(o: OverpassMapDataDao) : SimpleOverpassQuestType(o) {

    override val tagFilters =
	    " nodes, ways with  amenity=toilets and access !~ private|customers and !wheelchair"
    override val commitMessage = "Add wheelchair access to toilets"
    override val icon = R.drawable.ic_quest_toilets_wheelchair

	override fun getTitle(tags: Map<String, String>) =
		if (tags.containsKey("name"))
			R.string.quest_wheelchairAccess_toilets_name_title
		else
			R.string.quest_wheelchairAccess_toilets_title

    override fun createForm() = AddWheelchairAccessToiletsForm()

    override fun applyAnswerTo(answer: Bundle, changes: StringMapChangesBuilder) {
        val wheelchair = answer.getString(AddWheelchairAccessToiletsForm.ANSWER)
        if (wheelchair != null) {
            changes.add("wheelchair", wheelchair)
        }
    }
}
