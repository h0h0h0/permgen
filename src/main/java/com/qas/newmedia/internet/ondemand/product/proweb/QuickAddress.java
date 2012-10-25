/* ----------------------------------------------------------------------------
 * QAS On Demand > (c) QAS Ltd > www.qas.com
 *
 * Common Classes > QuickAddress.java
 * Main searching functionality
 * ----------------------------------------------------------------------------
 */
package com.qas.newmedia.internet.ondemand.product.proweb;

// generated SOAP client stubs
import com.qas.newmedia.internet.ondemand.product.proweb.servlet.Constants;
import com.qas.ondemand_2011_03.*;


import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import javax.xml.soap.Detail;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.Holder;
import javax.xml.ws.soap.SOAPFaultException;
import org.apache.log4j.Logger;


/**
 * This class is a facade into ProWeb and provides the main functionality
 * of the package.  It uses the com.qas.proweb.soap package in a stateless manner,
 * but some state is maintained between construction and the "business" call to
 * the soap package - this is to allow for optional settings (e.g.timeout).
 * The instance of this class is not intended to be preserved across (for example) JSPs.
 * That is, the intended usage idiom is
 * "construct instance - set optional settings - call main method (e.g. search) - discard instance"
 *
 */
public class QuickAddress implements Serializable {
    /**
     * public constants
     */

	private static final Logger logger = Logger.getLogger(QuickAddress.class);

    /* Constants for referring to search engine types */
	public static final String SINGLELINE = EngineEnumType.SINGLELINE.value();
	public static final String TYPEDOWN = EngineEnumType.TYPEDOWN.value();
	public static final String VERIFICATION = EngineEnumType.VERIFICATION.value();
    public static final String KEYFINDER = EngineEnumType.KEYFINDER.value();
    public static final String INTUITIVE = EngineEnumType.INTUITIVE.value(); 
	/* Constants for referring to engine search intensity */
	public static final String EXACT = EngineIntensityType.EXACT.value();
	public static final String CLOSE = EngineIntensityType.CLOSE.value();
	public static final String EXTENSIVE = EngineIntensityType.EXTENSIVE.value();

    /* This is defined in the server */
    private static final String LINE_SEPARATOR = "|";

    /**
     * private data
     */

	private QAQueryHeader authentication;
	private Holder<QAInformation> holder = new Holder<QAInformation>();

	private QAPortType m_Port = null;

    /** optional settings */
    private EngineType          m_EngineType    = null;
    private QAPortType        m_Config        = null;

	private HttpServletRequest request;


    /**
     * public methods - construction, settings
     */

    /** disallow default constructor */
    private QuickAddress() {
    }

    /**
     * Constructs an instance with TCP/IP as the transport method for the
     * SOAP/XML messages between this client and the QuickAddress server.
     * @param endpoint   the endpoint of the QuickAddress server. This must be
     *                   in the format
     *                   <code>http://servername:portnumber</code>
     * @throws QasException for any error situation
     */
    public QuickAddress(String endpoint, HttpServletRequest request) throws QasException {

		QAQueryHeader qaQueryHeader = new QAQueryHeader();
		qaQueryHeader.setQAAuthentication((QAAuthentication)request.getAttribute(Constants.QA_AUTHENTICATION));

		this.authentication = qaQueryHeader;

		logger.debug("QuickAddress object initialised [QAAuthentication: " + authentication.getQAAuthentication().getUsername() + "]");

		/**
		 * Build wsdl URL
		 */
		URL url = null;
        try {
            URL baseUrl;
            baseUrl = com.qas.ondemand_2011_03.QASOnDemandIntermediary.class.getResource(".");
            url = new URL(baseUrl, endpoint);
        } catch (MalformedURLException e) {
            logger.error("Failed to create URL for the wsdl Location: " + endpoint);
            logger.error(e.getMessage());
        }
		QASOnDemandIntermediary service = new QASOnDemandIntermediary(url);

		m_Port = service.getQAPortType();
        if (request.getAttribute(Constants.HTTP_PROXY_HOST) != null) {
        	System.getProperties().put("http.proxyHost", request.getAttribute(Constants.HTTP_PROXY_HOST));
            System.getProperties().put("http.proxyPort", request.getAttribute(Constants.HTTP_PROXY_PORT));
            System.getProperties().put("http.proxyUser", request.getAttribute(Constants.HTTP_PROXY_USER));
            System.getProperties().put("http.proxyPassword", request.getAttribute(Constants.HTTP_PROXY_PASS));
        }
		this.request = request;
    }

