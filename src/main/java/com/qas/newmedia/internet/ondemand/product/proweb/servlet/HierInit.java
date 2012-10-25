/* ----------------------------------------------------------------------------
 * QAS On Demand
 * (c) 2004 QAS Ltd. All rights reserved.
 * File: HierInit.java
 * Created: 26-Apr-2004
 * ----------------------------------------------------------------------------
 */
package com.qas.newmedia.internet.ondemand.product.proweb.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Minimal command for first page, included to allow Back button to work correctly.  No proweb logic here.
 * Output attributes: DataId, UserInput
 */
public class HierInit implements Command {
    public static final String NAME = "HierInit";

        /* (non-Javadoc)
         * @see com.qas.ondemand_2011_03.servlet.Command#execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
         */
    public String execute(
            HttpServletRequest request,
            HttpServletResponse response) {
        HttpHelper.passThrough(request, Constants.DATA_ID);
        HttpHelper.passThrough(request, Constants.USER_INPUT);
        return Constants.HIER_INIT_PAGE;
    }

}
