//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.10.09 at 04:34:21 PM EDT 
//


package com.dialogic.xms.msml;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for rcvobjType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="rcvobjType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="objuri" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *       &lt;attribute name="maxpages" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" />
 *       &lt;attribute ref="{http://www.dialogic.com/DialogicTypes}objuriindex"/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "rcvobjType")
public class RcvobjType {

    @XmlAttribute(name = "objuri", required = true)
    @XmlSchemaType(name = "anyURI")
    protected String objuri;
    @XmlAttribute(name = "maxpages")
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger maxpages;
    @XmlAttribute(name = "objuriindex", namespace = "http://www.dialogic.com/DialogicTypes")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger objuriindex;

    /**
     * Gets the value of the objuri property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getObjuri() {
        return objuri;
    }

    /**
     * Sets the value of the objuri property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setObjuri(String value) {
        this.objuri = value;
    }

    /**
     * Gets the value of the maxpages property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getMaxpages() {
        return maxpages;
    }

    /**
     * Sets the value of the maxpages property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setMaxpages(BigInteger value) {
        this.maxpages = value;
    }

    /**
     * Gets the value of the objuriindex property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getObjuriindex() {
        return objuriindex;
    }

    /**
     * Sets the value of the objuriindex property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setObjuriindex(BigInteger value) {
        this.objuriindex = value;
    }

}