    /** Set the members of the engine by calling relevant methods
     *
     *  @param flatten          <code>boolean</code> flatten value
     *  @param intensity	<code>String</code> value corresponding to one of the constants: EXACT..EXTENSIVE
     *  @param promptSet	<code>String</code> flatten value
     *  @param threshold	<code>int</code> value for the threshold.
     *  @param timeout          <code>int</code> value for timeout period in milliseconds (0 signifies that searches will not timeout).
     */

    public void setEngineType(boolean flatten, String intensity, String promptSet, int threshold, int timeout) {
        setFlatten(flatten);
        setEngineIntensity(intensity);
        setPromptSet(promptSet);
    }



    /** Sets engine type to be used in the initial search (SINGLELINE, TYPEDOWN or VERIFICATION).
     *  If this is not called before <code>search</code> the default value is used (<code>SINGLE LINE</code>).
     *  @param s	<code>String</code> value corresponding to one of the constants: SINGLE LINE..VERIFICATION
     */
    public void setEngineType(String s) {
		EngineType engineType = new EngineType();
		engineType.setValue(EngineEnumType.fromValue(s));
		m_EngineType = engineType;
    }

    public EngineType getEngineType() {
        return m_EngineType;
    }

    /** Sets engine intensity, i.e. how hard the search engine will work to obtain a match.
     * Higher intensity values may yield more results but will also result in longer search times.
     * If this is not called before <code>search</code> the default server value is used (usually CLOSE).
     *  @param s	<code>String</code> value corresponding to one of the constants: EXACT..EXTENSIVE
     */

    public void setEngineIntensity(String s) {
        m_EngineType.setIntensity(EngineIntensityType.fromValue(s));
    }

    /** This defines whether the search results will be ‘flattened’ to a single picklist of deliverable results,
     * or potentially multiple hierarchical picklists of results that can be stepped into.
     * Flattened picklists are simpler for untrained users to navigate (i.e. website users),
     * although can potentially lead to larger number of results.
     * Hierarchical picklists are more suited to users that are trained in using the product
     * (i.e. intranet users), and produce smaller picklists with results that can be stepped into.
     * If this is not called before <code>search</code> the default value <code>false</code> is used, which corresponds to hierarchical.
     * @param b	<code>boolean</code> indicating whether the picklist is to contain only leaf addresses or is to be hierarchical.
     */
    public void setFlatten(boolean b) {
        m_EngineType.setFlatten(b);
    }

    /** Sets the prompt set to the engine,
     *  default vaule is "Default".
     *  @param s        <code>String</code> value for promptset type.
     */


    public void setPromptSet(String s) {
        m_EngineType.setPromptSet(PromptSetType.fromValue(s));
    }

    /**
     * public methods - searching operations
     */

