package ru.geekbrains.android2.semenov_a2.ui.home;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import ru.geekbrains.android2.semenov_a2.R;
import ru.geekbrains.android2.semenov_a2.ui.options.OptionsFragment;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    private final static int REQUEST_CODE = 1;

    private TextView townTextView;
    private TextView temperatureTextView;
    private TextView pressureTextView;
    private TextView windTextView;
    private TextView skyTexView;
    private ImageView skyImageView;
    private boolean isPressureShow = true;
    private boolean isWindShow = true;
    private Button goOptionsSelectActivityBtn;

    private final Handler handler = new Handler();
    //private final static String LOG_TAG = MainActivity.class.getSimpleName();
    Typeface weatherFont;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
//              textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        townTextView = view.findViewById(R.id.townTextView);
        temperatureTextView = view.findViewById(R.id.temperatureTextView);
        pressureTextView = view.findViewById(R.id.pressureTextView);
        windTextView = view.findViewById(R.id.windTextView);
        skyTexView = view.findViewById(R.id.skyTextView);
        skyImageView = view.findViewById(R.id.skyImageView);
        goOptionsSelectActivityBtn = view.findViewById(R.id.goOptionsSelectActivityBtn);

        initFonts();
        updateWeatherData(townTextView.getText().toString());
        setOnGoOptionsSelectBtnClick();
    }

    private void initFonts(){
        weatherFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/weather.ttf"); //если в MainActivity, то getActivity(). не нужен
        skyTexView.setTypeface(weatherFont);
    }

    private void setOnGoOptionsSelectBtnClick() {
        goOptionsSelectActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                townTextView.setVisibility(View.INVISIBLE);
                temperatureTextView.setVisibility(View.INVISIBLE);
                pressureTextView.setVisibility(View.INVISIBLE);
                windTextView.setVisibility(View.INVISIBLE);
                skyTexView.setVisibility(View.INVISIBLE);
                skyImageView.setVisibility(View.INVISIBLE);
                goOptionsSelectActivityBtn.setVisibility(View.INVISIBLE);
                OptionsFragment optionsFragment = new OptionsFragment();
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
//                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.mainContainerForFragment, optionsFragment);
                transaction.commit();
            }
        });
    }

    private void updateWeatherData(final String city) {
        new Thread() {
            @Override
            public void run() {
                final JSONObject jsonObject = WeatherDataLoader.getJSONData(city);
                if(jsonObject == null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), R.string.place_not_found,
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            renderWeather(jsonObject);
                        }
                    });
                }
            }
        }.start();
    }

    private void renderWeather(JSONObject jsonObject) {
        //Log.d(LOG_TAG, "json: " + jsonObject.toString());
        try {
            JSONObject details = jsonObject.getJSONArray("weather").getJSONObject(0);
            JSONObject main = jsonObject.getJSONObject("main");

            setPlaceName(jsonObject);
            setDetails(details, main);
            setCurrentTemp(main);
            setUpdatedText(jsonObject);
            setWeatherIcon(details.getInt("id"),
                    jsonObject.getJSONObject("sys").getLong("sunrise") * 1000,
                    jsonObject.getJSONObject("sys").getLong("sunset") * 1000);
        } catch (Exception exc) {
            exc.printStackTrace();
            //Log.e(LOG_TAG, "One or more fields not found in the JSON data");
        }
    }

    private void setPlaceName(JSONObject jsonObject) throws JSONException {
        String cityText = jsonObject.getString("name").toUpperCase() + ", "
                + jsonObject.getJSONObject("sys").getString("country");
        townTextView.setText(cityText);
    }

    private void setDetails(JSONObject details, JSONObject main) throws JSONException {
        String detailsText = details.getString("description").toUpperCase() + "\n" + main.getString("pressure") + "hPa";
        pressureTextView.setText(detailsText);
    }

    private void setCurrentTemp(JSONObject main) throws JSONException {
        String currentTextText = String.format(Locale.getDefault(), "%.2f", main.getDouble("temp"));
        temperatureTextView.setText(currentTextText);
    }

    private void setUpdatedText(JSONObject jsonObject) throws JSONException {
        DateFormat dateFormat = DateFormat.getDateTimeInstance();
        String updateOn = dateFormat.format(new Date(jsonObject.getLong("dt") * 1000));
        String updatedText = "Last update: " + updateOn;
        windTextView.setText("100");
    }

    private void setWeatherIcon(int id, long sunrise, long sunset) {
        String icon = "";
        String skyPictureName = "snow_white";

        switch (id / 100) {
            case 2: {
                icon = getString(R.string.weather_thunder); skyPictureName = "thunder_white";
                break;
            }
            case 3: {
                if (id < 302) {
                    icon = getString(R.string.weather_drizzle); skyPictureName = "rain_light_white";
                } else {
                    icon = getString(R.string.weather_drizzle); skyPictureName = "rain_shower_white";
                }
                break;
            }
            case 5: {
                if (id < 502) {
                    icon = getString(R.string.weather_rainy); skyPictureName = "rain_light_white";
                } else {
                    icon = getString(R.string.weather_rainy); skyPictureName = "rain_shower_white";
                }
                break;
            }
            case 6: {
                icon = getString(R.string.weather_snowy); skyPictureName = "snow_white";
                break;
            }
            case 7: {
                icon = getString(R.string.weather_foggy); skyPictureName = "foggy_white";
                break;
            }
            case 8: {
                if (id > 802) {
                    icon = getString(R.string.weather_foggy); skyPictureName = "cloud_overcast_white";
                    break;
                }
                if (id == 802) {
                    icon = getString(R.string.weather_foggy); skyPictureName = "cloud_broken_white";
                    break;
                }
                if (id == 801 ) {
                    long currentTime = new Date().getTime();
                    if(currentTime >= sunrise && currentTime < sunset) {
                        icon = "\u2600"; skyPictureName = "cloud_few_white";
                    } else {
                        icon = getString(R.string.weather_clear_night); skyPictureName = "night_cloud_few_white";
                    }
                    break;
                }
                if (id == 800 ) {
                    long currentTime = new Date().getTime();
                    if(currentTime >= sunrise && currentTime < sunset) {
                        icon = "\u2600"; skyPictureName = "clear_sky_white";
                    } else {
                        icon = getString(R.string.weather_clear_night); skyPictureName = "night_clear_sky_white";
                    }
                }
            }
        }
        skyTexView.setText(id + " ww " + icon);
//        int idPicture = getResources().getIdentifier(skyPictureName, "drawable", getN);
//        skyImageView.setImageResource(idPicture);
    }



