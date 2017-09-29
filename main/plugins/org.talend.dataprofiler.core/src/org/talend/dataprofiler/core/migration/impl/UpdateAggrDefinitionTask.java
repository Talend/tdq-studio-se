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
package org.talend.dataprofiler.core.migration.impl;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.talend.commons.utils.io.FilesUtils;
import org.talend.dataprofiler.core.migration.AbstractWorksapceUpdateTask;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dq.writer.EMFSharedResources;

/**
 * 
 * DOC qiongli class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 55206 2011-02-15 17:32:14Z mhirt $
 * 
 */
public class UpdateAggrDefinitionTask extends AbstractWorksapceUpdateTask {

    protected static String[] needUpateKeys;

    protected static HashMap<String, String[]> map = new HashMap<String, String[]>();

    private static Logger log = Logger.getLogger(UpdateAggrDefinitionTask.class);

    private List<IndicatorDefinition> indicatorsDefinitions = new ArrayList<IndicatorDefinition>();

    public Date getOrder() {
        return createDate(2012, 2, 29);
    }

    public MigrationTaskType getMigrationTaskType() {
        return MigrationTaskType.FILE;
    }

    protected void initializtion() {
        needUpateKeys = new String[] { "Simple Statistics", "Text Statistics", "Phone Number Statistics", "Catalog Overview", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                "Connection Overview", "Schema Overview", "Range", "IQR", "Summary Statistics" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

        String[] simpArray = new String[] { "Blank Count", "Distinct Count", "Duplicate Count", "Unique Count", "Null Count", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
                "Row Count", "Default Value Count" }; //$NON-NLS-1$ //$NON-NLS-2$
        map.put("Simple Statistics", simpArray); //$NON-NLS-1$
        String[] textArray = new String[] { "Average Length", "Maximal Length", "Minimal Length", "Minimal Length With Null", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                "Minimal Length With Blank", "Minimal Length With Blank and Null", "Maximal Length With Null", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                "Maximal Length With Blank", "Maximal Length With Blank and Null", "Average Length With Null", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                "Average Length With Blank", "Average Length With Blank and Null" }; //$NON-NLS-1$ //$NON-NLS-2$
        map.put("Text Statistics", textArray); //$NON-NLS-1$
        String[] phoneNumArray = new String[] { "Invalid Region Code Count", "Possible Phone Number Count", //$NON-NLS-1$ //$NON-NLS-2$
                "Valid Phone Number Count", "Valid Region Code Count", "Well Formed E164 Phone Number Count", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                "Well Formed International Phone Number Count", "Well Formed National Phone Number Count", "Format Frequency Pie" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        map.put("Phone Number Statistics", phoneNumArray); //$NON-NLS-1$
        String[] sumaryArray = new String[] { "IQR", "Mean", "Median", "Range" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        map.put("Summary Statistics", sumaryArray); //$NON-NLS-1$
        String[] connViewArray = new String[] { "Catalog Overview", "Schema Overview" }; //$NON-NLS-1$ //$NON-NLS-2$
        map.put("Connection Overview", connViewArray); //$NON-NLS-1$
        String[] cataViewArray = new String[] { "Schema Overview", "Table Overview" }; //$NON-NLS-1$ //$NON-NLS-2$
        map.put("Catalog Overview", cataViewArray); //$NON-NLS-1$
        String[] scheViewArray = new String[] { "Table Overview", "View Overview" }; //$NON-NLS-1$ //$NON-NLS-2$
        map.put("Schema Overview", scheViewArray); //$NON-NLS-1$
        String[] interRangeArray = new String[] { "Lower Quartile", "Upper Quartile" }; //$NON-NLS-1$ //$NON-NLS-2$
        map.put("IQR", interRangeArray); //$NON-NLS-1$
        String[] rangeArray = new String[] { "Maximum", "Minimum" }; //$NON-NLS-1$ //$NON-NLS-2$
        map.put("Range", rangeArray); //$NON-NLS-1$
    }

    @Override
    protected boolean doExecute() throws Exception {
        initializtion();

        try {
            for (IndicatorDefinition indiDefinition : getIndicatorsDefinitions()) {
                if (indiDefinition.eIsProxy()) {
                    continue;
                }
                String name = indiDefinition.getLabel();
                if (findName(name)) {
                    List<IndicatorDefinition> newAgrrDefiLs = new ArrayList<IndicatorDefinition>();
                    String[] array = map.get(name);
                    if (array == null) {
                        continue;
                    }
                    for (String label : array) {
                        IndicatorDefinition findDef = getIndicatorsDefinitionByLabel(label);
                        if (findDef != null) {
                            newAgrrDefiLs.add(findDef);
                        }
                    }
                    indiDefinition.getAggregatedDefinitions().clear();
                    indiDefinition.getAggregatedDefinitions().addAll(newAgrrDefiLs);
                    EMFSharedResources.getInstance().saveResource(indiDefinition.eResource());
                }
            }
            // clear the indicatorsDefinitions
            this.indicatorsDefinitions.clear();
        } catch (Exception exc) {
            log.error("do migration for UpdateAggrDefinitionTask failed:", exc); //$NON-NLS-1$
        }

        return true;
    }

    /**
     * DOC xqliu Comment method "getIndicatorsDefinitionByLabel".
     * 
     * @param label
     * @return
     */
    private IndicatorDefinition getIndicatorsDefinitionByLabel(String label) {
        for (IndicatorDefinition indDef : this.getIndicatorsDefinitions()) {
            if (label.equals(indDef.getLabel())) {
                return indDef;
            }
        }
        return null;
    }

    /**
     * DOC xqliu Comment method "getIndicatorsDefinitions".
     * 
     * @return
     */
    private List<IndicatorDefinition> getIndicatorsDefinitions() {
        if (this.indicatorsDefinitions.isEmpty()) {
            File sysIndsFolder = getWorkspacePath().append("TDQ_Libraries/Indicators/System Indicators").toFile(); //$NON-NLS-1$
            ArrayList<File> fileList = new ArrayList<File>();
            FilesUtils.getAllFilesFromFolder(sysIndsFolder, fileList, new FilenameFilter() {

                public boolean accept(File dir, String name) {
                    if (name.endsWith("definition")) {//$NON-NLS-1$
                        return true;
                    }
                    return false;
                }
            });
            for (File file : fileList) {
                IndicatorDefinition indDef = getIndicatorDefinitionFromFile(file);
                if (indDef != null) {
                    this.indicatorsDefinitions.add(indDef);
                }
            }
        }
        return this.indicatorsDefinitions;
    }

    /**
     * DOC xqliu Comment method "getIndicatorDefinitionFromFile".
     * 
     * @param file
     * @return
     */
    private IndicatorDefinition getIndicatorDefinitionFromFile(File file) {
        Resource itemResource = getResource(file);
        for (EObject object : itemResource.getContents()) {
            if (object instanceof IndicatorDefinition) {
                return (IndicatorDefinition) object;
            }
        }

        return null;
    }

    private boolean findName(String name) {
        if (name == null) {
            return false;
        }
        for (String na : needUpateKeys) {
            if (na.equals(name)) {
                return true;
            }
        }
        return false;
    }

}
