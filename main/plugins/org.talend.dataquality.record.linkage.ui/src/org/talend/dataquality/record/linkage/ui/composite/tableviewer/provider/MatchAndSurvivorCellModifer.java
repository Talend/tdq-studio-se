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
package org.talend.dataquality.record.linkage.ui.composite.tableviewer.provider;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.widgets.TableItem;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.dataquality.record.linkage.constant.AttributeMatcherType;
import org.talend.dataquality.record.linkage.constant.TokenizedResolutionMethod;
import org.talend.dataquality.record.linkage.ui.composite.tableviewer.AbstractMatchAnalysisTableViewer;
import org.talend.dataquality.record.linkage.ui.composite.tableviewer.AbstractMatchCellModifier;
import org.talend.dataquality.record.linkage.ui.composite.tableviewer.definition.MatchKeyAndSurvivorDefinition;
import org.talend.dataquality.record.linkage.ui.composite.tableviewer.filter.ColumnsDateFilter;
import org.talend.dataquality.record.linkage.utils.HandleNullEnum;
import org.talend.dataquality.record.linkage.utils.MatchAnalysisConstant;
import org.talend.dataquality.record.linkage.utils.SurvivorShipAlgorithmEnum;
import org.talend.dataquality.rules.MatchKeyDefinition;

/**
 * DOC yyin class global comment. Detailled comment
 */
public class MatchAndSurvivorCellModifer extends AbstractMatchCellModifier<MatchKeyAndSurvivorDefinition> {

