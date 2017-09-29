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
package test.udi;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.talend.dataquality.domain.Domain;
import org.talend.dataquality.domain.JavaUDIIndicatorParameter;
import org.talend.dataquality.indicators.sql.impl.UserDefIndicatorImpl;

/**
 * @author sizhaoliu
 * 
 * Java implementation of a user defined indicator who returns matching and unmatching count of defined regex
 * pattern(s).
 * 
 * This example also shows how to retrieve parameters from user interface of the indicator.
 * 
 * User can setup one or more "pattern"s in the indicator page or the analyze page. When there are several patterns, the
 * data is matched if it matches with all the patterns.
 */
public class MyAndRegexIndicator extends UserDefIndicatorImpl {

    List<Pattern> patternList = new ArrayList<Pattern>();

    @Override
    public boolean prepare() {
        this.reset();
        String paramValue;
        Pattern pattern;
        if (parameters != null) {
            final Domain dataValidDomain = parameters.getIndicatorValidDomain();
            if (dataValidDomain != null) {
                patternList.clear();
                for (JavaUDIIndicatorParameter param : dataValidDomain.getJavaUDIIndicatorParameter()) {
                    if (param != null) {
                        if (param.getKey().startsWith("pattern")) {
                            paramValue = param.getValue();
                            pattern = Pattern.compile(paramValue);
                            patternList.add(pattern);
                        }
                    }
                }
            }
        }
        return !patternList.isEmpty();
    }

    @Override
    public boolean reset() {
        super.reset(); // reset count and nullCount values
        matchingValueCount = 0L;
        notMatchingValueCount = NOT_MATCHING_VALUE_COUNT_EDEFAULT;
        return true;
    }

    @Override
    public boolean handle(Object data) {
        // Check null value and increment count
        super.handle(data);

        if (data != null) {
            for (Pattern p : patternList) {
                if (!p.matcher(data.toString()).matches()) {
                    return true;
                }
            }
            matchingValueCount++;
        }
        return true;
    }

    @Override
    public boolean finalizeComputation() {
        // compute non matching value
        notMatchingValueCount = count - matchingValueCount;
        // set userCount value so that user can select "User Defined Count" as indicator type too.
        userCount = matchingValueCount;
        return true;
    }

}
