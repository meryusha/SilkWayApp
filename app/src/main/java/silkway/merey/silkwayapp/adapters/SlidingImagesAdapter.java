package silkway.merey.silkwayapp.adapters;

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
import java.util.ArrayList;

import silkway.merey.silkwayapp.R;
import silkway.merey.silkwayapp.classes.Tour;


public class SlidingImagesAdapter extends PagerAdapter {
    //temp array - will be removed when list of tours is obtained
    private ArrayList<Integer> IMAGES;

    private LayoutInflater inflater;
    private Context context;
    private TextView textView;
    private ArrayList<Tour> tours;


    public SlidingImagesAdapter(Context context, ArrayList<Integer> IMAGES, ArrayList<Tour> tours) {
        this.tours = tours;
        this.context = context;
        this.IMAGES = IMAGES;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        //will return size of images for this tour
        return IMAGES.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        final View imageLayout = inflater.inflate(R.layout.viewpager_images_item, view, false);

        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout
                .findViewById(R.id.image);

        //will retrieve image from TOUR_IMAGE relationship
        imageView.setImageResource(IMAGES.get(position));


        textView = (TextView) imageLayout.findViewById(R.id.textView);
        textView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                textView.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        textView.setMovementMethod(new ScrollingMovementMethod());
        imageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImageClick(imageLayout, position);
            }
        });
        view.addView(imageLayout, 0);

        return imageLayout;
    }

    private void onImageClick(View v, int position) {
        textView = (TextView) v.findViewById(R.id.textView);
        textView.setText(tours.get(position).getDescription() + position);


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