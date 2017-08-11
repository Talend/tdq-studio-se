/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.talend.dataquality.rules.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;
import org.talend.dataquality.indicators.definition.IndicatorDefinition;
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
import org.talend.dataquality.rules.RulesPackage;
import org.talend.dataquality.rules.SpecifiedDQRule;
import org.talend.dataquality.rules.SurvivorshipKeyDefinition;
import org.talend.dataquality.rules.WhereRule;
import orgomg.cwm.objectmodel.core.Element;
import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see org.talend.dataquality.rules.RulesPackage
 * @generated
 */
public class RulesAdapterFactory extends AdapterFactoryImpl {
    /**
     * The cached model package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected static RulesPackage modelPackage;

    /**
     * Creates an instance of the adapter factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RulesAdapterFactory() {
        if (modelPackage == null) {
            modelPackage = RulesPackage.eINSTANCE;
        }
    }

    /**
     * Returns whether this factory is applicable for the type of the object.
     * <!-- begin-user-doc -->
     * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
     * <!-- end-user-doc -->
     * @return whether this factory is applicable for the type of the object.
     * @generated
     */
    @Override
    public boolean isFactoryForType(Object object) {
        if (object == modelPackage) {
            return true;
        }
        if (object instanceof EObject) {
            return ((EObject)object).eClass().getEPackage() == modelPackage;
        }
        return false;
    }

