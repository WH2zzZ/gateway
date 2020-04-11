package com.wanghan.microservices.gateway.utils;

import com.netflix.zuul.context.RequestContext;
import org.springframework.stereotype.Component;

/**
 * 过滤器的工具类
 *
 * @Author WangHan
 * @Create 2020/1/2 12:37 下午
 */
@Component
public class FilterUtils {


    public static final String TRACE_ID = "trace_id";
    public static final String PRE_FILTER_TYPE = "pre";
    public static final String ROUTE_FILTER_TYPE = "route";
    public static final String POST_FILTER_TYPE = "post";
    public static final String ERROR_FILTER_TYPE = "error";

    public String getTraceId(){
        RequestContext requestContext = RequestContext.getCurrentContext();

        if (requestContext.getRequest().getHeader(TRACE_ID) != null){
            return requestContext.getRequest().getHeader(TRACE_ID);
        }else {
            return requestContext.getZuulRequestHeaders().get(TRACE_ID);
        }
    }

    public String setTraceId(String traceId){
        RequestContext requestContext = RequestContext.getCurrentContext();

        requestContext.addZuulRequestHeader(TRACE_ID, traceId);
        if (requestContext.getRequest().getHeader(TRACE_ID) != null){
            return requestContext.getRequest().getHeader(TRACE_ID);
        }else {
            return requestContext.getZuulRequestHeaders().get(TRACE_ID);
        }
    }

    public static boolean find(int target, int [][] array) {
        int width = array[0].length;
        int length = array.length;
        for (int i = 0; i < length; i++) {
            if (array[i][0] > target){
                return false;
            }else if (array[i][0] == target){
                return true;
            }else {
                if (target < array[i][width-1]){
                    boolean flag = false;
                    for (int j = 0; j < width; j++) {
                        if (array[i][j] == target){
                            flag = true;
                            break;
                        }
                    }
                    return flag;
                }else {
                    if (i == --length){
                        return false;
                    }
                }
            }
        }
        return false;
    }

}
