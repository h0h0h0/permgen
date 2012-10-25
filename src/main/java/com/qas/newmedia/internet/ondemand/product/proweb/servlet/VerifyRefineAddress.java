/* ----------------------------------------------------------------------------
 * QAS On Demand > (c) QAS Ltd > www.qas.com
 *
 * Web > Verification > VerifyRefine.java
 * Prompt for additional (premise) address details, and check for suitability
 * ----------------------------------------------------------------------------
 */
package com.qas.newmedia.internet.ondemand.product.proweb.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.qas.newmedia.internet.ondemand.product.proweb.Picklist;
import com.qas.newmedia.internet.ondemand.product.proweb.PicklistItem;
import com.qas.newmedia.internet.ondemand.product.proweb.QasException;
import com.qas.newmedia.internet.ondemand.product.proweb.QuickAddress;
/**
 * Encapsulates logic for the refinement page in the verification scenario.  This is only called
 * if an unresolvable range, incomplete address or phantom primary point (AUS) is selected,
 * as these need <code>refine</code> called in order to pass additional info for the final address.
 * Input params: DataId, CountryName, RefineInput, Moniker (also set as output attributes)
 * Output atributes: IsPhantom, RefineLine, RefineAddress, RefineFail; Route, ErrorInfo (if an exception is caught)
 *
 */
public class VerifyRefineAddress implements Command {
    public static final String NAME = "VerifyRefineAddress";

    // Code-to-page field constants
    public static final String PHANTOM = "IsPhantom";

    /**
     * @see com.qas.ondemand_2011_03.servlet.Command#execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public String execute(HttpServletRequest request,
            HttpServletResponse response) {
        // Parameters to pass through
        HttpHelper.passThrough(request, Constants.COUNTRY_NAME);
        HttpHelper.passThrough(request, Constants.DATA_ID);
        HttpHelper.passThroughArray(request, Constants.USER_INPUT);

        String sMoniker = HttpHelper.passThrough(request, Constants.MONIKER);
        String sRefineInput = HttpHelper.passThrough(request, Constants.REFINE_INPUT);

        boolean bGoFormatPage = false;
        Picklist picklist = null;

        try {
            QuickAddress searchService = new QuickAddress(Constants.ENDPOINT, request);

            // Perform the refinement (add space to ensure only perfect matches)
            picklist = searchService.refine(sMoniker, sRefineInput + " ");
            request.setAttribute(Constants.ROUTE, Constants.ROUTE_NORMAL);

            // If the refined search produces no results, recreate the picklist and update the message
            if ( picklist.getItems() == null || picklist.getTotal() == 0) {
                // No acceptable address match - recreate without using refinement text
                picklist = searchService.refine(sMoniker, "");
                request.setAttribute(Constants.ROUTE, Constants.ROUTE_NO_MATCHES);
            }

            PicklistItem item = picklist.getItems()[0];
            request.setAttribute(Constants.PICKTEXT, item.getText());
            request.setAttribute(Constants.PARTIAL, item.getPartialAddress());

            if (sRefineInput.equals("")) {
                // First time through - simply display appropriate message
                if (item.isPhantomPrimaryPoint()) {
                    request.setAttribute(Constants.ROUTE, PHANTOM);
                }
            } else if (item.isUnresolvableRange()) {
                // Redisplay with explanation as to how the refinement text failed to match
                request.setAttribute(Constants.ROUTE, Constants.ROUTE_NO_MATCHES);
            } else if (picklist.getTotal() == 1) {
                // Accept the address (or force accept in the Phantom case)
                request.setAttribute(Constants.MONIKER, item.getMoniker());
                // No more refinement necessary - now go straight to the format address command
                bGoFormatPage = true;
            }
        } catch (Exception e) {
            // Dump the exception to error stream
            System.err.println("~~~~~~~~~ Caught exception in VerifyRefine command ~~~~~~~~~~");
            e.printStackTrace();
            System.err.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

            if (e instanceof QasException) {
            	QasException qe = (QasException)e;
            	request.setAttribute(Constants.ERROR_CODE, qe.getErrorNumber());
            }

            request.setAttribute(Constants.ROUTE, Constants.ROUTE_FAILED);
            request.setAttribute(Constants.ERROR_INFO, e.toString());
            bGoFormatPage = true;
        }

        if (bGoFormatPage) {
            // Go straight to format address
            Command formatAddress = new VerifyFormatAddress();
            return formatAddress.execute(request, response);
        }

        // picklist case: populate arrays of monikers, text, postcode & action function
        Verify.outputPicklist(request, picklist);
        request.setAttribute(Constants.PROMPT, picklist.getPrompt());

        return Constants.VERIFY_REFINE_PAGE;
    }

}
