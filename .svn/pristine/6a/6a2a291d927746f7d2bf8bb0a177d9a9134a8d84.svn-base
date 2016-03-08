package com.shanghai.haojiajiao.weight;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;
import android.widget.ScrollView;

/**
 * 自定义listview，解决ScrollView中嵌套ListView显示不正常的问题（1行半）
 *
 * @author zjy
 * @version 2015-12-03
 */
public class ListViewForScrollView extends ListView {

    public ListViewForScrollView(Context context) {
        super(context);
    }

    public ListViewForScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListViewForScrollView(Context context, AttributeSet attrs,
                                 int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    /**

     * 重写该方法，达到使ListView适应ScrollView的效果

     */
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch
                (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(false);//当手指触到listview的时候，让父ScrollView交出ontouch权限，也就是让父scrollview
            case MotionEvent.ACTION_MOVE:
                break;

            case MotionEvent.ACTION_UP:

            case MotionEvent.ACTION_CANCEL:
                getParent().requestDisallowInterceptTouchEvent(true);//当手指松开时，让父ScrollView重新拿到onTouch权限
                break;
            default:
                break;

        }
        return super.onInterceptTouchEvent(ev);

    }
}
