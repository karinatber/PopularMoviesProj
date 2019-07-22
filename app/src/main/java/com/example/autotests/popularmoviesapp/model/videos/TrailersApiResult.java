package com.example.autotests.popularmoviesapp.model.videos;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.asif.gsonpojogenerator")
public class TrailersApiResult {

	@SerializedName("id")
	private int id;

	@SerializedName("results")
	private List<Trailer> results;

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setResults(List<Trailer> results){
		this.results = results;
	}

	public List<Trailer> getResults(){
		return results;
	}

	@Override
 	public String toString(){
		return 
			"TrailersApiResult{" +
			"id = '" + id + '\'' + 
			",results = '" + results + '\'' + 
			"}";
		}
}