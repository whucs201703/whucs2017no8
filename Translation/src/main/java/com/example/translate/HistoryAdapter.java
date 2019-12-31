package com.example.translate;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.translate.litepal.TranslationHistory;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HisHolder> {
    private List<TranslationHistory> mHistoryList;

    public HistoryAdapter(List<TranslationHistory> mHistoryList) {
        this.mHistoryList = mHistoryList;
    }

    public static class HisHolder extends RecyclerView.ViewHolder {
        TextView from_textView;
        TextView to_textView;
        public HisHolder(View itemView) {
            super(itemView);
            from_textView = itemView.findViewById(R.id.from_text);
            to_textView = itemView.findViewById(R.id.to_text);
        }
    }

    @NonNull
    @Override
    public HisHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_piece, parent, false);
        return new HisHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HisHolder holder, int position) {
        holder.from_textView.setText(mHistoryList.get(position).getFrom());
        holder.to_textView.setText(mHistoryList.get(position).getTo());
    }

    @Override
    public int getItemCount() {
        return mHistoryList.size();
    }
}
