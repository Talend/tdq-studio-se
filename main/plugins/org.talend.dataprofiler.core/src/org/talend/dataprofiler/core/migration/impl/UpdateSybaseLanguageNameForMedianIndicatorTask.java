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
import java.util.Date;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.talend.commons.emf.EMFUtil;
import org.talend.commons.utils.WorkspaceUtils;
import org.talend.core.model.metadata.builder.database.dburl.SupportDBUrlType;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.PropertiesPackage;
import org.talend.core.model.properties.Property;
import org.talend.cwm.helper.ModelElementHelper;
import org.talend.cwm.relational.TdExpression;
import org.talend.dataprofiler.core.migration.AbstractWorksapceUpdateTask;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dataquality.properties.TDQIndicatorDefinitionItem;
import org.talend.dq.helper.PropertyHelper;
import org.talend.dq.indicators.definitions.DefinitionHandler;

/**
 * Update the language from "Adaptive Server Enterprise" to "Adaptive Server Enterprise | Sybase Adaptive Server IQ" in
 * Median Indicator.
 */
public class UpdateSybaseLanguageNameForMedianIndicatorTask extends AbstractWorksapceUpdateTask {

    private String medianLabel = "Median"; //$NON-NLS-1$

    private String sybase1 = "Adaptive Server Enterprise"; //$NON-NLS-1$

    private String sybase2 = SupportDBUrlType.SYBASEDEFAULTURL.getLanguage();

    public Date getOrder() {
        return createDate(2012, 9, 14);
    }

    public MigrationTaskType getMigrationTaskType() {
        return MigrationTaskType.FILE;
    }

    @Override
    protected boolean doExecute() throws Exception {
        boolean result = false;
        boolean needSave = false;
        IndicatorDefinition indicatorDefinition = DefinitionHandler.getInstance().getIndicatorDefinition(this.medianLabel);
        if (indicatorDefinition != null) {
            EList<TdExpression> sqlGenericExpression = indicatorDefinition.getSqlGenericExpression();
            for (TdExpression exp : sqlGenericExpression) {
                if (this.sybase1.equals(exp.getLanguage())) {
                    exp.setLanguage(this.sybase2);
                    needSave = true;
                }
            }
            if (needSave) {
                result = saveMedianIndicator(indicatorDefinition);
            } else {
                result = true;
            }
        }
        return result;
    }

    /**
     * save the System Indicator Definition: Median.
     * 
     * @param medianInd
     * @return
     */
    private boolean saveMedianIndicator(IndicatorDefinition medianInd) {
        URI uriItem = medianInd.eResource().getURI();
        File fileItem = null;
        if (uriItem.isPlatform()) {
            fileItem = WorkspaceUtils.ifileToFile(ModelElementHelper.getIFile(medianInd));
        } else {
            fileItem = new File(uriItem.toFileString());
        }

        File fileProp = WorkspaceUtils.ifileToFile(PropertyHelper.getPropertyFile(medianInd));
        Property property = PropertyHelper.getProperty(medianInd);
        Item item = property.getItem();

        // save TDQIndicatorDefinitionItem
        TDQIndicatorDefinitionItem indDefItem = (TDQIndicatorDefinitionItem) item;
        indDefItem.setIndicatorDefinition(medianInd);
        Resource itemResource = getResource(fileItem);
        EMFUtil.saveResource(itemResource);

        // save the property
        Resource propResource = getResource(fileProp);
        Property newProperty = (Property) EcoreUtil.getObjectByType(propResource.getContents(),
                PropertiesPackage.eINSTANCE.getProperty());

        newProperty.setAuthor(property.getAuthor());
        newProperty.setLabel(indDefItem.getIndicatorDefinition().getName());
        newProperty.setItem(item);

        item.setProperty(newProperty);

        propResource.getContents().clear();
        propResource.getContents().add(newProperty);
        propResource.getContents().add(item);
        propResource.getContents().add(item.getState());

        EMFUtil.saveResource(propResource);

        return true;
    }
}
