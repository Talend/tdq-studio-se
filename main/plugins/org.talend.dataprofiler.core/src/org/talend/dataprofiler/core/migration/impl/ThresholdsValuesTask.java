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

import java.util.Collection;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.common.util.EList;
import org.talend.dataprofiler.core.migration.AbstractWorksapceUpdateTask;
import org.talend.dataprofiler.core.migration.helper.WorkspaceVersionHelper;
import org.talend.dataprofiler.migration.helper.VersionComparator;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.helpers.IndicatorHelper;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dq.helper.resourcehelper.AnaResourceFileHelper;
import org.talend.dq.writer.impl.ElementWriterFactory;
import org.talend.resource.ResourceManager;
import org.talend.utils.ProductVersion;

/**
 * DOC bZhou class global comment. Detailled comment
 */
public class ThresholdsValuesTask extends AbstractWorksapceUpdateTask {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.migration.AWorkspaceTask#valid()
     */
    @Override
    public boolean valid() {
        // There handle a special case, the Thresholds is different in TOP and TDQ in 3.2.2(r33000)
        ProductVersion vesion = WorkspaceVersionHelper.getVesion();
        ProductVersion version322 = new ProductVersion(3, 2, 2);
        if (VersionComparator.isEqual(vesion, version322) && ResourceManager.getReportsFolder().exists()) {
            return false;
        }

        return super.valid();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.migration.IWorkspaceMigrationTask#getMigrationTaskType()
     */
    public MigrationTaskType getMigrationTaskType() {
        return MigrationTaskType.FILE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.migration.AMigrationTask#doExecute()
     */
    @Override
    protected boolean doExecute() throws Exception {

        Collection<Analysis> allAnalysis = (Collection<Analysis>) AnaResourceFileHelper.getInstance().getAllElement();
        for (Analysis analysis : allAnalysis) {
            EList<Indicator> indicators = analysis.getResults().getIndicators();
            for (Indicator indicator : indicators) {
                String[] pThresholds = IndicatorHelper.getIndicatorThresholdInPercent(indicator);
                if (pThresholds != null) {
                    String sOldMin = pThresholds[0];
                    String sOldMax = pThresholds[1];

                    String sNewMin = null, sNewMax = null;
                    if (StringUtils.isNotBlank(sOldMin)) {
                        sNewMin = String.valueOf(Double.valueOf(sOldMin) / 100);
                    }

                    if (StringUtils.isNotBlank(sOldMax)) {
                        sNewMax = String.valueOf(Double.valueOf(sOldMax) / 100);
                    }

                    IndicatorHelper.setIndicatorThresholdInPercent(indicator.getParameters(), sNewMin, sNewMax);
                }
            }

            ElementWriterFactory.getInstance().createAnalysisWrite().save(analysis);
        }

        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.migration.IMigrationTask#getOrder()
     */
    public Date getOrder() {
        // MOD xqliu 2010-08-02 bug 14698, this task must be called after MergeMetadataTask done.
        return createDate(2010, 6, 25);
        // return createDate(2010, 4, 6);
    }

}