    /** This method checks that the combination of country, engine and layout are valid to search upon.
     * Some data sets are only available for the single-line engine and not the verification engine.
     * Layouts can also be defined for specific data sets, and so may not always be valid for other sets.
     * @param   sDataId  <code>String</code> ID of the dataset to be searched on
     * @param   sLayoutName  <code>String</code> Layout to be used by the final address
     * @param 	sPromptSet   <code>String</code> The prompt set to use
     * @return  boolean
     */
    public CanSearch canSearch(String sDataId,
    						   String sLayoutName,
    						   String sPromptSet)
    throws QasException {
        CanSearch result = null;

        // set up the parameter for the SOAP call
        QACanSearch param = new QACanSearch();

        param.setCountry(new String(sDataId));
        param.setEngine(getEngine());
        param.setLayout(sLayoutName);
        param.getEngine().setPromptSet(PromptSetType.fromValue(sPromptSet));
        

        try {
            // make the call to the server
            QASearchOk ok = getServer().doCanSearch(param, authentication, holder);

			logger.debug("QASearchOk [" + ok.isIsOk() + ", " + ok.getErrorCode() + ", " + ok.getErrorMessage() + "]");

            result = new CanSearch(ok);
        }catch(SOAPFaultException fault){
            mapQAFault(fault);
        }
        return result;
    }

    public CanSearch canSearch(String sDataID,
    						   String sLayout) 
    throws QasException {
        return canSearch(sDataID, sLayout, PromptSetType.DEFAULT.value());
    }
    
    public CanSearch canSearch(String sDataID) 
    throws QasException {
    	return canSearch(sDataID, null);
    }

    /** This method is used to submit an initial search to the server.
     *  A search must be performed against a specific dataset and engine.
     *  A search will produce either:
     *		- A picklist of results, with match information
     *		- A formatted final address (<code>VERIFICATION</code> only)
     *		- and the verification level
     *  The behaviour of a search depends upon the choice of engine and engine options
     *  Since the <code>Verification</code> engine can result in a <code>FormattedAddress</code>, the layout must be specified.
     *  All other engine types produce only a <code>Picklist</code> in the <code>SearchResult</code>.
     * @param   sDataId  <code>String</code> ID of the dataset to be searched on
     * @param   asSearch    <code>String[]</code> array of search terms
     * @param   sPromptSet <code>String</code> name of the prompt set used for these search terms
     * @param   sLayout       <code>String</code> name of the layout, with which to format the address (optional)
     * @param  	sRequestTag	  <code>String</code> Request tag supplied by the user
     * @return SearchResult
     */
    public SearchResult search(String sDataId, String[] asSearch, 
    		String sPromptSet, String sLayout, String sRequestTag)
    throws QasException {
        // concatenate search terms
        StringBuffer sSearch = new StringBuffer(asSearch[0]);
        for (int i=1; i < asSearch.length; i++) {
            sSearch.append(LINE_SEPARATOR);
            sSearch.append(asSearch[i]);
        }

        return search(sDataId, sSearch.toString(), sPromptSet, sLayout, sRequestTag);
    }

    /** Method overload: provides the search function without the optional sLayout argument
     * @param   sDataId  <code>String</code> ID of the dataset to be searched on
     * @param   asSearch    <code>String[]</code> array of search terms
     * @param   sPromptSet <code>String</code> name of the prompt set used for these search terms
     * * @param   sLayout       <code>String</code> name of the layout, with which to format the address (optional)
     * @return SearchResult
     */
    public SearchResult search(String sDataId, String[] asSearch,
    								String sPromptSet, String sLayout)
    throws QasException {
        return search(sDataId, asSearch, sPromptSet, sLayout, null);
    }
    
    /** Method overload: provides the search function without the optional sLayout argument
     * @param   sDataId  <code>String</code> ID of the dataset to be searched on
     * @param   asSearch    <code>String[]</code> array of search terms
     * @param   sPromptSet <code>String</code> name of the prompt set used for these search terms
     * @return SearchResult
     */
    public SearchResult search(String sDataId, String[] asSearch, String sPromptSet)
    throws QasException {
        return search(sDataId, asSearch, sPromptSet, null);
    }