    /**
     * The switch that delegates to the <code>createXXX</code> methods.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected RulesSwitch<Adapter> modelSwitch =
        new RulesSwitch<Adapter>() {
            @Override
            public Adapter caseDQRule(DQRule object) {
                return createDQRuleAdapter();
            }
            @Override
            public Adapter caseSpecifiedDQRule(SpecifiedDQRule object) {
                return createSpecifiedDQRuleAdapter();
            }
            @Override
            public Adapter caseInferredDQRule(InferredDQRule object) {
                return createInferredDQRuleAdapter();
            }
            @Override
            public Adapter caseWhereRule(WhereRule object) {
                return createWhereRuleAdapter();
            }
            @Override
            public Adapter caseJoinElement(JoinElement object) {
                return createJoinElementAdapter();
            }
            @Override
            public Adapter caseParserRule(ParserRule object) {
                return createParserRuleAdapter();
            }
            @Override
            public Adapter caseMatchRuleDefinition(MatchRuleDefinition object) {
                return createMatchRuleDefinitionAdapter();
            }
            @Override
            public Adapter caseBlockKeyDefinition(BlockKeyDefinition object) {
                return createBlockKeyDefinitionAdapter();
            }
            @Override
            public Adapter caseKeyDefinition(KeyDefinition object) {
                return createKeyDefinitionAdapter();
            }
            @Override
            public Adapter caseMatchKeyDefinition(MatchKeyDefinition object) {
                return createMatchKeyDefinitionAdapter();
            }
            @Override
            public Adapter caseAlgorithmDefinition(AlgorithmDefinition object) {
                return createAlgorithmDefinitionAdapter();
            }
            @Override
            public Adapter caseMatchRule(MatchRule object) {
                return createMatchRuleAdapter();
            }
            @Override
            public Adapter caseAppliedBlockKey(AppliedBlockKey object) {
                return createAppliedBlockKeyAdapter();
            }
            @Override
            public Adapter caseSurvivorshipKeyDefinition(SurvivorshipKeyDefinition object) {
                return createSurvivorshipKeyDefinitionAdapter();
            }
            @Override
            public Adapter caseDefaultSurvivorshipDefinition(DefaultSurvivorshipDefinition object) {
                return createDefaultSurvivorshipDefinitionAdapter();
            }
            @Override
            public Adapter caseParticularDefaultSurvivorshipDefinitions(ParticularDefaultSurvivorshipDefinitions object) {
                return createParticularDefaultSurvivorshipDefinitionsAdapter();
            }
            @Override
            public Adapter caseElement(Element object) {
                return createElementAdapter();
            }
            @Override
            public Adapter caseModelElement(ModelElement object) {
                return createModelElementAdapter();
            }
            @Override
            public Adapter caseIndicatorDefinition(IndicatorDefinition object) {
                return createIndicatorDefinitionAdapter();
            }
            @Override
            public Adapter defaultCase(EObject object) {
                return createEObjectAdapter();
            }
        };

    /**
     * Creates an adapter for the <code>target</code>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param target the object to adapt.
     * @return the adapter for the <code>target</code>.
     * @generated
     */
    @Override
    public Adapter createAdapter(Notifier target) {
        return modelSwitch.doSwitch((EObject)target);
    }


    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.rules.DQRule <em>DQ Rule</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.rules.DQRule
     * @generated
     */
    public Adapter createDQRuleAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.rules.SpecifiedDQRule <em>Specified DQ Rule</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.rules.SpecifiedDQRule
     * @generated
     */
    public Adapter createSpecifiedDQRuleAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.rules.InferredDQRule <em>Inferred DQ Rule</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.rules.InferredDQRule
     * @generated
     */
    public Adapter createInferredDQRuleAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.rules.WhereRule <em>Where Rule</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.rules.WhereRule
     * @generated
     */
    public Adapter createWhereRuleAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.rules.JoinElement <em>Join Element</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.rules.JoinElement
     * @generated
     */
    public Adapter createJoinElementAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.rules.ParserRule <em>Parser Rule</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.rules.ParserRule
     * @generated
     */
    public Adapter createParserRuleAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.rules.MatchRuleDefinition <em>Match Rule Definition</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.rules.MatchRuleDefinition
     * @generated
     */
    public Adapter createMatchRuleDefinitionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.rules.BlockKeyDefinition <em>Block Key Definition</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.rules.BlockKeyDefinition
     * @generated
     */
    public Adapter createBlockKeyDefinitionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.rules.KeyDefinition <em>Key Definition</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.rules.KeyDefinition
     * @generated
     */
    public Adapter createKeyDefinitionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.rules.MatchKeyDefinition <em>Match Key Definition</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.rules.MatchKeyDefinition
     * @generated
     */
    public Adapter createMatchKeyDefinitionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.rules.AlgorithmDefinition <em>Algorithm Definition</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.rules.AlgorithmDefinition
     * @generated
     */
    public Adapter createAlgorithmDefinitionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.rules.MatchRule <em>Match Rule</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.rules.MatchRule
     * @generated
     */
    public Adapter createMatchRuleAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.rules.AppliedBlockKey <em>Applied Block Key</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.rules.AppliedBlockKey
     * @generated
     */
    public Adapter createAppliedBlockKeyAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.rules.SurvivorshipKeyDefinition <em>Survivorship Key Definition</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.rules.SurvivorshipKeyDefinition
     * @generated
     */
    public Adapter createSurvivorshipKeyDefinitionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.rules.DefaultSurvivorshipDefinition <em>Default Survivorship Definition</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.rules.DefaultSurvivorshipDefinition
     * @generated
     */
    public Adapter createDefaultSurvivorshipDefinitionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.rules.ParticularDefaultSurvivorshipDefinitions <em>Particular Default Survivorship Definitions</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.rules.ParticularDefaultSurvivorshipDefinitions
     * @generated
     */
    public Adapter createParticularDefaultSurvivorshipDefinitionsAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link orgomg.cwm.objectmodel.core.Element <em>Element</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see orgomg.cwm.objectmodel.core.Element
     * @generated
     */
    public Adapter createElementAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link orgomg.cwm.objectmodel.core.ModelElement <em>Model Element</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see orgomg.cwm.objectmodel.core.ModelElement
     * @generated
     */
    public Adapter createModelElementAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.talend.dataquality.indicators.definition.IndicatorDefinition <em>Indicator Definition</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.talend.dataquality.indicators.definition.IndicatorDefinition
     * @generated
     */
    public Adapter createIndicatorDefinitionAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for the default case.
     * <!-- begin-user-doc -->
     * This default implementation returns null.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @generated
     */
    public Adapter createEObjectAdapter() {
        return null;
    }

} //RulesAdapterFactory
