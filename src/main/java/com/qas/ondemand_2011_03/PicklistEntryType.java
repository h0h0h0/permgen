
package com.qas.ondemand_2011_03;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * EnhancedData        : This entry is from the base dataset but enhanced by the subsidiary data set
 * 
 * <p>Java class for PicklistEntryType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PicklistEntryType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Moniker" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="PartialAddress" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Picklist" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Postcode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Score" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger"/>
 *         &lt;element name="QAAddress" type="{http://www.qas.com/OnDemand-2011-03}QAAddressType" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="FullAddress" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="Multiples" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="CanStep" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="AliasMatch" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="PostcodeRecoded" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="CrossBorderMatch" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="DummyPOBox" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="Name" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="Information" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="WarnInformation" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="IncompleteAddr" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="UnresolvableRange" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="PhantomPrimaryPoint" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="SubsidiaryData" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="ExtendedData" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="EnhancedData" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PicklistEntryType", propOrder = {
    "moniker",
    "partialAddress",
    "picklist",
    "postcode",
    "score",
    "qaAddress"
})
public class PicklistEntryType {

    @XmlElement(name = "Moniker", required = true)
    protected String moniker;
    @XmlElement(name = "PartialAddress", required = true)
    protected String partialAddress;
    @XmlElement(name = "Picklist", required = true)
    protected String picklist;
    @XmlElement(name = "Postcode", required = true)
    protected String postcode;
    @XmlElement(name = "Score", required = true)
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger score;
    @XmlElement(name = "QAAddress")
    protected QAAddressType qaAddress;
    @XmlAttribute(name = "FullAddress")
    protected Boolean fullAddress;
    @XmlAttribute(name = "Multiples")
    protected Boolean multiples;
    @XmlAttribute(name = "CanStep")
    protected Boolean canStep;
    @XmlAttribute(name = "AliasMatch")
    protected Boolean aliasMatch;
    @XmlAttribute(name = "PostcodeRecoded")
    protected Boolean postcodeRecoded;
    @XmlAttribute(name = "CrossBorderMatch")
    protected Boolean crossBorderMatch;
    @XmlAttribute(name = "DummyPOBox")
    protected Boolean dummyPOBox;
    @XmlAttribute(name = "Name")
    protected Boolean name;
    @XmlAttribute(name = "Information")
    protected Boolean information;
    @XmlAttribute(name = "WarnInformation")
    protected Boolean warnInformation;
    @XmlAttribute(name = "IncompleteAddr")
    protected Boolean incompleteAddr;
    @XmlAttribute(name = "UnresolvableRange")
    protected Boolean unresolvableRange;
    @XmlAttribute(name = "PhantomPrimaryPoint")
    protected Boolean phantomPrimaryPoint;
    @XmlAttribute(name = "SubsidiaryData")
    protected Boolean subsidiaryData;
    @XmlAttribute(name = "ExtendedData")
    protected Boolean extendedData;
    @XmlAttribute(name = "EnhancedData")
    protected Boolean enhancedData;

