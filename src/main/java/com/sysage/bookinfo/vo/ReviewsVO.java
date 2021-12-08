package com.sysage.bookinfo.vo;

import java.util.ArrayList;
import java.util.List;

public class ReviewsVO {
	
	private int id;
	private List<ReviewsDetailVO> reviews = new ArrayList<ReviewsDetailVO>();
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public List<ReviewsDetailVO> getReviews() {
		return reviews;
	}
	public void addReviews(ReviewsDetailVO reviewsDetail) {
		reviews.add(reviewsDetail);
	}

}
