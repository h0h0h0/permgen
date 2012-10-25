/*********************************************************************************************************************************************************************
**********************************************************************************************************************************************************************
*
*QAS Best Practices AJAX Sample Code
*Release v5.1
*Date: 2/7/2011
*
**********************************************************************************************************************************************************************
*
*Tested with:
*	Proweb:
*		v6.45
*	Browsers:
*		Firefox v3.6
*		Chrome v6.0
*		Safari v4.0
*		Opera v10.62
*		IE v6, v7, v8
*
**********************************************************************************************************************************************************************
*
*This code is written to be used in conjunction with QAS Pro Web and the provided Common Classes along with a language specific qas_proxy file. It is dependent
*on both jQuery and jQueryUI.
*
*This code processes all addresses on a web page through the proweb engine and requests interaction from an end-user when appropriate. All cleansed
*addresses are then returned to the proper form. All unique addresses will be processed exactly once, if there are two addresses with the same input ignoring case
*and after extraneous spaces have been stripped they will be considered the same and processed only once. If any user interaction is needed, it will be requested
*only once and used for both addresses.
*
*
*All settings are created at the top of the code and can be changed to properly integrate into a website. QAS_Verify() is the function that should be called by
*a website in order to initiate address verificaton. The Classes are as follows:
*
*Main
*	Instantiates all objects
*	Loops through addresses
*	Calls function to return all results
*	Calls pre and post validation functions
*
*Address
*	Retrieves and stores addresses
*	Determines unique addresses
*	Builds search strings
*	Stores cleaned addresses
*	Returns addresses to web page
*
*Clean
*	Used to clean a single address by making an AJAX call to qas_proxy
*	Cleans/Refines/Formats addresses
*	Stores verifylevel/cleansed result/picklist
*
*Business
*	All business logic is handled here
*	Controls interaction
*
*Interface
*	creates div tags
*	populates tags with appropriate messages
*	displays pop up
*	accepts user interaction
*
**********************************************************************************************************************************************************************
*
*Programmer: Jonathan Reimels
*Date: 10/5/2010
*
**********************************************************************************************************************************************************************
*Please log any internal changes to the code here with the following format(Programmer, Date, Reason for change, Change made)
*
*UPDATES:
*
*Programmer:  Zulfiqar Ahmad
*Date: 12/15/10
*Reason: Renaming/Reorganization for clarity and consistency with prior versions of BP
*Change: Alphabetized countries in stripPostCode switch statement.   JS updated to match proxy name changes: dpv->dpvstatus; matchtype->verifylevel; isfull->fulladdress
*
*Programmer:  Jonathan Reimels
*Date: 12/15/10
*Reason: Messaging added for when secondary info inputed on prompt is out of range, as per older versions of BP
*Change:  Additions to lines 156, 164, 979-986, 994-1001
**********************************************************************************************************************************************************************
*********************************************************************************************************************************************************************/


/*********************************************************************************************************************************************************************
*
*Settings
*
*Set all variables here to properly integrate into website
*
*********************************************************************************************************************************************************************/

//location of the qas_proxy file - path needs to either be fixed or relative to the web page the js is loaded on
var PROXY_PATH = "qas_proxy.jsp";

//the proweb configuration to use
var QAS_LAYOUT = "Database layout";

//This is an array of string arrays, the id's for each set of address fields (excluding country fields) should be listed in an individual string array. These should  
//be listed to match with the proweb config. For each cleaned result, the first item (ie Address Line 1) in the config will go into the first field in the string array
var ADDRESS_FIELD_IDS = [
					["add1","add2","add3","city","state","zip"],
					["billadd1","billadd2","billadd3","billcity","billstate","billzip"],
					["altadd1","altadd2","altadd3","altcity","altstate","altzip"]
				];

//country field id's, these should be listed in the same order as the string arrays in ADDRESS_FIELD_IDS. In other words the first country field ID should be part of the same
//address as is in the first string array in ADDRESS_FIELD_IDS. If a layout doesn't have a country field, then enter false in for the appropriate address within the string.
var COUNTRY_FIELD_IDS = ["country", "billcountry", "altcountry"];

//all addresses from these countries will be attempted to be cleaned by proweb, if an address from a different country is entered it will not be cleaned
var DATA_SETS = ["USA", "CAN"];

//dataset to use if the countryField is empty
var DEFAULT_DATA = "USA";

//map country name to QAS country code (not cap-sensitive)
var COUNTRY_MAP = [
					['US', 'USA'],
					['U.S.', 'USA'],
					['U.S.A.', 'USA'],
					['United States', 'USA'],
					['United States of America', 'USA'],
					['Canada', 'CAN'],
					['CA', 'CAN']
				];
					

//This is only for Canadian addresses.
//The proweb configuration should be setup to include LVR and Building name as one of the last lines in order to properly handle CAN apartments.
//This variable should be set to the line number within the config that contains these fields.
var LVR = 8;

//prompt user for information to correct address when needed
var NO_INTERACTION = false;

//display any errors encountered in an alert, should only be used for debugging
var DISPLAY_ERRORS = true;

//ajax timeout
var TIMEOUT = 5000;

//number of lines to display to user in an interaction required address, this will prevent, additional data, such as dpv indicator, or lat/long from being displayed to user
var DISPLAY_LINES = 6;	

