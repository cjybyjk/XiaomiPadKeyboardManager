/*
 * Copyright (C) 2022 Project Kaleidoscope
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
 * limitations under the License
 */

package ink.kscope.xiaomikeyboardmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.input.InputManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.InputDevice;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mTextView;
    private TextView mTextView2;
    private boolean mKeyboardEnabled;

    InputManager mInputManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.button);
        mTextView = findViewById(R.id.textView);
        mTextView2 = findViewById(R.id.textView2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(new Intent(MainActivity.this, XiaomiKeyboardService.class));
            }
        });
        mInputManager = getSystemService(InputManager.class);
        mInputManager.registerInputDeviceListener(new InputManager.InputDeviceListener() {
            @Override
            public void onInputDeviceAdded(int deviceId) {
                updateInputDevices();
            }

            @Override
            public void onInputDeviceRemoved(int deviceId) {
                updateInputDevices();

            }

            @Override
            public void onInputDeviceChanged(int deviceId) {
                updateInputDevices();

            }
        }, new Handler(Looper.getMainLooper()));
        updateInputDevices();
    }


    private void updateInputDevices() {
        StringBuilder sb = new StringBuilder("input devices:\n");
        for (int x : mInputManager.getInputDeviceIds()) {
            InputDevice inputDevice = mInputManager.getInputDevice(x);
            if (inputDevice == null) continue;;
            sb.append(inputDevice.getId())
                    .append(' ')
                    .append(inputDevice.getName())
                    .append(' ')
                    .append(inputDevice.getVendorId()).append(':').append(inputDevice.getProductId())
                    .append(" enabled=").append(inputDevice.isEnabled());
            sb.append('\n');
        }
        mTextView2.setText(sb.toString());
    }

}