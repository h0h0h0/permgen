/* ----------------------------------------------------------------------------
 * QAS Pro Web
 * (c) 2004 QAS Ltd.
 * File: KeyFormatAddress.java
 * ----------------------------------------------------------------------------
 */
package com.qas.newmedia.internet.ondemand.product.proweb.servlet;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.qas.newmedia.internet.ondemand.product.proweb.AddressLine;
import com.qas.newmedia.internet.ondemand.product.proweb.FormattedAddress;
import com.qas.newmedia.internet.ondemand.product.proweb.QuickAddress;
import com.qas.ondemand_2011_03.QAAddressType;
import com.qas.ondemand_2011_03.QAExampleAddress;

/**
 * Command to encapsulate logic for the address edit page in the Keytened scenario.
 * Input params: DataId, CountryName, PromptSet, SkippedPicklist, UserInput[],
 * Route, Moniker, OriginalMoniker, RefineInput.  All also set as output params.
 * Output params: attributes named Lines[], Labels[], BackCommand, BackMoniker.
 *
 */
public class KeyFormatAddress implements Command {
    public static final String NAME = "KeyFormatAddress";
    
    /**
     * @see com.qas.proweb.servlet.Command#execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public String execute(HttpServletRequest request,
            HttpServletResponse response) {
        // Parameters to pass through
        String sDataId = HttpHelper.passThrough(request, Constants.DATA_ID);
        String sCountryName = HttpHelper.passThrough(request, Constants.COUNTRY_NAME);
        String sPromptSet = HttpHelper.passThrough(request, Constants.PROMPT_SET);
        String[] asUserInput = HttpHelper.passThroughArray(request, Constants.USER_INPUT);
        
        HttpHelper.passThrough(request, Constants.PICKLIST_MONIKER);
        String sRefineMoniker = HttpHelper.passThrough(request, Constants.REFINE_MONIKER);
        String sMoniker = HttpHelper.passThrough(request, Constants.MONIKER);
        String sRoute = HttpHelper.passThrough(request, Constants.ROUTE);
        String sRequestTag = 
        	HttpHelper.passThrough(request, Constants.REQUEST_TAG);
        
        // Output arrays
        String[] asLines = null;
        String[] asLabels = null;
        
        if (!sRoute.equals(Constants.ROUTE_PRE_SEARCH_FAILED) && !sRoute.equals(Constants.ROUTE_FAILED)) {
            try {
                QuickAddress searchService = new QuickAddress(Constants.ENDPOINT, request);
                FormattedAddress address = null;
                List<QAExampleAddress> exampleAddresses = null;
                String sLayout = (String) request.getSession().getAttribute(Constants.LAYOUT);
                
                if ( sRoute.equals(Constants.ROUTE_NORMAL) ) {
                    // Get the formatted address from the moniker
                	address = 
                    	searchService.getFormattedAddress(sLayout, sMoniker, sRequestTag);
                } else {
                    // Otherwise get the first of the example addresses
                	QAAddressType qaAddressType = searchService.getExampleAddresses(sDataId, sLayout).get(0).getAddress(); 
                	address = new FormattedAddress(qaAddressType); 
                }
                
                AddressLine[] aLines = address.getAddressLines();
                asLines = new String[aLines.length];
                asLabels = new String[aLines.length];
                for (int i = 0; i < aLines.length; i++) {
                    asLines[i] = aLines[i].getLine();
                    asLabels[i] = aLines[i].getLabel();
                }
            } catch (Exception e) {
                // Dump the exception to error stream
                System.err.println("~~~~~~~ Caught exception in FormatAddress command ~~~~~~~");
                e.printStackTrace();
                System.err.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                
                // If a problem occurred, then go to the manual address entry page anyway
                request.setAttribute(Constants.ROUTE, Constants.ROUTE_FAILED);
                sRoute = Constants.ROUTE_FAILED;
                request.setAttribute(Constants.ERROR_INFO, e.toString());
            }
        }
        
        if (asLabels == null || asLines == null) {
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
        
        // Set the back-command depending on the arrival route
        String sBackCommand;
        if ( sRoute.equals(Constants.ROUTE_NORMAL) ) {
            sBackCommand = KeySearch.NAME;
        } else if ( sRoute.equals(Constants.ROUTE_NO_MATCHES)
        || sRoute.equals(Constants.ROUTE_TIMEOUT)
        || sRoute.equals(Constants.ROUTE_TOO_MANY) ) {
            sBackCommand = KeyPromptEntry.NAME;
        } else {
            sBackCommand = KeyInit.NAME;
        }
        
        // set the address lines and labels
        request.setAttribute(Constants.LINES, asLines);
        request.setAttribute(Constants.LABELS, asLabels);
        // set the back command attribute
        request.setAttribute(Constants.BACK_COMMAND, sBackCommand);
        
        return Constants.KEY_FORMAT_ADDRESS_PAGE;
    }
}
