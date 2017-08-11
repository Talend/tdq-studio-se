/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.talend.dataquality.rules.impl;

import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.eclipse.emf.ecore.xml.type.XMLTypePackage;
import org.talend.core.model.properties.PropertiesPackage;
import org.talend.cwm.relational.RelationalPackage;
import org.talend.dataquality.analysis.AnalysisPackage;
import org.talend.dataquality.analysis.category.CategoryPackage;
import org.talend.dataquality.analysis.category.impl.CategoryPackageImpl;
import org.talend.dataquality.analysis.impl.AnalysisPackageImpl;
import org.talend.dataquality.domain.DomainPackage;
import org.talend.dataquality.domain.impl.DomainPackageImpl;
import org.talend.dataquality.domain.pattern.PatternPackage;
import org.talend.dataquality.domain.pattern.impl.PatternPackageImpl;
import org.talend.dataquality.domain.sql.SQLPackage;
import org.talend.dataquality.domain.sql.impl.SQLPackageImpl;
import org.talend.dataquality.expressions.ExpressionsPackage;
import org.talend.dataquality.expressions.impl.ExpressionsPackageImpl;
import org.talend.dataquality.indicators.IndicatorsPackage;
import org.talend.dataquality.indicators.columnset.ColumnsetPackage;
import org.talend.dataquality.indicators.columnset.impl.ColumnsetPackageImpl;
import org.talend.dataquality.indicators.definition.DefinitionPackage;
import org.talend.dataquality.indicators.definition.impl.DefinitionPackageImpl;
import org.talend.dataquality.indicators.definition.userdefine.UserdefinePackage;
import org.talend.dataquality.indicators.definition.userdefine.impl.UserdefinePackageImpl;
import org.talend.dataquality.indicators.impl.IndicatorsPackageImpl;
import org.talend.dataquality.indicators.schema.SchemaPackage;
import org.talend.dataquality.indicators.schema.impl.SchemaPackageImpl;
import org.talend.dataquality.indicators.sql.IndicatorSqlPackage;
import org.talend.dataquality.indicators.sql.impl.IndicatorSqlPackageImpl;
import org.talend.dataquality.properties.impl.PropertiesPackageImpl;
import org.talend.dataquality.reports.ReportsPackage;
import org.talend.dataquality.reports.impl.ReportsPackageImpl;
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
import org.talend.dataquality.rules.ParticularDefaultSurvivorshipDefinitions;
import org.talend.dataquality.rules.RulesFactory;
import org.talend.dataquality.rules.RulesPackage;
import org.talend.dataquality.rules.SpecifiedDQRule;
import org.talend.dataquality.rules.SurvivorshipKeyDefinition;
import org.talend.dataquality.rules.WhereRule;
import orgomg.cwm.objectmodel.core.CorePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class RulesPackageImpl extends EPackageImpl implements RulesPackage {
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass dqRuleEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass specifiedDQRuleEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass inferredDQRuleEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass whereRuleEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass joinElementEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass parserRuleEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass matchRuleDefinitionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass blockKeyDefinitionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass keyDefinitionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass matchKeyDefinitionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass algorithmDefinitionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass matchRuleEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass appliedBlockKeyEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass survivorshipKeyDefinitionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass defaultSurvivorshipDefinitionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass particularDefaultSurvivorshipDefinitionsEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType tdExpressionListEDataType = null;

    /**
     * Creates an instance of the model <b>Package</b>, registered with
     * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
     * package URI value.
     * <p>Note: the correct way to create the package is via the static
     * factory method {@link #init init()}, which also performs
     * initialization of the package, or returns the registered package,
     * if one already exists.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.emf.ecore.EPackage.Registry
     * @see org.talend.dataquality.rules.RulesPackage#eNS_URI
     * @see #init()
     * @generated
     */
    private RulesPackageImpl() {
        super(eNS_URI, RulesFactory.eINSTANCE);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private static boolean isInited = false;

    /**
     * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
     * 
     * <p>This method is used to initialize {@link RulesPackage#eINSTANCE} when that field is accessed.
     * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #eNS_URI
     * @see #createPackageContents()
     * @see #initializePackageContents()
     * @generated
     */
    public static RulesPackage init() {
        if (isInited) return (RulesPackage)EPackage.Registry.INSTANCE.getEPackage(RulesPackage.eNS_URI);

        // Obtain or create and register package
        RulesPackageImpl theRulesPackage = (RulesPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof RulesPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new RulesPackageImpl());

        isInited = true;

        // Initialize simple dependencies
        PropertiesPackage.eINSTANCE.eClass();
        XMLTypePackage.eINSTANCE.eClass();

        // Obtain or create and register interdependencies
        AnalysisPackageImpl theAnalysisPackage = (AnalysisPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(AnalysisPackage.eNS_URI) instanceof AnalysisPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(AnalysisPackage.eNS_URI) : AnalysisPackage.eINSTANCE);
        CategoryPackageImpl theCategoryPackage = (CategoryPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(CategoryPackage.eNS_URI) instanceof CategoryPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(CategoryPackage.eNS_URI) : CategoryPackage.eINSTANCE);
        ReportsPackageImpl theReportsPackage = (ReportsPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(ReportsPackage.eNS_URI) instanceof ReportsPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(ReportsPackage.eNS_URI) : ReportsPackage.eINSTANCE);
        IndicatorsPackageImpl theIndicatorsPackage = (IndicatorsPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(IndicatorsPackage.eNS_URI) instanceof IndicatorsPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(IndicatorsPackage.eNS_URI) : IndicatorsPackage.eINSTANCE);
        SchemaPackageImpl theSchemaPackage = (SchemaPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(SchemaPackage.eNS_URI) instanceof SchemaPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(SchemaPackage.eNS_URI) : SchemaPackage.eINSTANCE);
        DefinitionPackageImpl theDefinitionPackage = (DefinitionPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(DefinitionPackage.eNS_URI) instanceof DefinitionPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(DefinitionPackage.eNS_URI) : DefinitionPackage.eINSTANCE);
        UserdefinePackageImpl theUserdefinePackage = (UserdefinePackageImpl)(EPackage.Registry.INSTANCE.getEPackage(UserdefinePackage.eNS_URI) instanceof UserdefinePackageImpl ? EPackage.Registry.INSTANCE.getEPackage(UserdefinePackage.eNS_URI) : UserdefinePackage.eINSTANCE);
        IndicatorSqlPackageImpl theIndicatorSqlPackage = (IndicatorSqlPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(IndicatorSqlPackage.eNS_URI) instanceof IndicatorSqlPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(IndicatorSqlPackage.eNS_URI) : IndicatorSqlPackage.eINSTANCE);
        ColumnsetPackageImpl theColumnsetPackage = (ColumnsetPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(ColumnsetPackage.eNS_URI) instanceof ColumnsetPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(ColumnsetPackage.eNS_URI) : ColumnsetPackage.eINSTANCE);
        ExpressionsPackageImpl theExpressionsPackage = (ExpressionsPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(ExpressionsPackage.eNS_URI) instanceof ExpressionsPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(ExpressionsPackage.eNS_URI) : ExpressionsPackage.eINSTANCE);
        DomainPackageImpl theDomainPackage = (DomainPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(DomainPackage.eNS_URI) instanceof DomainPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(DomainPackage.eNS_URI) : DomainPackage.eINSTANCE);
        PatternPackageImpl thePatternPackage = (PatternPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(PatternPackage.eNS_URI) instanceof PatternPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(PatternPackage.eNS_URI) : PatternPackage.eINSTANCE);
        SQLPackageImpl theSQLPackage = (SQLPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(SQLPackage.eNS_URI) instanceof SQLPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(SQLPackage.eNS_URI) : SQLPackage.eINSTANCE);
        PropertiesPackageImpl thePropertiesPackage_1 = (PropertiesPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(org.talend.dataquality.properties.PropertiesPackage.eNS_URI) instanceof PropertiesPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(org.talend.dataquality.properties.PropertiesPackage.eNS_URI) : org.talend.dataquality.properties.PropertiesPackage.eINSTANCE);

        // Create package meta-data objects
        theRulesPackage.createPackageContents();
        theAnalysisPackage.createPackageContents();
        theCategoryPackage.createPackageContents();
        theReportsPackage.createPackageContents();
        theIndicatorsPackage.createPackageContents();
        theSchemaPackage.createPackageContents();
        theDefinitionPackage.createPackageContents();
        theUserdefinePackage.createPackageContents();
        theIndicatorSqlPackage.createPackageContents();
        theColumnsetPackage.createPackageContents();
        theExpressionsPackage.createPackageContents();
        theDomainPackage.createPackageContents();
        thePatternPackage.createPackageContents();
        theSQLPackage.createPackageContents();
        thePropertiesPackage_1.createPackageContents();

        // Initialize created meta-data
        theRulesPackage.initializePackageContents();
        theAnalysisPackage.initializePackageContents();
        theCategoryPackage.initializePackageContents();
        theReportsPackage.initializePackageContents();
        theIndicatorsPackage.initializePackageContents();
        theSchemaPackage.initializePackageContents();
        theDefinitionPackage.initializePackageContents();
        theUserdefinePackage.initializePackageContents();
        theIndicatorSqlPackage.initializePackageContents();
        theColumnsetPackage.initializePackageContents();
        theExpressionsPackage.initializePackageContents();
        theDomainPackage.initializePackageContents();
        thePatternPackage.initializePackageContents();
        theSQLPackage.initializePackageContents();
        thePropertiesPackage_1.initializePackageContents();

        // Mark meta-data to indicate it can't be changed
        theRulesPackage.freeze();

  
        // Update the registry and return the package
        EPackage.Registry.INSTANCE.put(RulesPackage.eNS_URI, theRulesPackage);
        return theRulesPackage;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDQRule() {
        return dqRuleEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDQRule_CriticalityLevel() {
        return (EAttribute)dqRuleEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDQRule_Elements() {
        return (EReference)dqRuleEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getSpecifiedDQRule() {
        return specifiedDQRuleEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getInferredDQRule() {
        return inferredDQRuleEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getWhereRule() {
        return whereRuleEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getWhereRule_WhereExpression() {
        return (EAttribute)whereRuleEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getWhereRule_JoinExpression() {
        return (EAttribute)whereRuleEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getWhereRule_Joins() {
        return (EReference)whereRuleEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getJoinElement() {
        return joinElementEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getJoinElement_ColA() {
        return (EReference)joinElementEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getJoinElement_ColB() {
        return (EReference)joinElementEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getJoinElement_Operator() {
        return (EAttribute)joinElementEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getJoinElement_TableAliasA() {
        return (EAttribute)joinElementEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getJoinElement_TableAliasB() {
        return (EAttribute)joinElementEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getJoinElement_ColumnAliasA() {
        return (EAttribute)joinElementEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getJoinElement_ColumnAliasB() {
        return (EAttribute)joinElementEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getParserRule() {
        return parserRuleEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getMatchRuleDefinition() {
        return matchRuleDefinitionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getMatchRuleDefinition_BlockKeys() {
        return (EReference)matchRuleDefinitionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getMatchRuleDefinition_MatchRules() {
        return (EReference)matchRuleDefinitionEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMatchRuleDefinition_XmiId() {
        return (EAttribute)matchRuleDefinitionEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMatchRuleDefinition_RecordLinkageAlgorithm() {
        return (EAttribute)matchRuleDefinitionEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getMatchRuleDefinition_AppliedBlockKeys() {
        return (EReference)matchRuleDefinitionEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getMatchRuleDefinition_SurvivorshipKeys() {
        return (EReference)matchRuleDefinitionEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getMatchRuleDefinition_DefaultSurvivorshipDefinitions() {
        return (EReference)matchRuleDefinitionEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMatchRuleDefinition_MatchGroupQualityThreshold() {
        return (EAttribute)matchRuleDefinitionEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getMatchRuleDefinition_ParticularDefaultSurvivorshipDefinitions() {
        return (EReference)matchRuleDefinitionEClass.getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getBlockKeyDefinition() {
        return blockKeyDefinitionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getBlockKeyDefinition_PreAlgorithm() {
        return (EReference)blockKeyDefinitionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getBlockKeyDefinition_Algorithm() {
        return (EReference)blockKeyDefinitionEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getBlockKeyDefinition_PostAlgorithm() {
        return (EReference)blockKeyDefinitionEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getKeyDefinition() {
        return keyDefinitionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getKeyDefinition_Column() {
        return (EAttribute)keyDefinitionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getMatchKeyDefinition() {
        return matchKeyDefinitionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getMatchKeyDefinition_Algorithm() {
        return (EReference)matchKeyDefinitionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMatchKeyDefinition_ConfidenceWeight() {
        return (EAttribute)matchKeyDefinitionEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMatchKeyDefinition_HandleNull() {
        return (EAttribute)matchKeyDefinitionEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMatchKeyDefinition_Threshold() {
        return (EAttribute)matchKeyDefinitionEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMatchKeyDefinition_TokenizationType() {
        return (EAttribute)matchKeyDefinitionEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAlgorithmDefinition() {
        return algorithmDefinitionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAlgorithmDefinition_AlgorithmType() {
        return (EAttribute)algorithmDefinitionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getAlgorithmDefinition_AlgorithmParameters() {
        return (EAttribute)algorithmDefinitionEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getMatchRule() {
        return matchRuleEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getMatchRule_MatchKeys() {
        return (EReference)matchRuleEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getMatchRule_MatchInterval() {
        return (EAttribute)matchRuleEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAppliedBlockKey() {
        return appliedBlockKeyEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getSurvivorshipKeyDefinition() {
        return survivorshipKeyDefinitionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getSurvivorshipKeyDefinition_Function() {
        return (EReference)survivorshipKeyDefinitionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getSurvivorshipKeyDefinition_AllowManualResolution() {
        return (EAttribute)survivorshipKeyDefinitionEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getDefaultSurvivorshipDefinition() {
        return defaultSurvivorshipDefinitionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getDefaultSurvivorshipDefinition_DataType() {
        return (EAttribute)defaultSurvivorshipDefinitionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getDefaultSurvivorshipDefinition_Function() {
        return (EReference)defaultSurvivorshipDefinitionEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getParticularDefaultSurvivorshipDefinitions() {
        return particularDefaultSurvivorshipDefinitionsEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getTdExpressionList() {
        return tdExpressionListEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RulesFactory getRulesFactory() {
        return (RulesFactory)getEFactoryInstance();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private boolean isCreated = false;

    /**
     * Creates the meta-model objects for the package.  This method is
     * guarded to have no affect on any invocation but its first.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void createPackageContents() {
        if (isCreated) return;
        isCreated = true;

        // Create classes and their features
        dqRuleEClass = createEClass(DQ_RULE);
        createEAttribute(dqRuleEClass, DQ_RULE__CRITICALITY_LEVEL);
        createEReference(dqRuleEClass, DQ_RULE__ELEMENTS);

        specifiedDQRuleEClass = createEClass(SPECIFIED_DQ_RULE);

        inferredDQRuleEClass = createEClass(INFERRED_DQ_RULE);

        whereRuleEClass = createEClass(WHERE_RULE);
        createEAttribute(whereRuleEClass, WHERE_RULE__WHERE_EXPRESSION);
        createEAttribute(whereRuleEClass, WHERE_RULE__JOIN_EXPRESSION);
        createEReference(whereRuleEClass, WHERE_RULE__JOINS);

        joinElementEClass = createEClass(JOIN_ELEMENT);
        createEReference(joinElementEClass, JOIN_ELEMENT__COL_A);
        createEReference(joinElementEClass, JOIN_ELEMENT__COL_B);
        createEAttribute(joinElementEClass, JOIN_ELEMENT__OPERATOR);
        createEAttribute(joinElementEClass, JOIN_ELEMENT__TABLE_ALIAS_A);
        createEAttribute(joinElementEClass, JOIN_ELEMENT__TABLE_ALIAS_B);
        createEAttribute(joinElementEClass, JOIN_ELEMENT__COLUMN_ALIAS_A);
        createEAttribute(joinElementEClass, JOIN_ELEMENT__COLUMN_ALIAS_B);

        parserRuleEClass = createEClass(PARSER_RULE);

        matchRuleDefinitionEClass = createEClass(MATCH_RULE_DEFINITION);
        createEReference(matchRuleDefinitionEClass, MATCH_RULE_DEFINITION__BLOCK_KEYS);
        createEReference(matchRuleDefinitionEClass, MATCH_RULE_DEFINITION__MATCH_RULES);
        createEAttribute(matchRuleDefinitionEClass, MATCH_RULE_DEFINITION__XMI_ID);
        createEAttribute(matchRuleDefinitionEClass, MATCH_RULE_DEFINITION__RECORD_LINKAGE_ALGORITHM);
        createEReference(matchRuleDefinitionEClass, MATCH_RULE_DEFINITION__APPLIED_BLOCK_KEYS);
        createEReference(matchRuleDefinitionEClass, MATCH_RULE_DEFINITION__SURVIVORSHIP_KEYS);
        createEReference(matchRuleDefinitionEClass, MATCH_RULE_DEFINITION__DEFAULT_SURVIVORSHIP_DEFINITIONS);
        createEAttribute(matchRuleDefinitionEClass, MATCH_RULE_DEFINITION__MATCH_GROUP_QUALITY_THRESHOLD);
        createEReference(matchRuleDefinitionEClass, MATCH_RULE_DEFINITION__PARTICULAR_DEFAULT_SURVIVORSHIP_DEFINITIONS);

        blockKeyDefinitionEClass = createEClass(BLOCK_KEY_DEFINITION);
        createEReference(blockKeyDefinitionEClass, BLOCK_KEY_DEFINITION__PRE_ALGORITHM);
        createEReference(blockKeyDefinitionEClass, BLOCK_KEY_DEFINITION__ALGORITHM);
        createEReference(blockKeyDefinitionEClass, BLOCK_KEY_DEFINITION__POST_ALGORITHM);

        keyDefinitionEClass = createEClass(KEY_DEFINITION);
        createEAttribute(keyDefinitionEClass, KEY_DEFINITION__COLUMN);

        matchKeyDefinitionEClass = createEClass(MATCH_KEY_DEFINITION);
        createEReference(matchKeyDefinitionEClass, MATCH_KEY_DEFINITION__ALGORITHM);
        createEAttribute(matchKeyDefinitionEClass, MATCH_KEY_DEFINITION__CONFIDENCE_WEIGHT);
        createEAttribute(matchKeyDefinitionEClass, MATCH_KEY_DEFINITION__HANDLE_NULL);
        createEAttribute(matchKeyDefinitionEClass, MATCH_KEY_DEFINITION__THRESHOLD);
        createEAttribute(matchKeyDefinitionEClass, MATCH_KEY_DEFINITION__TOKENIZATION_TYPE);

        algorithmDefinitionEClass = createEClass(ALGORITHM_DEFINITION);
        createEAttribute(algorithmDefinitionEClass, ALGORITHM_DEFINITION__ALGORITHM_TYPE);
        createEAttribute(algorithmDefinitionEClass, ALGORITHM_DEFINITION__ALGORITHM_PARAMETERS);

        matchRuleEClass = createEClass(MATCH_RULE);
        createEReference(matchRuleEClass, MATCH_RULE__MATCH_KEYS);
        createEAttribute(matchRuleEClass, MATCH_RULE__MATCH_INTERVAL);

        appliedBlockKeyEClass = createEClass(APPLIED_BLOCK_KEY);

        survivorshipKeyDefinitionEClass = createEClass(SURVIVORSHIP_KEY_DEFINITION);
        createEReference(survivorshipKeyDefinitionEClass, SURVIVORSHIP_KEY_DEFINITION__FUNCTION);
        createEAttribute(survivorshipKeyDefinitionEClass, SURVIVORSHIP_KEY_DEFINITION__ALLOW_MANUAL_RESOLUTION);

        defaultSurvivorshipDefinitionEClass = createEClass(DEFAULT_SURVIVORSHIP_DEFINITION);
        createEAttribute(defaultSurvivorshipDefinitionEClass, DEFAULT_SURVIVORSHIP_DEFINITION__DATA_TYPE);
        createEReference(defaultSurvivorshipDefinitionEClass, DEFAULT_SURVIVORSHIP_DEFINITION__FUNCTION);

        particularDefaultSurvivorshipDefinitionsEClass = createEClass(PARTICULAR_DEFAULT_SURVIVORSHIP_DEFINITIONS);

        // Create data types
        tdExpressionListEDataType = createEDataType(TD_EXPRESSION_LIST);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private boolean isInitialized = false;

    /**
     * Complete the initialization of the package and its meta-model.  This
     * method is guarded to have no affect on any invocation but its first.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void initializePackageContents() {
        if (isInitialized) return;
        isInitialized = true;

        // Initialize package
        setName(eNAME);
        setNsPrefix(eNS_PREFIX);
        setNsURI(eNS_URI);

        // Obtain other dependent packages
        DefinitionPackage theDefinitionPackage = (DefinitionPackage)EPackage.Registry.INSTANCE.getEPackage(DefinitionPackage.eNS_URI);
        CorePackage theCorePackage = (CorePackage)EPackage.Registry.INSTANCE.getEPackage(CorePackage.eNS_URI);
        XMLTypePackage theXMLTypePackage = (XMLTypePackage)EPackage.Registry.INSTANCE.getEPackage(XMLTypePackage.eNS_URI);
        RelationalPackage theRelationalPackage = (RelationalPackage)EPackage.Registry.INSTANCE.getEPackage(RelationalPackage.eNS_URI);
        EcorePackage theEcorePackage = (EcorePackage)EPackage.Registry.INSTANCE.getEPackage(EcorePackage.eNS_URI);

        // Create type parameters

        // Set bounds for type parameters

        // Add supertypes to classes
        dqRuleEClass.getESuperTypes().add(theDefinitionPackage.getIndicatorDefinition());
        specifiedDQRuleEClass.getESuperTypes().add(this.getDQRule());
        inferredDQRuleEClass.getESuperTypes().add(this.getDQRule());
        whereRuleEClass.getESuperTypes().add(this.getSpecifiedDQRule());
        parserRuleEClass.getESuperTypes().add(this.getDQRule());
        matchRuleDefinitionEClass.getESuperTypes().add(theDefinitionPackage.getIndicatorDefinition());
        blockKeyDefinitionEClass.getESuperTypes().add(this.getKeyDefinition());
        keyDefinitionEClass.getESuperTypes().add(theCorePackage.getModelElement());
        matchKeyDefinitionEClass.getESuperTypes().add(this.getKeyDefinition());
        matchRuleEClass.getESuperTypes().add(theCorePackage.getModelElement());
        appliedBlockKeyEClass.getESuperTypes().add(this.getKeyDefinition());
        survivorshipKeyDefinitionEClass.getESuperTypes().add(this.getKeyDefinition());
        particularDefaultSurvivorshipDefinitionsEClass.getESuperTypes().add(this.getKeyDefinition());
        particularDefaultSurvivorshipDefinitionsEClass.getESuperTypes().add(this.getDefaultSurvivorshipDefinition());

        // Initialize classes and features; add operations and parameters
        initEClass(dqRuleEClass, DQRule.class, "DQRule", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getDQRule_CriticalityLevel(), ecorePackage.getEInt(), "criticalityLevel", null, 0, 1, DQRule.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getDQRule_Elements(), theCorePackage.getModelElement(), null, "elements", null, 0, -1, DQRule.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(specifiedDQRuleEClass, SpecifiedDQRule.class, "SpecifiedDQRule", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(inferredDQRuleEClass, InferredDQRule.class, "InferredDQRule", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(whereRuleEClass, WhereRule.class, "WhereRule", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getWhereRule_WhereExpression(), ecorePackage.getEString(), "whereExpression", null, 0, 1, WhereRule.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getWhereRule_JoinExpression(), ecorePackage.getEString(), "joinExpression", null, 0, 1, WhereRule.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getWhereRule_Joins(), this.getJoinElement(), null, "joins", null, 0, -1, WhereRule.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(joinElementEClass, JoinElement.class, "JoinElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getJoinElement_ColA(), theCorePackage.getModelElement(), null, "colA", null, 1, 1, JoinElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getJoinElement_ColB(), theCorePackage.getModelElement(), null, "colB", null, 1, 1, JoinElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getJoinElement_Operator(), ecorePackage.getEString(), "operator", null, 0, 1, JoinElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getJoinElement_TableAliasA(), ecorePackage.getEString(), "tableAliasA", null, 0, 1, JoinElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getJoinElement_TableAliasB(), ecorePackage.getEString(), "tableAliasB", null, 0, 1, JoinElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getJoinElement_ColumnAliasA(), ecorePackage.getEString(), "columnAliasA", null, 0, 1, JoinElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getJoinElement_ColumnAliasB(), ecorePackage.getEString(), "columnAliasB", null, 0, 1, JoinElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(parserRuleEClass, ParserRule.class, "ParserRule", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        EOperation op = addEOperation(parserRuleEClass, theXMLTypePackage.getBoolean(), "addExpression", 0, 1, IS_UNIQUE, IS_ORDERED);
        addEParameter(op, theXMLTypePackage.getString(), "name", 0, 1, IS_UNIQUE, IS_ORDERED);
        addEParameter(op, theXMLTypePackage.getString(), "type", 0, 1, IS_UNIQUE, IS_ORDERED);
        addEParameter(op, theXMLTypePackage.getString(), "value", 0, 1, IS_UNIQUE, IS_ORDERED);

        op = addEOperation(parserRuleEClass, theRelationalPackage.getTdExpression(), "getExpression", 0, 1, IS_UNIQUE, IS_ORDERED);
        addEParameter(op, theXMLTypePackage.getString(), "name", 0, 1, IS_UNIQUE, IS_ORDERED);

        addEOperation(parserRuleEClass, this.getTdExpressionList(), "getExpression", 0, 1, IS_UNIQUE, IS_ORDERED);

        initEClass(matchRuleDefinitionEClass, MatchRuleDefinition.class, "MatchRuleDefinition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getMatchRuleDefinition_BlockKeys(), this.getBlockKeyDefinition(), null, "blockKeys", null, 0, -1, MatchRuleDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getMatchRuleDefinition_MatchRules(), this.getMatchRule(), null, "matchRules", null, 0, -1, MatchRuleDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getMatchRuleDefinition_XmiId(), theEcorePackage.getEString(), "xmiId", null, 0, 1, MatchRuleDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getMatchRuleDefinition_RecordLinkageAlgorithm(), theEcorePackage.getEString(), "recordLinkageAlgorithm", null, 0, 1, MatchRuleDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getMatchRuleDefinition_AppliedBlockKeys(), this.getAppliedBlockKey(), null, "appliedBlockKeys", null, 0, -1, MatchRuleDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getMatchRuleDefinition_SurvivorshipKeys(), this.getSurvivorshipKeyDefinition(), null, "survivorshipKeys", null, 0, -1, MatchRuleDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getMatchRuleDefinition_DefaultSurvivorshipDefinitions(), this.getDefaultSurvivorshipDefinition(), null, "defaultSurvivorshipDefinitions", null, 0, -1, MatchRuleDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getMatchRuleDefinition_MatchGroupQualityThreshold(), theEcorePackage.getEDouble(), "matchGroupQualityThreshold", null, 0, 1, MatchRuleDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getMatchRuleDefinition_ParticularDefaultSurvivorshipDefinitions(), this.getParticularDefaultSurvivorshipDefinitions(), null, "particularDefaultSurvivorshipDefinitions", null, 0, -1, MatchRuleDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(blockKeyDefinitionEClass, BlockKeyDefinition.class, "BlockKeyDefinition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getBlockKeyDefinition_PreAlgorithm(), this.getAlgorithmDefinition(), null, "preAlgorithm", null, 0, 1, BlockKeyDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getBlockKeyDefinition_Algorithm(), this.getAlgorithmDefinition(), null, "algorithm", null, 0, 1, BlockKeyDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getBlockKeyDefinition_PostAlgorithm(), this.getAlgorithmDefinition(), null, "postAlgorithm", null, 0, 1, BlockKeyDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(keyDefinitionEClass, KeyDefinition.class, "KeyDefinition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getKeyDefinition_Column(), theEcorePackage.getEString(), "column", null, 0, 1, KeyDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(matchKeyDefinitionEClass, MatchKeyDefinition.class, "MatchKeyDefinition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getMatchKeyDefinition_Algorithm(), this.getAlgorithmDefinition(), null, "algorithm", null, 0, 1, MatchKeyDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getMatchKeyDefinition_ConfidenceWeight(), theEcorePackage.getEInt(), "confidenceWeight", null, 0, 1, MatchKeyDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getMatchKeyDefinition_HandleNull(), theEcorePackage.getEString(), "handleNull", null, 0, 1, MatchKeyDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getMatchKeyDefinition_Threshold(), theEcorePackage.getEDouble(), "threshold", null, 0, 1, MatchKeyDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getMatchKeyDefinition_TokenizationType(), theEcorePackage.getEString(), "tokenizationType", "", 0, 1, MatchKeyDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(algorithmDefinitionEClass, AlgorithmDefinition.class, "AlgorithmDefinition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getAlgorithmDefinition_AlgorithmType(), theEcorePackage.getEString(), "algorithmType", null, 0, 1, AlgorithmDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getAlgorithmDefinition_AlgorithmParameters(), theEcorePackage.getEString(), "algorithmParameters", null, 0, 1, AlgorithmDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(matchRuleEClass, MatchRule.class, "MatchRule", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getMatchRule_MatchKeys(), this.getMatchKeyDefinition(), null, "matchKeys", null, 0, -1, MatchRule.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getMatchRule_MatchInterval(), theEcorePackage.getEDouble(), "matchInterval", null, 0, 1, MatchRule.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(appliedBlockKeyEClass, AppliedBlockKey.class, "AppliedBlockKey", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(survivorshipKeyDefinitionEClass, SurvivorshipKeyDefinition.class, "SurvivorshipKeyDefinition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getSurvivorshipKeyDefinition_Function(), this.getAlgorithmDefinition(), null, "function", null, 0, 1, SurvivorshipKeyDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getSurvivorshipKeyDefinition_AllowManualResolution(), theEcorePackage.getEBoolean(), "allowManualResolution", null, 0, 1, SurvivorshipKeyDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(defaultSurvivorshipDefinitionEClass, DefaultSurvivorshipDefinition.class, "DefaultSurvivorshipDefinition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getDefaultSurvivorshipDefinition_DataType(), theEcorePackage.getEString(), "dataType", null, 0, 1, DefaultSurvivorshipDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getDefaultSurvivorshipDefinition_Function(), this.getAlgorithmDefinition(), null, "function", null, 0, 1, DefaultSurvivorshipDefinition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(particularDefaultSurvivorshipDefinitionsEClass, ParticularDefaultSurvivorshipDefinitions.class, "ParticularDefaultSurvivorshipDefinitions", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        // Initialize data types
        initEDataType(tdExpressionListEDataType, List.class, "TdExpressionList", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS, "java.util.List<org.talend.cwm.relational.TdExpression>");

        // Create resource
        createResource(eNS_URI);
    }

} //RulesPackageImpl
