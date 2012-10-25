/* ----------------------------------------------------------------------------
 * QAS Pro Web
 * (c) 2004 QAS Ltd.
 * File: KeyPromptEntry.java
 * ----------------------------------------------------------------------------
 */
package com.qas.newmedia.internet.ondemand.product.proweb.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.qas.newmedia.internet.ondemand.product.proweb.CanSearch;
import com.qas.newmedia.internet.ondemand.product.proweb.PromptLine;
import com.qas.newmedia.internet.ondemand.product.proweb.PromptSet;
import com.qas.newmedia.internet.ondemand.product.proweb.QuickAddress;

/**
 * Encapsulates logic for prompt set entry page in the Key Search scenario.
 * Includes QAS pre-check and retrieval of the appropriate prompt set.
 * Input params: DataId, CountryName (both of these are also set as output attributes)
 * PromptSet (if it is set to alternative).
 * Output params: attributes named Prompts[], Examples[], SuggestedInputLengths[] and
 * BackCommand, BackPromptSet, Route, PromptSet, ErrorInfo (if an exception is caught)
 *
 */
public class KeyPromptEntry implements Command {
    public static final String NAME = "KeyPromptEntry";
    
    public String execute(HttpServletRequest request,
            HttpServletResponse response) {
        String sDestinationPage = null;
        
        // Parameters to pass through
        String sDataId = HttpHelper.passThrough(request, Constants.DATA_ID);
        String sCountryName = HttpHelper.passThrough(request, Constants.COUNTRY_NAME);
        
        // Determine which prompt set to use.
        // There are two main prompt sets  - Optimal (leading to the most efficient search) and Alternate (allowing entry of more address fields, if the Optimal fields aren't known)
        // We present the Optimal prompts by default. However, once the user has clicked "I don't know all the information required" - we want the Alternate fields from then on.
        // Therefore use the Optimal prompt set UNLESS the PromptSet variable was different.
        String sPromptSet = HttpHelper.getValue(request, Constants.PROMPT_SET);
        if ( sPromptSet.equals("") || sPromptSet.equals(PromptSet.OPTIMAL)) {
            sPromptSet = PromptSet.OPTIMAL;
            
            // back to country page
            request.setAttribute(Constants.BACK_COMMAND, KeyInit.NAME);
            request.setAttribute(Constants.BACK_PROMPT_SET, "");
        } else    // must be a different prompt set so use that
        {
            // back here but with the optimal prompt set instead
            request.setAttribute(Constants.BACK_COMMAND, KeyPromptEntry.NAME);
            request.setAttribute(Constants.BACK_PROMPT_SET, PromptSet.OPTIMAL);
        }
        // pass it through
        request.setAttribute(Constants.PROMPT_SET, sPromptSet);
        
        // displayable attributes that are the output of this command
        String [] asLabels;
        String [] asExamples;
        String [] asSuggestedInputLength;
        
        // business logic to get the above data
        try {
            // create the QuickAddress object - the URL is that of the QuickAddress web service
            QuickAddress searchService = new QuickAddress(Constants.ENDPOINT, request);
            searchService.setEngineType(searchService.KEYFINDER);
            searchService.setFlatten(true);
            String sLayout = (String) request.getSession().getAttribute(Constants.LAYOUT);
            // perform the search pre-check
            
            CanSearch canSearch = searchService.canSearch(sDataId, sLayout);
            
            if ( canSearch.isOk() ) {
                // Physically, a "prompt set" is an array of string/example pairs which
                // are encapsulated in PromptLine objects.
                // These prompt for certain address elements, e.g. "Postcode" and "SW4 0QL"
                // Here, we obtain the promptSet and extract the prompts into asPrompts
                PromptSet promptSet = searchService.getPromptSet( sDataId, sPromptSet);
                PromptLine[] aPrompts = promptSet.getLines();
                
                asLabels= new String[aPrompts.length];
                asExamples = new String[aPrompts.length];
                asSuggestedInputLength = new String[asLabels.length];
                
                // populate the output arrays from the data model
                for (int i = 0; i < asLabels.length; ++i) {
                    asLabels[i] = aPrompts[i].getPrompt();
                    asExamples[i] = aPrompts[i].getExample();
                    asSuggestedInputLength[i] = String.valueOf(aPrompts[i].getSuggestedInputLength());
                }
                
                // set the output arrays as attributes
                request.setAttribute(Constants.LABELS, asLabels);
                request.setAttribute(Constants.EXAMPLES, asExamples);
                request.setAttribute(Constants.SUGGESTED_INPUT_LENGTHS, asSuggestedInputLength);
                
                // use N version of array pass through because we need a minimum number of user inputs
                // for the other prompt set.
                String[] asUserInput = HttpHelper.passThroughArrayN(request, Constants.USER_INPUT, asLabels.length);
                
                // the page that will use this data
                sDestinationPage = Constants.KEY_PROMPT_ENTRY_PAGE;
            } else {
                // QuickAddress is not available for this country
                request.setAttribute(Constants.ROUTE, Constants.ROUTE_PRE_SEARCH_FAILED);
                request.setAttribute(Constants.ERROR_INFO, canSearch.getMessage());
            }
        } catch (Exception e) {
            // dump the exception to error stream
            System.err.println("~~~~~~~ Caught exception in KeyPromptEntry command ~~~~~~~");
            e.printStackTrace();
            System.err.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            
            // go to manual entry page
            request.setAttribute(Constants.ROUTE, Constants.ROUTE_FAILED);
            request.setAttribute(Constants.ERROR_INFO, e.toString());
        }
        
        if (sDestinationPage == null) {   // this is the error or manual entry case
            // go straight to format address
            Command formatAddress = new KeyFormatAddress();
            sDestinationPage = formatAddress.execute(request, response);
        }
        
        return sDestinationPage;
    }
}
