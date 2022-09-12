package com.zuzex.carfactory.exception

import com.zuzex.common.dto.ErrorMessageDto
import com.zuzex.common.exception.NotFoundException
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

private val log = KotlinLogging.logger {}

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException::class)
    fun handleNotFoundException(ex: NotFoundException): ResponseEntity<ErrorMessageDto> {
        log.error(ex.message)
        return ResponseEntity(ErrorMessageDto(ex.message), HttpStatus.NOT_FOUND)
    }
}
