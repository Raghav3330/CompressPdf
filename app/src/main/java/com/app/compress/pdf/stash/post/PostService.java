package com.app.compress.pdf.stash.post;



import com.app.compress.pdf.stash.post.model.ConfirmationResponse;
import com.app.compress.pdf.stash.post.model.ForYouResponse;
import com.app.compress.pdf.stash.post.model.PostsResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Ananda.Kumar on 21-09-2017.
 */

public interface PostService {

    @FormUrlEncoded
    @POST("user/create/")
    Call<ConfirmationResponse> createUser(@Field("data") String data);

    @FormUrlEncoded
    @POST("user/update/")
    Call<ConfirmationResponse> updateUser(@Field("data") String data);

    @FormUrlEncoded
    @POST("fetch_data/v2/")
    Call<PostsResponse> getPosts(@Field("data") String data);

    @FormUrlEncoded
    @POST("/fetch_data/v2/")
    Call<ForYouResponse> getForYouPosts(@Field("data") String data);

    @FormUrlEncoded
    @POST("user_action/")
    Call<ConfirmationResponse> userActions(@Field("data") String data);

    @FormUrlEncoded
    @POST("fetch_data/post/")
    Call<PostsResponse> notificationPosts(@Field("data") String data);

    @FormUrlEncoded
    @POST("user/update_contact/")
    Call<ConfirmationResponse> sendContactNumber(@Field("data") String data);


}
