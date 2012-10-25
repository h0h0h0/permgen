/* ----------------------------------------------------------------------------
 * QAS On Demand > (c) QAS Ltd > www.qas.com
 *
 * Common Classes > ExampleAddress.java
 * Example address details
 * ----------------------------------------------------------------------------
 */
package com.qas.newmedia.internet.ondemand.product.proweb;

import java.io.Serializable;

import com.qas.ondemand_2011_03.QAExampleAddress;

/**
 * Wrapper class to encapsulate example address data - consisting of an
 * example FormattedAddress and a comment.  This is commonly used to
 * preview a given layout with an example address.
 */
public class ExampleAddress implements Serializable {
    // ------------------------------------------------------------------------
    // private data
    // ------------------------------------------------------------------------
    private String m_Comment;
    private FormattedAddress m_Address;

    // ------------------------------------------------------------------------
    // public methods
    // ------------------------------------------------------------------------
    /** construct instance from SOAP layer object */
    public ExampleAddress(QAExampleAddress a) {
        m_Comment = a.getComment();
        m_Address = new FormattedAddress(a.getAddress());
    }

    /** create array from SOAP layer array */
    public static ExampleAddress[] createArray(QAExampleAddress[] aAddresses) {
        ExampleAddress[] results = null;
        if (aAddresses != null) {
            int iSize = aAddresses.length;
            if (iSize > 0) {
                results = new ExampleAddress[iSize];
                for (int i=0; i < iSize; i++) {
                    results[i] = new ExampleAddress(aAddresses[i]);
                }
            }
        }
        return results;
    }

    /** returns a textual description of the example address.
     * For example “A typical residential address”
     */
    public String getComment() {
        return m_Comment;
    }

    /** returns the formatted example address */
    public FormattedAddress getAddress() {
        return m_Address;
    }
}
