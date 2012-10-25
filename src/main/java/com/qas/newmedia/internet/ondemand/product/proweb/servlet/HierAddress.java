/* ----------------------------------------------------------------------------
 * QAS On Demand
 * (c) 2004 QAS Ltd. All rights reserved.
 * File: HierAddress.java
 * Created: 22-Apr-2004
 * ----------------------------------------------------------------------------
 */
package com.qas.newmedia.internet.ondemand.product.proweb.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.qas.newmedia.internet.ondemand.product.proweb.AddressLine;
import com.qas.newmedia.internet.ondemand.product.proweb.FormattedAddress;
import com.qas.newmedia.internet.ondemand.product.proweb.QasException;
import com.qas.newmedia.internet.ondemand.product.proweb.QuickAddress;
import com.qas.ondemand_2011_03.DPVStatusType;

/**
 * Command to encapsulate Final Address retrieval/formatting logic in the hierarchical case.
 * Input params/Output attributes: DataId, CountryName, UserInput, Moniker, MonikerHistory[],
 * PartialHistory[], PostcodeHistory[], ScoreHistory[], Route
 * Output only Attributes: Lines[], Labels[], BackCommand, ErrorInfo (if exception caught)
 */
public class HierAddress implements Command {
    public static final String NAME = "HierAddress";

        /* (non-Javadoc)
         * @see com.qas.ondemand_2011_03.servlet.Command#execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
         */
    public String execute(HttpServletRequest request,
            HttpServletResponse response) {
        // Handle query parameters & request attributes
        String sDataId = HttpHelper.passThrough(request, Constants.DATA_ID);
        String sCountryName = HttpHelper.passThrough(request, Constants.COUNTRY_NAME);
        String sUserInput = HttpHelper.passThrough(request, Constants.USER_INPUT);

        String sRoute = HttpHelper.getValue(request, Constants.ROUTE); // Re-write at end
        String sMoniker = HttpHelper.passThrough(request, Constants.MONIKER);
        //String sPartial = HttpHelper.passThrough(request, Constants.PARTIAL);
        //String sUserRefine = HttpHelper.passThrough(request, Constants.REFINE_INPUT);
        HttpHelper.passThroughArray(request, Constants.MONIKER_HISTORY);
        HttpHelper.passThroughArray(request, Constants.PARTIAL_HISTORY);
        HttpHelper.passThroughArray(request, Constants.POSTCODE_HISTORY);
        HttpHelper.passThroughArray(request, Constants.SCORE_HISTORY);
        String sRequestTag = 
        	HttpHelper.passThrough(request, Constants.REQUEST_TAG);
        // Set up Back button command (i.e. destination)
        String sBackCommand = (sRoute.equals(Constants.ROUTE_NORMAL)) ? HierSearch.NAME : HierInit.NAME;

        // Output attributes
        String[] asLabels = null;
        String[] asLines = null;

        /** Retrieve address **/
        if (sRoute.equals(Constants.ROUTE_NORMAL)) {
            try {
                QuickAddress searchService = new QuickAddress(Constants.ENDPOINT, request);
                String sLayout = (String) request.getSession().getAttribute(Constants.LAYOUT);
                FormattedAddress objAddress = 
                	searchService.getFormattedAddress(sLayout, sMoniker, sRequestTag);
                objAddress.setDpvMessage(request, objAddress.getDPVStatus());

                AddressLine[] aLines = objAddress.getAddressLines();

                // Build display address
                int iSize = aLines.length;
                asLabels = new String[iSize];
                asLines = new String[iSize];
                for (int i = 0; i < iSize; i++) {
                    asLabels[i] = aLines[i].getLabel();
                    asLines[i] = aLines[i].getLine();
                }
            } catch (Exception e) {
                // dump the exception to error stream
                System.err.println("~~~~~~~~~ Caught exception in HierAddress command ~~~~~~~~~");
                e.printStackTrace();
                System.err.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

                if (e instanceof QasException) {
                	QasException qe = (QasException)e;
                	request.setAttribute(Constants.ERROR_CODE, qe.getErrorNumber());
                }
                request.setAttribute(Constants.ROUTE, Constants.ROUTE_FAILED);

                sRoute = Constants.ROUTE_FAILED;
                request.setAttribute(Constants.ERROR_INFO, e.toString());
            }
        }

        if (!sRoute.equals(Constants.ROUTE_NORMAL)) {
            // Manual address entry: set up the lines and labels
            asLabels = new String[]
            {
                "Address Line 1", "Address Line 2", "Address Line 3",
                "City", "State or Province", "ZIP or Postal Code"
            };
            asLines = new String[]
            {
                "", "", "", "", "", ""
            };
        }

        // Set the output attributes
        request.setAttribute(Constants.LINES, asLines);
        request.setAttribute(Constants.LABELS, asLabels);
        request.setAttribute(Constants.ROUTE, sRoute);
        request.setAttribute(Constants.BACK_COMMAND, sBackCommand);

        return Constants.HIER_ADDRESS_PAGE;
    }
}
