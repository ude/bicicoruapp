package com.udepardo.bicicoru.ui.userdata.summary;

import com.udepardo.bicicoru.R;
import com.udepardo.bicicoru.domain.model.TrackViewModel;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * Created by ude on 21/7/17.
 */

public class UserTrackAdapter extends RecyclerView.Adapter<UserTrackAdapter.ViewHolder> {
	private List<TrackViewModel> items = new ArrayList<>();

	public UserTrackAdapter() {
		setHasStableIds(true);
	}

	public void updateModel(List<TrackViewModel> items) {
		Collections.sort(items, new Comparator<TrackViewModel>() {
			@Override
			public int compare(TrackViewModel o1, TrackViewModel o2) {
				return Long.compare(o2.getId(), o1.getId());
			}
		});
		this.items.clear();
		this.items.addAll(items);
		notifyDataSetChanged();
	}


	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_track, null);
		ViewHolder viewHolder = new ViewHolder(view);
		return viewHolder;
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		holder.origen.setText(items.get(position).getBeginStation());
		holder.destino.setText(items.get(position).getEndStation());
		holder.duration.setText(items.get(position).getDuration());
		holder.date.setText(items.get(position).getBeginTime());
	}

	@Override
	public int getItemCount() {
		return (null != items ? items.size() : 0);

	}

	@Override
	public long getItemId(int position) {
		return items.get(position).getId();
	}

	public static class ViewHolder extends RecyclerView.ViewHolder {
		TextView origen;
		TextView destino;
		TextView duration;
		TextView date;

		public ViewHolder(View itemView) {
			super(itemView);
			origen = itemView.findViewById(R.id.origen);
			destino = itemView.findViewById(R.id.destino);
			duration = itemView.findViewById(R.id.duration);
			date = itemView.findViewById(R.id.date);
		}
	}
}
