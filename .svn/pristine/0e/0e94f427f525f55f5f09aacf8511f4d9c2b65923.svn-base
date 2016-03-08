package com.shanghai.haojiajiao.provider;

import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.model.ConversationProviderTag;
import io.rong.imkit.widget.provider.CameraInputProvider;
import io.rong.imkit.widget.provider.ImageInputProvider;
import io.rong.imkit.widget.provider.InputProvider;
import io.rong.imkit.widget.provider.LocationInputProvider;
import io.rong.imkit.widget.provider.PrivateConversationProvider;
import io.rong.imlib.model.Conversation;

/**
 * Created by chenyajun on 2016/1/2.
 */
@ConversationProviderTag(conversationType = "private", portraitPosition = 2)
public class MyPrivateConversationProvider extends PrivateConversationProvider {

    public MyPrivateConversationProvider(){
        //我需要让他显示的内容的数组  此处为图片与拍照
        InputProvider.ExtendProvider[] ep = {new ImageInputProvider(RongContext.getInstance()),new CameraInputProvider(RongContext.getInstance())};
        //我需要让他在什么会话类型中的 bar 展示
        RongIM.resetInputExtensionProvider(Conversation.ConversationType.PRIVATE, ep);
    }



}
