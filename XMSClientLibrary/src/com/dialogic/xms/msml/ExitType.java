//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.10.09 at 04:34:21 PM EDT 
//


package com.dialogic.xms.msml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for exitType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="exitType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="namelist" type="{}momlNamelist.datatype" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "exitType")
public class ExitType {

    @XmlAttribute(name = "namelist")
    protected String namelist;

    /**
     * Gets the value of the namelist property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNamelist() {
        return namelist;
    }

    /**
     * Sets the value of the namelist property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNamelist(String value) {
        this.namelist = value;
    }

}
