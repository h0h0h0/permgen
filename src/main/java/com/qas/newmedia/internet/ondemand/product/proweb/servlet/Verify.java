/* ----------------------------------------------------------------------------
 * QAS On Demand > (c) QAS Ltd > www.qas.com
 *
 * Web > Verification > Verify.java
 * Verify the address entered > Perfect match, best match, picklist of matches
 * ----------------------------------------------------------------------------
 */
package com.qas.newmedia.internet.ondemand.product.proweb.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

import com.qas.newmedia.internet.ondemand.product.proweb.AddressLine;
import com.qas.newmedia.internet.ondemand.product.proweb.CanSearch;
import com.qas.newmedia.internet.ondemand.product.proweb.FormattedAddress;
import com.qas.newmedia.internet.ondemand.product.proweb.Picklist;
import com.qas.newmedia.internet.ondemand.product.proweb.PicklistItem;
import com.qas.newmedia.internet.ondemand.product.proweb.PromptSet;
import com.qas.newmedia.internet.ondemand.product.proweb.QasException;
import com.qas.newmedia.internet.ondemand.product.proweb.QuickAddress;
import com.qas.newmedia.internet.ondemand.product.proweb.SearchResult;
import com.qas.ondemand_2011_03.DPVStatusType;

/**
 * Command to encapsulate verify functionality where minimal workflow changes are required in the web
 * application.
 * Input params DataId, CountryName, Route and UserInput[]
 * (all set as output attributes).
 * Output attributes Address[], CountryName, Route and Verified.
 * ErrorInfo (if an exception is caught)
 * Address will contain the verified (cleaned) address if this was done to the "Verified" level,
 * and the final address page will be shown.
 * If displaying a picklist, PickMonikers[], PickTexts[], PickPostcodes[] and PickScores[] will be set.
 * If interaction with an address is required, Labels[] and Lines[] will be set.
 * Otherwise, interaction with no address or picklist is required provided this is the first time through
 * (indicated by the Route).
 */
public class Verify implements Command {
    public static final String NAME = "Verify";

    /** these data members are not intended to live across invocations of execute */
    private String m_sDataId = null;
    private String[] m_asUserInput = null;
    private SearchResult m_Result = null;
    private HttpServletRequest m_Request = null;

    /**
     * @see com.qas.ondemand_2011_03.servlet.Command#execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public String execute(HttpServletRequest request,
            HttpServletResponse response) {
        m_Request = request;

        // input attributes/params
        m_asUserInput = HttpHelper.getArrayValue(m_Request, Constants.USER_INPUT);
        String[] asOriginalInput = HttpHelper.getArrayValue(m_Request, Constants.ORIGINAL_INPUT);

        // input & output
        HttpHelper.passThrough(m_Request, Constants.COUNTRY_NAME);
        m_sDataId = HttpHelper.passThrough(m_Request, Constants.DATA_ID);
        boolean bAlreadyVerified = HttpHelper.passThrough(m_Request, Constants.ROUTE).equals(Constants.ROUTE_ALREADY_VERIFIED);

        if (bAlreadyVerified && Arrays.equals(m_asUserInput, asOriginalInput)) {
            return noInteraction("address accepted unchanged");
        } else if (bAlreadyVerified) {
        	m_Request.setAttribute(Constants.ADDRESS, m_asUserInput);
            return Constants.FINAL_ADDRESS_PAGE;
        }

        // output attributes
        String sRoute = Constants.ROUTE_NORMAL;
        String[] asAddress = null;
        try {
			QuickAddress searchService = new QuickAddress(Constants.ENDPOINT, request);
            searchService.setEngineType(QuickAddress.VERIFICATION);
            searchService.setFlatten(true);
            String sRequestTag = 
            	HttpHelper.passThrough(request, Constants.REQUEST_TAG);
            String sLayout = (String) request.getSession().getAttribute(Constants.LAYOUT);
            if (!bAlreadyVerified) {

                CanSearch canSearch = searchService.canSearch(m_sDataId, sLayout);
                if (!canSearch.isOk()) {
                    sRoute = Constants.ROUTE_PRE_SEARCH_FAILED;
                    m_Request.setAttribute(Constants.ROUTE, sRoute);
                    m_Request.setAttribute(Constants.ERROR_INFO, canSearch.getMessage());
                }
            }

            if (sRoute.equals(Constants.ROUTE_NORMAL)) {
            	m_Result = searchService.search(m_sDataId,
												m_asUserInput,
												PromptSet.DEFAULT,
												sLayout,
												sRequestTag);
                m_Request.setAttribute(Constants.ROUTE, m_Result.getVerifyLevel());
                m_Request.setAttribute(Constants.ADDRESS_INFO, getVerificationString());

                if (m_Result.getVerifyLevel().equals(SearchResult.Verified)
                    || (m_Result.getVerifyLevel().equals(SearchResult.VerifyPlace))
                    || (m_Result.getVerifyLevel().equals(SearchResult.VerifyStreet))) {
                	// Fully verified result, so formulate the output params (Address[] and Verify).
                    AddressLine[] lines = m_Result.getAddress().getAddressLines();
                    asAddress = new String[lines.length];
                    for (int i=0; i < lines.length; i++) {
                        asAddress[i] = lines[i].getLine();
                    }

                    // set the output attributes and return the final page
                    m_Result.getAddress().setDpvMessage(m_Request, m_Result.getAddress().getDPVStatus());
                    m_Request.setAttribute(Constants.ADDRESS, asAddress);
                    m_Request.setAttribute(Constants.ADDRESS_INFO,
                            getVerificationString() + Verify.getAddressWarnings(m_Result.getAddress()));

                    return Constants.FINAL_ADDRESS_PAGE;
                }
                // If you require no interaction at all, edit so it always calls noInteraction(..)

                else if (bAlreadyVerified) {
                    // If we've been once through already then we want no interaction
                    return noInteraction("");
                } else {
                    return processResultWithInteraction();
                }
            }
        } catch (Exception e) {
            // dump the exception to error stream
            System.err.println("~~~~~~~~~~~ Caught exception in Verify command ~~~~~~~~~~~~");
            e.printStackTrace();
            System.err.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

            if (e instanceof QasException) {
            	QasException qe = (QasException)e;
            	request.setAttribute(Constants.ERROR_CODE, qe.getErrorNumber());
            }
            request.setAttribute(Constants.ROUTE, Constants.ROUTE_FAILED);
            request.setAttribute(Constants.ERROR_INFO, e.toString());

            sRoute = Constants.ROUTE_FAILED;
            m_Request.setAttribute(Constants.ERROR_INFO, e.toString());
        }

        return noInteraction("address verification " + sRoute);
    }

    /** Offer best match, or picklist of matches, when the verification level is too low */
    private String processResultWithInteraction() {
        // Forward through original input address
        m_Request.setAttribute(Constants.USER_INPUT, m_asUserInput);

        // Three different cases - formatted address, picklist, neither:
        //  if address is present, we show the address
        //  if picklist is present, we show the items
        //  otherwise, we show just the original address
        if (m_Result.getAddress() != null) {
            // address case: populate Lines and Labels output params
            AddressLine[] lines = m_Result.getAddress().getAddressLines();
            String[] asLines = new String[lines.length];
            String[] asLabels = new String[lines.length];
            for (int i=0; i < lines.length; i++) {
                asLines[i] = lines[i].getLine();
                asLabels[i] = lines[i].getLabel();
            }
            m_Result.getAddress().setDpvMessage(m_Request, m_Result.getAddress().getDPVStatus());
            m_Request.setAttribute(Constants.LABELS, asLabels);
            m_Request.setAttribute(Constants.LINES, asLines);
            m_Request.setAttribute(Constants.ADDRESS_INFO,
                    getVerificationString() + Verify.getAddressWarnings(m_Result.getAddress()));
        } else if (m_Result.getPicklist() != null
                && m_Result.getPicklist().getItems() != null) {
            // picklist case: populate arrays of monikers, text, postcode & action function
            boolean bRequireRefine = Verify.outputPicklist(m_Request, m_Result.getPicklist());

            // if refinement required, prompt for refinement and start with picklist hidden
            if (bRequireRefine || m_Result.getVerifyLevel().equals(SearchResult.StreetPartial)) {
                m_Request.setAttribute(Constants.MONIKER, m_Result.getPicklist().getMoniker());
                m_Request.setAttribute(Constants.PROMPT, m_Result.getPicklist().getPrompt());
                m_Request.setAttribute(Constants.REFINE_INPUT, "");
            }
        } else {
            // no items - no matches
            m_Request.setAttribute(Constants.ADDRESS, m_asUserInput);
        }

        return "/verifyInteraction.jsp";
    }

