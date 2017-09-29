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
package org.talend.dataquality.helpers;

import org.talend.cwm.relational.TdColumn;
import org.talend.cwm.relational.TdExpression;
import org.talend.cwm.relational.util.RelationalSwitch;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.analysis.util.AnalysisSwitch;
import org.talend.dataquality.domain.DateValue;
import org.talend.dataquality.domain.Domain;
import org.talend.dataquality.domain.IntegerValue;
import org.talend.dataquality.domain.RealNumberValue;
import org.talend.dataquality.domain.TextValue;
import org.talend.dataquality.domain.pattern.Pattern;
import org.talend.dataquality.domain.pattern.RegularExpression;
import org.talend.dataquality.domain.pattern.util.PatternSwitch;
import org.talend.dataquality.domain.util.DomainSwitch;
import org.talend.dataquality.indicators.schema.CatalogIndicator;
import org.talend.dataquality.indicators.schema.ConnectionIndicator;
import org.talend.dataquality.indicators.schema.SchemaIndicator;
import org.talend.dataquality.indicators.schema.util.SchemaSwitch;

/**
 * @author scorreia
 * 
 * 
 */
public final class DataqualitySwitchHelper {

    private DataqualitySwitchHelper() {
    }

    public static final SchemaSwitch<ConnectionIndicator> CONNECTION_SWITCH = new SchemaSwitch<ConnectionIndicator>() {

        @Override
        public ConnectionIndicator caseConnectionIndicator(ConnectionIndicator object) {
            return object;
        }

    };

    public static final SchemaSwitch<CatalogIndicator> CATALOG_SWITCH = new SchemaSwitch<CatalogIndicator>() {

        @Override
        public CatalogIndicator caseCatalogIndicator(CatalogIndicator object) {
            return object;
        }

    };

    public static final SchemaSwitch<SchemaIndicator> SCHEMA_SWITCH = new SchemaSwitch<SchemaIndicator>() {

        @Override
        public SchemaIndicator caseSchemaIndicator(SchemaIndicator object) {
            return object;
        }

    };

    public static final AnalysisSwitch<Analysis> ANALYSIS_SWITCH = new AnalysisSwitch<Analysis>() {

        @Override
        public Analysis caseAnalysis(Analysis object) {
            return object;
        }

    };

    public static final DomainSwitch<RealNumberValue> REAL_NB_VALUE_SWITCH = new DomainSwitch<RealNumberValue>() {

        @Override
        public RealNumberValue caseRealNumberValue(RealNumberValue object) {
            return object;
        }

    };

    public static final DomainSwitch<TextValue> TEXT_VALUE_SWITCH = new DomainSwitch<TextValue>() {

        @Override
        public TextValue caseTextValue(TextValue object) {
            return object;
        }

    };

    public static final DomainSwitch<Domain> DOMAIN_SWITCH = new DomainSwitch<Domain>() {

        @Override
        public Domain caseDomain(Domain object) {
            return object;
        }

    };

    public static final RelationalSwitch<TdExpression> TDEXPRESSION_SWITCH = new RelationalSwitch<TdExpression>() {

        @Override
        public TdExpression caseTdExpression(TdExpression object) {
            return object;
        }

    };

    public static final RelationalSwitch<TdColumn> TDCOLUMN_SWITCH = new RelationalSwitch<TdColumn>() {

        @Override
        public TdColumn caseTdColumn(TdColumn object) {
            return object;
        }

    };

    public static final PatternSwitch<Pattern> PATTERN_SWITCH = new PatternSwitch<Pattern>() {

        /*
         * (non-Javadoc)
         * 
         * @see
         * org.talend.dataquality.domain.pattern.util.PatternSwitch#casePattern(org.talend.dataquality.domain.pattern
         * .Pattern)
         */
        @Override
        public Pattern casePattern(Pattern object) {
            return object;
        }

    };

    public static final PatternSwitch<RegularExpression> REGULAR_EXPR_SWITCH = new PatternSwitch<RegularExpression>() {

        /*
         * (non-Javadoc)
         * 
         * @see
         * org.talend.dataquality.domain.pattern.util.PatternSwitch#caseRegularExpression(org.talend.dataquality.domain
         * .pattern.RegularExpression)
         */
        @Override
        public RegularExpression caseRegularExpression(RegularExpression object) {
            return object;
        }

    };

    public static final DomainSwitch<String> LITTERAL_VALUE_AS_TEXT_SWITCH = new DomainSwitch<String>() {

        @Override
        public String caseDateValue(DateValue object) {
            return String.valueOf(object.getValue());
        }

        @Override
        public String caseIntegerValue(IntegerValue object) {
            return String.valueOf(object.getValue());
        }

        @Override
        public String caseRealNumberValue(RealNumberValue object) {
            return String.valueOf(object.getValue());
        }

        @Override
        public String caseTextValue(TextValue object) {
            return object.getValue();
        }

    };
}
