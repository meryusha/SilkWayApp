package silkway.merey.silkwayapp.adapters;

/**
 * Created by Merey on 16.02.17.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import silkway.merey.silkwayapp.R;
import silkway.merey.silkwayapp.classes.TourImage;

public class PhotosListViewAdapter extends BaseAdapter {


    private List<TourImage> images;
    private Context context;
    private LayoutInflater layoutInflater;

    public PhotosListViewAdapter(Context context, List<TourImage> images) {
        this.context = context;
        this.images = images;
        if (context != null) {
            layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        }
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.agent_tour_prop_list_view_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.imageView.setImageResource(R.drawable.nature);
        viewHolder.textView.setText("Белорусский санаторий «Боровое» получил свое название не случайно. Он уютно расположился вдалеке от областных городов и столицы, от бесконечного движения, спешки и шума, в величественном сосновом бору, на берегу живописного озера, в которое впадает река с мелодичным, как хрустальный звон ы");
        // viewHolder.categoryName.setText(images.get(position).getName());
        // Log.d(TAG, apartments.get(position).getImage());
        // Glide.with(context).load(images.get(position).getImageURL()).centerCrop().into(viewHolder.categoryImageView);
        return convertView;
    }

    private class ViewHolder {
        ImageView imageView;
        TextView textView;


        public ViewHolder(View v) {
            imageView = (ImageView) v.findViewById(R.id.imageView);
            textView = (TextView) v.findViewById(R.id.textView);
        }
    }
}