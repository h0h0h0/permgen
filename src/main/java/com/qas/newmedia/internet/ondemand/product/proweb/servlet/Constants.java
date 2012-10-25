/* ----------------------------------------------------------------------------
 * QAS On Demand > (c) QAS Ltd > www.qas.com
 *
 * All > Constants.java
 * Define field names and parameter values used throughout the scenarios
 * ----------------------------------------------------------------------------
 */
package com.qas.newmedia.internet.ondemand.product.proweb.servlet;

import com.qas.newmedia.internet.ondemand.product.proweb.QuickAddress;

/**
 * Constant strings used in the pages to transfer data between web server and browser -
 * the names of attributes/query parameters/hidden fields and their values
 */
public final class Constants {
/// Values that may be set externally (e.g. from servlet context params)

    /** The URL of the QAS ProWeb web service, including port number. e.g. "http://myserver:2021" */
    public static String ENDPOINT = null;
    /** The path of the application*/
    public static String PATH = null;

    /** String for the drop list*/
    public static final String DROPLIST = "DropList";

    /** The file name of the countries files*/
    public static final String COUNTRIES_OK_INC = "countries.ok.inc";
    public static final String COUNTRIES_ALL_INC = "countries.all.inc";
    public static final String COUNTRIES_EU_INC = "countries.eu.inc";

    /** String for errors*/
    public static String FILEIOERROR = null;
    public static String SERVERERROR = null;


/// Constant names for data attributes/parameters and values passed around in requests

    /** Three letter code used to identify QuickAddress datasets */
    public static final String DATA_ID = "DataId";
    /** Name of the country selected; displayed as the last line of the address */
    public static final String COUNTRY_NAME = "CountryName";
    /** Search terms entered by the user */
    public static final String USER_INPUT = "UserInput";
    /** Bulk Search Results */
    public static final String BULKSEARCH_RESULT = "BulkSearchResult";
    /** The layout name being used to format final addresses. This is currently, but need not be, the same across scenarios. */
    public static final String LAYOUT = "Layout";
    /** The request tag **/
    public static final String REQUEST_TAG = "RequestTag";


    /** Next command to execute */
    public static final String COMMAND = "Command";
    /** Moniker referring to the address at the current stage in the search process */
    public static final String MONIKER = "Moniker";

    // Route and route values: represent current search state
    /** Name of route attribute/parameter */
    public static final String ROUTE = "Route";
    /** Route: address capture successfully **/
    public static final String ROUTE_NORMAL = "Normal";
    /** Route: address capture failed/exception thrown **/
    public static final String ROUTE_FAILED = "Failed";
    /** Route: search did not produce any matches (flattened) **/
    public static final String ROUTE_NO_MATCHES = "NoMatches";
    /** Route: the country/engine/layout combination is not valid for searching **/
    public static final String ROUTE_PRE_SEARCH_FAILED = "PreSearchFailed";
    /** Route: search timed-out before it completed **/
    public static final String ROUTE_TIMEOUT = "Timeout";
    /** Route: search produced too many matches to display (flattened) **/
    public static final String ROUTE_TOO_MANY = "TooManyMatches";

    /** Route: the address re-submitted is unchanged from that already verified, so don't do so again (verification) **/
    public static final String ROUTE_ALREADY_VERIFIED = "AlreadyVerified";
    /** Route: moving backwards, the history should be trimmed and picklist recreated (rapid addressing) **/
    public static final String ROUTE_BACK = "Back";
    /** Route: initialise whole page using values; don't step-in (rapid addressing standard) **/
    public static final String ROUTE_INIT = "Init";
    /** Route: moving backwards, the picklist should be recreated from the top of the history (rapid addressing) **/
    public static final String ROUTE_RECREATE = "Recreate";
    /** Route: the web page has the country listed as unavailable (rapid addressing) **/
    public static final String ROUTE_UNSUPPORTED_COUNTRY = "UnsupportedCountry";
    /** Route - update the picklist iframe only (rapid addressing standard) **/
    public static final String ROUTE_UPDATE = "Update";

