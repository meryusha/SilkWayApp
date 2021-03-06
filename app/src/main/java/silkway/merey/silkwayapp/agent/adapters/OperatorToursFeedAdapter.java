package silkway.merey.silkwayapp.agent.adapters;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import silkway.merey.silkwayapp.R;
import silkway.merey.silkwayapp.classes.Category;
import silkway.merey.silkwayapp.classes.Location;
import silkway.merey.silkwayapp.classes.Tour;

public class OperatorToursFeedAdapter extends BaseAdapter {
    private List<Tour> tours;
    private LayoutInflater layoutInflater;
    private Context context;
    ItemViewHolder itemViewHolder;
    private List<Tour> toursCopy = new ArrayList<>();

    public OperatorToursFeedAdapter(Context context, List<Tour> tours) {
        this.context = context;
        this.tours = tours;
        toursCopy.addAll(tours);
        if (context != null) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
    }

    @Override
    public int getCount() {

        Log.d("I will load +", toursCopy.size() + "");
        return toursCopy.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        itemViewHolder = null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_view_item_tour, parent, false);
            itemViewHolder = new ItemViewHolder(convertView);
            convertView.setTag(itemViewHolder);
            // ViewGroup.LayoutParams lp = itemViewHolder.tourImageView.getLayoutParams();
            // lp.width = 450;
            // lp.height = 450;
            itemViewHolder.tourImageView.requestLayout();
            //itemViewHolder.postImageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            // itemViewHolder.tourImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            itemViewHolder.tourImageView.setPadding(5, 5, 5, 5);
        } else {
            itemViewHolder = (ItemViewHolder) convertView.getTag();
        }

        final Tour tour = toursCopy.get(position);
        if (tour.getOperator() != null) {
            //tour is already subscribed
            if (tour.getOperator().getObjectId().equals(Backendless.UserService.CurrentUser().getObjectId())) {
                // itemViewHolder.tourNumberOffersTextView.setText(tour.getNumberProposals() + "");
                itemViewHolder.tourStatusImageView.setImageResource(R.drawable.circle_green);
            } else {
                itemViewHolder.tourStatusImageView.setImageResource(R.drawable.circle_blue);
            }
        } else {
            itemViewHolder.tourStatusImageView.setVisibility(View.INVISIBLE);
        }
        itemViewHolder.tourImageView.setBackgroundResource(R.color.transpBlack);
        //  itemViewHolder.tourImageView.setColorFilter(Color.BLACK, PorterDuff.Mode.ADD);
        itemViewHolder.tourPriceTextView.setText("Цена: " + tour.getPrice() + "тг");
        itemViewHolder.tourNameTextView.setText(tour.getTitle() + "");
        if (tour.getLocation() != null) {
            itemViewHolder.tourLocationTextView.setText("Локация: " + tour.getLocation().getName() + "");
        }
        if (tour.getAuthor() != null && tour.getAuthor().getProperty("name") != null) {
            itemViewHolder.tourAuthorTextView.setText("Автор: " + tour.getAuthor().getProperty("name").toString());
        }
        if (tour.getCategory() != null) {
            itemViewHolder.tourCategoryTextView.setText("Категория: " + tour.getCategory().getName());
        }
        if (tour.getAvatarUrl() != null) {
            Glide.with(context).load(tour.getAvatarUrl()).into(itemViewHolder.tourImageView);
        }
        //itemViewHolder.tourCategoryTextView.setText("Дата Создания: " + tour.get.getName());
        return convertView;
    }


    public class ItemViewHolder {
        TextView tourNameTextView;
        ImageView tourImageView;
        ImageView tourStatusImageView;
        TextView tourPriceTextView;
        TextView tourCategoryTextView;
        TextView tourAuthorTextView;
        TextView tourLocationTextView;
        TextView tourNumberOffersTextView;


        public ItemViewHolder(View v) {
            tourLocationTextView = (TextView) v.findViewById(R.id.tourLocationTextView);
            tourCategoryTextView = (TextView) v.findViewById(R.id.tourCategoryTextView);
            tourAuthorTextView = (TextView) v.findViewById(R.id.tourAuthorTextView);
            tourPriceTextView = (TextView) v.findViewById(R.id.tourPriceTextView);
            tourNameTextView = (TextView) v.findViewById(R.id.tourNameTextView);
            tourImageView = (ImageView) v.findViewById(R.id.tourImageView);
            tourStatusImageView = (ImageView) v.findViewById(R.id.tourStatusImageView);
            tourNumberOffersTextView = (TextView) v.findViewById(R.id.tourNumberOffersTextView);

        }
    }

    public void filter(String charText) {

        charText = charText.toLowerCase(Locale.getDefault());

        toursCopy.clear();
        if (charText.length() == 0) {
            toursCopy.addAll(tours);
        } else {
            for (Tour tour : tours) {
                if (charText.length() != 0 && tour.getTitle().toLowerCase(Locale.getDefault()).contains(charText)) {
                    toursCopy.add(tour);
                } else if (charText.length() != 0 && tour.getDesc().toLowerCase(Locale.getDefault()).contains(charText)) {
                    toursCopy.add(tour);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void filterByCategory(Category category) {
        toursCopy.clear();
        if (category.getName().equals("Все")) {
            toursCopy.addAll(tours);
        } else {
            for (Tour tour : tours) {
                if (tour.getCategory() != null && tour.getCategory().getName().equals(category.getName())) {
                    toursCopy.add(tour);
                }
            }
        }


        notifyDataSetChanged();
    }

    public void filterByLocation(Location location) {
        toursCopy.clear();
        if (location.getName().equals("Все")) {
            toursCopy.addAll(tours);
        } else {
            for (Tour tour : tours) {
                if (tour.getLocation() != null && tour.getLocation().getName().equals(location.getName())) {
                    toursCopy.add(tour);
                }
            }
        }


        notifyDataSetChanged();
    }

    public void getMyTours(BackendlessUser user) {
        toursCopy.clear();
        for (Tour tour : tours) {
            if (tour.getOperator() != null && tour.getOperator().getObjectId().equals(user.getObjectId())) {
                toursCopy.add(tour);
            }
        }
        notifyDataSetChanged();
    }
}



