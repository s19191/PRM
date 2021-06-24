package pl.edu.pja.p03a.model

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(strict = false, name = "rss")
data class Image(
    @field:Element(name = "title", required = false) var title: String?,
    @field:Element(name = "url", required = false) var url: String?,
    @field:Element(name = "link", required = false) var link: String?,
)
{
    constructor() : this("", "", "")
}