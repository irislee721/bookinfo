package com.sysage.bookinfo.controller;

import java.util.ArrayList;

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
	
	Gson gson = new Gson();
	private final static String star_color = System.getenv("STAR_COLOR") == null ? "black" : System.getenv("STAR_COLOR");
	
	//	front call getProduct
	public ProductVO getProduct(int productId) {
		ArrayList<ProductVO> productList = getProducts();
		if (productId + 1 > productList.size()) {
			return null;
		} else {
			//判斷list get出來的物件id是否=productId?
			//productList.get(productId).getId().equals(productId)
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
//		return gson.toJson(detailsVO);
		return detailsVO;
	}
	
	//ratings and reviews放在一起了
	public ReviewsVO getProductReviews(int productId) {
		
		ReviewsVO reviewsVO = new ReviewsVO();
		ReviewsDetailVO reviewsDetail1 = new ReviewsDetailVO();
		ReviewsDetailVO reviewsDetail2 = new ReviewsDetailVO();
		RatingVO ratingVO1 = new RatingVO();
		RatingVO ratingVO2 = new RatingVO();

		ratingVO1.setStars(5);
		ratingVO1.setColor(star_color);
		reviewsDetail1.setReviewer("Reviewer1");
		reviewsDetail1.setText("An extremely entertaining play by Shakespeare. The slapstick humour is refreshing!");
		reviewsDetail1.setRating(ratingVO1);
		
		ratingVO2.setStars(4);
		ratingVO2.setColor(star_color);
		reviewsDetail2.setReviewer("Reviewer2");
		reviewsDetail2.setText("Absolutely fun and entertaining. The play lacks thematic depth when compared to other plays by Shakespeare.");
		reviewsDetail2.setRating(ratingVO2);
		
		reviewsVO.setId(productId);
		reviewsVO.addReviews(reviewsDetail1);
		reviewsVO.addReviews(reviewsDetail2);
		
		return reviewsVO;
	}
	

}
