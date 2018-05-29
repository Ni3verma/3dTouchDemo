package com.importio.nitin.a3dtouchdemo;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private ArrayList<RowData> eList;
    Context mContext;
    Dialog dialog;
    MyAdapter(ArrayList<RowData> a, Context context){
        this.eList = a;
        this.mContext=context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contest_list_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        RowData event = eList.get(position);
        holder.name.setText(event.data);
    }

    @Override
    public int getItemCount() {
        return eList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView name;
        public MyViewHolder(View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.name);
            itemView.setOnTouchListener(touchListener);
        }
        View.OnTouchListener touchListener=new View.OnTouchListener() {
            Handler handler=new Handler();
            Runnable longPressed=new Runnable() {
                @Override
                public void run() {
                    View dialogLayout= LayoutInflater.from(mContext).inflate(R.layout.peek_dialog,null);
                    dialog = new Dialog(mContext);

                    TextView name_dialog=dialogLayout.findViewById(R.id.name);

                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.setContentView(dialogLayout);
                    dialog.getWindow().setWindowAnimations(R.style.PeekAnimation);
                    dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            ((MainActivity)mContext).removeBlur();
                        }
                    });

                    name_dialog.setText(name.getText());

                    ((MainActivity)mContext).blurBackground();

                    dialog.show();
                }
            };

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP){
                    handler.removeCallbacks(longPressed);
                    Log.d("Nitin","ACTION UP");
                    ((MainActivity)mContext).removeBlur();

                    hideDialog();
                    return false;
                }
                else if (event.getAction() == MotionEvent.ACTION_DOWN){
                    handler.postDelayed(longPressed,250);
                    return true;
                }

                handler.removeCallbacks(longPressed);
                return false;
            }
        };
    }
    void hideDialog(){
        if (dialog != null)
            dialog.dismiss();
    }
}