    /** Array of final formatted address lines - this is the final output of all the scenarios */
    public static final String ADDRESS = "Address";
    /** Error message to display as integrator information */
    public static final String ERROR_INFO ="ErrorInfo";

    /** Array of address labels to display (for a formatted address) */
    public static final String LABELS = "Labels";
    /** Array of address lines to display (for a formatted address) */
    public static final String LINES = "Lines";

    /** Command to execute if the "Back" button is pressed */
    public static final String BACK_COMMAND = "BackCommand";
    /** PromptSet value to set if the "Back" button is pressed (flattened) */
    public static final String BACK_PROMPT_SET = "BackPromptSet";

/// Web > Address Capture ('flattened')

    /** Name of prompt set selected/used */
    public static final String PROMPT_SET = "PromptSet";
    /** Array of example lines, for a prompt set, displayed for guidance */
    public static final String EXAMPLES = "Examples";
    /** Array of lengths (in number of chars) suggested for that line of the address input */
    public static final String SUGGESTED_INPUT_LENGTHS = "SuggestedInputLengths";
    /** Moniker of the picklist produced by the initial search */
    public static final String PICKLIST_MONIKER = "PicklistMoniker";
    /** Moniker of the picklist used for refinement */
    public static final String REFINE_MONIKER = "RefineMoniker";
    /** Refinement input text entered by the user*/
    public static final String REFINE_INPUT = "RefineInput";

/// Web > Verification

    /** Original search terms entered by the user */
    public static final String ORIGINAL_INPUT = "OriginalInput";
    /** Verification level for the final address */
    public static final String ADDRESS_INFO = "AddressInfo";

    /** DPVStatus */
    public static final String INFO_DPVNOTCONFIGURED = "DPVNotConfigured";
    /** DPVStatus */
    public static final String INFO_DPVLOCKED = "DPVLocked";
    /** DPVStatus */
    public static final String INFO_DPVNOTCONFIRMED = "DPVNotConfirmed";
    /** DPVStatus */
    public static final String INFO_DPVCONFIRMED = "DPVConfirmed";
    /** DPVStatus */
    public static final String INFO_DPVCONFIRMEDMISSINGSEC = "DPVConfirmedMissingSec";
    /** DPVStatus */
    public static final String INFO_DPVSEEDHIT = "DPVSeedHit";

    public static final String FIELD_DPVSTATUS = "DPVStatus";

/// Intranet > Rapid Addressing - Standard ('rapid') & Single Line ('hierarchical')

    /** Prompt to display, appropriate to current picklist */
    public static final String PROMPT = "Prompt";

    // Information about picklist item selected: pass back to server
    /** 'Partial' address to be displayed (Single Line) */
    public static final String PARTIAL = "Partial";
    /** Display text of item selected */
    public static final String PICKTEXT = "PickText";
    /** Display postcode of item selected */
    public static final String POSTCODE = "Postcode";
    /** Score of item selected */
    public static final String SCORE = "Score";
    /** Warning flag of item selected (WARN_STEPPEDPASTCLOSE .. WARN_FORCEACCEPT) */
    public static final String STEPIN_WARNING = "StepWarning";

    // History stacks
    /** Array representing the history of monikers */
    public static final String MONIKER_HISTORY = "MonikerHistory";
    /** Array representing the history of 'Partial' (Single Line) */
    public static final String PARTIAL_HISTORY = "PartialHistory";
    /** Array representing the history of pick item texts for display */
    public static final String PICKTEXT_HISTORY = "PickTextHistory";
    /** Array representing the history of postcodes for display */
    public static final String POSTCODE_HISTORY = "PostcodeHistory";
    /** Array representing the history of scores for display */
    public static final String SCORE_HISTORY = "ScoreHistory";
    /** Array representing the history of refinement text at the point of step-in */
    public static final String REFINE_HISTORY = "RefineHistory";

