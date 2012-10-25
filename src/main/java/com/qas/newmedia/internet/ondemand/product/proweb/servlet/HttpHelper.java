/* ----------------------------------------------------------------------------
 * QAS On Demand
 * (c) 2004 QAS Ltd. All rights reserved.
 * File: HttpHelper.java
 * Created: 19-Apr-2004
 * ----------------------------------------------------------------------------
 */
package com.qas.newmedia.internet.ondemand.product.proweb.servlet;

import javax.servlet.http.*;
import java.util.*;

/**
 * Utility class for dealing with parameters and attributes in request objects
 * that also serves as a command factory.
 */
public class HttpHelper {
    /** Factory method for constructing Command objects of the class represented by the "Command" parameter of the request passed in */
    public static Command getCommand(HttpServletRequest request)
    throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        String sCommand = request.getParameter(Constants.COMMAND);
        if (sCommand != null) {
			String sFullyScopedClassName = "com.qas.newmedia.internet.ondemand.product.proweb.servlet." + sCommand;
            Class commandClass = Class.forName(sFullyScopedClassName);

            return (Command) commandClass.newInstance();
        }
        return null;
    }

    /** Helper to retrieve the request parameter/attribute. checks for an attribute first.
     * Returns the value of the attribute set in the request, or "" if the attribute AND the parameter were null
     */
    public static String getValue(HttpServletRequest request, String sParam) {
        String sValue = (String) request.getAttribute(sParam);
        if (sValue == null) {
            sValue = request.getParameter(sParam);
            if (sValue == null) {
                sValue = "";
            }
        }
        return sValue;
    }

    /** Helper to set the request parameter as an attribute, as well as retrieve the value.
     * Returns the value of the attribute set in the request, or "" if the attribute AND the parameter were null
     */
    public static String passThrough(HttpServletRequest request, String sParam) {
        String sValue = getValue(request, sParam);
        request.setAttribute(sParam, sValue);
        return sValue;
    }

    /** Helper to pass the (String array) request parameter through as an attribute IF the array is not already an attribute.
     * Returns <code>String[]</code> values of the set attrbute (or array of ""
     * if the param or attribute was not in the request)
     */
    public static String[] passThroughArray(HttpServletRequest request, String sParam) {
        return passThroughArrayN(request, sParam, 1);
    }

    /** Helper to retrieve an array - checks attribute then parameters.
     *
     * Return <code>String[]</code> values of the attribute/parameters (or array of ""
     * if the param or attribute was not in the request)
     */
    public static String[] getArrayValue(HttpServletRequest request, String sParam) {
        String[] asAttValues = (String[]) request.getAttribute(sParam);
        String[] asValues = null;
        if (asAttValues == null) {
            asValues = request.getParameterValues(sParam);
            if (asValues == null) {
                asValues = new String[] { "" };
            }
        } else {   // attribute present
            asValues = asAttValues;
        }
        return asValues;
    }

    /** Helper to pass the (String stack) request parameter through as an attribute
     * Returns <code>Stack</code> of <code>String</code> values of the attribute/parameters (or empty if unset)
     */
    public static Stack passThroughStack(HttpServletRequest request, String sParam) {
        Stack stack = new Stack();
        String[] asValues = (String[]) request.getAttribute(sParam);
        if (asValues == null) {
            asValues = request.getParameterValues(sParam);
        }
        if (asValues != null) {
            request.setAttribute(sParam, asValues);
            for (int i=0; i < asValues.length; i++) {
                stack.push(asValues[i]);
            }
        }
        return stack;
    }

    /** Helper to retrieve a array from a request
     * and convert it into a stack - checks attribute then parameters.
     *
     * Returns <code>Stack</code> of <code>String</code> values of the attribute/parameters (or empty if unset)
     */
    public static Stack requestArrayToStack(HttpServletRequest request, String sParam) {
        Stack stack = new Stack();
        String[] asValues = (String[]) request.getAttribute(sParam);
        if (asValues == null) {
            asValues = request.getParameterValues(sParam);
        }
        if (asValues != null) {
            for (int i=0; i < asValues.length; i++) {
                stack.push(asValues[i]);
            }
        }
        return stack;
    }

    /** Helper to convert a (history) stack into an array of <code>String</code>
     *
     * Returns <code>String[]</code>
     */
    public static String[] toStringArray(Stack aStack) {
        int iSize = aStack.size();
        String[] asValues = new String[iSize];
        for (int i=0; i < iSize; i++) {
            asValues[i] = aStack.get(i).toString();
        }
        return asValues;
    }

    /** Helper to concatenate the elements of a <code>String[]</code> together
     * Each element is separated by sSep
     */
    static public String join(String[] asIn, String sSep) {
        if (asIn.length == 0) {
            return "";
        } else {
            StringBuffer sResult = new StringBuffer(asIn[0]);
            for (int i = 1; i < asIn.length; ++i) {
                sResult.append(sSep + asIn[i]);
            }
            return sResult.toString();
        }
    }

    /** Method override which defaults to joining the elements with a space
     */
    static public String join(String[] asIn) {
        return join(asIn, " ");
    }

    /** Helper to pass the (String array) request parameter through as an attribute IF the array is not already an attribute.
     * If the array exists (either as an attribute or parameter) but is smaller than iSize the values are overwritten with ""
     * @param iSize the expected size of the array
     * Returns <code>String[iSize]</code> of values for the attribute set (or array with "" as each element
     * if the param or attribute was not in the request)
     */
    public static String[] passThroughArrayN(HttpServletRequest request, String sParam, int iSize) {
        String[] asAttValues = (String[]) request.getAttribute(sParam);
        if (asAttValues  == null)
            asAttValues = (String []) request.getParameterValues(sParam);
        String[] asValues = null;
        boolean bResetArray = false;
        if (asAttValues == null) {
            asValues = request.getParameterValues(sParam);
            if (asValues == null || (asValues.length < iSize)) {
                bResetArray = true;
            }
        } else {   // attribute already present ...
            if (asAttValues.length < iSize) {   // ... but check size
                bResetArray = true;
            } else {
                request.setAttribute(sParam, asAttValues);
                return asAttValues;
            }
        }

        if (bResetArray) {
            asValues = new String[iSize];
            for (int i =0; i < iSize; i++) {
                asValues[i]= "";
            }

            if (asAttValues != null) {
                for (int i =0; i < asAttValues.length; i++) {
                    asValues[i]= asAttValues[i];
                }
            }
        }
        request.setAttribute(sParam, asValues);
        return asValues;
    }

    /** DEBUG helper only. Prints the values of all parameters and attributes in the request object to stdout */
    public static String requestParmsToString(HttpServletRequest request) {
        StringBuffer buff = new StringBuffer("RequestParameters:");
        Enumeration namesIter = request.getParameterNames();
        while(namesIter.hasMoreElements()) {
            String sName = (String) namesIter.nextElement();
            buff.append(sName);
            Object[] asValues = request.getParameterValues(sName);
            for (int i=0; i < asValues.length; i++) {
                buff.append("...");
                buff.append(asValues[i].toString());
                buff.append("\n");
            }
        }
        return buff.toString() + requestAttributesToString(request);
    }

    /** DEBUG helper only. Prints the values of all attributes in the request object to stdout */
    public static String requestAttributesToString(HttpServletRequest request) {
        StringBuffer buff = new StringBuffer("RequestAttributes:");
        Enumeration namesIter = request.getAttributeNames();
        while(namesIter.hasMoreElements()) {
            String sName = (String) namesIter.nextElement();
            buff.append(sName);
            Object value = request.getAttribute(sName);
            if (value instanceof Object[]) {
                Object[] asValues = (Object[]) value;
                for (int i=0; i < asValues.length; i++) {
                    buff.append("...");
                    buff.append(asValues[i].toString());
                    buff.append("\n");
                }
            } else {
                buff.append("...");
                buff.append(value);
                buff.append("\n");
            }
        }
        return buff.toString();
    }

}