    /** Method overload: provides the search function with search term as a single string
     * @param   sDataId  <code>String</code> ID of the dataset to be searched on
     * @param   sSearch    <code>String</code> search terms
     * @param   sPromptSet <code>String</code> name of the prompt set used for these search terms
     * @param   sLayout       <code>String</code> name of the layout, with which to format the address (optional)
     * @param  	sRequestTag	  <code>String</code> Request tag supplied by the user
     * @return SearchResult
     */
    public SearchResult search(String sDataId, String sSearch, 
    					String sPromptSet, String sLayout, String sRequestTag)
    throws QasException {

        SearchResult result = null;

        // create engine parameter
        EngineType engine = getEngine();
        engine.setPromptSet(PromptSetType.fromValue(sPromptSet));

        // set up the parameter for the SOAP call
        QASearch param = new QASearch();
        param.setCountry(new String(sDataId));
        param.setEngine(engine);
        if (sLayout != null) {
            param.setLayout(sLayout);
        }
        if (sRequestTag != null) {
            param.setRequestTag(sRequestTag);
        }
        param.setSearch(sSearch);

        try {
            // make the call to the server
            QASearchResult searchResult = getServer().doSearch(param, authentication, holder);
            result = new SearchResult(searchResult);
        } catch(SOAPFaultException fault) {
            mapQAFault(fault);
        }
        return result;
    }
    
    /** Method overload: provides the search function with search term as a single string
     * @param   sDataId  <code>String</code> ID of the dataset to be searched on
     * @param   sSearch    <code>String</code> search terms
     * @param   sPromptSet <code>String</code> name of the prompt set used for these search terms
     * @param   sLayout       <code>String</code> name of the layout, with which to format the address (optional)
     * @return SearchResult
     */
    public SearchResult search(String sDataId, String sSearch, 
    					String sPromptSet, String sLayout)
    throws QasException {
    	return search(sDataId, sSearch, sPromptSet, sLayout, null);
    }

    /** Method overload: provides the search function with search term string, without sLayout argument
     * @param   sDataId  <code>String</code> ID of the dataset to be searched on
     * @param   sSearch    <code>String</code> search terms
     * @param   sPromptSet <code>String</code> name of the prompt set used for these search terms
     * @return SearchResult
     */
    public SearchResult search(String sDataId, String sSearch, String sPromptSet)
    throws QasException {
        return search(sDataId, sSearch, sPromptSet, null);
    }

    /** Method to perform a refinement.
     * This is used after an initial search has been performed,
     * when the user enters text to be used to filter upon the picklist,
     * creating a smaller set of picklist results.
     * Refinement and stepIn delegate to the same low-level function.
     * @param   sRefinementText <code>String</code> the refinement text
     * @param   sMoniker    <code>String</code> the search point moniker of the picklist (item) being refined.
     * @param sRequestTag	<code>String</code> Request tag supplied by user
     * @return Picklist containing the results of the refinement
     */
    public Picklist refine(String sMoniker, String sRefinementText, 
    												String sRequestTag)
    throws QasException {
        Picklist result = null;

        // set up the parameter for the SOAP call
        QARefine param = new QARefine();

        param.setMoniker(sMoniker);
        param.setRefinement(sRefinementText);
        if (sRequestTag != null) {
        	param.setRequestTag(sRequestTag);
        }


        try {
            // make the call to the server
			QAPicklistType picklist = getServer().doRefine(param, authentication, holder).getQAPicklist();

            result = new Picklist(picklist);
        } catch(SOAPFaultException fault) {
            mapQAFault(fault);
        }
        return result;
    }
    
    /** Method overload: to perform a refinement.
     * This is used after an initial search has been performed,
     * when the user enters text to be used to filter upon the picklist,
     * creating a smaller set of picklist results.
     * Refinement and stepIn delegate to the same low-level function.
     * @param   sRefinementText <code>String</code> the refinement text
     * @param   sMoniker    <code>String</code> the search point moniker of the picklist (item) being refined.
     * @param sRequestTag	<code>String</code> Request tag supplied by user
     * @return Picklist containing the results of the refinement
     */
    public Picklist refine(String sMoniker, String sRefinementText)
    throws QasException {
    	return refine(sMoniker, sRefinementText, null);
    }

