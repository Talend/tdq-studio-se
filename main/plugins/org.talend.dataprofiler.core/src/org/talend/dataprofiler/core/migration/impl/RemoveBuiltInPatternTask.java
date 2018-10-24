package org.talend.dataprofiler.core.migration.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.talend.dataprofiler.core.migration.AbstractWorksapceUpdateTask;
import org.talend.dataquality.analysis.Analysis;
import org.talend.dataquality.domain.pattern.Pattern;
import org.talend.dataquality.indicators.Indicator;
import org.talend.dataquality.indicators.PatternMatchingIndicator;
import org.talend.dataquality.indicators.RegexpMatchingIndicator;
import org.talend.dataquality.indicators.columnset.AllMatchIndicator;
import org.talend.dq.helper.resourcehelper.AnaResourceFileHelper;
import org.talend.dq.writer.EMFSharedResources;

public class RemoveBuiltInPatternTask extends AbstractWorksapceUpdateTask {

    private static Logger log = Logger.getLogger(RemoveBuiltInPatternTask.class);

    public Date getOrder() {
        return createDate(2018, 10, 24);
    }

    public MigrationTaskType getMigrationTaskType() {
        return MigrationTaskType.FILE;
    }

    @Override
    protected boolean doExecute() throws Exception {
        try {
            List<Analysis> analyses = AnaResourceFileHelper.getInstance().getAllElement();
            for (Analysis analysis : analyses) {
                EList<Indicator> allIndics = analysis.getResults().getIndicators();
                for (Indicator indicator : allIndics) {
                    if (!(indicator instanceof PatternMatchingIndicator)) {
                        continue;
                    }
                    List<Pattern> builtInLs = new ArrayList<Pattern>();
                    // columnset analysis case
                    if (indicator instanceof AllMatchIndicator) {
                        EList<RegexpMatchingIndicator> list =
                                ((AllMatchIndicator) indicator).getCompositeRegexMatchingIndicators();
                        for (RegexpMatchingIndicator regxIndicator : list) {
                            builtInLs.addAll(regxIndicator.getParameters().getDataValidDomain().getBuiltInPatterns());
                            regxIndicator.getParameters().getDataValidDomain().getBuiltInPatterns().clear();
                        }
                        // column analysis case
                    } else {
                        builtInLs.addAll(indicator.getParameters().getDataValidDomain().getBuiltInPatterns());
                        indicator.getParameters().getDataValidDomain().getBuiltInPatterns().clear();
                    }
                }
                EMFSharedResources.getInstance().saveResource(analysis.eResource());
            }
        } catch (Exception e) {
            log.error(e, e);
            return false;
        }
        return true;
    }
}
