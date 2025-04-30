package t1.school.tasks.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    private final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Before("@annotation(t1.school.tasks.aspects.annotations.LogStart)")
    public void logStart(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        String methodName = signature.getDeclaringTypeName() + "." + signature.getName();
        logger.info("Метод " + methodName + " начал выполняться");
    }

    @Around("@annotation(t1.school.tasks.aspects.annotations.LogExecutionTime)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        Signature signature = joinPoint.getSignature();
        String methodName = signature.getDeclaringTypeName() + "." + signature.getName();
        long startTime = System.currentTimeMillis();
        Boolean isResultSucceed = null;
        try {
            Object result = joinPoint.proceed();
            isResultSucceed = true;
            return result;
        } catch (Throwable throwable) {
            isResultSucceed = false;
            throw throwable;
        } finally {
            long endTime = System.currentTimeMillis();
            long execTime = endTime - startTime;
            if (Boolean.TRUE.equals(isResultSucceed)) {
                logger.info("Метод " + methodName + " выполнился успешно после " + execTime + " мс");
            } else {
                logger.info("Метод " + methodName + " завершился с ошибкой после " + execTime + " мс");
            }
        }
    }

    @After("@annotation(t1.school.tasks.aspects.annotations.LogEnd)")
    public void logEnd(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        String methodName = signature.getDeclaringTypeName() + "." + signature.getName();
        logger.info("Метод " + methodName + " закончил свое выполнение");
    }

    @AfterReturning(
            pointcut = "@annotation(t1.school.tasks.aspects.annotations.LogResult)",
            returning = "result"
    )
    public void logResult(JoinPoint joinPoint, Object result) {
        Signature signature = joinPoint.getSignature();
        String methodName = signature.getDeclaringTypeName() + "." + signature.getName();
        logger.info("Метод " + methodName + " вернул в качестве результата " + result);
    }

    @AfterThrowing(
            pointcut = "@annotation(t1.school.tasks.aspects.annotations.LogException)",
            throwing = "exception"
    )
    public void logException(JoinPoint joinPoint, Exception exception) {
        Signature signature = joinPoint.getSignature();
        String methodName = signature.getDeclaringTypeName() + "." + signature.getName();
        Object[] args = joinPoint.getArgs();
        logger.error(
                "Метод " + methodName + " после запуска с аргументами " + Arrays.toString(args) +
                        " завершился с ошибкой " + exception.getClass().getName() +
                        " и сообщением: " + exception.getMessage()
        );
    }
}
