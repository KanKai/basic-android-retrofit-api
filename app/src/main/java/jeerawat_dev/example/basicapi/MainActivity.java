package jeerawat_dev.example.basicapi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    // private TextView textViewResult;

    private ListView listView;
    private Button button;

    private JsonPlaceHolderApi jsonPlaceHolderApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // textViewResult = findViewById(R.id.text_view_result);

        listView = (ListView) findViewById(R.id.listview);
        button = (Button) findViewById(R.id.btn_search);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getComments();
            }
        });

        // สนใจค่า null ที่มาใน body request
        Gson gson = new GsonBuilder().serializeNulls().create();

        // log
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @NotNull
                    @Override
                    public okhttp3.Response intercept(@NotNull Chain chain) throws IOException {
                        Request originalRequest = chain.request();

                        Request newRequest = originalRequest.newBuilder()
                                .header("Interceptor-Header", "xyz")
                                .build();
                        return chain.proceed(newRequest);
                    }
                })
                .addInterceptor(loggingInterceptor)
                .build();


        Retrofit retrofit =  new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        // getPosts();
        // getComments();
        // createPost();
        // updatePost();
        // deletePost();
    }

    private void setList(ArrayList<String> list) {
        final ArrayList<String> arrayList = list;

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList);

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(MainActivity.this, "Click item" + i + " " + arrayList.get(i).toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getPosts() {

        Map<String, String> parameters = new HashMap<>();
        parameters.put("userId", "4");
        parameters.put("_sort", "id");
        parameters.put("_order", "desc");

        // Call<List<Post>> call = jsonPlaceHolderApi.getPosts(new Integer[]{1}, "id", "desc");

        Call<List<Post>> call = jsonPlaceHolderApi.getPosts(parameters);

        // แสดง List

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (!response.isSuccessful()) {
                    // textViewResult.setText("Code: " + response.code());
                    return;
                }

                List<Post> posts = response.body();

                final ArrayList<String> arrayList = new ArrayList<>();

                for (Post post: posts) {
                    String content = "";
                    content += "ID: " + post.getId() + "\n";
                    content += "User ID: " + post.getUserId() + "\n";
                    content += "Title: " + post.getTitle() + "\n";
                    content += "Text: " + post.getText() + "\n\n";

                    // textViewResult.append(content);
                    arrayList.add(post.getText());
                }

                setList(arrayList);
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                // textViewResult.setText(t.getMessage());
            }
        });
    }

    private void getComments() {
         Call<List<Comment>> call = jsonPlaceHolderApi.getComment(3);

//        Call<List<Comment>> call = jsonPlaceHolderApi.getComment("https://jsonplaceholder.typicode.com/posts/2/comments");

         call.enqueue(new Callback<List<Comment>>() {
             @Override
             public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                 if (!response.isSuccessful()) {
                     // textViewResult.setText("Code: " + response.code());
                     return;
                 }

                 List<Comment> comments = response.body();

                 final ArrayList<String> arrayList = new ArrayList<>();

                 for (Comment comment: comments) {
                     String content = "";
                     content += "ID: " + comment.getId() + "\n";
                     content += "Post ID: " + comment.getPostId() + "\n";
                     content += "Name: " + comment.getName() + "\n";
                     content += "Email: " + comment.getEmail() + "\n";
                     content += "Text: " + comment.getText() + "\n\n";

                     // textViewResult.append(content);
                     arrayList.add(comment.getEmail());
                 }
                 setList(arrayList);
             }

             @Override
             public void onFailure(Call<List<Comment>> call, Throwable t) {
                 // textViewResult.setText(t.getMessage());
             }
         });
    }

    private void createPost() {
        Post post = new Post(123, "New Title", "New Text");

        Map<String, String> fields = new HashMap<>();
        fields.put("userId", "123");
        fields.put("title", "New Title");

        // Call<Post> call = jsonPlaceHolderApi.createPost(post);
        // Call<Post> call = jsonPlaceHolderApi.createPost(123, "New Title", "New Text");
        Call<Post> call = jsonPlaceHolderApi.createPost(fields);

        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (!response.isSuccessful()) {
                    // textViewResult.setText("Code: " + response.code());
                    return;
                }

                Post postResponse = response.body();

                String content = "";
                content += "Code: " + response.code() + "\n";
                content += "ID: " + postResponse.getId() + "\n";
                content += "User ID: " + postResponse.getUserId() + "\n";
                content += "Title: " + postResponse.getTitle() + "\n";
                content += "Text: " + postResponse.getText() + "\n\n";

                // textViewResult.append(content);
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                // textViewResult.setText(t.getMessage());
            }
        });
    }

    private void updatePost() {
        Post post = new Post(123, null, "New Text");

        Map<String, String> headers = new HashMap<>();
        headers.put("Map-Header1", "qwe");
        headers.put("Map-Header2", "asd");

        // Call<Post> call = jsonPlaceHolderApi.putPost("abc", 5, post);
        // Call<Post> call = jsonPlaceHolderApi.patchPost(5, post);
        Call<Post> call = jsonPlaceHolderApi.patchPost(headers, 5, post);

        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (!response.isSuccessful()) {
                    // textViewResult.setText("Code: " + response.code());
                    return;
                }

                Post postResponse = response.body();

                String content = "";
                content += "Code: " + response.code() + "\n";
                content += "ID: " + postResponse.getId() + "\n";
                content += "User ID: " + postResponse.getUserId() + "\n";
                content += "Title: " + postResponse.getTitle() + "\n";
                content += "Text: " + postResponse.getText() + "\n\n";

                // textViewResult.append(content);
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                // textViewResult.setText(t.getMessage());
            }
        });
    }

    private void deletePost() {
        Call<Void> call = jsonPlaceHolderApi.deletePost(5);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                // textViewResult.setText("Code: " + response.code());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // textViewResult.setText(t.getMessage());
            }
        });
    }
}
