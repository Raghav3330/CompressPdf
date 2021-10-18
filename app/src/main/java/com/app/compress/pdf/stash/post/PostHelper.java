//package com.app.compress.pdf.stash.post;
//
//import androidx.annotation.NonNull;
//
//import com.google.gson.Gson;
//
//
//import java.util.concurrent.TimeUnit;
//
//import okhttp3.OkHttpClient;
//import retrofit2.Callback;
//import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;
//
///**
// * Created by Ananda.Kumar on 21-09-2017.
// */
//
//public class PostHelper {
//
//    private static final String BASE_URL = RemoteConfig.getString(R.string.server_url);
//    static OkHttpClient okHttpClient = new OkHttpClient.Builder()
//            .connectTimeout(1, TimeUnit.MINUTES)
//            .readTimeout(60, TimeUnit.SECONDS)
//            .writeTimeout(15, TimeUnit.SECONDS)
//            .build();
//
//    public static void registerUser(@NonNull RegistrationRequest pRequest, @NonNull Callback<ConfirmationResponse> pCallback) {
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(BASE_URL)
//                .client(okHttpClient)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        PostService postService = retrofit.create(PostService.class);
//        postService.createUser(new Gson().toJson(pRequest)).enqueue(pCallback);
//    }
//
//    public static void updateUser(@NonNull RegistrationRequest pRequest, @NonNull Callback<ConfirmationResponse> pCallback) {
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(BASE_URL)
//                .client(okHttpClient)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        PostService postService = retrofit.create(PostService.class);
//        postService.updateUser(new Gson().toJson(pRequest)).enqueue(pCallback);
//    }
//
//    public static void getPosts(@NonNull PostsRequest pRequest, @NonNull Callback<PostsResponse> pCallback) {
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(BASE_URL)
//                .client(okHttpClient)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        PostService postService = retrofit.create(PostService.class);
//        postService.getPosts(new Gson().toJson(pRequest)).enqueue(pCallback);
//    }
//
//    public static void getForYouPosts(@NonNull ForYouPostRequest pRequest, @NonNull Callback<ForYouResponse> pCallback) {
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(BASE_URL)
//                .client(okHttpClient)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        PostService postService = retrofit.create(PostService.class);
//        postService.getForYouPosts(new Gson().toJson(pRequest)).enqueue(pCallback);
//    }
//
//    public static void submitActions(@NonNull ActionRequest pRequest, @NonNull Callback<ConfirmationResponse> pCallback) {
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(BASE_URL)
//                .client(okHttpClient)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        PostService postService = retrofit.create(PostService.class);
//        postService.userActions(new Gson().toJson(pRequest)).enqueue(pCallback);
//    }
//
//    public static void getNotificationPosts(@NonNull PostNotificationRequest pRequest, @NonNull Callback<PostsResponse> pCallBack) {
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(BASE_URL)
//                .client(okHttpClient)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        PostService postService = retrofit.create(PostService.class);
//        postService.notificationPosts(new Gson().toJson(pRequest)).enqueue(pCallBack);
//    }
//
//    public static void sendContactNumber(@NonNull PhoneNumberRequest pRequest, @NonNull Callback<ConfirmationResponse> pCallback) {
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(BASE_URL)
//                .client(okHttpClient)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        PostService postService = retrofit.create(PostService.class);
//        postService.sendContactNumber(new Gson().toJson(pRequest)).enqueue(pCallback);
//    }
//
//}
