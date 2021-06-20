package pl.edu.pja.p03a.model

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(strict = false, name = "enclosure")
data class Enclosure constructor(
    @field:Element(name = "url") var url: String?,
    @field:Element(name = "length") var length: Int?,
    @field:Element(name = "type") var type: String?
)
{
    constructor() : this( "", 0, "")
}