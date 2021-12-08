//package com.sysage.bookinfo.controller;
//
//import java.io.IOException;
//import java.util.Map;
//
//import org.json.JSONObject;
//import org.apache.http.client.methods.CloseableHttpResponse;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.util.EntityUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestHeader;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.google.gson.Gson;
//import com.sysage.bookinfo.vo.RatingVO;
//import com.sysage.bookinfo.vo.ReviewsDetailVO;
//import com.sysage.bookinfo.vo.ReviewsVO;
//
//
//@RestController
//@RequestMapping("/")
//public class ReviewsController {
//
//	private final static Boolean ratings_enabled = Boolean.valueOf(System.getenv("ENABLE_RATINGS"));
//	private final static String services_domain = System.getenv("SERVICES_DOMAIN") == null ? "" : ("." + System.getenv("SERVICES_DOMAIN"));
//	private final static String ratings_hostname = System.getenv("RATINGS_HOSTNAME") == null ? "ratings" : System.getenv("RATINGS_HOSTNAME");
//	private final static String ratings_service = "http://" + ratings_hostname + services_domain + ":9080/ratings";
//	// HTTP headers to propagate for distributed tracing are documented at
//	// https://istio.io/docs/tasks/telemetry/distributed-tracing/overview/#trace-context-propagation
//	private final static String[] headers_to_propagate = {
//			// All applications should propagate x-request-id. This header is
//			// included in access log statements and is used for consistent trace
//			// sampling and log sampling decisions in Istio.
//			"x-request-id",
//
//			// Lightstep tracing header. Propagate this if you use lightstep tracing
//			// in Istio (see
//			// https://istio.io/latest/docs/tasks/observability/distributed-tracing/lightstep/)
//			// Note: this should probably be changed to use B3 or W3C TRACE_CONTEXT.
//			// Lightstep recommends using B3 or TRACE_CONTEXT and most application
//			// libraries from lightstep do not support x-ot-span-context.
//			"x-ot-span-context",
//
//			// Datadog tracing header. Propagate these headers if you use Datadog
//			// tracing.
//			"x-datadog-trace-id", "x-datadog-parent-id", "x-datadog-sampling-priority",
//
//			// W3C Trace Context. Compatible with OpenCensusAgent and Stackdriver Istio
//			// configurations.
//			"traceparent", "tracestate",
//
//			// Cloud trace context. Compatible with OpenCensusAgent and Stackdriver Istio
//			// configurations.
//			"x-cloud-trace-context",
//
//			// Grpc binary trace context. Compatible with OpenCensusAgent nad
//			// Stackdriver Istio configurations.
//			"grpc-trace-bin",
//
//			// b3 trace headers. Compatible with Zipkin, OpenCensusAgent, and
//			// Stackdriver Istio configurations. Commented out since they are
//			// propagated by the OpenTracing tracer above.
//			"x-b3-traceid", "x-b3-spanid", "x-b3-parentspanid", "x-b3-sampled", "x-b3-flags",
//
//			// Application-specific headers to forward.
//			"end-user", "user-agent", };
//
//	Gson gson = new Gson();
//
//
//
//	@Autowired
//	Details details;
//	
//	@GetMapping(value = "/health", produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity<String> health() {
//		String ss = "{\"status\": \"Reviews is healthy\"}";
//		ResponseEntity<String> rtn = new ResponseEntity<String>(ss, HttpStatus.OK);
//
//		return rtn;
//	}
//
//	@GetMapping(value = "/reviews/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
//	public String bookReviewsById(@PathVariable("productId") int productId) {
//		String s = details.getProductReviews(productId);
//		return s;
//	}
//
//}
