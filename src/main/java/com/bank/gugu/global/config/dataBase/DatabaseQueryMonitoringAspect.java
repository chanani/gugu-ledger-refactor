package com.bank.gugu.global.config.dataBase;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Order(1)
public class DatabaseQueryMonitoringAspect {

    @Around("@annotation(org.springframework.transaction.annotation.Transactional)")
    public Object monitorDatabaseQuery(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        try {
            Boolean isReadOnly = TransactionContextHolder.isReadOnly();
            String dataSource = (isReadOnly != null && isReadOnly) ? "SLAVE" : "MASTER";

            System.out.println("=== 라우팅 결정 (ThreadLocal) ===");
            System.out.println("ReadOnly from ThreadLocal: " + isReadOnly);
            System.out.println("Selected: " + dataSource);
            System.out.println("==============================");

            return joinPoint.proceed();
        } finally {
            Boolean isReadOnly = TransactionContextHolder.isReadOnly();
            String dataSource = (isReadOnly != null && isReadOnly) ? "SLAVE" : "MASTER";

            System.out.println("=== 라우팅 결정 ===");
            System.out.println("ReadOnly from ThreadLocal : " + isReadOnly);
            System.out.println("Selected : " + dataSource);
            System.out.println("==============================");


            long executionTime = System.currentTimeMillis() - startTime;
            System.out.println("=== 실행 완료 ===");
            System.out.println("실행 시간 : " + executionTime + "ms");
            System.out.println("====================");
        }
    }

}