    /**
     * Gets the value of the moniker property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMoniker() {
        return moniker;
    }

    /**
     * Sets the value of the moniker property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMoniker(String value) {
        this.moniker = value;
    }

    /**
     * Gets the value of the partialAddress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPartialAddress() {
        return partialAddress;
    }

    /**
     * Sets the value of the partialAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPartialAddress(String value) {
        this.partialAddress = value;
    }

    /**
     * Gets the value of the picklist property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPicklist() {
        return picklist;
    }

    /**
     * Sets the value of the picklist property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPicklist(String value) {
        this.picklist = value;
    }

    /**
     * Gets the value of the postcode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPostcode() {
        return postcode;
    }

    /**
     * Sets the value of the postcode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPostcode(String value) {
        this.postcode = value;
    }

    /**
     * Gets the value of the score property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getScore() {
        return score;
    }

    /**
     * Sets the value of the score property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setScore(BigInteger value) {
        this.score = value;
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
     * Gets the value of the fullAddress property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isFullAddress() {
        if (fullAddress == null) {
            return false;
        } else {
            return fullAddress;
        }
    }

    /**
     * Sets the value of the fullAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setFullAddress(Boolean value) {
        this.fullAddress = value;
    }

    /**
     * Gets the value of the multiples property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isMultiples() {
        if (multiples == null) {
            return false;
        } else {
            return multiples;
        }
    }

    /**
     * Sets the value of the multiples property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setMultiples(Boolean value) {
        this.multiples = value;
    }

    /**
     * Gets the value of the canStep property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isCanStep() {
        if (canStep == null) {
            return false;
        } else {
            return canStep;
        }
    }

    /**
     * Sets the value of the canStep property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setCanStep(Boolean value) {
        this.canStep = value;
    }

    /**
     * Gets the value of the aliasMatch property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isAliasMatch() {
        if (aliasMatch == null) {
            return false;
        } else {
            return aliasMatch;
        }
    }

    /**
     * Sets the value of the aliasMatch property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setAliasMatch(Boolean value) {
        this.aliasMatch = value;
    }

    /**
     * Gets the value of the postcodeRecoded property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isPostcodeRecoded() {
        if (postcodeRecoded == null) {
            return false;
        } else {
            return postcodeRecoded;
        }
    }

    /**
     * Sets the value of the postcodeRecoded property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setPostcodeRecoded(Boolean value) {
        this.postcodeRecoded = value;
    }

    /**
     * Gets the value of the crossBorderMatch property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isCrossBorderMatch() {
        if (crossBorderMatch == null) {
            return false;
        } else {
            return crossBorderMatch;
        }
    }

    /**
     * Sets the value of the crossBorderMatch property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setCrossBorderMatch(Boolean value) {
        this.crossBorderMatch = value;
    }

    /**
     * Gets the value of the dummyPOBox property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isDummyPOBox() {
        if (dummyPOBox == null) {
            return false;
        } else {
            return dummyPOBox;
        }
    }

    /**
     * Sets the value of the dummyPOBox property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDummyPOBox(Boolean value) {
        this.dummyPOBox = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isName() {
        if (name == null) {
            return false;
        } else {
            return name;
        }
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setName(Boolean value) {
        this.name = value;
    }

    /**
     * Gets the value of the information property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isInformation() {
        if (information == null) {
            return false;
        } else {
            return information;
        }
    }

    /**
     * Sets the value of the information property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setInformation(Boolean value) {
        this.information = value;
    }

    /**
     * Gets the value of the warnInformation property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isWarnInformation() {
        if (warnInformation == null) {
            return false;
        } else {
            return warnInformation;
        }
    }

    /**
     * Sets the value of the warnInformation property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setWarnInformation(Boolean value) {
        this.warnInformation = value;
    }

    /**
     * Gets the value of the incompleteAddr property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isIncompleteAddr() {
        if (incompleteAddr == null) {
            return false;
        } else {
            return incompleteAddr;
        }
    }

    /**
     * Sets the value of the incompleteAddr property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIncompleteAddr(Boolean value) {
        this.incompleteAddr = value;
    }

    /**
     * Gets the value of the unresolvableRange property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isUnresolvableRange() {
        if (unresolvableRange == null) {
            return false;
        } else {
            return unresolvableRange;
        }
    }

    /**
     * Sets the value of the unresolvableRange property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setUnresolvableRange(Boolean value) {
        this.unresolvableRange = value;
    }

    /**
     * Gets the value of the phantomPrimaryPoint property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isPhantomPrimaryPoint() {
        if (phantomPrimaryPoint == null) {
            return false;
        } else {
            return phantomPrimaryPoint;
        }
    }

    /**
     * Sets the value of the phantomPrimaryPoint property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setPhantomPrimaryPoint(Boolean value) {
        this.phantomPrimaryPoint = value;
    }

    /**
     * Gets the value of the subsidiaryData property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isSubsidiaryData() {
        if (subsidiaryData == null) {
            return false;
        } else {
            return subsidiaryData;
        }
    }

    /**
     * Sets the value of the subsidiaryData property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSubsidiaryData(Boolean value) {
        this.subsidiaryData = value;
    }

    /**
     * Gets the value of the extendedData property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isExtendedData() {
        if (extendedData == null) {
            return false;
        } else {
            return extendedData;
        }
    }

    /**
     * Sets the value of the extendedData property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setExtendedData(Boolean value) {
        this.extendedData = value;
    }

    /**
     * Gets the value of the enhancedData property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isEnhancedData() {
        if (enhancedData == null) {
            return false;
        } else {
            return enhancedData;
        }
    }

    /**
     * Sets the value of the enhancedData property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setEnhancedData(Boolean value) {
        this.enhancedData = value;
    }

}
