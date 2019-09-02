// ============================================================================
//
// Copyright (C) 2006-2019 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.cwm.compare.merge;

import static org.eclipse.emf.compare.utils.ReferenceUtil.safeEGet;
import static org.eclipse.emf.compare.utils.ReferenceUtil.safeESet;

import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.ReferenceChange;
import org.eclipse.emf.compare.merge.ReferenceChangeMerger;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.talend.cwm.helper.SwitchHelpers;
import org.talend.dq.helper.EObjectHelper;

import orgomg.cwm.foundation.softwaredeployment.DataManager;
import orgomg.cwm.resource.relational.Catalog;
import orgomg.cwm.resource.relational.Schema;

/**
 * created by talend on 2015-07-28 Detailled comment.
 *
 */
public class DQReferenceMerger extends ReferenceChangeMerger {

    public DQReferenceMerger() {
        // TODO Auto-generated constructor stub
    }

    /*
     * override this method to fix a specail case: when add a catalog on database server,this new one can't be added in
     * local db connection
     */
    @Override
    protected void addInTarget(ReferenceChange diff, boolean rightToLeft) {
        final Match match = diff.getMatch();
        final EObject expectedContainer;
        if (rightToLeft) {
            expectedContainer = match.getLeft();
        } else {
            expectedContainer = match.getRight();
        }

        if (expectedContainer == null) {
            // FIXME throw exception? log? re-try to merge our requirements?
            // one of the "required" diffs should have created our container.
            return;
        }

        final Comparison comparison = match.getComparison();
        final EReference reference = diff.getReference();
        final EObject expectedValue;
        final Match valueMatch = comparison.getMatch(diff.getValue());
        if (valueMatch == null) {
            // This is an out of scope value.
            if (diff.getValue().eIsProxy()) {
                // Copy the proxy
                expectedValue = EcoreUtil.copy(diff.getValue());
            } else {
                // Use the same value.
                expectedValue = diff.getValue();
            }
        } else if (rightToLeft) {
            if (reference.isContainment()) {
                // TDQ-17429 msjian: we need deepcopy to copy all info, for example: sqlDataType or taggedValue under
                // the column feature
                expectedValue = EObjectHelper.deepCopy(diff.getValue());
                valueMatch.setLeft(expectedValue);
            } else {
                // qiongli: when I add catalog on remote server,test on reload, should replace "valueMatch.getLeft()"
                // with "valueMatch.getRight()" at here.
                // expectedValue = valueMatch.getLeft();
                expectedValue = valueMatch.getRight();
                // qiongli: remove the newest DataManage from the right Catalog or Schema,avoid a missing
                // "datamanage herf=" "
                Catalog catlog = SwitchHelpers.CATALOG_SWITCH.doSwitch(expectedValue);
                Schema schema = SwitchHelpers.SCHEMA_SWITCH.doSwitch(expectedValue);
                if (catlog != null) {
                    EList<DataManager> dataManager = catlog.getDataManager();
                    dataManager.clear();
                } else if (schema != null) {
                    EList<DataManager> dataManager = schema.getDataManager();
                    dataManager.clear();
                }
            }
        } else {
            if (reference.isContainment()) {
                expectedValue = createCopy(diff.getValue());
                valueMatch.setRight(expectedValue);
            } else {
                expectedValue = valueMatch.getLeft();
            }
        }

        // We have the container, reference and value. We need to know the insertion index.
        if (reference.isMany()) {
            final int insertionIndex = findInsertionIndex(comparison, diff, rightToLeft);

            final List<EObject> targetList = (List<EObject>) safeEGet(expectedContainer, reference);
            addAt(targetList, expectedValue, insertionIndex);
        } else {
            safeESet(expectedContainer, reference, expectedValue);
        }

        if (reference.isContainment()) {
            // Copy XMI ID when applicable.
            final Resource initialResource = diff.getValue().eResource();
            final Resource targetResource = expectedValue.eResource();
            if (initialResource instanceof XMIResource && targetResource instanceof XMIResource) {
                ((XMIResource) targetResource).setID(expectedValue, ((XMIResource) initialResource).getID(diff.getValue()));
            }
        }

        // no need to check this for DQ items,
        // checkImpliedDiffsOrdering(diff, rightToLeft);
    }

}
