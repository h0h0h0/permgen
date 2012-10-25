/* ----------------------------------------------------------------------------
 * QAS On Demand > (c) QAS Ltd > www.qas.com
 *
 * Common Classes > SearchResult.java
 * The results of a search
 * ----------------------------------------------------------------------------
 */
package com.qas.newmedia.internet.ondemand.product.proweb;

import com.qas.ondemand_2011_03.*;

/** Wrapper class to encapsulate data that may be returned by an initial search, i.e.
 * a Picklist and/or FormattedAddress & VerifyLevel (for verification searches)
 */
public class SearchResult {
    // ------------------------------------------------------------------------
    // public constants
    // ------------------------------------------------------------------------
	/** Verify level indicating that there was no verification upon the search. */
	public static final String None = VerifyLevelType.NONE.value();
	/** Verify level indicating that the search has verified correct. */
	public static final String Verified = VerifyLevelType.VERIFIED.value();
	/** Verify level indicating that the search produced a single deliverable match, but user confirmation is advised */
	public static final String InteractionRequired = VerifyLevelType.INTERACTION_REQUIRED.value();
	/** Verify level indicating that the address was matched to a partial address at the premise level.
	 * This implies that there is also a picklist associated with the partial address. */
	public static final String PremisesPartial = VerifyLevelType.PREMISES_PARTIAL.value();
	/** Verify level indicating that the address was matched to a partial address at the street level.
	 * This implies that there is also a picklist associated with the partial address. */
	public static final String StreetPartial = VerifyLevelType.STREET_PARTIAL.value();
	/** Verify level indicating that the search was ambiguous and matched multiple separate addresses. */
	public static final String Multiple = VerifyLevelType.MULTIPLE.value();
    /** Verify level indicating that the address was matched at the Place Level (Address Dr Specific Matching) */
    public static final String VerifyPlace = VerifyLevelType.VERIFIED_PLACE.value();
    /** Verify level indicating that the address was matched at the Street Level (Address Dr Specific Matching) */
    public static final String VerifyStreet = VerifyLevelType.VERIFIED_STREET.value();

    // ------------------------------------------------------------------------
    // private data
    // ------------------------------------------------------------------------
    private FormattedAddress    m_Address = null;
    private Picklist            m_Picklist = null;
    private String              m_VerifyLevel = null;

    /** Construct an instance from a SOAP layer object */
    public SearchResult(QASearchResult sr) {
        QAAddressType address = sr.getQAAddress();
        QAPicklistType picklist = sr.getQAPicklist();

        VerifyLevelType level = sr.getVerifyLevel();
        if (level != null) {
            m_VerifyLevel = level.value();
        }
        if (picklist != null) {
            m_Picklist = new Picklist(picklist);
        }
        if (address != null) {
            m_Address = new FormattedAddress(address);
        }
    }

    /** (Verification only) Returns the address, which may be null as this will only ever be returned by the
     * verification engine for certain types of results.
     */
    public FormattedAddress getAddress() {
        return m_Address;
    }

    /** Returns the picklist, which may be null depending upon the engine and number of results.
     * Picklists are returned when searching with the single-line engine,
     * and may be returned from the verification engine.
     */
    public Picklist getPicklist() {
        return m_Picklist;
    }

    /** (Verification only) Returns the verify level, which specifies how well the search has
     * been matched, and the appropriate action that can be taken upon the result.  */
    public String getVerifyLevel() {
        return m_VerifyLevel;
    }
}
