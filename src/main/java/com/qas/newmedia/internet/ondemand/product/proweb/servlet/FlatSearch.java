/* ----------------------------------------------------------------------------
 * QAS On Demand
 * (c) 2004 QAS Ltd.
 * File: FlatSearch.java
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
 * Command to encapsulate initial search logic for the flattened scenario. The aim is to generate a picklist, where each
 * item knows which the next command is (set in PickFunctions), or to perform automatic step in as approapriate.
 * Input params: DataId, CountryName, PromptSet, SkippedPicklist, UserInput[] - all also set as output attributes.
 * Output attributes: Route, Moniker, BackCommand, PickMonikers[], PickFunctions[], PickTexts[], PickPostcodes[],
 * ErrorInfo (if an exception is caught)
 *
 */
public class FlatSearch implements Command {
    public static final String NAME = "FlatSearch";

    public String execute(HttpServletRequest request,
            HttpServletResponse response) {
        String sDestinationPage = null;

        // Parameters to pass through and pick up
        String sDataId = HttpHelper.passThrough(request, Constants.DATA_ID);
        String sCountryName = HttpHelper.passThrough(request, Constants.COUNTRY_NAME);
        String sPromptSet = HttpHelper.passThrough(request, Constants.PROMPT_SET);
        // Collect array of address search elements from user input fields from previous page
        String[] asSearch = HttpHelper.passThroughArray(request, Constants.USER_INPUT);
        String sPicklistMoniker = HttpHelper.getValue(request, Constants.PICKLIST_MONIKER);
        String sRequestTag = 
        	HttpHelper.passThrough(request, Constants.REQUEST_TAG);
        // Arrival route - initial search (moving forwards)
        //               - recreate picklist (moving backwards). don't auto-skip picklist
        boolean bInitialSearch = sPicklistMoniker == null || sPicklistMoniker.equals("");

        // We need to enclose the search call in a try block.
        try {
            // Point the search object at the web service.
            QuickAddress searchService = new QuickAddress(Constants.ENDPOINT, request); // May throw MalformedURL or ServiceException

            Picklist picklist;
            if (bInitialSearch) {
                // Perform the initial search (singleline engine, flattened picklists)
                searchService.setEngineType(QuickAddress.SINGLELINE);
                searchService.setFlatten(true);
                picklist = searchService.
                search(sDataId, asSearch, sPromptSet, null, sRequestTag).
                	getPicklist();	// May throw QasException
            } else {
                // Recreate picklist from moniker
                picklist = searchService.stepIn(sPicklistMoniker);
            }

            // Handle 'failure' return situations
            if ( picklist.isTimeout() ) {
                request.setAttribute(Constants.ROUTE, Constants.ROUTE_TIMEOUT);
            } else if ( picklist.isMaxMatches() ) {
                request.setAttribute(Constants.ROUTE, Constants.ROUTE_TOO_MANY);
            } else if ( picklist.getItems() == null) {
                request.setAttribute(Constants.ROUTE, Constants.ROUTE_NO_MATCHES);
            } else if ( picklist.getTotal() == 0) {
                request.setAttribute(Constants.ROUTE, Constants.ROUTE_NO_MATCHES);
            } else {
                // Save the overall picklist moniker (to recreate the picklist if the user steps back)
                request.setAttribute(Constants.PICKLIST_MONIKER, picklist.getMoniker());

                // Skip the picklist if this is an initial search, and we've got an autoformat result or single must-refine result

                if ( bInitialSearch && ( picklist.isAutoFormatSafe() || picklist.isAutoFormatPastClose() ) ) {
                    // Picklist is being skipped: format the address
                    request.setAttribute(Constants.MONIKER, picklist.getItems()[0].getMoniker());
                    request.setAttribute(Constants.ROUTE, Constants.ROUTE_NORMAL);
					request.setAttribute(Constants.SINGLE_MATCH, true);

                    // Go format page (sDestinationPage is already set appropriately)
					Command formatAddress = new FlatFormatAddress();
		            sDestinationPage = formatAddress.execute(request, response);

                } else if ( bInitialSearch && ( picklist.getItems().length == 1 ) && mustRefine( picklist.getItems()[0] ) ) {
                    // Picklist is being skipped: refine the address
                    request.setAttribute(Constants.MONIKER, picklist.getItems()[0].getMoniker());
                    Command refine = new FlatRefineAddress();
                    return refine.execute(request, response);
                } else {
                    // Populate the array that will be displayed as a picklist of results
                    int iSize = picklist.getItems().length;
                    String[] asMonikers		= new String[iSize];
                    String[] asCommands		= new String[iSize];    // "RefineAddress" or "FormatAddress"
                    String[] asPickTexts	= new String[iSize];
                    String[] asPostcodes	= new String[iSize];

                    for (int i = 0; i < iSize; i++) {
                        PicklistItem item = picklist.getItems()[i];
                        asMonikers[i] = item.getMoniker();
                        asPickTexts[i] = item.getText();
                        asPostcodes[i] = item.getPostcode();
                        if (mustRefine(item)) {
                            asCommands[i] = FlatRefineAddress.NAME;
                        } else {
                            asCommands[i] = FlatFormatAddress.NAME;
                        }
                    }

                    // set the output arrays as attributes
                    request.setAttribute(Constants.PICK_MONIKERS, asMonikers);
                    request.setAttribute(Constants.PICK_FUNCTIONS, asCommands);
                    request.setAttribute(Constants.PICK_TEXTS, asPickTexts);
                    request.setAttribute(Constants.PICK_POSTCODES, asPostcodes);
                    // display page
                    sDestinationPage = Constants.FLAT_SEARCH_PAGE;
                }
            }
        } catch (Exception e) {
            // dump the exception to error stream
            System.err.println("~~~~~~~~~ Caught exception in FlatSearch command ~~~~~~~~~~");
            e.printStackTrace();
            System.err.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

            if (e instanceof QasException) {
            	QasException qe = (QasException)e;
            	request.setAttribute(Constants.ERROR_CODE, qe.getErrorNumber());
            }

            request.setAttribute(Constants.ROUTE, Constants.ROUTE_FAILED);
            request.setAttribute(Constants.ERROR_INFO, e.toString());
        }

        if (sDestinationPage == null) {
            // go straight to format address
        	Command formatAddress = new FlatFormatAddress();
            sDestinationPage = formatAddress.execute(request, response);
        }

        return sDestinationPage;
    }

    // Must the picklist item be refined (text added) to form a final address?
    protected boolean mustRefine(PicklistItem item) {
        return ( item.isIncompleteAddress() || item.isUnresolvableRange() || item.isPhantomPrimaryPoint() );
    }
}
