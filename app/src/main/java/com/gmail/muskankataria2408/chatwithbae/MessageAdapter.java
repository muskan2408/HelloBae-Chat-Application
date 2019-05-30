package com.gmail.muskankataria2408.chatwithbae;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.gmail.muskankataria2408.chatwithbae.R.drawable.messge_sent;

/**
 * Created by Muskan on 24/07/17.
 */

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    String from_user,curentUseid;
    private List<Messages> mMessageList;
    private DatabaseReference mUserDatabase;
    FirebaseAuth mAuth;
    private static final int VIEW_HOLDER_TYPE_1=1;
    private static final int VIEW_HOLDER_TYPE_2=2;

    public MessageAdapter(List<Messages> mMessageList) {

        this.mMessageList = mMessageList;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v;
        mAuth = FirebaseAuth.getInstance();
        curentUseid=mAuth.getCurrentUser().getUid();



        switch (viewType) {
            // create a new view

            case VIEW_HOLDER_TYPE_1:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.message_single_layout, parent, false);
                return new MessageViewHolder(v);

            case VIEW_HOLDER_TYPE_2:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.message_recieve, parent, false);
                return new MyMessageViewHolder(v);

            default:
                break;
        }

        return null;


    }



    public class MessageViewHolder extends RecyclerView.ViewHolder {

        public TextView messageText;
        public CircleImageView profileImage;
        public TextView displayName,timeText;
        public ImageView messageImage;
        public RelativeLayout MessageView;

        public MessageViewHolder(View view) {
            super(view);
            messageText = (TextView) view.findViewById(R.id.message_text_layout);
            profileImage = (CircleImageView) view.findViewById(R.id.message_profile_layout);
            displayName = (TextView) view.findViewById(R.id.name_text_layout);
            messageImage = (ImageView) view.findViewById(R.id.message_image_layout);
            MessageView=(RelativeLayout)view.findViewById(R.id.relative);
            timeText=(TextView)view.findViewById(R.id.time_text_layout);
        }
    }

    public class MyMessageViewHolder extends RecyclerView.ViewHolder {

        public TextView messageText;
      //  public CircleImageView profileImage;
        public TextView timeText,displayName;
        public ImageView messageImage;
        public RelativeLayout MessageView;

        public MyMessageViewHolder(View view) {
            super(view);
            messageText = (TextView) view.findViewById(R.id.message_text_layout);
          //  profileImage = (CircleImageView) view.findViewById(R.id.message_profile_layout);
            displayName = (TextView) view.findViewById(R.id.name_text_layout);
            messageImage = (ImageView) view.findViewById(R.id.message_image_layout);
            MessageView=(RelativeLayout)view.findViewById(R.id.relative);
            timeText=(TextView)view.findViewById(R.id.time_text_layout);
        }
    }


    @Override
    public int getItemCount() {
        return mMessageList.size();
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, int i) {
        Messages c = mMessageList.get(i);
        c = mMessageList.get(i);
        from_user = c.getFrom();

        String current_user_id = mAuth.getCurrentUser().getUid();

        switch (getItemViewType(i)) {

            case VIEW_HOLDER_TYPE_1:
                c = mMessageList.get(i);
                from_user = c.getFrom();
                final MessageViewHolder viewHolder1=(MessageViewHolder)viewHolder;
                String message_type = c.getType();
                long time=mMessageList.get(i).getTime();
//                LocalDateTime dateTime = LocalDateTime.ofEpochSecond(time, 0, ZoneOffset.UTC);
//                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH);
//                String formattedDate = dateTime.format(formatter);
              // java.sql.Time timeValue = new java.sql.Time((long)formatter.parse(formattedDate)).getTime();
//                ZoneId zoneId = ZoneId.of("Asia/Kolkata");
//                LocalTime localTime= LocalTime.now(zoneId);
//                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm a");
//                String formattedDate=localTime.format(formatter);


                Date date = new Date(time);
                String strDateFormat = "hh:mm a";
                DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
                String formattedDate= dateFormat.format(date);


                viewHolder1.MessageView.setBackgroundResource(R.drawable.messge_sent);
                    viewHolder1.messageText.setTextColor(Color.WHITE);
                    viewHolder1.displayName.setTextColor(Color.WHITE);
                    viewHolder1.timeText.setTextColor(Color.WHITE);
                    viewHolder1.timeText.setText(formattedDate);



                mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(from_user);
                mUserDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        String name = dataSnapshot.child("name").getValue().toString();
                        String image = dataSnapshot.child("thumb_image").getValue().toString();

                        viewHolder1.displayName.setText(name);

                        Picasso.with(viewHolder1.profileImage.getContext()).load(image)
                                .placeholder(R.drawable.default_avatar).into(viewHolder1.profileImage);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                if (message_type.equals("text")) {

                    viewHolder1.messageText.setText(c.getMessage());
                    viewHolder1.messageImage.setVisibility(View.INVISIBLE);


                } else {

                    viewHolder1.messageText.setVisibility(View.INVISIBLE);
                    viewHolder1.MessageView.setVisibility(View.INVISIBLE);
                    Picasso.with(viewHolder1.messageImage.getContext()).load(c.getMessage())
                            .placeholder(R.drawable.default_avatar).into(viewHolder1.messageImage);

                }
                break;

            case VIEW_HOLDER_TYPE_2:
                current_user_id = mAuth.getCurrentUser().getUid();
                 c = mMessageList.get(i);
                from_user = c.getFrom();
                final MyMessageViewHolder viewHolder2=(MyMessageViewHolder)viewHolder;
                long time1=mMessageList.get(i).getTime();

//                LocalDateTime dateTime1 =LocalDateTime.ofEpochSecond(time1, 0, ZoneOffset.UTC);
//
//                DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH);
//                String formattedDate1 = dateTime1.format(formatter1);

                Date date1 = new Date(time1);
                String strDateFormat1 = "hh:mm a";
                DateFormat dateFormat1 = new SimpleDateFormat(strDateFormat1);
                String formattedDate1= dateFormat1.format(date1);


                message_type = c.getType();

                    viewHolder2.MessageView.setBackgroundResource(R.drawable.message_recieve);
                    viewHolder2.messageText.setTextColor(Color.BLACK);
                    viewHolder2.displayName.setTextColor(Color.BLACK);
                    viewHolder2.timeText.setTextColor(Color.BLACK);



                mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(from_user);
                mUserDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        String name = dataSnapshot.child("name").getValue().toString();
                        String image = dataSnapshot.child("thumb_image").getValue().toString();

                       viewHolder2.displayName.setText(name);
                       //   viewHolder2.displayName.setVisibility(View.GONE);
//                        Picasso.with(viewHolder2.profileImage.getContext()).load(image)
//                                .placeholder(R.drawable.default_avatar).into(viewHolder2.profileImage);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                if (message_type.equals("text")) {

                    viewHolder2.messageText.setText(c.getMessage());
                    viewHolder2.messageImage.setVisibility(View.INVISIBLE);


                } else {

                    viewHolder2.messageText.setVisibility(View.INVISIBLE);
                    viewHolder2.MessageView.setVisibility(View.INVISIBLE);
                    Picasso.with(viewHolder2.messageImage.getContext()).load(c.getMessage())
                            .placeholder(R.drawable.default_avatar).into(viewHolder2.messageImage);

                }
               viewHolder2.timeText.setText(formattedDate1);

                break;

            default:
                break;
        }

    }
    @Override
    public int getItemViewType(int position) {
        // Just as an example, return 1 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous
        if (mMessageList.get(position).isFromMe())
            return VIEW_HOLDER_TYPE_2;
        else
            return VIEW_HOLDER_TYPE_1;
    }


}
