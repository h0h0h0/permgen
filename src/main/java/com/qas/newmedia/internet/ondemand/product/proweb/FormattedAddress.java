/* ----------------------------------------------------------------------------
 * QAS On Demand > (c) QAS Ltd > www.qas.com
 *
 * Common Classes > FormattedAddress.java
 * Formatted address details
 * ----------------------------------------------------------------------------
 */
package com.qas.newmedia.internet.ondemand.product.proweb;

import java.io.Serializable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.qas.newmedia.internet.ondemand.product.proweb.servlet.Constants;
import com.qas.ondemand_2011_03.DPVStatusType;
import com.qas.ondemand_2011_03.QAAddressType;
import com.qas.ondemand_2011_03.AddressLineType;

/**
 *  Wrapper class to encapsulate data associated with a final formatted address, namely
 * an array of AddressLines plus some flags.
 */
public class FormattedAddress implements Serializable {
    // ------------------------------------------------------------------------
    // private data
    // ------------------------------------------------------------------------
    private AddressLine[]  m_AddressLines;
    private boolean       m_IsOverflow;
    private boolean       m_IsTruncated;
    private DPVStatusType m_DPVStatus;
    // ------------------------------------------------------------------------
    // public methods
    // ------------------------------------------------------------------------
    /** construct instance from SOAP layer object */
    public FormattedAddress(QAAddressType t) {
        m_IsOverflow = t.isOverflow();
        m_IsTruncated = t.isTruncated();
        m_DPVStatus = t.getDPVStatus();

		List<AddressLineType> aLines = t.getAddressLine();
        // we must have lines in an address so aLines should never be null
		int iSize = aLines.size();
        if (iSize > 0) {
            m_AddressLines = new AddressLine[iSize];
            for (int i=0; i < iSize; i++) {
				m_AddressLines[i] = new AddressLine(aLines.get(i));
            }
        }
    }

    public int getLength() {
        if (m_AddressLines == null)
            return 0;
        else
            return m_AddressLines.length;
    }
    /** Returns the array of individual formatted address line objects */
    public AddressLine[] getAddressLines() {
        return m_AddressLines;
    }

    public void setAddressLines(AddressLine a, int i) {
        m_AddressLines[i] = a;
    }

    public AddressLine getAddressLines(int i) {
        return m_AddressLines[i];
    }

    /** Flag that indicates that there were not enough layout lines to contain
     * all of the address line, and that some elements had to overflow onto other
     * lines.  If this is <code>true</code>, then the integrator should either
     * add more output address lines in the specified layout,
     * or specify larger widths (server configuration). */
    public boolean isOverflow() {
        return m_IsOverflow;
    }

    /** Flag that indicates that some of the address lines were too short to accommodate
     * all of the formatted address, and so truncation has occurred. If this is <code>true</code>,
     * then you should either add more output address lines in the specified layout,
     * or specify larger widths (server configuration). */
    public boolean isTruncated() {
        return m_IsTruncated;
    }

    /** Flags that indicate the Status of DPV */
    public DPVStatusType getDPVStatus()
    {
        return m_DPVStatus;
    }

    public void setDpvMessage(HttpServletRequest request, DPVStatusType dpvStatus)
    {
    	String dpvStatusString;
        switch (dpvStatus)
        {
            case DPV_CONFIRMED:
                dpvStatusString = "DPV validated";
                break;
            case DPV_NOT_CONFIRMED:
            	dpvStatusString = "WARNING - DPV not validated";
                break;
            case DPV_CONFIRMED_MISSING_SEC:
            	dpvStatusString = "DPV validated but secondary number incorrect or missing";
                break;
            case DPV_LOCKED:
            	dpvStatusString = "WARNING - DPV validation locked";
                break;
            case DPV_SEED_HIT:
            	dpvStatusString = "WARNING - DPV - Seed address hit";
                break;
            default:
            	dpvStatusString = "";
                break;
        }
        request.setAttribute(Constants.FIELD_DPVSTATUS, dpvStatusString);
   }
}
