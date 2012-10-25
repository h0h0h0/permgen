/* ----------------------------------------------------------------------------
 * QAS On Demand
 * (c) 2004 QAS Ltd.
 * File: HierSearch.java
 * ----------------------------------------------------------------------------
 */
package com.qas.newmedia.internet.ondemand.product.proweb.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Stack;

import com.qas.newmedia.internet.ondemand.product.proweb.CanSearch;
import com.qas.newmedia.internet.ondemand.product.proweb.Picklist;
import com.qas.newmedia.internet.ondemand.product.proweb.PicklistItem;
import com.qas.newmedia.internet.ondemand.product.proweb.PromptSet;
import com.qas.newmedia.internet.ondemand.product.proweb.QasException;
import com.qas.newmedia.internet.ondemand.product.proweb.QuickAddress;

/**
 * Command encapsulating search, stepIn & refinement functionality for hierarchical case.
 * Input Params/Output attributes: DataId, UserInput, CountryName, Route, Moniker, MonikerHistory[], Partial, PartialHistory[]
 * Postcode, PostcodeHistory[], Score, ScoreHistory[], RefineInput,
 * Output only attributes: Prompt,PickMonikers[], PickFunctions[], PickText[], PickPostcodes[], PickScores[], PickPartials[],
 * PickIsInfos[], PickIsAlias[], PickWarnings[], BackCommand, StepinWarning, ErrorInfo (if exception caught)
 */
public class HierSearch implements Command {
    public static final String NAME = "HierSearch";

        /* (non-Javadoc)
         * @see com.qas.ondemand_2011_03.servlet.Command#execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
         */
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        /** Handle query parameters & request attributes **/
        // Paramaters to pass through
        String sDataId = HttpHelper.passThrough(request, Constants.DATA_ID);
        String sUserInput = HttpHelper.passThrough(request, Constants.USER_INPUT);
        HttpHelper.passThrough(request, Constants.COUNTRY_NAME);
        String sRequestTag = 
        	HttpHelper.passThrough(request, Constants.REQUEST_TAG);

        // Reconstruct history stacks
        Stack monikerHistory = HttpHelper.requestArrayToStack(request, Constants.MONIKER_HISTORY);
        Stack displayHistory = HttpHelper.requestArrayToStack(request, Constants.PARTIAL_HISTORY);
        Stack postcodeHistory = HttpHelper.requestArrayToStack(request, Constants.POSTCODE_HISTORY);
        Stack scoreHistory = HttpHelper.requestArrayToStack(request, Constants.SCORE_HISTORY);

        // Local variables
        String sRoute = HttpHelper.getValue(request, Constants.ROUTE);
        boolean bGoNextPage = false;		// whether to skip over this page straight to the next one
        boolean bStepPastClose = false;	// have we auto-stepped past close matches?
        boolean bCrossBorder = false;		// have we auto-stepped through a cross-border warning?
        boolean bPostRecode = false; 		// have we auto-stepped through a postcode recode?

        /** Values for this operation **/
        String sMoniker;					// moniker of current picklist
        String sPartial;					// title of current picklist; partial address
        String sUserRefine;					// user-entered refinement text

        if (Constants.ROUTE_BACK.equals(sRoute)) {
            // Remove the (redundant) top item then peek at the (new) top of the history
            monikerHistory.pop();
            displayHistory.pop();
            postcodeHistory.pop();
            scoreHistory.pop();
            sRoute = Constants.ROUTE_RECREATE;
        }

        if (Constants.ROUTE_RECREATE.equals(sRoute)) {
            // Recreate picklist from top item in history
            sMoniker = (String) monikerHistory.peek();
            sPartial = (String) displayHistory.peek();
            sUserRefine = "";
        } else // Pick up search parameters
        {
            sMoniker = HttpHelper.getValue(request, Constants.MONIKER);
            sPartial = HttpHelper.getValue(request, Constants.PARTIAL);
            sUserRefine = HttpHelper.getValue(request, Constants.REFINE_INPUT);
        }


