
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
 *         &lt;element name="QAPicklist" type="{http://www.qas.com/OnDemand-2011-03}QAPicklistType" minOccurs="0"/>
 *         &lt;element name="QAAddress" type="{http://www.qas.com/OnDemand-2011-03}QAAddressType" minOccurs="0"/>
 *         &lt;element name="VerificationFlags" type="{http://www.qas.com/OnDemand-2011-03}VerificationFlagsType" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="VerifyLevel" type="{http://www.qas.com/OnDemand-2011-03}VerifyLevelType" default="None" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "qaPicklist",
    "qaAddress",
    "verificationFlags"
})
@XmlRootElement(name = "QASearchResult")
public class QASearchResult {

    @XmlElement(name = "QAPicklist")
    protected QAPicklistType qaPicklist;
    @XmlElement(name = "QAAddress")
    protected QAAddressType qaAddress;
    @XmlElement(name = "VerificationFlags")
    protected VerificationFlagsType verificationFlags;
    @XmlAttribute(name = "VerifyLevel")
    protected VerifyLevelType verifyLevel;

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

    /**
     * Gets the value of the qaAddress property.
     * 
     * @return
     *     possible object is
     *     {@link QAAddressType }
     *     
     */
    public QAAddressType getQAAddress() {
        return qaAddress;
    }

    /**
     * Sets the value of the qaAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link QAAddressType }
     *     
     */
    public void setQAAddress(QAAddressType value) {
        this.qaAddress = value;
    }

    /**
     * Gets the value of the verificationFlags property.
     * 
     * @return
     *     possible object is
     *     {@link VerificationFlagsType }
     *     
     */
    public VerificationFlagsType getVerificationFlags() {
        return verificationFlags;
    }

    /**
     * Sets the value of the verificationFlags property.
     * 
     * @param value
     *     allowed object is
     *     {@link VerificationFlagsType }
     *     
     */
    public void setVerificationFlags(VerificationFlagsType value) {
        this.verificationFlags = value;
    }

    /**
     * Gets the value of the verifyLevel property.
     * 
     * @return
     *     possible object is
     *     {@link VerifyLevelType }
     *     
     */
    public VerifyLevelType getVerifyLevel() {
        if (verifyLevel == null) {
            return VerifyLevelType.NONE;
        } else {
            return verifyLevel;
        }
    }

    /**
     * Sets the value of the verifyLevel property.
     * 
     * @param value
     *     allowed object is
     *     {@link VerifyLevelType }
     *     
     */
    public void setVerifyLevel(VerifyLevelType value) {
        this.verifyLevel = value;
    }

}
