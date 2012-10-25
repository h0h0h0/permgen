/* ----------------------------------------------------------------------------
 * QAS On Demand > (c) QAS Ltd > www.qas.com
 *
 * Common Classes > Picklist.java
 * Picklist details
 * ----------------------------------------------------------------------------
 */
package com.qas.newmedia.internet.ondemand.product.proweb;

import java.io.Serializable;
import java.util.List;

import com.qas.ondemand_2011_03.QAPicklistType;
import com.qas.ondemand_2011_03.PicklistEntryType;

/**
 * Wrapper class to encapsulate Picklist data.
 * This class defines a set of result items and properties that may be returned at certain stages in the search process.
 * The picklist will be sorted in the order in which is strongly advised to display the entries, from top to bottom.
 */
public class Picklist implements Serializable {
    // ------------------------------------------------------------------------
    // private data
    // ------------------------------------------------------------------------
    private int     m_Total;
    private String  m_Moniker;
    private String  m_Prompt;
    private PicklistItem[] m_Items;
    private boolean m_AutoStepinSafe;
    private boolean m_AutoStepinPastClose;
    private boolean m_AutoFormatSafe;
    private boolean m_AutoFormatPastClose;
    private boolean m_LargePotential;  // attribute
    private boolean m_MaxMatches;  // attribute
    private boolean m_MoreOtherMatches;  // attribute
    private boolean m_OverThreshold;  // attribute
    private boolean m_Timeout;  // attribute

    // ------------------------------------------------------------------------
    // public methods
    // ------------------------------------------------------------------------
    /** construct an instance from SOAP layer object */
    public Picklist(QAPicklistType p) {
        m_Total = p.getTotal().intValue();
        m_Moniker = p.getFullPicklistMoniker();
        m_Prompt = p.getPrompt();
        m_AutoStepinSafe = p.isAutoStepinSafe();
        m_AutoStepinPastClose = p.isAutoStepinPastClose();
        m_AutoFormatSafe = p.isAutoFormatSafe();
        m_AutoFormatPastClose = p.isAutoFormatPastClose();
        m_LargePotential = p.isLargePotential();
        m_MaxMatches = p.isMaxMatches();
        m_MoreOtherMatches = p.isMoreOtherMatches();
        m_OverThreshold = p.isOverThreshold();
        m_Timeout = p.isTimeout();

        // convert the lines in the picklist
		List<PicklistEntryType> aItems = p.getPicklistEntry();
        // check for null as we can have an empty picklist
        if (aItems != null) {
			int iSize = aItems.size();
            if (iSize > 0) {
                m_Items = new PicklistItem[iSize];
                for (int i=0; i < iSize; i++) {
					m_Items[i] = new PicklistItem(aItems.get(i));
                }
            }
        }
    }

    /** Returns the total number of matches (not including informationals)
     * This number should only be used for display purposes and not assumed as the size of the returned
     * picklist, which will often contain informational items and is restricted by a threshold.
     */
    public int getTotal() {
        return m_Total;
    }

    /** Returns the full picklist moniker, which is the moniker that can be used to replicate
     * the given picklist. Full picklist monikers are typically used when further refining a
     * picklist using search text to filter the results upon. Picklist refinement is performed using the
     * <code>refine</code> method of the <code>QuickAddress</code> class.*/
    public String getMoniker() {
        return m_Moniker;
    }

    /** Returns a short textual prompt that may be displayed to the user in an interactive scenario.
     * This prompts them upon what should be entered. For example “Enter building number/name or organisation”.
     */
    public String getPrompt() {
        return m_Prompt;
    }

    /** Returns the array of <code>PicklistItem</code> objects. Each picklist item represents a separate result,
     * and has associated information.
     */
    public PicklistItem[] getItems() {
        return m_Items;
    }

    /** Indicates a match to a single non-deliverable result that can be stepped.
     *  This signifies that it is advised that the integration should automatically step
     *  into the result without user interaction to aid address capture efficiency.
     * Stepping into address results is performed using the
     * <code>stepIn</code> method of the <code>QuickAddress</code> class.
     */
    public boolean isAutoStepinSafe() {
        return m_AutoStepinSafe;
    }

    /**  Indicates a match to a single non-deliverable result
     * that can be stepped into, but also other lesser matches.
     * This signifies that the integrator may choose to automatically step into the first picklist result without user
     * interaction to aid address capture efficiency. Stepping into address results is performed using the
     * <code>stepIn</code> method of the <code>QuickAddress</code> class.
     */
    public boolean isAutoStepinPastClose() {
        return m_AutoStepinPastClose;
    }

    /** Indicates whether the picklist contains a single, non-informational result that can be stepped into.
     * This signifies that the integration should automatically step into the first picklist result
     * without user interaction to aid address capture efficiency. Stepping into address results is performed using the
     * <code>stepIn</code> method of the <code>QuickAddress</code> class.
     */
    public boolean isAutoStepinSingle() {
        return getItems().length == 1
                && getItems()[0].canStep()
                && !getItems()[0].isInformation();
    }

    /** Indicates a match to a single deliverable result.
     * This signifies that it is advised that the integration may automatically format the result without
     * user interaction to aid address capture efficiency.  Formatting address results is performed using the
     * <code>getFormattedAddress</code> method of the <code>QuickAddress</code> class.
     */
    public boolean isAutoFormatSafe() {
        return m_AutoFormatSafe;
    }

    /** Indicates a match to a single deliverable result, but also other lesser matches.
     * This signifies that the integrator may choose to automatically format the first picklist result
     * without user interaction to aid address efficiency.Formatting address results is performed using the
     * <code>getFormattedAddress</code> method of the <code>QuickAddress</code> class.
     */
    public boolean isAutoFormatPastClose() {
        return m_AutoFormatPastClose;
    }

    /** Indicates that the picklist contains a single final address item.
     * This signifies that the integration should automatically format the first picklist result
     * without user interaction to aid address efficiency. Formatting address results is performed using the
     * <code>getFormattedAddress</code> method of the <code>QuickAddress</code> class.
     */
    public boolean isAutoFormatSingle() {
        return getItems().length == 1
                && getItems()[0].isFullAddress()
                && !getItems()[0].isInformation();
    }

    /** Indicates that the picklist potentially contains too many items to display.
     * This signifies that further refinement is required before a picklist containing
     * all of the results can be returned. Picklist refinement is performed using the
     * <code>refine</code> method of the <code>QuickAddress</code> class.
     */
    public boolean isLargePotential() {
        return m_LargePotential;
    }

    /** Indicates that the number of matches exceeded the maximum allowed.
     * This signifies that the search was too broad to match, and that a
     * new search should be performed with more information specified. */
    public boolean isMaxMatches() {
        return m_MaxMatches;
    }

    /** Indicates that there are additional other matches that can be displayed, i.e.
     * the number of results is in excess of the picklist threshold, but a subset of the
     * matches has been returned in the picklist, and an informational picklist item that
     * allows the user to access the others. */
    public boolean isMoreOtherMatches() {
        return m_MoreOtherMatches;
    }

    /** Indicates that the number of matches exceeded the threshold and only a single informational
     * picklist item that allows the user to access the full results has been retuurned. */
    public boolean isOverThreshold() {
        return m_OverThreshold;
    }

    /** Indicates that the search timed out.
     * If this is unexpected for the given search, this could either signify that the timeout is set too low,
     * or that the search was too broad.
     */
    public boolean isTimeout() {
        return m_Timeout;
    }
}
