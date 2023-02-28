package online.superh.harogatewayali.filter;

/**
 * @version: 1.0
 * @author: haro
 * @description:
 * @date: 2023-02-28 10:28
 */
public interface GlobalFilterOrderEnum {


    /**
     *  AccessLogGlobalFilter:
     *  自定义的GlobaFilter的order必须小于-1，否则标准 NettyWriteResponseFilter 将在过滤器有机会被调用之前发送响应
     */
    int ACCESS_LOG_GLOBALFILTER = -2;

}
