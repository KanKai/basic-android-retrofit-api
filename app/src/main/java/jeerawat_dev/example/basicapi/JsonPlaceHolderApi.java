package jeerawat_dev.example.basicapi;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface JsonPlaceHolderApi {

    @GET("posts")
    Call<List<Post>> getPosts(
            @Query("userId") Integer[] userId,
            @Query("_sort") String sort,
            @Query("_order") String order
    );

    @GET("posts")
    Call<List<Post>> getPosts(@QueryMap Map<String, String> parameters);

    @GET("posts/{id}/comments")
    Call<List<Comment>> getComment(@Path("id") int postId);

    @GET
    Call<List<Comment>> getComment(@Url String url);

    @POST("posts")
    Call<Post> createPost(@Body Post post);

    // userId=12&title=New%20Title&body=New%20Text
    @FormUrlEncoded
    @POST("posts")
    Call<Post> createPost(
            @Field("userId") int userId,
            @Field("title") String title,
            @Field("body") String text
    );

    @FormUrlEncoded
    @POST("posts")
    Call<Post> createPost(@FieldMap Map<String, String> fields);

    @Headers({"Static-Header1: 123", "Static-Header2: 456"})
    @PUT("posts/{id}")
    Call<Post> putPost(
            @Header("Dynamic-Header") String header,
            @Path("id") int id,
            @Body Post post
    );

    // posts/123
    @PATCH("posts/{id}")
    Call<Post> patchPost(@HeaderMap Map<String, String> headers, @Path("id") int id, @Body Post post);

    // posts/123
    // void ไม่ return ค่ากลัับ
    @DELETE("posts/{id}")
    Call<Void> deletePost(@Path("id") int id);


}
