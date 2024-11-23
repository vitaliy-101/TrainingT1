package com.example.trainingt1.aspect;

import com.example.trainingt1.dto.TaskDto;
import com.example.trainingt1.entity.Task;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;


@Aspect
@Component
public class ProjectAspect {
    private static final Logger logger = LoggerFactory.getLogger(ProjectAspect.class.getName());

    @Before("execution(* com.example.trainingt1.controller.*.*(..))")
    public void logBeforeControllerFunction(JoinPoint joinPoint) {
        logger.info("Calling the end point with the name = '{}' in the class '{}'", joinPoint.getSignature().getName(), joinPoint.getSignature().getDeclaringType().getName());
    }

    @AfterThrowing(pointcut = "@annotation(LogException)", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Exception exception) {
        logger.error("Exception named '{}' occurred in a method named '{}' in the class '{}'", exception.getClass().getName(), joinPoint.getSignature().getName(), joinPoint.getSignature().getDeclaringType().getName());
    }

    @Around("@annotation(LogExecutionTime)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) {
        logger.info("Calling the execution time method with the name = '{}' in the class '{}'", joinPoint.getSignature().getName(), joinPoint.getSignature().getDeclaringType().getName());
        Object[] args = joinPoint.getArgs();
        if (args != null && args.length != 0) {
            logger.info("Method named '{}' has args: {}", joinPoint.getSignature().getName(), Arrays.toString(args));
        }

        long startTime = System.currentTimeMillis();
        Object result = null;
        try {
            result = joinPoint.proceed();
        }
        catch (Throwable e) {
            logger.error("Error while getting result joinPoint.proceed");
            throw new RuntimeException(e);
        }
        long finishTime = System.currentTimeMillis();
        logger.info("Aspect method '{}' execution time: {} ms", joinPoint.getSignature().getName(), finishTime - startTime);

        return result;
    }

    @AfterReturning(
            pointcut = "@annotation(LogHandling)",
            returning = "resultTask"
    )
    public void handlingTaskResult(JoinPoint joinPoint, Task resultTask) {
        logger.info("Result Task of a method named '{}': {}", joinPoint.getSignature().getName(), resultTask.toString());
    }

    @AfterReturning(
            pointcut = "@annotation(LogHandlingDto)",
            returning = "resultTaskDto"
    )
    public void handlingTaskDtoResult(JoinPoint joinPoint, TaskDto resultTaskDto) {
        logger.info("Result Task DTO of a method named '{}': {}", joinPoint.getSignature().getName(), resultTaskDto.toString());
    }


}
