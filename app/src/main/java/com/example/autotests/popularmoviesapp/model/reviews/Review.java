package com.example.autotests.popularmoviesapp.model.reviews;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import javax.annotation.Generated;

@Generated("com.asif.gsonpojogenerator")
public class Review implements Parcelable {

	@SerializedName("author")
	private String author;

	@SerializedName("id")
	private String id;

	@SerializedName("content")
	private String content;

	@SerializedName("url")
	private String url;

	public void setAuthor(String author){
		this.author = author;
	}

	public String getAuthor(){
		return author;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setContent(String content){
		this.content = content;
	}

	public String getContent(){
		return content;
	}

	public void setUrl(String url){
		this.url = url;
	}

	public String getUrl(){
		return url;
	}

	@Override
 	public String toString(){
		return 
			"Movie{" +
			"author = '" + author + '\'' + 
			",id = '" + id + '\'' + 
			",content = '" + content + '\'' + 
			",url = '" + url + '\'' + 
			"}";
		}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(author);
		dest.writeString(id);
		dest.writeString(content);
		dest.writeString(url);
	}

	/**Constructor used for parcel**/
	public Review(Parcel parcel){
		author = parcel.readString();
		id = parcel.readString();
		content = parcel.readString();
		url = parcel.readString();
	}

	public static final Parcelable.Creator<Review> CREATOR
			= new Parcelable.Creator<Review>() {
		public Review createFromParcel(Parcel in) {
			return new Review(in);
		}

		public Review[] newArray(int size) {
			return new Review[size];
		}
	};
}