//This is the text displayed to the enduser when interaction is required, here you can change all of the text displayed.
var QAS_PROMPTS = {
					"InteractionRequired":
					{
						"header": "<b>We think that your address may be incorrect or incomplete.</b><br />To proceed, please choose one of the options below.",
						"prompt": "We recommend:",
						"button": "Use suggested address"
					},
					"PremisesPartial":
					{
						"header": "<b>Sorry, we think your apartment/suite/unit is missing or wrong</b><br />To proceed, please enter your apartment/suite/unit or use your address as entered",
						"prompt": "Confirm your Apartment/Suite/Unit number:",
						"button": "Confirm number",
						"showPicklist": "Show all potential matches",
						"invalidRange": "Secondary information not within valid range"
					},
					"StreetPartial":
					{
						"header": "<b>Sorry, we don't recognize your house or building number.</b><br />To proceed, please check and choose from one of the options below.",
						"prompt": "Confirm your House/Building number:",
						"button": "Confirm number",
						"showPicklist": "Show all potential matches",
						"invalidRange": "Primary information not within valid range"
					},
					"DPVPartial":
					{
						"header": "<b>Sorry, we don't recognize your house or building number.</b><br />To proceed, please check and choose from one of the options below.",
						"prompt": "Confirm your House/Building number:",
						"button": "Confirm number"
					},
					"AptAppend":
					{
						"header": "<b>Sorry, we think your apartment/suite/unit may be missing.</b><br />To proceed, please check and choose from one of the options below.",
						"prompt": "Confirm Apt/Ste:",
						"button": "Continue",
						"noApt": "I don’t have an apt or suite"
					},
					"Multiple":
					{
						"header": "<b>We found more than one match for your address.</b><br />To proceed, please choose one of the options below.",
						"prompt": "Our suggested matches:"
					},
					"None":
					{
						"header": "<b>Sorry, we could not find a match for your address.</b><br />To proceed, please choose one of the options below."
					},
					"RightSide":
					{
						"prompt": "You Entered:",
						"edit": "Edit",
						"button": "Use Address As Entered*",
						"warning": "<b>*Your address may be undeliverable</b>"
					},
					"waitMessage": "Please wait, your address is being verified",
					"title": "Verify your address details"
				};

//the initial function to call from the webpage in order to initiate address verification, set onclick events inside this function
function QAS_Verify()
{
	//set any onclick events and submit buttons to use pre and post validation
	var preOnclick = null;
	var postOnclick = null;
	var buttonID = "";
	
	if (preOnclick == null)
	{
		var m = new Main(postOnclick, buttonID);
		m.process();
	}
	else if ( preOnclick() )
	{
		var m = new Main(postOnclick, buttonID);
		m.process();
	}
	
	return false;
}


/*********************************************************************************************************************************************************************
*
*Main Class
*
*Public Methods
*	process		- instantiate Interface and Clean, perform clean and sent result to Business
*	next		- store cleaned address, move on to next address
*	finish		- put cleaned addresses in form, submit form
*	ajaxError	- handle any errors during the ajax call to proweb
*
*********************************************************************************************************************************************************************/

function Main(clickEvent, buttonID)
{

	//Private Variables
	var me = this;

	var m_click = clickEvent;
	var m_button = buttonID;

	var add = new Address(ADDRESS_FIELD_IDS, COUNTRY_FIELD_IDS, DATA_SETS);
	var strings = add.getSearchStrings();
	var countries = add.getSearchCountries();
	var orig = add.getOriginalAddresses();

	var inter;

	var clean;

	//keep track of address to be processed (the 'next' method controls this)
	var procIndex = 0;

	//process an address - part 1
	this.process = function()
	{
		//hide select boxes to handle bug with ie6, where select boxes show through the pop-up window
		$('select').css('visibility', 'hidden');
		
		//instantiate Interface to handle all user interaction
		inter = new Interface(me.returnEarly);

		//instantiate Clean, to process address
		clean = new Clean(strings[procIndex], countries[procIndex], PROXY_PATH, QAS_LAYOUT, me.ajaxError, TIMEOUT);

		//if string isn't false process it (false string means it is either an empty address or the country isn't specified in DATA_SETS)
		if ( strings[procIndex] )
		{
			//open the waiting widget, clean address, close waiting widget
			inter.waitOpen();
			clean.search(me.process2);
		}
		
		//if string is false use original address
		else
		{
			clean.result = orig[procIndex];
			me.next();
		}
	}
	
	//process an address - part 2, after callback from ajax call
	this.process2 = function()
	{
		inter.waitClose();

		//instantiate a new Business object and process the cleaned result
		var business = new Business(me.next, clean, orig[procIndex], inter);

		//call appropriate business function to process address depending on whether end-user interaction is allowed
		if (NO_INTERACTION)
		{
			business.noInteraction();
		}
		else
		{
			business.processResult();
		}
	}

	//this is called in order to store an address and increment procIndex so that if another address exists it will be cleaned
	this.next = function()
	{
		//add match type
		clean.result.push(clean.verifylevel);

		//store cleaned address
		add.storeCleanedAddress(clean.result);

		//increase procIndex to point to the next address
		procIndex++;

		//if another address exists, process it, otherwise move to end
		if (procIndex < strings.length)
		{
			me.process();
		}
		else
		{
			me.finish();
		}
	}

	//returns cleaned addresses to webpage, calls submit functions if any exist
	this.finish = function()
	{
		//unhide select boxes to handle bug with ie6, where select boxes show through the pop-up window
		$('select').css('visibility', '');
		
		//return cleaned addresses
		add.returnCleanAddresses();

		//if an onclick event exists, call it
		if ( m_click != null)
		{
			m_click();
		}

		//if a submit button exists, click it
		if (m_button != "")
		{
			$('#' + m_button).attr('onclick','');
			$('#' + m_button).parent('form').attr('onsubmit','');
			$('#' + m_button).click();
		}
	}
	
	//used for clicks on the edit button to return any addresses already cleaned
	this.returnEarly = function()
	{
		//unhide select boxes to handle bug with ie6, where select boxes show through the pop-up window
		$('select').css('visibility', '');
		
		//return cleaned addresses
		add.returnCleanAddresses();
	}

	//handle all ajax errors
	this.ajaxError = function(xml, text, msg)
	{
	
		if(text == "timeout")
		{
			//set match type to timeout
			clean.verifylevel = "Timeout";
		}
		else
		{
			//set match type to error
			clean.verifylevel = "Error";
		}

		//close the waiting widget
		inter.waitClose();

		//if display errors is set, then display the error
		if ( DISPLAY_ERRORS )
		{
			alert(text + " - Error with AJAX call. Check to make sure pro web is running correctly."); 
		}

		//set restult to the original address entered
		clean.result = orig[procIndex];
		
		//move onto next record
		me.next();
	}
}	//End Main Class


