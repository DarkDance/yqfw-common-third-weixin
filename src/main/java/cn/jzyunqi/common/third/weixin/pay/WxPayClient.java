package cn.jzyunqi.common.third.weixin.pay;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.feature.redis.RedisHelper;
import cn.jzyunqi.common.third.weixin.common.constant.WxCache;
import cn.jzyunqi.common.third.weixin.common.enums.WeixinPaySubType;
import cn.jzyunqi.common.third.weixin.common.utils.WxFormatUtils;
import cn.jzyunqi.common.third.weixin.pay.callback.WxPayCbHelper;
import cn.jzyunqi.common.third.weixin.pay.callback.model.WxPayResultCb;
import cn.jzyunqi.common.third.weixin.pay.cert.WxPayCertApiProxy;
import cn.jzyunqi.common.third.weixin.pay.cert.model.PlantCertData;
import cn.jzyunqi.common.third.weixin.pay.cert.model.PlantCertRedisDto;
import cn.jzyunqi.common.third.weixin.pay.order.WxPayOrderApi;
import cn.jzyunqi.common.third.weixin.pay.order.WxPayOrderApiProxy;
import cn.jzyunqi.common.third.weixin.pay.order.enums.RefundStatus;
import cn.jzyunqi.common.third.weixin.pay.order.enums.TradeState;
import cn.jzyunqi.common.third.weixin.pay.order.model.OrderData;
import cn.jzyunqi.common.third.weixin.pay.order.model.OrderRefundData;
import cn.jzyunqi.common.third.weixin.pay.order.model.PayAmountData;
import cn.jzyunqi.common.third.weixin.pay.order.model.PayPayerData;
import cn.jzyunqi.common.third.weixin.pay.order.model.RefundOrderParam;
import cn.jzyunqi.common.third.weixin.pay.order.model.UnifiedAppOrderData;
import cn.jzyunqi.common.third.weixin.pay.order.model.UnifiedH5OrderData;
import cn.jzyunqi.common.third.weixin.pay.order.model.UnifiedJsapiOrderData;
import cn.jzyunqi.common.third.weixin.pay.order.model.UnifiedOrderParam;
import cn.jzyunqi.common.third.weixin.pay.order.model.UnifiedOrderRsp;
import cn.jzyunqi.common.utils.CollectionUtilPlus;
import cn.jzyunqi.common.utils.DateTimeUtilPlus;
import cn.jzyunqi.common.utils.DigestUtilPlus;
import cn.jzyunqi.common.utils.RandomUtilPlus;
import cn.jzyunqi.common.utils.StringUtilPlus;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.SSLException;
import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wiiyaya
 * @since 2024/9/25
 */
@Slf4j
public class WxPayClient {

    @Resource
    public WxPayOrderApi order;

    @Resource
    public WxPayCbHelper cb;
}
