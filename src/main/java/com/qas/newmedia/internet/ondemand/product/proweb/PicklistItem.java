/* ----------------------------------------------------------------------------
 * QAS On Demand > (c) QAS Ltd > www.qas.com
 *
 * Common Classes > PicklistItem.java
 * Picklist item details
 * ----------------------------------------------------------------------------
 */
package com.qas.newmedia.internet.ondemand.product.proweb;

import java.io.Serializable;
import com.qas.ondemand_2011_03.PicklistEntryType;

/**
 * Wrapper class to encapsulate the data associated with one entry in a picklist,
 * which may be related to an address, another picklist or an informational item.
 */
public class PicklistItem implements Serializable {
    // ------------------------------------------------------------------------
    // private data
    // ------------------------------------------------------------------------
    private String  m_Text;
    private String  m_PartialAddress;
    private String  m_Postcode;
    private int     m_Score; // non-negative
    private String  m_Moniker;
    private boolean m_IsFullAddress;
    private boolean m_CanStep;
    private boolean m_IsAliasMatch;
    private boolean m_IsPostcodeRecoded;
    private boolean m_IsCrossBorderMatch;
    private boolean m_IsDummyPOBox;
    private boolean m_IsName;
    private boolean m_IsInformation;
    private boolean m_IsWarnInformation;
    private boolean m_IsIncompleteAddress;
    private boolean m_IsUnresolvableRange;
    private boolean m_IsPhantomPrimaryPoint;
    private boolean m_IsMultiples;

    // ------------------------------------------------------------------------
    // public methods
    // ------------------------------------------------------------------------
    /** construct an instance from SOAP layer object */
    public PicklistItem(PicklistEntryType t) {
        m_Text = t.getPicklist();
        m_Postcode = t.getPostcode();
        m_Score = t.getScore().intValue();
        m_Moniker = t.getMoniker();
        m_PartialAddress = t.getPartialAddress();
        // properties
        m_IsFullAddress = t.isFullAddress();
        m_CanStep = t.isCanStep();
        m_IsAliasMatch = t.isAliasMatch();
        m_IsPostcodeRecoded = t.isPostcodeRecoded();
        m_IsCrossBorderMatch = t.isCrossBorderMatch();
        m_IsDummyPOBox = t.isDummyPOBox();
        m_IsName = t.isName();
        m_IsInformation = t.isInformation();
        m_IsWarnInformation = t.isWarnInformation();
        m_IsIncompleteAddress = t.isIncompleteAddr();
        m_IsUnresolvableRange = t.isUnresolvableRange();
        m_IsPhantomPrimaryPoint = t.isPhantomPrimaryPoint();
        m_IsMultiples = t.isMultiples();
    }

    /** Returns the picklist text for display.
     * This should be combined with the <code>getPostcode</code> method.
     * The main picklist text and postcode have been separated to ease
     * integration formatting for display.
     */
    public String getText() {
        return m_Text;
    }

    /** Returns the postcode for display; may be empty.
     * This will only contain the postcode for an address where it is suitable
     * for display purposes only. It should not be assumed that this element
     * can be used to determine the postcode of any picklist entry
     */
    public String getPostcode() {
        return m_Postcode;
    }

    /** Returns the percentage score of this item; 0 if not applicable
     * This can be used to display to the user as a guide of the quality of match produced. */
    public int getScore() {
        return m_Score;
    }

    /** Returns the moniker representing this item.
     * The moniker can be used to perform actions upon the picklist entry, such as stepping in
     * (using the <code>stepIn</code> method of the <code>QuickAddress</code> class)
     * or formatting the item into a final address
     * (using the <code>getFormattedAddress</code> method of the <code>QuickAddress</code> class).
     */
    public String getMoniker() {
        return m_Moniker;
    }

    /** Returns the full address details captured thus far.
     * The partial address is the item partially formatted into a single string,
     * which may be useful for display in user interactive environments.
     * This string is not suitable to be displayed as the picklist text,
     * for which the <code>getText</code> method must be used instead.
     */
    public String getPartialAddress() {
        return m_PartialAddress;
    }

    /** Indicates whether this item represents a full deliverable address.
     * If the user selects this picklist item, then it should be formatted into a final address,
     * signifying the end of the address capture process.
     * Formatting picklist items is performed using the <code>getFormattedAddress</code> method of the <code>QuickAddress</code> class
     */
    public boolean isFullAddress() {
        return m_IsFullAddress;
    }

