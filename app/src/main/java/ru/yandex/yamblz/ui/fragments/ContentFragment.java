package ru.yandex.yamblz.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import ru.yandex.yamblz.R;
import ru.yandex.yamblz.ui.views.NotificationView;

public class ContentFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.notify) Button notifyButton;
    @BindView(R.id.notification_count) EditText notificationCount;
    @BindView(R.id.notification_view) NotificationView notificationView;

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_content, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        notifyButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        notificationView.setNotificationCount(Integer.valueOf(notificationCount.getText().toString()));
    }
}
