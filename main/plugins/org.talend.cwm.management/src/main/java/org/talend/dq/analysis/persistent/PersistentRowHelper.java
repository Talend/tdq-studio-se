// ============================================================================
//
// Copyright (C) 2006-2017 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.dq.analysis.persistent;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * created by zhao on Oct 16, 2013 Detailled comment
 * 
 */
public class PersistentRowHelper {

    private static final String UTF8 = "UTF-8"; //$NON-NLS-1$

    @SuppressWarnings("unchecked")
    public static int checkNullsAndCompare(Object object1, Object object2) {
        int returnValue = 0;
        if (object1 instanceof Comparable && object2 instanceof Comparable) {
            returnValue = ((Comparable<Object>) object1).compareTo(object2);
        } else if (object1 != null && object2 != null) {
            returnValue = compareStrings(object1.toString(), object2.toString());
        } else if (object1 == null && object2 != null) {
            returnValue = 1;
        } else if (object1 != null && object2 == null) {
            returnValue = -1;
        } else {
            returnValue = 0;
        }
        return returnValue;
    }

    private static int compareStrings(String string1, String string2) {
        return string1.compareTo(string2);
    }

    /**
     * DOC zhao Comment method "writeString".
     * 
     * @param line
     * @param dos
     * @throws IOException
     */
    public static void writeString(String str, ObjectOutputStream dos) throws IOException {
        if (str == null) {
            dos.writeInt(-1);
        } else {
            byte[] byteArray = str.getBytes(UTF8);
            dos.writeInt(byteArray.length);
            dos.write(byteArray);
        }
    }

    public static void writeString(String str, DataOutputStream dos) throws IOException {
        if (str == null) {
            dos.writeInt(-1);
        } else {
            byte[] byteArray = str.getBytes(UTF8);
            dos.writeInt(byteArray.length);
            dos.write(byteArray);
        }
    }

    /**
     * DOC zhao Comment method "readString".
     * 
     * @param dis
     * @throws IOException
     */
    public static String readString(byte[] readByteArray, ObjectInputStream dis) throws IOException {
        String strReturn = null;
        int length = 0;
        length = dis.readInt();

        byte[] byteArrayToRead = readByteArray;
        if (length == -1) {
            strReturn = null;
        } else {
            if (length > byteArrayToRead.length) {
                if (length < 1024 && byteArrayToRead.length == 0) {
                    byteArrayToRead = new byte[1024];
                } else {
                    byteArrayToRead = new byte[2 * length];
                }
            }
            dis.readFully(byteArrayToRead, 0, length);
            strReturn = new String(byteArrayToRead, 0, length, UTF8);
        }
        return strReturn;
    }

    public static String readString(DataInputStream dis) throws IOException {
        String strReturn = null;
        int length = 0;
        length = dis.readInt();

        if (length == -1) {
            strReturn = null;
        } else {
            byte[] byteArray = new byte[length];
            dis.read(byteArray);
            strReturn = new String(byteArray, UTF8);
        }
        return strReturn;
    }

}