    // Picklist items to display (arrays of)
    /** Moniker associated with each item in the picklist */
    public static final String PICK_MONIKERS = "PickMonikers";
    /** Javascript function to call per picklist item */
    public static final String PICK_FUNCTIONS = "PickFunctions";
    /** Display: text for each item of the picklist */
    public static final String PICK_TEXTS = "PickTexts";
    /** Display: postcode for each item in the picklist (can be blank) */
    public static final String PICK_POSTCODES = "PickPostcodes";
    /** Display: quality score for each item in the picklist (can be blank) */
    public static final String PICK_SCORES = "PickScores";
    /** Partial address for each item in the picklist */
    public static final String PICK_PARTIALS = "PickPartials";
    /** Warning flag for stepping into picklist item (WARN_STEPPEDPASTCLOSE .. WARN_FORCEACCEPT) */
    public static final String PICK_WARNINGS = "PickWarnings";

    /** Whether each picklist item is an alias match (icon choice: Single Line) */
    public static final String PICK_ISALIAS = "PickIsAlias";
    /** Whether each picklist item is an informational (icon choice: Single Line) */
    public static final String PICK_ISINFOS = "PickIsInfos";
    /** What type of picklist item it is (icon choice: Standard) (TYPE_ALIAS .. TYPE_POBOX) */
    public static final String PICK_TYPES = "PickTypes";

    // Operations on picklist items (& name of JavaScript handling function)
    /** PICK_FUNCTIONS: Force-accept an unrecognised address (Standard scenario) */
    public static final String OP_FORCE_FORMAT = "ForceFormat";
    /** PICK_FUNCTIONS: Select a final address */
    public static final String OP_FORMAT = "Format";
    /** PICK_FUNCTIONS: Step in to a picklist */
    public static final String OP_STEP_IN = "StepIn";
    /** PICK_FUNCTIONS: Cannot be stepped into - an unresolvable range */
    public static final String OP_HALT_RANGE = "HaltRange";
    /** PICK_FUNCTIONS: Cannot be stepped into - an incomplete address */
    public static final String OP_HALT_INCOMPLETE = "HaltIncomplete";
    /** PICK_FUNCTIONS: Cannot be acted on */
    public static final String OP_NONE = "";

    // Warning values (for STEPIN_WARNING, PICK_WARNINGS)
    /** Locality of item selected is in a bordering area to the locality entered */
    public static final String WARN_CROSSBORDER = "Bordering";
    /** Unrecognised address was force-accepted */
    public static final String WARN_FORCEACCEPT = "ForceAccept";
    /** Informational item - allows server to modify behaviour */
    public static final String WARN_INFO = "Info";
    /** Address elements have overflowed the layout */
    public static final String WARN_OVERFLOW = "Overflow";
    /** Postcode of item selected is different from entered postcode, as it has been recoded */
    public static final String WARN_POSTCODERECODE = "Recode";
    /** Close matches have been automatically stepped past */
    public static final String WARN_STEPPEDPASTCLOSE = "PastClose";
    /** Address elements have been truncated by the layout */
    public static final String WARN_TRUNCATE = "Truncate";

/// Intranet > Rapid Addressing - Standard ('rapid')

    /** Name of the client's function to call on completion */
    public static final String CALLBACK_FUNCTION = "Callback";
    /** Currently selected search engine */
    public static final String SEARCH_ENGINE = "Engine";
    public static final String TYPEDOWN = QuickAddress.TYPEDOWN;
    public static final String SINGLELINE= QuickAddress.SINGLELINE;
    public static final String KEYSEARCH= QuickAddress.KEYFINDER;
    /** Should the search be automatically submitted after a pause in typing */
    public static final String IS_DYNAMIC = "Dynamic";
    /** The total number of items represented by this picklist (not necessarily all shown) */
    public static final String PICK_TOTAL = "PickTotal";

