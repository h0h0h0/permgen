/* ----------------------------------------------------------------------------
 * QAS On Demand > (c) QAS Ltd > www.qas.com
 *
 * Intranet > Rapid Addressing > Standard > RapidSearch.java
 * Perform searching commands
 * ----------------------------------------------------------------------------
 */
package com.qas.newmedia.internet.ondemand.product.proweb.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Stack;

import com.qas.newmedia.internet.ondemand.product.proweb.Picklist;
import com.qas.newmedia.internet.ondemand.product.proweb.PicklistItem;
import com.qas.newmedia.internet.ondemand.product.proweb.PromptSet;
import com.qas.newmedia.internet.ondemand.product.proweb.QasException;
import com.qas.newmedia.internet.ondemand.product.proweb.QuickAddress;

/**
 * Command encapsulating search, stepIn & refinement functionality.
 * Input params/Output attributes:
 *    DataID, SearchEngine, UserInput, Route, Moniker, Partial, Postcode, Score, StepInWarning,
 *    MonikerHistory[], PartialHistory[], PostcodeHistory[], ScoreHistory[]
 * Output only attributes:
 *    Prompt, PickMonikers[], PickFunctions[], PickTexts[], PickPostcodes[], PickScores[], PickPartials[],
 *    PickIsInfos[], PickIsAlias[], PickWarnings[], BackCommand, ErrorInfo (if exception caught)
 */
public class RapidSearch implements Command {
    public static final String NAME = "RapidSearch";

        /* (non-Javadoc)
         * @see com.qas.ondemand_2011_03.servlet.Command#execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
         */
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        /** Handle query parameters & request attributes **/

        // Paramaters to pass through
        HttpHelper.passThrough(request, Constants.CALLBACK_FUNCTION);
        String sDataID = HttpHelper.passThrough(request, Constants.DATA_ID);
        String sSearchEngine = HttpHelper.passThrough(request, Constants.SEARCH_ENGINE);

        // Reconstruct picklist history stacks
        Stack monikerHistory = HttpHelper.requestArrayToStack(request, Constants.MONIKER_HISTORY);
        Stack pickTextHistory = HttpHelper.requestArrayToStack(request, Constants.PICKTEXT_HISTORY);
        Stack postcodeHistory = HttpHelper.requestArrayToStack(request, Constants.POSTCODE_HISTORY);
        Stack scoreHistory = HttpHelper.requestArrayToStack(request, Constants.SCORE_HISTORY);
        Stack refineHistory = HttpHelper.requestArrayToStack(request, Constants.REFINE_HISTORY);

        // Values for this operation

        // Moniker of current picklist
        String sMoniker = HttpHelper.getValue(request, Constants.MONIKER);
        // Search/refinement text
        String sUserInput = HttpHelper.getValue(request, Constants.USER_INPUT);
        // Route indicates the initial action
        String sRoute = HttpHelper.getValue(request, Constants.ROUTE);

        // Working variables
        boolean bGoNextPage = false;			// whether to skip over this page straight to the final address
        boolean bStepPastClose = false;		// have we auto-stepped past close matches?
        boolean bCrossBorder = false;			// have we auto-stepped through a cross-border warning?
        boolean bPostRecode = false; 			// have we auto-stepped through a postcode recode?

        // Are we updating the picklist directly, or dispatching to the main outer page?
        boolean bPicklistUpdate = sRoute.equals(Constants.ROUTE_UPDATE);
        // First major search action? Or just initial update, picklist update or step-in
        boolean bInitial = (monikerHistory.size() == 0 && !bPicklistUpdate && !sRoute.equals(Constants.ROUTE_INIT));
        // Did we just step into an informational?
        boolean bInfoStepin = HttpHelper.getValue(request, Constants.STEPIN_WARNING).equals(Constants.WARN_INFO);

        /** Handle actions specified by the route **/

        if (sRoute.equals(Constants.ROUTE_INIT)) {
            // Empty the history
            monikerHistory.clear();
            pickTextHistory.clear();
            postcodeHistory.clear();
            scoreHistory.clear();
            refineHistory.clear();
            sMoniker = "";
        } else if (sRoute.equals(Constants.ROUTE_BACK)) {
            // Remove the (redundant) top item then peek at the (new) top of the history
            monikerHistory.pop();
            pickTextHistory.pop();
            postcodeHistory.pop();
            scoreHistory.pop();
            sUserInput = (String) refineHistory.pop();
            sMoniker = (String) monikerHistory.peek();
        } else if (sRoute.equals(Constants.ROUTE_RECREATE)) {
            // Recreate picklist from top item in history (and sUserInput)
            sMoniker = (String) monikerHistory.peek();
        }

        /** Perform search **/

