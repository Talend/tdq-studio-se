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
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.talend.commons.emf.EMFUtil;
import org.talend.commons.utils.WorkspaceUtils;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.PropertiesPackage;
import org.talend.core.model.properties.Property;
import org.talend.dataprofiler.core.migration.AbstractWorksapceUpdateTask;
import org.talend.dataprofiler.core.migration.helper.IndicatorDefinitionFileHelper;
import org.talend.dataquality.helpers.MetadataHelper;
import org.talend.dataquality.properties.TDQBusinessRuleItem;
import org.talend.dataquality.rules.WhereRule;
import org.talend.dq.dqrule.WhereRuleHandler;
import org.talend.dq.helper.PropertyHelper;
import org.talend.model.bridge.ReponsitoryContextBridge;

import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * Update the demo dqrule to add the join clause.
 */
public class UpdateDemoDqruleTask extends AbstractWorksapceUpdateTask {

    private static final String DEMO_DQRULE_NAME = "Demo DQ Rule"; //$NON-NLS-1$

    private static final String SQL = "SQL"; //$NON-NLS-1$

    private static final String BODY_DEMO_DQRULE = "SELECT COUNT(*) FROM <%=__TABLE_NAME__%> <%=__JOIN_CLAUSE__%> <%=__WHERE_CLAUSE__%>"; //$NON-NLS-1$

    public Date getOrder() {
        return createDate(2011, 12, 29);
    }

    public MigrationTaskType getMigrationTaskType() {
        return MigrationTaskType.FILE;
    }

    @Override
    protected boolean doExecute() throws Exception {
        boolean result = true;
        try {
            WhereRule whereRule = WhereRuleHandler.getInstance().getWhereRule(DEMO_DQRULE_NAME);
            URI uriItem = whereRule.eResource().getURI();
            File fileItem = null;
            if (uriItem.isPlatform()) {
                fileItem = WorkspaceUtils.ifileToFile(getIFile(whereRule));
            } else {
                fileItem = new File(uriItem.toFileString());
            }

            File fileProp = WorkspaceUtils.ifileToFile(PropertyHelper.getPropertyFile(whereRule));
            Property property = PropertyHelper.getProperty(whereRule);
            Item item = property.getItem();

            if (whereRule != null) {
                TDQBusinessRuleItem dqruleItem = (TDQBusinessRuleItem) item;
                Resource itemResource = getResource(fileItem);

                for (EObject object : itemResource.getContents()) {
                    if (object instanceof WhereRule) {
                        whereRule = (WhereRule) object;
                        whereRule.getSqlGenericExpression().clear();
                        IndicatorDefinitionFileHelper.addSqlExpression(whereRule, SQL, BODY_DEMO_DQRULE);
                        String relationPropPath = ReponsitoryContextBridge.getRootProject().getFullPath()
                                .append(new Path(fileProp.getPath()).makeRelativeTo(this.getWorkspacePath())).toOSString();
                        MetadataHelper.setPropertyPath(relationPropPath, whereRule);
                        dqruleItem.setDqrule(whereRule);
                    }

                    EMFUtil.saveResource(itemResource);

                    Resource propResource = getResource(fileProp);

                    Property newProperty = (Property) EcoreUtil.getObjectByType(propResource.getContents(),
                            PropertiesPackage.eINSTANCE.getProperty());

                    newProperty.setAuthor(property.getAuthor());
                    newProperty.setLabel(dqruleItem.getDqrule().getName());
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
