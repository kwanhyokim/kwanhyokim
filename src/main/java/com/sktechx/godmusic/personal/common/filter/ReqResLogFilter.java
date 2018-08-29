package com.sktechx.godmusic.personal.common.filter;

import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.io.TeeOutputStream;
import org.slf4j.MDC;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 * Created by Kobe.
 *
 * @author Kobe/최훈영/SKTECHX (hunyoung.choi@sk.com)
 * @date 2018. 8. 29.
 * @time PM 5:29
 */
@Slf4j
@Component
public class ReqResLogFilter implements Filter {
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		try {
			HttpServletRequest httpServletRequest = (HttpServletRequest)request;
			HttpServletResponse httpServletResponse = (HttpServletResponse)response;

			httpServletResponse.setHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);

//            lastLineLog.info("[REST URI] " + httpServletRequest.getRequestURI());

			Map<String, String> requestMap = this.getTypesafeRequestMap(httpServletRequest);
			BufferedRequestWrapper bufferedReqest = new BufferedRequestWrapper(httpServletRequest);
			BufferedResponseWrapper bufferedResponse = new BufferedResponseWrapper(httpServletResponse);

			long startTime = System.currentTimeMillis();

			chain.doFilter (bufferedReqest, bufferedResponse);

			long elapsed = System.currentTimeMillis() - startTime;

			if (requestMap.get("password") != null) {
				requestMap.put("password", "********");
			}

			final StringBuilder logMessage = new StringBuilder("REST Request - ")
					.append("[").append(elapsed).append("ms] ");

			if (!StringUtils.isEmpty(httpServletRequest.getMethod())) {
				logMessage.append("[HTTP METHOD:")
						.append(httpServletRequest.getMethod())
						.append("] ");
			}

			if (!StringUtils.isEmpty(httpServletRequest.getRequestURI())) {
				logMessage.append("[PATH INFO:")
						.append(httpServletRequest.getRequestURI())
						.append("] ");
			}

			if (getHeadersInfo(httpServletRequest) != null && getHeadersInfo(httpServletRequest).size() > 0) {
				logMessage.append("[REQUEST HEADER:")
						.append(getHeadersInfo(httpServletRequest))
						.append("] ");
			}

			if (requestMap != null && requestMap.size() > 0) {
				logMessage.append("[REQUEST PARAMETERS:")
						.append(requestMap)
						.append("] ");
			}

			if (!StringUtils.isEmpty(bufferedReqest.getRequestBody())) {
				String reqBody = bufferedReqest.getRequestBody();

				if (reqBody.contains("password")) {
					reqBody = reqBody.replaceAll("password\":\"[^\"]*", "password\":\"******");
				}

				logMessage.append("[REQUEST BODY:")
						.append(reqBody)
						.append("] ");
			}

			if (!StringUtils.isEmpty(httpServletRequest.getRemoteAddr())) {
				logMessage.append("[REMOTE ADDRESS:")
						.append(httpServletRequest.getRemoteAddr())
						.append("] ");
			}

			if (!StringUtils.isEmpty(bufferedResponse.getContent())) {
				logMessage.append("[RESPONSE:")
						.append( bufferedResponse.getContent())
						.append("]");
			}

