package com.taskmanagement.accounting.analytics

import java.math.BigDecimal

data class AnalyticsTodayStatisticsResponseDto(
    val managementAccrualAmount: BigDecimal,
    val negativeBalanceCount: Int,
)
