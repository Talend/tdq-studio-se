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
package org.talend.dataprofiler.common.ui.dialog;

import java.io.File;
import java.net.URL;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;
import org.talend.dataprofiler.common.ui.i18n.Messages;
import org.talend.dataquality.record.linkage.attribute.IAttributeMatcher;
import org.talend.dq.helper.CustomAttributeMatcherHelper;
import org.talend.utils.sugars.ReturnCode;

/**
 * created by zshen on Nov 11, 2013 Detailled comment
 * 
 */
public class MatchCustomJarSelectDialog extends AbstractJarSelectDialog<IAttributeMatcher> {

    private final String CURRENTPLUGINID = "org.talend.dataprofiler.common.ui"; //$NON-NLS-1$

    /**
     * DOC zshen MatchCustomJarSelectDialog constructor comment.
     * 
     * @param definition
     * @param parent
     * @param labelProvider
     * @param contentProvider
     */
    public MatchCustomJarSelectDialog(Shell parent, ILabelProvider labelProvider, ITreeContentProvider contentProvider) {
        super(parent, labelProvider, contentProvider);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.common.ui.dialog.AbstractJarSelectDialog#checkJarDependency(java.io.File)
     */
    @Override
    public ReturnCode checkJarDependency(File delFile) {
        return new ReturnCode(true);
    }

    @Override
    public String getSelectResult() {
        String selectResult = StringUtils.EMPTY;
        for (URL jarUrl : listURL) {
            if (jarUrl != null) {
                selectResult += new File(jarUrl.getFile()).getName() + CustomAttributeMatcherHelper.SEPARATOR;
            }
        }
        return selectResult += selectClassName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.common.ui.dialog.AbstractJarSelectDialog#initfValidator()
     */
    @Override
    protected ISelectionStatusValidator initfValidator() {

        return new ISelectionStatusValidator() {

            @Override
            public IStatus validate(Object[] selection) {
                if (jarList.getSelectionCount() <= 0) {
                    return new Status(IStatus.ERROR, CURRENTPLUGINID,
                            Messages.getString("MatchCustomJarSelectDialog.atleastSelectedOneJar")); //$NON-NLS-1$
                }
                return new Status(IStatus.OK, CURRENTPLUGINID, StringUtils.EMPTY);
            }

        };
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.common.ui.dialog.AbstractJarSelectDialog#getSuperClass()
     */
    @Override
    protected Class<IAttributeMatcher> getSuperClass() {

        return IAttributeMatcher.class;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.common.ui.dialog.AbstractJarSelectDialog#spliteJarFile()
     */
    @Override
    protected String[] spliteJarFile() {

        return CustomAttributeMatcherHelper.splitJarPath(getCheckValue());
    }

}
