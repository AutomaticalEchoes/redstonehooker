package com.automaticalechoes.redstonehooker.api.messageEntityBlock;

import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

public interface MessagesPreviewable {
    Component messages();
    boolean shouldShowMessages();
    default float width(){
        return 1.0F;
    }

    default float border(){
        return 0.1F;
    }

    default int textColor(){
        return 0;
    }

}
