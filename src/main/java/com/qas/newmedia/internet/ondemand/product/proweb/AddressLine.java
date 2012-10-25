/* ----------------------------------------------------------------------------
 * QAS On Demand > (c) QAS Ltd > www.qas.com
 *
 * Common Classes > AddressLine.java
 * Address line details
 * ----------------------------------------------------------------------------
 */
package com.qas.newmedia.internet.ondemand.product.proweb;

import java.io.Serializable;
import java.util.List;

import com.qas.ondemand_2011_03.*;

/**
 * Wrapper class to encapsulates data associated with an address line,
 * namely the formatted text itself plus various flags.
 */
public class AddressLine implements Serializable {
    // ------------------------------------------------------------------------
    // public constants
    // ------------------------------------------------------------------------
	/** Constant to indicate that there was no content specified for this line */
	public static final String NONE     = LineContentType.NONE.value();
	/** Constant to indicate that there are address elements specified upon this line */
	public static final String ADDRESS  = LineContentType.ADDRESS.value();
	/** Constant to indicate that there is name information specified upon this line */
	public static final String NAME     = LineContentType.NAME.value();
	/** Constant to indicate that there is ancillary data specified upon this line */
	public static final String ANCILLARY = LineContentType.ANCILLARY.value();
	/** Constant to indicate that there is DataPlus information specified upon this line */
	public static final String DATAPLUS = LineContentType.DATA_PLUS.value();


    // ------------------------------------------------------------------------
    // private data
    // ------------------------------------------------------------------------
    private String  m_Label;
    private String  m_Line;
    private String  m_LineType;
    private List<DataplusGroupType> m_DataplusGroup;
    private boolean m_IsTruncated;
    private boolean m_IsOverflow;

    // ------------------------------------------------------------------------
    // public methods
    // ------------------------------------------------------------------------
    /** construct instance from SOAP layer object */
    public AddressLine(AddressLineType t) {
        m_Label = t.getLabel();
        m_Line = t.getLine();
        m_LineType = t.getLineContent().toString();
        m_IsTruncated = t.isTruncated();
        m_IsOverflow = t.isOverflow();
        m_DataplusGroup = t.getDataplusGroup();
    }

    public List<DataplusGroupType> getDataplusGroup() {
        return m_DataplusGroup;
    }

    /** Returns any textual label for the line, which describes the contents of the line. For example “Town”.
     * Line labels may not be returned if multiple elements are fixed to a single line in the layout.
     */
    public String getLabel() {
        return m_Label;
    }

    /** Returns the final formatted address line,
     * as described by the layout that was used to format the address result.
     */
    public String getLine() {
        return m_Line;
    }

    /** Returns the line type as enumerated by the constants above.  For example NAME.*/
    public String getLineType() {
        return m_LineType;
    }

    /** Flag that indicates that some of the address lines were too short to accommodate
     * all of the formatted address, and so truncation has occurred. If this is <code>true</code>,
     * then you should either add more output address lines in the specified layout,
     * or specify larger widths (server configuration). */
    public boolean isTruncated() {
        return m_IsTruncated;
    }

    /** Flag that indicates that there were not enough layout lines to contain
     * all of the address line, and that some elements had to overflow onto other
     * lines.  If this is <code>true</code>, then the integrator should either
     * add more output address lines in the specified layout,
     * or specify larger widths (server configuration).
     */
    public boolean isOverflow() {
        return m_IsOverflow;
    }
}