/*********************************************************************************************************************************************************************
*
*Address Class
*
*Public Methods
*	getSearchStrings		- returns an array of strings ready to be sent to qas, a value of false means the address should not be processed
*	getSearchCountries		- returns an array of countries corresponding to the search strings
*	getOriginalAddresses	- returns an array of original addresses corresponding to the search strings
*	storeCleanedAddress		- stores a cleaned address
*	returnCleanAddresses	- returns cleaned addresses to the webpage
*
*********************************************************************************************************************************************************************/

function Address(addressIds, countryIds, countryList)
{
	/**************************PUBLIC**************************/
	this.getSearchStrings = function() 
	{
		return searchStrings;
	}

	this.getSearchCountries = function() 
	{
		return searchCountries;
	}

	this.getOriginalAddresses = function()
	{
		return uniqueAddresses;
	}

	this.storeCleanedAddress = function(cleanAddress)
	{
		cleanedAddresses.push(cleanAddress);
	}

	this.returnCleanAddresses = function()
	{
		returnAddresses();
	}


	/**************************PRIVATE**************************/
	var ids = addressIds;
	var cIds = countryIds;
	var addresses = new Array();
	var uniqueAddresses = new Array();
	var uniqueTracker = new Array(ids.length);
	var searchStrings = new Array();
	var searchCountries = new Array();
	var cleanedAddresses = new Array();
	var cList = countryList;

	//retrieve address values from forms and return array				
	var getAddresses = function()
	{
		//loop through forms
		for ( i = 0; i < ids.length; i++ )
		{
			//a variable to temporarily store an address form
			var tempAddress = new Array();

			//loop through fields in form
			for ( j = 0; j< ids[i].length; j++ )
			{
				//get data in address field
				var fieldValue = $('#' + ids[i][j]).val();

				//if this field is undefined and display errors is on, display an error, otherwise this will be handled later
				if ( fieldValue == undefined )
				{
					if ( DISPLAY_ERRORS ) 
					{
						alert("ID '" + ids[i][j] + "' is undefined");
					}
				}
				else
				{
					//trim whitespace
					fieldValue = fieldValue.replace(/^\s+|\s+$/g,"");
				}

				//push the value into the temporary variable
				tempAddress.push( fieldValue );
			}

			//get the country from the form
			var c3 = $('#' + cIds[i]).val();

			//if the country is empty or undefined, use the default country
			if ( (c3 == "") || (c3 == undefined) )
			{
				c3 = DEFAULT_DATA;
			}
		
			//convert to QAS country codes
			for ( cIndex = 0; cIndex < COUNTRY_MAP.length; cIndex++ )
			{
				if ( c3.toLowerCase() == COUNTRY_MAP[cIndex][0].toString().toLowerCase() )
				{
					c3 = COUNTRY_MAP[cIndex][1].toString();
				}
			}

			//push country into the temporary variable
			tempAddress.push( c3 );

			//push temporary address into array of addresses
			addresses.push(tempAddress);
		}
	}

	//determine which forms contain unique addresses
	var getUnique = function()
	{
		var isUnique = true;
		var j = 0;

		//loop through addresses
		for ( i = 0; i < addresses.length; i++ )
		{
			//assume address is unique, point uniqueTracker to where address will be added in uniqueAddresses, and set isUnique to true
			uniqueTracker[i] = uniqueAddresses.length;	
			isUnique = true;
			j = 0;

			//loop through unique addresses until the current address either matches a unique 
			//address or no more unique addresses are left, in which case the address is unique 
			//and is added to the unique address list - if this is the first address it will 
			//be unique by default
			while ( isUnique && ( j < uniqueAddresses.length ) )
			{
				if ( addresses[i].toString().toLowerCase() == uniqueAddresses[j].toString().toLowerCase() )
				{
					isUnique = false;
					uniqueTracker[i] = j;
				}
				j++;
			}

			if (isUnique)
			{
				uniqueAddresses.push(addresses[i]);
			}
		}
	}

	//build the SearchString array from the unique addresses
	var buildSearchStrings = function()
	{
		for ( i = 0; i < uniqueAddresses.length; i++ )
		{
			searchCountries.push(uniqueAddresses[i].pop());

			if ( cleanCheck(uniqueAddresses[i], searchCountries[i]) )
			{
				searchStrings.push(uniqueAddresses[i].join("|"));	
			}
			else
			{
				searchStrings.push(false);
			}
		}
	}

	//check if an address should be cleaned
	var cleanCheck = function(address, country)
	{
		var addNotEmpty = false;
		var j = 0;

		//if an address is empty or has an undefined field, then false will be returned
		while ( j < address.length )
		{
			if ( address[j] != "" )
			{
				addNotEmpty = true;
			}

			if ( address[j] == undefined )
			{
				return false;
			}
			j++;
		}

		//if the country is not in the list, return false
		if (addNotEmpty)
		{
			for ( k = 0; k < cList.length; k++ )
			{
				if ( country == cList[k] )
				{
					return true;
				}
			}
		}
		return false;
	}

	//return cleansed address
	var returnAddresses = function()
	{
		for ( i = 0; i < ids.length; i++ )
		{
			//if edit is clicked, not all addresses will have been validated, only update validated addresses in this case
			if (cleanedAddresses[uniqueTracker[i]] != undefined)
			{
				for ( j = 0; j < ids[i].length; j++ )
				{
					$('#' + ids[i][j]).val(cleanedAddresses[uniqueTracker[i]][j]);
				}
			}
		}
	}

	//constructor
	getAddresses();
	getUnique();
	buildSearchStrings();

}	//end Address Class


