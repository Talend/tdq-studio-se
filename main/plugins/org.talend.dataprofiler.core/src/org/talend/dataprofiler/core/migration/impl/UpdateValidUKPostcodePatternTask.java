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
package org.talend.dataprofiler.core.migration.impl;

import java.util.Date;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.emf.common.util.EList;
import org.talend.cwm.relational.TdExpression;
import org.talend.dataprofiler.core.migration.AbstractWorksapceUpdateTask;
import org.talend.dataprofiler.core.pattern.PatternLanguageType;
import org.talend.dataquality.domain.pattern.Pattern;
import org.talend.dataquality.domain.pattern.PatternComponent;
import org.talend.dataquality.domain.pattern.RegularExpression;
import org.talend.dq.helper.resourcehelper.PatternResourceFileHelper;
import org.talend.dq.writer.impl.ElementWriterFactory;
import org.talend.resource.ResourceManager;
import org.talend.utils.sugars.ReturnCode;

public class UpdateValidUKPostcodePatternTask extends AbstractWorksapceUpdateTask {

    private final String NEW_REGEX_BODY =
            "'^(([BEGLMNSWbeglmnsw][0-9][0-9]?)|(([A-PR-UWYZa-pr-uwyz][A-HK-Ya-hk-y][0-9][0-9]?)|(([ENWenw][0-9][A-HJKSTUWa-hjkstuw])|([ENWenw][A-HK-Ya-hk-y][0-9][ABEHMNPRVWXYabehmnprvwxy])))) ?[0-9][ABD-HJLNP-UW-Zabd-hjlnp-uw-z]{2}$'"; //$NON-NLS-1$

    private final String OLD_REGEX_BODY =
            "'^ (([BEGLMNSWbeglmnsw][0-9][0-9]?)|(([A-PR-UWYZa-pr-uwyz][A-HK-Ya-hk-y][0-9][0-9]?)|(([ENWenw][0-9][A-HJKSTUWa-hjkstuw])|([ENWenw][A-HK-Ya-hk-y][0-9][ABEHMNPRVWXYabehmnprvwxy])))) ?[0-9][ABD-HJLNP-UW-Zabd-hjlnp-uw-z]{2}$'"; //$NON-NLS-1$

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.migration.IMigrationTask#getOrder()
     */
    public Date getOrder() {
        return createDate(2018, 7, 9);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.migration.IMigrationTask#getMigrationTaskType()
     */
    public MigrationTaskType getMigrationTaskType() {
        return MigrationTaskType.FILE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.migration.AMigrationTask#doExecute()
     */
    @Override
    protected boolean doExecute() throws Exception {
        ReturnCode rc1 = new ReturnCode(true);

        // address folder
        IFolder folder = ResourceManager.getPatternRegexFolder().getFolder("address"); //$NON-NLS-1$
        if (folder.exists()) {
            IFile patternFile = folder.getFile("Valid_UK_Post_Codes_Upper_and_Lower_Case_0.1.pattern"); //$NON-NLS-1$
            // only do it when the pattern has existed.
            if (patternFile.exists()) {
                Pattern pattern = PatternResourceFileHelper.getInstance().findPattern(patternFile);
                if (pattern != null) {
                    EList<PatternComponent> components = pattern.getComponents();
                    for (PatternComponent patternComponent : components) {
                        TdExpression expression = ((RegularExpression) patternComponent).getExpression();
                        String language = expression.getLanguage();
                        String body = expression.getBody();
                        if (PatternLanguageType.Default.getLiteral().equals(language)
                                && this.OLD_REGEX_BODY.equals(body)) {
                            expression.setBody(this.NEW_REGEX_BODY);
                            rc1 = ElementWriterFactory.getInstance().createPatternWriter().save(pattern);
                        }
                    }
                }
            }
        }
        return rc1.isOk();
    }
}
