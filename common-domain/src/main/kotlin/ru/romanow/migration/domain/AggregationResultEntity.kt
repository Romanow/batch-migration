package ru.romanow.migration.domain

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "aggregation_result")
data class AggregationResultEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column
    var accountCode: String? = null,

    @Column
    var accountNumber: String? = null,

    @Column
    var accruedInterestAmount: BigDecimal? = null,

    @Column
    var accruedInterestRublesAmount: BigDecimal? = null,

    @Column
    var agreementRank: String? = null,

    @Column
    var averagePrincipalAmount: BigDecimal? = null,

    @Column
    var averagePrincipalRublesAmount: BigDecimal? = null,

    @Column
    var balanceNumber: String? = null,

    @Column
    var clientId: String? = null,

    @Column
    var comment1: String? = null,

    @Column
    var comment2: String? = null,

    @Column
    var commissionAmount: BigDecimal? = null,

    @Column
    var commissionRublesAmount: BigDecimal? = null,

    @Column
    var currencyId: String? = null,

    @Column
    var currentPrincipalAmount: BigDecimal? = null,

    @Column
    var currentPrincipalRubAmount: BigDecimal? = null,

    @Column
    var customerRank: String? = null,

    @Column
    var dayConventionCode: String? = null,

    @Column
    var dealDate: LocalDateTime? = null,

    @Column
    var dealId: String? = null,

    @Column
    var discriminator: String? = null,

    @Column
    var endDate: LocalDateTime? = null,

    @Column
    var ftpIncomeAmount: BigDecimal? = null,

    @Column
    var ftpIncomeRublesAmount: BigDecimal? = null,

    @Column
    var hasNoScheduleFlag: String? = null,

    @Column
    var inBalanceAmount: BigDecimal? = null,

    @Column
    var inBalanceRublesAmount: BigDecimal? = null,

    @Column
    var interestIncomeAmount: BigDecimal? = null,

    @Column
    var interestIncomeRublesAmount: BigDecimal? = null,

    @Column
    var interestPaymentRulePeriodCode: String? = null,

    @Column
    var interestPaymentRuleStartDate: LocalDateTime? = null,

    @Column
    var interestRate: BigDecimal? = null,

    @Column
    var interestPaymentAmount: BigDecimal? = null,

    @Column
    var loadDate: LocalDateTime? = null,

    @Column
    var level1ProductBankNumber: String? = null,

    @Column
    var level2ProductBankNumber: String? = null,

    @Column
    var maturityDate: LocalDateTime? = null,

    @Column
    var outBalanceAmount: BigDecimal? = null,

    @Column
    var outBalanceRublesAmount: BigDecimal? = null,

    @Column
    var principalPaymentAmount: BigDecimal? = null,

    @Column
    var principalPaymentRublesAmount: BigDecimal? = null,

    @Column
    var rateTypeCode: String? = null,

    @Column
    var regularPaymentRulePeriodCode: String? = null,

    @Column
    var regularPaymentRuleStartDate: LocalDateTime? = null,

    @Column
    var reportDate: LocalDateTime? = null,

    @Column
    var solveId: String? = null,

    @Column
    var sourceCode: String? = null,

    @Column
    var startDate: LocalDateTime? = null,

    @Column
    var startPrincipalAmount: BigDecimal? = null,

    @Column
    var startPrincipalRublesAmount: BigDecimal? = null,

    @Column
    var timeBucketCode: String? = null,

    @Column
    var timeBucketNumber: Long? = null,

    @Column
    var transferRate: BigDecimal? = null,

    @Column
    var valueDate: LocalDateTime? = null,

    @Column
    var ytmFlag: Long? = null,

    @Column
    var interestPaymentRublesAmount: BigDecimal? = null,

    @Column
    var interestPaymentFixAmount: BigDecimal? = null,

    @Column
    var interestPaymentFixRublesAmount: BigDecimal? = null,

    @Column
    var interestPaymentFloatAmount: BigDecimal? = null,

    @Column
    var interestPaymentFloatRublesAmount: BigDecimal? = null,

    @Column
    var avgInterestRate: BigDecimal? = null,

    @Column
    var prepaymentAmount: BigDecimal? = null,

    @Column
    var prepaymentRublesAmount: BigDecimal? = null,

    @Column
    var accruedInterestFixAmount: BigDecimal? = null,

    @Column
    var accruedInterestFixRublesAmount: BigDecimal? = null,

    @Column
    var accruedInterestFloatAmount: BigDecimal? = null,

    @Column
    var accruedInterestFloatRublesAmount: BigDecimal? = null,

    @Column
    var interestIncomeFixAmount: BigDecimal? = null,

    @Column
    var interestIncomeFixRublesAmount: BigDecimal? = null,

    @Column
    var interestIncomeFloatAmount: BigDecimal? = null,

    @Column
    var interestIncomeFloatRublesAmount: BigDecimal? = null,

    @Column
    var interestRateFloat: BigDecimal? = null,

    @Column
    var interestRateFixed: BigDecimal? = null,

    @Column
    var interestPaymentRuleDayNumber: Long? = null,

    @Column
    var regularPaymentRuleDayNumber: Long? = null,

    @Column
    var dealStartPrincipalAmount: BigDecimal? = null,

    @Column
    var dealCurrentPrincipalAmount: BigDecimal? = null,

    @Column
    var dealPrincipalPaymentAmount: BigDecimal? = null,

    @Column
    var dealAccruedInterestAmount: BigDecimal? = null,

    @Column
    var dealCurrencyId: String? = null,

    @Column
    var segment: String? = null,

    @Column
    var intergroupFlg: String? = null,

    @Column
    var termGroup: String? = null,

    @Column
    var partitionReportDate: String? = null,

    @Column
    var depositTypeCode: String? = null,

    @Column
    var depositTypeNumber: String? = null,

    @Column
    var additionFlag: String? = null,

    @Column
    var callDepositFlag: String? = null,

    @Column
    var onlineFlag: String? = null,

    @Column
    var serviceModelCode: String? = null,

    @Column
    var credentialTypeNumber: String? = null,

    @Column
    var programNumber: String? = null,

    @Column
    var productLevel1: String? = null,

    @Column
    var productLevel2: String? = null,

    @Column
    var productLevel3: String? = null,

    @Column
    var productLevel4: String? = null,

    @Column
    var productLevel5: String? = null,

    @Column
    var productRulesMaturity: String? = null,

    @Column
    var evrProduct: String? = null,

    @Column
    var evrAmortizationType: String? = null,

    @Column
    var portRisksScheduledLinkId: String? = null,

    @Column
    var behaviorModelCode: String? = null,
)