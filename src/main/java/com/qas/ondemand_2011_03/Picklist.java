
package com.qas.ondemand_2011_03;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
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
 *       &lt;sequence>
 *         &lt;element name="QAPicklist" type="{http://www.qas.com/OnDemand-2011-03}QAPicklistType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "qaPicklist"
})
@XmlRootElement(name = "Picklist")
public class Picklist {

    @XmlElement(name = "QAPicklist", required = true)
    protected QAPicklistType qaPicklist;

    /**
     * Gets the value of the qaPicklist property.
     * 
     * @return
     *     possible object is
     *     {@link QAPicklistType }
     *     
     */
    public QAPicklistType getQAPicklist() {
        return qaPicklist;
    }

    /**
     * Sets the value of the qaPicklist property.
     * 
     * @param value
     *     allowed object is
     *     {@link QAPicklistType }
     *     
     */
    public void setQAPicklist(QAPicklistType value) {
        this.qaPicklist = value;
    }

}
