package cn.jzyunqi.common.third.weixin.common;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.third.weixin.common.model.WeixinRsp;
import cn.jzyunqi.common.utils.StringUtilPlus;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;

import java.util.Optional;

/**
 * @author wiiyaya
 * @since 2024/9/23
 */
@Slf4j
@Aspect
@Order
public class WxHttpExchangeWrapper {

    /**
     * 所有标记了@WxHttpExchange的类下所有的方法
     */
    @Pointcut("within(@cn.jzyunqi.common.third.weixin.common.WxHttpExchange *)")
    public void wxHttpExchange() {
    }

    @Around(value = "wxHttpExchange() ", argNames = "proceedingJoinPoint")
    public Object Around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        log.debug("======wxHttpExchange start=======");
        Object resultObj;
        try {
            resultObj = proceedingJoinPoint.proceed();
        } catch (Throwable e) {
            log.debug("======wxHttpExchange proceed throw exception=======");
            throw new BusinessException("common_error_wx_http_exchange_error", e);
        }
        log.debug("======wxHttpExchange proceed success=======");
        if (resultObj instanceof WeixinRsp weixinRsp) {
            if (StringUtilPlus.isNotBlank(weixinRsp.getErrorCode()) && !"0".equals(weixinRsp.getErrorCode())) {
                throw new BusinessException("common_error_wx_http_exchange_failed", weixinRsp.getErrorCode(), weixinRsp.getErrorMsg());
            }
        }
        log.debug("======wxHttpExchange end=======");
        return resultObj;
    }
}