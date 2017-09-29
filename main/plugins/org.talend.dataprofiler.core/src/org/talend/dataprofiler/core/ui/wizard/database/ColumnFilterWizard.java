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
package org.talend.dataprofiler.core.ui.wizard.database;

import org.apache.log4j.Logger;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.general.Project;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.properties.DatabaseConnectionItem;
import org.talend.core.model.properties.Property;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.cwm.helper.ColumnHelper;
import org.talend.cwm.helper.ColumnSetHelper;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.dataprofiler.core.ImageLib;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.wizard.AbstractWizard;
import org.talend.dq.analysis.parameters.ConnectionParameter;
import org.talend.dq.helper.resourcehelper.ResourceFileMap;
import org.talend.repository.ProjectManager;
import org.talend.repository.model.IRepositoryNode;
import org.talend.utils.sugars.TypedReturnCode;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.objectmodel.core.Package;
import orgomg.cwm.resource.relational.NamedColumnSet;

/**
 * created by qiongli on 2013-12-25 Detailled comment
 * 
 */
public class ColumnFilterWizard extends AbstractWizard {

    private Logger log = Logger.getLogger(ColumnFilterWizard.class);

    private NamedColumnSet namedColumnSet;

    private Package packageObj;

    private Connection tdDataProvider;

    private String oldColumnFilter;

    private ColumnFilterWizardPage columnFilterWizardPage = null;

    public ColumnFilterWizard(NamedColumnSet namedColumnSet) {
        super();
        initAction(namedColumnSet);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.wizard.ICWMResouceAdapter#initCWMResourceBuilder()
     */
    public ModelElement initCWMResourceBuilder() {
        return this.tdDataProvider;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.Wizard#addPages()
     */
    @Override
    public void addPages() {
        initColumnFilterDescription();
        addPage(columnFilterWizardPage);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.wizard.AbstractWizard#performFinish()
     */
    @Override
    public boolean performFinish() {
        String columnFilter = this.columnFilterWizardPage.getColumnFilterText().getText();
        if (!this.getOldColumnFilter().equals(columnFilter)) {
            Property property = org.talend.dq.helper.PropertyHelper.getProperty(tdDataProvider);
            DatabaseConnectionItem connectionItem = (DatabaseConnectionItem) property.getItem();
            ColumnHelper.setColumnFilter(columnFilter, namedColumnSet);
            Project currentProject = ProjectManager.getInstance().getCurrentProject();
            try {
                ProxyRepositoryFactory.getInstance().save(currentProject, connectionItem);
            } catch (PersistenceException e) {
                log.error(e);
                return false;
            }
        }
        return true;

    }

    private void initColumnFilterDescription() {
        setWindowTitle(DefaultMessagesImpl.getString("MetaDataFilterWizard.columnFilter")); //$NON-NLS-1$
        setDefaultPageImageDescriptor(ImageLib.getImageDescriptor(ImageLib.RUN_IMAGE));
        this.columnFilterWizardPage = new ColumnFilterWizardPage(this);
        this.columnFilterWizardPage.setTitle(DefaultMessagesImpl.getString("MetaDataFilterWizard.columnFilter")); //$NON-NLS-1$
        this.columnFilterWizardPage.setDescription(DefaultMessagesImpl.getString("MetaDataFilterWizard.columnFilterDesc")); //$NON-NLS-1$
    }

    private void initAction(NamedColumnSet namedCS) {
        this.namedColumnSet = namedCS;
        this.packageObj = ColumnSetHelper.getParentCatalogOrSchema(this.namedColumnSet);
        this.tdDataProvider = ConnectionHelper.getTdDataProvider(this.packageObj);
        this.oldColumnFilter = ColumnHelper.getColumnFilter(namedColumnSet);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.wizard.ICWMResouceAdapter#createAndSaveCWMFile(orgomg.cwm.objectmodel.core.
     * ModelElement)
     */
    public TypedReturnCode<Object> createAndSaveCWMFile(ModelElement repositoryObject) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.dataprofiler.core.ui.wizard.analysis.AbstractAnalysisWizard#openEditor(org.talend.repository.model.IRepositoryNode
     * )
     */
    @Override
    public void openEditor(IRepositoryNode repNode) {
        // no need to implement anything now
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.wizard.AbstractWizard#getResourceFileMap()
     */
    @Override
    protected ResourceFileMap getResourceFileMap() {
        // no need to implement anything now
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.wizard.AbstractWizard#getParameter()
     */
    @Override
    public ConnectionParameter getParameter() {
        // no need to implement anything now
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.wizard.AbstractWizard#getEditorName()
     */
    @Override
    protected String getEditorName() {
        return DefaultMessagesImpl.getString("MetaDataFilterWizard.ColumnFilter"); //$NON-NLS-1$
    }

    public NamedColumnSet getNamedColumnSet() {
        return namedColumnSet;
    }

    public Connection getTdDataProvider() {
        return tdDataProvider;
    }

    public String getOldColumnFilter() {
        return oldColumnFilter;
    }

    public Package getPackageObj() {
        return this.packageObj;
    }

}
