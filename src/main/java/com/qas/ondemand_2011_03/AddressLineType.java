
package com.qas.ondemand_2011_03;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Truncated        : Truncation occurred on this line
 * 
 * <p>Java class for AddressLineType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AddressLineType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Label" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Line" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DataplusGroup" type="{http://www.qas.com/OnDemand-2011-03}DataplusGroupType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="LineContent" type="{http://www.qas.com/OnDemand-2011-03}LineContentType" default="Address" />
 *       &lt;attribute name="Overflow" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="Truncated" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AddressLineType", propOrder = {
    "label",
    "line",
    "dataplusGroup"
})
public class AddressLineType {

    @XmlElement(name = "Label")
    protected String label;
    @XmlElement(name = "Line")
    protected String line;
    @XmlElement(name = "DataplusGroup")
    protected List<DataplusGroupType> dataplusGroup;
    @XmlAttribute(name = "LineContent")
    protected LineContentType lineContent;
    @XmlAttribute(name = "Overflow")
    protected Boolean overflow;
    @XmlAttribute(name = "Truncated")
    protected Boolean truncated;

    /**
     * Gets the value of the label property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLabel() {
        return label;
    }

    /**
     * Sets the value of the label property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLabel(String value) {
        this.label = value;
    }

    /**
     * Gets the value of the line property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLine() {
        return line;
    }

    /**
     * Sets the value of the line property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLine(String value) {
        this.line = value;
    }

    /**
     * Gets the value of the dataplusGroup property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the dataplusGroup property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDataplusGroup().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DataplusGroupType }
     * 
     * 
     */
    public List<DataplusGroupType> getDataplusGroup() {
        if (dataplusGroup == null) {
            dataplusGroup = new ArrayList<DataplusGroupType>();
        }
        return this.dataplusGroup;
    }

    /**
     * Gets the value of the lineContent property.
     * 
     * @return
     *     possible object is
     *     {@link LineContentType }
     *     
     */
    public LineContentType getLineContent() {
        if (lineContent == null) {
            return LineContentType.ADDRESS;
        } else {
            return lineContent;
        }
    }

    /**
     * Sets the value of the lineContent property.
     * 
     * @param value
     *     allowed object is
     *     {@link LineContentType }
     *     
     */
    public void setLineContent(LineContentType value) {
        this.lineContent = value;
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

}
