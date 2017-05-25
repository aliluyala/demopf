package com.ydy258.ydy.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ydy258.ydy.Setting;
import com.ydy258.ydy.util.SettingUtils;

@Component("siteStatusFilter")
public class SiteStatusFilter extends OncePerRequestFilter {
	/** Ĭ�Ϻ���URL */
	private static final String[] DEFAULT_IGNORE_URL_PATTERNS = new String[] { "/admin/**" };

	/** Ĭ���ض���URL */
	private static final String DEFAULT_REDIRECT_URL = "/common/site_close.jhtml";

	/** antPathMatcher */
	private static AntPathMatcher antPathMatcher = new AntPathMatcher();

	/** ����URL */
	private String[] ignoreUrlPatterns = DEFAULT_IGNORE_URL_PATTERNS;

	/** �ض���URL */
	private String redirectUrl = DEFAULT_REDIRECT_URL;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		Setting setting = SettingUtils.get();
		if (setting.getIsSiteEnabled()) {
			filterChain.doFilter(request, response);
		} else {
			String path = request.getServletPath();
			if (path.equals(redirectUrl)) {
				filterChain.doFilter(request, response);
				return;
			}
			if (ignoreUrlPatterns != null) {
				for (String ignoreUrlPattern : ignoreUrlPatterns) {
					if (antPathMatcher.match(ignoreUrlPattern, path)) {
						filterChain.doFilter(request, response);
						return;
					}
				}
			}
			response.sendRedirect(request.getContextPath() + redirectUrl);
		}
	}

	/**
	 * ��ȡ����URL
	 * 
	 * @return ����URL
	 */
	public String[] getIgnoreUrlPatterns() {
		return ignoreUrlPatterns;
	}

	/**
	 * ���ú���URL
	 * 
	 * @param ignoreUrlPatterns
	 *            ����URL
	 */
	public void setIgnoreUrlPatterns(String[] ignoreUrlPatterns) {
		this.ignoreUrlPatterns = ignoreUrlPatterns;
	}

	/**
	 * ��ȡ�ض���URL
	 * 
	 * @return �ض���URL
	 */
	public String getRedirectUrl() {
		return redirectUrl;
	}

	/**
	 * �����ض���URL
	 * 
	 * @param redirectUrl
	 *            �ض���URL
	 */
	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}
}