    public MatchAndSurvivorCellModifer(AbstractMatchAnalysisTableViewer<MatchKeyAndSurvivorDefinition> newTableViewer) {
        this.tableViewer = newTableViewer;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ICellModifier#canModify(java.lang.Object, java.lang.String)
     */
    @Override
    public boolean canModify(Object element, String property) {
        if (element != null && element instanceof MatchKeyAndSurvivorDefinition) {
            MatchKeyAndSurvivorDefinition mkd = (MatchKeyAndSurvivorDefinition) element;
            if (MatchAnalysisConstant.CUSTOM_MATCHER.equalsIgnoreCase(property)) {
                if (!AttributeMatcherType.CUSTOM.name().equals(mkd.getMatchKey().getAlgorithm().getAlgorithmType())) {
                    return false;
                }
            } else if (MatchAnalysisConstant.PARAMETER.equalsIgnoreCase(property)) {
                return isSurvivorShipAlgorithm(mkd, SurvivorShipAlgorithmEnum.MOST_TRUSTED_SOURCE)
                        | isSurvivorShipAlgorithm(mkd, SurvivorShipAlgorithmEnum.CONCATENATE);
            } else if (MatchAnalysisConstant.THRESHOLD.equalsIgnoreCase(property)) {
                return !isMatcherType(mkd, AttributeMatcherType.EXACT);
            } else if (MatchAnalysisConstant.TOKENIZATION_TYPE.equalsIgnoreCase(property)) {
                if (AttributeMatcherType.CUSTOM.name().equals(mkd.getMatchKey().getAlgorithm().getAlgorithmType())) {
                    return false;
                }
            } else if (MatchAnalysisConstant.REFERENCE_COLUMN.equalsIgnoreCase(property)) {
                return isSurvivorShipAlgorithm(mkd, SurvivorShipAlgorithmEnum.MOST_ANCIENT)
                        || isSurvivorShipAlgorithm(mkd, SurvivorShipAlgorithmEnum.MOST_RECENT);
            }
            return true;
        }
        return false;
    }

    private boolean isMatcherType(MatchKeyAndSurvivorDefinition mkd, AttributeMatcherType matcherType) {
        return mkd.getMatchKey().getAlgorithm().getAlgorithmType().equals(matcherType.name());
    }

    private boolean isSurvivorShipAlgorithm(MatchKeyAndSurvivorDefinition mkd, SurvivorShipAlgorithmEnum algorithm) {
        return mkd.getSurvivorShipKey().getFunction().getAlgorithmType().equals(algorithm.getComponentValueName());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ICellModifier#getValue(java.lang.Object, java.lang.String)
     */
    @Override
    public Object getValue(Object element, String property) {
        MatchKeyAndSurvivorDefinition mkd = (MatchKeyAndSurvivorDefinition) element;
        if (MatchAnalysisConstant.HANDLE_NULL.equalsIgnoreCase(property)) {
            return HandleNullEnum.getTypeByValue(mkd.getMatchKey().getHandleNull()).ordinal();
        } else if (MatchAnalysisConstant.MATCHING_TYPE.equalsIgnoreCase(property)) {
            // TDQ-13070: fix when click the have selected "Custom" Function, the show text is changed to
            // others(t-swoosh)
            return AttributeMatcherType.valueOf(mkd.getMatchKey().getAlgorithm().getAlgorithmType()).ordinal();
        } else if (MatchAnalysisConstant.CUSTOM_MATCHER.equalsIgnoreCase(property)) {
            return mkd.getMatchKey().getAlgorithm().getAlgorithmParameters();
        } else if (MatchAnalysisConstant.INPUT_COLUMN.equalsIgnoreCase(property)) {
            int colIdx = 0;
            for (MetadataColumn metaColumn : columnList) {
                if (metaColumn.getName().equals(mkd.getMatchKey().getColumn())) {
                    break;
                }
                colIdx++;
            }
            return colIdx;
        } else if (MatchAnalysisConstant.CONFIDENCE_WEIGHT.equalsIgnoreCase(property)) {
            return String.valueOf(mkd.getMatchKey().getConfidenceWeight());
        } else if (MatchAnalysisConstant.MATCH_KEY_NAME.equalsIgnoreCase(property)) {
            return mkd.getMatchKey().getName();
        } else if (MatchAnalysisConstant.THRESHOLD.equalsIgnoreCase(property)) {
            return String.valueOf(mkd.getMatchKey().getThreshold());
        } else if (MatchAnalysisConstant.FUNCTION.equalsIgnoreCase(property)) {
            return SurvivorShipAlgorithmEnum.getTypeBySavedValue(
                    mkd.getSurvivorShipKey().getFunction().getAlgorithmType()).getIndex();
        } else if (MatchAnalysisConstant.PARAMETER.equalsIgnoreCase(property)) {
            return mkd.getSurvivorShipKey().getFunction().getAlgorithmParameters();
        } else if (MatchAnalysisConstant.TOKENIZATION_TYPE.equalsIgnoreCase(property)) {
            return TokenizedResolutionMethod
                    .getTypeByValueWithDefault(mkd.getMatchKey().getTokenizationType())
                    .ordinal();
        } else if (MatchAnalysisConstant.REFERENCE_COLUMN.equalsIgnoreCase(property)) {
            return getReferenceColumnValue(mkd);
        }
        return null;

    }

    protected Object getReferenceColumnValue(MatchKeyAndSurvivorDefinition mkd) {
        int colIdx = 0;
        String referenceColumn = mkd.getSurvivorShipKey().getFunction().getReferenceColumn();
        boolean isReferenceColumnExist = true;
        if (StringUtils.isEmpty(referenceColumn)) {
            isReferenceColumnExist = false;
        }
        for (MetadataColumn metaColumn : columnList) {
            ColumnsDateFilter dateColFilter = new ColumnsDateFilter();
            if (!dateColFilter.test(metaColumn)) {
                continue;
            }
            if (metaColumn.getName().equals(isReferenceColumnExist ? mkd.getMatchKey().getColumn() : referenceColumn)) {
                break;
            }
            colIdx++;
        }
        return colIdx;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ICellModifier#modify(java.lang.Object, java.lang.String, java.lang.Object)
     */
    @Override
    public void modify(Object element, String property, Object value) {
        if (element instanceof TableItem) {
            MatchKeyAndSurvivorDefinition mkd = (MatchKeyAndSurvivorDefinition) ((TableItem) element).getData();
            String newValue = String.valueOf(value);
            MatchKeyDefinition matchKey = mkd.getMatchKey();
            if (MatchAnalysisConstant.HANDLE_NULL.equalsIgnoreCase(property)) {
                HandleNullEnum valueByIndex = HandleNullEnum.values()[Integer.valueOf(newValue)];
                if (StringUtils.equals(matchKey.getHandleNull(), valueByIndex.getValue())) {
                    return;
                }
                matchKey.setHandleNull(valueByIndex.getValue());
            } else if (MatchAnalysisConstant.MATCHING_TYPE.equalsIgnoreCase(property)) {
                int idx = Integer.valueOf(newValue);
                if (idx == AttributeMatcherType.DUMMY.ordinal()) {
                    idx += 1; // The DUMMY algorithm is internal and does not exist in the combo lists of
                              // MatchRuleItemEditor or MatchRuleAnalysisEditor. So we increment the index by 1 in order
                              // to get the correct Matcher.
                }
                AttributeMatcherType valueByIndex = AttributeMatcherType.values()[idx];
                if (StringUtils.equals(matchKey.getAlgorithm().getAlgorithmType(), valueByIndex.name())) {
                    return;
                }
                matchKey.getAlgorithm().setAlgorithmType(valueByIndex.name());
                matchKey.getAlgorithm().setAlgorithmParameters(StringUtils.EMPTY);
                if (isMatcherType(mkd, AttributeMatcherType.EXACT)) {
                    matchKey.setThreshold(1.0d);
                }
            } else if (MatchAnalysisConstant.CUSTOM_MATCHER.equalsIgnoreCase(property)) {
                if (StringUtils.equals(matchKey.getAlgorithm().getAlgorithmParameters(), newValue)) {
                    return;
                }
                matchKey.getAlgorithm().setAlgorithmParameters(String.valueOf(value));
            } else if (MatchAnalysisConstant.CONFIDENCE_WEIGHT.equalsIgnoreCase(property)) {
                if (!org.apache.commons.lang.math.NumberUtils.isDigits(newValue)) {
                    return;
                }
                if (matchKey.getConfidenceWeight() == Integer.valueOf(newValue).intValue()) {
                    return;
                }
                matchKey.setConfidenceWeight(Integer.valueOf(newValue).intValue());
            } else if (MatchAnalysisConstant.MATCH_KEY_NAME.equalsIgnoreCase(property)) {
                if (StringUtils.equals(matchKey.getName(), newValue)) {
                    return;
                }
                matchKey.setName(newValue);
                mkd.getSurvivorShipKey().setName(newValue);
            } else if (MatchAnalysisConstant.INPUT_COLUMN.equalsIgnoreCase(property)) {
                String metaColumnName = convertColIndex2Name(newValue, null);
                if (metaColumnName == null) {
                    return;
                    // no modify anything
                }
                matchKey.setColumn(metaColumnName);
                mkd.getSurvivorShipKey().setColumn(metaColumnName);
                // added TDQ-9296, nodify the change to let the upper layer know this
                tableViewer.noticeColumnSelectChange();
            } else if (MatchAnalysisConstant.THRESHOLD.equalsIgnoreCase(property)) {
                if (!org.apache.commons.lang.math.NumberUtils.isNumber(newValue)) {
                    return;
                }
                if (matchKey.getThreshold() == Double.parseDouble(newValue)) {
                    return;
                }
                try {
                    matchKey.setThreshold(Double.parseDouble(newValue));
                } catch (NumberFormatException e) {
                    // revert user change at here so don't need do anything
                }
            } else if (MatchAnalysisConstant.FUNCTION.equalsIgnoreCase(property)) {
                SurvivorShipAlgorithmEnum valueByIndex =
                        SurvivorShipAlgorithmEnum.getTypeByIndex(Integer.valueOf(newValue).intValue());
                if (StringUtils.equals(mkd.getSurvivorShipKey().getFunction().getAlgorithmType(),
                        valueByIndex.getComponentValueName())) {
                    return;
                }
                mkd.getSurvivorShipKey().getFunction().setAlgorithmType(valueByIndex.getComponentValueName());
                if (!(isSurvivorShipAlgorithm(mkd, SurvivorShipAlgorithmEnum.MOST_TRUSTED_SOURCE) | (isSurvivorShipAlgorithm(
                        mkd, SurvivorShipAlgorithmEnum.CONCATENATE)))) {
                    mkd.getSurvivorShipKey().getFunction().setAlgorithmParameters(StringUtils.EMPTY);
                }
                if (!(isSurvivorShipAlgorithm(mkd, SurvivorShipAlgorithmEnum.MOST_RECENT) || isSurvivorShipAlgorithm(
                        mkd, SurvivorShipAlgorithmEnum.MOST_ANCIENT))) {
                    mkd.getSurvivorShipKey().getFunction().setReferenceColumn(StringUtils.EMPTY);
                }

            } else if (MatchAnalysisConstant.REFERENCE_COLUMN.equalsIgnoreCase(property)) {
                String metaColumnName = convertColIndex2Name(newValue, new ColumnsDateFilter());
                if (metaColumnName == null) {
                    return;
                    // no modify anything
                }
                mkd.getSurvivorShipKey().getFunction().setReferenceColumn(metaColumnName);
            } else if (MatchAnalysisConstant.PARAMETER.equalsIgnoreCase(property)) {
                mkd.getSurvivorShipKey().getFunction().setAlgorithmParameters(newValue);
            } else if (MatchAnalysisConstant.TOKENIZATION_TYPE.equalsIgnoreCase(property)) {
                TokenizedResolutionMethod valueByIndex = TokenizedResolutionMethod.values()[Integer.valueOf(newValue)];
                if (StringUtils.equals(mkd.getMatchKey().getTokenizationType(), valueByIndex.getComponentValue())) {
                    return;
                }
                mkd.getMatchKey().setTokenizationType(valueByIndex.getComponentValue());
            } else {
                return;
            }
            tableViewer.update(mkd, null);
        }
    }

}
