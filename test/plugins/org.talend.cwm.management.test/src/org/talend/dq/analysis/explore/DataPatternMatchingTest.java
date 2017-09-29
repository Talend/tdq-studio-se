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
package org.talend.dq.analysis.explore;

import static org.junit.Assert.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

/**
 * DOC scorreia class global comment. Detailled comment
 */
public class DataPatternMatchingTest {

    private static final String REGEX = "SELECT (.*)\\s*, COUNT\\(\\*\\)\\s*(AS|as)?\\s*\\w*\\s* FROM"; //$NON-NLS-1$

    @Test
    public void test() {
        String body = "SELECT REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(`TEST_STRING_NULL`,'B','A'),'C','A'),'D','A'),'E','A'),'F','A'),'G','A'),'H','A'),'I','A'),'J','A'),'K','A'),'L','A'),'M','A'),'N','A'),'O','A'),'P','A'),'Q','A'),'R','A'),'S','A'),'T','A'),'U','A'),'V','A'),'W','A'),'X','A'),'Y','A'),'Z','A'),'b','a'),'c','a'),'d','a'),'e','a'),'f','a'),'g','a'),'h','a'),'i','a'),'j','a'),'k','a'),'l','a'),'m','a'),'n','a'),'o','a'),'p','a'),'q','a'),'r','a'),'s','a'),'t','a'),'u','a'),'v','a'),'w','a'),'x','a'),'y','a'),'z','a'),'1','9'),'2','9'),'3','9'),'4','9'),'5','9'),'6','9'),'7','9'),'8','9'),'0','9'), COUNT(*) AS c FROM `TEST_DATAPROFILER`.`TEST_DUPLICATES` t  GROUP BY REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(`TEST_STRING_NULL`,'B','A'),'C','A'),'D','A'),'E','A'),'F','A'),'G','A'),'H','A'),'I','A'),'J','A'),'K','A'),'L','A'),'M','A'),'N','A'),'O','A'),'P','A'),'Q','A'),'R','A'),'S','A'),'T','A'),'U','A'),'V','A'),'W','A'),'X','A'),'Y','A'),'Z','A'),'b','a'),'c','a'),'d','a'),'e','a'),'f','a'),'g','a'),'h','a'),'i','a'),'j','a'),'k','a'),'l','a'),'m','a'),'n','a'),'o','a'),'p','a'),'q','a'),'r','a'),'s','a'),'t','a'),'u','a'),'v','a'),'w','a'),'x','a'),'y','a'),'z','a'),'1','9'),'2','9'),'3','9'),'4','9'),'5','9'),'6','9'),'7','9'),'8','9'),'0','9') ORDER BY c ASC LIMIT 10"; //$NON-NLS-1$
        Pattern p = Pattern.compile(REGEX, Pattern.CASE_INSENSITIVE);
        Matcher matcher = p.matcher(body);
        matcher.find();
        final int groupCount = matcher.groupCount();

        assertEquals(2, groupCount);
        assertEquals(
                "REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(`TEST_STRING_NULL`,'B','A'),'C','A'),'D','A'),'E','A'),'F','A'),'G','A'),'H','A'),'I','A'),'J','A'),'K','A'),'L','A'),'M','A'),'N','A'),'O','A'),'P','A'),'Q','A'),'R','A'),'S','A'),'T','A'),'U','A'),'V','A'),'W','A'),'X','A'),'Y','A'),'Z','A'),'b','a'),'c','a'),'d','a'),'e','a'),'f','a'),'g','a'),'h','a'),'i','a'),'j','a'),'k','a'),'l','a'),'m','a'),'n','a'),'o','a'),'p','a'),'q','a'),'r','a'),'s','a'),'t','a'),'u','a'),'v','a'),'w','a'),'x','a'),'y','a'),'z','a'),'1','9'),'2','9'),'3','9'),'4','9'),'5','9'),'6','9'),'7','9'),'8','9'),'0','9')", //$NON-NLS-1$
                matcher.group(1));
    }
}
