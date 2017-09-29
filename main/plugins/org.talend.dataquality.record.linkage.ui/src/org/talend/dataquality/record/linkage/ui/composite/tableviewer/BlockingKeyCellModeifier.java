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
package org.talend.dataquality.record.linkage.ui.composite.tableviewer;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.eclipse.swt.widgets.TableItem;
import org.talend.dataquality.record.linkage.utils.BlockingKeyAlgorithmEnum;
import org.talend.dataquality.record.linkage.utils.BlockingKeyPostAlgorithmEnum;
import org.talend.dataquality.record.linkage.utils.BlockingKeyPreAlgorithmEnum;
import org.talend.dataquality.record.linkage.utils.MatchAnalysisConstant;
import org.talend.dataquality.rules.BlockKeyDefinition;

/**
 * created by zshen on Aug 6, 2013 Detailled comment
 * 
 */
public class BlockingKeyCellModeifier extends AbstractMatchCellModifier<BlockKeyDefinition> {

    /**
     * DOC zshen MatchRuleCellModifier constructor comment.
     */
    public BlockingKeyCellModeifier(AbstractMatchAnalysisTableViewer<BlockKeyDefinition> newTableViewer) {
        this.tableViewer = newTableViewer;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ICellModifier#canModify(java.lang.Object, java.lang.String)
     */
    @Override
    public boolean canModify(Object element, String property) {
        if (element != null && element instanceof BlockKeyDefinition) {
            BlockKeyDefinition bkd = (BlockKeyDefinition) element;
            if (MatchAnalysisConstant.PRE_VALUE.equalsIgnoreCase(property)) {
                return BlockingKeyPreAlgorithmEnum.getTypeBySavedValue(bkd.getPreAlgorithm().getAlgorithmType())
                        .isTakeParameter();
            } else if (MatchAnalysisConstant.KEY_VALUE.equalsIgnoreCase(property)) {
                return BlockingKeyAlgorithmEnum.getTypeBySavedValue(bkd.getAlgorithm().getAlgorithmType()).isTakeParameter();
            } else if (MatchAnalysisConstant.POST_VALUE.equalsIgnoreCase(property)) {
                return BlockingKeyPostAlgorithmEnum.getTypeBySavedValue(bkd.getPostAlgorithm().getAlgorithmType())
                        .isTakeParameter();
            } else if (MatchAnalysisConstant.PRECOLUMN.equalsIgnoreCase(property)) {
                return columnList.size() > 0;
            }
            return true;
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ICellModifier#getValue(java.lang.Object, java.lang.String)
     */
    @Override
    public Object getValue(Object element, String property) {
        BlockKeyDefinition bkd = (BlockKeyDefinition) element;
        if (MatchAnalysisConstant.PRE_ALGO.equalsIgnoreCase(property)) {
            return BlockingKeyPreAlgorithmEnum.getTypeBySavedValue(bkd.getPreAlgorithm().getAlgorithmType()).ordinal();
        } else if (MatchAnalysisConstant.PRE_VALUE.equalsIgnoreCase(property)) {
            return bkd.getPreAlgorithm().getAlgorithmParameters();
        } else if (MatchAnalysisConstant.KEY_ALGO.equalsIgnoreCase(property)) {
            return BlockingKeyAlgorithmEnum.getTypeBySavedValue(bkd.getAlgorithm().getAlgorithmType()).ordinal();
        } else if (MatchAnalysisConstant.KEY_VALUE.equalsIgnoreCase(property)) {
            return bkd.getAlgorithm().getAlgorithmParameters();
        } else if (MatchAnalysisConstant.POST_ALGO.equalsIgnoreCase(property)) {
            return BlockingKeyPostAlgorithmEnum.getTypeBySavedValue(bkd.getPostAlgorithm().getAlgorithmType()).ordinal();
        } else if (MatchAnalysisConstant.POST_VALUE.equalsIgnoreCase(property)) {
            return bkd.getPostAlgorithm().getAlgorithmParameters();
        } else if (MatchAnalysisConstant.PRECOLUMN.equalsIgnoreCase(property)) {
            return columnList.indexOf(bkd.getColumn());
        } else if (MatchAnalysisConstant.BLOCKING_KEY_NAME.equalsIgnoreCase(property)) {
            return bkd.getName();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ICellModifier#modify(java.lang.Object, java.lang.String, java.lang.Object)
     */
    @Override
    public void modify(Object element, String property, Object value) {
        if (element instanceof TableItem) {
            BlockKeyDefinition bkd = (BlockKeyDefinition) ((TableItem) element).getData();
            String newValue = String.valueOf(value);
            if (MatchAnalysisConstant.PRE_ALGO.equalsIgnoreCase(property)) {
                BlockingKeyPreAlgorithmEnum valueByIndex = BlockingKeyPreAlgorithmEnum.getTypeByIndex(Integer.valueOf(newValue)
                        .intValue());
                if (StringUtils.equals(bkd.getPreAlgorithm().getAlgorithmType(), valueByIndex.getComponentValueName())) {
                    return;
                }

                bkd.getPreAlgorithm().setAlgorithmType(valueByIndex.getComponentValueName());
                bkd.getPreAlgorithm().setAlgorithmParameters(valueByIndex.getDefaultValue());
            } else if (MatchAnalysisConstant.PRE_VALUE.equalsIgnoreCase(property)) {
                if (StringUtils.equals(bkd.getPreAlgorithm().getAlgorithmParameters(), newValue)) {
                    return;
                }
                BlockingKeyPreAlgorithmEnum valueBySavedValue = BlockingKeyPreAlgorithmEnum.getTypeBySavedValue(bkd
                        .getPreAlgorithm().getAlgorithmType());
                if (isParameterInValid(valueBySavedValue.getDefaultValue(), newValue)) {
                    return;
                }
                bkd.getPreAlgorithm().setAlgorithmParameters(String.valueOf(value));
            } else if (MatchAnalysisConstant.KEY_ALGO.equalsIgnoreCase(property)) {
                BlockingKeyAlgorithmEnum valueByIndex = BlockingKeyAlgorithmEnum.getTypeByIndex(Integer.valueOf(newValue)
                        .intValue());
                if (StringUtils.equals(bkd.getAlgorithm().getAlgorithmType(), valueByIndex.getComponentValueName())) {
                    return;
                }
                bkd.getAlgorithm().setAlgorithmType(valueByIndex.getComponentValueName());
                bkd.getAlgorithm().setAlgorithmParameters(valueByIndex.getDefaultValue());
            } else if (MatchAnalysisConstant.KEY_VALUE.equalsIgnoreCase(property)) {
                if (StringUtils.equals(bkd.getAlgorithm().getAlgorithmParameters(), newValue)) {
                    return;
                }
                BlockingKeyAlgorithmEnum valueBySavedValue = BlockingKeyAlgorithmEnum.getTypeBySavedValue(bkd.getAlgorithm()
                        .getAlgorithmType());
                if (isParameterInValid(valueBySavedValue.getDefaultValue(), newValue)) {
                    return;
                }
                bkd.getAlgorithm().setAlgorithmParameters(newValue);
            } else if (MatchAnalysisConstant.POST_ALGO.equalsIgnoreCase(property)) {
                BlockingKeyPostAlgorithmEnum valueByIndex = BlockingKeyPostAlgorithmEnum.getTypeByIndex(Integer.valueOf(newValue)
                        .intValue());
                if (StringUtils.equals(bkd.getPostAlgorithm().getAlgorithmType(), valueByIndex.getComponentValueName())) {
                    return;
                }
                bkd.getPostAlgorithm().setAlgorithmType(valueByIndex.getComponentValueName());
                bkd.getPostAlgorithm().setAlgorithmParameters(valueByIndex.getDefaultValue());
            } else if (MatchAnalysisConstant.POST_VALUE.equalsIgnoreCase(property)) {
                if (StringUtils.equals(bkd.getPostAlgorithm().getAlgorithmParameters(), newValue)) {
                    return;
                }
                BlockingKeyPostAlgorithmEnum valueBySavedValue = BlockingKeyPostAlgorithmEnum.getTypeBySavedValue(bkd
                        .getPostAlgorithm().getAlgorithmType());
                if (isParameterInValid(valueBySavedValue.getDefaultValue(), newValue)) {
                    return;
                }
                bkd.getPostAlgorithm().setAlgorithmParameters(newValue);
            } else if (MatchAnalysisConstant.PRECOLUMN.equalsIgnoreCase(property)) {
                if (Integer.parseInt(newValue) == -1) {
                    return;
                }
                String columnName = columnList.get(Integer.parseInt(newValue)).getName();
                if (StringUtils.equals(bkd.getColumn(), columnName)) {
                    return;
                }
                bkd.setColumn(columnName);
                tableViewer.noticeColumnSelectChange();
            } else if (MatchAnalysisConstant.BLOCKING_KEY_NAME.equalsIgnoreCase(property)) {
                if (StringUtils.equals(bkd.getName(), newValue)) {
                    return;
                }
                bkd.setName(newValue);
            } else {
                return;
            }
            tableViewer.update(bkd, null);
        }
    }

    /**
     * DOC zshen Comment method "isParameterInValid".
     * 
     * @param defaultValue
     * @param newValue
     * @return
     */
    private boolean isParameterInValid(String defaultValue, String newValue) {
        if (isIntegerType(defaultValue)) {
            return !isIntegerType(newValue);
        }
        return false;
    }

    /**
     * DOC zshen Comment method "isIntegerType".
     * 
     * @param defaultValue
     * @return
     */
    private boolean isIntegerType(String value) {
        return NumberUtils.isDigits(value);
    }

}
