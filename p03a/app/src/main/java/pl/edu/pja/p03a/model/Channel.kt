package pl.edu.pja.p03a.model

import org.simpleframework.xml.*

@Root(strict = false, name = "rss")
data class Channel(
    @field:ElementList(inline=true) var item: MutableList<Item>?,
//    @field:Element(name = "title", required = false) var title: String?,
//    @field:Element(name = "link", required = false) var link: String?,
//    @field:Element(name = "description", required = false) var description: String?,
    @field:Element(name = "image", required = false) var image: Image?
)
{
    constructor() : this(null, null)
}