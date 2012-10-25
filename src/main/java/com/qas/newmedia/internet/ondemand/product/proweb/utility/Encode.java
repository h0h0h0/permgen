package com.qas.newmedia.internet.ondemand.product.proweb.utility;


public class Encode {
 
    public static String escapeJavaScript(String str) {
        return escapeJavaStyleString(str, true);
    }
    private static String escapeJavaStyleString(String str, boolean escapeSingleQuote){
        String result = "";
        if (str == null) {
            return "";
        }
        int sz;
        sz = str.length();
        for (int i = 0; i < sz; i++) {
            char ch = str.charAt(i);

            // handle unicode
            if (ch > 0xfff) {
                result += ("\\u" + hex(ch));
            } else if (ch > 0xff) {
                result += ("\\u0" + hex(ch));
            } else if (ch > 0x7f) {
                result += ("\\u00" + hex(ch));
            } else if (ch < 32) {
                switch (ch) {
                    case '\b':
                        result += ('\\');
                        result += ('b');
                        break;
                    case '\n':
                        result += ('\\');
                        result += ('n');
                        break;
                    case '\t':
                        result += ('\\');
                        result += ('t');
                        break;
                    case '\f':
                        result += ('\\');
                        result += ('f');
                        break;
                    case '\r':
                        result += ('\\');
                        result += ('r');
                        break;
                    default :
                        if (ch > 0xf) {
                            result += ("\\u00" + hex(ch));
                        } else {
                            result += ("\\u000" + hex(ch));
                        }
                        break;
                }
            } else {
                switch (ch) {
                    case '\'':
                        if (escapeSingleQuote) {
                          result += ('\\');
                        }
                        result += ('\'');
                        break;
                    case '"':
                        result += ('\\');
                        result += ('"');
                        break;
                    case '\\':
                        result += ('\\');
                        result += ('\\');
                        break;
                    default :
                        result += (ch);
                        break;
                }
            }
        }
        return result;
    }

    /**
     * <p>Returns an upper case hexadecimal <code>String</code> for the given
     * character.</p>
     * 
     * @param ch The character to convert.
     * @return An upper case hexadecimal <code>String</code>
     */
    private static String hex(char ch) {
        return Integer.toHexString(ch).toUpperCase();
    }
    private static Entities en;


    public static String escapeHtml(String str) {
        if (str == null) {
            return null;
        }
        return Entities.HTML40.escape(str);
    }

    public static String encodeEntities(String in) {
        String out      = "";
        char   c;
        int    inLength;
        
        /* Ensure the string is not null */
        if (in == null) return "";
        
        /* Get the length */
        inLength = in.length();
        
        /* Get each character and ensure it is valid for XML */
        for (int i=0; i < inLength; i++) {
            
            c = in.charAt(i);
            
            switch (c) {
                case '&':  out += "&amp;";
                break;
                case '\"': out += "&quot;";
                break;
                case '<':  out += "&lt;";
                break;
                case '>':  out += "&gt;";
                break;
                case '\'': out += "&apos;";
                break;
                case '\t': out += "&#9;";
                break;
                case '\n': out += "&#10;";
                break;
                case '\r': out += "&#13;";
                break;
                
                default  : if (c > 0x80) {
                    out += "&#" + ((int)c) + ";";
                } else {
                    out += c;
                }
            }
        }
        return out;
    }
}
