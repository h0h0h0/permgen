
package com.qas.ondemand_2011_03;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * MissingSubPremise: Please refer to the associated Pro On Demand web service documentation for information about this feature.
 * 
 * <p>Java class for QAAddressType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="QAAddressType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="AddressLine" type="{http://www.qas.com/OnDemand-2011-03}AddressLineType" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *       &lt;attribute name="Overflow" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="Truncated" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="DPVStatus" type="{http://www.qas.com/OnDemand-2011-03}DPVStatusType" />
 *       &lt;attribute name="MissingSubPremise" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "QAAddressType", propOrder = {
    "addressLine"
})
public class QAAddressType {

    @XmlElement(name = "AddressLine", required = true)
    protected List<AddressLineType> addressLine;
    @XmlAttribute(name = "Overflow")
    protected Boolean overflow;
    @XmlAttribute(name = "Truncated")
    protected Boolean truncated;
    @XmlAttribute(name = "DPVStatus")
    protected DPVStatusType dpvStatus;
    @XmlAttribute(name = "MissingSubPremise")
    protected Boolean missingSubPremise;

    /**
     * Gets the value of the addressLine property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the addressLine property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAddressLine().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AddressLineType }
     * 
     * 
     */
    public List<AddressLineType> getAddressLine() {
        if (addressLine == null) {
            addressLine = new ArrayList<AddressLineType>();
        }
        return this.addressLine;
    }

    /**
     * Gets the value of the overflow property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isOverflow() {
        if (overflow == null) {
            return false;
        } else {
            return overflow;
        }
    }

    /**
     * Sets the value of the overflow property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setOverflow(Boolean value) {
        this.overflow = value;
    }

    /**
     * Gets the value of the truncated property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isTruncated() {
        if (truncated == null) {
            return false;
        } else {
            return truncated;
        }
    }

    /**
     * Sets the value of the truncated property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTruncated(Boolean value) {
        this.truncated = value;
    }

    /**
     * Gets the value of the dpvStatus property.
     * 
     * @return
     *     possible object is
     *     {@link DPVStatusType }
     *     
     */
    public DPVStatusType getDPVStatus() {
        return dpvStatus;
    }

    /**
     * Sets the value of the dpvStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link DPVStatusType }
     *     
     */
    public void setDPVStatus(DPVStatusType value) {
        this.dpvStatus = value;
    }

    /**
     * Gets the value of the missingSubPremise property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isMissingSubPremise() {
        if (missingSubPremise == null) {
            return false;
        } else {
            return missingSubPremise;
        }
    }

    /**
     * Sets the value of the missingSubPremise property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setMissingSubPremise(Boolean value) {
        this.missingSubPremise = value;
    }

}
