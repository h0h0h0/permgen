/* ----------------------------------------------------------------------------
 * QAS On Demand > (c) QAS Ltd > www.qas.com
 *
 * Web > Verification > VerifyFormatAddress.java
 * Format the address selected from the picklist
 * ----------------------------------------------------------------------------
 */
package com.qas.newmedia.internet.ondemand.product.proweb.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.qas.newmedia.internet.ondemand.product.proweb.AddressLine;
import com.qas.newmedia.internet.ondemand.product.proweb.FormattedAddress;
import com.qas.newmedia.internet.ondemand.product.proweb.QasException;
import com.qas.newmedia.internet.ondemand.product.proweb.QuickAddress;
import com.qas.ondemand_2011_03.*;

/**
 * Command to encapsulate formatting addresses in the interaction case of the verification scenario.
 * Input params: CountryName (also set as output attribute), Moniker, UserInput[] (if error this is also set as output attribute)
 * Output attributes: Verified, Address[], ErrorInfo (if exception caught)
 */
public class VerifyFormatAddress implements Command {
    public static final String NAME = "VerifyFormatAddress";

        /* (non-Javadoc)
         * @see com.qas.ondemand_2011_03.servlet.Command#execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
         */
    public String execute(
            HttpServletRequest request,
            HttpServletResponse response) {
        HttpHelper.passThrough(request, Constants.COUNTRY_NAME);

        String sMoniker = HttpHelper.getValue(request, Constants.MONIKER);

        // get final address
        try {
            QuickAddress searchService = new QuickAddress(Constants.ENDPOINT, request);
            String sLayout = (String) request.getSession().getAttribute(Constants.LAYOUT);
            FormattedAddress address = searchService.getFormattedAddress(sLayout, sMoniker);
            address.setDpvMessage(request, address.getDPVStatus());

            AddressLine[] lines = address.getAddressLines();

            // set up output attributes
            String[] asAddress = new String[lines.length];
            for (int i=0; i < lines.length; i++) {
                asAddress[i] = lines[i].getLine();
            }
            request.setAttribute(Constants.ADDRESS, asAddress);
            request.setAttribute(Constants.ADDRESS_INFO, Verify.getAddressWarnings(address));
        } catch (Exception e) {
            // dump the exception to error stream
            System.err.println("~~~~~~ Caught exception in VerifyFromatAddress command ~~~~~~~");
            e.printStackTrace();
            System.err.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

            if (e instanceof QasException) {
            	QasException qe = (QasException)e;
            	request.setAttribute(Constants.ERROR_CODE, qe.getErrorNumber());
            }
            request.setAttribute(Constants.ROUTE, Constants.ROUTE_FAILED);

            // use user input as address in final screen
            request.setAttribute(Constants.ADDRESS, HttpHelper.getArrayValue(request, Constants.USER_INPUT) );
            request.setAttribute(Constants.ADDRESS_INFO, "address verification " + Constants.ROUTE_FAILED + ", so the entered address has been used");
            request.setAttribute(Constants.ERROR_INFO, e.toString());
        }

        return Constants.FINAL_ADDRESS_PAGE;
    }
}
