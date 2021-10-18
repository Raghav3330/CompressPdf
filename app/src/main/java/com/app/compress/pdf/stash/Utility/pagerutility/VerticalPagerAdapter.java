//package com.app.compress.pdf.stash.Utility.pagerutility;
//
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Color;
//import android.net.Uri;
//import android.os.Environment;
//import androidx.viewpager.widget.PagerAdapter;
//import androidx.palette.graphics.Palette;
//import androidx.cardview.widget.CardView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.request.target.SimpleTarget;
//import com.bumptech.glide.request.transition.Transition;
//import com.stash.junkcleaner.Activities.FeedFullScreenYoutube;
//import com.stash.junkcleaner.Analytics.AnalyticsConstants;
//import com.stash.junkcleaner.Analytics.FeedAnalytics;
//import com.stash.junkcleaner.R;
//import com.stash.junkcleaner.Utility.PrefManager;
//import com.stash.junkcleaner.Utility.UtilityMethods;
//import com.stash.junkcleaner.apkgenerator.constants.Constants;
//import com.stash.junkcleaner.data.version_one.AppDBHelper;
//import com.stash.junkcleaner.fragments.BasePostFragment;
//import com.stash.junkcleaner.models.viewpagermodel.ViewPagerModel;
//import com.thefinestartist.finestwebview.FinestWebView;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.lang.ref.WeakReference;
//import java.text.SimpleDateFormat;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//
///**
// * Created by Dhruv on 12/13/16.
// */
//
//public class VerticalPagerAdapter extends PagerAdapter {
//    public static final String TYPE_IMAGE = "image";
//    public static final String TYPE_VIDEO = "video";
//
//    private Context mContext;
//    private LayoutInflater mLayoutInflater;
//    private Map<Integer, WeakReference<PostPageView>> viewMap = new HashMap<>();
//    private List<ViewPagerModel> itemListViewPager;
//    private PostCallback mCallback;
//    private BasePostFragment.ActivityCallback activityCallback;
//    private String FEED_TYPE;
//    private boolean fromBubble;
//
//    public VerticalPagerAdapter(Context context, List<ViewPagerModel> itemListViewPager, BasePostFragment.ActivityCallback mCallback, String FEED_TYPE, boolean fromBubble) {
//        this.itemListViewPager = itemListViewPager;
//        this.fromBubble = fromBubble;
//        this.FEED_TYPE = FEED_TYPE;
//        mContext = context;
//        this.activityCallback = mCallback; //OnCLickCardOpenFilterCallBack
//        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//    }
//
//    @Override
//    public int getCount() {
//        return itemListViewPager.size();
//    }
//
//    @Override
//    public boolean isViewFromObject(View view, Object object) {
//        return view == object;
//    }
//
//    @Override
//    public Object instantiateItem(ViewGroup container, final int position) {
//        PostPageView itemView = null;
//        if (TYPE_IMAGE.equals(itemListViewPager.get(position).getMultiMediaModel().getType())) {
//
//            if (itemListViewPager.get(position).getTitle().length() == 0)
//                itemView = (PostPageView) mLayoutInflater.inflate(R.layout.feed_image, container, false);
//            else if (itemListViewPager.get(position).getBody().length() > 0)
//                itemView = (PostPageView) mLayoutInflater.inflate(R.layout.feed_image_with_titlewithbodycard, container, false);
//            else
//                itemView = (PostPageView) mLayoutInflater.inflate(R.layout.feed_image_with_title_card, container, false);
//
//            TextView score = (TextView) itemView.findViewById(R.id.score);
//            score.setText(mContext.getString(R.string.score, getPostScore(position)));
//
//            final ImageView imageViewPager = (ImageView) itemView.findViewById(R.id.image_illustration);
//            final ImageView thumbnailImage = (ImageView) itemView.findViewById(R.id.image_illustration1);
//            TextView like_and_comment = (TextView) itemView.findViewById(R.id.like_and_comment);
//            TextView read_more = (TextView) itemView.findViewById(R.id.read_more);
//            read_more.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    openFullContent(position);
//                }
//            });
//
//            String likeAndComment = "";
//            try {
//                likeAndComment = UtilityMethods.withSuffix(Long.parseLong(itemListViewPager.get(position).getLikeCount())) + " Likes· " + UtilityMethods.withSuffix(Long.parseLong(itemListViewPager.get(position).getShareCount())) + " Shares   ";
//            } catch (NumberFormatException e) {
//                likeAndComment = mContext.getString(R.string.like_and_comment, itemListViewPager.get(position).getLikeCount(), itemListViewPager.get(position).getShareCount());
//                e.printStackTrace();
//            }
//            like_and_comment.setText(likeAndComment);
//            //  Glide.with(mContext).load(itemListViewPager.get(position).getMultiMediaModel().getThumbnail()).into(imageViewPager);
//            Glide.with(mContext).asBitmap().load(itemListViewPager.get(position).getMultiMediaModel().getUrl())
//                    .into(new SimpleTarget<Bitmap>() {
//                        @Override
//                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
//                            if (resource != null && !resource.isRecycled()) {
//                                Palette palette = Palette.from(resource).generate();
//                                int color = palette.getDominantColor(Color.parseColor("#000000"));
//                                thumbnailImage.setBackgroundColor(color);
//                            }
//
//                            imageViewPager.setImageBitmap(resource);
//                        }
//                    });
//            TextView title = (TextView) itemView.findViewById(R.id.title);
//            title.setText(itemListViewPager.get(position).getTitle());
//
//            title.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    openFullContent(position);
//                }
//            });
//            final ImageView likeButton = (ImageView) itemView.findViewById(R.id.like_btn);
//            ImageView share_btn = (ImageView) itemView.findViewById(R.id.share_btn);
//            final TextView like_text = (TextView) itemView.findViewById(R.id.like_text);
//            TextView share_text = (TextView) itemView.findViewById(R.id.share_text);
//            if (itemListViewPager.get(position).getCanLike().equals("true")) {
//                likeButton.setImageDrawable(mContext.getResources().getDrawable(R.drawable.heart));
//                like_text.setText(R.string.like_space);
//            } else {
//                likeButton.setImageDrawable(mContext.getResources().getDrawable(R.drawable.heart_on));
//                like_text.setText(R.string.liked);
//            }
//            likeButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    String canLike = itemListViewPager.get(position).getCanLike();
//                    if ("true".equals(canLike)) {
//                        addLike(likeButton, like_text, position);
//
//
//                    } else {
//                        removeLike(likeButton, like_text, position);
//
//                    }
//
//                }
//            });
//            like_text.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    String canLike = itemListViewPager.get(position).getCanLike();
//                    if ("true".equals(canLike)) {
//                        addLike(likeButton, like_text, position);
//
//
//                    } else {
//                        removeLike(likeButton, like_text, position);
//                    }
//                }
//            });
//            final PostPageView finalItemView = itemView;
//            final CardView stash_share_card = (CardView) finalItemView.findViewById(R.id.card_view);
//            stash_share_card.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    activityCallback.reverseToolbarCallBack();
//                }
//            });
//
//            share_btn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    shareCard(stash_share_card, position);
//                }
//            });
//            share_text.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    shareCard(stash_share_card, position);
//                }
//            });
//            viewMap.put(position, new WeakReference<>(itemView));
//            container.addView(itemView);
//        } else if (TYPE_VIDEO.equals(itemListViewPager.get(position).getMultiMediaModel().getType())) {
//
//            itemView = (PostPageView) mLayoutInflater.inflate(R.layout.feed_video_with_title_card, container, false);
//            final ImageView rel_image_blur = (ImageView) itemView.findViewById(R.id.image_illustration1);
//            final ImageView thumbnailImage = (ImageView) itemView.findViewById(R.id.image_illustration);
//
//            TextView score = (TextView) itemView.findViewById(R.id.score);
//            score.setText(mContext.getString(R.string.score, getPostScore(position)));
//
//            TextView like_and_comment = (TextView) itemView.findViewById(R.id.like_and_comment);
//            String likeAndComment = "";
//
//            final String videoId = extactVideoId(itemListViewPager.get(position).getMultiMediaModel().getUrl());
//
//            try {
//                likeAndComment = UtilityMethods.withSuffix(Long.parseLong(itemListViewPager.get(position).getLikeCount())) + " Likes· " + UtilityMethods.withSuffix(Long.parseLong(itemListViewPager.get(position).getShareCount())) + " Shares   ";
//            } catch (NumberFormatException e) {
//                likeAndComment = mContext.getString(R.string.like_and_comment, itemListViewPager.get(position).getLikeCount(), itemListViewPager.get(position).getShareCount());
//                e.printStackTrace();
//            }
//            like_and_comment.setText(likeAndComment);
//
//            TextView title = (TextView) itemView.findViewById(R.id.title);
//            String videoTitle = itemListViewPager.get(position).getTitle();
//            if (videoTitle.length() == 0) {
//                videoTitle = mContext.getString(R.string.whatsapp_viral_video);
//            }
//
//            title.setText(videoTitle);
//
//            Glide.with(mContext).asBitmap()
//                    .load("https://img.youtube.com/vi/" + videoId + "/mqdefault.jpg")
//                    .into(new SimpleTarget<Bitmap>() {
//                        @Override
//                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
//                            if (resource != null && !resource.isRecycled()) {
//                                Palette palette = Palette.from(resource).generate();
//                                int color = palette.getDominantColor(Color.parseColor("#000000"));
//                                rel_image_blur.setBackgroundColor(color);
//                                thumbnailImage.setImageBitmap(resource);
//                            }
//                        }
//                    });
//
//           /* TextView body = (TextView) itemView.findViewById(R.id.body);
//            if (itemListViewPager.get(position).getBody() != null && itemListViewPager.get(position).getBody().length() > 0)
//                body.setText(itemListViewPager.get(position).getBody());
//           */
//            final ImageView likeButton = (ImageView) itemView.findViewById(R.id.like_btn);
//            ImageView share_btn = (ImageView) itemView.findViewById(R.id.share_btn);
//            final TextView like_text = (TextView) itemView.findViewById(R.id.like_text);
//            TextView share_text = (TextView) itemView.findViewById(R.id.share_text);
//            if (itemListViewPager.get(position).getCanLike().equals("true")) {
//                likeButton.setImageDrawable(mContext.getResources().getDrawable(R.drawable.heart));
//                like_text.setText(R.string.like_space);
//            } else {
//                likeButton.setImageDrawable(mContext.getResources().getDrawable(R.drawable.heart_on));
//                like_text.setText(R.string.liked);
//            }
//            likeButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    String canLike = itemListViewPager.get(position).getCanLike();
//                    if ("true".equals(canLike)) {
//                        addLike(likeButton, like_text, position);
//
//
//                    } else {
//                        removeLike(likeButton, like_text, position);
//
//                    }
//
//                }
//            });
//            like_text.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    String canLike = itemListViewPager.get(position).getCanLike();
//
//                    if ("true".equals(canLike)) {
//                        addLike(likeButton, like_text, position);
//
//
//                    } else {
//                        removeLike(likeButton, like_text, position);
//                    }
//                }
//            });
//            final PostPageView finalItemView = itemView;
//            final CardView stash_share_card = (CardView) finalItemView.findViewById(R.id.card_view);
//            stash_share_card.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    activityCallback.reverseToolbarCallBack();
//                }
//            });
//
//            share_btn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    shareCard(stash_share_card, position);
//                }
//            });
//            share_text.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    shareCard(stash_share_card, position);
//                }
//            });
//
//
//            final String finalLikeAndComment = likeAndComment;
//            thumbnailImage.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    showFullScreenVideo(position, videoId, finalLikeAndComment);
//                }
//            });
//
//            title.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    showFullScreenVideo(position, videoId, finalLikeAndComment);
//                }
//            });
//            viewMap.put(position, new WeakReference<>(itemView));
//            container.addView(itemView);
//
//        }
//
//        if (position == getCount() - 1) {
//            if (mCallback != null) {
//                mCallback.loadPosts(TAG_NEXT);
//            }
//        }
//        return itemView;
//    }
//
//    private void showFullScreenVideo(int position, String videoId, String finalLikeAndComment) {
//        String valueContent = itemListViewPager.get(position).getTag() + "_" +
//                itemListViewPager.get(position).getMultiMediaModel().getSource() + "_" + FEED_TYPE;
//
//        if (fromBubble) {
//            new FeedAnalytics(mContext).sendAnalytics(AnalyticsConstants.Category.CONTENT_BUBBLE, AnalyticsConstants.Action.TOTAL_VIDEO_SEEN, valueContent, null);
//        }
//        new FeedAnalytics(mContext).sendAnalytics(AnalyticsConstants.Category.CONTENT, AnalyticsConstants.Action.TOTAL_VIDEO_SEEN, valueContent, null);
//
//        Intent intent = new Intent(mContext, FeedFullScreenYoutube.class);
//        intent.putExtra(Constants.FULL_SCREEN_TYPE, Constants.FULL_YOUTUBE_VIDEO);
//        intent.putExtra(Constants.PARAM_TITLE, itemListViewPager.get(position).getTitle());
//        intent.putExtra(Constants.PARAM_DESCRIPTION, itemListViewPager.get(position).getBody());
//        intent.putExtra(Constants.PARAM_LIKE_AND_COMMENT, finalLikeAndComment);
//        intent.putExtra(Constants.PARAM_URL, videoId);
//
//        mContext.startActivity(intent);
//    }
//
//    private void openFullContent(int position) {
//        new FinestWebView.Builder(mContext).show(itemListViewPager.get(position).getUrl());
//
//
//        String valueContent = itemListViewPager.get(position).getTag() + "_" +
//                itemListViewPager.get(position).getMultiMediaModel().getSource() + "_" + FEED_TYPE;
//        if (fromBubble) {
//            new FeedAnalytics(mContext).sendAnalytics(AnalyticsConstants.Category.CONTENT_BUBBLE, AnalyticsConstants.Action.TOTAL_POSTS_SEEN, valueContent, null);
//        }
//        new FeedAnalytics(mContext).sendAnalytics(AnalyticsConstants.Category.CONTENT, AnalyticsConstants.Action.TOTAL_POST_OPENED, valueContent, null);
//    }
//
//    private float getPostScore(int position) {
//        ViewPagerModel viewPagerModel = itemListViewPager.get(position);
//        try {
//            int like = Integer.parseInt(viewPagerModel.getLikeCount());
//            int disLike = Integer.parseInt(viewPagerModel.getDisLikeCount());
//            int shareCount = Integer.parseInt(viewPagerModel.getShareCount());
//            float score = (((like - disLike) / 1000.0f) * 0.333f + (shareCount / 500.0f) * 0.666f) * 10.0f;
//            return score > 10.0f ? 10.0f : score;
//        } catch (NumberFormatException | ArithmeticException e) {
//            e.printStackTrace();
//        }
//        return 5.0f;
//    }
//
//    private String extactVideoId(String path) {
//        String pattern = "(?<=watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";
//
//        Pattern compiledPattern = Pattern.compile(pattern);
//        Matcher matcher = compiledPattern.matcher(path);
//
//        if (matcher.find()) {
//            return matcher.group();
//        }
//        return "";
//    }
//
//    private void shareCard(CardView stash_share_card, int position) {
//
//
//        //Analytics
//
//        String valueContent = itemListViewPager.get(position).getTag() + "_" +
//                itemListViewPager.get(position).getMultiMediaModel().getSource() + "_" + FEED_TYPE;
//        if ("image".equals(itemListViewPager.get(position).getMultiMediaModel().getType())) {
//            new FeedAnalytics(mContext).sendAnalytics(AnalyticsConstants.Category.CONTENT, AnalyticsConstants.Action.TOTAL_SHARED_POST, valueContent, null);
//        } else if ("video".equals(itemListViewPager.get(position).getMultiMediaModel().getType())) {
//            new FeedAnalytics(mContext).sendAnalytics(AnalyticsConstants.Category.CONTENT, AnalyticsConstants.Action.TOTAL_SHARED_VIDEOS, valueContent, null);
//
//        }
//
//        File directorySent = new File(Constants.WHATSAPP_IMAGES_PATH_SENT);
//        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
//        String whatsappFolderModifiedDate = sdf.format(directorySent.lastModified());
//        PrefManager.getInstance(mContext).putString(PrefManager.LAST_MODIFIED_WHATSAPP_SENT, whatsappFolderModifiedDate);
//        PrefManager.getInstance(mContext).putBoolean(PrefManager.WHATSAPP_SHARE_STARTED, true);
//        stash_share_card.setDrawingCacheEnabled(true);
//        stash_share_card.buildDrawingCache();
//        Bitmap bm = stash_share_card.getDrawingCache();
//        Bitmap icon = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.below_share_card_image);
//        Bitmap temp = UtilityMethods.combineImages(bm, icon);
//        FileOutputStream out = null;
//        String fileName = Environment.getExternalStorageDirectory().getPath() + Constants.STASH + System.currentTimeMillis() + ".png";
//        PrefManager.getInstance(mContext).putString(PrefManager.WHATSAPP_IMAGE_PREPARED, fileName);
//        PrefManager.getInstance(mContext).putString(PrefManager.WHATSAPP_TITLE_PREPARED, itemListViewPager.get(position).getTitle() + mContext.getString(R.string.share_text_feed));
//
//        try {
//            File file = new File(fileName);
//            file.createNewFile();
//            out = new FileOutputStream(fileName);
//            temp.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
//            // PNG is a lossless format, the compression factor (100) is ignored
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (out != null) {
//                    out.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        Uri imageUri = Uri.parse(fileName);
//        Intent shareIntent = new Intent();
//        shareIntent.setAction(Intent.ACTION_SEND);
//        shareIntent.setPackage("com.whatsapp");
//        shareIntent.putExtra(Intent.EXTRA_TEXT, itemListViewPager.get(position).getTitle() + mContext.getString(R.string.share_text_feed));
//        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
//        shareIntent.setType("image/jpeg");
//        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        try {
//            mContext.startActivity(shareIntent);
//        } catch (android.content.ActivityNotFoundException ex) {
//            Toast.makeText(mContext, "Whatsapp have not been installed.", Toast.LENGTH_SHORT).show();
//
//        }
//
//    }
//
//
//    private void removeLike(ImageView like_and_comment, TextView like_text, int position) {
//        like_and_comment.setImageDrawable(mContext.getResources().getDrawable(R.drawable.heart));
//        itemListViewPager.get(position).setCanLike("true");
//        like_text.setText(R.string.like_space);
//        AppDBHelper.getInstance(mContext).setLike(itemListViewPager.get(position).getId(), Constants.UNLIKE);
//
//    }
//
//
//    private void addLike(ImageView like_and_comment, TextView like_text, int position) {
//        like_and_comment.setImageDrawable(mContext.getResources().getDrawable(R.drawable.heart_on));
//        itemListViewPager.get(position).setCanLike("false");
//        like_text.setText(R.string.liked);
//        AppDBHelper.getInstance(mContext).setLike(itemListViewPager.get(position).getId(), Constants.LIKE);
//
//
//        //Analytics
//
//        String valueContent = itemListViewPager.get(position).getTag() + "_" +
//                itemListViewPager.get(position).getMultiMediaModel().getSource() + "_" + FEED_TYPE;
//        if ("image".equals(itemListViewPager.get(position).getMultiMediaModel().getType())) {
//            new FeedAnalytics(mContext).sendAnalytics(AnalyticsConstants.Category.CONTENT, AnalyticsConstants.Action.TOTAL_LIKED_POST, valueContent, null);
//        } else if ("video".equals(itemListViewPager.get(position).getMultiMediaModel().getType())) {
//            new FeedAnalytics(mContext).sendAnalytics(AnalyticsConstants.Category.CONTENT, AnalyticsConstants.Action.TOTAL_LIKED_VIDEOS, valueContent, null);
//
//        }
//
//    }
//
//    public void destroyItem(ViewGroup container, int position, Object object) {
//        container.removeView((PostPageView) object);
//    }
//
//    public void performItemClick(int position, int x, int y) {
//
//        WeakReference<PostPageView> weakReference = viewMap.get(position);
//        if (weakReference != null) {
//            PostPageView postPageView = weakReference.get();
//            if (postPageView != null) {
//                postPageView.clickAction(x, y);
//            }
//        }
//    }
//
//    public void setCallback(PostCallback mCallback) {
//        this.mCallback = mCallback;
//    }
//
//    public interface PostCallback {
//        void loadPosts(int tag);
//
//        void openWebView(String url);
//    }
//
//}
