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
package org.talend.dataprofiler.core.ui.wizard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.resources.IFolder;
import org.eclipse.jface.wizard.Wizard;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.Item;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.dataprofiler.core.CorePlugin;
import org.talend.dataprofiler.core.ui.utils.MessageUI;
import org.talend.dataprofiler.core.ui.utils.UIMessages;
import org.talend.dataquality.helpers.MetadataHelper;
import org.talend.dataquality.properties.TDQMatchRuleItem;
import org.talend.dq.analysis.parameters.ConnectionParameter;
import org.talend.dq.helper.RepositoryNodeHelper;
import org.talend.dq.helper.resourcehelper.AnaResourceFileHelper;
import org.talend.dq.helper.resourcehelper.DQRuleResourceFileHelper;
import org.talend.dq.helper.resourcehelper.IndicatorResourceFileHelper;
import org.talend.dq.helper.resourcehelper.PatternResourceFileHelper;
import org.talend.dq.helper.resourcehelper.RepResourceFileHelper;
import org.talend.dq.helper.resourcehelper.ResourceFileMap;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;
import org.talend.utils.sugars.ReturnCode;
import org.talend.utils.sugars.TypedReturnCode;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * DOC zqin class global comment. Detailled comment
 */
public abstract class AbstractWizard extends Wizard implements ICWMResouceAdapter, IConnectionParameter {

    protected ModelElement modelElement = null;

    protected boolean isDoingPerformFinish = false;

    @Override
    public boolean performFinish() {
        // TDQ-12153: add a flag to avoid this method run twice when the user quickly click the finish button on the
        // wizard. TODO: consider other better solution.
        if (isDoingPerformFinish) {
            return true;
        }
        isDoingPerformFinish = true;

        // MOD mzhao feature 15750 Use repository object represent ModelElement.
        modelElement = initCWMResourceBuilder();
        if (modelElement != null) {
            fillMetadataToCWMResource(modelElement);
            // Save the repository objects.
            TypedReturnCode<Object> csResult = createAndSaveCWMFile(modelElement);
            if (csResult.isOk()) {
                Object savedObj = csResult.getObject();
                if (savedObj instanceof Item) {
                    if (!RepositoryNodeHelper.isOpenDQCommonViewer() && savedObj instanceof TDQMatchRuleItem) {
                        openEditor((TDQMatchRuleItem) savedObj);
                    } else {
                        RepositoryNode recursiveFind = RepositoryNodeHelper.recursiveFind(((Item) savedObj).getProperty());
                        openEditor(recursiveFind);
                    }
                }

                CorePlugin.getDefault().refresh(modelElement);
                isDoingPerformFinish = false;
                return true;
            } else {
                MessageUI.openError(csResult.getMessage());
            }
        }

        isDoingPerformFinish = false;
        return false;
    }

    public abstract void openEditor(IRepositoryNode repNode);

    public void openEditor(TDQMatchRuleItem item) {

    }

    public ReturnCode checkMetadata() {
        String elementName = getParameter().getName();
        IFolder folderResource = getParameter().getFolderProvider().getFolderResource();
        if (elementName == null || folderResource == null) {
            return new ReturnCode("", false); //$NON-NLS-1$
        } else {
            Collection<ModelElement> modelElements = new ArrayList<ModelElement>();

            switch (getParameter().getParamType()) {
            case ANALYSIS:
                modelElements.addAll(AnaResourceFileHelper.getInstance().getAllElement(folderResource));
                break;
            case REPORT:
                modelElements.addAll(RepResourceFileHelper.getInstance().getAllElement(folderResource));
                break;
            case PATTERN:
                modelElements.addAll(PatternResourceFileHelper.getInstance().getAllElement(folderResource));
                break;
            case CONNECTION:
                List<Connection> conns = new ArrayList<Connection>();
                try {
                    for (ConnectionItem connItem : ProxyRepositoryFactory.getInstance().getMetadataConnectionsItem()) {
                        conns.add(connItem.getConnection());
                    }
                } catch (PersistenceException e) {
                    ExceptionHandler.process(e);
                }
                modelElements.addAll(conns);
                break;
            case DQRULE:
                modelElements.addAll(DQRuleResourceFileHelper.getInstance().getAllElement(folderResource));
                break;
            case UDINDICATOR:
                modelElements.addAll(IndicatorResourceFileHelper.getInstance().getAllElement(folderResource));
                break;
            default:
                break;
            }

            if (!modelElements.isEmpty()) {
                for (ModelElement element : modelElements) {
                    String theElementName = element.getName();
                    if (theElementName == null) {
                        if (element instanceof Connection) {
                            // MOD klliu 2011-04-18 get repository Object label
                            // theElementName =RepositoryNodeHelper.recursiveFind(element).getObject().getLabel();
                        }
                    }

                    if (elementName.equals(theElementName)) {
                        // MOD xqliu 2010-09-21 bug 15762
                        return new ReturnCode(UIMessages.MSG_ANALYSIS_SAME_NAME, false);
                        // if (!MessageUI.openConfirm(UIMessages.MSG_ANALYSIS_SAME_NAME)) {
                        //     getParameter().setName(elementName + DateFormatUtils.format(new Date(), "yyyyMMddHHmmss")); //$NON-NLS-1$
                        // }
                        // break;
                        // ~ 15762
                    }
                }
            }
        }
        return new ReturnCode(true);
    }

    public void fillMetadataToCWMResource(ModelElement cwmElement) {
        MetadataHelper.setDevStatus(cwmElement, getParameter().getStatus());
        MetadataHelper.setAuthor(cwmElement, getParameter().getAuthor());
        MetadataHelper.setPurpose(getParameter().getPurpose(), cwmElement);
        MetadataHelper.setDescription(getParameter().getDescription(), cwmElement);
        MetadataHelper.setVersion(getParameter().getVersion(), cwmElement);
    }

    protected abstract ResourceFileMap getResourceFileMap();

    public abstract ConnectionParameter getParameter();

    protected abstract String getEditorName();

}
