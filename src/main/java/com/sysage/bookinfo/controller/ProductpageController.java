package com.sysage.bookinfo.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;

import javax.servlet.http.HttpSession;

@Controller
public class ProductpageController {

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

	private final static String services_domain = System.getenv("SERVICES_DOMAIN") == null ? "" : ("." + System.getenv("SERVICES_DOMAIN"));
	private final static String ratings_hostname = System.getenv("RATINGS_HOSTNAME") == null ? "ratings" : System.getenv("RATINGS_HOSTNAME");
	private final static String details_hostname = System.getenv("DETAILS_HOSTNAME") == null ? "details" : System.getenv("DETAILS_HOSTNAME");
	private final static String reviews_hostname = System.getenv("REVIEWS_HOSTNAME") == null ? "reviews" : System.getenv("REVIEWS_HOSTNAME");
//	private final static String details_service = "http://" + details_hostname + services_domain + ":9080";///details";
//	private final static String ratings_service = "http://" + ratings_hostname + services_domain + ":9080";///ratings";
//	private final static String reviews_service = "http://" + reviews_hostname + services_domain + ":9080";///reviews";
//	private final static String productpage_service = "http://" + ratings_hostname + services_domain + ":9080";///details";

	private static Map<String, Object> detailsMap;
	static {
		detailsMap = new LinkedHashMap<String, Object>();
		detailsMap.put("name", "http://" + details_hostname + services_domain + ":9080");
		detailsMap.put("endpoint", "details");
		detailsMap.put("children", new ArrayList<Object>());
	}
	private static Map<String, Object> ratingsMap;
	static {
		ratingsMap = new LinkedHashMap<String, Object>();
		ratingsMap.put("name", "http://" + ratings_hostname + services_domain + ":9080");
		ratingsMap.put("endpoint", "ratings");
		ratingsMap.put("children", new ArrayList<Object>());
	}
	private static Map<String, Object> reviewsMap;
	static {
		reviewsMap = new LinkedHashMap<String, Object>();
		reviewsMap.put("name", "http://" + reviews_hostname + services_domain + ":9080");
		reviewsMap.put("endpoint", "reviews");
		reviewsMap.put("children", new ArrayList<Object>(Arrays.asList(ratingsMap)));
	}
	private static Map<String, Object> productpageMap;
	static {
		productpageMap = new LinkedHashMap<String, Object>();
		productpageMap.put("name", "http://" + details_hostname + services_domain + ":9080");
		productpageMap.put("endpoint", "details");
		productpageMap.put("children", new ArrayList<Object>(Arrays.asList(detailsMap, reviewsMap)));
	}
	private static Map<String, Object> service_dict;
	static {
		service_dict = new LinkedHashMap<String, Object>();
		service_dict.put("productpage", productpageMap);
		service_dict.put("details", detailsMap);
		service_dict.put("reviews", reviewsMap);
	}

	public static void main(String[] args) {
		Gson gson = new Gson();
		StringBuilder htmlBuilder = new StringBuilder();
		htmlBuilder.append("<table class=\"table table-condensed table-bordered table-hover\">");


	}

	Gson gson = new Gson();

	public void trace() {

	}
	public void getForwardHeaders() {

	}

	@RequestMapping(value = {"/", "/index.html"}, method = RequestMethod.GET)
	public String  index(Model model) {
		String table = "<table class=\"table table-condensed table-bordered table-hover\"><tr><th>name</th><td>http://details:9080</td></tr><tr><th>endpoint</th><td>details</td></tr><tr><th>children</th><td><table class=\"table table-condensed table-bordered table-hover\"><tr><th>name</th><th>endpoint</th><th>children</th></tr><tr><td>http://details:9080</td><td>details</td><td></td></tr><tr><td>http://reviews:9080</td><td>reviews</td><td><table class=\"table table-condensed table-bordered table-hover\"><tr><th>name</th><th>endpoint</th><th>children</th></tr><tr><td>http://ratings:9080</td><td>ratings</td><td></td></tr></table></td></tr></table></td></tr></table>";
		model.addAttribute("name", "www");
		model.addAttribute("serviceTable", table);
		return "index";
	}
	
	@RequestMapping(value = "/health", method = RequestMethod.GET)
	public String health() {
		return "Product page is healthy";
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ModelAndView login(@RequestParam String username, HttpSession session) {
		session.setAttribute("user", username);
		ModelAndView mav = new ModelAndView();
		mav.setViewName("productpage");
		return mav;
	}
	
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public ModelAndView logout(HttpSession session) {
		session.invalidate();
		ModelAndView mav = new ModelAndView();
		mav.setViewName("productpage");
		return mav;
	}
	
//	async
	public void getProductReviewsIgnoreResponse() {
		
	}
	
//	async
	public void floodReviewsAsynchronously() {
		
	}
	
	
	public void floodReviews() {
		
	}
	
	@RequestMapping(value = "/productpage", method = RequestMethod.GET)
	public String front() {
//		ModelAndView mav = new ModelAndView();
//		mav.setViewName("productpage");
		return "productpage";
	}
	
//	Data providers
	public ArrayList<Map<String, String>> getProducts() {
		ArrayList<Map<String, String>> productList= new ArrayList<Map<String, String>>();
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("id", "0");
		map.put("title", "The Comedy of Errors");
		map.put("descriptionHtml", "<a href=\"https://en.wikipedia.org/wiki/The_Comedy_of_Errors\">Wikipedia Summary</a>: The Comedy of Errors is one of <b>William Shakespeare\\'s</b> early plays. It is his shortest and one of his most farcical comedies, with a major part of the humour coming from slapstick and mistaken identity, in addition to puns and word play.");
		productList.add(map);
		return productList;
		
	}
//	public String getProducts() {
//		ProductVO product = new ProductVO();
//		product.setId("0");
//		product.setTitle("The Comedy of Errors");
//		product.setDescriptionHtml("<a href=\\\"https://en.wikipedia.org/wiki/The_Comedy_of_Errors\\\">Wikipedia Summary</a>: The Comedy of Errors is one of <b>William Shakespeare\\\\'s</b> early plays. It is his shortest and one of his most farcical comedies, with a major part of the humour coming from slapstick and mistaken identity, in addition to puns and word play.");
//	
//		return gson.toJson(product);
//	}
	
	public void getProduct(int productId) {
		
	}
	
	public void getProductDetails(int productId, HttpHeaders headers) {
		
	}
	
	public void getProductReviews(int productId, HttpHeaders headers) {
		
	}
	
	public void getProductRatings(int productId, HttpHeaders headers) {
		
	}

	
//	class Writer(object):
//	    def __init__(self, filename):
//	        self.file = open(filename, 'w')
//
//	    def write(self, data):
//	        self.file.write(data)
//
//	    def flush(self):
//	        self.file.flush()
	


}
