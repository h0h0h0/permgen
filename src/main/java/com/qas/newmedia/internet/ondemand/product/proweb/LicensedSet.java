/* ----------------------------------------------------------------------------
 * QAS On Demand > (c) QAS Ltd > www.qas.com
 *
 * Common Classes > LicensedSet.java
 * Data licencing details
 * ----------------------------------------------------------------------------
 */
package com.qas.newmedia.internet.ondemand.product.proweb;

import java.io.Serializable;

import com.qas.ondemand_2011_03.*;
import java.util.*;

/**
 * Wrapper class that gives licensing information about a specific data file.
 * This info is intended for integrators/administrators rather than users.
 * <code>LicensedSet</code>s also differ from <code>DataSet</code>s in that the former may relate to any data set
 * (including DataPlus), rather than just those data sets that may be searched against.
 */
public class LicensedSet implements Serializable {
    // ------------------------------------------------------------------------
    // public constants
    // enumeration of warning levels that can be returned
    // ------------------------------------------------------------------------
	/** Warning level constant indicating there is nothing to be warned about the data file */
	public static final String NONE = com.qas.ondemand_2011_03.LicenceWarningLevel.NONE.value();
	/** Warning level constant indicating that the data file is close to its expiry date.
	 * The days before this warning level is returned can be controlled using a server configuration setting */
	public static final String DATA_EXPIRING = LicenceWarningLevel.DATA_EXPIRING.value();
	/** Warning level constant indicating that the license that controls the usage of the data file
	 * is close to its expiry date. The days before this warning level is returned can be controlled using
	 * a server configuration setting */
	public static final String LICENCE_EXPIRING = LicenceWarningLevel.LICENCE_EXPIRING.value();
	/** Warning level constant indicating that there are few clicks left on the meter for this data set */
	public static final String CLICKS_LOW = LicenceWarningLevel.CLICKS_LOW.value();
	/** Warning level constant indicating that this is an evaluation-only licence */
	public static final String EVALUATION = LicenceWarningLevel.EVALUATION.value();
	/** Warning level constant indicating that there are no more clicks left on the meter for this data set */
	public static final String NO_CLICKS = LicenceWarningLevel.NO_CLICKS.value();
	/** Warning level constant indicating that the data file has passed the expiry date and so cannot be used */
	public static final String DATA_EXPIRED = LicenceWarningLevel.DATA_EXPIRED.value();
	/** Warning level constant indicating that the evaluation license which controls the use of the data set
	 * has expired */
	public static final String EVAL_LICENCE_EXPIRED = LicenceWarningLevel.EVAL_LICENCE_EXPIRED.value();
	/** Warning level constant indicating that the full (non-evaluation) license which controls the use of the
	 * data set has expired */
	public static final String FULL_LICENCE_EXPIRED = LicenceWarningLevel.FULL_LICENCE_EXPIRED.value();
	/** Warning level constant indicating that the product is unable to locate a license for one
	 * of the datasets, and so it cannot be used */
	public static final String LICENCE_NOT_FOUND = LicenceWarningLevel.LICENCE_NOT_FOUND.value();
	/** Warning level constant indicating that a data file cannot be opened or read, and so is unusable */
	public static final String DATA_UNREADABLE = LicenceWarningLevel.DATA_UNREADABLE.value();

    public static final String [] LICENCE_ORDER = {NONE, DATA_EXPIRING, LICENCE_EXPIRING, CLICKS_LOW, EVALUATION, NO_CLICKS, DATA_EXPIRED,EVAL_LICENCE_EXPIRED,FULL_LICENCE_EXPIRED,LICENCE_NOT_FOUND,DATA_UNREADABLE};

    // ------------------------------------------------------------------------
    // private data
    // ------------------------------------------------------------------------
    private String m_ID;
    private String m_Description;
    private String m_Copyright;
    private String m_Version;
    private String m_BaseCountry;
    private String m_Status;
    private String m_Server;
    private String m_WarningLevel; // enumeration
    private int m_DaysLeft; // non-negative
    private int m_DataDaysLeft; // non-negative
    private int m_LicenceDaysLeft; // non-negative

