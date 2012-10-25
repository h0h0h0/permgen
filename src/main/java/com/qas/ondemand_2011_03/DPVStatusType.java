
package com.qas.ondemand_2011_03;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DPVStatusType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="DPVStatusType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="DPVNotConfigured"/>
 *     &lt;enumeration value="DPVConfigured"/>
 *     &lt;enumeration value="DPVConfirmed"/>
 *     &lt;enumeration value="DPVConfirmedMissingSec"/>
 *     &lt;enumeration value="DPVNotConfirmed"/>
 *     &lt;enumeration value="DPVLocked"/>
 *     &lt;enumeration value="DPVSeedHit"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "DPVStatusType")
@XmlEnum
public enum DPVStatusType {

    @XmlEnumValue("DPVNotConfigured")
    DPV_NOT_CONFIGURED("DPVNotConfigured"),
    @XmlEnumValue("DPVConfigured")
    DPV_CONFIGURED("DPVConfigured"),
    @XmlEnumValue("DPVConfirmed")
    DPV_CONFIRMED("DPVConfirmed"),
    @XmlEnumValue("DPVConfirmedMissingSec")
    DPV_CONFIRMED_MISSING_SEC("DPVConfirmedMissingSec"),
    @XmlEnumValue("DPVNotConfirmed")
    DPV_NOT_CONFIRMED("DPVNotConfirmed"),
    @XmlEnumValue("DPVLocked")
    DPV_LOCKED("DPVLocked"),
    @XmlEnumValue("DPVSeedHit")
    DPV_SEED_HIT("DPVSeedHit");
    private final String value;

    DPVStatusType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static DPVStatusType fromValue(String v) {
        for (DPVStatusType c: DPVStatusType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
