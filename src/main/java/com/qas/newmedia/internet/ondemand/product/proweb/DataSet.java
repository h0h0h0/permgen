/* ----------------------------------------------------------------------------
 * QAS On Demand > (c) QAS Ltd > www.qas.com
 *
 * Common Classes > DataSet.java
 * Data set details
 * ----------------------------------------------------------------------------
 */
package com.qas.newmedia.internet.ondemand.product.proweb;

import java.io.Serializable;
import java.util.List;

import com.qas.ondemand_2011_03.QADataSet;

/**
 * Wrapper class to encapsulate the name & id of a data set that may be searched on
 * by ProWeb.
 */
public class DataSet implements Serializable, Comparable {
    // Member data
    private String m_ID      = null;
    private String m_Name    = null;

    // Member methods

    /** construct instance from SOAP layer object */
    public DataSet(QADataSet d) {
        m_ID = d.getID().toString();
        m_Name = d.getName();
    }

    /** create array from SOAP layer array */
    public static DataSet[] createArray(List<QADataSet> lDataSets) {
        DataSet[] results = null;
        if (lDataSets != null) {
            int iSize = lDataSets.size();
            if (iSize > 0) {
                results = new DataSet[iSize];
                for (int i=0; i < iSize; i++) {
                    results[i] = new DataSet(lDataSets.get(i));
                }
            }
        }
        return results;
    }

    /** Returns the textual name of the dataset that can be displayed to users,
     * if you wish to have interactive selection of the dataset to search against.
     * For example, "Australia".
     * This string is defined within the InstalledCountries server configuration setting.
     */
    public String getName() {
        return m_Name;
    }

    /** Returns the data set ISO identifier. This is the string that should be passed in
     * to QuickAddress searching methods. For example, "AUS". */
    public String getID() {
        return m_ID;
    }

    /** Helper returns the data set with the given ID, or null if not found */
    public static DataSet findByID(DataSet[] array, String sID) {
        if (array != null) {
            for (int i=0; i < array.length; i++) {
                if (array[i].getID().equals(sID)) {
                    return array[i];
                }
            }
        }
        return null;
    }

    public int compareTo(Object otherObject) {
        DataSet other = (DataSet)otherObject;
        return getName().compareTo(other.getName());
    }
}
