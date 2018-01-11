package com.example.autotests.popularmoviesapp.utils.videos;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.asif.gsonpojogenerator")
public class TrailersJson{

	@SerializedName("id")
	private int id;

	@SerializedName("results")
	private List<VideoResultsItem> results;

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setResults(List<VideoResultsItem> results){
		this.results = results;
	}

	public List<VideoResultsItem> getResults(){
		return results;
	}

	@Override
 	public String toString(){
		return 
			"TrailersJson{" + 
			"id = '" + id + '\'' + 
			",results = '" + results + '\'' + 
			"}";
		}
}