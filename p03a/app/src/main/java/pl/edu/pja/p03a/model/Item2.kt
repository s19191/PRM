package pl.edu.pja.p03a.model

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root
import java.util.*

@Root
data class Item2 (
    @field:ElementList(name = "item", required = false)
//    @Path("item")
//    @Text(required=false)
    var item: List<Item>?,
)
{
    constructor() : this(null)
}