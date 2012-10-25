/* ----------------------------------------------------------------------------
 * QAS On Demand > (c) QAS Ltd > www.qas.com
 *
 * Common Classes > CanSearch.java
 * Details about searching availability
 * ----------------------------------------------------------------------------
 */
package com.qas.newmedia.internet.ondemand.product.proweb;
import com.qas.ondemand_2011_03.*;

/**
 * Wrapper class to encapsulate the result of a CanSearch operation:
 * searching availability, and the reasons when unavailable
 */
public class CanSearch {
    // ------------------------------------------------------------------------
    // private data
    // ------------------------------------------------------------------------
    private boolean m_Okay = false;
    private int m_QasErrorNumber = 0;
    private String m_Message = null;

    /** Construct an instance from a SOAP layer object */
    public CanSearch(QASearchOk cs) {
        m_Okay = cs.isIsOk();
        if (cs.getErrorCode() != null) {
            m_QasErrorNumber = Integer.parseInt(cs.getErrorCode());
        }
        if (cs.getErrorMessage() != null) {
            m_Message = cs.getErrorMessage() + " [" + m_QasErrorNumber + "]";
        }
    }

    /** Returns whether address searching is possible
     */
    public boolean isOk() {
        return m_Okay;
    }

    /** Returns the reason for failure
     */
    public String getMessage() {
        return m_Message;
    }

}
