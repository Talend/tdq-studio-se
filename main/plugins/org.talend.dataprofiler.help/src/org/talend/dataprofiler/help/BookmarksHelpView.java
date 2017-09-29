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
package org.talend.dataprofiler.help;

import org.eclipse.help.ui.internal.IHelpUIConstants;
import org.eclipse.help.ui.internal.views.HelpView;

@SuppressWarnings("restriction")
public class BookmarksHelpView extends HelpView {

    public static final String HELP_VIEW_ID = "org.talend.dataprofiler.help.BookmarksHelpView"; //$NON-NLS-1$

    @Override
    protected String getFirstPage() {
        return IHelpUIConstants.HV_BOOKMARKS_PAGE;
    }

    public BookmarksHelpView() {

    }

}
