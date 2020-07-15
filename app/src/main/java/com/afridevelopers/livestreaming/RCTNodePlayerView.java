package com.afridevelopers.livestreaming;

import android.app.Activity;

import cn.nodemedia.NodePlayer;
import cn.nodemedia.NodePlayerDelegate;
import cn.nodemedia.NodePlayerView;

/**
 * Created by Harminder Singh on 15,July,2020
 */
public class RCTNodePlayerView extends NodePlayerView  {
    private NodePlayer mNodePlayer;
    private Boolean isAutoPlay = false;

    public RCTNodePlayerView(Activity context, final LifecycleEventListener lifecycleEventListener,NodePlayerView nodePlayerView) {
        super(context);
        mNodePlayer = new NodePlayer(context,"");
        mNodePlayer.setPlayerView(nodePlayerView);
        mNodePlayer.setNodePlayerDelegate(new NodePlayerDelegate() {
            @Override
            public void onEventCallback(NodePlayer nodePlayer, int i, String s) {
                if (lifecycleEventListener!= null){
                    lifecycleEventListener.codeBack(i,s);
                }
            }
        });
    }





    public void setBufferTime(int bufferTime) {
        mNodePlayer.setBufferTime(bufferTime);
    }

    public void setMaxBufferTime(int maxBufferTime) {
        mNodePlayer.setMaxBufferTime(maxBufferTime);
    }

    public void setScaleMode(String smode) {
        NodePlayerView.UIViewContentMode mode = NodePlayerView.UIViewContentMode.valueOf(smode);
        setUIViewContentMode(mode);
    }

    public void setRenderType(String stype) {
        NodePlayerView.RenderType type =  NodePlayerView.RenderType.valueOf(stype);
        setRenderType(type);
    }


//    public int pause() {
//        return mNodePlayer.pause();
//    }
//
//    public int start() {
//        return mNodePlayer.start();
//    }
//
//    public int stop() {
//        return mNodePlayer.stop();
//    }

    public void release(){
        mNodePlayer.release();
    }

    public void autoPlay() {
        isAutoPlay = true;
       mNodePlayer.start();
    }

    public void play(String inputUrl) {

        mNodePlayer.setInputUrl(inputUrl);
        mNodePlayer.start();
    }

    public void onHostResume() {

    }

    public void onHostPause() {

    }

    public Boolean isPlaying(){
      return   mNodePlayer.isPlaying();
    }


    public void onHostDestroy() {
        mNodePlayer.stop();
        release();
    }
    public void stop() {
        mNodePlayer.stop();
        release();
    }
}
