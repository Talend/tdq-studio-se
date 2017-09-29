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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.talend.cwm.helper.TaggedValueHelper;
import org.talend.dataprofiler.core.migration.AbstractWorksapceUpdateTask;
import org.talend.dataquality.domain.pattern.ExpressionType;
import org.talend.dataquality.domain.pattern.Pattern;
import org.talend.dataquality.domain.pattern.PatternComponent;
import org.talend.dataquality.domain.pattern.RegularExpression;
import org.talend.dataquality.helpers.BooleanExpressionHelper;
import org.talend.dataquality.helpers.MetadataHelper;
import org.talend.dq.analysis.parameters.PatternParameter;
import org.talend.dq.helper.resourcehelper.PatternResourceFileHelper;
import org.talend.dq.pattern.PatternBuilder;
import org.talend.dq.writer.impl.ElementWriterFactory;
import org.talend.resource.ResourceManager;
import org.talend.utils.sugars.ReturnCode;

public class AddPatternISBN13Task extends AbstractWorksapceUpdateTask {

    private PatternParameter parameter = null;

    private final String REGEX_BODY = "'^((([0-9A-Fa-f]{1,4}:){7}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){6}:[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){5}:([0-9A-Fa-f]{1,4}:)?[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){4}:([0-9A-Fa-f]{1,4}:){0,2}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){3}:([0-9A-Fa-f]{1,4}:){0,3}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){2}:([0-9A-Fa-f]{1,4}:){0,4}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){6}((\\b((25[0-5])|(1\\d{2})|(2[0-4]\\d)|(\\d{1,2}))\\b)\\.){3}(\\b((25[0-5])|(1\\d{2})|(2[0-4]\\d)|(\\d{1,2}))\\b))|(([0-9A-Fa-f]{1,4}:){0,5}:((\\b((25[0-5])|(1\\d{2})|(2[0-4]\\d)|(\\d{1,2}))\\b)\\.){3}(\\b((25[0-5])|(1\\d{2})|(2[0-4]\\d)|(\\d{1,2}))\\b))|(::([0-9A-Fa-f]{1,4}:){0,5}((\\b((25[0-5])|(1\\d{2})|(2[0-4]\\d)|(\\d{1,2}))\\b)\\.){3}(\\b((25[0-5])|(1\\d{2})|(2[0-4]\\d)|(\\d{1,2}))\\b))|([0-9A-Fa-f]{1,4}::([0-9A-Fa-f]{1,4}:){0,5}[0-9A-Fa-f]{1,4})|(::([0-9A-Fa-f]{1,4}:){0,6}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){1,7}:))$'"; //$NON-NLS-1$

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.dataprofiler.migration.IMigrationTask#getOrder()
     */
    public Date getOrder() {
        return createDate(2015, 9, 30);
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
        parameter = new PatternParameter();
        ReturnCode rc1 = new ReturnCode(true), rc2 = new ReturnCode(true), rc3 = new ReturnCode(true);

        // number folder

        IFolder folder = ResourceManager.getPatternRegexFolder().getFolder("number"); //$NON-NLS-1$
        if (folder.exists()) {
            // only create ISBN 13 Checker when not found.
            if (!folder.getFile("ISBN_13_Checker_0.1.pattern").exists()) {
                Pattern pattern = newPattern("ISBN 13 Checker", "Java", //$NON-NLS-1$ //$NON-NLS-2$
                        "'^ISBN(?:-13)?:?\\ *(97(?:8|9)([ -]?)(?=[0-9]{1,5}\\2?[0-9]{1,7}\\2?[0-9]{1,6}\\2?[0-9])(?:[0-9]\\2*){9}[0-9])$'"); //$NON-NLS-1$
                if (pattern != null) {
                    setTagValue(pattern, "ISBN-13: 978-2711791415 | ISBN 978-2711791415 |  ISBN: 978-2711791415", //$NON-NLS-1$
                            "International Standard Book Number 13 digits"); //$NON-NLS-1$
                    rc1 = ElementWriterFactory.getInstance().createPatternWriter().create(pattern, folder);
                }
            }
        }
        // Update IPv6 Address.pattern.
        IFile file = ResourceManager.getPatternRegexFolder().getFolder("internet").getFile("IPv6_Address_0.1.pattern"); //$NON-NLS-1$  
        if (file.exists()) {
            Pattern ipv6Pattern = PatternResourceFileHelper.getInstance().findPattern(file);
            if (ipv6Pattern != null) {
                List<PatternComponent> componentLs = new ArrayList<PatternComponent>();
                componentLs.add(BooleanExpressionHelper.createRegularExpression("Java", REGEX_BODY, ExpressionType.REGEXP)); //$NON-NLS-1$
                componentLs.add(BooleanExpressionHelper.createRegularExpression("MySQL", REGEX_BODY, ExpressionType.REGEXP)); //$NON-NLS-1$
                componentLs.add(BooleanExpressionHelper.createRegularExpression("PostgreSQL", REGEX_BODY, ExpressionType.REGEXP)); //$NON-NLS-1$
                ipv6Pattern.getComponents().clear();
                ipv6Pattern.getComponents().addAll(componentLs);

                rc2 = PatternResourceFileHelper.getInstance().save(ipv6Pattern);
            }
        }

        // Update EN Month.pattern. for expresstion add ()
        IFile file2 = ResourceManager.getPatternRegexFolder().getFolder("date").getFile("EN_Month_0.1.pattern"); //$NON-NLS-1$ //$NON-NLS-2$
        if (file2.exists()) {
            Pattern enMonthPattern = PatternResourceFileHelper.getInstance().findPattern(file2);
            if (enMonthPattern != null) {
                List<PatternComponent> componentLs = new ArrayList<PatternComponent>();
                String regexBody = "'^(January|June|July|February|March|May|April|August|September|October|November|December)$'"; //$NON-NLS-1$
                componentLs.add(BooleanExpressionHelper.createRegularExpression("SQL", regexBody, ExpressionType.REGEXP)); //$NON-NLS-1$
                enMonthPattern.getComponents().clear();
                enMonthPattern.getComponents().addAll(componentLs);

                rc3 = PatternResourceFileHelper.getInstance().save(enMonthPattern);

            }
        }
        return rc1.isOk() && rc2.isOk() && rc3.isOk();
    }

    private Pattern newPattern(String name, String lang, String express) {
        PatternBuilder patternBuilder = new PatternBuilder();
        boolean patternInitialized = patternBuilder.initializePattern(name);
        if (patternInitialized) {
            Pattern pattern = patternBuilder.getPattern();
            RegularExpression regularExpr = BooleanExpressionHelper.createRegularExpression(lang, express);
            regularExpr.setExpressionType("REGEXP"); //$NON-NLS-1$
            pattern.getComponents().add(regularExpr);
            return pattern;
        }
        return null;
    }

    private void setTagValue(Pattern pattern, String purpose, String decription) {
        TaggedValueHelper.setTaggedValue(pattern, TaggedValueHelper.DESCRIPTION, decription);
        TaggedValueHelper.setTaggedValue(pattern, TaggedValueHelper.PURPOSE, purpose);
        TaggedValueHelper.setTaggedValue(pattern, TaggedValueHelper.VALID_STATUS, String.valueOf(true));
        if (parameter == null) {
            parameter = new PatternParameter();
        }
        MetadataHelper.setDevStatus(pattern, parameter.getStatus());
        MetadataHelper.setAuthor(pattern, parameter.getAuthor());
        MetadataHelper.setVersion(parameter.getVersion(), pattern);
    }

}
