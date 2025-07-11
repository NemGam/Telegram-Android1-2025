package org.telegram.ui;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.renderscript.RenderScript;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.R;

public class ActionButton extends LinearLayout {

    private final ImageView iconView;
    private final TextView labelView;
    private int priority;

    public ActionButton(@NonNull Context context) {
        this(context, null);
    }

    public ActionButton(@NonNull Context context, @Nullable AttributeSet attrs){
        this(context, attrs, "", 0, -1, null);
    }

    public ActionButton(@NonNull Context context, String text, int drawable, View.OnClickListener onClick){
        this(context, null, text, drawable, -1, onClick);
    }

    public ActionButton(@NonNull Context context, String text, int drawable, int priority, View.OnClickListener onClick){
        this(context, null, text, drawable, priority, onClick);
    }

    public ActionButton(@NonNull Context context, @Nullable AttributeSet attrs, String text, int drawable, int priority, View.OnClickListener onClick) {
        super(context, attrs);

        this.priority = priority;
        setOrientation(VERTICAL);
        setPivotY(0);
        setClickable(true);
        setFocusable(true);

        Drawable bgDrawable;
        int corner = AndroidUtilities.dp(16);
        int purple = Color.parseColor("#551a00ab");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // base shape
            GradientDrawable shape = new GradientDrawable();
            shape.setColor(purple);
            shape.setCornerRadius(corner);
            GradientDrawable mask = new GradientDrawable();
            mask.setCornerRadius(corner);
            mask.setColor(0x44FFFFFF);
            bgDrawable = new RippleDrawable(
                    ColorStateList.valueOf(0x44FFFFFF),
                    shape,
                    mask
            );
        } else {
            // normal shape
            GradientDrawable normal = new GradientDrawable();
            normal.setColor(purple);
            normal.setCornerRadius(corner);
            GradientDrawable overlay = new GradientDrawable();
            overlay.setColor(Color.WHITE);
            overlay.setCornerRadius(corner);
            overlay.setAlpha(0x33);
            LayerDrawable pressedLayer = new LayerDrawable(new Drawable[]{ normal, overlay });
            // state list: pressed → overlayed, default → normal
            StateListDrawable states = new StateListDrawable();
            states.addState(new int[]{ android.R.attr.state_pressed }, pressedLayer);
            states.addState(new int[0], normal);
            bgDrawable = states;
        }

        setBackground(bgDrawable);

        FrameLayout iconContainer = new FrameLayout(context);

        iconView = new ImageView(context);
        iconView.setAdjustViewBounds(true);
        iconView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        iconView.setImageResource(drawable);
        LayoutParams iconLp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1f
        );
        iconLp.gravity = Gravity.CENTER_HORIZONTAL;
        iconContainer.setPadding(AndroidUtilities.dp(8), AndroidUtilities.dp(8), AndroidUtilities.dp(8), AndroidUtilities.dp(8));
        iconContainer.addView(iconView);
        addView(iconContainer, iconLp);

        labelView = new TextView(context);
        labelView.setText(text);
        labelView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        labelView.setTextColor(Color.WHITE);
        labelView.setGravity(Gravity.CENTER);
        LayoutParams labelLp = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT,
                0f
        );
        labelLp.gravity = Gravity.CENTER_HORIZONTAL;
        labelLp.bottomMargin = AndroidUtilities.dp(8);
        addView(labelView, labelLp);

        setOnClickListener(onClick);
    }

    public int getPriority(){
        return priority;
    }

    private Drawable getPressDrawable(Context ctx) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // real ripple on Lollipop+
            TypedValue tv = new TypedValue();
            ctx.getTheme().resolveAttribute(
                    android.R.attr.selectableItemBackgroundBorderless,
                    tv, true);
            return ContextCompat.getDrawable(ctx, tv.resourceId);
        } else {
            // simple white‐tint overlay on press for pre-21
            StateListDrawable sd = new StateListDrawable();
            sd.addState(
                    new int[]{android.R.attr.state_pressed},
                    new ColorDrawable(0x33FFFFFF)
            );

            sd.addState(
                    new int[0],
                    new ColorDrawable(Color.TRANSPARENT)
            );
            return sd;
        }
    }

    public void setIconResource(@DrawableRes int resId) {
        iconView.setImageResource(resId);
    }

    public void setLabel(@NonNull String text) {
        labelView.setText(text);
    }


    public void setButtonAlpha(float alpha){
        if (alpha <= 0){
            setVisibility(GONE);
        }
        else{
            setVisibility(VISIBLE);
        }

        labelView.setAlpha(alpha);
        iconView.setAlpha(alpha);
        setAlpha(alpha);
        invalidate();
    }

}
