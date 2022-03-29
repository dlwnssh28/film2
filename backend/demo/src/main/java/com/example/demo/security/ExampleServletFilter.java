package com.example.demo.security;

import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/* ������ - ������ ������� ����. */
public class ExampleServletFilter extends HttpFilter {

	private TokenProvider tokenProvider;

	@Override
	protected void doFilter(HttpServletRequest request,
	                        HttpServletResponse response,
	                        FilterChain filterChain)
					throws IOException, ServletException {
		try {
			final String token = parseBearerToken(request);

			if (token != null && !token.equalsIgnoreCase("null")) {
				// userId ��������. ���� �� ��� ���� ó�� �ȴ�.
				String userId = tokenProvider.validateAndGetUserId(token);

				// ���� ServletFilter ����
				filterChain.doFilter(request, response);
			}
		} catch (Exception e) {
			// ���� �߻��� response�� 403 Forbidden���� ����.
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		}
	}

	private String parseBearerToken(HttpServletRequest request) {
		// Http ������Ʈ�� ����� �Ľ��� Bearer ��ū�� �����Ѵ�.
		String bearerToken = request.getHeader("Authorization");

		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}
}