    /** Method to perform a step-in.
     * This is used after an initial search has been performed, when the user selects an entry that can be
     * expanded into elements ‘beneath’ it. For example, a picklist entry that represents a street can
     * often be stepped into so that a picklist of premises beneath the street are displayed.
     *  Refinement and stepin delegate to the same low-level function.
     * @param   sMoniker    <code>String</code> the search point moniker of the picklist (item) being stepped into
     * @return Picklist  containing the results of the stepin.
     */
    public Picklist stepIn(String sMoniker)
    throws QasException {
        return refine(sMoniker, "");
    }

    /** Method to retrieve the final formatted address, formatting a picklist entry.
     * Typically the user selects a <code>PicklistItem</code> for which <code>isFullAddress()</code>
     * returns <code>true</code>.
     * Address formatting is performed using the picklist item moniker and the specified layout.
     * @param   sLayoutName     <code>String</code> layout name (specifies how the address should be formatted)
     * @param   sMoniker        <code>String</code> search point moniker that represents the address
     * @param 	sRequestTag		<code>String</code> User supplied tag for the request
     * @return  FormattedAddress relating to the search point moniker and layout.
     */
    public FormattedAddress getFormattedAddress(String sLayoutName,
    											String sMoniker,
    											String sRequestTag)
    throws QasException {
        FormattedAddress result = null;

        // set up the parameter for the SOAP call
        QAGetAddress param = new QAGetAddress();
        param.setLayout(sLayoutName);
        param.setMoniker(sMoniker);
        if (sRequestTag != null) {
        	param.setRequestTag(sRequestTag);
        }

        try {
            // make the call to the server
			QAAddressType address = getServer().doGetAddress(param, authentication, holder).getQAAddress();
            result = new FormattedAddress(address);
        } catch(SOAPFaultException fault) {
            mapQAFault(fault);
        }

        return result;
    }
    
    /** Method overload: to retrieve the final formatted address, formatting a picklist entry.
     * Typically the user selects a <code>PicklistItem</code> for which <code>isFullAddress()</code>
     * returns <code>true</code>.
     * Address formatting is performed using the picklist item moniker and the specified layout.
     * @param   sLayoutName     <code>String</code> layout name (specifies how the address should be formatted)
     * @param   sMoniker        <code>String</code> search point moniker that represents the address
     * @return  FormattedAddress relating to the search point moniker and layout.
     */
    public FormattedAddress getFormattedAddress(String sLayoutName,
    											String sMoniker)
    throws QasException {
    	return getFormattedAddress(sLayoutName, sMoniker, null);
    }

    /**
     * public methods - status operations
     */

    /** Method to retrieve a list of available data sets that can be searched against with ProWeb.
     * Specifically, this will return an array of <code>DataSet</code> objects that are valid to
     * be passed to the searching methods.
     * For a list of installed data files and their respective license status, the
     * <code>getLicenseInfo</code> method can be used instead.
     * @return  DataSet[]
     */
    public DataSet[] getAllDataSets()
    throws QasException {
        DataSet[] results = null;

        try {
            // make the call to the server
        	QAData qaData = getServer().doGetData(new QAGetData(), authentication, holder);
        	List<QADataSet> l = qaData.getDataSet();
        	results = DataSet.createArray(l);
        } catch(SOAPFaultException fault) {
            mapQAFault(fault);
        }

        return results;
    }

    /** Method to retrieve a list of defined licensed sets .
     * Specifically, this will return an array of <code>LicensedSet</code> objects that are valid to
     * @return  LicensedSet[]
     */

    public LicensedSet[] getDataMapDetail( String sID ) throws QasException {
        LicensedSet[] aDatasets = null;

        try {
            QAGetDataMapDetail tRequest = new QAGetDataMapDetail();
            tRequest.setDataMap(sID);
            QADataMapDetail tMapDetail = getServer().doGetDataMapDetail(tRequest, authentication, holder);
            aDatasets = LicensedSet.createArray(tMapDetail);
        } catch(SOAPFaultException fault) {
            mapQAFault(fault);
        }
        return aDatasets;
    }

