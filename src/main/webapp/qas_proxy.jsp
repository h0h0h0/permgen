<%@ page language="java" pageEncoding="utf-8" contentType="text/xml;charset=utf-8" %>
<%@ page import="com.qas.ondemand_2011_03.QAAuthentication" %>
<%@ page import="com.qas.newmedia.internet.ondemand.product.proweb.*" %>
<%@ page import="com.qas.*" %>

<proweb>
<%
	// Constants section: change the variables below as appropriate
	String URL = "https://ws2.ondemand.qas.com/ProOnDemand/V3/ProOnDemandService.asmx?WSDL";
	String Username = "e7f3f4b3-440";
	String Password = "Micros.123";
	
    //get search information from post
    String action = (request.getParameter("action") != null) ? request.getParameter("action") : "";
    String addLayout = (request.getParameter("addlayout") != null) ? request.getParameter("addlayout") : "";
    String country = (request.getParameter("country") != null) ? request.getParameter("country") : "";

	String sAction = (request.getParameter("action") != null) ? request.getParameter("action") : "";
	QAAuthentication authenticationInfo = new QAAuthentication();
	authenticationInfo.setUsername(Username);
	authenticationInfo.setPassword(Password);
	request.setAttribute("authentication", authenticationInfo);
	
    String searchstring = (request.getParameter("searchstring") != null) ? request.getParameter("searchstring") : "";

    String moniker = (request.getParameter("moniker") != null) ? request.getParameter("moniker") : "";
    String refineText = (request.getParameter("refinetext") != null) ? request.getParameter("refinetext") : "";
    %>

    <%!
        public String htmlEncode(String str){

            str = str.replace("&", "&amp;");
            str = str.replace("<", "&lt;");
            str = str.replace(">", "&gt;");
            str = str.replace("\"", "&quot;");



            return str;
            }
    %>




    
    <%

    //setup qas engine
    QuickAddress qAddress = new QuickAddress(URL,request);
    qAddress.setEngineType(QuickAddress.VERIFICATION);
    qAddress.setFlatten(true);
	

    //search for address
    if (action.equals("search"))
    {
        //send address to QAS
        SearchResult result = qAddress.search(country, searchstring, PromptSet.DEFAULT, addLayout);

        // Set the verified level
        %>
            <verifylevel><%=htmlEncode(result.getVerifyLevel())%></verifylevel>
        <%
        
        //for Verified and InteractionRequired addresses get the result
        if ((result.getVerifyLevel() == "Verified") || (result.getVerifyLevel() == "InteractionRequired") )
        {
            %>
                <dpvstatus><%=htmlEncode(result.getAddress().getDPVStatus().toString())%></dpvstatus>
                <address>
            <%

            for( AddressLine line : result.getAddress().getAddressLines())
            {
                %>
                    <line><%=htmlEncode(line.getLine())%></line>
                <%
            }
            %>
                </address>
            <%
        }
        else
        {
            %>
                <picklist>
                <fullmoniker><%=htmlEncode(result.getPicklist().getMoniker())%></fullmoniker>
            <%
            for( PicklistItem item : result.getPicklist().getItems())
            {
                %>
                    <picklistitem>
                        <partialtext><%=htmlEncode(item.getPartialAddress())%></partialtext>
                        <addresstext><%=htmlEncode(item.getText())%></addresstext>
                        <postcode><%=htmlEncode(item.getPostcode())%></postcode>
                        <moniker><%=htmlEncode(item.getMoniker())%></moniker>
                        <fulladdress><%=item.isFullAddress()%></fulladdress>
                    </picklistitem>
                <%
            }
            %>
                </picklist>
            <%
        }
    }
    else if(action.equals("GetFormattedAddress"))
    {
        FormattedAddress formatResult = qAddress.getFormattedAddress(addLayout, moniker);

        %>
            <verifylevel>Verified</verifylevel>
            <dpvstatus><%=htmlEncode(formatResult.getDPVStatus().toString())%></dpvstatus>
            <address>
        <%
        for( AddressLine line : formatResult.getAddressLines())
        {
            %>
                <line><%=htmlEncode(line.getLine())%></line>
            <%
        }
        %>
            </address>
        <%
    }
    else if(action.equals("refine"))
    {
        Picklist pickList = qAddress.refine(moniker, refineText);

        if((pickList.getItems().length == 1) && (pickList.getItems()[0].isFullAddress()))
        {
            FormattedAddress formatResult = qAddress.getFormattedAddress(addLayout, pickList.getItems()[0].getMoniker());

            %>
                <verifylevel>Verified</verifylevel>
                <dpvstatus><%=htmlEncode(formatResult.getDPVStatus().toString())%></dpvstatus>
                <address>
            <%
            for( AddressLine line : formatResult.getAddressLines())
            {
                %>
                    <line><%=htmlEncode(line.getLine())%></line>
                <%
            }
            %>
                </address>
            <%
        }
        else
        {
            %>
                <verifylevel>PremisesPartial</verifylevel>
                <picklist>
                <fullmoniker><%=htmlEncode(pickList.getMoniker())%></fullmoniker>
            <%
            for( PicklistItem item : pickList.getItems())
            {
                %>
                    <picklistitem>
                        <partialtext><%=htmlEncode(item.getPartialAddress())%></partialtext>
                        <addresstext><%=htmlEncode(item.getText())%></addresstext>
                        <postcode><%=htmlEncode(item.getPostcode())%></postcode>
                        <moniker><%=htmlEncode(item.getMoniker())%></moniker>
                        <fulladdress><%=item.isFullAddress()%></fulladdress>
                    </picklistitem>
                <%
            }
            %>
                </picklist>
            <%
        }
    }


%>
</proweb>

