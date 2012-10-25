
package com.qas.ondemand_2011_03;

import java.util.ArrayList;
import java.util.List;
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
 *         &lt;element name="WarningLevel" type="{http://www.qas.com/OnDemand-2011-03}LicenceWarningLevel"/>
 *         &lt;element name="LicensedSet" type="{http://www.qas.com/OnDemand-2011-03}QALicensedSet" maxOccurs="unbounded" minOccurs="0"/>
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
    "warningLevel",
    "licensedSet"
})
@XmlRootElement(name = "QADataMapDetail")
public class QADataMapDetail {

    @XmlElement(name = "WarningLevel", required = true)
    protected LicenceWarningLevel warningLevel;
    @XmlElement(name = "LicensedSet")
    protected List<QALicensedSet> licensedSet;

    /**
     * Gets the value of the warningLevel property.
     * 
     * @return
     *     possible object is
     *     {@link LicenceWarningLevel }
     *     
     */
    public LicenceWarningLevel getWarningLevel() {
        return warningLevel;
    }

    /**
     * Sets the value of the warningLevel property.
     * 
     * @param value
     *     allowed object is
     *     {@link LicenceWarningLevel }
     *     
     */
    public void setWarningLevel(LicenceWarningLevel value) {
        this.warningLevel = value;
    }

    /**
     * Gets the value of the licensedSet property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the licensedSet property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLicensedSet().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link QALicensedSet }
     * 
     * 
     */
    public List<QALicensedSet> getLicensedSet() {
        if (licensedSet == null) {
            licensedSet = new ArrayList<QALicensedSet>();
        }
        return this.licensedSet;
    }

}
