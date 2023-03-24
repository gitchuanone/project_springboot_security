package com.jwt.filter;


import com.alibaba.fastjson.JSON;
import com.auth0.jwt.interfaces.Claim;
import com.jwt.utils.JwtUtils;
import io.micrometer.core.instrument.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * 模拟拦截请求，进行token验证。
 *
 * @author yangchuan
 * @date 2023/3/24
 */
public class RequestFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(RequestFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(filterConfig.getServletContext());
//        redisTemplate = (RedisTemplate)context.getBean("redisTemplate");

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        if (request.getRequestURI().matches(".*(/public|protect).*")) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        String httpMethod = request.getMethod();
        if("OPTIONS".equalsIgnoreCase(httpMethod)){
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String allowOrigin = request.getHeader("Origin");
        String allowHeader = request.getHeader("Access-Control-Request-Headers");
        response.setHeader("Access-Control-Allow-Origin", allowOrigin);
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "OPTIONS, POST, PUT, GET, DELETE");
        response.setHeader("Access-Control-Allow-Headers", allowHeader);
        String token = request.getHeader("token");
        // token不存在或jwt中的时间过期
        if(StringUtils.isEmpty(token)) {
            writeToResponse(response, "无token值");
        } else {
            logger.info("===============>verifyToken");
            Map<String, Claim> verifyToken = JwtUtils.verifyToken(token);
            for (Map.Entry<String, Claim> entry : verifyToken.entrySet()){
                if (entry.getValue().asString() != null) {
                    logger.info(entry.getKey() + "===" + entry.getValue().asString());
                }else {
                    logger.info(entry.getKey() + "===" + entry.getValue().asDate());
                }
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }


    /**
     * 向HttpServletResponse中写入返回数据
     * @param response
     * @param o      返回数据
     */
    private void writeToResponse(HttpServletResponse response, Object o){
        OutputStream os = null;
        response.setContentType("application/json;charset=utf-8");
        response.setCharacterEncoding("UTF-8");
        try {
            os = response.getOutputStream();
            String str = JSON.toJSONString(o);
            os.write(str.getBytes("utf-8"));
            os.flush();
        } catch (IOException e) {
            logger.error("ValidateFilter.writeToResponse error",e);
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    logger.error("ValidateFilter.writeToResponse close OutputStream error",e);
                }
            }
        }
    }

}
