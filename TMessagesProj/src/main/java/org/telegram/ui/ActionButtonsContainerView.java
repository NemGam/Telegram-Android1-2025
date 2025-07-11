package org.telegram.ui;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.telegram.messenger.AndroidUtilities;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ActionButtonsContainerView extends LinearLayout {

    List<ActionButton> currentButtons;

    private final Comparator<ActionButton> comparator = (a, b) -> Integer.compare(b.getPriority(), a.getPriority());

    public ActionButtonsContainerView(Context context) {
        super(context);
        currentButtons = new ArrayList<>();
    }

    public void setButtonsAlpha(float alpha){
        System.out.println("1:" + alpha);
        for (int i = 0; i < currentButtons.size(); i++){
            System.out.println("1:" + alpha);
            currentButtons.get(i).setButtonAlpha(alpha);
        }
    }

    public void SetButtons(List<ActionButton> buttons){
        removeAllViews();
        currentButtons = buttons;
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.MATCH_PARENT, 1f
        );

        currentButtons.sort(comparator);

        for (int i = 0; i < buttons.size(); i++){
            lp.setMarginEnd(AndroidUtilities.dp(3));
            lp.setMarginStart(AndroidUtilities.dp(3));
            lp.gravity = Gravity.CENTER_HORIZONTAL;
            addView(buttons.get(i), lp);
        }
        requestLayout();
    }
}
