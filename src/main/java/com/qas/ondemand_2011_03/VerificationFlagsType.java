
package com.qas.ondemand_2011_03;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for VerificationFlagsType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="VerificationFlagsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="BldgFirmNameChanged" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="PrimaryNumberChanged" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="StreetCorrected" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="RuralRteHighwayContractMatched" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="CityNameChanged" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="CityAliasMatched" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="StateProvinceChanged" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="PostCodeCorrected" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="SecondaryNumRetained" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="IdenPreStInfoRetained" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="GenPreStInfoRetained" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="PostStInfoRetained" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VerificationFlagsType", propOrder = {
    "bldgFirmNameChanged",
    "primaryNumberChanged",
    "streetCorrected",
    "ruralRteHighwayContractMatched",
    "cityNameChanged",
    "cityAliasMatched",
    "stateProvinceChanged",
    "postCodeCorrected",
    "secondaryNumRetained",
    "idenPreStInfoRetained",
    "genPreStInfoRetained",
    "postStInfoRetained"
})
public class VerificationFlagsType {

    @XmlElement(name = "BldgFirmNameChanged", defaultValue = "false")
    protected Boolean bldgFirmNameChanged;
    @XmlElement(name = "PrimaryNumberChanged", defaultValue = "false")
    protected Boolean primaryNumberChanged;
    @XmlElement(name = "StreetCorrected", defaultValue = "false")
    protected Boolean streetCorrected;
    @XmlElement(name = "RuralRteHighwayContractMatched", defaultValue = "false")
    protected Boolean ruralRteHighwayContractMatched;
    @XmlElement(name = "CityNameChanged", defaultValue = "false")
    protected Boolean cityNameChanged;
    @XmlElement(name = "CityAliasMatched", defaultValue = "false")
    protected Boolean cityAliasMatched;
    @XmlElement(name = "StateProvinceChanged", defaultValue = "false")
    protected Boolean stateProvinceChanged;
    @XmlElement(name = "PostCodeCorrected", defaultValue = "false")
    protected Boolean postCodeCorrected;
    @XmlElement(name = "SecondaryNumRetained", defaultValue = "false")
    protected Boolean secondaryNumRetained;
    @XmlElement(name = "IdenPreStInfoRetained", defaultValue = "false")
    protected Boolean idenPreStInfoRetained;
    @XmlElement(name = "GenPreStInfoRetained", defaultValue = "false")
    protected Boolean genPreStInfoRetained;
    @XmlElement(name = "PostStInfoRetained", defaultValue = "false")
    protected Boolean postStInfoRetained;

    /**
     * Gets the value of the bldgFirmNameChanged property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isBldgFirmNameChanged() {
        return bldgFirmNameChanged;
    }

    /**
     * Sets the value of the bldgFirmNameChanged property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setBldgFirmNameChanged(Boolean value) {
        this.bldgFirmNameChanged = value;
    }

    /**
     * Gets the value of the primaryNumberChanged property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isPrimaryNumberChanged() {
        return primaryNumberChanged;
    }

    /**
     * Sets the value of the primaryNumberChanged property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setPrimaryNumberChanged(Boolean value) {
        this.primaryNumberChanged = value;
    }

    /**
     * Gets the value of the streetCorrected property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isStreetCorrected() {
        return streetCorrected;
    }

    /**
     * Sets the value of the streetCorrected property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setStreetCorrected(Boolean value) {
        this.streetCorrected = value;
    }

    /**
     * Gets the value of the ruralRteHighwayContractMatched property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isRuralRteHighwayContractMatched() {
        return ruralRteHighwayContractMatched;
    }

    /**
     * Sets the value of the ruralRteHighwayContractMatched property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setRuralRteHighwayContractMatched(Boolean value) {
        this.ruralRteHighwayContractMatched = value;
    }

    /**
     * Gets the value of the cityNameChanged property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isCityNameChanged() {
        return cityNameChanged;
    }

    /**
     * Sets the value of the cityNameChanged property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setCityNameChanged(Boolean value) {
        this.cityNameChanged = value;
    }

    /**
     * Gets the value of the cityAliasMatched property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isCityAliasMatched() {
        return cityAliasMatched;
    }

    /**
     * Sets the value of the cityAliasMatched property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setCityAliasMatched(Boolean value) {
        this.cityAliasMatched = value;
    }

    /**
     * Gets the value of the stateProvinceChanged property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isStateProvinceChanged() {
        return stateProvinceChanged;
    }

    /**
     * Sets the value of the stateProvinceChanged property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setStateProvinceChanged(Boolean value) {
        this.stateProvinceChanged = value;
    }

    /**
     * Gets the value of the postCodeCorrected property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isPostCodeCorrected() {
        return postCodeCorrected;
    }

    /**
     * Sets the value of the postCodeCorrected property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setPostCodeCorrected(Boolean value) {
        this.postCodeCorrected = value;
    }

    /**
     * Gets the value of the secondaryNumRetained property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isSecondaryNumRetained() {
        return secondaryNumRetained;
    }

    /**
     * Sets the value of the secondaryNumRetained property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSecondaryNumRetained(Boolean value) {
        this.secondaryNumRetained = value;
    }

    /**
     * Gets the value of the idenPreStInfoRetained property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIdenPreStInfoRetained() {
        return idenPreStInfoRetained;
    }

    /**
     * Sets the value of the idenPreStInfoRetained property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIdenPreStInfoRetained(Boolean value) {
        this.idenPreStInfoRetained = value;
    }

    /**
     * Gets the value of the genPreStInfoRetained property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isGenPreStInfoRetained() {
        return genPreStInfoRetained;
    }

    /**
     * Sets the value of the genPreStInfoRetained property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setGenPreStInfoRetained(Boolean value) {
        this.genPreStInfoRetained = value;
    }

    /**
     * Gets the value of the postStInfoRetained property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isPostStInfoRetained() {
        return postStInfoRetained;
    }

    /**
     * Sets the value of the postStInfoRetained property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setPostStInfoRetained(Boolean value) {
        this.postStInfoRetained = value;
    }

}
