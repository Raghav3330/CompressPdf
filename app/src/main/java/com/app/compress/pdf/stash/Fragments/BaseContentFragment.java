package com.app.compress.pdf.stash.Fragments;


import androidx.fragment.app.Fragment;
import android.view.View;

import com.app.compress.pdf.stash.post.model.Post;


/**
 * Created by Sajal.Jain1 on 12-12-2017.
 */

public abstract class BaseContentFragment extends Fragment implements View.OnClickListener {

    public interface ContentActivityCallback {
        //  function calls to be made from fragments to activity
        void expandAppBar();

        void noInternetAvailable(final int fragmentPosition);

        void openAccessibilityHelper(int fragmentPosition);

        Post havingNotification();
        public void setNotificationNull();
        void takePermission();

    }


    public interface ZoomImageInterface {

        void zoomImageFromThumb(final View thumbView, String imageResId);

    }


    public interface FragmentCallback {
        void loadNextPosts();

        boolean getIsLoading();

        boolean getIsLastPage();

        boolean sendPhNumberToServer(String phNumber);
    }

    public abstract void refreshPosts(final int tag);

    public abstract void setAccessibilityRunning();

    public abstract void setCurrentNotification();
}
