/* ----------------------------------------------------------------------------
 * QAS On Demand > (c) QAS Ltd > www.qas.com
 *
 * This is the facility to display the dataplus infomation
 * producing the appropriate HTML code in String format
 *
 * ----------------------------------------------------------------------------
 */

package com.qas.newmedia.internet.ondemand.product.proweb.servlet;

import java.util.ArrayList;
import java.util.List;

import com.qas.newmedia.internet.ondemand.product.proweb.AddressLine;
import com.qas.newmedia.internet.ondemand.product.proweb.FormattedAddress;
import com.qas.ondemand_2011_03.DataplusGroupType;


public class DataplusDisplayHelper {


    private String m_sDataID = "";
    //private ArrayList m_MultiDataplusDisplayGroups;
    //private MultiDataplusControl[] m_MultiDataplusControls;
    private ArrayList tableAddress, tableMultiDPCtrl;

    /** Creates a new instance of DataplusDisplayHelper */
    public DataplusDisplayHelper() {
        tableAddress = new ArrayList();
        tableMultiDPCtrl = new ArrayList();
    }


    public String getDataID() {
        if (m_sDataID == null) {
            return "";
        }
        return this.m_sDataID;
    }

    public void setDataID(String value) {
        if ( value == null ) {
            m_sDataID = "";
        } else {
            m_sDataID = value;
        }

    }

    /*
    public ArrayList getMultiDataplusDisplayGroups() {
        return this.m_MultiDataplusDisplayGroups;
    }

    public MultiDataplusControl[] getMultiDataplusControls() {
        return this.m_MultiDataplusControls;
    }
    */

    public String getTableAddress() {
        String table = "";
        for (int i = 0; i < tableAddress.size(); i++) {
            table += (String)tableAddress.get(i);
        }
        return table;
    }

    public String getTableMultiDPCtrl() {
        String table = "";
        for (int i = 0; i < tableMultiDPCtrl.size(); i++) {
            table += (String)tableMultiDPCtrl.get(i);
        }
        return table;
    }

    protected void displayAddress( FormattedAddress addr ) {
        for( int iIndex = 0; iIndex < addr.getLength(); ++iIndex ) {
            if ( addr.getAddressLines(iIndex).getLineType().equals(AddressLine.DATAPLUS)) {
                if ( addr.getAddressLines(iIndex).getDataplusGroup() != null && addr.getAddressLines(iIndex).getDataplusGroup().size()!=0) {
                    /*
                	addMultiDataplusLine( addr.getAddressLines(iIndex).getLabel(),
                            addr.getAddressLines(iIndex).getDataplusGroup(),
                            iIndex );
                    */
                } else {
                    addDataplusLine(addr.getAddressLines(iIndex).getLabel(), addr.getAddressLines(iIndex).getLine(), iIndex);
                }
            } else {
                addAddressLine(addr.getAddressLines(iIndex).getLabel(), addr.getAddressLines(iIndex).getLine(), iIndex);
            }
        }

    }

    /// <summary>
    /// Dynamically populate the TableAddress table control from the arguments
    /// </summary>
    protected void displayAddress(String[] asLabels, String[] asLines) {
        for (int iIndex = 0; iIndex < asLines.length; ++iIndex) {
            addAddressLine(asLabels[iIndex], asLines[iIndex], iIndex);
        }
    }

