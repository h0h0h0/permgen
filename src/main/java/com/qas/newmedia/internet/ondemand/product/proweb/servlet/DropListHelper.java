/* ----------------------------------------------------------------------------
 * QAS On Demand > (c) QAS Ltd > www.qas.com
 *
 * DropListHelper.java
 * This class is to create the select list for the search input pages.
 * Producing the output (String) of options for the available dataset, together with available countries without duplicate
 *
 * ----------------------------------------------------------------------------
 */

package com.qas.newmedia.internet.ondemand.product.proweb.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import com.qas.newmedia.internet.ondemand.product.proweb.DataSet;
import com.qas.newmedia.internet.ondemand.product.proweb.QuickAddress;
import com.qas.ondemand_2011_03.QADataSet;


public class DropListHelper {

    private BufferedReader in = null;
    private String sError = "";
    private DataSet[] atDatasets = null;
    private ArrayList countriesList = null;
    private DataSet selected = null;

    QuickAddress searchService = null;


    /** Creates a new instance of DropListHelper */
    public DropListHelper(BufferedReader in, HttpServletRequest request) {
        initList(in, request);
    }

    /** produce HTML option tag of one DataSet object*/
    private String getOption(DataSet d) {
        return "<option value=\"" + d.getID() + "\">" + d.getName() + "</option>";
    }

    /** produce HTML option tag of selected DataSet object*/
    private String getSelectedOption(DataSet d) {
        return "<option selected value=\"" + d.getID() + "\">" + d.getName() + "</option>";
    }

    /** produce HTML options tag for the select drop list with given selected option*/
    public String getList(HttpServletRequest request, String sDataID) {
        String sList = getList();
        if (sDataID == null || sDataID.equals("")) {
            sDataID = selected.getID();
            request.setAttribute(Constants.DATA_ID, selected.getID());
            HttpHelper.passThrough(request, Constants.DATA_ID);
        }
        sList = sList.replaceAll("<option selected value=", "<option value=");
        sList = sList.replaceAll("<option value=\""+sDataID+"\">","<option selected value=\""+sDataID+"\">");
        return sList;
    }



    /** produce HTML options tag for the select drop list*/
    public String getList() {
        StringBuffer options = new StringBuffer();
        if (atDatasets != null && atDatasets.length != 0) {
            if (countriesList != null && countriesList.size() != 0) {
                options = options.append("<option value=\""+ "\" class=\"heading\">-- Datamaps available --</option>");
            }
            for (int i = 0; i < atDatasets.length; i ++) {
                if (i == 0 && selected == null){
                    selected = atDatasets[0];
                }
                options = options.append( getOption(atDatasets[i]) );
            }
        }

        if (countriesList != null && countriesList.size() != 0) {
            if (atDatasets != null && atDatasets.length != 0) {
                options = options.append( "<option value=\""+ "\" class=\"heading\">-- Other --</option>" );
            }
            for (int i = 0; i < countriesList.size(); i ++) {
                DataSet ds = (DataSet)countriesList.get(i);

                if (i == 0 && selected == null){
                    selected = ds;
                }

                if (selected != null &&
                        ds.getID().equals(selected.getID()) &&
                        ds.getName().equals(selected.getName())) {
                    options = options.append(getSelectedOption(ds));
                }

                else {
                    options = options.append(getOption(ds));
                }
            }
        }
        return options.toString();
    }


    /** initialise method*/
    public void initList(BufferedReader in, HttpServletRequest request) {
        atDatasets = initDataMap(request);
        if (in != null) {
            countriesList = initCountriesSet(in);
            countriesList = removeDuplicate(atDatasets, countriesList);
        }
    }


    /** initialise the arraylist of countries info from a file*/
    private ArrayList initCountriesSet(BufferedReader in) {
        ArrayList list;
        try {
            list = new ArrayList();
            String str;
            while ((str = in.readLine()) != null) {
                DataSet s = process(str);
                if (s != null) {
                    list.add(s);
                }
            }
            in.close();
            Collections.sort(list);
            return list;
        } catch (IOException e) {
            sError += "Countries file is not found</br>\n";
        }
        return null;
    }