			log.info(logMessage.toString());
		} catch( Throwable a ) {
//            lastLineLog.error(a.getMessage());
			log.error(a.getMessage());
		}
	}

	private Map<String, String> getHeadersInfo(HttpServletRequest request) {

		Map<String, String> map = new HashMap<String, String>();

		Enumeration headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String key = (String) headerNames.nextElement();
			if ("X-ANNE-INFOX-CLIENT-UID-ACCEPTUSER-AGENT".contains(key.toUpperCase())) {
				String value = request.getHeader(key);
				map.put(key, value);
			}
		}

		return map;
	}


	private Map<String, String> getTypesafeRequestMap(HttpServletRequest request) {
		Map<String, String> typesafeRequestMap = new HashMap<String, String>();
		Enumeration<?> requestParamNames = request.getParameterNames();
		while (requestParamNames.hasMoreElements()) {
			String requestParamName = (String)requestParamNames.nextElement();
			String requestParamValue = request.getParameter(requestParamName);
			typesafeRequestMap.put(requestParamName, requestParamValue);
		}
		return typesafeRequestMap;
	}


	@Override
	public void destroy() {
	}


	private static final class BufferedRequestWrapper extends HttpServletRequestWrapper {

		private ByteArrayInputStream bais = null;
		private ByteArrayOutputStream baos = null;
		private BufferedServletInputStream bsis = null;
		private byte[] buffer = null;


		public BufferedRequestWrapper(HttpServletRequest req) throws IOException {
			super(req);
			// Read InputStream and store its content in a buffer.
			InputStream is = req.getInputStream();
			this.baos = new ByteArrayOutputStream();
			byte buf[] = new byte[1024];
			int letti;
			while ((letti = is.read(buf)) > 0) {
				this.baos.write(buf, 0, letti);
			}
			this.buffer = this.baos.toByteArray();
		}


		@Override
		public ServletInputStream getInputStream() {
			this.bais = new ByteArrayInputStream(this.buffer);
			this.bsis = new BufferedServletInputStream(this.bais);
			return this.bsis;
		}



		String getRequestBody() throws IOException  {
			BufferedReader reader = new BufferedReader(new InputStreamReader(this.getInputStream()));
			String line = null;
			StringBuilder inputBuffer = new StringBuilder();
			do {
				line = reader.readLine();
				if (null != line) {
					inputBuffer.append(line.trim());
				}
			} while (line != null);
			reader.close();
			return inputBuffer.toString().trim();
		}

	}


	private static final class BufferedServletInputStream extends ServletInputStream {

		private ByteArrayInputStream bais;

		public BufferedServletInputStream(ByteArrayInputStream bais) {
			this.bais = bais;
		}

		@Override
		public boolean isReady() {
			return false;
		}

		@Override
		public boolean isFinished() {
			return false;
		}

		@Override
		public void setReadListener(ReadListener readListener) {

		}

		@Override
		public int available() {
			return this.bais.available();
		}

		@Override
		public int read() {
			return this.bais.read();
		}

		@Override
		public int read(byte[] buf, int off, int len) {
			return this.bais.read(buf, off, len);
		}


	}

	public class TeeServletOutputStream extends ServletOutputStream {

		private final TeeOutputStream targetStream;

		public TeeServletOutputStream( OutputStream one, OutputStream two ) {
			targetStream = new TeeOutputStream( one, two);
		}

		@Override
		public void setWriteListener(WriteListener writeListener) {

		}

		@Override
		public boolean isReady() {
			return false;
		}

		@Override
		public void write(int arg0) throws IOException {
			this.targetStream.write(arg0);
		}

		public void flush() throws IOException {
			super.flush();
			this.targetStream.flush();
		}

		public void close() throws IOException {
			super.close();
			this.targetStream.close();
		}
	}



	public class BufferedResponseWrapper implements HttpServletResponse {

		HttpServletResponse original;
		TeeServletOutputStream tee;
		ByteArrayOutputStream bos;

		@Override
		public Collection<String> getHeaderNames() {
			return null;
		}

		@Override
		public int getStatus() {
			return original.getStatus();
		}

		@Override
		public void setContentLengthLong(long l) {

		}

		@Override
		public String getHeader(String s) {
			return null;
		}

		@Override
		public Collection<String> getHeaders(String s) {
			return null;
		}

		public BufferedResponseWrapper(HttpServletResponse response) {
			original = response;
		}

		public String getContent() {
			return bos == null ? "" : bos.toString();
		}

		public PrintWriter getWriter() throws IOException {
			return original.getWriter();
		}

		public ServletOutputStream getOutputStream() throws IOException {
			if( tee == null ){
				bos = new ByteArrayOutputStream();
				tee = new TeeServletOutputStream( original.getOutputStream(), bos );
			}
			return tee;

		}

		@Override
		public String getCharacterEncoding() {
			return original.getCharacterEncoding();
		}

		@Override
		public String getContentType() {
			return original.getContentType();
		}

		@Override
		public void setCharacterEncoding(String charset) {
			original.setCharacterEncoding(charset);
		}

		@Override
		public void setContentLength(int len) {
			original.setContentLength(len);
		}

		@Override
		public void setContentType(String type) {
			original.setContentType(type);
		}

		@Override
		public void setBufferSize(int size) {
			original.setBufferSize(size);
		}

		@Override
		public int getBufferSize() {
			return original.getBufferSize();
		}

		@Override
		public void flushBuffer() throws IOException {
			tee.flush();
		}

		@Override
		public void resetBuffer() {
			original.resetBuffer();
		}

		@Override
		public boolean isCommitted() {
			return original.isCommitted();
		}

		@Override
		public void reset() {
			original.reset();
		}

		@Override
		public void setLocale(Locale loc) {
			original.setLocale(loc);
		}

		@Override
		public Locale getLocale() {
			return original.getLocale();
		}

		@Override
		public void addCookie(Cookie cookie) {
			original.addCookie(cookie);
		}

		@Override
		public boolean containsHeader(String name) {
			return original.containsHeader(name);
		}

		@Override
		public String encodeURL(String url) {
			return original.encodeURL(url);
		}

		@Override
		public String encodeRedirectURL(String url) {
			return original.encodeRedirectURL(url);
		}

		@SuppressWarnings("deprecation")
		@Override
		public String encodeUrl(String url) {
			return original.encodeUrl(url);
		}

		@SuppressWarnings("deprecation")
		@Override
		public String encodeRedirectUrl(String url) {
			return original.encodeRedirectUrl(url);
		}

		@Override
		public void sendError(int sc, String msg) throws IOException {
			original.sendError(sc, msg);
		}

		@Override
		public void sendError(int sc) throws IOException {
			original.sendError(sc);
		}

		@Override
		public void sendRedirect(String location) throws IOException {
			original.sendRedirect(location);
		}

		@Override
		public void setDateHeader(String name, long date) {
			original.setDateHeader(name, date);
		}

		@Override
		public void addDateHeader(String name, long date) {
			original.addDateHeader(name, date);
		}

		@Override
		public void setHeader(String name, String value) {
			original.setHeader(name, value);
		}

		@Override
		public void addHeader(String name, String value) {
			original.addHeader(name, value);
		}

		@Override
		public void setIntHeader(String name, int value) {
			original.setIntHeader(name, value);
		}

		@Override
		public void addIntHeader(String name, int value) {
			original.addIntHeader(name, value);
		}

		@Override
		public void setStatus(int sc) {
			original.setStatus(sc);
		}

		@SuppressWarnings("deprecation")
		@Override
		public void setStatus(int sc, String sm) {
			original.setStatus(sc, sm);
		}

	}
}