    /// <summary>
    /// Add a table row, with cells for the label, a gap, and a text input control
    /// </summary>
    /// <returns>The text input control</returns>
    protected String addAddressLine(String sLabel, String sLine, int iLineNum) {
        String cellLabel = "<td class=\"label\">"+sLabel+"</td>";
        String cellAddress = "<td class=\"line\"><input name=\""+Constants.ADDRESS+iLineNum+"\" type=\"text\" id=\""+Constants.ADDRESS+iLineNum+"\" value=\""+sLine+"\" /></td>";

        String rowDetail = "<tr>" + cellLabel + cellAddress + "</tr>";
        tableAddress.add(rowDetail);

        return rowDetail;
    }
    /*
    public MultiDataplusControl[] getMultiDPControls() {
        if ( m_MultiDataplusControls == null && m_MultiDataplusDisplayGroups != null ) {

            ArrayList lControls = new ArrayList();
            ArrayList lGroupsUsed = new ArrayList();

            for (int i = 0; i < m_MultiDataplusDisplayGroups.size(); i++) {
                MultiDataplusDisplayGroup grp = (MultiDataplusDisplayGroup)m_MultiDataplusDisplayGroups.get(i);
                if ( !lGroupsUsed.contains(grp.getGroup()) && grp.getGroup() != "" ) {
                    MultiDataplusControl ctrl = new MultiDataplusControl();

                    ctrl.setGroup(grp.getGroup());

                    ctrl.setFwdID("fwd" + lControls.size());
                    ctrl.setBackID("bck" + lControls.size());
                    ctrl.setReturnID("rtn" + lControls.size());
                    ctrl.setIndexID("idx" + lControls.size());
                    ctrl.setRender((grp.getAItems().length > 1) ? true : false);

                    lGroupsUsed.add(grp.getGroup());
                    lControls.add(ctrl);
                }
            }

            m_MultiDataplusControls = (MultiDataplusControl[]) lControls.toArray(new MultiDataplusControl[0]);

        }

        return m_MultiDataplusControls;
    }
    public void renderMultiDataplusControls() {
        MultiDataplusControl[] aCtrls = getMultiDPControls();

        if (aCtrls != null) {
            for (int i = 0; i < aCtrls.length; i++) {
                MultiDataplusControl ctrl = aCtrls[i];
                if (ctrl.isRender() == true) {
                    String cellName = "<td>&nbsp;"+ctrl.getGroup()+"&nbsp;</td>";

                    String cellBack = "<td><input name=\""+ctrl.getBackID()+"\" type=\"button\" id=\""+ctrl.getBackID()+"\" value=\"<\" /></td>";
                    String cellIndex = "<td id=\""+ctrl.getIndexID()+"\"></td>";

                    String cellFwd = "<td><input name=\""+ctrl.getFwdID()+"\" type=\"button\" id=\""+ctrl.getFwdID()+"\" value=\">\" /></td>";

                    String cellRtn = "<td><input name=\""+ctrl.getReturnID()+"\" type=\"checkbox\" id=\""+ctrl.getReturnID()+"\" /></td>";

                    String cellRtnLabel = "<td>&nbsp;Return this</td>";

                    String rowDetail = "<tr>" + cellName + cellBack + cellIndex + cellFwd + cellRtn + cellRtnLabel + "</tr>";
                    tableMultiDPCtrl.add(rowDetail);
                }
            }
        }
    }
    protected void addMultiDataplusLine( String sLabel, List<DataplusGroupType> lGroupTypes, int iLineNum ) {
        if ( m_MultiDataplusDisplayGroups == null ) {
            m_MultiDataplusDisplayGroups = new ArrayList();
        }

        String rowDetail;

        rowDetail = "<tr id=\""+Constants.ADDRESS+iLineNum+"\" class=\"multidp\">";

        String cellLabel;
        cellLabel = "<td class=\"label\">"+sLabel+"</td>";
        String cellAddress;
        cellAddress = "<td class=\"line\">";


        String sElemID = "";

        for( int i = 0; i < lGroupTypes.size(); ++i ) {
            if (i != 0) {
                cellAddress += "<span id=\",\"></span>";
            }

            DataplusGroupType grpType = lGroupTypes.get(i);

            sElemID = grpType.getGroupName() + m_MultiDataplusDisplayGroups.size();

            MultiDataplusDisplayGroup dispGrp = new MultiDataplusDisplayGroup();

            dispGrp.setGroup(grpType.getGroupName());
            dispGrp.setLineNum(iLineNum);
            dispGrp.setElemID(sElemID);
            dispGrp.setAItems((String[]) grpType.getDataplusGroupItem().toArray());

            sElemID = "<span id=\""+sElemID+"\"></span>";
            cellAddress += sElemID;
            m_MultiDataplusDisplayGroups.add(dispGrp);
        }
        rowDetail += cellLabel + cellAddress + "</td></tr>";
        tableAddress.add(rowDetail);

    }
    */
    protected void addDataplusLine( String sLabel, String sLine, int iLineNum ) {
        String cellLabel = "";

        cellLabel += "<td class=\"label\">"+ sLabel +"</td>";
        String cellAddress = "";
        cellAddress += "<td class=\"line\"><input name=\""+ Constants.ADDRESS + iLineNum + "\" " +
                "type=\"text\" id=\""+ Constants.ADDRESS + iLineNum + "\" " +
                "value=\""+sLine+"\" /></td>";

        String rowDetail = "<tr>" + cellLabel + cellAddress + "</tr>";
        tableAddress.add(rowDetail);
    }

}
