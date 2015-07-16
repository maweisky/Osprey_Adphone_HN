package net.tsz.afinal.bitmap.display;

/*
 * Copyright 2014 Pedro Álvarez Fernández <pedroafa@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import net.tsz.afinal.bitmap.core.BitmapDisplayConfig;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * A Drawable that draws an oval with given {@link android.graphics.Bitmap} adding a border with a given
 * color and width
 */
public class BorderedRoundedAvatarDrawable extends RoundedAvatarDrawable {

    private final AvatarBorder mAvatarBorder;
    private BitmapDisplayConfig displayConfig;
    public BorderedRoundedAvatarDrawable(AvatarBorder avatarBorder,BitmapDisplayConfig displayConfig, Bitmap bitmap) {
        super(bitmap);
        mAvatarBorder = avatarBorder;
        this.displayConfig=displayConfig;
    }

    @Override
    public void draw(Canvas canvas) {
        float borderWidth = mAvatarBorder.getRoundWidth();

        Paint paintBorder = new Paint();
        paintBorder.setAntiAlias(true);
        paintBorder.setColor(mAvatarBorder.getColor());
        paintBorder.setStyle(Paint.Style.STROKE);
        paintBorder.setStrokeWidth(borderWidth);

        int viewWidth = getIntrinsicWidth() - ((int)borderWidth * 2);
        int viewHeight = getIntrinsicHeight() - ((int)borderWidth * 2);
        int circleWidthCenter = viewWidth / 2;
        int circleHeightCenter = viewHeight / 2;
        float radius;
        if(circleWidthCenter>circleHeightCenter){
        	if(displayConfig!=null&&displayConfig.getBitmapHeight()!=0){
        		if(circleWidthCenter>circleHeightCenter){
            		radius=circleHeightCenter;
            	}else{        		
            		radius=circleWidthCenter;
            	}
        	}else{
        		radius=circleHeightCenter;
        	}
        }else{
        	if(displayConfig!=null && displayConfig.getBitmapWidth()!=0){
        		if(circleWidthCenter>circleHeightCenter){
            		radius=circleHeightCenter;
            	}else{
            		radius=circleWidthCenter;
            	}
        	}else{
        		radius=circleWidthCenter;
        	}
        }
        canvas.drawCircle(circleWidthCenter + borderWidth, circleHeightCenter + borderWidth,
        		radius + borderWidth - 4.0f, paintBorder);
        canvas.drawCircle(circleWidthCenter + borderWidth, circleHeightCenter + borderWidth,
        		radius, getPaint());
    }
}