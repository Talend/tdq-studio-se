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
package org.talend.dataquality.record.linkage.ui.composite.tableviewer.definition;

import org.talend.dataquality.rules.MatchKeyDefinition;
import org.talend.dataquality.rules.SurvivorshipKeyDefinition;

/**
 * DOC yyin class global comment. Detailled comment
 */
public class MatchKeyAndSurvivorDefinition {

    MatchKeyDefinition matchKey;

    SurvivorshipKeyDefinition survivorShipKey;

    public MatchKeyAndSurvivorDefinition() {

    }

    public MatchKeyDefinition getMatchKey() {
        return this.matchKey;
    }

    // when the Table viewer remove a row, need to compare if it is equals
    // TDQ-8264: modify: use the match key to judge if equals or not, but not only use the name of the match key
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MatchKeyAndSurvivorDefinition && matchKey != null
                && matchKey.equals(((MatchKeyAndSurvivorDefinition) obj).getMatchKey())) {
            return true;
        } else {
            return false;
        }
    }

    public void setMatchKey(MatchKeyDefinition matchKey) {
        this.matchKey = matchKey;
    }

    public SurvivorshipKeyDefinition getSurvivorShipKey() {
        return this.survivorShipKey;
    }

    public void setSurvivorShipKey(SurvivorshipKeyDefinition survivorShipKey) {
        this.survivorShipKey = survivorShipKey;
    }

    public String getColumn() {
        return matchKey.getColumn();
    }
}
