package silkway.merey.silkwayapp.agent.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import silkway.merey.silkwayapp.classes.Category;

/**
 * Created by Merey on 25.02.17.
 */

public class CategoryAdapter extends ArrayAdapter<Category> {

    private Context context;
    private List<Category> categoryList;

    public CategoryAdapter(Context context, int resource, List<Category> categoryList) {
        super(context, resource, categoryList);
        this.context = context;
        this.categoryList = categoryList;
    }

    public int getCount() {
        return categoryList.size();
    }

    public Category getItem(int position) {
        return categoryList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = new TextView(context);
        view.setTextColor(Color.BLACK);
        view.setGravity(Gravity.CENTER);
        view.setText(categoryList.get(position).getName());

        return view;
    }

    //View of Spinner on dropdown Popping

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView view = new TextView(context);
        view.setTextColor(Color.BLACK);
        view.setText(categoryList.get(position).getName());
        view.setHeight(100);

        return view;
    }
}
