package com.bloodmatch.bloodlink.Patient;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.bloodmatch.bloodlink.R;

public class ObjectFragment extends Fragment {
    private static final String ARG_OBJECT = "object";
    private static final String ARG_NOTIFICATION_TYPE = "notification_type";
    private static final String ARG_PAGE_NOTIFICATION_TYPE = "page_notification_type";

    private int pageNumber;
    private NotificationType notificationType;
    private NotificationType pageNotificationType;

    public static ObjectFragment newInstance(int pageNumber, NotificationType notificationType, NotificationType pageNotificationType) {
        ObjectFragment fragment = new ObjectFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_OBJECT, pageNumber);
        args.putSerializable(ARG_NOTIFICATION_TYPE, notificationType);
        args.putSerializable(ARG_PAGE_NOTIFICATION_TYPE, pageNotificationType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pageNumber = getArguments().getInt(ARG_OBJECT);
            notificationType = (NotificationType) getArguments().getSerializable(ARG_NOTIFICATION_TYPE);
            pageNotificationType = (NotificationType) getArguments().getSerializable(ARG_PAGE_NOTIFICATION_TYPE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_collection_object, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView textView = view.findViewById(android.R.id.text1);
        textView.setText(String.valueOf(pageNumber));

        // Example usage of notificationType and pageNotificationType:
        if (notificationType != null && pageNotificationType != null) {
            // Do something with notificationType and pageNotificationType
        }
    }
}