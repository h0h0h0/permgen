
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
 *         &lt;element name="Country" type="{http://www.qas.com/OnDemand-2011-03}DataIDType"/>
 *         &lt;element name="Engine" type="{http://www.qas.com/OnDemand-2011-03}EngineType"/>
 *         &lt;element name="Layout" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Search" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="FormattedAddressInPicklist" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="Localisation" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="RequestTag" type="{http://www.qas.com/OnDemand-2011-03}RequestTagType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "country",
    "engine",
    "layout",
    "search",
    "formattedAddressInPicklist"
})
@XmlRootElement(name = "QASearch")
public class QASearch {

    @XmlElement(name = "Country", required = true)
    protected String country;
    @XmlElement(name = "Engine", required = true)
    protected EngineType engine;
    @XmlElement(name = "Layout")
    protected String layout;
    @XmlElement(name = "Search", required = true)
    protected String search;
    @XmlElement(name = "FormattedAddressInPicklist", defaultValue = "false")
    protected Boolean formattedAddressInPicklist;
    @XmlAttribute(name = "Localisation")
    protected String localisation;
    @XmlAttribute(name = "RequestTag")
    protected String requestTag;

    /**
     * Gets the value of the country property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets the value of the country property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCountry(String value) {
        this.country = value;
    }

    /**
     * Gets the value of the engine property.
     * 
     * @return
     *     possible object is
     *     {@link EngineType }
     *     
     */
    public EngineType getEngine() {
        return engine;
    }

    /**
     * Sets the value of the engine property.
     * 
     * @param value
     *     allowed object is
     *     {@link EngineType }
     *     
     */
    public void setEngine(EngineType value) {
        this.engine = value;
    }

    /**
     * Gets the value of the layout property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLayout() {
        return layout;
    }

    /**
     * Sets the value of the layout property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLayout(String value) {
        this.layout = value;
    }

    /**
     * Gets the value of the search property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSearch() {
        return search;
    }

    /**
     * Sets the value of the search property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSearch(String value) {
        this.search = value;
    }

    /**
     * Gets the value of the formattedAddressInPicklist property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isFormattedAddressInPicklist() {
        return formattedAddressInPicklist;
    }

    /**
     * Sets the value of the formattedAddressInPicklist property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setFormattedAddressInPicklist(Boolean value) {
        this.formattedAddressInPicklist = value;
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

    /**
     * Gets the value of the requestTag property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRequestTag() {
        return requestTag;
    }

    /**
     * Sets the value of the requestTag property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRequestTag(String value) {
        this.requestTag = value;
    }

}
