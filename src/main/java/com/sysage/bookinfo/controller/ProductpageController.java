package com.sysage.bookinfo.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.sysage.bookinfo.vo.DetailsVO;
import com.sysage.bookinfo.vo.ProductVO;
import com.sysage.bookinfo.vo.ReviewsDetailVO;
import com.sysage.bookinfo.vo.ReviewsVO;

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

	@Autowired
	Details details;

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
		ModelAndView mav = new ModelAndView("redirect:/productpage");
		return mav;
	}
	
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public ModelAndView logout(HttpSession session) {
		session.invalidate();
		ModelAndView mav = new ModelAndView("redirect:/productpage");
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
	public String front(Model model) {
		try {
			int prodId = 0; // TODO: replace default value
			ProductVO product = details.getProduct(prodId);
			model.addAttribute("product", product);
			
			DetailsVO productdetails = details.getProductDetails(prodId);
			model.addAttribute("details", productdetails);
			
			ReviewsVO reviews = details.getProductReviews(prodId);
			List<ReviewsDetailVO> list = reviews.getReviews();
			
			model.addAttribute("review", reviews.getReviews());
			
		} catch (Exception e) {
			model.addAttribute("error", "Sorry, product details are currently unavailable for this book.");
		}

		model.addAttribute("", "");

		return "productpage";
	}
	
	
	public void getProductDetails(int productId, HttpHeaders headers) {
		
	}
	
	public void getProductReviews(int productId, HttpHeaders headers) {
		
	}
	
	public void getProductRatings(int productId, HttpHeaders headers) {
		
	}

}
