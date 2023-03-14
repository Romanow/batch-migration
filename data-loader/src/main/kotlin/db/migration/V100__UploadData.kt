package db.migration

import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context
import org.slf4j.LoggerFactory
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.datasource.SingleConnectionDataSource
import ru.romanow.migration.domain.AggregationResultEntity
import java.math.BigDecimal.valueOf
import java.time.LocalDateTime.now
import java.time.format.DateTimeFormatter.BASIC_ISO_DATE
import java.util.*
import kotlin.random.Random.Default.nextDouble
import kotlin.random.Random.Default.nextLong

class V100__UploadData : BaseJavaMigration() {
    private val logger = LoggerFactory.getLogger("migration")

    override fun migrate(context: Context) {
        val dataSource = SingleConnectionDataSource(context.connection, true)
        val jdbcTemplate = NamedParameterJdbcTemplate(dataSource)
        val schema = "staged"

        logger.info("Uploading ${BATCH_SIZE * ITERATIONS} entities to database")
        for (i in 1..ITERATIONS) {
            val beans: Array<BeanPropertySqlParameterSource> = (1..BATCH_SIZE)
                .map { BeanPropertySqlParameterSource(buildAggregationResult(OPERATION_ID)) }
                .toTypedArray()

            jdbcTemplate.batchUpdate(
                """
                INSERT INTO $schema.aggregation_result
                    ( report_date, load_date, solve_id, time_bucket_number, time_bucket_code, end_date
                    , currency_id, account_code, account_number, rate_type_code, in_balance_amount, out_balance_amount
                    , interest_payment_amount, principal_payment_amount, commission_amount
                    , average_principal_amount, accrued_interest_amount, ftp_income_amount, source_code
                    , interest_payment_rule_day_number, regular_payment_rule_day_number, segment, term_group
                    , partition_report_date, ytm_flag)
                    VALUES ( :reportDate, :loadDate, :solveId, :timeBucketNumber, :timeBucketCode, :endDate
                           , :currencyId, :accountCode, :accountNumber, :rateTypeCode, :inBalanceAmount, :outBalanceAmount
                           , :interestPaymentAmount, :principalPaymentAmount, :commissionAmount
                           , :averagePrincipalAmount, :accruedInterestAmount, :ftpIncomeAmount, :sourceCode
                           , :interestPaymentRuleDayNumber, :regularPaymentRuleDayNumber, :segment, :termGroup
                           , :partitionReportDate, :ytmFlag);
            """.trimIndent(), beans
            )

            logger.info("Uploaded ${i * BATCH_SIZE} items")
        }
        logger.info("Uploading completed")
    }

    private fun buildAggregationResult(solveId: String) = AggregationResultEntity(
        reportDate = now(),
        loadDate = now(),
        solveId = solveId,
        timeBucketNumber = 2,
        timeBucketCode = "1M-2M",
        endDate = now(),
        currencyId = "RUB",
        accountCode = "B_L_DEP_CORP_TERM_DEP_CORP",
        accountNumber = UUID.randomUUID().toString(),
        rateTypeCode = "FLOAT",
        inBalanceAmount = valueOf(nextDouble(1000.0)),
        outBalanceAmount = valueOf(nextDouble(1000.0)),
        interestPaymentAmount = valueOf(nextDouble(1000.0)),
        principalPaymentAmount = valueOf(nextDouble(1000.0)),
        commissionAmount = valueOf(nextDouble(1000.0)),
        averagePrincipalAmount = valueOf(nextDouble(1000.0)),
        accruedInterestAmount = valueOf(nextDouble(1000.0)),
        interestIncomeAmount = valueOf(nextDouble(1000.0)),
        ftpIncomeAmount = valueOf(nextDouble(1000.0)),
        sourceCode = "NEW_BUSINESS",
        interestPaymentRuleDayNumber = nextLong(100),
        regularPaymentRuleDayNumber = nextLong(1000),
        segment = "???",
        termGroup = "2Y",
        partitionReportDate = BASIC_ISO_DATE.format(now()),
        ytmFlag = 0
    )

    companion object {
        private const val ITERATIONS = 80
        private const val BATCH_SIZE = 5000
        private const val OPERATION_ID = "solve_id"
    }
}