    // Possible picklist item types (affects icon choice) (for PICK_TYPES)
    /** Picklist item is an alias (synonym) */
    public static final String TYPE_ALIAS = "Alias";
    /** Picklist item is an informational */
    public static final String TYPE_INFO = "Info";
    /** Picklist item is a warning informational */
    public static final String TYPE_INFO_WARN = "InfoWarn";
    /** Picklist item is a name/person */
    public static final String TYPE_NAME = "Name";
    /** Picklist item is a name alias (i.e. forename synonym) */
    public static final String TYPE_NAME_ALIAS = "NameAlias";
    /** Picklist item is a PO Box grouping */
    public static final String TYPE_POBOX = "POBox";
    /** Picklist item is standard */
    public static final String TYPE_STANDARD = "";

/// Page names - the leading '/' is required by <code>HttpServlet.dispatch</code> */

    /** The final address page. NB: no leading / because it may be a destination page without <code>HttpServlet.dispatch</code> being called */
    public static final String FINAL_ADDRESS_PAGE = "address.jsp";
    /** The country selector page for key search */
    public static final String KEY_INIT_PAGE = "/key.country.jsp";
    /** The prompt entry page for key search*/
    public static final String KEY_PROMPT_ENTRY_PAGE = "/key.prompt.jsp";
    /** The picklist page  */
    public static final String KEY_SEARCH_PAGE = "/key.picklist.jsp";
    /** The confirm address page  */
    public static final String KEY_FORMAT_ADDRESS_PAGE = "/key.address.jsp";
    public static final String INTUITIVE_PAGE = "/intuitive.jsp";
	/** The country selector page (flattened) */
	public static final String FLAT_INIT_PAGE = "/flat.country.jsp";
	/** The prompt entry page (flattened) */
	public static final String FLAT_PROMPT_ENTRY_PAGE = "/flat.prompt.jsp";
	/** The picklist page (flattened) */
	public static final String FLAT_SEARCH_PAGE = "/flat.picklist.jsp";
	/** The refine address page (flattened) */
	public static final String FLAT_REFINE_ADDRESS_PAGE = "/flat.refine.jsp";
	/** The confirm address page (flattened) */
	public static final String FLAT_FORMAT_ADDRESS_PAGE = "/flat.address.jsp";
	/** The country selector/search input page (hierarchical) */
	public static final String HIER_INIT_PAGE = "/hier.input.jsp";
	/** The picklist/refinement page (hierarchical) */
	public static final String HIER_SEARCH_PAGE = "/hier.search.jsp";
	/** The address confirmation page (hierarchical) */
	public static final String HIER_ADDRESS_PAGE = "/hier.address.jsp";
	/** The outer search/refinement page (rapid addressing) */
	public static final String RAPID_SEARCH_PAGE = "/rapidSearch.jsp";
	/** The inner picklist frame (rapid addressing) */
	public static final String RAPID_PICKLIST_FRAME = "/rapidResults.jsp";
	/** The address confirmation page for the rapid addressing scenario */
	public static final String RAPID_ADDRESS_PAGE = "/rapidAddress.jsp";
	/** The country selector/search input page for address verification */
    public static final String VERIFY_INIT_PAGE = "/verifyInput.jsp";
    /** The country selector/search input page for address verification */
    public static final String VERIFY_INTERACTION_PAGE = "/verifyInteraction.jsp";
    /** The country selector/search input page for address verification */
    public static final String VERIFY_REFINE_PAGE = "/verifyRefine.jsp";

	/// Authentication objects
	public static final String QA_AUTHENTICATION = "authentication";
	public static final String CLIENT_INFORMATION = "clientInformation";
	/** The username used to authenticate the web service request. */
	public static String USERNAME = null;
	/** The username used to authenticate the web service request. */
	public static String PASSWORD = null;

	// HTTP Proxy
	public static final String HTTP_PROXY_HOST = "HttpProxyHost";
	public static final String HTTP_PROXY_PORT = "HttpProxyPort";
	public static final String HTTP_PROXY_USER = "HttpProxyUser";
	public static final String HTTP_PROXY_PASS = "HttpProxyPass";

	// Custom request attribute keys
	public static final String SINGLE_MATCH = "singleMatch";
	public static final String ERROR_CODE = "errorCode";

}
