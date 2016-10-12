package com.zmt.e_read.Utils;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zmt.e_read.R;

/**
 * Created by Dangelo on 2016/9/29.
 */
public class ProgressViewHolder extends RecyclerView.ViewHolder {

    public ProgressBar progressBar;
    public TextView textView;

    public ProgressViewHolder(View itemView) {
        super(itemView);
        progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
        textView = (TextView) itemView.findViewById(R.id.textView);
    }
}