/*********************************************************************************************************************************************************************
*
*Clean Class
*
*Public Properties
*	result		- cleaned result from proweb, either a picklist, or a cleaned address
*	verifylevel	- match type from the cleaning process
*	dpv			- dpv information
*	country		- country of cleaned address
*
*Public Methods
*	search					- main search, to be used to process an address
*	searchPremisesPartial	- reprocesses a premises partial address
*	searchStreetPartial		- reprocesses a street partial address
*	searchDPVPartial		- reprocesses an address that failed dpv
*	formatAddress			- get a formatted address
*	refineAddress			- refine on a picklist
*
*********************************************************************************************************************************************************************/

function Clean(searchString, country_3, proxyPath, layout, ajaxErr, ajaxTimeout)
{

	var me = this;
	var m_ajaxErr = ajaxErr;
	var m_ajaxTimeout = ajaxTimeout;
	var premClean = false;
	var strClean = false;
	var partialAddress = "";
	var m_callback; 	

	/**************************PUBLIC**************************/

	this.result = new Array();
	this.verifylevel = "";
	this.dpv = "";

	this.country = country_3;

	this.search = function(callback)
	{
		m_callback = callback;
		doSearch(origSearchString, me.country);
	}

	this.searchPremisesPartial = function(aptNo, callback)
	{
		m_callback = callback;
		
		premClean = true;
		//strip the +4 from a partial address and append the apt to the end of the first line
		var noPost = stripPostCode(partialAddress);
		var aptAddress = noPost.replace(/,/, " # " + aptNo + ",");

		//process address
		doSearch(aptAddress, me.country);
	}

	this.searchStreetPartial = function(buildingNo, callback)
	{
		m_callback = callback;
		
		strClean = true;
		//strip the +4 from a partial address and append the building number to the start of the first line	
		var noPost = stripPostCode(partialAddress);
		var buildAddress = buildingNo + " " + noPost;

		//process address
		doSearch(buildAddress, me.country);
	}

	this.searchDPVPartial = function(buildingNo, callback)
	{
		m_callback = callback;
	
		//replace old building number with new building number to original address
		var wholeAddress = me.result.join("|");
		wholeAddress = wholeAddress.replace(/\|?\d+\w*\s/, "|" + buildingNo + " ");

		//process address
		doSearch(wholeAddress, me.country);
	}

	this.formatAddress = function(moniker, callback)
	{
		m_callback = callback;
	
		//format on the moniker
		doFormat(moniker);
	}

	this.refineAddress = function(moniker, callback)
	{
		m_callback = callback;
		//refine on the moniker
		doRefine(moniker);
	}

	/**************************PRIVATE**************************/

	var origSearchString = searchString;
	var cleansedResult = new Array();

	//build up ajax parameters for verification search, and call ajax search
	var doSearch = function(address, c3)
	{
		var ajaxParams =
		{
			"action": "search",
			"addlayout": layout,
			"country": c3,
			"searchstring": address
		};

		//pass in valid err param
		ajaxCall(ajaxParams);
	}

	//build up ajax parameters for format, and call ajax
	var doFormat = function(moniker)
	{
		var ajaxParams =
		{
			"action": "GetFormattedAddress",
			"addlayout": layout,
			"moniker": moniker
		};

		//pass in valid err param
		ajaxCall(ajaxParams);
	}

	//build up ajax parameters for refine, and call ajax
	var doRefine = function(moniker)
	{
		var ajaxParams =
		{
			"action": "refine",
			"addlayout": layout,
			"moniker": moniker,
			"refinetext": ""
		};

		ajaxCall(ajaxParams);
	}

	//send ajax request to proweb
	var ajaxCall = function(parameters)
	{
		$.ajax({
			type: "POST",
			url: proxyPath,
			async: true,
			data: parameters,
			dataType: "xml",
			success: saveResult,
			timeout: m_ajaxTimeout,
			error: m_ajaxErr
		});
	}

	//process result from ajax call
	var saveResult = function(xml)
	{
		//get verifylevel and dpv status
		me.verifylevel = $(xml).find("verifylevel").text();
		me.dpv = $(xml).find("dpvstatus").text();
		
		//if a premisesPartial is searched on and a premisesPartial is returned, 
		//keep old result, so as not to retain the incorectly entered premise info
		if( premClean && (me.verifylevel == "PremisesPartial") )
		{
			premClean = false;
		}
		else if( strClean && (me.verifylevel == "StreetPartial") )
		{
			strClean = false;
		}
		else
		{
			//re-initialize this.result
			me.result = new Array();
			
			premClean = false;
			strClean = false;

			//save each line of the address if result is 'Verified' or 'InteractionRequired'
			if ( (me.verifylevel == "Verified") || (me.verifylevel == "InteractionRequired") )
			{
				$(xml).find("line").each(saveAddress);
			}
			//otherwise save each picklist item
			else
			{	
				fullMoniker = $(xml).find("fullmoniker").text();
				$(xml).find("picklistitem").each(savePickList);
				
				if ( (me.verifylevel == "PremisesPartial") || (me.verifylevel == "StreetPartial") )
				{
					partialAddress = getPartialAddress();
					if (partialAddress == null)
					{
						me.verifylevel = "Multiple";
					}
				}
			}
		}
		m_callback();
	}

	//append each line from the returned xml to result
	var saveAddress = function()
	{
		me.result.push( $(this).text() );
	}

	//build array of picklist items from the returned xml
	var savePickList = function()
	{
		////try-catch here

		var partialText = $(this).find("partialtext").text();
		var addressText = $(this).find("addresstext").text();
		var postCode = $(this).find("postcode").text();
		var moniker = $(this).find("moniker").text();
		var fulladdress = $(this).find("fulladdress").text();

		me.result.push(
			{
				"partialText": partialText,
				"addressText": addressText,
				"postCode": postCode,
				"moniker": moniker,
				"fulladdress": fulladdress
			}
		);
	}

	//get a partial address within a picklist that is not a full address
	//this is used to append building or apt info, and research on the resulting address
	var getPartialAddress = function()
	{
		for (i=0;i < me.result.length;i++)
		{
			if ( me.result[i].fulladdress.toString().toLowerCase() == "false" )
			{
				return me.result[i].partialText;
			}
		}
		return null;
	}
	
	//strip postcodes from strings based on country
	//used to strip the postcode out of premises and street 
	//partial addresses prior to address being re-submitted
	var stripPostCode = function(str)
	{
		switch(me.country)
		{
			case "AUS":
				str = str.replace(/\d{4}$/, "");
				break;
			case "DEU":
				str = str.replace(/\d{5}-\d{5}$/, "");
				break;
			case "DNK":
				str = str.replace(/\s\d{4}\s/, " ");
				break;
			case "FRA":
				str = str.replace(/\s\d{5}\s/, " ");
				break;
			case "GBR":
				str = str.replace(/\w{1,2}\d{1,2}\w?\s\d\w{2}$/, "");
				break;
			case "LUX":
				str = str.replace(/\s\d{4}\s/, "" );
				break;
			case "NLD":
				str = str.replace(/\s\d{4}\s\w{2}\s/, " ");
				break;
			case "NZL":
				str = str.replace(/\d{4}$/, "");
				break;
			case "SGP":
				str = str.replace(/\d{6}$/, "");
				break;
			case "USA":
				str = str.replace(/-\d{4}$/, "");
				break;
		}
		
		return str;
	}

}	//end Clean Class


