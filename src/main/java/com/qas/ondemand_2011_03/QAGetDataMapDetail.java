
package com.qas.ondemand_2011_03;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
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
 *         &lt;element name="DataMap" type="{http://www.qas.com/OnDemand-2011-03}DataIDType"/>
 *       &lt;/sequence>
 *       &lt;attribute name="Localisation" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "dataMap"
})
@XmlRootElement(name = "QAGetDataMapDetail")
public class QAGetDataMapDetail {

    @XmlElement(name = "DataMap", required = true)
    protected String dataMap;
    @XmlAttribute(name = "Localisation")
    protected String localisation;

    /**
     * Gets the value of the dataMap property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDataMap() {
        return dataMap;
    }

    /**
     * Sets the value of the dataMap property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataMap(String value) {
        this.dataMap = value;
    }

    /**
     * Gets the value of the localisation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocalisation() {
        return localisation;
    }

    /**
     * Sets the value of the localisation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocalisation(String value) {
        this.localisation = value;
    }

}
