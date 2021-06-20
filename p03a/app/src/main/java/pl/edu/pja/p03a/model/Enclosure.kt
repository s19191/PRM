package pl.edu.pja.p03a.model

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(strict = false, name = "enclosure")
data class Enclosure constructor(
    @field:Attribute(name = "url") var url: String?,
    @field:Attribute(name = "type") var type: String?
)
{
    constructor() : this( "", "")
}