    /** read in and produce the DataSet object */

    private DataSet process(String str) {

        String value_head = "<option value=\"";
        String value_tail = "\">";
        String option_end = "</option>";
        String match = "<option value=\".*\">.*</option>";
        String matchSelected = "<option selected value=\".*\">.*</option>";
        String id, name;

        QADataSet s;
        DataSet ds;

        if (str.matches(match) || str.matches(matchSelected)) {
            s = new QADataSet();
            id = str.substring(value_head.length(),str.indexOf(value_tail));
            name = str.substring(str.indexOf(value_tail)+value_tail.length(),str.indexOf(option_end));
            s.setID(id);
            s.setName(name);
            ds = new DataSet(s);
            if (str.matches(matchSelected)) {
                selected = ds;
            }
            return ds;
        }
        return null;
    }

    /** initialise the array of Datamap*/
    private DataSet[] initDataMap(HttpServletRequest request) {
        try {
            searchService = new QuickAddress(Constants.ENDPOINT, request);
            atDatasets = searchService.getAllDataSets();
            Arrays.sort(atDatasets);

            if (atDatasets == null) {
                sError += "Unable to retrieve datamaps from the Pro On Demand server.  Please try again later.<br />";
            }
            return atDatasets;
        } catch (Exception x) {
            sError += "The Pro On Demand server is currently unavailable.  Please try again later. " + x.getMessage() + "<br />";
        }
        return null;
    }

    /** remove the duplicate from the arraylist according to data map value */
    private ArrayList removeDuplicate(DataSet [] atDatasets, ArrayList countriesList) {
        if (atDatasets != null) {
            for (int j = 0 ; j < atDatasets.length; j++) {
                if (countriesList != null && countriesList.size() != 0) {
                    for (int i = 0 ; i < countriesList.size(); i++) {
                        DataSet serverDset = atDatasets[j];
                        DataSet dset = (DataSet) countriesList.get(i);
                        if (serverDset.getName().equalsIgnoreCase(dset.getName()) || serverDset.getID().equals(dset.getID())) {
                            countriesList.remove(i);
                            break;
                        }
                    }
                }
            }
        }
        return countriesList;
    }
    public String getErrorMessage() {
        return sError;
    }

    public static String initDropListHelper(HttpServlet servlet, HttpServletRequest request) {
        String command = request.getParameter(Constants.COMMAND);
        BufferedReader br;
        String file = "";
        if (command.equals(HierInit.NAME)) {
            file = "/countries.all.inc";
        } else if (command.equals(FlatInit.NAME)){
            file = "/countries.all.inc";
        } else if (command.equals(VerifyInit.NAME)){
            file = "/countries.all.inc";
        } else if (command.equals(RapidInit.NAME) || command.equals(RapidSearch.NAME) || command.equals(RapidAddress.NAME)){
            file = "/countries.ok.inc";
        } else {
            file = "/countries.all.inc";
        }
        if (!file.equals(""))
            br = new BufferedReader(new InputStreamReader(servlet.getServletContext().getResourceAsStream(file)));
        else
            br = null;

        DropListHelper dl = new DropListHelper(br, request);

        if (!(dl.getErrorMessage().equals("")) && dl.getErrorMessage() != null) {
            String error = (String) request.getAttribute(Constants.ERROR_INFO);
            if (error == null || error.equals(""))
                error = dl.getErrorMessage();
            else
                error += "</br>" + dl.getErrorMessage();
            request.getSession().setAttribute(Constants.ERROR_INFO, error);
            request.setAttribute(Constants.ERROR_INFO, error);
        } else {
            request.getSession().setAttribute(Constants.ERROR_INFO, null);
        }

        return dl.getList(request, (String)request.getParameter(Constants.DATA_ID));

    }
}
