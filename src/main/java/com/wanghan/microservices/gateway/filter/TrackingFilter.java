package com.wanghan.microservices.gateway.filter;

import cn.hutool.core.util.IdUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.wanghan.microservices.gateway.utils.FilterUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * zuul的前置过滤器
 *
 * 为用户的请求添加一个traceId，追踪用户这次请求的调用链
 *
 * @Author WangHan
 * @Create 2019/12/31 12:52 下午
 */
@Slf4j
@Component
public class TrackingFilter extends ZuulFilter {

    private static final int FILTER_ORDER = 1;

    @Autowired
    private FilterUtils filterUtils;

    /**
     * 告诉zuul,该过滤器是前置过滤器，路由过滤器还是后置过滤器
     * filterType：返回一个字符串代表过滤器的类型，在zuul中定义了四种不同生命周期的过滤器类型，具体如下：
     * pre：可以在请求被路由之前调用
     * route：在路由请求时候被调用
     * post：在route和error过滤器之后被调用
     * error：处理请求时发生错误时被调用
     *
     * @Author WangHan
     * @Create 1:03 下午 2019/12/31
     * @Param []
     * @Return java.lang.String
     */
    @Override
    public String filterType() {
        return FilterUtils.PRE_FILTER_TYPE;
    }

    /**
     * 返回一个整数值，指示不同类型的过滤器执行顺序
     * 数字越大，优先级越低
     * @Author WangHan
     * @Create 1:04 下午 2019/12/31
     * @Param []
     * @Return int
     */
    @Override
    public int filterOrder() {
        return FILTER_ORDER;
    }

    /**
     * 返回一个布尔值，指示该过滤器是否要执行
     * 是否执行该过滤器，此处为true，说明需要过滤
     * @Author WangHan
     * @Create 1:04 下午 2019/12/31
     * @Param []
     * @Return boolean
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * 每次服务通过过滤器时要执行的代码
     * 在这里检查判断traceId是否存在。如果不存在则生成一个关联值。并设置http的首部traceId
     * @Author WangHan
     * @Create 1:05 下午 2019/12/31
     * @Param []
     * @Return java.lang.Object
     */
    @Override
    public Object run() {
        if (isTraceIdPresent()){
            log.info("traceId found in tracking filter: {}", filterUtils.getTraceId());
        }else {
            filterUtils.setTraceId(generateTraceId());
            log.info("traceId generated in tracking filter: {}", filterUtils.getTraceId());
        }

        RequestContext requestContext = RequestContext.getCurrentContext();
        log.info("Processing incoming request : {}", requestContext.getRequest().getRequestURI());
        return null;
    }

    private boolean isTraceIdPresent(){
        String traceId = filterUtils.getTraceId();
        if (traceId == null){
            return false;
        }else {
            return true;
        }
    }

    private String generateTraceId(){
        return IdUtil.simpleUUID();
    }
}
