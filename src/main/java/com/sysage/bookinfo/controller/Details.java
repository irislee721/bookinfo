package com.sysage.bookinfo.controller;

import java.util.ArrayList;
import java.util.Optional;

import org.apache.http.Header;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;
import com.sysage.bookinfo.vo.DetailsVO;
import com.sysage.bookinfo.vo.ProductVO;
import com.sysage.bookinfo.vo.RatingVO;
import com.sysage.bookinfo.vo.ReviewsDetailVO;
import com.sysage.bookinfo.vo.ReviewsVO;

@Component
public class Details {

	private final static Boolean ratings_enabled = Boolean.valueOf(System.getenv("ENABLE_RATINGS"));
	private final static String star_color = Optional.ofNullable(System.getenv("STAR_COLOR")).orElse("black");
	
	//	front call getProduct
	public ProductVO getProduct(int productId) {
		ArrayList<ProductVO> productList = getProducts();
		if (productId + 1 > productList.size()) {
			return null;
		} else {
			//判斷list get出來的物件id是否=productId?
			return productList.get(productId);
		}
	}
	
	//	Data providers
	//	getProduct call getProducts
	//  可以改合成一個by id查詢，不要用list，除非要list all product
	public ArrayList<ProductVO> getProducts() {
		ArrayList<ProductVO> productList = new ArrayList<ProductVO>();
		ProductVO product = new ProductVO();
		product.setId(0);
		product.setTitle("The Comedy of Errors");
		product.setDescriptionHtml("<a href=\"https://en.wikipedia.org/wiki/The_Comedy_of_Errors\">Wikipedia Summary</a>: The Comedy of Errors is one of <b>William Shakespeare\'s</b> early plays. It is his shortest and one of his most farcical comedies, with a major part of the humour coming from slapstick and mistaken identity, in addition to puns and word play.");
		productList.add(product);
		return productList;
	}

	//	call DB
	//	front call getProductDetails
	public DetailsVO getProductDetails(int productId) throws Exception{
		DetailsVO detailsVO = new DetailsVO();
		detailsVO.setId(productId);
		detailsVO.setAuthor("William Shakespeare");
		detailsVO.setYear(1596);
		detailsVO.setType("paperback");
		detailsVO.setPages(200);
		detailsVO.setPublisher("PublisherA");
		detailsVO.setLanguage("English");
		detailsVO.setISBN_10("1234567890");
		detailsVO.setISBN_13("123-1234567890");
		return detailsVO;
	}
	
	//ratings and reviews放在一起了
	public ReviewsVO getProductReviews(int productId) {
		ReviewsVO reviews = new ReviewsVO();
		ReviewsDetailVO reviewsDetail = new ReviewsDetailVO();

		reviews.setId(productId);
		reviewsDetail.setReviewer("Reviewer1");
		reviewsDetail.setText("An extremely entertaining play by Shakespeare. The slapstick humour is refreshing!");

		if (ratings_enabled) {
			RatingVO rating = new RatingVO();
			rating.setStars(5);
			rating.setColor(star_color);
			reviewsDetail.setRating(rating);
		}
		reviews.addReviews(reviewsDetail);

		reviewsDetail = new ReviewsDetailVO();
		reviewsDetail.setReviewer("Reviewer2");
		reviewsDetail.setText("Absolutely fun and entertaining. The play lacks thematic depth when compared to other plays by Shakespeare.");

		if (ratings_enabled) {
			RatingVO rating = new RatingVO();
			rating.setStars(4);
			rating.setColor(star_color);
			reviewsDetail.setRating(rating);
		}
		reviews.addReviews(reviewsDetail);
		
		return reviews;
	}
	
}
