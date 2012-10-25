
package com.qas.ondemand_2011_03;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.oasis_open.docs.wss._2004._01.oasis_200401_wss_wssecurity_secext_1_0.SecurityHeaderType;


/**
 * <p>Java class for QAQueryHeader complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="QAQueryHeader">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="QAAuthentication" type="{http://www.qas.com/OnDemand-2011-03}QAAuthentication"/>
 *         &lt;element name="Security" type="{http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd}SecurityHeaderType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "QAQueryHeader", propOrder = {
    "qaAuthentication",
    "security"
})
public class QAQueryHeader {

    @XmlElement(name = "QAAuthentication", required = true)
    protected QAAuthentication qaAuthentication;
    @XmlElement(name = "Security", required = true)
    protected SecurityHeaderType security;

    /**
     * Gets the value of the qaAuthentication property.
     * 
     * @return
     *     possible object is
     *     {@link QAAuthentication }
     *     
     */
    public QAAuthentication getQAAuthentication() {
        return qaAuthentication;
    }

    /**
     * Sets the value of the qaAuthentication property.
     * 
     * @param value
     *     allowed object is
     *     {@link QAAuthentication }
     *     
     */
    public void setQAAuthentication(QAAuthentication value) {
        this.qaAuthentication = value;
    }

    /**
     * Gets the value of the security property.
     * 
     * @return
     *     possible object is
     *     {@link SecurityHeaderType }
     *     
     */
    public SecurityHeaderType getSecurity() {
        return security;
    }

    /**
     * Sets the value of the security property.
     * 
     * @param value
     *     allowed object is
     *     {@link SecurityHeaderType }
     *     
     */
    public void setSecurity(SecurityHeaderType value) {
        this.security = value;
    }

}
