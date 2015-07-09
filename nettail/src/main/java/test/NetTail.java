package test;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClients;

public class NetTail {

	protected HttpClient httpClient;
	protected HttpClientContext localContext;
	protected HttpHost target;
	protected String fileUrl;

	protected String lastModified;
	protected Integer contentLength;

	protected String prevLastModified;
	protected Integer prevContentLength;

	protected boolean firstRun = true;

	public static void main(String[] args) throws Exception {

		if (args.length == 0) {
			System.err.println("usage: nettail <url> [username] [password] [firstRead] [sleepTime]");
			System.err.println("example: nettail http://some.host.com/logs/error.log username pass123");
			System.exit(-1);
		}

		String url = args[0];
		String username = null;
		String password = null;
		Integer sleepTime = 1000;
		Integer firstRead = 2000;
		if (args.length >= 3) {
			username = args[1];
			password = args[2];
		}
		if (args.length >= 4) {
			firstRead = Integer.parseInt(args[3]);
		}
		if (args.length >= 5) {
			sleepTime = Integer.parseInt(args[4]);
		}

		NetTail tail = new NetTail();
		tail.setupHttpClient(username, password, url);

		while (true) {
			Thread.sleep(sleepTime);
			tail.readHeaders();
			Integer bytesToRead = tail.getNewByteLength();
			if (tail.firstRun) {
				if (tail.contentLength < firstRead)
					firstRead = tail.contentLength;

				List<String> linesToPrint = tail.readRange(tail.contentLength - firstRead, tail.contentLength);
				for (String line : linesToPrint)
					System.out.println(line);
				
				tail.firstRun = false;
			} else if (bytesToRead > 0) {
				List<String> linesToPrint = tail.readRange(tail.prevContentLength, tail.contentLength);
				for (String line : linesToPrint)
					System.out.println(line);
				
			}

		}

	}

	protected List<String> readRange(Integer from, Integer to) throws Exception {
		HttpGet get = new HttpGet(fileUrl);
		get.setHeader("Range", "bytes=" + from + "-" + to);
		HttpResponse response = httpClient.execute(target, get, localContext);
		InputStream data = response.getEntity().getContent();
		return IOUtils.readLines(data);
	}

	protected String getHeader(String name, Header[] headers) throws Exception {
		for (int i = 0; i < headers.length; i++)
			if (name.equals(headers[i].getName()))
				return headers[i].getValue();
		return null;
	}

	protected Integer getNewByteLength() {
		if (this.prevContentLength != null && this.contentLength != null)
			return this.contentLength - this.prevContentLength;
		else
			return 0;
	}

	protected void readHeaders() throws Exception {
		HttpHead head = new HttpHead(fileUrl);

		HttpResponse response = httpClient.execute(target, head, localContext);
		Header[] headers = response.getAllHeaders();

		if (lastModified != null)
			prevLastModified = lastModified;
		if (contentLength != null)
			prevContentLength = contentLength;

		lastModified = getHeader("Last-Modified", headers);
		String length = getHeader("Content-Length", headers);
		contentLength = Integer.parseInt(length);
	}

	protected void setupHttpClient(String username, String password,
			String fileUrl) throws Exception {
		URL url = new URL(fileUrl);

		String host = url.getHost();

		this.fileUrl = fileUrl;
		this.target = new HttpHost(host);

		if (username != null && password != null) {
			AuthScope authScope = new AuthScope(target);
			Credentials credentials = new UsernamePasswordCredentials(username, password);
			CredentialsProvider credsProvider = new BasicCredentialsProvider();
			credsProvider.setCredentials(authScope, credentials);

			this.httpClient = HttpClients
						.custom()
						.setDefaultCredentialsProvider(credsProvider)
						.build();

			AuthCache authCache = new BasicAuthCache();

			BasicScheme basicAuth = new BasicScheme();
			authCache.put(this.target, basicAuth);

			this.localContext = HttpClientContext.create();
			this.localContext.setAuthCache(authCache);
		} else {
			this.httpClient = HttpClients
						.custom()
						.build();
		}
	}
}
