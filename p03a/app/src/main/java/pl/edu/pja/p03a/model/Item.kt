package pl.edu.pja.p03a.model

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementArray
import org.simpleframework.xml.Root
import java.util.*

@Root(strict = false, name = "item")
data class Item (
    @field:Element(name = "title", required = false) var title: String?,
    @field:Element(name = "link", required = false) var link: String?,
    @field:Element(name = "description", required = false) var description: String?,
//    @field:Element(name = "enclosure", required = false) var enclosure: Enclosure?,
//    @field:Element(name = "pubDate", required = false) var pubDate: Date?,
    @field:Element(name = "guid", required = false) var guid: String?,
    @field:Element(name = "creator", required = false) var creator: String?,
//    @field:Element(name = "date", required = false) var date: Date?,
//    @field:Element(name = "comments", required = false) var comments: String?,
//    @field:ElementArray(name = "category", required = false) var category: Array<String>?,
)
{
    constructor() : this("","","", "","")
}