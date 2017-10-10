//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.10.09 at 04:34:21 PM EDT 
//


package com.dialogic.xms.msml;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice maxOccurs="unbounded">
 *         &lt;group ref="{}momlRequest"/>
 *         &lt;element ref="{}event"/>
 *       &lt;/choice>
 *       &lt;attribute name="version" use="required" type="{http://www.w3.org/2001/XMLSchema}string" fixed="1.0" />
 *       &lt;attribute name="id" use="required" type="{}momlID.datatype" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "primitiveOrControlAndSend"
})
@XmlRootElement(name = "moml")
public class Moml {

    @XmlElementRefs({
        @XmlElementRef(name = "exit", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "send", type = Send.class, required = false),
        @XmlElementRef(name = "disconnect", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "control", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "event", type = Event.class, required = false),
        @XmlElementRef(name = "primitive", type = JAXBElement.class, required = false)
    })
    protected List<Object> primitiveOrControlAndSend;
    @XmlAttribute(name = "version", required = true)
    protected String version;
    @XmlAttribute(name = "id", required = true)
    protected String id;

    /**
     * Gets the value of the primitiveOrControlAndSend property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the primitiveOrControlAndSend property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPrimitiveOrControlAndSend().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link ExitType }{@code >}
     * {@link JAXBElement }{@code <}{@link Gain }{@code >}
     * {@link JAXBElement }{@code <}{@link Dtmf }{@code >}
     * {@link JAXBElement }{@code <}{@link Faxdetect }{@code >}
     * {@link JAXBElement }{@code <}{@link Agc }{@code >}
     * {@link JAXBElement }{@code <}{@link ExitType }{@code >}
     * {@link JAXBElement }{@code <}{@link Play }{@code >}
     * {@link JAXBElement }{@code <}{@link Record }{@code >}
     * {@link Event }
     * {@link JAXBElement }{@code <}{@link PrimitiveType }{@code >}
     * {@link JAXBElement }{@code <}{@link Faxrcv }{@code >}
     * {@link JAXBElement }{@code <}{@link Fileop }{@code >}
     * {@link Send }
     * {@link JAXBElement }{@code <}{@link Dtmfgen }{@code >}
     * {@link JAXBElement }{@code <}{@link Transfer }{@code >}
     * {@link JAXBElement }{@code <}{@link Group }{@code >}
     * {@link JAXBElement }{@code <}{@link Collect }{@code >}
     * {@link JAXBElement }{@code <}{@link Object }{@code >}
     * {@link JAXBElement }{@code <}{@link Faxsend }{@code >}
     * 
     * 
     */
    public List<Object> getPrimitiveOrControlAndSend() {
        if (primitiveOrControlAndSend == null) {
            primitiveOrControlAndSend = new ArrayList<Object>();
        }
        return this.primitiveOrControlAndSend;
    }

    /**
     * Gets the value of the version property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersion() {
        if (version == null) {
            return "1.0";
        } else {
            return version;
        }
    }

    /**
     * Sets the value of the version property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersion(String value) {
        this.version = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

}
