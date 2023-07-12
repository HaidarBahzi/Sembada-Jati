package com.example.sembadajati;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.Window;

import com.ramotion.paperonboarding.PaperOnboardingFragment;
import com.ramotion.paperonboarding.PaperOnboardingPage;
import com.ramotion.paperonboarding.listeners.PaperOnboardingOnRightOutListener;

import java.util.ArrayList;

public class Onboarding extends AppCompatActivity {

    private FragmentManager frag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_onboarding);

        frag = getSupportFragmentManager();
        PaperOnboardingFragment onboardingFragment = PaperOnboardingFragment.newInstance(listOnboarding());
        FragmentTransaction fragmentTransaction = frag.beginTransaction();
        fragmentTransaction.add(R.id.frame_layout, onboardingFragment);
        fragmentTransaction.commit();

        onboardingFragment.setOnRightOutListener(new PaperOnboardingOnRightOutListener() {
            @Override
            public void onRightOut() {
                Intent intent = new Intent(Onboarding.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private ArrayList<PaperOnboardingPage> listOnboarding() {
        ArrayList<PaperOnboardingPage> dataPage = new ArrayList<>();

        String[] onboardingTitle = getResources().getStringArray(R.array.onboarding_title);
        String[] onboardingDescription = getResources().getStringArray(R.array.onboarding_desc);
        TypedArray onboardingPoster = getResources().obtainTypedArray(R.array.onboarding_poster);
        TypedArray onboardingIcon = getResources().obtainTypedArray(R.array.onboarding_icon);

        for (int i = 0; i < onboardingTitle.length; i++) {
            PaperOnboardingPage onboardingPage = new PaperOnboardingPage();

            onboardingPage.setTitleText(onboardingTitle[i]);
            onboardingPage.setDescriptionText(onboardingDescription[i]);
            onboardingPage.setBgColor(getResources().getColor(R.color.white));
            onboardingPage.setContentIconRes(onboardingPoster.getResourceId(i, -1));
            onboardingPage.setBottomBarIconRes(onboardingIcon.getResourceId(i,-1));

            dataPage.add(onboardingPage);
        }

        return dataPage;
    }
}