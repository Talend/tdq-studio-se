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
package org.talend.dq.analysis;

import org.apache.log4j.Logger;

/**
 * DOC scorreia class global comment. Detailled comment
 */
public class PatternMain {

    private static Logger log = Logger.getLogger(PatternMain.class);

    /**
     * DOC scorreia Comment method "main".
     * 
     * @param args
     */
    public static void main(String[] args) {
        String[] connectionString = new String[] { "jdbc:mysql://localhost:3306", "jdbc:mysql://localhost:3306/",
                "jdbc:mysql://localhost:3306/weka" };
        for (String string : connectionString) {
            check(string);
        }
    }

    /**
     * DOC scorreia Comment method "check".
     * 
     * @param connectionString
     */
    private static void check(String connectionString) {
        // if (log.isDebugEnabled()) {
        // log.debug("connection string: " + connectionString);
        // }
        if (connectionString.matches("jdbc:mysql://\\p{Alnum}*\\:\\p{Digit}*.*")) {
            if (!connectionString.matches("jdbc:mysql://\\p{Alnum}*\\:\\p{Digit}*/(\\p{Alnum})+")) {
                if (log.isInfoEnabled()) {
                    log.info("INVALID Mysql connection string: " + connectionString);
                }
            } else {
                if (log.isInfoEnabled()) {
                    log.info("Valid Mysql connection string: " + connectionString);
                }

            }
        }
    }
}
