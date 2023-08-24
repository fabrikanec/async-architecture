package com.taskmanagement.accounting.balance

import com.taskmanagement.accounting.employee.jpa.EmployeeRepository
import com.taskmanagement.accounting.employee.jpa.EmployeeRepository.Companion.getByIdOrThrow
import com.user.User
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal

@RestController
@RequestMapping("/balances")
class BalanceController(
    private val employeeRepository: EmployeeRepository,
) {

    @GetMapping
    fun getBalance(
        user: User,
        pageable: Pageable,
    ): BigDecimal =
        employeeRepository.getByIdOrThrow(user.id).balance
}
