package ru.geekbrains.android2.semenov_a2.ui.help;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.button.MaterialButton;

import ru.geekbrains.android2.semenov_a2.R;
import ru.geekbrains.android2.semenov_a2.ui.home.HomeFragment;
import ru.geekbrains.android2.semenov_a2.ui.options.OptionsFragment;

public class HelpFragment extends Fragment {

    private HelpViewModel helpViewModel;
    private MaterialButton goBackMainActivityBtn;
    private MaterialButton goUrlBtn;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        helpViewModel =
                ViewModelProviders.of(this).get(HelpViewModel.class);
        View root = inflater.inflate(R.layout.fragment_help, container, false);

        helpViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
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
        goUrlBtn = view.findViewById(R.id.goUrlButton);
        setOnGoBackToOptionsSelectActivityBtnClick();
        setOnGoUrlBtnClick();
    }

    private void setOnGoBackToOptionsSelectActivityBtnClick() {
        goBackMainActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBackMainActivityBtn.setVisibility(View.INVISIBLE);
                goUrlBtn.setVisibility(View.INVISIBLE);
                OptionsFragment optionsFragment = new OptionsFragment();
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.replace(R.id.mainContainerForFragment, optionsFragment);
                transaction.commit();
            }
        });
    }

    private void setOnGoUrlBtnClick() {
        goUrlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String urlText = getString(R.string.yandex_link);
                Uri uri = Uri.parse(urlText);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(browserIntent);
            }
        });
    }
}
