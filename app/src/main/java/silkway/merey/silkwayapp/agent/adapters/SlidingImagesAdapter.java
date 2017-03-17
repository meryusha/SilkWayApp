package silkway.merey.silkwayapp.agent.adapters;

/**
 * Created by Merey on 11.02.17.
 */

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import silkway.merey.silkwayapp.R;
import silkway.merey.silkwayapp.classes.TourPhoto;


public class SlidingImagesAdapter extends PagerAdapter {
    //temp array - will be removed when list of tours is obtained
    private ArrayList<Integer> IMAGES;

    private LayoutInflater inflater;
    private Context context;
    private TextView textView;
    private List<TourPhoto> images;
    private ImageView imageView;


    public SlidingImagesAdapter(Context context, List<TourPhoto> images) {
        this.images = images;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        final View imageLayout = inflater.inflate(R.layout.viewpager_images_item, view, false);

        assert imageLayout != null;
        imageView = (ImageView) imageLayout
                .findViewById(R.id.image);
        Glide.with(context).load(images.get(position).getUrl()).into(imageView);

        textView = (TextView) imageLayout.findViewById(R.id.textView);
        textView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                textView.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        textView.setMovementMethod(new ScrollingMovementMethod());
        textView.setText("" + images.get(position).getDescription());
        textView.setVisibility(View.INVISIBLE);
        textView.setEnabled(false);
        imageLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onImageClick(imageLayout);
            }
        });
        view.addView(imageLayout, 0);

        return imageLayout;
    }

    private void onImageClick(View v) {
        textView = (TextView) v.findViewById(R.id.textView);

        if (textView.isEnabled()) {
            imageView.setAlpha(255);
            textView.setVisibility(View.INVISIBLE);
            textView.setEnabled(false);

        } else {
            imageView.setAlpha(150);
            textView.setVisibility(View.VISIBLE);
            textView.setEnabled(true);

        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }


}