package pl.edu.pja.p03a.model

import org.simpleframework.xml.*

@Root(strict = false, name = "rss")
data class Channel(
    @field:ElementList(inline=true)
//    @Path("item")
//    @Text(required=false)
    var item: List<Item>?,
    @field:Element(name = "title", required = false) var title: String?,
    @field:Element(name = "link", required = false) var link: String?,
    @field:Element(name = "description", required = false) var description: String?,
//    @field:Element(name = "language", required = false) var language: String?,
    @field:Element(name = "copyright", required = false) var copyright: String?,
    @field:Element(name = "ttl", required = false) var ttl: Int?,
    @field:Element(name = "dc:language", required = false) var dclanguage: String?,
    @field:Element(name = "dc:rights", required = false) var rights: String?,
    @field:Element(name = "image", required = false) var image: Image?,
//    @field:Element(name = "lastBuildDate", required = false) var lastBuildDate: String?,
//    @field:Element(name = "generator", required = false) var generator: String?,
)
{
    constructor() : this(null, "", "", "", "", 0, "","", null)
}