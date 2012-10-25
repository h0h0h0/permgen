/* ----------------------------------------------------------------------------
 * QAS On Demand
 * (c) 2004 QAS Ltd.
 * File: FlatRefineAddress.java
 * ----------------------------------------------------------------------------
 */
package com.qas.newmedia.internet.ondemand.product.proweb.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.qas.newmedia.internet.ondemand.product.proweb.Picklist;
import com.qas.newmedia.internet.ondemand.product.proweb.PicklistItem;
import com.qas.newmedia.internet.ondemand.product.proweb.QuickAddress;

/**
 * Encapsulates logic for the refinement page in the flattened scenario.  This is only called
 * if an unresolvable range or a Phantom Primary Point (AUS only) is selected, as in these cases
 * need to call <code>refine</code> in order to pass additional info for the final address.
 * Input params: DataId, CountryName, PromptSet, SkippedPicklist, UserInput[], Route,
 * Refine, RefineInput, RefinInLine, Moniker, OriginalMoniker, IsPhantom
 * (all also set as output attributes)
 * Output atributes: Lines, Labels[], BackRoute, BackSkippedPicklist, BackCommand, Result, ErrorInfo (if an exception is caught)
 *
 */
public class FlatRefineAddress implements Command {
    public static final String NAME = "FlatRefineAddress";

    // Code-to-page field constants
    public static final String IS_PHANTOM = "IsPhantom";
    public static final String REFINE_FAIL = "RefineFailed";
    public static final String REFINE_LINE = "RefineLine";
    public static final String REFINE_ADDRESS = "RefineAddress";

    /**
     * @see com.qas.ondemand_2011_03.servlet.Command#execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public String execute(HttpServletRequest request,
            HttpServletResponse response) {
        // Parameters to pass through
        HttpHelper.passThrough(request, Constants.DATA_ID);
        HttpHelper.passThrough(request, Constants.COUNTRY_NAME);
        HttpHelper.passThrough(request, Constants.PROMPT_SET);
        HttpHelper.passThroughArray(request, Constants.USER_INPUT);

        HttpHelper.passThrough(request, Constants.PICKLIST_MONIKER);
        String sRefineMoniker = HttpHelper.getValue(request, Constants.REFINE_MONIKER);
        String sRefineInput = HttpHelper.passThrough(request, Constants.REFINE_INPUT);
        String sRequestTag = 
        	HttpHelper.passThrough(request, Constants.REQUEST_TAG);
        
        // Pick up RefineMoniker (arriving backwards), otherwise it will be stored in 'Moniker'
        if ( sRefineMoniker == null || sRefineMoniker.equals("")) {
            sRefineMoniker = HttpHelper.getValue(request,Constants.MONIKER);
        }
        request.setAttribute(Constants.REFINE_MONIKER, sRefineMoniker);

        String sDestinationPage = Constants.FLAT_REFINE_ADDRESS_PAGE;

        try {
            QuickAddress searchService = new QuickAddress(Constants.ENDPOINT, request);

            // Perform the refinement
            Picklist picklist = searchService.refine(sRefineMoniker,
													 sRefineInput,
													 sRequestTag);

            // If the refined search produces no results, recreate the picklist and update the message
            if ( picklist.getItems() == null ) {
                // No acceptable address match - recreate without using refinement text
                picklist = searchService.refine(sRefineMoniker, "");
            }

            PicklistItem item = picklist.getItems()[0];
            request.setAttribute(REFINE_LINE, item.getText());
            request.setAttribute(REFINE_ADDRESS, item.getPartialAddress());

            if (sRefineInput.equals("")) {
                // First time through - simply display appropriate message
                if (item.isPhantomPrimaryPoint()) {
                    request.setAttribute(IS_PHANTOM, Boolean.TRUE.toString());
                }
            } else if (item.isUnresolvableRange()) {
                // Redisplay with explanation as to how the refinement text failed to match
                request.setAttribute(REFINE_FAIL, Boolean.TRUE.toString());
            } else {
                // accept the address (or force accept in the Phantom case)
                request.setAttribute(Constants.MONIKER, item.getMoniker());
                // no more refinement necessary - now go straight to the format address command
                sDestinationPage = null;
            }
        } catch (Exception e) {
            // dump the exception to error stream
            System.err.println("~~~~~~~~~ Caught exception in FlatRefine command ~~~~~~~~~~");
            e.printStackTrace();
            System.err.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

            request.setAttribute(Constants.ROUTE, Constants.ROUTE_FAILED);
            request.setAttribute(Constants.ERROR_INFO, e.toString());
            sDestinationPage = null;
        }

        if (sDestinationPage == null) {
            // go straight to format address
            Command formatAddress = new FlatFormatAddress();
            sDestinationPage = formatAddress.execute(request, response);
        }

        return sDestinationPage;
    }

}
