/* ----------------------------------------------------------------------------
 * QAS On Demand
 * (c) 2004 QAS Ltd. All rights reserved.
 * File: FlatInit.java
 * Created: 21-Apr-2004
 * ----------------------------------------------------------------------------
 */
package com.qas.newmedia.internet.ondemand.product.proweb.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Minimal command for first page in the flattened scenario.
 * No proweb logic here, this is required for the Back command of later pages to work.
 * Input/Output params: DataId is set as an output attribute if present.
 *
 */
public class FlatInit implements Command {
    public static final String NAME = "FlatInit";

    /**
     * @see com.qas.ondemand_2011_03.servlet.Command#execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public String execute(
            HttpServletRequest request,
            HttpServletResponse response) {
        HttpHelper.passThrough(request, Constants.DATA_ID);

        return Constants.FLAT_INIT_PAGE;
    }

}
