package de.westnordost.streetcomplete.quests.max_height

import android.os.Bundle

import de.westnordost.osmapi.map.data.BoundingBox
import de.westnordost.osmapi.map.data.Element
import de.westnordost.streetcomplete.R
import de.westnordost.streetcomplete.data.osm.OsmElementQuestType
import de.westnordost.streetcomplete.data.osm.changes.StringMapChangesBuilder
import de.westnordost.streetcomplete.data.osm.download.MapDataWithGeometryHandler
import de.westnordost.streetcomplete.data.osm.download.OverpassMapDataDao
import de.westnordost.streetcomplete.data.osm.tql.FiltersParser
import de.westnordost.streetcomplete.data.osm.tql.OverpassQLUtil

class AddMaxHeight constructor(private val overpassServer: OverpassMapDataDao) : OsmElementQuestType {

    private val nodeFilter by lazy { FiltersParser().parse("""
        nodes with
        (barrier=height_restrictor or amenity=parking_entrance and parking ~ underground|multi-storey)
        and !maxheight and !maxheight:physical
    """)}

    private val wayFilter by lazy { FiltersParser().parse("""
        ways with
        (highway ~ motorway|motorway_link|trunk|trunk_link|primary|primary_link|secondary|secondary_link|tertiary|tertiary_link|unclassified|residential|living_street|track|road
          or (highway=service and access!~private|no and vehicle!~private|no)
        )
        and (covered=yes or tunnel~yes|building_passage|avalanche_protector)
        and !maxheight and !maxheight:physical
    """)}

    override val commitMessage = "Add maximum heights"
    override val icon = R.drawable.ic_quest_max_height

    override fun download(bbox: BoundingBox, handler: MapDataWithGeometryHandler): Boolean {
        return overpassServer.getAndHandleQuota(getOverpassQuery(bbox), handler)
    }

    private fun getOverpassQuery(bbox: BoundingBox) =
        OverpassQLUtil.getGlobalOverpassBBox(bbox) +
        "(" +
        nodeFilter.toOverpassQLString(null) +
        wayFilter.toOverpassQLString(null) +
        ");" +
        OverpassQLUtil.getQuestPrintStatement()

    override fun createForm() = AddMaxHeightForm()

    override fun applyAnswerTo(answer: Bundle, changes: StringMapChangesBuilder) {
        val maxHeight = answer.getString(AddMaxHeightForm.MAX_HEIGHT)
        val noSign = answer.getString(AddMaxHeightForm.NO_SIGN)

        if (maxHeight != null) {
            changes.add("maxheight", maxHeight)
        } else if (noSign != null) {
            if (noSign == AddMaxHeightForm.BELOW_DEFAULT)
                changes.add("maxheight", "below_default")
            else if (noSign == AddMaxHeightForm.DEFAULT)
                changes.add("maxheight", "default")
        }
    }

    override fun isApplicableTo(element: Element) =
        nodeFilter.matches(element) || wayFilter.matches(element)

    override fun getTitle(tags: Map<String, String>): Int {
        val isParkingEntrance = tags["amenity"] == "parking_entrance"
        val isHeightRestrictor =  tags["barrier"] == "height_restrictor"
        val isTunnel = tags["tunnel"] == "yes"

        return when {
            isParkingEntrance -> R.string.quest_maxheight_parking_entrance_title
            isHeightRestrictor -> R.string.quest_maxheight_height_restrictor_title
            isTunnel -> R.string.quest_maxheight_tunnel_title
            else -> R.string.quest_maxheight_title
        }
    }
}