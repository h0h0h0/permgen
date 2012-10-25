/* ----------------------------------------------------------------------------
 * QAS On Demand > (c) QAS Ltd > www.qas.com
 *
 * Common Classes > Layout.java
 * Layout details (for address formatting)
 * ----------------------------------------------------------------------------
 */
package com.qas.newmedia.internet.ondemand.product.proweb;

import java.io.Serializable;
import com.qas.ondemand_2011_03.QALayout;

/**
 * Wrapper class to encapsulate data that identifies a layout to be used in formatting a final address.
 * The layouts that are relevant for a particular <code>DataSet</code> are fully specified in
 * the server configuration file.
 */
public class Layout implements Serializable {
    // Member data
    private String m_Name        = null;
    private String m_Comment     = null;

    // Member methods
    /** construct instance from soap layer object */
    public Layout(QALayout l) {
        m_Name = l.getName();
        m_Comment = l.getComment();
    }

    /** create array from SOAP layer array */
    public static Layout[] createArray(QALayout[] aLayouts) {
        Layout[] results = null;
        if (aLayouts != null) {
            int iSize = aLayouts.length;
            if (iSize > 0) {
                results = new Layout[iSize];
                for (int i=0; i < iSize; i++) {
                    results[i] = new Layout(aLayouts[i]);
                }
            }
        }
        return results;
    }

    /** Returns the name of a layout.
     * It is the layout name that needs to be passed to the QuickAddress class to format a picklist entry. */
    public String getName() {
        return m_Name;
    }

    /** Returns a description of the layout.  */
    public String getComment() {
        return m_Comment;
    }

    /** Helper returns the layout with the given name, or null if not found */
    public static Layout findByName(Layout[] aLayouts, String sLayoutName) {
        if (aLayouts != null) {
            for (int i=0; i < aLayouts.length; i++) {
                if (aLayouts[i].getName().equals(sLayoutName)) {
                    return aLayouts[i];
                }
            }
        }
        return null;
    }
}