//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode != REQUEST_CODE) {
//            super.onActivityResult(requestCode, resultCode, data);
//            return;
//        }
//        if (resultCode == RESULT_OK) {
//            townTextView.setText(data.getStringExtra(Constants.TOWN_DATA_KEY));
//            isPressureShow = data.getBooleanExtra(Constants.PRESSURE_IS_CHECKED_KEY, true);
//            isWindShow = data.getBooleanExtra(Constants.WIND_IS_CHECKED_KEY, true);
//            if (isPressureShow) pressureTextView.setVisibility(View.VISIBLE); else pressureTextView.setVisibility(View.GONE);
//            if (isWindShow) windTextView.setVisibility(View.VISIBLE); else windTextView.setVisibility(View.GONE);
//            updateWeatherData(townTextView.getText().toString());
//        }
//        updateWeatherData(townTextView.getText().toString());
//    }

//    @Override
//    protected void onSaveInstanceState(@NonNull Bundle saveInstanceState){
//        Toast.makeText(getApplicationContext(), "onSaveInstanceState()", Toast.LENGTH_SHORT).show();
//        saveInstanceState.putString(Constants.TOWN_DATA_KEY, townTextView.getText().toString());
//        super.onSaveInstanceState(saveInstanceState);
//    }
//
//    @Override
//    protected void onRestoreInstanceState(@NonNull Bundle saveInstanceState){
//        super.onRestoreInstanceState(saveInstanceState);
//        townTextView.setText(saveInstanceState.getString(Constants.TOWN_DATA_KEY));
//        Toast.makeText(getApplicationContext(), "Повторный запуск!! - onRestoreInstanceState()", Toast.LENGTH_SHORT).show();
//    }




}