    /** Method to retrieve a list of layouts that have been configured within
     * the server configuration file, and can be used to format address results.
     * A list of layouts is useful for situations where the integration would give
     * the user a choice of which layout to use to format an address result.
     * If only one layout is ever used within an integration, it would be more
     * common to explicitly code the layout name.
     * @param   sDataId  <code>String</code> ID of the dataset whose layouts are required
     * @return  Layout[]
     */
    public List<QALayout> getAllLayouts(String sDataId)
    throws QasException {
		List<QALayout> results = null;

        // set up the parameter for the SOAP call
        QAGetLayouts param = new QAGetLayouts();
        param.setCountry(new String(sDataId));

        try {
            // make the call to the server
			results = getServer().doGetLayouts(param, authentication, holder).getLayout();
        }catch(SOAPFaultException fault){
            mapQAFault(fault);
        }

        return results;
    }

    /** Method to return an array of fully formatted example addresses. This may commonly be used
     * to preview a given layout with a set of addresses.
     * @param   sDataId      <code>String</code> ID of the dataset for which examples are required
     * @param   sLayoutName     <code>String</code> name of the layout for formatting
     * @param sRequestTag		<code>String</code> User supplied request tag
     * @return  ExampleAddress[]
     */
    public List<QAExampleAddress> getExampleAddresses(String sDataId,
    									String sLayoutName, String sRequestTag)
    throws QasException {
    	List<QAExampleAddress> results = null;

        // set up the parameter for the SOAP call
        QAGetExampleAddresses param = new QAGetExampleAddresses();
        param.setLayout(sLayoutName);
        param.setCountry(new String(sDataId));
        if (sRequestTag != null) {
        	param.setRequestTag(sRequestTag);
        }

        try {
            // make the call to the server
			results = getServer().doGetExampleAddresses(param, authentication, holder).getExampleAddress();
        }catch(SOAPFaultException fault) {
            mapQAFault(fault);
        }

        return results;
    }
    
    /** Method overload: to return an array of fully formatted example addresses. This may commonly be used
     * to preview a given layout with a set of addresses.
     * @param   sDataId      <code>String</code> ID of the dataset for which examples are required
     * @param   sLayoutName     <code>String</code> name of the layout for formatting
     * @return  ExampleAddress[]
     */
    public List<QAExampleAddress> getExampleAddresses(String sDataId, String sLayoutName)
    throws QasException {
    	return getExampleAddresses(sDataId, sLayoutName, null);
    }

    /** Method to obtain a list of installed data, and any relevant licensing information such as
     * days until data expiry, and data versions. Unlike the <code>getAllDataSets</code> method,
     * this will return information about all data sets, including DataPlus files.
     * This method is designed for the integrator to access information regarding the data files,
     * it is not suitable for display to web users.  If you wish to obtain a list of datasets that
     * can be searched against, then the <code>getAllDataSets</code> method should be used instead.
     * @return  LicensedSet[] array of objects detailing the licence state.
     */
    public LicensedSet[] getLicenceInfo()
    throws QasException {
        LicensedSet[] results = null;

        try {
            // make the call to the server
            QALicenceInfo info = getServer().doGetLicenseInfo(new QAGetLicenseInfo(), authentication, holder);
            results = LicensedSet.createArray(info);
        } catch(SOAPFaultException fault) {
            mapQAFault(fault);
        }

        return results;
    }