        /** Perform search **/
        try {
            // Create QuickAddress search object
            QuickAddress searchService = new QuickAddress(Constants.ENDPOINT, request);
            Picklist picklist = null;

            // Main search logic
            if (!sMoniker.equals("")) {
                // Add to history if not already on the top
                if (!monikerHistory.peek().toString().equals(sMoniker)) {
                    monikerHistory.push(sMoniker);
                    displayHistory.push(sPartial);
                    postcodeHistory.push(HttpHelper.getValue(request, Constants.POSTCODE));
                    scoreHistory.push(HttpHelper.getValue(request, Constants.SCORE));
                }

                /** Refine using moniker and refine text **/
                if (!sUserRefine.equals("")) {
                    // Add space to ensure we only get perfect matches
                	picklist = searchService.refine(sMoniker,
							 						sUserRefine +
							 						" ",
							 						sRequestTag);// throws QasException

                    // Auto-step-in logic - if a solo step-in item
                    if (picklist.isAutoStepinSingle()) {
                        sMoniker = picklist.getItems()[0].getMoniker();
                        sPartial = picklist.getItems()[0].getPartialAddress();
                        bCrossBorder |= picklist.getItems()[0].isCrossBorderMatch();
                        bPostRecode |= picklist.getItems()[0].isPostcodeRecoded();

                        // Add this step-in to history
                        monikerHistory.push(sMoniker);
                        displayHistory.push(picklist.getItems()[0].getText());
                        postcodeHistory.push(picklist.getItems()[0].getPostcode());
                        scoreHistory.push(Integer.toString(picklist.getItems()[0].getScore()));

                        sUserRefine = "";
                        picklist = searchService.stepIn(sMoniker);										// throws QasException
                    }
                    // Auto-format logic - if a solo address item
                    else if (picklist.isAutoFormatSingle()) {
                        sMoniker = picklist.getItems()[0].getMoniker();
                        bCrossBorder |= picklist.getItems()[0].isCrossBorderMatch();
                        bPostRecode |= picklist.getItems()[0].isPostcodeRecoded();
                        bGoNextPage = true;
                    }
                }
                /** Step-in/recreate picklist from moniker **/
                else {
                    picklist = searchService.stepIn(sMoniker);											// throws QasException
                }

                // set the route for these cases
                request.setAttribute(Constants.ROUTE, Constants.ROUTE_NORMAL);
            }
            /** Initial search using country and input **/
            else if (!sDataId.equals("") && !sUserInput.equals("")) {
                searchService.setEngineType(QuickAddress.SINGLELINE);
                searchService.setFlatten(true);
                String sLayout = (String) request.getSession().getAttribute(Constants.LAYOUT);
                CanSearch canSearch = 
                	searchService.canSearch(sDataId, sLayout, PromptSet.ONELINE);
                // Perform pre-search check
                if ( !canSearch.isOk() ) {   // Cannot search on this country and layout, so go straight to address page
                    bGoNextPage = true;
                    request.setAttribute(Constants.ROUTE, Constants.ROUTE_PRE_SEARCH_FAILED);
                    request.setAttribute(Constants.ERROR_INFO, canSearch.getMessage());
                } else {
                    // We can search as normal
                	picklist = searchService.
                	search(sDataId, sUserInput, PromptSet.ONELINE, null, sRequestTag).
                		getPicklist();	// throws QasException
                    sMoniker = picklist.getMoniker();
                    sPartial = "Searching on... '" + sUserInput + "'";

                    // Initialise the history
                    monikerHistory.clear();
                    displayHistory.clear();
                    postcodeHistory.clear();
                    scoreHistory.clear();
                    monikerHistory.push(sMoniker);
                    displayHistory.push(sPartial);
                    postcodeHistory.push("");
                    scoreHistory.push("0");

                    // Auto-step-in logic
                    while (picklist.isAutoStepinSafe() || picklist.isAutoStepinPastClose()) {
                        sMoniker = picklist.getItems()[0].getMoniker();
                        sPartial = picklist.getItems()[0].getPartialAddress();
                        bStepPastClose |= picklist.isAutoStepinPastClose();
                        bCrossBorder |= picklist.getItems()[0].isCrossBorderMatch();
                        bPostRecode |= picklist.getItems()[0].isPostcodeRecoded();

                        // Add this step-in to history
                        monikerHistory.push(sMoniker);
                        displayHistory.push(picklist.getItems()[0].getText());
                        postcodeHistory.push(picklist.getItems()[0].getPostcode());
                        scoreHistory.push(Integer.toString(picklist.getItems()[0].getScore()));

                        picklist = searchService.stepIn(sMoniker);											// throws QasException
                    }

                    // Auto-formatting logic
                    if (picklist.isAutoFormatSafe() || picklist.isAutoFormatPastClose()) {
                        sMoniker = picklist.getItems()[0].getMoniker();
                        bStepPastClose |= picklist.isAutoFormatPastClose();
                        bCrossBorder |= picklist.getItems()[0].isCrossBorderMatch();
                        bPostRecode |= picklist.getItems()[0].isPostcodeRecoded();
                        bGoNextPage = true;
                    }

                    /** [RM] 27.03.08 **/
                    if (picklist.isMaxMatches()) {
                        bGoNextPage = true;
                        request.setAttribute(Constants.ROUTE, Constants.ROUTE_TOO_MANY);
                    } else {
	                    // set the route as success
                    	request.setAttribute(Constants.ROUTE, Constants.ROUTE_NORMAL);
                    }
                }
            }

            /** Build picklist output arrays **/
            if (!bGoNextPage) {
                int iSize = picklist.getItems().length;

                String[] asPickMonikers = new String[iSize];
                String[] asPickOperations = new String[iSize];
                String[] asPickTexts = new String[iSize];
                String[] asPickPartials = new String[iSize];
                String[] asPickPostcodes = new String[iSize];
                String[] asPickScores = new String[iSize];
                String[] asPickIsInfos = new String[iSize];
                String[] asPickIsAlias = new String[iSize];
                String[] asPickWarnings = new String[iSize];

                for (int iIndex = 0; iIndex < iSize; ++iIndex) {
                    PicklistItem item = picklist.getItems()[iIndex];

                    asPickMonikers[iIndex] = item.getMoniker();
                    asPickTexts[iIndex] = item.getText();
                    asPickScores[iIndex] = Integer.toString(item.getScore());
                    asPickPostcodes[iIndex] = item.getPostcode();
                    asPickPartials[iIndex] = item.getPartialAddress();
                    asPickIsInfos[iIndex] = ( new Boolean(item.isInformation()) ).toString();
                    asPickWarnings[iIndex] = item.isCrossBorderMatch() ? Constants.WARN_CROSSBORDER
                            : item.isPostcodeRecoded() ? Constants.WARN_POSTCODERECODE : "";
                    asPickIsAlias[iIndex] = ( new Boolean(item.isAliasMatch()) ).toString();

                    if (item.canStep()) {
                        asPickOperations[iIndex] = Constants.OP_STEP_IN;
                    } else if (item.isFullAddress()) {
                        asPickOperations[iIndex] = Constants.OP_FORMAT;
                    } else if (item.isUnresolvableRange()) {
                        asPickOperations[iIndex] = Constants.OP_HALT_RANGE;
                    } else if (item.isIncompleteAddress()) {
                        asPickOperations[iIndex] = Constants.OP_HALT_INCOMPLETE;
                    } else // All else
                    {
                        asPickOperations[iIndex] = Constants.OP_NONE;
                    }
                }

                /** Set the picklist output attributes */
                request.setAttribute(Constants.PARTIAL, sPartial);
                request.setAttribute(Constants.REFINE_INPUT, sUserRefine);
                request.setAttribute(Constants.PROMPT, picklist.getPrompt());

                request.setAttribute(Constants.PICK_MONIKERS, asPickMonikers);
                request.setAttribute(Constants.PICK_FUNCTIONS, asPickOperations);
                request.setAttribute(Constants.PICK_TEXTS, asPickTexts);
                request.setAttribute(Constants.PICK_POSTCODES, asPickPostcodes);
                request.setAttribute(Constants.PICK_SCORES, asPickScores);
                request.setAttribute(Constants.PICK_PARTIALS, asPickPartials);
                request.setAttribute(Constants.PICK_ISINFOS, asPickIsInfos);
                request.setAttribute(Constants.PICK_ISALIAS, asPickIsAlias);
                request.setAttribute(Constants.PICK_WARNINGS, asPickWarnings);
            }

            /** Set the standard output attributes (route was set above) */
            request.setAttribute(Constants.MONIKER, sMoniker);
            // Back button destination: what is the previous page
            request.setAttribute(Constants.BACK_COMMAND, (monikerHistory.size() > 1) ? HierSearch.NAME : HierInit.NAME);
            // Auto-step warning
            request.setAttribute(Constants.STEPIN_WARNING, bStepPastClose ? Constants.WARN_STEPPEDPASTCLOSE
                    : bCrossBorder ? Constants.WARN_CROSSBORDER
                    : bPostRecode ? Constants.WARN_POSTCODERECODE
                    : HttpHelper.getValue(request, Constants.STEPIN_WARNING));

            request.setAttribute(Constants.MONIKER_HISTORY, HttpHelper.toStringArray(monikerHistory));
            request.setAttribute(Constants.PARTIAL_HISTORY, HttpHelper.toStringArray(displayHistory));
            request.setAttribute(Constants.POSTCODE_HISTORY, HttpHelper.toStringArray(postcodeHistory));
            request.setAttribute(Constants.SCORE_HISTORY, HttpHelper.toStringArray(scoreHistory));
        } catch (Exception e) {
            // dump the exception to error stream
            System.err.println("~~~~~~~~~ Caught exception in HierSearch command ~~~~~~~~~~");
            e.printStackTrace();
            System.err.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

            if (e instanceof QasException) {
            	QasException qe = (QasException)e;
            	request.setAttribute(Constants.ERROR_CODE, qe.getErrorNumber());
            }

            // If a problem then move to manual address entry page
            bGoNextPage = true;
            request.setAttribute(Constants.ROUTE, Constants.ROUTE_FAILED);
            request.setAttribute(Constants.ERROR_INFO, e.toString());
        }

        /** Handle error and straight-to-final-address cases **/
        if (bGoNextPage) {
            Command nextCmd = new HierAddress();
            return nextCmd.execute(request, response);
        }

        return Constants.HIER_SEARCH_PAGE;
    }
}
