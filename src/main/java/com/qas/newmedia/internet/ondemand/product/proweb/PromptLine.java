/* ----------------------------------------------------------------------------
 * QAS On Demand > (c) QAS Ltd > www.qas.com
 *
 * Common Classes > PromptLine.java
 * Prompt line details
 * ----------------------------------------------------------------------------
 */
package com.qas.newmedia.internet.ondemand.product.proweb;

import java.io.Serializable;

/**
 * Wrapper class encapsulating information regarding a specific prompt set line.
 * An ordered list (array) of prompt lines is a prompt set.
 */
public class PromptLine implements Serializable {
    // Member data
    private String  m_Prompt     = null;
    private String  m_Example    = null;
    private int     m_SuggestedInputLength = 0; // positive integer

    // Member methods
    /** construct instance from SOAP layer object */
	public PromptLine(com.qas.ondemand_2011_03.PromptLine t){
        m_Prompt = t.getPrompt();
        m_Example = t.getExample();
        m_SuggestedInputLength = t.getSuggestedInputLength().intValue();
    }

    /** Returns a textual hint about what should be entered into the
     * input field associated with this prompt line.
     * For example, “Building number or name”.
     */
    public String getPrompt() {
        return m_Prompt;
    }
    /** Returns an example of what could be entered into the prompt line,
     * to aid the user.For example, “12”.
     */
    public String getExample() {
        return m_Example;
    }
    /** Returns the suggested length of the input field, although will typically
     * be ignored by the integrator.
     */
    public int getSuggestedInputLength() {
        return m_SuggestedInputLength;
    }
}