    /** Method to retrieve a prompt set for a given data set.
     * This is used to obtain information regarding a prompt set, such as the number of lines and suggested input.
     * When searching with the verification engine, the default prompt set should be always used. The reported number
     * of input lines will be set if an input line restriction is specified within the configuration file using the
     * setting InputLineCount. If there is no restriction applied, there is no defined number of input lines and the
     * integrator can create the input fields in what ever manner they wish.
     * For all other prompt sets, a prompt set may have a different number of input lines depending upon the
     * data set being searched upon.
     * @param   sDataId  <code>String</code> ID of the dataset whose prompt set is required
     * @param   sPromptSetName   <code>String</code> identifies the type of promt set: one of the <code>PromptSet</code> class.
     * @return  PromptSet  which wraps an array of prompt lines identified by the name and country.
     */
    public PromptSet getPromptSet(String sDataId, String sPromptSetName)
    throws QasException {
        PromptSet result = null;

        // set up the parameter for the SOAP call
        QAGetPromptSet param = new QAGetPromptSet();
        param.setCountry(new String(sDataId));
        param.setEngine(getEngine());
        param.setPromptSet(PromptSetType.fromValue(sPromptSetName));

        try {
            // make the call to the server
			QAPromptSet promptSet = getServer().doGetPromptSet(param, authentication, holder);
            result = new PromptSet(promptSet);
        } catch(SOAPFaultException fault) {
            mapQAFault(fault);
        }

        return result;
    }

    /** Method to get textual system (diagnostic) information from the server.
     * @return  String[] each string in the array is a (tab separated) name/value pair of system info.
     */
	public List<String> getSystemInfo()
    throws QasException {
		QASystemInfo results = null;

        try {
            // make the call to the server
			results = getServer().doGetSystemInfo(new QAGetSystemInfo(), authentication, holder);
        } catch(SOAPFaultException fault) {
            mapQAFault(fault);
        }

        return results.getSystemInfo();
    }

    /**
     * private methods
     */

    /** helper to return the port */
    private QAPortType getServer() {
        return m_Port;
    }

    /** helper */
    public EngineType getEngine() {
        if (m_EngineType == null) {
            setEngineType(SINGLELINE);
        }
        return m_EngineType;
    }

    /** helper that converts a QAFault exception (potentially thrown from the
     *  com.qas.proweb.soap package) into a QasException.
     */
    private void mapQAFault(SOAPFaultException x)
    throws QasException {
    	Detail d = x.getFault().getDetail();
		//String errorCode = fault.getFaultCode().getLocalPart();
		String errorDetail = d.getFirstChild().getFirstChild().getTextContent();
		//if (fault.getDetail().getChildText("ExceptionCode") != null) {
			//Want to provide a more detailed message if the user is using
			//an external account incorrectly.
			//if (fault.getMessage().contains("External account cannot perform DoSearch with Flatten set to false")){
				//errorDetail = fault.getMessage();
			//}else{
				//errorDetail = fault.getDetail().getChildText("ExceptionCode");
			//}
		//} else {
			//errorDetail = fault.getMessage();
		//}
		String errorDescription = x.getMessage();

		logger.error("Caught SOAPFaultException [" + 
				errorDetail + ", " + errorDescription + "]");

		/** initialise the error code to default */
		int qasErrorCode = QasException.UNKNOWN_COMMS_ERROR;

		/** custom ondemand error: insufficient credits */
		if ((errorDetail != null) && (errorDetail.equals("InsufficientCredits"))) {
			qasErrorCode  = QasException.INSUFFICIENT_CREDITS;
		}

		/** custom ondemand error: authentication invalid */
		if ((errorDetail != null) && (errorDetail.equals("AuthenticationFailed"))) {
			qasErrorCode = QasException.USER_AUTHENTICATION_INVALID;
		}

		/** custom ondemand error: authentication invalid */
		if ((errorDetail != null) && (errorDetail.equals("LicenseInvalid"))) {
			qasErrorCode = QasException.LICENSE_INVALID;
		}

		/** custom ondemand error: authentication invalid */
		if ((errorDetail != null) && (errorDetail.equals("ServerUnavailable"))) {
			qasErrorCode = QasException.SERVER_UNAVAILABLE;
		}

		throw new QasException("XFireRuntimeException",
				qasErrorCode, new String[] {errorDetail});
    }
}
