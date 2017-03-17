package silkway.merey.silkwayapp.agent.adapters;

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
import silkway.merey.silkwayapp.classes.TourProposal;

/**
 * Created by Merey on 17.02.17.
 */

public class AgentViewOfferListViewAdapter extends BaseAdapter {

    private List<TourProposal> proposals;
    private Context context;
    private LayoutInflater layoutInflater;

    public AgentViewOfferListViewAdapter(Context context, List<TourProposal> proposals) {
        this.context = context;
        this.proposals = proposals;
        if (context != null) {
            layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        }
    }

    @Override
    public int getCount() {
        return proposals.size();
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
        AgentViewOfferListViewAdapter.ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_view_item_agent_view_offer, null);
            viewHolder = new AgentViewOfferListViewAdapter.ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (AgentViewOfferListViewAdapter.ViewHolder) convertView.getTag();
        }
        viewHolder.agentNameTextView.setText("" + proposals.get(position).getFrom().getProperty("name"));
        Glide.with(context).load(proposals.get(position).getFrom().getProperty("avatarUrl")).centerCrop().into(viewHolder.imageView);
        return convertView;
    }

    private class ViewHolder {
        ImageView imageView;
        TextView numberChangesTextView;
        TextView agentNameTextView;


        public ViewHolder(View v) {
            imageView = (ImageView) v.findViewById(R.id.imageView);
            numberChangesTextView = (TextView) v.findViewById(R.id.numberChangesTextView);
            agentNameTextView = (TextView) v.findViewById(R.id.agentNameTextView);
        }
    }
}