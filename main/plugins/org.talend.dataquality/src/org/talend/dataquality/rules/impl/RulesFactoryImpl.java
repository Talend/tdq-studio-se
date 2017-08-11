/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.talend.dataquality.rules.impl;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.talend.cwm.relational.TdExpression;
import org.talend.dataquality.rules.*;
import org.talend.dataquality.rules.AlgorithmDefinition;
import org.talend.dataquality.rules.AppliedBlockKey;
import org.talend.dataquality.rules.BlockKeyDefinition;
import org.talend.dataquality.rules.DQRule;
import org.talend.dataquality.rules.DefaultSurvivorshipDefinition;
import org.talend.dataquality.rules.InferredDQRule;
import org.talend.dataquality.rules.JoinElement;
import org.talend.dataquality.rules.KeyDefinition;
import org.talend.dataquality.rules.MatchKeyDefinition;
import org.talend.dataquality.rules.MatchRule;
import org.talend.dataquality.rules.MatchRuleDefinition;
import org.talend.dataquality.rules.ParserRule;
import org.talend.dataquality.rules.RulesFactory;
import org.talend.dataquality.rules.RulesPackage;
import org.talend.dataquality.rules.SpecifiedDQRule;
import org.talend.dataquality.rules.SurvivorshipKeyDefinition;
import org.talend.dataquality.rules.WhereRule;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class RulesFactoryImpl extends EFactoryImpl implements RulesFactory {
    /**
     * Creates the default factory implementation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static RulesFactory init() {
        try {
            RulesFactory theRulesFactory = (RulesFactory)EPackage.Registry.INSTANCE.getEFactory(RulesPackage.eNS_URI);
            if (theRulesFactory != null) {
                return theRulesFactory;
            }
        }
        catch (Exception exception) {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new RulesFactoryImpl();
    }

    /**
     * Creates an instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RulesFactoryImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EObject create(EClass eClass) {
        switch (eClass.getClassifierID()) {
            case RulesPackage.DQ_RULE: return createDQRule();
            case RulesPackage.SPECIFIED_DQ_RULE: return createSpecifiedDQRule();
            case RulesPackage.INFERRED_DQ_RULE: return createInferredDQRule();
            case RulesPackage.WHERE_RULE: return createWhereRule();
            case RulesPackage.JOIN_ELEMENT: return createJoinElement();
            case RulesPackage.PARSER_RULE: return createParserRule();
            case RulesPackage.MATCH_RULE_DEFINITION: return createMatchRuleDefinition();
            case RulesPackage.BLOCK_KEY_DEFINITION: return createBlockKeyDefinition();
            case RulesPackage.KEY_DEFINITION: return createKeyDefinition();
            case RulesPackage.MATCH_KEY_DEFINITION: return createMatchKeyDefinition();
            case RulesPackage.ALGORITHM_DEFINITION: return createAlgorithmDefinition();
            case RulesPackage.MATCH_RULE: return createMatchRule();
            case RulesPackage.APPLIED_BLOCK_KEY: return createAppliedBlockKey();
            case RulesPackage.SURVIVORSHIP_KEY_DEFINITION: return createSurvivorshipKeyDefinition();
            case RulesPackage.DEFAULT_SURVIVORSHIP_DEFINITION: return createDefaultSurvivorshipDefinition();
            case RulesPackage.PARTICULAR_DEFAULT_SURVIVORSHIP_DEFINITIONS: return createParticularDefaultSurvivorshipDefinitions();
            default:
                throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object createFromString(EDataType eDataType, String initialValue) {
        switch (eDataType.getClassifierID()) {
            case RulesPackage.TD_EXPRESSION_LIST:
                return createTdExpressionListFromString(eDataType, initialValue);
            default:
                throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String convertToString(EDataType eDataType, Object instanceValue) {
        switch (eDataType.getClassifierID()) {
            case RulesPackage.TD_EXPRESSION_LIST:
                return convertTdExpressionListToString(eDataType, instanceValue);
            default:
                throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DQRule createDQRule() {
        DQRuleImpl dqRule = new DQRuleImpl();
        return dqRule;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SpecifiedDQRule createSpecifiedDQRule() {
        SpecifiedDQRuleImpl specifiedDQRule = new SpecifiedDQRuleImpl();
        return specifiedDQRule;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public InferredDQRule createInferredDQRule() {
        InferredDQRuleImpl inferredDQRule = new InferredDQRuleImpl();
        return inferredDQRule;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public WhereRule createWhereRule() {
        WhereRuleImpl whereRule = new WhereRuleImpl();
        return whereRule;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public JoinElement createJoinElement() {
        JoinElementImpl joinElement = new JoinElementImpl();
        return joinElement;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ParserRule createParserRule() {
        ParserRuleImpl parserRule = new ParserRuleImpl();
        return parserRule;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public MatchRuleDefinition createMatchRuleDefinition() {
        MatchRuleDefinitionImpl matchRuleDefinition = new MatchRuleDefinitionImpl();
        return matchRuleDefinition;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BlockKeyDefinition createBlockKeyDefinition() {
        BlockKeyDefinitionImpl blockKeyDefinition = new BlockKeyDefinitionImpl();
        return blockKeyDefinition;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public KeyDefinition createKeyDefinition() {
        KeyDefinitionImpl keyDefinition = new KeyDefinitionImpl();
        return keyDefinition;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public MatchKeyDefinition createMatchKeyDefinition() {
        MatchKeyDefinitionImpl matchKeyDefinition = new MatchKeyDefinitionImpl();
        return matchKeyDefinition;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AlgorithmDefinition createAlgorithmDefinition() {
        AlgorithmDefinitionImpl algorithmDefinition = new AlgorithmDefinitionImpl();
        return algorithmDefinition;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public MatchRule createMatchRule() {
        MatchRuleImpl matchRule = new MatchRuleImpl();
        return matchRule;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AppliedBlockKey createAppliedBlockKey() {
        AppliedBlockKeyImpl appliedBlockKey = new AppliedBlockKeyImpl();
        return appliedBlockKey;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SurvivorshipKeyDefinition createSurvivorshipKeyDefinition() {
        SurvivorshipKeyDefinitionImpl survivorshipKeyDefinition = new SurvivorshipKeyDefinitionImpl();
        return survivorshipKeyDefinition;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DefaultSurvivorshipDefinition createDefaultSurvivorshipDefinition() {
        DefaultSurvivorshipDefinitionImpl defaultSurvivorshipDefinition = new DefaultSurvivorshipDefinitionImpl();
        return defaultSurvivorshipDefinition;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ParticularDefaultSurvivorshipDefinitions createParticularDefaultSurvivorshipDefinitions() {
        ParticularDefaultSurvivorshipDefinitionsImpl particularDefaultSurvivorshipDefinitions = new ParticularDefaultSurvivorshipDefinitionsImpl();
        return particularDefaultSurvivorshipDefinitions;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    public List<TdExpression> createTdExpressionListFromString(EDataType eDataType, String initialValue) {
        return (List<TdExpression>)super.createFromString(initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertTdExpressionListToString(EDataType eDataType, Object instanceValue) {
        return super.convertToString(instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RulesPackage getRulesPackage() {
        return (RulesPackage)getEPackage();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @deprecated
     * @generated
     */
    @Deprecated
    public static RulesPackage getPackage() {
        return RulesPackage.eINSTANCE;
    }

} //RulesFactoryImpl
