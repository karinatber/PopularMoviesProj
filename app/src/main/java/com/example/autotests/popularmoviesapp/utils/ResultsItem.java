package com.example.autotests.popularmoviesapp.utils;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

@Generated("com.asif.gsonpojogenerator")
public class ResultsItem implements Parcelable{

	@SerializedName("overview")
	private String overview;

	@SerializedName("original_language")
	private String originalLanguage;

	@SerializedName("original_title")
	private String originalTitle;

	@SerializedName("video")
	private boolean video;

	@SerializedName("title")
	private String title;

	@SerializedName("genre_ids")
	private List<Object> genreIds;

	@SerializedName("poster_path")
	private String posterPath;

	@SerializedName("backdrop_path")
	private Object backdropPath;

	@SerializedName("release_date")
	private String releaseDate;

	@SerializedName("vote_average")
	private float voteAverage;

	@SerializedName("popularity")
	private double popularity;

	@SerializedName("id")
	private int id;

	@SerializedName("adult")
	private boolean adult;

	@SerializedName("vote_count")
	private int voteCount;

	public void setOverview(String overview){
		this.overview = overview;
	}

	public String getOverview(){
		return overview;
	}

	public void setOriginalLanguage(String originalLanguage){
		this.originalLanguage = originalLanguage;
	}

	public String getOriginalLanguage(){
		return originalLanguage;
	}

	public void setOriginalTitle(String originalTitle){
		this.originalTitle = originalTitle;
	}

	public String getOriginalTitle(){
		return originalTitle;
	}

	public void setVideo(boolean video){
		this.video = video;
	}

	public boolean isVideo(){
		return video;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	public void setGenreIds(List<Object> genreIds){
		this.genreIds = genreIds;
	}

	public List<Object> getGenreIds(){
		return genreIds;
	}

	public void setPosterPath(String posterPath){
		this.posterPath = posterPath;
	}

	public String getPosterPath(){
		return posterPath;
	}

	public void setBackdropPath(Object backdropPath){
		this.backdropPath = backdropPath;
	}

	public Object getBackdropPath(){
		return backdropPath;
	}

	public void setReleaseDate(String releaseDate){
		this.releaseDate = releaseDate;
	}

	public String getReleaseDate(){
		return releaseDate;
	}

	public void setVoteAverage(float voteAverage){
		this.voteAverage = voteAverage;
	}

	public float getVoteAverage(){
		return voteAverage;
	}

	public void setPopularity(double popularity){
		this.popularity = popularity;
	}

	public double getPopularity(){
		return popularity;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setAdult(boolean adult){
		this.adult = adult;
	}

	public boolean isAdult(){
		return adult;
	}

	public void setVoteCount(int voteCount){
		this.voteCount = voteCount;
	}

	public int getVoteCount(){
		return voteCount;
	}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString( overview);
        parcel.writeString( originalLanguage);
        parcel.writeString (originalTitle);
        parcel.writeValue ((boolean)video);
        parcel.writeString (title);
        parcel.writeList (genreIds);
        parcel.writeString (posterPath);
        parcel.writeValue(backdropPath);
        parcel.writeString (releaseDate);
        parcel.writeFloat (voteAverage);
        parcel.writeDouble (popularity);
        parcel.writeInt (id);
        parcel.writeValue ((boolean) adult);
        parcel.writeInt(voteCount);
	}
    /**Constructor used for parcel**/
    public ResultsItem(Parcel parcel){
        overview = parcel.readString();
        originalLanguage = parcel.readString();
        originalTitle = parcel.readString();
        video = (boolean) parcel.readValue(null);
        title = parcel.readString();
        genreIds = new ArrayList<>();
        parcel.readList(genreIds, ResultsItem.class.getClassLoader());
        posterPath = parcel.readString ();
        backdropPath = parcel.readValue(null);
        releaseDate = parcel.readString ();
        voteAverage = parcel.readFloat ();
        popularity = parcel.readDouble ();
        id = parcel.readInt ();
        adult = (boolean) parcel.readValue (null);
        voteCount = parcel.readInt();
    }

    public static final Parcelable.Creator<ResultsItem> CREATOR
            = new Parcelable.Creator<ResultsItem>() {
        public ResultsItem createFromParcel(Parcel in) {
            return new ResultsItem(in);
        }

        public ResultsItem[] newArray(int size) {
            return new ResultsItem[size];
        }
    };

	@Override
	public String toString() {
		return  "{\"overview\":\"" + overview + "\","+
                "\"original_title\":\"" + originalTitle + "\"," +
                "\"original_language\":\"" + originalLanguage + "\"," +
                "\"video\": " + video + "," +
                "\"title\":\"" + title + "\"," +
                "\"genre_ids\": " + genreIds + "," +
                "\"poster_path\":\"" + posterPath + "\"," +
                "\"backdrop_path\":\"" + backdropPath + "\"," +
                "\"release_date\":\"" + releaseDate + "\"," +
                "\"vote_average\": " + voteAverage + "," +
                "\"popularity\": " + popularity + "," +
                "\"id\": " + id + "," +
                "\"adult\": " + adult + "," +
                "\"vote_count\": " + voteCount + "}";
	}
}