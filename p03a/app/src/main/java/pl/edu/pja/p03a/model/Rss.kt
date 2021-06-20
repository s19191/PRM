package pl.edu.pja.p03a.model

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(strict = false, name = "rss")
data class Rss(
//    @field:Attribute(name = "xmlns:dc", required = false) var dc: String?,
    @field:Element(name = "channel", required = false) var channel: Channel?,
//    @field:Element(name = "version", required = false) var version: String?,
)
{
    constructor() : this(null)
}