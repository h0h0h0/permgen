/* ----------------------------------------------------------------------------
 * QAS On Demand > (c) QAS Ltd > www.qas.com
 *
 * Intranet > Rapid Addressing > Standard > RapidAddress.java
 * Format the final address
 * ----------------------------------------------------------------------------
 */
package com.qas.newmedia.internet.ondemand.product.proweb.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.qas.newmedia.internet.ondemand.product.proweb.AddressLine;
import com.qas.newmedia.internet.ondemand.product.proweb.FormattedAddress;
import com.qas.newmedia.internet.ondemand.product.proweb.QasException;
import com.qas.newmedia.internet.ondemand.product.proweb.QuickAddress;
import com.qas.ondemand_2011_03.*;
import javax.servlet.http.HttpSession;

/**
 * Command to encapsulate Final Address retrieval/formatting logic
 * Input params/Output attributes:
 *    DataID, SearchEngine, UserInput, Moniker, Route
 *    MonikerHistory[], PartialHistory[], PostcodeHistory[], ScoreHistory[], RefineHistory[]
 * Output only Attributes:
 *    Lines[], Labels[], BackCommand, ErrorInfo (if exception caught)
 */
public class RapidAddress implements Command {
    public static final String NAME = "RapidAddress";

        /* (non-Javadoc)
         * @see com.qas.ondemand_2011_03.servlet.Command#execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
         */
    public String execute(HttpServletRequest request,
            HttpServletResponse response) {
        /** Handle query parameters & request attributes **/

        // Paramaters to pass through
        HttpHelper.passThrough(request, Constants.CALLBACK_FUNCTION);
        HttpHelper.passThrough(request, Constants.DATA_ID);
        HttpHelper.passThrough(request, Constants.SEARCH_ENGINE);

        // Special pass through for ERROR_CODE attribute
        Integer iValue = (Integer) request.getAttribute(Constants.ERROR_CODE);
        if (iValue != null && iValue.intValue() >= 0) {
        	request.setAttribute(Constants.ERROR_CODE, iValue);
        }

        // Paramaters to act on
        String sRoute = HttpHelper.getValue(request, Constants.ROUTE);
        String sMoniker = HttpHelper.getValue(request, Constants.MONIKER);
        HttpHelper.passThrough(request, Constants.USER_INPUT);
        HttpHelper.passThrough(request, Constants.STEPIN_WARNING);

        // Picklist history stacks
        HttpHelper.passThroughStack(request, Constants.MONIKER_HISTORY);
        HttpHelper.passThroughStack(request, Constants.PICKTEXT_HISTORY);
        HttpHelper.passThroughStack(request, Constants.POSTCODE_HISTORY);
        HttpHelper.passThroughStack(request, Constants.SCORE_HISTORY);
        HttpHelper.passThroughStack(request, Constants.REFINE_HISTORY);
        
        String sRequestTag = 
        	HttpHelper.passThrough(request, Constants.REQUEST_TAG);

        /** Retrieve address - set output attributes **/

        // Calculate Back button command
        String sBackCommand = Constants.ROUTE_NORMAL.equals(sRoute) ? RapidSearch.NAME : RapidInit.NAME;
        String[] asLabels = null;
        String[] asLines = null;
        DataplusDisplayHelper d = new DataplusDisplayHelper();
        FormattedAddress objAddress = null;

        if (sRoute.equals(Constants.ROUTE_NORMAL)) {
            try {
				QuickAddress searchService = new QuickAddress(Constants.ENDPOINT, request);
                String sLayout = (String) request.getSession().getAttribute(Constants.LAYOUT);
                objAddress = 
                	searchService.getFormattedAddress(sLayout, sMoniker, sRequestTag);
                AddressLine[] aLines = objAddress.getAddressLines();

                // Build display address
                int iSize = aLines.length;
                asLabels = new String[iSize];
                asLines = new String[iSize];
                for (int i = 0; i < iSize; i++) {
                    asLabels[i] = aLines[i].getLabel();
                    asLines[i] = aLines[i].getLine();
                }

                String DPVStatus = objAddress.getDPVStatus().value();

                if(!DPVStatus.equals(Constants.INFO_DPVNOTCONFIGURED))
                {
                    if(DPVStatus.equals(Constants.INFO_DPVCONFIRMED))
                    {
                        request.setAttribute(Constants.STEPIN_WARNING, Constants.INFO_DPVCONFIRMED);
                    }
                    else if(DPVStatus.equals(Constants.INFO_DPVCONFIRMEDMISSINGSEC))
                    {
                        request.setAttribute(Constants.STEPIN_WARNING, Constants.INFO_DPVCONFIRMEDMISSINGSEC);
                    }
                    else
                    {
                        request.setAttribute(Constants.STEPIN_WARNING, Constants.INFO_DPVNOTCONFIRMED);
                    }
                }

                // Address layout issues override other warnings
                if (objAddress.isOverflow()) {
                    request.setAttribute(Constants.STEPIN_WARNING, Constants.WARN_OVERFLOW);
                } else if (objAddress.isTruncated()) {
                    request.setAttribute(Constants.STEPIN_WARNING, Constants.WARN_TRUNCATE);
                }

                if(DPVStatus.equals(Constants.INFO_DPVLOCKED))
                {
                    request.setAttribute(Constants.STEPIN_WARNING, Constants.INFO_DPVLOCKED);
                }

                if(DPVStatus.equals(Constants.INFO_DPVSEEDHIT))
                {
                    request.setAttribute(Constants.STEPIN_WARNING, Constants.INFO_DPVSEEDHIT);
                }

            } catch (Exception e) {
                // dump the exception to error stream
                System.err.println("-------- Caught exception in RapidAddress command --------");
                e.printStackTrace();
                System.err.println("----------------------------------------------------------");

                if (e instanceof QasException) {
                	QasException qe = (QasException)e;
                	request.setAttribute(Constants.ERROR_CODE, qe.getErrorNumber());
                }
                request.setAttribute(Constants.ROUTE, Constants.ROUTE_FAILED);

                sRoute = Constants.ROUTE_FAILED;
                request.setAttribute(Constants.ERROR_INFO, e.getMessage());
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

        if ( objAddress != null) {
            d.displayAddress(objAddress);
        } else {
            d.displayAddress( asLabels, asLines );
        }

        //d.renderMultiDataplusControls();


        // Set the output attributes
        request.setAttribute(Constants.LABELS, asLabels);
        request.setAttribute(Constants.LINES, asLines);
        request.setAttribute(Constants.ROUTE, sRoute);
        request.setAttribute(Constants.BACK_COMMAND, sBackCommand);


        request.setAttribute("DataplusDisplayHelper", d);

        return Constants.RAPID_ADDRESS_PAGE;
    }
}
