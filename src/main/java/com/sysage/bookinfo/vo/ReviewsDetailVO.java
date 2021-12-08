package com.sysage.bookinfo.vo;

public class ReviewsDetailVO {

	private String reviewer;
	private String text;
	private RatingVO rating;
	
	public String getReviewer() {
		return reviewer;
	}
	public void setReviewer(String reviewer) {
		this.reviewer = reviewer;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public RatingVO getRating() {
		return rating;
	}
	public void setRating(RatingVO rating) {
		this.rating = rating;
	}
	
}
