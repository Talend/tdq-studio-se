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

import java.util.Date;

import org.eclipse.core.resources.IFolder;
import org.talend.cwm.helper.TaggedValueHelper;
import org.talend.dataprofiler.core.migration.AbstractWorksapceUpdateTask;
import org.talend.dataquality.domain.pattern.Pattern;
import org.talend.dataquality.domain.pattern.RegularExpression;
import org.talend.dataquality.helpers.BooleanExpressionHelper;
import org.talend.dataquality.helpers.MetadataHelper;
import org.talend.dq.analysis.parameters.PatternParameter;
import org.talend.dq.pattern.PatternBuilder;
import org.talend.dq.writer.impl.ElementWriterFactory;
import org.talend.resource.ResourceManager;

public class CreatePatternsMigratorTask extends AbstractWorksapceUpdateTask {

    private final String SQLLanguage = "SQL"; //$NON-NLS-1$

    private final String PATH_ADDRESS = "address"; //$NON-NLS-1$

    private final String PATH_CUSTOMER = "customer"; //$NON-NLS-1$

    private final String PATH_NUMBER = "number"; //$NON-NLS-1$

    private final String PATH_MONEY = "currency"; //$NON-NLS-1$

    private final String PATH_DATE = "date"; //$NON-NLS-1$

    private final String PATH_INTERNET = "internet"; //$NON-NLS-1$

    private PatternParameter parameter = null;

    public Date getOrder() {
        return createDate(2015, 8, 13);
    }

    public MigrationTaskType getMigrationTaskType() {
        return MigrationTaskType.FILE;
    }

