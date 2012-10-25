/* ----------------------------------------------------------------------------
 * QAS On Demand > (c) QAS Ltd > www.qas.com
 *
 * Intranet > Rapid Addressing > Standard > RapidInit.java
 * Initialise the searching page
 * ----------------------------------------------------------------------------
 */
package com.qas.newmedia.internet.ondemand.product.proweb.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Stack;

import com.qas.newmedia.internet.ondemand.product.proweb.CanSearch;
import com.qas.newmedia.internet.ondemand.product.proweb.PromptSet;
import com.qas.newmedia.internet.ondemand.product.proweb.QasException;
import com.qas.newmedia.internet.ondemand.product.proweb.QuickAddress;
import com.qas.ondemand_2011_03.*;

/**
 * Initial (new) search logic, goes to main rapidSearch page.
 * Checks for availability of searching and gets initial prompt, picklist, match count, etc.
 * Output attributes:
 *    DataID, SearchEngine, UserInput, Moniker, Prompt, IsDynamic; Route & ErrorInfo (if exception caught)
 */
public class RapidInit implements Command {
    public static final String NAME = "RapidInit";

        /* (non-Javadoc)
         * @see com.qas.ondemand_2011_03.servlet.Command#execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
         */
    public String execute(
            HttpServletRequest request,
            HttpServletResponse response) {
        HttpHelper.passThrough(request, Constants.CALLBACK_FUNCTION);
        String sDataID = HttpHelper.getValue(request, Constants.DATA_ID);
        String sSearchEngine = HttpHelper.getValue(request, Constants.SEARCH_ENGINE);

        String sUserInput = HttpHelper.getValue(request, Constants.USER_INPUT);
        Stack refineHistory = HttpHelper.requestArrayToStack(request, Constants.REFINE_HISTORY);

        boolean bOkay = false, bSearch = false;

        if (!sSearchEngine.equals(QuickAddress.SINGLELINE) && !sSearchEngine.equals(QuickAddress.KEYFINDER)) {
            sSearchEngine = QuickAddress.TYPEDOWN;
        }
        if (HttpHelper.getValue(request, Constants.ROUTE).equals(Constants.ROUTE_INIT)) {
            // Blank the search text if requested to do a New search
            sUserInput = "";
        } else if (refineHistory.size() > 0) {
            // Retrieve previous search text from the top of the history
            sUserInput = (String) refineHistory.firstElement();
        }

        request.setAttribute(Constants.MONIKER, "");
        request.setAttribute(Constants.DATA_ID, sDataID);
        request.setAttribute(Constants.SEARCH_ENGINE, sSearchEngine);
        request.setAttribute(Constants.USER_INPUT, sUserInput);

        try {
            // Create QuickAddress search object
			QuickAddress searchService = new QuickAddress(Constants.ENDPOINT, request);

            // Check user will be able to complete a search of the data using this engine and layout
            //  - highlights problems with data availability, age, licencing, meters, layout
            searchService.setEngineType(sSearchEngine);
            searchService.getEngine().setFlatten(false);
            searchService.setFlatten(false);
            String sLayout = (String) request.getSession().getAttribute(Constants.LAYOUT);
            CanSearch result = searchService.canSearch(sDataID, sLayout);
            bOkay = result.isOk();

            if (!bOkay) {
                request.setAttribute(Constants.ROUTE, Constants.ROUTE_PRE_SEARCH_FAILED);
                request.setAttribute(Constants.ERROR_INFO, result.getMessage());
            } else {
                // Get initial prompt
                PromptSet prompts = searchService.getPromptSet(sDataID, PromptSet.DEFAULT);

                if (prompts.isSearchingDynamic()) {
                    // Send to the search page to display initial dynamic picklist
                    request.setAttribute(Constants.ROUTE, Constants.ROUTE_INIT);
                    bSearch = true;
                } else {
                    // Pass values through to page
                    request.setAttribute(Constants.PROMPT, prompts.getLines()[0].getPrompt());
                    request.setAttribute(Constants.IS_DYNAMIC, (new Boolean(prompts.isSearchingDynamic())).toString());
                    request.setAttribute(Constants.PICK_TOTAL, "0");
                }
            }
        } catch ( Exception e ) {
            // dump the exception to error stream
            System.err.println("-------- Caught exception in RapidInit command --------");
            e.printStackTrace();
            System.err.println("-------------------------------------------------------");

            if (e instanceof QasException) {
            	QasException qe = (QasException)e;
            	request.setAttribute(Constants.ERROR_CODE, qe.getErrorNumber());
            }

            // If a problem then move to manual address entry page
            bOkay = false;
            request.setAttribute(Constants.ROUTE, Constants.ROUTE_FAILED);
            request.setAttribute(Constants.ERROR_INFO, e.getMessage());
        }

        if (bSearch) {
            Command nextCmd = new RapidSearch();
            return nextCmd.execute(request, response);
        } else if (!bOkay) {
            Command nextCmd = new RapidAddress();
            return nextCmd.execute(request, response);
        } else {
            return Constants.RAPID_SEARCH_PAGE;
        }
    }
}
