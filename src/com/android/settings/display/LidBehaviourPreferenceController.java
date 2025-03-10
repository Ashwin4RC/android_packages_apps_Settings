/*
 * Copyright (C) 2020 The Android Open Source Project
 * Copyright (C) 2024 The LineageOS Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings.display;

import android.content.Context;
import android.provider.Settings;
import android.provider.Settings.Global;

import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;

import com.android.settings.core.BasePreferenceController;

public class LidBehaviourPreferenceController extends BasePreferenceController
        implements Preference.OnPreferenceChangeListener {

    private ListPreference mListPreference;

    // To match WindowManagerPolicy.java
    private final int LID_BEHAVIOR_NONE = 0;
    private final int LID_BEHAVIOR_LOCK = 2;
    private final int LID_BEHAVIOR_SLEEP = 1;


    public LidBehaviourPreferenceController(Context context,String key) {
        super(context, key);
    }

    @Override
    public int getAvailabilityStatus() {
        if (mContext.getResources().getBoolean(
                com.android.internal.R.bool.config_lidControlsSleep)
            || mContext.getResources().getBoolean(
                    com.android.internal.R.bool.config_lidControlsScreenLock)) {
            return AVAILABLE;
        }
        return UNSUPPORTED_ON_DEVICE;
    }

    @Override
    public void displayPreference(PreferenceScreen screen) {
        mListPreference = screen.findPreference(getPreferenceKey());
        mListPreference.setOnPreferenceChangeListener(this);
        super.displayPreference(screen);
    }

    @Override
    public void updateState(Preference preference) {
        int curLidBehaviour = Settings.Global.getInt(mContext.getContentResolver(),
        			Settings.Global.LID_BEHAVIOR, LID_BEHAVIOR_NONE);
        mListPreference.setValue(Integer.toString(curLidBehaviour));
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        Settings.Global.putInt(mContext.getContentResolver(), Settings.Global.LID_BEHAVIOR,
             Integer.valueOf((String) newValue));
        updateState(preference);
        return true;
    }
}
