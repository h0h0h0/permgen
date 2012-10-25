/* ----------------------------------------------------------------------------
 * QAS On Demand > (c) QAS Ltd > www.qas.com
 *
 * Command.java
 *
 * ----------------------------------------------------------------------------
 */
package com.qas.newmedia.internet.ondemand.product.proweb.servlet;

import javax.servlet.http.*;

/**
 * Command interface defines one method - <code>execute</code>.  Concrete subclasses perform a well defined
 * task, operating on input parameters (i.e. <code>HttpServletRequest</code> attributes or parameters) and
 * producing ouput parameters (i.e. attributes set in the <code>request</code>) that may be displayed by the
 * destination page returned by <code>execute</code>.
 *
 */
public interface Command {
    /** Execute this command.
     * @param request
     * @param response
     * @return the destination JSP
     */
    public String execute(HttpServletRequest request,
            HttpServletResponse response);
}
