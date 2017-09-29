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

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.talend.dataquality.rules.RulesPackage
 * @generated
 */
public interface RulesFactory extends EFactory {
    /**
     * The singleton instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    RulesFactory eINSTANCE = org.talend.dataquality.rules.impl.RulesFactoryImpl.init();

    /**
     * Returns a new object of class '<em>DQ Rule</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>DQ Rule</em>'.
     * @generated
     */
    DQRule createDQRule();

    /**
     * Returns a new object of class '<em>Specified DQ Rule</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Specified DQ Rule</em>'.
     * @generated
     */
    SpecifiedDQRule createSpecifiedDQRule();

    /**
     * Returns a new object of class '<em>Inferred DQ Rule</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Inferred DQ Rule</em>'.
     * @generated
     */
    InferredDQRule createInferredDQRule();

    /**
     * Returns a new object of class '<em>Where Rule</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Where Rule</em>'.
     * @generated
     */
    WhereRule createWhereRule();

    /**
     * Returns a new object of class '<em>Join Element</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Join Element</em>'.
     * @generated
     */
    JoinElement createJoinElement();

    /**
     * Returns a new object of class '<em>Parser Rule</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Parser Rule</em>'.
     * @generated
     */
    ParserRule createParserRule();

    /**
     * Returns a new object of class '<em>Match Rule Definition</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Match Rule Definition</em>'.
     * @generated
     */
    MatchRuleDefinition createMatchRuleDefinition();

    /**
     * Returns a new object of class '<em>Block Key Definition</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Block Key Definition</em>'.
     * @generated
     */
    BlockKeyDefinition createBlockKeyDefinition();

    /**
     * Returns a new object of class '<em>Key Definition</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Key Definition</em>'.
     * @generated
     */
    KeyDefinition createKeyDefinition();

    /**
     * Returns a new object of class '<em>Match Key Definition</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Match Key Definition</em>'.
     * @generated
     */
    MatchKeyDefinition createMatchKeyDefinition();

    /**
     * Returns a new object of class '<em>Algorithm Definition</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Algorithm Definition</em>'.
     * @generated
     */
    AlgorithmDefinition createAlgorithmDefinition();

    /**
     * Returns a new object of class '<em>Match Rule</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Match Rule</em>'.
     * @generated
     */
    MatchRule createMatchRule();

    /**
     * Returns a new object of class '<em>Applied Block Key</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Applied Block Key</em>'.
     * @generated
     */
    AppliedBlockKey createAppliedBlockKey();

    /**
     * Returns a new object of class '<em>Survivorship Key Definition</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Survivorship Key Definition</em>'.
     * @generated
     */
    SurvivorshipKeyDefinition createSurvivorshipKeyDefinition();

    /**
     * Returns a new object of class '<em>Default Survivorship Definition</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Default Survivorship Definition</em>'.
     * @generated
     */
    DefaultSurvivorshipDefinition createDefaultSurvivorshipDefinition();

    /**
     * Returns a new object of class '<em>Particular Default Survivorship Definitions</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Particular Default Survivorship Definitions</em>'.
     * @generated
     */
    ParticularDefaultSurvivorshipDefinitions createParticularDefaultSurvivorshipDefinitions();

    /**
     * Returns the package supported by this factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the package supported by this factory.
     * @generated
     */
    RulesPackage getRulesPackage();

} //RulesFactory
