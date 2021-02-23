package com.example.nfctest2;

import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

public class Videoplay_Fragment extends Fragment {

    private int video;
    private VideoView mVideo;
    private int videocondition;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        int video = bundle.getInt("video");
        this.video = video;

        // make sure the orientation of the fragment is landscape
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_videoplay__fragment, container, false);

        mVideo= (VideoView)view.findViewById(R.id.videoView1);

        Uri uri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + video);

        mVideo.setVideoURI(uri);

        MediaController mediaController = new MediaController(getActivity());
        mediaController.setAnchorView(mVideo);
        mVideo.setMediaController(mediaController);


        mVideo.start();
        return view;
    }


    public void videoPause(){
        mVideo.pause();
    }

    public void videoResume(){
        mVideo.start();
    }

    public void videopauseplay(){
        if(mVideo.isPlaying()==true){
            mVideo.pause();
        }
        else if(mVideo.isPlaying()==false){
            mVideo.start();
        }

        else if(mVideo==null){
            return;
        }
    }

    public static Videoplay_Fragment newInstance(int video) {
        Videoplay_Fragment fragment = new Videoplay_Fragment();
        Bundle bundle = new Bundle();
        bundle.putInt("video", video);
        fragment.setArguments(bundle);
        return fragment;
    }

}

