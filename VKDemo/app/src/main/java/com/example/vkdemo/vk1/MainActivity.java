package com.example.vkdemo.vk1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {


    private String[] scope = new String[]{VKScope.STATUS};
    private TextView tvLstName, tvFstName, tvCountry, tvCity, tvBirthday;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.ivFotoProf);
        tvLstName = (TextView) findViewById(R.id.tvLstName);
        tvFstName = (TextView) findViewById(R.id.tvFstName);
        tvCountry = (TextView) findViewById(R.id.tvCountry);
        tvBirthday = (TextView) findViewById(R.id.tvBirthday);
        tvCity = (TextView) findViewById(R.id.tvCity);

        VKSdk.login(this, scope);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {

                VKRequest request = VKApi.users()
                        .get(VKParameters.from(VKApiConst.FIELDS,"first_name, last_name, photo, bdate, country, home_town"));

                request.executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {
                        super.onComplete(response);
                        JSONObject json = response.json;
                        try {
                            JSONArray array = json.getJSONArray("response");
                            JSONObject jsonObject = new JSONObject(array.getString(0));

                            tvLstName.setText(jsonObject.getString("last_name"));
                            tvFstName.setText(jsonObject.getString("first_name"));
                            tvBirthday.setText(jsonObject.getString("bdate"));
                            tvCity.setText(jsonObject.getString("home_town"));
                            tvCountry.setText(jsonObject.getJSONObject("country").getString("title"));
                            Picasso.with(getApplicationContext()).load(jsonObject.getString("photo")).into(imageView);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onError(VKError error) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
