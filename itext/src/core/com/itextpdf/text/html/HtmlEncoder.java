/*
 * $Id$
 *
 * This file is part of the iText project.
 * Copyright (c) 1998-2009 1T3XT BVBA
 * Authors: Bruno Lowagie, Paulo Soares, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY 1T3XT,
 * 1T3XT DISCLAIMS THE WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA, or download the license from the following URL:
 * http://itextpdf.com/terms-of-use/
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License,
 * you must retain the producer line in every PDF that is created or manipulated
 * using iText.
 *
 * You can be released from the requirements of the license by purchasing
 * a commercial license. Buying such a license is mandatory as soon as you
 * develop commercial activities involving the iText software without
 * disclosing the source code of your own applications.
 * These activities include: offering paid services to customers as an ASP,
 * serving PDFs on the fly in a web application, shipping iText with a closed
 * source product.
 *
 * For more information, please contact iText Software Corp. at this
 * address: sales@itextpdf.com
 */
package com.itextpdf.text.html;

import java.awt.Color;

import com.itextpdf.text.Element;

/**
 * This class converts a <CODE>String</CODE> to the HTML-format of a String.
 * <P>
 * To convert the <CODE>String</CODE>, each character is examined:
 * <UL>
 * <LI>ASCII-characters from 000 till 031 are represented as &amp;#xxx;<BR>
 *     (with xxx = the value of the character)
 * <LI>ASCII-characters from 032 t/m 127 are represented by the character itself, except for:
 *     <UL>
 *     <LI>'\n'	becomes &lt;BR&gt;\n
 *     <LI>&quot; becomes &amp;quot;
 *     <LI>&amp; becomes &amp;amp;
 *     <LI>&lt; becomes &amp;lt;
 *     <LI>&gt; becomes &amp;gt;
 *     </UL>
 * <LI>ASCII-characters from 128 till 255 are represented as &amp;#xxx;<BR>
 *     (with xxx = the value of the character)
 * </UL>
 * <P>
 * Example:
 * <P><BLOCKQUOTE><PRE>
 *    String htmlPresentation = HtmlEncoder.encode("Marie-Th&#233;r&#232;se S&#248;rensen");
 * </PRE></BLOCKQUOTE><P>
 * for more info: see O'Reilly; "HTML: The Definitive Guide" (page 164)
 *
 * @author  mario.maccarini@ugent.be
 */

public final class HtmlEncoder {
    
    // membervariables
    
/** List with the HTML translation of all the characters. */
    private static final String[] htmlCode = new String[256];
    
    static {
        for (int i = 0; i < 10; i++) {
            htmlCode[i] = "&#00" + i + ";";
        }
        
        for (int i = 10; i < 32; i++) {
            htmlCode[i] = "&#0" + i + ";";
        }
        
        for (int i = 32; i < 128; i++) {
            htmlCode[i] = String.valueOf((char)i);
        }
        
        // Special characters
        htmlCode['\t'] = "\t";
        htmlCode['\n'] = "<" + HtmlTags.NEWLINE + " />\n";
        htmlCode['\"'] = "&quot;"; // double quote
        htmlCode['&'] = "&amp;"; // ampersand
        htmlCode['<'] = "&lt;"; // lower than
        htmlCode['>'] = "&gt;"; // greater than
        
        for (int i = 128; i < 256; i++) {
            htmlCode[i] = "&#" + i + ";";
        }
    }
    
    
    // constructors
    
/**
 * This class will never be constructed.
 * <P>
 * HtmlEncoder only contains static methods.
 */
    
    private HtmlEncoder () { }
    
    // methods
    
/**
 * Converts a <CODE>String</CODE> to the HTML-format of this <CODE>String</CODE>.
 *
 * @param	string	The <CODE>String</CODE> to convert
 * @return	a <CODE>String</CODE>
 */
    
    public static String encode(String string) {
        int n = string.length();
        char character;
        StringBuffer buffer = new StringBuffer();
        // loop over all the characters of the String.
        for (int i = 0; i < n; i++) {
            character = string.charAt(i);
            // the Htmlcode of these characters are added to a StringBuffer one by one
            if (character < 256) {
                buffer.append(htmlCode[character]);
            }
            else {
                // Improvement posted by Joachim Eyrich
                buffer.append("&#").append((int)character).append(';');
            }
        }
        return buffer.toString();
    }
    
/**
 * Converts a <CODE>Color</CODE> into a HTML representation of this <CODE>Color</CODE>.
 *
 * @param	color	the <CODE>Color</CODE> that has to be converted.
 * @return	the HTML representation of this <COLOR>Color</COLOR>
 */
    
    public static String encode(Color color) {
        StringBuffer buffer = new StringBuffer("#");
        if (color.getRed() < 16) {
            buffer.append('0');
        }
        buffer.append(Integer.toString(color.getRed(), 16));
        if (color.getGreen() < 16) {
            buffer.append('0');
        }
        buffer.append(Integer.toString(color.getGreen(), 16));
        if (color.getBlue() < 16) {
            buffer.append('0');
        }
        buffer.append(Integer.toString(color.getBlue(), 16));
        return buffer.toString();
    }
    
/**
 * Translates the alignment value.
 *
 * @param   alignment   the alignment value
 * @return  the translated value
 */
    
    public static String getAlignment(int alignment) {
        switch(alignment) {
            case Element.ALIGN_LEFT:
                return HtmlTags.ALIGN_LEFT;
            case Element.ALIGN_CENTER:
                return HtmlTags.ALIGN_CENTER;
            case Element.ALIGN_RIGHT:
                return HtmlTags.ALIGN_RIGHT;
            case Element.ALIGN_JUSTIFIED:
            case Element.ALIGN_JUSTIFIED_ALL:
                return HtmlTags.ALIGN_JUSTIFIED;
            case Element.ALIGN_TOP:
                return HtmlTags.ALIGN_TOP;
            case Element.ALIGN_MIDDLE:
                return HtmlTags.ALIGN_MIDDLE;
            case Element.ALIGN_BOTTOM:
                return HtmlTags.ALIGN_BOTTOM;
            case Element.ALIGN_BASELINE:
                return HtmlTags.ALIGN_BASELINE;
                default:
                    return "";
        }
    }
}