package ru.geekbrains.android2.semenov_a2.ui.options;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

import ru.geekbrains.android2.semenov_a2.R;
import ru.geekbrains.android2.semenov_a2.ui.help.HelpFragment;
import ru.geekbrains.android2.semenov_a2.ui.home.HomeFragment;

public class OptionsFragment extends Fragment {

    private OptionsViewModel optionsViewModel;

    private static final String TAG = "OptionsSelect";
    private final String TOWN_EDIT_TEXT_STATE_KEY = "TOWN_EDIT_TEXT_STATE_KEY";
    private final String TOWN_SPINNER_STATE_KEY = "TOWN_SPINNER_STATE_KEY";
    private final String PRESSURE_IS_CHECKED_STATE_KEY = "PRESSURE_IS_CHECKED_STATE_KEY";
    private final String WIND_IS_CHECKED_STATE_KEY = "WIND_IS_CHECKED_STATE_KEY";

    private MaterialButton goBackMainActivityBtn;
    private MaterialButton goHelpInstructionActivityBtn;

    private TextInputLayout loginName;
    private TextInputEditText townSelectEditView;

    private CheckBox pressureCheckBox, windCheckBox;
    private TextView pressureTextView, windTextView;

    Pattern checkTown = Pattern.compile("^[A-Z]|[a-z]|[А-Я]|[а-я]{3,}$");

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        optionsViewModel =
                ViewModelProviders.of(this).get(OptionsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_options, container, false);
        //final TextView textView = root.findViewById(R.id.text_gallery);
        optionsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        goBackMainActivityBtn = view.findViewById(R.id.goBackMainActivityBtn);
        goHelpInstructionActivityBtn = view.findViewById(R.id.goHelpInstructionActivityBtn);
        loginName = view.findViewById(R.id.loginName);
        townSelectEditView = view.findViewById(R.id.townSelectEditText);
        pressureCheckBox = view.findViewById(R.id.pressureCheckBox);
        pressureTextView = view.findViewById(R.id.pressureTextView);
        windCheckBox = view.findViewById(R.id.windCheckBox);
        windTextView = view.findViewById(R.id.windTextView);

        checkTownField();

        setOnGoBackMainActivityBtnClick();
        setOnHelpBtnClick();

        setOnPressureCheckBoxClick();
        setOnWindCheckBoxClick();
    }


    private void checkTownField() {
        townSelectEditView.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) return;
            TextView tv = (TextView) v;
            validate(tv, checkTown, "Только буквы (в любом регистре), минимум 3 символа");
        });
    }

    // Валидация
    private void validate(TextView tv, Pattern check, String message){
        String value = tv.getText().toString();
        if (check.matcher(value).matches()) {    // Проверим на основе регулярных выражений
            hideError(tv);
        } else {
            showError(tv, message);
        }
    }

    // Показать ошибку
    private void showError(TextView view, String message) {
        view.setError(message);
    }

    // спрятать ошибку
    private void hideError(TextView view) {
        view.setError(null);
    }

    private void setOnGoBackMainActivityBtnClick() {
        goBackMainActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                goBackMainActivityBtn.setVisibility(View.INVISIBLE);
//                goHelpInstructionActivityBtn.setVisibility(View.INVISIBLE);
//                loginName.setVisibility(View.INVISIBLE);
//                townSelectEditView.setVisibility(View.INVISIBLE);
//                pressureCheckBox.setVisibility(View.INVISIBLE);
//                pressureTextView.setVisibility(View.INVISIBLE);
//                windCheckBox.setVisibility(View.INVISIBLE);
//                windTextView.setVisibility(View.INVISIBLE);
                HomeFragment homeFragment = new HomeFragment();
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.replace(R.id.mainContainerForFragment, homeFragment);
                transaction.commit();
            }
        });
    }

    private void setOnHelpBtnClick() {
        goHelpInstructionActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                goBackMainActivityBtn.setVisibility(View.INVISIBLE);
//                goHelpInstructionActivityBtn.setVisibility(View.INVISIBLE);
//                loginName.setVisibility(View.INVISIBLE);
//                townSelectEditView.setVisibility(View.INVISIBLE);
//                pressureCheckBox.setVisibility(View.INVISIBLE);
//                pressureTextView.setVisibility(View.INVISIBLE);
//                windCheckBox.setVisibility(View.INVISIBLE);
//                windTextView.setVisibility(View.INVISIBLE);
                HelpFragment helpFragment = new HelpFragment();
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.replace(R.id.mainContainerForFragment, helpFragment);
                transaction.commit();
            }
        });
    }

    private void setOnPressureCheckBoxClick() {
        pressureCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) pressureTextView.setVisibility(View.VISIBLE); else pressureTextView.setVisibility(View.GONE);
            }
        });
    }

    private void setOnWindCheckBoxClick() {
        windCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) windTextView.setVisibility(View.VISIBLE); else windTextView.setVisibility(View.GONE);
            }
        });
    }

}