    /** Accept originally entered address, when the verification level is too low **/
    private String noInteraction(String sReason) {
        m_Request.setAttribute(Constants.ADDRESS, m_asUserInput);
        if (!sReason.equals(""))
            m_Request.setAttribute(Constants.ADDRESS_INFO, sReason + ", so the entered address has been used");
        else
            m_Request.setAttribute(Constants.ADDRESS_INFO, "");
        return Constants.FINAL_ADDRESS_PAGE;
    }

    /** Return the standard address verification info string **/
    private String getVerificationString() {
        return "address verification level was " + m_Result.getVerifyLevel();
    }

    /*
     * Common verification methods
     */

    /** Must the picklist item be refined (text added) to form a final address? **/
    public static boolean mustRefine(PicklistItem item) {
        return (item.isIncompleteAddress() || item.isUnresolvableRange() || item.isPhantomPrimaryPoint());
    }

    /** Return formatted address warnings, line separated **/
    public static String getAddressWarnings(FormattedAddress tFormattedAddress) {
        String sWarning = "";

        if (tFormattedAddress.isOverflow()) {
            sWarning += "\nWarning: Address has overflowed the layout &#8211; elements lost";
        }
        if (tFormattedAddress.isTruncated()) {
            sWarning += "\nWarning: Address elements have been truncated";
        }

        return sWarning;
    }

    /** Write out picklist: populate arrays of monikers, display, postcodes & actions **/
    public static boolean outputPicklist(HttpServletRequest request, Picklist picklist) {
        boolean bRequireRefine = false;

        PicklistItem[] items = picklist.getItems();
        int total = picklist.getTotal();

        String[] asMonikers = new String[total];
        String[] asText = new String[total];
        String[] asPostcodes = new String[total];
        String[] asCommands = new String[total];

        for (int i=0; i < total; i++) {
            asMonikers[i] = items[i].getMoniker();
            asText[i] = items[i].getText();
            asPostcodes[i] = items[i].getPostcode();
            if (Verify.mustRefine(items[i])) {
                asCommands[i] = VerifyRefineAddress.NAME;
                bRequireRefine = true;
            } else {
                asCommands[i] = VerifyFormatAddress.NAME;
            }
        }

        request.setAttribute(Constants.PICK_MONIKERS, asMonikers);
        request.setAttribute(Constants.PICK_TEXTS, asText);
        request.setAttribute(Constants.PICK_POSTCODES, asPostcodes);
        request.setAttribute(Constants.PICK_FUNCTIONS, asCommands);

        return bRequireRefine;
    }
}
