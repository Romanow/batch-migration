package ru.romanow.batch.migration.service

import org.slf4j.LoggerFactory
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Service
import ru.romanow.batch.migration.config.properties.BatchProcessingProperties
import java.time.LocalDateTime.now
import java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME

@Service
class MigrationService(
    private val migration: Job,
    private val jobLauncher: JobLauncher,
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    private val properties: BatchProcessingProperties
) {
    private val logger = LoggerFactory.getLogger(MigrationService::class.java)

    fun batch(solveId: String) {
        logger.info("Request to start batch processing")
        val params = JobParametersBuilder()
            .addString("solveId", solveId)
            .addString("key", ISO_LOCAL_DATE_TIME.format(now()))
            .toJobParameters()

        val execution = jobLauncher.run(migration, params)

        logger.info("Started batch process ${execution.jobId}: ${execution.status} (start: ${execution.startTime})")
    }

    fun database(solveId: String) {
        logger.info("Request to start sql migration")
        val updated = jdbcTemplate.update(
            """
            INSERT INTO ${properties.mainSchema}.aggregation_result (account_code, account_number, accrued_interest_amount,
                                               accrued_interest_fix_amount, accrued_interest_fix_rubles_amount,
                                               accrued_interest_float_amount, accrued_interest_float_rubles_amount,
                                               accrued_interest_rubles_amount, addition_flag, agreement_rank,
                                               average_principal_amount, average_principal_rubles_amount, average_interest_rate,
                                               balance_number, behavior_model_code, call_deposit_flag, client_id, comment1,
                                               comment2, commission_amount, commission_rubles_amount, credential_type_number,
                                               currency_id, current_principal_amount, current_principal_rub_amount,
                                               customer_rank, day_convention_code, deal_accrued_interest_amount,
                                               deal_currency_id, deal_current_principal_amount, deal_date, deal_id,
                                               deal_principal_payment_amount, deal_start_principal_amount, deposit_type_code,
                                               deposit_type_number, discriminator, end_date, evr_amortization_type, evr_product,
                                               ftp_income_amount, ftp_income_rubles_amount, has_no_schedule_flag,
                                               in_balance_amount, in_balance_rubles_amount, interest_income_amount,
                                               interest_income_fix_amount, interest_income_fix_rubles_amount,
                                               interest_income_float_amount, interest_income_float_rubles_amount,
                                               interest_income_rubles_amount, interest_payment_amount,
                                               interest_payment_fix_amount, interest_payment_fix_rubles_amount,
                                               interest_payment_float_amount, interest_payment_float_rubles_amount,
                                               interest_payment_rubles_amount, interest_payment_rule_day_number,
                                               interest_payment_rule_period_code, interest_payment_rule_start_date,
                                               interest_rate, interest_rate_fixed, interest_rate_float, intergroup_flg,
                                               level1product_bank_number, level2product_bank_number, load_date, maturity_date,
                                               online_flag, out_balance_amount, out_balance_rubles_amount,
                                               partition_report_date, port_risks_scheduled_link_id, prepayment_amount,
                                               prepayment_rubles_amount, principal_payment_amount,
                                               principal_payment_rubles_amount, product_level1, product_level2, product_level3,
                                               product_level4, product_level5, product_rules_maturity, program_number,
                                               rate_type_code, regular_payment_rule_day_number,
                                               regular_payment_rule_period_code, regular_payment_rule_start_date, report_date,
                                               segment, service_model_code, solve_id, source_code, start_date,
                                               start_principal_amount, start_principal_rubles_amount, term_group,
                                               time_bucket_code, time_bucket_number, transfer_rate, value_date, ytm_flag)
        SELECT ar.account_code,
               ar.account_number,
               ar.accrued_interest_amount,
               ar.accrued_interest_fix_amount,
               ar.accrued_interest_fix_rubles_amount,
               ar.accrued_interest_float_amount,
               ar.accrued_interest_float_rubles_amount,
               ar.accrued_interest_rubles_amount,
               ar.addition_flag,
               ar.agreement_rank,
               ar.average_principal_amount,
               ar.average_principal_rubles_amount,
               ar.avg_interest_rate,
               ar.balance_number,
               ar.behavior_model_code,
               ar.call_deposit_flag,
               ar.client_id,
               ar.comment1,
               ar.comment2,
               ar.commission_amount,
               ar.commission_rubles_amount,
               ar.credential_type_number,
               ar.currency_id,
               ar.current_principal_amount,
               ar.current_principal_rub_amount,
               ar.customer_rank,
               ar.day_convention_code,
               ar.deal_accrued_interest_amount,
               ar.deal_currency_id,
               ar.deal_current_principal_amount,
               ar.deal_date,
               ar.deal_id,
               ar.deal_principal_payment_amount,
               ar.deal_start_principal_amount,
               ar.deposit_type_code,
               ar.deposit_type_number,
               ar.discriminator,
               ar.end_date,
               ar.evr_amortization_type,
               ar.evr_product,
               ar.ftp_income_amount,
               ar.ftp_income_rubles_amount,
               ar.has_no_schedule_flag,
               ar.in_balance_amount,
               ar.in_balance_rubles_amount,
               ar.interest_income_amount,
               ar.interest_income_fix_amount,
               ar.interest_income_fix_rubles_amount,
               ar.interest_income_float_amount,
               ar.interest_income_float_rubles_amount,
               ar.interest_income_rubles_amount,
               ar.interest_payment_amount,
               ar.interest_payment_fix_amount,
               ar.interest_payment_fix_rubles_amount,
               ar.interest_payment_float_amount,
               ar.interest_payment_float_rubles_amount,
               ar.interest_payment_rubles_amount,
               ar.interest_payment_rule_day_number,
               ar.interest_payment_rule_period_code,
               ar.interest_payment_rule_start_date,
               ar.interest_rate,
               ar.interest_rate_fixed,
               ar.interest_rate_float,
               ar.intergroup_flg,
               ar.level1product_bank_number,
               ar.level2product_bank_number,
               ar.load_date,
               ar.maturity_date,
               ar.online_flag,
               ar.out_balance_amount,
               ar.out_balance_rubles_amount,
               ar.partition_report_date,
               ar.port_risks_scheduled_link_id,
               ar.prepayment_amount,
               ar.prepayment_rubles_amount,
               ar.principal_payment_amount,
               ar.principal_payment_rubles_amount,
               ar.product_level1,
               ar.product_level2,
               ar.product_level3,
               ar.product_level4,
               ar.product_level5,
               ar.product_rules_maturity,
               ar.program_number,
               ar.rate_type_code,
               ar.regular_payment_rule_day_number,
               ar.regular_payment_rule_period_code,
               ar.regular_payment_rule_start_date,
               ar.report_date,
               ar.segment,
               ar.service_model_code,
               ar.solve_id,
               ar.source_code,
               ar.start_date,
               ar.start_principal_amount,
               ar.start_principal_rubles_amount,
               ar.term_group,
               ar.time_bucket_code,
               ar.time_bucket_number,
               ar.transfer_rate,
               ar.value_date,
               ar.ytm_flag
        FROM ${properties.stagedSchema}.aggregation_result ar
        WHERE ar.solve_id = :solveId;
        """.trimIndent(), mapOf("solveId" to solveId)
        )

        logger.info("Moved $updated records from ${properties.stagedSchema} to ${properties.mainSchema}")

        val deleted = jdbcTemplate.update(
            "DELETE FROM ${properties.stagedSchema}.aggregation_result WHERE solve_id = :solveId",
            mapOf("solveId" to solveId)
        )
        logger.info("Removed $deleted items from ${properties.stagedSchema}")
    }
}
