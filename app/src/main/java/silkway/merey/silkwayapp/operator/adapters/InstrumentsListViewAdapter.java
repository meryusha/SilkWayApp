package silkway.merey.silkwayapp.operator.adapters;

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

import java.util.List;

import silkway.merey.silkwayapp.R;

public class InstrumentsListViewAdapter extends BaseAdapter {


    private List<String> instruments;
    private Context context;
    private List<Integer> images;
    private LayoutInflater layoutInflater;

    public InstrumentsListViewAdapter(Context context, List<String> instruments, List<Integer> images) {
        this.images = images;
        this.context = context;
        this.instruments = instruments;
        if (context != null) {
            layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        }
    }

    @Override
    public int getCount() {
        return instruments.size();
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
            convertView = layoutInflater.inflate(R.layout.list_view_item_operator_instruments, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.imageView.setImageResource(images.get(position));
        viewHolder.textView.setText(instruments.get(position)); // viewHolder.categoryName.setText(images.get(position).getName());
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