/*********************************************************************************************************************************************************************
*
*Business Class
*
*The public methods of this class are used to process a cleansed address, prompt for interaction if necessary, handle interaction, pass address back to main
*
*********************************************************************************************************************************************************************/

function Business(callback, clean, orig, inter)
{
	var me = this;

	var m_callback = callback;
	var m_clean = clean;
	var m_orig = orig;
	var m_inter = inter;
	
	//used for double street partials and double premise partials
	var previousMatch = "";
	var previousCleanResult;	

	var count = 0;

	//handle addresses with no end-user interaction
	this.noInteraction = function()
	{
		if ( (m_clean.verifylevel == "Verified") || (m_clean.verifylevel == "InteractionRequired") )
		{
			m_callback();
		}
		else
		{
			me.useOriginal();
		}
	}

	this.processResult = function()
	{
		count++;

		//handle address based on verifylevel
		switch(m_clean.verifylevel)
		{
			case "Verified":
				//if address is USA, then check DPV status
				if ( m_clean.country == "USA")
				{
					//if dpv is not confirmed, prompt for Building Number
					if (clean.dpv == "DPVNotConfirmed") 
					{
						m_inter.setDPVPartial(m_orig, QAS_PROMPTS.DPVPartial, me.refineDPV, me.useOriginal);
						m_inter.display();
					}
					//if dpv is missing secondary, treat address as an Interactino Required
					else if ( clean.dpv == "DPVConfirmedMissingSec")
					{
						m_inter.setInterReq(m_clean.result, m_orig, QAS_PROMPTS.InteractionRequired, me.acceptInter, me.useOriginal);
						m_inter.display();
					}
					//otherwise, dpv was passed or not set. Accept the address
					else
					{
						m_callback();
					}
				}
				//if address is Canadian, check to see if there should be an apartment
				else if (m_clean.country == "CAN")
				{
					//if there should be an apt and the address currently doesn't have one, prompt for an apt
					if ( !aptCheck(LVR - 1) )
					{
						m_inter.setAptAppend(m_orig, QAS_PROMPTS.AptAppend, me.appendApt, m_callback, me.useOriginal);
						m_inter.display();
					}
					//otherwise, apartment was already entered, or address doesn't need an apt
					else
					{
						m_callback();
					}
				}
				//all other countries, accept verified address
				else
				{
					m_callback();
				}
				break;

			case "InteractionRequired":
			
				//if there should be an apt and the address currently doesn't have one, prompt for an apt
				if ( (m_clean.country == "CAN") && (!aptCheck(LVR - 1)) )
				{
					m_inter.setAptAppend(m_orig, QAS_PROMPTS.AptAppend, me.appendApt, m_callback, me.useOriginal);
					m_inter.display();
				}
				//if interaction has already happened and resulting address is an interaction required, accept the address without further interaction
				else if(count > 1)
				{
					m_callback();
				}
				//otherwise display interaction required dialog
				else
				{
					m_inter.setInterReq(m_clean.result, m_orig, QAS_PROMPTS.InteractionRequired, me.acceptInter, me.useOriginal);
					m_inter.display();
				}
				break;

			case "PremisesPartial":
			
				//display premises partial dialog
				m_inter.setPartial(m_clean.result, m_orig, QAS_PROMPTS.PremisesPartial, me.refineApt, me.acceptMoniker, me.useOriginal);
				m_inter.display();
					
				//if previous address was a PremisesPartial, inform user that invalid range was entered
				if (previousMatch == "PremisesPartial")
				{
					alert(QAS_PROMPTS.PremisesPartial.invalidRange);
				}
				
				//set previous match type
				previousMatch = "PremisesPartial";
				break;

			case "StreetPartial":
			
				//display street partial dialog
				m_inter.setPartial(m_clean.result, m_orig, QAS_PROMPTS.StreetPartial, me.refineBuild, me.acceptMoniker, me.useOriginal);
				m_inter.display();
				
				//if previous address was a StreetPartial, inform user that invalid range was entered
				if (previousMatch == "StreetPartial")
				{	
					alert(QAS_PROMPTS.StreetPartial.invalidRange);
				}
					
				//set previous match type
				previousMatch = "StreetPartial";
				break;

			case "Multiple":
				//display multiple dialog
				m_inter.setMultiple(m_clean.result, m_orig, QAS_PROMPTS.Multiple, me.acceptMoniker, me.refineMult, me.useOriginal);
				m_inter.display();
				break;

			case "None":
				//display none dialog
				m_inter.setNone(m_orig, QAS_PROMPTS.None, me.useOriginal);
				m_inter.display();
				break;
		}
	}

	this.acceptInter = function()
	{
		//accept interaction address
		m_callback();
	}

	this.acceptMoniker = function(moniker)
	{
		//get formatted address associated with moniker and accept it
		m_clean.formatAddress(moniker, m_callback);
	}

	this.refineApt = function()
	{
		//clean a premisespartial address and process it
		var aptNo = $('#QAS_RefineText').val();
		m_clean.searchPremisesPartial(aptNo, me.processResult);
	}

	this.refineBuild = function()
	{
		//clean a streetpartial address and process it
		var buildNo = $('#QAS_RefineText').val();
		m_clean.searchStreetPartial(buildNo, me.processResult);
	}

	this.refineDPV = function()
	{
		//clean an address that failed dpv and process it
		var buildNo = $('#QAS_RefineText').val();
		m_clean.searchDPVPartial(buildNo, me.processResult);
	}

	this.appendApt = function()
	{
		//append apt to address and accept it
		var aptNo = $('#QAS_RefineText').val();
		
		var aptIndex = 0;
		var aptLine = false;
		
		//find address line one and add apt to it
		while( (!aptLine) && (aptIndex < m_clean.result.length) )
		{
			if (m_clean.result[aptIndex].search(/^\d+\s/) != -1 )
			{
				aptLine = true;
				m_clean.result[aptIndex] = aptNo + "-" + m_clean.result[aptIndex];
			}
			aptIndex++;
		}
		m_callback();
	}

	this.refineMult = function(moniker)
	{
		//refine on multiple address and process the result
		m_clean.refineAddress(moniker, me.processResult);
	}

	this.useOriginal = function()
	{
		//accept orignally entered address
		m_clean.result = m_orig;
		m_callback();
	}

	var aptCheck = function(lvrLine)
	{
		var isApt = "";

		//check if address should have apt
		isApt = m_clean.result[lvrLine];

		//if address should have apt, check if it already does have an apt
		if (isApt)
		{
			//search on wholeaddress as address line 1 is unknown
			var wholeAddress = m_clean.result.join("|");
			if ( wholeAddress.search(/\|?\d+\s*-\s*\d+/) != -1 )	
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			return true;
		}
	}
}	//end Business Class