    @Override
    protected boolean doExecute() throws Exception {
        parameter = new PatternParameter();
        // FR Insee Code
        IFolder folder = ResourceManager.getPatternRegexFolder().getFolder(PATH_ADDRESS);
        if (folder.exists()) {
            if (!folder.getFile("FR_Insee_Code_0.1.pattern").exists()) {
                Pattern pattern = newPattern("FR Insee Code", SQLLanguage, "'^(F-|FRA?(-| ))?((2[A|B])|[0-9]{2})[0-9]{3}$'"); //$NON-NLS-1$ //$NON-NLS-2$
                if (pattern != null) {
                    setTagValue(pattern, " FRA-2A235 |   F-2B128 |  FRA 2B356", //$NON-NLS-1$
                            "French Insee code of cities with Corsica and colonies"); //$NON-NLS-1$
                    ElementWriterFactory.getInstance().createPatternWriter().create(pattern, folder);
                }
            }
        }

        // SEDOL
        folder = ResourceManager.getPatternRegexFolder().getFolder(PATH_CUSTOMER);
        if (folder.exists()) {
            if (!folder.getFile("SEDOL_0.1.pattern").exists()) {
                Pattern pattern = newPattern("SEDOL", SQLLanguage, "'^([B-Db-dF-Hf-hJ-Nj-nP-Tp-tV-Xv-xYyZz0-9]{6}[0-9])$'"); //$NON-NLS-1$ //$NON-NLS-2$
                if (pattern != null) {
                    setTagValue(pattern, "B01HL06 | 4155586", "Stock Exchange Daily Official List "); //$NON-NLS-1$ //$NON-NLS-2$
                    ElementWriterFactory.getInstance().createPatternWriter().create(pattern, folder);
                }
            }
        }

        // IPV6 MAC Address
        folder = ResourceManager.getPatternRegexFolder().getFolder(PATH_INTERNET);
        if (folder.exists()) {
            if (!folder.getFile("IPv6_Address_0.1.pattern").exists()) {
                Pattern pattern = newPattern(
                        "IPv6 Address", //$NON-NLS-1$
                        SQLLanguage,
                        "'^((([0-9A-Fa-f]{1,4}:){7}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){6}:[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){5}:([0-9A-Fa-f]{1,4}:)?[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){4}:([0-9A-Fa-f]{1,4}:){0,2}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){3}:([0-9A-Fa-f]{1,4}:){0,3}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){2}:([0-9A-Fa-f]{1,4}:){0,4}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){6}((\\b((25[0-5])|(1\\d{2})|(2[0-4]\\d)|(\\d{1,2}))\\b)\\.){3}(\\b((25[0-5])|(1\\d{2})|(2[0-4]\\d)|(\\d{1,2}))\\b))|(([0-9A-Fa-f]{1,4}:){0,5}:((\\b((25[0-5])|(1\\d{2})|(2[0-4]\\d)|(\\d{1,2}))\\b)\\.){3}(\\b((25[0-5])|(1\\d{2})|(2[0-4]\\d)|(\\d{1,2}))\\b))|(::([0-9A-Fa-f]{1,4}:){0,5}((\\b((25[0-5])|(1\\d{2})|(2[0-4]\\d)|(\\d{1,2}))\\b)\\.){3}(\\b((25[0-5])|(1\\d{2})|(2[0-4]\\d)|(\\d{1,2}))\\b))|([0-9A-Fa-f]{1,4}::([0-9A-Fa-f]{1,4}:){0,5}[0-9A-Fa-f]{1,4})|(::([0-9A-Fa-f]{1,4}:){0,6}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){1,7}:))$'"); //$NON-NLS-1$
                if (pattern != null) {
                    setTagValue(pattern, "Check if it is a IPv6 address", "IPv6 address"); //$NON-NLS-1$ //$NON-NLS-2$
                    ElementWriterFactory.getInstance().createPatternWriter().create(pattern, folder);
                }
            }
            if (!folder.getFile("MAC_Address_0.1.pattern").exists()) {
                Pattern pattern = newPattern(
                        "MAC Address", SQLLanguage, "'^([0-9a-fA-F][0-9a-fA-F]:){5}([0-9a-fA-F][0-9a-fA-F])$'"); //$NON-NLS-1$ //$NON-NLS-2$
                if (pattern != null) {
                    setTagValue(pattern, "A4:4E:31:B9:C5:B4", "Match MAC Address"); //$NON-NLS-1$ //$NON-NLS-2$
                    ElementWriterFactory.getInstance().createPatternWriter().create(pattern, folder);
                }
            }
        }

        // GPS Coordinate ,ISBN 13,UK SSN
        folder = ResourceManager.getPatternRegexFolder().getFolder(PATH_NUMBER);
        if (folder.exists()) {
            if (!folder.getFile("GPS_Coordinate_0.1.pattern").exists()) {
                Pattern pattern = newPattern("GPS Coordinate", SQLLanguage, //$NON-NLS-1$
                        "'^([0-9]{1,3}[\\.][0-9]*)[, ]+-?([0-9]{1,3}[\\.][0-9]*)$'"); //$NON-NLS-1$
                if (pattern != null) {
                    setTagValue(pattern, "40.7127837,-74.00594130000002", "Google Maps style GPS Decimal format"); //$NON-NLS-1$ //$NON-NLS-2$
                    ElementWriterFactory.getInstance().createPatternWriter().create(pattern, folder);
                }
            }

            if (!folder.getFile("UK_SSN_0.1.pattern").exists()) {
                Pattern pattern = newPattern("UK SSN", SQLLanguage, //$NON-NLS-1$
                        "'^[A-CEGHJ-PR-TW-Z]{1}[A-CEGHJ-NPR-TW-Z]{1}([0-9]{6}|( [0-9]{2}){3} )[A-DFM]{0,1}$'"); //$NON-NLS-1$
                if (pattern != null) {
                    setTagValue(pattern, "AB123456C | AB 12 34 56 C", //$NON-NLS-1$
                            "National identification number, national identity number, or national insurance number generally called an NI Number (NINO)"); //$NON-NLS-1$
                    ElementWriterFactory.getInstance().createPatternWriter().create(pattern, folder);
                }
            }

        }

        // EN Amount Money ,FR Amount Money
        folder = ResourceManager.getPatternRegexFolder().getFolder(PATH_MONEY);
        if (!folder.exists()) {
            folder.create(true, true, null);
        }
        if (folder.exists()) {
            if (!folder.getFile("EN_Amount_Money_0.1.pattern").exists()) {
                Pattern pattern = newPattern("EN Amount Money", SQLLanguage, //$NON-NLS-1$
                        "'^((US|CA)?\\$|\\￡|\\€|\\￥)(([1-9][0-9]{0,2}(\\,[0-9]{3})*)|([1-9][0-9]*)|(0))(\\.[0-9]{2}|k|M|G|T)?$'"); //$NON-NLS-1$

                if (pattern != null) {
                    RegularExpression regularExpr = BooleanExpressionHelper
                            .createRegularExpression("MySQL", //$NON-NLS-1$
                                    "'^((US|CA)?\\\\$|\\￡|\\€|\\￥)(([1-9][0-9]{0,2}(\\,[0-9]{3})*)|([1-9][0-9]*)|(0))(\\.[0-9]{2}|k|M|G|T)?$'"); //$NON-NLS-1$
                    regularExpr.setExpressionType("REGEXP"); //$NON-NLS-1$
                    pattern.getComponents().add(regularExpr);
                    setTagValue(pattern, "$3,000 || CA$3000", "Amount of money in English format"); //$NON-NLS-1$ //$NON-NLS-2$
                    ElementWriterFactory.getInstance().createPatternWriter().create(pattern, folder);
                }
            }

            if (!folder.getFile("FR_Amount_Money_0.1.pattern").exists()) {
                Pattern pattern = newPattern("FR Amount Money", SQLLanguage, //$NON-NLS-1$
                        "'^(([1-9][0-9]{0,2}( [0-9]{3})*)|([1-9][0-9]*)|0)((,[0-9]{2} | (k|M|G|T))?| )(\\$( (US|CA))?|\\￡|\\€|\\￥)$'"); //$NON-NLS-1$
                if (pattern != null) {
                    setTagValue(pattern, "3000 € | 35 k€ | 35 054 T€", "Amount of money in French format"); //$NON-NLS-1$ //$NON-NLS-2$
                    ElementWriterFactory.getInstance().createPatternWriter().create(pattern, folder);
                }
            }
        }

        // EN_Month_Abbrev,EN_Month
        folder = ResourceManager.getPatternRegexFolder().getFolder(PATH_DATE);
        if (folder.exists()) {
            if (!folder.getFile("EN_Month_Abbrev_0.1.pattern").exists()) {
                Pattern pattern = newPattern(
                        "EN_Month_Abbrev", SQLLanguage, "'^(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)$'"); //$NON-NLS-1$ //$NON-NLS-2$
                if (pattern != null) {
                    setTagValue(pattern, "Jan | Feb ", "Month English abbreviation"); //$NON-NLS-1$ //$NON-NLS-2$
                    ElementWriterFactory.getInstance().createPatternWriter().create(pattern, folder);
                }
            }

            if (!folder.getFile("EN_Month_0.1.pattern").exists()) {
                Pattern pattern = newPattern("EN_Month", SQLLanguage, //$NON-NLS-1$
                        "'^(January|June|July|February|March|May|April|August|September|October|November|December)$'"); //$NON-NLS-1$
                if (pattern != null) {
                    setTagValue(pattern, "January | February ", "Month in English"); //$NON-NLS-1$ //$NON-NLS-2$
                    ElementWriterFactory.getInstance().createPatternWriter().create(pattern, folder);
                }
            }
        }
        return true;
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
