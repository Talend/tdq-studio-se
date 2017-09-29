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
package org.talend.dataprofiler.core.ui.wizard.indicator;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.jface.wizard.Wizard;
import org.talend.commons.emf.FactoriesUtil;
import org.talend.cwm.helper.TaggedValueHelper;
import org.talend.dataprofiler.core.i18n.internal.DefaultMessagesImpl;
import org.talend.dataprofiler.core.pattern.ExportFactory;
import org.talend.dataprofiler.core.ui.utils.UDIUtils;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import org.talend.dq.helper.resourcehelper.IndicatorResourceFileHelper;
import org.talend.dq.nodes.SysIndicatorDefinitionRepNode;
import org.talend.utils.io.FilesUtils;
import orgomg.cwm.objectmodel.core.TaggedValue;

/**
 * DOC xqliu class global comment. Detailled comment
 */
public class ExportUDIWizard extends Wizard {

    private static Logger log = Logger.getLogger(ExportUDIWizard.class);

    private IFolder folder;

    private ExportUDIWizardPage page;

    private boolean isForExchange;

    public ExportUDIWizard(IFolder folder, boolean isForExchange) {
        this.folder = folder;
        this.isForExchange = isForExchange;
    }

    @Override
    public boolean performFinish() {

        String targetFile = page.getTargetFile();
        Object[] elements = page.getSelectedTree().getCheckedElements();

        List<IndicatorDefinition> seletedIndicators = new ArrayList<IndicatorDefinition>();
        for (Object element : elements) {
            if (element instanceof IFile) {
                IFile file = (IFile) element;
                if (FactoriesUtil.DEFINITION.equalsIgnoreCase(file.getFileExtension())) {
                    seletedIndicators.add(IndicatorResourceFileHelper.getInstance().findIndDefinition(file));
                }
            } else if (element instanceof SysIndicatorDefinitionRepNode
                    && !((SysIndicatorDefinitionRepNode) element).isSystemIndicator()) {
                SysIndicatorDefinitionRepNode udiElement = (SysIndicatorDefinitionRepNode) element;
                seletedIndicators.add(udiElement.getIndicatorDefinition());
            }
        }

        if ("".equals(targetFile)) { //$NON-NLS-1$
            MessageDialog
                    .openError(
                            getShell(),
                            DefaultMessagesImpl.getString("ExportUDIWizard.Error"), DefaultMessagesImpl.getString("ExportUDIWizard.SpecifyValidResource")); //$NON-NLS-1$ //$NON-NLS-2$
            return false;
        } else {
            File resource = new File(targetFile);

            if (isForExchange) {
                ExportFactory.export(resource, folder,
                        seletedIndicators.toArray(new IndicatorDefinition[seletedIndicators.size()]));

                for (Iterator<IndicatorDefinition> iterator = seletedIndicators.iterator(); iterator.hasNext();) {
                    IndicatorDefinition id = iterator.next();
                    File idFile = new File(resource, id.getName() + ".csv"); //$NON-NLS-1$
                    if (idFile.isFile() && idFile.exists()) {
                        try {
                            List<File> udiAndJarfiles = new ArrayList<File>();
                            udiAndJarfiles.add(idFile);
                            // MOD by zshen for bug 18724 2011.03.01
                            TaggedValue tv = TaggedValueHelper.getTaggedValue(TaggedValueHelper.JAR_FILE_PATH,
                                    id.getTaggedValue());
                            if (tv != null) {
                                for (IFile udiJarFile : UDIUtils.getLibJarFileList()) {
                                    for (String jarName : tv.getValue().split("\\|\\|")) { //$NON-NLS-1$
                                        if (udiJarFile.getName().equals(jarName)) {
                                            udiAndJarfiles.add(udiJarFile.getLocation().toFile());
                                            break;
                                        }
                                    }
                                }
                            }
                            // ~

                            FilesUtils.zips(udiAndJarfiles.toArray(new File[udiAndJarfiles.size()]), idFile.getPath() + ".zip"); //$NON-NLS-1$
                            idFile.delete();

                        } catch (Exception e) {
                            log.error(e.getMessage(), e);
                        }
                    }
                }

                return true;
            } else {

                boolean isContinue = true;
                if (resource.exists()) {
                    isContinue = MessageDialogWithToggle.openConfirm(null,
                            DefaultMessagesImpl.getString("ExportPatternsWizard.waring"), //$NON-NLS-1$
                            DefaultMessagesImpl.getString("ExportPatternsWizard.fileAlreadyExist")); //$NON-NLS-1$
                }

                if (isContinue) {
                    ExportFactory.export(resource, folder,
                            seletedIndicators.toArray(new IndicatorDefinition[seletedIndicators.size()]));
                    return true;
                }

                return false;
            }
        }
    }

    @Override
    public void addPages() {
        page = new ExportUDIWizardPage(folder, isForExchange);
        addPage(page);
    }
}
