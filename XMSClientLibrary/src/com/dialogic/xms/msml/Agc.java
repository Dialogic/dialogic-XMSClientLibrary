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
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;extension base="{}primitiveType">
 *       &lt;attribute name="tgtlvl" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}nonPositiveInteger">
 *             &lt;minInclusive value="-40"/>
 *             &lt;maxInclusive value="0"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="maxgain" default="10">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger">
 *             &lt;minInclusive value="0"/>
 *             &lt;maxInclusive value="40"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
public class Agc
    extends PrimitiveType
{

    @XmlAttribute(name = "tgtlvl", required = true)
    protected int tgtlvl;
    @XmlAttribute(name = "maxgain")
    protected Integer maxgain;

    /**
     * Gets the value of the tgtlvl property.
     * 
     */
    public int getTgtlvl() {
        return tgtlvl;
    }

    /**
     * Sets the value of the tgtlvl property.
     * 
     */
    public void setTgtlvl(int value) {
        this.tgtlvl = value;
    }

    /**
     * Gets the value of the maxgain property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public int getMaxgain() {
        if (maxgain == null) {
            return  10;
        } else {
            return maxgain;
        }
    }

    /**
     * Sets the value of the maxgain property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMaxgain(Integer value) {
        this.maxgain = value;
    }

}
