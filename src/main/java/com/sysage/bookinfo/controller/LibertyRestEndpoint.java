//package com.sysage.bookinfo.controller;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//@Controller
//public class LibertyRestEndpoint {
//
//	@RequestMapping("/www")
//	public String greeting(@RequestParam(value = "title", required = false, defaultValue = "aaaaa") String title,
//			Model model) {
//		model.addAttribute("name", title);
//		return "productpage";
//	}
//}

/*******************************************************************************
 * Copyright (c) 2017 Istio Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.sysage.bookinfo.controller;

import java.io.IOException;
import java.util.Map;

import org.json.JSONObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*******************************************************************************
 * Copyright (c) 2017 Istio Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *******************************************************************************/

@RestController
@RequestMapping("/")
public class LibertyRestEndpoint {

	private final static Boolean ratings_enabled = Boolean.valueOf(System.getenv("ENABLE_RATINGS"));
	private final static String star_color = System.getenv("STAR_COLOR") == null ? "black" : System.getenv("STAR_COLOR");
	private final static String services_domain = System.getenv("SERVICES_DOMAIN") == null ? "" : ("." + System.getenv("SERVICES_DOMAIN"));
	private final static String ratings_hostname = System.getenv("RATINGS_HOSTNAME") == null ? "ratings" : System.getenv("RATINGS_HOSTNAME");
	private final static String ratings_service = "http://" + ratings_hostname + services_domain + ":9080/ratings";
	// HTTP headers to propagate for distributed tracing are documented at
	// https://istio.io/docs/tasks/telemetry/distributed-tracing/overview/#trace-context-propagation
	private final static String[] headers_to_propagate = {
			// All applications should propagate x-request-id. This header is
			// included in access log statements and is used for consistent trace
			// sampling and log sampling decisions in Istio.
			"x-request-id",

			// Lightstep tracing header. Propagate this if you use lightstep tracing
			// in Istio (see
			// https://istio.io/latest/docs/tasks/observability/distributed-tracing/lightstep/)
			// Note: this should probably be changed to use B3 or W3C TRACE_CONTEXT.
			// Lightstep recommends using B3 or TRACE_CONTEXT and most application
			// libraries from lightstep do not support x-ot-span-context.
			"x-ot-span-context",

			// Datadog tracing header. Propagate these headers if you use Datadog
			// tracing.
			"x-datadog-trace-id", "x-datadog-parent-id", "x-datadog-sampling-priority",

			// W3C Trace Context. Compatible with OpenCensusAgent and Stackdriver Istio
			// configurations.
			"traceparent", "tracestate",

			// Cloud trace context. Compatible with OpenCensusAgent and Stackdriver Istio
			// configurations.
			"x-cloud-trace-context",

			// Grpc binary trace context. Compatible with OpenCensusAgent nad
			// Stackdriver Istio configurations.
			"grpc-trace-bin",

			// b3 trace headers. Compatible with Zipkin, OpenCensusAgent, and
			// Stackdriver Istio configurations. Commented out since they are
			// propagated by the OpenTracing tracer above.
			"x-b3-traceid", "x-b3-spanid", "x-b3-parentspanid", "x-b3-sampled", "x-b3-flags",

			// Application-specific headers to forward.
			"end-user", "user-agent", };

	private String getJsonResponse(String productId, int starsReviewer1, int starsReviewer2) {
		String result = "{";
		result += "\"id\": \"" + productId + "\",";
		result += "\"reviews\": [";

		// reviewer 1:
		result += "{";
		result += "  \"reviewer\": \"Reviewer1\",";
		result += "  \"text\": \"An extremely entertaining play by Shakespeare. The slapstick humour is refreshing!\"";
		if (ratings_enabled) {
			if (starsReviewer1 != -1) {
				result += ", \"rating\": {\"stars\": " + starsReviewer1 + ", \"color\": \"" + star_color + "\"}";
			} else {
				result += ", \"rating\": {\"error\": \"Ratings service is currently unavailable\"}";
			}
		}
		result += "},";

		// reviewer 2:
		result += "{";
		result += "  \"reviewer\": \"Reviewer2\",";
		result += "  \"text\": \"Absolutely fun and entertaining. The play lacks thematic depth when compared to other plays by Shakespeare.\"";
		if (ratings_enabled) {
			if (starsReviewer2 != -1) {
				result += ", \"rating\": {\"stars\": " + starsReviewer2 + ", \"color\": \"" + star_color + "\"}";
			} else {
				result += ", \"rating\": {\"error\": \"Ratings service is currently unavailable\"}";
			}
		}
		result += "}";

		result += "]";
		result += "}";

		return result;
	}