/*********************************************************************************************************************************************************************
*
*Interface Class
*
*	Display dialog to user
*
*********************************************************************************************************************************************************************/

function Interface(editCall)
{
	//open waiting diaglog
	this.waitOpen = function()
	{
		$('#QAS_Wait').dialog('open');
		//remove close button from top right of dialog
		$('.ui-dialog-titlebar-close').css('display','none');
	}

	//close waiting dialog
	this.waitClose = function()
	{
		$('#QAS_Wait').dialog('close');
	}	

	//display interaction dialog
	this.display = function()
	{
		window.scroll(0,0);

		$('#QAS_Dialog').dialog('open');

		//remove close button from top right of dialog
		$('.ui-dialog-titlebar-close').css('display','none');
		
		//remove the default focus from interaction required button(so that it is not highlighted as if mouse is hovering on it)
		$('#QAS_RefineBtn').blur();
		$('.QAS_Header').focus();
	}

	//set dialog to handle interaction required address
	this.setInterReq = function(cleaned, orig, message, acceptCallback, origCallback)
	{
		m_orig = orig;
		m_message = message;

		var cleanedHtml = "";

		//build right side of dialog
		buildRightSide(origCallback);

		//build cleansed address to show to end-user
		for (i=0; i < DISPLAY_LINES; i++)
		{
			cleanedHtml += "<tr><td>" + cleaned[i] + "</td></tr>";
		}

		//display proper messages
		$(".QAS_Header").html(message.header);
		$(".QAS_PromptText").html(message.prompt);
		$(".QAS_PromptData").html(
			"<br /><br />" +
			"<table>" +
				cleanedHtml +
			"</table>"
		);
		$(".QAS_Input").html("<input type='button' id='QAS_RefineBtn' value='" + message.button + "' />");
		$(".QAS_MultPick").html("");
		$(".QAS_ShowPick").html("");
		$(".QAS_Pick").html("");
		
		$(".QAS_MultPick").hide();

		//add jqueryui button
		$('#QAS_RefineBtn').button();
		
		//add onclick event to the button
		$('#QAS_RefineBtn').click(
			function() {
				$('#QAS_Dialog').dialog('close');
				acceptCallback();
			}
		);
	}

	//set dialog to handle premises and street partial addresses
	this.setPartial = function(pickList, orig, message, refineCallback, monikerCallback, origCallback)
	{
		m_pickList = pickList;
		m_orig = orig;
		m_message = message;

		//build picklist to display and right side of dialog
		buildPick();
		buildRightSide(origCallback);

		//display proper messages and picklist
		$(".QAS_Header").html(message.header);
		$(".QAS_PromptText").html(message.prompt);
		$(".QAS_PromptData").html("");
		$(".QAS_Input").html(
			"<input type='text' id='QAS_RefineText' />"	+
			"<input type='button' id='QAS_RefineBtn' value='" + message.button + "' />"
		);
		$(".QAS_MultPick").html("");
		$(".QAS_ShowPick").html("<a href='#'>" + message.showPicklist + "</a>");
		$(".QAS_Pick").html(
			"<table>"	+
				m_pickHtml	+
			"</table>"
		);
		
		$(".QAS_MultPick").hide();
		
		//add jqueryui button
		$('#QAS_RefineBtn').button();

		//add onclick event to the button
		$('#QAS_RefineBtn').click(
			function() {
				if ( $('#QAS_RefineText').val() == "")	//if no value was entered in field, display error message
				{
					alert("No value entered");
				}
				else
				{
					$('#QAS_Dialog').dialog('close');
					refineCallback();
				}
			}
		);

		//add onclick event to any full addresses in the picklist
		$('.QAS_StepIn').click(
			function() {
				$('#QAS_Dialog').dialog('close');
				var mon = $(this).attr('moniker');
				monikerCallback(mon);
			}
		);
	}

	//set dialog to handle addresses that fail dpv
	this.setDPVPartial = function(orig, message, refineCallback, origCallback)
	{
		m_orig = orig;
		m_message = message;

		//build right side of dialog
		buildRightSide(origCallback);

		//display proper messages
		$(".QAS_Header").html(message.header);
		$(".QAS_PromptText").html(message.prompt);
		$(".QAS_PromptData").html("");
		$(".QAS_Input").html(
			"<input type='text' id='QAS_RefineText' />"	+
			"<input type='button' id='QAS_RefineBtn' value='" + message.button + "' />"
		);
		$(".QAS_MultPick").html("");
		
		$(".QAS_MultPick").hide();
		
		//add jqueryui button
		$('#QAS_RefineBtn').button();

		//add onclick event to the button
		$('#QAS_RefineBtn').click(
			function() {
				if ( $('#QAS_RefineText').val() == "")	//if no value was entered in field, display error message
				{
					alert("No value entered");
				}
				else
				{
					$('#QAS_Dialog').dialog('close');
					refineCallback();
				}
			}
		);
	}

	//set dialog to handle addresses missing apt info
	this.setAptAppend = function(orig, message, refineCallback, noAptCallback, origCallback)
	{
		m_orig = orig;
		m_message = message;

		//build right side of dialog
		buildRightSide(origCallback);

		//display proper messages
		$(".QAS_Header").html(message.header);
		$(".QAS_PromptText").html(message.prompt);
		$(".QAS_PromptData").html("");
		$(".QAS_Input").html(
			"<input type='text' id='QAS_RefineText' />"	+
			"<input type='button' id='QAS_RefineBtn' value='" + message.button + "' />" +
			"<br />" +
			"<input type='button' id='QAS_NoApt' value='" + message.noApt + "' />"
		);
		$(".QAS_MultPick").html("");
		
		$(".QAS_MultPick").hide();		
		
		//add jqueryui button
		$('#QAS_RefineBtn').button();
		$('#QAS_NoApt').button();

		//add onclick event to the button
		$('#QAS_RefineBtn').click(
			function() {
				if ( $('#QAS_RefineText').val() == "")	//if no value was entered in field, display error message
				{
					alert("No value entered");
				}
				else
				{
					$('#QAS_Dialog').dialog('close');
					refineCallback();
				}
			}
		);

		//add onclick event to button, in order to accept cleaned address as is, with no apt
		$('#QAS_NoApt').click(
			function() {
				$('#QAS_Dialog').dialog('close');
				noAptCallback();
			}
		);
	}

	//set dialog to handle multiple addresses
	this.setMultiple = function(pickList, orig, message, formatCallback, refineCallback, origCallback)
	{
		m_pickList = pickList;
		m_orig = orig;
		m_message = message;

		//build picklist to display and right side of dialog
		buildMultPick();
		buildRightSide(origCallback);

		//display proper messages and picklist
		$(".QAS_Header").html(message.header);
		$(".QAS_PromptText").html(message.prompt);
		$(".QAS_PromptData").html("");
		$(".QAS_Input").html("");
		$(".QAS_MultPick").html(
			"<table>"	+
				m_pickHtml	+
			"</table>"
		);
		$(".QAS_ShowPick").html("");
		$(".QAS_Pick").html("");
		
		$(".QAS_MultPick").show();
		
		//step into any full address
		$('.QAS_StepIn').click(
			function() {
				$('#QAS_Dialog').dialog('close');
				var mon = $(this).attr('moniker');
				formatCallback(mon);
			}
		);

		//refine on non-full address
		$('.QAS_Refine').click(
			function() {
				$('#QAS_Dialog').dialog('close');
				var mon = $(this).attr('moniker');
				refineCallback(mon);
			}
		);
	}

	//set display for none verifylevel
	this.setNone = function(orig, message, origCallback)
	{
		m_orig = orig;
		m_message = message;

		buildRightSide(origCallback);
		
		$(".QAS_Header").html(message.header);
		$(".QAS_Prompt").remove();
		$(".QAS_Input").remove();
		$(".QAS_MultPick").html("");
		$(".QAS_ShowPick").remove();
		$(".QAS_Pick").remove();
		$('.QAS_RightDetails').css('float', 'left');
		
		$(".QAS_MultPick").hide();
	}


	/**************************PRIVATE**************************/

	var m_editCall = editCall;
	var m_pickList;
	var m_orig;
	var m_message;
	var m_pickHtml = "";

	//create a picklist
	var buildPick = function()
	{
		//reinitialize
		m_pickHtml = "";

		for (i=0; i < m_pickList.length; i++)
		{
			if ( m_pickList[i].fulladdress.toString().toLowerCase() == "true" )
			{
				m_pickHtml += "<tr><td NOWRAP><a href='#' class='QAS_StepIn' moniker='" + m_pickList[i].moniker + "'>" + m_pickList[i].addressText + "</a></td><td NOWRAP><a href='#' class='QAS_StepIn' moniker='" + m_pickList[i].moniker + "'>" + m_pickList[i].postCode + "</a></td></tr>";
			}
			else
			{
				m_pickHtml += "<tr><td NOWRAP>" + m_pickList[i].addressText + "</td><td NOWRAP>" + m_pickList[i].postCode + "</td></tr>";
			}
		}
	}

	//create a picklist for multiple address, all items must be clickable
	var buildMultPick = function()
	{
		//reinitialize
		m_pickHtml = "";

		for (i=0; i < m_pickList.length; i++)
		{
			if ( m_pickList[i].fulladdress.toString().toLowerCase() == "true" )
			{
				m_pickHtml += "<tr><td NOWRAP><a href='#' class='QAS_StepIn' moniker='" + m_pickList[i].moniker + "'>" + m_pickList[i].addressText + "</a></td><td NOWRAP><a href='#' class='QAS_StepIn' moniker='" + m_pickList[i].moniker + "'>" + m_pickList[i].postCode + "</a></td></tr>";
			}
			else
			{
				m_pickHtml += "<tr><td NOWRAP><a href='#' class='QAS_Refine' moniker='" + m_pickList[i].moniker + "'>" + m_pickList[i].addressText + "</a></td><td NOWRAP><a href='#' class='QAS_Refine' moniker='" + m_pickList[i].moniker + "'>" + m_pickList[i].postCode + "</a></td></tr>";
			}
		}
	}

	//build display of original address and button to click
	var buildRightSide = function(callback)
	{
		var origHtml = "";

		for (i=0; i < m_orig.length; i++)
		{
			origHtml += "<tr><td>" + m_orig[i] + "</td></tr>";
		}

		$(".QAS_RightDetails").html(
			"<div class='QAS_RightSidePrompt'>" +
				"<div class='QAS_RightSidePromptText'>" +
					QAS_PROMPTS.RightSide.prompt + //"&nbsp;[<a href='#' id='QAS_Edit'>" + QAS_PROMPTS.RightSide.edit + "</a>]"	+
					"<span class='QAS_EditLink'>[<a href='#' id='QAS_Edit'>" + QAS_PROMPTS.RightSide.edit + "</a>]</span>" +
				"</div>" +
				//"<br />"	+
				"<input type='button' id='QAS_AcceptOriginal' value='" + QAS_PROMPTS.RightSide.button + "' />"	+
			"</div>" +
			//"<br />"	+
			"<table>"	+
				origHtml	+
			"</table>"	+
			//"<br />"	+
			"<div class='QAS_DeliverableWarning'>"	+
				QAS_PROMPTS.RightSide.warning	+
			"</div>"
		);
		
		$('#QAS_AcceptOriginal').button();

		//assign onclick for accepting original address
		$('#QAS_AcceptOriginal').click(
			function() {
				$('#QAS_Dialog').dialog('close');
				callback();
			}
		);

		//assign onclick for edit button
		$('#QAS_Edit').click(
			function() {
				$('#QAS_Dialog').dialog('close');
				m_editCall();
			}
		);
	}

	//load div tags to page and set modal dialogs
	var load = function()
	{
		//remove the dialog if it already exists
		$("#QAS_Dialog").remove();
		$("#QAS_Wait").remove();

		//add div tag to page
		$(document.body).append(
			"<div id='QAS_Dialog' title='" + QAS_PROMPTS.title + "'>"	+
				"<div class='QAS_Header ui-state-highlight'></div>"	+
				"<div class='QAS_Prompt'>"	+
					"<div class='QAS_PromptText'></div>"	+
					"<div class='QAS_Input'></div>"	+
					"<div class='QAS_PromptData'></div>"	+
				"</div>"	+
				"<div class='QAS_RightDetails'></div>"	+
				"<div class='QAS_Picklist'>"	+
					"<div class='QAS_MultPick'></div>"	+
					"<div class='QAS_ShowPick'></div>"	+
					"<div class='QAS_Pick'></div>"	+
				"</div>"	+
			"</div>"	+
			"<div id='QAS_Wait' title = '" + QAS_PROMPTS.waitMessage + "'></div>"
		);

		//add jqueryui modal dialog to div tag, for user interaction
		$("#QAS_Dialog").dialog(
		{
			modal: true,
			//height: 450,	////causes issues with IE
			width: 850,
			autoOpen: false,
			closeOnEscape: false,
			resizable: false,
			draggable: false
		});

		//add jqueryui modal dialog to div tag, for waiting dialog
		$("#QAS_Wait").dialog(
		{
			modal: true,
			height: 100,
			width: 200,
			autoOpen: false,
			closeOnEscape: false,
			resizable: false,
			draggable: false
		});

		//add slide toggle to show pick list
		$(".QAS_ShowPick").click(function()
		{
			$(".QAS_Pick").slideToggle("slow");
		});
		
		//re-center popup when window is resized
		$(window).resize(function()
		{
			$("#QAS_Dialog").dialog("option", "position", 'center')
		});
	}

	//constructor
	load();

}	//end Interface Class