    /** Indicates whether the item may be stepped into to produce a new picklist with more detail.
     * For example, a picklist entry that represents a street can be stepped into to produce a new
     * picklist of premises within the street.
     * Stepping into picklist items is performed using the <code>stepIn</code> method of the <code>QuickAddress</code> class.
     * This only has significance when searching when <code>QuickAddress.setFlatten(false)</code> has been called.
     */
    public boolean canStep() {
        return m_CanStep;
    }

    /** Indicates whether this entry is an alias match
     * This is just to allow the integrator to display the picklist entry with a different icon
     * to a non-alias match, if they so choose. */
    public boolean isAliasMatch() {
        return m_IsAliasMatch;
    }

    /** Indicates that this item was searched against a postcode, and that the match was made
     * to a newer recoded version of the postcode
     */
    public boolean isPostcodeRecoded() {
        return m_IsPostcodeRecoded;
    }

    /** Indicates whether this entry is a dummy, i.e. the picklist
     * item does not contain all of the information required to make it deliverable.
     * This is commonly returned when searching against datasets that do not contain premise information.
     * In this case, the integration should prompt the user for further information that will be passed
     * to the <code>refine</code> method of the <code>QuickAddress</code> class in order to get a
     * deliverable address.
     */
    public boolean isIncompleteAddress() {
        return m_IsIncompleteAddress;
    }

    /** Indicates whether this item is a range dummy (when exact premise numbers unknown), i.e.
     * it represents a range of premises which cannot be expanded, due to no information about
     * the possible elements within the range.
     * In this case, the integration should prompt the user for further information that will be passed
     * to the <code>refine</code> method of the <code>QuickAddress</code> class in order to get a
     * deliverable address.
     */
    public boolean isUnresolvableRange() {
        return m_IsUnresolvableRange;
    }

    /** Indicates whether this item represents a nearby area,
     * outside the strict initial boundaries of the search.
     * CrossBorderMatches are commonly returned from bordering locality matches
     * in the AUS dataset, as specified by AMAS certification.
     */
    public boolean isCrossBorderMatch() {
        return m_IsCrossBorderMatch;
    }

    /** Indicates whether this item is the dummy PO Box, i.e. it represents the set of PoBoxes.
     * If this item is stepped into, the resulting picklist will contain all PoBoxes.
     * This allows the integrator to display the picklist item with a different icon to a normal match, if they so choose.
     * This only has significance when searching when <code>QuickAddress.setFlatten(false)</code> has been called.
     * */
    public boolean isDummyPOBox() {
        return m_IsDummyPOBox;
    }

    /** Indicates whether this item is a Names result.
     * This signifies that the picklist item contains name information, allowing the
     * integrator to display the picklist item with a different icon to a non-name match, if they so choose.
     * This only has significance when searching against datasets that contain name information,
     * such as GBN.
     */
    public boolean isName() {
        return m_IsName;
    }

    /** Indicates whether this item is an informational prompt rather than an address.
     * This means that the item does not correspond to any particular address item.
     * Informational picklist entries are designed to aid the user complete the address capture process,
     * and must be displayed in the picklist: this attribute allows the integrator to display the
     * picklist entry with a different icon to a non-informational entry, if they so choose.
     * This only has significance when searching when <code>QuickAddress.setFlatten(false)</code> has been called.
     */
    public boolean isInformation() {
        return m_IsInformation;
    }

    /** Indicates whether this itemis a warning informational item.
     * Unlike a normal informational item, this will usually be displayed in the picklist
     * when the user should be warned of a situation (such as a search yielding no matches).
     * This attribute allows the integrator to display the picklist item with a different
     * icon to a non-informational entry, if they so choose.
     * This only has significance when searching when <code>QuickAddress.setFlatten(false)</code> has been called.
     */
    public boolean isWarnInformation() {
        return m_IsWarnInformation;
    }

    /** Indicates whether this item is a phantom primary point.
     * This only has significance when searching against the AUS dataset.
     * Phantom primary points are undeliverable premises where there is incomplete information
     * about subpremises beneath the premise. If the user selects a picklist item with this
     * attribute then the integration must prompt the user for the subpremise information and
     * pass this to the <code>refine</code> method of the <code>QuickAddress</code> class in order to get a
     * deliverable address.
     */
    public boolean isPhantomPrimaryPoint() {
        return m_IsPhantomPrimaryPoint;
    }

    /** Indicates that the picklist item represents multiple addresses, merged into a single entry.
     * This element is solely to allow the integrator to display the picklist entry with a different
     * icon than a non-multiple entry, if they so choose.
     */
    public boolean isMultiples() {
        return m_IsMultiples;
    }
}
