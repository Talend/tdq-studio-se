/**
 * // ============================================================================
 * //
 * // Copyright (C) 2006-2017 Talend Inc. - www.talend.com
 * //
 * // This source code is available under agreement available at
 * // %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
 * //
 * // You should have received a copy of the agreement
 * // along with this program; if not, write to Talend SA
 * // 9 rue Pages 92150 Suresnes, France
 * //
 * // ============================================================================
 * 
 *
 * $Id$
 */
package org.talend.dataquality.rules;

import org.eclipse.emf.common.util.EList;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>DQ Rule</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * The data quality rule base class.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.talend.dataquality.rules.DQRule#getCriticalityLevel <em>Criticality Level</em>}</li>
 *   <li>{@link org.talend.dataquality.rules.DQRule#getElements <em>Elements</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.talend.dataquality.rules.RulesPackage#getDQRule()
 * @model
 * @generated
 */
public interface DQRule extends IndicatorDefinition {
    /**
     * Returns the value of the '<em><b>Criticality Level</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The criticality level. An integer specified by the user. The higher it is, the more critical is the Data quality rule. Suggested values are between 1 and 10, but they are not restricted to this range.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Criticality Level</em>' attribute.
     * @see #setCriticalityLevel(int)
     * @see org.talend.dataquality.rules.RulesPackage#getDQRule_CriticalityLevel()
     * @model
     * @generated
     */
    int getCriticalityLevel();

    /**
     * Sets the value of the '{@link org.talend.dataquality.rules.DQRule#getCriticalityLevel <em>Criticality Level</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Criticality Level</em>' attribute.
     * @see #getCriticalityLevel()
     * @generated
     */
    void setCriticalityLevel(int value);

    /**
     * Returns the value of the '<em><b>Elements</b></em>' reference list.
     * The list contents are of type {@link orgomg.cwm.objectmodel.core.ModelElement}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * the elements to which the DQ rule applies.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Elements</em>' reference list.
     * @see org.talend.dataquality.rules.RulesPackage#getDQRule_Elements()
     * @model
     * @generated
     */
    EList<ModelElement> getElements();

} // DQRule