        try {
            // Create QuickAddress search object
			QuickAddress searchService = new QuickAddress(Constants.ENDPOINT, request);
            Picklist picklist = null;

            // Main search logic
            if (sMoniker.equals("")) {
                /** Initial search using country and input **/

                searchService.setEngineType(sSearchEngine);
                searchService.setFlatten(false);
                picklist = searchService.search(sDataID, sUserInput, PromptSet.DEFAULT).getPicklist();	// throws QasException
                sMoniker = picklist.getMoniker();

                // Initialise the history when doing a full initial search
                if (bInitial) {
                    monikerHistory.push(sMoniker);
                    pickTextHistory.push("Searching on... '" + sUserInput + "'");
                    postcodeHistory.push("");
                    scoreHistory.push("0");
                    refineHistory.push(sUserInput);
                    sUserInput = "";
                }
            } else {
                /** Refine/step-in/recreate picklist using moniker and input **/

                // If stepping-in, add to history (if not already present)
                if (!bPicklistUpdate &&
                        (monikerHistory.size() == 0 || !monikerHistory.peek().toString().equals(sMoniker))) {
                    // Pick up values from last stepped-in picklist item, sent through by browser javascript
                    monikerHistory.push(sMoniker);
                    pickTextHistory.push(HttpHelper.getValue(request, Constants.PICKTEXT));
                    postcodeHistory.push(HttpHelper.getValue(request, Constants.POSTCODE));
                    scoreHistory.push(HttpHelper.getValue(request, Constants.SCORE));
                    refineHistory.push(sUserInput);

                    // Stepping in, so clear refine text, UNLESS it was an informational (i.e. 'Click to Show All')
                    if (!bInfoStepin) {
                        sUserInput = "";
                    }
                }

                // PS: step-in is really just a refinement with empty refine text
                picklist = searchService.refine(sMoniker, sUserInput);									// throws QasException
            }

            /** Perform automatic step-ins **/

            if (bInitial) {
                // Auto-step-in logic
                while (picklist.isAutoStepinSafe() || picklist.isAutoStepinPastClose()) {
                    sMoniker = picklist.getItems()[0].getMoniker();
                    bStepPastClose |= picklist.isAutoStepinPastClose();
                    bCrossBorder |= picklist.getItems()[0].isCrossBorderMatch();
                    bPostRecode |= picklist.getItems()[0].isPostcodeRecoded();

                    // Add this step-in to history
                    monikerHistory.push(sMoniker);
                    pickTextHistory.push(picklist.getItems()[0].getText());
                    postcodeHistory.push(picklist.getItems()[0].getPostcode());
                    scoreHistory.push(Integer.toString(picklist.getItems()[0].getScore()));
                    refineHistory.push("");

                    picklist = searchService.stepIn(sMoniker);											// throws QasException
                }

                // Auto-formatting logic
                if (picklist.isAutoFormatSafe() || picklist.isAutoFormatPastClose()) {
                    sMoniker = picklist.getItems()[0].getMoniker();
                    bStepPastClose |= picklist.isAutoFormatPastClose();
                    bCrossBorder |= picklist.getItems()[0].isCrossBorderMatch();
                    bPostRecode |= picklist.getItems()[0].isPostcodeRecoded();

                    request.setAttribute(Constants.ROUTE, Constants.ROUTE_NORMAL);
                    bGoNextPage = true;
                }
            }

            /** Build picklist output arrays **/

            if (!bGoNextPage) {
                int iSize = picklist.getItems().length;

                String[] asPickMonikers = new String[iSize];
                String[] asPickOperations = new String[iSize];
                String[] asPickTypes = new String[iSize];
                String[] asPickWarnings = new String[iSize];

                String[] asPickTexts = new String[iSize];
                String[] asPickPostcodes = new String[iSize];
                String[] asPickScores = new String[iSize];
                String[] asPickPartials = new String[iSize];

                for (int i = 0; i < iSize; ++i) {
                    PicklistItem item = picklist.getItems()[i];

                    asPickMonikers[i] = item.getMoniker();
                    asPickTexts[i] = item.getText();
                    asPickScores[i] = Integer.toString(item.getScore());
                    asPickPostcodes[i] = item.getPostcode();
                    asPickPartials[i] = item.getPartialAddress();
                    asPickWarnings[i] = item.isCrossBorderMatch() ? Constants.WARN_CROSSBORDER
                            : item.isPostcodeRecoded() ? Constants.WARN_POSTCODERECODE
                            : "";

                    // Operation: what to do if they click on the item
                    if (item.canStep()) {
                        asPickOperations[i] = Constants.OP_STEP_IN;
                    } else if (item.isFullAddress()) {
                        asPickOperations[i] = item.isInformation() ? Constants.OP_FORCE_FORMAT : Constants.OP_FORMAT;
                    } else if (item.isUnresolvableRange()) {
                        asPickOperations[i] = Constants.OP_HALT_RANGE;
                    } else if (item.isIncompleteAddress()) {
                        asPickOperations[i] = Constants.OP_HALT_INCOMPLETE;
                    } else {
                        asPickOperations[i] = Constants.OP_NONE;
                    }

                    // Type: indicates the type of icon to display (used in combination with the operation)
                    if (item.isInformation()) {
                        asPickTypes[i] = item.isWarnInformation() ? Constants.TYPE_INFO_WARN : Constants.TYPE_INFO;
                        asPickWarnings[i] = Constants.WARN_INFO;
                    } else if (item.isDummyPOBox()) {
                        asPickTypes[i] = Constants.TYPE_POBOX;
                    } else if (item.isName()) {
                        asPickTypes[i] = item.isAliasMatch() ? Constants.TYPE_NAME_ALIAS : Constants.TYPE_NAME;
                    } else if (item.isAliasMatch() || item.isCrossBorderMatch() || item.isPostcodeRecoded()) {
                        asPickTypes[i] = Constants.TYPE_ALIAS;
                    } else {
                        asPickTypes[i] = Constants.TYPE_STANDARD;
                    }
                }

                /** Set the picklist output attributes */
                request.setAttribute(Constants.PROMPT, picklist.getPrompt());
                request.setAttribute(Constants.PICK_TOTAL, Integer.toString(picklist.getTotal()));

                request.setAttribute(Constants.PICK_MONIKERS, asPickMonikers);
                request.setAttribute(Constants.PICK_FUNCTIONS, asPickOperations);
                request.setAttribute(Constants.PICK_TEXTS, asPickTexts);
                request.setAttribute(Constants.PICK_POSTCODES, asPickPostcodes);
                request.setAttribute(Constants.PICK_SCORES, asPickScores);
                request.setAttribute(Constants.PICK_PARTIALS, asPickPartials);
                request.setAttribute(Constants.PICK_TYPES, asPickTypes);
                request.setAttribute(Constants.PICK_WARNINGS, asPickWarnings);
            }

            request.setAttribute(Constants.USER_INPUT, sUserInput);


            /** Set the standard output attributes (main page only) */
            if (!bPicklistUpdate) {
                request.setAttribute(Constants.MONIKER, sMoniker);
                request.setAttribute(Constants.IS_DYNAMIC, (new Boolean(true)).toString());
                // Back button destination: what is the previous page
                request.setAttribute(Constants.BACK_COMMAND, (monikerHistory.size() > 1) ? RapidSearch.NAME : RapidInit.NAME);
                // Auto-step warning to display on screen
                request.setAttribute(Constants.STEPIN_WARNING, bStepPastClose ? Constants.WARN_STEPPEDPASTCLOSE
                        : bCrossBorder ? Constants.WARN_CROSSBORDER
                        : bPostRecode ? Constants.WARN_POSTCODERECODE
                        : HttpHelper.getValue(request, Constants.STEPIN_WARNING));

                request.setAttribute(Constants.MONIKER_HISTORY, HttpHelper.toStringArray(monikerHistory));
                request.setAttribute(Constants.PICKTEXT_HISTORY, HttpHelper.toStringArray(pickTextHistory));
                request.setAttribute(Constants.POSTCODE_HISTORY, HttpHelper.toStringArray(postcodeHistory));
                request.setAttribute(Constants.SCORE_HISTORY, HttpHelper.toStringArray(scoreHistory));
                request.setAttribute(Constants.REFINE_HISTORY, HttpHelper.toStringArray(refineHistory));
            }
        } catch (Exception e) {
            // Dump the exception to error stream
            System.err.println("-------- Caught exception in RapidSearch command --------");
            e.printStackTrace();
            System.err.println("---------------------------------------------------------");

            if (e instanceof QasException) {
            	QasException qe = (QasException)e;
            	request.setAttribute(Constants.ERROR_CODE, qe.getErrorNumber());
            }

            // If a problem then move to manual address entry page
            bGoNextPage = true;
            request.setAttribute(Constants.ROUTE, Constants.ROUTE_FAILED);
            request.setAttribute(Constants.ERROR_INFO, e.getMessage());
        }


        if (bGoNextPage) {
            // Handle error and straight-to-final-address cases
            Command nextCmd = new RapidAddress();
            return nextCmd.execute(request, response);
        } else if (bPicklistUpdate) {
            // Update just the picklist, directly
            return Constants.RAPID_PICKLIST_FRAME;
        } else {
            // Dispatch to main outer page
            return Constants.RAPID_SEARCH_PAGE;
        }
    }
}
