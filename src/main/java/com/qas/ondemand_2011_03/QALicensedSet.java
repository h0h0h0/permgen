
package com.qas.ondemand_2011_03;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * LicenceDaysLeft : The number of days before the licence expires
 * 
 * <p>Java class for QALicensedSet complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="QALicensedSet">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Description" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Copyright" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Version" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="BaseCountry" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Status" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Server" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="WarningLevel" type="{http://www.qas.com/OnDemand-2011-03}LicenceWarningLevel"/>
 *         &lt;element name="DaysLeft" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger"/>
 *         &lt;element name="DataDaysLeft" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger"/>
 *         &lt;element name="LicenceDaysLeft" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "QALicensedSet", propOrder = {
    "id",
    "description",
    "copyright",
    "version",
    "baseCountry",
    "status",
    "server",
    "warningLevel",
    "daysLeft",
    "dataDaysLeft",
    "licenceDaysLeft"
})
public class QALicensedSet {

    @XmlElement(name = "ID", required = true)
    protected String id;
    @XmlElement(name = "Description", required = true)
    protected String description;
    @XmlElement(name = "Copyright", required = true)
    protected String copyright;
    @XmlElement(name = "Version", required = true)
    protected String version;
    @XmlElement(name = "BaseCountry", required = true)
    protected String baseCountry;
    @XmlElement(name = "Status", required = true)
    protected String status;
    @XmlElement(name = "Server", required = true)
    protected String server;
    @XmlElement(name = "WarningLevel", required = true)
    protected LicenceWarningLevel warningLevel;
    @XmlElement(name = "DaysLeft", required = true)
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger daysLeft;
    @XmlElement(name = "DataDaysLeft", required = true)
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger dataDaysLeft;
    @XmlElement(name = "LicenceDaysLeft", required = true)
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger licenceDaysLeft;

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getID() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setID(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the copyright property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCopyright() {
        return copyright;
    }

    /**
     * Sets the value of the copyright property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCopyright(String value) {
        this.copyright = value;
    }

    /**
     * Gets the value of the version property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersion(String value) {
        this.version = value;
    }

    /**
     * Gets the value of the baseCountry property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBaseCountry() {
        return baseCountry;
    }

    /**
     * Sets the value of the baseCountry property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBaseCountry(String value) {
        this.baseCountry = value;
    }

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatus(String value) {
        this.status = value;
    }

    /**
     * Gets the value of the server property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServer() {
        return server;
    }

    /**
     * Sets the value of the server property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServer(String value) {
        this.server = value;
    }

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
     * Gets the value of the daysLeft property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getDaysLeft() {
        return daysLeft;
    }

    /**
     * Sets the value of the daysLeft property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setDaysLeft(BigInteger value) {
        this.daysLeft = value;
    }

    /**
     * Gets the value of the dataDaysLeft property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getDataDaysLeft() {
        return dataDaysLeft;
    }

    /**
     * Sets the value of the dataDaysLeft property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setDataDaysLeft(BigInteger value) {
        this.dataDaysLeft = value;
    }

    /**
     * Gets the value of the licenceDaysLeft property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getLicenceDaysLeft() {
        return licenceDaysLeft;
    }

    /**
     * Sets the value of the licenceDaysLeft property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setLicenceDaysLeft(BigInteger value) {
        this.licenceDaysLeft = value;
    }

}
