package com.app.compress.pdf.stash.Utility.pagerutility;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.compress.pdf.stash.R;

/**
 * Created by dhurv on 27-09-2017.
 */

public class PostPageView extends LinearLayout {

    private TextView share_text, title, read_more;
    private ImageView like_and_comment, share_btn, thumbnailImage;
    private TextView like_text;
    private CardView stash_share_card;

    public PostPageView(Context context) {
        super(context);
    }

    public PostPageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PostPageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PostPageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        init();
    }

    private void init() {
        //  share_btn = (TextView) findViewById(R.id.share);
//        share_btn = (ImageView) findViewById(R.id.share_btn);
//        like_and_comment = (ImageView) findViewById(R.id.like_btn);
//        like_text = (TextView) findViewById(R.id.like_text);
//        share_text = (TextView) findViewById(R.id.share_text);
//        title = (TextView) findViewById(R.id.title);
//        stash_share_card = (CardView) findViewById(R.id.card_view);
//        thumbnailImage = (ImageView) findViewById(R.id.image_illustration);
//        read_more = (TextView) findViewById(R.id.read_more);
    }

    public void clickAction(int xPosition, int yPosition) {
        Rect r = new Rect();
        if (thumbnailImage != null) {
            thumbnailImage.getHitRect(r);
            if (r.contains(xPosition, yPosition)) {
                thumbnailImage.performClick();
            }
        }
        if (read_more != null) {
            read_more.getHitRect(r);
            if (r.contains(xPosition, yPosition)) {
                read_more.performClick();
            }
        }
        if (share_btn != null) {
            share_btn.getHitRect(r);
            if (r.contains(xPosition, yPosition)) {
                share_btn.performClick();
            }
        }
        if (like_text != null) {
            like_text.getHitRect(r);
            if (r.contains(xPosition, yPosition)) {
                like_text.performClick();
                return;
            }
        }

        if (like_and_comment != null) {
            like_and_comment.getHitRect(r);
            if (r.contains(xPosition, yPosition)) {
                like_and_comment.performClick();
                return;
            }
        }

        if (share_text != null) {
            share_text.getHitRect(r);
            if (r.contains(xPosition, yPosition)) {
                share_text.performClick();
                return;
            }
        }


        if (title != null) {
            title.getHitRect(r);
            if (r.contains(xPosition, yPosition)) {
                title.performClick();
            }
        }
        if (stash_share_card != null) {
            stash_share_card.getHitRect(r);
            if (r.contains(xPosition, yPosition)) {
                stash_share_card.performClick();
            }
        }


    }
}
