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
package org.talend.dq.indicators;

import java.io.File;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.talend.commons.emf.EMFUtil;
import org.talend.dataquality.indicators.FrequencyIndicator;
import org.talend.dataquality.indicators.util.IndicatorsSwitch;

/**
 * DOC scorreia class global comment. Detailled comment
 */
public class LoadSerialDataMain {

    /**
     * DOC scorreia Comment method "main".
     * 
     * @param args
     */
    public static void main(String[] args) {
        EMFUtil util = new EMFUtil();
        File file = new File("out/columnTest_0.1.ana");
        System.out.println("Loading file " + file.getAbsolutePath());
        ResourceSet rs = util.getResourceSet();
        Resource r = rs.getResource(URI.createFileURI(file.getAbsolutePath()), true);

        EList<EObject> contents = r.getContents();
        if (contents.isEmpty()) {
            System.err.println("No content in " + r);
        }
        System.out.println("Nb elements in contents " + contents.size());
        IndicatorsSwitch<FrequencyIndicator> mySwitch = new IndicatorsSwitch<FrequencyIndicator>() {

            @Override
            public FrequencyIndicator caseFrequencyIndicator(FrequencyIndicator object) {
                return object;
            }

        };
        for (EObject object : contents) {
            FrequencyIndicator freqI = mySwitch.doSwitch(object);
            if (freqI != null) {
                Long uniqueValueCount = freqI.getUniqueValueCount();
                System.out.println("nb unique values = " + uniqueValueCount);
                EList<Object> uniqueValues = freqI.getUniqueValues();
                for (Object data : uniqueValues) {
                    System.out.println("unique value= " + data + " " + freqI.getCount(data));
                }
            }
        }
    }

}