    // ------------------------------------------------------------------------
    // public methods
    // ------------------------------------------------------------------------
    /** construct from SOAP layer object */

    public LicensedSet(QALicensedSet s) {
        m_ID = s.getID();
        m_Description = s.getDescription();
        m_Copyright = s.getCopyright();
        m_Version = s.getVersion();
        m_BaseCountry = s.getBaseCountry();
        m_Status = s.getStatus();
        m_Server = s.getServer();
        m_WarningLevel = s.getWarningLevel().toString();
        m_DaysLeft = s.getDaysLeft().intValue();
        m_DataDaysLeft = s.getDataDaysLeft().intValue();
        m_LicenceDaysLeft = s.getLicenceDaysLeft().intValue();
    }
    /**
     * Create array of objects given a soap layer aggregating object.
     * This is required because there is no type mapped for QALicenceInfo,
     * and consequently the QALicenceInfo.warningLevel member data is lost in the mapping.
     */
    public static LicensedSet[] createArray(QALicenceInfo info) {
        LicensedSet[] results = null;
		List<QALicensedSet> aLics = info.getLicensedSet();
        if (aLics != null) {
			int iSize = aLics.size();
            if (iSize > 0) {
                results = new LicensedSet[iSize];
                for (int i=0; i < iSize; i++) {
					results[i] = new LicensedSet(aLics.get(i));
                }
            }
        }
        return results;
    }


    public static LicensedSet[] createArray(QADataMapDetail info) {
        LicensedSet[] aResults = null;
        List<QALicensedSet> aInfo = info.getLicensedSet();

        if ( aInfo != null ) {
            if ( aInfo.size() > 0 ) {
                aResults = new LicensedSet[aInfo.size()];

                for ( int i = 0; i < aInfo.size(); ++i ) {
                    aResults[i] = new LicensedSet(aInfo.get(i));
                }
            }
        }

        return aResults;
    }

    /** returns a short identifier for the dataset.
     * For example “AUSMOS” for the Australian Mosaic DataPlus set */
    public String getID() {
        return m_ID;
    }

    /** returns a textual description of the dataset.
     * For example, “Australia MOSAIC code” for the Australian Mosaic DataPlus set */
    public String getDescription() {
        return m_Description;
    }

    /** returns a textual copyright message for the data */
    public String getCopyright() {
        return m_Copyright;
    }

    /** returns a textual description of the data version.
     * For example, “01 2004 (PAF v2004.3)” */
    public String getVersion() {
        return m_Version;
    }

    /** returns a short identifier for the base country (corresponding to a DataSet ID)
     * This may be useful for grouping the license information for display purposes */
    public String getBaseCountry() {
        return m_BaseCountry;
    }

    /** returns a textual description of the data status, with respect to license and expiry information */
    public String getStatus() {
        return m_Status;
    }

    /** returns the server name where the data is situated */
    public String getServer() {
        return m_Server;
    }

    /** returns the warning level (one of the constants above) for the data set.
     * A warning level is returned for data sets that have potential issues */
    public String getWarningLevel() {
        return m_WarningLevel;
    }

    /** returns the number of days before the data is unusable.
     * This is a combination of the two values DataDaysLeft and LicenceDaysLeft */
    public int getDaysLeft() {
        return m_DaysLeft;
    }

    /** returns the number of days before the data set expires */
    public int getDataDaysLeft() {
        return m_DataDaysLeft;
    }

    /** returns the number of data before the license that controls the data file expires */
    public int getLicenceDaysLeft() {
        return m_LicenceDaysLeft;
    }

    public int getValue(String key) {
        for (int i = 0; i < LICENCE_ORDER.length; i++) {
            if (LICENCE_ORDER [i].equals(key))
                return i;
        }
        return -1;
    }
}
