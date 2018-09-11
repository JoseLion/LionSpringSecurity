package com.lionware.lionspringsecurity;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.io.IOUtils;

public class XSSRequestWrapper extends HttpServletRequestWrapper {
	private byte[] rawData;
	private HttpServletRequest request;
	private ResettableServletInputStream servletStream;

	public XSSRequestWrapper(HttpServletRequest request) throws IOException {
		super(request);
		this.request = request;
		this.servletStream = new ResettableServletInputStream(this.request.getInputStream());
	}
	
	public void resetInputStream(byte[] newRawData) {
		rawData = newRawData;
		servletStream.stream = new ByteArrayInputStream(rawData);
	}
	
	@Override
	public ServletInputStream getInputStream() throws IOException {
	    if (rawData == null) {
	        rawData = IOUtils.toByteArray(this.request.getInputStream());
	    }
	    
	    servletStream.stream = new ByteArrayInputStream(rawData);
	    
	    return servletStream;
	}

	@Override
	public BufferedReader getReader() throws IOException {
	    if (rawData == null) {
	        rawData = IOUtils.toByteArray(this.request.getInputStream());
	    }
	    
	    servletStream.stream = new ByteArrayInputStream(rawData);
	    return new BufferedReader(new InputStreamReader(servletStream));
	}
	
	private class ResettableServletInputStream extends ServletInputStream {
		private InputStream stream;
		
		public ResettableServletInputStream(InputStream is) {
			this.stream = is;
		}

		@Override
		public boolean isFinished() {
			return true;
		}

		@Override
		public boolean isReady() {
			return true;
		}

		@Override
		public void setReadListener(ReadListener listener) {
			
		}

		@Override
		public int read() throws IOException {
			return stream.read();
		}
	}
}
