// ============================================================================
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
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
import org.talend.cwm.helper.ColumnSetHelper;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.ui.wizard.AbstractWizard;
import org.talend.dq.analysis.parameters.ConnectionParameter;
import org.talend.dq.helper.resourcehelper.ResourceFileMap;
import org.talend.repository.ProjectManager;
import org.talend.repository.model.IRepositoryNode;
import org.talend.utils.sugars.TypedReturnCode;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.objectmodel.core.Package;

/**
 * DOC xqliu class global comment. Detailled comment
 */
public class TableViewFilterWizard extends AbstractWizard {

    protected static Logger log = Logger.getLogger(TableViewFilterWizard.class);

    private TableViewFilterWizardPage tableViewFilterWizardPage;

    private Package packageObj;

    private Connection tdDataProvider;

    private String oldTableFilter, oldViewFilter;

    public String getOldTableFilter() {
        return oldTableFilter;
    }

    public void setOldTableFilter(String oldTableFilter) {
        this.oldTableFilter = oldTableFilter;
    }

    public String getOldViewFilter() {
        return oldViewFilter;
    }

    public void setOldViewFilter(String oldViewFilter) {
        this.oldViewFilter = oldViewFilter;
    }

    public Package getPackageObj() {
        return packageObj;
    }

    public void setPackageObj(Package packageObj) {
        this.packageObj = packageObj;
    }

    public Connection getTdDataProvider() {
        return tdDataProvider;
    }

    public void setTdDataProvider(Connection tdDataProvider) {
        this.tdDataProvider = tdDataProvider;
    }

    public TableViewFilterWizard() {
        super();
    }

    public TableViewFilterWizard(Package packageObj) {
        super();
        initAction(packageObj);
    }

    @Override
    protected String getEditorName() {
        return DefaultMessagesImpl.getString("TableViewFilterWizard.Table/ViewFilter"); //$NON-NLS-1$
    }

    @Override
    public boolean performFinish() {
        boolean needSave = false;

        String tableFilter = this.tableViewFilterWizardPage.getTableFilterText().getText();
        if (!this.getOldTableFilter().equals(tableFilter)) {
            needSave = true;
        }

        String viewFilter = this.tableViewFilterWizardPage.getViewFilterText().getText();
        if (!this.getOldViewFilter().equals(viewFilter)) {
            needSave = true;
        }

        if (needSave) {

            Property property = org.talend.dq.helper.PropertyHelper.getProperty(tdDataProvider);
            DatabaseConnectionItem connectionItem = (DatabaseConnectionItem) property.getItem();
            ColumnSetHelper.setTableFilter(tableFilter, packageObj);
            ColumnSetHelper.setViewFilter(viewFilter, packageObj);
            Project currentProject = ProjectManager.getInstance().getCurrentProject();
            try {
                ProxyRepositoryFactory.getInstance().save(currentProject, connectionItem);
            } catch (PersistenceException e) {
                return false;
            }
            // return ElementWriterFactory.getInstance().createDataProviderWriter().save(tdDataProvider).isOk();
        }
        return true;
    }

    @Override
    public ConnectionParameter getParameter() {
        return null;
    }

    @Override
    protected ResourceFileMap getResourceFileMap() {
        return null;
    }

    @Override
    public void addPages() {
        setWindowTitle(DefaultMessagesImpl.getString("TableViewFilterWizard.tableViewFilter")); //$NON-NLS-1$
        tableViewFilterWizardPage = new TableViewFilterWizardPage(this);
        tableViewFilterWizardPage.setTitle(DefaultMessagesImpl.getString("TableViewFilterWizard.tableViewFilter")); //$NON-NLS-1$
        tableViewFilterWizardPage.setDescription(DefaultMessagesImpl.getString("TableViewFilterWizard.tableViewFilterDesc")); //$NON-NLS-1$

        addPage(tableViewFilterWizardPage);
    }

    public TypedReturnCode<Object> createAndSaveCWMFile(ModelElement cwmElement) {
        return null;
    }

    public ModelElement initCWMResourceBuilder() {
        return this.tdDataProvider;
    }

    private void initAction(Package packageObj) {
        this.packageObj = packageObj;
        this.tdDataProvider = ConnectionHelper.getTdDataProvider(this.packageObj);
        this.oldTableFilter = ColumnSetHelper.getTableFilter(packageObj);
        this.oldViewFilter = ColumnSetHelper.getViewFilter(packageObj);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.core.ui.wizard.AbstractWizard#openEditor(org.talend.repository.model.IRepositoryNode)
     */
    @Override
    public void openEditor(IRepositoryNode repNode) {
        // do nothing until now
    }
}
