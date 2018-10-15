package com.lionware.lionspringsecurity.filters;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;

import com.lionware.lionspringsecurity.core.LionSecurityConst;
import com.lionware.lionspringsecurity.properties.LionSecurityProperties;
import com.lionware.lionspringsecurity.properties.PropertiesConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest(classes={ PropertiesConfiguration.class, CorsFilter.class })
public class CorsFilterTest {
	
	@Autowired
	private LionSecurityProperties securityProperties;
	
	@Autowired
	private CorsFilter corsFilter;
	
	@Test
	public void whenAllowedOriginsIsDisabled_sholdNotSetThoseHeaders() {
		MockHttpServletRequest mockRequest = new MockHttpServletRequest();
		MockHttpServletResponse mockResponse = new MockHttpServletResponse();
		
		corsFilter.setHeadersOnResponse(mockRequest, mockResponse);
		
		assertThat(mockRequest).isNotNull();
		assertThat(mockResponse.getHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN)).isNull();
		assertThat(mockResponse.getHeader(HttpHeaders.VARY)).isNull();
	}
	
	@Test
	public void whenOriginDoesntMatch_sholdNotSetThoseHeaders() {
		MockHttpServletRequest mockRequest = new MockHttpServletRequest();
		MockHttpServletResponse mockResponse = new MockHttpServletResponse();
		mockRequest.addHeader(HttpHeaders.ORIGIN, "http://localhost");
		
		securityProperties.setAllowedOrigins(Arrays.asList("https://www.my-mock-site.com"));
		corsFilter.setHeadersOnResponse(mockRequest, mockResponse);
		
		assertThat(mockRequest).isNotNull();
		assertThat(mockResponse.getHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN)).isNull();
		assertThat(mockResponse.getHeader(HttpHeaders.VARY)).isNull();
	}
	
	@Test
	public void whenOriginMatch_sholdSetThoseHeaders() {
		MockHttpServletRequest mockRequest = new MockHttpServletRequest();
		MockHttpServletResponse mockResponse = new MockHttpServletResponse();
		mockRequest.addHeader(HttpHeaders.ORIGIN, "https://www.my-mock-site.com");
		
		securityProperties.setAllowedOrigins(Arrays.asList("https://www.my-mock-site.com"));
		corsFilter.setHeadersOnResponse(mockRequest, mockResponse);
		
		assertThat(mockRequest).isNotNull();
		assertThat(mockResponse.getHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN)).isEqualTo("https://www.my-mock-site.com");
		assertThat(mockResponse.getHeader(HttpHeaders.VARY)).isEqualTo(HttpHeaders.ORIGIN);
	}
	
	@Test
	public void whenDefaultProperties_shouldSetDefaultAccessControlHeaders() {
		MockHttpServletRequest mockRequest = new MockHttpServletRequest();
		MockHttpServletResponse mockResponse = new MockHttpServletResponse();
		
		corsFilter.setHeadersOnResponse(mockRequest, mockResponse);
		
		assertThat(mockResponse).isNotNull();
		assertThat(mockResponse.getHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS)).isEqualTo("true");
		assertThat(mockResponse.getHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS)).isEqualTo("Accept, Authorization, Content-Encoding, Content-Type, Origin, X-Requested-With, " + LionSecurityConst.CSRF_HEADER_NAME);
		assertThat(mockResponse.getHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS)).isEqualTo("OPTIONS, GET, POST, PUT, DELETE");
		assertThat(mockResponse.getHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS)).isEqualTo("Content-Encoding, Content-Type, " + LionSecurityConst.CSRF_HEADER_NAME);
		assertThat(mockResponse.getHeader(HttpHeaders.ACCESS_CONTROL_MAX_AGE)).isEqualTo("3600");
	}
	
	@Test
	public void whenRequestIsNotCompressed_shouldprocessRequest() throws IOException {
		MockHttpServletRequest mockRequest = new MockHttpServletRequest();
		mockRequest.setContent("Some content body".getBytes());
		HttpServletRequest request = mockRequest;
		
		request = corsFilter.processRequest(request);
		
		assertThat(request).isNotNull();
		assertThat(request.getInputStream()).isNotNull();
		assertThat(IOUtils.toString(request.getInputStream(), StandardCharsets.UTF_8)).isEqualTo("Some content body");
	}
	
	@Test
	public void whenRequestIsCompressed_shouldprocessRequest() throws IOException {
		MockHttpServletRequest mockRequest = new MockHttpServletRequest();
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		GZIPOutputStream gzipOutputStream = new GZIPOutputStream(outputStream);
		gzipOutputStream.write("Some content body".getBytes());
		gzipOutputStream.close();
		
		mockRequest.setContent(outputStream.toByteArray());
		mockRequest.addHeader("Content-Encoding", "gzip");
		HttpServletRequest request = mockRequest;
		
		request = corsFilter.processRequest(request);
		
		assertThat(request).isNotNull();
		assertThat(request.getInputStream()).isNotNull();
		assertThat(IOUtils.toString(request.getInputStream(), StandardCharsets.UTF_8)).isEqualTo("Some content body");
	}
	
	@Test(expected=ZipException.class)
	public void whenRequestIsCompressed_andBodyIsNotGZip_shouldprocessRequest() throws IOException {
		MockHttpServletRequest mockRequest = new MockHttpServletRequest();
		mockRequest.addHeader("Content-Encoding", "gzip");
		mockRequest.setContent("Some content body".getBytes());
		HttpServletRequest request = mockRequest;
		
		corsFilter.processRequest(request);
	}
}