	private JSONObject getRatings(String productId, Map<String, String> requestHeaders) {
//		ClientBuilder cb = ClientBuilder.newBuilder();
//		Integer timeout = star_color.equals("black") ? 10000 : 2500;
//		cb.property("com.ibm.ws.jaxrs.client.connection.timeout", timeout);
//		cb.property("com.ibm.ws.jaxrs.client.receive.timeout", timeout);
//		Client client = cb.build();
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet(ratings_service + "/" + productId);
		httpget.setHeader("Content-Type", "application/json");

//		WebTarget ratingsTarget = client.target(ratings_service + "/" + productId);
//		Invocation.Builder builder = ratingsTarget.request(MediaType.APPLICATION_JSON);
		for (String header : headers_to_propagate) {
//			String value = requestHeaders.getHeaderString(header);
			String value = requestHeaders.get(header);
			if (value != null) {
				httpget.setHeader(header, value);
//				builder.header(header, value);
			}
		}
		CloseableHttpResponse httpResponse = null;
		try {
			httpResponse = httpclient.execute(httpget);
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			System.out.println("statusCode: "+statusCode);
//			int statusCode = r.getStatusInfo().getStatusCode();
//			if (statusCode == HttpStatus.OK.value()) {
			if (statusCode == 200 && httpResponse.getEntity() != null) {
				String content = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
				JSONObject jsonObject = new JSONObject(content);
				return jsonObject;
			} else {
				System.out.println("Error: unable to contact " + ratings_service + " got status of " + statusCode);
				return null;
			}
		} catch (Exception e) {
			System.err.println("Error: unable to contact " + ratings_service + " got exception " + e);
			return null;
		} finally {
			if (httpclient != null) {
				try {
					httpclient.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (httpResponse != null) {
				try {
					httpResponse.close();
					httpclient.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	@GetMapping(value = "/health", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> health() {
		String ss = "{\"status\": \"Reviews is healthy\"}";
		ResponseEntity<String> rtn = new ResponseEntity<String>(ss, HttpStatus.OK);

		return rtn;
	}

	@GetMapping(value = "/reviews/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> bookReviewsById(@PathVariable("productId") int productId,
			@RequestHeader Map<String, String> requestHeaders) {
//	public ResponseEntity<String> bookReviewsById(@RequestParam("productId") int productId,
//			@RequestHeader Map<String, String> requestHeaders) {
		int starsReviewer1 = -1;
		int starsReviewer2 = -1;
		System.err.println(ratings_service);
		System.err.println(ratings_hostname);
		System.err.println(services_domain);
		if (ratings_enabled) {
			JSONObject ratingsResponse = getRatings(Integer.toString(productId), requestHeaders);
			if (ratingsResponse != null) {
				if (ratingsResponse.has("ratings")) {
					JSONObject ratings = ratingsResponse.getJSONObject("ratings");
					if (ratings.has("Reviewer1")) {
						starsReviewer1 = ratings.getInt("Reviewer1");
					}
					if (ratings.has("Reviewer2")) {
						starsReviewer2 = ratings.getInt("Reviewer2");
					}
				}
			}
		}

		String jsonResStr = getJsonResponse(Integer.toString(productId), starsReviewer1, starsReviewer2);
		ResponseEntity<String> rtn = new ResponseEntity<String>(jsonResStr, HttpStatus.OK);
		return rtn;
//		return Response.ok().type(MediaType.APPLICATION_JSON).entity(jsonResStr).build();
	}

}
