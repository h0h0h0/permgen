/* ----------------------------------------------------------------------------
 * QAS Pro Web
 * (c) 2004 QAS Ltd. All rights reserved.
 * File: KeyInit.java
 * Created: 06-Sep-2007
 * ----------------------------------------------------------------------------
 */
package com.qas.newmedia.internet.ondemand.product.proweb.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Minimal command for first page in the key search scenario.
 * No proweb logic here, this is required for the Back command of later pages to work.
 * Input/Output params: DataId is set as an output attribute if present.
 *
 */
public class KeyInit implements Command {
    public static final String NAME = "KeyInit";
    
    /**
     * @see com.qas.proweb.servlet.Command#execute(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public String execute(
            HttpServletRequest request,
            HttpServletResponse response) {
        HttpHelper.passThrough(request, Constants.DATA_ID);
        
        return Constants.KEY_INIT_PAGE;
    }
    
}
