package com.ydy258.ydy.filter;
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.log4j.NDC;

import com.ydy258.ydy.util.utils;
public class Log4jNdcFilter implements Filter {
public void doFilter(ServletRequest request, ServletResponse response,FilterChain chain) throws IOException, ServletException {
	    //String address = request.getRemoteAddr();
		String address = utils.generaterOrderNumber();
	    NDC.push(address);
	    chain.doFilter(request, response);
	    NDC.pop();
    }

@Override
public void destroy() {
	// TODO Auto-generated method stub
	
}

@Override
public void init(FilterConfig arg0) throws ServletException {
	// TODO Auto-generated method stub
	
}
}
