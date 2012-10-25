/* ----------------------------------------------------------------------------
 * QAS On Demand > (c) QAS Ltd > www.qas.com
 *
 * Common Classes > PromptSet.java
 * Prompt set details
 * ----------------------------------------------------------------------------
 */
package com.qas.newmedia.internet.ondemand.product.proweb;

import java.io.Serializable;
import java.util.List;

import com.qas.ondemand_2011_03.PromptSetType; // parameter to SOAP layer
import com.qas.ondemand_2011_03.QAPromptSet; // returned by SOAP layer

/** Wrapper class to encapsulate data representing a prompt set, which is a structure
 * that contains a set of (user) prompt lines (for different address terms).
 */
public class PromptSet implements Serializable {
    // ------------------------------------------------------------------------
    // public constants
    // ------------------------------------------------------------------------
    /** Constant referring to the prompt set where all search text to be
     * submitted upon a single line. */
	public static final String ONELINE = PromptSetType.ONE_LINE.value();
	/** Constant for referring to the default prompt set for the engine. */
	public static final String DEFAULT = PromptSetType.DEFAULT.value();
	/** Constant for referring to the general data-independent prompt set. */
	public static final String GENERIC = PromptSetType.GENERIC.value();
	/** Constant for referring to the prompt set that requires the minimum possible
	* amount of search text to perform a search. */
	public static final String OPTIMAL = PromptSetType.OPTIMAL.value();
	/** Constant for referring to an extended country-specific
	* set for where the information required for an optimal search is not available. */
	public static final String ALTERNATE = PromptSetType.ALTERNATE.value();
	/** Constant for referring to a different alternative prompt set.*/
	public static final String ALTERNATE2 = PromptSetType.ALTERNATE_2.value();



    // ------------------------------------------------------------------------
    // private data
    // ------------------------------------------------------------------------
    private PromptLine[] m_Lines;
    private boolean m_IsDynamic;


    // ------------------------------------------------------------------------
    // public methods
    // ------------------------------------------------------------------------
    /** construct an instance from SOAP layer object */
    public PromptSet(QAPromptSet t) throws QasException {
        m_IsDynamic = t.isDynamic();
		List<com.qas.ondemand_2011_03.PromptLine> aLines = t.getLine();
        if (aLines == null) {
            m_Lines = null;
        } else {
            int iSize = aLines.size();
            if (iSize >0) {
                m_Lines = new PromptLine[iSize];
                for (int i=0; i < iSize; i++) {
					m_Lines[i] = new PromptLine(aLines.get(i));
                }
            }
        }
    }

    /** Returns the array on prompt lines that make up this prompt set.
     * Each prompt set line should correspond to a separate input field
     * for the user to enter their search terms. */
    public PromptLine[] getLines() {
        return m_Lines;
    }

    /** Returns a <code>String[]</code> of prompts (from the prompt line array) */
    public String[] getLinePrompts() {
        String[] asResults = new String[m_Lines.length];

        for (int i=0; i < m_Lines.length; i++) {
            asResults[i] = m_Lines[i].getPrompt();
        }

        return asResults;
    }

    /** Returns whether dynamic search submission is recommended.
     * i.e. automatically submit the search after a short pause in typing. */
    public boolean isSearchingDynamic() {
        return m_IsDynamic;
    }
}
