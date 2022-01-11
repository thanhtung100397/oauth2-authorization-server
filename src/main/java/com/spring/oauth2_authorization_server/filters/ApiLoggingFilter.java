package com.spring.oauth2_authorization_server.filters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.oauth2_authorization_server.constants.StringConstants;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.*;
import java.util.*;

@Order(1)
public class ApiLoggingFilter implements Filter {
    private static Logger logger = LoggerFactory.getLogger(ApiLoggingFilter.class);

    private ObjectMapper objectMapper;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String logID = UUID.randomUUID().toString();
        request.setAttribute(StringConstants.ID, logID);
        request = logRequest(logID, request);
        logResponse(logID, request, response, chain);
    }

    private ServletRequest logRequest(String logID, ServletRequest request) throws IOException {
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            ResettableStreamHttpServletRequest wrappedRequest = new ResettableStreamHttpServletRequest(httpRequest);
            String bodyString = IOUtils.toString(wrappedRequest.getReader());
            wrappedRequest.resetInputStream();
            try {
                JsonNode jsonNode = objectMapper.readValue(bodyString, JsonNode.class);
                bodyString = jsonNode.toString();
            } catch (Exception e) {
                bodyString = "{}";
            }

            Map<String, String> headers = new HashMap<>();
            Enumeration<String> headerNames = httpRequest.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                headers.put(headerName, httpRequest.getHeader(headerName));
            }
            String headerString;
            try {
                headerString = objectMapper.writeValueAsString(headers);
            } catch (JsonProcessingException e) {
                headerString = "{}";
            }
            String method = httpRequest.getMethod();
            String route = httpRequest.getServletPath();

            logger.info("{\"request\": {\"id\":\"{}\",\"client_ip\":\"{}\",\"host\": \"{}\", \"method\": \"{}\", \"route\": \"{}\", \"header\":{}, \"body\": {}}}",
                    logID,
                    request.getRemoteAddr(),
                    request.getServerName(),
                    method,
                    route,
                    headerString,
                    bodyString);

            return wrappedRequest;
        }
        return request;
    }

    private void logResponse(String logID,
                             ServletRequest request, ServletResponse response,
                             FilterChain chain) throws IOException, ServletException {
        if (response instanceof HttpServletResponse) {
            OutputStream outputStream = response.getOutputStream();
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            ResponseWrapper wrapper = new ResponseWrapper(httpResponse);

            chain.doFilter(request, wrapper);

            byte[] bodyRaw = wrapper.getData();
            String bodyString = new String(bodyRaw);
            if (bodyString.isEmpty()) {
                bodyString = "{}";
            }
            outputStream.write(bodyRaw);
            outputStream.close();

            Map<String, String> headers = new HashMap<>();
            Collection<String> headerNames = httpResponse.getHeaderNames();
            for (String headerName : headerNames) {
                headers.put(headerName, httpResponse.getHeader(headerName));
            }
            String headerString;
            try {
                headerString = objectMapper.writeValueAsString(headers);
            } catch (JsonProcessingException e) {
                headerString = "{}";
            }

            String method = "";
            String route = "";
            if (request instanceof HttpServletRequest) {
                HttpServletRequest httpRequest = (HttpServletRequest) request;
                method = httpRequest.getMethod();
                route = httpRequest.getServletPath();
            }

            logger.info("{\"response\": {\"id\":\"{}\",\"host\": \"{}\", \"method\": \"{}\", \"route\": \"{}\", \"header\":{},\"body\": {}}}",
                    logID,
                    request.getRemoteAddr(),
                    method,
                    route,
                    headerString,
                    bodyString);
        } else {
            chain.doFilter(request, response);
        }
    }


    @Override
    public void destroy() {

    }

    public class CopyServletOutputStream extends ServletOutputStream {
        private DataOutputStream stream;
        private String copy;

        public CopyServletOutputStream(OutputStream output) {
            stream = new DataOutputStream(output);
            copy = "";
        }

        public void write(int b) throws IOException {
            stream.write(b);
            copy += b;
        }

        public void write(byte[] b) throws IOException {
            stream.write(b);
            copy += new String(b);
        }

        public void write(byte[] b, int off, int len) throws IOException {
            stream.write(b, off, len);
            copy += new String(b);
        }

        public String getCopy() {
            return copy;
        }

        @Override
        public boolean isReady() {
            return false;
        }

        @Override
        public void setWriteListener(WriteListener listener) {

        }
    }

    public class ResponseWrapper extends HttpServletResponseWrapper {
        private ByteArrayOutputStream output;
        private int contentLength;
        private String contentType;

        public ResponseWrapper(HttpServletResponse response) {
            super(response);
            output = new ByteArrayOutputStream();
        }

        public byte[] getData() {
            return output.toByteArray();
        }

        public ServletOutputStream getOutputStream() {
            return new CopyServletOutputStream(output);
        }

        public PrintWriter getWriter() {
            return new PrintWriter(getOutputStream(), true);
        }

        public void setContentLength(int length) {
            this.contentLength = length;
            super.setContentLength(length);
        }

        public int getContentLength() {
            return contentLength;
        }

        public void setContentType(String type) {
            this.contentType = type;
            super.setContentType(type);
        }


        public String getContentType() {
            return contentType;
        }
    }

    private class ResettableStreamHttpServletRequest extends HttpServletRequestWrapper {
        private byte[] rawData;
        private HttpServletRequest request;
        private ResettableServletInputStream servletStream;

        ResettableStreamHttpServletRequest(HttpServletRequest request) {
            super(request);
            this.request = request;
            this.servletStream = new ResettableServletInputStream();
        }

        public void resetInputStream() {
            servletStream.stream = new ByteArrayInputStream(rawData);
        }

        @Override
        public ServletInputStream getInputStream() throws IOException {
            if (rawData == null) {
                rawData = IOUtils.toByteArray(this.request.getReader());
                servletStream.stream = new ByteArrayInputStream(rawData);
            }
            return servletStream;
        }

        @Override
        public BufferedReader getReader() throws IOException {
            if (rawData == null) {
                rawData = IOUtils.toByteArray(this.request.getReader());
                servletStream.stream = new ByteArrayInputStream(rawData);
            }
            return new BufferedReader(new InputStreamReader(servletStream));
        }

        private class ResettableServletInputStream extends ServletInputStream {

            private InputStream stream;

            @Override
            public int read() throws IOException {
                return stream.read();
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener listener) {

            }
        }
    }
}
