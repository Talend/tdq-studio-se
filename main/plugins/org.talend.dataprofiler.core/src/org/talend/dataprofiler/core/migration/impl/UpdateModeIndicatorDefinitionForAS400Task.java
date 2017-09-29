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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.talend.commons.emf.EMFUtil;
import org.talend.commons.utils.WorkspaceUtils;
import org.talend.core.model.metadata.builder.database.dburl.SupportDBUrlType;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.PropertiesPackage;
import org.talend.core.model.properties.Property;
import org.talend.cwm.relational.TdExpression;
import org.talend.dataprofiler.core.migration.AbstractWorksapceUpdateTask;
import org.talend.dataprofiler.core.migration.helper.IndicatorDefinitionFileHelper;
import org.talend.dataquality.helpers.MetadataHelper;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dataquality.properties.TDQIndicatorDefinitionItem;
import org.talend.dq.helper.PropertyHelper;
import org.talend.dq.indicators.definitions.DefinitionHandler;
import org.talend.model.bridge.ReponsitoryContextBridge;

import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * Add AS/400 support for ModeIndicatorDefinition.
 */
public class UpdateModeIndicatorDefinitionForAS400Task extends AbstractWorksapceUpdateTask {

    private static final String MODE_INDICATOR_LABEL = "Mode"; //$NON-NLS-1$

    private static final String BODY_MODE_AS400 = "SELECT  <%=__COLUMN_NAMES__%>, COUNT(*) c FROM <%=__TABLE_NAME__%> m <%=__WHERE_CLAUSE__%> GROUP BY <%=__COLUMN_NAMES__%> ORDER BY c DESC"; //$NON-NLS-1$

    public Date getOrder() {
        return createDate(2011, 12, 20);
    }

    public MigrationTaskType getMigrationTaskType() {
        return MigrationTaskType.FILE;
    }

    @Override
    protected boolean doExecute() throws Exception {
        boolean result = true;
        try {
            IndicatorDefinition indicatorDefinition = DefinitionHandler.getInstance()
                    .getIndicatorDefinition(MODE_INDICATOR_LABEL);
            URI uriItem = indicatorDefinition.eResource().getURI();
            File fileItem = null;
            if (uriItem.isPlatform()) {
                fileItem = WorkspaceUtils.ifileToFile(getIFile(indicatorDefinition));
            } else {
                fileItem = new File(uriItem.toFileString());
            }

            File fileProp = WorkspaceUtils.ifileToFile(PropertyHelper.getPropertyFile(indicatorDefinition));
            Property property = PropertyHelper.getProperty(indicatorDefinition);
            Item item = property.getItem();

            if (indicatorDefinition != null) {
                EList<TdExpression> sqlGenericExpression = indicatorDefinition.getSqlGenericExpression();
                boolean addAS400Expression = true;

                for (TdExpression expression : sqlGenericExpression) {
                    if (SupportDBUrlType.AS400DEFAULTURL.getLanguage().equals(expression.getLanguage())) {
                        addAS400Expression = false;
                        break;
                    }
                }

                if (addAS400Expression) {
                    TDQIndicatorDefinitionItem indDefItem = (TDQIndicatorDefinitionItem) item;
                    Resource itemResource = getResource(fileItem);
                    IndicatorDefinition indDef = null;

                    for (EObject object : itemResource.getContents()) {
                        if (object instanceof IndicatorDefinition) {
                            indDef = (IndicatorDefinition) object;
                            IndicatorDefinitionFileHelper.addSqlExpression(indDef,
                                    SupportDBUrlType.AS400DEFAULTURL.getLanguage(), BODY_MODE_AS400);
                            String relationPropPath = ReponsitoryContextBridge.getRootProject().getFullPath()
                                    .append(new Path(fileProp.getPath()).makeRelativeTo(this.getWorkspacePath())).toOSString();
                            MetadataHelper.setPropertyPath(relationPropPath, indDef);
                            indDefItem.setIndicatorDefinition(indDef);
                        }
                    }
                    EMFUtil.saveResource(itemResource);

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
                }
            }
        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        }
        return result;
    }

    private IFile getIFile(ModelElement modelElement) {
        IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
        String platformString = modelElement.eResource().getURI().toPlatformString(true);
        return root.getFile(new Path(platformString));
    }
}
